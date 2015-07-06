package org.kevoree.modeling.c.generator;

import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

public class TemplateManager {

    public static Template gen_method_add;
    public static Template gen_method_remove;
    public static Template gen_visitor;
    public static Template gen_visitor_ref;
    public static Template gen_destructor_ref;

    protected VelocityEngine ve = new VelocityEngine();

    private static TemplateManager self;

    private TemplateManager() {
        ve.setProperty("file.resource.loader.class", ClasspathResourceLoader.class.getName()) ;
        ve.init();
        gen_method_add      = ve.getTemplate("templates/add_method.vm");
        gen_method_remove   = ve.getTemplate("templates/remove_method.vm");
        gen_visitor         = ve.getTemplate("templates/visitor.vm");
        gen_visitor_ref     = ve.getTemplate("templates/visitor_ref.vm");
        gen_destructor_ref  = ve.getTemplate("templates/destructor_ref.vm");
    }

    public static TemplateManager getInstance() {
        if (self == null)
            self = new TemplateManager();
        return self;
    }
}
