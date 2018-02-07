package HCHomeServer.model.result;

import java.io.Serializable;
import java.util.Date;

import HCHomeServer.model.db.PostReply;
import HCHomeServer.model.db.User;

/**
 * 返回给前端的一条帖子回复的相关信息的包装类
 * @author cj
 *
 */
public class ReplyInfo implements Serializable {
	
	private static final long serialVersionUID = 3590614070783600427L;
	//回复Id
	private int replyId;
	//帖子Id
	private int postId;
	//回复者Id
	private int replierId;
	//回复者头像
	private String replierAvatar;
	//回复者昵称
	private String replierNickname;
	//所回复的楼层数，若回复帖子本身，则为0
	private int repliedFloor;
	//回复内容
	private String text;
	//回复时间
	private Date createdDate;
	//楼层数
	private int floor;
	/**
	 * 构建一条回复信息
	 * @param postReply
	 * @param user
	 * @return
	 */
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

	
}
