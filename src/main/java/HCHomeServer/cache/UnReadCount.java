package HCHomeServer.cache;

import java.util.concurrent.ConcurrentSkipListMap;
/**
 * 一个用户未读消息数的管理器，采用内部类方式实现的单例模式来保证线程安全和懒加载机制
 * @author cj
 */
public class UnReadCount {
	private UnReadCount() {
		unReadCountMap = new ConcurrentSkipListMap<>();
	}
	public static UnReadCount getInstance() {
		return UnReadCountHolder.unReadCount;
	}
	//内部类
	private static class UnReadCountHolder{
		public static UnReadCount unReadCount = new UnReadCount();
	}
	
	private ConcurrentSkipListMap<Integer, Integer> unReadCountMap;
	public UnReadCount upUnRead(int userId) {
		if(unReadCountMap.containsKey(userId)) {
			unReadCountMap.put(userId, unReadCountMap.get(userId)+1);
		}else {
			unReadCountMap.put(userId, 1);
		}
		return this;
	}
	public Integer getUnRead(int userId) {
		if(unReadCountMap.containsKey(userId)) {
			return unReadCountMap.get(userId);
		}else {
			return 0;
		}
	}
	public Integer getAndRemoveUnRead(int userId) {
		Integer count = getUnRead(userId);
		if(count!=0) {
			unReadCountMap.remove(userId);
		}
		return count;
	}
}
