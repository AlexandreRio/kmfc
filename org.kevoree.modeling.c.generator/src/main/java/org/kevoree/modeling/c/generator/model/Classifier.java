package org.kevoree.modeling.c.generator.model;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.kevoree.modeling.c.generator.utils.ConverterDataTypes;

import java.util.ArrayList;
import java.util.List;

public class Classifier {
    private String name;
    private String superClass;
    private List<String> allSuperClass;
    private boolean isAbstract;
    private List<Variable> variables;
    private List<Function> functions;

    public Classifier(String name, String sClass, boolean isAbstract) {
        this.name = name;
        this.superClass = sClass;
        this.isAbstract = isAbstract;
        this.allSuperClass = new ArrayList<String>();
        this.variables = new ArrayList<Variable>();
        this.functions = new ArrayList<Function>();
    }

    public static Classifier createFromEClass(EClass cls) {
        String sType = null;
        if (cls.getESuperTypes().size() > 0)
            sType = cls.getESuperTypes().get(0).getName();

        Classifier c = new Classifier(cls.getName(), sType, cls.isAbstract());

        c.createAttributes(cls);
        c.createFunction(cls);
        return c;
    }

    public static List<String> getLinkedClassifier(Classifier cls) {
        List<String> ret = new ArrayList<String>();
        ret.add(cls.getSuperClass());
        for (Variable v : cls.getVariables())
            if (v.getLinkType() != Variable.LinkType.PRIMITIVE && !ret.contains(v.getType()))
                ret.add(v.getType());
        return ret;
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

        if (!this.allSuperClass.contains("KMFContainer"))
            this.addSuperType("KMFContainer");
        for (EClass ec : cls.getEAllSuperTypes())
            this.addSuperType(ec.getName());

        if (!this.name.equals("NamedElement") && !this.superClass.equals("KMFContainer"))
            this.addVariable(new Variable("generated_KMF_ID", "char*", Variable.LinkType.UNARY_LINK, false));
    }

    private void createFunction(EClass cls) {
        this.createMetaClassNameFunction();
        this.createInitFunction();
    }

    private void createMetaClassNameFunction() {
        String metaClassNameSignature = this.name + "_metaClassName";
        String returnType = "static char*";
        Parameter param = new Parameter(this.name + "* const", "this");
        String metaClassNameBody = "\treturn \"" + this.name + "\";";
        Function metaClassFunction = new Function(metaClassNameSignature, returnType, Function.Visibility_Type.PRIVATE);
        metaClassFunction.addParameter(param);
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

    private void addSuperType(String name) {
        this.allSuperClass.add(name);
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

    public List<String> getAllSuperClass() {
        return allSuperClass;
    }

    public List<Variable> getVariables() {
        return variables;
    }

    public List<Function> getFunctions() {
        return functions;
    }

}

