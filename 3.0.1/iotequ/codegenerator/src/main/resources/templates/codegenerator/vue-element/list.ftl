<#assign D = "$" />
<#assign J = "#" />
<#assign USE_CG_INPUT = false />
<#function canUse p pp>
  <#if pp?? && pp?trim!='' && (pp?trim?contains(p) || (p?starts_with(':') && pp?contains(p?substring(1))))>
    <#return false />
  <#else>
    <#return true />
  </#if>
</#function>
<#function dictionary f>
<#if f.dictTable?? && f.dictTable?trim?starts_with("dict:")>
  <#return "dictionary.dict"+f.dictTable?trim?substring(5)?cap_first>
<#else>
  <#return "dictionary.dict"+f.entityName?cap_first>
</#if>
</#function>
<#function getDbName entity>
  <#list fields as f>
    <#if f.entityName == entity>
      <#return f.name>
    </#if>
  </#list>
  <#return ''>
</#function>
<#function isEmpty v>
  <#return v?trim=='' />
</#function>
<#macro WT f key value="true"><#if canUse(key,f.tableProperties!'')>${key}<#if value!='true'>="${value}"</#if> </#if></#macro>
<#macro WL f key value="true"><#if canUse(key,f.columnProperties!'')>${key}<#if value!='true'>="${value}"</#if> </#if></#macro>
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
      <#assign dict_text_list = ''/>
      <#list f.dictField?split(',') as df>
        <#assign dict_text_list = dict_text_list + generatorName?uncap_first + '.field.' + f.entityName + '_' + df_index />
        <#if df_has_next><#assign dict_text_list = dict_text_list + ','/></#if>
      </#list>
      <#assign DICTLIST = DICTLIST + ["dict"+f.entityName?cap_first+": this.getDictionary('"+f.dictField?trim+"','"+dict_text_list+"')"] />
    <#elseif (!f.dictTable?? || f.dictTable?trim=="") && f.dictField?? && f.dictField?trim!="">
      <#assign DICTLIST = DICTLIST + ["dict"+f.entityName?cap_first+": this.getDictionary('"+f.dictField?trim+"')"] />
    </#if>
  </#if>
</#list>
<#assign additinalAction = false />
<#assign hasQueryField = false />
<#assign joinedFileds = '', joinClickField='' />
<#list fields as f>
<#if f.queryMode gt 0>
<#assign hasQueryField = true >
</#if>
</#list>
<#if buttons??>
  <#list buttons as btn>
    <#if btn.displayProperties?? && btn.displayProperties?index_of("tb") gte 0 && LP.actionList?? && LP.actionList?index_of(","+btn.action+",") gte 0>
      <#assign additinalAction = true />
    </#if>
  </#list>
</#if>
<#assign useSubModule = (table.subPackage?? && table.subPackage?trim!='') />
<#assign editInlineFields = "" />
<#macro fieldShow f editinline n=0>
<#if f.cellDisplaySlot?? && f.cellDisplaySlot?trim!=''>
<template slot-scope="scope">
  ${f.cellDisplaySlot}
