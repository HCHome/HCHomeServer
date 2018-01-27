package HCHomeServer.wechat;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public class WeChat {
	final private static String APPID =  "wx663f60640393fd1e";
	final private static String SECRET =  "60b630f2c9a13f0af355390ad3fc481c";
	final private static String GRANT_TYPE = "authorization_code";
	final private static String GET_SESSION_URL = "https://api.weixin.qq.com/sns/jscode2session";
	
	public static Session getSession(String jsCode) {
		Map<String, String>params = new HashMap<String, String>();
		params.put("appid", APPID);
		params.put("secret", SECRET);
		params.put("js_code", jsCode);
		params.put("grant_type", GRANT_TYPE);
		String result = Request.get(GET_SESSION_URL, params);
		if(result == null||result.isEmpty()) {
			return null;
		}else {
			JSONObject resultJson = JSONObject.parseObject(result);
			if(resultJson.getString("openid")!=null) {
				return Session.build_from_json(resultJson);
			}else {
				return null;
			}
		}
	}

	public static String getUnionKey(String jsCode) {
		return String.valueOf((new Date()).getTime()).concat(String.valueOf(jsCode.hashCode()));
	}
}
