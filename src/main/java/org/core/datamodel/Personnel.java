package org.core.datamodel;

public record Personnel(String profession, int manHours) {

    @Override
    public String toString() {
        return profession;
    }

}