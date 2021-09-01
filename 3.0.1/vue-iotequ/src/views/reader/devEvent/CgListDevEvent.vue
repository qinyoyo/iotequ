
<template>
  <div :class="joinMode?'cg-join-list':'cg-list'">
    <el-backtop v-if="isTableMode()" ref="cgListBacktop" class="el-backtop" target=".cg-list-devevent .el-table__body-wrapper" :visibility-height="200">
      <i class="el-icon-caret-top" />
    </el-backtop>
    <el-table ref="cgList" v-if="isTableMode()" v-loading="listLoading" :data="rows" :class="className" row-key="id" :row-class-name="rowClassName" 
              style="width: 100%" :height="tableHeight()" :size="$store.state.app.size" 
              stripe :border="!mobile" highlight-current-row fit :span-method="groupFields" 
              @row-click="(row, column, event)=>cgList.list_rowClick(myself,{ row, column, event })" 
              @row-contextmenu="(row, column, event)=>cgList.list_rowContextmenu(myself,{ row, column, event })" 
              @header-click="(column, event)=>cgList.list_headClick(myself,{ column, event })" 
              @row-dblclick="(row, column, event)=>cgList.list_rowDblclick(myself,{ row, column, event })" 
              @cell-click="(row, column, cell, event)=>cgList.list_cellClick(myself,{ row, column, cell, event })" 
              @selection-change="(selection)=>cgList.list_selectionChange(myself, selection)" 
              @current-change="(selection)=>cgList.list_selectionChange(myself, selection)" 
              @sort-change="(options)=>cgList.list_sortChange(myself, options)" 
    >
      <cg-icon slot="empty" icon="el-icon-minus" color="grey" />
      <el-table-column v-if="!mobile" type="index" width="50" align="center" class-name="drag-filter" label-class-name="pointer-cursor" header-align="center">
        <i slot="header" class="el-icon-menu"/>
      </el-table-column>
      <el-table-column v-if="multiple" type="selection" align="center" reserve-selection class-name="drag-filter" width="36" />
      <cg-table-column prop="datDate" :page="paginationCurrentPage" :label="$t('devEvent.field.datDate')" align="left" >
        <template slot-scope="scope">
          {{ scope.row.datDate }}
        </template>

      </cg-table-column>
      <cg-table-column prop="devType" :page="paginationCurrentPage" :label="$t('devEvent.field.devType')" align="left" >
        <template slot-scope="scope">
          {{ scope.row.devType }}
        </template>

      </cg-table-column>
      <cg-table-column prop="devNo" :page="paginationCurrentPage" :label="$t('devEvent.field.devNo')" sortable :sort-method="(a,b)=>chineseSort(a.devNo,b.devNo)" align="left" >
        <template slot-scope="scope">
          {{ scope.row.devNo }}
        </template>

      </cg-table-column>
      <cg-table-column prop="orgCode" type="dict" :page="paginationCurrentPage" :label="$t('devEvent.field.orgCode')" sortable align="left" >
        <template slot-scope="scope">
          {{ dictValue(scope.row.orgCode,dictionary.dictOrgCode,true,true) }}
        </template>

      </cg-table-column>
      <cg-table-column prop="userNo" :page="paginationCurrentPage" :label="$t('devEvent.field.userNo')" align="left" >
        <template slot-scope="scope">
          {{ scope.row.userNo }}
        </template>

      </cg-table-column>
      <cg-table-column prop="realName" :page="paginationCurrentPage" :label="$t('devPeople.field.realName')" sortable :sort-method="(a,b)=>chineseSort(a.realName,b.realName)" align="left" >
        <template slot-scope="scope">
          {{ scope.row.realName }}
        </template>

      </cg-table-column>
      <cg-table-column prop="auditeeAuthType" type="dict" :page="paginationCurrentPage" :label="$t('devEvent.field.auditeeAuthType')" align="left" >
        <template slot-scope="scope">
          {{ dictValue(scope.row.auditeeAuthType,dictionary.dictAuditeeAuthType,false,true) }}
        </template>

      </cg-table-column>
      <cg-table-column prop="datTime" :page="paginationCurrentPage" :label="$t('devEvent.field.datTime')" align="left" >
        <template slot-scope="scope">
          {{ scope.row.datTime }}
        </template>

      </cg-table-column>
      <cg-table-column prop="status" type="dict" :page="paginationCurrentPage" :label="$t('devEvent.field.status')" sortable align="left" >
        <template slot-scope="scope">
          {{ dictValue(scope.row.status,dictionary.dictStatus,false,true) }}
        </template>

      </cg-table-column>
      <cg-action v-model="showActionView" mode="2" :url="baseUrl" :actions="cgList.list_allActions(myself,'main')" @actionClick="doAction" />
    </el-table>
    <cg-card-list v-else ref="cgList" v-loading="listLoading" :render="rowRender" :data="rows" :mainClass="className" row-key="id" :multiple="false" :height="tableHeight()" 
                  :isLoading="listLoading" :groupBy="rowRenderGroupTitle?rowRenderGroupTitle:'datDate'"
                  :isUnMore="paginationCurrentPage * paginationPageSize >= paginationTotalRecords" :cgList="myself"
                  @doAction="(a,row)=>doAction(a,{row})"
                  @loadMore="cgList.list_loadMore(myself)"
                  @refresh="doAction('refresh',{ isPullDownEvent : true})"
                  @row-click="(row, event)=>cgList.list_rowClick(myself,{ row, event })" 
                  @row-contextmenu="(row, event)=>cgList.list_rowContextmenu(myself,{ row, event })" 
                  @row-dblclick="(row, event)=>cgList.list_rowDblclick(myself,{ row, event })" 
                  @selection-change="(selection)=>cgList.list_selectionChange(myself, selection)" 
                  @current-change="(selection)=>cgList.list_selectionChange(myself, selection)" 
    >
      <template slot="append">
	      <cg-action v-model="showActionView" mode="2" :url="baseUrl" :actions="cgList.list_allActions(myself,'main')" @actionClick="doAction" />
      </template>
    </cg-card-list>
    <nut-scroller v-if="mobile && isTableMode()"
                  wrapperElement=".cg-list-devevent .el-table__body-wrapper"
                  :isLoading="listLoading"
                  :isUnMore="paginationCurrentPage * paginationPageSize >= paginationTotalRecords"
                  type="vertical"
                  :pulldownTxt="$t('system.message.pullDownRefresh')"
                  :loadMoreTxt="$t('system.message.pullUpLoad')"
                  :unloadMoreTxt="$t('system.message.noMoreData')"
                  @loadMore="cgList.list_loadMore(myself)"
                  @pulldown="doAction('refresh',{ isPullDownEvent : true})"
    />
    <el-pagination v-if="!mobile" hide-on-single-page @size-change="doAction('refresh')" @current-change="doAction('refresh')" :page-sizes="[10, 20, 30, 50, 100, 200]" layout="total, sizes, prev, pager, next, jumper"
      :current-page.sync="paginationCurrentPage" :page-size.sync="paginationPageSize" :total="paginationTotalRecords">
    </el-pagination>
    <cg-context-menu :show="contextMenu.visible" :actions="contextMenu.actions"
                     :top="contextMenu.top" :left="contextMenu.left" 
                     @hide="contextMenu.visible = false" @select="(a)=>doAction(a, {row: contextMenu.row, trElement: contextMenu.trElement})"
    />
    <cg-query-condition v-model="showQuery" ref="query" :modal="!joinMode" :queryRecord="queryRecord"
                        @refresh="doAction('refresh')" @reset="queryRecord=initialQueryRecord()">
      <el-form-item :label="$t('system.action.fuzzyQuery')" prop="search" :size="$store.state.app.size">
        <el-input v-model="queryRecord.search" type="text" name="search" clearable resize autofocus
                  prefix-icon="el-icon-search" :placeholder="$t('system.message.fuzzyQueryTip')" @keyup.enter.native="doAction('refresh')" />
      </el-form-item>
      <el-form-item v-show="queryRecord.search" :label="$t('system.action.field')" prop="searchFields" :size="$store.state.app.size">
        <cg-select v-model="queryRecord.searchFields" dictionary="devNo|devEvent.field.devNo,orgCode|devEvent.field.orgCode,realName|devPeople.field.realName,status|devEvent.field.status,time|devEvent.field.time," multiple/>
      </el-form-item>
      <el-divider />
      <div v-show="!queryRecord.search">
        <el-form-item :label="$t('devEvent.field.devNo')" prop="devNo" :size="$store.state.app.size">
          <el-input v-model="queryRecord.devNo" type="text" name="devNo"
                    :readonly="fixedQueryRecord.devNo?true:false" :label="$t('devEvent.field.devNo')" clearable resize autofocus/>
        </el-form-item>
        <el-form-item :label="$t('devEvent.field.orgCode')" prop="orgCode" :size="$store.state.app.size">
          <cg-cascader v-model="queryRecord.orgCode" name="orgCode"  multiple collapse-tags clearable
                       :disabled="fixedQueryRecord.orgCode?true:false" :dictionary="dictionary.dictOrgCode" show-all-levels/>
        </el-form-item>
        <el-form-item :label="$t('devPeople.field.realName')" prop="realName" :size="$store.state.app.size">
          <el-input v-model="queryRecord.realName" type="text" name="realName"
                    :readonly="fixedQueryRecord.realName?true:false" :label="$t('devPeople.field.realName')" clearable resize autofocus/>
        </el-form-item>
        <el-form-item :label="$t('devEvent.field.status')" prop="status" :size="$store.state.app.size">
          <cg-select v-model="queryRecord.status" :dictionary="dictionary.dictStatus"
                     :disabled="fixedQueryRecord.status?true:false"  :allow-create="!mobile" multiple clearable />
        </el-form-item>
        <el-form-item :label="$t('devEvent.field.time')" prop="time" :size="$store.state.app.size">
          <cg-date-picker v-model="queryRecord.time" :title="$t('devEvent.field.time')" name="time" :align="mobile?'right':'center'" type="daterange" :picker-options="datePickerOptions()"
                          :readonly="fixedQueryRecord.time?true:false"  clearable />
        </el-form-item>
      </div>
    </cg-query-condition>
  </div>