</template>
<#elseif f.showType == 'number' && f.numericScale?? && f.numericScale gt 0>
<template slot-scope="scope">
<#if editinline>
<#list 1..n as i> </#list>  <cg-number-input v-if="scope.row.inlineEditting" v-model="scope.row.${f.entityName}" <#if f.validExpression?? && f.validExpression?trim!="" && f.validExpression?contains(",")><#list f.validExpression?trim?split(",") as d><#if d_index==0 && d?trim!=""><#assign MIN = d /><@WF f ":min" ""+d/><#elseif d_index==1  && d?trim!=""><#assign MAX = d /><@WF f ":max" ""+d/><#elseif d_index==2  && d?trim!=""><@WF f ":step" ""+d/></#if></#list></#if><#if MIN!='*' || MAX!='*'><@WF f ":title" "$t('system.message.valueRange') + ': "+MIN+" - "+MAX+"'" /></#if>/>
<#assign editInlineFields = editInlineFields+((editInlineFields=='')?string('',', '))+"\'${f.entityName}\'" />
</#if>
<#list 1..n as i> </#list>  <#if editinline><span v-else></#if>{{ printf(scope.row.${f.entityName},<#if f.formatter?? && f.formatter?trim!="">'${f.formatter}'<#elseif f.numericScale?? && f.numericScale gt 0>'%.${f.numericScale}d'<#else>'%d'</#if>) }}<#if editinline></span></#if>
<#list 1..n as i> </#list></template>
<#elseif f.showType == 'date' || f.showType == 'datetime' || f.showType == 'time'>
<template slot-scope="scope">
<#if editinline>
<#list 1..n as i> </#list>  <cg-date-picker v-if="scope.row.inlineEditting" v-model="scope.row.${f.entityName}" :align="mobile?'right':'center'" type="${f.showType}" :title="$t('${f.title}')" <#if f.formatter?? && f.formatter?trim!="">format="${f.formatter}"</#if><#if f.isNull> clearable</#if>/>
<#assign editInlineFields = editInlineFields+((editInlineFields=='')?string('',', '))+"\'${f.entityName}\'" />
</#if>
<#list 1..n as i> </#list>  <#if editinline><span v-else></#if>{{ time2String(scope.row.${f.entityName},<#if f.formatter?? && f.formatter?trim!="">'${f.formatter}'<#elseif f.showType=='date'>'YYYY-MM-DD'<#elseif f.showType=='datetime'>'YYYY-MM-DD HH:mm'<#elseif f.showType=='time'>'HH:mm'</#if>) }}<#if editinline></span></#if>
<#list 1..n as i> </#list></template>
<#elseif f.showType == 'color'>
<template slot-scope="scope" :style="'background-color:'+scope.row.${f.entityName}">
<#list 1..n as i> </#list>  {{ scope.row.${f.entityName} }}
<#list 1..n as i> </#list></template>
<#elseif f.showType=="select" && f.dictParent?? && f.dictParent?trim!="">
<template slot-scope="scope">
<#if editinline && (!f.dynaCondition?? || f.dynaCondition?trim=='')>
<#list 1..n as i> </#list>  <cg-cascader v-if="scope.row.inlineEditting" v-model="scope.row.${f.entityName}" :dictionary="${dictionary(f)}"<#if f.dictMultiple?? && f.dictMultiple> multiple<#elseif f.type!='String'> numberic</#if><#if f.isNull> clearable</#if><#if f.dictFullName?? && f.dictFullName> show-all-levels</#if> />
<#assign editInlineFields = editInlineFields+((editInlineFields=='')?string('',', '))+"\'${f.entityName}\'" />
</#if>
<#list 1..n as i> </#list>  <#if editinline  && (!f.dynaCondition?? || f.dynaCondition?trim=='')><span v-else></#if>{{ dictValue(scope.row.${f.entityName},${dictionary(f)},<#if f.dictFullName?? && f.dictFullName>true<#else>false</#if>,true) }}<#if editinline  && (!f.dynaCondition?? || f.dynaCondition?trim=='')></span></#if>
<#list 1..n as i> </#list></template>
<#elseif f.showType=="select">
<template slot-scope="scope">
<#if editinline && (!f.dynaCondition?? || f.dynaCondition?trim=='')>
<#list 1..n as i> </#list>  <cg-select v-if="scope.row.inlineEditting" v-model="scope.row.${f.entityName}" automaticDropdown appendToBody :dictionary="${dictionary(f)}" allow-create<#if f.dictMultiple?? && f.dictMultiple> multiple<#elseif f.type!='String'> numberic</#if> />
<#assign editInlineFields = editInlineFields+((editInlineFields=='')?string('',', '))+"\'${f.entityName}\'" />
</#if>
<#list 1..n as i> </#list>  <#if editinline  && (!f.dynaCondition?? || f.dynaCondition?trim=='')><span v-else></#if>{{ dictValue(scope.row.${f.entityName},${dictionary(f)},<#if f.dictFullName?? && f.dictFullName>true<#else>false</#if>,true) }}<#if editinline  && (!f.dynaCondition?? || f.dynaCondition?trim=='')></span></#if>
<#list 1..n as i> </#list></template>
<#elseif f.showType=="checkbox" || f.showType=="radio">
<template slot-scope="scope">
<#if editinline>
<#list 1..n as i> </#list>  <cg-select v-if="scope.row.inlineEditting" v-model="scope.row.${f.entityName}" automaticDropdown appendToBody :dictionary="${dictionary(f)}"<#if f.showType=="checkbox"> multiple</#if> />
<#assign editInlineFields = editInlineFields+((editInlineFields=='')?string('',', '))+"\'${f.entityName}\'" />
</#if>
<#list 1..n as i> </#list>  <#if editinline><span v-else></#if>{{ dictValue(scope.row.${f.entityName},${dictionary(f)},<#if f.dictFullName?? && f.dictFullName>true<#else>false</#if>,true) }}<#if editinline></span></#if>
<#list 1..n as i> </#list></template>
<#elseif f.showType == '2dcode'>
<template slot-scope="scope">
<#list 1..n as i> </#list>  <el-avatar shape="square" :size="50" fit="scale-down"  :src="apiUrl('/res/get2dCode?text='+scope.row.${f.entityName}+'<#if f.formatter??>&size=${f.formatter}<#else>&size=128</#if>'+'<#if f.faIcon??>&icon=${f.faIcon}</#if>')" />
<#list 1..n as i> </#list></template>
<#elseif f.showType == 'boolean'>
<template slot-scope="scope">
<#if editinline>
<#list 1..n as i> </#list>  <el-switch v-if="scope.row.inlineEditting" v-model="scope.row.${f.entityName}" />
<#assign editInlineFields = editInlineFields+((editInlineFields=='')?string('',', '))+"\'${f.entityName}\'" />
</#if>
<#list 1..n as i> </#list>  <cg-icon<#if editinline> v-else</#if> :color="scope.row.${f.entityName}?'#46a6ff':'grey'" :icon="scope.row.${f.entityName}?'fa fa-check-circle':'fa fa-circle-o'"/>
<#list 1..n as i> </#list></template>
<#elseif f.showType == 'false_if_null'>
<template slot-scope="scope">
<#list 1..n as i> </#list>  <cg-icon color="#46a6ff" :icon="scope.row.${f.entityName}?'fa fa-check-circle':'fa fa-circle-o'"/>
<#list 1..n as i> </#list></template>
<#elseif f.showType == 'image'>
<template slot-scope="scope">
<#list 1..n as i> </#list>  <el-avatar v-if="scope.row.${f.entityName}" shape="square" :size="50" fit="scale-down" :src="apiUrl('/res/getUploadImage?name='+scope.row.${f.entityName}+(scope.row.${f.entityName}.indexOf('/')>=0 || scope.row.${f.entityName}.indexOf('\\')>=0 ? '' : '&id='+scope.row.${pk.entityName}+'&path=${generatorName?uncap_first}&field=${f.entityName}'))"/>
<#list 1..n as i> </#list></template>
<#elseif f.showType == 'file'>
<template slot-scope="scope">
<#list 1..n as i> </#list>  {{ scope.row.${f.entityName} }}
<#list 1..n as i> </#list></template>
<#else>
<template slot-scope="scope">
<#if editinline>
<#list 1..n as i> </#list>  <#if f.showType == 'mltext'><cg-input<#else><el-input</#if> v-if="scope.row.inlineEditting" v-model="scope.row.${f.entityName}" type="text"<#if f.showType == 'password'> show-password</#if> />
<#assign editInlineFields = editInlineFields+((editInlineFields=='')?string('',', '))+"\'${f.entityName}\'" />
</#if>
<#list 1..n as i> </#list>  <#if editinline><span v-else></#if>{{ <#if f.showType == 'mltext'><#assign USE_CG_INPUT = true />localeText(</#if><#if f.formatter?? && f.formatter?trim?starts_with('js:')>${f.formatter?trim?substring(3)?replace('{field}', 'scope.row.'+f.entityName)?replace('{row}','scope.row')}<#else>scope.row.${f.entityName}</#if><#if f.showType == 'mltext'>)</#if> }}<#if editinline></span></#if>
<#list 1..n as i> </#list></template>
</#if>
</#macro>

