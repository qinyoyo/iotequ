/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.reader.dao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import top.iotequ.framework.service.IDaoService;
import top.iotequ.reader.pojo.DevOrg;
import java.util.*;

//  Mapper dao : DevOrgDao (组织机构数据访问接口)
public interface DevOrgDao  extends IDaoService<DevOrg> {
    int insert(DevOrg record);
    int insertBatchWithoutId(List<DevOrg> list);
    int insertBatchWithId(List<DevOrg> list);
    DevOrg select(Integer orgCode);
    int delete(Integer orgCode);
    int deleteBatch(String orgCodes);
    int deleteList(List<DevOrg> list);
    List<DevOrg> selectTree(@Param("id") Integer id);  // 选全部参数为null
    int update(DevOrg record);
    int updateSelective(DevOrg record);
    int updateBy(@Param("record") DevOrg record, @Param("orgCode") Integer id);
    //条件为所有非空字段 and，String采用like的查询模式，其他为=
    List<DevOrg> list(DevOrg record);
    //!!!  如 whereString="user_name like '王%'" ,orderString="born_date desc,employee_no asc"
    List<DevOrg> listBy(@Param("whereString") String whereString, @Param("orderString") String orderString);
    List<DevOrg> query(@Param("sqlString") String sqlString);
    @Update("${sqlString}")
    int execute(@Param("sqlString") String sqlString);
}
