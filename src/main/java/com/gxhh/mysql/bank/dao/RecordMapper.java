package com.gxhh.mysql.bank.dao;

import com.gxhh.mysql.bank.model.Record;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;

@Component
public interface RecordMapper {
    @Select("Select * from record")
    List<Record> getRecordList();

    @Select("Select * from record where scardnum=#{cardnum} or rcardnum=#{cardnum}")
    List<Record> getRecordListByCardnum(BigInteger cardnum);

    @Insert("Insert into record(scardnum, rcardnum, money, time) " +
    "values(#{scardnum}, #{rcardnum}, #{money}, #{time})")
    void insertRecord(BigInteger scardnum, BigInteger rcardnum, Double money, String time);
}
