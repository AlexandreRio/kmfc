package org.kevoree.modeling.c.generator;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.impl.EClassImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.kevoree.modeling.c.generator.model.ClassGenerator;
import org.kevoree.modeling.c.generator.model.Classifier;
import org.kevoree.modeling.c.generator.model.FactoryGenerator;
import org.kevoree.modeling.c.generator.model.Variable;
import org.kevoree.modeling.c.generator.utils.CheckerConstraint;
import org.kevoree.modeling.c.generator.utils.ConverterDataTypes;
import org.kevoree.modeling.c.generator.utils.FileManager;
import org.kevoree.modeling.c.generator.utils.HelperGenerator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 28/10/13
 * Time: 11:46
 * To change this templates use File | Settings | File Templates.
 */
public class Generator {

    private GenerationContext context;
    private File ecoreFile;
    private StringBuilder classes = new StringBuilder();
    private List<ClassGenerator> generators;

    public Generator(GenerationContext ctx) {
        this.context = ctx;
        this.ecoreFile = ctx.getEcore();
        this.generators = new ArrayList<ClassGenerator>();
    }

    public void generateModel() throws Exception {
        URI fileUri = URI.createFileURI(ecoreFile.getAbsolutePath());
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ecore", new EcoreResourceFactoryImpl());
        ResourceSetImpl rs = new ResourceSetImpl();
        XMIResource resource = (XMIResource) rs.createResource(fileUri);


        CheckerConstraint checkerConstraint = new CheckerConstraint(context);
        checkerConstraint.verify(resource);

        ClassGenerator classGenerator;

        resource.load(null);
        EcoreUtil.resolveAll(resource);

        for (Iterator i = resource.getAllContents(); i.hasNext(); ) {
            classGenerator = new ClassGenerator(context);
            EObject eo = (EObject) i.next();

            if (eo instanceof EClass) {
                Classifier c = initClassifier((EClass) eo);
//                classGenerator.generateClass((EClass) eo);
//                factoryGenerator.generateFactory((EClass) eo);

                classes.append(HelperGenerator.genIncludeLocal(((EClass) eo).getName()));
                this.generators.add(classGenerator);
            }
        }

        String output = context.getRootGenerationDirectory() + File.separatorChar +
                context.getName_package() + File.separatorChar;

        for (ClassGenerator gen : this.generators) {
            gen.link_generation();
            gen.writeHeader();
            gen.writeClass();
        }
    }

    private Classifier initClassifier(EClass cls) {
        Classifier c = new Classifier(cls.getName(), cls.isAbstract());
        System.out.println("Class " + cls.getName());

        for (EAttribute attr : cls.getEAttributes()) {
            String t = ConverterDataTypes.getInstance().check_type(attr.getEAttributeType().getName());
            c.addVariable(new Variable(attr.getName(), t, Variable.LinkType.PRIMITIVE, false));
        }

        for (EReference ref : cls.getEReferences()) {
            int bound = ref.getUpperBound();
            Variable.LinkType lt = Variable.LinkType.UNARY_LINK;
            if (bound == -1)
                lt = Variable.LinkType.MULTIPLE_LINK;

            String t = ConverterDataTypes.getInstance().check_type(ref.getEReferenceType().getName());
            c.addVariable(new Variable(ref.getName(), t, lt, ref.isContainment()));
        }
        return c;
    }

}
