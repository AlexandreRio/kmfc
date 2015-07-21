package org.kevoree.modeling.c.generator.model;

import java.util.ArrayList;
import java.util.List;

public class Function {
    /**
     * Return type, name and other attributes, like const.
     */
    private String signature;
    /**
     * Whether it should be included in the header file.
     */
    private boolean isPublic;
    private List<Parameter> parameters;
    private String body;

    public Function(boolean isPublic, String signature) {
        this.isPublic = isPublic;
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

    public boolean isPublic() {
        return isPublic;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }
}
