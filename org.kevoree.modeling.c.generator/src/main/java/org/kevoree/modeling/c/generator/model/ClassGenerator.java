package org.kevoree.modeling.c.generator.model;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.kevoree.modeling.c.generator.GenerationContext;
import org.kevoree.modeling.c.generator.Generator;
import org.kevoree.modeling.c.generator.TemplateManager;
import org.kevoree.modeling.c.generator.utils.ConverterDataTypes;
import org.kevoree.modeling.c.generator.utils.FileManager;
import org.kevoree.modeling.c.generator.utils.HelperGenerator;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Generator for a class of the meta-model.
 * Call order are important.
 * Some code is generated while parsing, some after the full parsing since
 * parents can be reached after child class.
 *
 * @see #link_generation() For final concatenation
 * @see Generator#generateModel()
 */
public class ClassGenerator extends AGenerator {

    private EClass cls;

    public ClassGenerator(GenerationContext ctx) {
        this.ctx = ctx;
    }

    /**
     * global generation method
     */
    public void generateClass(EClass cls) throws IOException {
        this.cls = cls;
        initGeneration(cls.getName());

        generateBeginHeader();
        generateVirtualTableComment();
        generateInternalGetKey();
        generateFunctionHeader();
        generateInit();
        generateAttributes();
        generateMethodAdd();
        generateMethodRemove();
        generateGetterMetaClassName();
        generateDeleteMethod();
    }

    private void generateVirtualTableComment() {
        add_virtual_table_H("/* " + cls.getName() + " */");
    }

    private void generateDeleteMethod() {
        StringWriter result = new StringWriter();

        for (EReference ref : cls.getEAllReferences()) {

            if (ref.getUpperBound() == -1) {
                if (ref.isContainment()) {
                    if (ref.getEReferenceType().getEIDAttribute() != null) {
                        VelocityContext context = new VelocityContext();
                        context.put("refname", ref.getName());
                        context.put("type", ref.getEReferenceType().getName());

                        TemplateManager.getInstance().getGen_destructor_ref().merge(context, result);
                    }
                } else {
                    add_DESTRUCTOR(ref.getName() + ".clear();");
                }

            } else {
                if (ref.isContainment()) {
                    result.append("if(" + ref.getName() + " != NULL){\n");
                    result.append("delete " + ref.getName() + ";\n");
                    result.append(ref.getName() + "= NULL;");
                    result.append("}\n");
                }

            }


        }
        add_DESTRUCTOR(result.toString());
    }

    private void generateInit() {
        // header
        add_method_signature_H("void init" + cls.getName() + "(" + cls.getName() + "* const this);");

        // implementation
        String parentType = "";
        if (cls.getESuperTypes().size() == 1)
            parentType = cls.getEAllSuperTypes().get(0).getName();
        else if (cls.getEAllSuperTypes().size() == 0)
            parentType = "KMFContainer";
        else
            System.err.println("Invalid number of parent for " + cls.getName());

        add_init("init" + parentType + "((" + parentType + "*)this);");
    }

    private void generateGetterMetaClassName() {
        add_C("static char* " + cls.getName() + "_metaClassName(" + cls.getName() + "* const this) {");
        add_C("\treturn \"" + cls.getName() + "\";");
        add_C("}");
    }

    private void generateMethodRemove() {
        for (EReference ref : cls.getEReferences()) {
            String type = ConverterDataTypes.getInstance().check_type(ref.getEReferenceType().getName());

            add_C("static void\n" + cls.getName() + "_remove" + HelperGenerator.genToUpperCaseFirstChar(ref.getName()) +
                    "(" + cls.getName() + "* const this, " + type + " *ptr) {");
            if (ref.getUpperBound() == -1) {
                if (ref.getEReferenceType().getEIDAttribute() == null) {
                    System.err.println("null id are not in the compiler scope");
                } else {
                    VelocityContext context = new VelocityContext();
                    context.put("refname", ref.getName());
                    context.put("type", type);

                    if (ref.isContainment())
                        context.put("iscontained", "ptr->eContainer = NULL;");
                    else
                        context.put("iscontained", "");

                    StringWriter result = new StringWriter();
                    TemplateManager.getInstance().getGen_method_remove().merge(context, result);

                    add_C(result.toString());
                }

            } else { // Unary link
                add_C("\tthis->" + ref.getName() + " = NULL;");
                if (ref.isContainment())
                    add_C("\tptr->eContainer = NULL;");
            }
            add_C("}\n");
        } // end for
    } // end method

