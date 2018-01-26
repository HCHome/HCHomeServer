package HCHomeServer.cos;


import com.alibaba.fastjson.JSONObject;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.meta.InsertOnly;
import com.qcloud.cos.request.UploadFileRequest;
import com.qcloud.cos.sign.Credentials;

/**
 * 腾讯云Cos对象服务器接口封装类
 * 使用的是4.6老版SDK
 * @author cj
 */
public class CosTool {
	final private static long appId = 1253647932;
	final private static String secretId = "AKIDDG6kCQoIuJVTElfB3jCRVJr9JFxPOWBa";
	final private static String secretKey = "GH2XmlX6RXO1PYT9amkUzGI5ey3ua3II";
	final private static String bucketName = "hc";
	private static Credentials cred;
	private static ClientConfig clientConfig;
	static {
		cred = new Credentials(appId, secretId, secretKey);
		clientConfig = new ClientConfig();
		clientConfig.setRegion("gz");
	}
	/**
	 * 文件上传接口
	 * @param picture
	 * @param filePath
	 * @return
	 */
	public static boolean uploadPostPicture(byte[] picture, String filePath) {
		COSClient client = new COSClient(clientConfig, cred);
		UploadFileRequest request = new UploadFileRequest(bucketName, filePath, picture);
		request.setInsertOnly(InsertOnly.OVER_WRITE);
		String uploadFileRet = client.uploadFile(request);
		JSONObject res = JSONObject.parseObject(uploadFileRet);
		client.shutdown();
		if(res.getIntValue("code")==0) {

			return true;
		}else {
			System.out.println(uploadFileRet);
			
			return false;
		}
 	}
}
