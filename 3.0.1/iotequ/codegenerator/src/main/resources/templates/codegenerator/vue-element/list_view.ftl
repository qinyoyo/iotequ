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
<template>
  <div class="cg-list">
    <el-card shadow="hover">
      <div slot="header">
        <cg-header homeMenu<#if sons??> :showMaster="getShowMaster()"</#if> icon="<#if LP.icon?? && LP.icon?trim!=''>${LP.icon?trim}<#else>el-icon-s-operation</#if>" :hasMenu="hasMenu()"
                   :title="title" :content="content" @goBack="goBack" @menuAction="showActionSheet"<#if sons??> @showDetail="showDetail"</#if>
        />
      </div>
      <#if !sons?? && LP.images?? && LP.images?trim!=''>
      <#if LP.images?index_of(",") gte 0>
      <el-carousel class="cg-hide-join" :interval="3000" type="card" height="auto">
        <el-carousel-item v-for="item in '${LP.images}'.split(',')" key="item">
          <img class="img-center" :src="'/static/img/'+item" alt="" />
        </el-carousel-item>
      </el-carousel>
      <#else>
      <img class="img-center cg-hide-join" src="/static/img/${LP.images}" alt="" />
      </#if>
      </#if>
      <#if sons??>
      <div v-if="mobile">
        <div v-show="showMaster" class="cg-father">
          <CgList${LP.name?cap_first} ref="cgList"<#if !isEmpty(LP.viewProperties!'')> ${LP.viewProperties?trim}</#if><#if canUse('detail',LP.viewProperties!'')> @detail="doShowDetail"</#if><#if canUse('rowClick',LP.viewProperties!'')> @rowClick="rowClick"</#if><#if canUse('refreshed',LP.viewProperties!'')> @refreshed="refreshed"</#if><#if canUse('fixedQueryRecord',LP.viewProperties!'')> :fixedQueryRecord="fixedQueryRecord"</#if> />
        </div>
        <el-tabs v-show="!showMaster" v-model="selectedSon" class="cg-child">
          <#list sons as son>
            <el-tab-pane :label="$t('${son.title}')" name="cgList_son${son_index}" :style="{height: childHeight+'px'}">
              <${son.component} ref="cgList_son${son_index}"<#if !isEmpty(son.viewProperties!'')> ${son.viewProperties?trim}</#if><#if canUse('height',son.viewProperties!'')> :height="childHeight"</#if><#if son.mode=='list'><#if canUse('fixedQueryRecord',son.viewProperties!'')> :fixedQueryRecord.sync="son${son_index}Condition"</#if><#else><#if canUse('queryById',son.viewProperties!'')> :queryById.sync="son${son_index}Condition"</#if></#if> />
            </el-tab-pane>
          </#list>
        </el-tabs>
      </div>
      <div v-else class="components-container" :style="'height:'+clientHeight+'px'">
        <split-pane :default-percent="${LP.generatorType}" <#if LP.sonAlign?? && LP.sonAlign?starts_with('h')>split="horizontal" @resize="resize"<#else>split="vertical"</#if>>
          <template slot="paneL">
            <div class="cg-father">
              <CgList${LP.name?cap_first} ref="cgList"<#if !isEmpty(LP.viewProperties!'')> ${LP.viewProperties?trim}</#if><#if canUse('rowClick',LP.viewProperties!'')> @rowClick="rowClick"</#if><#if canUse('refreshed',LP.viewProperties!'')> @refreshed="refreshed"</#if><#if canUse('fixedQueryRecord',LP.viewProperties!'')> :fixedQueryRecord="fixedQueryRecord"</#if><#if canUse('height',LP.viewProperties!'')> :height="fatherHeight"</#if> />
            </div>
          </template>
          <template slot="paneR">
            <el-tabs v-model="selectedSon" class="cg-child" type="border-card">
              <#list sons as son>
              <el-tab-pane :label="$t('${son.title}')" name="cgList_son${son_index}" :style="{height: childHeight+'px'}">
                <${son.component} ref="cgList_son${son_index}"<#if !isEmpty(son.viewProperties!'')> ${son.viewProperties?trim}</#if><#if canUse('height',son.viewProperties!'')> :height="childHeight"</#if><#if son.mode=='list'><#if canUse('fixedQueryRecord',son.viewProperties!'')> :fixedQueryRecord.sync="son${son_index}Condition"</#if><#else><#if canUse('queryById',son.viewProperties!'')> :queryById.sync="son${son_index}Condition"</#if></#if> />
              </el-tab-pane>
              </#list>
            </el-tabs>
          </template>
        </split-pane>
      </div>
      <#else>
      <CgList${LP.name?cap_first} ref="cgList"<#if !isEmpty(LP.viewProperties!)> ${LP.viewProperties?trim}</#if><#if canUse('height',LP.viewProperties!'')> :height="clientHeight"</#if><#if canUse('detail',LP.viewProperties!'')> @detail="doShowDetail"</#if><#if LP.titleField?? && LP.titleField?trim!='' && canUse('rowClick',LP.viewProperties!'')> @rowClick="rowClick"</#if>/>
      </#if>
    </el-card>
  </div>
