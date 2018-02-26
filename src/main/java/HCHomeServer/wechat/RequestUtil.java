package HCHomeServer.wechat;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * wechat包下的工具类，用于根据http协议发起请求获取数据
 * @author cj
 *
 */
class RequestUtil {

	public static String get(String utlStr, Map<String, String> params) {
		HttpURLConnection httpURLConnection = null;
		try {
				//get请求完整地址拼凑
				if(params!=null) {
					utlStr = utlStr+"?";
					Iterator<Entry<String, String>>iterator =params.entrySet().iterator();
					while(iterator.hasNext()) {
						Entry<String, String> entry = iterator.next();
						utlStr = utlStr + entry.getKey()+"=" + entry.getValue()+"&&";
					}
				}
	            URL url = new URL(utlStr);
	            //连接
	            httpURLConnection = (HttpURLConnection) url.openConnection();
	            httpURLConnection.setDoOutput(true);
	            httpURLConnection.setRequestMethod("GET");
	            httpURLConnection.connect();
	            //读取数据
	            if(httpURLConnection.getResponseCode()==HttpURLConnection.HTTP_OK) {
		            InputStream inputStream = httpURLConnection.getInputStream();
		            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		            String temp = new String("");
		            while (bufferedReader.ready()) {
		            	String x = bufferedReader.readLine();
		            	System.out.println(x);
		                temp = temp.concat(x);
		            }
		            return temp;
	            }else {
	            	return null;
	            }
	        } catch (Exception e) {
	        	e.printStackTrace();
	        	return null;
	        } finally {
	            if (httpURLConnection != null)
	                httpURLConnection.disconnect();
	        }
	}
	
}
