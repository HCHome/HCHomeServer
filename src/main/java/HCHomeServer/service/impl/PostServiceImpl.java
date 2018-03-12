package HCHomeServer.service.impl;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import HCHomeServer.cos.CosTool;
import HCHomeServer.mapper.PostMapper;
import HCHomeServer.mapper.PostPictureMapper;
import HCHomeServer.mapper.PostReplyMapper;
import HCHomeServer.mapper.UserMapper;
import HCHomeServer.model.db.Post;
import HCHomeServer.model.db.PostPicture;
import HCHomeServer.model.db.PostReply;
import HCHomeServer.model.db.User;
import HCHomeServer.model.result.PostInfo;
import HCHomeServer.model.result.ReceivedReply;
import HCHomeServer.model.result.ReplyInfo;
import HCHomeServer.service.PostService;

@Service
public class PostServiceImpl implements PostService {
	
	@Autowired
	private PostMapper postMapper;
	@Autowired
	private PostPictureMapper postPictureMapper;
	@Autowired
	private PostReplyMapper postReplyMapper;
	@Autowired
	private UserMapper userMapper;
	
	private Logger logger = Logger.getLogger(getClass());
	
	@Override
	public void addPost(Post post) {
		postMapper.addPost(post);
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public PostPicture addPostPicture(MultipartFile postPictureEntity, int postId, int order) throws Exception {
		//生成文件名
		String originalFilename = postPictureEntity.getOriginalFilename();
		String[] subString = originalFilename.split("\\.");
		System.out.println(postPictureEntity.getOriginalFilename());
		String suffix = subString[subString.length-1];
		String fileName = "/post/"+Long.toString(postId)+"_"+String.valueOf(originalFilename.hashCode())+"."+suffix;
		//更新数据库
		PostPicture postPicture = PostPicture.creatPostPicture(CosTool.COS_BASE_URL+fileName,postId,order);
		postPictureMapper.addPostPicture(postPicture);
		//上传图片到对象服务器
		CosTool.uploadPostPicture(postPictureEntity.getBytes(), fileName);
		
		return postPicture;
	}
	
	@Override
	@Transactional
	public void deletePost(int userId, int postId) {
		//删除帖子的回复
		postReplyMapper.deletePostReplyByPostId(postId);
		//获取帖子图片的url并删除帖子在数据库的图片记录
		List<String> urlList = postPictureMapper.getPictureUrlArrayByPostId(postId);
		postPictureMapper.deletePictureByPostId(postId);
		//删除帖子记录
		postMapper.deletePostByPostId(postId);
		//删除帖子在对象服务器上的图片
		try {
			new Thread(new Runnable() {
				@Override
				public void run() {
					Iterator<String>iterator = urlList.iterator();
					while(iterator.hasNext()) {
						CosTool.deleteFoodPicture(iterator.next().replaceFirst(CosTool.COS_BASE_URL, ""));
					}
				}
			}).run();
		}catch (Exception e) {
			logger.info("remove picture exception");
		}
	}

	@Override
	public ArrayList<PostInfo> getPostListForAll(int lastPostId) {
		//获取帖子记录列表
		List<Post> posts = null;
		if(lastPostId <= 0) {
			posts = postMapper.getRecentPostsForAll(30);
		}else {
			posts = postMapper.getEarlierPostsForAll(lastPostId,30);
		}
		//抓取相关返回信息
		Iterator<Post> iterator = posts.iterator();
		ArrayList<PostInfo> postInfos = new ArrayList<>();
		while(iterator.hasNext()) {
			Post temp = iterator.next();
			User user = userMapper.getUserByUserId(temp.getUserId());
			int repliesCount = postReplyMapper.getRepliesCount(temp.getPostId());
			List<String> pictureUrls = postPictureMapper.getPictureUrlArrayByPostId(temp.getPostId());
			postInfos.add(PostInfo.build(temp, user, pictureUrls, repliesCount));
		}
		return postInfos;
	}

	@Override
	public ArrayList<PostInfo> getPostListForCategory(String category, int lastPostId) {
		//获取帖子记录列表
		List<Post> posts = null;
		if(lastPostId <=0) {
			posts = postMapper.getRecentPostsForCategory(category,30);
		}else {
			posts = postMapper.getEarlierPostsForCategory(category, lastPostId, 30);
		}
		//抓取相关返回信息
		Iterator<Post> iterator = posts.iterator();
		ArrayList<PostInfo> postInfos = new ArrayList<>();
		while(iterator.hasNext()) {
			Post temp = iterator.next();
			User user = userMapper.getUserByUserId(temp.getUserId());
			int repliesCount = postReplyMapper.getRepliesCount(temp.getPostId());
			List<String> pictureUrls = postPictureMapper.getPictureUrlArrayByPostId(temp.getPostId());
			postInfos.add(PostInfo.build(temp, user, pictureUrls, repliesCount));
		}
		return postInfos;
	}

	@Override
	public ArrayList<PostInfo> getTopPosts() {
		//获取帖子记录列表
		List<Post> posts = postMapper.getTopPosts();
		//抓取相关返回信息
		Iterator<Post> iterator = posts.iterator();
		ArrayList<PostInfo> postInfos = new ArrayList<>();
		while(iterator.hasNext()) {
			Post temp = iterator.next();
			User user = userMapper.getUserByUserId(temp.getUserId());
			int repliesCount = postReplyMapper.getRepliesCount(temp.getPostId());
			List<String> pictureUrls = postPictureMapper.getPictureUrlArrayByPostId(temp.getPostId());
			postInfos.add(PostInfo.build(temp, user, pictureUrls,repliesCount));
		}
		return postInfos;
	}

	@Override
	public List<ReplyInfo> getReplyListByPostId(int postId, int lastReplyId) {
		//获取回复列表
		List<PostReply> postReplies;
		if(lastReplyId <= 0) {
			postReplies= postReplyMapper.getEarliestRepliesByPostId(postId, 20);
		}else {
			postReplies = postReplyMapper.getLaterRepliesByPostId(postId, lastReplyId, 20);
		}
		//抓取相关回复列表
		Iterator<PostReply> iterator = postReplies.iterator();
		ArrayList<ReplyInfo> replyInfos = new ArrayList<>();
		while(iterator.hasNext()) {
			PostReply temp = iterator.next();
			
			User user = userMapper.getUserByUserId(temp.getReplierId());
			replyInfos.add(ReplyInfo.build(temp, user));
		}
		return replyInfos;
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public ReplyInfo addReply(PostReply postReply) throws Exception {
		//新增回复记录
		postReplyMapper.addReply(postReply);
		//重新抓取回复记录，避免楼层数为0
		postReply = postReplyMapper.getReplyByReplyId(postReply.getReplyId());
		//更新帖子楼层数和日期
		postMapper.updateReply(postReply.getPostId());
		return ReplyInfo.build(postReply, userMapper.getUserByUserId(postReply.getReplierId()));

	}

	@Override
	public void deleteReply(int replyId) {
		postReplyMapper.deletePostReplyByReplyId(replyId);
		
	}

	@Override
	public ArrayList<PostInfo> getUserPostList(int userId) {
		//获取帖子记录列表
		List<Post> posts = postMapper.getMyPostList(userId);
		//抓取相关返回信息
		Iterator<Post> iterator = posts.iterator();
		ArrayList<PostInfo> postInfos = new ArrayList<>();
		while(iterator.hasNext()) {
			Post temp = iterator.next();
			//帖子回复数
			int repliesCount = postReplyMapper.getRepliesCount(temp.getPostId());
			//帖子图片
			List<String> pictureUrls = postPictureMapper.getPictureUrlArrayByPostId(temp.getPostId());
			//帖子信息整理
			postInfos.add(PostInfo.build(temp, pictureUrls, repliesCount));
		}
		return postInfos;
	}

	@Override
	public PostInfo getPostInfoByPostId(int postId) {
		Post post = postMapper.getPostByPostId(postId);
		
		//帖子回复数
		int repliesCount = postReplyMapper.getRepliesCount(post.getPostId());
		//帖子图片
		List<String> pictureUrls = postPictureMapper.getPictureUrlArrayByPostId(post.getPostId());
		//贴主信息
		User user = userMapper.getUserByUserId(post.getUserId());
		//帖子信息整理
		return PostInfo.build(post, user, pictureUrls, repliesCount);
	}

	@Override
	public List<ReceivedReply> getReplyListByUserId(int userId, int lastReplyId) {
		List<PostReply> postReplies = null;
		if(lastReplyId==0) {
			postReplies = postReplyMapper.getUserRecentReceivedReplies(userId, 20);
		}else {
			postReplies = postReplyMapper.getEarlierReceivedReplies(userId, 20, lastReplyId);
		}
		List<ReceivedReply> receivedReplies = new ArrayList<>();
		Iterator<PostReply> iterator = postReplies.iterator();
		while(iterator.hasNext()) {
			PostReply temp = iterator.next();
			receivedReplies.add(ReceivedReply.buildFromReplyAndUser(temp, userMapper.getUserByUserId(userId)));
		}
		return receivedReplies;
	}

	@Override
	public List<PostInfo> searchPosts(String searchWord, String category, int lastPostId) {
		//获取帖子记录列表
		List<Post> posts = postMapper.searchPosts(searchWord, category, lastPostId);
		//抓取相关返回信息
		Iterator<Post> iterator = posts.iterator();
		ArrayList<PostInfo> postInfos = new ArrayList<>();
		while(iterator.hasNext()) {
			Post temp = iterator.next();
			User user = userMapper.getUserByUserId(temp.getUserId());
			int repliesCount = postReplyMapper.getRepliesCount(temp.getPostId());
			List<String> pictureUrls = postPictureMapper.getPictureUrlArrayByPostId(temp.getPostId());
			postInfos.add(PostInfo.build(temp, user, pictureUrls,repliesCount));
		}
		return postInfos;
	}


}
