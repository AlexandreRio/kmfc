package org.kevoree.modeling.c.generator;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.StringWriter;

public class TemplateManager {

    private static TemplateManager self;
    protected VelocityEngine ve = new VelocityEngine();
    /*
     * Templates with variables.
     */
    private Template gen_cmakelists;
    private Template gen_add;
    private Template gen_add_unary_containment;
    private Template gen_remove;
    private Template gen_visitor;
    private Template gen_visitor_ref;
    private Template gen_delete;
    private Template gen_new;
    private Template gen_find_by_id;

    private Template gen_toJSON_multiple;
    private Template gen_toJSON_unary;
    private Template gen_toJSON_primitive_as_str;
    private Template gen_toJSON_primitive_bool;

    private Template gen_test_runner;
    private Template gen_test_header;
    private Template gen_test_source;
    private Template gen_test_remove_primitive;
    private Template gen_test_remove_unary;
    private Template gen_test_remove_multiple;
    private Template gen_test_add_unary;
    private Template gen_test_add_multiple;
    private Template gen_test_add_primitive;
    private Template gen_test_find;
    /*
     * string from static template, i.e. with no variables
     * or variables determined before parsing any files.
     */
    private String getKey_DeployUnit;
    private String getKey_TypeDefinition;
    private String KMFContainer_fptr;
    private String KMFContainer;
    private String print_debug;
    private String license;
    private String header_comment;

    private TemplateManager() {
        String BASE_DIR = "templates/";
        String CODE_DIR = "code/";
        String TEST_DIR = "test/";
        String COMMENTS_DIR = "comments/";
        String SOURCE_DIR = "source/";
        String HEADER_DIR = "header/";
        ve.setProperty("file.resource.loader.class", ClasspathResourceLoader.class.getName());
        ve.init();

        gen_cmakelists = ve.getTemplate(BASE_DIR + "cmake.vm");
        gen_add = ve.getTemplate(BASE_DIR + CODE_DIR + SOURCE_DIR + "add.vm");
        gen_add_unary_containment = ve.getTemplate(BASE_DIR + CODE_DIR + SOURCE_DIR + "add_unary_containment.vm");
        gen_remove = ve.getTemplate(BASE_DIR + CODE_DIR + SOURCE_DIR + "remove.vm");
        gen_visitor = ve.getTemplate(BASE_DIR + CODE_DIR + SOURCE_DIR + "visitor.vm");
        gen_visitor_ref = ve.getTemplate(BASE_DIR + CODE_DIR + SOURCE_DIR + "visitor_ref.vm");
        gen_delete = ve.getTemplate(BASE_DIR + CODE_DIR + SOURCE_DIR + "delete.vm");
        gen_new = ve.getTemplate(BASE_DIR + CODE_DIR + SOURCE_DIR + "new.vm");
        gen_find_by_id = ve.getTemplate(BASE_DIR + CODE_DIR + SOURCE_DIR + "findById.vm");

        gen_toJSON_multiple = ve.getTemplate(BASE_DIR + CODE_DIR + SOURCE_DIR + "toJSON_multiple.vm");
        gen_toJSON_unary = ve.getTemplate(BASE_DIR + CODE_DIR + SOURCE_DIR + "toJSON_unary.vm");
        gen_toJSON_primitive_as_str = ve.getTemplate(BASE_DIR + CODE_DIR + SOURCE_DIR + "toJSON_primitive_as_str.vm");
        gen_toJSON_primitive_bool = ve.getTemplate(BASE_DIR + CODE_DIR + SOURCE_DIR + "toJSON_primitive_bool.vm");

        gen_test_runner = ve.getTemplate(BASE_DIR + TEST_DIR + SOURCE_DIR + "testRunner.vm");
        gen_test_header = ve.getTemplate(BASE_DIR + TEST_DIR + HEADER_DIR + "testHeader.vm");
        gen_test_source = ve.getTemplate(BASE_DIR + TEST_DIR + SOURCE_DIR + "testSource.vm");
        gen_test_remove_primitive = ve.getTemplate(BASE_DIR + TEST_DIR + SOURCE_DIR + "remove/removePrimitive.vm");
        gen_test_remove_unary = ve.getTemplate(BASE_DIR + TEST_DIR + SOURCE_DIR + "remove/removeUnary.vm");
        gen_test_remove_multiple = ve.getTemplate(BASE_DIR + TEST_DIR + SOURCE_DIR + "remove/removeMultiple.vm");
        gen_test_add_unary = ve.getTemplate(BASE_DIR + TEST_DIR + SOURCE_DIR + "add/addUnary.vm");
        gen_test_add_primitive = ve.getTemplate(BASE_DIR + TEST_DIR + SOURCE_DIR + "add/addPrimitive.vm");
        gen_test_add_multiple = ve.getTemplate(BASE_DIR + TEST_DIR + SOURCE_DIR + "add/addMultiple.vm");
        gen_test_find = ve.getTemplate(BASE_DIR + TEST_DIR + SOURCE_DIR + "find/find.vm");

        VelocityContext context = new VelocityContext();
        StringWriter result = new StringWriter();
        ve.getTemplate(BASE_DIR + CODE_DIR + SOURCE_DIR + "internalGetKey_DeployUnit.vm").merge(context, result);
        getKey_DeployUnit = result.toString();

        result = new StringWriter();
        ve.getTemplate(BASE_DIR + CODE_DIR + SOURCE_DIR + "internalGetKey_TypeDefinition.vm").merge(context, result);
        getKey_TypeDefinition = result.toString();

        result = new StringWriter();
        ve.getTemplate(BASE_DIR + CODE_DIR + HEADER_DIR + "kmfcontainer_fptr.vm").merge(context, result);
        KMFContainer_fptr = result.toString();

        result = new StringWriter();
        context.put("kmfcontainer_fptr", KMFContainer_fptr);
        ve.getTemplate(BASE_DIR + CODE_DIR + HEADER_DIR + "kmfcontainer.vm").merge(context, result);
        KMFContainer = result.toString();

        result = new StringWriter();
        ve.getTemplate(BASE_DIR + CODE_DIR + SOURCE_DIR + "print_debug.vm").merge(context, result);
        print_debug = result.toString();

        result = new StringWriter();
        ve.getTemplate(BASE_DIR + COMMENTS_DIR + "header.vm").merge(context, result);
        header_comment = result.toString();

        result = new StringWriter();
        context.put("project", "KMC");
        ve.getTemplate(BASE_DIR + "LICENSE.vm").merge(context, result);
        license = result.toString();

    }

