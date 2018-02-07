package HCHomeServer.mapper;

import org.apache.ibatis.annotations.Param;

import HCHomeServer.model.db.UserApply;
import HCHomeServer.model.result.LightUserApply;

/**
 * 账户申请信息数据库映射管理
 * @author cj
 */
public interface UserApplyMapper {

	public void addUserApply(UserApply userApply);

	public LightUserApply checkApply(@Param("openId")String openId);

}
