package HCHomeServer.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import HCHomeServer.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private PersonInfoMapper personInfoMapper;
	Logger logger = Logger.getLogger(getClass());
	@Autowired
	private UserApplyMapper userApplyMapper;	
	@Autowired
	private SignRecordMapper signRecordMapper;
	
	
	@Override
	@Transactional
	public LightUser checkUser(String verificationCode, String openedId, String avatar) {
		//安全码检验
		PersonInfo info = personInfoMapper.checkCode(verificationCode);
		if(info!=null) {
			//新增用户
			User user = User.createUser(info, openedId, avatar);
			userMapper.addUser(user);
			return LightUser.buildByUser(user, null);
		}
		return null;
	}

	@Override
	public LightUser login(String openId) {
		//检查用户是否注册
		User user = userMapper.getUserByOpenId(openId);
		if(user==null) {
			return null;
		}
		//检查今天是否已经签到
		SignRecord sign = signRecordMapper.getTodaySignRecord(user.getUserId()); 
		return LightUser.buildByUser(user,sign);
	}

	@Override
	public void addUserApply(UserApply userApply) {
		userApplyMapper.addUserApply(userApply);
	}

	@Override
	@Transactional
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

}
