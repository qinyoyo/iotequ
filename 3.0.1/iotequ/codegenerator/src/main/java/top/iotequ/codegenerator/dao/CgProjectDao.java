/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.codegenerator.dao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import top.iotequ.framework.service.IDaoService;
import top.iotequ.codegenerator.pojo.CgProject;
import java.util.*;

//  Mapper dao : CgProjectDao (项目数据访问接口)
public interface CgProjectDao  extends IDaoService<CgProject> {
    int insert(CgProject record);
    int insertBatchWithId(List<CgProject> list);
    CgProject select(String id);
    int delete(String id);
    int deleteBatch(String ids);
    CgProject selectByCodeCreator(@Param("code")String code , @Param("creator")String creator);
    int deleteByCodeCreator(@Param("code")String code , @Param("creator")String creator);
    int deleteList(List<CgProject> list);
    int update(CgProject record);
    int updateSelective(CgProject record);
    int updateBy(@Param("record")CgProject record,@Param("id")String id);
    //条件为所有非空字段 and，String采用like的查询模式，其他为=
    List<CgProject> list(CgProject record);
    //!!!  如 whereString="user_name like '王%'" ,orderString="born_date desc,code asc"
    List<CgProject> listBy(@Param("whereString")String whereString,@Param("orderString")String orderString);
    List<CgProject> query(@Param("sqlString")String sqlString);
    @Update("${sqlString}")
    int execute(@Param("sqlString") String sqlString);
}
