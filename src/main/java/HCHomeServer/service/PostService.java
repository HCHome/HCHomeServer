package HCHomeServer.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import HCHomeServer.model.db.Post;
import HCHomeServer.model.db.PostPicture;
import HCHomeServer.model.db.PostReply;
import HCHomeServer.model.result.PostInfo;
import HCHomeServer.model.result.ReplyInfo;

public interface PostService {

	public void addPost(Post post);

	public PostPicture addPostPicture(MultipartFile postPictureEntity, int postId, int order) throws Exception;

	public void deletePost(int userId, int postId);

	public ArrayList<PostInfo> getPostListForCategory(String category, int lastPostId);
	
	public ArrayList<PostInfo> getPostListForAll(int lastPostId);

	public ArrayList<PostInfo> getTopPosts();


	public ReplyInfo addReply(PostReply create);

	public void deleteReply(int replyId);

	List<ReplyInfo> getReplyListByPostId(int postId, int lastReplyId);

	public ArrayList<PostInfo> getMyPostList(int userId);

	

}
