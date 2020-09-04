/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.codegenerator.dao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import top.iotequ.framework.service.IDaoService;
import top.iotequ.codegenerator.pojo.CgButton;
import java.util.*;

//  Mapper dao : CgButtonDao (按钮定义数据访问接口)
public interface CgButtonDao  extends IDaoService<CgButton> {
    int insert(CgButton record);
    int insertBatchWithoutId(List<CgButton> list);
    int insertBatchWithId(List<CgButton> list);
    CgButton select(String id);
    int delete(String id);
    int deleteBatch(String ids);
    CgButton selectByTableIdAction(@Param("tableId")String tableId , @Param("action")String action);
    int deleteByTableIdAction(@Param("tableId")String tableId , @Param("action")String action);
    int deleteList(List<CgButton> list);
    int update(CgButton record);
    int updateSelective(CgButton record);
    int updateBy(@Param("record")CgButton record,@Param("id")String id);
    //条件为所有非空字段 and，String采用like的查询模式，其他为=
    List<CgButton> list(CgButton record);
    //!!!  如 whereString="user_name like '王%'" ,orderString="born_date desc,code asc"
    List<CgButton> listBy(@Param("whereString")String whereString,@Param("orderString")String orderString);
    List<CgButton> query(@Param("sqlString")String sqlString);
    @Update("${sqlString}")
    int execute(@Param("sqlString") String sqlString);
}
