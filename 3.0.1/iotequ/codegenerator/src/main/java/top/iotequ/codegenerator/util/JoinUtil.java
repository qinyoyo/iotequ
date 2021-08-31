package top.iotequ.codegenerator.util;

import top.iotequ.codegenerator.pojo.CgList;
import top.iotequ.codegenerator.pojo.CgField;
import top.iotequ.codegenerator.pojo.CgListField;
import top.iotequ.codegenerator.pojo.CgTable;
import top.iotequ.codegenerator.service.impl.DtoService;
import top.iotequ.util.EntityUtil;
import top.iotequ.util.SqlUtil;
import top.iotequ.util.StringUtil;
import top.iotequ.util.Util;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class JoinUtil {
    // 完整的join引入字段名
    public static final String JOININFIELD = "join_in_field";
    public static String joinFullColumnName(CgTable jtab , CgField f, CgField jf) {
        return jf.getFkColumn();
        /*
        if (JOININFIELD.equals(jf.getFkTable())) return jf.getFkColumn();
        return f.getId()+"."+jf.getName(); */
    }
    // join关联字段必须在join表中定义
    public static String joinOnFullColumnName(CgTable table, CgField f) throws CgException {
        if (f==null|| Util.isEmpty(f.getDictTable())) throw new CgException(String.format("code：%s join 表定义的表单不能为空", table.getCode()));
        DtoService dtoService= Util.getBean(DtoService.class);
        CgTable jf = joinTable(f);
        if (jf==null) throw new CgException(String.format("code：%s join 表定义的表单 %s 不存在", table.getCode(),f.getDictTable().split(":")[0].trim()));
        CgField ref=dtoService.getFieldBy(jf.getId(),f.getDictField().trim());
        return f.getId()+"."+ref.getName();
    }
    // join关联字段必须在join表中定义
    public static String joinOnFieldEntity(CgTable jtab , String name) throws CgException {
        CgField f = Util.getBean(DtoService.class).getFieldBy(jtab.getId(),name);
        return f.getEntityName();
    }
    // 根据字段定义获得字段对应join表定义
    public static CgTable joinTable(CgField f) throws CgException {
        DtoService dtoService = Util.getBean(DtoService.class);
        CgList cgList = joinList(f);
        CgTable jf = dtoService.selectTable(cgList.getTableId());
        if (jf==null) throw new CgException(String.format("join 表定义的对应表单 %s 不存在",cgList.getTableId()));
        return jf;
    }

    /* join 引入字段的默认名称 */
    public static String joinName(CgTable jtab, CgField f, List<CgField> jtabAllFields, String joinField, List<CgField> tabFields, List<CgField> joinFields, List<CgField> calFields) throws CgException {
        DtoService dtoService = Util.getBean(DtoService.class);
        if (jtabAllFields==null) jtabAllFields = dtoService.getAllFieldsIncludeSplitedJoin(jtab);
        CgField j = joinField(f,joinField,jtabAllFields);
        String jname = (f.getShowType().equals("dict_list") ? ("dict_list_"+f.getName()):(f.getName()+"_"+jtab.getName()+"_"+j.getName()));
        String name = j.getName();
        if (Util.isEmpty(name)) throw new CgException(String.format("join 不能引入计算字段 (%s.%s)", jtab.getName(),j.getEntityName()));
        for (CgField field : tabFields) {
            if (field.getName()!=null && name.equals(field.getName())) return jname;
        }
        for (CgField field : calFields) {
            if (field.getName()!=null && name.equals(field.getName())) return jname;
        }
        for (CgField field : joinFields) {
            if (field.getName()!=null && name.equals(field.getName())) return jname;
            if (f.getEntityName().equals(field.getEntityName())) break;
            String [] jjff = field.getDictText().trim().split(",");
            for (String jf : jjff) {
                CgTable jt = joinTable(field);
                String [] jfArr1 = splitedJoinFieldDef(jf);
                List<CgField> jAllFields = dtoService.getAllFieldsIncludeSplitedJoin(jt);
                String nm = Util.isEmpty(jfArr1[1]) ? joinName(jt, field, jAllFields, jfArr1[2], tabFields, joinFields, calFields) : jfArr1[1];
                if (name.equals(nm)) return jname;
            }
        }
        return name;
    }

    public static CgList joinList(CgField f) throws CgException {
        if (f==null|| Util.isEmpty(f.getDictTable())) throw new CgException(String.format("join 表定义的列表视图不能为空", f.getDictTable()));
        DtoService dtoService = Util.getBean(DtoService.class);
        CgList cgList = dtoService.getCgList(f.getDictTable().trim());
        if (cgList==null) throw new CgException(String.format("join 表定义的列表视图 %s 不存在", f.getDictTable()));
        return cgList;
    }

    public static CgField joinField(CgField f, String fieldDef, List<CgField> jtabAllFields) throws CgException {
        CgList list = joinList(f);
        DtoService dtoService = Util.getBean(DtoService.class);
        List<CgListField> lfs = dtoService.getCgListFields(list.getId());
        if (jtabAllFields == null) jtabAllFields = dtoService.getAllFieldsIncludeSplitedJoin(joinTable(f));
        for (CgField field : jtabAllFields) {
            if (fieldDef.equals(field.getEntityName()) || fieldDef.equals(field.getName())) {
                if (lfs.stream().anyMatch(x -> field.getEntityName().equals(x.getEntityField()))) return field;
                else throw new CgException(String.format("join 表定义的列表视图 %s 没有包含字段 %s", f.getDictTable(), fieldDef));
            }
        }
        return null;
    }

    public static boolean useFirstJoinFieldInsteadOfMainField(CgField f) {
        String [] firstJc = f.getDictText().split(",")[0].trim().split("\\|");
        if ((firstJc.length>1 && !firstJc[1].isEmpty()) ||  (firstJc.length>2 && !firstJc[2].isEmpty())) return false;
        else return true;
    }
    /*
     * join字段处理方式（vue模式下不再需要 ：
     *   1、原字段通过 convertedJoinField 修改为新的字段进行生成。附加属性包括
     *        id = 关联弹窗的组件名
     *        tableId = join表单的code -> 通过  class = join-select 的 data-code 传递
     *        setDictTable = 关联组件组件对应的vue文件
     *        dictField ：修改为 原字段的entity名
     *        dictText : 本表字段名=关联表字段名,...
     *        editInline ： false
     *        fkTable : 嵌套的join条件
     *        fkColumn : join 进来的字段数据库字段名称,包含数据库表名，如 sys_user.real_name
     */
    public static CgField convertedJoinField(CgTable table, CgField f,List<CgField> tabFields, List<CgField> joinFields, List<CgField> calFields) throws CgException {
        CgField newField= EntityUtil.entityCopyFrom(CgField.class,f);
        CgTable refTab=joinTable(f);
        if (refTab!=null)  {
            DtoService dtoService = Util.getBean(DtoService.class);
            List<CgField> jtabAllFields = dtoService.getAllFieldsIncludeSplitedJoin(refTab);
            // dictParent 保留，为update传递数据
            String entity = joinOnFieldEntity(refTab,f.getDictField().trim());
            if (entity==null) throw new CgException(String.format("code：%s join 表定义的表单 %s entity字段 %s 不存在", table.getCode(),refTab.getCode(),f.getDictField().trim()));
            newField.setDictField(entity);
            newField.setFkTable(null);
            newField.setFkColumn(table.getName()+"."+f.getName());
            String [] jjff = f.getDictText().trim().split(",");
            String jFields = "";
            for (String jf:jjff) {
                String[] jfArr = splitedJoinFieldDef(jf);
                String joinField = jfArr[2];
                CgField j = joinField(f,joinField, jtabAllFields);
                if (j==null)  throw new CgException(String.format("code：%s join 表定义的表单 %s entity字段 %s 不存在", table.getCode(),refTab.getCode(),joinField));
                if (j.getShowType().indexOf("join")>=0)  throw new CgException(String.format("code：%s join 表定义的表单 %s entity字段 %s 存在嵌套的join调用", table.getCode(),refTab.getCode(),joinField));
                String jname = Util.isEmpty(jfArr[1]) ? joinName(refTab,f,jtabAllFields,jfArr[2],tabFields, joinFields, calFields) : jfArr[1];
                String jentity = StringUtil.camelString(jname);
                String exp = jentity + "=" + j.getEntityName();
                jFields = (jFields.isEmpty() ? exp : (jFields+","+exp));
                if ("dict_list".equals(f.getShowType())) break;
            }
            newField.setDictText(jFields);  // 标准化  x=y 的格式，在update使用);
            newField.setDictTable(joinComponentPath(f));   // 组件对应的文件
            newField.setId(joinComponent(f));  // 组件名
        }
        return newField;
    }
    public static String joinComponent(CgField f) throws CgException {
        CgList cgList=joinList(f);
       return NameUtil.componentName(cgList);  // 组件名

    }
    public static String joinComponentPath(CgField f) throws CgException {
        CgList cgList=joinList(f);
        return NameUtil.componentPath(cgList);
    }

    // join字段定义 [[自定义标题=]自定义名称=]join的字段
    // 返回 0：自定义标题 1: 自定义名称 2：join的字段
    public static String[] splitedJoinFieldDef(String jf) {
        String [] jfjf = jf.split("=");
        if (jfjf.length>=3)      return new String[] {jfjf[0].trim(),jfjf[1].trim(),jfjf[2].trim()};
        else if (jfjf.length==2) return new String[] {null,jfjf[0].trim(),jfjf[1].trim()};
        else if (jfjf.length==1) return new String[] {null,null,jfjf[0].trim()};
        else return new String[] {null,null,null};
    }
    public static boolean isSplitedJoinField(CgField f) {
        return f.getId().startsWith("join:") || f.getId().startsWith("list:");
    }
    /*   	  id = 'join:' 或 'list:' + 原表的entityName + ':' + dictField字段 ，只修改第一个，这个字段会弹出选择框，其余字段不弹框，不修改
     *        tableId = 表示弹框列表采用的组件
     *        title = 如果为字典模式，修改为原字段的标题
     *        fkTable : join_in_field 标识为一个join 进来的字段
     *        fkOnUpdate : joinCondition
    */
    public static  List<CgField> splitedJoinFields(CgTable table, CgField f,List<CgField> tabFields, List<CgField> joinFields, List<CgField> calFields) throws CgException {
        System.out.println(table.getName() + " " + f.getName());
        List<CgField> list=new ArrayList<CgField>();
        CgTable refTab = joinTable(f);
        if (refTab==null) return list;
        int  joinIndex=1;
        String sql = "select * from cg_field where table_id=? and (entity_name=? or name=?)";
        String defSql=null;
        if (!Util.isEmpty(f.getDefaultValue())) {
            try {
                CgField nf = SqlUtil.sqlQueryFirst(CgField.class, false, sql, refTab.getId(), f.getDictField().trim(), f.getDictField().trim());  //findFieldByName(refTab,f.getDictField().trim());
                if (nf != null && !Util.isEmpty(nf.getName())) {
                    defSql = String.format("sql:select %s from %s where %s=${%s}", "%s", refTab.getName(), nf.getName(), f.getEntityName());
                }
            } catch (Exception e) {
                throw new CgException(e);
            }
        }
        DtoService dtoService = Util.getBean(DtoService.class);
        List<CgField> jtabAllFields = dtoService.getAllFieldsIncludeSplitedJoin(refTab);
        String [] jjff = f.getDictText().trim().split(",");
        for (String jf:jjff) {
            String [] jfArr = splitedJoinFieldDef(jf);
            String joinField = jfArr[2];
            CgField nf = joinField(f,joinField,jtabAllFields);
            if (nf==null)  throw new CgException(String.format("code：%s join 表定义的表单 %s entity字段 %s 不存在", table.getCode(),refTab.getCode(),joinField));
            if (nf.getShowType().indexOf("join")>=0)  throw new CgException(String.format("code：%s join 表定义的表单 %s entity字段 %s 存在嵌套的join调用", table.getCode(),refTab.getCode(),joinField));
            nf.setFkColumn(f.getId()+"."+nf.getName());
            if (nf.getId().startsWith("join:")) { // join字段为另一个join引入字段，遍历找到最终字段
                String id = nf.getId();
                String entity = nf.getEntityName();
                String jc1 = "";
                CgTable pat = refTab;
                nf.setFkTable(JOININFIELD);
                String alias = f.getName();
                while (id.startsWith("join:")) {
                    String jfE = id.split(":")[1];
                    System.out.println(table.getName() + " " + f.getName()+" "+jfE);
                    CgField jf1 = dtoService.getFieldBy(pat.getId(), jfE);
                    if (jf1==null) throw new CgException("join 循环定义字段未找到："+pat.getId() + " "+jfE);
                    CgTable jtab = joinTable(jf1);
                    if (jtab==null) throw new CgException("join 循环定义join表未找到："+jf1.getName());
                    List<CgField> jFields = dtoService.getAllFieldsIncludeSplitedJoin(jtab);
                    CgField jjf = joinField(jf1,entity,jFields);
                    if (jjf==null) throw new CgException("join 循环定义字段未找到："+entity);
                    jf1.setId(alias + "_" + jtab.getName());
                    jf1.setShowType("left_join"); // 多重 join 一律修改为 左连接
                    if (jc1.isEmpty()) jc1 = joinCondition(pat, jtab, jf1, alias.equals(f.getName())?null:alias);
                    else jc1 += ("," + joinCondition(pat, jtab, jf1, alias.equals(f.getName())?null:alias));
                    alias += "_" + jtab.getName();
                    nf.setFkColumn(alias + "." + jjf.getName());
                    if (Util.isEmpty(jfArr[0]) && joinIndex>1) nf.setTitle(NameUtil.generatorName(jtab)+".field."+nf.getEntityName());
                    id = jjf.getId();
                    entity = jjf.getEntityName();
                    pat = jtab;
                }
                nf.setFkOnUpdate(jc1);
            } else {
                nf.setFkTable(null);
                nf.setFkOnUpdate(null);
                if (Util.isEmpty(jfArr[0]) && joinIndex>1) nf.setTitle(NameUtil.generatorName(refTab)+".field."+nf.getEntityName());    // 原字段字段标识
            }
            String jname = Util.isEmpty(jfArr[1]) ? joinName(refTab,f,jtabAllFields,jfArr[2],tabFields, joinFields, calFields) : jfArr[1];
            String jentity = StringUtil.camelString(jname);
            nf.setName(jname);
            nf.setEntityName(jentity);
            if (defSql==null) nf.setDefaultValue(null);
            else {
                nf.setDefaultValue(String.format(defSql,nf.getName()));
            }
            nf.setId((f.getShowType().equals("dict_list")?"list:":"join:")+f.getEntityName()+":"+ joinOnFieldEntity(refTab,f.getDictField().trim()));    // join 字段标识,用于 弹出join选择表，只在第一个 join字段上设置，所以第一个join字段必须可见
            nf.setDictMultiple(f.getShowType().equals("dict_list") ? true : f.getDictMultiple());
            //if (!Util.isEmpty(jfArr[0])) nf.setTitle(NameUtil.generatorName(table)+".field."+jentity);
            //else if (joinIndex==1) nf.setTitle(NameUtil.generatorName(table)+".field."+f.getEntityName());    // join字段字段标识
            nf.setTitle(NameUtil.generatorName(refTab)+".field."+jentity);  // 使用原表标签， 2021-08-31
            nf.setOrderNum(f.getOrderNum()+joinIndex);

            nf.setTableId(joinComponent(f));
            if (joinIndex==1 && useFirstJoinFieldInsteadOfMainField(f)) {  // 字典方式
                nf.setIsNull(f.getIsNull());
                nf.setValidExpression(f.getValidExpression());
            } else {
                nf.setIsNull(true);
            }
            nf.setDynaCondition(Util.isEmpty(f.getDynaCondition()) ? null : f.getDynaCondition());
            nf.setKeyType("0");
            list.add(nf);
            joinIndex++;
            if (f.getShowType().equals("dict_list")) break; // dict_list 只解析一个字段
        }
        return list;
    }

    // join 条件， f 必须是converted field
    private static String joinCondition(CgTable table, CgTable jtab, CgField f,String leftAlias) throws CgException {
        if (f.getShowType().indexOf("join")>=0) {
            boolean usealias = (!jtab.getName().equals(f.getId()));
            String jfield = joinOnFullColumnName(table,f);
            //CgField jfield = joinField(f,f.getDictField().trim(),null);
            return  f.getShowType().replace("_", " ").toUpperCase().replace("FULL ","")+" "
                    +jtab.getName()+(usealias ? " " + f.getId() : "")
                    +" ON "+(leftAlias==null?table.getName().trim():leftAlias) + "." + NameUtil.fieldName(f)+" = "+jfield; // joinFullColumnName(jtab,f,jfield);
        } else return null;
    }

    // join 条件
    public static String convertedJoinCondition(CgTable table, CgTable jtab, CgField f, List<CgField> tabFields, List<CgField> joinFields, List<CgField> calFields) throws CgException {
        if (f.getShowType().indexOf("join")>=0) {
            String jcondition = joinCondition(table,jtab,f,null);

            List<CgField> jfs = splitedJoinFields(table,f,tabFields,joinFields,calFields);
            jfs=jfs.stream().filter(item->!Util.isEmpty(item.getFkOnUpdate())).collect(Collectors.toList());
            for (int i=0;i<jfs.size();i++) {
                CgField item = jfs.get(i);
                if (Util.isEmpty(item.getFkOnUpdate())) continue;
                String [] joins = item.getFkOnUpdate().trim().split(",");
                if (joins.length>0) {
                    jcondition += " " + item.getFkOnUpdate().trim().replaceAll(","," ");
                    for (int j=i+1;j<jfs.size();j++) {
                        CgField item1=jfs.get(j);
                        String [] joins1 = item1.getFkOnUpdate().trim().split(",");
                        int difPos=0;
                        for (int k=0;k<joins.length && k<joins1.length; k++) {
                            if (joins[k].equals(joins1[k])) difPos = k+1;
                            else break;
                        }
                        if (difPos>0) {
                            if (difPos<joins1.length) {
                                String jcjc = joins1[difPos];
                                for (int k=difPos+1;k< joins1.length;k++) jcjc += (","+joins1[k]) ;
                                item1.setFkOnUpdate(jcjc);
                            } else item1.setFkOnUpdate("");
                        }
                    }
                }
            }


            return  jcondition ;
        } else return null;
    }
    public static String [] changeAlias(String jc, String alias) {
        Pattern p = Pattern.compile("(\\S+)\\s+join\\s+(.+)\\s+on\\s+(.+)",Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(jc);
        if (m.find()) {
            String table = m.group(2).trim().split(" ")[0];
            String [] r = new String[2];
            r[0] = table+"_"+alias;
            r[1] = m.group(1) + " JOIN " + table + " " + r[0] + " ON " + m.group(3);
            return r;
        } return null;
    }
}
