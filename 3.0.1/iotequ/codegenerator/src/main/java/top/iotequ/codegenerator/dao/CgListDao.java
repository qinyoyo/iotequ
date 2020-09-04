/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.codegenerator.dao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import top.iotequ.framework.service.IDaoService;
import top.iotequ.codegenerator.pojo.CgList;
import java.util.*;

//  Mapper dao : CgListDao (列表视图定义数据访问接口)
public interface CgListDao  extends IDaoService<CgList> {
    int insert(CgList record);
    int insertBatchWithId(List<CgList> list);
    CgList select(String id);
    int delete(String id);
    int deleteBatch(String ids);
    CgList selectByPathTableId(@Param("path")String path , @Param("tableId")String tableId);
    int deleteByPathTableId(@Param("path")String path , @Param("tableId")String tableId);
    int deleteList(List<CgList> list);
    int update(CgList record);
    int updateSelective(CgList record);
    int updateBy(@Param("record")CgList record,@Param("id")String id);
    //条件为所有非空字段 and，String采用like的查询模式，其他为=
    List<CgList> list(CgList record);
    //!!!  如 whereString="user_name like '王%'" ,orderString="born_date desc,code asc"
    List<CgList> listBy(@Param("whereString")String whereString,@Param("orderString")String orderString);
    List<CgList> query(@Param("sqlString")String sqlString);
    @Update("${sqlString}")
    int execute(@Param("sqlString") String sqlString);
}
