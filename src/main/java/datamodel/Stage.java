package datamodel;

import java.util.ArrayList;
import java.util.List;


public class Stage {
    private Long id;
    private String stageName;
    private Organization responsibleOrganization;
    private ArrayList<Component> requiredComponents;
    private ArrayList<Equipment> requiredEquipment;
    private ArrayList<Personnel> requiredPersonnel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String name) {
        this.stageName = name;
    }

    public Organization getResponsibleOrganization() {
        return responsibleOrganization;
    }

    public void setResponsibleOrganization(Organization responsibleOrganization) {
        this.responsibleOrganization = responsibleOrganization;
    }

    public ArrayList<Component> getRequiredComponents() {
        return requiredComponents;
    }

    public void setRequiredComponents(ArrayList<Component> requiredComponents) {
        this.requiredComponents = requiredComponents;
    }

    public ArrayList<Equipment> getRequiredEquipment() {
        return requiredEquipment;
    }

    public void setRequiredEquipment(ArrayList<Equipment> requiredEquipment) {
        this.requiredEquipment = requiredEquipment;
    }

    public ArrayList<Personnel> getRequiredPersonnel() {
        return requiredPersonnel;
    }

    public void setRequiredPersonnel(ArrayList<Personnel> requiredPersonnel) {
        this.requiredPersonnel = requiredPersonnel;
    }
}

