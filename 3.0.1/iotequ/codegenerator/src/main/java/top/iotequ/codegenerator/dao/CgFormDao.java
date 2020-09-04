/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.codegenerator.dao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import top.iotequ.framework.service.IDaoService;
import top.iotequ.codegenerator.pojo.CgForm;
import java.util.*;

//  Mapper dao : CgFormDao (表单定义表数据访问接口)
public interface CgFormDao  extends IDaoService<CgForm> {
    int insert(CgForm record);
    int insertBatchWithId(List<CgForm> list);
    CgForm select(String id);
    int delete(String id);
    int deleteBatch(String ids);
    CgForm selectByPathTableId(@Param("path")String path , @Param("tableId")String tableId);
    int deleteByPathTableId(@Param("path")String path , @Param("tableId")String tableId);
    int deleteList(List<CgForm> list);
    int update(CgForm record);
    int updateSelective(CgForm record);
    int updateBy(@Param("record")CgForm record,@Param("id")String id);
    //条件为所有非空字段 and，String采用like的查询模式，其他为=
    List<CgForm> list(CgForm record);
    //!!!  如 whereString="user_name like '王%'" ,orderString="born_date desc,code asc"
    List<CgForm> listBy(@Param("whereString")String whereString,@Param("orderString")String orderString);
    List<CgForm> query(@Param("sqlString")String sqlString);
    @Update("${sqlString}")
    int execute(@Param("sqlString") String sqlString);
}
