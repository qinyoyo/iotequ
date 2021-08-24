/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.checkIn.dao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import java.util.Map;
import top.iotequ.framework.service.IDaoService;
import top.iotequ.checkIn.pojo.CkSignIn;
import java.util.*;

//  Mapper dao : CkSignInDao (认证记录数据访问接口)
public interface CkSignInDao  extends IDaoService<CkSignIn> {
    int insert(CkSignIn record);
    int insertBatchWithoutId(List<CkSignIn> list);
    int insertBatchWithId(List<CkSignIn> list);
    CkSignIn select(String id);
    int delete(String id);
    int deleteBatch(String ids);
    List<CkSignIn> selectByOrgCode(Integer orgCode);
    int deleteByOrgCode(Integer orgCode);
    int deleteBatchByOrgCode(String orgCodes);
    List<CkSignIn> selectByRecTime(Date recTime);
    int deleteByRecTime(Date recTime);
    int deleteBatchByRecTime(String recTimes);
    List<CkSignIn> selectByUserNo(String userNo);
    int deleteByUserNo(String userNo);
    int deleteBatchByUserNo(String userNos);
    int deleteList(List<CkSignIn> list);
    int update(CkSignIn record);
    int updateSelective(Map<String,Object> record);
    int updateBy(@Param("record")CkSignIn record,@Param("id")String id);
    //条件为所有非空字段 and，String采用like的查询模式，其他为=
    List<CkSignIn> list(CkSignIn record);
    //!!!  如 whereString="db_table_name.user_name like '王%'" ,orderString="db_table_name.born_date desc,db_table_name.code asc"
    List<CkSignIn> listBy(@Param("whereString")String whereString,@Param("orderString")String orderString);
    List<CkSignIn> query(@Param("sqlString")String sqlString);
    @Update("${sqlString}")
    int execute(@Param("sqlString") String sqlString);
}
