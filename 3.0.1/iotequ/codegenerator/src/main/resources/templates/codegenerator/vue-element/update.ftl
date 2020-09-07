<#assign D = "$" />
<#assign J = "#" />
<#assign SP='' />
<#assign AUTOFOCUS=true, USEFILE = false , joinedFileds = '', joinClickField='', tabPaneIndex = -1 />

<#assign DICTLIST = [] , NEEDLOADFROMSERVER = false />
<#list fields as f>
  <#if (f.showType=="radio" || f.showType=="checkbox" || f.showType=="select") >
    <#if f.dictTable?? && f.dictTable?trim!="" && f.dictTable?starts_with("js:") >
      <#assign DICTLIST = DICTLIST + ["dict"+f.entityName?cap_first+": "+f.dictTable?substring(3)] />
    <#elseif f.dictTable?? && f.dictTable?trim!="" && !f.dictTable?trim?starts_with("dict:")>
      <#assign DICTLIST = DICTLIST + ["dict"+f.entityName?cap_first+": []"], NEEDLOADFROMSERVER = true />
    <#elseif (!f.dictTable?? || f.dictTable?trim=="") && f.dictField?? && f.dictField?trim!="" && (f.dictField?trim?starts_with("f:") || (f.dictText?? && f.dictText?trim?starts_with("f:"))) >
      <#assign DICTLIST = DICTLIST + ["dict"+f.entityName?cap_first+": []"], NEEDLOADFROMSERVER = true />
    <#elseif (!f.dictTable?? || f.dictTable?trim=="") && f.dictField?? && f.dictField?trim!="" && f.dictText?? && f.dictText?trim!=''>
      <#assign DICTLIST = DICTLIST + ["dict"+f.entityName?cap_first+": this.getDictionary('"+f.dictField?trim+"','"+f.dictText?trim+"')"] />
    <#elseif (!f.dictTable?? || f.dictTable?trim=="") && f.dictField?? && f.dictField?trim!="">
      <#assign DICTLIST = DICTLIST + ["dict"+f.entityName?cap_first+": this.getDictionary('"+f.dictField?trim+"')"] />
    </#if>
  </#if>
</#list>

<#if pk??>
<#list fields as f>
<#if (f.showType=='file' || f.showType=='image')>
  <#assign USEFILE = true />
</#if>
</#list>
</#if>
<#function canUse p pp>
  <#if pp?? && pp?trim!='' && (pp?trim?contains(p) || (p?starts_with(':') && pp?contains(p?substring(1))))>
    <#return false />
  <#else>
    <#return true />
  </#if>
</#function>
<#function getTextFrom p pp def>
  <#if pp==''><#return def /></#if>
  <#assign res = pp?matches('(:{0,1})'+p+'\\s*=\\s*"([^"]*)"')>
  <#list res as m>
    <#if m?groups[1]==':'>
    <#return '{{'+m?groups[2]+'}}' />
    <#else>
    <#return m?groups[2] />
    </#if>
  </#list>
  <#return def />
</#function>
<#function dictionary f>
<#if f.dictTable?? && f.dictTable?trim?starts_with("dict:")>
  <#return "dictionary.dict"+f.dictTable?trim?substring(5)?cap_first>    
<#else>
  <#return "dictionary.dict"+f.entityName?cap_first>
