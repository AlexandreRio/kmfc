package org.kevoree.modeling.c.generator.model;

import org.apache.velocity.VelocityContext;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.kevoree.modeling.c.generator.Generator;
import org.kevoree.modeling.c.generator.TemplateManager;
import org.kevoree.modeling.c.generator.utils.HelperGenerator;
import org.kevoree.modeling.c.generator.utils.ConverterDataTypes;
import org.kevoree.modeling.c.generator.GenerationContext;
import org.kevoree.modeling.c.generator.utils.FileManager;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Generator for a class of the meta-model
 * Call order are important
 * Some code is generated while parsing, some after the full parsing since
 * parents can be reached after child class.
 *
 * @see #link_generation() For final concatenation
 * @see Generator#generateModel()
 *
 * Initial code comment:
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 28/10/13
 * Time: 11:26
 * To change this templates use File | Settings | File Templates.
 */
public class ClassGenerator extends AGenerator {

    private EClass cls;

    public ClassGenerator(GenerationContext ctx){
        this.ctx = ctx;
    }

    /**
     * GLOBAL GENERATION METHOD
     */
    public void generateClass(EClass cls) throws IOException {
        this.cls = cls;
        initGeneration(cls.getName());
        generateBeginHeader(cls);
        generateVirtualTableComment();
        generateFunctionHeader(cls);
        generateInit(cls);
        generateAttributes(cls);
        //generateMethodAdd(cls);
        //generateMethodRemove(cls);
        generateGetterMetaClassName(cls);
        generateDeleteMethod(cls);
    }

    private void generateVirtualTableComment() {
        add_virtual_table_H("/* " + cls.getName() + " */");
    }

    private void generateDeleteMethod(EClass cls) {
        StringWriter result = new StringWriter();

        for(EReference ref :cls.getEAllReferences())
        {

            if(ref.getUpperBound() == -1 )
            {
                if(ref.isContainment()){
                    if(ref.getEReferenceType().getEIDAttribute() != null){
                        VelocityContext context = new VelocityContext();
                        context.put("refname",ref.getName());
                        context.put("type",ref.getEReferenceType().getName());

                        TemplateManager.getInstance().gen_destructor_ref.merge(context,result);
                    }
                }else {
                    add_DESTRUCTOR(ref.getName()+".clear();");
                }

            } else {
                if(ref.isContainment()){
                    result.append("if("+ref.getName()+" != NULL){\n")  ;
                    result.append("delete "+ref.getName()+";\n");
                    result.append(ref.getName()+"= NULL;");
                    result.append("}\n");
                }

            }


        }
        add_DESTRUCTOR(result.toString());
    }

    private void generateInit(EClass cls) {
        add_method_signature_H("void init" + cls.getName() + "(" + cls.getName() + "* const this);");
    }

    private void generateGetterMetaClassName(EClass cls) {
        add_C("static char* " + cls.getName() + "_metaClassName(" + cls.getName() + "* const this) {");
        add_C("\treturn \"" + cls.getName() + "\";");
        add_C("}");
    }

    private void generateMethodRemove(EClass cls) {

        for(EReference ref : cls.getEReferences()) {

            String type = ConverterDataTypes.getInstance().check_type(ref.getEReferenceType().getName());
            add_method_signature_H("void remove" + ref.getName() + "(" + type + " *ptr);");


            if(ref.getUpperBound() == -1) {
                if(ref.getEReferenceType().getEIDAttribute() == null) {
                    add_C("void " + cls.getName() + "::remove" + ref.getName() + "(" + type + " *ptr){");
                    add_C("delete ptr;");
                    add_C("}\n");
                }else {
                    VelocityContext context = new VelocityContext();
                    context.put("classname",cls.getName());
                    context.put("refname",ref.getName());
                    context.put("typeadd",type);
                    if(ref.isContainment())
                    {
                        context.put("isContainment","delete container;");  // TODO shared ptr
                    } else {
                        context.put("isContainment","");
                    }

                    StringWriter result = new StringWriter();
                    TemplateManager.getInstance().gen_method_remove.merge(context, result);

                    add_C(result.toString());
                }

            }  else {
                add_C("void " + cls.getName() + "::remove" + ref.getName() + "(" + type + " *ptr){");
                add_C("delete ptr;");
                add_C("}\n");
            }

        }
    }

