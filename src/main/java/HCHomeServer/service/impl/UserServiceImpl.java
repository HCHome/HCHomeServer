package HCHomeServer.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import HCHomeServer.cache.UnReadCount;
import HCHomeServer.mapper.PersonInfoMapper;
import HCHomeServer.mapper.SignRecordMapper;
import HCHomeServer.mapper.UserApplyMapper;
import HCHomeServer.mapper.UserMapper;
import HCHomeServer.model.db.PersonInfo;
import HCHomeServer.model.db.SignRecord;
import HCHomeServer.model.db.User;
import HCHomeServer.model.db.UserApply;
import HCHomeServer.model.result.LightUser;
import HCHomeServer.model.result.LightUserApply;
import HCHomeServer.model.result.ScoreRank;
import HCHomeServer.model.result.UserSearchManager;
import HCHomeServer.service.UserService;
import HCHomeServer.model.result.UserSearchManager.*;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private PersonInfoMapper personInfoMapper;
	@Autowired
	private UserApplyMapper userApplyMapper;	
	@Autowired
	private SignRecordMapper signRecordMapper;
	
//	private Logger logger = Logger.getLogger(getClass());
	
	@Override
	@Transactional(rollbackFor=Exception.class)
	public LightUser checkUser(String verificationCode, String openedId, String avatar) {
		//安全码检验
		PersonInfo info = personInfoMapper.checkCode(verificationCode);
		if(info!=null) {
			//新增用户
			User user;
			user = userMapper.getUserByOpenId(openedId);
			if(user == null) {
				user = User.createUser(info, openedId, avatar);
				userMapper.addUser(user);
			}else {
				userMapper.updateAvatar(user.getUserId(), avatar);
			}
			return LightUser.buildByUser(user, null);
		}
		return null;
	}

	@Override
	public LightUser login(String openId, String avatar) {
		//检查用户是否注册
		User user = userMapper.getUserByOpenId(openId);
		if(user==null) {
			return null;
		}
		//检查今天是否已经签到
		SignRecord sign = signRecordMapper.getTodaySignRecord(user.getUserId()); 
		//头像
		if(user.getAvatar()==null||!user.getAvatar().equals(avatar)) {
			user.setAvatar(avatar);
			(new Thread(new Runnable() {
				@Override
				public void run() {
					updateAvatar(user.getUserId(),avatar);	
				}
			})).start();
		}
		LightUser lightUser = LightUser.buildByUser(user,sign);
		
		if(user.getSchool()==null) {
			lightUser.setIsFirstLogin(Boolean.TRUE);
		}else {
			lightUser.setIsFirstLogin(Boolean.FALSE);
		}
		//获取读者未读消息数
		lightUser.setUnReadCount(UnReadCount.getInstance().getUnRead(user.getUserId()));
		return lightUser;
	}

	@Override
	public void addUserApply(UserApply userApply) {
		userApplyMapper.addUserApply(userApply);
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public boolean sign(int userId) {
		//检查今天是否已经签到
		if(signRecordMapper.getTodaySignRecord(userId)!=null) {
			return false;
		}else {
			//签到并更新积分
			signRecordMapper.addSign(SignRecord.creatSign(userId));
			userMapper.signScoreAdd(userId, 1);
			return true;
		}
	}

	@Override
	public void updateAvatar(int userId, String avatar) {
		userMapper.updateAvatar(userId, avatar);
		
	}

	@Override
	public LightUserApply checkApply(String openId) {
		return userApplyMapper.checkApply(openId);
		
	}

	@Override
	public ScoreRank getScoreRank(int userId) {
		ScoreRank scoreRank = new ScoreRank();
		scoreRank.setUserScoreRankFromUser(userMapper.getUserByUserId(userId), userMapper.getRankByUserId(userId));
		scoreRank.addRankList(userMapper.getTopSignScoreRankList(50));
		return scoreRank;
	}

	@Override
	public void modifyUserInfo(User user) {
		userMapper.modifyUserInfo(user);
		
	}

	@Override
	public UserSearchManager searchFuzzilyUser(String searchWord, MatchType[] matchTypes) {
		searchWord = UserSearchManager.searchWordPreHandle(searchWord);
		UserSearchManager userSearchManager = new UserSearchManager();
		List<User> users = null;
		if(matchTypes==null) {
			for(MatchType e : MatchType.values()) {
				users = userMapper.searchUser(searchWord, e.name());
				userSearchManager.addResultsByUsers(users, e);
			}
		}else {
			for(MatchType matchType : matchTypes) {
				users = userMapper.searchUser(searchWord, matchType.name());
				userSearchManager.addResultsByUsers(users, matchType);
			}
		}
		return userSearchManager;
	}

	@Override
	public User getUserInfo(int userId) {
		return userMapper.getUserByUserId(userId);
	}

}
