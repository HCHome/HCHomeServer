package HCHomeServer.model.db;

import java.util.Date;

public class Post {
	private int postId;
	private int userId;
	private String category;
	private String title;
	private String text;
	private int isTop;
	private int pictureCount;
	private Date createdDate;
	private Date lastUpdate;
	private int currentFloor;
	public int getPostId() {
		return postId;
	}
	public void setPostId(int postId) {
		this.postId = postId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
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
	public int getIsTop() {
		return isTop;
	}
	public void setIsTop(int isTop) {
		this.isTop = isTop;
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
	public Date getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	public static Post creatPost(String category, String title, String text, int pictureCount, int userId) {
		Post post = new Post();
		post.setCategory(category);
		post.setCreatedDate(new Date());
		post.setIsTop(0);
		post.setLastUpdate(new Date());
		post.setPictureCount(pictureCount);
		post.setText(text);
		post.setTitle(title);
		post.setUserId(userId);
		post.setCurrentFloor(0);
		return post;
	}
	public int getCurrentFloor() {
		return currentFloor;
	}
	public void setCurrentFloor(int currentFloor) {
		this.currentFloor = currentFloor;
	}
	
}
