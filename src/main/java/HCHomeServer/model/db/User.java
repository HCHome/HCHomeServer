package HCHomeServer.model.db;


public class User {
	private Integer userId;
	private String nickname;
	private Integer signScore;
	private Integer infoId;
	private String wechatIdentify;
	private String avatar;
	private String sex;
	private String school;
	private String profession;
	private String job;
	private String term;
	private String phoneNumber;
	private String qqNumber;
	private String wechatNumber;
	private Boolean isSingleDog;
	private Boolean isDisplay;
	private User() {
	}
	public static User createUser(PersonInfo info, String openedId, String avatar) {
		User user = new User();
		user.setInfoId(info.getInfoId());
		user.setNickname(info.getName());
		user.setSignScore(0);
		user.setWechatIdentify(openedId);
		user.setAvatar(avatar);
		return user;
	}
	public Integer getUserId() {
		return userId;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public void setWechatIdentify(String wechatIdentify) {
		this.wechatIdentify = wechatIdentify;
	}
	public String getWechatIdentify() {
		return wechatIdentify;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public Integer getSignScore() {
		return signScore;
	}
	public void setSignScore(Integer signScore) {
		this.signScore = signScore;
	}
	public Integer getInfoId() {
		return infoId;
	}
	public void setInfoId(Integer infoId) {
		this.infoId = infoId;
	}

	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getSchool() {
		return school;
	}
	public void setSchool(String school) {
		this.school = school;
	}
	public String getProfession() {
		return profession;
	}
	public void setProfession(String profession) {
		this.profession = profession;
	}
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getQqNumber() {
		return qqNumber;
	}
	public void setQqNumber(String qqNumber) {
		this.qqNumber = qqNumber;
	}
	public String getWechatNumber() {
		return wechatNumber;
	}
	public void setWechatNumber(String wechatNumber) {
		this.wechatNumber = wechatNumber;
	}
	public Boolean getIsSingleDog() {
		return isSingleDog;
	}
	public void setIsSingleDog(Boolean isSingleDog) {
		this.isSingleDog = isSingleDog;
	}
	public Boolean getIsDisplay() {
		return isDisplay;
	}
	public void setIsDisplay(Boolean isDisplay) {
		this.isDisplay = isDisplay;
	}
	
}