</#if>
</#function>
<#function isEmpty v>
<#return v?trim=='' />
</#function>
<#macro SPACE n><#list 1..n as i> </#list></#macro>
<#macro WF f key value="true"><#if canUse(key,f.itemProperties!'')>${key}<#if value!='true'>="${value}"</#if> </#if></#macro>
<#macro WForm f key value="true"><#if canUse(key,f.formProperties!'')>${key}<#if value!='true'>="${value}"</#if> </#if></#macro>
<#macro READONLY f><#if isJoinFieldNow><@WF f "readonly" /><#elseif f.readonly && f.mustInput><@WF f ":readonly" "!isNew" /><#elseif f.readonly><@WF f "readonly"/><#else><@WF f ":readonly" "isDetail"/></#if></#macro>
<#assign SYSACTIONS = ",_add,_edit,_view,_delete,_batdel,_import,_export,_flow,_refresh,_reset,_toolbar,_switch,_column,_search,_doSearch," />
<#assign USERACTION = false, USEINPUTNUMBER = false />
<#macro elFormItem f>
      ${HD}<#nt><el-form-item<#if !isEmpty(f.formItemProperties!'')> ${f.formItemProperties?trim}</#if><#if canUse("class",f.formItemProperties!'')> class="cg-item-${f.showType}<#if AUTOFOCUS && (!f.readonly || f.mustInput) && f.showType!='image' && f.showType!='2dcode' && f.showType!='html' && f.showType!='color'> cg-auto-focus<#assign AUTOFOCUS=false/></#if>"</#if><#if popoverinner><#if canUse("slot",f.formItemProperties!'')> slot="reference"</#if></#if><#if canUse("label",f.formItemProperties!'')> :label="$t('${f.title}')"</#if><#if f.validateAsTitle && f.validTitle?? && f.validTitle?trim!=''><#if canUse("title",f.formItemProperties!'')> :title="$t('${f.title}Valid')"</#if></#if><#if canUse("prop",f.formItemProperties!'')> prop="${f.entityName}"</#if><#if canUse("size",f.formItemProperties!'')> :size="$store.state.app.size"</#if> >
      <#assign HD=HD+'  '/>
      <#if (f.icon?? && f.icon?trim!='') || f.showType=='date' || f.showType=='datetime'|| f.showType=='time'>
      ${HD}<#nt><<#if f.href?? && f.href?trim!=''>a href="${f.href}" target="blank"<#else>span</#if> slot="label"><cg-icon icon="<#if f.icon?? && f.icon?trim!=''>${f.icon}<#elseif f.showType=='date'>fa fa-calendar<#elseif f.showType=='time'>fa fa-clock-o<#else>fa fa-calendar-plus-o</#if>" :size="14" :width="20"/>${getTextFrom('label',f.formItemProperties!'',"{{$t('"+f.title+"')}}")}</<#if f.href?? && f.href?trim!=''>a<#else>span</#if>>
      </#if>
      <#if f.showType == 'textarea'>
      ${HD}<#nt><el-input v-model="record.${f.entityName}" name="${f.entityName}" <#if joinClickField!=''>@click.native = "${joinClickField}JoinVisible=true"</#if>
      <#if !isEmpty(f.itemProperties!'')>
      ${HD}<#nt>          ${f.itemProperties?trim}
      </#if>
      ${HD}<#nt>          <@WF f 'type' "textarea" /><#if f.length?? && f.length gt 0><@WF f ':maxlength' f.length?c /><@WF f 'show-word-limit' 'true' /></#if>
      ${HD}<#nt>          <@WF f ':label' "$t('"+f.title+"')" /><@WF f ":placeholder" "$t('system.message."+(f.mustInput || !f.isNull)?string("needValue","unknown")+"')"/><#if f.isNull && !f.mustInput><#if f.isNull && !f.mustInput><@WF f "clearable"/></#if></#if><#if f.faIcon?? && f.faIcon?trim!=''><@WF f "prefix-icon" f.faIcon /></#if>
      ${HD}<#nt>          <@READONLY f /><@WF f "resize" /><@WF f ":autosize" "{ minRows: 1, maxRows: 10 }" /><@WF f "autofocus" /><@WF f "validate-event" />/>
      <#elseif f.showType == 'number'>
      <#assign MIN = '*', MAX = '*', USEINPUTNUMBER = true />
      ${HD}<#nt><cg-number-input v-model="record.${f.entityName}" name="${f.entityName}" <#if joinClickField!=''>@click.native = "${joinClickField}JoinVisible=true"</#if>
      <#if !isEmpty(f.itemProperties!'')>
      ${HD}<#nt>                 ${f.itemProperties?trim}
      </#if>
      ${HD}<#nt>                 <@READONLY f />
      ${HD}<#nt>                 <@WF f ":label" "$t('"+f.title+"')"/><@WF f ":placeholder" "$t('system.message."+(f.mustInput || !f.isNull)?string("needValue","unknown")+"')"/><#if f.isNull && !f.mustInput><@WF f "clearable"/></#if><#if f.numericScale??><@WF f ":precision" ""+f.numericScale/></#if><#if f.faIcon?? && f.faIcon?trim!=''></#if>
      ${HD}<#nt>                 <#if f.validExpression?? && f.validExpression?trim!="" && f.validExpression?contains(",")><#list f.validExpression?trim?split(",") as d><#if d_index==0 && d?trim!=""><#assign MIN = d /><@WF f ":min" ""+d/><#elseif d_index==1  && d?trim!=""><#assign MAX = d /><@WF f ":max" ""+d/><#elseif d_index==2  && d?trim!=""><@WF f ":step" ""+d/></#if></#list></#if><#if MIN!='*' || MAX!='*'><@WF f ":title" "$t('system.message.valueRange') + ': "+MIN+" - "+MAX+"'" /></#if>/>
      <#elseif f.showType == 'date' || f.showType == 'datetime' || f.showType == 'time'>
      ${HD}<#nt><cg-date-picker v-model="record.${f.entityName}" name="${f.entityName}" <#if joinClickField!=''>@pickerClick = "${joinClickField}JoinVisible=true"</#if>
      <#if !isEmpty(f.itemProperties!'')>
      ${HD}<#nt>                ${f.itemProperties?trim}
      </#if>
      ${HD}<#nt>                <@WF f ":align" "mobile?'right':'center'"/><@WF f "type" "${f.showType}"/><@WF f ":title" "$t('"+f.title+"')"/><@READONLY f />
      ${HD}<#nt>                <#if f.formatter?? && f.formatter?trim!=""><@WF f "format" f.formatter/></#if><#if f.isNull && !f.mustInput><@WF f "clearable"/></#if>/>
      <#elseif f.showType == 'color'>
      ${HD}<#nt><el-color-picker v-model="record.${f.entityName}" name="${f.entityName}"
      <#if !isEmpty(f.itemProperties!'')>
      ${HD}<#nt>                 ${f.itemProperties?trim}
      </#if>
      ${HD}<#nt>                 <@WF f ":disabled" "isDetail"/><#if f.formatter?? && f.formatter?trim!=""><@WF f "color-format" f.formatter/><#else><@WF f "color-format" "rgb"/></#if><@WF f "show-alpha"/>/>
      <#elseif f.showType=="select" && f.dictParent?? && f.dictParent?trim!="">
      ${HD}<#nt><cg-cascader v-model="record.${f.entityName}" name="${f.entityName}"
      <#if !isEmpty(f.itemProperties!'')>
      ${HD}<#nt>             ${f.itemProperties?trim}
      </#if>
      ${HD}<#nt>             <@READONLY f /><@WF f ":filterable" "false"/><#if f.dictMultiple?? && f.dictMultiple><@WF f "multiple"/><@WF f "collapse-tags"/><#elseif f.type!='String'><@WF f "numberic"/></#if><#if f.mustInput><@WF f "required"/><#else><#if f.isNull && !f.mustInput><@WF f "clearable"/></#if></#if>
      ${HD}<#nt>             <@WF f ":dictionary" dictionary(f)/><#if f.dictFullName?? && f.dictFullName><@WF f "show-all-levels"/></#if><@WF f ":placeholder" "$t('system.message."+(f.mustInput || !f.isNull)?string("needValue","unknown")+"')"/>/>
      <#elseif f.showType=="select">
      ${HD}<#nt><cg-select v-model="record.${f.entityName}" name="${f.entityName}"
      <#if !isEmpty(f.itemProperties!'')>
      ${HD}<#nt>           ${f.itemProperties?trim}
      </#if>
      ${HD}<#nt>           <@WF f ":dictionary" dictionary(f)/><@READONLY f /><@WF f ":filterable" "false"/><@WF f ":allow-create" "false"/><#if f.dictMultiple?? && f.dictMultiple><@WF f "multiple"/><#elseif f.type!='String'><@WF f "numberic"/></#if><@WF f ":placeholder" "$t('system.message."+(f.mustInput || !f.isNull)?string("needValue","unknown")+"')"/><#if f.isNull && !f.mustInput><@WF f "clearable"/></#if>/>
      <#elseif f.showType == 'html'>
      ${HD}<#nt><div name="${f.entityName}"<#if !isEmpty(f.itemProperties!'')> ${f.itemProperties?trim}</#if>>
      ${HD}<#nt>  <#if f.validTitle?? && f.validTitle?trim!=""><div v-html="$t('${f.title}Valid')"></div></#if>
      ${HD}<#nt>  <div v-html="record.${f.entityName}"></div>
      ${HD}<#nt></div>
      <#elseif f.showType == 'image'>
      <#assign imageWidth='' />
      ${HD}<#nt><cg-image v-model="blobRecord.${f.entityName}" name="${f.entityName}" <@WF f ":title" "$t('"+f.title+"')"/> <@READONLY f /><#if !isEmpty(f.itemProperties!'')> ${f.itemProperties?trim}</#if> <@WF f ":id" "record."+pk.entityName/><@WF f "generatorName" generatorName?uncap_first/><@WF f "field" f.entityName/><#if f.formatter?? && f.formatter?trim!=""><#list f.formatter?trim?split(",") as d><#if d_index==0 && d?trim!=""><#assign imageWidth=""+d/><@WF f ":maxWidth" ""+d/><#if !d_has_next><@WF f ":maxHeight" ""+d/></#if><#elseif d_index==1  && d?trim!=""><@WF f ":maxHeight" ""+d/><#elseif d_index==1  && d?trim=="" && imageWidth!=''><@WF f ":maxHeight" imageWidth/><#elseif d_index==2  && d?trim!="" && d?trim!="0"><@WF f "alignCenter"/></#if></#list></#if>
      ${HD}<#nt>          <@WF f "@input" "recordChanged=true"/>/>
      <#elseif f.showType == '2dcode'>
      ${HD}<#nt><cg-zxing v-model="record.${f.entityName}" name="${f.entityName}"<#if !isEmpty(f.itemProperties!'')> ${f.itemProperties?trim}</#if> <@WF f ":size" isEmpty(f.formatter!'')?string("128",f.formatter) /><#if f.faIcon??><@WF f "icon" f.faIcon/></#if>/>
      <#elseif f.showType == 'file'>
      ${HD}<#nt><cg-file v-model="blobRecord.${f.entityName}" name="${f.entityName}"<#if !isEmpty(f.itemProperties!'')> ${f.itemProperties?trim}</#if> <@WF f ":id" "record."+pk.entityName/><@WF f ":baseUrl" "baseUrl"/><@WF f "field" f.entityName/><#if f.formatter?? && f.formatter?trim!=''>
      ${HD}<#nt>         <@WF f "accept" f.formatter/></#if><#if f.dictMultiple><@WF f "multiple" /></#if><@READONLY f />
      ${HD}<#nt>         <@WF f "@input" "recordChanged=true"/> />
      <#elseif f.showType == 'boolean'>
      ${HD}<#nt><el-switch v-model="record.${f.entityName}" name="${f.entityName}"<#if !isEmpty(f.itemProperties!'')> ${f.itemProperties?trim}</#if> <@WF f ":active-text" "mobile?'':$t('"+f.title+"')"/><@WF f "inactive-text" ""/><@WF f ":disabled" "isDetail"/> />
      <#elseif f.showType == 'false_if_null'>
      ${HD}<#nt><el-switch name="${f.entityName}"<#if !isEmpty(f.itemProperties!'')> ${f.itemProperties?trim}</#if> <@WF f ":value" "record."+f.entityName+"?true:false"/><@WF f ":active-text" "mobile?'':$t('"+f.title+"')"/><@WF f "inactive-text" ""/><@WF f "disabled"/>/>
      <#elseif f.showType == 'checkbox'>
      ${HD}<#nt><cg-checkbox v-model="record.${f.entityName}" name="${f.entityName}"<#if !isEmpty(f.itemProperties!'')> ${f.itemProperties?trim}</#if> <@WF f ":dictionary" dictionary(f)/><@READONLY f /> />
      <#elseif f.showType == 'radio'>
      ${HD}<#nt><cg-radio v-model="record.${f.entityName}" name="${f.entityName}"<#if !isEmpty(f.itemProperties!'')> ${f.itemProperties?trim}</#if> <@WF f ":dictionary" dictionary(f)/><@READONLY f /><#if f.type!='String'><@WF f "numberic"/></#if> />
      <#else>
      ${HD}<#nt><el-input v-model="record.${f.entityName}" name="${f.entityName}" <#if joinClickField!=''>@click.native = "${joinClickField}JoinVisible=true"</#if>
      <#if !isEmpty(f.itemProperties!'')>
      ${HD}<#nt>          ${f.itemProperties?trim}
      </#if>
      ${HD}<#nt>          <#if f.formatter?? && (f.formatter=='mobile' || f.formatter=='fixed')><@WF f "type" "tel"/><#else><@WF f "type" "${f.showType}"/></#if>
      ${HD}<#nt>          <@WF f ":label" "$t('"+f.title+"')"/><@WF f ":placeholder" "$t('system.message."+(f.mustInput || !f.isNull)?string("needValue","unknown")+"')"/><#if f.faIcon?? && f.faIcon?trim!=''><@WF f "prefix-icon" f.faIcon/></#if>
      ${HD}<#nt>          <#if f.showType == 'password'><@WF f "show-password"/></#if><@WF f "resize"/><@WF f "autofocus"/><@WF f "validate-event"/>
      ${HD}<#nt>          <#if fastMultiJoinField?? && fastMultiJoinField!=''> clearable @clear="clearJoinValues(myself,'${fastMultiJoinField}Join')"<#else><@READONLY f /><#if f.length?? && f.length gt 0><@WF f ":maxlength" f.length?c+""/><@WF f "show-word-limit"/></#if><#if f.isNull && !f.mustInput><@WF f "clearable"/></#if></#if><#if !f.slotTemplates?? || f.slotTemplates?trim==''>/</#if>>
