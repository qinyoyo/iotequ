/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.codegenerator.dao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import top.iotequ.framework.service.IDaoService;
import top.iotequ.codegenerator.pojo.CgFormField;
import java.util.*;

//  Mapper dao : CgFormFieldDao (表单字段定义数据访问接口)
public interface CgFormFieldDao  extends IDaoService<CgFormField> {
    int insert(CgFormField record);
    int insertBatchWithoutId(List<CgFormField> list);
    int insertBatchWithId(List<CgFormField> list);
    CgFormField select(Integer id);
    int delete(Integer id);
    int deleteBatch(String ids);
    CgFormField selectByFormIdEntityField(@Param("formId")String formId , @Param("entityField")String entityField);
    int deleteByFormIdEntityField(@Param("formId")String formId , @Param("entityField")String entityField);
    int deleteList(List<CgFormField> list);
    int update(CgFormField record);
    int updateSelective(CgFormField record);
    int updateBy(@Param("record")CgFormField record,@Param("id")Integer id);
    //条件为所有非空字段 and，String采用like的查询模式，其他为=
    List<CgFormField> list(CgFormField record);
    //!!!  如 whereString="user_name like '王%'" ,orderString="born_date desc,code asc"
    List<CgFormField> listBy(@Param("whereString")String whereString,@Param("orderString")String orderString);
    List<CgFormField> query(@Param("sqlString")String sqlString);
    @Update("${sqlString}")
    int execute(@Param("sqlString") String sqlString);
}
