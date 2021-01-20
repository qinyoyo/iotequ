package top.iotequ.codegenerator.util;

import lombok.NonNull;
import top.iotequ.codegenerator.pojo.CgField;
import top.iotequ.codegenerator.pojo.CgFormField;
import top.iotequ.framework.flow.IFlowService;
import top.iotequ.util.Util;

import java.util.List;
import java.util.Optional;

public class FlowUtil {
    private static void setFieldProperties(@NonNull CgField f,@NonNull String entityField, String tableId, boolean isNew,String dynaFields, String type) {
        if (isNew) {
            f.setId("id");
            f.setOrderNum(0);
            f.setKeyType("0");
            f.setType(Util.isEmpty(type,"varchar"));
            f.setTableId(tableId);
            f.setEntityName(entityField);
        }
        f.setDictFullName(false);
        f.setDictMultiple(false);
        f.setDictParent(null);
        f.setIsNull(true);
        if (Util.isEmpty(dynaFields))   f.setDynaCondition(null);
        else {
            String [] dd = dynaFields.split(",");
            String dyna="";
            for (String d: dd) dyna = dyna +" ${" + d + "}";
            f.setDynaCondition(dyna);
        }
        switch (entityField) {
            case IFlowService.flowSelection :
                f.setTitle("system.action.pleaseSelect");
                f.setShowType("radio");
                f.setDefaultValue(IFlowService.APPROVE);
                f.setDictTable("f:getFlowService()==null ? null: getFlowService().getSelections(Util.getRequest().getParameter(IFlowService.flowAction))");
                f.setDynaCondition(null);
                break;
            case IFlowService.flowNote:
                f.setTitle("system.action.flowNote");
                f.setShowType("textarea");
                f.setDictTable(null);
                break;
            case IFlowService.flowNextOperator:
                f.setTitle("system.action.flowNextOperator");
                f.setShowType("select");
                f.setDefaultValue(null);
                f.setDictTable("f:getFlowService()==null ? null: getFlowService().getDictionaryOfNextOperator(obj,Util.getRequest().getParameter(IFlowService.flowAction),Util.getRequest().getParameter(IFlowService.flowSelection))");
                break;
            case IFlowService.flowCopyToList:
                f.setTitle("system.action.flowCopyToList");
                f.setShowType("select");
                f.setDefaultValue(null);
                f.setDictMultiple(true);
                f.setDictTable("f:getFlowService()==null ? null: getFlowService().getDictionaryOfCopyToList(obj,Util.getRequest().getParameter(IFlowService.flowAction),Util.getRequest().getParameter(IFlowService.flowSelection))");
                break;
/*
            case IFlowService.flowState:
                f.setTitle("system.action.flowState");
                f.setShowType("select");
                f.setDefaultValue(null);
                f.setDictTable("f:getFlowService()==null ? null: getFlowService().getStates()");
                f.setDynaCondition(null);
                break;

 */
            default:
        }
    }
    public static void setFlowField(@NonNull List<CgField> list, @NonNull String entityField, String dynaFields, String type) {
        String tableId = list.get(0).getTableId();
        Optional<CgField> of = list.stream().filter(f -> entityField.equals(f.getEntityName())).findFirst();
        if (of.isPresent()) {
            CgField f=of.get();
            setFieldProperties(f,entityField, tableId,false,dynaFields, type);
        } else {
            CgField f = new CgField();
            setFieldProperties(f,entityField,tableId,true,dynaFields, type);
            list.add(f);
        }
    }

    private static void setFormFieldProperties(CgFormField f,@NonNull String entityField, String formId, boolean isNew) {
        if (isNew) {
            f.setFormId(formId);
            f.setEntityField(entityField);
            f.setGroupTitle(null);
            f.setValidateAsTitle(false);
            f.setReadonly(false);
            f.setId(1);
        }
        f.setHidden(false);
        switch (entityField) {
            case IFlowService.flowSelection :
                f.setWidth(24);
                f.setMustInput(true);
                break;
            case IFlowService.flowNote:
                f.setWidth(24);
                f.setMustInput(false);
                break;
            case IFlowService.flowNextOperator:
                f.setWidth(8);
                f.setMustInput(false);
                break;
            case IFlowService.flowCopyToList:
                f.setWidth(16);
                f.setMustInput(false);
                break;
            default:
        }
    }

    public static void setFormFlowField(@NonNull List<CgFormField> list, @NonNull String entityField, boolean newGroup, String groupTitle) {
        String formId = list.get(0).getFormId();
        Optional<CgFormField> of = list.stream().filter(f -> entityField.equals(f.getEntityField())).findFirst();
        if (of.isPresent()) {
            CgFormField f=of.get();
            setFormFieldProperties(f,entityField, formId,false);
            if (!Util.isEmpty(groupTitle)) f.setGroupTitle(groupTitle);
            if (newGroup) {
                list.remove(f);
                f.setOrderNum(list.get(list.size() - 1).getOrderNum() + 1);
                list.add(f);
            }
        } else {
            CgFormField f = new CgFormField();
            setFormFieldProperties(f,entityField,formId,true);
            if (!Util.isEmpty(groupTitle)) f.setGroupTitle(groupTitle);
            if (!newGroup) {
                for (int i = 1; i < list.size(); i++) {
                    if (!Util.isEmpty(list.get(i).getGroupTitle())) {
                        f.setOrderNum(list.get(i - 1).getOrderNum() + 1);
                        list.add(i, f);
                        return;
                    }
                }
            }
            f.setOrderNum(list.get(list.size() - 1).getOrderNum() + 1);
            list.add(f);
        }
    }
}
