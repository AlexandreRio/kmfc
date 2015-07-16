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

        //TODO organize function call and identify header and implementation
        generateBeginHeader();
        generateVirtualTableComment();
        generateInternalGetKey();
        generateFunctionHeader();
        generateLinkedClassTypeDef();
        generateInit();
        generateAttributes();
        generateMethodAdd();
        generateMethodRemove();
        generateGetterMetaClassName();
        generateInitVT();
        generateMethodNew();
        generateMethodDelete();
    }

    private void generateLinkedClassTypeDef() {
        add_begin_header("typedef struct _" + cls.getName() + " " + cls.getName() + ";\n");
        for (String t : typeToDef) {
            add_begin_header(HelperGenerator.genIncludeLocal(t));
            add_begin_header("typedef struct _" + t + " " + t + ";\n");
        }
    }

    private void generateVirtualTableComment() {
        add_virtual_table_H("/* " + cls.getName() + " */");
    }

    private void generateMethodDelete() {
        add_C("static void\ndelete" + cls.getName() + "(" + cls.getName() + "* const this) {");

        // call parent delete method
        if (cls.getESuperTypes().size() == 1) {
            String parentType = cls.getEAllSuperTypes().get(0).getName();
            add_C("\tvt_" + parentType + ".delete((" + parentType
                    + "*)this);\n");
        } else if (cls.getEAllSuperTypes().size() == 0) {
            add_C("\tVT_KMF.delete((KMFContainer*)this);\n");
        } else {
            System.err.println("Invalid number of parent for " + cls.getName());
        }

        VelocityContext context;
        StringWriter result;
        // delete self attributes
        for (EReference ref : cls.getEAllReferences()) {
            if (ref.getUpperBound() == -1) {
                if (ref.getEReferenceType().getEIDAttribute() != null) {
                    context = new VelocityContext();
                    result = new StringWriter();
                    context.put("refname", ref.getName());
                    if (ref.isContainment())
                        context.put("iscontained", "deleteContainerContents(this->" + ref.getName() + ");");
                    else
                        context.put("iscontained", "");

                    TemplateManager.getInstance().getGen_delete_ref().merge(context, result);
                    add_C(result.toString());
                }
            }
            //nothing to do for unary link ?

        }
        add_C("}");
    }

    private void generateInit() {
        // implementation
        String parentType = "";
        if (cls.getESuperTypes().size() == 1)
            parentType = cls.getEAllSuperTypes().get(0).getName();
        else if (cls.getEAllSuperTypes().size() == 0)
            parentType = "KMFContainer";
        else
            System.err.println("Invalid number of parent for " + cls.getName());

        add_init("\tinit" + parentType + "((" + parentType + "*)this);");

        // if parent is KMFContainer and the class isn't NamedElement, we generate a KMF_ID
        if (cls.getEAllSuperTypes().size() == 0 && !cls.getName().equals("NamedElement")) {
            add_init("\tmemset(&this->generated_KMF_ID[0], 0, sizeof(this->generated_KMF_ID));\n" +
                    "\trand_str(this->generated_KMF_ID, 8);");
            add_ATTRIBUTE("char generated_KMF_ID[9];");
        }
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
                    context.put("classname", cls.getName());

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
            if (!typeToDef.contains(ref.getEType().getName()))
                typeToDef.add(ref.getEType().getName());

            for (String methodName : new String[]{"Add", "Remove"}) {
                String type = ConverterDataTypes.getInstance().check_type(ref.getEReferenceType().getName());
                String name = HelperGenerator.genToUpperCaseFirstChar(ref.getName());
                String ptrName = "fptr" + cls.getName() + methodName + name;

                add_method_signature_H("typedef void (*" + ptrName + ")(" + cls.getName() + "*, " + type + "*);");
                add_virtual_table_H(ptrName + " " + HelperGenerator.genToLowerCaseFirstChar(methodName) + name + ";");
                add_class_virtual_table(cls.getName(), ptrName + " " + HelperGenerator.genToLowerCaseFirstChar(methodName) + name + ";");
            }
        }
    }

    public void generateSuperAndVTAttr() {
        if (cls.getESuperTypes().size() == 1)
            add_begin_virtual_table_H("VT_" + cls.getESuperTypes().get(0).getName() + " *super;");
        else if (cls.getESuperTypes().size() == 0)
            add_begin_virtual_table_H("VT_KMFContainer *super;");
        else
            System.err.println("Invalid number of parent in " + cls.getName());

        add_self_attribute_H("extern const VT_" + cls.getName() + " " +
                "vt_" + cls.getName() + ";");
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
            add_ATTRIBUTE("char *internalKey;");
        } else if (cls.getName().equals("TypeDefinition")) {
            Template method = TemplateManager.getInstance().getTp_getKey_TypeDefinition();
            method.merge(context, writer);
            fun = writer.toString();
            add_ATTRIBUTE("char *internalKey;");
        } else if (cls.getName().equals("NamedElement")) {
            fun = "\treturn this->name;";
        } else if (cls.getESuperTypes().size() == 0) {
            fun = "\treturn this->generated_KMF_ID;";
        } else { // in all other cases we inherit from NamedElement
            fun = "\treturn vt_" + cls.getESuperTypes().get(0).getName()
                    + ".internalGetKey((" + cls.getESuperTypes().get(0).getName() + "*)this);";
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
        context.put("type", cls.getName());
        context.put("refname", ref.getName());
        context.put("majrefname", HelperGenerator.genToUpperCaseFirstChar(ref.getName()));
        context.put("name", eClass.getName());
        TemplateManager.getInstance().getGen_find_by_id().merge(context, result);
        add_C(result.toString());
    }

    private void generateAttributes() {
        add_ATTRIBUTE("VT_" + cls.getName() + " *VT;");

        add_ATTRIBUTE("/* " + cls.getName() + " */");
        for (EAttribute eAttribute : cls.getEAttributes()) {
            // we have already assigned this attribute
            if (!eAttribute.getName().equals("generated_KMF_ID")) {
                String attr = ConverterDataTypes.getInstance().check_type(eAttribute.getEAttributeType().getName())
                        + " " + eAttribute.getName() + ";";
                add_class_attribute(cls.getName(), attr);
                add_init("\tthis->" + eAttribute.getName() + " = " + HelperGenerator.
                        genDefaultValue(eAttribute.getEAttributeType().getName()) + ";");
                add_ATTRIBUTE(attr);
            }
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
        add_C("#include \"" + name + ".h\"\n");

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

    /**
     * @see #generateSuperAndVTAttr()
     */
    private void generateInitVT() {
        //TODO only add self methods to a table
        String initParent = "";
        if (cls.getESuperTypes().size() == 1)
            initParent = "\t.super = &vt_" + cls.getESuperTypes().get(0).getName() + ",";
        else if (cls.getESuperTypes().size() == 0)
            initParent = ".super = &VT_KMF,";
        initVT.append(initParent + "\n");
    }

//    private void generateInheritedInitVT() {
//        for (EClass parent : cls.getEAllSuperTypes())
//            initVT.append(classInitVT.get(parent.getName()));
//    }

    private void generateMethodNew() {
        if (!cls.isAbstract()) {
            add_method_signature_H(cls.getName() + "* new_" + cls.getName() + "(void);");
            VelocityContext context = new VelocityContext();
            context.put("classname", cls.getName());
            context.put("vtName", "vt_" + cls.getName());
            StringWriter result = new StringWriter();
            TemplateManager.getInstance().getGen_method_new().merge(context, result);
            add_new(result.toString());
        }
    }

    public void link_generation() {
        // c file
        class_result.append(header);
        class_result.append("void init" + cls.getName() + "(" + cls.getName() + "* const this) {\n");
        class_result.append(init);
        class_result.append("}\n");
        class_result.append(body);
        add_C("const VT_" + cls.getName()
                + " vt_" + cls.getName() + " = {");
        class_result.append(initVT);
        add_C("};");
        class_result.append(newmethod);

        // header file
        generateInheritedAttributes();
        generateInheritedVirtualTable();
        generateSuperAndVTAttr();
        header_result.append(begin_header.append("\n"));
        header_result.append(method_signature.append("\n"));

        header_result.append("typedef struct _VT_" + this.className + " {\n");
        header_result.append(virtual_table);
        header_result.append("} VT_" + this.className + ";\n\n");

        header_result.append("typedef struct _" + this.className + " {\n");
        header_result.append(attributes);
        header_result.append("} " + this.className + ";\n\n");
        header_result.append(self_attribute + "\n");
        header_result.append("void init" + cls.getName() + "(" + cls.getName() + "* const this);\n");
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