    public void generateMethodAdd() {
        for (EReference ref : cls.getEReferences()) {
            String type = ConverterDataTypes.getInstance().check_type(ref.getEReferenceType().getName());
            // function signature
            add_C("static void\n" + cls.getName() + "_add" + HelperGenerator.genToUpperCaseFirstChar(ref.getName()) +
                "(" + cls.getName() + "* const this, " + type + " *ptr) {");

            StringWriter result = new StringWriter();
            if (ref.getUpperBound() == -1) { // 0..* link

                if (ref.getEReferenceType().getEIDAttribute() == null) {
                    // never happen… weird
                    System.err.println("null id are not in the compiler scope");
                } else {
                    VelocityContext context = new VelocityContext();
                    context.put("classname", cls.getName());
                    context.put("refname", ref.getName());
                    context.put("type", type);
                    context.put("methname", HelperGenerator.genToUpperCaseFirstChar(ref.getName()));

                    if (!ref.isContainment())
                        context.put("iscontained", "");
                    else
                        context.put("iscontained", "ptr->eContainer = this;");

                    TemplateManager.getInstance().getGen_method_add().merge(context, result);
                    add_C(result.toString());
                }

            } else { // Unary link
                if (ref.isContainment()) { // if it's a container
                    VelocityContext context = new VelocityContext();
                    context.put("refname", ref.getName());
                    context.put("methname", HelperGenerator.genToUpperCaseFirstChar(ref.getName()));
                    TemplateManager.getInstance().getGen_method_add_unary_containment().merge(context, result);
                    add_C(result.toString());
                } else {
                    add_C("\tthis->" + ref.getName() + " = ptr;");
                }
                add_C("}\n");
            }

        }
    }

    public void generateFunctionHeader() {
        for (EReference ref : cls.getEReferences()) {
            for (String methodName : new String[]{"Add", "Remove"}) {
                String type = ConverterDataTypes.getInstance().check_type(ref.getEReferenceType().getName());
                String name = HelperGenerator.genToUpperCaseFirstChar(ref.getName());
                String ptrName = "fptr" + cls.getName() + methodName + name;

                add_method_signature_H("typedef void (*" + ptrName + ")(" + cls.getName() + "*, " + type + "*);");
                add_virtual_table_H(ptrName + " add" + name + ";");
                add_class_virtual_table(cls.getName(), ptrName + " add" + name + ";");
            }
        }
    }

    public void generateSuperAndVTAttr() {
        if (cls.getESuperTypes().size() == 1)
            add_begin_virtual_table_H(cls.getESuperTypes().get(0).getName() + "_VT *super;");
        else if (cls.getESuperTypes().size() == 0)
            add_begin_virtual_table_H("KMFContainer_VT *super;");
        else
            System.err.println("Invalid number of parent in " + cls.getName());

        add_self_attribute_H("extern const " + cls.getName() + "_VT " +
                HelperGenerator.genToLowerCaseFirstChar(cls.getName()) + "_VT;");
    }

    /**
     * This method is a bit tricky to generate.
     * DeployUnit and TypeDefinition have their own implementation
     * If the only parent is KMFContainer then the key is generated_KMF_ID,
     * in all other cases the class inherit from NamedElement so we return
     * its name. Of course NamedElement have a specific implementation too.
     *
     * @see TemplateManager
     */
    public void generateInternalGetKey() {
        // Header signature is defined in the non-generated class KMFContainer
        String fun;
        VelocityContext context = new VelocityContext();
        StringWriter writer = new StringWriter();
        add_C("static char\n*" + cls.getName() + "_internalGetKey(" + cls.getName() + "* const this) {");
        if (cls.getName().equals("DeployUnit")) {
            Template method = TemplateManager.getInstance().getTp_getKey_DeployUnit();
            method.merge(context, writer);
            fun = writer.toString();
        } else if (cls.getName().equals("TypeDefinition")) {
            Template method = TemplateManager.getInstance().getTp_getKey_TypeDefinition();
            method.merge(context, writer);
            fun = writer.toString();
        } else if (cls.getName().equals("NamedElement")) {
            fun = "\treturn this->name;";
        } else if (cls.getESuperTypes().size() == 0) {
            fun = "\treturn this->generated_KMF_ID;";
        } else { // in all other cases we inherit from NamedElement
            fun = "\treturn instance_VT.internalGetKey((NamedElement*)this);";
        }
        add_C(fun);
        add_C("}\n");
    }

    public void generateFindByIdAttribute(EClass eClass, EReference ref) {
        String type = ref.getEReferenceType().getName();
        String name = HelperGenerator.genToUpperCaseFirstChar(ref.getName());

        // header
        String ptrName = "fptr" + eClass.getName() + "Find" + name + "ByID";
        add_method_signature_H("typedef " + type + "* (*" + ptrName + ")(" +
                eClass.getName() + "*, char*);");
        add_virtual_table_H(ptrName + " find" + name + "ByID;");
        add_class_virtual_table(cls.getName(), ptrName + " find" + name + "ByID;");

        // implementation
        VelocityContext context = new VelocityContext();
        StringWriter result = new StringWriter();
        context.put("type", type);
        context.put("refname", ref.getName());
        context.put("name", eClass.getName());
        TemplateManager.getInstance().getGen_find_by_id().merge(context, result);
        add_C(result.toString());
    }

