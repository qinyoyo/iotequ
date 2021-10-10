/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.ewallet.dao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import java.util.Map;
import top.iotequ.framework.service.IDaoService;
import top.iotequ.ewallet.pojo.EwTimeProject;
import java.util.*;

//  Mapper dao : EwTimeProjectDao (计时消费项目数据访问接口)
public interface EwTimeProjectDao  extends IDaoService<EwTimeProject> {
    int insert(EwTimeProject record);
    int insertBatchWithoutId(List<EwTimeProject> list);
    int insertBatchWithId(List<EwTimeProject> list);
    EwTimeProject select(Integer id);
    int delete(Integer id);
    int deleteBatch(String ids);
    int deleteList(List<EwTimeProject> list);
    int update(EwTimeProject record);
    int updateSelective(Map<String,Object> record);
    int updateBy(@Param("record")EwTimeProject record,@Param("id")Integer id);
    //条件为所有非空字段 and，String采用like的查询模式，其他为=
    List<EwTimeProject> list(EwTimeProject record);
    //!!!  如 whereString="user_name like '王%'" ,orderString="born_date desc,code asc"
    List<EwTimeProject> listBy(@Param("whereString")String whereString,@Param("orderString")String orderString);
    List<EwTimeProject> query(@Param("sqlString")String sqlString);
    @Update("${sqlString}")
    int execute(@Param("sqlString") String sqlString);
}
