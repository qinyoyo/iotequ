/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.reader.dao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import java.util.Map;
import top.iotequ.framework.service.IDaoService;
import top.iotequ.reader.pojo.DevAuthGroup;
import java.util.*;

//  Mapper dao : DevAuthGroupDao (人员授权分组数据访问接口)
public interface DevAuthGroupDao  extends IDaoService<DevAuthGroup> {
    int insert(DevAuthGroup record);
    int insertBatchWithoutId(List<DevAuthGroup> list);
    int insertBatchWithId(List<DevAuthGroup> list);
    DevAuthGroup select(Integer id);
    int delete(Integer id);
    int deleteBatch(String ids);
    int deleteList(List<DevAuthGroup> list);
    int update(DevAuthGroup record);
    int updateSelective(Map<String,Object> record);
    int updateBy(@Param("record")DevAuthGroup record,@Param("id")Integer id);
    //条件为所有非空字段 and，String采用like的查询模式，其他为=
    List<DevAuthGroup> list(DevAuthGroup record);
    //!!!  如 whereString="user_name like '王%'" ,orderString="born_date desc,code asc"
    List<DevAuthGroup> listBy(@Param("whereString")String whereString,@Param("orderString")String orderString);
    List<DevAuthGroup> query(@Param("sqlString")String sqlString);
    @Update("${sqlString}")
    int execute(@Param("sqlString") String sqlString);
}
