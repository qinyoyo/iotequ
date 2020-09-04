/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.codegenerator.dao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import top.iotequ.framework.service.IDaoService;
import top.iotequ.codegenerator.pojo.CgTable;
import java.util.*;

//  Mapper dao : CgTableDao (表单定义数据访问接口)
public interface CgTableDao  extends IDaoService<CgTable> {
    int insert(CgTable record);
    int insertBatchWithId(List<CgTable> list);
    CgTable select(String id);
    int delete(String id);
    int deleteBatch(String ids);
    CgTable selectByCodeProjectId(@Param("code")String code , @Param("projectId")String projectId);
    int deleteByCodeProjectId(@Param("code")String code , @Param("projectId")String projectId);
    int deleteList(List<CgTable> list);
    int update(CgTable record);
    int updateSelective(CgTable record);
    int updateBy(@Param("record")CgTable record,@Param("id")String id);
    //条件为所有非空字段 and，String采用like的查询模式，其他为=
    List<CgTable> list(CgTable record);
    //!!!  如 whereString="db_table_name.user_name like '王%'" ,orderString="db_table_name.born_date desc,db_table_name.code asc"
    List<CgTable> listBy(@Param("whereString")String whereString,@Param("orderString")String orderString);
    List<CgTable> query(@Param("sqlString")String sqlString);
    @Update("${sqlString}")
    int execute(@Param("sqlString") String sqlString);
}
