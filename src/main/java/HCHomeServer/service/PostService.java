package HCHomeServer.service;

import org.springframework.web.multipart.MultipartFile;

import HCHomeServer.model.db.Post;
import HCHomeServer.model.db.PostPicture;

public interface PostService {

	public void addPost(Post post);

	public PostPicture addPostPicture(MultipartFile postPictureEntity, int postId, int order) throws Exception;

	public void deletePost(int userId, int postId);

}
