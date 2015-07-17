package org.kevoree.modeling.c.generator.model;

import java.util.ArrayList;
import java.util.List;

public class Classifier {
    private String name;
    private boolean isAbstract;
    private List<Variable> variables;
    private List<Function> functions;

    public Classifier(String name, boolean isAbstract) {
        this.name = name;
        this.isAbstract = isAbstract;
        this.variables = new ArrayList<Variable>();
        this.functions = new ArrayList<Function>();
    }

    public void addVariable(Variable var) {
        this.variables.add(var);
    }

    public void addFunction(Function fun) {
        this.functions.add(fun);
    }

    public String getName() {
        return name;
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public List<Variable> getVariables() {
        return variables;
    }

    public List<Function> getFunctions() {
        return functions;
    }
}
