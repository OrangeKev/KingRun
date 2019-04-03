package com.king.run.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextPaint;
import android.widget.EditText;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA. User: weiguo.ren Date: 13-9-16 Time: 下午3:10 To
 * change this template use File | Settings | File Templates.
 */
public class StringUtil {

    private static String hexString = "1234567GHJKLMNBV";

    /**
     * 字符串转换成十六进制值
     *
     * @param bin String 我们看到的要转换成十六进制的字符串
     * @return
     */
    public static String bin2hex(String bin) {
        char[] digital = "0123456789ABCDEF".toCharArray();
        StringBuffer sb = new StringBuffer("");
        byte[] bs = bin.getBytes();
        int bit;
        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(digital[bit]);
            bit = bs[i] & 0x0f;
            sb.append(digital[bit]);
        }
        return sb.toString();
    }

    /**
     * 十六进制转换字符串
     *
     * @param hex String 十六进制
     * @return String 转换后的字符串
     */
    public static String hex2bin(String hex) {
        String digital = "0123456789ABCDEF";
        char[] hex2char = hex.toCharArray();
        byte[] bytes = new byte[hex.length() / 2];
        int temp;
        for (int i = 0; i < bytes.length; i++) {
            temp = digital.indexOf(hex2char[2 * i]) * 16;
            temp += digital.indexOf(hex2char[2 * i + 1]);
            bytes[i] = (byte) (temp & 0xff);
        }
        return new String(bytes);
    }

    public static String toStringVersion(String bytes) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(
                bytes.length() / 2);
        // 将每2位16进制整数组装成一个字节
        for (int i = 0; i < bytes.length(); i += 2)
            baos.write((hexString.indexOf(bytes.charAt(i)) << 4 | hexString
                    .indexOf(bytes.charAt(i + 1))));

        return new String(baos.toByteArray());
    }

    public static boolean isBlank(String str) {
        int strLen;
        if ((str == null) || ((strLen = str.length()) == 0)
                || str.equals("null")) {
            return true;
        }
        for (int i = 0; i < strLen; ++i) {
            if (!(Character.isWhitespace(str.charAt(i)))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(String str) {
        return (!(isBlank(str)));
    }

    public static String isString(String str) {
        if (str == null || str.equals("") || str.length() == 0
                || str.equals("null")) {
            return "";
        } else {
            return str;
        }
    }

    /**
     * 截取字符串
     *
     * @param s      要被截取的字符串
     * @param iLen   截取的长度
     * @param hasDot 是否加...
     * @return
     */
    public static String getSubString(String s, int iLen, boolean hasDot) {
        if (StringUtil.isNotBlank(s)) {
            char c = ' ';
            String sAsc = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789.+-";
            String lsResult = " ";
            int iGetedLen = 0;
            boolean flag = false;
            for (int i = 0; i < s.length(); i++) {
                c = s.charAt(i);
                if (sAsc.indexOf(c) >= 0) {
                    lsResult += c;
                    iGetedLen += 1;
                } else {
                    lsResult += c;
                    iGetedLen += 2;
                }
                if (iGetedLen >= iLen) {
                    if (i + 1 < s.length()) {
                        flag = true;
                    }
                    break;
                }
            }
            if (iGetedLen <= iLen) {
                if (flag) {
                    if (hasDot) {
                        return lsResult.trim() + "...";
                    } else {
                        return lsResult.trim();
                    }
                } else {
                    return lsResult.trim();
                }
            }
            if (hasDot) {
                lsResult = lsResult + "...";
            }
            return (lsResult.trim());
        } else {
            return "";
        }
    }

    /**
     * 给控件文字设置 粗体
     *
     * @param view 需要设置 字体粗体 的控件
     */
    public static void setFakeBoldText(TextView view) {
        TextPaint tp = view.getPaint();
        tp.setFakeBoldText(true);

    }

    /**
     * 判断字符串是否有特殊字符
     */

    public static boolean isSpecialChar(String str) {
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？_]";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }

    // 最大位数
    public static void lengthFilter(final Context context,
                                    final EditText editText, final int max_length, final String err_msg) {
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter.LengthFilter(max_length) {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                int destLen = getCharacterNum(dest.toString()); // 获取字符个数(一个中文算2个字符)
                int sourceLen = getCharacterNum(source.toString());
                if (destLen + sourceLen > max_length) {
                    // Util.sendToast(context, err_msg);
                    return "";
                }
                return source;
            }
        };

        editText.setFilters(filters);

    }

    public static int getCharacterNum(final String content) {
        if (null == content || "".equals(content)) {
            return 0;
        } else {
            return (content.length() + getChineseNum(content));
        }
    }

    public static int getChineseNum(String s) {
        int num = 0;
        char[] myChar = s.toCharArray();
        for (int i = 0; i < myChar.length; i++) {
            if ((char) (byte) myChar[i] != myChar[i]) {
                num++;
            }
        }
        return num;

    }

    /**
     * 半角 转全角 ：
     *
     * @param input
     * @return
     */
    public static String ToSBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 32) {
                c[i] = (char) 12288;
                continue;
            }
            if (c[i] < 127)
                c[i] = (char) (c[i] + 65248);
        }
        return new String(c);
    }

    /**
     * 去除特殊字符或将所有中文标号替换为英文标号
     *
     * @param str
     * @return
     */
    public static String stringFilter(String str) {
        str = str.replaceAll("【", "[").replaceAll("】", "]")
                .replaceAll("！", "!").replaceAll("：", ":");// 替换中文标号
        String regEx = "[『』]"; // 清除掉特殊字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 字符串大写变小写 小写变大写
     *
     * @param b
     * @return
     */
    public static String change(String b) {
        char letters[] = new char[b.length()];
        for (int i = 0; i < b.length(); i++) {

            char letter = b.charAt(i);
            if (letter >= 'a' && letter <= 'z')
                letter = (char) (letter - 32);
            else if (letter >= 'A' && letter <= 'Z')
                letter = (char) (letter + 32);
            letters[i] = letter;
        }

        return new String(letters);
    }

    /**
     * 小写变大写
     *
     * @param b
     * @return
     */
    public static String changeBig(String b) {
        char letters[] = new char[b.length()];
        for (int i = 0; i < b.length(); i++) {

            char letter = b.charAt(i);
            if (letter >= 'a' && letter <= 'z')
                letter = (char) (letter - 32);
            letters[i] = letter;
        }

        return new String(letters);
    }

    public static String str2blank(String str) {
        if (isBlank(str)) {
            return "无";
        }
        return str;
    }

//    public static String addToken(String url) {
//        String token = PrefName.getToken();
//        // 如果url中已经包括"?"
//        String paramStr = "?";
//        if (url.contains("?")) {
//            paramStr = "&";
//        }
//        if (!url.contains("token=")) {
//            url += paramStr + "token=" + token;
//        }
//        url += "&lang=" + LanguageSetting.getCurrLangStr();
//        return url;
//    }

    /*
     * 判断ssid是否相等，三星4.3中返回的ssid可能会包含双引号
     */
    public static boolean isSsidSame(String str1, String str2) {
        if (str1 == null) {
            return false;
        }
        if (str2 == null) {
            return false;
        }
        if (str1.equals(str2)) {
            return true;
        }
//		if (str1.equals(str2.substring(1, str2.length() - 1))) {
//			return true;
//		}
//		if (str2.equals(str1.substring(1, str1.length() - 1))) {
//			return true;
//		}
        if (str1.equals("\"" + str2 + "\"")) {
            return true;
        }
        if (str2.equals("\"" + str1 + "\"")) {
            return true;
        }
        return false;
    }

    /*
     * 判断是否有效的邮箱地址
     */
    public static boolean isMailAddr(String str) {
        if (str == null) {
            return false;
        }
        Pattern p = Pattern.compile("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /*
     * 判断是否有效的域名
     */
    public static boolean isDomain(String str) {
        if (str == null) {
            return false;
        }
        Pattern p = Pattern.compile("([\\w\\-]+\\.)+[\\w\\-]+");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    public static String null2blank(String str) {
        if (isBlank(str)) {
            return "";
        }
        return str;
    }

    // 判断email格式是否正确

    public static boolean isEmail(String email) {

        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";

        Pattern p = Pattern.compile(str);

        Matcher m = p.matcher(email);

        return m.matches();

    }

    /*
    * 判断字符串是否为数字
    */
    public static boolean isNumeric(String str) {
        if (StringUtil.isBlank(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    public static String replaceBlank(String str) {

        Pattern p = Pattern.compile("\\s*|\t|\r|\n");
//        System.out.println("before:" + str);

        Matcher m = p.matcher(str);

        return m.replaceAll("");

//        System.out.println("after:" + after);

    }

    public static boolean getNumberConvert(String time) {
        Pattern pattern = Pattern.compile("\\d+\\.\\d+$|-\\d+\\.\\d+$");
        Matcher isNum = pattern.matcher(time);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }


//    public static String configPhotoUrl(String photoUrl) {
//        if(StringUtil.isNotBlank(photoUrl)){
//            //不是绝对路径的需要拼接
//            if(!photoUrl.contains("://")){
//                photoUrl = ServerUrlType.FILE.getUrl()+photoUrl;
//            }
//        }else {
//            photoUrl = "";
//        }
//        Util.print("show image  url: "+photoUrl);
//        return photoUrl;
//    }

    /**
     * @param text
     * @return 0：纯字母
     * 1: 纯汉字
     * 2：混合
     */
    public static int checkStringContent(String text) {
        if (StringUtil.isBlank(text)) {
            return 0;
        }
        char[] input = text.trim().toCharArray();
        boolean hasLetter = false;
        boolean hasChinese = false;
        for (int i = 0; i < input.length; i++) {
            // \\u4E00是unicode编码，判断是不是中文
            if (Character.toString(input[i]).matches(
                    "[\\u4E00-\\u9FA5]+")) {
                hasChinese = true;
            } else {
                hasLetter = true;
            }
        }

        if (hasChinese && hasLetter) {
            return 2;
        } else if (hasChinese) {
            return 1;
        } else {
            return 0;
        }
    }

    public static String filterHtml(String str) {

        String regxp = "<b.*?>(.*?)</b>";
        Pattern pattern = Pattern.compile(regxp);
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        boolean result1 = matcher.find();
        while (result1) {
            matcher.appendReplacement(sb, "<a href=" + matcher.group(1) + ">" + matcher.group(1) + "</a>");
            result1 = matcher.find();
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static String getVersion(Context context) {
        String version = "";
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            version = info.versionName;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return version;
    }
}
