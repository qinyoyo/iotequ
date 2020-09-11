/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.pay.dao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import java.util.Map;
import top.iotequ.framework.service.IDaoService;
import top.iotequ.pay.pojo.PayCorporation;
import java.util.*;

//  Mapper dao : PayCorporationDao (运营公司数据访问接口)
public interface PayCorporationDao  extends IDaoService<PayCorporation> {
    int insert(PayCorporation record);
    int insertBatchWithoutId(List<PayCorporation> list);
    int insertBatchWithId(List<PayCorporation> list);
    PayCorporation select(Integer id);
    int delete(Integer id);
    int deleteBatch(String ids);
    int deleteList(List<PayCorporation> list);
    List<PayCorporation> selectTree(@Param("id")Integer id);  // 选全部参数为null
    int update(PayCorporation record);
    int updateSelective(Map<String,Object> record);
    int updateBy(@Param("record")PayCorporation record,@Param("id")Integer id);
    //条件为所有非空字段 and，String采用like的查询模式，其他为=
    List<PayCorporation> list(PayCorporation record);
    //!!!  如 whereString="user_name like '王%'" ,orderString="born_date desc,code asc"
    List<PayCorporation> listBy(@Param("whereString")String whereString,@Param("orderString")String orderString);
    List<PayCorporation> query(@Param("sqlString")String sqlString);
    @Update("${sqlString}")
    int execute(@Param("sqlString") String sqlString);
}
