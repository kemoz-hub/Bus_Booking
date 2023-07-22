package com.example.busbooking;

public class userhelper {
    String fullname;
    String email;
    String password;
    String confirm;
    String uri;
    public userhelper() {

    }
    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    String Uid;
    int Usertype;
    String phone;
    String id;



    public userhelper(String uid, String fullname, String email, String password, String phone, String confirm, String id, int Usertype, String uri) {
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.confirm = confirm;
        this.id=id;
        this.Usertype=Usertype;
        this.uri=uri;

    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public int getUsertype() {
        return Usertype;
    }

    public void setUsertype(int usertype) {
        this.Usertype = usertype;
    }

    public String getConfirm() {
        return confirm;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
