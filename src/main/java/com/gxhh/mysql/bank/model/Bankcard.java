package com.gxhh.mysql.bank.model;

import java.math.BigInteger;

public class Bankcard {
    private BigInteger cardnum;
    private Double balance;
    private String bpw;
    private String bname;
    private Integer cno;

    public String toString() {
        return "cardnum = " + getCardnum() +
                "balance = " + getBalance() +
                "banme = " + getBname() +
                "cno = " + getCno();
    }

    public BigInteger getCardnum() {
        return this.cardnum;
    }

    public Double getBalance() {
        return this.balance;
    }

    public String getBpw() {
        return this.bpw;
    }

    public String getBname() {
        return this.bname;
    }

    public Integer getCno() {
        return this.cno;
    }

    public void setCardnum(BigInteger cardnum) {
        this.cardnum = cardnum;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public void setBpw(String bpw) {
        this.bpw = bpw;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public void setCno(Integer cno) {
        this.cno = cno;
    }
}
