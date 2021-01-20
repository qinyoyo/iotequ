package top.iotequ.codegenerator.service.impl;

import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.iotequ.codegenerator.dao.CgFieldDao;
import top.iotequ.codegenerator.dao.CgTableDao;
import top.iotequ.codegenerator.dao.SchemaDao;
import top.iotequ.codegenerator.pojo.*;
import top.iotequ.codegenerator.util.TypeUtil;
import top.iotequ.framework.exception.IotequException;
import top.iotequ.framework.exception.IotequThrowable;
import top.iotequ.framework.flow.IFlowService;
import top.iotequ.framework.service.impl.SqlService;
import top.iotequ.util.*;
import top.iotequ.util.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AutoGenService {
    @Autowired
    DtoService dtoService;
    @Autowired
    SchemaDao dao;
    @Autowired
    private Environment env;
    @Autowired
    private GenService genService;
    @Autowired
    SchemaDao schemaDao;
    @Autowired
    SqlService sqlService;
    @Autowired
    CgTableDao tableDao;
    @Autowired
    CgFieldDao fieldDao;

    static public String optionSelected = "";
    private static final Logger log = LoggerFactory.getLogger(AutoGenService.class);
     /**
     * 根据数据库表名自动批量创建表定义
     * @param tableNames 数据库表名列表
     * @return RestJson
     */
    public RestJson batchAutoCreate(String tableNames) {
        RestJson j = new RestJson();
        if (Util.isEmpty(tableNames)) {
            j.setSuccess(false);
            j.setErrorCode(IotequThrowable.NULL_OBJECT,"");
        }
        try {
            String [] nn = tableNames.split(",");
            String created="";
            for (String n:nn) {
                if (Util.isEmpty(n)) continue;
                String id = autoCreate(n);
                if (!Util.isEmpty(id)) {
                    if (created.isEmpty()) created = n;
                    else created += (","+n);
                }
            }
            j.setSuccess(!created.isEmpty());
            j.setMessage(created.isEmpty()?"没有表可导入，请检查命名规则且项目是否创建":("导入表(满足命名规则且项目已创建):"+created));
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMessage(e);
        }
        return j;
    }
    public RestJson fieldsIncludeJoin(String tableId) {
        RestJson j = new RestJson();
        try {
            CgTable tab = dtoService.selectTable(tableId);
            genService.setTable(tab.getProjectId(), tab.getCode());
            List<CgField> list = genService.getAllFieldsIncludeSplitedJoinFields();
            list.sort((a,b)->a.getOrderNum() - b.getOrderNum());
            j.data(list);
        } catch (Exception e) {
            j.setMessage(e);
        }
        return j;
    }

    public Map<String, Object> getNotUsedTablesDictionary() {
        Map<String, Object> map = new HashMap<>();
        try {
            Map<String, String> setting = SqlUtil.getDatabaseSetting(env);
            String databaseName = setting.get("database");
            if (databaseName != null && "mysql".equals(setting.get("databaseId").toLowerCase())) {
                List<Table> tableList = schemaDao.notUsedTables(databaseName);
                if (!Util.isEmpty(tableList)) {
                    List<Map<String,Object>> list = new ArrayList<>();
                    tableList.forEach(t->{
                        list.add(new HashMap<String,Object>(){{
                            put("value",t.getTableName());
                            put("text", Util.isEmpty(t.getTableComment(),""));
                        }});
                    });
                    map.put("dictNotUsedTables",list);
                }
            }

        } catch (IotequException e) {
        }
        return map;
    }

    public RestJson doSync(String options, String tableIds) {
        RestJson j = new RestJson();
        if (options == null)
            options = "";
        optionSelected = options;
        int syncTables = 0;

        if (!Util.isEmpty(tableIds)) {
            String[] ids = tableIds.split(",");
            for (String id : ids) {
                try {
                    log.info("Sync {} ...",id);
                    CgTable nt = tableDao.select(id);
                    if (nt == null || Util.isEmpty(nt.getName()))   // 数据库表未定义，跳过
                        continue;
                    CgField fc = new CgField();
                    fc.setTableId(id);
                    List<CgField> ff = fieldDao.list(fc);
                    Map<String, Object> db = generateFromDb(nt.getName(), null, null, null);
                    if (db == null) { // 数据库不存在记录
                        if (!options.contains("0"))
                            continue;
                        createDbTable(nt, ff);
                        continue;
                    }
                    CgTable dbT = (CgTable) db.get("table");
                    @SuppressWarnings("unchecked")
                    List<CgField> dbFF = (List<CgField>) db.get("fields");
                    if (Util.isEmpty(nt.getTitle()) && !Util.isEmpty(dbT.getTitle())) {
                        nt.setTitle(dbT.getTitle());
                        tableDao.update(nt);
                    } else if (!Util.isEmpty(nt.getTitle()) && Util.isEmpty(dbT.getTitle())) {
                        sqlService.execute("ALTER TABLE `" + dbT.getName() + "` " + "COMMENT = '"
                                + nt.getTitle() + "'");
                    } else if (!Util.isEmpty(nt.getTitle()) && !Util.isEmpty(dbT.getTitle())) { // 表注释冲突
                        if (options.contains("3"))
                            sqlService.execute("ALTER TABLE `" + dbT.getName() + "` " + "COMMENT = '"
                                    + nt.getTitle() + "'");
                        else {
                            nt.setTitle(dbT.getTitle());
                            tableDao.update(nt);
                        }
                    }
                    if (options.contains("1")) { // 删除表单中数据库不存在的字段
                        deleteFieldNotInDb(ff, dbFF);
                    }
                    if (options.contains("2")) { // 删除表单未定义的数据库字段
                        deleteFieldNotInForm(nt.getName(), ff, dbFF);
                    }
                    combineFields(nt.getName(), nt.getId(), ff, dbFF, options.contains("3"));
                    if (options.contains("4")) { // 同步数据库属性
                        syncDbProperties(ff, dbFF);
                    }
                    syncTables ++;
                } catch (Exception e) {
                    j.setSuccess(false);
                    j.setMessage(e);
                    return j;
                }
            }
            j.setMessage("同步表定义 "+syncTables + " 项");
        }
        return j;
    }
    /**
     * 根据表名获得 表定义，参数根据表名自动解析
     * @param tableName 表名
     * @return 表定义map
     * @throws IotequException 异常
     */
    private Map<String, Object> generateFromDb(@NonNull String tableName) throws IotequException {
        String [] tt=tableName.split("_");
        if (tt.length<2) return null;
        String projectId = SqlUtil.sqlQueryString(false,"select id from cg_project where code=? and creator=?",tt[0], Util.getUser().getName());
        if (Util.isEmpty(projectId)) return null;
        Map<String, String> setting = SqlUtil.getDatabaseSetting(env);
        String databaseName = setting.get("database");
        if (databaseName == null || !"mysql".equals(setting.get("databaseId").toLowerCase()))
            throw new IotequException(IotequThrowable.ERROR, "数据配置错或不支持的数据库类型");
        if (!Util.isEmpty(tableName)) {
            List<Table> tableList = dao.tables(databaseName);
            if (tableList != null && tableList.size() > 0) {
                for (Table t : tableList) {
                    if (tableName.equals(t.getTableName())) {
                        return generateFromDb(tableName,tableName.substring(tt[0].length()+1),projectId,null);
                    }
                }
            }
        }
        return null;
    }

    private Map<String, Object> generateFromDb(String tab, String code, String projectId, String module) throws IotequException {
        Map<String, String> setting = SqlUtil.getDatabaseSetting(env);
        String databaseName = setting.get("database");
        if (databaseName == null || !"mysql".equals(setting.get("databaseId").toLowerCase()))
            throw new IotequException(IotequThrowable.ERROR, "数据配置错或不支持的数据库类型");
        CgTable nt = new CgTable();
        nt.setTitle("标题");
        if (!Util.isEmpty(tab)) {
            List<Table> tableList = dao.tables(databaseName);
            if (tableList != null && tableList.size() > 0) {
                for (Table t : tableList) {
                    if (!t.getTableName().equals(tab))
                        continue;
                    if (Util.isEmpty(code)) code = t.getTableName();
                    nt.setTitle(t.getTableComment());
                }
            } else if (Util.isEmpty(code)) return null;
        }
        if (Util.isEmpty(code)) return null;
        int i = 0;
        do {
            i++;
            if (dtoService.getTableByProjectIdAndCode(projectId, code) != null)
                code = code + i;
            else
                break;
        } while (true);
        nt.setEntity(StringUtil.firstLetterUpper(StringUtil.camelString(Util.isEmpty(tab, code))));
        nt.setCreator(dtoService.getUserName());
        nt.setSubPackage(module);
        nt.setCode(code);
        nt.setId(dtoService.getUserName() + "." + code);
        nt.setName(tab);
        nt.setProjectId(projectId);
        nt.setTemplate("vue-element");
        if (Util.isEmpty(tab)) nt.setActionList("edit");
        else nt.setActionList("list,add,view,edit,delete,batdel,import,export");

        Map<String, Object> result = new HashMap<String, Object>();
        if (!Util.isEmpty(tab)) {
            List<Column> columns = dao.columns(databaseName, tab);
            List<CgField> ff = new ArrayList<CgField>();
            int index = 10;
            for (Column c : columns) {
                CgField f = new CgField();
                f.setEntityName(StringUtil.camelString(c.getColumnName()));
                String key = c.getColumnKey();
                if (!Util.isEmpty(key) && key.toLowerCase().indexOf("pri") >= 0) {
                    if (!Util.isEmpty(c.getExtra())
                            && c.getExtra().toLowerCase().indexOf("auto_increment") >= 0)
                        f.setKeyType("1");
                    else if (c.getColumnType().toLowerCase().indexOf("char") >= 0
                            && !Util.isEmpty(c.getCharacterMaximumLength())
                            && c.getCharacterMaximumLength() >= 32)
                        f.setKeyType("2");
                    else
                        f.setKeyType("3");
                }
                else if (!Util.isEmpty(key) && key.toLowerCase().indexOf("uni") >= 0) f.setKeyType("4");
                else if (!Util.isEmpty(key) && key.toLowerCase().indexOf("mul") >= 0) f.setKeyType("5");
                else f.setKeyType("0");
                f.setIsNull(Util.boolValue(c.getIsNullable()));
                f.setDictMultiple(false);
                f.setDictFullName(false);
                TypeUtil.setTypeMysqlField(f,c.getColumnType());
                /* 不使用 dataType,characterMaximumLength,characterMaximumLength,numericScale 等参数，语义不完全一致，是为了生成语句统一
                f.setType(c.getDataType());
                int cl = c.getCharacterMaximumLength() == null ? 0
                        : c.getCharacterMaximumLength().intValue();
                if (cl > 0)
                    f.setLength(cl);
                f.setNumericPrecision(
                        c.getNumericPrecision() == null ? null : c.getNumericPrecision().intValue());
                f.setNumericScale(c.getNumericScale() == null ? null : c.getNumericScale().intValue());
                */
                f.setName(c.getColumnName());
                f.setOrderNum(index);
                index += 10;
                f.setTableId(nt.getId());
                f.setTitle(c.getColumnComment());

                f.setDefaultValue(c.getColumnDefault());
                String dt = TypeUtil.javaType(f);
                switch (dt) {
                    case "Integer":
                    case "Short":
                    case "Long":
                    case "BigDecimal":
                        f.setShowType("number");
                        break;
                    case "Double":
                    case "Float":
                        f.setShowType("number");
                        f.setValidExpression("^(\\-|\\+)?\\d+(\\.\\d+)?$");
                        f.setFormatter("%" + f.getNumericPrecision() + "." + f.getNumericScale() + "f");
                        break;
                    case "Date":
                        if (TypeUtil.jdbcType(f).equals("DATE")) {
                            f.setShowType("date");
                            f.setFormatter("yyyy-MM-dd");
                        } else if (TypeUtil.jdbcType(f).equals("TIME")) {
                            f.setShowType("time");
                            f.setFormatter("HH:mm");
                        } else {
                            f.setShowType("datetime");
                            f.setFormatter("yyyy-MM-dd HH:mm");
                        }
                        break;
                    case "Boolean":
                        f.setShowType("boolean");
                        break;
                    default:
                        f.setShowType(f.getType().contains("text")?"textarea":"text");
                }
                String name = f.getEntityName();
                switch (name) {
                    case IFlowService.flowNote:
                        f.setShowType("textarea");
                        break;
                    case IFlowService.flowCopyToList:
                        f.setDictTable("qinyoyo.sys_user");
                        f.setDictField("id");
                        f.setDictText("realName");
                        f.setShowType("dict_list");
                        f.setDynaCondition("f:");
                        break;
                    case IFlowService.flowRegisterBy:
                        f.setDefaultValue("f:Util.getUser().getId()");
                    case IFlowService.flowNextOperator:
                        f.setDictTable("qinyoyo.sys_user");
                        f.setDictField("id");
                        f.setDictText("realName");
                        f.setShowType("left_join");
                        f.setDynaCondition("f:");
                        break;
                    case IFlowService.flowRegisterTime:
                        f.setShowType("datetime");
                        f.setFormatter("YYYY-MM-DD HH:mm");
                        f.setDefaultValue("f:new Date()");
                        break;
                }
                ff.add(f);
            }
            result.put("fields", ff);
        }
        result.put("table", nt);
        return result;
    }
    private void createCgList(CgTable t, List<CgField> ff) {
        CgList item=new CgList();
        item.setName(StringUtil.camelString(t.getCode()));
        item.setId(t.getId() + "_" + item.getName());
        item.setPath("list");
        item.setTableId(t.getId());
        item.setHeadTitle(t.getTitle());
        item.setTagTitle(t.getTitle());
        item.setEditInline(false);
        item.setDetailInline(false);
        item.setSonAlign("h");
        item.setGeneratorType(0);
        item.setShowSearch(true);
        item.setToolbarMode(2);
        item.setTableHeight(0);
        item.setStripe(true);
        item.setBorder(true);
        item.setHighlightCurrentRow(true);
        item.setMultiple(false);
        item.setShowHeader(true);
        item.setLocalExport(false);
        item.setActionList(t.getActionList());
        item.setPagination(true);
        dtoService.insert(item);
        String id = item.getId();
        if (Util.isEmpty(id)) return;
        int order=10;
        for (CgField f : ff) {
            String type = f.getType();
            if (type.contains("blob") || type.contains("binary") || type.contains("text")) continue;
            if (type.contains("char") && f.getLength()>45) continue;
            CgListField im = new CgListField();
            im.setListId(id);
            im.setOrderNum(order);
            order += 10;
            im.setEntityField(f.getEntityName());
            String key = f.getKeyType();
            im.setQueryMode("0".equals(key) || "1".equals(key) || "2".equals(key) ? 0 : 1);
            im.setFix(false);
            im.setExpand(false);
            im.setOverflowTooltip(true);
            im.setAlign(TypeUtil.isNumbericType(f)?"right":"left");
            im.setWidth(0);
            im.setHidden("1".equals(key) || "2".equals(key) ? true : false);
            im.setEditInline(false);
            dtoService.insert(im);
        }
    }
    private void createCgForm(CgTable t, List<CgField> ff) {
        CgForm item=new CgForm();
        item.setName(StringUtil.camelString(t.getCode()));
        item.setId(t.getId() + "_" + item.getName());
        item.setPath("record");
        item.setTableId(t.getId());
        item.setHeadTitle(t.getTitle());
        item.setIsFlow(false);
        item.setTagTitle(t.getTitle());
        item.setLabelPosition("top");
        item.setIsDialog(false);
        item.setContinueAdd(false);
        dtoService.insert(item);
        String id = item.getId();
        if (Util.isEmpty(id)) return;
        int order=10;
        for (CgField f : ff) {
            String type = f.getType();
            if (type.contains("blob") || type.contains("binary")) continue;
            CgFormField im = new CgFormField();
            im.setFormId(id);
            im.setOrderNum(order);
            order += 10;
            im.setEntityField(f.getEntityName());
            String key = f.getKeyType();
            im.setWidth(f.getShowType().equals("textarea") || (f.getType().contains("char") && f.getLength() >= 100) ?24:12);
            im.setHidden("1".equals(key) || "2".equals(key) ? true : false);
            im.setReadonly(false);
            im.setMustInput(!f.getIsNull());
            im.setValidateAsTitle(false);
            dtoService.insert(im);
        }
    }
    @Transactional(rollbackFor=Exception.class)
    String autoCreate(String name) throws IotequException {
        Map<String, Object> map = generateFromDb(name);
        if (map==null) return null;
        CgTable nt = (CgTable) map.get("table");
        if (Util.isEmpty(nt.getTitle())) nt.setTitle(nt.getCode().replace("_"," "));
        dtoService.insert(nt);
        String tid = nt.getId();
        if (!Util.isEmpty(tid)) {
            Object obj = map.get("fields");
            try {
                if (obj != null) {
                    @SuppressWarnings("unchecked")
                    List<CgField> ff = (List<CgField>) obj;
                    for (CgField f : ff) {
                        f.setTableId(tid);
                        if (Util.isEmpty(f.getTitle())) f.setTitle(f.getName().replace("_", " "));
                        dtoService.insert(f);
                    }
                    createCgList(nt, ff);
                    createCgForm(nt, ff);
                }
            } catch (Exception e) {
                e.printStackTrace();
                tableDao.delete(tid);
                return null;
            }
        }
        return tid;
    }

    private void syncDbProperties(List<CgField> ff, List<CgField> dbFF) {
        ff.forEach(f->{
            if (!Util.isEmpty(f.getName())) {
                dbFF.stream().filter(df -> f.getName().equals(df.getName())).findFirst().ifPresent(df -> {
                    f.setType(df.getType());
                    f.setIsNull(df.getIsNull());
                    f.setLength(df.getLength());
                    f.setNumericPrecision(df.getNumericPrecision());
                    f.setNumericScale(df.getNumericScale());
                });
            }
        });
    }
    private void deleteFieldNotInDb(List<CgField> ff, List<CgField> dbFF) {
        List<CgField> needRemove = ff.stream().filter(f->!Util.isEmpty(f.getName()) && f.getName().indexOf(":") < 0
                && !dbFF.stream().anyMatch(d->f.getName().equals(d.getName()))).collect(Collectors.toList());
        for (CgField f : needRemove) {
            ff.remove(f);
            fieldDao.delete(f.getId());
        }
    }

    private void deleteFieldNotInForm(String tab, List<CgField> ff, List<CgField> dbFF) {
        List<CgField> needRemove = dbFF.stream().filter(f->!ff.stream().anyMatch(d-> Util.equals(f.getName(),d.getName()))).collect(Collectors.toList());
        for (CgField dbf : needRemove) {
            dbFF.remove(dbf);
            try {
                sqlService.execute("ALTER TABLE `" + tab + "` DROP COLUMN `" + dbf.getName() + "`");
            } catch (Exception e) {
            }
        }
    }

    private void combineFields(String tab, String tabId, List<CgField> ff, List<CgField> dbFF, boolean usingFormData) {
        List<CgField> sameInForm = new ArrayList<CgField>();
        List<CgField> sameInDb = new ArrayList<CgField>();
        int orderNum = 0;
        String name = "";
        for (CgField dbf : dbFF) { // 插入新的数据库字段
            name = dbf.getName();
            boolean exists = false;
            for (CgField f : ff) {
                if (orderNum < f.getOrderNum())
                    orderNum = f.getOrderNum();
                if (EntityUtil.entityEquals(f.getName(), dbf.getName())) {
                    exists = true;
                    sameInForm.add(f);
                    sameInDb.add(dbf);
                    break;
                }
            }
            orderNum += 10;
            if (!exists) {
                dbf.setTableId(tabId);
                dbf.setOrderNum(orderNum);
                dbf.setId(null);
                fieldDao.insert(dbf);
            }
        }
        for (CgField f : ff) { // 在数据库中创建新的字段
            if (Util.isEmpty(f.getName()) || f.getName().indexOf(":") >= 0) continue;     //  跳过非数据库字段
            boolean exists = false;
            for (CgField dbf : dbFF) {
                if (f.getName().equals(dbf.getName())) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                String sql = "ALTER TABLE `" + tab + "` ADD COLUMN `" + f.getName() + "`" + " " + f.getType()
                        + (f.getIsNull() ? " NULL" : " NOT NULL")
                        + (Util.isEmpty(f.getDefaultValue()) ? ""
                        : " DEFAULT '" + f.getDefaultValue() + "'")
                        + (f.getKeyType() != null && f.getKeyType().equals("1") ? " AUTO_INCREMENT" : "")
                        + (Util.isEmpty(f.getTitle()) ? "" : " COMMENT '" + f.getTitle() + "'")
                        + (Util.isEmpty(name) ? "" : " AFTER `" + name + "`");
                try {
                    sqlService.execute(sql);
                } catch (Exception e) {
                }
                name = f.getName();
            }
        }
        for (int i = 0; i < sameInForm.size(); i++) { // 相同字段冲突处理
            CgField f = sameInForm.get(i);
            CgField dbf = sameInDb.get(i);
            boolean dbchanged = false;
            boolean formchanged = false;
            if (!EntityUtil.entityEquals(f.getIsNull(), dbf.getIsNull())) {
                if (usingFormData) {
                    dbf.setIsNull(f.getIsNull());
                    dbchanged = true;
                } else {
                    f.setIsNull(dbf.getIsNull());
                    formchanged = true;
                }
            }
            if (!EntityUtil.entityEquals(f.getType(), dbf.getType())) {
                if (usingFormData) {
                    dbf.setType(f.getType());
                    dbchanged = true;
                } else {
                    f.setType(dbf.getType());
                    formchanged = true;
                }
            }
            if (!EntityUtil.entityEquals(f.getDefaultValue(), dbf.getDefaultValue())) {
                if (usingFormData && f.getDefaultValue() != null && !f.getDefaultValue().startsWith("f:")) {
                    dbf.setDefaultValue(f.getDefaultValue());
                    dbchanged = true;
                } else if (f.getDefaultValue() == null || !f.getDefaultValue().startsWith("f:")) {
                    f.setDefaultValue(dbf.getDefaultValue());
                    formchanged = true;
                }
            }
            if (Util.isEmpty(f.getTitle()) && !Util.isEmpty(dbf.getTitle())) {
                f.setTitle(dbf.getTitle());
                formchanged = true;
            } else if (!Util.isEmpty(f.getTitle()) && Util.isEmpty(dbf.getTitle())) {
                dbf.setTitle(f.getTitle());
                dbchanged = true;
            } else if (!Util.isEmpty(f.getTitle()) && !Util.isEmpty(dbf.getTitle())
                    && !f.getTitle().equals(dbf.getTitle())) {
                if (usingFormData) {
                    dbf.setTitle(f.getTitle());
                    ;
                    dbchanged = true;
                } else {
                    f.setTitle(dbf.getTitle());
                    ;
                    formchanged = true;
                }
            }
            if (dbchanged) {
                String sql = "ALTER TABLE `" + tab + "` CHANGE COLUMN `" + dbf.getName() + "` `" + dbf.getName()
                        + "` " + " " + dbf.getType() + (dbf.getIsNull() ? " NULL" : " NOT NULL")
                        + (Util.isEmpty(dbf.getDefaultValue()) ? ""
                        : " DEFAULT '" + dbf.getDefaultValue() + "'")
                        + (dbf.getKeyType() != null && dbf.getKeyType().equals("1") ? " AUTO_INCREMENT"
                        : "")
                        + (Util.isEmpty(dbf.getTitle()) ? "" : " COMMENT '" + dbf.getTitle() + "'");
                try {
                    sqlService.execute(sql);
                } catch (Exception e) {
                }
            }
            if (formchanged)
                fieldDao.update(f);
        }
    }

    private void createDbTable(CgTable nt, List<CgField> ff) throws Exception {
        StringBuilder sb = new StringBuilder();
        // sb.append("DROP TABLE IF EXISTS `" + nt.getName() + "`;\n");
        sb.append("CREATE TABLE `" + nt.getName() + "` (\n");
        CgField pk = null;
        for (CgField f : ff) {
            if (Util.isEmpty(f.getName()) || f.getName().indexOf(":") >= 0) continue;     //  跳过非数据库字段
            if (f.getKeyType() != null && (f.getKeyType().equals("1") || f.getKeyType().equals("2")))
                pk = f;
            sb.append("`" + f.getName() + "`" + " " + f.getType() + (f.getIsNull() ? " NULL" : " NOT NULL")
                    + (Util.isEmpty(f.getDefaultValue()) ? "" : " DEFAULT '" + f.getDefaultValue() + "'")
                    + (f.getKeyType() != null && f.getKeyType().equals("1") ? " AUTO_INCREMENT" : "")
                    + (Util.isEmpty(f.getTitle()) ? ",\n" : " COMMENT '" + f.getTitle() + "',\n"));
        }
        if (pk != null)
            sb.append("PRIMARY KEY (`" + pk.getName() + "`) USING BTREE\n");
        sb.append(") ENGINE=InnoDB AUTO_INCREMENT=263 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='"
                + (nt.getTitle() == null ? "" : nt.getTitle()) + "'");
        sqlService.execute(sb.toString());
    }

}
