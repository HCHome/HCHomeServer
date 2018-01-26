package HCHomeServer.mapper;

import org.apache.ibatis.annotations.Param;

import HCHomeServer.model.db.User;

/**
 * 用户个人信息映射管理
 * @author cj
 */
public interface UserMapper {

	public User getUserByOpenId(@Param("openId")String openId);
	public void addUser(User user);
	public void signScoreAdd(@Param("userId")int userId, @Param("score")int score);
	
}
