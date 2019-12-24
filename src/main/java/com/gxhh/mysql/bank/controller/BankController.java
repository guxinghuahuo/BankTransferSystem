package com.gxhh.mysql.bank.controller;

import com.gxhh.mysql.bank.service.BankServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BankController {
    @Autowired
    private BankServiceImpl bankService;

}
