/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.reader.dao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import top.iotequ.framework.service.IDaoService;
import top.iotequ.reader.pojo.DevAuthConfig;
import java.util.*;

//  Mapper dao : DevAuthConfigDao (权限配置数据访问接口)
public interface DevAuthConfigDao  extends IDaoService<DevAuthConfig> {
    int insert(DevAuthConfig record);
    int insertBatchWithoutId(List<DevAuthConfig> list);
    int insertBatchWithId(List<DevAuthConfig> list);
    DevAuthConfig select(Integer id);
    int delete(Integer id);
    int deleteBatch(String ids);
    int deleteList(List<DevAuthConfig> list);
    int update(DevAuthConfig record);
    int updateSelective(DevAuthConfig record);
    int updateBy(@Param("record")DevAuthConfig record,@Param("id")Integer id);
    //条件为所有非空字段 and，String采用like的查询模式，其他为=
    List<DevAuthConfig> list(DevAuthConfig record);
    //!!!  如 whereString="user_name like '王%'" ,orderString="born_date desc,employee_no asc"
    List<DevAuthConfig> listBy(@Param("whereString")String whereString,@Param("orderString")String orderString);
    List<DevAuthConfig> query(@Param("sqlString")String sqlString);
    @Update("${sqlString}")
    int execute(@Param("sqlString") String sqlString);
}
