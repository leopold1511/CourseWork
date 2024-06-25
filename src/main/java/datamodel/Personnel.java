package datamodel;

public class Personnel {
    private Long id;
    private String profession;
    private int manHours;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public int getManHours() {
        return manHours;
    }

    public void setManHours(int manHours) {
        this.manHours = manHours;
    }
}