package org.kevoree.modeling.c.generator.model;

import org.eclipse.emf.ecore.EClass;
import org.kevoree.modeling.c.generator.utils.HelperGenerator;
import org.kevoree.modeling.c.generator.GenerationContext;
import org.kevoree.modeling.c.generator.utils.FileManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 31/10/13
 * Time: 08:50
 * To change this template use File | Settings | File Templates.
 */
public class FactoryGenerator extends AGenerator {

    private StringBuilder result = new StringBuilder();
    private String factoryname;
    private List<String> classes = new ArrayList<String>();

    public FactoryGenerator(GenerationContext context) {
        super();

        this.ctx = context;
        factoryname = "Default" + ctx.getName_package() + "Factory";
        initGeneration(ctx.getName_package());
        generateClassHeader();
    }

    public void generateFactory(EClass e) {
        if (!e.isAbstract() && !e.isInterface()) {
            generateClass(e);
            classes.add(e.getName());
        }
    }


    public void generateClassHeader() {
        //add_H(HelperGenerator.genIFDEF(factoryname));
        //add_H(HelperGenerator.genIncludeLocal(ctx.getName_package()));
        //add_H(HelperGenerator.genInclude("microframework/api/KMFFactory.h"));

        //add_H("class "+factoryname+" : public KMFFactory {");
        //add_H("public:");

    }

    public void generateClass(EClass e) {
        add_C(e.getName() + "* create" + e.getName() + "(){return new " + e.getName() + "();\n};");
    }

    public void generateVersion() {
        //add_H("string getVersion();");
        add_C("string getVersion(){ return " + ctx.getVersion() + ";\n}");
    }

    public void generateCreate() {
        add_C("KMFContainer* create(std::string metaClassName){");
        for (String name : classes) {
            add_C("if(metaClassName.compare(\"org." + ctx.getName_package() + "." + name + "\")==0){");
            add_C("return create" + name + "();");
            add_C("}");
        }
        add_C("return NULL;");
        add_C("}");
    }

    public void write() {
        link_generation();
        try {
            generateCreate();
            class_result.append("};\n"); // end class
            class_result.append(HelperGenerator.genENDIF());

            FileManager.writeFile(ctx.getPackageGenerationDirectory() + factoryname + ".h", header_result.toString(), false);
            FileManager.writeFile(ctx.getPackageGenerationDirectory() + factoryname + ".h", class_result.toString(), true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
