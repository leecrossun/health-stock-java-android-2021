package ddwucom.mobile.healthstock.dto;

public class UserInfo {
    private String userName;
    private int point;
    private double height;
    private double weight;

    public UserInfo() { }

    public UserInfo(String userName, int point, double height, double weight) {
        this.userName = userName;
        this.point = point;
        this.height = height;
        this.weight = weight;
    }

    public void setUserName(String username){
        this.userName = username;
    }
    public String getUserName(){
        return this.userName;
    }

    public void setPoint(int point){
        this.point = point;
    }
    public int getPoint(){
        return this.point;
    }

    public void setHeight(double height){
        this.height = height;
    }
    public double getHeight(){
        return this.height;
    }

    public void setWeight(double weight){
        this.weight = weight;
    }
    public double getWeight(){
        return this.weight;
    }
}
