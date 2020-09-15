<#function canUse p pp>
  <#if pp?? && pp?trim!='' && (pp?trim?contains(p) || (p?starts_with(':') && pp?contains(p?substring(1))))>
    <#return false />
  <#else>
    <#return true />
  </#if>
</#function>
<#function isEmpty v>
  <#return v?trim=='' />
</#function>
<#macro SPACE n><#list 1..n as i> </#list></#macro>
<template>
  <div class="cg-form"<#if FP.isDialog> :class="dialogClass"</#if>>
    <#if FP.isDialog>
    <el-dialog ref="dialog" v-el-drag-dialog :visible.sync="showDialog" top="0px" :class="dialogClass" :close-on-click-modal="false"
               :append-to-body="true" :show-close="!mobile || isDetail" :before-close="beforeClose" @closed="close">
    <#else>
    <el-card shadow="hover">
    </#if>
      <div slot="<#if FP.isDialog>title<#else>header</#if>" :class="titleColor">
        <cg-header <#if !FP.isDialog>hoemMenu </#if>:icon="icon" :content="content" :title="title"
                   :hasMenu="mobile && !isDetail" rightIcon = 'el-icon-check color-primary'
                   @menuAction="submit" @goBack="<#if FP.isDialog>close<#else>goBack</#if>"
        />
      </div>
      <#if FP.images?? && FP.images?trim!=''>
      <#if FP.images?index_of(",") gte 0>
      <el-carousel :interval="3000" type="card" height="auto">
        <el-carousel-item v-for="item in '${FP.images}'.split(',')" :key="item">
          <img class="img-center" :src="'/static/img/'+item" alt="" />
        </el-carousel-item>
      </el-carousel>
      <#else>
      <img class="img-center" src="/static/img/${FP.images}" alt="" />
      </#if>
      </#if>
      <CgForm${FP.name?cap_first} ref="cgForm"<#if !isEmpty(FP.viewProperties!'')> ${FP.viewProperties?trim}</#if>
       <#if FP.isDialog>
         <#if canUse('dialogParams',FP.viewProperties!'')>
              <#nt><@SPACE FP.name?length/>:dialogParams="dialogParams"
         </#if>
         <#if canUse('showInDialog',FP.viewProperties!'')>
              <#nt><@SPACE FP.name?length/>:showInDialog="showDialog"
         </#if>
         <#if canUse('closeDialog',FP.viewProperties!'')>
              <#nt><@SPACE FP.name?length/>@closeDialog="handleClose"
         </#if>
       </#if>
         <#if canUse('openModeChanged',FP.viewProperties!'')>
              <#nt><@SPACE FP.name?length/>@openModeChanged="openModeChanged" />
         </#if>
    <#if FP.isDialog>
    </el-dialog>
    <#else>
    </el-card>
    </#if>
  </div>
</template>

<script>
import ParentForm from '@/views/common-views/components/<#if FP.isDialog>dialog<#else>record</#if>'
import CgForm${FP.name?cap_first} from './CgForm${FP.name?cap_first}'
const mixins = [ParentForm]
const mixinContext = require.context('.', false, /${FP.path?split(",")[0]}-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default {
  name: '${FP.name?cap_first}Form',
  components: { CgForm${FP.name?cap_first} },
  mixins,
  data() {
    return {
      <#if !table.actionList ?? || (table.actionList?index_of(",edit,") lt 0 && table.actionList?index_of(",view,") lt 0) >
      allowViewRecord: false,
      </#if>
      <#if !table.actionList ?? || table.actionList?index_of(",edit,") lt 0  >
      allowEditRecord: false,
      </#if>
      <#if !table.actionList ?? || (table.actionList?index_of(",add,") lt 0 && table.actionList?index_of(",new,") lt 0) >
      allowAddRecord: false,
      </#if>
      <#if FP.isFlow>
      isFlowRecord: true,
      </#if>
      <#if FP.icon?? && FP.icon?trim!=''>
      <#if FP.isFlow>
      <#assign icons = FP.icon?split(",") />
      <#if icons?size lte 1>
      defaultIcon: '${FP.icon?trim}',
      <#else>
      defaultIcon: {
      <#list FP.path?split(",") as path>
         ${path}: <#if path_index lt icons?size>'${icons[path_index]}'<#else>'${icons[0]}'</#if><#if path_has_next>,</#if>
      </#list>
      },
      </#if>
      <#else>
      defaultIcon: '${FP.icon?trim}',
      </#if>
      </#if>
      path: <#if FP.isFlow><#if FP.isDialog>this.openParams()<#else>this.$route.query</#if>.flowAction ? <#if FP.isDialog>this.openParams()<#else>this.$route.query</#if>.flowAction : '${FP.path?split(",")[0]}'<#else>'${FP.path}'</#if>,
      generatorName: '${generatorName}',
      baseUrl: '/${moduleName}/<#if subModule??>${subModule}/</#if>${generatorName}'
    }
  }
}
</script>
