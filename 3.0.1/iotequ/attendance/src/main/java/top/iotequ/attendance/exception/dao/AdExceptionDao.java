/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.attendance.exception.dao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import top.iotequ.framework.service.IDaoService;
import top.iotequ.attendance.exception.pojo.AdException;
import java.util.*;

//  Mapper dao : AdExceptionDao (节假日调休安排数据访问接口)
public interface AdExceptionDao  extends IDaoService<AdException> {
    int insert(AdException record);
    int insertBatchWithoutId(List<AdException> list);
    int insertBatchWithId(List<AdException> list);
    AdException select(Integer id);
    int delete(Integer id);
    int deleteBatch(String ids);
    int deleteList(List<AdException> list);
    int update(AdException record);
    int updateSelective(AdException record);
    int updateBy(@Param("record")AdException record,@Param("id")Integer id);
    //条件为所有非空字段 and，String采用like的查询模式，其他为=
    List<AdException> list(AdException record);
    //!!!  如 whereString="user_name like '王%'" ,orderString="born_date desc,code asc"
    List<AdException> listBy(@Param("whereString")String whereString,@Param("orderString")String orderString);
    List<AdException> query(@Param("sqlString")String sqlString);
    @Update("${sqlString}")
    int execute(@Param("sqlString") String sqlString);
}
