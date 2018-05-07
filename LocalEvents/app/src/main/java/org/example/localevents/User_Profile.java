package org.example.localevents;

public class user_profile {
    private String Email;
    private String FName;
    private String LName;
    private String DOB;
    private String UID;

    public user_profile(){
        //constructor
    }

    public user_profile from_data(String email, String first, String last, String dob, String uid) {
        //set variables
        Email =email;
        FName = first;
        LName = last;
        DOB = dob;
        UID = uid;
        return this;
    }
    //Getters
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
    public String getUID() {
        return UID;
    }

    //setters
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

    public void setUID(String UID) {
        this.UID = UID;
    }
}
