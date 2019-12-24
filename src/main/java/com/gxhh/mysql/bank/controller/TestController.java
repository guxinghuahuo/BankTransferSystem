package com.gxhh.mysql.bank.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gxhh.mysql.bank.model.Bank;
import com.gxhh.mysql.bank.model.Bankcard;
import com.gxhh.mysql.bank.model.Client;
import com.gxhh.mysql.bank.model.Record;
import com.gxhh.mysql.bank.service.BankServiceImpl;
import com.gxhh.mysql.bank.service.BankcardServiceImpl;
import com.gxhh.mysql.bank.service.ClientServiceImpl;
import com.gxhh.mysql.bank.service.RecordServiceImpl;
import com.gxhh.mysql.bank.tools.BCSha3Utils;
import com.gxhh.mysql.bank.tools.BankCardNoGenerator;
import com.gxhh.mysql.bank.tools.IdCardGenerator;
import com.gxhh.mysql.bank.tools.RandomClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class TestController {
    @Autowired
    private ClientServiceImpl clientService;
    @Autowired
    private BankcardServiceImpl bankcardService;
    @Autowired
    private RecordServiceImpl recordService;
    @Autowired
    private BankServiceImpl bankService;

    private Integer number;
    private Client[] client;

    @RequestMapping(value = "/test", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView addLotsOfEntityRandomly(@RequestParam(value = "test_entity", required = false) String entity,
                                                @RequestParam(value = "test_number", required = false) Integer number) {
        this.number = number;
        switch (entity) {
            case "client":
                addClientRandomly();
                break;
            case "bankcard":
                addBankcardRandomly();
                break;
            case "record":
                addRecordRandomly();
                break;
            case "selectclient":
                selectClientByPhone();
                break;
            case "addselectclient":
                addClientAndSelect();
                break;
        }
        return showTest();
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public ModelAndView showTest() {
        return new ModelAndView();
    }

    private void addClientRandomly() {
        try {
            //打开日志文件
            FileWriter fw = new FileWriter("src/main/java/com/gxhh/mysql/bank/log/test.log", true);
            BufferedWriter bw = new BufferedWriter(fw);
            FileWriter fwd = new FileWriter("src/main/java/com/gxhh/mysql/bank/log/duplication.log", true);
            BufferedWriter bwd = new BufferedWriter(fwd);
            Date date = new Date();
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            bwd.write("**************************************************************");
            bwd.newLine();
            bwd.write(sdf.format(date) + " 开始测试...");
            bwd.newLine();
            bw.write("**************************************************************");
            bw.newLine();
            bw.write(sdf.format(date) + " 开始测试...");
            bw.newLine();
            System.out.println("开始测试...");
            System.out.println("正在随机生成测试数据...");
            bw.write("正在随机生成测试数据...");
            bw.newLine();
            //计时器
            long startTime = System.currentTimeMillis();

            //生成测试用随机数据
            client = new Client[number];
            IdCardGenerator idCardGenerator = new IdCardGenerator();
            String[] idcard = new String[number];
            String defaultPW = "000000";
            final int count = 10000; //每count条数据记录一次日志
            for (int i = 0; i < number; i++) {
                idcard[i] = idCardGenerator.generate();
                Map<String, Object> map = RandomClient.getClient();
                client[i] = new Client();
                client[i].setCname(map.get("name").toString());
                client[i].setSex(map.get("sex").toString());
                client[i].setCphone(map.get("tel").toString());
                client[i].setIdcard(idcard[i]);
                Integer age = 2019 - Integer.parseInt(idcard[i].substring(6, 10));
                client[i].setAge(age);
                byte[] bytes = (map.get("tel").toString() + defaultPW).getBytes(StandardCharsets.UTF_8);
                String loginpw_sha3 = BCSha3Utils.sha3512(bytes);
                client[i].setLoginpw(loginpw_sha3);
                if ((i + 1) % count == 0) {
                    long nowTime = System.currentTimeMillis();
                    String s = String.valueOf((float) (i + 1) / number * 100);
                    bw.write(" 进度：" + s.substring(0, s.indexOf('.') + 2) + "%" + "耗时：" + (float)(nowTime - startTime)/1000 + "s");
                    bw.newLine();
                }
            }
            long endTime = System.currentTimeMillis();
            bw.write("随机数据生成完毕，耗时：" + (float)(endTime - startTime)/1000 + "s");
            bw.newLine();
            System.out.println("随机数据生成完毕");
            System.out.println("现在开始多线程向数据库中插入数据...");
            bw.write("现在开始多线程向数据库中插入数据...");
            bw.newLine();

            //多线程插入数据
            Integer old = number;
            final int routine = 10; //线程数
            AtomicInteger duplication = new AtomicInteger();
            startTime = System.currentTimeMillis();
            Thread[] threads = new Thread[routine];
            for (int i = 1; i <= routine; i++) {
                long finalStartTime = startTime;
                threads[i - 1] = new Thread(() -> {
                    while (number > 0) {
                        Map<String, Object> map = new HashMap<>();
                        synchronized (bwd) {
                            number--;
                            if (number < 0) {
                                break;
                            }
                            if (number % 1000 == 0) {
                                String s = String.valueOf((float) (old - number) / old * 100);
                                System.out.println((old - number) + "/" + old + " 进度：" + s.substring(0, s.indexOf('.') + 2) + "%");
                            }
                            map.put("cname", client[number].getCname());
                            map.put("idcard", client[number].getIdcard());
                            map.put("cage", client[number].getCage());
                            map.put("csex", client[number].getCsex());
                            map.put("cphone", client[number].getCphone());
                            map.put("loginpw", client[number].getLoginpw());
                            try {
                                clientService.insertClient(map);
                            }catch (Exception e) {
                                try {
                                    bwd.write("产生冲突，冲突内容如下：");
                                    bwd.write(e.getMessage());
                                    bwd.newLine();
                                    bwd.newLine();
                                    duplication.getAndIncrement();
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                        synchronized (bw){
                            if ((old - number) % count == 0) {
                                long nowTime = System.currentTimeMillis();
                                String s = String.valueOf((float) (old - number) / old * 100);
                                try {
                                    bw.write(" 进度：" + s.substring(0, s.indexOf('.') + 2) + "%" + "耗时：" + (float)(nowTime - finalStartTime)/1000 + "s");
                                    bw.newLine();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }, "线程" + i);
            }
            for (int i = 0; i < routine; i++){
                threads[i].start();
            }
            for (int i = 0; i < routine; i++){
                threads[i].join();
            }
            endTime = System.currentTimeMillis();
            number = old;
            bw.write(number + "条数据插入完毕，耗时：" + (float)(endTime - startTime)/1000 + "s");
            bw.newLine();
            bw.write("本次测试完毕...");
            bw.newLine();
            bw.newLine();
            bw.newLine();
            bwd.write("本次测试共产生" + duplication + "条冲突数据");
            bwd.newLine();
            bwd.write("本次测试完毕...");
            bwd.newLine();
            bwd.newLine();
            bwd.newLine();
            System.out.println("本次测试完毕...");
            System.out.println();
            bwd.close();
            fwd.close();
            bw.close();
            fw.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void addBankcardRandomly() {
        System.out.println("开始测试...");
        System.out.println("正在随机生成测试数据...");

        //生成测试用随机数据
        List<Client> clients = clientService.getClientList();
        List<Bank> banks = bankService.getBankList();
        Bankcard[] bankcards = new Bankcard[number];
        Random random = new Random();
        String defaultPW = "111111";
        for (int i = 0; i < number; i++) {
            String cardnum = BankCardNoGenerator.generateFourBankCardNumber();
            bankcards[i] = new Bankcard();
            bankcards[i].setCardnum(new BigInteger(cardnum));
            if (i <= number / 3){
                bankcards[i].setBalance(random.nextDouble() * 10000);
            } else if (i > number / 3 && i <= number * 5 / 6){
                bankcards[i].setBalance(random.nextDouble() * 100000);
            } else {
                bankcards[i].setBalance(random.nextDouble() * 1000000);
            }
            bankcards[i].setBname(banks.get(random.nextInt(banks.size())).getBname());
            bankcards[i].setCno(clients.get(random.nextInt(clients.size())).getCno());
            byte[] bytes = (cardnum + defaultPW).getBytes(StandardCharsets.UTF_8);
            String paypw_sha3 = BCSha3Utils.sha3512(bytes);
            bankcards[i].setBpw(paypw_sha3);
        }
        System.out.println("随机数据生成完毕");
        System.out.println("现在开始多线程向数据库中插入数据...");
        //多线程插入数据
        Integer old = number;

        final int routine = 1; //线程数
        //插入线程
        Thread[] insertThreads = new Thread[routine];
        for (int i = 1; i <= routine; i++) {
            insertThreads[i - 1] = new Thread(() -> {
                while (number > 0) {
                    Map<String, Object> map = new HashMap<>();
                    synchronized (this) {
                        number--;
                        if (number < 0) {
                            break;
                        }
                        if (number % 1000 == 0) {
                            System.out.println((old - number) + "/" + old + " 进度：" + String.format("%.1f", ((float) (old - number) / old * 100)) + "%");
                        }
                        map.put("cardnum", bankcards[number].getCardnum());
                        map.put("balance", bankcards[number].getBalance());
                        map.put("bname", bankcards[number].getBname());
                        map.put("bpw", bankcards[number].getBpw());
                        map.put("cno", bankcards[number].getCno());
                        try {
                            bankcardService.insertBankcard(map);
                        } catch (Exception ignored) {
                        }
                    }
                }
            }, "线程" + i);
        }
        for (int i = 0; i < routine; i++){
            insertThreads[i].start();
        }
        for (int i = 0; i < routine; i++){
            try {
                insertThreads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("本次测试完毕...");
        System.out.println();
    }

    private void addRecordRandomly() {
        System.out.println("开始测试...");
        System.out.println("正在随机生成测试数据...");

        //生成测试用随机数据

        List<Bankcard> bankcards = bankcardService.getBankcardList();
        Record[] records = new Record[number];
        Random random = new Random();
        for (int i = 0; i < number; i++) {
            Bankcard scard = bankcards.get(random.nextInt(bankcards.size()));
            Bankcard rcard = bankcards.get(random.nextInt(bankcards.size()));
            records[i] = new Record();
            records[i].setScardnum(scard.getCardnum());
            records[i].setRcardnum(rcard.getCardnum());
            int max = (int)scard.getBalance().doubleValue();
            records[i].setMoney(random.nextDouble() * max);
            String date = (random.nextInt(1) + 2019) + "/" + (random.nextInt(12) + 1) + "/" + (random.nextInt(18) + 1);
            records[i].setTime(date);
        }
        //根据日期从远到近排序
        Arrays.sort(records, new Comparator<Record>() {
            @Override
            public int compare(Record r1, Record r2) {
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
            }
        });
        System.out.println("随机数据生成完毕");
        System.out.println("现在开始多线程向数据库中插入数据...");
        //多线程插入数据
        Integer old = number;

        final int routine = 1; //线程数
        //插入线程
        Thread[] insertThreads = new Thread[routine];
        for (int i = 1; i <= routine; i++) {
            insertThreads[i - 1] = new Thread(() -> {
                while (number > 0) {
                    Map<String, Object> map = new HashMap<>();
                    synchronized (this) {
                        number--;
                        if (number < 0) {
                            break;
                        }
                        if (number % 1000 == 0) {
                            System.out.println((old - number) + "/" + old + " 进度：" + String.format("%.1f", ((float) (old - number) / old * 100)) + "%");
                        }
                        map.put("scardnum", records[number].getScardnum());
                        map.put("rcardnum", records[number].getRcardnum());
                        map.put("money", records[number].getMoney());
                        map.put("time", records[number].getTime());
                        try {
                            recordService.insertRecord(map);
                        } catch (Exception ignored) {
                        }
                    }
                }
            }, "线程" + i);
        }
        for (int i = 0; i < routine; i++){
            insertThreads[i].start();
        }
        for (int i = 0; i < routine; i++){
            try {
                insertThreads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("本次测试完毕...");
        System.out.println();
    }

    private void selectClientByPhone(){
        try {
            //打开日志文件
            FileWriter fw = new FileWriter("src/main/java/com/gxhh/mysql/bank/log/testselectooo.log", true);
            BufferedWriter bw = new BufferedWriter(fw);
            Date date = new Date();
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            bw.write("**************************************************************");
            bw.newLine();
            bw.write(sdf.format(date) + " 开始测试...");
            bw.newLine();
            bw.write("查询测试");
            bw.newLine();
            System.out.println("开始测试...");
            System.out.println("初始化数据中...");

            List<Client> clients = clientService.getClientList();
            int min = Math.min(number, clients.size());
            String[] phone = new String[min];
            for (int i = 0; i < min; i++) {
                phone[i] = clients.get((i + (int)(Math.random() * min * 10)) % min).getCphone();
            }

            System.out.println("数据初始化完毕");
            Double[] difTime = new Double[clients.size()];

            //计时器
            long startTime = System.currentTimeMillis();
            for (int i = 0; i < min; i++){
                clientService.getClientByPhone(phone[i]);
                long endTime = System.currentTimeMillis();
                difTime[i] = (double)(endTime - startTime)/1000;
                if (i % 1000 == 0) {
                    System.out.println(i + "/" + min);
                }
            }
            bw.write(min + "条数据读入完毕，耗时：" + difTime[min - 1] + "s");
            bw.newLine();
            bw.write("本次测试完毕...");
            bw.newLine();
            bw.newLine();
            bw.newLine();

            //数据保存为JSON格式
            FileWriter fwjs = new FileWriter("src/main/java/com/gxhh/mysql/bank/log/not_has_time.json");
            BufferedWriter bwjs = new BufferedWriter(fwjs);
            Gson gson = new GsonBuilder().create();

            Map<Integer, Double> map = new LinkedHashMap<Integer, Double>();

            for (int i = 0; i < min; i += 1000) {
                map.put(i, difTime[i]);
            }
            String timeJson = gson.toJson(map);
            bwjs.write(timeJson);
            bwjs.close();
            fwjs.close();
            System.out.println("本次测试完毕...");
            System.out.println();
            bw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addClientAndSelect() {
        try {
            //打开日志文件
            FileWriter fw = new FileWriter("src/main/java/com/gxhh/mysql/bank/log/testinsertselect.log", true);
            BufferedWriter bw = new BufferedWriter(fw);
            Date date = new Date();
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            bw.write("**************************************************************");
            bw.newLine();
            bw.write(sdf.format(date) + " 开始测试...");
            bw.newLine();


            System.out.println("开始测试...");
            System.out.println("正在随机生成测试数据...");
            //计时器

            //生成测试用随机数据
            client = new Client[number];
            IdCardGenerator idCardGenerator = new IdCardGenerator();
            String[] idcard = new String[number];
            String defaultPW = "000000";
            final int count = 10000; //每count条数据记录一次日志
            for (int i = 0; i < number; i++) {
                idcard[i] = idCardGenerator.generate();
                Map<String, Object> map = RandomClient.getClient();
                client[i] = new Client();
                client[i].setCname(map.get("name").toString());
                client[i].setSex(map.get("sex").toString());
                client[i].setCphone(map.get("tel").toString());
                client[i].setIdcard(idcard[i]);
                Integer age = 2019 - Integer.parseInt(idcard[i].substring(6, 10));
                client[i].setAge(age);
                byte[] bytes = (map.get("tel").toString() + defaultPW).getBytes(StandardCharsets.UTF_8);
                String loginpw_sha3 = BCSha3Utils.sha3512(bytes);
                client[i].setLoginpw(loginpw_sha3);
            }
            System.out.println("随机数据生成完毕");
            System.out.println("现在开始多线程向数据库中插入数据...");
            bw.write("现在开始多线程向数据库中插入数据...");
            bw.newLine();
            //多线程插入数据
            Integer old = number;

            final int routine = 10; //线程数
            Double[] insertTime = new Double[old];
            final int[] insertCount = {0};
            long startTime = System.currentTimeMillis();
            //插入线程
            Thread[] insertThreads = new Thread[routine];
            for (int i = 1; i <= routine; i++) {
                insertThreads[i - 1] = new Thread(() -> {
                    while (number > 0) {
                        Map<String, Object> map = new HashMap<>();
                        synchronized (this) {
                            number--;
                            if (number < 0) {
                                break;
                            }
                            if (number % 1000 == 0) {
                                System.out.println((old - number) + "/" + old + " 进度：" + String.format("%.1f", ((float) (old - number) / old * 100)) + "%");
                            }
                            map.put("cname", client[number].getCname());
                            map.put("idcard", client[number].getIdcard());
                            map.put("cage", client[number].getCage());
                            map.put("csex", client[number].getCsex());
                            map.put("cphone", client[number].getCphone());
                            map.put("loginpw", client[number].getLoginpw());
                            try {
                                clientService.insertClient(map);
                            } catch (Exception ignored) {
                            }
                            if ((old - number) % count == 0) {
                                long nowTime = System.currentTimeMillis();
                                insertTime[insertCount[0]] = (double)(nowTime - startTime) / 1000;
                                insertCount[0]++;
                            }
                        }
                    }
                }, "线程" + i);
            }

            bw.write("查询测试");
            bw.newLine();
            System.out.println("开始查询...");
            Double[] selectTime = new Double[old];
            Integer[] selectData = new Integer[old];
            final int[] selectCount = {0};

            Timer timer = new Timer();
            long delay = 0;
            long intervalPeriod = 1000;
            for (int i = 0; i < routine; i++){
                insertThreads[i].start();
            }
            TimerTask selectTask = new TimerTask() {
                @Override
                public void run() {
                    System.out.println("执行第" + (selectCount[0] + 1) + "次查询...");
                    long selectStartTime = System.currentTimeMillis();
                    selectData[selectCount[0]] = old - number;
                    try {
                        clientService.getClientByPhone(client[(int) (Math.random() * 10 * old) % (old - number)].getCphone());
                    } catch (Exception ignored){
                    }
                    long selectEndTime = System.currentTimeMillis();
                    selectTime[selectCount[0]] = (double) (selectEndTime - selectStartTime) / 1000;
                    selectCount[0]++;
                }
            };
            timer.scheduleAtFixedRate(selectTask, delay, intervalPeriod);

            for (int i = 0; i < routine; i++){
                insertThreads[i].join();
            }
            selectTask.cancel();
            timer.cancel();
            long endTime = System.currentTimeMillis();

            //查询时间保存为JSON格式
            FileWriter fwsjs = new FileWriter("src/main/java/com/gxhh/mysql/bank/log/select_all_time.json");
            BufferedWriter bwsjs = new BufferedWriter(fwsjs);
            Gson sgson = new GsonBuilder().create();
            Map<Integer, Double> smap = new LinkedHashMap<Integer, Double>();
            for (int i = 1; i < selectCount[0]; i ++) {
                smap.put(selectData[i], selectTime[i]);
            }
            String timeJson = sgson.toJson(smap);
            bwsjs.write(timeJson);
            bwsjs.close();
            fwsjs.close();

            //插入时间保存为JSON格式
            FileWriter fwijs = new FileWriter("src/main/java/com/gxhh/mysql/bank/log/insert_all_time.json");
            BufferedWriter bwijs = new BufferedWriter(fwijs);
            Gson igson = new GsonBuilder().create();
            Map<Integer, Double> imap = new LinkedHashMap<Integer, Double>();
            for (int i = 1; i < insertCount[0]; i++) {
                imap.put(i * 1000, insertTime[i] - insertTime[i - 1]);
            }
            String iTimeJson = igson.toJson(imap);
            bwijs.write(iTimeJson);
            bwijs.close();
            fwijs.close();

            bw.write("测试共消耗" + (float)(endTime - startTime)/1000 + "s");
            bw.newLine();;
            number = old;
            bw.write("本次测试完毕...");
            bw.newLine();
            bw.newLine();
            bw.newLine();
            System.out.println("本次测试完毕...");
            System.out.println();
            bw.close();
            fw.close();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

}
