/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.pay.dao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import top.iotequ.framework.service.IDaoService;
import top.iotequ.pay.pojo.PayShop;
import java.util.*;

//  Mapper dao : PayShopDao (商店数据访问接口)
public interface PayShopDao  extends IDaoService<PayShop> {
    int insert(PayShop record);
    int insertBatchWithoutId(List<PayShop> list);
    int insertBatchWithId(List<PayShop> list);
    PayShop select(Integer id);
    int delete(Integer id);
    int deleteBatch(String ids);
    int deleteList(List<PayShop> list);
    int update(PayShop record);
    int updateSelective(PayShop record);
    int updateBy(@Param("record")PayShop record,@Param("id")Integer id);
    //条件为所有非空字段 and，String采用like的查询模式，其他为=
    List<PayShop> list(PayShop record);
    //!!!  如 whereString="user_name like '王%'" ,orderString="born_date desc,code asc"
    List<PayShop> listBy(@Param("whereString")String whereString,@Param("orderString")String orderString);
    List<PayShop> query(@Param("sqlString")String sqlString);
    @Update("${sqlString}")
    int execute(@Param("sqlString") String sqlString);
}
