/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.ewallet.dao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import java.util.Map;
import top.iotequ.framework.service.IDaoService;
import top.iotequ.ewallet.pojo.EwCountProject;
import java.util.*;

//  Mapper dao : EwCountProjectDao (计次消费项目数据访问接口)
public interface EwCountProjectDao  extends IDaoService<EwCountProject> {
    int insert(EwCountProject record);
    int insertBatchWithoutId(List<EwCountProject> list);
    int insertBatchWithId(List<EwCountProject> list);
    EwCountProject select(Integer id);
    int delete(Integer id);
    int deleteBatch(String ids);
    int deleteList(List<EwCountProject> list);
    int update(EwCountProject record);
    int updateSelective(Map<String,Object> record);
    int updateBy(@Param("record")EwCountProject record,@Param("id")Integer id);
    //条件为所有非空字段 and，String采用like的查询模式，其他为=
    List<EwCountProject> list(EwCountProject record);
    //!!!  如 whereString="user_name like '王%'" ,orderString="born_date desc,code asc"
    List<EwCountProject> listBy(@Param("whereString")String whereString,@Param("orderString")String orderString);
    List<EwCountProject> query(@Param("sqlString")String sqlString);
    @Update("${sqlString}")
    int execute(@Param("sqlString") String sqlString);
}