<template>
  <div :class="joinMode?'cg-join-list':'cg-list'">
    <el-backtop v-if="isTableMode()" ref="cgListBacktop" class="el-backtop" target=".cg-list-${LP.name?lower_case} .el-table__body-wrapper" :visibility-height="200">
      <i class="el-icon-caret-top" />
    </el-backtop>
    <el-table ref="cgList" v-if="isTableMode()" v-loading="listLoading" :data="rows" :class="className"<#if pk??> row-key="${pk.entityName}"</#if> <@WT LP ":row-class-name" "rowClassName"/>
			  <#if !isEmpty(LP.tableProperties!'')>
			  ${LP.tableProperties}
			  </#if>
              <#nt><@WT LP "style" "width: 100%"/><@WT LP ":height" "tableHeight()"/><#if LP.maxHeight?? && LP.maxHeight gt 0><@WT LP ":max-height" ""+LP.maxHeight/></#if><@WT LP ":size" "$store.state.app.size"/><#if !LP.showHeader><@WT LP ":show-header" "false"/></#if>
              <#nt><#if LP.editInline><@WT LP "v-set-input:no-tab-index" "{tabIndex: -1}"/><@WT LP "v-table-enter-tab" /></#if><#if LP.stripe><@WT LP "stripe"/></#if><#if LP.border><@WT LP ":border" "!mobile"/></#if><#if LP.highlightCurrentRow><@WT LP "highlight-current-row"/></#if><#if LP.showSummary?? && LP.showSummary><@WT LP "show-summary"/></#if><@WT LP "fit"/><#if LP.spanEntities?? && LP.spanEntities?trim!=''><@WT LP ":span-method" "groupFields"/></#if>
              <#nt><@WT LP "@row-click" "(row, column, event)=>cgList.list_rowClick(myself,{ row, column, event })"/>
              <#nt><@WT LP "@row-contextmenu" "(row, column, event)=>cgList.list_rowContextmenu(myself,{ row, column, event })"/>
              <#nt><@WT LP "@header-click" "(column, event)=>cgList.list_headClick(myself,{ column, event })"/>
              <#nt><@WT LP "@row-dblclick" "(row, column, event)=>cgList.list_rowDblclick(myself,{ row, column, event })"/>
              <#nt><@WT LP "@cell-click" "(row, column, cell, event)=>cgList.list_cellClick(myself,{ row, column, cell, event })"/>
              <#nt><@WT LP "@selection-change" "(selection)=>cgList.list_selectionChange(myself, selection)"/>
              <#nt><@WT LP "@current-change" "(selection)=>cgList.list_selectionChange(myself, selection)"/>
    >
      <cg-icon slot="empty" icon="el-icon-minus" color="grey" />
      <#assign hasExpandField = false />
      <#list fields as f>
      <#if !f.hidden && f.expand?? && f.expand>
        <#assign hasExpandField = true />
        <#break>
      </#if>
      </#list>
      <#if hasExpandField>
      <el-table-column type="expand">
        <template slot-scope="props">
          <el-form label-position="left" inline class="demo-table-expand">
            <#list fields as f>
            <#if !f.hidden && f.expand?? && f.expand>
            <el-form-item :label="$t('${f.title}')">
              <#nt><@fieldShow f, LP.editInline?? && LP.editInline && f.editInline, 14/>
            </el-form-item>
            </#if>
            </#list>
          </el-form>
        </template>
      </el-table-column>
      </#if>
      <el-table-column v-if="!mobile" type="index" width="50" align="center" class-name="drag-filter" label-class-name="pointer-cursor" header-align="center">
        <i slot="header" class="el-icon-menu"/>
      </el-table-column>
      <el-table-column v-if="multiple" type="selection" align="center"<#if pk??> reserve-selection</#if> class-name="drag-filter<#if LP.editInline?? && LP.editInline > no-tab-index</#if>" width="36" />
      <#assign TREESHOWENTITY = '~0~' />
      <#if LP.parentEntity?? && LP.parentEntity?trim!='' && LP.treeShowEntity?? && LP.treeShowEntity?trim!=''>
      <#list fields as f>
      <#if f.entityName == LP.treeShowEntity?trim >
      <#nt><<#if f.width?? && f.width gt 0>el<#else>cg</#if>-table-column prop="${f.entityName}"<#if f.showType=='date' || f.showType=='datetime'|| f.showType=='time'> type="${f.showType}"<#elseif f.showType=='select' || f.showType=="checkbox" || f.showType=="radio"> type="dict"</#if> <#if !f.width?? || f.width == 0 >:page="<#if LP.pagination>paginationCurrentPage<#else>1</#if>" </#if><#if !isEmpty(f.columnProperties!'')>${f.columnProperties} </#if><#if f.width?? && f.width gt 0><@WL f "width" ""+f.width?c/></#if><@WL f ":label" "$t('"+f.title+"')"/><#if f.fix?? && f.fix><@WL f "fixed"/></#if><#if (f.queryMode?? && f.queryMode gt 0) || (LP.sortField?? && f.entityName == LP.sortField)><@WL f "sortable"/></#if><@WL f "align" "left"/><#if f.headerAlign?? && f.headerAlign?trim!=''><@WL f "header-align" f.headerAlign/></#if><#if f.overflowTooltip></#if>>
        <#nt><@fieldShow f,LP.editInline?? && LP.editInline && f.editInline,8/>
      </<#if f.width?? && f.width gt 0>el<#else>cg</#if>-table-column>
      <#assign TREESHOWENTITY = f.entityName />
      <#break>
      </#if>
      </#list>
      </#if>
      <#list fields as f>
      <#if !f.hidden && f.entityName !=TREESHOWENTITY>
      <#nt><<#if f.width?? && f.width gt 0>el<#else>cg</#if>-table-column prop="${f.entityName}"<#if f.showType=='date' || f.showType=='datetime'|| f.showType=='time'> type="${f.showType}"<#elseif f.showType=='select' || f.showType=="checkbox" || f.showType=="radio"> type="dict"</#if> <#if !f.width?? || f.width == 0>:page="<#if LP.pagination>paginationCurrentPage<#else>1</#if>" </#if><#if !isEmpty(f.columnProperties!'')>${f.columnProperties} </#if><#if f.width?? && f.width gt 0><@WL f "width" f.width?c+""/></#if><@WL f ":label" "$t('"+f.title+"')"/><#if f.fix?? && f.fix><@WL f "fixed"/></#if><#if (f.queryMode?? && f.queryMode gt 0) || (LP.sortField?? && f.entityName == LP.sortField)><@WL f "sortable"/></#if><@WL f "align" f.align/><#if f.headerAlign?? && f.headerAlign?trim!=''><@WL f "header-align" f.headerAlign/></#if><#if f.overflowTooltip></#if>>
        <#nt><@fieldShow f,LP.editInline?? && LP.editInline && f.editInline,8/>
      </<#if f.width?? && f.width gt 0>el<#else>cg</#if>-table-column>
      </#if>
      </#list>
      <cg-action v-model="showActionView" mode="${LP.toolbarMode}" :url="baseUrl" :actions="cgList.list_allActions(myself,'main')" @actionClick="doAction" />
    </el-table>
    <cg-card-list v-else ref="cgList" v-loading="listLoading" :render="rowRender" :data="rows" :mainClass="className" row-key="${pk.entityName}" :multiple="false" <@WT LP ":height" "tableHeight()"/>
                  :isLoading="listLoading" <#if LP.spanEntities?? && LP.spanEntities?trim!=''>:groupBy="rowRenderGroupTitle?rowRenderGroupTitle:'${LP.spanEntities?trim}'"</#if>
                  :isUnMore="<#if LP.pagination>paginationCurrentPage * paginationPageSize >= paginationTotalRecords<#else>true</#if>" :cgList="myself"
                <#if !isEmpty(LP.tableProperties!'')>
                  ${LP.tableProperties}
                </#if>
                  @doAction="(a,row)=>doAction(a,{row})"
                  @loadMore="cgList.list_loadMore(myself)"
                  @refresh="doAction('refresh',{ isPullDownEvent : true})"
    <#nt>              <@WT LP "@row-click" "(row, event)=>cgList.list_rowClick(myself,{ row, event })"/>
    <#nt>              <@WT LP "@row-contextmenu" "(row, event)=>cgList.list_rowContextmenu(myself,{ row, event })"/>
    <#nt>              <@WT LP "@row-dblclick" "(row, event)=>cgList.list_rowDblclick(myself,{ row, event })"/>
    <#nt>              <@WT LP "@selection-change" "(selection)=>cgList.list_selectionChange(myself, selection)"/>
    <#nt>              <@WT LP "@current-change" "(selection)=>cgList.list_selectionChange(myself, selection)"/>
    >
      <template slot="append">
	      <cg-action v-model="showActionView" mode="${LP.toolbarMode}" :url="baseUrl" :actions="cgList.list_allActions(myself,'main')" @actionClick="doAction" />
      </template>
    </cg-card-list>
    <#if LP.pagination>
    <nut-scroller v-if="mobile && isTableMode()"
                  wrapperElement=".cg-list-${LP.name?lower_case} .el-table__body-wrapper"
                  :isLoading="listLoading"
                  :isUnMore="paginationCurrentPage * paginationPageSize >= paginationTotalRecords"
                  type="vertical"
                  :pulldownTxt="$t('system.message.pullDownRefresh')"
                  :loadMoreTxt="$t('system.message.pullUpLoad')"
                  :unloadMoreTxt="$t('system.message.noMoreData')"
                  @loadMore="cgList.list_loadMore(myself)"
                  @pulldown="doAction('refresh',{ isPullDownEvent : true})"
    />
    <el-pagination v-if="!mobile" @size-change="doAction('refresh')" @current-change="doAction('refresh')" :page-sizes="[10, 20, 30, 50, 100, 200]" layout="total, sizes, prev, pager, next, jumper"
      :current-page.sync="paginationCurrentPage" :page-size.sync="paginationPageSize" :total="paginationTotalRecords">
    </el-pagination>
    </#if>
    <cg-context-menu :show="contextMenu.visible" :actions="contextMenu.actions"
                     :top="contextMenu.top" :left="contextMenu.left" <#if flowList??>:flowActions="contextMenu.flowActions"</#if>
                     @hide="contextMenu.visible = false" @select="(a)=>doAction(a, {row: contextMenu.row, trElement: contextMenu.trElement})"
    />
    <#assign hasFilterQuery = false />
    <#if hasQueryField>
    <#assign INDEX = 1 />
    <cg-query-condition v-model="showQuery" ref="query" :modal="!joinMode" :queryRecord="queryRecord"
                        @refresh="doAction('refresh')" @reset="queryRecord=initialQueryRecord()">
      <#if LP.showSearch>
      <el-form-item :label="$t('system.action.fuzzyQuery')" prop="search" :size="$store.state.app.size">
        <el-input v-model="queryRecord.search" type="text" name="search" clearable resize autofocus
                  prefix-icon="el-icon-search" :placeholder="$t('system.message.fuzzyQueryTip')" @keyup.enter.native="doAction('refresh')" />
      </el-form-item>
      <el-form-item v-show="queryRecord.search" :label="$t('system.action.field')" prop="searchFields" :size="$store.state.app.size">
        <cg-select v-model="queryRecord.searchFields" dictionary="<#list fields as f><#if f.queryMode?? && f.queryMode gt 0>${f.entityName}|${f.title},</#if></#list>" multiple/>
      </el-form-item>
      <el-divider />
      </#if>
      <div<#if LP.showSearch> v-show="!queryRecord.search"</#if>>
        <#list fields as f>
        <#if f.queryMode?? && f.queryMode gt 0>
        <#assign INDEX = INDEX + 1 />
        <#if (f.id?starts_with("join:") || f.id?starts_with("list:")) && f.queryMode != 2>
          <#assign joinFields = f.id?split(":")/>
        <#if joinedFileds?index_of(','+joinFields[1]+',') lt 0>
        <#assign joinedFileds = joinedFileds + ','+ joinFields[1] +',' />
        <cg-join v-model="${joinFields[1]}JoinVisible">
          <${f.tableId} slot="popover" ref="${joinFields[1]}Join" openID="${joinFields[1]?lower_case}-join" :height="joinHeight()" :joinShow="${joinFields[1]}JoinVisible" joinMultiple<#if f.dynaCondition?? && f.dynaCondition?trim!=''> :fixedQueryRecord="${joinFields[1]}DynaCondition"</#if>
            :originSelections="queryRecord.${joinFields[1]}" selectionKey="${joinFields[2]}" joinMode @closeJoinList="(rows)=>{ getJoinFields('${joinFields[1]}',rows)}" @showJoinList="${joinFields[1]}JoinVisible=true"/>
        <#assign popoverinner = true />
        </#if>
        <#else>
          <#assign popoverinner = false />
        </#if>
        <el-form-item <#if popoverinner>slot="reference" </#if>:label="$t('${f.title}')" prop="${f.entityName}" :size="$store.state.app.size">
          <#if f.queryMode == 2>
          <#assign hasFilterQuery = true />
          <cg-select v-model="queryRecord.${f.entityName}" :dictionary="filterQueryDictionary.${f.entityName}"
                     :disabled="fixedQueryRecord.${f.entityName}?true:false" allow-create multiple clearable />
          <#elseif f.queryMode == 3 && f.showType == 'number'>
          <el-row>
            <el-col :span="12"><cg-number-input v-model="queryRecord.${f.entityName}_start" resize autofocus :readonly="fixedQueryRecord.${f.entityName}?true:false" :label="$t('${f.title}')" clearable<#if f.numericScale??> :precision="${f.numericScale}"</#if>/></el-col>
          <#assign INDEX = INDEX + 1 />
            <el-col :span="12"><cg-number-input v-model="queryRecord.${f.entityName}_end" resize autofocus :readonly="fixedQueryRecord.${f.entityName}?true:false" label="" clearable<#if f.numericScale??> :precision="${f.numericScale}"</#if>/></el-col>
          </el-row>
          <#elseif f.showType == 'textarea'>
          <el-input v-model="queryRecord.${f.entityName}" type="textarea" name="${f.entityName}" :label="$t('${f.title}')" clearable<#if f.faIcon?? && f.faIcon?trim!=''> prefix-icon="${f.faIcon}"</#if>
                    :readonly="fixedQueryRecord.${f.entityName}?true:false" resize :autosize="{ minRows: 1, maxRows: 10 }" autofocus/>
            <#if f.faIcon?? && f.faIcon?trim!=''><cg-icon slot="suffix" icon="${f.faIcon}"></cg-icon></#if>
          <#elseif f.showType == 'number'>
          <cg-number-input v-model="queryRecord.${f.entityName}" name="${f.entityName}" resize autofocus<#if f.faIcon?? && f.faIcon?trim!=''> prefix-icon="${f.faIcon}"</#if> :readonly="fixedQueryRecord.${f.entityName}?true:false" :label="$t('${f.title}')" clearable<#if f.numericScale??> :precision="${f.numericScale}"</#if><#if f.validExpression?? && f.validExpression?trim!="" && f.validExpression?contains(",")><#list f.validExpression?trim?split(",") as d><#if d_index==0 && d?trim!=""> :min="${d}"<#elseif d_index==1  && d?trim!=""> :max="${d}"<#elseif d_index==2  && d?trim!=""> :step="${d}"</#if></#list></#if>/>
          <#elseif f.showType == 'date' || f.showType == 'datetime' || f.showType == 'time'>
          <cg-date-picker v-model="queryRecord.${f.entityName}" :title="$t('${f.title}')" name="${f.entityName}" :align="mobile?'right':'center'" type="${f.showType}range"<#if f.showType != 'time'> :picker-options="datePickerOptions()"</#if>
                          :readonly="fixedQueryRecord.${f.entityName}?true:false" <#if f.formatter?? && f.formatter?trim!="">format="${f.formatter}"</#if> clearable />
          <#elseif f.showType=="select" && f.dictParent?? && f.dictParent?trim!="">
          <cg-cascader v-model="queryRecord.${f.entityName}" name="${f.entityName}"  multiple collapse-tags clearable
                       :disabled="fixedQueryRecord.${f.entityName}?true:false" :dictionary="${dictionary(f)}"<#if f.dictFullName?? && f.dictFullName> show-all-levels</#if>/>
          <#elseif f.showType=="select" || f.showType == 'checkbox' || f.showType == 'radio'>
          <cg-select v-model="queryRecord.${f.entityName}" :dictionary="${dictionary(f)}"
                     :disabled="fixedQueryRecord.${f.entityName}?true:false"  :allow-create="!mobile" multiple clearable />
          <#elseif f.showType == 'boolean'>
          <el-checkbox-group v-model="queryRecord.${f.entityName}" :max="1">
            <el-checkbox name="${f.entityName}" :label="true" :disabled="fixedQueryRecord.${f.entityName}?true:false">{{ $t('system.action.yes') }}</el-checkbox>
            <el-checkbox name="${f.entityName}" :label="false" :disabled="fixedQueryRecord.${f.entityName}?true:false">{{ $t('system.action.no') }}</el-checkbox>
          </el-checkbox-group>
          <#elseif f.showType == 'false_if_null'>
            <el-checkbox-group v-model="queryRecord.${f.entityName}" :max="1">
              <el-checkbox name="${f.entityName}" label="is not null" :disabled="fixedQueryRecord.${f.entityName}?true:false">{{ $t('system.action.yes') }}</el-checkbox>
              <el-checkbox name="${f.entityName}" label="is null" :disabled="fixedQueryRecord.${f.entityName}?true:false">{{ $t('system.action.no') }}</el-checkbox>
            </el-checkbox-group>
          <#else>
          <#if f.showType == 'mltext'><cg-input<#else><el-input</#if> v-model="queryRecord.${f.entityName}" type="text" name="${f.entityName}"<#if f.faIcon?? && f.faIcon?trim!=''> prefix-icon="${f.faIcon}"</#if>
                    :readonly="fixedQueryRecord.${f.entityName}?true:false" :label="$t('${f.title}')" clearable resize autofocus<#if popoverinner> @clear="clearJoinValues(myself,'${joinFields[1]}Join')"</#if>/>
          </#if>
        </el-form-item>
          <#if popoverinner>
        </cg-join>
          </#if>
        </#if>
        </#list>
      </div>
    </cg-query-condition>
    </#if>
  </div>
