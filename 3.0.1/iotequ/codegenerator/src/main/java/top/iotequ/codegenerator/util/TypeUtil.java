package top.iotequ.codegenerator.util;

import top.iotequ.codegenerator.pojo.CgField;
import top.iotequ.framework.exception.IotequThrowable;
import top.iotequ.framework.util.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// 数据库类型定义值 :
// int,varchar,boolean,datetime,tinyint,smallint,mediumint,bigint,double,float,decimal,date,time,timestamp,
// char,tinytext,text,mediumtext,longtext,tinyblob,blob,mediumblob,longblob,binary,varbinary
public class TypeUtil {
    /**
     * 获得java类型
     * @param f 字段定义
     * @return java 类名
     */
    public static String javaType(CgField f) {
        String dt = "String";
        if (Util.isEmpty(f.getName())) {  // 非数据库字段
            String type = f.getShowType().toLowerCase();
            if (type.equals("boolean"))
                return "Boolean";
            else if (type.indexOf("date") >= 0 || type.indexOf("time") >= 0)
                return "Date";
            else if (type.equals("number")) {
                return "Double";
            } else
                return "String";
        } else {
            String type = f.getType().toLowerCase();
            if (type.equals("tinyint(1)") || (f.getShowType() != null && f.getShowType().equals("boolean")))
                return "Boolean";
            else if (type.startsWith("tinyint"))
                return "Byte";
            else if (type.startsWith("int") || type.startsWith("mediumint"))
                dt = "Integer";
            else if (type.startsWith("smallint"))
                dt = "Short";
            else if (type.startsWith("bigint"))
                dt = "Long";
            else if (type.indexOf("char") >= 0 || type.indexOf("text") >= 0)
                dt = "String";
            else if (type.indexOf("date") >= 0 || type.indexOf("time") >= 0)
                dt = "Date";
            else if (type.startsWith("double") || type.startsWith("real") || type.startsWith("decimal")) {
                Integer sc = f.getNumericScale(), pr = f.getNumericPrecision();
                if (sc != null && pr != null && sc == 0) {
                    if (pr < 5)
                        dt = "Short";
                    else if (pr < 10)
                        dt = "Integer";
                    else if (pr < 19)
                        dt = "Long";
                    else
                        dt = "BigDecimal";
                } else
                    dt = "Double";
            } else if (type.startsWith("float"))
                dt = "Float";
            else if (type.indexOf("blob") >= 0 || type.indexOf("bit") >= 0 || type.indexOf("binary") >= 0)
                dt = "byte[]";
            else
                dt = "String";
        }
        return dt;
    }

    public static long lengthOf(CgField f) {
        String type = f.getType().toLowerCase();
        Integer length = f.getLength();
        if (length!=null && length>0) return length;
        switch (type) {
            case "tinyblob":
            case "tinytext": return 255;
            case "blob":
            case "text" : return 65535;
            case "mediumblob":
            case "mediumtext": return 16777215;
            case "longblob":
            case "longtext": return 4294967295L;
        }
        return 0;
    }
    /**
     * 判断是否数字类型
     * @param f 字段定义
     * @return 是否数值类型
     */
    public static boolean isNumbericType(CgField f) {
        String type = f.getType().toLowerCase();
        if (type.startsWith("int") || type.startsWith("mediumint") || type.startsWith("smallint") || type.startsWith("bigint")
                || type.startsWith("tinyint") || type.startsWith("double") || type.startsWith("number")
                || type.startsWith("real") || type.startsWith("decimal") || type.startsWith("float")) return true;
        else return false;
    }

    /**
     * 获得jdbc类型
     * @param f 字段定义
     * @return jdbc类型
     */
    public static String jdbcType(CgField f) {
        return jdbcType(f.getType());
    }

