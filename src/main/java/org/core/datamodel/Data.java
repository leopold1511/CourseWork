package org.core.datamodel;

public abstract class Data {
    protected String id;
    protected String name;

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
