package top.iotequ.codegenerator.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import top.iotequ.codegenerator.dao.*;
import top.iotequ.codegenerator.pojo.*;
import top.iotequ.codegenerator.util.CgException;
import top.iotequ.codegenerator.util.CgUtil;
import top.iotequ.codegenerator.util.JoinUtil;
import top.iotequ.codegenerator.util.NameUtil;
import top.iotequ.framework.dao.DataDictDao;
import top.iotequ.framework.exception.IotequThrowable;
import top.iotequ.framework.service.impl.SqlService;
import top.iotequ.framework.util.SqlUtil;
import top.iotequ.framework.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class DtoService {
    @Autowired
    CgTableDao cgTableDao;
    @Autowired
    CgFieldDao cgFieldDao;
    @Autowired
    SchemaDao dao;
    @Autowired
    SqlService sqlService;
    @Autowired
    DataDictDao dataDictDao;
    @Autowired
    CgFormDao cgFormDao;
    @Autowired
    CgListDao cgListDao;
    @Autowired
    CgFormFieldDao cgFormFieldDao;
    @Autowired
    CgListFieldDao cgListFieldDao;
    @Autowired
    private Environment env;

    public String getUserName() {
        return Util.getUser().getName();
    }

    public CgTable selectTable(String id) throws CgException {
        CgTable tab = cgTableDao.select(id);
        boolean localMode = Util.boolValue(env.getProperty("generator.localMode"));
        if (Objects.nonNull(tab)) {
            if (localMode || tab.getCreator().equals(getUserName())) return tab;
            else throw new CgException("不能访问其他人的表定义");
        } else throw new CgException("表定义不存在");
    }

    public CgTable getTableByProjectIdAndCode(String projectId, String code) throws CgException {
        if (Util.isEmpty(code)) return null;
        CgTable cgt = cgTableDao.selectByCodeProjectId(code, projectId);
        if (cgt != null) {
            try {
                CgUtil.checkJavaReservedWord(cgt.getEntity());
            } catch (Exception e) {
                throw new CgException("表单定义(" + code + ") 使用了java关键字 : " + cgt.getEntity());
            }
            try {
                CgUtil.checkDbReservedWord(cgt.getName());
            } catch (Exception e) {
                throw new CgException("表单定义(" + code + ")的表名 " + e.getMessage());
            }
        }
        return cgt;
    }

    public List<CgField> getFields(String tableId) {
        return cgFieldDao.listBy("table_id='" + tableId + "'", "order_num");
    }

    public CgField getFieldBy(String tableId, String entityOrFieldName) throws CgException {
        List<CgField> ff = cgFieldDao.listBy(SqlUtil.sqlString("table_id = ? and (name = ? or entity_name = ?)",
                tableId, entityOrFieldName, entityOrFieldName), null);
        if (ff.isEmpty()) throw new CgException( "表字段不存在：" + entityOrFieldName);
        return ff.get(0);
    }

    public CgField getFieldIncludeJoinBy(CgTable table, String entityOrFieldName) throws CgException {
        List<CgField> tabFields = new ArrayList<CgField>();    // 数据库表字段
        List<CgField> joinFields = new ArrayList<CgField>();    // join字段
        List<CgField> calFields = new ArrayList<CgField>();     // 计算字段
        resetFieldList(table, tabFields, joinFields, calFields);
        List<CgField> allFields = getAllFieldsIncludeSplitedJoin(table, tabFields, joinFields, calFields);
        for (CgField f : allFields) {
            if (entityOrFieldName.equals(f.getEntityName()) || (entityOrFieldName.equals(f.getName()))) return f;
        }
        throw new CgException(IotequThrowable.NULL_OBJECT, "表字段不存在：" + table.getCode() + "." + entityOrFieldName);
    }

    public List<CgList> getCgLists(String tableId) {
        List<CgList> l = cgListDao.listBy("table_id='" + tableId + "'", null);
        if (!Util.isEmpty(l)) {
            for (CgList item : l) {
                if (!Util.isEmpty(item.getActionList())) item.setActionList("," + item.getActionList() + ",");
            }
        }
        return l;
    }

    public String getAllCgListSql(String projectId) {
        boolean localMode = Util.boolValue(env.getProperty("generator.localMode"));
        if (Util.isEmpty(projectId) || projectId.equals("qinyoyo.sys")) {
            return (localMode ? "select * from cg_list join cg_table on cg_list.table_id = cg_table.id order by (case cg_table.project_id when 'qinyoyo.sys' then '1' else cg_table.project_id end), cg_list.name"
                    : SqlUtil.sqlString("SELECT * FROM cg_list join cg_table on cg_list.table_id=cg_table.id where cg_table.creator = ? or cg_table.project_id='qinyoyo.sys' order by cg_list.name", getUserName()));
        } else {
            return (localMode ? SqlUtil.sqlString("select * from cg_list join cg_table on cg_list.table_id = cg_table.id order by (case cg_table.project_id when ? then '0' when 'qinyoyo.sys' then '1' else cg_table.project_id end), cg_list.name", projectId)
                    : SqlUtil.sqlString("SELECT * FROM cg_list join cg_table on cg_list.table_id=cg_table.id where cg_table.project_id = ? or cg_table.project_id='qinyoyo.sys' order by (case cg_table.project_id when ? then '0' when 'qinyoyo.sys' then '1' else cg_table.project_id end), cg_list.name", projectId,projectId));

        }

    }

    public CgList getCgList(String listName) throws CgException {
        try {
            boolean localMode = Util.boolValue(env.getProperty("generator.localMode"));
            if (localMode) return SqlUtil.sqlQueryFirst(CgList.class,
                    "select * from cg_list where name=?", listName);
            else return SqlUtil.sqlQueryFirst(CgList.class,
                    "select * from cg_list where name=? and table_id in (select id from cg_table where creator = ?)",
                    listName, getUserName());
        } catch (Exception e) {
            throw new CgException(e);
        }
    }

    public List<CgListField> getCgListFields(String listId) throws CgException {
        try {
            return SqlUtil.sqlQuery(CgListField.class, "select * from cg_list_field where list_id=? order by order_num", listId);
        } catch (Exception e) {
            throw new CgException(e);
        }
    }

    public int insert(CgList item) {
        return cgListDao.insert(item);
    }

    public int insert(CgForm item) {
        return cgFormDao.insert(item);
    }

    public int insert(CgListField item) {
        return cgListFieldDao.insert(item);
    }

    public int insert(CgFormField item) {
        return cgFormFieldDao.insert(item);
    }

    public int insert(CgField item) {
        return cgFieldDao.insert(item);
    }

    public int insert(CgTable item) {
        return cgTableDao.insert(item);
    }

    public int update(CgListField item) {
        return cgListFieldDao.update(item);
    }

    public int update(CgTable item) {
        return cgTableDao.update(item);
    }

    public List<CgForm> getCgForms(String tableId) {
        List<CgForm> l = cgFormDao.listBy("table_id='" + tableId + "'", null);
        if (!Util.isEmpty(l)) {
            for (CgForm item : l) {
                if (!Util.isEmpty(item.getActionList())) item.setActionList("," + item.getActionList() + ",");
            }
        }
        return l;
    }

    public CgForm getCgForm(String formName) throws CgException {
        boolean localMode = Util.boolValue(env.getProperty("generator.localMode"));
        try {
            if (localMode) return SqlUtil.sqlQueryFirst(CgForm.class,
                    "select * from cg_form where name=?", formName);
            else return SqlUtil.sqlQueryFirst(CgForm.class,
                    "select * from cg_form where name=? and table_id in (select id from cg_table where creator = ?)",
                    formName, getUserName());
        } catch (Exception e) {
            throw new CgException(e);
        }
    }

    public List<CgFormField> getCgFormFields(String formId) throws CgException {
        try {
            return SqlUtil.sqlQuery(CgFormField.class, "select * from cg_form_field where form_id=? order by order_num", formId);
        } catch (Exception e) {
            throw new CgException(e);
        }
    }

    public List<Map<String, Object>> query(String sql, Object... params) throws CgException {
        try {
            return SqlUtil.sqlQuery(sql, params);
        } catch (Exception e) {
            throw new CgException(e);
        }
    }

    public <T> List<T> query(Class<T> clazz, String sql, Object... params) throws CgException {
        try {
            return SqlUtil.sqlQuery(clazz, sql, params);
        } catch (Exception e) {
            throw new CgException(e);
        }
    }

    public void execute(String sql, Object... params) throws CgException {
        try {
            SqlUtil.sqlExecute(sql, params);
        } catch (Exception e) {
            throw new CgException(e);
        }
    }

    public void resetFieldList(@NonNull CgTable cgTable, List<CgField> tabFields, List<CgField> joinFields, List<CgField> calFields) throws CgException {
        tabFields.clear();
        joinFields.clear();
        calFields.clear();
        List<CgField> fields = getFields(cgTable.getId());
        if (fields != null && fields.size() > 0) {
            for (CgField f : fields) {
                try {
                    CgUtil.checkJavaReservedWord(f.getEntityName());
                } catch (Exception e) {
                    throw new CgException("表单定义(" + cgTable.getCode() + ")的字段 " + f.getEntityName() + " 是java关键字");
                }
                try {
                    CgUtil.checkDbReservedWord(f.getName());
                } catch (Exception e) {
                    throw new CgException("表单定义(" + cgTable.getCode() + ")的字段 " + e.getMessage());
                }
                if (Util.isEmpty(f.getName()) || f.getName().indexOf(":") >= 0) f.setIsNull(true);
                if (f.getShowType().endsWith("_join") || f.getShowType().indexOf("dict_list") >= 0) {  // join(inner join),left join, right join, full join 字段条件 name 不为空且必须为数据库表的一个字段，关联表为 dict_table,关联字段为dict_field，查询值为 dic_text
                    CgTable jt = JoinUtil.joinTable(f);
                    if (jt == null || Util.isEmpty(f.getDictTable()) || Util.isEmpty(f.getDictField()) || Util.isEmpty(f.getDictText())) {
                        f.setShowType("text");
                        if (Util.isEmpty(f.getName()) || f.getName().indexOf(":") >= 0) {
                            f.setIsNull(true);
                            calFields.add(f);
                        } else tabFields.add(f);
                    } else joinFields.add(f);
                } else if (f.getShowType().indexOf("dict_list") >= 0) {
//                    f.setDictTable(JoinUtil.joinCode(table, f));
                    f.setId(JoinUtil.joinComponent(f));
                    tabFields.add(f);
                } else if (Util.isEmpty(f.getName()) || f.getName().indexOf(":") >= 0) {
                    f.setIsNull(true);
                    calFields.add(f);
                } else tabFields.add(f);
            }
        }
        /* 用 字段 id 来记录 join 的表的别名。由于一个表可以join多次，因此可能有后缀 */
        for (int i = 0; i < joinFields.size(); i++) {
            CgTable jtab = JoinUtil.joinTable(joinFields.get(i));
            if (jtab != null) {
                joinFields.get(i).setId(jtab.getName());
            } else joinFields.get(i).setId(null);
        }
        for (int i = joinFields.size() - 1; i >= 0; i--) {
            String name = joinFields.get(i).getId();
            if (name != null && i > 0) {
                int found = 0;
                for (int j = 0; j < i; j++) if (name.equals(joinFields.get(j).getId())) found++;
                if (found > 0) joinFields.get(i).setId(name + found);
            }
        }
    }

    public static List<CgField> getAllFieldsNotSplitedJoin(List<CgField> tabFields, List<CgField> joinFields, List<CgField> calFields) {
        List<CgField> allFields = new ArrayList<CgField>();
        if (!Util.isEmpty(tabFields)) allFields.addAll(tabFields);
        if (!Util.isEmpty(joinFields)) allFields.addAll(joinFields);
        if (!Util.isEmpty(calFields)) allFields.addAll(calFields);
        return allFields;
    }

    public static List<CgField> getAllFieldsIncludeSplitedJoin(CgTable table, List<CgField> tabFields, List<CgField> joinFields, List<CgField> calFields) throws CgException {
        List<CgField> allFields = new ArrayList<CgField>();
        if (!Util.isEmpty(tabFields)) {
            tabFields.forEach(f -> f.setTitle(NameUtil.generatorName(table) + ".field." + f.getEntityName()));
            allFields.addAll(tabFields);
        }
        if (!Util.isEmpty(joinFields)) {
            for (CgField f : joinFields) {
                CgField cf = JoinUtil.convertedJoinField(table, f, tabFields, joinFields, calFields);
                cf.setTitle(NameUtil.generatorName(table) + ".field." + f.getEntityName());
                allFields.add(cf);
                List<CgField> jf = JoinUtil.splitedJoinFields(table, f, tabFields, joinFields, calFields);
                allFields.addAll(jf);
            }
        }
        if (!Util.isEmpty(calFields)) {
            calFields.forEach(f -> f.setTitle(NameUtil.generatorName(table) + ".field." + f.getEntityName()));
            allFields.addAll(calFields);
        }
        return allFields;
    }

    public List<CgField> getAllFieldsIncludeSplitedJoin(CgTable table) throws CgException {
        List<CgField> tabFields = new ArrayList<>(), joinFields = new ArrayList<>(), calFields = new ArrayList<>();
        resetFieldList(table, tabFields, joinFields, calFields);
        return getAllFieldsIncludeSplitedJoin(table, tabFields, joinFields, calFields);
    }
}
