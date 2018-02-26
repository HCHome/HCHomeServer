package HCHomeServer.cache;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.fastjson.JSONObject;

import HCHomeServer.wechat.Session;
import HCHomeServer.cache.TimeCacheMap;
/**
 * 对于微信会话的缓存类
 * @author cj
 *
 */
public class SessionCache {
	
	public final static SessionCache getInstance() {
		return HolderOnSessionCache.sessionCache;
	}
	/**
	 * 内部静态类，为了实现线程安全的单例类
	 * @author cj
	 *
	 */
	private static class HolderOnSessionCache{
		public static SessionCache sessionCache = new SessionCache();
	}
	
	private SessionCache() {
		loginedSessionCache = new ConcurrentHashMap<>();
		tempSessionCache = new TimeCacheMap<String, Session>(30*60);
	}
	/**
	 * 
	 * 枚举，TEMP表示小程序临时用户，LOGINED表示小程序已登录用户
	 * @author cj
	 *
	 */
	public static enum SessionType{
		TEMP, LOGINED
	}
	
	private Map<String, Session> loginedSessionCache ;
	private Map<String, Session> tempSessionCache;
	
	private final Map<String, Session> getCache(SessionType type){
		switch (type) {
			case TEMP:
				return tempSessionCache;
			case LOGINED:
				return loginedSessionCache;
			default:
				throw new NoSuchElementException();
		}
	}
	
	public void put(String key, Session value, SessionType type) {
		getCache(type).put(key, value);
		System.out.println("put:"+JSONObject.toJSONString(getCache(type)));
	}
	
	public Session get(String key, SessionType type) {
		return getCache(type).get(key);
	}
	
	public Session remove(String key, SessionType type) {
		System.out.println("remove:"+JSONObject.toJSONString(getCache(type)));
		return getCache(type).remove(key);
	}
	
	public boolean containsKey(String key, SessionType type) {
		return getCache(type).containsKey(key);
	}
}
