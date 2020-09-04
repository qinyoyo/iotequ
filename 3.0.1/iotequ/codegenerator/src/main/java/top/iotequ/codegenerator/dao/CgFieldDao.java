/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.codegenerator.dao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import top.iotequ.framework.service.IDaoService;
import top.iotequ.codegenerator.pojo.CgField;
import java.util.*;

//  Mapper dao : CgFieldDao (字段定义表数据访问接口)
public interface CgFieldDao  extends IDaoService<CgField> {
    int insert(CgField record);
    int insertBatchWithoutId(List<CgField> list);
    int insertBatchWithId(List<CgField> list);
    CgField select(String id);
    int delete(String id);
    int deleteBatch(String ids);
    CgField selectByTableIdEntityName(@Param("tableId")String tableId , @Param("entityName")String entityName);
    int deleteByTableIdEntityName(@Param("tableId")String tableId , @Param("entityName")String entityName);
    int deleteList(List<CgField> list);
    int update(CgField record);
    int updateSelective(CgField record);
    int updateBy(@Param("record")CgField record,@Param("id")String id);
    //条件为所有非空字段 and，String采用like的查询模式，其他为=
    List<CgField> list(CgField record);
    //!!!  如 whereString="user_name like '王%'" ,orderString="born_date desc,code asc"
    List<CgField> listBy(@Param("whereString")String whereString,@Param("orderString")String orderString);
    List<CgField> query(@Param("sqlString")String sqlString);
    @Update("${sqlString}")
    int execute(@Param("sqlString") String sqlString);
}
