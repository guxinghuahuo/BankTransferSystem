package com.gxhh.mysql.bank.service;

import com.gxhh.mysql.bank.dao.TransferMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Map;

@Repository
public class TransferServiceImpl implements TransferService{
    @Autowired
    private TransferMapper transferMapper;

    @Override
    public void callTransfer(Map<String, Object> map) {
        transferMapper.excTransfer((BigInteger) map.get("scardnum"),(BigInteger) map.get("rcardnum"), Double.parseDouble(map.get("money").toString()), map.get("note").toString());
    }
}
