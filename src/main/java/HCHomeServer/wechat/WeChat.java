package HCHomeServer.wechat;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
/**
 * 与微信服务器交互的封装
 * @author cj
 *
 */
public class WeChat {
	final private static String APPID =  "wx663f60640393fd1e";
	final private static String SECRET =  "60b630f2c9a13f0af355390ad3fc481c";
	final private static String GRANT_TYPE = "authorization_code";
	final private static String GET_SESSION_URL = "https://api.weixin.qq.com/sns/jscode2session";
	/**
	 * 从微信服务器获取用户session
	 * @param jsCode
	 * @return
	 */
	public static Session getSession(String jsCode) {
		Map<String, String>params = new HashMap<String, String>();
		params.put("appid", APPID);
		params.put("secret", SECRET);
		params.put("js_code", jsCode);
		params.put("grant_type", GRANT_TYPE);
		String result = RequestUtil.get(GET_SESSION_URL, params);
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
	/**
	 * 根据jsCode生成唯一的编码
	 * @param jsCode 
	 * @return
	 */
	public static String getUnionKey(String jsCode) {
		return String.valueOf((new Date()).getTime()).concat(String.valueOf(jsCode.hashCode()));
	}
}
