/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.reader.dao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import top.iotequ.framework.service.IDaoService;
import top.iotequ.reader.pojo.DevReaderGroup;
import java.util.*;

//  Mapper dao : DevReaderGroupDao (设备分组数据访问接口)
public interface DevReaderGroupDao  extends IDaoService<DevReaderGroup> {
    int insert(DevReaderGroup record);
    int insertBatchWithoutId(List<DevReaderGroup> list);
    int insertBatchWithId(List<DevReaderGroup> list);
    DevReaderGroup select(Integer id);
    int delete(Integer id);
    int deleteBatch(String ids);
    DevReaderGroup selectByName(String name);
    int deleteByName(String name);
    int deleteBatchByName(String names);
    int deleteList(List<DevReaderGroup> list);
    List<DevReaderGroup> selectTree(@Param("id")Integer id);  // 选全部参数为null
    int update(DevReaderGroup record);
    int updateSelective(DevReaderGroup record);
    int updateBy(@Param("record")DevReaderGroup record,@Param("id")Integer id);
    //条件为所有非空字段 and，String采用like的查询模式，其他为=
    List<DevReaderGroup> list(DevReaderGroup record);
    //!!!  如 whereString="user_name like '王%'" ,orderString="born_date desc,code asc"
    List<DevReaderGroup> listBy(@Param("whereString")String whereString,@Param("orderString")String orderString);
    List<DevReaderGroup> query(@Param("sqlString")String sqlString);
    @Update("${sqlString}")
    int execute(@Param("sqlString") String sqlString);
}
