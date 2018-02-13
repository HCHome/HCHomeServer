package HCHomeServer.service;

import HCHomeServer.model.db.User;
import HCHomeServer.model.db.UserApply;
import HCHomeServer.model.result.LightUser;
import HCHomeServer.model.result.LightUserApply;
import HCHomeServer.model.result.ScoreRank;
import HCHomeServer.model.result.UserSearchManager;
import HCHomeServer.model.result.UserSearchManager.MatchType;

/**
 * 处理与用户个人信息相关的业务层接口抽象
 * @author cj
 */
public interface UserService {
	//检查验证码是否存在，若存在则新增用户
	public LightUser checkUser(String verificationCode, String openedId, String avatar);
	//登录检验
	public LightUser login(String openId);
	//新增申请
	public void addUserApply(UserApply userApply);
	//签到
	public boolean sign(int userId);
	//更新头像
	public void updateAvatar(int userId, String avatar);
	//检验申请是否存在
	public LightUserApply checkApply(String openId);
	//获取积分排行列表
	public ScoreRank getScoreRank(int userId);
	//修改用户信息
	public void modifyUserInfo(User user);
	//用户模糊搜索
	public UserSearchManager searchFuzzilyUser(String searchWord, MatchType[] matchTypes);
	
	public User getUserInfo(int userId);

}
