package com.gxhh.mysql.bank.service;

import com.gxhh.mysql.bank.model.Bankcard;
import com.gxhh.mysql.bank.model.Client;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@Service
public interface BankcardService {
    List<Bankcard> getBankcardList();

    List<Bankcard> getBankcardListByClient(Client client);

    Bankcard getBankcardByCardnum(BigInteger cardnum);

    void insertBankcard(Map<String, Object> map);
}
