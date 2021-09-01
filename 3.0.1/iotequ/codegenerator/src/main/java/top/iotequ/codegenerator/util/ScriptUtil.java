package top.iotequ.codegenerator.util;

import top.iotequ.codegenerator.pojo.CgField;
import top.iotequ.codegenerator.pojo.CgTable;
import top.iotequ.codegenerator.service.impl.DtoService;
import top.iotequ.codegenerator.service.impl.GenService;
import top.iotequ.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ScriptUtil {
    public static final int MYSQL = 1;
    public static final int SQLSERVER = 2;
    public static final int ORACLE = 3;


    public static String unionUniqueIndex(CgTable table, List<CgField> tabFields, List<CgField> joinFields, int dbType, String no) {
        List<CgField> uf = new ArrayList<CgField>();
        for (CgField f : tabFields)
            if (!Util.isEmpty(f.getName()) && f.getName().indexOf(":") < 0 && no.equals(f.getKeyType()))
                uf.add(f);   // 计算字段不能作为索引字段
        for (CgField f : joinFields)
            if (!Util.isEmpty(f.getName()) && f.getName().indexOf(":") < 0 && no.equals(f.getKeyType()))
                uf.add(f);   // 计算字段不能作为索引字段
        if (uf.isEmpty()) return null;
        String uname = "ui_" + table.getName();
        String fname = "";
        int i = 0;
        for (CgField f : uf) {
            uname += String.valueOf(i);
            if (fname.isEmpty()) fname = NameUtil.fieldName(dbType, f.getName());
            else fname = fname + "," + NameUtil.fieldName(dbType, f.getName());
            i++;
        }
        uname = NameUtil.fieldName(dbType, uname);
        return "ALTER TABLE " + NameUtil.objectName(dbType, table.getName()) +
                " ADD CONSTRAINT  " + uname + " UNIQUE (" + fname + ")";
    }

    public static void dbScript(String generatorPath, CgTable table, List<CgField> tabFields, List<CgField> joinFields, int dbType, List<Map<String, Object>> allCodeList,boolean onlyFK) throws CgException {
        assert (table != null);
        DtoService dtoService = Util.getBean(DtoService.class);
        String dFile = NameUtil.dbScriptName(generatorPath, table, dbType);
        StringBuilder sb = new StringBuilder();
        StringBuilder sb1 = new StringBuilder();

        if (!onlyFK) {
            sb.append("-- ----------------------------\n" +
                    "-- Table structure for " + table.getName() + "\n" +
                    "-- ----------------------------\n");
//		String L="",R="";
            if (dbType == MYSQL) {
                sb.append("DROP TABLE IF EXISTS " + NameUtil.objectName(dbType, table.getName()) + ";\n");
            } else if (dbType == SQLSERVER) {
                sb.append("IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('" + NameUtil.objectName(dbType, table.getName())
                        + "') AND type IN ('U'))\n" +
                        "	DROP TABLE " + NameUtil.objectName(dbType, table.getName()) + "\n" +
                        "GO\n");

            } else if (dbType == ORACLE) {
                sb.append("CALL REMOVE_OBJECT('TABLE','" + table.getName().toUpperCase() + "','IOTEQU');\n");
            }
            sb.append("CREATE TABLE " + NameUtil.objectName(dbType, table.getName()) + " (\n");
            List<CgField> columList = new ArrayList<CgField>();
            columList.addAll(tabFields);
            columList.addAll(joinFields);
            GenService.fieldListSort(columList);
            for (CgField f : columList) {
                if (Util.isEmpty(f.getName()) || f.getName().indexOf(":") >= 0) continue;
                sb.append("  " + NameUtil.fieldName(dbType, f.getName()));
                sb.append(" " + (dbType == ORACLE ? TypeUtil.dbDataType(f, dbType).toUpperCase() : TypeUtil.dbDataType(f, dbType)));
                if (f.getKeyType() != null && f.getKeyType().equals("1")) {
                    if (dbType == MYSQL) {
                        sb.append(" AUTO_INCREMENT");
                    } else if (dbType == SQLSERVER) {
                        sb.append(" IDENTITY(1,1)");
                    }
                }
                if (!Util.isEmpty(f.getDefaultValue()) && !f.getDefaultValue().startsWith("f:") && !f.getDefaultValue().startsWith("js:")) {
                    sb.append(" DEFAULT ");
                    String dv = (TypeUtil.isNumbericType(f) ? f.getDefaultValue() : "'" + f.getDefaultValue() + "'");
                    if (dbType == SQLSERVER)
                        sb.append((TypeUtil.isNumbericType(f) ? "((" : "(") + dv + (TypeUtil.isNumbericType(f) ? "))" : ")"));
                    else
                        sb.append(dv);
                }
                sb.append(f.getIsNull() ? " NULL" : " NOT NULL");   //  oracle 空串 就是null
                String key = f.getKeyType();
                if (key != null && ("1".equals(key) || "2".equals(key) || "3".equals(key))) { // 1
                    sb.append(" PRIMARY KEY");
                    if ("1".equals(key) && dbType == ORACLE) {
                        sb1.append("CALL REMOVE_OBJECT('SEQUENCE','SEQUENCE_" + table.getName().toUpperCase() + "','IOTEQU');\n");
                        sb1.append("CREATE SEQUENCE SEQUENCE_" + table.getName().toUpperCase() + ";\n");
                    }
                } else if ("4".equals(key)) {
                    if (!f.getIsNull() || dbType == MYSQL || dbType == ORACLE)
                        sb.append(" UNIQUE");
                    else if (dbType == SQLSERVER) {
                        String fn = "fn_multi_null_" + table.getName() + "_" + f.getName();
                        sb1.append("IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('" + NameUtil.objectName(dbType, fn) + "') AND type IN ('FN'))\n" +
                                "	DROP FUNCTION " + NameUtil.objectName(dbType, fn) + "\n" +
                                "GO\n");
                        sb1.append("CREATE FUNCTION " + NameUtil.objectName(dbType, fn) +
                                "(" + "    @value " + TypeUtil.dbDataType(f, dbType) + ")\n");
                        sb1.append("RETURNS bit\n" +
                                "AS\n" +
                                "BEGIN\n" +
                                "  DECLARE @result bit\n" +
                                "	DECLARE @ct  int\n" +
                                "	IF @value is null  or @value =" + (TypeUtil.isNumbericType(f) ? "0" : "''") + "\n" +
                                "		SET @result=1\n" +
                                "	ELSE\n" +
                                "	  BEGIN\n" +
                                "			select @ct=count(*) from "+table.getName()+" where "+f.getName()+"=@value\n" +
                                "			select @result = case when @ct>1 then 0 else 1 end\n" +
                                "		END	\n" +
                                "  RETURN @result\n" +
                                "END\nGO\n");
                        sb1.append("Alter table " + NameUtil.objectName(dbType, table.getName()) + " Add constraint unique_multi_null_" + table.getName() + "_" + f.getName()
                                + " check (" + NameUtil.objectName(dbType, fn) + "(" + f.getName() + ")=1);\nGO\n");
                    }
                } else if ("5".equals(key)) {
                    if (dbType == ORACLE) {
                        sb1.append("CALL REMOVE_OBJECT('INDEX','INDEX_" + table.getName().toUpperCase() + "_" + f.getName().toUpperCase() + "','IOTEQU');\n");
                        sb1.append("CREATE INDEX " + NameUtil.objectName(dbType, "INDEX_" + table.getName().toUpperCase() + "_" + f.getName().toUpperCase())
                                + " ON " + NameUtil.objectName(dbType, table.getName().toUpperCase()) + "(" + NameUtil.fieldName(dbType, f.getName().toUpperCase()) + ");\n");
                    } else {
                        sb1.append("CREATE INDEX " + NameUtil.objectName(dbType, "index_" + table.getName() + "_" + f.getName())
                                + " ON " + NameUtil.objectName(dbType, table.getName()) + "(" + NameUtil.fieldName(dbType, f.getName()) + ");\n");
                    }
                }
                if (dbType == SQLSERVER && TypeUtil.jdbcType(f).toLowerCase().contains("char")) {
                    sb.append(" COLLATE Chinese_PRC_CI_AS");
                }
                if (!Util.isEmpty(f.getTitle())) {
                    if (dbType == MYSQL) {
                        sb.append(" COMMENT '" + f.getTitle() + "'");
                    }
                }
                sb.append(",\n");
            }
            sb.deleteCharAt(sb.length() - 2);
            if (dbType == MYSQL) {
                sb.append(") ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci "
                        + (Util.isEmpty(table.getTitle()) ? "" : "COMMENT = '" + table.getTitle() + "' ")
                        + "ROW_FORMAT = Dynamic;\n");
            } else if (dbType == SQLSERVER) {
                sb.append(")\n" + "GO\n" +
                        "ALTER TABLE [dbo].[" + table.getName() + "] SET (LOCK_ESCALATION = TABLE)\n" + "GO\n");
            } else if (dbType == ORACLE) {
                sb.append(");\n");
            }
            GenService.appendToFile(sb.toString(), dFile);
            sb1.append("\n");
            //处理多字段唯一索引
            for (int i = 11; i <= 13; i++) {
                String ui = unionUniqueIndex(table, tabFields, joinFields, dbType, String.valueOf(i));
                if (ui != null) {
                    sb1.append(ui);
                    if (dbType == SQLSERVER)
                        sb1.append("\nGO\n");
                    else sb1.append(";\n");
                }
            }
        }
        if (allCodeList != null && allCodeList.size() > 0) {
            for (int i = 0; i < allCodeList.size(); i++) {
                String code = allCodeList.get(i).get("code").toString();
                String projectId = allCodeList.get(i).get("project_id").toString();
                CgTable t = dtoService.getTableByProjectIdAndCode(projectId,code);
                if (t != null && t.getId() != null) {
                    List<CgField> ff = dtoService.getFields(t.getId())
                            .stream().filter(f->!Util.isEmpty(f.getName()) && !Util.isEmpty(f.getFkTable()) && !Util.isEmpty(f.getFkColumn())).collect(Collectors.toList());
                    if (!Util.isEmpty(ff)) {
                        for (CgField f : ff) {
                            String fkName = NameUtil.objectName(dbType, "fk_" + t.getName() + "_" + f.getName() + "_" + f.getFkTable() + "_" + f.getFkColumn());
                            String script = String.format("ALTER TABLE %s ADD CONSTRAINT %s FOREIGN KEY(%s) REFERENCES %s(%s)",
                                    NameUtil.objectName(dbType, t.getName()), fkName, NameUtil.fieldName(dbType, f.getName()), NameUtil.objectName(dbType, f.getFkTable()), NameUtil.fieldName(dbType, f.getFkColumn()));
                            sb1.append(script);
                            if (!Util.isEmpty(f.getFkOnDelete())) sb1.append(" ON DELETE ").append(f.getFkOnDelete());
                            if (!Util.isEmpty(f.getFkOnUpdate())) sb1.append(" ON UPDATE ").append(f.getFkOnUpdate());
                            sb1.append(";\n");
                        }
                    }
                }
            }
        }
        if ("sys_menu".equals(table.getName()))
            init_menu(sb1, dbType);
        GenService.appendToFile(sb1.toString(), dFile);
    }

    static void init_menu(StringBuilder sb, int dbType) {
        return;
        /*
        String s =
                "INSERT INTO [dbo].[sys_menu] ([id],[name],[mobile_hidden],[sort_num],[parent],[icon],[bigIcon],[action],[js_cmd],[class_name],[data_action],[is_divider]) VALUES (1,'menu.systemManage',0,10,NULL,'fa fa-gear','',NULL,'','','',0);\n" +
                "INSERT INTO [dbo].[sys_menu] ([id],[name],[mobile_hidden],[sort_num],[parent],[icon],[bigIcon],[action],[js_cmd],[class_name],[data_action],[is_divider]) VALUES (2,'sysMenu.title.code',0,130,1,'fa fa-bars','','/framework/sysMenu/list',NULL,NULL,NULL,0);\n" +
                "INSERT INTO [dbo].[sys_menu] ([id],[name],[mobile_hidden],[sort_num],[parent],[icon],[bigIcon],[action],[js_cmd],[class_name],[data_action],[is_divider]) VALUES (3,'sysRole.title.code',0,70,1,'fa fa-chrome','','/framework/sysRole/list',NULL,NULL,NULL,0);\n" +
                "INSERT INTO [dbo].[sys_menu] ([id],[name],[mobile_hidden],[sort_num],[parent],[icon],[bigIcon],[action],[js_cmd],[class_name],[data_action],[is_divider]) VALUES (4,'sysAction.title.code',1,110,1,'fa fa-qrcode','','/framework/sysAction/list',NULL,NULL,NULL,0);\n" +
                "INSERT INTO [dbo].[sys_menu] ([id],[name],[mobile_hidden],[sort_num],[parent],[icon],[bigIcon],[action],[js_cmd],[class_name],[data_action],[is_divider]) VALUES (7,'sysOrg.title.code',0,90,1,'fa fa-sitemap','','/framework/sysOrg/list',NULL,NULL,NULL,0);\n" +
                "INSERT INTO [dbo].[sys_menu] ([id],[name],[mobile_hidden],[sort_num],[parent],[icon],[bigIcon],[action],[js_cmd],[class_name],[data_action],[is_divider]) VALUES (8,'sysUser.title.code',0,100,1,'fa fa-user-plus','','/framework/sysUser/list',NULL,NULL,NULL,0);\n" +
                "INSERT INTO [dbo].[sys_menu] ([id],[name],[mobile_hidden],[sort_num],[parent],[icon],[bigIcon],[action],[js_cmd],[class_name],[data_action],[is_divider]) VALUES (9,'sysDataDict.title.code',1,80,1,'fa fa-book','','/framework/sysDataDict/list','','','',0);\n" +
                "INSERT INTO [dbo].[sys_menu] ([id],[name],[mobile_hidden],[sort_num],[parent],[icon],[bigIcon],[action],[js_cmd],[class_name],[data_action],[is_divider]) VALUES (10,'sysLog.title.code',0,140,1,'fa fa-file-text-o','','/framework/sysLog/list','','','',0);\n" +
                "INSERT INTO [dbo].[sys_menu] ([id],[name],[mobile_hidden],[sort_num],[parent],[icon],[bigIcon],[action],[js_cmd],[class_name],[data_action],[is_divider]) VALUES (11,'sysTask.title.code',1,150,1,'fa fa-clock-o','','/framework/sysTask/list','','','',0);\n";
        if (dbType == MYSQL) {
            sb.append(s.replace("[dbo].", "").replace("[", "`").replace("]", "`"));
        } else if (dbType == SQLSERVER) {
            sb.append("set IDENTITY_INSERT sys_menu ON;\n");
            sb.append(s);
            sb.append("set IDENTITY_INSERT sys_menu OFF;\n");
        } else if (dbType == ORACLE) {
            sb.append(s.replace("[dbo].[sys_menu] ([id],[name],[mobile_hidden],[sort_num],[parent],[icon],[bigIcon],[action],[js_cmd],[class_name],[data_action],[is_divider])",
                    "\"SYS_MENU\"(\"ID\",\"NAME\",\"MOBILE_HIDDEN\",\"SORT_NUM\",\"PARENT\",\"ICON\",\"BIGICON\",\"ACTION\",\"JS_CMD\",\"CLASS_NAME\",\"DATA_ACTION\",\"IS_DIVIDER\")"
            ));
        }
        */
    }

}
