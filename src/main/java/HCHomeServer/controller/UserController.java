package HCHomeServer.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import HCHomeServer.model.result.ResultData;
import HCHomeServer.model.result.ScoreRank;
import HCHomeServer.model.result.UserSearchManager;
import HCHomeServer.model.result.UserSearchManager.MatchType;
import HCHomeServer.cache.SessionCache;
import HCHomeServer.cache.SessionCache.SessionType;
import HCHomeServer.model.db.User;
import HCHomeServer.model.db.UserApply;
import HCHomeServer.model.result.LightUser;
import HCHomeServer.model.result.LightUserApply;
import HCHomeServer.service.UserService;
import HCHomeServer.wechat.Session;
import HCHomeServer.wechat.WeChat;
/**
 * 用于接收关于用户个人信息请求并分发的控制器
 * @author cj
 */
@Controller
@RequestMapping(value="/user")//,method=RequestMethod.POST)
public class UserController {
	
	@Autowired
	private UserService userService;
	
	Logger logger = Logger.getLogger(getClass());
	
	/**
	 * 登录接口，用户打开小程序且授权后所调用的接口
	 * 检查用户是否已经注册，并向微信服务器注册会话
	 * 未实现定时清除emmCode
	 * @param jsCode 
	 * @param request
	 * @return ResutData
	 */
	@RequestMapping("/login")
	@ResponseBody
	public ResultData login(
			@RequestParam("jsCode")String jsCode,
			@RequestParam("avatar")String avatar) {
		Map<String, Object> data = new HashMap<>();
		ResultData resultData=null;

		try {
			//向微信服务器申请会话
			Session session = WeChat.getSession(jsCode);
			//检查会话申请情况
			if(session==null) {
				resultData = ResultData.build_fail_result(data, "jsCode不正确", 10004);
			}else {
				LightUser user = userService.login(session.getOpenId(), avatar);
				//检查用户是否已经注册
				if(user == null) {
					LightUserApply userApply = userService.checkApply(session.getOpenId());
					//检查用户是否正在申请中
					if(userApply == null) {
						String key = WeChat.getUnionKey(jsCode);
						SessionCache.getInstance().put(key, session,SessionType.TEMP);
						data.put("emmCode", key);
						resultData = ResultData.build_fail_result(data, "用户不存在", 10003);
					}else {
						data.put("userApply", userApply);
						resultData = ResultData.build_fail_result(data, "申请信息审核中", 10006);
					}
				}else {
					SessionCache.getInstance().put(String.valueOf(user.getUserId()), session, SessionType.LOGINED);
					data.put("user", user);	
					resultData = ResultData.build_success_result(data);
				}
			}
			return resultData;
		}catch (Exception e) {
			e.printStackTrace();
			resultData = ResultData.build_fail_result(data, "异常", 10002);
			return resultData;
		}
	}
	/**
	 * 持有检验码的用户的注册接口
	 * 暂未实现重试次数限制
	 * @param emmCode 自定义密钥
	 * @param verificationCode 安全码
	 * @param request
	 * @return
	 */
	@RequestMapping("/register")
	@ResponseBody
	public ResultData register(
			@RequestParam("emmCode")String emmCode,
			@RequestParam("verificationCode")String verificationCode,
			@RequestParam("avatar")String avatar) {
		Map<String, Object> data = new HashMap<>();
		ResultData resultData = null;
		try {
			//检查是否已经向微信服务器申请会话
			Session session = SessionCache.getInstance().get(emmCode, SessionType.TEMP);
			if(session == null) {
				resultData = ResultData.build_fail_result(data, "与微信会话断开中", 10004);
			}else{
				LightUser user = userService.checkUser(verificationCode, session.getOpenId(), avatar);
				//检查检验码是否正确
				if(user != null) {
					data.put("user", user);
					resultData = ResultData.build_success_result(data);
				}else {
					resultData = ResultData.build_fail_result(data, "验证码不存在", 10003);
				}
			}		
			return resultData;
		}catch (Exception e) {
			e.printStackTrace();
			resultData = ResultData.build_fail_result(data, "异常", 10002);
			return resultData;
		}
	}
	/**
	 * 未持有安全码用户申请帐号接口，需要管理员审核
	 * @param term 届别
	 * @param name 姓名
	 * @param message 备注内容
	 * @param request
	 * @return
	 */
	@RequestMapping("/apply")
	@ResponseBody
	public ResultData apply(
			@RequestParam("term")int term,
			@RequestParam("name")String name,
			@RequestParam("message")String message,
			@RequestParam("emmCode")String emmCode,
			@RequestParam("avatar")String avatar){
		Map<String, Object> data = new HashMap<>();
		ResultData resultData = null;
		try {
			
			//检查是否已经向微信服务器申请会话
			if(SessionCache.getInstance().containsKey(emmCode, SessionType.TEMP)) {
				resultData = ResultData.build_fail_result(data, "与微信会话断开中", 10004);
			}else{
				UserApply userApply = new UserApply(term, name, message, 
						SessionCache.getInstance().get(emmCode, SessionType.TEMP).getOpenId(), avatar);
				userService.addUserApply(userApply);
				resultData = ResultData.build_success_result(data);
			}
			
			return resultData;
		}catch (Exception e) {
			e.printStackTrace();
			resultData = ResultData.build_fail_result(data, "异常", 10002);
			return resultData;
		}
	}
	/**
	 * 签到接口
	 * @param userId
	 * @return
	 */
	@RequestMapping(value="sign")
	@ResponseBody
	public ResultData sign(
			@RequestParam("userId")int userId) {
		Map<String, Object> data = new HashMap<>();
		ResultData resultData = null;
		try {
			boolean result = userService.sign(userId);
			//检查是否已经签到过了
			if(result) {
				resultData = ResultData.build_success_result(data);
			}else {
				resultData = ResultData.build_fail_result(data, "今日已签到", 10006);
			}
			return resultData;
		}catch (Exception e) {
			e.printStackTrace();
			resultData = ResultData.build_fail_result(data, "异常", 10002);
			return resultData;
		}
	}
	/**
	 * 注销接口，用于清除application中保存的数据
	 * @param userId
	 * @return
	 */
	@RequestMapping(value="/logout")
	@ResponseBody
	public ResultData logout(
			@RequestParam("userId")String userId) {
		Map<String, Object> data = new HashMap<>();
		ResultData resultData = null;
		try {
			SessionCache sessionCache = SessionCache.getInstance();
			if(sessionCache.containsKey(userId, SessionType.LOGINED))sessionCache.remove(userId, SessionType.LOGINED);
			resultData = ResultData.build_success_result(data);
			return resultData;
		}catch (Exception e) {
			e.printStackTrace();
			resultData = ResultData.build_fail_result(data, "异常", 10002);
			return resultData;
		}
	}
	/**
	 * 积分排行
	 * @param userId
	 * @return
	 */
	@RequestMapping("/scoreRankList")
	@ResponseBody
	public ResultData scoreRankList(
			@RequestParam("userId")int userId) {
		ResultData resultData = null;
		try {
			ScoreRank scoreRank = userService.getScoreRank(userId);
			resultData = ResultData.build_success_result(scoreRank.convertToMap());
			return resultData;
		}catch (Exception e) {
			e.printStackTrace();
			resultData = ResultData.build_fail_result(null, "异常", 10002);
			return resultData;
		}
	}
	/**
	 * 未实现
	 * @return
	 */
	@RequestMapping(value="/modifyUserInfo",
			params= {
				"userId", "sex", "school", "profession", "job", "term", "isDisplay", 
				"phoneNumber", "qqNumber", "wechatNumber", "isSingleDog"
			})
	@ResponseBody
	public ResultData modifyUserInfo(
//			@RequestParam("userId")int userId,
//			@RequestParam("sex")String sex,
//			@RequestParam("school")String school,
//			@RequestParam("profession")String profession,
//			@RequestParam("job")String job,
//			@RequestParam("term")String term,
//			@RequestParam("phoneNumber")String phoneNumber,
//			@RequestParam("qqNumber")String qqNumber,
//			@RequestParam("wechatNumber")String wechatNumber,
//			@RequestParam("isSingleDog")boolean isSingleDog,
			User user) {
		Map<String, Object> data = new HashMap<>();
		ResultData resultData = null;
		try {
			userService.modifyUserInfo(user);
			resultData = ResultData.build_success_result(data);
			return resultData;
		}catch (Exception e) {
			e.printStackTrace();
			resultData = ResultData.build_fail_result(data, "异常", 10002);
			return resultData;
		}
	}
	/**
	 * 通过关键字搜人
	 * @param searchWord
	 * @return
	 */
	@RequestMapping("/searchUser")
	@ResponseBody
	public ResultData searchUser(
			@RequestParam("searchWord")String searchWord,
			@RequestParam(value="matchType", required=false)MatchType[] matchTypes){
		Map<String, Object> data = new HashMap<>();
		ResultData resultData = null;
		try {
			UserSearchManager searchResult = userService.searchFuzzilyUser(searchWord, matchTypes);
			resultData = ResultData.build_success_result(searchResult.convertToMap());
			return resultData;
		}catch (Exception e) {
			e.printStackTrace();
			resultData = ResultData.build_fail_result(data, "异常", 10002);
			return resultData;
		}
	}
	/**
	 * 需求不明，暂未实现
	 * @param userId
	 * @return
	 */
	@RequestMapping("/getUserInfo")
	@ResponseBody
	public ResultData getUserInfo(
			@RequestParam("userId")int userId) {
		Map<String, Object> data = new HashMap<>();
		ResultData resultData = null;
		try {
			User user = userService.getUserInfo(userId);
			data.put("user", user);
			resultData = ResultData.build_success_result(data);
			return resultData;
		}catch (Exception e) {
			e.printStackTrace();
			resultData = ResultData.build_fail_result(data, "异常", 10002);
			return resultData;
		}
	}
}
