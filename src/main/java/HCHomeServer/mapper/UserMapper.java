package HCHomeServer.mapper;

import java.util.List;

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
	public void updateAvatar(@Param("userId")int userId, @Param("avatar")String avatar);
	public User getUserByUserId(@Param("userId")int userId);
	public int getRankByUserId(@Param("userId")int userId);
	public List<User> getTopSignScoreRankList(@Param("count")int count);
	
}
