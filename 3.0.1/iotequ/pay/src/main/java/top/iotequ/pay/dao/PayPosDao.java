/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.pay.dao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import top.iotequ.framework.service.IDaoService;
import top.iotequ.pay.pojo.PayPos;
import java.util.*;

//  Mapper dao : PayPosDao (交易终端数据访问接口)
public interface PayPosDao  extends IDaoService<PayPos> {
    int insert(PayPos record);
    int insertBatchWithoutId(List<PayPos> list);
    int insertBatchWithId(List<PayPos> list);
    PayPos select(Integer id);
    int delete(Integer id);
    int deleteBatch(String ids);
    PayPos selectByNo(String no);
    int deleteByNo(String no);
    int deleteBatchByNo(String nos);
    int deleteList(List<PayPos> list);
    int update(PayPos record);
    int updateSelective(PayPos record);
    int updateBy(@Param("record")PayPos record,@Param("id")Integer id);
    //条件为所有非空字段 and，String采用like的查询模式，其他为=
    List<PayPos> list(PayPos record);
    //!!!  如 whereString="user_name like '王%'" ,orderString="born_date desc,code asc"
    List<PayPos> listBy(@Param("whereString")String whereString,@Param("orderString")String orderString);
    List<PayPos> query(@Param("sqlString")String sqlString);
    @Update("${sqlString}")
    int execute(@Param("sqlString") String sqlString);
}
