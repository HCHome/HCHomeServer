package HCHomeServer.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
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
	@SuppressWarnings({"finally" })
	@RequestMapping("/login")
	@ResponseBody
	public ResultData login(
			@RequestParam("jsCode")String jsCode,
			HttpServletRequest request) {
		Map<String, Object> data = new HashMap<>();
		ResultData resultData=null;
		HttpSession httpSession = request.getSession();
//		ServletContext application = httpSession.getServletContext();
		try {
			//向微信服务器申请会话
			Session session = WeChat.getSession(jsCode);
			//检查会话申请情况
			if(session==null) {
				resultData = ResultData.build_fail_result(data, "jsCode不正确", 10004);
			}else {
				LightUser user = userService.login(session.getOpenId());
				//将微信会话存进session
				httpSession.setAttribute("wechat_session", session);
				//检查用户是否已经注册
				if(user == null) {
//					Map<String, Session>tempSessionMap = (ConcurrentHashMap<String, Session>) application.getAttribute("tempSessionMap");
//					if(tempSessionMap==null) {
//						tempSessionMap = new ConcurrentHashMap<>();
//					}
					String key = WeChat.getUnionKey(jsCode);
					
					data.put("emmCode", key);
					resultData = ResultData.build_fail_result(data, "用户不存在", 10003);
				}else {
//					Map<String, Session>userSessionMap = (ConcurrentHashMap<String, Session>) application.getAttribute("userSessionMap");
//					userSessionMap.put(String.valueOf(user.getUserId()), session);
					data.put("user", user);
					resultData = ResultData.build_success_result(data);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			resultData = ResultData.build_fail_result(data, "异常", 10002);
		}finally {
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
	@SuppressWarnings("finally")
	@RequestMapping("/register")
	@ResponseBody
	public ResultData register(
			@RequestParam("emmCode")String emmCode,
			@RequestParam("verificationCode")String verificationCode,
			HttpServletRequest request) {
		HttpSession httpSession =request.getSession();
		Map<String, Object> data = new HashMap<>();
		ResultData resultData = null;
		try {
			Session session = (Session)httpSession.getAttribute("wechat_session");
			//检查是否已经向微信服务器申请会话
			if(session==null) {
				resultData = ResultData.build_fail_result(data, "与微信会话断开中", 10004);
			}else {
				
				LightUser user = userService.checkUser(verificationCode, session.getOpenId());
				//检查检验码是否正确
				if(user != null) {
					data.put("user", user);
					resultData = ResultData.build_success_result(data);
				}else {
					resultData = ResultData.build_fail_result(data, "验证码不存在", 10003);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			resultData = ResultData.build_fail_result(data, "异常", 10002);
		}finally{
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
	@SuppressWarnings("finally")
	@RequestMapping("/apply")
	@ResponseBody
	public ResultData apply(
			@RequestParam("term")int term,
			@RequestParam("name")String name,
			@RequestParam("message")String message,
			HttpServletRequest request){
		HttpSession httpSession = request.getSession();
		Map<String, Object> data = new HashMap<>();
		ResultData resultData = null;
		try {
			Session session = (Session)httpSession.getAttribute("wechat_session");
			//检查是否已经向微信服务器申请会话
			if(session==null) {
				resultData = ResultData.build_fail_result(data, "与微信会话断开中", 10004);
			}else {
				UserApply userApply = new UserApply(term, name, message, session.getOpenId());
				userService.addUserApply(userApply);
				resultData = ResultData.build_success_result(data);
			}
		}catch (Exception e) {
			e.printStackTrace();
			resultData = ResultData.build_fail_result(data, "异常", 10002);
		}finally{
			return resultData;
		}
	}
	/**
	 * 签到接口
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("finally")
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
		}catch (Exception e) {
			e.printStackTrace();
			resultData = ResultData.build_fail_result(data, "异常", 10002);
		}finally{
			return resultData;
		}
	}
}
