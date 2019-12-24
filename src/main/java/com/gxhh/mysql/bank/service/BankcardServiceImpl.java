package com.gxhh.mysql.bank.service;

import com.gxhh.mysql.bank.dao.BankcardMapper;
import com.gxhh.mysql.bank.model.Bankcard;
import com.gxhh.mysql.bank.model.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@Repository
public class BankcardServiceImpl implements BankcardService{
    @Autowired
    private BankcardMapper bankcardMapper;

    @Override
    public List<Bankcard> getBankcardList() {
        return bankcardMapper.getBankcardList();
    }

    @Override
    public List<Bankcard> getBankcardListByClient(Client client) {
        return bankcardMapper.getBankcardListByCno(client.getCno());
    }

    @Override
    public Bankcard getBankcardByCardnum(BigInteger cardnum) {
        return bankcardMapper.getBankcardByCardnum(cardnum);
    }

    @Override
    public void insertBankcard(Map<String, Object> map) {
        bankcardMapper.insertBankcard(new BigInteger(map.get("cardnum").toString()), Double.parseDouble(map.get("balance").toString()), map.get("bpw").toString(), map.get("bname").toString(), Integer.parseInt(map.get("cno").toString()));
    }
}
