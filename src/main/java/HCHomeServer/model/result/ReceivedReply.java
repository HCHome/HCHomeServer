package HCHomeServer.model.result;

import HCHomeServer.model.db.PostReply;
import HCHomeServer.model.db.User;

public class ReceivedReply {
	private int replyId;
	private String text;
	private int postId;
	private int replierId;
	private String replierAvatar;
	private String replierNickname;
	private int floor;
	public int getReplyId() {
		return replyId;
	}
	public void setReplyId(int replyId) {
		this.replyId = replyId;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
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
	public int getFloor() {
		return floor;
	}
	public void setFloor(int floor) {
		this.floor = floor;
	}
	public static ReceivedReply buildFromReplyAndUser(PostReply postReply, User user) {
		ReceivedReply receivedReply = new ReceivedReply();
		receivedReply.replyId = postReply.getReplyId();
		receivedReply.text = postReply.getText();
		receivedReply.postId = postReply.getPostId();
		receivedReply.replierId = postReply.getReplierId();
		receivedReply.replierAvatar = user.getAvatar();
		receivedReply.replierNickname = user.getNickname();
		receivedReply.floor = postReply.getFloor();
		return receivedReply;
	}
	
}
