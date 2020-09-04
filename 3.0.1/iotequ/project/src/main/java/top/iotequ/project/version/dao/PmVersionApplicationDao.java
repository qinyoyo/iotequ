/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.project.version.dao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import top.iotequ.framework.service.IDaoService;
import top.iotequ.project.version.pojo.PmVersionApplication;
import java.util.*;

//  Mapper dao : PmVersionApplicationDao (版本申请数据访问接口)
public interface PmVersionApplicationDao  extends IDaoService<PmVersionApplication> {
    int insert(PmVersionApplication record);
    int insertBatchWithoutId(List<PmVersionApplication> list);
    int insertBatchWithId(List<PmVersionApplication> list);
    PmVersionApplication select(String id);
    int delete(String id);
    int deleteBatch(String ids);
    List<PmVersionApplication> selectByProject(String project);
    int deleteByProject(String project);
    int deleteBatchByProject(String projects);
    int deleteList(List<PmVersionApplication> list);
    int update(PmVersionApplication record);
    int updateSelective(PmVersionApplication record);
    int updateBy(@Param("record")PmVersionApplication record,@Param("id")String id);
    //条件为所有非空字段 and，String采用like的查询模式，其他为=
    List<PmVersionApplication> list(PmVersionApplication record);
    //!!!  如 whereString="db_table_name.user_name like '王%'" ,orderString="db_table_name.born_date desc,db_table_name.code asc"
    List<PmVersionApplication> listBy(@Param("whereString")String whereString,@Param("orderString")String orderString);
    List<PmVersionApplication> query(@Param("sqlString")String sqlString);
    @Update("${sqlString}")
    int execute(@Param("sqlString") String sqlString);
}
