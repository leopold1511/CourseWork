package datamodel;

import java.util.ArrayList;
import java.util.List;

public class Product {
    private Long id;
    private String productName;
    private String strategicDirection;
    private ArrayList<Stage> stages;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getStrategicDirection() {
        return strategicDirection;
    }

    public void setStrategicDirection(String strategicDirection) {
        this.strategicDirection = strategicDirection;
    }

    public List<Stage> getStages() {
        return stages;
    }

    public void setStages(ArrayList<Stage> stages) {
        this.stages = stages;
    }
}