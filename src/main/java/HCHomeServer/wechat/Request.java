package HCHomeServer.wechat;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;


public class Request {

	public static String get(String address, Map<String, String> params) {
		HttpURLConnection httpURLConnection = null;
		try {
				if(params!=null) {
					address = address+"?";
					Iterator<Entry<String, String>>iterator =params.entrySet().iterator();
					while(iterator.hasNext()) {
						Entry<String, String> entry = iterator.next();
						address = address + entry.getKey()+"=" + entry.getValue()+"&&";
					}
				}
	            URL url = new URL(address);
	            
	            httpURLConnection = (HttpURLConnection) url.openConnection();
	            httpURLConnection.setDoOutput(true);
	            httpURLConnection.setRequestMethod("GET");
//	            if(params!=null) {
//	            	Iterator<Entry<String, String>>iterator =params.entrySet().iterator();
//	            	while(iterator.hasNext()) {
//	            		Entry<String, String> entry = iterator.next();
//	            		httpURLConnection.addRequestProperty(entry.getKey(), entry.getValue());
//	            	}
//	            }
	            httpURLConnection.connect();
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
