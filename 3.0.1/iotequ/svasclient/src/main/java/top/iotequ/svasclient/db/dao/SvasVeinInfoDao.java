/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.svasclient.db.dao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import top.iotequ.framework.service.IDaoService;
import top.iotequ.svasclient.db.pojo.SvasVeinInfo;
import java.util.*;

//  Mapper dao : SvasVeinInfoDao (指静脉信息数据访问接口)
public interface SvasVeinInfoDao  extends IDaoService<SvasVeinInfo> {
    int insert(SvasVeinInfo record);
    int insertBatchWithoutId(List<SvasVeinInfo> list);
    int insertBatchWithId(List<SvasVeinInfo> list);
    SvasVeinInfo select(Integer id);
    int delete(Integer id);
    int deleteBatch(String ids);
    List<SvasVeinInfo> selectByUserNo(String userNo);
    int deleteByUserNo(String userNo);
    int deleteBatchByUserNo(String userNos);
    int deleteList(List<SvasVeinInfo> list);
    int update(SvasVeinInfo record);
    int updateSelective(SvasVeinInfo record);
    int updateBy(@Param("record")SvasVeinInfo record,@Param("id")Integer id);
    //条件为所有非空字段 and，String采用like的查询模式，其他为=
    List<SvasVeinInfo> list(SvasVeinInfo record);
    //!!!  如 whereString="user_name like '王%'" ,orderString="born_date desc,employee_no asc"
    List<SvasVeinInfo> listBy(@Param("whereString")String whereString,@Param("orderString")String orderString);
    List<SvasVeinInfo> query(@Param("sqlString")String sqlString);
    @Update("${sqlString}")
    int execute(@Param("sqlString") String sqlString);
}
