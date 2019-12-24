package com.gxhh.mysql.bank.dao;

import com.gxhh.mysql.bank.model.Bankcard;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;

@Component
public interface BankcardMapper {
    @Select("Select * from bankcard")
    List<Bankcard> getBankcardList();

    @Select("Select * from bankcard where cno=#{cno}")
    List<Bankcard> getBankcardListByCno(Integer cno);

    @Select("Select * from bankcard where cardnum=#{cardnum}")
    Bankcard getBankcardByCardnum(BigInteger cardnum);

    @Insert("Insert into bankcard values(#{cardnum}, #{balance}, #{bpw}, #{bname}, #{cno})")
    void insertBankcard(BigInteger cardnum, Double balance, String bpw, String bname, Integer cno);
}
