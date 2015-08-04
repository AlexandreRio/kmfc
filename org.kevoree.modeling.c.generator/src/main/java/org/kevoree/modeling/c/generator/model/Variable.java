package org.kevoree.modeling.c.generator.model;

public class Variable {
    private String name;
    private String type;
    private LinkType linkType;
    private boolean isContained;
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
