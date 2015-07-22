package org.kevoree.modeling.c.generator;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.impl.EClassImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.kevoree.modeling.c.generator.model.*;
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
    private String folderName = "kevoree";
    private File ecoreFile;
    private List<Classifier> classifiers;

    public Generator(GenerationContext ctx) {
        this.context = ctx;
        this.context.setName_package(folderName);
        this.ecoreFile = ctx.getEcore();
        this.classifiers = new ArrayList<Classifier>();
    }

    public void generateModel() throws Exception {
        URI fileUri = URI.createFileURI(ecoreFile.getAbsolutePath());
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ecore", new EcoreResourceFactoryImpl());
        ResourceSetImpl rs = new ResourceSetImpl();
        XMIResource resource = (XMIResource) rs.createResource(fileUri);

        CheckerConstraint checkerConstraint = new CheckerConstraint(context);
        checkerConstraint.verify(resource);

        resource.load(null);
        EcoreUtil.resolveAll(resource);

        for (Iterator i = resource.getAllContents(); i.hasNext(); ) {
            EObject eo = (EObject) i.next();

            if (eo instanceof EClass) {
                Classifier c = Classifier.createFromEClass((EClass) eo);
                this.classifiers.add(c);
            }
        }

        for (Classifier c : this.classifiers) {
            Serializer.writeHeader(c, context);
            Serializer.writeSource(c, context);
        }
    }


}