    public static TemplateManager getInstance() {
        if (self == null)
            self = new TemplateManager();
        return self;
    }

    public Template getGen_add() {
        return gen_add;
    }

    public Template getGen_add_unary_containment() {
        return gen_add_unary_containment;
    }

    public Template getGen_remove() {
        return gen_remove;
    }

    public Template getGen_visitor() {
        return gen_visitor;
    }

    public Template getGen_visitor_ref() {
        return gen_visitor_ref;
    }

    public Template getGen_delete() {
        return gen_delete;
    }

    public Template getGen_new() {
        return gen_new;
    }

    public Template getGen_find_by_id() {
        return gen_find_by_id;
    }

    public Template getGen_toJSON_multiple() {
        return gen_toJSON_multiple;
    }

    public Template getGen_toJSON_unary() {
        return gen_toJSON_unary;
    }

    public Template getGen_toJSON_primitive_as_str() {
        return gen_toJSON_primitive_as_str;
    }

    public Template getGen_toJSON_primitive_bool() {
        return gen_toJSON_primitive_bool;
    }

    public Template getGen_cmakelists() {
        return gen_cmakelists;
    }

    public Template getGen_test_runner() {
        return gen_test_runner;
    }

    public Template getGen_test_header() {
        return gen_test_header;
    }

    public Template getGen_test_source() {
        return gen_test_source;
    }

    public String getGetKey_DeployUnit() {
        return getKey_DeployUnit;
    }

    public String getGetKey_TypeDefinition() {
        return getKey_TypeDefinition;
    }

    public String getKMFContainer_fptr() {
        return KMFContainer_fptr;
    }

    public String getKMFContainer() {
        return KMFContainer;
    }

    public String getPrint_debug() {
        return print_debug;
    }

    public String getLicense() {
        return license;
    }

    public String getHeader_comment() {
        return header_comment;
    }

    public Template getGen_test_remove_primitive() {
        return gen_test_remove_primitive;
    }

    public Template getGen_test_remove_unary() {
        return gen_test_remove_unary;
    }

    public Template getGen_test_remove_multiple() {
        return gen_test_remove_multiple;
    }

    public Template getGen_test_add_unary() {
        return gen_test_add_unary;
    }

    public Template getGen_test_add_primitive() {
        return gen_test_add_primitive;
    }

    public Template getGen_test_add_multiple() {
        return gen_test_add_multiple;
    }

    public Template getGen_test_find() {
        return gen_test_find;
    }
}
