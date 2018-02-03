package HCHomeServer.service;

import HCHomeServer.model.db.UserApply;
import HCHomeServer.model.result.LightUser;
import HCHomeServer.model.result.LightUserApply;
import HCHomeServer.model.result.ScoreRank;

/**
 * 处理与用户个人信息相关的业务层接口抽象
 * @author cj
 */
public interface UserService {

	public LightUser checkUser(String verificationCode, String openedId, String avatar);

	public LightUser login(String openId);

	public void addUserApply(UserApply userApply);

	public boolean sign(int userId);

	public void updateAvatar(int userId, String avatar);

	public LightUserApply checkApply(String openId);

	public ScoreRank getScoreRank(int userId);

}