<#if f.slotTemplates?? && f.slotTemplates?trim!=''>
${f.slotTemplates}
      ${HD}<#nt></el-input>
</#if>
      </#if>
      <#assign HD=HD?substring(2) />${HD}<#nt></el-form-item>
</#macro>
<#if buttons??>
  <#list buttons as btn>
    <#if SYSACTIONS?index_of(btn.action) lt 0>
      <#assign USERACTION = true />
      <#break>
    </#if>
  </#list>
</#if>
<#assign USERFILE = false, hasDynamicQuery = false , groupPaneTitle = false />
<#list fields as f>
  <#if (f.showType=='file' || f.showType=='image') && !f.hidden>
    <#assign USERFILE = true />
  </#if>
  <#if f.showType=='select' && f.dictTable?? && f.dynaCondition?? && f.dynaCondition?trim!=''>
    <#assign hasDynamicQuery = true >
  </#if>
</#list>
<#assign USETAB = false />
<#list fields as f>
<#if !f.hidden && f.groupTitle?? && f.groupTitle?trim!=''>
  <#assign USETAB = true />
</#if>
</#list>
<template>
  <div :class="mobile || height==0 ? 'cg-form':'cg-form just-v-scroll'" :style="mobile || height==0?'':'height:'+height+'px'">
    <el-form ref="cgForm" v-loading="recordLoading" :model="record" v-cg-form-adjust
             <#if FP.formProperties?? && FP.formProperties?trim!=''>  label-width="80px"
             ${FP.formProperties?trim}
             </#if>
             <#nt><@WForm FP ":class" "className"/><@WForm FP ":rules" "rules"/>
             <#nt><@WForm FP ":label-position" "labelPosition"/><@WForm FP ":label-width" "labelWidth"/><@WForm FP ":size" "$store.state.app.size"/>
             <#nt><@WForm FP "hide-required-asterisk"/><#if USERFILE><@WForm FP "enctype" "multipart/form-data"/></#if>>
      <#assign FIRSTTABPANE = true, HD = '' />
      <#if USETAB>
      <el-tabs ref="tabs" v-model="tabSelected" @tab-click="autoFocus"><#assign HD='  '/>
      </#if>
      <#assign inRow = false, newLine = true, colUsed = 0, colWidth = 0, INDEX = 1 />
      <#list fields as f>
      <#assign popoverinner = false, isJoinFieldNow = false />
      <#if !f.hidden>
      <#if f.entityName=='flowNote'>
        <#assign colWidth = 24 , newLine = true />
      <#else>
        <#if f.width gt 0 && f.width lt 24>
        <#assign colWidth = f.width />
        <#else>
        <#assign colWidth = 24 />
        </#if>
        <#if (f.groupTitle?? && f.groupTitle?trim!='') || (colWidth + colUsed) gt 24 || colUsed == 0>
        <#assign newLine = true />
        <#else>
        <#assign newLine = false />
        </#if>
      </#if>
      <#if colUsed gt 0 && inRow>
        <#if newLine>
      <#assign HD=HD?substring(2)/>${HD}<#nt></el-col>
      <#assign HD=HD?substring(2)/>${HD}<#nt></el-row>
          <#assign colUsed = 0 , inRow = false />
        <#else>
      <#assign HD=HD?substring(2)/>${HD}<#nt></el-col>
        </#if>
      </#if>
      <#if f.groupTitle?? && f.groupTitle?trim!=''>
        <#if FIRSTTABPANE>
          <#assign FIRSTTABPANE = false />
        <#else>
      <#assign HD=HD?substring(2)/>${HD}<#nt></el-tab-pane><#assign AUTOFOCUS=true />
        </#if>
      ${HD}<#nt><el-tab-pane :label="groupPaneTitle('${f.groupTitle?trim}')">
        <#assign HD=HD+'  ', groupPaneTitle = true,  tabPaneIndex = tabPaneIndex + 1/>
      <#if f.entityName=='flowSelection' && FP.isFlow>
      ${HD}<el-collapse accordion>
        ${HD}<el-collapse-item :title="$t('system.action.flow')">
          ${HD}<CgFlow :url="baseUrl+'/list'" :queryById.sync="record.${pk.entityName}" />
        ${HD}</el-collapse-item>
      ${HD}</el-collapse>
      </#if>
      </#if>
      <#if newLine && colWidth lt 24>
      ${HD}<#nt><el-row :gutter="mobile?0:20">
        <#assign HD=HD+'  '/>
      ${HD}<#nt><el-col :span="${colWidth}">
        <#assign HD=HD+'  '/>
        <#assign colUsed = colWidth , inRow = true/>
        <#elseif inRow && colUsed gt 0>
      ${HD}<#nt><el-col :span="${colWidth}">
        <#assign HD=HD+'  '/>
        <#assign colUsed = colUsed + colWidth , inRow = true />
      </#if>
      <#if f.id?starts_with("join:") || f.id?starts_with("list:")>
      <#assign joinFields = f.id?split(":")/>
      <#if joinedFileds?index_of(','+joinFields[1]+',') lt 0>
      <#assign joinedFileds = joinedFileds + ','+ joinFields[1] +',' />
      ${HD}<#nt><cg-join v-model="${joinFields[1]}JoinVisible" <@READONLY f />>
      <#if f.id?starts_with("join:") && f.dictMultiple?? && f.dictMultiple>
      <#assign fastMultiJoinField = joinFields[1] />
      </#if>
      ${HD}<#nt>  <${f.tableId} slot="popover" ref="${joinFields[1]}Join" openID="${joinFields[1]?lower_case}-join" :height="500" :joinShow="${joinFields[1]}JoinVisible" :joinMultiple="<#if f.id?starts_with("join:") && (!f.dictMultiple?? || !f.dictMultiple)>false<#else>true</#if>"<#if f.dynaCondition?? && f.dynaCondition?trim!=''> :fixedQueryRecord="${joinFields[1]}DynaCondition"</#if>
      ${HD}<#nt>  <@SPACE f.tableId?length />  :originSelections="record.${joinFields[1]}" selectionKey="${joinFields[2]}" joinMode @closeJoinList="(rows)=>{ getJoinFields('${joinFields[1]}',rows)}"
      ${HD}<#nt>  <@SPACE f.tableId?length />  @showJoinList="${joinFields[1]}JoinVisible=true" />
      <#assign popoverinner = true, HD=HD+'  ', isJoinFieldNow = true  />
      </#if>
      <#else>
      <#assign popoverinner = false />
      </#if>
      <@elFormItem f />
      <#assign INDEX = INDEX + 1 />
      <#if popoverinner>
      <#assign HD=HD?substring(2) />${HD}<#nt></cg-join>
      </#if>
      </#if>
    </#list>
      <#if colUsed gt 0 && inRow>
      <#assign HD=HD?substring(2) />${HD}<#nt></el-col>
      <#assign HD=HD?substring(2) />${HD}<#nt></el-row>
      </#if>
    <#if USETAB>
    <#if !FIRSTTABPANE>
      <#assign HD=HD?substring(2) />${HD}<#nt></el-tab-pane><#assign AUTOFOCUS=true />
    </#if>
      </el-tabs>
    </#if>
    </el-form>
    <#assign USEBTN = false />
    <div v-if="!isDetail" class="cg-form-buttons">
      <el-divider />
      <#assign USEBTN = false />
      <#if buttons?? && FP.actionList?? && FP.actionList?trim!=''>
        <#list buttons as btn>
          <#if btn.displayProperties?? && btn.displayProperties?index_of("ed") gte 0 && FP.actionList?index_of(','+btn.action+',') gte 0>
            <#assign USEBTN = true />
          </#if>
        </#list>
      </#if>
      <#if USEBTN>
      <el-button-group v-for = "(btn,index) in addtionalActions" :key="btn.action" style="margin-right:5px">
        <el-button v-if="hasAuthorityOf(myself,baseUrl,btn,record)" :class="'cg-button'+<#if FP.buttonClass??>' ${FP.buttonClass}'<#else>''</#if>+(btn.appendClass?' '+btn.appendClass:'')"
                   type="default"<#if FP.buttonStyle?? && FP.buttonStyle?trim!=''> style="${FP.buttonStyle}"</#if> plain :icon="btn.icon" :disabled="hasOwnProperty('disabledAction') ? disabledAction(btn) : false"
                   @click.native="doAction(btn.action)" >
        {{ $t('${generatorName}.action.'+btn.action) }}
        </el-button>
      </el-button-group>
      </#if>
      <#assign INDEX = INDEX+10 />
      <#if FP.continueAdd && pk??>
      <el-button v-if="!mobile && hasAuthorityOf(myself,baseUrl,'add',record)" class="cg-button<#if FP.buttonClass??> ${FP.buttonClass}</#if>" type="default" plain icon="fa fa-plus"<#if FP.buttonStyle?? && FP.buttonStyle?trim!=''> style="${FP.buttonStyle}"</#if>
                 @click.native="doAction('add')">
        {{ $t('system.action.new') }}
      </el-button>
      <#assign INDEX = INDEX+1 />
      </#if>
      <#if FP.isFlow>
      <el-button v-if="hasAuthority(baseUrl+'/f_'+flowAction)" v-show="tabSelected=='${tabPaneIndex}'" class="cg-button<#if FP.buttonClass??> ${FP.buttonClass}</#if>" type="default" plain :disabled="!recordChanged"
                 icon="el-icon-check"<#if FP.buttonStyle?? && FP.buttonStyle?trim!=''> style="${FP.buttonStyle}"</#if> @click.native="submit()">
        {{ $t('system.action.ok') }}
      </el-button>
      <#else>
      <el-button v-if="hasAuthorityOf(myself,baseUrl,'edit',record)" class="cg-button<#if FP.buttonClass??> ${FP.buttonClass}</#if>" type="primary" plain :disabled="!recordChanged"
                 icon="el-icon-check"<#if FP.buttonStyle?? && FP.buttonStyle?trim!=''> style="${FP.buttonStyle}"</#if> @click.native="submit()">
        {{ $t('system.action.<#if table.name?? && table.name?trim!=''>save<#else>ok</#if>') }}
      </el-button>
      </#if>
      <#assign INDEX = INDEX+1 />
      <el-button v-if="!mobile && showInDialog" class="cg-button<#if FP.buttonClass??> ${FP.buttonClass}</#if>" plain<#if FP.buttonStyle?? && FP.buttonStyle?trim!=''> style="${FP.buttonStyle}"</#if> icon="el-icon-close"
                 @click.native="$emit('closeDialog')">
        {{ $t('system.action.close') }}
      </el-button>
    </div>
  </div>
