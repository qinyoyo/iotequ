package top.iotequ.codegenerator.util;

import top.iotequ.codegenerator.dao.CgFormDao;
import top.iotequ.codegenerator.dao.CgFormFieldDao;
import top.iotequ.codegenerator.dao.CgListDao;
import top.iotequ.codegenerator.pojo.*;
import top.iotequ.codegenerator.service.impl.DtoService;
import top.iotequ.codegenerator.service.impl.GenService;
import top.iotequ.framework.exception.IotequException;
import top.iotequ.util.SqlUtil;
import top.iotequ.util.StringUtil;
import top.iotequ.util.Util;

import java.util.List;
import java.util.Objects;

public class MessageUtil {
    public static String tab="  ";
    static StringBuilder writeKey(StringBuilder sb,int tabs,String key,String value,boolean lastItem) {
        for (int i=0;i<tabs;i++) sb.append(tab);
        sb.append(key).append(": '").append(value.replace("'","\\'")).append("'");
        if (lastItem) sb.append("\n");
        else sb.append(",\n");
        return sb;
    }
    public static void message(String generatorPath, CgTable table, List<CgField> tabFields, List<CgField> joinFields, List<CgField> calFields) throws CgException {
        assert (table != null);
        StringBuilder en = new StringBuilder(), zh = new StringBuilder();
        List<CgForm> forms = Util.getBean(CgFormDao.class).listBy("table_id='"+table.getId()+"'",null);
        List<CgList> lists = Util.getBean(CgListDao.class).listBy("table_id='"+table.getId()+"'",null);


        en.append("export default {\n").append(tab).append(NameUtil.generatorName(table)).append(": {\n");
        zh.append("export default {\n").append(tab).append(NameUtil.generatorName(table)).append(": {\n");

        // title
        en.append(tab).append(tab).append("title: {\n");
        zh.append(tab).append(tab).append("title: {\n");

        if (lists!=null && !lists.isEmpty()) {
            for (CgList item : lists) {
                writeKey(en, 3, item.getPath() , item.getName(), false);
                writeKey(zh, 3, item.getPath(), item.getHeadTitle(), false);
            }
        }
        if (forms!=null && !forms.isEmpty()) {
            for (CgForm item : forms) {
                if (item.getIsFlow() && item.getPath().split(",").length>1) { // 不同流程可以共用一个form表单
                    String [] pp = item.getPath().split(",");
                    String [] tt = item.getHeadTitle().split(",");
                    for (int i=0;i<pp.length;i++) {
                        writeKey(en, 3, pp[i], pp[i], false);
                        writeKey(zh, 3, pp[i], i<tt.length ? tt[i] : tt[0], false);
                    }
                } else {
                    writeKey(en, 3, item.getPath(), item.getName(), false);
                    writeKey(zh, 3, item.getPath(), item.getHeadTitle(), false);
                }
                List<CgFormField> formFields = Util.getBean(CgFormFieldDao.class).listBy("form_id='"+item.getId()+"'","order_num");
                for (CgFormField f : formFields) {
                    if (!Util.isEmpty(f.getGroupTitle())) {
                        writeKey(en, 3, "group"+StringUtil.firstLetterUpper(item.getName())+StringUtil.firstLetterUpper(f.getEntityField()) , "group"+StringUtil.firstLetterUpper(item.getName())+StringUtil.firstLetterUpper(f.getEntityField()), false);
                        writeKey(zh, 3, "group"+StringUtil.firstLetterUpper(item.getName())+StringUtil.firstLetterUpper(f.getEntityField()) , f.getGroupTitle(), false);
                    }
                }
            }
        }
        writeKey(en,3,"code",StringUtil.firstLetterUpper(NameUtil.generatorName(table)).replaceAll("_", " "),true);
        writeKey(zh,3,"code",table.getTitle(),true);
        en.append(tab).append(tab).append("},\n");
        zh.append(tab).append(tab).append("},\n");

        // buttons
        List<CgButton> buttons = null;
        try {
            buttons = SqlUtil.sqlQuery(CgButton.class, "select * from cg_button where table_id=? order by order_num", table.getId());
        } catch (IotequException e) {
            throw new CgException(e);
        }
        if (buttons != null) {
            en.append(tab).append(tab).append("action: {\n");
            zh.append(tab).append(tab).append("action: {\n");
            for (int i=0;i<buttons.size();i++) {
                CgButton btn=buttons.get(i);
                if (!Util.isEmpty(btn.getConfirmText())) {
                    writeKey(en,3,btn.getAction()+"Confirm","Confirm " + StringUtil.firstLetterUpper(btn.getAction()).replaceAll("_", " "),false);
                    writeKey(zh,3,btn.getAction()+"Confirm",btn.getConfirmText(),false);
                }
                writeKey(en,3,btn.getAction(),StringUtil.firstLetterUpper(btn.getAction()).replaceAll("_", " "),i==(buttons.size()-1));
                writeKey(zh,3,btn.getAction(),btn.getTitle(),i==(buttons.size()-1));
            }
            en.append(tab).append(tab).append("},\n");
            zh.append(tab).append(tab).append("},\n");
        }

        // route
        int routes = 0;
        if (lists!=null && !lists.isEmpty())
            for (CgList item : lists)
                if (Objects.nonNull(item.getTagTitle()) && !item.getTagTitle().trim().isEmpty())  routes ++;
        if (forms!=null && !forms.isEmpty())
            for (CgForm item : forms)
                if (Objects.nonNull(item.getTagTitle()) && !item.getTagTitle().trim().isEmpty()) routes ++;
        if (routes > 0) {
            en.append(tab).append(tab).append("route: {\n");
            zh.append(tab).append(tab).append("route: {\n");
            int index = 0;
            if (lists!=null && !lists.isEmpty()) {
                for (CgList item : lists) {
                    if (Objects.nonNull(item.getTagTitle()) && !item.getTagTitle().trim().isEmpty()) {
                        writeKey(en, 3, item.getPath() + "Tag", item.getName(), index == routes - 1);
                        writeKey(zh, 3, item.getPath() + "Tag", item.getTagTitle(), index == routes - 1);
                        index++;
                    }
                }
            }
            if (forms!=null && !forms.isEmpty()) {
                for (CgForm item : forms) {
                    if (Objects.nonNull(item.getTagTitle()) && !item.getTagTitle().trim().isEmpty()) {
                        writeKey(en, 3, item.getPath().split(",")[0] + "Tag", item.getName(), index == routes - 1);
                        writeKey(zh, 3, item.getPath().split(",")[0] + "Tag", item.getTagTitle(), index == routes - 1);
                        index++;
                    }
                }
            }
            en.append(tab).append(tab).append("},\n");
            zh.append(tab).append(tab).append("},\n");
        }

        // fields
        en.append(tab).append(tab).append("field: {\n");
        zh.append(tab).append(tab).append("field: {\n");
        List<CgField> allFields = DtoService.getAllFieldsNotSplitedJoin(tabFields, joinFields, calFields);
        for (int i=0;i<allFields.size();i++) {
            CgField f = allFields.get(i);
            if (!Util.isEmpty(f.getValidTitle())) {
                writeKey(en,3,f.getEntityName() + "Valid","Please input correct value for " + f.getEntityName(),false);
                writeKey(zh,3,f.getEntityName() + "Valid",f.getValidTitle(),false);
            }

            String list = f.getDictText();
            if (list != null && !list.startsWith("f:") && Util.isEmpty(f.getDictTable()) && !Util.isEmpty(f.getDictField())) {
                String[] ll = list.split(",");
                String[] cc=f.getDictField().split(",");
                for (int c = 0; c < cc.length; c++) {
                    String ls="";
                    if (c<ll.length) ls = ll[c].trim();
                    writeKey(en,3,f.getEntityName() + "_" + c,ls,false);
                    writeKey(zh,3,f.getEntityName() + "_" + c,ls,false);
                }
            }
            if (f.getShowType().indexOf("join")>=0 || f.getShowType().equals("dict_list")) {
                String [] jjff = f.getDictText().trim().split(",");
                for (String jf:jjff) {
                    String[] jfArr = JoinUtil.splitedJoinFieldDef(jf);
                    if (!Util.isEmpty(jfArr[0]) && !Util.isEmpty(jfArr[1])) {
                        String entity = StringUtil.camelString(jfArr[1]);
                        writeKey(en,3,entity,jfArr[1].replaceAll("[_-]", " "),false);
                        writeKey(zh,3,entity,jfArr[0],false);
                    }
                }
            }
            writeKey(en,3,f.getEntityName(),StringUtil.firstLetterUpper(f.getEntityName()).replaceAll("_", " "),i==(allFields.size()-1));
            writeKey(zh,3,f.getEntityName(),f.getTitle(),i==(allFields.size()-1));
        }

        en.append(tab).append(tab).append("}\n");
        zh.append(tab).append(tab).append("}\n");

        en.append(tab).append("}\n").append("}\n");
        zh.append(tab).append("}\n").append("}\n");
        String path = NameUtil.languagePath(generatorPath,table,false);
        try {
            GenService.writeToFile(en.toString(), path + "/en-cg.js");
            GenService.writeToFile(zh.toString(), path + "/zh-cg.js");
        } catch (Exception e) {
            throw new CgException(e);
        }
    }
}
