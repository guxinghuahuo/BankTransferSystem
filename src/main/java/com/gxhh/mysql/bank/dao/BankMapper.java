package com.gxhh.mysql.bank.dao;

import com.gxhh.mysql.bank.model.Bank;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface BankMapper {
    @Select("Select * from bank")
    List<Bank> getBankList();

    @Insert("Insert into bank values(#{bname}, #{bphone});")
    void insertBank(String bname, String bphone);
}
