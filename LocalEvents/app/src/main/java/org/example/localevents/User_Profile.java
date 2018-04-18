package org.example.localevents;

public class User_Profile {
    private String Email;
    private String FName;
    private String LName;
    private String DOB;

    public User_Profile(String email, String first, String last, String dob) {
        Email =email;
        FName = first;
        LName = last;
        DOB = dob;
    }

    public String getEmail() {
        return Email;
    }

    public String getFName() {
        return FName;
    }

    public String getLName() {
        return LName;
    }

    public String getDOB() {
        return DOB;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public void setFName(String FName) {
        this.FName = FName;
    }

    public void setLName(String LName) {
        this.LName = LName;
    }
}
