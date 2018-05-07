package org.example.localevents;

import org.json.JSONObject;

import java.util.ArrayList;

public class Event {
    private String Event_name;
    private String Description;
    private String Street;
    private String City;
    private String State;
    private String Zip;
    private boolean Private;
    private String ID;
    private String Host;

    public Event(){
        //constructor
    }

    public Event Event_from_data(String Name, String event_description, String event_street, String event_city, String event_state, String event_zip, String user, boolean checkBox){
        Event_name = Name;
        Description = event_description;
        Street = event_street;
        City = event_city;
        State = event_state;
        Zip = event_zip;
        Host = user;
        Private = checkBox;
        return this;
    }

    //setters
    public void setEvent_name(String name){
        this.Event_name = name;
    }
    public void setDescription(String desc){
        this.Description = desc;
    }
    public void setAddress(ArrayList<String> add){
        this.Street = add.get(0);
        this.City = add.get(1);
        this.State = add.get(2);
        this.Zip = add.get(3);
    }

    public void setPrivate(boolean aPrivate) {
        Private = aPrivate;
    }

    public void setHost(String host) {
        Host = host;
    }

    //getters
    public String getEvent_name(){
        return Event_name;
    }
    public String getDescription(){
        return Description;
    }
    public ArrayList<String> getAddress(){
        ArrayList<String> address = new ArrayList<>();
        address.add(Street);
        address.add(City);
        address.add(State);
        address.add(Zip);
        return address;
    }
    public String getHost(){
        return Host;
    }

    public String getAddressString(){
        return Street + " " + City + ", " + State + " " + Zip;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String idReturned() {
        return ID;
    }

    public boolean isPrivate() {
        return Private;
    }
}
