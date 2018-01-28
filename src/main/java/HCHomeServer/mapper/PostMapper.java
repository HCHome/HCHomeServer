package HCHomeServer.mapper;

import HCHomeServer.model.db.Post;

public interface PostMapper {

	public void addPost(Post post);

	public void deletePostByPostId(int postId);

}
