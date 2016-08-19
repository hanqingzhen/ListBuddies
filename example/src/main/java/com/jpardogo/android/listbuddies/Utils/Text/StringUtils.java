/*
 * Copyright (c) 2014.
 * Jackrex
 */

package com.jpardogo.android.listbuddies.Utils.Text;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.text.TextUtils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串操作工具包
 */
public class StringUtils {
	private final static Pattern emailer = Pattern
			.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");

	/**
	 * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
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
	 * 判断是不是一个合法的电子邮件地址
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		if (email == null || email.trim().length() == 0)
			return false;
		return emailer.matcher(email).matches();
	}

	// used by friendly_time
	private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd");
		}
	};

	// used by toDate
	private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	};

	/**
	 * 将字符串转位日期类型
	 * 
	 * @param sdate
	 * @return
	 */
	public static Date toDate(String sdate) {
		try {
			return dateFormater.get().parse(sdate);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 以友好的方式显示时间
	 * 
	 * @param sdate
	 * @return
	 */
	public static String friendly_time(String sdate) {
		Date time = toDate(sdate);
		if (time == null) {
			return "Unknown";
		}
		String ftime = "";
		Calendar cal = Calendar.getInstance();
		// 判断是否是同一天
		String curDate = dateFormater2.get().format(cal.getTime());
		String paramDate = dateFormater2.get().format(time);
		if (curDate.equals(paramDate)) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0)
				ftime = Math.max(
						(cal.getTimeInMillis() - time.getTime()) / 60000, 1)
						+ "分钟前";
			else
				ftime = hour + "小时前";
			return ftime;
		}

		long lt = time.getTime() / 86400000;
		long ct = cal.getTimeInMillis() / 86400000;
		int days = (int) (ct - lt);
		if (days == 0) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0)
				ftime = Math.max(
						(cal.getTimeInMillis() - time.getTime()) / 60000, 1)
						+ "分钟前";
			else
				ftime = hour + "小时前";
		} else if (days == 1) {
			ftime = "昨天";
		} else if (days == 2) {
			ftime = "前天";
		} else if (days > 2 && days <= 10) {
			ftime = days + "天前";
		} else if (days > 10) {
			ftime = dateFormater2.get().format(time);
		}
		return ftime;
	}

	public static boolean isStringEmpty(String string) {

		if (string == null) {
			return true;
		}
		return TextUtils.isEmpty(string);

	}

	private static final double EARTH_RADIUS = 6378137;

	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}

	public static float GetDistance(float lng1, float lat1, float lng2,
			float lat2) {
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		return (float) s;
	}

	/**
	 * 字符串敏感截取，超出�?..结束，处理后的长�?=num区分字母和汉�?
	 * 
	 * @param str
	 * @param num
	 * @return String
	 */
	public static String subStrSensitive(String str, int num) {
		if (str == null || num <= 3) {
			return str;
		}
		StringBuffer sbStr = new StringBuffer();
		String returnStr = "";
		int length = 0;
		char temp;
		for (int i = 0; i < str.length(); i++) {
			temp = str.charAt(i);
			if (isChinese(temp)) {
				length = length + 2;
			} else {
				length++;
			}
			if (length == num && i == (str.length() - 1)) {
				sbStr.append(temp);
				returnStr = sbStr.toString();
				break;
			} else if (length >= num) {
				sbStr.append("...");
				returnStr = sbStr.toString();
				try {
					int lastL = returnStr.getBytes("GBK").length;

					while (lastL > num) {
						returnStr = returnStr.substring(0,
								returnStr.length() - 4) + "...";

						lastL = returnStr.getBytes("GBK").length;
					}
					break;
				} catch (Exception e) {
					break;
				}
			}
			sbStr.append(temp);
			if (i == (str.length() - 1)) {

				returnStr = sbStr.toString();
			}
		}

		return returnStr;
	}

	public static boolean isChinese(char a) {
		int v = (int) a;
		return (v >= 19968 && v <= 171941);
	}

	public static boolean isChinese(String s) {
		if (null == s || "".equals(s.trim()))
			return false;
		for (int i = 0; i < s.length(); i++) {
			if (!isChinese(s.charAt(i)))
				return false;
		}
		return true;
	}

	public static boolean containsChinese(String s) {
		if (null == s || "".equals(s.trim()))
			return false;
		for (int i = 0; i < s.length(); i++) {
			if (isChinese(s.charAt(i)))
				return true;
		}
		return false;
	}

	public static boolean isBlank(String str) {
		return (str == null || str.trim().length() == 0);
	}

	public static String defaultString(String value) {
		return defaultString(value, "");
	}

	public static String defaultString(String value, String defaultValue) {
		return (value == null) ? defaultValue : value;
	}

	public static String encodeHexString(byte[] bytes) {
		StringBuilder buffer = new StringBuilder();
		StringUtils.encodeHexString(bytes, buffer);
		return buffer.toString();
	}

	public static void encodeHexString(byte[] bytes, StringBuilder buffer) {
		for (byte b : bytes) {
			int hi = (b >>> 4) & 0x0f;
			int lo = (b >>> 0) & 0x0f;
			buffer.append(Character.forDigit(hi, 16));
			buffer.append(Character.forDigit(lo, 16));
		}
	}

	public static String join(String[] array, String delimiter) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < array.length; i++) {
			sb.append(array[i]);

			if (i < (array.length - 1)) {
				sb.append(delimiter);
			}
		}

		return sb.toString();
	}

	public static boolean isNullOrEmpty(String value) {
		return value == null || value.length() == 0;
	}

	public static String createUrlWithParam(String url, String param) {
		if (url == null) {
			return null;
		}
		StringBuilder urlBuilder = new StringBuilder(url);
		if (!url.endsWith("/")) {
			urlBuilder.append("/");
		}

		urlBuilder.append(param);
		return urlBuilder.toString();
	}

	public static String decodeMessage(String str, String jsonNodeName) {
		String result = str;
		try {
			JSONObject json = new JSONObject(str);
			result = (String) json.get(jsonNodeName);
		} catch (Exception e) {
			result = null;
		}
		return result;
	}

	public static String getMD5(String str) {
		if(str == null || str.length() ==0)
			return "";
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
		} catch (UnsupportedEncodingException e) {
		}
		byte[] byteArray = messageDigest.digest();
		StringBuilder md5StrBuff = new StringBuilder();
		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
				md5StrBuff.append("0").append(
						Integer.toHexString(0xFF & byteArray[i]));
			} else {
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
			}
		}
		return md5StrBuff.substring(8, 24);
	}

	public static String convertStreamToString(InputStream is)
			throws IOException {
		if (is != null) {

			StringBuilder sb = new StringBuilder();
			String line;

			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "UTF-8"));
				while ((line = reader.readLine()) != null) {
					sb.append(line).append("\n");
				}
			} finally {
				is.close();
			}
			return sb.toString();
		} else {
			return "";
		}
	}

	/**
	 * Convert and replace "abc","2013-06-15 15:15:12" -> "abc","1371280512000"
	 * 
	 * @param
	 * @return
	 */
	/*
	public static String convertAndReplace(String source) {
		String formatString = "[\"]\\d{4}[\\-]\\d{2}[\\-]\\d{2} \\d{2}[\\:]\\d{2}[\\:]\\d{2}[\"]";

		Pattern pattern = Pattern.compile(formatString);
		Matcher matcher = pattern.matcher(source);

		while (matcher.find()) {
			String find = matcher.group();
			int len = find.length();
			find = find.substring(1, len - 1);
			Calendar cal = CalendarUtils.YYYYMMDDHHMMSSToCalendar(find);
			// cal.add(Calendar.MONTH, 1);
			if (cal != null) {
				long time = cal.getTimeInMillis();
				String targetString = "\"" + String.valueOf(time) + "\"";
				source = source.replaceFirst(formatString, targetString);
			}
		}

		return source;
	}
*/
	public static String fullTohalf(String fullStr) {
		String outStr = "";
		String Tstr = "";
		byte[] b = null;

		for (int i = 0; i < fullStr.length(); i++) {
			try {
				Tstr = fullStr.substring(i, i + 1);
				b = Tstr.getBytes("unicode");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			if (b[3] == -1) {
				b[2] = (byte) (b[2] + 32);
				b[3] = 0;
				try {
					outStr = outStr + new String(b, "unicode");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			} else
				outStr = outStr + Tstr;
		}
		return outStr;
	}

	public static String urlWithParameters(String url,
										   HashMap<String, Object> params) {
		StringBuilder builder = new StringBuilder();

		if (params != null) {
			for (HashMap.Entry<String, Object> entry : params.entrySet()) {
				if (builder.length() == 0) {
					if (url.contains("?")) {
						builder.append("&");
					} else {
						builder.append("?");
					}
				} else {
					builder.append("&");
				}
				builder.append(entry.getKey()).append("=")
						.append(entry.getValue());
			}
		}

		return url + builder.toString();
	}

	public static String urlWithParameters(String url,
										   Map<String, Object> params) {
		StringBuilder builder = new StringBuilder();

		if (params != null) {
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				if (builder.length() == 0) {
					if (url.contains("?")) {
						builder.append("&");
					} else {
						builder.append("?");
					}
				} else {
					builder.append("&");
				}
				builder.append(entry.getKey()).append("=")
						.append(entry.getValue());
			}
		}

		return url + builder.toString();
	}

//	public static void mergeParam(HashMap<String, Object> params,
//			List<NameValuePair> valuePair) {
//		if (params != null) {
//			for (HashMap.Entry<String, Object> entry : params.entrySet()) {
//				String key = entry.getKey();
//				String value = String.valueOf(entry.getValue());
//				valuePair.add(new BasicNameValuePair(key, value));
//			}
//		}
//	}

	public static String getCountString(float count) {
		int a = (int) count;
		float b = count - a;

		if (b > 0) {
			return String.format("%.1f", count);
		} else {
			return String.valueOf(a);
		}
	}

	// public static void mergeParam(HashMap<String, Object> params,
	// LinkedMultiValueMap valuePair) {
	// if (params != null) {
	// for (HashMap.Entry<String, Object> entry : params.entrySet()) {
	// String key = entry.getKey();
	// String value = String.valueOf(entry.getValue());
	// valuePair.add(key, value);
	// }
	// }
	// }

	public static String[] getIpAndPort(String ip) {
		if (ip == null || ip.trim().length() < 4) {
			return null;
		}
		String[] strs = null;

		int index = ip.indexOf(":");
		if (index != -1) {
			strs = ip.split(":");
		} else {
			strs = new String[] { ip, "80" };
		}
		return strs;
	}

	@SuppressLint("NewApi")
	public String insertWithOnConflict(String table, ContentValues initialValues) {
		StringBuilder col = new StringBuilder();
		col.append("INSERT");
		col.append(" INTO ");
		col.append(table);
		col.append('(');

		StringBuilder val = new StringBuilder();
		val.append(" VALUES (");

		try {
			int size = (initialValues != null && initialValues.size() > 0) ? initialValues
					.size() : 0;
			if (size > 0) {
				int i = 0;
				for (String colName : initialValues.keySet()) {
					col.append((i > 0) ? "," : "");
					col.append("[" + colName + "]");

					Object o = initialValues.get(colName);
					val.append((i > 0) ? "," : "");
					val.append("\"");
					val.append(o);
					val.append("\"");
					i++;
				}
				col.append(')');
				val.append(')');

				col.append(val);
				col.append(";");
				return col.toString();
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 将整型添加“,”隔位符 “123456789”-》“123，456，789”
	 *
	 * @param number
	 * @return
	 */
	public static String addSeparateSign(int number) {
		DecimalFormat df = new DecimalFormat("#,###");
		return df.format(number);
	}

	public static boolean checkEmail(String email) {
		boolean flag = false;
		try {
			String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(email);
			flag = matcher.matches();
		} catch (Exception e) {
			flag = false;
		}

		return flag;
	}

	/**
	 * ��֤�ֻ����
	 *
	 * @param mobiles
	 * @return [0-9]{5,9}
	 */
	/*
	public static boolean isMobileNO(String mobiles) {
		boolean flag = false;
		try {
			Pattern p = Pattern
					.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
			Matcher m = p.matcher(mobiles);
			flag = m.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}
	*/
	public static boolean isMobileNO(String mobiles) {
		boolean flag = false;
		if(mobiles!= null && mobiles.length() == 11)
			flag = true;

		return flag;
	}

	public static boolean isNum(String number) {
		boolean flag = false;
		try {
			Pattern p = Pattern.compile("^[0-9]{5}$");
			Matcher m = p.matcher(number);
			flag = m.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	/**
	 * 身份证号码验证 1、号码的结构 公民身份号码是特征组合码，由十七位数字本体码和一位校验码组成。排列顺序从左至右依次为：六位数字地址码，
	 * 八位数字出生日期码，三位数字顺序码和一位数字校验码。 2、地址码(前六位数）
	 * 表示编码对象常住户口所在县(市、旗、区)的行政区划代码，按GB/T2260的规定执行。 3、出生日期码（第七位至十四位）
	 * 表示编码对象出生的年、月、日，按GB/T7408的规定执行，年、月、日代码之间不用分隔符。 4、顺序码（第十五位至十七位）
	 * 表示在同一地址码所标识的区域范围内，对同年、同月、同日出生的人编定的顺序号， 顺序码的奇数分配给男性，偶数分配给女性。 5、校验码（第十八位数）
	 * （1）十七位数字本体码加权求和公式 S = Sum(Ai * Wi), i = 0, ... , 16 ，先对前17位数字的权求和
	 * Ai:表示第i位置上的身份证号码数字值 Wi:表示第i位置上的加权因子 Wi: 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4
	 * 2 （2）计算模 Y = mod(S, 11) （3）通过模得到对应的校验码 Y: 0 1 2 3 4 5 6 7 8 9 10 校验码: 1 0
	 * X 9 8 7 6 5 4 3 2
	 */

	/**
	 * 功能：身份证的有效验证
	 *
	 * @param IDStr
	 *            身份证号
	 * @return 有效：返回"" 无效：返回String信息
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public static String IDCardValidate(String IDStr) throws ParseException {
		String errorInfo = "";// 记录错误信息
		String[] ValCodeArr = { "1", "0", "x", "9", "8", "7", "6", "5", "4",
				"3", "2" };
		String[] Wi = { "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7",
				"9", "10", "5", "8", "4", "2" };
		String Ai = "";
		// ================ 号码的长度 15位或18位 ================
		if (IDStr.length() != 15 && IDStr.length() != 18) {
			errorInfo = "身份证号码长度应该为15位或18位";
			return errorInfo;
		}
		// =======================(end)========================

		// ================ 数字 除最后以为都为数字 ================
		if (IDStr.length() == 18) {
			Ai = IDStr.substring(0, 17);
		} else if (IDStr.length() == 15) {
			Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);
		}
		if (isNumeric(Ai) == false) {
			errorInfo = "身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字"+Ai;
			return errorInfo;
		}
		// =======================(end)========================

		// ================ 出生年月是否有效 ================
		String strYear = Ai.substring(6, 10);// 年份
		String strMonth = Ai.substring(10, 12);// 月份
		String strDay = Ai.substring(12, 14);// 月份
		if (isDate(strYear + "-" + strMonth + "-" + strDay) == false) {
			errorInfo = "身份证生日无效";
			return errorInfo;
		}
		GregorianCalendar gc = new GregorianCalendar();
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
					|| (gc.getTime().getTime() - s.parse(
							strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
				errorInfo = "身份证生日不在有效范围";
				return errorInfo;
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
			errorInfo = "身份证月份无效";
			return errorInfo;
		}
		if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
			errorInfo = "身份证日期无效";
			return errorInfo;
		}
		// =====================(end)=====================

		// ================ 地区码时候有效 ================
		Hashtable h = GetAreaCode();
		if (h.get(Ai.substring(0, 2)) == null) {
			errorInfo = "身份证地区编码错误";
			return errorInfo;
		}
		// ==============================================

		// ================ 判断最后一位的值 ================
		int TotalmulAiWi = 0;
		for (int i = 0; i < 17; i++) {
			TotalmulAiWi = TotalmulAiWi
					+ Integer.parseInt(String.valueOf(Ai.charAt(i)))
					* Integer.parseInt(Wi[i]);
		}
		int modValue = TotalmulAiWi % 11;
		String strVerifyCode = ValCodeArr[modValue];
		Ai = Ai + strVerifyCode;

		if (IDStr.length() == 18) {
			if (Ai.equals(IDStr) == false) {
				errorInfo = "身份证无效，不是合法的身份证号码";
				return errorInfo;
			}
		} else {
			return "";
		}
		// =====================(end)=====================
		return "";
	}

	/**
	 * 功能：判断字符串是否为日期格式
	 * 
	 *
	 * @return
	 */
	public static boolean isDate(String strDate) {
		Pattern pattern = Pattern
				.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
		Matcher m = pattern.matcher(strDate);
		return m.matches();
	}

	/**
	 * 功能：设置地区编码
	 * 
	 * @return Hashtable 对象
	 */
	@SuppressWarnings("unchecked")
	private static Hashtable GetAreaCode() {
		Hashtable hashtable = new Hashtable();
		hashtable.put("11", "北京");
		hashtable.put("12", "天津");
		hashtable.put("13", "河北");
		hashtable.put("14", "山西");
		hashtable.put("15", "内蒙古");
		hashtable.put("21", "辽宁");
		hashtable.put("22", "吉林");
		hashtable.put("23", "黑龙江");
		hashtable.put("31", "上海");
		hashtable.put("32", "江苏");
		hashtable.put("33", "浙江");
		hashtable.put("34", "安徽");
		hashtable.put("35", "福建");
		hashtable.put("36", "江西");
		hashtable.put("37", "山东");
		hashtable.put("41", "河南");
		hashtable.put("42", "湖北");
		hashtable.put("43", "湖南");
		hashtable.put("44", "广东");
		hashtable.put("45", "广西");
		hashtable.put("46", "海南");
		hashtable.put("50", "重庆");
		hashtable.put("51", "四川");
		hashtable.put("52", "贵州");
		hashtable.put("53", "云南");
		hashtable.put("54", "西藏");
		hashtable.put("61", "陕西");
		hashtable.put("62", "甘肃");
		hashtable.put("63", "青海");
		hashtable.put("64", "宁夏");
		hashtable.put("65", "新疆");
		hashtable.put("71", "台湾");
		hashtable.put("81", "香港");
		hashtable.put("82", "澳门");
		hashtable.put("91", "国外");
		return hashtable;
	}
	
	  /**
     * 功能：判断字符串是否为数字
     * 
     * @param str
     * @return
     */
    private static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
		return isNum.matches();
    }

}
