package top.iotequ.codegenerator.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.iotequ.codegenerator.pojo.CgField;
import top.iotequ.codegenerator.pojo.CgForm;
import top.iotequ.codegenerator.pojo.CgList;
import top.iotequ.framework.exception.IotequException;

import top.iotequ.framework.service.utils.DictionaryUtil;
import top.iotequ.util.*;
import top.iotequ.util.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class CgFieldService extends CgCgFieldService {
    // 删除字段定义前同步删除列表和表单使用的该字段
    @Autowired
    DtoService dtoService;
    @Override
    public void beforeDelete(String ids, HttpServletRequest request) throws IotequException {
        List<CgField> ff = dtoService.query(CgField.class, "select * from cg_field where id = ?", ids);
        if (!Util.isEmpty(ff)) {
            for (CgField f : ff) {
                List<CgList> ll = dtoService.getCgLists(f.getTableId());
                if (!Util.isEmpty(ll)) {
                    List<String> listIds = new ArrayList<>();
                    ll.forEach(a -> listIds.add(a.getId()));
                    dtoService.execute("delete from cg_list_field where list_id = ? and entity_field = ?", listIds, f.getEntityName());
                }
                List<CgForm> forms = dtoService.getCgForms(f.getTableId());
                if (!Util.isEmpty(forms)) {
                    List<String> formIds = new ArrayList<>();
                    forms.forEach(a -> formIds.add(a.getId()));
                    dtoService.execute("delete from cg_form_field where form_id = ? and entity_field = ?", formIds, f.getEntityName());
                }
            }
        }
    }

    @Override
    public void beforeSave(CgField obj0, CgField obj, boolean updateSelective, HttpServletRequest request) throws IotequException {
        if (obj0==null && obj!=null && !updateSelective) { // 快速新建
            if (Util.isEmpty(obj.getEntityName()) && !Util.isEmpty(obj.getName()))
                obj.setEntityName(StringUtil.camelString(obj.getName().split(":")[0].trim()));
            if (Util.isEmpty(obj.getTitle()) && !Util.isEmpty(obj.getName())) obj.setTitle(StringUtil.firstLetterUpper(obj.getName()));
            if (obj.getIsNull()==null) obj.setIsNull(false);
            if (obj.getDictMultiple()==null) obj.setDictMultiple(false);
            if (obj.getDictFullName()==null) obj.setDictFullName(false);
            if (Util.isEmpty(obj.getKeyType())) obj.setKeyType("0");
            if (Util.isEmpty(obj.getShowType()) && !Util.isEmpty(obj.getType())) {
                switch (obj.getType()) {
                    case "int" : case "smallint" : case "bigint":
                        obj.setShowType("number"); break;
                    case "decimal" :
                        obj.setShowType("number"); obj.setNumericPrecision(10); obj.setNumericScale(2); break;
                    case "tinyint" :
                        obj.setShowType("boolean"); obj.setLength(1); break;
                    case "datetime" : obj.setShowType("datetime"); break;
                    case "date" : obj.setShowType("date"); break;
                    case "time" : obj.setShowType("time"); break;
                    default : obj.setShowType("text");
                }
            }
            if (Util.isEmpty(obj.getOrderNum()) && !Util.isEmpty(obj.getTableId())) {
                Integer max = SqlUtil.sqlQueryInteger(false,"select max(order_num) from cg_field where table_id = ?",obj.getTableId());
                if (max==null) obj.setOrderNum(10);
                else obj.setOrderNum(max+10);
            }
        }
    }

    @Override
    public void afterSave(CgField obj0, CgField obj, HttpServletRequest request, RestJson j) throws IotequException {
        if (Objects.nonNull(obj0) && Objects.nonNull(obj0)) {
            if (!obj0.getEntityName().equals(obj.getEntityName())) {
                String tableId = obj.getTableId();
                String sql = "update cg_%s_field set entity_field = '%s' where %s_id in (select id from cg_form where table_id = '%s') and entity_field = '%s'";
                SqlUtil.sqlExecute(String.format(sql, "form", obj.getEntityName(), "form", tableId, obj0.getEntityName()));
                SqlUtil.sqlExecute(String.format(sql, "list", obj.getEntityName(), "list", tableId, obj0.getEntityName()));
            }
        }
    }

    @Override
    public RestJson doAction(String action, String id, HttpServletRequest request) throws IotequException {
        RestJson j=new RestJson();
        String tableId = request.getParameter("tableId");
        String projectId = Util.isEmpty(tableId)? null : SqlUtil.sqlQueryString(false,"select project_id from cg_table where id=?",tableId);
        if ("extDict".equals(action)) {
            Map<String, Object> map = new HashMap<>();

            map.put("dictCgList", DictionaryUtil.getDictListFromDatabase(EntityUtil.createEntity(getEntityClass(),request),
                    dtoService.getAllCgListSql(projectId),
                    "cg_list.name", "concat(cg_list.name,':',cg_list.head_title)", null, false, null));
            List<Map<String, Object>> list = DictionaryUtil.getDictListFromDatabase(EntityUtil.createEntity(getEntityClass(),request),
                    "select distinct dict from sys_data_dict order by dict;",
                    "dict", "dict", null, false, null);
            list.add(0, new HashMap<String, Object>() {{
                put("value", "");
                put("text", "输入sql语句");
            }});
            list.add(1, new HashMap<String, Object>() {{
                put("value", "f:");
                put("text", "后端java函数");
            }});
            list.add(2, new HashMap<String, Object>() {{
                put("value", "js:");
                put("text", "前端js函数");
            }});
            list.add(3, new HashMap<String, Object>() {{
                put("value", "dict:");
                put("text", "字典引用");
            }});
            map.put("dictDataDict", list);
            j.dictionary(map);
        }
        return j;
    }
}
