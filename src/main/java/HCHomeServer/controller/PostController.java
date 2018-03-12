package HCHomeServer.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import HCHomeServer.cache.UnReadCount;
import HCHomeServer.model.db.Post;
import HCHomeServer.model.db.PostPicture;
import HCHomeServer.model.db.PostReply;
import HCHomeServer.model.result.PostInfo;
import HCHomeServer.model.result.ReceivedReply;
import HCHomeServer.model.result.ReplyInfo;
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
	public ResultData uploadPostPicture(
			@RequestParam("postPictureEntity")MultipartFile postPictureEntity,
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
	/**
	 * 删除帖子接口
	 * @param userId
	 * @param postId
	 * @return
	 */
	@RequestMapping("deletePost")
	@ResponseBody
	public ResultData deletePost(
			@RequestParam("userId")int userId,
			@RequestParam("postId")int postId) {
		Map<String, Object> data = new HashMap<>();
		ResultData resultData = null;
		try {
			postService.deletePost(userId, postId);
			resultData = ResultData.build_success_result(data);
			return resultData;
		}catch (Exception e) {
			e.printStackTrace();
			resultData = ResultData.build_fail_result(data, "异常", 10002);
			return resultData;
		}	
	}
	/**
	 * 获取置顶帖列表接口
	 * @return
	 */
	@RequestMapping("/topPosts")
	@ResponseBody
	public ResultData topPosts() {
		Map<String, Object> data = new HashMap<>();
		ResultData resultData = null;
		try {
			ArrayList<PostInfo> postInfos = postService.getTopPosts();
			data.put("topPosts", postInfos);
			resultData = ResultData.build_success_result(data);
			return resultData;
		}catch (Exception e) {
			e.printStackTrace();
			resultData = ResultData.build_fail_result(data, "异常", 10002);
			return resultData;
		}	
	}
	/**
	 * 获取所有最新帖子接口
	 * @param userId
	 * @param lastPostId
	 * @return
	 */
	@RequestMapping(value="/postListForAll")
	@ResponseBody
	public ResultData postListForAll(
//			@RequestParam("userId")int userId,
			@RequestParam("lastPostId")int lastPostId) {
		Map<String, Object> data = new HashMap<>();
		ResultData resultData = null;
		try {
			ArrayList<PostInfo> postInfos = postService.getPostListForAll(lastPostId);
			data.put("postList", postInfos);
			resultData = ResultData.build_success_result(data);
			return resultData;
		}catch (Exception e) {
			e.printStackTrace();
			resultData = ResultData.build_fail_result(data, "异常", 10002);
			return resultData;
		}	
	}
	/**
	 * 获取分类帖子列表接口
	 * @param userId
	 * @param category
	 * @param lastPostId
	 * @return
	 */
	@RequestMapping("/postListForCategory")
	@ResponseBody
	public ResultData postListForCategory(
//			@RequestParam("userId")int userId,
			@RequestParam("category")String category,
			@RequestParam(value="lastPostId", required=false, defaultValue="0")int lastPostId) {
		Map<String, Object> data = new HashMap<>();
		ResultData resultData = null;
		try {
			ArrayList<PostInfo> postInfos = postService.getPostListForCategory(category,lastPostId);
			data.put("postList", postInfos);
			resultData = ResultData.build_success_result(data);
			return resultData;
		}catch (Exception e) {
			e.printStackTrace();
			resultData = ResultData.build_fail_result(data, "异常", 10002);
			return resultData;
		}	
	}
	/**
	 * 获取帖子回复列表接口
	 * @param userId
	 * @param postId
	 * @param lastReplyId
	 * @return
	 */
	@RequestMapping("/postReplies")
	@ResponseBody
	public ResultData postReplies(
//			@RequestParam("userId")int userId,
			@RequestParam("postId")int postId,
			@RequestParam(value="lastReplyId", required=false, defaultValue="0" )int lastReplyId) {
		Map<String, Object> data = new HashMap<>();
		ResultData resultData = null;
		try {
			List<ReplyInfo> replyInfos = postService.getReplyListByPostId(postId, lastReplyId);
			data.put("replyList", replyInfos);
			resultData = ResultData.build_success_result(data);
			return resultData;
		}catch (Exception e) {
			e.printStackTrace();
			resultData = ResultData.build_fail_result(data, "异常", 10002);
			return resultData;
		}	
	}
	/**
	 * 新增回复接口
	 * @param userId
	 * @param postId
	 * @param repliedFloor 所回复的楼层号，若回复帖子本身，则为0
	 * @param repliedFloorUserId 所回复楼层的发表者id，若回复帖子本身，则为贴主id
	 * @param posterId 贴主id
	 * @param text
	 * @return
	 */
	@RequestMapping("/addReply")
	@ResponseBody
	public ResultData addReply(
			@RequestParam("userId")int userId,
			@RequestParam("postId")int postId,
			@RequestParam("repliedFloor")int repliedFloor,
			@RequestParam("repliedFloorUserId")int repliedFloorUserId,
			@RequestParam("posterId")int posterId,
			@RequestParam("text")String text) {
		Map<String, Object> data = new HashMap<>();
		ResultData resultData = null;
		try {
			
			ReplyInfo replyInfo = postService.addReply(PostReply.create(postId,userId,repliedFloor,text));
			//更新未读消息
			if(repliedFloorUserId==posterId) {
				UnReadCount.getInstance().upUnRead(posterId);
			}else {
				UnReadCount.getInstance().upUnRead(posterId).upUnRead(repliedFloorUserId);
			}
			
			data.put("replyInfo", replyInfo);
			resultData = ResultData.build_success_result(data);
			return resultData;
		}catch (Exception e) {
			e.printStackTrace();
			resultData = ResultData.build_fail_result(data, "异常", 10002);
			return resultData;
		}	
	}
	/**
	 * 删除一条回复
	 * @param replyId
	 * @return
	 */
	@RequestMapping("/deleteReply")
	@ResponseBody
	public ResultData deleteReply(@RequestParam("replyId")int replyId) {
		Map<String, Object> data = new HashMap<>();
		ResultData resultData = null;
		try {
			postService.deleteReply(replyId);
			resultData = ResultData.build_success_result(data);
			return resultData;
		}catch (Exception e) {
			e.printStackTrace();
			resultData = ResultData.build_fail_result(data, "异常", 10002);
			return resultData;
		}	
	}
	/**
	 * 获取我发布的帖子列表
	 * @param userId
	 * @return
	 */
	@RequestMapping(value="/getUserPostList")
	@ResponseBody
	public ResultData getUserPostList(
			@RequestParam("userId")int userId) {
		Map<String, Object> data = new HashMap<>();
		ResultData resultData = null;
		try {
			ArrayList<PostInfo> postInfos = postService.getUserPostList(userId);
			data.put("postList", postInfos);
			resultData = ResultData.build_success_result(data);
			return resultData;
		}catch (Exception e) {
			e.printStackTrace();
			resultData = ResultData.build_fail_result(data, "异常", 10002);
			return resultData;
		}	
	}
	/**
	 * 通过帖子Id获取帖子的详细信息，不包括回复
	 * @param postId
	 * @return
	 */
	@RequestMapping(value="/getPostInfoByPostId")
	@ResponseBody
	public ResultData getPostInfoByPostId(
			@RequestParam("postId")int postId) {
		Map<String, Object> data = new HashMap<>();
		ResultData resultData = null;
		try {
			PostInfo postInfo = postService.getPostInfoByPostId(postId);
			data.put("postInfo", postInfo);
			resultData = ResultData.build_success_result(data);
			return resultData;
		}catch (Exception e) {
			e.printStackTrace();
			resultData = ResultData.build_fail_result(data, "异常", 10002);
			return resultData;
		}	
	}
	/**
	 * 获取我收到的回复列表
	 * @param userId
	 * @return
	 */
	@RequestMapping("/getMyReceivedReplies")
	@ResponseBody
	public ResultData getMyReceivedReplies(
			@RequestParam("userId")int userId,
			@RequestParam(value="lastReplyId",required=false,defaultValue="0")int lastReplyId) {
		Map<String, Object> data = new HashMap<>();
		ResultData resultData = null;
		try {
			List<ReceivedReply> receivedReplies = postService.getReplyListByUserId(userId, lastReplyId);
			UnReadCount.getInstance().getAndRemoveUnRead(userId);
			data.put("receivedReplies", receivedReplies);
			resultData = ResultData.build_success_result(data);
			return resultData;
		}catch (Exception e) {
			e.printStackTrace();
			resultData = ResultData.build_fail_result(data, "异常", 10002);
			return resultData;
		}	
	}
	
	@RequestMapping("/searchPosts")
	@ResponseBody
	public ResultData searchPosts(
			@RequestParam("searchWord")String searchWord,
			@RequestParam(value = "category", required = false)String category,
			@RequestParam(value = "lastPostId", required = false, defaultValue="0")int lastPostId) {
		Map<String, Object> data = new HashMap<>();
		ResultData resultData = null;
		try {
			List<PostInfo> posts = postService.searchPosts(searchWord, category, lastPostId);
			data.put("postList", posts);
			resultData = ResultData.build_success_result(data);
			return resultData;
		}catch (Exception e) {
			e.printStackTrace();
			resultData = ResultData.build_fail_result(data, "异常", 10002);
			return resultData;
		}	
	}
}
