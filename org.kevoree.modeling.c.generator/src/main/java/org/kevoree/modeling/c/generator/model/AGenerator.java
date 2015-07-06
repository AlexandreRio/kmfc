package org.kevoree.modeling.c.generator.model;

import org.kevoree.modeling.c.generator.GenerationContext;
import org.apache.velocity.app.VelocityEngine;
import org.eclipse.emf.ecore.EClass;
import org.kevoree.modeling.c.generator.utils.HelperGenerator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 29/10/13
 * Time: 10:23
 * To change this templates use File | Settings | File Templates.
 */
public abstract class AGenerator {
    protected GenerationContext ctx;

    protected StringBuilder header;
    protected StringBuilder body;
    protected StringBuilder constructor;
    protected StringBuilder destructor;

    protected StringBuilder begin_header;
    protected StringBuilder attributes;
    protected StringBuilder method_signature;
    protected StringBuilder virtual_table;
    protected StringBuilder self_attribute;

    protected StringBuilder class_result;
    protected StringBuilder header_result;

    protected String className;
    protected static Map<String, StringBuilder> classAttributes = new HashMap<String, StringBuilder>();

    protected void add_CONSTRUCTOR(String source) {
        constructor.append(source + "\n");
    }

    protected void add_DESTRUCTOR(String source) {
        destructor.append(source + "\n");
    }

    protected void add_C(String source) {
        class_result.append(source + "\n");
    }

    protected String msg_DEBUG(EClass cls, String msg) {
        return "LOGGER_WRITE(Logger::DEBUG_MODEL,\"" + cls.getName() + " --> " + msg + "\");";
    }

    protected String msg_ERROR(EClass cls, String msg) {
        return "LOGGER_WRITE(Logger::DEBUG_MODEL,\"" + cls.getName() + " --> " + msg + "\");";
    }

    protected void ADD_DEBUG(EClass cls, String msg) {
        if (ctx.isDebug_model()) {
            add_C(msg_DEBUG(cls, msg));
        }
    }

    protected void add_begin_header(String source) {
        begin_header.append(source);
    }

    protected void add_ATTRIBUTE(String source) {
        attributes.append(source + "\n");
    }

    public static void add_class_attribute(String className, String attr) {
        // can be refactored to remove double access
        if (classAttributes.containsKey(className))
            classAttributes.get(className).append(attr + "\n");
        else
            classAttributes.put(className, new StringBuilder(attr + "\n"));
    }

    protected void add_method_signature_H(String source) {
        method_signature.append(source + "\n");
    }

    protected void add_virtual_table_H(String source) {
        virtual_table.append(source + "\n");
    }

    protected void add_self_attribute_H(String source) {
        self_attribute.append(source + "\n");
    }

    protected void initGeneration(String className) {
        this.className = className;

        //C file
        header = new StringBuilder();
        body = new StringBuilder();
        constructor = new StringBuilder();
        destructor = new StringBuilder();

        //H file
        begin_header = new StringBuilder();
        attributes = new StringBuilder();
        method_signature = new StringBuilder();
        virtual_table = new StringBuilder();
        self_attribute = new StringBuilder();

        header_result = new StringBuilder();
        class_result = new StringBuilder();
    }

//    protected void generateDestructorMethod(EClass cls) {
//        add_H("~" + cls.getName() + "();\n");
//        add_C(cls.getName() + "::~" + cls.getName() + "(){\n");
//        add_C(destructor.toString());
//        add_C("}\n");
//    }

//    protected void generateConstructorMethod(EClass cls) {
//        add_H(cls.getName() + "* new_" + cls.getName() + "(void);\n");
//        add_C(cls.getName() + "* new_" + cls.getName() + "(){\n");
//        add_C(constructor.toString());
//        add_C("}\n");
//    }

    public void link_generation() {
        // c file
        class_result.append(header);
        class_result.append(body);

        // header file
        header_result.append(begin_header + "\n");
        header_result.append(method_signature + "\n");

        header_result.append("typedef struct _" + this.className +
                "_VT {typedef struct _" + this.className + "_VT {\n");
        header_result.append(virtual_table);
        header_result.append("} " + this.className + "_VT;\n\n");

        header_result.append("typedef struct _" + this.className + " {\n");
        header_result.append(attributes);
        header_result.append("} " + this.className + ";\n\n");
        header_result.append(HelperGenerator.genENDIF());
    }

}
