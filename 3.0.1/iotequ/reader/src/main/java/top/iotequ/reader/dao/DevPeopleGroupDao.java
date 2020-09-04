/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.reader.dao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import top.iotequ.framework.service.IDaoService;
import top.iotequ.reader.pojo.DevPeopleGroup;
import java.util.*;

//  Mapper dao : DevPeopleGroupDao (分组人员数据访问接口)
public interface DevPeopleGroupDao  extends IDaoService<DevPeopleGroup> {
    int insert(DevPeopleGroup record);
    int insertBatchWithoutId(List<DevPeopleGroup> list);
    int insertBatchWithId(List<DevPeopleGroup> list);
    DevPeopleGroup select(Integer id);
    int delete(Integer id);
    int deleteBatch(String ids);
    DevPeopleGroup selectByGroupIdUserNo(@Param("groupId")Integer groupId , @Param("userNo")String userNo);
    int deleteByGroupIdUserNo(@Param("groupId")Integer groupId , @Param("userNo")String userNo);
    int deleteList(List<DevPeopleGroup> list);
    int update(DevPeopleGroup record);
    int updateSelective(DevPeopleGroup record);
    int updateBy(@Param("record")DevPeopleGroup record,@Param("id")Integer id);
    //条件为所有非空字段 and，String采用like的查询模式，其他为=
    List<DevPeopleGroup> list(DevPeopleGroup record);
    //!!!  如 whereString="db_table_name.user_name like '王%'" ,orderString="db_table_name.born_date desc,db_table_name.code asc"
    List<DevPeopleGroup> listBy(@Param("whereString")String whereString,@Param("orderString")String orderString);
    List<DevPeopleGroup> query(@Param("sqlString")String sqlString);
    @Update("${sqlString}")
    int execute(@Param("sqlString") String sqlString);
}
