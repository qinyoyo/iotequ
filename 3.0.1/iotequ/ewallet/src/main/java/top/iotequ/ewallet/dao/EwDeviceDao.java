/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.ewallet.dao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import top.iotequ.framework.service.IDaoService;
import top.iotequ.ewallet.pojo.EwDevice;
import java.util.*;

//  Mapper dao : EwDeviceDao (授信设备数据访问接口)
public interface EwDeviceDao  extends IDaoService<EwDevice> {
    int insert(EwDevice record);
    int insertBatchWithoutId(List<EwDevice> list);
    int insertBatchWithId(List<EwDevice> list);
    EwDevice select(Integer id);
    int delete(Integer id);
    int deleteBatch(String ids);
    int deleteList(List<EwDevice> list);
    int update(EwDevice record);
    int updateSelective(EwDevice record);
    int updateBy(@Param("record")EwDevice record,@Param("id")Integer id);
    //条件为所有非空字段 and，String采用like的查询模式，其他为=
    List<EwDevice> list(EwDevice record);
    //!!!  如 whereString="user_name like '王%'" ,orderString="born_date desc,code asc"
    List<EwDevice> listBy(@Param("whereString")String whereString,@Param("orderString")String orderString);
    List<EwDevice> query(@Param("sqlString")String sqlString);
    @Update("${sqlString}")
    int execute(@Param("sqlString") String sqlString);
}
