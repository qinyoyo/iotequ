/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.reader.dao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import top.iotequ.framework.service.IDaoService;
import top.iotequ.reader.pojo.DevPeople;
import java.util.*;

//  Mapper dao : DevPeopleDao (注册人员数据访问接口)
public interface DevPeopleDao  extends IDaoService<DevPeople> {
    int insert(DevPeople record);
    int insertBatchWithId(List<DevPeople> list);
    DevPeople select(String userNo);
    int delete(String userNo);
    int deleteBatch(String userNos);
    DevPeople selectByCardNo(String cardNo);
    int deleteByCardNo(String cardNo);
    int deleteBatchByCardNo(String cardNos);
    DevPeople selectByMobilePhone(String mobilePhone);
    int deleteByMobilePhone(String mobilePhone);
    int deleteBatchByMobilePhone(String mobilePhones);
    DevPeople selectByEmail(String email);
    int deleteByEmail(String email);
    int deleteBatchByEmail(String emails);
    DevPeople selectByIdTypeIdNumber(@Param("idType")Integer idType , @Param("idNumber")String idNumber);
    int deleteByIdTypeIdNumber(@Param("idType")Integer idType , @Param("idNumber")String idNumber);
    int deleteList(List<DevPeople> list);
    int update(DevPeople record);
    int updateSelective(DevPeople record);
    int updateBy(@Param("record")DevPeople record,@Param("userNo")String id);
    //条件为所有非空字段 and，String采用like的查询模式，其他为=
    List<DevPeople> list(DevPeople record);
    //!!!  如 whereString="user_name like '王%'" ,orderString="born_date desc,employee_no asc"
    List<DevPeople> listBy(@Param("whereString")String whereString,@Param("orderString")String orderString);
    List<DevPeople> query(@Param("sqlString")String sqlString);
    @Update("${sqlString}")
    int execute(@Param("sqlString") String sqlString);
}
