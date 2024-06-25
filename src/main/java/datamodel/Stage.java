package datamodel;

import java.util.ArrayList;
import java.util.List;

import java.util.List;

public class Stage {
    private String stageId;
    private String stageName;
    private String company;
    private List<String> components;
    private List<String> equipment;
    private List<Personnel> personnel;
    private List<String> nextStages;

    public String getStageId() {
        return stageId;
    }

    public void setStageId(String stageId) {
        this.stageId = stageId;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public List<String> getComponents() {
        return components;
    }

    public void setComponents(List<String> components) {
        this.components = components;
    }

    public List<String> getEquipment() {
        return equipment;
    }

    public void setEquipment(List<String> equipment) {
        this.equipment = equipment;
    }

    public List<Personnel> getPersonnel() {
        return personnel;
    }

    public void setPersonnel(List<Personnel> personnel) {
        this.personnel = personnel;
    }

    public List<String> getNextStages() {
        return nextStages;
    }

    public void setNextStages(List<String> nextStages) {
        this.nextStages = nextStages;
    }
}


