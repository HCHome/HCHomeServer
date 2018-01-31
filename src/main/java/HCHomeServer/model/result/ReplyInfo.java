package HCHomeServer.model.result;

import java.util.Date;

import HCHomeServer.model.db.PostReply;
import HCHomeServer.model.db.User;

public class ReplyInfo {
	private int replyId;
	private int postId;
	private int replierId;
	private String replierAvatar;
	private String replierNickname;
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
	public int getReplierId() {
		return replierId;
	}
	public void setReplierId(int replierId) {
		this.replierId = replierId;
	}
	public String getReplierAvatar() {
		return replierAvatar;
	}
	public void setReplierAvatar(String replierAvatar) {
		this.replierAvatar = replierAvatar;
	}
	public String getReplierNickname() {
		return replierNickname;
	}
	public void setReplierNickname(String replierNickname) {
		this.replierNickname = replierNickname;
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
	public static ReplyInfo build(PostReply postReply, User user) {
		ReplyInfo replyInfo = new ReplyInfo();
		replyInfo.setReplyId(postReply.getReplyId());
		replyInfo.setPostId(postReply.getPostId());
		replyInfo.setReplierId(postReply.getReplierId());
		replyInfo.setReplierAvatar(user.getAvatar());
		replyInfo.setReplierNickname(user.getNickname());
		replyInfo.setRepliedFloor(postReply.getRepliedFloor());
		replyInfo.setText(postReply.getText());
		replyInfo.setCreatedDate(postReply.getCreatedDate());
		replyInfo.setFloor(postReply.getFloor());
		return replyInfo;
	}
	
}
