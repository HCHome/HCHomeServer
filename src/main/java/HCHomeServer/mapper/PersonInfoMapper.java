package HCHomeServer.mapper;

import org.apache.ibatis.annotations.Param;

import HCHomeServer.model.db.PersonInfo;

/**
 * 潮友个人信息数据库orm映射接口管理
 * @author cj
 */
public interface PersonInfoMapper {

	PersonInfo checkCode(@Param("verificationCode")String verificationCode);

}
