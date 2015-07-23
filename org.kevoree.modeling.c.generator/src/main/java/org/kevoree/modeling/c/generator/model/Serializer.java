package org.kevoree.modeling.c.generator.model;

import org.apache.velocity.VelocityContext;
import org.kevoree.modeling.c.generator.GenerationContext;
import org.kevoree.modeling.c.generator.Generator;
import org.kevoree.modeling.c.generator.TemplateManager;
import org.kevoree.modeling.c.generator.model.Function.Visibility;
import org.kevoree.modeling.c.generator.utils.FileManager;
import org.kevoree.modeling.c.generator.utils.HelperGenerator;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;

import static org.kevoree.modeling.c.generator.utils.HelperGenerator.genToLowerCaseFirstChar;

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
            if (f.getVisibilityType() == Visibility.IN_HEADER || f.getVisibilityType() == Visibility.IN_VT) {
                ret += f.getReturnType() + " " + f.getSignature() + "(";
                Iterator<Parameter> iv = f.getParameters().iterator();
                if (iv.hasNext())
                    ret += iv.next().getType();
                else
                    ret += "void";
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
                ret += "\t/*" + sClass + "*/\n";
                for (Function f : Generator.classifiers.get(sClass).getFunctions()) {
                    if (f.getVisibilityType() == Visibility.IN_VT)
                        ret += "\tftpr" + f.getSignature() + " " +
                                genToLowerCaseFirstChar(f.getSignature()) + ";\n";
                }
            }
        }
        ret += "} VT_" + cls.getName() + ";\n\n";
        return ret;
    }

    private static String generateAttributesFromClassifier(Classifier cls) {
        String ret = "";
        for (Variable v : cls.getVariables()) {
            if (v.getLinkType() == Variable.LinkType.UNARY_LINK)
                ret += "\t" + v.getType() + " " + v.getName() + ";\n";
            else if (v.getLinkType() == Variable.LinkType.MULTIPLE_LINK)
                ret += "\tmap_t " + v.getName() + ";\n";
            else
                ret += "\t" + v.getType() + " " + v.getName() + ";\n";
        }
        return ret;
    }

    private static String generateAttributes(Classifier cls) {
        String ret = "typedef struct _VT_" + cls.getName() + " {\n";
        ret += "\tVT_" + cls.getName() + " *VT;\n";
        for (String sClass : cls.getAllSuperClass())
            if (sClass.equals("KMFContainer")) {
                ret += "\t/* KMFContainer */\n" +
                        "\tKMFContainer *eContainer;\n";
            } else {
                ret += "\t/* " + sClass + " */\n";
                ret += generateAttributesFromClassifier(Generator.classifiers.get(sClass));
            }
        ret += "\t/* " + cls.getName() + " */\n";
        ret += generateAttributesFromClassifier(cls);
        ret += "} VT_" + cls.getName() + ";\n\n";
        return ret;
    }

    private static String generateHeaderFile(Classifier cls) {
        String ret = "";
        ret += HelperGenerator.genIFDEF(cls.getName());
        ret += "\n";
        ret += generateHeaderIncludes(cls);
        ret += generateFunctionSignatures(cls);
        ret += "\n";
        ret += generateVT(cls);
        ret += "\n";
        ret += generateAttributes(cls);
        ret += "\n";
        ret += HelperGenerator.genENDIF();
        return ret;
    }

    private static String generateBodyIncludes(Classifier cls) {
        String ret = "";
        for (String s : Classifier.getLinkedClassifier(cls))
            ret += HelperGenerator.genIncludeLocal(s);
        return ret;
    }

    private static String generateDebug() {
        VelocityContext context = new VelocityContext();
        StringWriter result = new StringWriter();
        TemplateManager.getInstance().getTp_print_debug().merge(context, result);
        return result.toString();
    }

    private static String generateSourceFile(Classifier cls) {
        String ret = "";
        ret += HelperGenerator.genIncludeLocal(cls.getName());
        ret += "\n";
        ret += generateBodyIncludes(cls);
        ret += generateDebug();
        ret += "\n";
        for (Function f : cls.getFunctions()) {
            ret += f.getReturnType() + "\n";
            ret += f.getSignature() + "(";
            Iterator<Parameter> iv = f.getParameters().iterator();
            if (iv.hasNext()) {
                Parameter p = iv.next();
                ret += p.getType() + " " + p.getName();
            } else {
                ret += "void";
            }
            while (iv.hasNext()) {
                Parameter p = iv.next();
                ret += ", " + p.getType() + " " + p.getName();
            }
            ret += ")\n{\n";
            ret += f.getBody();
            ret += "}\n\n";
        }
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
