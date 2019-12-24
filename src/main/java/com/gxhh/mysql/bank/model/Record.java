package com.gxhh.mysql.bank.model;

import java.math.BigInteger;

public class Record {
    private Integer rno;
    private BigInteger scardnum;
    private BigInteger rcardnum;
    private Double money;
    private String time;
    private String note;

    public String toString() {
        return "rno = " + getRno() +
                "scardnum = " + getScardnum() +
                "rcardnum = " + getRcardnum() +
                "money = " + getMoney() +
                "time = " + getTime() +
                "note = " + getNote();
    }

    public Integer getRno() {
        return this.rno;
    }

    public BigInteger getScardnum() {
        return this.scardnum;
    }

    public BigInteger getRcardnum() {
        return this.rcardnum;
    }

    public Double getMoney() {
        return this.money;
    }

    public String getTime() {
        return this.time;
    }

    public String getNote() {
        return this.note;
    }

    public void setRno(Integer rno) {
        this.rno = rno;
    }

    public void setScardnum(BigInteger scardnum) {
        this.scardnum = scardnum;
    }

    public void setRcardnum(BigInteger rcardnum) {
        this.rcardnum = rcardnum;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
