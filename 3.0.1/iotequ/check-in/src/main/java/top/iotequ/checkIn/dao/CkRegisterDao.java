/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.checkIn.dao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import java.util.Map;
import top.iotequ.framework.service.IDaoService;
import top.iotequ.checkIn.pojo.CkRegister;
import java.util.*;

//  Mapper dao : CkRegisterDao (上机记录数据访问接口)
public interface CkRegisterDao  extends IDaoService<CkRegister> {
    int insert(CkRegister record);
    int insertBatchWithoutId(List<CkRegister> list);
    int insertBatchWithId(List<CkRegister> list);
    CkRegister select(String id);
    int delete(String id);
    int deleteBatch(String ids);
    List<CkRegister> selectByOrgCode(Integer orgCode);
    int deleteByOrgCode(Integer orgCode);
    int deleteBatchByOrgCode(String orgCodes);
    List<CkRegister> selectByUserNo(String userNo);
    int deleteByUserNo(String userNo);
    int deleteBatchByUserNo(String userNos);
    int deleteList(List<CkRegister> list);
    int update(CkRegister record);
    int updateSelective(Map<String,Object> record);
    int updateBy(@Param("record")CkRegister record,@Param("id")String id);
    //条件为所有非空字段 and，String采用like的查询模式，其他为=
    List<CkRegister> list(CkRegister record);
    //!!!  如 whereString="db_table_name.user_name like '王%'" ,orderString="db_table_name.born_date desc,db_table_name.code asc"
    List<CkRegister> listBy(@Param("whereString")String whereString,@Param("orderString")String orderString);
    List<CkRegister> query(@Param("sqlString")String sqlString);
    @Update("${sqlString}")
    int execute(@Param("sqlString") String sqlString);
}
