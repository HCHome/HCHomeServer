package HCHomeServer.model.db;

import java.util.Date;

public class PostReply {
	private int replyId;
	private int postId;
	private int replierId;
	private int repliedFloor;
	private String text;
	private Date createdDate;
	private int floor;
	public int getReplyId() {
		return replyId;
	}
	public void setReplyId(int replyId) {
		this.replyId = replyId;
	}
	public int getPostId() {
		return postId;
	}
	public void setPostId(int postId) {
		this.postId = postId;
	}
	public int getRepliedFloor() {
		return repliedFloor;
	}
	public void setRepliedFloor(int repliedFloor) {
		this.repliedFloor = repliedFloor;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public int getFloor() {
		return floor;
	}
	public void setFloor(int floor) {
		this.floor = floor;
	}
	public int getReplierId() {
		return replierId;
	}
	public void setReplierId(int replierId) {
		this.replierId = replierId;
	}
	public static PostReply create(int postId, int replierId, int repliedFloor, String text) {
		PostReply postReply = new PostReply();
		postReply.setPostId(postId);
		postReply.setReplierId(replierId);
		postReply.setRepliedFloor(repliedFloor);
		postReply.setText(text);
		postReply.setCreatedDate(new Date());
		return postReply;
	}
}
