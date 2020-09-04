/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.reader.dao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import top.iotequ.framework.service.IDaoService;
import top.iotequ.reader.pojo.DevReader;
import java.util.*;

//  Mapper dao : DevReaderDao (终端设备表数据访问接口)
public interface DevReaderDao  extends IDaoService<DevReader> {
    int insert(DevReader record);
    int insertBatchWithoutId(List<DevReader> list);
    int insertBatchWithId(List<DevReader> list);
    DevReader select(String id);
    int delete(String id);
    int deleteBatch(String ids);
    DevReader selectByReaderNo(String readerNo);
    int deleteByReaderNo(String readerNo);
    int deleteBatchByReaderNo(String readerNos);
    int deleteList(List<DevReader> list);
    int update(DevReader record);
    int updateSelective(DevReader record);
    int updateBy(@Param("record")DevReader record,@Param("id")String id);
    //条件为所有非空字段 and，String采用like的查询模式，其他为=
    List<DevReader> list(DevReader record);
    //!!!  如 whereString="user_name like '王%'" ,orderString="born_date desc,code asc"
    List<DevReader> listBy(@Param("whereString")String whereString,@Param("orderString")String orderString);
    List<DevReader> query(@Param("sqlString")String sqlString);
    @Update("${sqlString}")
    int execute(@Param("sqlString") String sqlString);
}
