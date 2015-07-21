package org.kevoree.modeling.c.generator.model;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.kevoree.modeling.c.generator.GenerationContext;
import org.kevoree.modeling.c.generator.utils.ConverterDataTypes;
import org.kevoree.modeling.c.generator.utils.FileManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Classifier {
    private String name;
    private String superClass;
    private boolean isAbstract;
    private List<Variable> variables;
    private List<Function> functions;

    public Classifier(String name, String sClass, boolean isAbstract) {
        this.name = name;
        this.superClass = sClass;
        this.isAbstract = isAbstract;
        this.variables = new ArrayList<Variable>();
        this.functions = new ArrayList<Function>();
    }

    public static Classifier createFromEClass(EClass cls) {
        String sType = null;
        if (cls.getESuperTypes().size() > 0)
            sType = cls.getESuperTypes().get(0).getName();

        Classifier c = new Classifier(cls.getName(), sType, cls.isAbstract());

        c.createAttributes(cls);


//        c.createFunction(cls);
        return c;
    }

    private void createAttributes(EClass cls) {
        for (EAttribute attr : cls.getEAttributes()) {
            String t = ConverterDataTypes.getInstance().check_type(attr.getEAttributeType().getName());
            this.addVariable(new Variable(attr.getName(), t, Variable.LinkType.PRIMITIVE, false));
        }

        for (EReference ref : cls.getEReferences()) {
            int bound = ref.getUpperBound();
            Variable.LinkType lt = Variable.LinkType.UNARY_LINK;
            if (bound == -1)
                lt = Variable.LinkType.MULTIPLE_LINK;

            String t = ConverterDataTypes.getInstance().check_type(ref.getEReferenceType().getName());
            this.addVariable(new Variable(ref.getName(), t, lt, ref.isContainment()));
        }

        if (cls.getESuperTypes().size() == 1)
            this.superClass = cls.getEAllSuperTypes().get(0).getName();
        else if (cls.getEAllSuperTypes().size() == 0)
            this.superClass = "KMFContainer";

        if (!this.name.equals("NamedElement") && !this.superClass.equals("KMFContainer"))
            this.addVariable(new Variable("generated_KMF_ID", "char*", Variable.LinkType.UNARY_LINK, false ));
    }

    private void createFunction(EClass cls) {
        this.createMetaClassNameFunction();
        this.createInitFunction();
    }

    private void createMetaClassNameFunction() {
        String metaClassNameSignature = "static char* " + this.name
                + "_metaClassName(" + this.name + "* const this)";
        String metaClassNameBody = "\treturn \"" + this.name + "\";";
        Function metaClassFunction = new Function(true, metaClassNameSignature);
        metaClassFunction.setBody(metaClassNameBody);
        this.addFunction(metaClassFunction);
    }

    private void createInitFunction() {
        String initSignature = "";
        String initBody = "\tinit" + this.superClass + "((" + this.superClass
                + "*)this);";
        if (!this.name.equals("NamedElement") && !this.superClass.equals("KMFContainer"))
            initBody += "\tmemset(&this->generated_KMF_ID[0], 0, sizeof(this->generated_KMF_ID));\n" +
                    "\trand_str(this->generated_KMF_ID, 8);";
    }

    private void addVariable(Variable var) {
        this.variables.add(var);
    }

    private void addFunction(Function fun) {
        this.functions.add(fun);
    }

    public String getName() {
        return name;
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public String getSuperClass() {
        return superClass;
    }

    public List<Variable> getVariables() {
        return variables;
    }

    public List<Function> getFunctions() {
        return functions;
    }

    public void writeHeader(GenerationContext ctx) throws IOException {
        FileManager.writeFile(ctx.getPackageGenerationDirectory() +
                this.name + ".h", null, false);
    }

    public void writeClass(GenerationContext ctx) throws IOException {
        FileManager.writeFile(ctx.getPackageGenerationDirectory() +
                this.name + ".c", null, false);
    }
}
