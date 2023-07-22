package com.example.busbooking;

public class paymentfirebase {

    public paymentfirebase(){

    }
    String fullnames;
    String amount;
    String idnumber;
    String payment_method;
    String payment_code;

    public paymentfirebase(String fullnames, String amount, String idnumber, String payment_method, String payment_code) {
        this.fullnames = fullnames;
        this.amount = amount;
        this.idnumber = idnumber;
        this.payment_method = payment_method;
        this.payment_code = payment_code;
    }

    public String getFullnames() {
        return fullnames;
    }

    public void setFullnames(String fullnames) {
        this.fullnames = fullnames;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getIdnumber() {
        return idnumber;
    }

    public void setIdnumber(String idnumber) {
        this.idnumber = idnumber;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getPayment_code() {
        return payment_code;
    }

    public void setPayment_code(String payment_code) {
        this.payment_code = payment_code;
    }
}
