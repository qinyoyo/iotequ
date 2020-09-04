/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.reader.dao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import top.iotequ.framework.service.IDaoService;
import top.iotequ.reader.pojo.DevOrgGroup;
import java.util.*;

//  Mapper dao : DevOrgGroupDao (分组部门数据访问接口)
public interface DevOrgGroupDao  extends IDaoService<DevOrgGroup> {
    int insert(DevOrgGroup record);
    int insertBatchWithoutId(List<DevOrgGroup> list);
    int insertBatchWithId(List<DevOrgGroup> list);
    DevOrgGroup select(Integer id);
    int delete(Integer id);
    int deleteBatch(String ids);
    DevOrgGroup selectByGroupIdOrgId(@Param("groupId")Integer groupId , @Param("orgId")Integer orgId);
    int deleteByGroupIdOrgId(@Param("groupId")Integer groupId , @Param("orgId")Integer orgId);
    int deleteList(List<DevOrgGroup> list);
    int update(DevOrgGroup record);
    int updateSelective(DevOrgGroup record);
    int updateBy(@Param("record")DevOrgGroup record,@Param("id")Integer id);
    //条件为所有非空字段 and，String采用like的查询模式，其他为=
    List<DevOrgGroup> list(DevOrgGroup record);
    //!!!  如 whereString="db_table_name.user_name like '王%'" ,orderString="db_table_name.born_date desc,db_table_name.employee_no asc"
    List<DevOrgGroup> listBy(@Param("whereString")String whereString,@Param("orderString")String orderString);
    List<DevOrgGroup> query(@Param("sqlString")String sqlString);
    @Update("${sqlString}")
    int execute(@Param("sqlString") String sqlString);
}
