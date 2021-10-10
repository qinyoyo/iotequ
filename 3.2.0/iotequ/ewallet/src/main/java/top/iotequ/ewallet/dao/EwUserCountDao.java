/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.ewallet.dao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import java.util.Map;
import top.iotequ.framework.service.IDaoService;
import top.iotequ.ewallet.pojo.EwUserCount;
import java.util.*;

//  Mapper dao : EwUserCountDao (计次钱包数据访问接口)
public interface EwUserCountDao  extends IDaoService<EwUserCount> {
    int insert(EwUserCount record);
    int insertBatchWithoutId(List<EwUserCount> list);
    int insertBatchWithId(List<EwUserCount> list);
    EwUserCount select(Integer id);
    int delete(Integer id);
    int deleteBatch(String ids);
    EwUserCount selectByUserNoCountId(@Param("userNo")String userNo , @Param("countId")Integer countId);
    int deleteByUserNoCountId(@Param("userNo")String userNo , @Param("countId")Integer countId);
    int deleteList(List<EwUserCount> list);
    int update(EwUserCount record);
    int updateSelective(Map<String,Object> record);
    int updateBy(@Param("record")EwUserCount record,@Param("id")Integer id);
    //条件为所有非空字段 and，String采用like的查询模式，其他为=
    List<EwUserCount> list(EwUserCount record);
    //!!!  如 whereString="db_table_name.user_name like '王%'" ,orderString="db_table_name.born_date desc,db_table_name.code asc"
    List<EwUserCount> listBy(@Param("whereString")String whereString,@Param("orderString")String orderString);
    List<EwUserCount> query(@Param("sqlString")String sqlString);
    @Update("${sqlString}")
    int execute(@Param("sqlString") String sqlString);
}