    public void generateMethodAdd(EClass cls)
    {
        for(EReference ref : cls.getEReferences()) {
            String type = ConverterDataTypes.getInstance().check_type(ref.getEReferenceType().getName());
            add_method_signature_H("void add" + ref.getName() + "(" + type + " *ptr);");


            if(ref.getUpperBound() == -1) {
                if(ref.getEReferenceType().getEIDAttribute() == null) {
                    add_C("void " + cls.getName() + "::add" + ref.getName() + "(" + type + " *ptr){");
                    add_C(ref.getName() + ".push_back(ptr);");
                    add_C("}\n");
                }else
                {
                    VelocityContext context = new VelocityContext();
                    context.put("classname",cls.getName());
                    context.put("refname",ref.getName());
                    context.put("typeadd",type);


                    if(!ref.isContainment()){
                        context.put("iscontained","");
                    } else    {
                        StringBuilder iscontainer = new StringBuilder();
                        iscontainer.append("\tany ptr_any = container;\n");
                        iscontainer.append("\tRemoveFromContainerCommand  *cmd = new  RemoveFromContainerCommand(this,REMOVE,\"" + ref.getName() + "\",ptr_any);\n");
                        iscontainer.append("\tcontainer->setEContainer(this,cmd,\"" + ref.getName() + "\");\n");
                        context.put("iscontained", iscontainer.toString());
                    }




                    StringWriter result = new StringWriter();
                    TemplateManager.getInstance().gen_method_add.merge(context, result);
                    add_C(result.toString());
                }

            }  else
            {
                add_C("void " + cls.getName() + "::add" + ref.getName() + "(" + type + " *ptr){");

                if(ref.isContainment()){

                    StringBuilder iscontainer = new StringBuilder();
                    iscontainer.append("if("+ref.getName()+" != ptr ){\n");
                    iscontainer.append("if("+ref.getName()+" != NULL ){\n");
                    iscontainer.append(ref.getName()+"->setEContainer(NULL,NULL,\"\");\n");
                    iscontainer.append("}\n");
                    iscontainer.append("if(ptr != NULL ){\n");
                    iscontainer.append("ptr->setEContainer(this,NULL,\"" + ref.getName() + "\");\n");
                    iscontainer.append("}\n");


                    iscontainer.append(ref.getName()+" =ptr;\n");
                    iscontainer.append("}\n");
                    add_C(iscontainer.toString());
                }   else
                {
                    add_C(ref.getName() + " =ptr;\n");
                }
                add_C("}\n");
            }

        }
    }

