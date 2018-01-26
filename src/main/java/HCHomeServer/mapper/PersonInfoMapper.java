package HCHomeServer.mapper;

import org.apache.ibatis.annotations.Param;

import HCHomeServer.model.db.PersonInfo;

public interface PersonInfoMapper {

	PersonInfo checkCode(@Param("verificationCode")String verificationCode);

}
