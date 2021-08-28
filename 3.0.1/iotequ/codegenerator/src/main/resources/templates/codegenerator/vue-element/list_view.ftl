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
  <div class="cg-list cg-list-${generatorName}">
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
      <div class="img-center-box">
        <img class="img-center cg-header-image cg-hide-join" src="/static/img/${LP.images}" alt="" />
      </div>
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
      <CgList${LP.name?cap_first} ref="cgList"<#if !isEmpty(LP.viewProperties!)> ${LP.viewProperties?trim}</#if><#if canUse('height',LP.viewProperties!'')> :height="clientHeight"</#if><#if canUse('detail',LP.viewProperties!'')> @detail="doShowDetail"</#if><#if canUse('fixedQueryRecord',LP.viewProperties!'')> :fixedQueryRecord="fixedQueryRecord"</#if><#if LP.titleField?? && LP.titleField?trim!='' && canUse('rowClick',LP.viewProperties!'')> @rowClick="rowClick"</#if>/>
      </#if>
    </el-card>
  </div>
</template>

<script>
import CgList${LP.name?cap_first} from './CgList${LP.name?cap_first}.vue'
<#if sons??>
import ParentList from '@/views/common-views/components/list-sons'
<#list sons as son>
<#if son.componentPath??>
import ${son.component} from '@/views${son.componentPath}.vue'
</#if>
</#list>
<#else>
import ParentList from '@/views/common-views/components/list'
</#if>
const Comp = {
  name: '${LP.name?cap_first}List',
  components: { CgList${LP.name?cap_first}<#if sons??><#list sons as son><#if son.componentPath??>, ${son.component}</#if></#list></#if> },
  mixins: [ParentList],
  data() {
    return {
      <#if LP.sonAlign?? && LP.sonAlign?starts_with('h')>
      fatherHeightPercent: ${LP.generatorType},
      </#if>
      <#if LP.titleField?? && LP.titleField?trim!=''>
      titleField: '${LP.titleField}',
      </#if>
      <#if sons??>
      sonCount: ${sons?size},
      <#list sons as son>
      <#if son.mode=='list'>
      son${son_index}Condition: {
        ${son.entityField}: 'null'
      },
      <#else>
      son${son_index}Condition: 'null',
      </#if>
      </#list>
      sonPkFields: [<#list sons as son>'${son.pk}'<#if son?has_next>, </#if></#list>],
      </#if>
      path: '${LP.path}',
      generatorName: '${generatorName}',
      baseUrl: '/${moduleName}/<#if subModule??>${subModule}/</#if>${generatorName}'
    }
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /${LP.path}-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
  mixins
}
</script>
