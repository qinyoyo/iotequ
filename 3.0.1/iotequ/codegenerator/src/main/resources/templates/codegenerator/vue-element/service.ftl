<#ftl strip_whitespace=true>
<#assign D = "$" />
<#assign J = "#" />
<#assign useEntity = (table.entity?? && table.entity?trim!="") >
<#assign USEDB = (table.name?? && table.name?trim!="") >
<#assign USEFILE = false />
<#assign needSetDefault = false />
<#list fields as f>
    <#if pk?? && (f.showType=='file' || f.showType=='image')>
        <#assign USEFILE = true />
    </#if>
    <#if f.defaultValue?? && f.defaultValue?trim!="" && !f.isNull>
        <#assign needSetDefault = true />
    </#if>
</#list>
package ${basePackage}.service.impl;
<#assign FASTEXPORT = false />
<#if useEntity>
import ${basePackage}.${POJO}.${table.entity};
<#if USEDB>
import ${basePackage}.${DAO}.${table.entity}${DAO?cap_first};
<#if sons?? && table.actionList ?? && table.actionList?index_of(",export,") gte 0>
<#list sons as son>
<#if son.fastExport?? && son.fastExport!="">
<#assign FASTEXPORT = true />
import ${son.fastExport}.${POJO}.${son.entity};
import ${son.fastExport}.${DAO}.${son.entity}${DAO?cap_first};
</#if>
</#list>
</#if>
</#if>
<#if flowList ??>
import top.iotequ.framework.flow.*;
</#if>
</#if>
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import top.iotequ.framework.exception.IotequException;
import top.iotequ.framework.flow.IFlowService;
import top.iotequ.framework.service.impl.CgService;
import top.iotequ.framework.service.IDaoService;
import org.springframework.stereotype.Service;
import top.iotequ.framework.service.utils.DictionaryUtil;
import top.iotequ.framework.service.utils.UploadDownUtil;
import top.iotequ.framework.service.utils.QueryUtil;
import top.iotequ.util.*;
<#if table.imports?? && table.imports?trim !="">
<#assign imports = table.imports?trim?split(",") >
<#list imports as i>
import ${i};
</#list>
</#if>
import java.util.*;
<#assign hasDynamicQuery = false >
<#list fields as f>
    <#if f.showType=='select' && f.dictTable?? && f.dynaCondition?? && f.dynaCondition?trim!=''>
        <#assign hasDynamicQuery = true >
        <#break>
    </#if>
</#list>

