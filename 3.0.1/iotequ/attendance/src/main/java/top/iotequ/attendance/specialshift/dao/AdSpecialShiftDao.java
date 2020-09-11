/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.attendance.specialshift.dao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import java.util.Map;
import top.iotequ.framework.service.IDaoService;
import top.iotequ.attendance.specialshift.pojo.AdSpecialShift;
import java.util.*;

//  Mapper dao : AdSpecialShiftDao (特殊排班表数据访问接口)
public interface AdSpecialShiftDao  extends IDaoService<AdSpecialShift> {
    int insert(AdSpecialShift record);
    int insertBatchWithoutId(List<AdSpecialShift> list);
    int insertBatchWithId(List<AdSpecialShift> list);
    AdSpecialShift select(Integer id);
    int delete(Integer id);
    int deleteBatch(String ids);
    int deleteList(List<AdSpecialShift> list);
    int update(AdSpecialShift record);
    int updateSelective(Map<String,Object> record);
    int updateBy(@Param("record")AdSpecialShift record,@Param("id")Integer id);
    //条件为所有非空字段 and，String采用like的查询模式，其他为=
    List<AdSpecialShift> list(AdSpecialShift record);
    //!!!  如 whereString="user_name like '王%'" ,orderString="born_date desc,code asc"
    List<AdSpecialShift> listBy(@Param("whereString")String whereString,@Param("orderString")String orderString);
    List<AdSpecialShift> query(@Param("sqlString")String sqlString);
    @Update("${sqlString}")
    int execute(@Param("sqlString") String sqlString);
}
