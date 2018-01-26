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

@Controller
@RequestMapping(value="/user")//,method=RequestMethod.POST)
public class UserController {
	
	@Autowired
	private UserService userService;
	
	Logger logger = Logger.getLogger(getClass());
	
	@SuppressWarnings({ "unchecked", "finally" })
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
			Session session = WeChat.getSession(jsCode);
			//jscode
			if(session==null) {
				resultData = ResultData.build_fail_result(data, "jsCode不正确", 10004);
			}else {
				LightUser user = userService.login(session.getOpenId());
				httpSession.setAttribute("wechat_session", session);
				System.out.println(user);
				//
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
			if(session==null) {
				resultData = ResultData.build_fail_result(data, "与微信会话断开中", 10004);
			}else {
				
				LightUser user = userService.checkUser(verificationCode, session.getOpenId());
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
	
	@SuppressWarnings("finally")
	@RequestMapping(value="sign")
	@ResponseBody
	public ResultData sign(
			@RequestParam("userId")int userId) {
		Map<String, Object> data = new HashMap<>();
		ResultData resultData = null;
		try {
			boolean result = userService.sign(userId);
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
