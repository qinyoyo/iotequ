package top.iotequ.codegenerator.util;

import top.iotequ.codegenerator.pojo.CgField;
import top.iotequ.codegenerator.pojo.CgTable;
import top.iotequ.codegenerator.service.impl.DtoService;
import top.iotequ.codegenerator.service.impl.GenService;
import top.iotequ.framework.exception.IotequThrowable;
import top.iotequ.framework.util.StringUtil;
import top.iotequ.framework.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MapperUtil {
    public static List<CgField> tableAndDictListFields(List<CgField> tabFields, List<CgField> joinFields) {
        List<CgField> allFields = new ArrayList<CgField>();
        if (!Util.isEmpty(tabFields)) allFields.addAll(tabFields);
        if (!Util.isEmpty(joinFields)) allFields.addAll(joinFields.stream().filter(f->!Util.isEmpty(f.getName()) && "dict_list".equals(f.getShowType())).collect(Collectors.toList()));
        return allFields;
    }
    public static void mapper(String generatorPath, CgTable table, List<CgField> tabFields, List<CgField> joinFields, List<CgField> calFields) throws CgException {
        assert (table != null && tabFields.size() > 0);
        if (Util.isEmpty(table.getName())) return;
        String entity = table.getEntity();
        String pojoClass = NameUtil.basePackage(table) + "." + GenService.POJO + "." + entity;
        String daoClass = NameUtil.basePackage(table) + "." + GenService.DAO + "." + entity + StringUtil.firstLetterUpper(GenService.DAO);
        String filePath = NameUtil.baseResourcePath(generatorPath, table, "mybatis",false) + "/" + entity + StringUtil.firstLetterUpper(GenService.DAO) + ".xml";
        StringBuilder sb = new StringBuilder();
        DtoService dtoService = Util.getBean(DtoService.class);

        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n");
        sb.append("<mapper namespace=\"" + daoClass + "\">\n"); // Dao 名

        /***************基本字段映射表********************/
        sb.append("  <resultMap id=\"BaseResultMap\" type=\"" + pojoClass + "\">\n"); // Entity名
        String tablePreFix = (joinFields.isEmpty() ? "" : table.getName().trim() + ".");
        StringBuilder[] selectFields = {null, null, null}, realSelectFields = {null, null, null};
        selectFields[ScriptUtil.MYSQL - 1] = new StringBuilder();
        String DH = "";
        CgField pk = GenService.priKey(table,tabFields,joinFields);
        if (pk != null) {
            sb.append("    <id column=\"" + pk.getName() + "\" jdbcType=\"" + TypeUtil.jdbcType(pk) + "\" property=\""
                    + pk.getEntityName() + "\" />\n");
            if ("right_join".equals(pk.getShowType())) { // 主键右连接，使用连接的值而非本值，避免主键为空
                realSelectFields[ScriptUtil.MYSQL - 1] = new StringBuilder();
                realSelectFields[ScriptUtil.MYSQL - 1].append(tablePreFix + NameUtil.fieldName(pk));
                selectFields[ScriptUtil.MYSQL - 1].append(JoinUtil.joinOnFullColumnName(table, pk) + " AS " + NameUtil.fieldName(pk));
                DH = ",";
            } else {
                selectFields[ScriptUtil.MYSQL - 1].append(tablePreFix + NameUtil.fieldName(pk));
                if (selectFields[ScriptUtil.SQLSERVER - 1] != null)
                    selectFields[ScriptUtil.SQLSERVER - 1].append(tablePreFix + NameUtil.fieldName(pk));
                if (selectFields[ScriptUtil.ORACLE - 1] != null) selectFields[ScriptUtil.ORACLE - 1].append(tablePreFix + NameUtil.fieldName(pk));
                if (pk.getShowType().equals("dict_list")) {
                    sb.append("    <result column=\"dict_list_" + pk.getName() + "\" jdbcType=\"VARCHAR\" property=\"dictList" + StringUtil.firstLetterUpper(pk.getEntityName()) + "\" />\n");
                    selectFields[ScriptUtil.SQLSERVER - 1] = appendDictList(ScriptUtil.SQLSERVER, table.getName().trim(), pk, selectFields[ScriptUtil.SQLSERVER - 1], selectFields[ScriptUtil.MYSQL - 1]);
                    selectFields[ScriptUtil.ORACLE - 1] = appendDictList(ScriptUtil.ORACLE, table.getName().trim(), pk, selectFields[ScriptUtil.ORACLE - 1], selectFields[ScriptUtil.MYSQL - 1]);
                    appendDictList(ScriptUtil.MYSQL, table.getName().trim(), pk, selectFields[ScriptUtil.MYSQL - 1], selectFields[ScriptUtil.MYSQL - 1]);
                }
                DH = ",";
            }
        }

        List<CgField> tableAndDict = tableAndDictListFields(tabFields,joinFields);
        for (CgField f : tableAndDict) {
            if (pk == null || !pk.getName().equals(f.getName())) {
                sb.append("    <result column=\"" + f.getName() + "\" jdbcType=\"" + TypeUtil.jdbcType(f) + "\" property=\"" + f.getEntityName() + "\" />\n");
                selectFields[ScriptUtil.MYSQL - 1].append(DH).append(tablePreFix + NameUtil.fieldName(f));
                if (selectFields[ScriptUtil.SQLSERVER - 1] != null)
                    selectFields[ScriptUtil.SQLSERVER - 1].append(DH).append(tablePreFix + NameUtil.fieldName(f));
                if (selectFields[ScriptUtil.ORACLE - 1] != null)
                    selectFields[ScriptUtil.ORACLE - 1].append(DH).append(tablePreFix + NameUtil.fieldName(f));
                if (f.getShowType().equals("dict_list")) {
                    sb.append("    <result column=\"dict_list_" + f.getName() + "\" jdbcType=\"VARCHAR\" property=\"dictList" + StringUtil.firstLetterUpper(f.getEntityName()) + "\" />\n");
                    selectFields[ScriptUtil.SQLSERVER - 1] = appendDictList(ScriptUtil.SQLSERVER, table.getName().trim(), f, selectFields[ScriptUtil.SQLSERVER - 1], selectFields[ScriptUtil.MYSQL - 1]);
                    selectFields[ScriptUtil.ORACLE - 1] = appendDictList(ScriptUtil.ORACLE, table.getName().trim(), f, selectFields[ScriptUtil.ORACLE - 1], selectFields[ScriptUtil.MYSQL - 1]);
                    appendDictList(ScriptUtil.MYSQL, table.getName().trim(), f, selectFields[ScriptUtil.MYSQL - 1], selectFields[ScriptUtil.MYSQL - 1]);
                }
                if (realSelectFields[ScriptUtil.MYSQL - 1] != null) {
                    realSelectFields[ScriptUtil.MYSQL - 1].append(DH).append(tablePreFix + NameUtil.fieldName(f));
                    if (realSelectFields[ScriptUtil.SQLSERVER - 1] != null)
                        realSelectFields[ScriptUtil.SQLSERVER - 1].append(DH).append(tablePreFix + NameUtil.fieldName(f));
                    if (realSelectFields[ScriptUtil.ORACLE - 1] != null)
                        realSelectFields[ScriptUtil.ORACLE - 1].append(DH).append(tablePreFix + NameUtil.fieldName(f));
                    if (f.getShowType().equals("dict_list")) {
                        appendDictList(ScriptUtil.MYSQL, table.getName().trim(), f, realSelectFields[ScriptUtil.MYSQL - 1], realSelectFields[ScriptUtil.MYSQL - 1]);
                        realSelectFields[ScriptUtil.SQLSERVER - 1] = appendDictList(ScriptUtil.SQLSERVER, table.getName().trim(), f, realSelectFields[ScriptUtil.SQLSERVER - 1], realSelectFields[ScriptUtil.MYSQL - 1]);
                        realSelectFields[ScriptUtil.ORACLE - 1] = appendDictList(ScriptUtil.ORACLE, table.getName().trim(), f, realSelectFields[ScriptUtil.ORACLE - 1], realSelectFields[ScriptUtil.MYSQL - 1]);
                    }
                }
                if (DH.isEmpty()) DH = ",";
            }
        }
        List<CgField> joinWithoutDictList = joinFields.stream().filter(f->!"dict_list".equals(f.getShowType())).collect(Collectors.toList());
        for (CgField f : joinWithoutDictList) {
            CgTable jtab = JoinUtil.joinTable(f);
            if (jtab==null) throw new CgException("join 表不存在 :"+f.getDictTable());
            if (pk == null || !pk.getName().equals(f.getName())) {
                sb.append("    <result column=\"" + f.getName() + "\" jdbcType=\"" + TypeUtil.jdbcType(f) + "\" property=\"" + f.getEntityName() + "\" />\n");
                selectFields[ScriptUtil.MYSQL - 1].append(DH).append("right_join".equals(f.getShowType()) ? JoinUtil.joinOnFullColumnName(table, f) + " AS " + NameUtil.fieldName(f) : tablePreFix + NameUtil.fieldName(f));     // 右连接特殊处理
                if (selectFields[ScriptUtil.SQLSERVER - 1] != null)
                    selectFields[ScriptUtil.SQLSERVER - 1].append(DH).append("right_join".equals(f.getShowType()) ? JoinUtil.joinOnFullColumnName(table, f) + " AS " + NameUtil.fieldName(f) : tablePreFix + NameUtil.fieldName(f));
                ;
                if (selectFields[ScriptUtil.ORACLE - 1] != null)
                    selectFields[ScriptUtil.ORACLE - 1].append(DH).append("right_join".equals(f.getShowType()) ? JoinUtil.joinOnFullColumnName(table, f) + " AS " + NameUtil.fieldName(f) : tablePreFix + NameUtil.fieldName(f));
                if (realSelectFields[ScriptUtil.MYSQL - 1] != null) {
                    realSelectFields[ScriptUtil.MYSQL - 1].append(DH).append(tablePreFix + NameUtil.fieldName(f));
                    if (realSelectFields[ScriptUtil.SQLSERVER - 1] != null)
                        realSelectFields[ScriptUtil.SQLSERVER - 1].append(DH).append(tablePreFix + NameUtil.fieldName(f));
                    if (realSelectFields[ScriptUtil.ORACLE - 1] != null)
                        realSelectFields[ScriptUtil.ORACLE - 1].append(DH).append(tablePreFix + NameUtil.fieldName(f));
                }
                if (DH.isEmpty()) DH = ",";
            }
            if (f != null) {
                List<CgField> splitedFields = JoinUtil.splitedJoinFields(table, f, tabFields, joinFields, calFields);
                for (CgField jf : splitedFields) {
                    String dt = TypeUtil.jdbcType(jf.getType());
                    sb.append("    <result column=\"" + jf.getName() + "\" jdbcType=\"" + dt + "\" property=\"" + jf.getEntityName() + "\" />\n");
                    selectFields[ScriptUtil.MYSQL - 1].append(DH).append(JoinUtil.joinFullColumnName(jtab, f, jf) + " AS " + jf.getName());
                    if (selectFields[ScriptUtil.SQLSERVER - 1] != null)
                        selectFields[ScriptUtil.SQLSERVER - 1].append(DH).append(JoinUtil.joinFullColumnName(jtab, f, jf) + " AS " + jf.getName());
                    if (selectFields[ScriptUtil.ORACLE - 1] != null)
                        selectFields[ScriptUtil.ORACLE - 1].append(DH).append(JoinUtil.joinFullColumnName(jtab, f, jf) + " AS " + jf.getName());
                    if (realSelectFields[ScriptUtil.MYSQL - 1] != null) {
                        realSelectFields[ScriptUtil.MYSQL - 1].append(DH).append(JoinUtil.joinFullColumnName(jtab, f, jf) + " AS " + jf.getName());
                        if (realSelectFields[ScriptUtil.SQLSERVER - 1] != null)
                            realSelectFields[ScriptUtil.SQLSERVER - 1].append(DH).append(JoinUtil.joinFullColumnName(jtab, f, jf) + " AS " + jf.getName());
                        if (realSelectFields[ScriptUtil.ORACLE - 1] != null)
                            realSelectFields[ScriptUtil.ORACLE - 1].append(DH).append(JoinUtil.joinFullColumnName(jtab, f, jf) + " AS " + jf.getName());
                    }
                    if (DH.isEmpty()) DH = ",";
                }
            }
        }
        for (CgField f : calFields) {
            if (f.getShowType().endsWith("dict_list"))
                throw new CgException(IotequThrowable.ERROR,"计算字段不允许使用dict_list显示类型(" + table.getCode() + ":" + f.getEntityName() + ")");
            if (Util.isEmpty(f.getName())) continue;     //  跳过非数据库字段
            String[] ff = f.getName().split(":");   //  计算字段形式为 : 名称:计算表达式
            selectFields[ScriptUtil.MYSQL - 1].append(DH).append(f.getName().substring(f.getName().indexOf(":") + 1) + " as " + ff[0]);
            if (selectFields[ScriptUtil.SQLSERVER - 1] != null)
                selectFields[ScriptUtil.SQLSERVER - 1].append(DH).append(f.getName().substring(f.getName().indexOf(":") + 1) + " as " + ff[0]);
            if (selectFields[ScriptUtil.ORACLE - 1] != null)
                selectFields[ScriptUtil.ORACLE - 1].append(DH).append(f.getName().substring(f.getName().indexOf(":") + 1) + " as " + ff[0]);
            if (realSelectFields[ScriptUtil.MYSQL - 1] != null) {
                realSelectFields[ScriptUtil.MYSQL - 1].append(DH).append(f.getName().substring(f.getName().indexOf(":") + 1) + " as " + ff[0]);
                if (realSelectFields[ScriptUtil.SQLSERVER - 1] != null)
                    realSelectFields[ScriptUtil.SQLSERVER - 1].append(f.getName().substring(f.getName().indexOf(":") + 1) + " as " + ff[0]);
                if (realSelectFields[ScriptUtil.ORACLE - 1] != null)
                    realSelectFields[ScriptUtil.ORACLE - 1].append(f.getName().substring(f.getName().indexOf(":") + 1) + " as " + ff[0]);
            }
            if (DH.isEmpty()) DH = ",";
            sb.append("    <result column=\"" + ff[0] + "\" jdbcType=\"" + TypeUtil.jdbcType(f)
                    + "\" property=\"" + f.getEntityName() + "\" />\n");
        }

        sb.append("  </resultMap>\n");

        StringBuilder joinCondition = new StringBuilder(), realJoinCondition = (realSelectFields[ScriptUtil.MYSQL - 1] == null ? null : new StringBuilder());
        for (CgField f : joinWithoutDictList) {
            CgTable jtab = JoinUtil.joinTable(f);
            if (jtab != null) {
                String jc = JoinUtil.convertedJoinCondition(table,jtab,f,tabFields,joinFields,calFields);
                joinCondition.append(" ").append(jc);
                if (realJoinCondition != null)
                    realJoinCondition.append(" ").append(jc);
            }
        }

        sb.append("  <sql id = \"selectSql\">\n");
        sb.append("     ").append("SELECT ").append(selectFields[ScriptUtil.MYSQL - 1]).append(" FROM ").append(table.getName()).append(joinCondition).append("\n");
        sb.append("  </sql>\n");
        if (selectFields[ScriptUtil.SQLSERVER - 1] != null) {
            sb.append("  <sql id = \"selectSql\" databaseId=\"SQLServer\">\n");
            sb.append("     ").append("SELECT ").append(selectFields[ScriptUtil.SQLSERVER - 1]).append(" FROM ").append(table.getName()).append(joinCondition).append("\n");
            sb.append("  </sql>\n");
        }
        if (selectFields[ScriptUtil.ORACLE - 1] != null) {
            sb.append("  <sql id = \"selectSql\" databaseId=\"Oracle\">\n");
            sb.append("     ").append("SELECT ").append(selectFields[ScriptUtil.ORACLE - 1]).append(" FROM ").append(table.getName()).append(joinCondition).append("\n");
            sb.append("  </sql>\n");
        }
        String sql = "<include refid=\"selectSql\" />", realSql = null;

        if (realSelectFields[ScriptUtil.MYSQL - 1] != null) {
            sb.append("  <sql id = \"realSelectSql\">\n");
            sb.append("     ").append("SELECT ").append(realSelectFields[ScriptUtil.MYSQL - 1]).append(" FROM ").append(table.getName()).append(realJoinCondition).append("\n");
            sb.append("  </sql>\n");
            if (realSelectFields[ScriptUtil.SQLSERVER - 1] != null) {
                sb.append("  <sql id = \"realSelectSql\" databaseId=\"SQLServer\">\n");
                sb.append("     ").append("SELECT ").append(realSelectFields[ScriptUtil.SQLSERVER - 1]).append(" FROM ").append(table.getName()).append(realJoinCondition).append("\n");
                sb.append("  </sql>\n");
            }
            if (realSelectFields[ScriptUtil.ORACLE - 1] != null) {
                sb.append("  <sql id = \"realSelectSql\" databaseId=\"Oracle\">\n");
                sb.append("     ").append("SELECT ").append(realSelectFields[ScriptUtil.ORACLE - 1]).append(" FROM ").append(table.getName()).append(realJoinCondition).append("\n");
                sb.append("  </sql>\n");
            }
            realSql = "<include refid=\"realSelectSql\" />";
        }

        /*************** 树结构字段映射表********************/
        CgField pf = Util.getBean(GenService.class).parentEntity(table,tabFields,joinFields);
        if (pf != null && pk != null) {
            sb.append("  <resultMap id=\"TreeResultMap\" type=\"" + pojoClass + "\">\n"); // Entity名
            sb.append("    <id column=\"" + pk.getName() + "\" jdbcType=\"" + TypeUtil.jdbcType(pk) + "\" property=\""
                    + pk.getEntityName() + "\" />\n");

            for (CgField f : tableAndDict) {
                if (pk == null || !pk.getName().equals(f.getName())) {
                    sb.append("    <result column=\"" + f.getName() + "\" jdbcType=\"" + TypeUtil.jdbcType(f) + "\" property=\"" + f.getEntityName() + "\" />\n");
                }
            }
            for (CgField f : joinWithoutDictList) {
                if (pk == null || !pk.getName().equals(f.getName())) {
                    sb.append("    <result column=\"" + f.getName() + "\" jdbcType=\"" + TypeUtil.jdbcType(f) + "\" property=\"" + f.getEntityName() + "\" />\n");
                }
                CgTable jtab = JoinUtil.joinTable(f);
                if (jtab != null) {
                    List<CgField> splitedFields = JoinUtil.splitedJoinFields(table, f, tabFields, joinFields, calFields);
                    for (CgField jf : splitedFields) {
                        sb.append("    <result column=\"" + jf.getName() + "\" jdbcType=\"" + TypeUtil.jdbcType(jf) + "\" property=\"" + jf.getEntityName() + "\" />\n");
                    }
                }
            }
            for (CgField f : calFields) {
                if (Util.isEmpty(f.getName())) continue;     //  跳过非数据库字段
                String[] ff = f.getName().split(":");   //  计算字段形式为 : 名称:计算表达式
                sb.append("    <result column=\"" + ff[0] + "\" jdbcType=\"" + TypeUtil.jdbcType(f)
                        + "\" property=\"" + f.getEntityName() + "\" />\n");
            }

            sb.append("    <collection column=\"" + pk.getName() + "\" property=\"children\" "
                    + "ofType=\"" + pojoClass + "\" javaType=\"java.util.ArrayList\" select=\"selectChildrenById\"/>\n");
            sb.append("  </resultMap>\n");
            sb.append("  <select id=\"selectChildrenById\" resultMap=\"TreeResultMap\">\n" +
                      "		" + sql + "  WHERE  " + tablePreFix + NameUtil.fieldName(pf) + " = #{id}\n" +
                      "  </select>\n");
            sb.append("  <select id=\"selectTree\" parameterType=\"" + TypeUtil.fullJavaType(pf) + "\" resultMap=\"TreeResultMap\">\n" +
                    "		" + sql + " where \n" +
                    "		<choose>\n" +
                    "			<when test=\"id == null\">" + tablePreFix + NameUtil.fieldName(pf) + " = " + (TypeUtil.javaType(pf).equals("Integer") ? "0" : "''") + " or " + tablePreFix + NameUtil.fieldName(pf) + " is null </when>\n" +
                    "			<otherwise>" + tablePreFix + NameUtil.fieldName(pf) + " = " + "#{id,jdbcType=" + TypeUtil.jdbcType(pf) + "} </otherwise>\n" +
                    "		</choose>\n" +
                    "	</select>\n");
        }

        mapperInsert(table,tabFields,joinFields,pojoClass, pk, sb);
        mapperInsertWithoutId(table,tabFields,joinFields,pojoClass, pk, sb);
        mapperInsertWithId(table,tabFields,joinFields,pojoClass, pk, sb);

        List<CgField> dbFields = GenService.databaseFields(tabFields,joinFields);
        /********************  搜索所有的键值 *****************/
        for (CgField key : dbFields) {     //  // 1 自动增长int 2 uuid 3 普通主键 4 唯一索引 5 多记录索引
            if (Util.isEmpty(key.getName()) || key.getName().indexOf(":") >= 0) continue;     //  跳过非数据库字段
            if (Util.isEmpty(key.getKeyType()) || key.getKeyType().equals("0") || key.getKeyType().equals("11") || key.getKeyType().equals("12") || key.getKeyType().equals("13"))
                continue;
            // select
            String idTail = key.getKeyType().equals("1") || key.getKeyType().equals("2") || key.getKeyType().equals("3") ? "" : "By" + StringUtil.firstLetterUpper(key.getEntityName());
            sb.append("  <select id=\"select" + idTail + "\" parameterType=\"" + TypeUtil.fullJavaType(key)
                    + "\" resultMap=\"BaseResultMap\">\n" + "    " + sql + " where "
                    + ("right_join".equals(key.getShowType()) ? JoinUtil.joinOnFullColumnName(table, key) : tablePreFix + NameUtil.fieldName(key))      // 右连接使用右边条件
                    + " = " + "#{" + key.getEntityName() + ",jdbcType=" + TypeUtil.jdbcType(key) + "}\n" + "  </select>\n");
            if (key == pk && realSql != null) {
                sb.append("  <select id=\"realSelect" + "\" parameterType=\"" + TypeUtil.fullJavaType(key)
                        + "\" resultMap=\"BaseResultMap\">\n" + "    " + realSql + " where "
                        + tablePreFix + NameUtil.fieldName(key)
                        + " = " + "#{" + key.getEntityName() + ",jdbcType=" + TypeUtil.jdbcType(key) + "}\n" + "  </select>\n");
            }
            // delete
            sb.append("  <delete id=\"delete" + idTail + "\" parameterType=\"" + TypeUtil.fullJavaType(key) + "\">\n");
            sb.append("    delete from " + table.getName() + " where " + key.getName() + " = " + "#{" + key.getEntityName() + ",jdbcType="
                    + TypeUtil.jdbcType(key) + "}\n" + "  </delete>\n");
            // deleteBatch
            sb.append("  <delete id=\"deleteBatch" + idTail + "\" parameterType=\"java.lang.String\">\n");
            if (TypeUtil.isNumbericType(key)) {
                sb.append("    delete from " + table.getName() + " where " + key.getName() + " in (${_parameter})\n"
                        + "  </delete>\n");
            } else {
                sb.append("    delete from " + table.getName() + " where " + key.getName() + " in "
                        + "    <foreach item=\"item\" index=\"index\" collection=\"_parameter.split(',')\" open=\"(\" separator=\",\" close=\")\">\n" +
                        "      #{item}\n" +
                        "    </foreach>\n" +
                        "  </delete>\n");

            }
        }
        for (int i = 11; i <= 13; i++) {
            List<CgField> uf = GenService.unionUniqueIndexFields(tabFields,joinFields,ScriptUtil.MYSQL, String.valueOf(i));
            if (uf != null && uf.size() > 0) {
                String idTail = "By";
                for (int index = 0; index < uf.size(); index++) {
                    idTail += StringUtil.firstLetterUpper(uf.get(index).getEntityName());
                }
                // select
                sb.append("  <select id=\"select" + idTail + "\" parameterType=\"map"
                        + "\" resultMap=\"BaseResultMap\">\n" + "    " + sql + " where ");
                String and = "";
                String where = "";
                for (CgField key : uf) {
                    where += (and + ("right_join".equals(key.getShowType()) ? JoinUtil.joinOnFullColumnName(table, key) : tablePreFix + NameUtil.fieldName(key)) + " = " + "#{" + key.getEntityName() + "}");     // 右连接使用右边条件
                    and = " AND ";
                }
                sb.append(where);
                sb.append("\n" + "  </select>\n");
                // delete
                sb.append("  <delete id=\"delete" + idTail + "\" parameterType=\"map" + "\" >\n");
                sb.append("    delete from " + table.getName() + " where ");
                sb.append(where);
                sb.append("\n" + "  </delete>\n");
            }
        }
        if (pk != null) {
            // delete list
            sb.append("  <delete id=\"deleteList\" parameterType=\"java.util.List\">\n");
            sb.append("    delete from " + table.getName() + " where " + pk.getName() + " in\n");
            sb.append("    <foreach collection=\"list\" item=\"item\" index=\"index\" open=\"(\" separator=\",\" close=\")\"> \n");
            sb.append("       #{item." + pk.getEntityName() + ",jdbcType=" + TypeUtil.jdbcType(pk) + "}\n");
            sb.append("    </foreach>\n");
            sb.append("  </delete>\n");

            /********************  Update **************/
            sb.append("  <update id=\"update\" parameterType=\"" + pojoClass + "\">\n");
            sb.append("    update " + table.getName() + " set \n");
            boolean firstField = true;
            for (CgField f : tableAndDict) {
                if (Util.isEmpty(f.getName()) || f.getName().indexOf(":") >= 0) continue;     //  跳过非数据库字段
                if (f.getName().equals(pk.getName()))
                    continue;
                if (!firstField)
                    sb.append(",\n");
                sb.append("      " + f.getName() + " = #{" + f.getEntityName() + ",jdbcType=" + TypeUtil.jdbcType(f)
                        + "}");
                firstField = false;
            }
            for (CgField f : joinWithoutDictList) {
                if (Util.isEmpty(f.getName()) || f.getName().indexOf(":") >= 0) continue;     //  跳过非数据库字段
                if (f.getName().equals(pk.getName()))
                    continue;
                if (!firstField)
                    sb.append(",\n");
                sb.append("      " + f.getName() + " = #{" + f.getEntityName() + ",jdbcType=" + TypeUtil.jdbcType(f)
                        + "}");
                firstField = false;
            }
            sb.append("\n      where " + pk.getName() + " = #{" + pk.getEntityName() + ",jdbcType=" + TypeUtil.jdbcType(pk)
                    + "}");
            sb.append("\n  </update>\n");

            // update updateSelective
            sb.append("  <update id=\"updateSelective\" parameterType=\"" + pojoClass + "\">\n");
            sb.append("    update " + table.getName() + " \n");
            sb.append("      <set>\n");
            for (CgField f : tableAndDict) {
                if (Util.isEmpty(f.getName()) || f.getName().indexOf(":") >= 0) continue;     //  跳过非数据库字段
                if (f.getName().equals(pk.getName()))
                    continue;
                sb.append("        <if test=\"" + f.getEntityName() + " != null\"> " + f.getName() + " = #{"
                        + f.getEntityName() + ",jdbcType=" + TypeUtil.jdbcType(f) + "},</if>\n");
            }
            for (CgField f : joinWithoutDictList) {
                if (Util.isEmpty(f.getName()) || f.getName().indexOf(":") >= 0) continue;     //  跳过非数据库字段
                if (f.getName().equals(pk.getName()))
                    continue;
                sb.append("        <if test=\"" + f.getEntityName() + " != null\"> " + f.getName() + " = #{"
                        + f.getEntityName() + ",jdbcType=" + TypeUtil.jdbcType(f) + "},</if>\n");
            }
            sb.append("      </set>");
            sb.append("\n      where " + pk.getName() + " = #{" + pk.getEntityName() + ",jdbcType=" + TypeUtil.jdbcType(pk)
                    + "}");
            sb.append("\n  </update>\n");

            // update updateBy ,修改主键
            sb.append("  <update id=\"updateBy\" parameterType=\"" + pojoClass + "\">\n");
            sb.append("    update " + table.getName() + " set \n");
            firstField = true;
            for (CgField f : tableAndDict) {
                if (Util.isEmpty(f.getName()) || f.getName().indexOf(":") >= 0) continue;     //  跳过非数据库字段
                if (!firstField)
                    sb.append(",\n");
                sb.append("      " + f.getName() + " = #{record." + f.getEntityName() + ",jdbcType=" + TypeUtil.jdbcType(f)
                        + "}");
                firstField = false;
            }
            for (CgField f : joinWithoutDictList) {
                if (Util.isEmpty(f.getName()) || f.getName().indexOf(":") >= 0) continue;     //  跳过非数据库字段
                if (!firstField)
                    sb.append(",\n");
                sb.append("      " + f.getName() + " = #{record." + f.getEntityName() + ",jdbcType=" + TypeUtil.jdbcType(f)
                        + "}");
                firstField = false;
            }
            sb.append("\n      where " + pk.getName() + " = #{" + pk.getEntityName() + ",jdbcType=" + TypeUtil.jdbcType(pk)
                    + "}");
            sb.append("\n  </update>\n");
        }

        // list
        sb.append("  <select id=\"list\" parameterType=\"" + pojoClass + "\" resultMap=\"BaseResultMap\">\n"
                + "    " + sql + "\n" + "     where 1=1 \n");
        for (CgField f : tableAndDict) {
            if (Util.isEmpty(f.getName()) || f.getName().indexOf(":") >= 0) continue;     //  跳过非数据库字段
            String typ = TypeUtil.javaType(f);
            boolean useLike = typ.equals("String") || typ.equals("Date");
            sb.append("        <if test=\"" + f.getEntityName() + " != null\"> and " + tablePreFix + NameUtil.fieldName(f)
                    + (useLike ? " like " : " = ") + "#{" + f.getEntityName() + ",jdbcType=" + TypeUtil.jdbcType(f)
                    + "} </if>\n");
        }
        for (CgField f : joinWithoutDictList) {
            if (Util.isEmpty(f.getName()) || f.getName().indexOf(":") >= 0) continue;     //  跳过非数据库字段
            String typ = TypeUtil.javaType(f);
            CgTable jtab = JoinUtil.joinTable(f);
            boolean useLike = typ.equals("String") || typ.equals("Date");
            sb.append("        <if test=\"" + f.getEntityName() + " != null\"> and " + ("right_join".equals(f.getShowType()) ? JoinUtil.joinOnFullColumnName(table, f) : tablePreFix + NameUtil.fieldName(f))    // 右连接处理
                    + (useLike ? " like " : " = ") + "#{" + f.getEntityName() + ",jdbcType=" + TypeUtil.jdbcType(f)
                    + "} </if>\n");
            if (jtab != null) {
                List<CgField> splitedFields = JoinUtil.splitedJoinFields(table, f, tabFields, joinFields, calFields);
                for (CgField jf : splitedFields) {
                    String jtype = TypeUtil.javaType(jf);
                    useLike = jtype.equals("String") || typ.equals("Date");
                    sb.append("        <if test=\"" + jf.getEntityName() + " != null\"> and " + JoinUtil.joinFullColumnName(jtab, f, jf)
                            + (useLike ? " like " : " = ") + "#{" + jf.getEntityName() + ",jdbcType=" + TypeUtil.jdbcType(jf)
                            + "} </if>\n");
                }
            }

        }
        sb.append("  </select>\n");

        // listBy(String whereString,String orderString)
        sb.append("  <select id=\"listBy\" parameterType=\"map\" resultMap=\"BaseResultMap\">\n" + "    " + sql
                + "\n");
        sb.append("        <if test=\"whereString != null and whereString !=''\"> where ${whereString} </if>\n");
        sb.append("        <if test=\"orderString != null and orderString!=''\"> order by ${orderString} </if>\n");
        sb.append("  </select>\n");

        sb.append("  <select id=\"query\" parameterType=\"map\" resultMap=\"BaseResultMap\">\n");
        sb.append("        ${sqlString}\n");
        sb.append("  </select>\n");
        sb.append("</mapper>\n");
        GenService.writeToFile(sb.toString(), filePath);
    }
    private static StringBuilder appendDictList(int dbid, String tabAlias, CgField f, StringBuilder sb, StringBuilder mysqlsb) throws CgException {
        if (sb == null) {
            sb = new StringBuilder();
            sb.append(mysqlsb);
        }
        if (Util.isEmpty(f.getDictTable()) || Util.isEmpty(f.getDictTable()) || Util.isEmpty(f.getDictTable()))
            throw new CgException(IotequThrowable.ERROR,"dict_list 类型必须配置 数据表、关联字段和显示字段");
        CgTable jtab = JoinUtil.joinTable(f);
        f.setId(StringUtil.camelString(NameUtil.pureCode(jtab.getCode())));
        if (jtab == null) throw new CgException(IotequThrowable.ERROR,"dict_list 对应表单未定义");
        CgField textField = Util.getBean(DtoService.class).getFieldBy(jtab.getId(), f.getDictText().trim());
        CgField valueField = Util.getBean(DtoService.class).getFieldBy(jtab.getId(), f.getDictField().trim());
        f.setDictText(textField.getEntityName());
        f.setDictField(valueField.getEntityName());
        if (Util.isEmpty(textField.getName()) || Util.isEmpty(valueField.getName())) throw new CgException(IotequThrowable.ERROR,"dict_list 对应字段未定义");
        if (dbid == ScriptUtil.MYSQL) {
            mysqlsb.append(",\n      (select group_concat(").append(textField.getName()).append(") from ").append(jtab.getName())
                    .append(" where FIND_IN_SET(").append(valueField.getName()).append(",").append(tabAlias).append(".").append(f.getName()).append("))")
                    .append(" AS dict_list_").append(f.getName()).append("\n      ");
            return mysqlsb;
        } else if (dbid == ScriptUtil.SQLSERVER) {
            sb.append(",\n      (select CAST(").append(textField.getName())
                    .append(" as varchar)+',' from ").append(jtab.getName())
                    .append("  where CHARINDEX(','+CAST(").append(valueField.getName())
                    .append(" as varchar)+',',','+").append(tabAlias).append(".").append(f.getName()).append("+',') >0 for xml path(''))")
                    .append(" AS dict_list_").append(f.getName()).append("\n      ");
        } else if (dbid == ScriptUtil.ORACLE) {
            sb.append(",\n      (select  LISTAGG(").append(textField.getName()).append(",',') WITHIN GROUP(ORDER BY ").append(valueField.getName())
                    .append(") from ").append(jtab.getName())
                    .append(" where INSTR(','||")
                    .append(tabAlias).append(".").append(f.getName())
                    .append("||',',','||to_char(").append(valueField.getName()).append(")||',')>0)")
                    .append(" AS dict_list_").append(f.getName()).append("\n      ");
        }
        return sb;
    }

    private static String tableFieldsList(CgTable table, List<CgField> tabFields, List<CgField> joinFields, boolean includePK) throws CgException {
        String sp = "", s = "";
        CgField pk = GenService.priKey(table,tabFields,joinFields);
        List<CgField> tableAndDict = tableAndDictListFields(tabFields,joinFields);
        List<CgField> joinWithoutDictList = joinFields.stream().filter(f->!"dict_list".equals(f.getShowType())).collect(Collectors.toList());
        if (pk != null && includePK && !Util.isEmpty(pk.getName()) && pk.getName().indexOf(":") < 0) {   //  将 pk 放到 第一个
            s = pk.getName();
            sp = ",";
        }
        for (CgField f : tableAndDict) {
            if (pk != null && pk.getName().equals(f.getName()))
                continue;
            s = s + sp + f.getName();
            sp = ",";
        }
        for (CgField f : joinWithoutDictList) {
            if (pk != null && pk.getName().equals(f.getName()))
                continue;
            s = s + sp + f.getName();
            sp = ",";
        }
        return s;
    }

    private static String tableValuesList(CgTable table, List<CgField> tabFields, List<CgField> joinFields, boolean includePK, boolean forListItem) throws CgException {
        String sp = "", s = "";
        CgField pk = GenService.priKey(table,tabFields,joinFields);
        List<CgField> tableAndDict = tableAndDictListFields(tabFields,joinFields);
        List<CgField> joinWithoutDictList = joinFields.stream().filter(f->!"dict_list".equals(f.getShowType())).collect(Collectors.toList());
        if (pk != null && includePK && !Util.isEmpty(pk.getName()) && pk.getName().indexOf(":") < 0) {   //  将 pk 放到 第一个
            s = "#{" + (forListItem ? "item." : "") + pk.getEntityName() + ",jdbcType=" + TypeUtil.jdbcType(pk) + "}";
            sp = ",";
        }
        for (CgField f : tableAndDict) {
            if (pk != null && pk.getName().equals(f.getName())) continue;
            s = s + sp + "#{" + (forListItem ? "item." : "") + f.getEntityName() + ",jdbcType=" + TypeUtil.jdbcType(f) + "}";
            sp = ",";
        }
        for (CgField f : joinWithoutDictList) {
            if (pk != null && pk.getName().equals(f.getName())) continue;
            s = s + sp + "#{" + (forListItem ? "item." : "") + f.getEntityName() + ",jdbcType=" + TypeUtil.jdbcType(f) + "}";
            sp = ",";
        }
        return s;
    }

    private static String tableValuesList(CgTable table, List<CgField> tabFields, List<CgField> joinFields, String pkValue, boolean forListItem) throws CgException {
        String sp = "", s = "";
        CgField pk = GenService.priKey(table,tabFields,joinFields);
        List<CgField> tableAndDict = tableAndDictListFields(tabFields,joinFields);
        List<CgField> joinWithoutDictList = joinFields.stream().filter(f->!"dict_list".equals(f.getShowType())).collect(Collectors.toList());
        if (pk != null && !Util.isEmpty(pkValue) && !Util.isEmpty(pk.getName()) && pk.getName().indexOf(":") < 0) {  //  将 pk 放到 第一个
            s = pkValue;
            sp = ",";
        }
        for (CgField f : tableAndDict) {
            if (pk != null && pk.getName().equals(f.getName())) continue;
            else
                s = s + sp + "#{" + (forListItem ? "item." : "") + f.getEntityName() + ",jdbcType=" + TypeUtil.jdbcType(f) + "}";
            sp = ",";
        }
        for (CgField f : joinWithoutDictList) {
            if (pk != null && pk.getName().equals(f.getName())) continue;
            else
                s = s + sp + "#{" + (forListItem ? "item." : "") + f.getEntityName() + ",jdbcType=" + TypeUtil.jdbcType(f) + "}";
            sp = ",";
        }
        return s;
    }

    private static String selectKey(CgTable table, List<CgField> tabFields, List<CgField> joinFields, int dbId, boolean useDbId) throws CgException {
        CgField pk = GenService.priKey(table,tabFields,joinFields);
        if (pk == null) return "";
        String sql = "", dbStr = "", when = "", keyType = "";
        if (useDbId)
            dbStr = (dbId == ScriptUtil.SQLSERVER ? "databaseId=\"SQLServer\"" : (dbId == ScriptUtil.ORACLE ? "databaseId=\"Oracle\"" : ""));
        if (pk.getKeyType().equals("1")) {
            keyType = "java.lang.Integer";
            if (dbId == ScriptUtil.MYSQL) {
                sql = "SELECT LAST_INSERT_ID()";
                when = "AFTER";
            } else if (dbId == ScriptUtil.SQLSERVER) {
                sql = "SELECT @@IDENTITY";
                when = "AFTER";
            } else if (dbId == ScriptUtil.ORACLE) {
                sql = "SELECT SEQUENCE_" + table.getName().toUpperCase() + ".nextval from DUAL";
                when = "BEFORE";
            }
        } else if (pk.getKeyType().equals("2")) {
            keyType = "java.lang.String";
            when = "BEFORE";
            if (dbId == ScriptUtil.MYSQL) {
                sql = "SELECT replace(uuid(), '-', '')";
            } else if (dbId == ScriptUtil.SQLSERVER) {
                sql = "SELECT replace(lower(newid()), '-', '')";
            } else if (dbId == ScriptUtil.ORACLE) {
                sql = "SELECT replace(lower(sys_guid()), '-', '') from dual";
            }
        } else return "";
        return "    <selectKey keyProperty=\"" + pk.getEntityName() + "\" order=\"" + when + "\" "
                + "resultType=\"" + keyType + "\" " + dbStr + ">\n"
                + "      " + sql + "\n"
                + "    </selectKey>\n";
    }


    private static void mapperInsert(CgTable table, List<CgField> tabFields, List<CgField> joinFields, String pojoClass, CgField pk, StringBuilder sb) throws CgException {
        sb.append("  <insert id=\"insert\" parameterType=\"" + pojoClass + "\"" + ">\n");
        boolean selKey = (pk != null && (pk.getKeyType().equals("1") || pk.getKeyType().equals("2")));
        boolean ai = (pk != null && pk.getKeyType().equals("1"));
        if (selKey) {
            sb.append(selectKey(table,tabFields,joinFields,ScriptUtil.MYSQL, false));
            sb.append(selectKey(table,tabFields,joinFields,ScriptUtil.SQLSERVER, true));
            if (!ai) sb.append(selectKey(table,tabFields,joinFields,ScriptUtil.ORACLE, true));
        }
        sb.append("    insert into ").append(table.getName()).append(" (")
                .append(tableFieldsList(table,tabFields,joinFields,!ai)).append(")\n");
        sb.append("      values \n");
        sb.append("      (").append(tableValuesList(table,tabFields,joinFields,!ai, false)).append(")\n");
        sb.append("  </insert>\n");
        if (ai) {  // Oracle  AI
            sb.append("  <insert id=\"insert\" parameterType=\"" + pojoClass + "\" databaseId=\"Oracle\"" + ">\n");
            sb.append(selectKey(table,tabFields,joinFields,ScriptUtil.ORACLE, false));
            sb.append("    insert into ").append(table.getName()).append(" (")
                    .append(tableFieldsList(table,tabFields,joinFields,true)).append(")\n");
            sb.append("      values \n");
            sb.append("      (").append(tableValuesList(table,tabFields,joinFields,true, false)).append(")\n");
            sb.append("  </insert>\n");
        }
    }

    private static void mapperInsertWithoutId(CgTable table, List<CgField> tabFields, List<CgField> joinFields, String pojoClass, CgField pk, StringBuilder sb) throws CgException {
        if (pk == null) return;
        if (pk.getKeyType().equals("1")) {  // ai
            String dbId = "";
            sb.append("  <insert id=\"insertBatchWithoutId\" parameterType=\"" + pojoClass + "\"" + dbId + ">\n");
            sb.append("    insert into ").append(table.getName()).append(" (")
                    .append(tableFieldsList(table,tabFields,joinFields,false)).append(")\n");
            sb.append("      values \n");
            sb.append("    <foreach collection=\"list\" item=\"item\" index=\"index\" separator=\",\">\n");
            sb.append("      (").append(tableValuesList(table,tabFields,joinFields,false, true)).append(")\n");
            sb.append("    </foreach>\n");
            sb.append("  </insert>\n");
            dbId = " databaseId=\"Oracle\"";
            sb.append("  <insert id=\"insertBatchWithoutId\" parameterType=\"" + pojoClass + "\"" + dbId + ">\n");
            sb.append("     insert into ").append(table.getName()).append(" (")
                    .append(tableFieldsList(table,tabFields,joinFields,true)).append(")\n");
            sb.append("       select " + "SEQUENCE_" + table.getName().toUpperCase() + ".nextval" + ",cd.* from(\n");
            sb.append("          <foreach collection=\"list\" item=\"item\" index=\"index\" separator=\"union all\">\n");
            sb.append("             select " + tableValuesList(table,tabFields,joinFields,false, true) + " from dual\n");
            sb.append("          </foreach>\n");
            sb.append("       ) cd\n");
            sb.append("  </insert>\n");
        } else if (pk.getKeyType().equals("2")) {
            String dbId = "";
            sb.append("  <insert id=\"insertBatchWithoutId\" parameterType=\"" + pojoClass + "\"" + dbId + ">\n");
            sb.append("    insert into ").append(table.getName()).append(" (")
                    .append(tableFieldsList(table,tabFields,joinFields,true)).append(")\n");
            sb.append("      values \n");
            sb.append("    <foreach collection=\"list\" item=\"item\" index=\"index\" separator=\",\">\n");
            String kv = "replace(uuid(), '-', '')";
            sb.append("      (").append(tableValuesList(table,tabFields,joinFields,kv, true)).append(")\n");
            sb.append("    </foreach>\n");
            sb.append("  </insert>\n");
            dbId = " databaseId=\"SQLServer\"";
            sb.append("  <insert id=\"insertBatchWithoutId\" parameterType=\"" + pojoClass + "\"" + dbId + ">\n");
            sb.append("    insert into ").append(table.getName()).append(" (")
                    .append(tableFieldsList(table,tabFields,joinFields,true)).append(")\n");
            sb.append("      values \n");
            sb.append("    <foreach collection=\"list\" item=\"item\" index=\"index\" separator=\",\">\n");
            kv = "replace(lower(newid()), '-', '')";
            sb.append("      (").append(tableValuesList(table,tabFields,joinFields,kv, true)).append(")\n");
            sb.append("    </foreach>\n");
            sb.append("  </insert>\n");
            dbId = " databaseId=\"Oracle\"";
            sb.append("  <insert id=\"insertBatchWithoutId\" parameterType=\"" + pojoClass + "\"" + dbId + ">\n");
            sb.append("    insert all \n");
            sb.append("    <foreach collection=\"list\" item=\"item\" index=\"index\">\n");
            kv = "replace(lower(sys_guid()), '-', '')";
            sb.append("      into ").append(table.getName()).append(" (")
                    .append(tableFieldsList(table,tabFields,joinFields,true)).append(") values(")
                    .append(tableValuesList(table,tabFields,joinFields,kv, true)).append(")\n");
            sb.append("    </foreach>\n");
            sb.append("    select 1 from dual\n");
            sb.append("  </insert>\n");
        } else {   // 其他情况无法自动产生pk

        }
    }

    private static void mapperInsertWithId(CgTable table, List<CgField> tabFields, List<CgField> joinFields, String pojoClass, CgField pk, StringBuilder sb) throws CgException {
        String dbId = "";
        sb.append("  <insert id=\"insertBatchWithId\" parameterType=\"" + pojoClass + "\"" + dbId + ">\n");
        sb.append("    insert into ").append(table.getName()).append(" (")
                .append(tableFieldsList(table,tabFields,joinFields,true)).append(")\n");
        sb.append("      values \n");
        sb.append("    <foreach collection=\"list\" item=\"item\" index=\"index\" separator=\",\">\n");
        sb.append("      (").append(tableValuesList(table,tabFields,joinFields,true, true)).append(")\n");
        sb.append("    </foreach>\n");
        sb.append("  </insert>\n");
        if (pk != null && pk.getKeyType().equals("1")) {  //  SQLServer
            dbId = " databaseId=\"SQLServer\"";
            sb.append("  <insert id=\"insertBatchWithId\" parameterType=\"" + pojoClass + "\"" + dbId + ">\n");
            if (pk != null && pk.getKeyType().equals("1"))
                sb.append("    set IDENTITY_INSERT " + table.getName() + " ON \n");
            sb.append("    insert into ").append(table.getName()).append(" (")
                    .append(tableFieldsList(table,tabFields,joinFields,true)).append(")\n");
            sb.append("      values \n");
            sb.append("    <foreach collection=\"list\" item=\"item\" index=\"index\" separator=\",\">\n");
            sb.append("      (").append(tableValuesList(table,tabFields,joinFields,true, true)).append(")\n");
            sb.append("    </foreach>\n");
            if (pk != null && pk.getKeyType().equals("1"))
                sb.append("    set IDENTITY_INSERT " + table.getName() + " OFF \n");
            sb.append("  </insert>\n");
        }
        dbId = " databaseId=\"Oracle\"";
        sb.append("  <insert id=\"insertBatchWithId\" parameterType=\"" + pojoClass + "\"" + dbId + ">\n");
        sb.append("    insert all \n");
        sb.append("    <foreach collection=\"list\" item=\"item\" index=\"index\">\n");
        sb.append("      into ").append(table.getName()).append(" (")
                .append(tableFieldsList(table,tabFields,joinFields,true)).append(") values(")
                .append(tableValuesList(table,tabFields,joinFields,true, true)).append(")\n");
        sb.append("    </foreach>\n");
        sb.append("    select 1 from dual\n");
        sb.append("  </insert>\n");
    }
}
