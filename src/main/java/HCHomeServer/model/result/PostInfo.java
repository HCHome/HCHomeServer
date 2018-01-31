package HCHomeServer.model.result;

import java.util.Date;

import java.util.List;

import HCHomeServer.model.db.Post;
import HCHomeServer.model.db.User;

public class PostInfo {
	private int postId;
	private int posterId;
	private String posterAvatar;
	private String posterNickname;
	private String category;
	private String title;
	private String text;
	private int pictureCount;
	private int repliesCount;
	private Date createdDate;
	private Date updatedDate;
	private List<String> pictureUrl;
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
		postInfo.setPictureCount(repliesCount);
		postInfo.setUpdatedDate(post.getLastUpdate());
		postInfo.setPictureUrl(pictureUrls);
		return postInfo;
	}
	public int getRepliesCount() {
		return repliesCount;
	}
	public void setRepliesCount(int repliesCount) {
		this.repliesCount = repliesCount;
	}
}