    private void generateAttributes() {
        add_ATTRIBUTE(cls.getName() + "_VT *VT;");

        add_ATTRIBUTE("/* " + cls.getName() + " */");
        for (EAttribute eAttribute : cls.getEAttributes()) {
            String attr = ConverterDataTypes.getInstance().check_type(eAttribute.getEAttributeType().getName())
                    + " " + eAttribute.getName() + ";";
            add_class_attribute(cls.getName(), attr);
            add_init("this->" + eAttribute.getName() + " = " + HelperGenerator.
                    genDefaultValue(eAttribute.getEAttributeType().getName()) + ";");
            add_ATTRIBUTE(attr);
        }


        for (EReference ref : cls.getEReferences()) {
            String gen_type;

            gen_type = ConverterDataTypes.getInstance().check_type(ref.getEReferenceType().getName());

            if (ref.getUpperBound() == -1) {
                if (ref.getEReferenceType().getEIDAttribute() != null) {
                    String attr = "map_t " + ref.getName() + ";";
                    add_class_attribute(cls.getName(), attr);
                    add_ATTRIBUTE(attr);
                    add_CONSTRUCTOR(ref.getName() + " = NULL;");
                    generateFindByIdAttribute(cls, ref);
                } else {
                    System.err.println("NO ID " + ref.getName());
                }
            } else {
                // TODO implements shared_ptr to fix delete from other class
                add_ATTRIBUTE(gen_type + " *" + ref.getName() + ";");
                add_CONSTRUCTOR(ref.getName() + " = NULL;");
            }
        }
    }

    /**
     * Should be called after a whole model parsing
     */
    public void generateInheritedAttributes() {
        for (EClass c : this.cls.getEAllSuperTypes()) {
            if (classAttributes.containsKey(c.getName())) {
                add_begin_ATTRIBUTE(classAttributes.get(c.getName()).toString());
                add_begin_ATTRIBUTE("/* " + c.getName() + " */");
            }
        }
        //Every class inherit from KMFContainer
        add_begin_ATTRIBUTE("KMFContainer *eContainer;");
        add_begin_ATTRIBUTE("/* KMFContainer */");
    }

    public void generateInheritedVirtualTable() {
        for (EClass c : this.cls.getEAllSuperTypes()) {
            // Some classes don't have method
            if (classVirtualTable.containsKey(c.getName())) {
                add_begin_virtual_table_H(classVirtualTable.get(c.getName()).toString());
                add_begin_virtual_table_H("/* " + c.getName() + " */");
            }
        }
        //Every class inherit from KMFContainer
        VelocityContext context = new VelocityContext();
        StringWriter result = new StringWriter();
        TemplateManager.getInstance().getTp_KMFContainer_fptr().merge(context, result);
        add_begin_virtual_table_H(result.toString());
    }

    public void generateBeginHeader() {
        String name = cls.getName();
        add_begin_header(HelperGenerator.genIFDEF(name));

        add_begin_header("\n");
        add_begin_header(HelperGenerator.genInclude("string.h"));
        add_begin_header(HelperGenerator.genInclude("stdio.h"));

        // only greater than 0 value should be 1
        if (cls.getESuperTypes().size() > 0) {
            for (EClass super_eclass : cls.getESuperTypes())
                add_begin_header(HelperGenerator.genIncludeLocal(super_eclass.getName()));
        } else {
            add_begin_header(HelperGenerator.genIncludeLocal("KMFContainer"));
        }
    }

    public void link_generation() {
        // c file
        class_result.append(header);
        class_result.append("void init" + cls.getName() + "(" + cls.getName() + "* const this) {\n");
        class_result.append(init);
        class_result.append("}\n");
        class_result.append(body);

        // header file
        generateInheritedAttributes();
        generateInheritedVirtualTable();
        generateSuperAndVTAttr();
        header_result.append(begin_header.append("\n"));
        header_result.append(method_signature.append("\n"));

        header_result.append("typedef struct _" + this.className + "_VT {\n");
        header_result.append(virtual_table);
        header_result.append("} " + this.className + "_VT;\n\n");

        header_result.append("typedef struct _" + this.className + " {\n");
        header_result.append(attributes);
        header_result.append("} " + this.className + ";\n\n");
        header_result.append(self_attribute + "\n");
        header_result.append(HelperGenerator.genENDIF());
    }

    public void writeHeader() throws IOException {
        FileManager.writeFile(ctx.getPackageGenerationDirectory() +
                this.className + ".h", header_result.toString(), false);
    }

    public void writeClass() throws IOException {
        FileManager.writeFile(ctx.getPackageGenerationDirectory() +
                this.className + ".c", class_result.toString(), false);
    }

}
