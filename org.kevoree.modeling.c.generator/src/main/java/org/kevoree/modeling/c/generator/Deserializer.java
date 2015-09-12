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

    // FIXME temp method, should it fact try to determine the actual type
    for (Classifier c : Generator.classifiers.values())
      if (c.isAbstract())
	ret += "void* new_" + c.getName() + "()\n{}\n\n";

    ret += "\n";
    ret += "const fptrConstruct construct[NB_CLASSES] = {\n";
    for (Classifier c : Generator.classifiers.values()) // see for abstract classes
      ret += "\t[" + c.getName().toUpperCase() + "_TYPE] = new_" + c.getName() + ",\n";
    ret += "};\n";

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
	  if (!c.isAbstract()) {
	    ret += "\tvoid* dest = ((" + c.getName() + "*)o)->" + v.getName() + ";\n";
	    ret += "\tif (strlen(param) < 9)\n";
	    ret += "\t\tstrcpy(dest, param);\n";
	  }
	} else if (v.getType().equals("char*")) {
	  ret += "\tchar* param = parseStr(state);\n";
	  if (!c.isAbstract()) {
	    //                        ret += "((" + c.getName() + "*)o)->" + v.getName() + " = malloc(strlen(param) * sizeof(char) +1);\n";
	    ret += "((" + c.getName() + "*)o)->" + v.getName() + " = param;\n";
	    //                        ret += "printf(\"setted value is %s\\n\", ((" + c.getName() + "*)o)->" + v.getName() + ");\n";
	  }
	} else if (v.getLinkType() == Variable.LinkType.UNARY_LINK) {
	  if (v.isContained()) {
	    ret += "char type;\n";
	    ret += "while((type = jsonparse_next(state)) != '{') {}\n";
	    ret += v.getType() + "* ptr = construct[ptr_type]();\n";
	    if (!c.isAbstract()) {
	      ret += "((" + c.getName() + "*)o)->" + v.getName() + " = ptr;\n";
	      ret += "parseObject(state, ptr, ptr_type, ptr_type);\n";
	    }
	  } else {
	    ret += "ref* r = malloc(sizeof(ref) + 1);\n";
	    ret += "char* str = parseStr(state);\n";
	    ret += "printf(\"storing: %s\\n\", str);\n";
	    if (!c.isAbstract()) {
	      ret += "r->id = malloc(strlen(str) * sizeof(char) + 1);\n";
	      ret += "strcpy(r->id, str);\n";
	      ret += "hashmap_put(ref_map, r->id, r);\n";
	      ret += "r->whereToWrite = &((" + c.getName() + "*)o)->" + v.getName() + ";\n";
	    }
	  }
	} else if (v.getLinkType() == Variable.LinkType.MULTIPLE_LINK) {
	  String fun = "";
	  //TODO look into _all_ functions
	  for (Function f : c.getFunctions())
	    if (f.getSignature().contains("Add" + HelperGenerator.upperCaseFirstChar(v.getName())))
	      fun = f.getSignature();
	  System.out.println("Found function is " + fun);
	  ret += 
	    "char type = JSON_TYPE_ARRAY;\n" +
	    "while((type = jsonparse_next(state)) != ']')\n" +
	    "{\n" +
	    "if (type == JSON_TYPE_OBJECT)\n" +
	    "{\n" +
	    "void* ptr = construct[ptr_type]();\n" +
	    "parseObject(state, ptr, ptr_type, ptr_type);\n" +

	    //TODO look into the Classifier for the correct name of the function because it may be inherited
	    "((" + c.getName() + "*)o)->VT->" + fun + "(o, ptr);\n" +
	    "}\n" +
	    "}\n";
	  ret += "}\n\n";
	}
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


    ret += TemplateManager.getInstance().getJsondeserial_source();

    FileManager.writeFile(context.getGenerationDirectory().getAbsolutePath() + File.separator +
	"jsondeserializer.c", ret, false);
  }
}
