package HCHomeServer.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import HCHomeServer.model.db.Post;

public interface PostMapper {

	public void addPost(Post post);

	public void deletePostByPostId(int postId);

	public List<Post> getRecentPosts(@Param("category")String category, @Param("count")int count);

	public List<Post> getEarlierPosts(@Param("category")String category, @Param("basePostId")int basePostId, @Param("count")int count);

	public List<Post> getTopPosts();

	public void updateFloor(@Param("postId")int postId);

}