    public static String jdbcType(String type) {
        String dt = "VARCHAR";
        if (Util.isEmpty(type)) return dt;
        type = type.toLowerCase();
        if (type.startsWith("varchar") || type.startsWith("nvarchar"))
            dt = "VARCHAR";
        else if (type.startsWith("int") || type.startsWith("mediumint"))
            dt = "INTEGER";
        else if (type.startsWith("smallint"))
            dt = "SMALLINT";
        else if (type.startsWith("bigint"))
            dt = "BIGINT";
        else if (type.startsWith("tinyint"))
            dt = "TINYINT";
        else if (type.startsWith("datetime") || type.startsWith("timestamp"))
            dt = "TIMESTAMP";
        else if (type.startsWith("time"))
            dt = "TIME";
        else if (type.startsWith("date") || type.startsWith("year"))
            dt = "DATE";
        else if (type.startsWith("decimal"))
            dt = "DECIMAL";
        else if (type.startsWith("double") || type.startsWith("real"))
            dt = "DOUBLE";
        else if (type.startsWith("float"))
            dt = "REAL";
        else if (type.startsWith("binary") || type.startsWith("tinyblob"))
            dt = "BINARY";
        else if (type.startsWith("bit"))
            dt = "BIT";
        else if (type.startsWith("char") || type.startsWith("nchar"))
            dt = "CHAR";
        else if (type.startsWith("blob") || type.startsWith("longblob") || type.startsWith("mediumblob"))
            dt = "TIMESTAMP";
        else if (type.startsWith("varbinary"))
            dt = "VARBINARY";
        else
            dt = "VARCHAR";
        return dt;
    }

    /**
     * 数据库类型定义
     * @param f 字段定义
     * @param dbType 数据库类型
     * @return 数据库字段类型
     */
    public static String dbDataType(CgField f, int dbType) {
        if ("2".equals(f.getKeyType())) return "char(32)";   // uuid 使用32为char而不使用varchar
        String type = f.getType().toLowerCase();
        int length = (f.getLength() == null ? 0 : f.getLength());
        int prec = (f.getNumericPrecision() == null ? 0 : f.getNumericPrecision());
        int scale = (f.getNumericScale() == null ? 0 : f.getNumericScale());
        if (type.startsWith("char") || type.startsWith("nchar"))
            return "char(" + (length == 0 ? 32 : length) + (dbType == ScriptUtil.ORACLE ? " byte" : "") + ")";
        else if (type.startsWith("varchar") || type.startsWith("nvarchar"))
            return dbType == ScriptUtil.ORACLE ? "varchar2(" + (length == 0 ? 32 : length) + " byte)" : "varchar(" + (length == 0 ? 32 : length) + ")";
        else if (type.startsWith("text") || type.startsWith("longtext") || type.startsWith("clob"))
            return (dbType == ScriptUtil.MYSQL ? "text" : (dbType == ScriptUtil.SQLSERVER ? "text" : (dbType == ScriptUtil.ORACLE ? "CLOB" : "text")));
        else if (type.startsWith("int") || type.startsWith("mediumint"))
            return (dbType == ScriptUtil.MYSQL ? "int(" + (length == 0 ? 11 : length) + ")" : (dbType == ScriptUtil.SQLSERVER ? "int" : (dbType == ScriptUtil.ORACLE ? "INTEGER" : "int")));
        else if (type.startsWith("smallint"))
            return (dbType == ScriptUtil.MYSQL ? "smallint(" + (length == 0 ? 6 : length) + ")" : (dbType == ScriptUtil.SQLSERVER ? "smallint" : (dbType == ScriptUtil.ORACLE ? "SMALLINT" : "smallint")));
        else if (type.startsWith("bigint"))
            return (dbType == ScriptUtil.MYSQL ? "bigint(" + (length == 0 ? 22 : length) + ")" : (dbType == ScriptUtil.SQLSERVER ? "bigint" : (dbType == ScriptUtil.ORACLE ? "long" : "bigint")));
        else if (type.startsWith("boolean") || type.startsWith("tinyint(1)") || type.startsWith("bit"))
            return (dbType == ScriptUtil.MYSQL ? "tinyint(1)" : (dbType == ScriptUtil.SQLSERVER ? "bit" : (dbType == ScriptUtil.ORACLE ? "number(1)" : "tinyint")));
        else if (type.startsWith("tinyint"))
            return (dbType == ScriptUtil.MYSQL ? "tinyint(" + (length == 0 ? 3 : length) + ")" : (dbType == ScriptUtil.SQLSERVER ? "tinyint" : (dbType == ScriptUtil.ORACLE ? "number(3)" : "tinyint")));
        else if (type.startsWith("datetime") || type.startsWith("timestamp"))
            return (dbType == ScriptUtil.MYSQL ? "datetime" : (dbType == ScriptUtil.SQLSERVER ? "datetime" : (dbType == ScriptUtil.ORACLE ? "DATE" : "datetime")));
        else if (type.startsWith("time"))
            return (dbType == ScriptUtil.MYSQL ? "time(" + (length == 0 ? 6 : length) + ")" : (dbType == ScriptUtil.SQLSERVER ? "datetime" : (dbType == ScriptUtil.ORACLE ? "DATE" : "time")));
        else if (type.startsWith("date") || type.startsWith("year"))
            return (dbType == ScriptUtil.MYSQL ? "date" : (dbType == ScriptUtil.SQLSERVER ? "datetime" : (dbType == ScriptUtil.ORACLE ? "DATE" : "date")));
        else if (type.startsWith("decimal") || type.startsWith("double") || type.startsWith("real") || type.startsWith("float")) {
            if (scale == 0) scale = 2;
            if (prec == 0) prec = 12;
            return (dbType == ScriptUtil.MYSQL ? "double(" + prec + "," + scale + ")" : (dbType == ScriptUtil.SQLSERVER ? "double(" + prec + ":" + scale + ")" : (dbType == ScriptUtil.ORACLE ? "number(" + prec + ":" + scale + ")" : "double(" + prec + ":" + scale + ")")));
        } else if (type.startsWith("binary") || type.startsWith("tinyblob") || type.startsWith("blob") || type.startsWith("longblob") || type.startsWith("mediumblob"))
            return "blob(" + (length == 0 ? 10 : length) + ")";
        else if (type.startsWith("varbinary"))
            return "varblob(" + (length == 0 ? 10 : length) + ")";
        else
            return type;
    }

