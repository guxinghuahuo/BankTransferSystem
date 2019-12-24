package com.gxhh.mysql.bank.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gxhh.mysql.bank.model.Bank;
import com.gxhh.mysql.bank.model.Bankcard;
import com.gxhh.mysql.bank.model.Client;
import com.gxhh.mysql.bank.model.Record;
import com.gxhh.mysql.bank.service.BankcardServiceImpl;
import com.gxhh.mysql.bank.service.ClientServiceImpl;
import com.gxhh.mysql.bank.service.RecordServiceImpl;
import com.gxhh.mysql.bank.service.TransferServiceImpl;
import com.gxhh.mysql.bank.tools.BCSha3Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Controller
public class LoginController {
    @Autowired
    private ClientServiceImpl clientService;

    @Autowired
    private BankcardServiceImpl bankcardService;

    @Autowired
    private RecordServiceImpl recordService;

    @Autowired
    private TransferServiceImpl transferService;

    private Client nowClient;
    private List<Bankcard> nowBankcards;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @ResponseBody
    public String login(@RequestParam(value = "client_phone", required = false) String phone,
                      @RequestParam(value = "client_loginpw", required = false) String loginpw) {
        byte[] bytes = (phone + loginpw).getBytes(StandardCharsets.UTF_8);
        String input_login_sha3 = BCSha3Utils.sha3512(bytes);
        Client client = clientService.getClientByPhone(phone);
        if (client != null){
            String client_loginpw = client.getLoginpw();
            if (input_login_sha3.equals(client_loginpw)) {
                nowClient = client;
                Gson gson = new GsonBuilder().create();
                Map<String, String> map = new LinkedHashMap<String, String>();
                map.put("Status", "1");
                map.put("msg", "登录成功！");
                return gson.toJson(map);
            }
        }
        Gson gson = new GsonBuilder().create();
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("Status", "-1");
        map.put("msg", "用户名或密码不正确！");
        return gson.toJson(map);
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView showLogin() {
        return new ModelAndView();
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView showIndex() {
        return new ModelAndView("index");
    }

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String showHome() {
        if(nowClient==null){
            return "index";
        }
        return "home";
    }

    @RequestMapping(value = "/information")
    public ModelAndView showInformation(@RequestParam(value = "pagenum", required = false) String pagenum) {
        if(nowClient==null){
            return new ModelAndView("index");
        }
        int page = Integer.parseInt(pagenum);
        nowBankcards = bankcardService.getBankcardListByClient(nowClient);
        List<List<Record>> nowRecords = new LinkedList<>();
        int recordcount = 0;
        for (int i = 0; i < nowBankcards.size(); i++) {
            nowRecords.add(recordService.getRecordListByBankcard(nowBankcards.get(i)));
            recordcount += nowRecords.get(i).size();
        }
        Record[] records = new Record[recordcount];
        int k = 0;
        for (List<Record> nowRecord : nowRecords) {
            for (Record record : nowRecord) {
                records[k++] = record;
            }
        }
        //按日期排序
        Arrays.sort(records, (r1, r2) -> {
            String time1 = r1.getTime();
            String time2 = r2.getTime();
            int year1 = Integer.parseInt(time1.substring(0, time1.indexOf('/')));
            int year2 = Integer.parseInt(time2.substring(0, time2.indexOf('/')));
            if (year1 != year2) {
                return year2 - year1;
            }
            time1 = time1.substring(time1.indexOf('/') + 1);
            time2 = time2.substring(time2.indexOf('/') + 1);
            int month1 = Integer.parseInt(time1.substring(0, time1.indexOf('/')));
            int month2 = Integer.parseInt(time2.substring(0, time2.indexOf('/')));
            if (month1 != month2) {
                return month2 - month1;
            }
            time1 = time1.substring(time1.indexOf('/') + 1);
            time2 = time2.substring(time2.indexOf('/') + 1);
            int day1 = Integer.parseInt(time1);
            int day2 = Integer.parseInt(time2);
            return day2 - day1;
        });
        List<Record> showRecords = new LinkedList<>(Arrays.asList(records).subList(20 * (page - 1), Math.min(20 * page, records.length)));
        int maxPage = recordcount/20;
        if (recordcount % 20 != 0){
            maxPage++;
        }
        ModelAndView model = new ModelAndView("information");
        model.addObject("name", nowClient.getCname());
        model.addObject("idcard", nowClient.getIdcard());
        model.addObject("sex", nowClient.getCsex());
        model.addObject("age", nowClient.getCage());
        model.addObject("phone", nowClient.getCphone());
        model.addObject("bankcards", nowBankcards);
        model.addObject("records", showRecords);
        model.addObject("page", maxPage);
        return model;
    }

    @RequestMapping(value = "/transfer", method = RequestMethod.POST)
    @ResponseBody
    public String transfer(@RequestParam(value = "scardnum", required = false) String scardnum,
                                 @RequestParam(value = "rcardnum", required = false) String rcardnum,
                                 @RequestParam(value = "money", required = false) String money,
                                 @RequestParam(value = "bpw", required = false) String bpw,
                                 @RequestParam(value = "note", required = false) String note) {
        if(nowClient==null){
            return "index";
        }
        nowBankcards = bankcardService.getBankcardListByClient(nowClient);

        Bankcard thisBankcard = new Bankcard();

        boolean flag = false;
        for (Bankcard nowBankcard : nowBankcards) {
            if (scardnum.equals(nowBankcard.getCardnum().toString())) {
                thisBankcard = nowBankcard;
                flag = true;
                break;
            }
        }

        Bankcard recBankcard;
        recBankcard = bankcardService.getBankcardByCardnum(new BigInteger(rcardnum));

        if (!flag || recBankcard == null){
            //输入的卡号不存在
            Gson gson = new GsonBuilder().create();
            Map<String, String> map = new LinkedHashMap<String, String>();
            map.put("Status", "-1");
            map.put("msg", "输入的卡号不存在，请检查输入的银行卡号！");
            return gson.toJson(map);
        }


        byte[] bytes = (scardnum + bpw).getBytes(StandardCharsets.UTF_8);
        String input_pay_sha3 = BCSha3Utils.sha3512(bytes);
        if (!input_pay_sha3.equals(thisBankcard.getBpw().toString())) {
            //输入的支付密码不对
            Gson gson = new GsonBuilder().create();
            Map<String, String> map = new LinkedHashMap<String, String>();
            map.put("Status", "-2");
            map.put("msg", "输入的银行卡密码不正确，请重新输入！");
            return gson.toJson(map);
        }

        if (Double.parseDouble(money) <= 0) {
            //输入的金额格式不对
            Gson gson = new GsonBuilder().create();
            Map<String, String> map = new LinkedHashMap<String, String>();
            map.put("Status", "-3");
            map.put("msg", "输入的金额格式不正确，请检查金额是否大于0！");
            return gson.toJson(map);
        }

        if (thisBankcard.getBalance() - Double.parseDouble(money) < 0) {
            //银行卡的余额不足
            Gson gson = new GsonBuilder().create();
            Map<String, String> map = new LinkedHashMap<String, String>();
            map.put("Status", "-4");
            map.put("msg", "该银行卡余额不足！");
            return gson.toJson(map);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("scardnum", new BigInteger(scardnum));
        map.put("rcardnum", new BigInteger(rcardnum));
        map.put("money", Double.parseDouble(money));
        map.put("note", note);

        transferService.callTransfer(map);

        //转账成功
        Gson gson = new GsonBuilder().create();
        Map<String, String> smap = new LinkedHashMap<String, String>();
        smap.put("Status", "1");
        smap.put("msg", "转账成功！");
        return gson.toJson(smap);
    }

    @RequestMapping(value = "/transfer", method = RequestMethod.GET)
    public ModelAndView showTransfer() {
        if(nowClient==null){
            return new ModelAndView("index");
        }
        nowBankcards = bankcardService.getBankcardListByClient(nowClient);
        ModelAndView model = new ModelAndView("transfer");
        model.addObject("bankcards", nowBankcards);
        return model;
    }
}
