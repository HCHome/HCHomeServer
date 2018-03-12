package HCHomeServer.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import HCHomeServer.model.db.Post;
import HCHomeServer.model.db.PostPicture;
import HCHomeServer.model.db.PostReply;
import HCHomeServer.model.result.PostInfo;
import HCHomeServer.model.result.ReceivedReply;
import HCHomeServer.model.result.ReplyInfo;

public interface PostService {
	//新增帖子
	public void addPost(Post post);
	//上传帖子的图片
	public PostPicture addPostPicture(MultipartFile postPictureEntity, int postId, int order) throws Exception;
	//删除帖子
	public void deletePost(int userId, int postId);
	//获取一个分类的帖子列表，最近更新的优先返回
	public ArrayList<PostInfo> getPostListForCategory(String category, int lastPostId);
	//获取总的帖子列表，最近更新的优先返回
	public ArrayList<PostInfo> getPostListForAll(int lastPostId);
	//获取置顶的帖子列表
	public ArrayList<PostInfo> getTopPosts();
	//新增回复
	public ReplyInfo addReply(PostReply create)throws Exception;
	//删除回复
	public void deleteReply(int replyId);
	//获取一个帖子的回复信息
	List<ReplyInfo> getReplyListByPostId(int postId, int lastReplyId);
	//获取一个用户发布的帖子列表
	public ArrayList<PostInfo> getUserPostList(int userId);
	//获取一个帖子的详细信息，不包含回复
	public PostInfo getPostInfoByPostId(int postId);
	//获取用户收到的回复列表
	public List<ReceivedReply> getReplyListByUserId(int userId, int lastReplyId);
	//在帖子标题或者正文中搜索帖子
	public List<PostInfo> searchPosts(String searchWord, String category, int lastPostId);

}
