/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.attendance.dayresult.dao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import java.util.Map;
import top.iotequ.framework.service.IDaoService;
import top.iotequ.attendance.dayresult.pojo.AdDayResult;
import java.util.*;

//  Mapper dao : AdDayResultDao (日考勤结果|Attendance result by day数据访问接口)
public interface AdDayResultDao  extends IDaoService<AdDayResult> {
    int insert(AdDayResult record);
    int insertBatchWithoutId(List<AdDayResult> list);
    int insertBatchWithId(List<AdDayResult> list);
    AdDayResult select(Integer id);
    int delete(Integer id);
    int deleteBatch(String ids);
    int deleteList(List<AdDayResult> list);
    int update(AdDayResult record);
    int updateSelective(Map<String,Object> record);
    int updateBy(@Param("record")AdDayResult record,@Param("id")Integer id);
    //条件为所有非空字段 and，String采用like的查询模式，其他为=
    List<AdDayResult> list(AdDayResult record);
    //!!!  如 whereString="db_table_name.user_name like '王%'" ,orderString="db_table_name.born_date desc,db_table_name.code asc"
    List<AdDayResult> listBy(@Param("whereString")String whereString,@Param("orderString")String orderString);
    List<AdDayResult> query(@Param("sqlString")String sqlString);
    @Update("${sqlString}")
    int execute(@Param("sqlString") String sqlString);
}
