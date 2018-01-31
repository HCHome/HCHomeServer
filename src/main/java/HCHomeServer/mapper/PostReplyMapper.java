package HCHomeServer.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import HCHomeServer.model.db.PostReply;

public interface PostReplyMapper {

	public void deletePostReplyByPostId(@Param("postId")int postId);

	public List<PostReply> getEarliestRepliesByPostId(@Param("postId")int postId, @Param("count")int count);

	public List<PostReply> getLaterRepliesByPostId(@Param("postId")int postId, @Param("lastReplyId")int lastReplyId, @Param("count")int count);

	public int getRepliesCount(@Param("postId")int postId);

	public void addReply(PostReply postReply);

	public void deletePostReplyByReplyId(@Param("replyId")int replyId);

	public PostReply getReplyByReplyId(@Param("replyId")int replyId);
	
}
