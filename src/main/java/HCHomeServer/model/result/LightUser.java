package HCHomeServer.model.result;


import java.io.Serializable;

import HCHomeServer.model.db.SignRecord;
import HCHomeServer.model.db.User;

/**
 * 返回前端的用户信息包装类
 * 现用于登录接口
 * @author cj
 */
public class LightUser implements Serializable{

	private static final long serialVersionUID = 8862880821556043645L;
	//用户Id
	private int userId;
	//昵称
	private String nickname;
	//签到积分
	private int signScore;
	//今日是否签到
	private boolean isSign;
	//头像
	private String avatar;
	//未读消息数量
	private Integer unReadCount;
	//是否第一次登录
	private Boolean isFirstLogin;
	/**
	 * 构建一个LightUser
	 * @param user
	 * @param sign
	 * @return
	 */
	public static LightUser buildByUser(User user, SignRecord sign) {
		LightUser lightUser = new LightUser();
		lightUser.setNickname(user.getNickname());
		lightUser.setSignScore(user.getSignScore());
		lightUser.setUserId(user.getUserId());
		lightUser.setAvatar(user.getAvatar());
		if(sign==null) {
			lightUser.setSign(false);
		}else {
			lightUser.setSign(true);
		}
		if(user.getSchool()==null) {
			lightUser.setIsFirstLogin(Boolean.TRUE);
		}else {
			lightUser.setIsFirstLogin(Boolean.FALSE);
		}
		lightUser.unReadCount = 0;
		return lightUser;
	}
	
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public int getUserId() {
		return userId;
	}
	public boolean isSign() {
		return isSign;
	}
	public void setSign(boolean isSign) {
		this.isSign = isSign;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public int getSignScore() {
		return signScore;
	}
	public void setSignScore(int signScore) {
		this.signScore = signScore;
	}
	public Integer getUnReadCount() {
		return unReadCount;
	}
	public void setUnReadCount(Integer unReadCount) {
		this.unReadCount = unReadCount;
	}
	public Boolean getIsFirstLogin() {
		return isFirstLogin;
	}
	public void setIsFirstLogin(Boolean isFirstLogin) {
		this.isFirstLogin = isFirstLogin;
	}


}
