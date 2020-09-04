/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.project.product.dao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import top.iotequ.framework.service.IDaoService;
import top.iotequ.project.product.pojo.PmProject;
import java.util.*;

//  Mapper dao : PmProjectDao (项目及产品列表数据访问接口)
public interface PmProjectDao  extends IDaoService<PmProject> {
    int insert(PmProject record);
    int insertBatchWithoutId(List<PmProject> list);
    int insertBatchWithId(List<PmProject> list);
    PmProject select(String id);
    int delete(String id);
    int deleteBatch(String ids);
    PmProject selectByName(String name);
    int deleteByName(String name);
    int deleteBatchByName(String names);
    PmProject selectByCode(String code);
    int deleteByCode(String code);
    int deleteBatchByCode(String codes);
    int deleteList(List<PmProject> list);
    int update(PmProject record);
    int updateSelective(PmProject record);
    int updateBy(@Param("record")PmProject record,@Param("id")String id);
    //条件为所有非空字段 and，String采用like的查询模式，其他为=
    List<PmProject> list(PmProject record);
    //!!!  如 whereString="db_table_name.user_name like '王%'" ,orderString="db_table_name.born_date desc,db_table_name.code asc"
    List<PmProject> listBy(@Param("whereString")String whereString,@Param("orderString")String orderString);
    List<PmProject> query(@Param("sqlString")String sqlString);
    @Update("${sqlString}")
    int execute(@Param("sqlString") String sqlString);
}
