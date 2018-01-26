package HCHomeServer.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.mchange.v2.c3p0.impl.NewProxyCallableStatement;

import HCHomeServer.cos.CosTool;
import HCHomeServer.model.result.ResultData;

@Controller
@RequestMapping(value="/post")
public class PostController {
	
	@RequestMapping("newPost")
	@ResponseBody
	public ResultData newPost(
			@RequestParam("category")int category,
			@RequestParam("title")String title,
			@RequestParam("text")String text,
			@RequestParam("pictureCount")int pictureCount,
			@RequestParam("userId")int userId) {
		
				return null;
		
	}
	
	
	
	@RequestMapping("/uploadPostPicture")
	@ResponseBody
	public ResultData uploadPostPicture(@RequestParam("postPicture")MultipartFile postPicture) throws Exception {
		CosTool.uploadPostPicture(postPicture.getBytes(), "/post/hhh.jpg");
		
		return ResultData.build_success_result(null);
		
	}
}
