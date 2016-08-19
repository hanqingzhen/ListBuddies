package com.jpardogo.android.listbuddies.Utils.Text;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JsonUtils {


	/**
	 * Json 转成 Map<>
	 * 
	 * @param jsonStr
	 * @return
	 */
	public static Map<String, Object> getMapForJson(String jsonStr) {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(jsonStr);

			Iterator<String> keyIter = jsonObject.keys();
			String key;
			Object value;
			Map<String, Object> valueMap = new HashMap<String, Object>();
			while (keyIter.hasNext()) {
				key = keyIter.next();
				value = jsonObject.get(key);
				valueMap.put(key, value);
			}
			return valueMap;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			// Log.e(HttpClientUtils.TAG, e.toString());
		}
		return null;
	}

	/**
	 * Json 转成 List<Map<>>
	 * 
	 * @param jsonStr
	 * @return
	 */
	public static List<Map<String, Object>> getlistForJson(String jsonStr) {
		List<Map<String, Object>> list = null;
		try {
			JSONArray jsonArray = new JSONArray(jsonStr);
			JSONObject jsonObj;
			list = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < jsonArray.length(); i++) {
				jsonObj = (JSONObject) jsonArray.get(i);
				list.add(getMapForJson(jsonObj.toString()));
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * jsonString转换成list
	 */
	public static List<Serializable> stringJsonToList(String json) {
		try {
			Type type = new TypeToken<ArrayList<Serializable>>() {
			}.getType();
			Gson gson = new Gson();
			List<Serializable> list = new ArrayList<Serializable>();
			if (!TextUtils.isEmpty(json)) {
				list = gson.fromJson(json, type);
				if (list != null && list.size() > 0) {
					return list;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

}
