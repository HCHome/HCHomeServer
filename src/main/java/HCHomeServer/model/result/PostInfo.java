package HCHomeServer.model.result;

import java.io.Serializable;
import java.util.Date;

import java.util.List;

import HCHomeServer.model.db.Post;
import HCHomeServer.model.db.User;

/**
 * 返回给前端的帖子的详细信息的打包类
 * 不包括回复信息
 * @author cj
 *
 */
public class PostInfo implements Serializable {
	
	private static final long serialVersionUID = 7105849959115994467L;
	
	//帖子Id
	private int postId;
	//帖子发布者Id
	private int posterId;
	//发布者头像地址，获取我的帖子列表接口不会返回
	private String posterAvatar;
	//发布者昵称，获取我的帖子列表接口不会返回
	private String posterNickname;
	//帖子分类
	private String category;
	//帖子标题
	private String title;
	//帖子正文
	private String text;
	//帖子图片数量
	private int pictureCount;
	//帖子回复数
	private int repliesCount;
	//帖子创建日期
	private Date createdDate;
	//帖子最新动态日期
	private Date updatedDate;
	//帖子是否置顶，只在获取我的帖子列表接口返回
	private Integer is_top;
	//帖子图片地址列表
	private List<String> pictureUrl;
	/**
	 * 构建一个帖子信息，基本用于一些用户无关的获取帖子列表的接口
	 * @param post
	 * @param user
	 * @param pictureUrls
	 * @param repliesCount
	 * @return
	 */
	public static PostInfo build(Post post, User user, List<String> pictureUrls, int repliesCount) {
		PostInfo postInfo = new PostInfo();
		postInfo.setPostId(post.getPostId());
		postInfo.setPosterId(post.getUserId());
		postInfo.setPosterAvatar(user.getAvatar());
		postInfo.setPosterNickname(user.getNickname());
		postInfo.setCategory(post.getCategory());
		postInfo.setTitle(post.getTitle());
		postInfo.setText(post.getText());
		postInfo.setCreatedDate(post.getCreatedDate());
		postInfo.setPictureCount(post.getPictureCount());
		postInfo.setRepliesCount(repliesCount);
		postInfo.setUpdatedDate(post.getLastUpdate());
		postInfo.setPictureUrl(pictureUrls);
		postInfo.setIs_top(null);
		return postInfo;
	}
	/**
	 * 构建一个帖子信息，基本用于一些用户相关的获取帖子列表的接口
	 * 如获取我发布的帖子列表
	 * @param post
	 * @param pictureUrls
	 * @param repliesCount
	 * @return
	 */
	public static PostInfo build(Post post, List<String> pictureUrls, int repliesCount) {
		PostInfo postInfo = new PostInfo();
		postInfo.setPostId(post.getPostId());
		postInfo.setPosterId(post.getUserId());
		postInfo.setPosterAvatar(null);
		postInfo.setPosterNickname(null);
		postInfo.setCategory(post.getCategory());
		postInfo.setTitle(post.getTitle());
		postInfo.setText(post.getText());
		postInfo.setCreatedDate(post.getCreatedDate());
		postInfo.setPictureCount(post.getPictureCount());
		postInfo.setRepliesCount(repliesCount);
		postInfo.setUpdatedDate(post.getLastUpdate());
		postInfo.setPictureUrl(pictureUrls);
		postInfo.setIs_top(post.getIsTop());
		return postInfo;
	}
	//属性设置与获取
	public int getPostId() {
		return postId;
	}
	public void setPostId(int postId) {
		this.postId = postId;
	}
	public int getPosterId() {
		return posterId;
	}
	public void setPosterId(int posterId) {
		this.posterId = posterId;
	}
	public String getPosterAvatar() {
		return posterAvatar;
	}
	public void setPosterAvatar(String posterAvatar) {
		this.posterAvatar = posterAvatar;
	}
	public String getPosterNickname() {
		return posterNickname;
	}
	public void setPosterNickname(String posterNickname) {
		this.posterNickname = posterNickname;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getPictureCount() {
		return pictureCount;
	}
	public void setPictureCount(int pictureCount) {
		this.pictureCount = pictureCount;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	public List<String> getPictureUrl() {
		return pictureUrl;
	}
	public void setPictureUrl(List<String> pictureUrl) {
		this.pictureUrl = pictureUrl;
	}
	
	public int getRepliesCount() {
		return repliesCount;
	}
	public void setRepliesCount(int repliesCount) {
		this.repliesCount = repliesCount;
	}
	public Integer getIs_top() {
		return is_top;
	}
	public void setIs_top(Integer is_top) {
		this.is_top = is_top;
	}
}
