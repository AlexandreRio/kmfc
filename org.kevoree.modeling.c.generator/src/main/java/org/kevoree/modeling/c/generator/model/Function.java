package org.kevoree.modeling.c.generator.model;

import java.util.ArrayList;
import java.util.List;

public class Function {
    /**
     * Return type, name and other attributes, like const.
     */
    private String signature;
    private String returnType;
    /**
     * Whether it should be included in the header file.
     */
    private Visibility visibility;
    private List<Parameter> parameters;
    private String body;

    public Function(String signature, String returnType, Visibility visibility) {
        this.visibility = visibility;
        this.returnType = returnType;
        this.signature = signature;
        this.parameters = new ArrayList<Parameter>();
        this.body = "";
    }

    public String getSignature() {
        return signature;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Visibility getVisibilityType() {
        return visibility;
    }

    public void addParameter(Parameter param) {
        this.parameters.add(param);
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public enum Visibility {PRIVATE, IN_HEADER, IN_VT}
}
