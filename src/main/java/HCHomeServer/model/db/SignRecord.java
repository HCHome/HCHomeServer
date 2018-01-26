package HCHomeServer.model.db;

import java.util.Date;

public class SignRecord {
	private int signId;
	private int userId;
	private Date signDate;
	public int getSignId() {
		return signId;
	}
	public void setSignId(int signId) {
		this.signId = signId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public Date getSignDate() {
		return signDate;
	}
	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}
	public static SignRecord creatSign(int userId) {
		SignRecord signRecord = new SignRecord();
		signRecord.setUserId(userId);
		signRecord.setSignDate(new Date());
		return signRecord;
	}
}
