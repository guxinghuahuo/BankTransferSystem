package com.gxhh.mysql.bank.model;

public class Client {
    private Integer cno;
    private String cname;
    private String idcard;
    private String csex;
    private Integer cage;
    private String cphone;
    private String loginpw;

    public String toString() {
        return "cno = " + getCno() +
                ", cname = " + getCname() +
                ", idcard = " + getIdcard() +
                ", sex = " + getCsex() +
                ", age = " + getCage() +
                ", cphone = " + getCphone();
    }

    public Integer getCno(){
        return this.cno;
    }

    public String getCname() {
        return this.cname;
    }

    public String getIdcard() {
        return this.idcard;
    }

    public String getCsex() {
        return this.csex;
    }

    public Integer getCage() {
        return this.cage;
    }

    public String getCphone() {
        return this.cphone;
    }

    public String getLoginpw() {
        return this.loginpw;
    }

    public void setCno(Integer cno) {
        this.cno = cno;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public void setSex(String sex) {
        this.csex = sex;
    }

    public void setAge(Integer age) {
        this.cage = age;
    }

    public void setCphone(String cphone) {
        this.cphone = cphone;
    }

    public void setLoginpw(String loginpw) {
        this.loginpw = loginpw;
    }
}
