package org.kevoree.modeling.c.generator;

import org.kevoree.modeling.c.generator.model.Classifier;
import org.kevoree.modeling.c.generator.model.Variable;
import org.kevoree.modeling.c.generator.model.Function;
import org.kevoree.modeling.c.generator.utils.FileManager;
import org.kevoree.modeling.c.generator.utils.HelperGenerator;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

//TODO should be renamed to JSONDeserializer

/**
 * This implementation instantiate object based on what type they should be regarding the model,
 * it does look for the eClass type only for polymorphic classes, i.e abstract attributes.
 */
public class Deserializer {

    //TODO this should be moved or at least reused in all the project
    // or this can even be useless and should be a local variable
    private static List<String> abstractClassifiers;
    private static List<String> concreteClassifiers;

    //TODO this should definitively not be here but eh ¯\_(ツ)_/¯

    /**
     *
     */
    private static void constructAbstractHierarchy() {
        abstractClassifiers = new ArrayList<String>();
        concreteClassifiers = new ArrayList<String>();

        for (Classifier cl : Generator.classifiers.values())
            if (cl.isAbstract())
                abstractClassifiers.add(cl.getName());

        for (Classifier cl : Generator.classifiers.values()) {
            for (String s : cl.getAllSuperClass()) {
                if (abstractClassifiers.contains(s))
                    if (!concreteClassifiers.contains(cl.getName())) {
                        concreteClassifiers.add(cl.getName());
                    }
            }
        }

    }

    public static void generateDeserializer(GenerationContext context) throws IOException {
        constructAbstractHierarchy();
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

        ret += generateDeclaration();

        // FIXME temp method, should in fact try to determine the actual type
        for (Classifier c : Generator.classifiers.values())
            if (c.isAbstract())
                ret += "void* new_" + c.getName() + "()\n{}\n\n";

        ret += "\n";
        ret += generateLookAheadFunction();
        ret += generateConstructorTable();
        ret += generateSetteFunctions();
        ret += generateClassifierMaps();
        ret += generateClassDescriptors();
        ret += "\n";

        ret += TemplateManager.getInstance().getJsondeserial_source();

        FileManager.writeFile(context.getGenerationDirectory().getAbsolutePath() + File.separator +
                "jsondeserializer.c", ret, false);
    }

    private static String generateDeclaration() {
        String ret = "\nchar attr[200];\n" +
                "map_t ref_map;\n" +
                "\n" +
                "typedef struct _ref {\n" + // TODO add another id because we may have duplicate object ID
                "  char* id;\n" +
                "  void** whereToWrite;\n" +
                "} ref;\n" +
                "\n" +
                "\n" +
                "char* getIdFromRef(any_t ref_t)\n" +
                "{\n" +
                "  ref *refS = (ref*) ref_t;\n" +
                "  return refS->id;\n" +
                "}\n\n" +
                "void ContainerRootSetKMF_ID(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type);\n" +
                "void doNothing(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type);\n";

        return ret;
    }

    //TODO produce pure abstact or full abstract table based on compiler option
    private static String generateLookAheadFunction() {
        String fun = "\n" +
                "char* readNexteClass(struct jsonparse_state* state)\n" +
                "{\n" +
                "  char type = JSON_TYPE_ARRAY;\n" +
                "  while ((type = jsonparse_next(state)) != JSON_TYPE_PAIR_NAME)\n" +
                "  { }\n" +
                "  jsonparse_copy_value(state, attr, sizeof attr);\n" +
                "  return parseStr(state);\n" +
                "}\n" +
                "\n" +
                "void* lookAheadNexteClass(struct jsonparse_state* state)\n" +
                "{\n" +
                "  struct jsonparse_state copyState = *state;\n" +
                "  char* eClass = readNexteClass(&copyState);\n" +
                "  void* (*constructorFptr)();\n" +
                "  if (strcmp(\"org.kevoree.NodeType\", eClass) == 0)\n" + // TODO should be generated
                "    constructorFptr = new_NodeType;\n" +
                "  else if (strcmp(\"org.kevoree.GroupType\", eClass) == 0)\n" +
                "    constructorFptr = new_GroupType;\n" +
                "  else if (strcmp(\"org.kevoree.ComponentType\", eClass) == 0)\n" +
                "    constructorFptr = new_ComponentType;\n" +
                "\n" +
                "  return constructorFptr();\n" +
                "}\n\n";
        return fun;
        //String ret = "void* lookAheadNexteClass(struct jsonparse_state* state)\n";
        //ret += "{\n" +  // TODO need to copy the state, get the next eClass str,
        //        // loop over the Java list concreteClass and compare
        //        // and finally return the associated constructor fptr
        //        "return NULL;\n" +
        //        "}\n";
        //return ret;
    }

