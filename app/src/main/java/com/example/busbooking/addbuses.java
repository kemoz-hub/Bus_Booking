package com.example.busbooking;

public class addbuses {

    public addbuses() {

    }

    public String getIdno() {
        return idno;
    }

    public void setIdno(String idno) {
        this.idno = idno;
    }

    public addbuses(String fullname, String email, String bus_number, String id, String from, String to, String departure, String cost,String idno) {
        this.fullname = fullname;
        this.email = email;
        Bus_number = bus_number;
        this.id = id;
        From = from;
        To = to;
        Departure = departure;
        this.cost = cost;
        this.idno=idno;
    }

    String fullname;
    String email;
    String Bus_number;
    String id;
    String From;
    String To;
    String Departure;
    String cost;
    String idno;


    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBus_number() {
        return Bus_number;
    }

    public void setBus_number(String bus_number) {
        Bus_number = bus_number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFrom() {
        return From;
    }

    public void setFrom(String from) {
        From = from;
    }

    public String getTo() {
        return To;
    }

    public void setTo(String to) {
        To = to;
    }

    public String getDeparture() {
        return Departure;
    }

    public void setDeparture(String departure) {
        Departure = departure;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }


}

