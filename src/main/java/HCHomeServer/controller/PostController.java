package HCHomeServer.controller;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


import HCHomeServer.model.db.Post;
import HCHomeServer.model.db.PostPicture;
import HCHomeServer.model.result.ResultData;
import HCHomeServer.service.PostService;
/**
 * 帖子管理接口的控制器
 * @author cj
 *
 */
@Controller
@RequestMapping(value="/post")
public class PostController {
	
	@Autowired
	private PostService postService;
	
	/**
	 * 新增帖子
	 * @param category 帖子类别，共四类，分别是潮友日常、实习工作、学习交流、求助发帖
	 * @param title
	 * @param text
	 * @param pictureCount
	 * @param userId
	 * @return
	 */
	@RequestMapping("/newPost")
	@ResponseBody
	public ResultData newPost(
			@RequestParam("category")String category,
			@RequestParam("title")String title,
			@RequestParam("text")String text,
			@RequestParam("pictureCount")int pictureCount,
			@RequestParam("userId")int userId) {
		Map<String, Object> data = new HashMap<>();
		ResultData resultData = null;
		try {
			Post post = Post.creatPost(category, title, text, pictureCount, userId);
			postService.addPost(post);
			data.put("postInfoWithoutImage", post);
			resultData = ResultData.build_success_result(data);
			return resultData;
		}catch (Exception e) {
			e.printStackTrace();
			resultData = ResultData.build_fail_result(data, "异常", 10002);
			return resultData;
		}
	}
	
	
	/**
	 * 帖子图片上传接口
	 * @param postPicture
	 * @return
	 */
	@RequestMapping("/uploadPostPicture")
	@ResponseBody
	public ResultData uploadPostPicture(@RequestParam("postPictureEntity")MultipartFile postPictureEntity,
			@RequestParam("postId")int postId,
			@RequestParam("order")int order) throws Exception {
		Map<String, Object> data = new HashMap<>();
		ResultData resultData = null;
		try {
//		CosTool.uploadPostPicture(postPicture.getBytes(), "/post/hhh.jpg");
			
			PostPicture postPicture = postService.addPostPicture(postPictureEntity,postId, order);
			data.put("pictureInfo", postPicture);
			resultData = ResultData.build_success_result(data);
			return resultData;
		}catch (Exception e) {
			e.printStackTrace();
			resultData = ResultData.build_fail_result(data, "异常", 10002);
			return resultData;
		}	
	}
	
	@RequestMapping("deletePost")
	@ResponseBody
	public ResultData deletePost(
			@RequestParam("userId")int userId,
			@RequestParam("postId")int postId) {
		Map<String, Object> data = new HashMap<>();
		ResultData resultData = null;
		try {
			postService.deletePost(userId, postId);
			return resultData;
		}catch (Exception e) {
			e.printStackTrace();
			resultData = ResultData.build_fail_result(data, "异常", 10002);
			return resultData;
		}	
	}
	
}
