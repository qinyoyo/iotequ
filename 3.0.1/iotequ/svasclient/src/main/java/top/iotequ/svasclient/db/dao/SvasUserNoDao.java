/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.svasclient.db.dao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import top.iotequ.framework.service.IDaoService;
import top.iotequ.svasclient.db.pojo.SvasUserNo;
import java.util.*;

//  Mapper dao : SvasUserNoDao (用户号列表数据访问接口)
public interface SvasUserNoDao  extends IDaoService<SvasUserNo> {
    int insert(SvasUserNo record);
    int insertBatchWithoutId(List<SvasUserNo> list);
    int insertBatchWithId(List<SvasUserNo> list);
    SvasUserNo select(Integer id);
    int delete(Integer id);
    int deleteBatch(String ids);
    SvasUserNo selectByUserNo(String userNo);
    int deleteByUserNo(String userNo);
    int deleteBatchByUserNo(String userNos);
    int deleteList(List<SvasUserNo> list);
    int update(SvasUserNo record);
    int updateSelective(SvasUserNo record);
    int updateBy(@Param("record")SvasUserNo record,@Param("id")Integer id);
    //条件为所有非空字段 and，String采用like的查询模式，其他为=
    List<SvasUserNo> list(SvasUserNo record);
    //!!!  如 whereString="user_name like '王%'" ,orderString="born_date desc,employee_no asc"
    List<SvasUserNo> listBy(@Param("whereString")String whereString,@Param("orderString")String orderString);
    List<SvasUserNo> query(@Param("sqlString")String sqlString);
    @Update("${sqlString}")
    int execute(@Param("sqlString") String sqlString);
}
