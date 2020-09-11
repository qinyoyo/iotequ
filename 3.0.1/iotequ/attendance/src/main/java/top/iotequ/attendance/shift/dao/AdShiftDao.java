/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.attendance.shift.dao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import java.util.Map;
import top.iotequ.framework.service.IDaoService;
import top.iotequ.attendance.shift.pojo.AdShift;
import java.util.*;

//  Mapper dao : AdShiftDao (考勤排班表数据访问接口)
public interface AdShiftDao  extends IDaoService<AdShift> {
    int insert(AdShift record);
    int insertBatchWithoutId(List<AdShift> list);
    int insertBatchWithId(List<AdShift> list);
    AdShift select(Integer id);
    int delete(Integer id);
    int deleteBatch(String ids);
    int deleteList(List<AdShift> list);
    int update(AdShift record);
    int updateSelective(Map<String,Object> record);
    int updateBy(@Param("record")AdShift record,@Param("id")Integer id);
    //条件为所有非空字段 and，String采用like的查询模式，其他为=
    List<AdShift> list(AdShift record);
    //!!!  如 whereString="user_name like '王%'" ,orderString="born_date desc,code asc"
    List<AdShift> listBy(@Param("whereString")String whereString,@Param("orderString")String orderString);
    List<AdShift> query(@Param("sqlString")String sqlString);
    @Update("${sqlString}")
    int execute(@Param("sqlString") String sqlString);
}
