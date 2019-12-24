package com.gxhh.mysql.bank.tools;

import java.util.*;

/**
 * 卡号生成器
 * Created by ###on 2016-8-29.
 */
public class BankCardNoGenerator {

    public static final String[] VISA_PREFIX_LIST = new String[] { "4539",
            "4556", "4916", "4532", "4929", "40240071", "4485", "4716", "4" };

    public static final String[] MASTERCARD_PREFIX_LIST = new String[] { "51",
            "52", "53", "54", "55" };

    public static final String[] AMEX_PREFIX_LIST = new String[] { "34", "37" };

    public static final String[] DISCOVER_PREFIX_LIST = new String[] { "6011" };

    public static final String[] DINERS_PREFIX_LIST = new String[] { "300",
            "301", "302", "303", "36", "38" };

    public static final String[] ENROUTE_PREFIX_LIST = new String[] { "2014",
            "2149" };

    public static final String[] JCB_PREFIX_LIST = new String[] { "35" };

    public static final String[] VOYAGER_PREFIX_LIST = new String[] { "8699" };

    public static final String[] ChinaUnionPayCARD_PREFIX_LIST = new String[] { "623524","623529"}; //中国银联  19位
    public static final String[] ChinaBankCARD_PREFIX_LIST = new String[] { "621660","621661"}; //中国银行 19位
    public static final String[] ChinaPostBankCARD_PREFIX_LIST = new String[] { "622150","622151"}; //中国邮储 19位
    public static final String[] ICBCARD_PREFIX_LIST = new String[] { "620200","620302"}; //中国工商银行 18位
    public static final String[] ABCCARD_PREFIX_LIST = new String[] { "622822","622827"}; //中国农业银行 19位

    public static final String[] FOURBANKCARD_PREFIX_LIST = new String[] { "622822","622827","623524","623529","621660","621661","620200","620302","622150","622151"};

    static String strrev(String str) {
        if (str == null)
            return "";
        String revstr = "";
        for (int i = str.length() - 1; i >= 0; i--) {
            revstr += str.charAt(i);
        }

        return revstr;
    }

    /*
     * 'prefix' is the start of the CC number as a string, any number of digits.
     * 'length' is the length of the CC number to generate. Typically 13 or 16
     */
    static String completed_number(String prefix, int length) {

        String ccnumber = prefix;

        // generate digits

        while (ccnumber.length() < (length - 1)) {
            ccnumber += new Double(Math.floor(Math.random() * 10)).intValue();
        }

        // reverse number and convert to int

        String reversedCCnumberString = strrev(ccnumber);

        List<Integer> reversedCCnumberList = new Vector<Integer>();
        for (int i = 0; i < reversedCCnumberString.length(); i++) {
            reversedCCnumberList.add(new Integer(String
                    .valueOf(reversedCCnumberString.charAt(i))));
        }

        // calculate sum

        int sum = 0;
        int pos = 0;

        Integer[] reversedCCnumber = reversedCCnumberList
                .toArray(new Integer[reversedCCnumberList.size()]);
        while (pos < length - 1) {

            int odd = reversedCCnumber[pos] * 2;
            if (odd > 9) {
                odd -= 9;
            }

            sum += odd;

            if (pos != (length - 2)) {
                sum += reversedCCnumber[pos + 1];
            }
            pos += 2;
        }

        // calculate check digit

        int checkdigit = new Double(
                ((Math.floor(sum / 10) + 1) * 10 - sum) % 10).intValue();
        ccnumber += checkdigit;

        return ccnumber;

    }

    public static String[] credit_card_number(String[] prefixList, int length,
                                              int howMany) {

        Stack<String> result = new Stack<String>();
        for (int i = 0; i < howMany; i++) {
            int randomArrayIndex = (int) Math.floor(Math.random()
                    * prefixList.length);
            String ccnumber = prefixList[randomArrayIndex];
            result.push(completed_number(ccnumber, length));
        }

        return result.toArray(new String[result.size()]);
    }

    public static boolean isValidCreditCardNumber(String creditCardNumber) {
        boolean isValid = false;

        try {
            String reversedNumber = new StringBuffer(creditCardNumber)
                    .reverse().toString();
            int mod10Count = 0;
            for (int i = 0; i < reversedNumber.length(); i++) {
                int augend = Integer.parseInt(String.valueOf(reversedNumber
                        .charAt(i)));
                if (((i + 1) % 2) == 0) {
                    String productString = String.valueOf(augend * 2);
                    augend = 0;
                    for (int j = 0; j < productString.length(); j++) {
                        augend += Integer.parseInt(String.valueOf(productString
                                .charAt(j)));
                    }
                }

                mod10Count += augend;
            }

            if ((mod10Count % 10) == 0) {
                isValid = true;
            }
        } catch (NumberFormatException e) {
        }

        return isValid;
    }

    /**
     * 生成银行卡号
     * @return
     */
    public static String generateFourBankCardNumber() {

        String bankCardNo = "";
        switch ((new Random().nextInt(4)+1)){
            case 1:
                bankCardNo = credit_card_number(ChinaBankCARD_PREFIX_LIST, 19, 1)[0];//中国银行卡号
                break;
            case 2:
                bankCardNo = credit_card_number(ICBCARD_PREFIX_LIST, 18, 1)[0];//中国工商卡号
                break;
            case 3:
                bankCardNo = credit_card_number(ABCCARD_PREFIX_LIST, 19, 1)[0];//中国农业卡号
                break;
            case 4:
                bankCardNo = credit_card_number(ChinaPostBankCARD_PREFIX_LIST, 19, 1)[0];//中国邮储卡号
                break;
            default:
                bankCardNo = credit_card_number(ChinaBankCARD_PREFIX_LIST, 19, 1)[0];//中国银联卡号
                break;
        }
        return bankCardNo;
    }

    /**
     * 批量生成银行卡号
     * @param howMany
     * @return
     */
    public static List generateFourBankCardNumbers(int howMany) {
        List list = new ArrayList();
        String[] strings = credit_card_number(FOURBANKCARD_PREFIX_LIST, 18, howMany);

        for (String s:strings) {
            list.add(s);
        }

        return list;
    }

}

/*
public class GetCardNum {

    public static void main(String[] argv) {
        for (int i = 0; i <= 100; i++){
            String cardNumber = BankCardNoGenerator.generateFourBankCardNumber();
            System.out.println(cardNumber);
        }

    }
}

 */