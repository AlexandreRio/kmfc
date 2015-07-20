package org.kevoree.modeling.c.generator;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 15/11/13
 * Time: 16:18
 * To change this template use File | Settings | File Templates.
 */
public class Test {

    public static void main(String args[]) throws Exception {

        GenerationContext context = new GenerationContext();
        context.setRootGenerationDirectory("/home/orbital/IRISA/kmfc");
        context.setEcore("/home/orbital/IRISA/kmfc/org.kevoree.modeling.c.generator/src/main/resources/metamodel/kevoree.ecore");
        context.setDebug_model(true);
        context.setVersion("1.0");
        context.setVersionmicroframework("1.3.2");


        Generator gen = new Generator(context);
        gen.generateModel();
        // gen.generateEnvironnement();


    }
}
