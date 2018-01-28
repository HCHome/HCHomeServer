package HCHomeServer.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import HCHomeServer.model.result.ResultData;
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
	 * @param jsCode 
	 * @param request
	 * @return ResutData
	 */
	@SuppressWarnings({"unchecked" })
	@RequestMapping("/login")
	@ResponseBody
	public ResultData login(
			@RequestParam("jsCode")String jsCode,
			@RequestParam("avatar")String avatar,
			HttpSession httpSession) {
		Map<String, Object> data = new HashMap<>();
		ResultData resultData=null;
//		HttpSession httpSession = request.getSession();
		ServletContext application = httpSession.getServletContext();
		try {
			//向微信服务器申请会话
			Session session = WeChat.getSession(jsCode);
			//检查会话申请情况
			if(session==null) {
				resultData = ResultData.build_fail_result(data, "jsCode不正确", 10004);
			}else {
				LightUser user = userService.login(session.getOpenId());
//				//将微信会话存进session
//				httpSession.setAttribute("wechat_session", session);
				//检查用户是否已经注册
				if(user == null) {
					LightUserApply userApply = userService.checkApply(session.getOpenId());
					if(userApply == null) {
						Map<String, Session> tempSessionMap = (ConcurrentHashMap<String, Session>) application.getAttribute("tempSessionMap");
						if(tempSessionMap==null) {
							tempSessionMap = new ConcurrentHashMap<>();
						}
						String key = WeChat.getUnionKey(jsCode);
						tempSessionMap.put(key, session);
						application.setAttribute("tempSessionMap", tempSessionMap);
						data.put("emmCode", key);
						resultData = ResultData.build_fail_result(data, "用户不存在", 10003);
					}else {
						data.put("userApply", userApply);
						resultData = ResultData.build_fail_result(data, "申请信息审核中", 10006);
					}
				}else {
					Map<String, Session>userSessionMap = (ConcurrentHashMap<String, Session>) application.getAttribute("userSessionMap");
					if(userSessionMap==null)userSessionMap = new ConcurrentHashMap<>();
					userSessionMap.put(String.valueOf(user.getUserId()), session);
					application.setAttribute("userSessionMap", userSessionMap);
					if(user.getAvatar()==null||!user.getAvatar().equals(avatar)) {
						user.setAvatar(avatar);
						(new Thread(new Runnable() {
							@Override
							public void run() {
								userService.updateAvatar(user.getUserId(),avatar);	
							}
						})).start();
					}
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
	@SuppressWarnings({"unchecked" })
	@RequestMapping("/register")
	@ResponseBody
	public ResultData register(
			@RequestParam("emmCode")String emmCode,
			@RequestParam("verificationCode")String verificationCode,
			@RequestParam("avatar")String avatar,
			HttpSession httpSession) {
		ServletContext application = httpSession.getServletContext();
		Map<String, Object> data = new HashMap<>();
		ResultData resultData = null;
		try {
			Map<String, Session>tempSessionMap = (ConcurrentHashMap<String, Session>)application.getAttribute("tempSessionMap");
			//检查是否已经向微信服务器申请会话
			if(tempSessionMap==null) {
				resultData = ResultData.build_fail_result(data, "与微信会话断开中", 10004);
			}else {
				Session session = tempSessionMap.get(emmCode);
				if(session == null) {
					resultData = ResultData.build_fail_result(data, "与微信会话断开中", 10004);
				}else{
					LightUser user = userService.checkUser(verificationCode, session.getOpenId(),avatar);
					//检查检验码是否正确
					if(user != null) {
						data.put("user", user);
						resultData = ResultData.build_success_result(data);
					}else {
						resultData = ResultData.build_fail_result(data, "验证码不存在", 10003);
					}
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
	@SuppressWarnings({"unchecked" })
	@RequestMapping("/apply")
	@ResponseBody
	public ResultData apply(
			@RequestParam("term")int term,
			@RequestParam("name")String name,
			@RequestParam("message")String message,
			@RequestParam("emmCode")String emmCode,
			@RequestParam("avatar")String avatar,
			HttpSession httpSession){
		ServletContext application = httpSession.getServletContext();
		Map<String, Object> data = new HashMap<>();
		ResultData resultData = null;
		try {
			Map<String, Session>tempSessionMap = (ConcurrentHashMap<String, Session>)application.getAttribute("tempSessionMap");
			//检查是否已经向微信服务器申请会话
			if(tempSessionMap==null) {
				resultData = ResultData.build_fail_result(data, "与微信会话断开中", 10004);
			}else {
				Session session = tempSessionMap.get(emmCode);
				if(session == null) {
					resultData = ResultData.build_fail_result(data, "与微信会话断开中", 10004);
				}else{
					UserApply userApply = new UserApply(term, name, message, session.getOpenId(),avatar);
					userService.addUserApply(userApply);
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
	 * @param httpSession
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	@RequestMapping(value="/logout")
	@ResponseBody
	public ResultData logout(
			@RequestParam("userId")String userId,
			HttpSession httpSession) {
		ServletContext application = httpSession.getServletContext();
		Map<String, Object> data = new HashMap<>();
		ResultData resultData = null;
		try {
			Map<String, Session>userSessionMap = (ConcurrentHashMap<String, Session>)application.getAttribute("userSessionMap");
			if(userSessionMap==null) {
				resultData = ResultData.build_success_result(data);
			}else {
				if(userSessionMap.containsKey("userId"))userSessionMap.remove(userId);
				resultData = ResultData.build_success_result(data);
			}
			return resultData;
		}catch (Exception e) {
			e.printStackTrace();
			resultData = ResultData.build_fail_result(data, "异常", 10002);
			return resultData;
		}
	}
}
