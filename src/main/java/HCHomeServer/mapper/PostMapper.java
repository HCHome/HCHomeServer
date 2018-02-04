package HCHomeServer.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import HCHomeServer.model.db.Post;

public interface PostMapper {

	public void addPost(Post post);

	public void deletePostByPostId(int postId);

	public List<Post> getRecentPostsForCategory(@Param("category")String category, @Param("count")int count);

	public List<Post> getEarlierPostsForCategory(@Param("category")String category, @Param("basePostId")int basePostId, @Param("count")int count);

	public List<Post> getRecentPostsForAll(@Param("count")int count);

	public List<Post> getEarlierPostsForAll(@Param("basePostId")int basePostId, @Param("count")int count);

	public List<Post> getTopPosts();

	public void updateReply(@Param("postId")int postId);

}
