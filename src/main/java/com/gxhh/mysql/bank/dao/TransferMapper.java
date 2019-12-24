package com.gxhh.mysql.bank.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.mapping.StatementType;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
public interface TransferMapper {
    @Insert("call transfer(" +
            "#{scardnum,mode=IN}," +
            "#{rcardnum,mode=IN}," +
            "#{money,mode=IN}," +
            "#{note,mode=IN});")
    @Options(statementType = StatementType.CALLABLE)
    void excTransfer(BigInteger scardnum, BigInteger rcardnum, Double money, String note);
}
