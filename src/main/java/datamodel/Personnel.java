package datamodel;

public class Personnel extends Data{

    private String profession;
    private int manHours;

    @Override
    public String toString() {
        return profession;
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