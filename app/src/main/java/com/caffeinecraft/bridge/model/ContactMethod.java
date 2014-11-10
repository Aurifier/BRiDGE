package com.caffeinecraft.bridge.model;

/**
 * Created by drew on 11/10/14.
 */
public class ContactMethod {
    public static enum Type {
        EMAIL, SMS
    }

    private Type type;
    private String value;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
