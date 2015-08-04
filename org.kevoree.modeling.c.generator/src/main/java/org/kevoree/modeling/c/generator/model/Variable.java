package org.kevoree.modeling.c.generator.model;

public class Variable {
    /**
     * Name of the variable
     */
    private String name;
    /**
     * Primitive type or pointer type. Example: "int" or "Class*"
     * <p>
     * Set {@link #linkType} accordingly
     */
    private String type;
    /**
     * Type of variable.
     *
     * @see org.kevoree.modeling.c.generator.model.Variable.LinkType
     */
    private LinkType linkType;
    /**
     * If the variable is contained, see the model specification for further details.
     */
    private boolean isContained;
    /**
     * If the variable value depends only on other variables.
     *
     * If true this will disable getter/setter and test generation for this
     * variable.
     */
    private boolean isGenerated;

    public Variable(String name, String type, LinkType linkType, boolean isContained, boolean isGenerated) {
        this.name = name;
        this.type = type;
        this.linkType = linkType;
        this.isContained = isContained;
        this.isGenerated = isGenerated;
    }

    public LinkType getLinkType() {
        return this.linkType;
    }

    public String getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public boolean isContained() {
        return this.isContained;
    }

    public boolean isGenerated() {
        return this.isGenerated;
    }

    public enum LinkType {PRIMITIVE, UNARY_LINK, MULTIPLE_LINK}
}
