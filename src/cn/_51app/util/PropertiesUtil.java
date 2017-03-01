package cn._51app.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * @author 
 *	配置文件工具类
 */
public class PropertiesUtil {
	private static Properties config = null;

	static {
		InputStream in = PropertiesUtil.class.getClassLoader().getResourceAsStream("system.properties");
		config = new Properties();
		try {
			config.load(in);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据Key获取值
	 * @param key
	 * @return
	 */
	public static String getValue(String key) {
		if (config.getProperty(key) != null) {
			String s = "";
			try {
				s = new String(config.getProperty(key).trim().getBytes("ISO-8859-1"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return s;
		} else {
			return "";
		}
	}

}
