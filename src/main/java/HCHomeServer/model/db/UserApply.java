package HCHomeServer.model.db;

import java.util.Date;

public class UserApply {
	private int applyId;
	private int term;
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	private String name;
	private String message;
	private Date createdDate;
	private String wechatIdentify;
	public UserApply(int term, String name, String message, String openId) {
		this.term = term;
		this.name = name;
		this.message = message;
		this.wechatIdentify = openId;
		this.createdDate = new Date();
	}
	public int getApplyId() {
		return applyId;
	}
	public void setApplyId(int applyId) {
		this.applyId = applyId;
	}
	public int getTerm() {
		return term;
	}
	public void setTerm(int term) {
		this.term = term;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getWechatIdentify() {
		return wechatIdentify;
	}
	public void setWechatIdentify(String wechatIdentify) {
		this.wechatIdentify = wechatIdentify;
	}
	
}
