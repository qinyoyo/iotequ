/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.attendance.specialshifttime.dao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import java.util.Map;
import top.iotequ.framework.service.IDaoService;
import top.iotequ.attendance.specialshifttime.pojo.AdSpecialShiftTime;
import java.util.*;

//  Mapper dao : AdSpecialShiftTimeDao (特殊排班时间表数据访问接口)
public interface AdSpecialShiftTimeDao  extends IDaoService<AdSpecialShiftTime> {
    int insert(AdSpecialShiftTime record);
    int insertBatchWithoutId(List<AdSpecialShiftTime> list);
    int insertBatchWithId(List<AdSpecialShiftTime> list);
    AdSpecialShiftTime select(Integer id);
    int delete(Integer id);
    int deleteBatch(String ids);
    int deleteList(List<AdSpecialShiftTime> list);
    int update(AdSpecialShiftTime record);
    int updateSelective(Map<String,Object> record);
    int updateBy(@Param("record")AdSpecialShiftTime record,@Param("id")Integer id);
    //条件为所有非空字段 and，String采用like的查询模式，其他为=
    List<AdSpecialShiftTime> list(AdSpecialShiftTime record);
    //!!!  如 whereString="user_name like '王%'" ,orderString="born_date desc,code asc"
    List<AdSpecialShiftTime> listBy(@Param("whereString")String whereString,@Param("orderString")String orderString);
    List<AdSpecialShiftTime> query(@Param("sqlString")String sqlString);
    @Update("${sqlString}")
    int execute(@Param("sqlString") String sqlString);
}
