/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.attendance.dao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import top.iotequ.framework.service.IDaoService;
import top.iotequ.attendance.pojo.AdTest;
import java.util.*;

//  Mapper dao : AdTestDao (她她数据访问接口)
public interface AdTestDao  extends IDaoService<AdTest> {
    int insert(AdTest record);
    int insertBatchWithoutId(List<AdTest> list);
    int insertBatchWithId(List<AdTest> list);
    AdTest select(String id);
    int delete(String id);
    int deleteBatch(String ids);
    int deleteList(List<AdTest> list);
    int update(AdTest record);
    int updateSelective(AdTest record);
    int updateBy(@Param("record")AdTest record,@Param("id")String id);
    //条件为所有非空字段 and，String采用like的查询模式，其他为=
    List<AdTest> list(AdTest record);
    //!!!  如 whereString="db_table_name.user_name like '王%'" ,orderString="db_table_name.born_date desc,db_table_name.employee_no asc"
    List<AdTest> listBy(@Param("whereString")String whereString,@Param("orderString")String orderString);
    List<AdTest> query(@Param("sqlString")String sqlString);
    @Update("${sqlString}")
    int execute(@Param("sqlString") String sqlString);
}