    private static String generateConstructorTable() {
        String ret = "const fptrConstruct construct[NB_CLASSES] = {\n";
        for (Classifier c : Generator.classifiers.values()) // see for abstract classes
            ret += "\t[" + c.getName().toUpperCase() + "_TYPE] = new_" + c.getName() + ",\n";
        ret += "};\n";
        return ret;
    }

    private static String generateSetteFunctions() {
        String ret = "";
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
                    if (v.isContained()) {
                        ret += "char type;\n";
                        ret += "while((type = jsonparse_next(state)) != '{') {}\n";
                        ret += v.getType() + "* ptr = construct[ptr_type]();\n";
                        ret += "((" + c.getName() + "*)o)->" + v.getName() + " = ptr;\n";
                        ret += "parseObject(state, ptr, ptr_type, ptr_type);\n";
                    } else {
                        ret += "ref* r = malloc(sizeof(ref) + 1);\n";
                        ret += "char* str = parseStr(state);\n";
                        ret += "printf(\"storing: %s\\n\", str);\n";
                        ret += "r->id = malloc(strlen(str) * sizeof(char) + 1);\n";
                        ret += "strcpy(r->id, str);\n";
                        ret += "hashmap_put(ref_map, r->id, r);\n";
                        ret += "r->whereToWrite = &((" + c.getName() + "*)o)->" + v.getName() + ";\n";
                    }
                } else if (v.getLinkType() == Variable.LinkType.MULTIPLE_LINK) {
                    String fun = "";

                    // Determine the signature of the function to call on the object to add a new
                    // element to the map
                    ArrayList<Function> allFunctions = new ArrayList<Function>(c.getFunctions());
                    for (String cl : c.getAllSuperClass())
                        if (!cl.equals("KMFContainer"))
                            allFunctions.addAll(Generator.classifiers.get(cl).getFunctions());
                    for (Function f : allFunctions)
                        if (f.getSignature().contains("Add" + HelperGenerator.upperCaseFirstChar(v.getName())))
                            fun = HelperGenerator.lowerCaseFirstChar(f.getSignature());

                    ret += "char type = JSON_TYPE_ARRAY;\n" +
                            "while((type = jsonparse_next(state)) != ']')\n" +
                            "{\n" +
                            "if (type == JSON_TYPE_OBJECT)\n" +
                            "{\n";

                    // Determine the constructor to call in order to create the new object
                    if (abstractClassifiers.contains(v.getType()))
                        ret += "void* ptr = lookAheadNexteClass(state);\n";
                    else
                        ret += "void* ptr = construct[ptr_type]();\n";

                    ret += "parseObject(state, ptr, ptr_type, ptr_type);\n" +
                            "if (ptr != NULL)\n" +
                            "\t((" + c.getName() + "*)o)->VT->" + fun + "(o, ptr);\n" +
                            "}\n" +
                            "}\n";
                }
                ret += "}\n\n";
            }
        }
        return ret;
    }

    private static String generateClassifierMaps() {
        String ret = "";
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
                    //parser = "parseArray";
                    type = v.getType().toUpperCase() + "_TYPE";
                } else if (v.getLinkType() == Variable.LinkType.UNARY_LINK) {
                    type = v.getType().toUpperCase() + "_TYPE";
                } else if (v.getLinkType() == Variable.LinkType.PRIMITIVE) {
                    type = "PRIMITIVE_TYPE";
                }

                ret += "{\"" + v.getName() + "\", " + parser + ", " + c.getName().toUpperCase() + "_TYPE, " + type + "},\n";
            }
            ret += "};\n\n";
        }
        return ret;
    }

    private static String generateClassDescriptors() {
        String ret = "const struct ClassType Classes[NB_CLASSES] = {\n";
        for (Classifier c : Generator.classifiers.values()) {
            ret += "\t{\n";
            ret += "\t\t.type = " + c.getName().toUpperCase() + "_TYPE,\n";
            ret += "\t\t.attributes = &" + c.getName() + "_Attr,\n";
            ret += "\t\t.nb_attributes = " + c.getName() + "_NB_ATTR,\n";
            ret += "\t},\n";
        }
        ret += "};\n";
        return ret;
    }

}
