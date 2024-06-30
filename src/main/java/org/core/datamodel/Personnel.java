package org.core.datamodel;

public class Personnel extends Data {

    private final String profession;
    private final int manHours;

    public Personnel(String profession, int manHours) {
        this.profession = profession;
        this.manHours = manHours;
    }

    @Override
    public String toString() {
        return profession;
    }

    public String getProfession() {
        return profession;
    }

    public int getManHours() {
        return manHours;
    }

}