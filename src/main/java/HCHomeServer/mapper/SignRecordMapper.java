package HCHomeServer.mapper;

import org.apache.ibatis.annotations.Param;

import HCHomeServer.model.db.SignRecord;

/**
 * 签到实体映射管理
 * @author cj
 */
public interface SignRecordMapper {

	public SignRecord getTodaySignRecord(@Param("userId")int userId);

	public void addSign(SignRecord creatSign);

}
