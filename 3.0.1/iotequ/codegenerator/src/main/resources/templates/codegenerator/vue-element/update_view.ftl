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
    <el-dialog v-el-drag-dialog :visible.sync="showDialog" top="0px" :class="dialogClass" :close-on-click-modal="false"
               :append-to-body="true" :show-close="!mobile || isDetail" @closed="close">
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
              <#nt><@SPACE FP.name?length/>@closeDialog="close"
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
<#if FP.isDialog>
import elDragDialog from '@/directive/el-drag-dialog'
<#else>
import cg from '@/utils/cg'
</#if>
import CgForm${FP.name?cap_first} from './CgForm${FP.name?cap_first}'
const mixins = []
const mixinContext = require.context('.', false, /${FP.path?split(",")[0]}-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default {
  name: '${FP.name?cap_first}Form',
  <#if FP.isDialog>
  directives: { elDragDialog },
  </#if>
  components: { CgForm${FP.name?cap_first} },
  mixins,
  <#if FP.isDialog>
  props: {
    dialogParams: {
      type: Object,
      default: null
    },
    dialogClass: {
      type: String,
      default: null
    }
  },
  </#if>
  data() {
    return {
      path: <#if FP.isFlow><#if FP.isDialog>this.openParams()<#else>this.$route.query</#if>.flowAction ? <#if FP.isDialog>this.openParams()<#else>this.$route.query</#if>.flowAction : '${FP.path?split(",")[0]}'<#else>'${FP.path}'</#if>,
      openMode: <#if FP.isDialog><#if FP.isDialog>this.openParams()<#else>this.$route.query</#if>.openMode ? <#if FP.isDialog>this.openParams()<#else>this.$route.query</#if>.openMode : </#if>null,
      <#if FP.isDialog>
      showDialog: true,
      </#if>
      generatorName: '${generatorName}',
      baseUrl: '/${moduleName}/<#if subModule??>${subModule}/</#if>${generatorName}'
    }
  },
  computed: {
    mobile() {
      return this.$store.state.app.device === 'mobile'
    },
    isDetail() {
      <#if table.actionList ?? && (table.actionList?index_of(",edit,") gte 0 || table.actionList?index_of(",view,") gte 0) >
      return this.openMode === 'detail' || this.openMode === 'view'
      <#else>
      return false
      </#if>
    },
    isNew() {
      <#if table.actionList?? && table.actionList?index_of(",add,") gte 0 >
      return !this.openMode || this.openMode === 'new' || this.openMode === 'add'
      <#else>
      return false
      </#if>
    },
    isEdit() {
      <#if table.actionList ?? && table.actionList?index_of(",edit,") gte 0>
      return this.openMode === 'edit' || this.openMode === 'modify'
      <#else>
      return false
      </#if>
    },
    titleColor() {
      if (this.isNew) return 'color-danger'
      else if (this.isEdit) return 'color-warning'
      else return 'color-info'
    },
    icon() {
      if (<#if FP.isDialog>this.openParams()<#else>this.$route.query</#if>.icon) return <#if FP.isDialog>this.openParams()<#else>this.$route.query</#if>.icon
      <#if FP.icon?? && FP.icon?trim!=''>
      <#if FP.isFlow>
      <#assign icons = FP.icon?split(",") />
      <#if icons?size lte 1>
      else return '${FP.icon?trim}'
      <#else>
      else {
        const icons = {
          <#list FP.path?split(",") as path>
          ${path}: <#if path_index lt icons?size>'${icons[path_index]}'<#else>'${icons[0]}'</#if><#if path_has_next>,</#if>
          </#list>
        }
        return icons[this.path]
      }
      </#if>
      <#else>
      else return '${FP.icon?trim}'
      </#if>
      <#else>
      else return this.isNew?'el-icon-circle-plus':(this.isEdit?'el-icon-edit-outline':'el-icon-view')
      </#if>
    },
    content() {
      if (<#if FP.isDialog>this.openParams()<#else>this.$route.query</#if>.content) return <#if FP.isDialog>this.openParams()<#else>this.$route.query</#if>.content
      else return this.$t('${generatorName}.title.'+this.path)
    },
    title() {
      if (<#if FP.isDialog>this.openParams()<#else>this.$route.query</#if>.title) return <#if FP.isDialog>this.openParams()<#else>this.$route.query</#if>.title
      else return <#if !FP.isFlow && table.name?? && table.name?trim!=''>this.isNew ? this.$t('system.action.new'):(this.isDetail ? this.$t('system.action.view') : this.$t('system.action.edit'))<#else>null</#if>
    }
  },
  <#if !FP.isDialog>
  activated() {
    this.openMode = <#if FP.isDialog>this.openParams()<#else>this.$route.query</#if>.openMode ? <#if FP.isDialog>this.openParams()<#else>this.$route.query</#if>.openMode : null
  },
  </#if>
  methods: {
    <#if FP.isDialog>
    openParams: function() {
      return this.dialogParams ? this.dialogParams : this.$route.query
    },
    close() {
      this.showDialog = false
      this.$emit('close')
      if (!this.dialogParams) this.$store.dispatch('tagsView/activeLastAfterRemove', this.$route)
    },
    <#else>
    goBack() {
      cg.goBack()
    },
    </#if>
    submit() {
      this.$refs.cgForm.submit()
    },
    openModeChanged(v) {
      this.openMode = v
    }
  }
}
</script>
