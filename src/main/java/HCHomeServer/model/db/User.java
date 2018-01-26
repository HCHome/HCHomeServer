package HCHomeServer.model.db;

public class User {
	private int userId;
	private String nickname;
	private int signScore;
	private int infoId;
	private String wechatIdentify;
	public int getUserId() {
		return userId;
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
	public int getInfoId() {
		return infoId;
	}
	public void setInfoId(int infoId) {
		this.infoId = infoId;
	}
	public String getWechatIdentify() {
		return wechatIdentify;
	}
	public void setWechatIndentify(String wechatIndentify) {
		this.wechatIdentify = wechatIndentify;
	}

	private User() {
	}
	public static User createUser(PersonInfo info, String openedId) {
		User user = new User();
		user.setInfoId(info.getInfoId());
		user.setNickname(info.getName());
		user.setSignScore(0);
		user.setWechatIndentify(openedId);
		return user;
	}
}
