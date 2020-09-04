/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.ewallet.dao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import top.iotequ.framework.service.IDaoService;
import top.iotequ.ewallet.pojo.EwUser;
import java.util.*;

//  Mapper dao : EwUserDao (电子钱包数据访问接口)
public interface EwUserDao  extends IDaoService<EwUser> {
    int insert(EwUser record);
    int insertBatchWithId(List<EwUser> list);
    EwUser select(String userNo);
    int delete(String userNo);
    int deleteBatch(String userNos);
    EwUser selectByMobilePhone(String mobilePhone);
    int deleteByMobilePhone(String mobilePhone);
    int deleteBatchByMobilePhone(String mobilePhones);
    EwUser selectByEmail(String email);
    int deleteByEmail(String email);
    int deleteBatchByEmail(String emails);
    EwUser selectByWechatOpenid(String wechatOpenid);
    int deleteByWechatOpenid(String wechatOpenid);
    int deleteBatchByWechatOpenid(String wechatOpenids);
    int deleteList(List<EwUser> list);
    int update(EwUser record);
    int updateSelective(EwUser record);
    int updateBy(@Param("record")EwUser record,@Param("userNo")String id);
    //条件为所有非空字段 and，String采用like的查询模式，其他为=
    List<EwUser> list(EwUser record);
    //!!!  如 whereString="user_name like '王%'" ,orderString="born_date desc,code asc"
    List<EwUser> listBy(@Param("whereString")String whereString,@Param("orderString")String orderString);
    List<EwUser> query(@Param("sqlString")String sqlString);
    @Update("${sqlString}")
    int execute(@Param("sqlString") String sqlString);
}
