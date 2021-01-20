package top.iotequ.codegenerator.util;

import top.iotequ.codegenerator.dao.CgTableDao;
import top.iotequ.codegenerator.pojo.CgForm;
import top.iotequ.codegenerator.pojo.CgList;
import top.iotequ.codegenerator.pojo.CgField;
import top.iotequ.codegenerator.pojo.CgTable;
import top.iotequ.codegenerator.service.impl.GenService;
import top.iotequ.framework.exception.IotequThrowable;
import top.iotequ.util.StringUtil;
import top.iotequ.util.Util;

public class NameUtil {
    public static String pureCode(String code) {
        if (code == null) return null;
        String[] cc = code.split("\\.");
        if (cc.length > 1) return cc[1].trim();
        else return code.trim();
    }
    public static String routeJs(CgTable t) throws CgException {
        return "/routes-cg.js";
    }
    public static String tableTag(CgTable t) throws CgException{
        return generatorName(t);
    }

    public static String fieldTag(CgField f, CgTable t) throws CgException{
        return generatorName(t) + "." + f.getEntityName();
    }
    public static String generatorName(CgTable t) {
         return StringUtil.camelString(t.getProject()+"_"+t.getCode());
    }

    public static String basePackage(CgTable t) {
        assert (t != null);
        String md = (Util.isEmpty(t.getGroupId()) ? "" : t.getGroupId().trim() + ".") + t.getModule().trim();
        if (!Util.isEmpty(t.getSubPackage())) md = md + "." + t.getSubPackage().trim();
        return md;
    }

    public static String projectName(CgTable t) {  // module的最后一段为项目名，用于多语言位置
        assert (t != null);
        return t.getModule();
    }

    public static String userPath(String basePath, boolean scanSource) {
        return basePath + (scanSource?"/":"/"+ Util.getUser().getName());
    }

    public static String serverPath(String basePath, boolean scanSource) {
        return userPath(basePath, scanSource) + "/iotequ";
    }
    public static String vuePath(String basePath, boolean scanSource) {
        return userPath(basePath, scanSource) + "/vue-iotequ";
    }
    public static String baseJavaPath(String basePath, CgTable t, boolean scanSource) {
        return serverPath(basePath, scanSource) + "/" + projectName(t) + "/src/main/java/" + basePackage(t).replace(".", "/");
    }
    public static String baseResourcePath(String basePath, CgTable t, String resourceType, boolean scanSource) {
        return serverPath(basePath, scanSource) + "/" + projectName(t) + "/src/main/resources/" + resourceType + "/" + t.getModule();
    }
    public static String vueViewPath(String basePath, CgTable t, boolean scanSource) throws CgException {
        return vuePath(basePath, scanSource) + "/src/views/"
                + projectName(t) + (Util.isEmpty(t.getSubPackage())?"":"/"+t.getSubPackage().trim())
                + "/"+generatorName(t);
    }
    public static String languagePath(String genPath, CgTable t, boolean scanSource) throws CgException {
        return vueViewPath(genPath,t,scanSource);
        //        return genPath + (scanSource?"/":"/server/") + projectName(t) + "/src/main/resources/languages/" + projectName(t) + "/";
    }
    public static String dbPath(String genPath) {
        return serverPath(genPath, false) + "/db-script/";
    }
    public static String controllerPath(String basePath, CgTable t, boolean scanSource) throws CgException {
        return baseJavaPath(basePath, t,scanSource) + "/" + GenService.CONTROLLER + "/" + StringUtil.firstLetterUpper(generatorName(t))
                + StringUtil.firstLetterUpper(GenService.CONTROLLER) + ".java";
    }

    //生成数据字典
    public static String dataDictName(String basePath, CgTable t) {
        return dbPath(basePath) + projectName(t) + "/" + projectName(t) + "_dict.txt";
    }

    public static String actionSqlName(String basePath, CgTable t, int dbType) {
        String dbName = (dbType == ScriptUtil.MYSQL ? "mysql" : (dbType == ScriptUtil.SQLSERVER ? "sqlserver" : (dbType == ScriptUtil.ORACLE ? "oracle" : ("sql"))));
        return dbPath(basePath) + projectName(t) + "/" + projectName(t) + "_action_" + dbName + ".sql";
    }

