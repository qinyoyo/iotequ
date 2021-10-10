/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.codegenerator.dao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import java.util.Map;
import top.iotequ.framework.service.IDaoService;
import top.iotequ.codegenerator.pojo.CgListField;
import java.util.*;

//  Mapper dao : CgListFieldDao (列表视图字段定义|Fields of list数据访问接口)
public interface CgListFieldDao  extends IDaoService<CgListField> {
    int insert(CgListField record);
    int insertBatchWithoutId(List<CgListField> list);
    int insertBatchWithId(List<CgListField> list);
    CgListField select(Integer id);
    int delete(Integer id);
    int deleteBatch(String ids);
    CgListField selectByListIdEntityField(@Param("listId")String listId , @Param("entityField")String entityField);
    int deleteByListIdEntityField(@Param("listId")String listId , @Param("entityField")String entityField);
    int deleteList(List<CgListField> list);
    int update(CgListField record);
    int updateSelective(Map<String,Object> record);
    int updateBy(@Param("record")CgListField record,@Param("id")Integer id);
    //条件为所有非空字段 and，String采用like的查询模式，其他为=
    List<CgListField> list(CgListField record);
    //!!!  如 whereString="user_name like '王%'" ,orderString="born_date desc,code asc"
    List<CgListField> listBy(@Param("whereString")String whereString,@Param("orderString")String orderString);
    List<CgListField> query(@Param("sqlString")String sqlString);
    @Update("${sqlString}")
    int execute(@Param("sqlString") String sqlString);
}
