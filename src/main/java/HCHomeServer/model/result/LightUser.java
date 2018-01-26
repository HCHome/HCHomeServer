package HCHomeServer.model.result;

import org.eclipse.jdt.internal.compiler.ast.ThisReference;

import HCHomeServer.model.db.PersonInfo;
import HCHomeServer.model.db.SignRecord;
import HCHomeServer.model.db.User;

public class LightUser {
	private int userId;
	private String nickname;
	private int signScore;
	private boolean isSign;
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
		if(sign==null) {
			lightUser.setSign(false);
		}else {
			lightUser.setSign(true);
		}
		return lightUser;
	}

}
