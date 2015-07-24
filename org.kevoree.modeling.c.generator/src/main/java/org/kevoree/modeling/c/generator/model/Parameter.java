package org.kevoree.modeling.c.generator.model;

public class Parameter {
    private String type;
    private String name;
    private boolean isConst;

    public Parameter(String type, String name) {
        this.type = type;
        this.name = name;
        this.isConst = false;
    }

    public Parameter(String type, String name, boolean isConst) {
        this(type, name);
        this.isConst = isConst;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public boolean isConst() {
        return isConst;
    }
}
