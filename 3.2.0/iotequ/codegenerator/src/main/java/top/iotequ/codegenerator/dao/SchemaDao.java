package top.iotequ.codegenerator.dao;

import top.iotequ.codegenerator.pojo.Column;
import top.iotequ.codegenerator.pojo.Table;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
//通用DAO方法，没有mapper文件，字段名采用camel进行映射mybatis.map-underscore-to-camel-case: true或采用数据字段名映射
public interface SchemaDao {
	@Select("SELECT * FROM `information_schema`.`TABLES` where TABLE_SCHEMA =#{db}  and TABLE_TYPE = 'BASE TABLE'")
	List<Table> tables(@Param("db") String db) ;

	@Select("SELECT * FROM `information_schema`.`TABLES` where TABLE_SCHEMA =#{db}  and TABLE_TYPE = 'BASE TABLE' and TABLE_NAME not in (select name from cg_table where name is not null)")
	List<Table> notUsedTables(@Param("db") String db) ;

	@Select("SELECT * FROM `information_schema`.`columns` where TABLE_SCHEMA =#{db}  and TABLE_NAME = #{tab}")
	List<Column> columns(@Param("db") String db,@Param("tab") String tab) ;

}