</template>

<script>
import {hasAuthority} from '@/utils/cg'
import ParentTable from '@/views/common-views/components/table'
const Comp = {
  name: 'CgListDevEvent',
  mixins: [ParentTable],
  props: {
    selectionKey: {
      type: String,
      default: 'id'
    }
  },
  data() {
    return {
      path: 'list',
      defaultOrder: 'time desc',
      queryRecordFields: ['devNo','orgCode','realName','status','time'],
      formPath: '/reader/devEvent/record',
      idField: 'id',
      dictionary: {
        dictAuditorAuthType: this.getDictionary('0,1,2,3,4,5,6,7','devEvent.field.auditorAuthType_0,devEvent.field.auditorAuthType_1,devEvent.field.auditorAuthType_2,devEvent.field.auditorAuthType_3,devEvent.field.auditorAuthType_4,devEvent.field.auditorAuthType_5,devEvent.field.auditorAuthType_6,devEvent.field.auditorAuthType_7'),
        dictAuditorOrg: [],
        dictAuthType: this.getDictionary('0,1','devEvent.field.authType_0,devEvent.field.authType_1'),
        dictOrgCode: [],
        dictAuditeeAuthType: this.getDictionary('0,1,2,3,4,5,6,7','devEvent.field.auditeeAuthType_0,devEvent.field.auditeeAuthType_1,devEvent.field.auditeeAuthType_2,devEvent.field.auditeeAuthType_3,devEvent.field.auditeeAuthType_4,devEvent.field.auditeeAuthType_5,devEvent.field.auditeeAuthType_6,devEvent.field.auditeeAuthType_7'),
        dictStatus: []
      },
      needLoadDictionary: true,
      paginationCurrentPage: 1,
      paginationPageSize: this.$store.state.app.device === 'mobile' ? 10 : 30,
      paginationTotalRecords: 0,
      groupByEntityFields: 'datDate',
      listName: 'devEvent',
      generatorName: 'devEvent',
      baseUrl: '/reader/devEvent'
    }
  },
  computed: {
    allActions() {
      if (this.joinMode) return 'refresh,query'
      else return 'query,,list,view,'
    }
  },
  mounted() {
    this.cgList.list_tableInit(this)
  },
  methods: {
    initialQueryRecord() {
      this.paginationPageSize = (this.$store.state.app.device === 'mobile' ? 10 : 30)
      return Object.assign({
        auditorAuthType: null,
        auditorOrg: null,
        authType: null,
        datDate: null,
        devType: null,
        devNo: null,
        orgCode: null,
        userNo: null,
        realName: null,
        auditeeAuthType: null,
        datTime: null,
        status: null,
        time: null,
      }, this.fixedQueryRecord)
    },
    groupFields({ row, column, rowIndex, columnIndex }) {
      return this.cgList.list_groupFields(this, this.groupByEntityFields.split(','), row, column, rowIndex, columnIndex)
    },
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /CgListDevEvent-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
  mixins
}
</script>
