package com.gxhh.mysql.bank.service;

import com.gxhh.mysql.bank.dao.ClientMapper;
import com.gxhh.mysql.bank.model.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ClientServiceImpl implements ClientService{
    @Autowired
    private ClientMapper clientMapper;

    @Override
    public List<Client> getClientList() {
        return clientMapper.getClientList();
    }

    @Override
    public Client getClientByPhone(String phone) {
        return clientMapper.getClientByPhone(phone);
    }

    @Override
    public void insertClient(Map<String, Object> map) {
        clientMapper.insertClient(map.get("cname").toString(), map.get("idcard").toString(), map.get("csex").toString(), Integer.parseInt(map.get("cage").toString()), map.get("cphone").toString(), map.get("loginpw").toString());
    }

}
