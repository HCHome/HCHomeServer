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
		
		PostPicture postPicture = PostPicture.creatPostPicture(CosTool.COS_BASE_URL+fileName,postId,order);
		postPictureMapper.addPostPicture(postPicture);
		
		CosTool.uploadPostPicture(postPictureEntity.getBytes(), fileName);
		
		return postPicture;
	}
	
	@Override
	@Transactional
	public void deletePost(int userId, int postId) {
		postReplyMapper.deletePostReplyByPostId(postId);
		List<String> urlList = postPictureMapper.getPictureUrlArrayByPostId(postId);
		postPictureMapper.deletePictureByPostId(postId);
		postMapper.deletePostByPostId(postId);
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
		List<Post> posts = null;
		if(lastPostId <=0) {
			posts = postMapper.getRecentPosts(category,30);
		}else {
			posts = postMapper.getEarlierPosts(category, lastPostId, 30);
		}
		
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
		List<Post> posts = postMapper.getTopPosts();
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
		List<PostReply> postReplies;
		if(lastReplyId <= 0) {
			postReplies= postReplyMapper.getEarliestRepliesByPostId(postId, 20);
		}else {
			postReplies = postReplyMapper.getLaterRepliesByPostId(postId, lastReplyId, 20);
		}
		
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
		postReplyMapper.addReply(postReply);
		postReply = postReplyMapper.getReplyByReplyId(postReply.getReplyId());
		postMapper.updateFloor(postReply.getPostId());
		return ReplyInfo.build(postReply, userMapper.getUserByUserId(postReply.getReplierId()));
	}

	@Override
	public void deleteReply(int replyId) {
		postReplyMapper.deletePostReplyByReplyId(replyId);
		
	}

}