/**************************************************
Create by iotequ codegenerator 3.0.0
Author : Qinyoyo
"**************************************************/
@ConditionalOnMissingClass({"${basePackage}.service.impl.${generatorName?cap_first}Service"})
@Service(value="${generatorName?uncap_first}Service")
public class Cg${generatorName?cap_first}Service extends CgService<${table.entity}>  {
    private static final Logger log = LoggerFactory.getLogger(Cg${generatorName?cap_first}Service.class);
<#if flowList ?? >
    private static final int maxNoteLength = ${maxNoteLength?c};
    @Autowired IFlowService<${table.entity}> flowService;
</#if>
<#if USEDB>
    @Autowired
    private ${table.entity}${DAO?cap_first} ${table.entity?uncap_first}${DAO?cap_first};
    <#if FASTEXPORT>
        <#list sons as son>
            <#if son.fastExport?? && son.fastExport!="">
    @Autowired
    private ${son.entity}${DAO?cap_first} ${son.entity?uncap_first}${DAO?cap_first};
            </#if>
        </#list>
    </#if>
</#if>
<#list fields as f>
<#if (f.showType=='select' || f.showType=='checkbox' || f.showType=='radio') && f.dictField?? && f.dictField?trim!="" &&  (!f.dictTable?? || f.dictTable?trim=="") && (f.dictField?trim?starts_with("f:") || (f.dictText?? && f.dictText?trim?starts_with("f:"))) >
<#if f.dictField?trim?starts_with("f:")>
    public static String [] dict${f.entityName?cap_first}Value= ${f.dictField?trim?substring(2)};
<#else>
    <#assign vv = f.dictField?split(",") >
    public static String [] dict${f.entityName?cap_first}Value= {<#list vv as v><#if v_index!=0>,</#if>"${v}"</#list>};
</#if>
<#if f.dictText?? && f.dictText?trim!="">
<#if f.dictText?trim?starts_with("f:")>
    public static String [] dict${f.entityName?cap_first}Text = ${f.dictText?trim?substring(2)};
<#else>
    <#assign tt = f.dictText?split(",") >
    public static String [] dict${f.entityName?cap_first}Text= {<#list tt as t><#if t_index!=0>,</#if>"${generatorName}.field.${f.entityName}_${t_index}"</#list>};
</#if>
<#else>
    public static String [] dict${f.entityName?cap_first}Text= null;
</#if>
</#if>
</#list>
    @Override
    public Class<${table.entity}> getEntityClass() {
        return ${table.entity}.class;
    }
    @Override
    public IDaoService<${table.entity}> getDaoService() {
        return <#if USEDB>${table.entity?uncap_first}${DAO?cap_first}<#else>null</#if> ;
    }
    @Override
    public IFlowService<${table.entity}> getFlowService() {
        return <#if flowList ?? >flowService<#else>null</#if>;
    }
    @Override
    public Map<String,Object> getDictionary(${table.entity} obj, Boolean useTree, String dynaFields) {
        Map<String,Object> map=new HashMap<>();
    <#list fields as f>
<#if (f.showType=="radio" || f.showType=="checkbox" || f.showType=="select")>
    <#if f.dictTable?? && f.dictTable?trim!="" && f.dictTable?starts_with("f:")>
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"${f.entityName}")) map.put("dict${f.entityName?cap_first}",${f.dictTable?substring(2)});
    <#elseif f.dictTable?? && f.dictTable?trim!="" && f.dictTable?starts_with("js:")>
        //dict${f.entityName?cap_first}: defined in vue file
    <#elseif f.dictField?? && f.dictField?trim!="" &&  (!f.dictTable?? || f.dictTable?trim=="") && (f.dictField?trim?starts_with("f:") || (f.dictText?? && f.dictText?trim?starts_with("f:"))) >
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"${f.entityName}")) map.put("dict${f.entityName?cap_first}", DictionaryUtil.getDictList(dict${f.entityName?cap_first}Value,dict${f.entityName?cap_first}Text));
    <#elseif f.dictTable?? &&  f.dictTable!="" && !f.dictTable?trim?starts_with("dict:")>
    <#if f.dictParent?? && f.dictParent?trim!="">
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"${f.entityName}")) {
            if (useTree) map.put("dict${f.entityName?cap_first}", DictionaryUtil.getTreeViewData(obj,"${f.dictTable}",<#if f.dictField?? && f.dictField?trim!="">"${f.dictField}"<#else>null</#if>,<#if f.dictText?? && f.dictText?trim!="">"${f.dictText}"<#else>null</#if>,"${f.dictParent}",<#if f.dictParentField?? && f.dictParentField?trim!="">"${f.dictParentField}"<#else>null</#if>,null,null,<#if f.dynaCondition?? && f.dynaCondition?trim!=''>"${f.dynaCondition}"<#else>null</#if>));
            else map.put("dict${f.entityName?cap_first}", DictionaryUtil.getDictListFromDatabase(obj,"${f.dictTable}",<#if f.dictField?? && f.dictField?trim!="">"${f.dictField}"<#else>null</#if>,<#if f.dictText?? && f.dictText?trim!="">"${f.dictText}"<#else>null</#if>,null,false,<#if f.dynaCondition?? && f.dynaCondition?trim!=''>"${f.dynaCondition}"<#else>null</#if>));
        }
    <#else>
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"${f.entityName}")) map.put("dict${f.entityName?cap_first}", DictionaryUtil.getDictListFromDatabase(obj,"${f.dictTable}",<#if f.dictField?? && f.dictField?trim!="">"${f.dictField}"<#else>null</#if>,<#if f.dictText?? && f.dictText?trim!="">"${f.dictText}"<#else>null</#if>,null,false,<#if f.dynaCondition?? && f.dynaCondition?trim!=''>"${f.dynaCondition}"<#else>null</#if>));
    </#if>
    </#if>
</#if>
    </#list>
        return map;
    }
    @Override
    public ${table.entity} getDefaultObject(${table.entity} ${table.entity?uncap_first}) throws IotequException {
<#if table.actionList ?? && table.actionList?index_of(",add,") gte 0 >
        checkAvailable();
        if (${table.entity?uncap_first}==null)  ${table.entity?uncap_first}=new ${table.entity}();
        <#if pk??>
        else ${table.entity?uncap_first}.set${pk.entityName?cap_first}(null);
        </#if>
        <#if flowList ?? >
        flowService.checkPrivilege(null,"add",null);
        </#if>
    <#list fields as f>
<#if f.defaultValue?? && f.defaultValue?trim!="" >
    <#if f.defaultValue?trim?length gt 2 && f.defaultValue?trim?substring(0,2)=="f:" >
        ${table.entity?uncap_first}.set${f.entityName?cap_first}(${f.defaultValue?trim?substring(2)});
    <#elseif f.defaultValue?trim?length gt 4 && f.defaultValue?trim?substring(0,4)=="sql:" >
        ${table.entity?uncap_first}.set${f.entityName?cap_first}((${f.type})QueryUtil.getSqlField("${f.defaultValue?trim?substring(4)}",${table.entity?uncap_first}));
    </#if>
</#if>
    </#list>
</#if>
        return ${table.entity?uncap_first};
    }
    @Override
    public void changeEmpty2Null(${table.entity} ${table.entity?uncap_first}) {
        if (Objects.nonNull(${table.entity?uncap_first})) {
        <#list fields as f>
            <#if f.isNull && f.type=="String" && f.keyType?? && f.keyType!='0' && f.keyType!=''>
            if ("".equals(${table.entity?uncap_first}.get${f.entityName?cap_first}())) ${table.entity?uncap_first}.set${f.entityName?cap_first}(null);
            </#if>
        </#list>
        }
    }
<#if needSetDefault>
    @Override
    public void changeNull2Default(${table.entity} ${table.entity?uncap_first}) {
<#list fields as f>
    <#if !f.isNull>
        if (${table.entity?uncap_first}.get${f.entityName?cap_first}()==null) {
        <#if f.defaultValue?? && f.defaultValue?trim!="" && (f.defaultValue?trim?length lt 3 || f.defaultValue?trim?substring(0,3)!="js:")>
            <#if f.defaultValue?trim?length gt 2 && f.defaultValue?trim?substring(0,2)=="f:" >
            ${table.entity?uncap_first}.set${f.entityName?cap_first}(${f.defaultValue?trim?substring(2)});
            <#elseif f.type=="Integer" || f.type=="Short" || f.type=="Long" || f.type=="Byte" || f.type=='BigDecimal'>
            ${table.entity?uncap_first}.set${f.entityName?cap_first}(${f.defaultValue});
            <#elseif f.type=="Boolean">
            ${table.entity?uncap_first}.set${f.entityName?cap_first}(Util.boolValue("${f.defaultValue}"));
            <#elseif f.type=="String">
            ${table.entity?uncap_first}.set${f.entityName?cap_first}("${f.defaultValue}");
            <#elseif f.type=="Date" && f.defaultValue=="now" >
            ${table.entity?uncap_first}.set${f.entityName?cap_first}(new Date());
            </#if>
        <#else>
            <#if f.type=="Integer" || f.type=="Short" || f.type=="Long" || f.type=="Byte" || f.type=='BigDecimal'>
            ${table.entity?uncap_first}.set${f.entityName?cap_first}(0);
            <#elseif f.type=="Boolean">
            ${table.entity?uncap_first}.set${f.entityName?cap_first}(false);
            <#elseif f.type=="String">
            ${table.entity?uncap_first}.set${f.entityName?cap_first}("");
            <#elseif f.type=="Date">
            ${table.entity?uncap_first}.set${f.entityName?cap_first}(new Date());
            </#if>
        </#if>
        }
    </#if>
</#list>
    }
</#if>
<#if USEFILE>
    @Override
    public void changeFileField( ${table.entity} ${table.entity?uncap_first}) {
<#list fields as f>
    <#if f.showType=='file' || f.showType=='image'>
        if (${table.entity?uncap_first}.get${pk.entityName?cap_first}()!=null)
            ${table.entity?uncap_first}.set${f.entityName?cap_first}(UploadDownUtil.removeFilesExclusive(getEntityClass(),"${f.entityName}",${table.entity?uncap_first}.get${pk.entityName?cap_first}().toString(),${table.entity?uncap_first}.get${f.entityName?cap_first}(),<#if f.isNull>true<#else>false</#if>));
        else
            ${table.entity?uncap_first}.set${f.entityName?cap_first}(<#if f.isNull>null<#else>""</#if>);
    </#if>
</#list>
    }
</#if>
}
