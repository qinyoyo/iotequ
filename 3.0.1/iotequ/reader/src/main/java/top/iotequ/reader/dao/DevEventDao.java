/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.reader.dao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import top.iotequ.framework.service.IDaoService;
import top.iotequ.reader.pojo.DevEvent;
import java.util.*;

//  Mapper dao : DevEventDao (设备事件数据访问接口)
public interface DevEventDao  extends IDaoService<DevEvent> {
    int insert(DevEvent record);
    int insertBatchWithoutId(List<DevEvent> list);
    int insertBatchWithId(List<DevEvent> list);
    DevEvent select(String id);
    int delete(String id);
    int deleteBatch(String ids);
    int deleteList(List<DevEvent> list);
    int update(DevEvent record);
    int updateSelective(DevEvent record);
    int updateBy(@Param("record")DevEvent record,@Param("id")String id);
    //条件为所有非空字段 and，String采用like的查询模式，其他为=
    List<DevEvent> list(DevEvent record);
    //!!!  如 whereString="db_table_name.user_name like '王%'" ,orderString="db_table_name.born_date desc,db_table_name.code asc"
    List<DevEvent> listBy(@Param("whereString")String whereString,@Param("orderString")String orderString);
    List<DevEvent> query(@Param("sqlString")String sqlString);
    @Update("${sqlString}")
    int execute(@Param("sqlString") String sqlString);
}
