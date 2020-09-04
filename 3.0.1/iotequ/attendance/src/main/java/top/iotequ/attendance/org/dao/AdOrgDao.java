/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.attendance.org.dao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import top.iotequ.framework.service.IDaoService;
import top.iotequ.attendance.org.pojo.AdOrg;
import java.util.*;

//  Mapper dao : AdOrgDao (考勤部门信息表数据访问接口)
public interface AdOrgDao  extends IDaoService<AdOrg> {
    int insert(AdOrg record);
    int insertBatchWithId(List<AdOrg> list);
    AdOrg select(Integer orgCode);
    AdOrg realSelect(Integer orgCode);
    int delete(Integer orgCode);
    int deleteBatch(String orgCodes);
    int deleteList(List<AdOrg> list);
    int update(AdOrg record);
    int updateSelective(AdOrg record);
    int updateBy(@Param("record")AdOrg record,@Param("orgCode")Integer id);
    //条件为所有非空字段 and，String采用like的查询模式，其他为=
    List<AdOrg> list(AdOrg record);
    //!!!  如 whereString="db_table_name.user_name like '王%'" ,orderString="db_table_name.born_date desc,db_table_name.code asc"
    List<AdOrg> listBy(@Param("whereString")String whereString,@Param("orderString")String orderString);
    List<AdOrg> query(@Param("sqlString")String sqlString);
    @Update("${sqlString}")
    int execute(@Param("sqlString") String sqlString);
}
