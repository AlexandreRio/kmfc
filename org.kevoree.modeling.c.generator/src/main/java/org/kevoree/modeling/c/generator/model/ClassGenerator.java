package org.kevoree.modeling.c.generator.model;

import org.apache.velocity.VelocityContext;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
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
        generateFunctionHeader(cls);
        generateInit(cls);
        generateAttributes(cls);
        //generateMethodAdd(cls);
        //generateMethodRemove(cls);
        generateGetterMetaClassName(cls);
        generateDeleteMethod(cls);
        generateVirtualTable(cls);
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


    private void generateFindById(EClass cls) {
        Boolean add = true;
        Boolean end = false;
        for(EReference ref :cls.getEAllReferences())
        {


            if(add)
            {
                add_method_signature_H("KMFContainer* findByID(string relationName,string idP);");
                add_C("KMFContainer* " + cls.getName() + "::findByID(string relationName,string idP){");

                if(ctx.isDebug_model())
                {
                    add_C("LOGGER_WRITE(Logger::DEBUG_MODEL,\"REQUEST findByID " + cls.getName() + " \" + relationName + \" \" + idP);");
                }
                add=false;
                end = true;

            }

            if(ref.getUpperBound() == -1 )
            {
                if(ref.getEReferenceType().getEIDAttribute() != null)
                {
                    add_C("if(relationName.compare(\"" + ref.getName() + "\")== 0){");

                    add_C("return (KMFContainer*)find" + ref.getName() + "ByID(idP);");
                    add_C("}\n");
                }
            } else
            {
                add_C("if(relationName.compare(\"" + ref.getName() + "\")== 0){");
                // TODO MAYBE CHECK match
                add_C("return " + ref.getName() + ";");

                add_C("}\n");
            }


        }



        if(end){
            if(ctx.isDebug_model()){
                add_C("LOGGER_WRITE(Logger::DEBUG_MODEL,\"END -- findByID " + cls.getName() + "\");");
            }
            add_C("return NULL;\n");
            add_C("}\n");

        }
    }


    private void generateFlatReflexiveSetters(EClass eClass) {

        add_method_signature_H("void reflexiveMutator(int ___mutatorType,string ___refName, any ___value, bool ___setOpposite,bool ___fireEvent );");


        add_C("void " + eClass.getName() + "::reflexiveMutator(int ___mutatorType,string ___refName, any ___value, bool ___setOpposite,bool ___fireEvent){");
        if(ctx.isDebug_model())
        {
            add_C("LOGGER_WRITE(Logger::DEBUG_MODEL,\"BEGIN -- reflexiveMutator " + eClass.getName() + " refName \" + ___refName); ");
        }


        int i=0;
        for(EAttribute a :  eClass.getEAllAttributes()){

            if(i==0){
                add_C("if(___refName.compare(\"" + a.getName() + "\")==0){");
            }else {
                add_C("} else if(___refName.compare(\"" + a.getName() + "\")==0){");
            }

            String type = ConverterDataTypes.getInstance().check_type(a.getEAttributeType().getName());

            if(type.contains("string")){
                add_C(a.getName() + "= AnyCast<string>(___value);");
            } else if(type.contains("short")){
                add_C("short f;");
                add_C("Utils::from_string<short>(f, AnyCast<string>(___value), std::dec);");
                add_C(a.getName() + "= f;");
            } else if(type.contains("int")){
                add_C("int f;");
                add_C("Utils::from_string<int>(f, AnyCast<string>(___value), std::dec);");
                add_C(a.getName() + "= f;");
            } else if(type.contains("bool")){
                add_C("if(AnyCast<string>(___value).compare(\"true\") == 0){");
                add_C(a.getName() + "= true;");

                add_C("}else { ");
                add_C(a.getName() + "= false;");
                add_C("}");

            }
            i++;
        }
        if(i> 0){
            add_C("}else {\n");
        }
        int j=0;
        for(EReference a :  eClass.getEAllReferences())
        {
            if(j==0){
                add_C("if(___refName.compare(\"" + a.getName() + "\")==0){");
            }else {
                add_C("} else if(___refName.compare(\"" + a.getName() + "\")==0){");
            }

            add_C("if(___mutatorType ==ADD){");
            add_C("add" + a.getName() + "((" + a.getEType().getName() + "*)AnyCast<KMFContainer*>(___value));");
            add_C("}else if(___mutatorType == REMOVE){");
            add_C("remove" + a.getName() + "((" + a.getEType().getName() + "*)AnyCast<" + a.getEType().getName() + "*>(___value));");
            add_C("}");
            j++;
        }
        if(j> 0){
            add_C("}\n");
        }
        if(i> 0){
            add_C("}\n");
        }
        if(ctx.isDebug_model()){
            add_C("LOGGER_WRITE(Logger::DEBUG_MODEL,\"END -- reflexiveMutator " + eClass.getName() + " refName \" + ___refName); ");
        }

        add_C("}\n");
    }


    private void generateVisitor(EClass cls) {

        add_method_signature_H("void visit(ModelVisitor *visitor,bool recursive,bool containedReference ,bool nonContainedReference);");

        StringWriter result_visitor_ref_contained = new StringWriter();
        StringWriter result_visitor_ref_non_contained= new StringWriter();

        for(EReference ref :cls.getEAllReferences())
        {
            String type = ref.getEReferenceType().getName();
            String refname =   ref.getName();


            if((ref.getUpperBound() == -1  | ref.getUpperBound() == 0 )&& ref.getEReferenceType().getEIDAttribute() != null)
            {


                VelocityContext context_visitor_ref = new VelocityContext();
                context_visitor_ref.put("refname",refname);
                context_visitor_ref.put("type",type);
                if(ctx.isDebug_model()){
                    context_visitor_ref.put("debug",msg_DEBUG(cls,"Visiting  \"+ current->path()+ \""));
                }else {
                    context_visitor_ref.put("debug","");
                }


                if(ref.isContainment()){
                    TemplateManager.getInstance().gen_visitor_ref.merge(context_visitor_ref,result_visitor_ref_contained);
                }      else {
                    TemplateManager.getInstance().gen_visitor_ref.merge(context_visitor_ref,result_visitor_ref_non_contained);
                }


            } else if(ref.getUpperBound() == 1){
                //
                //System.out.println(cls.getName()+" "+ref.getName()+" "+ref.getUpperBound());
                if(ref.isContainment()){
                    result_visitor_ref_contained.append("visitor->beginVisitRef(\""+refname+"\",\"org.kevoree."+type+"\");\n");
                    result_visitor_ref_contained.append("internal_visit(visitor,"+refname+",recursive,containedReference,nonContainedReference,\""+refname+"\");\n");
                    result_visitor_ref_contained.append("visitor->endVisitRef(\""+refname+"\");\n");


                }      else {
                    //result_visitor_ref_non_contained
                    result_visitor_ref_non_contained.append("visitor->beginVisitRef(\""+refname+"\",\"org.kevoree."+type+"\");\n");
                    result_visitor_ref_non_contained.append("internal_visit(visitor,"+refname+",recursive,containedReference,nonContainedReference,\""+refname+"\");\n");
                    result_visitor_ref_non_contained.append("visitor->endVisitRef(\""+refname+"\");\n");


                }
            }

        }

        VelocityContext context_visitor = new VelocityContext();
        StringWriter result_visitor = new StringWriter();
        context_visitor.put("classname",cls.getName());
        context_visitor.put("visitor_refs_contained",result_visitor_ref_contained);
        context_visitor.put("visitor_refs_non_contained", result_visitor_ref_non_contained);

        if(ctx.isDebug_model()){
            context_visitor.put("debug",msg_DEBUG(cls,"Visiting class "+cls.getName()));
        }else {
            context_visitor.put("debug", "");
        }
        TemplateManager.getInstance().gen_visitor.merge(context_visitor, result_visitor);

        add_C(result_visitor.toString());
    }

    private void generateVisitorAttribute(EClass cls) {
        add_method_signature_H("void visitAttributes(ModelAttributeVisitor *visitor);");

        add_C("void " + cls.getName() + "::visitAttributes(ModelAttributeVisitor *visitor){");

        for(EAttribute a: cls.getEAllAttributes())
        {
            ADD_DEBUG(cls,"Visiting attribute -> "+a.getName());
            add_C("visitor->visit(any(" + a.getName() + "),\"" + a.getName() + "\",this);");

        }

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

            add_method_signature_H("typedef void (*"+ ptrAddName + ")(" + cls.getName() + "*, " + type + "*);");
            add_method_signature_H("typedef void (*"+ ptrRemoveName + ")(" + cls.getName() + "*, " + type + "*);");
            add_virtual_table_H("\t" + ptrAddName + " add" + addName + ";");
            add_class_virtual_table(cls.getName(), "\t" + ptrAddName + " add" + addName + ";");
            add_virtual_table_H("\t" + ptrRemoveName + " remove" + removeName + ";");
            add_class_virtual_table(cls.getName(), "\t" + ptrRemoveName + " remove" + removeName + ";");
        }
    }

    public void generateVirtualTable(EClass cls) {
        //TODO .c file
        // C part


        // Header part
        if (cls.getESuperTypes().size() == 1)
            add_virtual_table_H("\t" + cls.getESuperTypes().get(0).getName() + "_VT *super;");
        else if (cls.getESuperTypes().size() == 0)
            add_virtual_table_H("\tKMFContainer_VT *super;");
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
            add_method_signature_H("std::string internalGetKey();");
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


        String ptrName = "fptr" + eClass.getName() + "Find" + name + "ByID";
        add_method_signature_H("typedef " + type + "* (*" + ptrName + ")(" +
                eClass.getName() + "*, char*);");
        add_virtual_table_H("\t" + ptrName + " find" + name + "ByID;");
        add_class_virtual_table(cls.getName(), "\t" + ptrName + " find" + name + "ByID;");

        add_C(type + "* " + eClass.getName() + "::find" + name + "ByID(std::string id){");
        if(ctx.isDebug_model()){
            add_C("LOGGER_WRITE(Logger::DEBUG_MODEL,\"REQUEST -- findByID " + ref.getName() + " => \"+id);");
        }

        add_C("if(" + ref.getName() + ".find(id) != " + ref.getName() + ".end())");
        add_C("{");
        add_C("return " + ref.getName() + "[id];");
        add_C("}else { return NULL; }");

        add_C("}");
    }

    private void generateAttributes(EClass cls) {
        add_ATTRIBUTE(cls.getName() + "_VT *VT;");

        add_ATTRIBUTE("/*\n * " + cls.getName() + "\n */");
        for( EAttribute eAttribute : cls.getEAttributes() ) {
            String attr = ConverterDataTypes.getInstance().check_type(eAttribute.getEAttributeType().getName())
                    + " " + eAttribute.getName() + ";";
            add_class_attribute(cls.getName(), attr);
            add_ATTRIBUTE(attr);
        }

        generateinternalGetKey(cls);

        for(EReference ref : cls.getEReferences()  ){
            String gen_type;
            String type_ref = ref.getEReferenceType().getName();

            if(ref.getEReferenceType().getEAllReferences().contains(cls)){
                // cycle dependency
                if(!type_ref.equals(cls.getName()))
                    header.append("class "+type_ref+";\n\n");
            }else {
                //  System.out.println(type_ref);
                if(!type_ref.equals(cls.getName())){
                    //   header.append(  HelperGenerator.genIncludeLocal(type_ref));
                    header.append("class "+type_ref+";\n\n");
                }
            }
            gen_type = ConverterDataTypes.getInstance().check_type(ref.getEReferenceType().getName());

            if(ref.getUpperBound() == -1)
            {
                if(ref.getEReferenceType().getEIDAttribute() != null)
                {
                    String attr = "map_t " + ref.getName() + ";";
                    add_class_attribute(cls.getName(), attr);
                    add_ATTRIBUTE(attr); ;
                    generateFindbyIdAttribute(cls, ref);
                }  else
                {
                    System.err.println("NO ID "+ref.getName());
                }

            }else
            {
                // TODO implements shared_ptr to fix delete from other class
                add_ATTRIBUTE(gen_type+" *"+ref.getName()+";");
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
        add_ATTRIBUTE("/*\n * KMFContainer\n */");
        add_ATTRIBUTE("KMFContainer *eContainer;");
        for (EClass c : this.cls.getEAllSuperTypes()) {
            if (classAttributes.containsKey(c.getName())) {
                add_ATTRIBUTE("/*\n * " + c.getName() + "\n */");
                add_ATTRIBUTE("" + classAttributes.get(c.getName()).toString());
            }
        }
    }

    //TODO remove \t char and add it to the helper
    public void generateInheritedVirtualTable() {
        //Every class inherit from KMFContainer
        add_virtual_table_H("\t/*\n\t * KMFContainer\n\t */");
        add_virtual_table_H("\tfptrKMFMetaClassName metaClassNameb");
        add_virtual_table_H("\tfptrKMFInternalGetKey internalGetKey");
        add_virtual_table_H("\tfptrKMFGetPath getPath");
        add_virtual_table_H("\tfptrVisit visit");
        add_virtual_table_H("\tfptrFindByPath findByPath");
        add_virtual_table_H("\tfptrDelete delete");
        for (EClass c : this.cls.getEAllSuperTypes()) {
            // Some classes don't have method
            if (classVirtualTable.containsKey(c.getName())) {
                add_virtual_table_H("\t/*\n\t * " + c.getName() + "\n\t */");
                add_virtual_table_H("\t" + classVirtualTable.get(c.getName()).toString());
            }
        }
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

    public void writeHeader() throws IOException {
        FileManager.writeFile(ctx.getPackageGenerationDirectory() +
                this.className + ".h", header_result.toString(), false);
    }

    public void writeClass() throws IOException {
        FileManager.writeFile(ctx.getPackageGenerationDirectory() +
                this.className + ".c", class_result.toString(), false);
    }

}
