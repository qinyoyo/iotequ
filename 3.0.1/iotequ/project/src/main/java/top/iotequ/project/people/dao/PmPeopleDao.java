/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.project.people.dao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import top.iotequ.framework.service.IDaoService;
import top.iotequ.project.people.pojo.PmPeople;
import java.util.*;

//  Mapper dao : PmPeopleDao (项目人员数据访问接口)
public interface PmPeopleDao  extends IDaoService<PmPeople> {
    int insert(PmPeople record);
    int insertBatchWithoutId(List<PmPeople> list);
    int insertBatchWithId(List<PmPeople> list);
    PmPeople select(Integer id);
    int delete(Integer id);
    int deleteBatch(String ids);
    PmPeople selectByGroupIdUserId(@Param("groupId")Integer groupId , @Param("userId")String userId);
    int deleteByGroupIdUserId(@Param("groupId")Integer groupId , @Param("userId")String userId);
    int deleteList(List<PmPeople> list);
    int update(PmPeople record);
    int updateSelective(PmPeople record);
    int updateBy(@Param("record")PmPeople record,@Param("id")Integer id);
    //条件为所有非空字段 and，String采用like的查询模式，其他为=
    List<PmPeople> list(PmPeople record);
    //!!!  如 whereString="db_table_name.user_name like '王%'" ,orderString="db_table_name.born_date desc,db_table_name.code asc"
    List<PmPeople> listBy(@Param("whereString")String whereString,@Param("orderString")String orderString);
    List<PmPeople> query(@Param("sqlString")String sqlString);
    @Update("${sqlString}")
    int execute(@Param("sqlString") String sqlString);
}
