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

import static org.kevoree.modeling.c.generator.utils.HelperGenerator.lowerCaseFirstChar;
import static org.kevoree.modeling.c.generator.utils.HelperGenerator.upperCaseFirstChar;

/**
 * Serialize all the tests of a Classifier
 */
public class TestSerializer {

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
        String header = TemplateManager.getInstance().getLicense();
        header += TemplateManager.getInstance().getHeader_comment();
        header += generateHeaderFile(cls);
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
        String code = "\t" + cls.getName() + " *o = new_" + cls.getName() + "();\n";
        code += "\tck_assert_str_eq(o->VT->metaClassName(o), \"" + cls.getName() + "\");\n";
        code += "\to->VT->delete(o);\n";
        code += "\tfree(o);";
        return code;
    }

    /**
     * Generate the creation of the object and the initialization of its mandatory attributes.
     * Mandatory attributes are handwritten and do not depend of any {@link Variable} attributes.
     *
     * @param cls         Classifier to test.
     * @param pointerName Name of the pointer referring to the object.
     * @return Source code of the initialization.
     */
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
        code += "\tck_assert_str_ne(o->VT->internalGetKey(o), \"\");\n";
        code += "\to->VT->delete(o);\n";
        code += "\tfree(o);";
        return code;
    }

    /**
     * Generate unit test functions for a particular Variable.
     *
     * @param calledClass   Classifier which contains the variable to test.
     * @param variableClass Classifier which originally contains the variable, it can be the same as calledClass if
     *                      there is no inheritance.
     * @param v             Variable to test
     * @param functions     Map used to store the created test function since we need to return both the name of the test
     *                      function and its source code.
     */
    private static void variableTestSerializer(Classifier calledClass, Classifier variableClass, Variable v, Map<String, String> functions) {
        if (v == null || functions == null)
            return;

        String funName;
        VelocityContext context = new VelocityContext();
        StringWriter result = new StringWriter();
        if (v.getLinkType() == Variable.LinkType.UNARY_LINK) {

            Classifier c = Generator.classifiers.get(v.getType());
            if (c != null && !c.isAbstract()) {
                context.put("class", calledClass.getName());
                context.put("ref_name", v.getName());
                context.put("ref_type", v.getType());
                context.put("lowerCaseVarClass", lowerCaseFirstChar(variableClass.getName()));
                context.put("upperCaseVar", upperCaseFirstChar(v.getName()));

                funName = "removeUnary" + upperCaseFirstChar(v.getName()) + "WhenSetManually";
                TemplateManager.getInstance().getGen_test_remove_unary().merge(context, result);
                functions.put(funName, result.toString());

                funName = "addUnary" + upperCaseFirstChar(v.getName());
                result = new StringWriter();
                TemplateManager.getInstance().getGen_test_add_unary().merge(context, result);
                functions.put(funName, result.toString());
            }

        } else if (v.getLinkType() == Variable.LinkType.MULTIPLE_LINK) {
            Classifier c = Generator.classifiers.get(v.getType());
            if (c != null && !c.isAbstract()) {
                context.put("initO", initObject(calledClass, "o"));
                context.put("initPtr", initObject(c, "ptr"));
                context.put("lowerCaseVarClass", lowerCaseFirstChar(variableClass.getName()));
                context.put("upperCaseVar", upperCaseFirstChar(v.getName()));
                context.put("ref_type", c.getName());
                context.put("ref_name", v.getName());


                context.put("free", "ptr->VT->delete(ptr);free(ptr);\n");

                funName = "removeMultiple" + upperCaseFirstChar(v.getName()) + "AfterAdd";
                TemplateManager.getInstance().getGen_test_remove_multiple().merge(context, result);
                functions.put(funName, result.toString());

                if (v.isContained()) {
                    context.put("free", "");
                } else {
                    context.put("free", "ptr->VT->delete(ptr);\nfree(ptr);\n");
                }

                funName = "addMultiple" + upperCaseFirstChar(v.getName());
                result = new StringWriter();
                TemplateManager.getInstance().getGen_test_add_multiple().merge(context, result);
                functions.put(funName, result.toString());

                funName = "find" + upperCaseFirstChar(v.getName());
                result = new StringWriter();
                TemplateManager.getInstance().getGen_test_find().merge(context, result);
                functions.put(funName, result.toString());
            }

        } else if (v.getLinkType() == Variable.LinkType.PRIMITIVE) {
            if (v.getType().equals("char*")) {
                context.put("class", calledClass.getName());
                context.put("ref_name", v.getName());
                context.put("lowerCaseVarClass", lowerCaseFirstChar(variableClass.getName()));
                context.put("upperCaseVar", upperCaseFirstChar(v.getName()));

                funName = "removePrimitive" + upperCaseFirstChar(v.getName()) + "WhenSetManually";
                TemplateManager.getInstance().getGen_test_remove_primitive().merge(context, result);
                functions.put(funName, result.toString());

                funName = "addPrimitive" + upperCaseFirstChar(v.getName());
                result = new StringWriter();
                TemplateManager.getInstance().getGen_test_add_primitive().merge(context, result);
                functions.put(funName, result.toString());
            }

        }
    }

    private static Map<String, String> attributeTestSerializer(Classifier cls) {
        Map<String, String> functions = new HashMap<String, String>();

        for (Variable v : cls.getVariables()) {
            if (!v.isGenerated())
                variableTestSerializer(cls, cls, v, functions);
        }
        for (String s : cls.getAllSuperClass())
            if (!s.equals("KMFContainer"))
                for (Variable v : Generator.classifiers.get(s).getVariables())
                    if (!v.isGenerated())
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
        String source = TemplateManager.getInstance().getLicense();
        source += TemplateManager.getInstance().getHeader_comment();
        source += generateSourceFile(cls);
        FileManager.writeFile(ctx.getGenerationDirectory().getAbsolutePath() + File.separator +
                cls.getName() + "Test.c", source, false);
    }
}
