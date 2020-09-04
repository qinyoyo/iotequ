/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.attendance.employee.dao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import top.iotequ.framework.service.IDaoService;
import top.iotequ.attendance.employee.pojo.AdEmployee;
import java.util.*;

//  Mapper dao : AdEmployeeDao (考勤职员表数据访问接口)
public interface AdEmployeeDao  extends IDaoService<AdEmployee> {
    int insert(AdEmployee record);
    int insertBatchWithId(List<AdEmployee> list);
    AdEmployee selectByEmployeeNo(String employeeNo);
    int deleteByEmployeeNo(String employeeNo);
    int deleteBatchByEmployeeNo(String employeeNos);
    AdEmployee select(String id);
    AdEmployee realSelect(String id);
    int delete(String id);
    int deleteBatch(String ids);
    int deleteList(List<AdEmployee> list);
    int update(AdEmployee record);
    int updateSelective(AdEmployee record);
    int updateBy(@Param("record")AdEmployee record,@Param("id")String id);
    //条件为所有非空字段 and，String采用like的查询模式，其他为=
    List<AdEmployee> list(AdEmployee record);
    //!!!  如 whereString="db_table_name.user_name like '王%'" ,orderString="db_table_name.born_date desc,db_table_name.code asc"
    List<AdEmployee> listBy(@Param("whereString")String whereString,@Param("orderString")String orderString);
    List<AdEmployee> query(@Param("sqlString")String sqlString);
    @Update("${sqlString}")
    int execute(@Param("sqlString") String sqlString);
}
