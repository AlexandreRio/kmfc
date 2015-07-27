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

public class Generator {

    public static Map<String, Classifier> classifiers;
    private GenerationContext context;
    private File eCoreFile;

    public Generator(GenerationContext ctx) {
        this.context = ctx;
        this.eCoreFile = ctx.getECore();
        classifiers = new HashMap<String, Classifier>();
    }

    public void generateModel() throws Exception {
        URI fileUri = URI.createFileURI(eCoreFile.getAbsolutePath());
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


    public void generateEnvironmement() {
    }
}
