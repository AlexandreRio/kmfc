package org.kevoree.modeling.c.generator.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Data structure in the intermediate representation of the compiler that represents a Function with its signature
 * its body, its parameters, its return type and some other attributes.
 */
public class Function {
    /**
     * Return type, name and other attributes, like const.
     */
    private String signature;
    private String returnType;
    private boolean isStatic;
    private boolean isTypeDef;
    /**
     * Whether it should be included in the header file.
     */
    private Visibility visibility;
    private List<Parameter> parameters;
    private String body;

    public Function(String signature, String returnType, Visibility visibility) {
        this.visibility = visibility;
        this.returnType = returnType;
        this.isStatic = false;
        this.signature = signature;
        this.parameters = new ArrayList<Parameter>();
        this.body = "";
        this.isTypeDef = true;
    }

    public Function(String signature, String returnType, Visibility visibility, boolean isStatic) {
        this(signature, returnType, visibility);
        this.isStatic = isStatic;
    }

    public Function(String signature, String returnType, Visibility visibility, boolean isStatic, boolean isTypeDef) {
        this(signature, returnType, visibility, isStatic);
        this.isTypeDef = isTypeDef;
    }

    public String getSignature() {
        return signature;
    }

    public String getReturnType() {
        return returnType;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public boolean isTypeDef() {
        return isTypeDef;
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
