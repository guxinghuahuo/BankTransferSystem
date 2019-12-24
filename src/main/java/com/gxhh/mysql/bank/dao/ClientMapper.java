package com.gxhh.mysql.bank.dao;

import com.gxhh.mysql.bank.model.Client;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ClientMapper {
    @Select("Select * from client")
    List<Client> getClientList();

    @Select("Select * from client where cphone=#{cphone}")
    Client getClientByPhone(String cphone);

    @Insert("Insert into client(cname, idcard, csex, cage, cphone, loginpw) " +
            "values(#{cname}, #{idcard}, #{csex}, #{cage}, #{cphone}, #{loginpw})")
    void insertClient(String cname, String idcard, String csex, Integer cage, String cphone, String loginpw);

}
