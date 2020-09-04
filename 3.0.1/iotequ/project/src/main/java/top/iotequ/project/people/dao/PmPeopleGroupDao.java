/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.project.people.dao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import top.iotequ.framework.service.IDaoService;
import top.iotequ.project.people.pojo.PmPeopleGroup;
import java.util.*;

//  Mapper dao : PmPeopleGroupDao (职能人员分组数据访问接口)
public interface PmPeopleGroupDao  extends IDaoService<PmPeopleGroup> {
    int insert(PmPeopleGroup record);
    int insertBatchWithoutId(List<PmPeopleGroup> list);
    int insertBatchWithId(List<PmPeopleGroup> list);
    PmPeopleGroup select(Integer id);
    int delete(Integer id);
    int deleteBatch(String ids);
    PmPeopleGroup selectByGroupType(String groupType);
    int deleteByGroupType(String groupType);
    int deleteBatchByGroupType(String groupTypes);
    int deleteList(List<PmPeopleGroup> list);
    List<PmPeopleGroup> selectTree(@Param("id")Integer id);  // 选全部参数为null
    int update(PmPeopleGroup record);
    int updateSelective(PmPeopleGroup record);
    int updateBy(@Param("record")PmPeopleGroup record,@Param("id")Integer id);
    //条件为所有非空字段 and，String采用like的查询模式，其他为=
    List<PmPeopleGroup> list(PmPeopleGroup record);
    //!!!  如 whereString="user_name like '王%'" ,orderString="born_date desc,code asc"
    List<PmPeopleGroup> listBy(@Param("whereString")String whereString,@Param("orderString")String orderString);
    List<PmPeopleGroup> query(@Param("sqlString")String sqlString);
    @Update("${sqlString}")
    int execute(@Param("sqlString") String sqlString);
}
