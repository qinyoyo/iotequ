/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.attendance.shifttime.dao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import java.util.Map;
import top.iotequ.framework.service.IDaoService;
import top.iotequ.attendance.shifttime.pojo.AdShiftTime;
import java.util.*;

//  Mapper dao : AdShiftTimeDao (排班时间表数据访问接口)
public interface AdShiftTimeDao  extends IDaoService<AdShiftTime> {
    int insert(AdShiftTime record);
    int insertBatchWithoutId(List<AdShiftTime> list);
    int insertBatchWithId(List<AdShiftTime> list);
    AdShiftTime select(Integer id);
    int delete(Integer id);
    int deleteBatch(String ids);
    int deleteList(List<AdShiftTime> list);
    int update(AdShiftTime record);
    int updateSelective(Map<String,Object> record);
    int updateBy(@Param("record")AdShiftTime record,@Param("id")Integer id);
    //条件为所有非空字段 and，String采用like的查询模式，其他为=
    List<AdShiftTime> list(AdShiftTime record);
    //!!!  如 whereString="user_name like '王%'" ,orderString="born_date desc,code asc"
    List<AdShiftTime> listBy(@Param("whereString")String whereString,@Param("orderString")String orderString);
    List<AdShiftTime> query(@Param("sqlString")String sqlString);
    @Update("${sqlString}")
    int execute(@Param("sqlString") String sqlString);
}
