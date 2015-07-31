package org.kevoree.modeling.c.generator.model;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.kevoree.modeling.c.generator.Generator;
import org.kevoree.modeling.c.generator.TemplateManager;
import org.kevoree.modeling.c.generator.model.Function.Visibility;
import org.kevoree.modeling.c.generator.utils.ConverterDataTypes;
import org.kevoree.modeling.c.generator.utils.HelperGenerator;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class Classifier {
    private String name;
    private String superClass;
    private List<String> allSuperClass;
    private boolean isAbstract;
    private List<Variable> variables;
    private List<Function> functions;

    /**
     * Class representation
     *
     * @param name       Name of the class
     * @param sClass     Direct super class
     * @param isAbstract If the class is abstract
     */
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
        c.createFunction();
        return c;
    }

    /**
     * Create a list of all the Classifier this Classifier is linked to.
     * This may be a super class or a link.
     * A Classifier is unique is the link, even if the Classifier has 2 links to
     * a specific Classifier.
     *
     * @param cls A classifier
     * @return List of all the linked Classifier
     */
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
            String t;
            if (attr.getName().equals("generated_KMF_ID"))
                t = "char";
            else
                t = ConverterDataTypes.getInstance().check_type(attr.getEAttributeType().getName());
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
            this.superClass = cls.getESuperTypes().get(0).getName();
        else if (cls.getEAllSuperTypes().size() == 0)
            this.superClass = "KMFContainer";

        if (!this.allSuperClass.contains("KMFContainer"))
            this.addSuperType("KMFContainer");
        for (EClass ec : cls.getEAllSuperTypes())
            this.addSuperType(ec.getName());

        if (this.name.equals("NamedElement"))
            this.addVariable(new Variable("internalKey", "char*", Variable.LinkType.PRIMITIVE, false));
    }

    private void createFunction() {
        this.createMetaClassNameFunction();
        this.createInitFunction();
        this.createInternalGetKeyFunction();
        this.createNewFunction();
        this.createDeleteFunction();
        this.createAttributesManipulationFunctions();
    }

    private void generateAddFunction(Variable v) {
        String addSignature = this.name + "Add" + HelperGenerator.genToUpperCaseFirstChar(v.getName());
        String returnType = "void";
        Parameter p1 = new Parameter(this.name + "*", "this", true);
        Parameter p2;
        if (v.getType().equals("char*"))
            p2 = new Parameter(v.getType(), "ptr");
        else
            p2 = new Parameter(v.getType() + "*", "ptr");

        StringWriter result = new StringWriter();
        String addBody;
        if (v.getLinkType() == Variable.LinkType.MULTIPLE_LINK) {
            VelocityContext context = new VelocityContext();
            context.put("classname", this.name);
            context.put("refname", v.getName());
            context.put("type", v.getType());
            context.put("methname", HelperGenerator.genToUpperCaseFirstChar(v.getName()));

            if (!v.isContained())
                context.put("iscontained", "");
            else
                context.put("iscontained", "ptr->eContainer = this;");

            TemplateManager.getInstance().getGen_add().merge(context, result);
            addBody = result.toString();
        } else {
            if (v.isContained()) { // if it's a container
                VelocityContext context = new VelocityContext();
                context.put("refname", v.getName());
                context.put("name", HelperGenerator.genToLowerCaseFirstChar(this.name));
                context.put("methname", HelperGenerator.genToUpperCaseFirstChar(v.getName()));
                TemplateManager.getInstance().getGen_add_unary_containment().merge(context, result);
                addBody = result.toString();
            } else {
                addBody = "\tthis->" + v.getName() + " = ptr;\n";
            }
        }

        Function addFunction = new Function(addSignature, returnType, Visibility.IN_VT, true);
        addFunction.addParameter(p1);
        addFunction.addParameter(p2);
        addFunction.setBody(addBody);
        this.addFunction(addFunction);
    }

    private void generateRemoveFunction(Variable v) {
        String removeSignature = this.name + "Remove" + HelperGenerator.genToUpperCaseFirstChar(v.getName());
        String returnType = "void";
        Parameter p1 = new Parameter(this.name + "*", "this", true);
        Parameter p2;
        if (v.getType().equals("char*"))
            p2 = new Parameter(v.getType(), "ptr");
        else
            p2 = new Parameter(v.getType() + "*", "ptr");

        StringWriter result = new StringWriter();
        String removeBody;
        if (v.getLinkType() == Variable.LinkType.MULTIPLE_LINK) {
            VelocityContext context = new VelocityContext();
            context.put("refname", v.getName());
            context.put("type", v.getType());
            context.put("classname", this.name);

            if (v.isContained())
                context.put("iscontained", "ptr->eContainer = NULL;\n");
            else
                context.put("iscontained", "");

            TemplateManager.getInstance().getGen_remove().merge(context, result);
            removeBody = result.toString();
        } else {
            removeBody = "\tthis->" + v.getName() + " = NULL;\n";
            if (v.isContained())
                removeBody += "\tptr->eContainer = NULL;\n";
        }

        Function removeFunction = new Function(removeSignature, returnType, Visibility.IN_VT, true);
        removeFunction.addParameter(p1);
        removeFunction.addParameter(p2);
        removeFunction.setBody(removeBody);
        this.addFunction(removeFunction);
    }

    private void generateFindFunction(Variable v) {
        String findSignature = this.name + "Find" + v.getName() + "ByID";
        String returnType = v.getType() + "*";
        Parameter p1 = new Parameter(this.name + "*", "this", true);
        Parameter p2 = new Parameter("char*", "id");

        VelocityContext context = new VelocityContext();
        StringWriter result = new StringWriter();
        String findBody;
        context.put("type", this.name);
        context.put("refname", v.getName());
        context.put("majrefname", HelperGenerator.genToUpperCaseFirstChar(v.getName()));
        TemplateManager.getInstance().getGen_find_by_id().merge(context, result);
        findBody = result.toString();

        Function findFunction = new Function(findSignature, returnType, Visibility.IN_VT, true);
        findFunction.addParameter(p1);
        findFunction.addParameter(p2);
        findFunction.setBody(findBody);
        this.addFunction(findFunction);
    }

    private void createAttributesManipulationFunctions() {
        for (Variable v : this.getVariables()) {
            /** TODO add field on {@link Variable} to avoid ad hoc */
            if (!v.getName().equals("generated_KMF_ID")) {
                this.generateAddFunction(v);
                this.generateRemoveFunction(v);
                if (v.getLinkType() == Variable.LinkType.MULTIPLE_LINK)
                    this.generateFindFunction(v);
            }
        }
    }

    private void createNewFunction() {
        if (!this.isAbstract) {
            String newSignature = "new_" + this.name;
            String returnType = this.name + "*";
            VelocityContext context = new VelocityContext();
            context.put("classname", this.name);
            context.put("vtName", "vt_" + this.name);
            StringWriter result = new StringWriter();
            TemplateManager.getInstance().getGen_new().merge(context, result);

            Function f = new Function(newSignature, returnType, Visibility.IN_HEADER);
            f.setBody(result.toString());
            this.addFunction(f);
        }
    }

    private void createDeleteFunction() {
        String deleteSignature = "delete" + this.name;
        String returnType = "void";
        Parameter param = new Parameter(this.name + "*", "this", true);
        String deleteBody = "\tvt_" + this.superClass + ".delete((" + this.superClass
                + "*)this);\n";

        VelocityContext context;
        StringWriter result;
        for (Variable v : this.getVariables()) {
            if (v.getLinkType() == Variable.LinkType.MULTIPLE_LINK) {
                context = new VelocityContext();
                result = new StringWriter();
                context.put("refname", v.getName());
                if (v.isContained())
                    context.put("iscontained", "deleteContainerContents(this->" + v.getName() + ");");
                else
                    context.put("iscontained", "");

                TemplateManager.getInstance().getGen_delete().merge(context, result);
                deleteBody += result.toString();
            }
        }

        Function deleteFunction = new Function(deleteSignature, returnType, Visibility.PRIVATE, true);
        deleteFunction.addParameter(param);
        deleteFunction.setBody(deleteBody);
        this.addFunction(deleteFunction);
    }

    private void createMetaClassNameFunction() {
        String metaClassNameSignature = this.name + "_metaClassName";
        String returnType = "char*";
        Parameter param = new Parameter(this.name + "*", "this", true);
        String metaClassNameBody = "\treturn \"" + this.name + "\";\n";
        Function metaClassFunction = new Function(metaClassNameSignature, returnType, Visibility.PRIVATE, true);
        metaClassFunction.addParameter(param);
        metaClassFunction.setBody(metaClassNameBody);
        this.addFunction(metaClassFunction);
    }

    private void createInitFunction() {
        String initSignature = "init" + this.name;
        String returnType = "void";
        String initBody = "\tinit" + this.superClass + "((" + this.superClass
                + "*)this);\n";

        if (this.containsVariable("generated_KMF_ID"))
            initBody += "\tmemset(&this->generated_KMF_ID[0], 0, sizeof(this->generated_KMF_ID));\n" +
                    "\trand_str(this->generated_KMF_ID, 8);\n";

        for (Variable v : this.getVariables())
            if (!v.getName().equals("generated_KMF_ID"))
                initBody += "\tthis->" + v.getName() + " = " + HelperGenerator.
                        genDefaultValue(v.getType()) + ";\n";
        Parameter p = new Parameter(this.name + "*", "this");
        Function initFunction = new Function(initSignature, returnType, Visibility.IN_HEADER);
        initFunction.addParameter(p);
        initFunction.setBody(initBody);
        this.addFunction(initFunction);
    }

    /**
     * TODO should be easier to produce code based on attributes and not on class name
     */
    private void createInternalGetKeyFunction() {
        VelocityContext context = new VelocityContext();
        StringWriter writer = new StringWriter();
        String internalGetKeySignature = this.name + "_internalGetKey";
        String returnType = "char*";
        Parameter param = new Parameter(this.name + "*", "this", true);

        String body;
        if (this.name.equals("DeployUnit")) {
            Template method = TemplateManager.getInstance().getTp_getKey_DeployUnit();
            method.merge(context, writer);
            body = writer.toString();
        } else if (this.name.equals("TypeDefinition") && this.containsVariable("version")) {
            Template method = TemplateManager.getInstance().getTp_getKey_TypeDefinition();
            method.merge(context, writer);
            body = writer.toString();
        } else if (this.name.equals("Repository")) {
            body = "\treturn this->url;\n";
        } else if (this.name.equals("NamedElement") ||
                (this.name.equals("DictionaryValue") && this.containsVariable("name"))) {
            body = "\treturn this->name;\n";
        } else if (this.superClass.equals("KMFContainer")) {
            body = "\treturn this->generated_KMF_ID;\n";
        } else { // in all other cases we inherit from NamedElement
            body = "\treturn vt_" + this.superClass + ".internalGetKey((" +
                    this.superClass + "*)this);\n";
        }

        Function internalGetKeyFunction = new Function(internalGetKeySignature, returnType, Visibility.PRIVATE, true);
        internalGetKeyFunction.addParameter(param);
        internalGetKeyFunction.setBody(body);
        this.addFunction(internalGetKeyFunction);
    }

    private boolean containsVariable(String name) {
        for (Variable v : this.getVariables())
            if (v.getName().equals(name))
                return true;
        return false;
    }

    /**
     * Only to call after full parsing! Safe to call when serializing
     * Check if the current Classifier contains a Variable with this given or if
     * any super classes contains it.
     *
     * @see Variable
     * @see #containsVariable(String)
     * @param name Name of the Variable
     * @return If the produced class will contain this Variable
     */
    public boolean inheritVariable(String name) {
        if (this.containsVariable(name))
            return true;
        for (String s : this.allSuperClass)
            if (!s.equals("KMFContainer") && Generator.classifiers.get(s).containsVariable(name))
                return true;
        return false;
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

    public String getSuperClass() {
        return superClass;
    }

    public boolean isAbstract() {
        return isAbstract;
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