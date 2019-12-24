package com.gxhh.mysql.bank.service;

import com.gxhh.mysql.bank.model.Bankcard;
import com.gxhh.mysql.bank.model.Record;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface RecordService {
    List<Record> getRecordList();

    List<Record> getRecordListByBankcard(Bankcard bankcard);

    void insertRecord(Map<String, Object> map);
}
