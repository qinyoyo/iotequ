/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.reader.dao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import java.util.Map;
import top.iotequ.framework.service.IDaoService;
import top.iotequ.reader.pojo.DevPeopleMapping;
import java.util.*;

//  Mapper dao : DevPeopleMappingDao (下发用户关系|Map of download users数据访问接口)
public interface DevPeopleMappingDao  extends IDaoService<DevPeopleMapping> {
    int insert(DevPeopleMapping record);
    int insertBatchWithoutId(List<DevPeopleMapping> list);
    int insertBatchWithId(List<DevPeopleMapping> list);
    DevPeopleMapping select(Integer id);
    int delete(Integer id);
    int deleteBatch(String ids);
    int deleteList(List<DevPeopleMapping> list);
    int update(DevPeopleMapping record);
    int updateSelective(Map<String,Object> record);
    int updateBy(@Param("record")DevPeopleMapping record,@Param("id")Integer id);
    //条件为所有非空字段 and，String采用like的查询模式，其他为=
    List<DevPeopleMapping> list(DevPeopleMapping record);
    //!!!  如 whereString="user_name like '王%'" ,orderString="born_date desc,code asc"
    List<DevPeopleMapping> listBy(@Param("whereString")String whereString,@Param("orderString")String orderString);
    List<DevPeopleMapping> query(@Param("sqlString")String sqlString);
    @Update("${sqlString}")
    int execute(@Param("sqlString") String sqlString);
}
