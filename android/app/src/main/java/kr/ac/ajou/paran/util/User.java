package kr.ac.ajou.paran.util;

/**
 * Created by dream on 2017-08-05.
 */

public class User {
    private int number; // 학번
    private String name; // 이름
    private String grade; // 학년
    private String campus; // 대학
    private String department; // 부서
    private String major; // 전공
    private boolean newORtrans; // 신/편입
    private boolean abeek;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public boolean isNewORtrans() {
        return newORtrans;
    }

    public void setNewORtrans(boolean newORtrans) {
        this.newORtrans = newORtrans;
    }

    public boolean isAbeek() {
        return abeek;
    }

    public void setAbeek(boolean abeek) {
        this.abeek = abeek;
    }

    public void setNumber(int number){
        this.number = number;
    }

    public int getNumber(){
        return number;
    }
}
