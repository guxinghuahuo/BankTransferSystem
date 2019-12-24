package com.gxhh.mysql.bank.model;

public class Bank {
    private String bname;
    private String bphone;

    public String toString() {
        return "bankname = " + getBname() +
                ", bankphone = " + getBphone();
    }

    public String getBname() {
        return this.bname;
    }

    public String getBphone() {
        return this.bphone;
    }
}