    public void generateFunctionHeader(EClass cls) {
        //TODO refactor
        for (EReference ref : cls.getEReferences()) {
            String type = ConverterDataTypes.getInstance().check_type(ref.getEReferenceType().getName());
            String addName = HelperGenerator.genToUpperCaseFirstChar(ref.getName());
            String removeName = HelperGenerator.genToUpperCaseFirstChar(ref.getName());
            String ptrAddName    = "fptr" + cls.getName() + "Add" + addName;
            String ptrRemoveName = "fptr" + cls.getName() + "Remove" + removeName;

            add_method_signature_H("typedef void (*" + ptrAddName + ")(" + cls.getName() + "*, " + type + "*);");
            add_method_signature_H("typedef void (*" + ptrRemoveName + ")(" + cls.getName() + "*, " + type + "*);");
            add_virtual_table_H(ptrAddName + " add" + addName + ";");
            add_class_virtual_table(cls.getName(), ptrAddName + " add" + addName + ";");
            add_virtual_table_H(ptrRemoveName + " remove" + removeName + ";");
            add_class_virtual_table(cls.getName(), ptrRemoveName + " remove" + removeName + ";");
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

    //TODO
    public void generateinternalGetKey(EClass cls) {
        List<String> idsgen  = new ArrayList<String>();

        for(EAttribute a : cls.getEAllAttributes()){
            if(a.isID() && !a.getName().equals(HelperGenerator.internal_id_name)){
                idsgen.add(a.getName());
            }
        }

        List<String> eAttribute  = new ArrayList<String>();

        for(EAttribute a : cls.getEAllAttributes()){
            if(a.isID()  ){
                if(a.getName().equals(HelperGenerator.internal_id_name) && idsgen.size() ==0)
                {
                    eAttribute.add(a.getName());
                }
                else  if(!a.getName().equals(HelperGenerator.internal_id_name))
                {
                    eAttribute.add(a.getName());

                }
            }

        }

        // sort
        Collections.sort(eAttribute);


        if(eAttribute.size() >0){

            // todo type
            add_C("std::string " + cls.getName() + "::internalGetKey(){");
            class_result.append("return ");

            for(int i=0;i<eAttribute.size();i++){
                class_result.append(eAttribute.get(i));
                if(i < eAttribute.size()-1){
                    class_result.append("+\"/\"+");
                }
            }

            add_C(";");

            add_C("}");
        }else {
            System.err.println("KMF NEED ID "+cls.getName());
        }

        if(eAttribute.size() == 1  && idsgen.size() ==0){
            if (eAttribute.get(0).equals(HelperGenerator.internal_id_name)){
                // System.out.println("INTERNAL "+cls.getName());
                add_begin_header(HelperGenerator.genInclude("microframework/api/utils/Uuid.h"));
                add_CONSTRUCTOR(HelperGenerator.internal_id_name+"= Uuid::getSingleton().generateUUID();");
            }

        }
    }

    public void generateFindbyIdAttribute(EClass eClass, EReference ref) {
        String type = ref.getEReferenceType().getName();
        String name = HelperGenerator.genToUpperCaseFirstChar(ref.getName());

        // header
        String ptrName = "fptr" + eClass.getName() + "Find" + name + "ByID";
        add_method_signature_H("typedef " + type + "* (*" + ptrName + ")(" +
                eClass.getName() + "*, char*);");
        add_virtual_table_H(ptrName + " find" + name + "ByID;");
        add_class_virtual_table(cls.getName(), ptrName + " find" + name + "ByID;");

        // implementation
        add_C("static " + type + "\n* " + eClass.getName() + "Find" + name + "ByID(" + type + "* const this, char *id) {");

        add_C(type + " *value = NULL;");
        add_C("if(this->" + ref.getName() + " != NULL) {");
        add_C("\tif(hashmap_get(this->" + ref.getName() + ", id, (void**)(&value)) == MAP_OK) {");
        add_C("\t\treturn value;");
        add_C("\t} else {");
        add_C("\t\treturn NULL;");
        add_C("\t}");
        add_C("} else {");
        add_C("\treturn NULL;");
        add_C("}");

        add_C("} // end of find");
    }

    private void generateAttributes(EClass cls) {
        add_ATTRIBUTE(cls.getName() + "_VT *VT;");

        add_ATTRIBUTE("/* " + cls.getName() + " */");
        for( EAttribute eAttribute : cls.getEAttributes() ) {
            String attr = ConverterDataTypes.getInstance().check_type(eAttribute.getEAttributeType().getName())
                    + " " + eAttribute.getName() + ";";
            add_class_attribute(cls.getName(), attr);
            add_ATTRIBUTE(attr);
        }

        generateinternalGetKey(cls);

        for(EReference ref : cls.getEReferences()  ) {
            String gen_type;
            String type_ref = ref.getEReferenceType().getName();

            gen_type = ConverterDataTypes.getInstance().check_type(ref.getEReferenceType().getName());

            if(ref.getUpperBound() == -1) {
                if(ref.getEReferenceType().getEIDAttribute() != null) {
                    String attr = "map_t " + ref.getName() + ";";
                    add_class_attribute(cls.getName(), attr);
                    add_ATTRIBUTE(attr);
                    generateFindbyIdAttribute(cls, ref);
                } else {
                    System.err.println("NO ID " + ref.getName());
                }
            } else {
                // TODO implements shared_ptr to fix delete from other class
                add_ATTRIBUTE(gen_type + " *" + ref.getName() + ";");
                //add_CONSTRUCTOR(ref.getName()+"=NULL;");
            }
        }
    }

    /**
     * Should be called after a whole model parsing
     *
     */
    public void generateInheritedAttributes() {
        //Every class inherit from KMFContainer
        add_ATTRIBUTE("/* KMFContainer */");
        add_ATTRIBUTE("KMFContainer *eContainer;");
        for (EClass c : this.cls.getEAllSuperTypes()) {
            if (classAttributes.containsKey(c.getName())) {
                add_ATTRIBUTE("/* " + c.getName() + " */");
                add_ATTRIBUTE(classAttributes.get(c.getName()).toString());
            }
        }
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
        add_begin_virtual_table_H("fptrKMFMetaClassName metaClassName;");
        add_begin_virtual_table_H("fptrKMFInternalGetKey internalGetKey;");
        add_begin_virtual_table_H("fptrKMFGetPath getPath;");
        add_begin_virtual_table_H("fptrVisit visit;");
        add_begin_virtual_table_H("fptrFindByPath findByPath;");
        add_begin_virtual_table_H("fptrDelete delete;");
        // reverse order for the insertion, so comment at the end
        add_begin_virtual_table_H("/* KMFContainer */");
    }

    public void generateBeginHeader(EClass cls) {
        String name =   cls.getName();
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
