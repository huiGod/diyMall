package cn._51app.util;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * JSON工具类
 * 
 * @author jonny
 * 
 */
public final class JSONUtil {

	/**
	 * 转换对象为JSON字串
	 */
	public final static String convertObjectToJSON(Object obj) {
		return JSONObject.fromObject(obj).toString();
	}

	/**
	 * 转换对象为JSON字串
	 */
	public final static String convertObjectToJSON(Object obj, String[] names) {
		JsonConfig config = new JsonConfig();
		config.setExcludes(names);
		return JSONObject.fromObject(obj, config).toString();
	}

	/**
	 * 生成数组为JSON字串
	 */
	public final static String convertArrayToJSON(List<?> list) {
		if (list == null || list.size() == 0)
			return "[]";
		return JSONArray.fromObject(list).toString();
	}

	/**
	 * 转换JSON字串为对象
	 * 
	 * @param json
	 *            目标JSON字串
	 * @return
	 */
	public final static Object convertJSONToObject(String json,
			Class<?> targetClass) {
		return JSONObject.toBean(JSONObject.fromObject(json), targetClass);
	}

	/**
	 * tengh 2016年11月18日 下午6:17:56
	 * @param json
	 * @return
	 * TODO json转换成 list<map>
	 */
	public final static List<Map<String, Object>> convertJSONToList(String json){
		if(StringUtils.isBlank(json)){
			return new ArrayList<>();
		}
		return (List<Map<String, Object>>)JSONArray.fromObject(json);
	}
	
	/**
	 * 在指定JSON字符串中的属性值前加入特定的文字
	 * 
	 * @param json
	 *            JSON字符串
	 * @param names
	 *            属性名
	 * @param types
	 *            属性值类型
	 * @param values
	 *            特定的文字
	 * @return
	 */
	public final static String replacePrefixText(String json, String[] names,
			int[] types, String[] values) {
		String returnJson = json;
		int count = (names == null || values == null || types == null
				|| names.length != types.length || names.length != values.length) ? 0
				: names.length;

		int type = 0;
		for (int i = 0; i < count; i++) {
			type = types[i];
			switch (type) {
			case Types.INTEGER:

				break;
			case Types.VARCHAR:
				returnJson = returnJson.replace("\"" + names[i] + "\":\"", "\""
						+ names[i] + "\":\"" + values[i] + "");
				break;
			}
		}
		return returnJson;
	}

	/**
	 * 修改JSON数据，在JSON下添加指定的键和值
	 * 
	 * @param json
	 *            源JSON数据
	 * @param names
	 *            键数组
	 * @param values
	 *            值数组
	 * @return
	 */
	public final static String joinPrefixText(String json, String[] names,
			String[] values) {
		int count = (names == null || values == null || names.length != values.length) ? 0
				: names.length;
		if (count == 0)
			return json;
		StringBuilder appendJson = new StringBuilder("{");
		for (int i = 0; i < count; i++) {
			appendJson.append("\"").append(names[i]).append("\":\"").append(
					values[i]).append("\"").append(",");
		}
		if (json.startsWith("{")) // JSON为单个对象
			return appendJson.toString() + json.substring(1);
		else if (json.startsWith("[")) // JSON为数组
			return appendJson.toString() + "\"item\":" + json + "}";
		else
			// 为普通字符串
			return appendJson.toString() + "\"item\":\"" + json + "\"}";
	}

	/**
	 * 修改JSON数据，在JSON下添加指定的键和值
	 * 
	 * @param json
	 *            源JSON数组
	 * @param name
	 *            新加的键
	 * @param value
	 *            新加的值
	 * @return
	 */
	public final static String joinPrefixText(String json, String name,
			String value) {
		if (StringUtils.isEmpty(name) || StringUtils.isEmpty(value))
			return json;
		String prev = "{\"" + name + "\":\"" + value + "\",";
		if (json.startsWith("{")) // JSON为单个对象
			return prev + json.substring(1);
		else if (json.startsWith("[")) // JSON为数组
			return prev + "\"item\":" + json + "}";
		else
			// 为普通字符串
			return prev + "\"item\":\"" + json + "\"}";
	}

	public static void main(String[] args) {
		 String jsonArrayData="[{\"a1\":\"12\",\"b1\":\"112\",\"c1\":\"132\",\"d1\":\"134\"},{\"a2\":\"12\",\"b2\":\"112\",\"c2\":\"132\",\"d2\":\"134\"},{\"a3\":\"12\",\"b3\":\"112\",\"c3\":\"132\",\"d3\":\"134\"}]"; 
		 List<Map<String, Object>> list=(List<Map<String, Object>>)JSONUtil.convertJSONToList(jsonArrayData);
		 System.err.println(list.get(0).get("a1"));
	}
}
