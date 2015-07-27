package org.kevoree.modeling.c.generator;

import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

public class TemplateManager {

    private static TemplateManager self;
    protected VelocityEngine ve = new VelocityEngine();
    /**
     * Templates with variables.
     */
    private Template gen_method_add;
    private Template gen_method_add_unary_containment;
    private Template gen_method_remove;
    private Template gen_visitor;
    private Template gen_visitor_ref;
    private Template gen_delete_ref;
    private Template gen_method_new;
    private Template gen_find_by_id;
    private Template gen_cmakelists;
    /**
     * Templates with no variables.
     */
    private Template tp_getKey_DeployUnit;
    private Template tp_getKey_TypeDefinition;
    private Template tp_KMFContainer_fptr;
    private Template tp_print_debug;

    private TemplateManager() {
        String BASE_DIR = "templates/";
        ve.setProperty("file.resource.loader.class", ClasspathResourceLoader.class.getName());
        ve.init();

        gen_method_add = ve.getTemplate(BASE_DIR + "add_method.vm");
        gen_method_add_unary_containment = ve.getTemplate(BASE_DIR + "add_unary_containment.vm");
        gen_method_remove = ve.getTemplate(BASE_DIR + "remove_method.vm");
        gen_visitor = ve.getTemplate(BASE_DIR + "visitor.vm");
        gen_visitor_ref = ve.getTemplate(BASE_DIR + "visitor_ref.vm");
        gen_delete_ref = ve.getTemplate(BASE_DIR + "delete_ref.vm");
        gen_method_new = ve.getTemplate(BASE_DIR + "new_method.vm");
        gen_find_by_id = ve.getTemplate(BASE_DIR + "findById_method.vm");
        gen_cmakelists = ve.getTemplate(BASE_DIR + "cmake.vm");

        tp_getKey_DeployUnit = ve.getTemplate(BASE_DIR + "internalGetKey_DeployUnit.vm");
        tp_getKey_TypeDefinition = ve.getTemplate(BASE_DIR + "internalGetKey_TypeDefinition.vm");
        tp_KMFContainer_fptr = ve.getTemplate(BASE_DIR + "kmfcontainer_fptr.vm");
        tp_print_debug = ve.getTemplate(BASE_DIR + "print_debug.vm");
    }

    public static TemplateManager getInstance() {
        if (self == null)
            self = new TemplateManager();
        return self;
    }

    public Template getGen_method_add() {
        return gen_method_add;
    }

    public Template getGen_method_add_unary_containment() {
        return gen_method_add_unary_containment;
    }

    public Template getGen_method_remove() {
        return gen_method_remove;
    }

    public Template getGen_visitor() {
        return gen_visitor;
    }

    public Template getGen_visitor_ref() {
        return gen_visitor_ref;
    }

    public Template getGen_delete_ref() {
        return gen_delete_ref;
    }

    public Template getGen_method_new() {
        return gen_method_new;
    }

    public Template getGen_find_by_id() {
        return gen_find_by_id;
    }

    public Template getGen_cmakelists() {
        return gen_cmakelists;
    }

    public Template getTp_getKey_DeployUnit() {
        return tp_getKey_DeployUnit;
    }

    public Template getTp_getKey_TypeDefinition() {
        return tp_getKey_TypeDefinition;
    }

    public Template getTp_KMFContainer_fptr() {
        return tp_KMFContainer_fptr;
    }

    public Template getTp_print_debug() {
        return tp_print_debug;
    }

}
