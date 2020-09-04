/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.attendance.data.dao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import top.iotequ.framework.service.IDaoService;
import top.iotequ.attendance.data.pojo.AdData;
import java.util.*;

//  Mapper dao : AdDataDao (考勤数据数据访问接口)
public interface AdDataDao  extends IDaoService<AdData> {
    int insert(AdData record);
    int insertBatchWithoutId(List<AdData> list);
    int insertBatchWithId(List<AdData> list);
    AdData select(String id);
    int delete(String id);
    int deleteBatch(String ids);
    List<AdData> selectByEmployeeNo(String employeeNo);
    int deleteByEmployeeNo(String employeeNo);
    int deleteBatchByEmployeeNo(String employeeNos);
    int deleteList(List<AdData> list);
    int update(AdData record);
    int updateSelective(AdData record);
    int updateBy(@Param("record")AdData record,@Param("id")String id);
    //条件为所有非空字段 and，String采用like的查询模式，其他为=
    List<AdData> list(AdData record);
    //!!!  如 whereString="db_table_name.user_name like '王%'" ,orderString="db_table_name.born_date desc,db_table_name.code asc"
    List<AdData> listBy(@Param("whereString")String whereString,@Param("orderString")String orderString);
    List<AdData> query(@Param("sqlString")String sqlString);
    @Update("${sqlString}")
    int execute(@Param("sqlString") String sqlString);
}
