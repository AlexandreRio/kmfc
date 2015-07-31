package org.kevoree.modeling.c.generator.model;

import org.apache.velocity.VelocityContext;
import org.kevoree.modeling.c.generator.GenerationContext;
import org.kevoree.modeling.c.generator.Generator;
import org.kevoree.modeling.c.generator.TemplateManager;
import org.kevoree.modeling.c.generator.utils.FileManager;
import org.kevoree.modeling.c.generator.utils.HelperGenerator;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.kevoree.modeling.c.generator.utils.HelperGenerator.genToLowerCaseFirstChar;
import static org.kevoree.modeling.c.generator.utils.HelperGenerator.genToUpperCaseFirstChar;

/**
 * Serialize all the tests of a Classifier
 */
public class TestSerializer {

    //TODO refactor, remove useless include
    private static String generateHeaderFile(Classifier cls) {
        String header = "";
        header += HelperGenerator.genIFDEF(cls.getName() + "_Test");
        VelocityContext context = new VelocityContext();
        StringWriter result = new StringWriter();
        context.put("name", cls.getName());
        List<String> allLinkedClasses = Classifier.getLinkedClassifier(cls);
        for (String s : cls.getAllSuperClass())
            if (!s.equals("KMFContainer"))
                for (String ls : Classifier.getLinkedClassifier(Generator.classifiers.get(s)))
                    if (!allLinkedClasses.contains(ls))
                        allLinkedClasses.add(ls);
        context.put("classes", allLinkedClasses);
        TemplateManager.getInstance().getGen_test_header().merge(context, result);
        header += result.toString();
        header += HelperGenerator.genENDIF();
        return header;
    }

    public static void writeHeader(Classifier cls, GenerationContext ctx) throws IOException {
        String header = generateHeaderFile(cls);
        FileManager.writeFile(ctx.getGenerationDirectory().getAbsolutePath() + File.separator +
                cls.getName() + "Test.h", header, false);
    }

    /**
     * Test if the metaClassName returns the name of the class.
     *
     * @param cls Classifier to test.
     * @return C Unit test code.
     */
    private static String metaClassNameTestSerializer(Classifier cls) {
        String code = cls.getName() + " *o = new_" + cls.getName() + "();\n";
        code += "\tck_assert_str_eq(o->VT->metaClassName(o), \"" + cls.getName() + "\");";
        return code;
    }

    private static String initObject(Classifier cls, String pointerName) {
        String code = cls.getName() + " *" + pointerName + " = new_" + cls.getName() + "();\n";
        /** Set some mandatory attributes */
        if (cls.inheritVariable("name"))
            code += "\t" + pointerName + "->name = \"some_name\";\n";
        if (cls.inheritVariable("version"))
            code += "\t" + pointerName + "->version = \"some_version\";\n";
        if (cls.inheritVariable("url"))
            code += "\t" + pointerName + " ->url = \"some_url\";\n";
        if (cls.inheritVariable("groupName"))
            code += "\t" + pointerName + " ->groupName = \"some_group_name\";\n";
        if (cls.inheritVariable("hashcode"))
            code += "\t" + pointerName + "->hashcode = \"some_hashcode\";\n";
        return code;
    }

    /**
     * Test if the internalGetKey function returns a non empty string.
     * Some mandatory attributes are set before the call since the only constructor
     * available doesn't accept parameter.
     *
     * @param cls Classifier to test.
     * @return C Unit test code.
     */
    private static String internalGetKeyTestSerializer(Classifier cls) {
        String code = initObject(cls, "o");
        code += "\tif (o->VT->internalGetKey(o) == NULL)\n\t\tck_abort();\n";
        code += "\tck_assert_str_ne(o->VT->internalGetKey(o), \"\");";
        return code;
    }

