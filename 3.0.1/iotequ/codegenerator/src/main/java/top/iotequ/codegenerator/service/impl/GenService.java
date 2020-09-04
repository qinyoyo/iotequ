package top.iotequ.codegenerator.service.impl;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.NullCacheStorage;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import top.iotequ.codegenerator.dao.CgProjectDao;
import top.iotequ.codegenerator.pojo.*;
import top.iotequ.codegenerator.util.*;
import top.iotequ.framework.flow.IFlowService;
import top.iotequ.framework.util.EntityUtil;
import top.iotequ.framework.util.SqlUtil;
import top.iotequ.framework.util.StringUtil;
import top.iotequ.framework.util.Util;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class GenService implements ApplicationContextAware {
    public static final String DAO = "dao";
    public static final String POJO = "pojo";
    public static final String CONTROLLER = "controller";
    public static final String HTML = "html";
    public static final String SYSTEM_ACTIONS = ",_add,_edit,_view,_delete,_batdel,_import,_export,_flow,_refresh,_reset,_toolbar,_switch,_column,_search,_doSearch,";
    private static final Logger log = LoggerFactory.getLogger(GenService.class);
    private static final Configuration CONFIGURATION = new Configuration(Configuration.VERSION_2_3_22);
    public static String writedProjectName = "";
    String generatorPath;
    boolean noSampleCode = false;
    boolean flowController = false;
    Map<String, Object> mapForFreeMaker = null;   //  一个表只能计算一次，否则报错
    @Autowired
    DtoService dtoService;
    @Autowired
    CgProjectDao cgProjectDao;
    @Autowired
    private Environment env;
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;
    private CgTable table;
    private List<CgField> tabFields = new ArrayList<CgField>();    // 数据库表字段
    private List<CgField> joinFields = new ArrayList<CgField>();    // join字段
    private List<CgField> calFields = new ArrayList<CgField>();     // 计算字段

    public static CgField priKey(CgTable tab) {
        List<CgField> ff = Util.getBean(DtoService.class).getFields(tab.getId());
        for (CgField f : ff) {
            String key = f.getKeyType();
            if (key != null && ("1".equals(key) || "2".equals(key) || "3".equals(key))) { // 1 自动增长int 2 uuid 3 其他
                return f;
            }
        }
        return null;
    }

    // 主键及其主键类型,主键可以定义join关联
    public static CgField priKey(CgTable table, List<CgField> tabFields, List<CgField> joinFields) throws CgException {
        for (CgField f : tabFields) {
            String key = f.getKeyType();
            if (key != null && ("1".equals(key) || "2".equals(key) || "3".equals(key))) { // 1 自动增长int 2 uuid 3 其他
                return f;
            }
        }
        for (CgField f : joinFields) {
            String key = f.getKeyType();
            if (key != null && ("1".equals(key) || "2".equals(key) || "3".equals(key))) { // 1 自动增长int 2 uuid 3 其他
                return f;
            }
        }
        if (!Util.isEmpty(table.getName())) throw new CgException("必须定义一个主键，一般为id");
        else return null;
    }

    /************************
     * 将字符串写入文件
     * @param s : 字符串
     * @param path : 路径
     * @throws CgException 出错抛出异常
     */
    static public void writeToFile(String s, String path) throws CgException {
        try {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            } else {
                File dir = file.getParentFile();
                if (!dir.exists()) dir.mkdirs();
            }
            PrintWriter fw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8")));
            fw.write(s);
            fw.close();
        } catch (Exception e) {
            throw new CgException(e);
        }
    }

    /************************
     * 将字符串添加到文件
     * @param s ： 字符
     * @param path ： 路径
     * @throws CgException 出错抛出异常
     */
    static public void appendToFile(String s, String path) throws CgException {
        try {
            File file = new File(path);
            if (!file.exists()) {
                File dir = file.getParentFile();
                if (!dir.exists()) dir.mkdirs();
            }
            PrintWriter fw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), "utf-8")));
            fw.write(s);
            fw.close();
        } catch (Exception e) {
            throw new CgException(e);
        }
    }

    public static StringBuilder newBuilder() {
        StringBuilder sb = new StringBuilder();
        sb.append("/**************************************************\n");
        sb.append("    Create by iotequ generator 3.0.0\n");
        sb.append("    Author : Qinyoyo\n");
        sb.append("**************************************************/\n\n");
        return sb;
    }

    public static List<CgField> unionUniqueIndexFields(List<CgField> tabFields, List<CgField> joinFields, int dbType, String no) {
        List<CgField> uf = new ArrayList<CgField>();
        for (CgField f : tabFields)
            if (!Util.isEmpty(f.getName()) && f.getName().indexOf(":") < 0 && no.equals(f.getKeyType()))
                uf.add(f);   // 计算字段不能作为索引字段
        for (CgField f : joinFields)
            if (!Util.isEmpty(f.getName()) && f.getName().indexOf(":") < 0 && no.equals(f.getKeyType()))
                uf.add(f);   // 计算字段不能作为索引字段
        if (uf.isEmpty()) return null;
        return uf;
    }

    public static void fieldListSort(List<CgField> list) {
        if (list != null) {
            list.sort((o1, o2) -> o1.getOrderNum() - o2.getOrderNum());
        }
    }

    public static List<CgField> databaseFields(List<CgField> tabFields, List<CgField> joinFields) {
        List<CgField> df = new ArrayList<CgField>();
        df.addAll(tabFields);
        df.addAll(joinFields);
        return df;
    }

    public CgField parentEntity(CgTable table, List<CgField> tabFields, List<CgField> joinFields) throws CgException {
        if (priKey(table, tabFields, joinFields) == null) return null;  // 树结构必须定义主键
        List<CgList> list = dtoService.getCgLists(table.getId());
        for (CgList l : list) {
            CgField f = parentEntity(l);
            if (Objects.nonNull(f)) return f;
        }
        ;
        return null;
    }

    public CgField parentEntity(CgList list) {
        try {
            if (Objects.nonNull(list) && !Util.isEmpty(list.getParentEntity())) {
                return dtoService.getFieldBy(list.getTableId(), list.getParentEntity());
            }
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    }

    public void generate(String project, String tab, List<Map<String, Object>> allCodeList) throws CgException {
        log.info("Generatting {} ...", tab);
        CgException.setTableCode(tab);
        CgException.setProjectCode(project);
        if (!"MySql".equals(sqlSessionTemplate.getConfiguration().getDatabaseId()))
            throw new CgException("cg模块只支持mysql数据库");
        if (Util.isEmpty(tab)) throw new CgException("表单不能为空");
        generatorPath = env.getProperty("generator.path");
        if (generatorPath == null) throw new CgException("请配置代码生成文件路径 generator.path");
        boolean gMessage = true, gController = true, gPojo = false, gDao = false, gMapper = false, gDict = false, gUpdate = false, gList = false;
        setTable(project, tab);
        if (!Util.isEmpty(table.getName()))
            gPojo = gDao = gMapper = gDict = true;
        String al = table.getActionList();
        if (!Util.isEmpty(al)) {
            if (al.indexOf("list") >= 0) gList = true;
            if (al.indexOf("add") >= 0 || al.indexOf("view") >= 0 || al.indexOf("edit") >= 0) gUpdate = true;
        } else gController = false;
        if (Util.isEmpty(table.getEntity()))
            gPojo = gDao = gController = gMapper = gDict = false;
        else
            gPojo = true;
        if (gDict || !Util.isEmpty(allCodeList)) {
            boolean onlyFK = !gDict;
            if (gDict) dataDict();
            ScriptUtil.dbScript(generatorPath, table, tabFields, joinFields, ScriptUtil.MYSQL, allCodeList,onlyFK);
            ScriptUtil.dbScript(generatorPath, table, tabFields, joinFields, ScriptUtil.SQLSERVER, allCodeList,onlyFK);
            ScriptUtil.dbScript(generatorPath, table, tabFields, joinFields, ScriptUtil.ORACLE, allCodeList,onlyFK);
        }
        if (gPojo)
            PojoUtil.pojo(generatorPath, table, tabFields, joinFields, calFields);
        if (gDao)
            dao();
        if (gMapper)
            MapperUtil.mapper(generatorPath, table, tabFields, joinFields, calFields);
        if (gMessage) {
            MessageUtil.message(generatorPath, table, tabFields, joinFields, calFields);
            // fieldsJson();
        }
        if (gController || gList || gUpdate)
            setupFreeMarker(table.getTemplate());
        if (gController)
            controller();
        if (gList || gUpdate)
            route(gList,gUpdate);
        if (gList)  // list update 修改了 fields，最后操作
            list();
        if (gUpdate)
            update();
        if (gList || gUpdate)
            rules();
        if (gController || gList || gUpdate)
            clearCache();
    }


    public void setTable(String projectId, String tabCode) throws CgException {
        assert (!Util.isEmpty(tabCode));
        mapForFreeMaker = null;
        table = dtoService.getTableByProjectIdAndCode(projectId, tabCode);
        if (table == null) {
            throw new CgException("表定义为空");
        } else {
            System.out.println("Generate : "+tabCode);
            if (!Util.isEmpty(table.getActionList())) table.setActionList(","+table.getActionList()+",");
        }
        dtoService.resetFieldList(table, tabFields, joinFields, calFields);
        log.debug("Generate code: {} , table columns: {} join columns: {} calcuture columns: {}\n", table.getCode(), tabFields.size(), joinFields.size(), calFields.size());
        String pn = NameUtil.projectName(table);
        if (!Util.isEmpty(generatorPath) && !writedProjectName.contains("[" + pn + "]")) {
            new File(NameUtil.dataDictName(generatorPath, table)).delete();
            new File(NameUtil.actionSqlName(generatorPath, table, ScriptUtil.MYSQL)).delete();
            new File(NameUtil.actionSqlName(generatorPath, table, ScriptUtil.SQLSERVER)).delete();
            new File(NameUtil.actionSqlName(generatorPath, table, ScriptUtil.ORACLE)).delete();
            new File(NameUtil.dbScriptName(generatorPath, table, ScriptUtil.MYSQL)).delete();
            new File(NameUtil.dbScriptName(generatorPath, table, ScriptUtil.SQLSERVER)).delete();
            new File(NameUtil.dbScriptName(generatorPath, table, ScriptUtil.ORACLE)).delete();
            // appendToFile("use iotequ;\n", NameUtil.dbScriptName(generatorPath, table, ScriptUtil.MYSQL));
            // appendToFile("use iotequ;\n", NameUtil.dbScriptName(generatorPath, table, ScriptUtil.SQLSERVER));
            String oracle_init = NameUtil.dbPath(generatorPath) + "oracle_init.sql";
            new File(oracle_init).delete();
            appendToFile("/*第一次执行脚本需要先单独执行以下注释中脚本*/\n" +
                    "CREATE USER \"IOTEQU\"\n" +
                    "     identified by \"iotequ\"\n" +
                    "     default tablespace users\n" +
                    "     temporary tablespace temp\n" +
                    "     profile DEFAULT;\n" +
                    "GRANT connect,resource,dba to \"IOTEQU\";\n" +
                    "GRANT unlimited tablespace to \"IOTEQU\";\n" +
                    "CREATE OR REPLACE\n" +
                    "	PROCEDURE \"IOTEQU\".\"REMOVE_OBJECT\"(TYP VARCHAR , OBJ VARCHAR , OWN VARCHAR) \n" +
                    "	IS\n" +
                    "		num NUMBER;\n" +
                    "	BEGIN\n" +
                    "	  SELECT count(1) INTO num FROM ALL_OBJECTS  WHERE OBJECT_NAME = OBJ AND OBJECT_TYPE = TYP AND OWNER = OWN;\n" +
                    "		IF num > 0 THEN\n" +
                    "	    EXECUTE IMMEDIATE 'DROP '||typ||' \"'||obj||'\"';\n" +
                    "	  END IF;\n" +
                    "	END;\n" +
                    "/\n" +
                    "CREATE OR REPLACE\n" +
                    "	PROCEDURE \"IOTEQU\".\"CALC_SEQUENCE\"(tab varchar2,pk varchar2) AS\n" +
                    "	n number(10);\n" +
                    "	m number(10);\n" +
                    "	BEGIN\n" +
                    " 		execute immediate 'select SEQUENCE_'||tab||'.nextval from dual' into n;\n" +
                    " 		execute immediate 'select max('||pk||') from '||tab into m; \n" +
                    " 		execute immediate 'alter sequence SEQUENCE_'||tab||' increment by '||to_char(m-n);\n" +
                    " 		execute immediate 'select SEQUENCE_'||tab||'.nextval from dual' into n;\n" +
                    " 		execute immediate 'alter sequence SEQUENCE_'||tab||' increment by 1';\n" +
                    "	END;\n" +
                    "/\n" +
                    "CREATE OR REPLACE TRIGGER IOTEQU_LOGON_TRIGGER\n" +
                    "	AFTER LOGON ON DATABASE\n" +
                    "	BEGIN\n" +
                    "	EXECUTE IMMEDIATE 'ALTER SESSION SET NLS_DATE_FORMAT=''YYYY-MM-DD HH24:MI:SS''';\n" +
                    "	END;\n" +
                    "/\n", oracle_init);

            new File(NameUtil.languagePath(generatorPath, table, false) + "message_en.properties").delete();
            new File(NameUtil.languagePath(generatorPath, table, false) + "message_zh_CN.properties").delete();
            writedProjectName += "[" + pn + "]";
        }
    }

    public void dao() throws CgException {
        assert (table != null && tabFields.size() > 0);
        if (Util.isEmpty(table.getName())) return;
        String entity = table.getEntity();
        CgField pk = priKey(table, tabFields, joinFields);
        String filePath = NameUtil.baseJavaPath(generatorPath, table, false) + "/" + DAO + "/" + entity + StringUtil.firstLetterUpper(DAO) + ".java";
        StringBuilder sb = newBuilder();
        sb.append("package " + NameUtil.basePackage(table) + "." + DAO + ";\n");
        sb.append("import org.apache.ibatis.annotations.Param;\n");
        sb.append("import org.apache.ibatis.annotations.Update;\n");
        sb.append("import top.iotequ.framework.service.IDaoService;\n");
        sb.append("import "
                + NameUtil.basePackage(table) + "." + POJO + "." + entity + ";\n");
        sb.append("import java.util.*;\n");
        sb.append("\n//  Mapper dao : " + entity + "Dao (" + table.getTitle() + "数据访问接口)\n");
        sb.append("public interface " + entity + StringUtil.firstLetterUpper(DAO) + "  extends IDaoService<" + entity +"> {\n");
        sb.append("    int insert(" + entity + " record);\n");
        if (pk != null && (pk.getKeyType().equals("1") || pk.getKeyType().equals("2")))
            sb.append("    int insertBatchWithoutId(List<" + entity + "> list);\n");
        sb.append("    int insertBatchWithId(List<" + entity + "> list);\n");
        List<CgField> dbFields = databaseFields(tabFields, joinFields);
        for (CgField key : dbFields) {     //  1 自动增长int 2 uuid 3 普通主键 4 唯一索引 5 多记录索引
            if (Util.isEmpty(key.getKeyType()) || key.getKeyType().equals("0") || key.getKeyType().equals("11") || key.getKeyType().equals("12") || key.getKeyType().equals("13"))
                continue;
            String idTail = (key == pk) ? "" : "By" + StringUtil.firstLetterUpper(key.getEntityName());
            String retType = key.getKeyType().equals("1") || key.getKeyType().equals("2") || key.getKeyType().equals("3") || key.getKeyType().equals("4") ? entity : "List<" + entity + ">";
            sb.append("    " + retType + " select" + idTail + "(" + TypeUtil.javaType(key) + " " + key.getEntityName() + ");\n");
            if (key == pk && pk.getShowType().equals("right_join"))
                sb.append("    " + retType + " realSelect" + idTail + "(" + TypeUtil.javaType(key) + " " + key.getEntityName() + ");\n");
            sb.append("    int delete" + idTail + "(" + TypeUtil.javaType(key) + " " + key.getEntityName() + ");\n");
            sb.append("    int deleteBatch" + idTail + "(String " + key.getEntityName() + "s);\n");
        }
        for (int i = 11; i <= 13; i++) {
            List<CgField> uf = unionUniqueIndexFields(tabFields, joinFields, ScriptUtil.MYSQL, String.valueOf(i));
            if (uf != null && uf.size() > 0) {
                String idTail = "By";
                String params = "";
                for (int index = 0; index < uf.size(); index++) {
                    idTail += StringUtil.firstLetterUpper(uf.get(index).getEntityName());
                    params += (index != 0 ? " , " : "") + "@Param(\"" + uf.get(index).getEntityName() + "\")" + TypeUtil.javaType(uf.get(index)) + " " + uf.get(index).getEntityName();
                }
                String retType = entity;
                sb.append("    " + retType + " select" + idTail + "(" + params + ");\n");
                sb.append("    int delete" + idTail + "(" + params + ");\n");
            }
        }
        if (pk != null) {
            sb.append("    int deleteList(List<" + entity + "> list);\n");
            if (parentEntity(table, tabFields, joinFields) != null)
                sb.append("    List<" + entity + "> selectTree(@Param(\"id\")" + TypeUtil.javaType(pk) + " id);  // 选全部参数为null\n");
            sb.append("    int update(" + entity + " record);\n");
            sb.append("    int updateSelective(" + entity + " record);\n");
            sb.append("    int updateBy(@Param(\"record\")" + entity + " record,@Param(\"" + pk.getEntityName() + "\")" + TypeUtil.javaType(pk) + " id);\n");
        }
        sb.append("    //条件为所有非空字段 and，String采用like的查询模式，其他为=\n");
        sb.append("    List<" + entity + "> list(" + entity + " record);\n");
        if (joinFields.isEmpty())
            sb.append("    //!!!  如 whereString=\"user_name like '王%'\" ,orderString=\"born_date desc,code asc\"\n");
        else
            sb.append("    //!!!  如 whereString=\"db_table_name.user_name like '王%'\" ,orderString=\"db_table_name.born_date desc,db_table_name.code asc\"\n");
        sb.append("    List<" + entity + "> listBy(@Param(\"whereString\")String whereString,@Param(\"orderString\")String orderString);\n");
        sb.append("    List<" + entity + "> query(@Param(\"sqlString\")String sqlString);\n");
        sb.append("    @Update(\"${sqlString}\")\n" +
                "    int execute(@Param(\"sqlString\") String sqlString);\n");
        sb.append("}\n");
        writeToFile(sb.toString(), filePath);
    }

    public void dataDict() throws CgException {
        assert (table != null);
        if (Util.isEmpty(table.getName())) return;
        String dFile = NameUtil.dataDictName(generatorPath, table);
        appendToFile(String.format("\n表：%s : %s\n", table.getName(), table.getTitle()), dFile);
        for (CgField f : tabFields) {
            if (Util.isEmpty(f.getName())) continue;
            String note = "";
            if (Util.isEmpty(f.getDictTable()) && !Util.isEmpty(f.getDictField()) && !Util.isEmpty(f.getDictText())) {
                String[] cc = f.getDictField().split(",");
                String[] tt = f.getDictText().split(",");
                for (int i = 0; i < cc.length; i++) note = note + cc[i] + ":" + tt[i] + "  ";
            }
            appendToFile(String.format("%s\t%s\t%s\t%s\t%s\n", f.getName(), f.getTitle(), f.getType(), f.getIsNull() ? "√" : "✘", note), dFile);
        }
        for (CgField f : joinFields) {
            if (Util.isEmpty(f.getName())) continue;
            String note = "";
            if (Util.isEmpty(f.getDictTable()) && !Util.isEmpty(f.getDictField()) && !Util.isEmpty(f.getDictText())) {
                String[] cc = f.getDictField().split(",");
                String[] tt = f.getDictText().split(",");
                for (int i = 0; i < cc.length; i++) note = note + cc[i] + ":" + tt[i] + "  ";
            }
            appendToFile(String.format("%s\t%s\t%s\t%s\t%s\n", f.getName(), f.getTitle(), f.getType(), f.getIsNull() ? "√" : "✘", note), dFile);
        }
    }

    private void setupFreeMarker(String subdir) {
        if (subdir != null) {
            CONFIGURATION.setTemplateLoader(
                    new ClassTemplateLoader(GenService.class, "/templates/codegenerator/" + subdir));
        } else {
            CONFIGURATION.setTemplateLoader(
                    new ClassTemplateLoader(GenService.class, "/templates/codegenerator"));
        }
        CONFIGURATION.setDefaultEncoding("UTF-8");
        CONFIGURATION.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        CONFIGURATION.setCacheStorage(NullCacheStorage.INSTANCE);
    }

    public Template getTemplate(String templateName) throws CgException {
        try {
            return CONFIGURATION.getTemplate(templateName);
        } catch (Exception e) {
            throw new CgException(e);
        }
    }

    public void clearCache() {
        CONFIGURATION.clearTemplateCache();
    }

    /**
     * @return 修改了字段属性的所有字段表
     * @throws CgException 异常
     */
    public List<CgField> getAllFieldsIncludeSplitedJoinFields() throws CgException {
        List<CgField> list = new ArrayList<CgField>();
        list.addAll(tabFields);
        for (CgField f : joinFields) {
            // vue 模式下不再需要转换 list.add(JoinUtil.convertedJoinField(table, f));
            list.add(f);
            list.addAll(JoinUtil.splitedJoinFields(table, f, tabFields, joinFields, calFields));
        }
        list.addAll(calFields);
        fieldListSort(list);
        return list;
    }

    // sons : listName或formName|u或c|component|path|title,...
    //        c定义component
    // sonFields : sf|mf的子表主表关联字段对序列,mf为主表主键时省略|mf
    List<Map<String, String>> getSons(CgList cgList) throws CgException {
        boolean useFlow = cgList.getActionList() != null && cgList.getActionList().indexOf("flow") >= 0;
        if (cgList.getGeneratorType() == 0 || (Util.isEmpty(cgList.getSons()) && !useFlow)) return null;
        else {
            CgField pk = priKey(table, tabFields, joinFields);
            List<Map<String, String>> sons = new ArrayList<>();
            if (useFlow) {
                Map<String, String> m = new HashMap<>();
                m.put("component", "CgFlow");
                m.put("title", "system.action.flow");
                m.put("mode", "component");
                m.put("viewProperties", ":url=\"baseUrl+'/list'\"");
                m.put("entityField", "flowId");
                m.put("pk", pk.getEntityName());
                sons.add(m);
                if (Util.isEmpty(cgList.getSons())) return sons;
            }
            String[] ss = cgList.getSons().split(",");
            String[] ff = cgList.getSonFields().split(",");
            String sonProperties = cgList.getSonsProperties();
            String[] sonPropertiesArray = Util.isEmpty(sonProperties) ? null : sonProperties.split(",");

            for (int i = 0; i < ss.length && i < ff.length; i++) {
                String[] sss = ss[i].split("\\|");
                if (sss[0].equals("c")) {
                    Map<String, String> m = new HashMap<>();
                    m.put("component", sss[1]);
                    m.put("componentPath", sss[2]);
                    m.put("title", sss[3]);
                    m.put("mode", "component");
                    String[] f_k = ff[i].split("\\|");
                    m.put("entityField", f_k[0]);
                    if (f_k.length > 1) m.put("pk", f_k[1]);
                    else m.put("pk", pk.getEntityName());
                    if (Objects.nonNull(sonPropertiesArray) && sonPropertiesArray.length > i && !Util.isEmpty(sonPropertiesArray[i]))
                        m.put("viewProperties", sonPropertiesArray[i]);
                    sons.add(m);
                    continue;
                }
                CgTable son;
                String[] f_define = ff[i].split("\\|");
                CgField sonField;
                Map<String, String> m = new HashMap<>();
                if (Objects.nonNull(sonPropertiesArray) && sonPropertiesArray.length > i && !Util.isEmpty(sonPropertiesArray[i]))
                    m.put("viewProperties", sonPropertiesArray[i]);
                if (sss.length == 1 || sss[1].equals("l") || sss[1].equals("f") || sss[1].equals("list")) {
                    CgList item = dtoService.getCgList(sss[0]);
                    if (Objects.isNull(item)) throw new CgException("子表引用的list定义不存在(name=" + sss[0] + ")");
                    son = dtoService.selectTable(item.getTableId());
                    if (Objects.isNull(son)) throw new CgException("子表引用的list定义对应的table定义不存在(id=" + item.getTableId() + ")");
                    sonField = dtoService.getFieldIncludeJoinBy(son, f_define[0]);
                    if (Util.isEmpty(item.getSons())) {
                        m.put("component", NameUtil.componentName(item));
                        m.put("componentPath", NameUtil.componentPath(item));
                    } else {
                        m.put("component", NameUtil.listViewComponentName(item));
                        m.put("componentPath", NameUtil.listViewComponentPath(item));
                    }
                    m.put("title", NameUtil.generatorName(son) + ".title." + item.getPath());
                } else if (sss[1].equals("u") || sss[1].equals("record")) {
                    CgForm item = dtoService.getCgForm(sss[0]);
                    if (Objects.isNull(item)) throw new CgException("子表引用的form定义不存在(name=" + sss[0] + ")");
                    son = dtoService.selectTable(item.getTableId());
                    if (Objects.isNull(son)) throw new CgException("子表引用的form定义对应的table定义不存在(id=" + item.getTableId() + ")");
                    sonField = dtoService.getFieldIncludeJoinBy(son, f_define[0]);
                    m.put("component", NameUtil.componentName(item));
                    m.put("componentPath", NameUtil.componentPath(item));
                    m.put("title", NameUtil.generatorName(son) + ".title." + item.getPath());
                } else throw new CgException("sons定义不正确(sons=" + ss[i] + ")");
                m.put("codeCamel", StringUtil.camelString(son.getCode()));
                m.put("code", son.getCode());
                m.put("name", son.getName());
                m.put("fk_field", sonField.getName());
                m.put("entityField", sonField.getEntityName());  // 子表对应外键
                if (sss.length > 1 && sss[1].toLowerCase().startsWith("u")) {
                    m.put("mode", "update");
                } else {
                    m.put("mode", "list");
                    if (sss.length > 1 && sss[1].toLowerCase().startsWith("f") && son.getName() != null && !son.getName().equals(table.getName())) {
                        m.put("fastExport", NameUtil.basePackage(son));
                        m.put("entity", son.getEntity());
                        CgField sonpk = priKey(son);
                        if (sonpk != null) {
                            m.put("pk_field", sonpk.getName());
                            m.put("pk_type", sonpk.getKeyType());
                        }
                    }
                }
                List<CgListField> cgListFields = dtoService.getCgListFields(cgList.getId());
                List<CgListField> fatherField = cgListFields.stream().filter(f -> sonField.getEntityName().equals(f.getEntityField())).collect(Collectors.toList());
                if (fatherField.isEmpty()) {
                    CgListField fat = new CgListField();
                    fat.setQueryMode(0);
                    fat.setWidth(0);
                    fat.setAlign("left");
                    fat.setExpand(false);
                    fat.setFix(false);
                    fat.setOverflowTooltip(false);
                    fat.setEntityField(sonField.getEntityName());
                    fat.setHeaderAlign("left");
                    fat.setListId(cgList.getId());
                    fat.setOrderNum(10);
                    fat.setHidden(true);
                    fat.setEditInline(false);
                    dtoService.insert(fat);
                } else {
                    dtoService.update(fatherField.get(0));
                }
                if (f_define.length > 1) {
                    for (CgField f : dtoService.getAllFieldsNotSplitedJoin(tabFields, joinFields, calFields)) {
                        if (f_define[1].equals(f.getName()) || f_define[1].equals(f.getEntityName())) {
                            m.put("pk", f.getEntityName());
                            m.put("pk_field", f.getName());
                            break;
                        }
                    }
                } else if (pk != null) {
                    m.put("pk", pk.getEntityName());
                    m.put("pk_field", pk.getName());
                } else throw new CgException("外键定义的主表关联字段未定义");
                sons.add(m);
            }
            if (sons.size() > 0) {
                return sons;
            } else return null;
        }
    }

    Map<String, Object> dataMap() throws CgException {
        flowController = false;
        Map<String, Object> map = new HashMap<String, Object>();
        CgField pk = priKey(table, tabFields, joinFields);
        map.put("rightJoinPK", pk != null && "right_join".equals(pk.getShowType()));
        String createTimeField = "";
        Integer maxNoteLength = 100;
        List<CgField> allFields = getAllFieldsIncludeSplitedJoinFields();
        if (!Util.isEmpty(table.getActionList()) && table.getActionList().indexOf("flow")>=0) {
            FlowUtil.setFlowField(allFields,IFlowService.flowSelection,null,null);
            FlowUtil.setFlowField(allFields,IFlowService.flowNote,null,null);
            FlowUtil.setFlowField(allFields,IFlowService.flowNextOperator, table.getFlowDynaFieldsOp(),null);
            FlowUtil.setFlowField(allFields,IFlowService.flowCopyToList, table.getFlowDynaFieldsCp(),null);
        }
        for (CgField f : allFields) {
            if ("2dcode".equals(f.getShowType())) {
                String s = f.getFormatter();
                try {
                    int size = Integer.parseInt(s);
                    if (size > 0) f.setFormatter(String.valueOf(size));
                    else f.setFormatter("128");
                } catch (Exception e) {
                    f.setFormatter("128");
                }
            }
            if ("flow_note".equals(f.getName()) && f.getLength() != null)
                maxNoteLength = f.getLength();
            if (Util.isEmpty(f.getName()) || Util.isEmpty(f.getType()) || f.getType().toLowerCase().indexOf("char") < 0)
                f.setLength(null);
            if ("flow_register_time".equals(f.getName())) createTimeField = "flow_register_time";
            else if ("create_time".equals(f.getName())) createTimeField = "create_time";
            f.setType(TypeUtil.javaType(f)); // controller和html你需要使用，因此它们在最后执行
            if (pk != null && pk.getEntityName().equals(f.getEntityName())) pk = f;
        }
        map.put("createTimeField", createTimeField);
        String imp = table.getControllerExtends();
        if (!Util.isEmpty(imp)) {
            imp = PojoUtil.addImplementsItem(imp, "BaseController<" + table.getEntity() + ">", "extends");
            table.setControllerExtends(imp);
        }
        map.put("table", table);
        map.put("fields", allFields);
        map.put("pk", pk);
        map.put("basePackage", NameUtil.basePackage(table));
        map.put("moduleName", NameUtil.projectName(table).toLowerCase());
        map.put("subModule", (Util.isEmpty(table.getSubPackage()) ? null : table.getSubPackage().trim()));
        map.put("generatorName", NameUtil.generatorName(table));
        if (table.getActionList() != null && table.getActionList().contains("flow")) {
            List<Map<String, Object>> cc = dtoService.query("SELECT path, icon FROM cg_form where is_flow = 1 and table_id = ?", table.getId());
            if (cc != null && cc.size() > 0) {
                map.put("flowList", cc);
                map.put("maxNoteLength", maxNoteLength);
                flowController = true;
            }
        }
        map.put("tableTag", NameUtil.tableTag(table));
        map.put("DAO", DAO);
        map.put("POJO", POJO);
        map.put("CONTROLLER", CONTROLLER);
        List<CgButton> buttons = dtoService.query(CgButton.class, "select * from cg_button where table_id=? order by order_num", table.getId());
        map.put("buttons", buttons);
        map.put("hasSonTables", !Util.isEmpty(table.getName()) &&
                SqlUtil.sqlExist(false, "SELECT * FROM cg_field f, cg_table t where f.table_id = t.id and fk_table = ? and fk_on_delete = 'CASCADE' and t.project_id = ?", table.getName(), table.getProjectId()));
        return map;
    }

    void freeMarkerWriter(final String ftl, String filePath, Map<String, Object> additional) throws CgException {
        Template template;
        try {
            writeToFile("", filePath); // 建立文件目录结构
            template = getTemplate(ftl);
            FileOutputStream fos = new FileOutputStream(filePath);
            Writer out = new BufferedWriter(new OutputStreamWriter(fos, "utf-8"), 102400);
            if (mapForFreeMaker == null) mapForFreeMaker = dataMap();
            Map<String, Object> p = mapForFreeMaker;
            if (additional != null)
                p.putAll(additional);
            template.process(p, out);
            out.close();
        } catch (Exception e) {
            throw new CgException(e);
        }
    }


    public void controller() throws CgException {
        String filePath = NameUtil.controllerPath(generatorPath, table, false);
        freeMarkerWriter("controller.ftl", filePath, null);
        String gn = NameUtil.generatorName(table);
        filePath = NameUtil.baseJavaPath(generatorPath, table, false) + "/service/impl/Cg" + StringUtil.firstLetterUpper(gn)
                + "Service" + ".java";
        freeMarkerWriter("service.ftl", filePath, null);
        if (flowController) {
            filePath = NameUtil.baseJavaPath(generatorPath, table, false) + "/service/impl/" + StringUtil.firstLetterUpper(gn)
                    + "FlowService" + ".java.example";
            freeMarkerWriter("flow.ftl", filePath, null);
        }
    }

    void freeMarkerWriterProject(final String ftl, String filePath, CgProject gp) throws CgException {
        Template template;
        writeToFile("", filePath); // 建立文件目录结构
        template = getTemplate(ftl);
        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            Writer out = new BufferedWriter(new OutputStreamWriter(fos, "utf-8"), 102400);
            Map<String, Object> map = new HashMap<>();
            map.put("gp", gp);
            String mdv = Util.getVersion("framework");
            if (mdv == null) mdv = "3.0.1-SNAPSHOT";
            map.put("mdVersion", mdv);
            map.put("camelName", StringUtil.camelString(gp.getName()));
            template.process(map, out);
            out.close();
        } catch (Exception e) {
            throw new CgException(e);
        }
    }

    public void project(String id) throws CgException {
        generatorPath = env.getProperty("generator.path");
        if (generatorPath == null) throw new CgException("请配置代码生成文件路径 generator.path");
        CgProject gp = cgProjectDao.select(id);
        if (id == null) return;
        setupFreeMarker("project");
        String basePath = NameUtil.serverPath(generatorPath, false) + "/" + gp.getName();
        if (!basePath.endsWith("/")) basePath += "/";

        File path = new File(basePath);
        if (!path.exists()) path.mkdirs();

        freeMarkerWriterProject("pom.ftl", basePath + "pom.xml", gp);
        if (gp.getSpringModule()) {
            freeMarkerWriterProject("yml.ftl", basePath + "src/main/resources/application.yml", gp);
            freeMarkerWriterProject("iotequ.ftl", basePath + "iotequ.yml", gp);
            if (gp.getSpringModule()) freeMarkerWriterProject("starter.ftl", basePath + "src/main/java/" +
                    gp.getGroupId().replace(".", "/") +
                    "/" + StringUtil.firstLetterUpper(StringUtil.camelString(gp.getName())) + "Application.java", gp);
        }
    }

    private List<Map<String, String>> dynaConditionFieldsForForm(List<CgField> fields) {
        List<Map<String, String>> list = new ArrayList<>();
        for (CgField f : fields) {
            if (("select".equals(f.getShowType()) || "radio".equals(f.getShowType()) || "checkbox".equals(f.getShowType())) && !Util.isEmpty(f.getDynaCondition())) {
                String regex = "\\$\\{([^{}]+?)\\}";
                Pattern pattern = Pattern.compile(regex);
                String condition = f.getDynaCondition();
                Matcher matcher = pattern.matcher(condition);
                while (matcher.find()) {
                    Optional<Map<String, String>> found = list.stream().filter(x -> x.get("watchField").equals(matcher.group(1))).findFirst();
                    if (found.isPresent()) {
                        found.ifPresent(x -> x.put("dynaFields", x.get("dynaFields") + "," + f.getEntityName()));
                    } else {
                        Map<String, String> map = new HashMap<>();
                        map.put("watchField", matcher.group(1));
                        map.put("dynaFields", f.getEntityName());
                        list.add(map);
                    }
                }
            }
        }
        return list;
    }

    CgListField defaultListField(CgList list, CgField f) {
        CgListField nf = new CgListField();
        nf.setOrderNum(f.getOrderNum());
        nf.setListId(list.getId());
        nf.setHeaderAlign("left");
        nf.setEntityField(f.getEntityName());
        nf.setOverflowTooltip(false);
        nf.setFix(false);
        nf.setExpand(false);
        nf.setAlign("left");
        nf.setWidth(0);
        nf.setQueryMode(0);
        nf.setEditInline(false);
        nf.setHidden(true);
        return nf;
    }

    private Map<String, Object> appendListProperties(CgList lp, List<CgListField> lists) throws CgException {
        dtoService.resetFieldList(table,tabFields, joinFields, calFields);  // list使用了流程数据的join类型，不能修改流程字段
        List<CgField> allFields = dtoService.getAllFieldsIncludeSplitedJoin(table, tabFields, joinFields, calFields);
        List<Map<String, Object>> list = new ArrayList<>();
        List<Map<String, Object>> joinedFields = new ArrayList<>();
        for (CgListField item : lists) {
            Optional<CgField> of = allFields.stream().filter(f -> item.getEntityField().equals(f.getEntityName())).findFirst();
            if (of.isPresent()) {
                CgField f = of.get();
                /*
                if (item.getWidth() == 0) { // 设置缺省宽度
                    if ("datetime".equals(f.getShowType())) item.setWidth(156);
                    else if ("image".equals(f.getShowType()) || "2dcode".equals(f.getShowType())) item.setWidth(64);
                    else if (f.getShowType().equals("boolean") || f.getShowType().equals("false_if_null"))
                        item.setWidth(10+20*f.getTitle().length()+(f.getQueryMode()>0 ? 30 : 0));
                    else item.setWidth(128);
                }
                 */
                Map<String, Object> map = EntityUtil.mapFromEntity(f);
                map.putAll(EntityUtil.mapFromEntity(item));
                map.put("id", f.getId()); // f.id 可能为join信息
                if (!Util.isEmpty(item.getShowType()))
                    map.put("showType", item.getShowType());  // item 未定义 showType，使用字段的showType
                if (f.getId().startsWith("join:") || f.getId().startsWith("list:")) { // f 为join字段
                    getJoinProperties(allFields,joinedFields,f);
                }
                list.add(map);
            } else {  // 附加字段，目前只支持 text
                System.out.println("Not found: "+table.getName()+"."+item.getEntityField()+" in cglist "+lp.getName());
                Map<String, Object> map = EntityUtil.mapFromEntity(item);
                map.put("type", "String");
                map.put("title", "");
                map.put("showType", "text");
                map.put("isNull", true);
                map.put("entityName", item.getEntityField());
                list.add(map);
            }
        }
        if (lp.getActionList()!=null && (lp.getActionList().indexOf("edit")>=0 || lp.getActionList().indexOf("view")>=0)) {
            allFields.stream().filter(f->"radio".equals(f.getShowType()) || "checkbox".equals(f.getShowType()) || "select".equals(f.getShowType()) ).forEach(f->{
                Optional<CgListField> of = lists.stream().filter(o->o.getEntityField().equals(f.getEntityName())).findFirst();
                if (!of.isPresent()) {
                    Map<String, Object> map = EntityUtil.mapFromEntity(f);
                    map.put("hidden", true);
                    map.put("entityField",f.getEntityName());
                    map.put("listId",lists.get(0).getListId());
                    map.put("orderNum",0);
                    map.put("queryMode",0);
                    list.add(map);
                }
            });
        }
        list.sort((a, b) -> (Integer) a.get("orderNum") - (Integer) b.get("orderNum"));
        Map<String, Object> ret = new HashMap<>();
        ret.put("list", list);
        ret.put("joinOnFields", joinedFields);
        return ret;
    }

    CgFormField defaultFormField(String formId, String entityName) {
        CgFormField nf = new CgFormField();
        nf.setMustInput(false);
        nf.setReadonly(false);
        nf.setWidth(0);
        nf.setEntityField(entityName);
        nf.setFormId(formId);
        nf.setOrderNum(0);
        return nf;
    }

    private void getJoinProperties( List<CgField> allFields,List<Map<String, Object>> joinedFields, CgField f) {
        if (f.getId().startsWith("join:") || f.getId().startsWith("list:")) { // f 为join字段
           String field = f.getId().split(":")[1];  // 引入join字段的本表字段
            if (!joinedFields.stream().filter(m -> field.equals(StringUtil.toString(m.get("entityName")))).findFirst().isPresent()) {  // joinFields中没有匹配的字段
                Map<String, Object> fm = new HashMap<>();
                fm.put("entityName", field);
                allFields.stream().filter(fff -> field.equals(fff.getEntityName())).findFirst().ifPresent(ffff -> { // 查找原字段
                    fm.put("dynaCondition", ffff.getDynaCondition());
                    fm.put("dictField", ffff.getDictField());
                    fm.put("dictText", ffff.getDictText());
                    if (!joinedFields.stream().filter(m -> ffff.getId().equals(StringUtil.toString(m.get("component")))).findFirst().isPresent()) {  // joinFields组件定义不可重复
                        fm.put("component", ffff.getId());
                        fm.put("vue", ffff.getDictTable());
                    }
                });
                joinedFields.add(fm);
            }
        }
    }
    // 将join，或 dict_list 字段插入到字段表，用 id = "join:" 或 "list:" + f.entityName
    //


    private Map<String, Object> appendFormProperties(CgForm form, List<CgFormField> forms) throws CgException {
        List<CgField> allFields = dtoService.getAllFieldsIncludeSplitedJoin(table, tabFields, joinFields, calFields);

        if (form.getIsFlow()!=null && form.getIsFlow()) {
            for (CgFormField f: forms) {
                if (!f.getHidden()) {
                    f.setGroupTitle("system.action.detail");
                    break;
                }
            }
            FlowUtil.setFlowField(allFields, IFlowService.flowSelection, null,"String");
            FlowUtil.setFlowField(allFields, IFlowService.flowNote,null,"String");
            FlowUtil.setFormFlowField(forms, IFlowService.flowSelection,true,"system.action.process");
            FlowUtil.setFormFlowField(forms, IFlowService.flowNote,true,null);
        }
        if (form.getActionList()!=null && (","+form.getActionList()+",").indexOf(",flow,")>=0) {
            FlowUtil.setFlowField(allFields, IFlowService.flowNextOperator,table.getFlowDynaFieldsOp(),"String");
            FlowUtil.setFlowField(allFields, IFlowService.flowCopyToList,table.getFlowDynaFieldsCp(),"String");
            FlowUtil.setFormFlowField(forms, IFlowService.flowNextOperator,form.getIsFlow()!=null && form.getIsFlow(),null);
            FlowUtil.setFormFlowField(forms, IFlowService.flowCopyToList,form.getIsFlow()!=null && form.getIsFlow(),null);
        }
        allFields.stream().forEach(f->f.setType(TypeUtil.javaType(f)));
        List<Map<String, Object>> list = new ArrayList<>();
        boolean useTabGroup = false;
        List<Map<String, Object>> joinedFields = new ArrayList<>();
        // item : form_field
        //    f : join field
        // ffff : 原表join字段，可以不在 fields里
        for (CgFormField item : forms) {
            Optional<CgField> of = allFields.stream().filter(f -> item.getEntityField().equals(f.getEntityName())).findFirst();
            if (of.isPresent()) {
                CgField f = of.get();
                if (!Util.isEmpty(item.getGroupTitle())) {
                    useTabGroup = true;
                    if (item.getGroupTitle().indexOf(".") <= 0)
                        item.setGroupTitle(NameUtil.generatorName(table) + ".title." + "group" + StringUtil.firstLetterUpper(form.getName()) + StringUtil.firstLetterUpper(item.getEntityField()));
                }
                Map<String, Object> map = EntityUtil.mapFromEntity(f);
                map.putAll(EntityUtil.mapFromEntity(item));
                map.put("showType", Util.isEmpty(item.getShowType(), f.getShowType()));  // item 未定义 showType，使用字段的showType
                map.put("id", f.getId()); // f.id 可能为join信息
                if (f.getId().startsWith("join:") || f.getId().startsWith("list:")) { // f 为join字段
                    getJoinProperties(allFields,joinedFields,f);
                }
                list.add(map);
            } else {  // 附加字段，目前只支持 text
                System.out.println("Not found: "+table.getName()+"."+item.getEntityField()+" in cgform "+form.getName());
                Map<String, Object> map = EntityUtil.mapFromEntity(item);
                map.put("type", "String");
                map.put("title", "");
                map.put("showType", Util.isEmpty(item.getShowType(), "text"));
                map.put("isNull", true);
                map.put("entityName", item.getEntityField());
                list.add(map);
            }
        }
        String formId = forms.get(0).getFormId();
        tabFields.stream().filter(f -> !f.getIsNull() // 将所有不能为空的forms没有定义的字段插入到表单
                && !forms.stream().filter(i -> Util.equals(f.getEntityName(), i.getEntityField())).findFirst().isPresent())
                .forEach(af -> {
                    CgFormField nf = defaultFormField(formId, af.getEntityName());
                    nf.setHidden(true);
                    Map<String, Object> map = EntityUtil.mapFromEntity(af);
                    map.putAll(EntityUtil.mapFromEntity(nf));
                    list.add(map);
                });
        list.sort((a, b) -> (Integer) a.get("orderNum") - (Integer) b.get("orderNum"));
        Map<String, Object> newItem = null;
        int i = 0;
        for (i = 0; i < list.size(); i++) {
            Map<String, Object> item = list.get(i);
            if ("password".equals(item.get("showType").toString())
                    && Util.boolValue(item.get("mustInput"))
                    && !Util.boolValue(item.get("hidden"))) {
                newItem = EntityUtil.mapCopy(item);
                int w = Integer.valueOf(item.get("width").toString()) / 2;
                Object ip = item.get("formItemProperties");
                String nip = "v-if=\"isNew\"" + (ip == null ? "" : " " + ip.toString());

                item.put("width", w);
                item.put("formItemProperties", nip);

                newItem.put("formItemProperties", nip);
                newItem.put("width", w);
                newItem.put("title", "system.message.passwordConfirm");
                newItem.put("entityName", "passwordConfirm");
                newItem.put("name", null);
                newItem.put("validExpression", "js:value === this.record." + item.get("entityName").toString());
                break;
            }
        }
        if (newItem != null) list.add(i + 1, newItem);
        if (useTabGroup && Util.isEmpty(list.get(0).get("groupTitle"))) {
            list.get(0).put("groupTitle", "system.layout.base");
        }
        Map<String, Object> ret = new HashMap<>();
        ret.put("list", list);
        ret.put("joinOnFields", joinedFields);
        List<Map<String, String>> dynaList = dynaConditionFieldsForForm(allFields);
        if (Objects.nonNull(dynaList) && dynaList.size() > 0)
            ret.put("dynaList", dynaList);
        return ret;
    }

    public void list() throws CgException {
        String filePath = NameUtil.vueViewPath(generatorPath, table, false);
        List<CgList> lists = dtoService.getCgLists(table.getId());
        if (mapForFreeMaker == null) mapForFreeMaker = dataMap();
        if (lists != null && !lists.isEmpty()) {
            for (CgList item : lists) {
                Map<String, Object> map = new HashMap<>();
                List<CgListField> usedFields = dtoService.getCgListFields(item.getId());
                if (Objects.isNull(usedFields) || usedFields.isEmpty()) continue;
                Map<String, Object> listProperties = appendListProperties(item, usedFields);
                mapForFreeMaker.put("fields", listProperties.get("list"));
                mapForFreeMaker.put("joinOnFields", listProperties.get("joinOnFields"));
                map.put("LP", item);
                if (item.getPath().equals("list")) map.put("sons", getSons(item)); // 仅 list 表生成主从表
                else map.put("sons", null);
                freeMarkerWriter("list_view.ftl", filePath + "/" + item.getPath() + ".vue", map);
                freeMarkerWriter("list.ftl", filePath + "/CgList" + StringUtil.firstLetterUpper(item.getName()) + ".vue", map);
            }
        }
    }

    public void update() throws CgException {
        String filePath = NameUtil.vueViewPath(generatorPath, table, false);
        List<CgForm> forms = dtoService.getCgForms(table.getId());
        if (mapForFreeMaker == null) mapForFreeMaker = dataMap();
        if (forms != null && !forms.isEmpty()) {
            for (int i=0;i<forms.size();i++) {
                CgForm item = forms.get(i);
                Map<String, Object> map = new HashMap<>();
                map.put("FP", item);
                List<CgFormField> usedFields = dtoService.getCgFormFields(item.getId());
                if (Objects.isNull(usedFields) || usedFields.isEmpty()) continue;
                Map<String, Object> formProperties = appendFormProperties(item, usedFields);
                mapForFreeMaker.put("fields", formProperties.get("list"));
                mapForFreeMaker.put("joinOnFields", formProperties.get("joinOnFields"));
                mapForFreeMaker.put("dynaList", formProperties.get("dynaList"));
                freeMarkerWriter("update_view.ftl", filePath + "/" + item.getPath().split(",")[0] + ".vue", map);
                freeMarkerWriter("update.ftl", filePath + "/CgForm" + StringUtil.firstLetterUpper(item.getName()) + ".vue", map);
            }
        }
    }

    private List<Map<String, Object>> rulesList() throws CgException {
        List<Map<String, Object>> list = new ArrayList<>();
        List<CgForm> forms = dtoService.getCgForms(table.getId());
        List<CgList> lists = dtoService.getCgLists(table.getId());
        CgField pk=priKey(table,tabFields,joinFields);
        for (CgField f: tabFields) {
            boolean hasRule = false;
            boolean mustInput = false;
            if (pk!=null && pk.getEntityName().equals(f.getEntityName())) continue;
            if (forms!=null) {
                for (CgForm fm : forms) {
                    try {
                        Integer mi = SqlUtil.sqlQueryInteger(false, "select must_input from cg_form_field where (hidden is null or hidden=0) and entity_field = ? and form_id=?",
                                f.getEntityName(), fm.getId());
                        if (mi != null) {
                            hasRule = true;
                            mustInput = Util.boolValue(mi.toString());
                            break;
                        }
                    }catch (Exception e) {
                        throw new CgException(e);
                    }
                }
            }
            if (!hasRule && lists!=null) {
                for (CgList l : lists) {
                    if (l.getEditInline()!=null && l.getEditInline()) {
                        if (SqlUtil.sqlExist(false, "select * from cg_list_field where (hidden is null or hidden=0) and edit_inline=1 and entity_field = ? and list_id= ?",
                                f.getEntityName(),l.getId())) {
                            hasRule=true;
                            break;
                        }
                    }
                }
            }
            if (f.getIsNull()!=null && f.getIsNull() && !mustInput
                    && Util.isEmpty(f.getValidExpression()) && !"url".equals(f.getShowType()) && !"email".equals(f.getShowType())
                    && !"mobile".equals(f.getFormatter()) && !"fixed".equals(f.getFormatter()) ) continue;
            else if (f.getShowType().equals("number") && f.getIsNull()!=null && f.getIsNull() && !mustInput) continue;
            if (hasRule) {
                Map<String, Object> map = EntityUtil.mapFromEntity(f);
                map.put("mustInput",mustInput);
                list.add(map);
            }
        }
        return list;
    }


    public void rules() throws CgException {
        String filePath = NameUtil.vueViewPath(generatorPath, table, false);
        if (mapForFreeMaker == null) mapForFreeMaker = dataMap();
        mapForFreeMaker.put("fields", rulesList());
        freeMarkerWriter("rules.ftl", filePath + "/rules.js", null);
    }
    public void route(boolean gList,boolean gUpdate) throws CgException {
        Map<String, Object> map = new HashMap<>();
        if (gList) {
            List<CgList> lists = dtoService.getCgLists(table.getId());
            if (lists != null && !lists.isEmpty()) map.put("lists", lists);
        }
        if (gUpdate) {
            List<CgForm> forms = dtoService.getCgForms(table.getId());
            if (forms != null && !forms.isEmpty()) map.put("forms", forms);
        }
        if (!map.isEmpty()) {
            String filePath = NameUtil.vueViewPath(generatorPath, table, false) + NameUtil.routeJs(table);
            freeMarkerWriter("route.ftl", filePath, map);
        }
    }

    public int flowSync(String projectId, String code) throws CgException {
        int scanFiles = 0;
        CgTable t = dtoService.getTableByProjectIdAndCode(projectId, code);
        if (t == null) throw new CgException("流程定义引用的表单定义 " + code + " 不存在");
        List<CgField> ff = dtoService.getFields(t.getId());
        List<Map<String, Object>> cc = dtoService.query("select id from cg_table where id!=? and name=? and project_id=?",
                t.getId(), t.getName(), t.getProjectId());
        if (cc != null && cc.size() > 0) {
            for (Map<String, Object> m : cc) {
                String id = m.get("id").toString().trim();
                dtoService.execute("delete from cg_field where table_id=?", id);
                for (CgField f : ff) {
                    f.setId(null);
                    f.setTableId(id);
                    dtoService.insert(f);
                }
                scanFiles++;
            }
        }
        return scanFiles;
    }
}
