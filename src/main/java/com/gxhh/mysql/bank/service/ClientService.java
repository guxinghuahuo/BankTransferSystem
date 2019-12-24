package com.gxhh.mysql.bank.service;

import com.gxhh.mysql.bank.model.Client;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface ClientService {
    List<Client> getClientList();

    Client getClientByPhone(String phone);

    void insertClient(Map<String, Object> map);
}
