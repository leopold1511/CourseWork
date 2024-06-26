package datamodel;

import java.util.ArrayList;
import java.util.List;

public class Product extends Data {

    private String strategicDirection;
    private ArrayList<Stage> stages;

    public String getStrategicDirection() {
        return strategicDirection;
    }

    public void setStrategicDirection(String strategicDirection) {
        this.strategicDirection = strategicDirection;
    }

    public ArrayList<Stage> getStages() {
        return stages;
    }

    public void setStages(ArrayList<Stage> stages) {
        this.stages = stages;
    }
}