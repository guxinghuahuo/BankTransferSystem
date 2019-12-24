package com.gxhh.mysql.bank.service;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface TransferService {

    void callTransfer(Map<String, Object> map);
}
