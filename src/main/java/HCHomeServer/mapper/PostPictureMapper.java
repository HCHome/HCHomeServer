package HCHomeServer.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import HCHomeServer.model.db.PostPicture;

public interface PostPictureMapper {

	public void addPostPicture(PostPicture postPicture);

	public void deletePictureByPostId(@Param("postId")int postId);

	public List<String> getPictureUrlArrayByPostId(int postId);

}
