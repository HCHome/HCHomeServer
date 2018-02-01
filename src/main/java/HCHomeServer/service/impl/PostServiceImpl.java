package HCHomeServer.service.impl;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
	@Override
	public void addPost(Post post) {
		postMapper.addPost(post);
	}

	@Override
	@Transactional
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
		new Thread(new Runnable() {
			@Override
			public void run() {
				Iterator<String>iterator = urlList.iterator();
				while(iterator.hasNext()) {
					CosTool.deleteFoodPicture(iterator.next().replaceFirst(CosTool.COS_BASE_URL, ""));
				}
			}
		}).run();
	}

	@Override
	@Transactional
	public ArrayList<PostInfo> getPostList(String category, int lastPostId) {
		//获取帖子记录列表
		List<Post> posts = null;
		if(lastPostId <=0) {
			posts = postMapper.getRecentPosts(category,30);
		}else {
			posts = postMapper.getEarlierPosts(category, lastPostId, 30);
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
	@Transactional
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
	@Transactional
	public List<ReplyInfo> getReplyListByPostId(int userId, int postId, int lastReplyId) {
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
	@Transactional
	public ReplyInfo addReply(PostReply postReply) {
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

}
