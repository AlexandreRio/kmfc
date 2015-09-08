package org.kevoree.modeling.c.generator;

import org.kevoree.modeling.c.generator.model.Classifier;
import org.kevoree.modeling.c.generator.model.Variable;
import org.kevoree.modeling.c.generator.utils.FileManager;
import org.kevoree.modeling.c.generator.utils.HelperGenerator;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Deserializer {

    public static void generateDeserializer(GenerationContext context) throws IOException {
        generateHeader(context);
        generateSource(context);
    }


    private static void generateHeader(GenerationContext context) throws IOException {
        String ret = "";
        ret += HelperGenerator.genIFDEF("jsondeserializer");
        ret += "\n";
        ret += HelperGenerator.genIncludeLocal("kevoree");
        ret += HelperGenerator.genIncludeLocal("json");
        ret += HelperGenerator.genIncludeLocal("jsonparse");
        ret += "\n";
        ret += HelperGenerator.genInclude("stdlib.h");
        ret += "\n";

        int nbClass = Generator.classifiers.size();
        ret += "#define NB_CLASSES " + nbClass + "\n\n";

        for (Classifier c : Generator.classifiers.values()) {
            int nbVar = c.getVariables().size() + 1; // plus eClass attribute
            for (String s : c.getAllSuperClass())
                if (!s.equals("KMFContainer"))
                    nbVar += Generator.classifiers.get(s).getVariables().size();
            ret += "#define " + c.getName() + "_NB_ATTR " + nbVar + "\n";
        }

        ret += "\n";
        ret += "typedef enum TYPE {\n";
        int i = 0;
        for (String s : Generator.classifiers.keySet()) {
            ret += "\t" + s.toUpperCase() + "_TYPE = " + i + ",\n";
            i++;
        }
        ret += "\tPRIMITIVE_TYPE\n";
        ret += "} TYPE;\n\n";

        ret += TemplateManager.getInstance().getJsondeserial_header();

        ret += HelperGenerator.genENDIF();
        FileManager.writeFile(context.getGenerationDirectory().getAbsolutePath() + File.separator +
                "jsondeserializer.h", ret, false);
    }

    private static void generateSource(GenerationContext context) throws IOException {
        String ret = "";

        ret += HelperGenerator.genIncludeLocal("jsondeserializer");

        ret += "\nchar attr[200];\n" +
                "void ContainerRootSetKMF_ID(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type);\n" +
                "void doNothing(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type);\n";

        // generate setter functions
        for (Classifier c : Generator.classifiers.values()) {
            List<Variable> allVars = new LinkedList<Variable>(c.getVariables());
            for (String parent : c.getAllSuperClass())
                if (!parent.equals("KMFContainer"))
                    allVars.addAll(Generator.classifiers.get(parent).getVariables());

            for (Variable v : allVars) {
                ret += "void " + c.getName() + "Set" + v.getName() +
                        "(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)\n";
                ret += "{\n";
                if (v.getType().equals("char")) {
                    ret += "\tchar* param = parseStr(state);\n";
                    ret += "\tvoid* dest = ((" + c.getName() + "*)o)->" + v.getName() + ";\n";
                    ret += "\tif (strlen(param) < 9)\n";
                    ret += "\t\tstrcpy(dest, param);\n";
                } else if (v.getType().equals("char*")) {
                    ret += "\tchar* param = parseStr(state);\n";
                    ret += "((" + c.getName() + "*)o)->" + v.getName() + " = param;\n";
                } else if (v.getLinkType() == Variable.LinkType.UNARY_LINK) {
                    System.out.println("In " + c.getName() + " unaryLink of type " + v.getType());
                    ret += "printf(\"storing ref: %s\\n\", parseStr(state));\n";
                    ret += "printf(\"var: %p\\n\", &((" + c.getName() + "*)o)->" + v.getName() + ");\n";
                    //ret +=
                    //store the reference
                }
                ret += "}\n\n";
            }
        }

        for (Classifier c : Generator.classifiers.values()) {
            List<Variable> allVars = new LinkedList<Variable>(c.getVariables());
            ret += "const struct at " + c.getName() + "_Attr[" + c.getName() + "_NB_ATTR] = {\n";
            for (String parent : c.getAllSuperClass())
                if (!parent.equals("KMFContainer"))
                    allVars.addAll(Generator.classifiers.get(parent).getVariables());

            ret += "{\"eClass\", doNothing, PRIMITIVE_TYPE, PRIMITIVE_TYPE},\n";
            for (Variable v : allVars) {
                String parser = c.getName() + "Set" + v.getName();
                String type = "";
                if (v.getLinkType() == Variable.LinkType.MULTIPLE_LINK) {
                    parser = "parseArray";
                    type = v.getType().toUpperCase() + "_TYPE";
                } else if (v.getLinkType() == Variable.LinkType.UNARY_LINK) {
                    //parser = "parseRef";
                    type = v.getType().toUpperCase() + "_TYPE";
                } else if (v.getLinkType() == Variable.LinkType.PRIMITIVE) {
                    //if (v.getType().contains("char"))
                    //    parser = "parseStr";
                    //else
                    //    parser = "parseBool";
                    type = "PRIMITIVE_TYPE";
                }

                ret += "{\"" + v.getName() + "\", " + parser + ", " + c.getName().toUpperCase() + "_TYPE, " + type + "},\n";
            }
            ret += "};\n\n";
        }

        ret += "const struct ClassType Classes[NB_CLASSES] = {\n";
        for (Classifier c : Generator.classifiers.values()) {
            ret += "\t{\n";
            ret += "\t\t.type = " + c.getName().toUpperCase() + "_TYPE,\n";
            ret += "\t\t.attributes = &" + c.getName() + "_Attr,\n";
            ret += "\t\t.nb_attributes = " + c.getName() + "_NB_ATTR,\n";
            ret += "\t},\n";
        }
        ret += "};\n";
        ret += "\n";

        // FIXME temp method, should it fact try to determine the actual type
        for (Classifier c : Generator.classifiers.values())
            if (c.isAbstract())
                ret += "void* new_" + c.getName() + "()\n{}\n\n";

        ret += "\n";
        ret += "const fptrConstruct construct[NB_CLASSES] = {\n";
        for (Classifier c : Generator.classifiers.values()) // see for abstract classes
            ret += "\t[" + c.getName().toUpperCase() + "_TYPE] = new_" + c.getName() + ",\n";
        ret += "};\n";

        ret += TemplateManager.getInstance().getJsondeserial_source();

        FileManager.writeFile(context.getGenerationDirectory().getAbsolutePath() + File.separator +
                "jsondeserializer.c", ret, false);
    }
}
