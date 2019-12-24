package com.gxhh.mysql.bank.service;

import com.gxhh.mysql.bank.model.Bank;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface BankService {
    List<Bank> getBankList();

    void insertBank(Map<String, Object> map);
}