</template>

<script>
<#if USE_CG_INPUT>
import {localeText} from '@/lang'
</#if>
import {hasAuthority} from '@/utils/cg'
<#if joinOnFields?? && joinOnFields?size gt 0>
<#list joinOnFields as c>
<#if c.component?? && c.component?trim!=''>
import ${c.component} from '@/views${c.vue}.vue'
</#if>
</#list>
</#if>
<#if LP.editInline?? && LP.editInline>
import rulesObject from './rules.js'
</#if>
import ParentTable from '@/views/common-views/components/table'
const Comp = {
  name: 'CgList${LP.name?cap_first}',
  mixins: [ParentTable],
  props: {
    selectionKey: {
      type: String,
      default: '${pk.entityName}'
    }
  },
  <#assign firstItem = true />
  <#if joinOnFields?? && joinOnFields?size gt 0>
  components: { <#list joinOnFields as c><#if c.component?? && c.component?trim!=''><#if firstItem><#assign firstItem=false /><#else>, </#if>${c.component}</#if></#list> },
  </#if>
  data() {
    return {
      <#if LP.editInline?? && LP.editInline>
      rulesObject,
      </#if>
      path: '${LP.path}',
      <#assign firstField = true />
      defaultOrder: <#if LP.orderBy?? && LP.orderBy?trim !="">'${LP.orderBy?trim}'<#elseif LP.sortField?? && LP.sortField?trim!=''>'${getDbName(LP.sortField)} asc'<#elseif createTimeField=="">'${pk.name} desc'<#else>'${createTimeField} desc'</#if>,
      queryRecordFields: [<#assign firstItemInArray=true /><#list fields as f><#if f.queryMode?? && f.queryMode gt 0><#if firstItemInArray><#assign firstItemInArray=false /><#else>,</#if>'<#if (f.id?starts_with("join:") || f.id?starts_with("list:")) && f.queryMode != 2><#assign joinFields = f.id?split(":") />${joinFields[1]}<#else>${f.entityName}</#if>'</#if></#list>],
      formPath: '/${moduleName}/<#if subModule??>${subModule}/</#if>${generatorName}/record',
      <#if LP.actionList ?? && LP.actionList?index_of(",export,") gte 0 && LP.localExport ?? && LP.localExport>
      localExport: true,
      </#if>
      <#if rightJoinPK && LP.actionList ?? && LP.actionList?index_of(",delete,") gte 0>
      removeLeftRecordOnRightJoin: true,
      </#if>
      <#if pk?? && LP.parentEntity?? && LP.parentEntity?trim!=''>
      parentField: '${LP.parentEntity?trim}',
      </#if>
      <#if pk??>
      idField: '${pk.entityName}',
      </#if>
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
      <#if flowList ??>
      flowActionList: [
        <#list flowList as flow>
        <#assign icons = flow.icon?split(",") />
        <#list flow.path?split(",") as path>
        {
          action: '${path}',
          prefix: 'flow.',
          title: this.$t('${generatorName}.title.${path}'),
          icon: <#if path_index lt icons?size>'${icons[path_index]}'<#else>'${icons[0]}'</#if>,
          rowProperty: 'sr',
          actionProperty: 'aj',
          groupid: 20
        }<#if flow?has_next || path?has_next>,</#if>
        </#list>
        </#list>
      ],
      </#if>
      <#if hasFilterQuery>
      filterQueryDictionary: {
        <#list fields as f>
        <#if f.queryMode==2>
        ${f.entityName}: [],
        </#if>
        </#list>
      },
      </#if>
      <#assign useJoinFields = false />
      <#if joinOnFields?? && joinOnFields?size gt 0 >
      <#list joinOnFields as f>
      ${f.entityName}JoinVisible: false,
      <#assign useJoinFields = true />
      </#list>
      </#if>
      <#if LP.pagination>
      paginationCurrentPage: 1,
      paginationPageSize: this.$store.state.app.device === 'mobile' ? 10 : 30,
      paginationTotalRecords: 0,
      </#if>
      <#if editInlineFields!=''>
      totalEdittingRows: 0,
      editInlineFields: hasAuthority('/${moduleName}/<#if subModule??>${subModule}/</#if>${generatorName}/updateSelective')?[${editInlineFields}]:null,
      </#if>
      <#if sons?? || (hasSonTables?? && hasSonTables)>
      hasSonTables: true,
      </#if>
      <#if LP.spanEntities?? && LP.spanEntities?trim!=''>
      groupByEntityFields: '${LP.spanEntities?trim}',
      </#if>
      listName: '${LP.name}',
      <#if LP.multiple>
      multipleSelection: true,
      </#if>
      <#if LP.tableHeight?? && LP.tableHeight gt 0>
      defaultTableHeight: ${LP.tableHeight},
      </#if>
      generatorName: '${generatorName}',
      baseUrl: '/${moduleName}/<#if subModule??>${subModule}/</#if>${generatorName}'
    }
  },
  computed: {
    <#if joinOnFields?? && joinOnFields?size gt 0 >
    <#list joinOnFields as f>
    <#if f.dynaCondition?? && f.dynaCondition?trim!=''>
    ${f.entityName}DynaCondition() {
      return {
      <#list f.dynaCondition?split(',') as c>
      <#assign cc=c?split('=') />
      <#if cc[0]?trim!=''>
        ${cc[0]}: this.queryRecord.<#if cc?size gt 1>${cc[1]}<#else>${cc[0]}</#if><#if c?has_next>,</#if>
      </#if>
      </#list>
      }
    },
    </#if>
    </#list>
    </#if>
    <#if additinalAction>
    additionalActions() {
      if (this.joinMode) return []
      else return [
        <#list buttons as btn>
        <#if btn.displayProperties?? && btn.displayProperties?index_of("tb") gte 0 && LP.actionList?? && LP.actionList?index_of(","+btn.action+",") gte 0 >
        {
          action: '${btn.action}',
          icon: '${btn.icon!}',
          title: '${generatorName}.action.${btn.action}',
          groupid: 10,
          confirm: <#if btn.confirmText?? && btn.confirmText?trim!=''>'${generatorName}.action.${btn.action}Confirm'<#else>''</#if>,
          rowProperty: '${btn.rowProperty}',
          actionProperty: '${btn.actionProperty}',
          displayProperties: '${btn.displayProperties}',
          appendClass: '${btn.appendClass!}',
          needRefresh: <#if btn.isRefreshList>true<#else>false</#if>
        }<#if btn?has_next>,</#if>
        </#if>
        </#list>
      ]
    },
    </#if>
    allActions() {
      if (this.joinMode) return 'refresh<#if hasQueryField>,query</#if>'
      else return '<#if hasQueryField>query,</#if><#if LP.actionList?? && LP.localExport?? && LP.localExport>${LP.actionList?replace("export","localExport")}<#elseif LP.actionList??>${LP.actionList}</#if>'
    }
  },
  mounted() {
    this.cgList.list_tableInit(this<#if LP.sortField?? && LP.sortField?trim!=''>,'${LP.sortField?trim}'</#if>)
  },
  methods: {
    <#if hasQueryField>
    initialQueryRecord() {
      return Object.assign({
        <#list fields as f>
        <#if f.queryMode gt 0 && f.defaultQueryValue?? && f.defaultQueryValue?trim!=''>
        ${f.entityName}: ${f.defaultQueryValue},
        <#elseif f.queryMode gt 0 && f.showType == 'boolean' || f.showType == 'false_if_null'>
        ${f.entityName}: [],
        <#elseif f.showType != 'number'>
        ${f.entityName}: null,
        </#if>
        </#list>
      }, this.fixedQueryRecord)
    },
    </#if>
    <#if LP.spanEntities?? && LP.spanEntities?trim!=''>
    groupFields({ row, column, rowIndex, columnIndex }) {
      return this.cgList.list_groupFields(this, this.groupByEntityFields.split(','), row, column, rowIndex, columnIndex)
    },
    </#if>
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
      this.setJoinValues(this.queryRecord, field, joinDefine[field], rows)
    },
    </#if>
    <#if LP.actionList ?? && LP.actionList?index_of(",editInline_add,") gte 0 && editInlineFields!=''>
    newRecordForEditInline() {
      return {
        <#list fields as f>
        <#if f.defaultValue?? && f.defaultValue?trim!="" >
        <#if f.defaultValue?trim?length gt 3 && f.defaultValue?trim?substring(0,3)=="js:" >
        ${f.entityName}: ${f.defaultValue?trim?substring(3)},
        <#elseif (f.defaultValue?trim?length gt 4 && f.defaultValue?trim?substring(0,4)=="sql:") || (f.defaultValue?trim?length gt 2 && f.defaultValue?trim?substring(0,2)=="f:")  >
        <#continue>
        <#elseif f.type=="Integer" || f.type=="Short" || f.type=="Long" || f.type=="Byte" || f.type=='BigDecimal' >
        ${f.entityName}: <#if f.defaultValue=="">0<#else>${f.defaultValue}</#if>,
        <#elseif f.type=="Boolean">
        ${f.entityName}: <#if f.defaultValue?trim == '1' || f.defaultValue?trim?lower_case == 'true'>true<#else>false</#if>,
        <#elseif f.type=="Date" && f.defaultValue?trim?lower_case=="now" >
        ${f.entityName}: new Date(),
        <#else>
        ${f.entityName}: '${f.defaultValue}',
        </#if>
        <#elseif !f.isNull && (!pk?? || f.entityName != pk.entityName)>
        <#if f.type=="Integer" || f.type=="Short" || f.type=="Long" || f.type=="Byte" || f.type=='BigDecimal'>
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
    }
    </#if>
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /CgList${LP.name?cap_first}-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
  mixins
}
</script>