    /**
     * Generate unit test functions for a particular Variable.
     *
     * @param calledClass Classifier which contains the variable to test.
     * @param variableClass Classifier which originally contains the variable, it can be the same as calledClass if
     *                      there is no inheritance.
     * @param v Variable to test
     * @param functions Map used to store the created test function since we need to return both the name of the test
     *                  function and its source code.
     */
    private static void variableTestSerializer(Classifier calledClass, Classifier variableClass, Variable v, Map<String, String> functions) {
        if (v == null || functions == null)
            return;

        String funName;
        String funCode;
        if (v.getLinkType() == Variable.LinkType.UNARY_LINK) {

            Classifier c = Generator.classifiers.get(v.getType());
            if (c != null && !c.isAbstract()) {
                funName = "removeUnary" + genToUpperCaseFirstChar(v.getName()) + "WhenSetManually";
                VelocityContext context = new VelocityContext();
                StringWriter result = new StringWriter();
                context.put("class", calledClass.getName());
                context.put("ref_name", v.getName());
                context.put("ref_type", v.getType());
                context.put("lowerCaseVarClass", genToLowerCaseFirstChar(variableClass.getName()));
                context.put("upperCaseVar", genToUpperCaseFirstChar(v.getName()));
                TemplateManager.getInstance().getGen_test_remove_unary().merge(context, result);
                functions.put(funName, result.toString());
            }

        } else if (v.getLinkType() == Variable.LinkType.MULTIPLE_LINK) {
            Classifier c = Generator.classifiers.get(v.getType());
            if (c != null && !c.isAbstract()) {
                funName = "removeMultiple" + genToUpperCaseFirstChar(v.getName()) + "AfterAdd";
                VelocityContext context = new VelocityContext();
                StringWriter result = new StringWriter();
                context.put("initO", initObject(calledClass, "o"));
                context.put("initPtr", initObject(c, "ptr"));
                context.put("lowerCaseVarClass", genToLowerCaseFirstChar(variableClass.getName()));
                context.put("upperCaseVar", genToUpperCaseFirstChar(v.getName()));
                TemplateManager.getInstance().getGen_test_remove_multiple().merge(context, result);
                functions.put(funName, result.toString());
            }

        } else if (v.getLinkType() == Variable.LinkType.PRIMITIVE) {
            if (v.getType().equals("char*")) {
                funName = "removePrimitive" + genToUpperCaseFirstChar(v.getName()) + "WhenSetManually";
                VelocityContext context = new VelocityContext();
                StringWriter result = new StringWriter();
                context.put("class", calledClass.getName());
                context.put("ref_name", v.getName());
                context.put("lowerCaseVarClass", genToLowerCaseFirstChar(variableClass.getName()));
                context.put("upperCaseVar", genToUpperCaseFirstChar(v.getName()));
                TemplateManager.getInstance().getGen_test_remove_primitive().merge(context, result);
                functions.put(funName, result.toString());
            }

        }
    }

    private static Map<String, String> attributeTestSerializer(Classifier cls) {
        Map<String, String> functions = new HashMap<String, String>();

        for (Variable v : cls.getVariables()) {
            variableTestSerializer(cls, cls, v, functions);
        }
        for (String s : cls.getAllSuperClass())
            if (!s.equals("KMFContainer"))
                for (Variable v : Generator.classifiers.get(s).getVariables())
                    variableTestSerializer(cls, Generator.classifiers.get(s), v, functions);
        return functions;
    }

    /**
     * Create a test suite for the given Classifier
     *
     * @param cls Classifier to test.
     * @return Map the name of the test with its source code
     */
    private static Map<String, String> generateTestSuite(Classifier cls) {
        Map<String, String> ret = new HashMap<String, String>();
        ret.put("metaClassNameTest", metaClassNameTestSerializer(cls));
        ret.put("internalGetKeyTest", internalGetKeyTestSerializer(cls));
        Map<String, String> attrTest = attributeTestSerializer(cls);

        for (String fun : attrTest.keySet())
            ret.put(fun, attrTest.get(fun));
        return ret;
    }

    private static String generateSourceFile(Classifier cls) {
        VelocityContext context = new VelocityContext();
        StringWriter result = new StringWriter();

        Map<String, String> testSuite = generateTestSuite(cls);
        context.put("name", cls.getName());
        context.put("testSuite", testSuite);

        TemplateManager.getInstance().getGen_test_source().merge(context, result);
        return result.toString();
    }

    public static void writeSource(Classifier cls, GenerationContext ctx) throws IOException {
        String source = generateSourceFile(cls);
        FileManager.writeFile(ctx.getGenerationDirectory().getAbsolutePath() + File.separator +
                cls.getName() + "Test.c", source, false);
    }
}
