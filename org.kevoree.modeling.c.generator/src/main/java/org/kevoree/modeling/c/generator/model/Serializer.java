package org.kevoree.modeling.c.generator.model;

import org.apache.velocity.VelocityContext;
import org.kevoree.modeling.c.generator.GenerationContext;
import org.kevoree.modeling.c.generator.TemplateManager;
import org.kevoree.modeling.c.generator.utils.FileManager;
import org.kevoree.modeling.c.generator.utils.HelperGenerator;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;

public abstract class Serializer {

    private static String generateHeaderIncludes(Classifier cls) {
        String ret = "";
        ret += HelperGenerator.genInclude("string.h");
        ret += HelperGenerator.genInclude("stdio.h");
        ret += "\n";

        ret += HelperGenerator.genTypeDef(cls.getName());
        for (String s : Classifier.getLinkedClassifier(cls))
            ret += HelperGenerator.genTypeDef(s);
        ret += "\n";
        return ret;
    }

    private static String generateFunctionSignatures(Classifier cls) {
        String ret = "";
        for (Function f : cls.getFunctions())
            if (f.isPublic()) {
                ret += f.getSignature() + "(";
                Iterator<Parameter> iv = f.getParameters().iterator();
                if (iv.hasNext())
                    ret += iv.next().getType();
                while (iv.hasNext())
                    ret += ", " + iv.next();
                ret += ");\n";
            }
        return ret;
    }

    private static String generateVT(Classifier cls) {
        String ret = "";
        ret += "typedef struct _VT_" + cls.getName() + " {\n";
        ret += "\tVT_" + cls.getSuperClass() + " *super;\n";
        for (String sClass : cls.getAllSuperClass()) {
            if (sClass.equals("KMFContainer")) {
                VelocityContext context = new VelocityContext();
                StringWriter result = new StringWriter();
                TemplateManager.getInstance().getTp_KMFContainer_fptr().merge(context, result);
                ret += result.toString();
            } else {

            }
        }
        ret += "} VT_" + cls.getName() + ";\n\n";
        return ret;
    }

    private static String generateHeaderFile(Classifier cls) {
        String ret = "";
        ret += HelperGenerator.genIFDEF(cls.getName());
        ret += "\n";
        ret += generateHeaderIncludes(cls);
        ret += generateFunctionSignatures(cls);
        ret += generateVT(cls);
        ret += "\n";
//        ret += generateAttributes(cls);
        ret += "\n";
        ret += HelperGenerator.genENDIF();
        return ret;
    }

    private static String generateSourceFile(Classifier cls) {
        String ret = "";
        return ret;
    }

    public static void writeHeader(Classifier cls, GenerationContext ctx) throws IOException {
        String header = generateHeaderFile(cls);
        FileManager.writeFile(ctx.getPackageGenerationDirectory() +
                cls.getName() + ".h", header, false);
    }

    public static void writeSource(Classifier cls, GenerationContext ctx) throws IOException {
        String source = generateSourceFile(cls);
        FileManager.writeFile(ctx.getPackageGenerationDirectory() +
                cls.getName() + ".c", source, false);
    }
}
