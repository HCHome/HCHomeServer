package HCHomeServer.service.impl;


import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import HCHomeServer.cos.CosTool;
import HCHomeServer.mapper.PostMapper;
import HCHomeServer.mapper.PostPictureMapper;
import HCHomeServer.model.db.Post;
import HCHomeServer.model.db.PostPicture;
import HCHomeServer.service.PostService;

@Service
public class PostServiceImpl implements PostService {
	
	@Autowired
	private PostMapper postMapper;
	@Autowired
	private PostPictureMapper postPictureMapper;
	@Override
	public void addPost(Post post) {
		postMapper.addPost(post);
	}

	@Override
	@Transactional
	public PostPicture addPostPicture(MultipartFile postPictureEntity, int postId, int order) throws Exception {
		//生成文件名
		String originalFilename = postPictureEntity.getOriginalFilename();
		String[] subString = originalFilename.split("\\.");
		System.out.println(postPictureEntity.getOriginalFilename());
		String suffix = subString[subString.length-1];
		String fileName = "/post/"+Long.toString(postId)+"_"+String.valueOf(originalFilename.hashCode())+"."+suffix;
		
		PostPicture postPicture = PostPicture.creatPostPicture(CosTool.COS_BASE_URL+fileName,postId,order);
		postPictureMapper.addPostPicture(postPicture);
		
		CosTool.uploadPostPicture(postPictureEntity.getBytes(), fileName);
		
		return postPicture;
	}
	/**
	 * 未完成，未删除回复, 未删除云端相片
	 */
	@Override
	public void deletePost(int userId, int postId) {
		//postReplyMapper.deletePostReplyByPostId(postId);
		List<String> urlList = postPictureMapper.getPictureUrlArrayByPostId(postId);
		postPictureMapper.deletePictureByPostId(postId);
		postMapper.deletePostByPostId(postId);
		new Thread(new Runnable() {
			@Override
			public void run() {
				Iterator<String>iterator = urlList.iterator();
				while(iterator.hasNext()) {
					CosTool.deleteFoodPicture(iterator.next().replaceFirst(CosTool.COS_BASE_URL, ""));
				}
			}
		}).run();
	}

}