    public static void setTypeMysqlField(CgField f, String dbType) throws CgException {
        String type=dbType.toLowerCase().trim();
        Pattern pattern = Pattern.compile("([a-z]+)(\\((\\d+)(,(\\d+))?\\))?");
        Matcher matcher = pattern.matcher(type);
        if (matcher.find()) {
            String t = matcher.group(1);
            f.setType(t);
            if (matcher.group(2)==null) { // 没有找到 (*)
                f.setLength(null);
                f.setNumericPrecision(null);
                f.setNumericScale(null);
            } else if (matcher.group(4)==null) {  // 找到 (xx)
                int l = Integer.parseInt(matcher.group(3));
                if ("decimal".equals(t) || "double".equals(t) || "float".equals(t) || "real".equals(t))
                {
                    f.setNumericPrecision(3);
                    f.setNumericScale(null);
                    f.setLength(null);
                } else {
                    f.setNumericPrecision(null);
                    f.setNumericScale(null);
                    f.setLength(l);
                    if (("tinyint".equals(t) || "bit".equals(t) )&& l == 1) f.setType("boolean");
                }
            } else {  // 找到 (p,s)
                f.setNumericPrecision(Integer.parseInt(matcher.group(3)));
                f.setNumericScale(Integer.parseInt(matcher.group(5)));
                f.setLength(null);
            }
        } else throw new CgException(IotequThrowable.PARAMETER_ERROR,"不支持的数据类型"+dbType);
    }

    public static String fullJavaType(CgField f) {
        String jt = TypeUtil.javaType(f);
        if (jt.equals("Date")) return "java.util.Date";
        else return "java.lang." + jt;
    }
}
