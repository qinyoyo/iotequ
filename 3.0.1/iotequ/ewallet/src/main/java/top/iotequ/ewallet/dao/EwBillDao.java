/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.ewallet.dao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import top.iotequ.framework.service.IDaoService;
import top.iotequ.ewallet.pojo.EwBill;
import java.util.*;

//  Mapper dao : EwBillDao (消费明细数据访问接口)
public interface EwBillDao  extends IDaoService<EwBill> {
    int insert(EwBill record);
    int insertBatchWithId(List<EwBill> list);
    EwBill select(String no);
    int delete(String no);
    int deleteBatch(String nos);
    List<EwBill> selectByIsCharge(Boolean isCharge);
    int deleteByIsCharge(Boolean isCharge);
    int deleteBatchByIsCharge(String isCharges);
    List<EwBill> selectByUserNo(String userNo);
    int deleteByUserNo(String userNo);
    int deleteBatchByUserNo(String userNos);
    List<EwBill> selectByDt(Date dt);
    int deleteByDt(Date dt);
    int deleteBatchByDt(String dts);
    List<EwBill> selectByCostType(Integer costType);
    int deleteByCostType(Integer costType);
    int deleteBatchByCostType(String costTypes);
    List<EwBill> selectByProjectId(Integer projectId);
    int deleteByProjectId(Integer projectId);
    int deleteBatchByProjectId(String projectIds);
    EwBill selectByTradeNo(String tradeNo);
    int deleteByTradeNo(String tradeNo);
    int deleteBatchByTradeNo(String tradeNos);
    List<EwBill> selectByOperatorNo(String operatorNo);
    int deleteByOperatorNo(String operatorNo);
    int deleteBatchByOperatorNo(String operatorNos);
    List<EwBill> selectByLoginId(Integer loginId);
    int deleteByLoginId(Integer loginId);
    int deleteBatchByLoginId(String loginIds);
    List<EwBill> selectByShopId(String shopId);
    int deleteByShopId(String shopId);
    int deleteBatchByShopId(String shopIds);
    int deleteList(List<EwBill> list);
    int update(EwBill record);
    int updateSelective(EwBill record);
    int updateBy(@Param("record")EwBill record,@Param("no")String id);
    //条件为所有非空字段 and，String采用like的查询模式，其他为=
    List<EwBill> list(EwBill record);
    //!!!  如 whereString="db_table_name.user_name like '王%'" ,orderString="db_table_name.born_date desc,db_table_name.code asc"
    List<EwBill> listBy(@Param("whereString")String whereString,@Param("orderString")String orderString);
    List<EwBill> query(@Param("sqlString")String sqlString);
    @Update("${sqlString}")
    int execute(@Param("sqlString") String sqlString);
}
