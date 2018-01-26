package HCHomeServer.service;

import HCHomeServer.model.db.UserApply;
import HCHomeServer.model.result.LightUser;

/**
 * 处理与用户个人信息相关的业务层接口抽象
 * @author cj
 */
public interface UserService {

	public LightUser checkUser(String verificationCode, String openedId);

	public LightUser login(String openId);

	public void addUserApply(UserApply userApply);

	public boolean sign(int userId);

}
