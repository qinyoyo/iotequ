/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.ewallet.dao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import top.iotequ.framework.service.IDaoService;
import top.iotequ.ewallet.pojo.EwUserTime;
import java.util.*;

//  Mapper dao : EwUserTimeDao (计时钱包数据访问接口)
public interface EwUserTimeDao  extends IDaoService<EwUserTime> {
    int insert(EwUserTime record);
    int insertBatchWithoutId(List<EwUserTime> list);
    int insertBatchWithId(List<EwUserTime> list);
    EwUserTime select(Integer id);
    int delete(Integer id);
    int deleteBatch(String ids);
    EwUserTime selectByUserNoTimeId(@Param("userNo")String userNo , @Param("timeId")Integer timeId);
    int deleteByUserNoTimeId(@Param("userNo")String userNo , @Param("timeId")Integer timeId);
    int deleteList(List<EwUserTime> list);
    int update(EwUserTime record);
    int updateSelective(EwUserTime record);
    int updateBy(@Param("record")EwUserTime record,@Param("id")Integer id);
    //条件为所有非空字段 and，String采用like的查询模式，其他为=
    List<EwUserTime> list(EwUserTime record);
    //!!!  如 whereString="db_table_name.user_name like '王%'" ,orderString="db_table_name.born_date desc,db_table_name.code asc"
    List<EwUserTime> listBy(@Param("whereString")String whereString,@Param("orderString")String orderString);
    List<EwUserTime> query(@Param("sqlString")String sqlString);
    @Update("${sqlString}")
    int execute(@Param("sqlString") String sqlString);
}
