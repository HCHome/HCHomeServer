package HCHomeServer.mapper;

import org.apache.ibatis.annotations.Param;

import HCHomeServer.model.db.SignRecord;

public interface SignRecordMapper {

	public SignRecord getTodaySignRecord(@Param("userId")int userId);

	public void addSign(SignRecord creatSign);

}
