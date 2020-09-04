/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.pay.dao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import top.iotequ.framework.service.IDaoService;
import top.iotequ.pay.pojo.PayOperator;
import java.util.*;

//  Mapper dao : PayOperatorDao (操作员数据访问接口)
public interface PayOperatorDao  extends IDaoService<PayOperator> {
    int insert(PayOperator record);
    int insertBatchWithoutId(List<PayOperator> list);
    int insertBatchWithId(List<PayOperator> list);
    PayOperator select(Integer id);
    int delete(Integer id);
    int deleteBatch(String ids);
    PayOperator selectByName(String name);
    int deleteByName(String name);
    int deleteBatchByName(String names);
    PayOperator selectByUserNo(String userNo);
    int deleteByUserNo(String userNo);
    int deleteBatchByUserNo(String userNos);
    int deleteList(List<PayOperator> list);
    int update(PayOperator record);
    int updateSelective(PayOperator record);
    int updateBy(@Param("record")PayOperator record,@Param("id")Integer id);
    //条件为所有非空字段 and，String采用like的查询模式，其他为=
    List<PayOperator> list(PayOperator record);
    //!!!  如 whereString="user_name like '王%'" ,orderString="born_date desc,code asc"
    List<PayOperator> listBy(@Param("whereString")String whereString,@Param("orderString")String orderString);
    List<PayOperator> query(@Param("sqlString")String sqlString);
    @Update("${sqlString}")
    int execute(@Param("sqlString") String sqlString);
}
