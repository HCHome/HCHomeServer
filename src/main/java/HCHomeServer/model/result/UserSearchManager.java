package HCHomeServer.model.result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import HCHomeServer.model.db.User;

public class UserSearchManager {
	
	private List<User> nicknameResult;
	private List<User> schoolResult;
	private List<User> professionResult;
	private List<User> jobResult;
	
	public UserSearchManager() {
		super();
		this.nicknameResult = new ArrayList<>();
		this.schoolResult = new ArrayList<>();
		this.professionResult = new ArrayList<>();
		this.jobResult = new ArrayList<>();
	}
	public void addResultByUser(User user, MatchType matchType) {
		switch (matchType) {
		case nickname:
			nicknameResult.add(user);
			break;
		case school:
			schoolResult.add(user);
			break;
		case profession:
			professionResult.add(user);
			break;
		case job:
			jobResult.add(user);
		}
	}
	public void addResultsByUsers(List<User> users, MatchType matchType) {
		//不是用List.addAll是为每个元素做处理做预留空间
		if(users == null)return;
		Iterator<User> iterator = users.iterator();
		while(iterator.hasNext()) {
			this.addResultByUser(iterator.next(), matchType);
		}
	}
	public Map<String, Object> convertToMap(){
		Map<String, Object> result = new HashMap<>();
		result.put("nicknameResult", nicknameResult);
		result.put("schoolResult", schoolResult);
		result.put("professionResult", professionResult);
		result.put("jobResult", jobResult);
		return result;
	}
	public static String searchWordPreHandle(String searchWord) {
		return searchWord.replaceAll("[^0-9a-zA-Z\u4e00-\u9fa5]", "");
	}
	

	public static enum MatchType {
		nickname("nickname"), school("school"), profession("profession"), job("job");
		private final String value;
		private MatchType(String value) {
			this.value = value;
		}
		public String getValue() {
			return this.value;
		}
		public static MatchType create(String value) {
			if(value == null)return null;
			for(MatchType matchType : values()) {
				if(matchType.getValue().equals(value)) {
					return matchType;
				}
			}
			return null;
		}
	}
}
