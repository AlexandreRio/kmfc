package org.kevoree.modeling.c.generator.model;

import org.apache.velocity.VelocityContext;
import org.kevoree.modeling.c.generator.GenerationContext;
import org.kevoree.modeling.c.generator.TemplateManager;
import org.kevoree.modeling.c.generator.utils.FileManager;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class TestSerializer {

    private static String generateHeaderFile(Classifier cls) {
        VelocityContext context = new VelocityContext();
        StringWriter result = new StringWriter();
        context.put("name", cls.getName());
        TemplateManager.getInstance().getGen_test_header().merge(context, result);
        return result.toString();
    }

    public static void writeHeader(Classifier cls, GenerationContext ctx) throws IOException {
        String header = generateHeaderFile(cls);
        FileManager.writeFile(ctx.getGenerationDirectory().getAbsolutePath() + File.separator +
                cls.getName() + "Test.h", header, false);
    }

    private static String metaClassNameTestSerializer(Classifier cls) {
        String code = cls.getName() + " *o = new_" + cls.getName() + "();\n";
        code += "\tck_assert_str_eq(o->VT->metaClassName(o), \"" + cls.getName() + "\");";
        return code;
    }

    private static String internalGetKeyTestSerializer(Classifier cls) {
        String code = cls.getName() + " *o = new_" + cls.getName() + "();\n";
        code += "\tck_assert_str_ne(o->VT->internalGetKey(o), \"\");";
        return code;
    }

    private static Map<String, String> generateTestSuite(Classifier cls) {
        Map<String, String> ret = new HashMap<String, String>();
        ret.put("metaClassNameTest", metaClassNameTestSerializer(cls));
        ret.put("internalGetKeyTest", internalGetKeyTestSerializer(cls));
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
