package HCHomeServer.model.result;

import java.io.Serializable;
import java.util.Date;

public class LightUserApply implements Serializable{

	private static final long serialVersionUID = 4202944020243806718L;
	//申请Id
	private int applyId;
	//用户头像地址
	private String avatar;
	//用户名字
	private String name;
	private Date createdDate;
	private String wechatIdentify;

	public LightUserApply() {
	}

	public int getApplyId() {
		return applyId;
	}

	public void setApplyId(int applyId) {
		this.applyId = applyId;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getWechatIdentify() {
		return wechatIdentify;
	}

	public void setWechatIdentify(String wechatIdentify) {
		this.wechatIdentify = wechatIdentify;
	}
}