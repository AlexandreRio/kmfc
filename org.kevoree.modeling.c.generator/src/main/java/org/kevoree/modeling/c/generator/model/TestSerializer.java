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
import java.util.Map;

/**
 * Serialize all the tests of a Classifier
 */
public class TestSerializer {

    private static String generateHeaderFile(Classifier cls) {
        VelocityContext context = new VelocityContext();
        StringWriter result = new StringWriter();
        context.put("name", cls.getName());
        context.put("classes", Classifier.getLinkedClassifier(cls));
        TemplateManager.getInstance().getGen_test_header().merge(context, result);
        return result.toString();
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

    /**
     * Test if the internalGetKey function returns a non empty string.
     * Some mandatory attributes are set before the call since the only constructor
     * available doesn't accept parameter.
     *
     * @param cls Classifier to test.
     * @return C Unit test code.
     */
    private static String internalGetKeyTestSerializer(Classifier cls) {
        String code = cls.getName() + " *o = new_" + cls.getName() + "();\n";
        /** Set some mandatory attributes */
        if (cls.inheritVariable("name"))
            code += "\to->name = \"some_name\";\n";
        if (cls.inheritVariable("version"))
            code += "\to->version = \"some_version\";\n";
        if (cls.inheritVariable("url"))
            code += "\to->url = \"some_url\";\n";

        code += "\tif (o->VT->internalGetKey(o) == NULL)\n\t\tck_abort();\n";
        code += "\tck_assert_str_ne(o->VT->internalGetKey(o), \"\");";
        return code;
    }

    private static Map<String, String> selfAttributeTestSerializer(Classifier cls) {
        Map<String, String> functions = new HashMap<String, String>();
        String funName;
        String funCode;
        for (Variable v : cls.getVariables()) {
            if (v.getLinkType() == Variable.LinkType.UNARY_LINK) {
                //actually this test nothing…
                funName = "remove" + HelperGenerator.genToUpperCaseFirstChar(v.getName()) + "WhenNotSet";
                funCode = cls.getName() + "*o = new_" + cls.getName() + "();\n";
                funCode += "\tck_assert(o->" + v.getName() + " == NULL);";
                functions.put(funName, funCode);

                Classifier c = Generator.classifiers.get(v.getType());
                if (c != null && !c.isAbstract()) {
                    funName = "remove" + v.getName() + "WhenSetManually";
                    funCode = cls.getName() + "*o = new_" + cls.getName() + "();\n";
                    funCode += "\t" + v.getType() + " *ptr = new_" + v.getType() + "();\n";
                    funCode += "\to->" + v.getName() + " = ptr;\n";
                    funCode += "\tck_assert(o->" + v.getName() + " != NULL);\n";
                    funCode += "\to->VT->" + HelperGenerator.genToLowerCaseFirstChar(cls.getName()) +
                            "Remove" + HelperGenerator.genToUpperCaseFirstChar(v.getName()) + "(o, ptr);\n";
                    funCode += "\tck_assert(o->" + v.getName() + " == NULL);";
                    functions.put(funName, funCode);
                }
            } else if (v.getLinkType() == Variable.LinkType.MULTIPLE_LINK) {

            } else if (v.getLinkType() == Variable.LinkType.PRIMITIVE) {
                if (v.getType().equals("char*")) {
                    funName = "remove" + HelperGenerator.genToUpperCaseFirstChar(v.getName()) + "WhenSetManually";
                    funCode = cls.getName() + "*o = new_" + cls.getName() + "();\n";
                    funCode += "\tck_assert(o->" + v.getName() + " == NULL);\n";
                    funCode += "\tchar * str = \"my_str\";\n";
                    funCode += "\to->" + v.getName() + " = str;\n";
                    funCode += "\tck_assert(o->" + v.getName() + " != NULL);\n";
                    funCode += "\to->VT->" + HelperGenerator.genToLowerCaseFirstChar(cls.getName()) +
                            "Remove" + HelperGenerator.genToUpperCaseFirstChar(v.getName()) + "(o, str);\n";
                    funCode += "\tck_assert(o->" + v.getName() + " == NULL);";
                    functions.put(funName, funCode);
                }
            }
        }
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
        Map<String, String> selfAttrTest = selfAttributeTestSerializer(cls);
        for (String fun : selfAttrTest.keySet())
            ret.put(fun, selfAttrTest.get(fun));
        // test inherited attributes set/get
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
