package org.core.datamodel;

import java.util.ArrayList;

public class Stage extends Data {

    private ArrayList<String> organizations;
    private ArrayList<String> components;
    private ArrayList<String> equipment;
    private ArrayList<Personnel> personnel;
    private ArrayList<String> nextStages;

    public ArrayList<String> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(ArrayList<String> organizations) {
        this.organizations = organizations;
    }

    public ArrayList<String> getComponents() {
        return components;
    }

    public void setComponents(ArrayList<String> components) {
        this.components = components;
    }

    public ArrayList<String> getEquipment() {
        return equipment;
    }

    public void setEquipment(ArrayList<String> equipment) {
        this.equipment = equipment;
    }

    public ArrayList<Personnel> getPersonnel() {
        return personnel;
    }

    public void setPersonnel(ArrayList<Personnel> personnel) {
        this.personnel = personnel;
    }

    public ArrayList<String> getNextStages() {
        return nextStages;
    }

}


