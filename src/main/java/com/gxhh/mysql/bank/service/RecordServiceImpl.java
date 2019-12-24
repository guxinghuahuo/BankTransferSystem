package com.gxhh.mysql.bank.service;

import com.gxhh.mysql.bank.dao.RecordMapper;
import com.gxhh.mysql.bank.model.Bankcard;
import com.gxhh.mysql.bank.model.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@Repository
public class RecordServiceImpl implements RecordService{
    @Autowired
    private RecordMapper recordMapper;

    @Override
    public List<Record> getRecordList() {
        return recordMapper.getRecordList();
    }

    @Override
    public List<Record> getRecordListByBankcard(Bankcard bankcard) {
        return recordMapper.getRecordListByCardnum(bankcard.getCardnum());
    }

    @Override
    public void insertRecord(Map<String, Object> map) {
        recordMapper.insertRecord((BigInteger) map.get("scardnum"),(BigInteger) map.get("rcardnum"), Double.parseDouble(map.get("money").toString()), map.get("time").toString());
    }
}
