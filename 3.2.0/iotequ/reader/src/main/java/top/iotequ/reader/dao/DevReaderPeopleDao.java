/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.reader.dao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import java.util.Map;
import top.iotequ.framework.service.IDaoService;
import top.iotequ.reader.pojo.DevReaderPeople;
import java.util.*;

//  Mapper dao : DevReaderPeopleDao (设备人员信息|Device people infomation数据访问接口)
public interface DevReaderPeopleDao  extends IDaoService<DevReaderPeople> {
    int insert(DevReaderPeople record);
    int insertBatchWithoutId(List<DevReaderPeople> list);
    int insertBatchWithId(List<DevReaderPeople> list);
    DevReaderPeople select(Integer id);
    int delete(Integer id);
    int deleteBatch(String ids);
    int deleteList(List<DevReaderPeople> list);
    int update(DevReaderPeople record);
    int updateSelective(Map<String,Object> record);
    int updateBy(@Param("record")DevReaderPeople record,@Param("id")Integer id);
    //条件为所有非空字段 and，String采用like的查询模式，其他为=
    List<DevReaderPeople> list(DevReaderPeople record);
    //!!!  如 whereString="db_table_name.user_name like '王%'" ,orderString="db_table_name.born_date desc,db_table_name.code asc"
    List<DevReaderPeople> listBy(@Param("whereString")String whereString,@Param("orderString")String orderString);
    List<DevReaderPeople> query(@Param("sqlString")String sqlString);
    @Update("${sqlString}")
    int execute(@Param("sqlString") String sqlString);
}
