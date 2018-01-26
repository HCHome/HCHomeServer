package HCHomeServer.service;

import HCHomeServer.model.db.UserApply;
import HCHomeServer.model.result.LightUser;

public interface UserService {

	public LightUser checkUser(String verificationCode, String openedId);

	public LightUser login(String openId);

	public void addUserApply(UserApply userApply);

	public boolean sign(int userId);

}
