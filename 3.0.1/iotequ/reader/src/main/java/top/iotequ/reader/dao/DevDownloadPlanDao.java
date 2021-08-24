/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.reader.dao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import java.util.Map;
import top.iotequ.framework.service.IDaoService;
import top.iotequ.reader.pojo.DevDownloadPlan;
import java.util.*;

//  Mapper dao : DevDownloadPlanDao (下发计划|Download plan数据访问接口)
public interface DevDownloadPlanDao  extends IDaoService<DevDownloadPlan> {
    int insert(DevDownloadPlan record);
    int insertBatchWithoutId(List<DevDownloadPlan> list);
    int insertBatchWithId(List<DevDownloadPlan> list);
    DevDownloadPlan select(Integer id);
    int delete(Integer id);
    int deleteBatch(String ids);
    int deleteList(List<DevDownloadPlan> list);
    int update(DevDownloadPlan record);
    int updateSelective(Map<String,Object> record);
    int updateBy(@Param("record")DevDownloadPlan record,@Param("id")Integer id);
    //条件为所有非空字段 and，String采用like的查询模式，其他为=
    List<DevDownloadPlan> list(DevDownloadPlan record);
    //!!!  如 whereString="user_name like '王%'" ,orderString="born_date desc,code asc"
    List<DevDownloadPlan> listBy(@Param("whereString")String whereString,@Param("orderString")String orderString);
    List<DevDownloadPlan> query(@Param("sqlString")String sqlString);
    @Update("${sqlString}")
    int execute(@Param("sqlString") String sqlString);
}
