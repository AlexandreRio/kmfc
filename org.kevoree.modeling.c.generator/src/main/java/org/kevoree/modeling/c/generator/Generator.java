package org.kevoree.modeling.c.generator;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.kevoree.modeling.c.generator.model.Classifier;
import org.kevoree.modeling.c.generator.model.Serializer;
import org.kevoree.modeling.c.generator.utils.CheckerConstraint;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 28/10/13
 * Time: 11:46
 * To change this templates use File | Settings | File Templates.
 */
public class Generator {

    public static Map<String, Classifier> classifiers;
    private GenerationContext context;
    private String folderName = "kevoree";
    private File ecoreFile;

    public Generator(GenerationContext ctx) {
        this.context = ctx;
        this.context.setName_package(folderName);
        this.ecoreFile = ctx.getEcore();
        classifiers = new HashMap<String, Classifier>();
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
                classifiers.put(((EClass) eo).getName(), c);
            }
        }

        for (Classifier c : classifiers.values()) {
            Serializer.writeHeader(c, context);
            Serializer.writeSource(c, context);
        }
    }


}
