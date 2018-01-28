package HCHomeServer.wechat;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.JSONObject;

public class Session implements Serializable{

	private static final long serialVersionUID = -8333733472722833886L;
	private String openId;
	private String sessionKey;
	private Date date;
	public String getOpenId() {
		return openId;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getSessionKey() {
		return sessionKey;
	}
	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}
	public Session() {
	}
	public static Session build_from_json(JSONObject resultJson) {
		Session session = new Session();
		session.setOpenId(resultJson.getString("openid"));
		session.setSessionKey(resultJson.getString("session_key"));
		session.setDate(new Date());
		return session;
	}
	
}
