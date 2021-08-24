/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.attendance.adjust.dao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import java.util.Map;
import top.iotequ.framework.service.IDaoService;
import top.iotequ.attendance.adjust.pojo.AdAdjust;
import java.util.*;

//  Mapper dao : AdAdjustDao (考勤调整|Action of attendance数据访问接口)
public interface AdAdjustDao  extends IDaoService<AdAdjust> {
    int insert(AdAdjust record);
    int insertBatchWithoutId(List<AdAdjust> list);
    int insertBatchWithId(List<AdAdjust> list);
    AdAdjust select(String id);
    int delete(String id);
    int deleteBatch(String ids);
    int deleteList(List<AdAdjust> list);
    int update(AdAdjust record);
    int updateSelective(Map<String,Object> record);
    int updateBy(@Param("record")AdAdjust record,@Param("id")String id);
    //条件为所有非空字段 and，String采用like的查询模式，其他为=
    List<AdAdjust> list(AdAdjust record);
    //!!!  如 whereString="db_table_name.user_name like '王%'" ,orderString="db_table_name.born_date desc,db_table_name.code asc"
    List<AdAdjust> listBy(@Param("whereString")String whereString,@Param("orderString")String orderString);
    List<AdAdjust> query(@Param("sqlString")String sqlString);
    @Update("${sqlString}")
    int execute(@Param("sqlString") String sqlString);
}
