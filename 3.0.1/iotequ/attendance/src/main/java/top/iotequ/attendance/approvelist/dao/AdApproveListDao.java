/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.attendance.approvelist.dao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import java.util.Map;
import top.iotequ.framework.service.IDaoService;
import top.iotequ.attendance.approvelist.pojo.AdApproveList;
import java.util.*;

//  Mapper dao : AdApproveListDao (审核信息表数据访问接口)
public interface AdApproveListDao  extends IDaoService<AdApproveList> {
    int insert(AdApproveList record);
    int insertBatchWithoutId(List<AdApproveList> list);
    int insertBatchWithId(List<AdApproveList> list);
    AdApproveList select(Integer id);
    int delete(Integer id);
    int deleteBatch(String ids);
    int deleteList(List<AdApproveList> list);
    int update(AdApproveList record);
    int updateSelective(Map<String,Object> record);
    int updateBy(@Param("record")AdApproveList record,@Param("id")Integer id);
    //条件为所有非空字段 and，String采用like的查询模式，其他为=
    List<AdApproveList> list(AdApproveList record);
    //!!!  如 whereString="db_table_name.user_name like '王%'" ,orderString="db_table_name.born_date desc,db_table_name.code asc"
    List<AdApproveList> listBy(@Param("whereString")String whereString,@Param("orderString")String orderString);
    List<AdApproveList> query(@Param("sqlString")String sqlString);
    @Update("${sqlString}")
    int execute(@Param("sqlString") String sqlString);
}
