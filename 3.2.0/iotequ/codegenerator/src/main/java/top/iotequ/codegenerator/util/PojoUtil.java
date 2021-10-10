package top.iotequ.codegenerator.util;

import top.iotequ.codegenerator.pojo.CgField;
import top.iotequ.codegenerator.pojo.CgTable;
import top.iotequ.codegenerator.service.impl.DtoService;
import top.iotequ.codegenerator.service.impl.GenService;
import top.iotequ.util.StringUtil;
import top.iotequ.util.Util;

import java.util.List;

public class PojoUtil {

    public static void pojo(String generatorPath, CgTable table, List<CgField> tabFields, List<CgField> joinFields, List<CgField> calFields) throws CgException {
        assert (table != null);
        String tablePreFix = (joinFields.isEmpty() || Util.isEmpty(table.getName())? "" : table.getName().trim() + ".");
        String filePath = NameUtil.baseJavaPath(generatorPath, table,false) + "/" + GenService.POJO + "/" + table.getEntity() + ".java";
        StringBuilder sb = GenService.newBuilder();
        sb.append("package " + NameUtil.basePackage(table) + "." + GenService.POJO + ";\n");
        boolean imdtdeser = false,imdtser=false, imdser=false, imtser=false, imgson=false;
        boolean imbd = false;
        if (table.getTrialLicence() != null || table.getTrialDays() != null)
            sb.append("import top.iotequ.util.*;\n");
        List<CgField> allFields = DtoService.getAllFieldsNotSplitedJoin(tabFields,joinFields,calFields);
        sb.append("import top.iotequ.util.StringUtil;\n")
                .append("import top.iotequ.framework.pojo.CgEntity;\n")
                .append("import lombok.Getter;\n")
                .append("import lombok.Setter;\n");
        for (CgField f : allFields) {
            if (TypeUtil.javaType(f).indexOf("Date") >= 0) {
                if (!imdtdeser) {
                    sb //.append("import org.springframework.format.annotation.DateTimeFormat;\n")
                            .append("import com.fasterxml.jackson.databind.annotation.JsonDeserialize;\n")
                            .append("import com.fasterxml.jackson.databind.annotation.JsonSerialize;\n")
                            .append("import top.iotequ.framework.serializer.jackson.DateDeserializer;\n");
                    imdtdeser = true;
                }
                if (!imdtser && "datetime".equals(f.getShowType())) {
                    sb.append("import top.iotequ.framework.serializer.jackson.DatetimeSerializer;\n");
                    imdtser=true;
                } else if (!imdser && "date".equals(f.getShowType())) {
                    sb.append("import top.iotequ.framework.serializer.jackson.DateSerializer;\n")
                      .append("import top.iotequ.framework.serializer.gson.GsonDateTypeAdapter;\n");
                    if (!imgson) {
                        sb.append("import com.google.gson.annotations.JsonAdapter;\n");
                        imgson=true;
                    }
                    imdser=true;
                } else if (!imtser && "time".equals(f.getShowType())) {
                    sb.append("import top.iotequ.framework.serializer.jackson.TimeSerializer;\n")
                      .append("import top.iotequ.framework.serializer.gson.GsonTimeTypeAdapter;\n");
                    if (!imgson) {
                        sb.append("import com.google.gson.annotations.JsonAdapter;\n");
                        imgson=true;
                    }
                    imtser=true;
                }
            } else if (TypeUtil.javaType(f).indexOf("BigDecimal") >= 0 && !imbd) {
                sb.append("import java.math.BigDecimal;\n");
                imbd = true;
            }
            if (f.getShowType().indexOf("join") >= 0 && (!imdtdeser || !imdtser || !imdser || !imtser || !imbd)) {
                CgTable jtab = JoinUtil.joinTable(f);
                if (jtab != null) {
                    List<CgField> splitFields = JoinUtil.splitedJoinFields(table, f, tabFields, joinFields, calFields);
                    for (CgField jf : splitFields) {
                        if (TypeUtil.javaType(jf).indexOf("Date") >= 0) {
                            if (!imdtdeser) {
                                sb //.append("import org.springframework.format.annotation.DateTimeFormat;\n")
                                  .append("import com.fasterxml.jackson.databind.annotation.JsonDeserialize;\n")
                                  .append("import com.fasterxml.jackson.databind.annotation.JsonSerialize;\n")
                                  .append("import top.iotequ.framework.serializer.jackson.DateDeserializer;\n");
                                imdtdeser = true;
                            }
                            if (!imdtser && "datetime".equals(jf.getShowType())) {
                                sb.append("import top.iotequ.framework.serializer.jackson.DatetimeSerializer;\n");
                                imdtser=true;
                            } else if (!imdser && "date".equals(jf.getShowType())) {
                                sb.append("import top.iotequ.framework.serializer.jackson.DateSerializer;\n")
                                        .append("import top.iotequ.framework.serializer.gson.GsonDateTypeAdapter;\n");
                                if (!imgson) {
                                    sb.append("import com.google.gson.annotations.JsonAdapter;\n");
                                    imgson=true;
                                }

                                imdser=true;
                            } else if (!imtser && "time".equals(jf.getShowType())) {
                                sb.append("import top.iotequ.framework.serializer.jackson.TimeSerializer;\n")
                                        .append("import top.iotequ.framework.serializer.gson.GsonTimeTypeAdapter;\n");
                                if (!imgson) {
                                    sb.append("import com.google.gson.annotations.JsonAdapter;\n");
                                    imgson=true;
                                }
                                imtser=true;
                            }
                        } else if (TypeUtil.javaType(jf).indexOf("BigDecimal") >= 0 && !imbd) {
                            sb.append("import java.math.BigDecimal;\n");
                            imbd = true;
                        }
                    }
                }
            }
        }
        if (table.getPojoImports() != null) {
            String[] ii = table.getPojoImports().split(",");
            for (String i : ii) {
                i = i.trim();
                if (!i.isEmpty()) sb.append("import " + i + ";\n");
            }
        }
        if (!Util.isEmpty(table.getName())) {
            sb.append("import com.google.gson.annotations.SerializedName;\n");
            sb.append("import top.iotequ.util.CgFieldAnnotation;\n");
        }
        sb.append("import top.iotequ.util.CgTableAnnotation;\n");
        String imp = table.getPojoExtends();
        if (imp == null) imp = "";
        imp = addImplementsItem(imp, "CgEntity", "implements");
        boolean serialVersionUID = false;
        sb.append("import java.util.*;\n");
        sb.append("\n//  Pojo entity : " + table.getEntity() + " (" + table.getTitle() + ")\n");
        if ("sys_user".equals(table.getName())) {
            serialVersionUID = true;
            imp = addImplementsItem(imp, "UserDetails", "implements");
        } else if ("sys_role".equals(table.getName())) {
            serialVersionUID = true;
            imp = addImplementsItem(imp, "ConfigAttribute,GrantedAuthority", "implements");
        }
        StringBuilder joinCondition = new StringBuilder();
        CgField pk = GenService.priKey(table,tabFields,joinFields);
        String join = null;
        if (!Util.isEmpty(table.getName())) {
            for (CgField f : joinFields) {
                CgTable jtab = JoinUtil.joinTable(f);
                if (jtab != null) {
                    joinCondition.append(" ").append(JoinUtil.convertedJoinCondition(table,jtab,f,tabFields,joinFields,calFields));
                }
            }
            join = joinCondition.toString().trim();
        }
        sb.append("@CgTableAnnotation(name=\"" + table.getName() + "\",\n"
                + "                   title=\"" + NameUtil.tableTag(table) + "\",\n"
                + (Util.isEmpty(join) ? "" : "                   join=\"" + join + "\",\n")
                + ("id".equals(pk.getName()) ? "" : "                   pk=\""+pk.getName()+"\",\n")
                + ("id".equals(pk.getEntityName()) ? "" : "                   entityPk=\""+pk.getEntityName()+"\",\n")
                + "                   baseUrl=\"/" +  NameUtil.projectName(table).toLowerCase()
                        +"/" + (Util.isEmpty(table.getSubPackage()) ? "" : table.getSubPackage().trim() + "/") + NameUtil.generatorName(table) + "\",\n"
                + "                   hasLicence=" + ((table.getTrialLicence()!=null || table.getTrialDays()!=null) && pk!=null ? "true" : "false") + ",\n"
                + (table.getTrialLicence()==null ? "" : "                   trialLicence="+table.getTrialLicence()+",\n")
                + (table.getTrialDays()==null ? "" : "                   trialDays="+table.getTrialDays()+",\n")
                + (Util.getBean(GenService.class).parentEntity(table,tabFields,joinFields) == null ? "" : "                   parentEntityField=\""+ Util.getBean(GenService.class).parentEntity(table,tabFields,joinFields).getEntityName()+"\",\n")
                + (pk==null ? "" : "                   pkType=\""+TypeUtil.javaType(pk)+"\",\n")
                + (pk==null ? "" : "                   pkKeyType=\""+pk.getKeyType()+"\",\n")
                + "                   generatorName=\"" + NameUtil.generatorName(table) + "\",\n"
                + "                   module=\"" + NameUtil.projectName(table).toLowerCase() + "\")\n");
        sb.append("@Getter\n")
                .append("@Setter\n")
                .append("public class " + table.getEntity() + " " + imp + " {\n");
        if (serialVersionUID)
            sb.append("    private static final long serialVersionUID = "
                    + table.getEntity().hashCode() + "L;\n");
        for (CgField f : allFields) {
            String dt = TypeUtil.javaType(f);
            String name = f.getEntityName();
            if (dt.indexOf("Date") >= 0) {
                sb.append("    @JsonDeserialize(using = DateDeserializer.class)\n");
                if ("datetime".equals(f.getShowType())) {
                    sb // .append("    @DateTimeFormat(pattern = \"yyyy-MM-dd HH:mm:ss\")\n")
                            .append("    @JsonSerialize(using = DatetimeSerializer.class)\n");
                } else if ("date".equals(f.getShowType())) {
                    sb // .append("    @DateTimeFormat(pattern = \"yyyy-MM-dd\")\n")
                            .append("    @JsonSerialize(using = DateSerializer.class)\n")
                            .append("    @JsonAdapter(value= GsonDateTypeAdapter.class)\n");
                } else if ("time".equals(f.getShowType())) {
                    sb // .append("    @DateTimeFormat(pattern = \"HH:mm:ss\")\n")
                            .append("    @JsonSerialize(using = TimeSerializer.class)\n")
                            .append("    @JsonAdapter(value= GsonTimeTypeAdapter.class)\n");
                }
            }
            if (!Util.isEmpty(f.getName())) {
                String fmt = "";
                switch (f.getShowType()) {
                    case "date":
                        fmt = "yyyy-mm-dd";
                        break;
                    case "time":
                        fmt = "hh:mm";
                        break;
                    case "datetime":
                        fmt = "yyyy-mm-dd hh:mm";
                        break;
                    default:
                        switch (dt) {
                            case "Double":
                            case "Float":
                                fmt = "0.";
                                Integer scale = f.getNumericScale();
                                if (scale == null) scale = 2;
                                for (int i = 0; i < scale; i++) fmt += "0";
                                break;
                            case "String":
                                fmt = "@";
                                break;
                            default:
                                fmt = "";
                        }
                        ;
                }
                String aa = "";
                if (!name.equals(NameUtil.fieldName(f))) aa = "\"" + NameUtil.fieldName(f) + "\"";
                if (!name.equals(NameUtil.fieldName(f).toUpperCase()) && !NameUtil.fieldName(f).equals(NameUtil.fieldName(f).toUpperCase())) {
                    if (aa.isEmpty()) aa = "\"" + NameUtil.fieldName(f).toUpperCase() + "\"";
                    else aa += ",\"" + NameUtil.fieldName(f).toUpperCase() + "\"";
                }
                if (!Util.isEmpty(table.getName())) {
                    if (!aa.isEmpty())
                        sb.append("    @SerializedName(value = \"" + name + "\", alternate = {" + aa + "})\n");
                    sb.append("    @CgFieldAnnotation(name=\"" + (calFields.indexOf(f) >= 0 ? "" : tablePreFix) + NameUtil.fieldName(f) + "\""
                            + (Util.isEmpty(NameUtil.fieldExpression(f)) ? "" : ",expression=\"" + NameUtil.fieldExpression(f) + "\"")
                            + ",jdbcType=\"" + TypeUtil.jdbcType(f) + "\""
                            + (TypeUtil.lengthOf(f) > 0 ? ",length=" + TypeUtil.lengthOf(f) + (TypeUtil.lengthOf(f) > Integer.MAX_VALUE ? "L" : "") : "")
                            + ",nullable=" + (f.getIsNull() ? "true" : "false")
                            + ("image".equals(f.getShowType()) || "file".equals(f.getShowType()) ? ",multiple=" + (f.getDictMultiple() != null && f.getDictMultiple() ? "true" : "false") : "")
                            + ",format=\"" + fmt + "\""
                            + ")\n");
                }
            }
            sb.append("    private " + dt + " " + name + ";");
            if (Util.isEmpty(f.getTitle()))
                sb.append("\n");
            else
                sb.append("\t\t//" + f.getTitle() + (Util.isEmpty(f.getName()) ? " 非数据库字段" : " db field:" + f.getName()) + "\n");
            if (!Util.isEmpty(f.getName()) && (f.getShowType().indexOf("join")>= 0 || f.getShowType().equals("dict_list"))) {
                CgTable jtab = JoinUtil.joinTable(f);
                if (jtab != null) {
                    List<CgField> splitFields = JoinUtil.splitedJoinFields(table, f, tabFields, joinFields, calFields);
                    for (CgField jf : splitFields) {
                        if (TypeUtil.javaType(jf).indexOf("Date") >= 0) {
                            sb.append("    @JsonDeserialize(using = DateDeserializer.class)\n");
                            if ("datetime".equals(jf.getShowType())) {
                                sb // .append("    @DateTimeFormat(pattern = \"yyyy-MM-dd HH:mm:ss\")\n")
                                        .append("    @JsonSerialize(using = DatetimeSerializer.class)\n");
                            } else if ("date".equals(jf.getShowType())) {
                                sb // .append("    @DateTimeFormat(pattern = \"yyyy-MM-dd\")\n")
                                        .append("    @JsonSerialize(using = DateSerializer.class)\n")
                                        .append("    @JsonAdapter(value= GsonDateTypeAdapter.class)\n");
                            } else if ("time".equals(jf.getShowType())) {
                                sb // .append("    @DateTimeFormat(pattern = \"HH:mm:ss\")\n")
                                        .append("    @JsonSerialize(using = TimeSerializer.class)\n")
                                        .append("    @JsonAdapter(value= GsonTimeTypeAdapter.class)\n");
                            }
                        }

                        String sname = jf.getEntityName(), aname = jf.getName();
                        String aa = "";
                        if (!sname.equals(aname)) aa = "\"" + aname + "\"";
                        if (!sname.equals(aname.toUpperCase()) && !aname.equals(aname.toUpperCase())) {
                            if (aa.isEmpty()) aa = "\"" + aname.toUpperCase() + "\"";
                            else aa += ",\"" + aname.toUpperCase() + "\"";
                        }
                        if (!Util.isEmpty(table.getName())) {
                            if (!aa.isEmpty())
                                sb.append("    @SerializedName(value = \"" + sname + "\", alternate = {" + aa + "})\n");
                            sb.append("    @CgFieldAnnotation(name=\"" + JoinUtil.joinFullColumnName(jtab, f, jf) + "\""      // joinFields的id字段来记录数据表别名
                                    + (Util.isEmpty(NameUtil.fieldExpression(jf)) ? "" : ",expression=\"" + NameUtil.fieldExpression(jf) + "\"")
                                    + ",jdbcType=\"" + TypeUtil.jdbcType(f) + "\""
                                    + ")\n");
                        }
                        sb.append("    private " + TypeUtil.javaType(jf) + " " + jf.getEntityName() + ";\n");
                    }
                }

            }
            sb.append("\n");
        }
        if (Util.getBean(GenService.class).parentEntity(table,tabFields,joinFields) != null) {  // children 只能有一个，注意字段名不能有children
            sb.append("    private List<" + table.getEntity() + "> children;\n");
        }
        if (!Util.isEmpty(table.getActionList()) && table.getActionList().indexOf("flow")>=0) {
            sb.append("    private String flowAvailableActions;\n");
        }


        //<#if pk.type=="Integer"  || pk.type=="Short" || pk.type=="Long">
        sb.append("    @Override public Object getPkValue(){ return " +(pk==null?"null" : "get" +StringUtil.firstLetterUpper(pk.getEntityName()) + "()") + "; }\n");
        sb.append("    @Override\n" +
                "    public void setPkValue(Object value) {\n" +
                "        if (value==null) set" + StringUtil.firstLetterUpper(pk.getEntityName()) + "(null);\n" +
                "        else set" + StringUtil.firstLetterUpper(pk.getEntityName()) + "(" + TypeUtil.javaType(pk) + ".valueOf(value.toString()));\n" +
                "    }\n");
        sb.append("    @Override\n" +
                "    public String toString() {\n" +
                "    	return StringUtil.toJsonString(this);\n" +
                "    }\n");
        sb.append(CodeUtil.CODE_BY_USER + " start:\n");
        if (!Util.isEmpty(table.getPojoJavaCode())) {
            sb.append(table.getPojoJavaCode()).append("\n");
        }
        sb.append(CodeUtil.CODE_BY_USER + " end.\n");
        sb.append("}\n");
        GenService.writeToFile(sb.toString(), filePath);
    }
    public static String addImplementsItem(String s, String item, String key) {
        if (s == null || s.trim().isEmpty()) return key + " " + item;
        int pos = s.indexOf(key);
        if (pos < 0) return s + " " + key + " " + item;
        else {
            if (s.indexOf(item) > pos) return s;
            pos = pos + key.length();
            return s.substring(0, pos) + " " + item + "," + s.substring(pos);
        }
    }
}
