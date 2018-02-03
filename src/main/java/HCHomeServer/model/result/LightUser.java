package HCHomeServer.model.result;


import java.io.Serializable;

import HCHomeServer.model.db.SignRecord;
import HCHomeServer.model.db.User;

/**
 * 返回前端的用户信息包装类
 * @author cj
 */
public class LightUser implements Serializable{

	private static final long serialVersionUID = 8862880821556043645L;
	private int userId;
	private String nickname;
	private int signScore;
	private boolean isSign;
	private String avatar;
	private Integer unReadCount;
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
		lightUser.unReadCount = null;
		return lightUser;
	}
	public Integer getUnReadCount() {
		return unReadCount;
	}
	public void setUnReadCount(Integer unReadCount) {
		this.unReadCount = unReadCount;
	}

}
