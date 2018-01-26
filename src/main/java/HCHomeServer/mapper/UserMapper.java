package HCHomeServer.mapper;

import org.apache.ibatis.annotations.Param;

import HCHomeServer.model.db.User;

public interface UserMapper {

	public User getUserByOpenId(@Param("openId")String openId);
	public void addUser(User user);
	public void signScoreAdd(@Param("userId")int userId, @Param("score")int score);
	
}
