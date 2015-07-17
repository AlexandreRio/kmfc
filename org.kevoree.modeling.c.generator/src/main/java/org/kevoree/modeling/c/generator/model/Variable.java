package org.kevoree.modeling.c.generator.model;

public class Variable {
    private String name;
    private Type type;
    private boolean isContained;

    public Variable(String name, Type type, boolean isContained) {
        this.name = name;
        this.type = type;
        this.isContained = isContained;
    }

    public Type getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public enum Type {PRIMITIVE, UNARY_LINK, MULTIPLE_LINK}
}
