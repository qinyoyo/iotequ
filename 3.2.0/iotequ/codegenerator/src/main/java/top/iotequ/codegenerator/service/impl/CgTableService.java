package top.iotequ.codegenerator.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import top.iotequ.codegenerator.dao.CgFieldDao;
import top.iotequ.codegenerator.pojo.CgField;
import top.iotequ.codegenerator.pojo.CgTable;
import top.iotequ.codegenerator.util.CodeUtil;
import top.iotequ.framework.exception.IotequException;
import top.iotequ.framework.exception.IotequThrowable;

import top.iotequ.util.*;
import top.iotequ.util.RestJson;
import top.iotequ.util.SqlUtil;
import top.iotequ.util.StringUtil;
import top.iotequ.util.Util;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CgTableService extends CgCgTableService {
    @Autowired
    DtoService dtoService;
    @Autowired
    private GenService genService;
    @Autowired
    private CgFieldDao cgFieldDao;
    @Autowired
    private Environment env;
    @Autowired
    AutoGenService autoGenService;
    @Override
    public void setPrimaryKey(CgTable obj) throws IotequException {
        if (Objects.nonNull(obj)) {
            obj.setId(obj.getProjectId() + "_" + obj.getCode());
        }
    }

    @Override
    public Map<String, Object> getDictionary(CgTable obj, Boolean useTree, String dynaFields) {
        if ("notUsedTables".equals(dynaFields)) {
            return autoGenService.getNotUsedTablesDictionary();
        } else return super.getDictionary(obj, useTree, dynaFields);
    }

    @Override
    public void afterSave(CgTable obj0, CgTable obj, HttpServletRequest request, RestJson j) throws IotequException {
        if (obj0==null && obj!=null) { // 新建成功，插入一个id字段
            CgField f = new CgField();
            f.setIsNull(false);
            f.setShowType("text");
            f.setTitle("ID");
            f.setTableId(obj.getId());
            f.setKeyType("2");
            f.setType("char");
            f.setOrderNum(1);
            f.setEntityName("id");
            f.setLength(32);
            f.setName("id");
            f.setDictMultiple(false);
            f.setDictFullName(false);
            try {
                cgFieldDao.insert(f);
            } catch (Exception e) {}
        }
    }

    @Override
    public CgTable getDefaultObject(CgTable cgTable) throws IotequException {
        cgTable = super.getDefaultObject(cgTable);
        Map<String, String> db = SqlUtil.getDatabaseSetting(env);
        if (db == null) throw new IotequException(IotequThrowable.ERROR, "数据库配置错误");
        if (!"MySql".equals(db.get("databaseId"))) throw new IotequException(IotequThrowable.ERROR, "cg仅支持mysql数据库");
        String databaseName = db.get("database");
        if (Util.isEmpty(databaseName)) throw new IotequException(IotequThrowable.ERROR, "数据库配置错误");
        String sql = "SELECT TABLE_NAME as \"value\",concat(TABLE_COMMENT,'/',TABLE_NAME) as \"text\" FROM `information_schema`.`TABLES` where TABLE_SCHEMA = '"
                + databaseName + "'  and TABLE_TYPE = 'BASE TABLE'";
//		request.setAttribute("dict_name", getDictListFromDatabase(sql, null, null, null, false,true));
        Util.getRequest().setAttribute("org", Util.isEmpty(env.getProperty("generator.org"), "top.iotequ"));
        Util.getRequest().setAttribute("project", Util.isEmpty(env.getProperty("generator.project"), "codegenerator"));
        return cgTable;
    }

    @Override
    public String listFilter(String path) {
        String user = Util.getUser().getName();
        if (Util.isEmpty(user)) return "1=2";
        else return String.format("cg_table.creator='%s'", user);
    }
    private String getFkTables(String tableId) {
        String fkTables = "";
        List<CgField> fields = genService.dtoService.getFields(tableId);
        for(CgField f: fields) {
            String fkTab = f.getFkTable();
            if (!Util.isEmpty(fkTab)) {
                fkTables += (","+fkTab);
            }
        }
        if (!fkTables.isEmpty()) fkTables += ",";
        return fkTables;
    }
    @Override
    public RestJson doAction(String action, String ids, HttpServletRequest request) throws IotequException {
        if ("create".equals(action)) return autoGenService.batchAutoCreate(request.getParameter("tableNames"));
        else if ("sync".equals(action)) return autoGenService.doSync(request.getParameter("options"),request.getParameter("tableIds"));
        else if ("fields".equals(action)) return autoGenService.fieldsIncludeJoin(request.getParameter("tableId"));
        RestJson j = new RestJson();
        if (Util.isEmpty(ids)) throw new IotequException(IotequThrowable.NULL_OBJECT, "ids==null");
        int scanFiles = 0;
        String sql = "select id,name,code,project_id from cg_table where find_in_set(id,'" + ids + "') and creator='" + dtoService.getUserName() + "'";
        List<Map<String, Object>> list = SqlUtil.sqlQuery(sql);
        list.forEach(m->{
            Object name = m.get("name");
            if (Util.isEmpty(name)) m.put("fkTable","");
            else m.put("fkTable", getFkTables(m.get("id").toString()));
        });
        list.sort((m2,m1)->{
            String pid1 = StringUtil.toString(m1.get("project_id"));
            if (pid1==null) pid1="";
            String pid2 = StringUtil.toString(m2.get("project_id"));
            if (pid2==null) pid2="";
            if (!pid1.equals(pid2)) return pid1.compareTo(pid2);
            Object name1 = m1.get("name");
            if (Util.isEmpty(name1)) return -1;
            Object name2 = m2.get("name");
            if (Util.isEmpty(name2)) return 1;
            String fk1 = m1.get("fkTable").toString(), fk2 = m2.get("fkTable").toString();
            if (fk1.isEmpty() && fk2.isEmpty()) return name1.toString().compareTo(name2.toString());
            else if (fk1.isEmpty()) return -1;
            else if (fk2.isEmpty()) return 1;
            else if (fk1.indexOf(","+name2.toString()+",") >= 0) return 1;
            else if (fk2.indexOf(","+name1.toString()+",") >= 0) return -1;
            else return name1.toString().compareTo(name2.toString());
        });
        GenService.writedProjectName = "";
        String codePath = env.getProperty("generator.codepath");
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Util.setProgress((int) (100 * (i + 1)) / list.size());
                String code = list.get(i).get("code").toString();
                String projectId = list.get(i).get("project_id").toString();
                if ("scanCode".equals(action)) {
                    scanFiles += CodeUtil.scanCode2Cg(codePath, projectId, code);
                } else if ("generator".equals(action)) {
                    String pid = StringUtil.toString(list.get(i).get("project_id"));
                    if (i == list.size() - 1 || !Util.equals(pid,list.get(i+1).get("project_id"))) {
                        List<Map<String, Object>> proList = list
                                .stream().filter(f->pid.equals(StringUtil.toString(f.get("project_id")))).collect(Collectors.toList());
                        genService.generate(projectId, code, proList);
                    } else
                        genService.generate(projectId, code, null);
                    scanFiles++;
                } else if ("flowSync".equals(action)) {
                    scanFiles += genService.flowSync(projectId, code);
                }
            }
        } else {
            j.setSuccess(false);
            j.setMessage("不存在的定义");
        }
        j.parameter("rows", scanFiles);
        return j;
    }
}
