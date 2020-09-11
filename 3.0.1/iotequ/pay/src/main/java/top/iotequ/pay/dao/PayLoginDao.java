/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.pay.dao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import java.util.Map;
import top.iotequ.framework.service.IDaoService;
import top.iotequ.pay.pojo.PayLogin;
import java.util.*;

//  Mapper dao : PayLoginDao (签到日志数据访问接口)
public interface PayLoginDao  extends IDaoService<PayLogin> {
    int insert(PayLogin record);
    int insertBatchWithoutId(List<PayLogin> list);
    int insertBatchWithId(List<PayLogin> list);
    PayLogin select(Integer id);
    int delete(Integer id);
    int deleteBatch(String ids);
    int deleteList(List<PayLogin> list);
    int update(PayLogin record);
    int updateSelective(Map<String,Object> record);
    int updateBy(@Param("record")PayLogin record,@Param("id")Integer id);
    //条件为所有非空字段 and，String采用like的查询模式，其他为=
    List<PayLogin> list(PayLogin record);
    //!!!  如 whereString="db_table_name.user_name like '王%'" ,orderString="db_table_name.born_date desc,db_table_name.code asc"
    List<PayLogin> listBy(@Param("whereString")String whereString,@Param("orderString")String orderString);
    List<PayLogin> query(@Param("sqlString")String sqlString);
    @Update("${sqlString}")
    int execute(@Param("sqlString") String sqlString);
}