</template>
<script>
import cg from '@/utils/cg'
import cgForm from '@/utils/cgForm'
<#if joinOnFields?? && joinOnFields?size gt 0>
<#list joinOnFields as c>
<#if c.component?? && c.component?trim!=''>
import ${c.component} from '@/views${c.vue}.vue'
</#if>
</#list>
</#if>
import rulesObject from './rules.js'
const mixins = []
const mixinContext = require.context('.', false, /CgForm${FP.name?cap_first}-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
<#assign needServer = false />
export default {
  name: 'CgForm${FP.name?cap_first}',
  mixins,
  props: {
    <#if FP.isDialog>
    dialogParams: {
      type: Object,
      default: null
    },
    </#if>
    showInDialog: {
      type: Boolean,
      default: false
    },
    height: {
      type: Number,
      default: 0
    },
    queryById: [Number, String]
  },
  <#assign firstItem = true />
  <#if joinOnFields?? && joinOnFields?size gt 0>
  components: { <#list joinOnFields as c><#if c.component?? && c.component?trim!=''><#if firstItem><#assign firstItem=false /><#else>, </#if>${c.component}</#if></#list> },
  </#if>
  data() {
    return {
      myself: this,
      path: <#if FP.isFlow>this.openParams().flowAction ? this.openParams().flowAction : '${FP.path?split(",")[0]}'<#else>'${FP.path}'</#if>,
      <#if FP.isFlow>
      flowAction: this.openParams().flowAction ? this.openParams().flowAction : '${FP.path?split(",")[0]}',
      </#if>
      title: this.$t('${generatorName}.title.'+this.path),
      rules: {},
      idField: <#if pk??>'${pk.entityName}'<#else>null</#if>,
      <#if fastMultiJoinField??>
      fastMultiJoinField: '${fastMultiJoinField}',
      </#if>
      <#if pk??>
      ${pk.entityName}Saved: this.openParams().record && typeof this.openParams().record === 'object' ? this.openParams().record.${pk.entityName} : null,
      onChange: typeof this.openParams().onChange === 'function' ? this.openParams().onChange : null,
      </#if>
      recordChanged: false,
      recordLoading: false,
      fixedFields: typeof this.openParams().fixedFields === 'object' ? this.openParams().fixedFields : {},
      openMode: this.openParams().openMode ? this.openParams().openMode : null,
      record: <#if dynaList??>Object.assign({
          <#list dynaList as d>
          ${d.watchField}: null<#if d?has_next>,</#if>
          </#list>
        }, </#if>this.openParams().record && typeof this.openParams().record === 'object' ? this.openParams().record : {}<#if dynaList??>)</#if>,
      <#if USEFILE>
      blobRecord:
      {
      <#list fields as f>
      <#if !f.hidden && (f.showType=='file' || f.showType=='image')>
        ${f.entityName}: {
          <#if f.showType=='file' && f.dictMultiple?? && f.dictMultiple>
          multiple: true,
          name: this.openParams().record && typeof this.openParams().record === 'object' && this.openParams().record.${f.entityName} ? this.openParams().record.${f.entityName}.split(',') : [],
          blob: []
          <#else>
          name: this.openParams().record && typeof this.openParams().record === 'object' ? this.openParams().record.${f.entityName} : null,
          blob: null
          </#if>
        },
      </#if>
      </#list>
      },
      </#if>
      <#list fields as f>
      <#if flowList ?? && f.name?? && "flow_state"==f.name>
      <#elseif (f.showType=="radio" || f.showType=="checkbox" || f.showType=="select")>
      </#if>
      <#if f.defaultValue?? && f.defaultValue?trim!="" && ((f.defaultValue?trim?length gt 4 && f.defaultValue?trim?substring(0,4)=="sql:") || (f.defaultValue?trim?length gt 2 && f.defaultValue?trim?substring(0,2)=="f:"))>
      <#assign needServer = true />
      </#if>
      </#list>
      needDefaultFromServer: <#if needServer || FP.isFlow || (FP.actionList?? && FP.actionList?index_of(",flow,") gte 0) >true<#else>false</#if>,
      <#if DICTLIST?size gt 0>
      dictionary: {
      <#list DICTLIST as f>
        ${f}<#if f?has_next>,</#if>
      </#list>
      },
      <#if NEEDLOADFROMSERVER>
      needLoadDictionary: true,
      </#if>
      </#if>
      <#assign useJoinFields = false />
      <#if joinOnFields?? && joinOnFields?size gt 0 >
      <#list joinOnFields as f>
      ${f.entityName}JoinVisible: false,
      <#assign useJoinFields = true />
      </#list>
      </#if>
      <#if USETAB>
      tabSelected: '0',
      </#if>
      generatorName: '${generatorName}',
      baseUrl: '/${moduleName}/<#if subModule??>${subModule}/</#if>${generatorName}'
    }
  },
  computed: {
    mobile() {
      return this.$store.state.app.device === 'mobile'
    },
    hasMenu() {
      return false
    },
    className() {
      return (this.mobile?'cg-form-cell ':'')+'cg-no-border cg-form-${FP.name?lower_case}'
    },
    labelWidth() {
      return this.className.indexOf('cg-form-cell')>=0? '100px' : undefined
    },
    labelPosition() {
      return this.className.indexOf('cg-form-cell')>=0? 'left':'${FP.labelPosition}'
    },
    <#if joinOnFields?? && joinOnFields?size gt 0 >
    <#list joinOnFields as f>
    <#if f.dynaCondition?? && f.dynaCondition?trim!=''>
    ${f.entityName}DynaCondition() {
      return {
        <#list f.dynaCondition?split(',') as c>
        <#assign cc=c?split('=') />
        <#if cc[0]?trim!=''>
        ${cc[0]}: this.record.<#if cc?size gt 1>${cc[1]}<#else>${cc[0]}</#if><#if c?has_next>,</#if>
        </#if>
        </#list>
      }
    },
    </#if>
    </#list>
    </#if>
    <#if USEBTN>
    addtionalActions() {
      return [
        <#list buttons as btn>
        <#if btn.displayProperties?? && btn.displayProperties?index_of("ed") gte 0 && FP.actionList?index_of(","+btn.action+",") gte 0 >
        {
          action: '${btn.action}',
          icon: '${btn.icon}',
          title: '${generatorName}.action.${btn.action}',
          groupid: 10,
          confirm: <#if btn.confirmText?? && btn.confirmText?trim!=''>'${generatorName}.action.${btn.action}Confirm'<#else>''</#if>,
          rowProperty: '${btn.rowProperty}',
          displayProperties: '${btn.displayProperties}',
          actionProperty: '${btn.actionProperty}',
          appendClass: '${btn.appendClass!""}',
          needRefresh: <#if btn.isRefreshList>true<#else>false</#if>
        }<#if btn?has_next>,</#if>
        </#if>
        </#list>
      ]
    },
    </#if>
    isDetail() {
    <#if table.actionList ?? && (table.actionList?index_of(",edit,") gte 0 || table.actionList?index_of(",view,") gte 0) >
      return this.openMode === 'view'
      <#else>
      return false
      </#if>
    },
    isNew() {
      <#if table.actionList?? && table.actionList?index_of(",add,") gte 0 >
      return !this.openMode || this.openMode === 'add'
      <#else>
      return false
      </#if>
    },
    isEdit() {
      <#if table.actionList ?? && table.actionList?index_of(",edit,") gte 0>
      return this.openMode === 'edit'
      <#else>
      return false
      </#if>
    }
  },
  watch: {
    record: {
      handler() {
        this.recordChanged = true
        <#if USEINPUTNUMBER>
        this.just4elInputNumberNullBug()
        </#if>
      },
      deep: true
    },
    <#if dynaList??>
    <#list dynaList as d>
    'record.${d.watchField}': {
      handler(n,o)
      {
        cgForm.form_getDynaDict(this, '${d.dynaFields}')
      }
    },
    </#list>
    </#if>
    queryById: {
      handler(n, o) {
        if (n) this.doAction('refresh', {id: n})
      },
      immediate: true
    }
  },
  created() {
    this.rules = rulesObject.getRules(this)
    <#if FP.isFlow>
    this.record.flowSelection = 'approve'
    </#if>
    <#if DICTLIST?size gt 0 >
    cgForm.form_getQueryDictionary(this)
    </#if>
    if (this.queryById) {
      cgForm.form_getRecordFromServer(this,this.queryById)
      this.queryRefreshId = this.queryById
    } else if (this.isNew) cgForm.form_createNewRecord(this)
    else if ((this.isEdit || this.isDetail) && this.openParams().id && typeof this.openParams().record !== 'object') {
      cgForm.form_getRecordFromServer(this,this.openParams().id)
      this.queryRefreshId = this.openParams().id
    }
    <#if DICTLIST?size gt 0 >
    else if (this.needLoadDictionary) cgForm.form_getDictionary(this)
    </#if>
    <#if dynaList??>
    <#if FP.isFlow><#assign dynaFields = 'flowSelection,flowNextOperator,flowCopyToList' /><#else><#assign dynaFields = '' /></#if>
    <#list dynaList as d>
    <#if dynaFields==''><#assign dynaFields = d.dynaFields /><#else><#list d.dynaFields?split(",") as df><#if dynaFields?index_of(df) lt 0><#assign dynaFields = dynaFields + ',' + df /></#if></#list></#if>
    </#list>
    else cgForm.form_getDynaDict(this, '${dynaFields}')
    </#if>
    <#if USEINPUTNUMBER>
    this.just4elInputNumberNullBug()
    </#if>
  },
  <#if FP.isDialog>mounted<#else>activated</#if>() {
    <#if !FP.isDialog>
    cgForm.form_activedRefresh(this)
    </#if>
    cgForm.form_mounted(this)
  },
  methods: {
    openParams: function() {
      return <#if FP.isDialog>this.dialogParams ? this.dialogParams : </#if>this.$route.query
    },
    <#if USEINPUTNUMBER>
    just4elInputNumberNullBug: function() {
      <#list fields as f>
      <#if !f.hidden && f.showType == 'number'>
      if (this.record.${f.entityName} === null) this.record.${f.entityName} = undefined
      </#if>
      </#list>
    },
    </#if>
    <#if table.actionList?? && table.actionList?index_of(",add,") gte 0 >
    newRecord: function() {
        return {
            <#list fields as f>
            <#if f.defaultValue?? && f.defaultValue?trim!="" >
            <#if f.defaultValue?trim?length gt 3 && f.defaultValue?trim?substring(0,3)=="js:" >
            ${f.entityName}: ${f.defaultValue?trim?substring(3)},
            <#elseif (f.defaultValue?trim?length gt 4 && f.defaultValue?trim?substring(0,4)=="sql:") || (f.defaultValue?trim?length gt 2 && f.defaultValue?trim?substring(0,2)=="f:")  >
            <#continue>
            <#elseif f.type=="Integer" || f.type=="Short" || f.type=="Long">
            ${f.entityName}: <#if f.defaultValue=="">0<#else>${f.defaultValue}</#if>,
            <#elseif f.type=="Boolean">
            ${f.entityName}: <#if f.defaultValue?trim == '1' || f.defaultValue?trim?lower_case == 'true'>true<#else>false</#if>,
            <#elseif f.type=="Date" && f.defaultValue?trim?lower_case=="now" >
            ${f.entityName}: new Date(),
            <#else>
            ${f.entityName}: '${f.defaultValue}',
            </#if>
            <#elseif !f.isNull && (!pk?? || f.entityName != pk.entityName)>
            <#if f.type=="Integer" || f.type=="Short" || f.type=="Long">
            ${f.entityName}: 0,
            <#elseif f.type=="Boolean">
            ${f.entityName}: false,
            <#elseif f.type=="Date" >
            ${f.entityName}: new Date(),
            <#else>
            ${f.entityName}: '',
            </#if>
            </#if>
            </#list>
        }
    },
    </#if>
    <#if groupPaneTitle>
    groupPaneTitle: function(defTitle) {
      return this.$t(defTitle)
    },
    </#if>
    submit: function() {
      if (this.recordChanged) cgForm.form_submit(this, <#if FP.isFlow>'f_'+this.flowAction<#else>'save'</#if><#if !FP.continueAdd && FP.isDialog>,true</#if>)
    },
    <#if joinOnFields?? && joinOnFields?size gt 0 >
    getJoinFields(field,rows) {
      const joinDefine = {
        <#list joinOnFields as f>
        ${f.entityName}: {
          valueField: '${f.dictField}',
          fields: '${f.dictText}'
        },
        </#list>
      }
      this[field+'JoinVisible'] = false
      this.setJoinValues(this.record, field, joinDefine[field], rows)
    },
    </#if>
    doAction(action, options) {
      cgForm.form_doAction(this, action, <#if USEBTN>Object.assign(options ? options : {}, this.addtionalActions.find(e => e.action === action))<#else>options</#if>)
    },
    ...cg
  }
}
</script>
