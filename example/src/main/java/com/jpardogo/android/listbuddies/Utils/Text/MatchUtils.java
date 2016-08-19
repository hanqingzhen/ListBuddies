package com.jpardogo.android.listbuddies.Utils.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatchUtils {

	 /**
     * 取出数字获取验证码
     * @param str
     * @return
     */
    public static String getDynamicPassword(String str) {
        Pattern continuousNumberPattern = Pattern.compile("[0-9\\.]+");
        Matcher m = continuousNumberPattern.matcher(str);
        String dynamicPassword = "";
        while(m.find()){
            if(m.group().length() == 6) {
                System.out.print(m.group());
                dynamicPassword = m.group();
            }
        }

        return dynamicPassword;
    }
}
