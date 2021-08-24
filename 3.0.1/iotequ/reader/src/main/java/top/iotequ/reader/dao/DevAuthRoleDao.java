/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.reader.dao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import java.util.Map;
import top.iotequ.framework.service.IDaoService;
import top.iotequ.reader.pojo.DevAuthRole;
import java.util.*;

//  Mapper dao : DevAuthRoleDao (权限定义|Authorization role数据访问接口)
public interface DevAuthRoleDao  extends IDaoService<DevAuthRole> {
    int insert(DevAuthRole record);
    int insertBatchWithoutId(List<DevAuthRole> list);
    int insertBatchWithId(List<DevAuthRole> list);
    DevAuthRole select(Integer id);
    int delete(Integer id);
    int deleteBatch(String ids);
    int deleteList(List<DevAuthRole> list);
    int update(DevAuthRole record);
    int updateSelective(Map<String,Object> record);
    int updateBy(@Param("record")DevAuthRole record,@Param("id")Integer id);
    //条件为所有非空字段 and，String采用like的查询模式，其他为=
    List<DevAuthRole> list(DevAuthRole record);
    //!!!  如 whereString="user_name like '王%'" ,orderString="born_date desc,code asc"
    List<DevAuthRole> listBy(@Param("whereString")String whereString,@Param("orderString")String orderString);
    List<DevAuthRole> query(@Param("sqlString")String sqlString);
    @Update("${sqlString}")
    int execute(@Param("sqlString") String sqlString);
}
