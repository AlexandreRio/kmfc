package org.kevoree.modeling.c.generator;

public class Test {

    public static void main(String args[]) throws Exception {
        String root = "/home/ario/IRISA/kmfc/";
        String eCore = "org.kevoree.modeling.c.generator/src/main/resources/metamodel/kevoree.ecore";
        String framework = "org.kevoree.modeling.c.generator/src/main/resources/hardcoded/";
        String output = "kevoree/";

        GenerationContext context = new GenerationContext();
        context.setRoot(root);
        context.setGenerationDirectory(output);
        context.setECore(eCore);
        context.setFrameworkDirectory(framework);
//        context.setVersion("1.0");
//        context.setVersionMicroFramework("1.3.2");

        Generator gen = new Generator(context);
        gen.generateModel();
        gen.generateEnvironment();
    }
}
