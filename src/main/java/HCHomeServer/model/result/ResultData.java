package HCHomeServer.model.result;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * 返回前端的数据格式包装类，便于构造返回信息
 * @author cj
 */
public class ResultData implements Serializable{

	private static final long serialVersionUID = -796476823964403482L;
	private Map<String, Object> data;
	private String msg;
	private Integer status;
	private Date date;
	private ResultData() {
		
	}
	public static ResultData build_success_result(Map<String, Object> data) {
		ResultData resultData = new ResultData();
		resultData.setData(data);
		resultData.setMsg("成功");
		resultData.setStatus(10001);
		resultData.setDate(new Date());
		return resultData;
	}
	public static ResultData build_fail_result(Map<String, Object>data, String msg, Integer status) {
		ResultData resultData = new ResultData();
		resultData.setData(data);
		resultData.setMsg(msg);
		resultData.setStatus(status);
		resultData.setDate(new Date());
		return resultData;
	}
	public static ResultData bulid_blank_result() {
		ResultData resultData = new ResultData();
		resultData.setData(new HashMap<String, Object>());
		resultData.setDate(new Date());
		return resultData;
	}
	public ResultData addData(String key, Object value) {
		this.data.put(key, value);
		return this;
	}
	public Object getDataItem(String key) {
		return this.data.get(key);
	}
	public Object removeDataItem(String key) {
		return this.data.remove(key);
	}
	public Map<String, Object> getData() {
		return data;
	}
	public void setData(Map<String, Object> data) {
		this.data = data;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
}
