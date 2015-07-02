package org.kevoree.modeling.c.generator.model;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
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


    private Template gen_method_add;
    private Template gen_method_remove;
    private Template gen_visitor;
    private Template gen_visitor_ref;
    private Template gen_destructor_ref;

    public ClassGenerator(GenerationContext   ctx){
        this.ctx = ctx;
        ve.setProperty("file.resource.loader.class", ClasspathResourceLoader.class.getName()) ;
        ve.init();
        gen_method_add      = ve.getTemplate("templates/add_method.vm");
        gen_method_remove   = ve.getTemplate("templates/remove_method.vm");
        gen_visitor         = ve.getTemplate("templates/visitor.vm");
        gen_visitor_ref     = ve.getTemplate("templates/visitor_ref.vm");
        gen_destructor_ref  = ve.getTemplate("templates/destructor_ref.vm");
    }


    /*
        GLOBAL GENERATION METHOD

     */
    public void generateClass(EClass cls) throws IOException {

        initGeneration();
        generateBeginClassHeader(cls);
        generateAttributes(cls);
        generateMethodAdd(cls);
        generateMethodRemove(cls);
        generateGettermetaClassName(cls);
        //generateFlatReflexiveSetters(cls);
        //generateFindById(cls);
        //generateVisitor(cls);
        //generateVisitorAttribute(cls);
        generateDeleteMethod(cls);
        generateConstructorMethod(cls);
        //generateDestructorMethod(cls);
        link_generation();
        generateEndClass(cls);
        writeHeader(cls);

        FileManager.writeFile(ctx.getPackageGenerationDirectory() + cls.getName() + ".c", class_result.toString(), true);
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

                        gen_destructor_ref.merge(context,result);
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

    private void generateGettermetaClassName(EClass cls) {
        add_C("static char* " + cls.getName() + "_metaClassName(" + cls.getName() + "* const this) {");
        add_C("\treturn \"" + cls.getName() + "\";");
        add_C("}");
    }


    private void generateFindById(EClass cls)
    {


        Boolean add = true;
        Boolean end = false;
        for(EReference ref :cls.getEAllReferences())
        {


            if(add)
            {
                add_H("KMFContainer* findByID(string relationName,string idP);");
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

        add_H("void reflexiveMutator(int ___mutatorType,string ___refName, any ___value, bool ___setOpposite,bool ___fireEvent );");


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


    private void generateVisitor(EClass cls){

        add_H("void visit(ModelVisitor *visitor,bool recursive,bool containedReference ,bool nonContainedReference);");

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
                    gen_visitor_ref.merge(context_visitor_ref,result_visitor_ref_contained);
                }      else {
                    gen_visitor_ref.merge(context_visitor_ref,result_visitor_ref_non_contained);
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
        context_visitor.put("visitor_refs_non_contained",result_visitor_ref_non_contained);

        if(ctx.isDebug_model()){
            context_visitor.put("debug",msg_DEBUG(cls,"Visiting class "+cls.getName()));
        }else {
            context_visitor.put("debug","");
        }
        gen_visitor.merge(context_visitor, result_visitor);

        add_C(result_visitor.toString());
    }

    private void generateVisitorAttribute(EClass cls)
    {
        add_H("void visitAttributes(ModelAttributeVisitor *visitor);");

        add_C("void " + cls.getName() + "::visitAttributes(ModelAttributeVisitor *visitor){");

        for(EAttribute a: cls.getEAllAttributes())
        {
            ADD_DEBUG(cls,"Visiting attribute -> "+a.getName());
            add_C("visitor->visit(any(" + a.getName() + "),\"" + a.getName() + "\",this);");

        }

        add_C("}");
    }

    private void generateMethodRemove(EClass cls) {

        for(EReference ref : cls.getEReferences()  ){

            String type = ConverterDataTypes.getInstance().check_type(ref.getEReferenceType().getName());
            add_H("void remove"+ref.getName()+"("+type+" *ptr);");


            if(ref.getUpperBound() == -1)
            {
                if(ref.getEReferenceType().getEIDAttribute() == null)
                {
                    add_C("void " + cls.getName() + "::remove" + ref.getName() + "(" + type + " *ptr){");
                    add_C("delete ptr;");
                    add_C("}\n");
                }else
                {
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
                    gen_method_remove.merge(context, result);

                    add_C(result.toString());
                }

            }  else
            {
                add_C("void " + cls.getName() + "::remove" + ref.getName() + "(" + type + " *ptr){");
                add_C("delete ptr;");
                add_C("}\n");
            }

        }
    }


    public void generateMethodAdd(EClass cls)
    {
        for(EReference ref : cls.getEReferences()  ){
            String type = ConverterDataTypes.getInstance().check_type(ref.getEReferenceType().getName());
            add_H("void add"+ref.getName()+"("+type+" *ptr);");


            if(ref.getUpperBound() == -1)
            {
                if(ref.getEReferenceType().getEIDAttribute() == null)
                {
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
                    gen_method_add.merge(context, result);
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

    public void generateinternalGetKey(EClass cls)
    {
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
            add_H("std::string internalGetKey();");
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
                add_HEADER(HelperGenerator.genInclude("microframework/api/utils/Uuid.h"));
                add_CONSTRUCTOR(HelperGenerator.internal_id_name+"= Uuid::getSingleton().generateUUID();");


            }

        }
    }

    public void generateFindbyIdAttribute(EClass eClass,EReference ref){

        String type = ref.getEReferenceType().getName();
        String name = ref.getName();


        add_H(type + " *find" + name + "ByID(std::string id);");

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



    private void generateAttributes(EClass cls){


        add_PUBLIC_ATTRIBUTE("public:\n");

        for( EAttribute eAttribute : cls.getEAttributes() )
        {
            add_PUBLIC_ATTRIBUTE(ConverterDataTypes.getInstance().check_type(eAttribute.getEAttributeType().getName()));
            add_PUBLIC_ATTRIBUTE(" "+eAttribute.getName()+";\n");


        }

        generateinternalGetKey(cls);


        for(EReference ref : cls.getEReferences()  ){

            String gen_type;
            String type_ref = ref.getEReferenceType().getName();

            if(ref.getEReferenceType().getEAllReferences().contains(cls)){
                // cycle dependency
                if(!type_ref.equals(cls.getName())){
                    header.append("class "+type_ref+";\n\n");
                }

            }else {
                //  System.out.println(type_ref);
                if(!type_ref.equals(cls.getName())){
                    //   header.append(  HelperGenerator.genIncludeLocal(type_ref));
                    header.append("class "+type_ref+";\n\n");
                }

                //
            }





            gen_type = ConverterDataTypes.getInstance().check_type(ref.getEReferenceType().getName());


            if(ref.getUpperBound() == -1)
            {
                if(ref.getEReferenceType().getEIDAttribute() != null)
                {

                    add_PUBLIC_ATTRIBUTE("std::map<string,"+gen_type+"*> "+ref.getName()+"; \n") ;
                    //  add_CONSTRUCTOR(ref.getName() + ".set_empty_key(\"\");");
                    generateFindbyIdAttribute(cls, ref);
                }  else
                {

                    System.err.println("NO ID "+ref.getName());
                }

            }else
            {
                // TODO implements shared_ptr to fix delete from other class
                add_PUBLIC_ATTRIBUTE(gen_type+" *"+ref.getName()+"; \n");
                add_CONSTRUCTOR(ref.getName()+"=NULL;");
            }

        }


    }


    public void generateBeginClassHeader(EClass cls)
    {


        String name =   cls.getName();
        add_HEADER(HelperGenerator.genIFDEF(name));
        //  add_CPP(HelperGenerator.genIncludeLocal(name));

        add_HEADER(HelperGenerator.genInclude("string"));
        add_HEADER(HelperGenerator.genInclude("stdio"));

        if(cls.getESuperTypes().size() >0)
        {
            gen_class.append("class "+name+" : ");  // FIX
        } else {
            add_HEADER(HelperGenerator.genInclude("microframework/api/container/KMFContainerImpl.h"));
            gen_class.append("class "+name+" : public KMFContainerImpl");
        }

        boolean first = true;
        int i=0;


        for(EClass super_eclass : cls.getESuperTypes() )
        {
            /*
            TODO FIX ME CHECK DIAMANT ISSUE  ADD VIRTUAL ON ONE C++ CLASS
             */

            add_HEADER(HelperGenerator.genIncludeLocal(super_eclass.getName()));
            //    add_HEADER("class "+super_eclass.getName()+"\n");

            // implements
            gen_class.append("public " + super_eclass.getName());


            if(first && i <cls.getESuperTypes().size()-1 ) {
                gen_class.append(",");
            }


            i++;
        }

        gen_class.append("{ \n");
    }


    public void generateEndClass(EClass cls){
        api_result.append("}; // END CLASS \n");
        api_result.append(HelperGenerator.genENDIF());
    }





    public void writeHeader(EClass cls) throws IOException {
        // WRITE
        FileManager.writeFile(ctx.getPackageGenerationDirectory()+cls.getName()+".h", api_result.toString(),false);
    }




}