    public static String dbScriptName(String basePath, CgTable t, int dbType) {
        String dbName = (dbType == ScriptUtil.MYSQL ? "mysql" : (dbType == ScriptUtil.SQLSERVER ? "sqlserver" : (dbType == ScriptUtil.ORACLE ? "oracle" : ("sql"))));
        return dbPath(basePath) + projectName(t) + "/" + projectName(t) + "_" + dbName + ".sql";
    }

    public static String objectName(int dbType, String obj) {
        if (dbType == ScriptUtil.SQLSERVER) {
            if (obj.toLowerCase().contains("index_")) return "[" + obj + "]";
            else return "[dbo].[" + obj + "]";
        } else if (dbType == ScriptUtil.ORACLE) return "\"IOTEQU\".\"" + obj.toUpperCase() + "\"";
        else if (dbType == ScriptUtil.MYSQL) return "`" + obj + "`";
        else return "\"" + obj + "\"";
    }

    public static String fieldName(int dbType, String obj) {
        if (dbType == ScriptUtil.SQLSERVER) return "[" + obj + "]";
        else if (dbType == ScriptUtil.ORACLE) return "\"" + obj.toUpperCase() + "\"";
        else if (dbType == ScriptUtil.MYSQL) return "`" + obj + "`";
        else return "\"" + obj + "\"";
    }
    public static String fieldName(CgField f) {
        String name = f.getName();
        if (Util.isEmpty(name)) return null;
        String[] nn = name.split(":");
        return nn[0].trim();
    }
    public static String fieldExpression(CgField f) {
        String name = f.getName();
        if (Util.isEmpty(name)) return null;
        String[] nn = name.split(":");
        return nn.length>1?nn[1].trim():"";
    }
    public static String componentName(CgList item) throws CgException {
        return "CgList"+StringUtil.firstLetterUpper(StringUtil.toString(item.getName()));
    }
    public static String componentPath(CgList item) throws CgException{
        CgTable refTab = Util.getBean(CgTableDao.class).select(item.getTableId());
        if (refTab==null) throw new CgException(IotequThrowable.ERROR,"CgList "+item.getName()+" 对应的表定义不存在");
        String component = "CgList"+StringUtil.firstLetterUpper(StringUtil.toString(item.getName()));
        return "/"+NameUtil.projectName(refTab).toLowerCase() + "/"
                + (Util.isEmpty(refTab.getSubPackage()) ? "" : refTab.getSubPackage().trim() + "/")
                + NameUtil.generatorName(refTab) + "/" + component;
    }
    public static String listViewComponentName(CgList item) throws CgException {
        return "ListView"+StringUtil.firstLetterUpper(StringUtil.toString(item.getName()));
    }
    public static String listViewComponentPath(CgList item) throws CgException{
        CgTable refTab = Util.getBean(CgTableDao.class).select(item.getTableId());
        if (refTab==null) throw new CgException(IotequThrowable.ERROR,"CgList "+item.getName()+" 对应的表定义不存在");
        String component = "list";
        return "/"+NameUtil.projectName(refTab).toLowerCase() + "/"
                + (Util.isEmpty(refTab.getSubPackage()) ? "" : refTab.getSubPackage().trim() + "/")
                + NameUtil.generatorName(refTab) + "/" + component;
    }
    public static String componentName(CgForm item) throws CgException {
        return "CgForm"+StringUtil.firstLetterUpper(StringUtil.toString(item.getName()));
    }
    public static String componentPath(CgForm item) throws CgException{
        CgTable refTab = Util.getBean(CgTableDao.class).select(item.getTableId());
        if (refTab==null) throw new CgException(IotequThrowable.ERROR,"CgForm "+item.getName()+" 对应的表定义不存在");
        String component = "CgForm"+StringUtil.firstLetterUpper(StringUtil.toString(item.getName()));
        return "/"+NameUtil.projectName(refTab).toLowerCase() + "/"
                + (Util.isEmpty(refTab.getSubPackage()) ? "" : refTab.getSubPackage().trim() + "/")
                + NameUtil.generatorName(refTab) + "/" + component;
    }
}
