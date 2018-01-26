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
	public LightUser checkUser(String verificationCode, String openedId) {
		PersonInfo info = personInfoMapper.checkCode(verificationCode);
		if(info!=null) {
			User user = User.createUser(info, openedId);
			userMapper.addUser(user);
			return LightUser.buildByUser(user, null);
		}
		return null;
	}

	@Override
	public LightUser login(String openId) {
		User user = userMapper.getUserByOpenId(openId);
		if(user==null) {
			return null;
		}
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
		if(signRecordMapper.getTodaySignRecord(userId)!=null) {
			return false;
		}else {
			signRecordMapper.addSign(SignRecord.creatSign(userId));
			userMapper.signScoreAdd(userId, 1);
			return true;
		}
	}

}
