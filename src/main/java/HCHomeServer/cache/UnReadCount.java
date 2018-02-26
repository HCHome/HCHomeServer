package HCHomeServer.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.concurrent.ConcurrentSkipListMap;

import com.alibaba.fastjson.JSONObject;

/**
 * 一个用户未读消息数的管理器，采用内部类方式实现的单例模式来保证线程安全和懒加载机制。
 * 在项目加载的时候会根据SAVEPATH加载数据，项目中止时，会将数据保存到SAVEPATH路径下。
 * @author cj
 */
public class UnReadCount implements Serializable{
	
	private static final long serialVersionUID = 6945883647400709800L;
	private static final String webRoot = System.getProperty("webRoot");
	private static final String SAVEDIRPATH =  webRoot.substring(0, webRoot.length()-21)+"webcache/HCHomeServer";
	private static final String SAVEPATH = SAVEDIRPATH + "/UnReadCount.txt";
	private UnReadCount() {
		unReadCountMap = new ConcurrentSkipListMap<>();
		this.loadMap();
		
	}
	public static UnReadCount getInstance() {
		return HolderOnUnReadCount.unReadCount;
	}
	//内部类
	private static class HolderOnUnReadCount{
		public static UnReadCount unReadCount = new UnReadCount();
	}
	
	private ConcurrentSkipListMap<String, Integer> unReadCountMap;
	
	public UnReadCount upUnRead(int userId) {
		if(unReadCountMap.containsKey(Integer.toString(userId))) {
			unReadCountMap.put(Integer.toString(userId), unReadCountMap.get(Integer.toString(userId))+1);
		}else {
			unReadCountMap.put(Integer.toString(userId), 1);
		}
		return this;
	}
	public Integer getUnRead(int userId) {
		if(unReadCountMap.containsKey(Integer.toString(userId))) {
			return unReadCountMap.get(Integer.toString(userId));
		}else {
			return 0;
		}
	}
	public Integer getAndRemoveUnRead(int userId) {
		Integer count = getUnRead(userId);
		if(count!=0) {
			unReadCountMap.remove(Integer.toString(userId));
		}
		return count;
	}
	/**
	 * 将用户未读消息情况存到本地
	 */
	public void saveMap() {
		if(unReadCountMap.isEmpty())return;
		System.out.println(SAVEPATH);
		PrintStream printStream = null;
		try {
			File file = new File(SAVEPATH);
			if(file.exists()&&file.isFile()&&file.canWrite()) {							
				printStream = new PrintStream(new FileOutputStream(file));
				printStream.println(JSONObject.toJSON(unReadCountMap));
				System.out.println("saveMap:"+JSONObject.toJSONString(unReadCountMap));
			}else {
				file.deleteOnExit();
				new File(SAVEDIRPATH).mkdirs();
				if(file.createNewFile()) {
					printStream = new PrintStream(new FileOutputStream(file));
					printStream.println(JSONObject.toJSONString(unReadCountMap));
					System.out.println("saveMap:"+JSONObject.toJSONString(unReadCountMap));
				}
			}
			unReadCountMap.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(printStream!=null)printStream.close();
		}
	}
	/**
	 * 将用户未读消息情况从本地加载
	 */
	@SuppressWarnings("unchecked")
	public void loadMap(){
		if(!unReadCountMap.isEmpty())return;
		System.out.println(SAVEPATH);
		FileInputStream  fileInputStream = null;
		try {
			File file = new File(SAVEPATH);
			if(file.exists()&&file.isFile()) {
				fileInputStream = new FileInputStream(SAVEPATH);
				byte[] fileString = new byte[(int)file.length()+1];
				fileInputStream.read(fileString);
				String mapJson = new String(fileString);
				unReadCountMap = JSONObject.parseObject(mapJson, unReadCountMap.getClass());
				System.out.println("loadMap:" + mapJson);
				fileInputStream.close();
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
}
