package com.gxhh.mysql.bank.service;

import com.gxhh.mysql.bank.dao.BankMapper;
import com.gxhh.mysql.bank.model.Bank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class BankServiceImpl implements BankService{
    @Autowired
    private BankMapper bankMapper;

    @Override
    public List<Bank> getBankList() {
        return bankMapper.getBankList();
    }

    @Override
    public void insertBank(Map<String, Object> map) {
        bankMapper.insertBank(map.get("bname").toString(), map.get("bphone").toString());
    }

}