</template>

<script>
import cg from '@/utils/cg'
import cgList from '@/utils/cgList'
import CgList${LP.name?cap_first} from './CgList${LP.name?cap_first}.vue'
<#if sons??>
<#list sons as son>
<#if son.componentPath??>
import ${son.component} from '@/views${son.componentPath}.vue'
</#if>
</#list>
</#if>
const mixins = []
const mixinContext = require.context('.', false, /${LP.path}-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default {
  name: '${LP.name?cap_first}List',
  components: { CgList${LP.name?cap_first}<#if sons??><#list sons as son><#if son.componentPath??>, ${son.component}</#if></#list></#if> },
  mixins,
  <#if sons??>
  props: {
    height: {
      type: Number,
      default: 0
    },
    fixedQueryRecord: {
      type: Object,
      default: () => { return {} }
    }
  },
  </#if>
  data() {
    return {
      clientHeight: this.height ? this.height : cg.containerHeight(),
      fatherHeight: (this.height ? this.height : cg.containerHeight())<#if LP.sonAlign?? && LP.sonAlign?starts_with('h')> * (this.$store.state.app.device === 'mobile' ? 1: ${LP.generatorType} / 100)</#if>,
      childHeight: <#if LP.sonAlign?? && LP.sonAlign?starts_with('h')>(this.height?this.height:cg.containerHeight())*(this.$store.state.app.device === 'mobile' ? 1 : (100- ${LP.generatorType})/100)<#else>(this.height?this.height:cg.containerHeight())</#if>-70,
      <#if sons??>
      showMaster: true,
      selectedSon: 'cgList_son0',
      <#list sons as son>
      <#if son.mode=='list'>
      son${son_index}Condition: {
        ${son.entityField}: 'null'
      },
      <#else>
      son${son_index}Condition: 'null',
      </#if>
      </#list>
      </#if>
      <#if LP.titleField?? && LP.titleField?trim!=''>
      contentSubTitle: '',
      </#if>
      path: '${LP.path}',
      generatorName: '${generatorName}',
      baseUrl: '/${moduleName}/<#if subModule??>${subModule}/</#if>${generatorName}'
    }
  },
  watch: {
    fixedQueryRecord: {
      handler(n, o) {
        if (n && Object.keys(n).length > 0 && this.$refs.cgList && typeof this.$refs.cgList.doAction === 'function') this.$refs.cgList.doAction('refresh')
      },
      deep: true,
      immediate: true
    }
  },
  computed: {
    mobile() {
      return this.$store.state.app.device === 'mobile'
    },
    title() {
      <#if sons??>
      if (this.showMaster || !this.mobile) return this.$t('system.action.list')
      else return this.$t('system.action.detail')
      <#else>
      return this.$t('system.action.list')
      </#if>
    },
    content() {
      <#if sons??>
      if (this.showMaster || !this.mobile) return this.$t('${generatorName}.title.${LP.path}')<#if LP.titleField?? && LP.titleField?trim!=''> + (!this.mobile && this.contentSubTitle?' - '+this.contentSubTitle:'')</#if>
      else return <#if LP.titleField?? && LP.titleField?trim!=''>this.contentSubTitle ? this.contentSubTitle : this.$t('${generatorName}.title.${LP.path}')<#else>this.$t('${generatorName}.title.${LP.path}')</#if>
      <#else>
      return this.$t('${generatorName}.title.${LP.path}')<#if LP.titleField?? && LP.titleField?trim!=''> + (!this.mobile && this.contentSubTitle?' - '+this.contentSubTitle:'')</#if>
      </#if>
    }
  },
  methods: {
    goBack() {
      <#if sons??>
      if (this.mobile) {
        if (this.showMaster) cg.goBack()
        else {
          if (this.$refs[this.selectedSon] && this.$refs[this.selectedSon].showMaster !== undefined) {
            if (!this.$refs[this.selectedSon].showMaster) this.$refs[this.selectedSon].showMaster = true
            else this.showMaster = true
          } else this.showMaster = true
        }
      }
      <#else>
      if (this.mobile) cg.goBack()
      </#if>
    },
    hasMenu() {
      <#if sons??>
      if (!this.mobile) return false
      else if (this.showMaster) return true
      else if (this.$refs[this.selectedSon] && typeof this.$refs[this.selectedSon].hasMenu === 'function') return this.$refs[this.selectedSon].hasMenu()
      else return false
      <#else>
      return this.mobile
      </#if>
    },
    showActionSheet() {
      <#if sons??>
      if (this.showMaster && typeof this.$refs.cgList.showActionSheet === 'function') this.$refs.cgList.showActionSheet()
      else if (this.$refs[this.selectedSon] && typeof this.$refs[this.selectedSon].showActionSheet === 'function') this.$refs[this.selectedSon].showActionSheet()
      <#else>
      this.$refs.cgList.showActionSheet()
      </#if>
    },
    <#if sons??>
    <#if LP.sonAlign?? && LP.sonAlign?starts_with('h')>
    resize(percent) {
      this.fatherHeight = this.clientHeight * percent / 100
      this.childHeight = this.clientHeight - this.fatherHeight - 70
    },
    </#if>
    getShowMaster() {
      if (!this.mobile) return undefined
      else if (this.showMaster) return true
      else if (this.$refs[this.selectedSon] && this.$refs[this.selectedSon].showMaster !== undefined) return this.$refs[this.selectedSon].showMaster
      else return undefined
    },
    showDetail() {
      if (!this.mobile) return
      else if (this.showMaster) this.doShowDetail()
      else if (this.$refs[this.selectedSon] && this.$refs[this.selectedSon].showMaster && typeof this.$refs[this.selectedSon].doShowDetail ==='function') this.$refs[this.selectedSon].doShowDetail()
    },
    setChildrenParams(row) {
      <#list sons as son>
      <#if son.mode=='list'>
      if (row && row.${son.pk}) this.son${son_index}Condition.${son.entityField} = row.${son.pk}
      else this.son${son_index}Condition.${son.entityField} = 'null'
      <#else>
      if (row && row.${son.pk}) this.son${son_index}Condition = row.${son.pk}
      else this.son${son_index}Condition = 'null'
      </#if>
      </#list>
    },
    rowClick({ row, column, event }) {
      <#if LP.titleField?? && LP.titleField?trim!=''>
      this.contentSubTitle = row ? row.${LP.titleField} : ''
      </#if>
      if (!this.mobile) this.setChildrenParams(row)
    },
    <#elseif LP.titleField?? && LP.titleField?trim!=''>
    rowClick({ row, column, event }) {
      this.contentSubTitle = row && row.${LP.titleField} ? String(row.${LP.titleField}).local() : ''
    },
    </#if>
    refreshed(listObject) {
      const row = cgList.list_getCurrentRow(listObject)
      <#if sons??>
      <#list sons as son>
      if (this.$refs.cgList_son${son_index} && this.$refs.cgList_son${son_index}.hasOwnProperty('needLoadDictionary')) this.$refs.cgList_son${son_index}.needLoadDictionary = true
      </#list>
      </#if>
      this.setChildrenParams(row)
    },
    doShowDetail(row) {
      if (!row) row = cgList.list_getCurrentRow(this.$refs.cgList)
      <#if sons??>
      this.showMaster = false
      this.setChildrenParams(row)
      <#else>
      cgList.list_rowDblclick(this.$refs.cgList, { row })
      </#if>
    },
  }
}
</script>
