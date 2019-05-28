package com.common.util;

import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class StringUtil {

    // email
    private final static Pattern emailer = Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");

    // phone
    private final static Pattern phoner = Pattern.compile("^(13[0-9]|14[57]|15[0-35-9]|17[6-8]|18[0-9])[0-9]{8}$");

    // alipay name
    private final static Pattern alipayName = Pattern.compile("^[\\u4e00-\\u9fa5]+$");

    /**
     * if string is empty
     *
     * @param input
     * @return boolean
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * if email is valid
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        if (email == null || email.trim().length() == 0)
            return false;
        return emailer.matcher(email).matches();
    }

    public static boolean isAlipayName(String name) {
        if (isEmpty(name)) {
            return false;
        }
        return alipayName.matcher(name).matches();
    }

    /**
     * 半角转换为全角
     *
     * @param input
     * @return
     */
    public static String toDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    /**
     * encrypt phone number
     *
     * @param phone
     * @return
     */
    public static String encryptPhoneNumber(String phone) {
        if (phone == null) {
            return "*****";
        }
        if (phone.length() < 11) {
            return phone;
        }
        return phone.substring(0, 3) + "*****" + phone.substring(8, 11);
    }

    /**
     * convert user credit log's address
     *
     * @param address
     * @return
     */
    public static String convertUserDeliveryAddress(String address) {
        String[] info = address.split("#");
        StringBuffer text = new StringBuffer();
        text.append(info[1]) // province
                .append(info[2]) // city
                .append(info[3]) // zone
                .append(info[4]) // street
                .append("\n").append(info[0]) // name
                .append(" ").append(info[6]) // phone
        ;

        return text.toString();
    }

    public static boolean contains(String targetStr, String subStr) {
        if (isEmpty(targetStr) || isEmpty(subStr)) {
            return false;
        }
        return targetStr.contains(subStr);
    }

    public static String creditFormat(String creditStr) {
        StringBuffer buffer = new StringBuffer();
        if (creditStr.contains(".")) {
            int a = creditStr.indexOf(".");
            String string1 = creditStr.substring(0, a);
            String string2 = creditStr.substring(a + 1, creditStr.length());

            if (string2.length() == 1) {
                string2 = string2 + "0";
            } else {
                string2 = string2.substring(0, 2);
            }
            buffer = buffer.append(string1).append(".").append(string2);

        } else {
            buffer = buffer.append(creditStr).append(".00");
        }

        return buffer.toString();
    }

    public static String creditFormat(String creditStr, int floatDigit) {
        StringBuffer buffer = new StringBuffer();
        if (creditStr.contains(".")) {
            int a = creditStr.indexOf(".");
            String string1 = creditStr.substring(0, a);
            String string2 = creditStr.substring(a + 1, creditStr.length());

            for (int i = string2.length(); i < floatDigit; i++) {
                string2 = string2 + "0";
            }
            string2 = string2.substring(0, floatDigit);
            buffer = buffer.append(string1).append(".").append(string2);

        } else {
            buffer.append(creditStr).append(".");
            for (int i = 0; i < floatDigit; i++) {
                buffer = buffer.append("0");
            }
        }
        return buffer.toString();
    }

    public static String creFormat(String creditStr) {
        StringBuffer buffer = new StringBuffer();
        if (creditStr.contains(".")) {
            int a = creditStr.indexOf(".");
            String string1 = creditStr.substring(0, a);
            String string2 = creditStr.substring(a + 1, creditStr.length());

            if (string2.length() == 1) {
                string2 = string2 + "0";
            } else {
                string2 = string2.substring(0, 2);
            }
            if (Integer.parseInt(string2) > 0) {
                buffer = buffer.append(string1).append(".").append(string2);
            } else {
                buffer = buffer.append(string1);
            }

        } else {
            buffer = buffer.append(creditStr).append("");
        }

        return buffer.toString();
    }

    public static boolean isPhoneNum(String phoneNum) {

        if (phoneNum == null || phoneNum.trim().length() == 0)
            return false;
        return phoner.matcher(phoneNum).matches();
    }

    public static boolean isMobileNO(String mobiles) {
        boolean flag = false;
        try {
            mobiles = mobiles.replace(" ", "");
            mobiles = mobiles.replace("+86", "");
            Pattern p = Pattern.compile("^((1[0-9]))\\d{9}$");
            Matcher m = p.matcher(mobiles);
            flag = m.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    public static String currencyFormat(int credit) {
        NumberFormat nf = NumberFormat.getInstance();
        return StringUtil.creditFormat(nf.format(credit / 100.00f));
    }

    public static String currencyFormat(int credit, int floatDigit) {
        NumberFormat nf = NumberFormat.getInstance();
        return StringUtil.creditFormat(nf.format(credit / 100.00f), floatDigit);
    }

    public static String curFormat(int credit) {
        NumberFormat nf = NumberFormat.getInstance();
        return StringUtil.creFormat(nf.format(credit));
    }

    public static String specialLettersFilter(String str) throws PatternSyntaxException {
        String regEx = "[/\\！@#￥……~·——+{}：“’《》？/。，‘；】【=-·~&*（）*?<>|\"\n\t`~!@#$%^&*()-_=+{\\[\\]}:;,.]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    public static boolean isValidStr(String str) {
        if (isEmpty(str)) {
            return true;
        }
        byte[] nameBytes = str.getBytes();
        for (int i = 0; i < nameBytes.length; i += 3) {
            if ((nameBytes[i] & 0xF8) == 0xF0) {
                return false;
            }
        }
        return true;
    }

    public static String replace(int from, int to, String source, String element) {
        if (isEmpty(source) || isEmpty(element) || from < 0 || to < 0 || from > to || from > source.length()
                || to > source.length())
            return null;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(from == 0 ? "" : source.substring(0, from));
        stringBuilder.append(element);
        stringBuilder.append(to == source.length() ? "" : source.substring(to, source.length()));
        return stringBuilder.toString();
    }


    public static Spanned colorPartiallyString(String content, String colorString, int color) {
        int start = content.indexOf(colorString);
        int end = start + colorString.length();
        String result = "";
        if (start >= 0) {
            if (start > 0) {
                result += content.substring(0, start);
            }
            String colorStr = Integer.toHexString(color);
            if (colorStr.length() > 6) {
                colorStr = colorStr.substring(2);
            }
            result += "<font color='#" + colorStr + "'>" + content.substring(start, end) + "</font>";
            if (end < content.length()) {
                result += content.substring(end);
            }
            return Html.fromHtml(result);

        } else {
            return Html.fromHtml(content);
        }
    }

    public static String dateToString(Date currentTime, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    public static Date stringToDate(String strDate, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    public static boolean equals(String left, String right) {
        if (!StringUtil.isEmpty(left)) {
            return left.equals(right);
        } else if (!StringUtil.isEmpty(right)) {
            return false;
        }
        return true;
    }

    public static String addSplitEveryChar(String content, String splite) {
        if (TextUtils.isEmpty(content)) {
            return content;
        }
        StringBuilder desBuilder = new StringBuilder();
        char[] contentChars = content.toCharArray();
        for (int i = 0; i < contentChars.length; i++) {
            desBuilder.append(contentChars[i]);
            desBuilder.append(splite);
        }
        return desBuilder.substring(0, desBuilder.length() - 1).toString();
    }

    public static String insertSpacingToString(String content, int smallNum) {
        if (TextUtils.isEmpty(content)) {
            return content;
        }

        StringBuilder dividerBuilder = new StringBuilder();
        for (int j = 0; j <= 2 * smallNum; j++) {
            if (j == smallNum) {
                dividerBuilder.append(" ");
            } else if (j < smallNum) {
                dividerBuilder.append("<small>");
            } else {
                dividerBuilder.append("</small>");
            }

        }
        String dividerStr = dividerBuilder.toString();

        StringBuilder contentBuilder = new StringBuilder();
        char[] contentChars = content.toCharArray();
        for (int i = 0; i < contentChars.length; i++) {
            contentBuilder.append(contentChars[i]);
            if (i < contentChars.length - 1) {
                contentBuilder.append(dividerStr);
            }
        }
        return contentBuilder.toString();
    }

    public static String formatNoDigitIfCan(int credit) {
        return formatNoDigitIfCan(credit, 100f);
    }

    public static String formatNoDigitIfCan(int num, float base) {
        NumberFormat nf = NumberFormat.getInstance();
        String strNum = StringUtil.creditFormat(nf.format(num / base));
        if ("0.00".equals(strNum)) {
            return "0";
        }
        while (strNum.contains(".") && strNum.endsWith("0")) {
            strNum = strNum.substring(0, strNum.length() - 1);
        }
        if (strNum.endsWith(".")) {
            return strNum.substring(0, strNum.length() - 1);
        }
        return strNum;
    }

    public static String escapeJava(String str) {
        return escapeJavaStyleString(str, false, false);
    }

    private static String escapeJavaStyleString(String str, boolean escapeSingleQuotes, boolean escapeForwardSlash) {
        if (str == null) {
            return null;
        } else {
            try {
                StringWriter ioe = new StringWriter(str.length() * 2);
                escapeJavaStyleString(ioe, str, escapeSingleQuotes, escapeForwardSlash);
                return ioe.toString();
            } catch (IOException var4) {
            }
        }
        return null;
    }

    private static void escapeJavaStyleString(Writer out, String str, boolean escapeSingleQuote,
                                              boolean escapeForwardSlash) throws IOException {
        if (out == null) {
            throw new IllegalArgumentException("The Writer must not be null");
        } else if (str != null) {
            int sz = str.length();

            for (int i = 0; i < sz; ++i) {
                char ch = str.charAt(i);
                if (ch > 4095) {
                    out.write("\\u" + hex(ch));
                } else if (ch > 255) {
                    out.write("\\u0" + hex(ch));
                } else if (ch > 127) {
                    out.write("\\u00" + hex(ch));
                } else if (ch < 32) {
                    switch (ch) {
                        case '\b':
                            out.write(92);
                            out.write(98);
                            break;
                        case '\t':
                            out.write(92);
                            out.write(116);
                            break;
                        case '\n':
                            out.write(92);
                            out.write(110);
                            break;
                        case '\u000b':
                        default:
                            if (ch > 15) {
                                out.write("\\u00" + hex(ch));
                            } else {
                                out.write("\\u000" + hex(ch));
                            }
                            break;
                        case '\f':
                            out.write(92);
                            out.write(102);
                            break;
                        case '\r':
                            out.write(92);
                            out.write(114);
                    }
                } else {
                    switch (ch) {
                        case '\"':
                            out.write(92);
                            out.write(34);
                            break;
                        case '\'':
                            if (escapeSingleQuote) {
                                out.write(92);
                            }

                            out.write(39);
                            break;
                        case '/':
                            if (escapeForwardSlash) {
                                out.write(92);
                            }

                            out.write(47);
                            break;
                        case '\\':
                            out.write(92);
                            out.write(92);
                            break;
                        default:
                            out.write(ch);
                    }
                }
            }

        }
    }

    public static String unescapeJava(String str) {
        if (str == null) {
            return null;
        } else {
            try {
                StringWriter ioe = new StringWriter(str.length());
                unescapeJava(ioe, str);
                return ioe.toString();
            } catch (IOException var2) {
            }
        }
        return null;
    }

    public static void unescapeJava(Writer out, String str) throws IOException {
        if (out == null) {
            throw new IllegalArgumentException("The Writer must not be null");
        } else if (str != null) {
            int sz = str.length();
            StringBuilder unicode = new StringBuilder(4);
            boolean hadSlash = false;
            boolean inUnicode = false;

            for (int i = 0; i < sz; ++i) {
                char ch = str.charAt(i);
                if (inUnicode) {
                    unicode.append(ch);
                    if (unicode.length() == 4) {
                        try {
                            int nfe = Integer.parseInt(unicode.toString(), 16);
                            out.write((char) nfe);
                            unicode.setLength(0);
                            inUnicode = false;
                            hadSlash = false;
                        } catch (NumberFormatException var9) {
                        }
                    }
                } else if (hadSlash) {
                    hadSlash = false;
                    switch (ch) {
                        case '\"':
                            out.write(34);
                            break;
                        case '\'':
                            out.write(39);
                            break;
                        case '\\':
                            out.write(92);
                            break;
                        case 'b':
                            out.write(8);
                            break;
                        case 'f':
                            out.write(12);
                            break;
                        case 'n':
                            out.write(10);
                            break;
                        case 'r':
                            out.write(13);
                            break;
                        case 't':
                            out.write(9);
                            break;
                        case 'u':
                            inUnicode = true;
                            break;
                        default:
                            out.write(ch);
                    }
                } else if (ch == 92) {
                    hadSlash = true;
                } else {
                    out.write(ch);
                }
            }

            if (hadSlash) {
                out.write(92);
            }

        }
    }

    private static String hex(char ch) {
        return Integer.toHexString(ch).toUpperCase(Locale.ENGLISH);
    }

    public static String getLongerStr(String left, String right) {
        if (StringUtil.isEmpty(left)) {
            return right;
        } else if (StringUtil.isEmpty(right)) {
            return left;
        } else if (left.length() > right.length()) {
            return left;
        } else {
            return right;
        }
    }

    public static String creFormatThousands(int creditStr) {
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(creditStr);
    }

    /**
     * 格式化毫秒数为 xx:xx:xx这样的时间格式。
     *
     * @param ms 毫秒数
     * @return 格式化后的字符串
     */
    public static String formatMs(long ms) {
        int seconds = (int) (ms / 1000);
        int finalSec = seconds % 60;
        int finalMin = seconds / 60 % 60;
        int finalHour = seconds / 3600;

        StringBuilder msBuilder = new StringBuilder("");
        if (finalHour > 9) {
            msBuilder.append(finalHour).append(":");
        } else if (finalHour > 0) {
            msBuilder.append("0").append(finalHour).append(":");
        }

        if (finalMin > 9) {
            msBuilder.append(finalMin).append(":");
        } else if (finalMin > 0) {
            msBuilder.append("0").append(finalMin).append(":");
        } else {
            msBuilder.append("00").append(":");
        }

        if (finalSec > 9) {
            msBuilder.append(finalSec);
        } else if (finalSec > 0) {
            msBuilder.append("0").append(finalSec);
        } else {
            msBuilder.append("00");
        }
        return msBuilder.toString();
    }
}
