/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.reader.dao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import java.util.Map;
import top.iotequ.framework.service.IDaoService;
import top.iotequ.reader.pojo.DevNewDevice;
import java.util.*;

//  Mapper dao : DevNewDeviceDao (未注册设备数据访问接口)
public interface DevNewDeviceDao  extends IDaoService<DevNewDevice> {
    int insert(DevNewDevice record);
    int insertBatchWithId(List<DevNewDevice> list);
    DevNewDevice select(String readerNo);
    int delete(String readerNo);
    int deleteBatch(String readerNos);
    int deleteList(List<DevNewDevice> list);
    int update(DevNewDevice record);
    int updateSelective(Map<String,Object> record);
    int updateBy(@Param("record")DevNewDevice record,@Param("readerNo")String id);
    //条件为所有非空字段 and，String采用like的查询模式，其他为=
    List<DevNewDevice> list(DevNewDevice record);
    //!!!  如 whereString="user_name like '王%'" ,orderString="born_date desc,code asc"
    List<DevNewDevice> listBy(@Param("whereString")String whereString,@Param("orderString")String orderString);
    List<DevNewDevice> query(@Param("sqlString")String sqlString);
    @Update("${sqlString}")
    int execute(@Param("sqlString") String sqlString);
}
