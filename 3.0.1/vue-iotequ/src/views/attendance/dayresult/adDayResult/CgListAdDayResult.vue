
<template>
  <div :class="joinMode?'cg-join-list':'cg-list'">
    <el-backtop v-if="isTableMode()" ref="cgListBacktop" class="el-backtop" target=".cg-list-addayresult .el-table__body-wrapper" :visibility-height="200">
      <i class="el-icon-caret-top" />
    </el-backtop>
    <el-table ref="cgList" v-if="isTableMode()" v-loading="listLoading" :data="rows" :class="className" row-key="id" :row-class-name="rowClassName" 
              style="width: 100%" :height="tableHeight()" :size="$store.state.app.size" 
              stripe :border="!mobile" highlight-current-row fit :span-method="groupFields" 
              @row-click="(row, column, event)=>list_rowClick(myself,{ row, column, event })" 
              @row-contextmenu="(row, column, event)=>list_rowContextmenu(myself,{ row, column, event })" 
              @header-click="(column, event)=>list_headClick(myself,{ column, event })" 
              @row-dblclick="(row, column, event)=>list_rowDblclick(myself,{ row, column, event })" 
              @cell-click="(row, column, cell, event)=>list_cellClick(myself,{ row, column, cell, event })" 
              @selection-change="(selection)=>list_selectionChange(myself, selection)" 
              @current-change="(selection)=>list_selectionChange(myself, selection)" 
    >
      <cg-icon slot="empty" icon="el-icon-minus" color="grey" />
      <el-table-column v-if="!mobile" type="index" width="50" align="center" class-name="drag-filter" label-class-name="pointer-cursor" header-align="center">
        <i slot="header" class="el-icon-menu"/>
      </el-table-column>
      <el-table-column v-if="multiple" type="selection" align="center" reserve-selection class-name="drag-filter" width="36" />
      <cg-table-column prop="orgName" :page="paginationCurrentPage" :label="$t('adDayResult.field.orgName')" align="left" >
        <template slot-scope="scope">
          {{ scope.row.orgName }}
        </template>

      </cg-table-column>
      <cg-table-column prop="realName" :page="paginationCurrentPage" :label="$t('adDayResult.field.realName')" sortable align="left" >
        <template slot-scope="scope">
          {{ scope.row.realName }}
        </template>

      </cg-table-column>
      <cg-table-column prop="adDate" type="date" :page="paginationCurrentPage" :label="$t('adDayResult.field.adDate')" sortable align="center" >
        <template slot-scope="scope">
          {{ time2String(scope.row.adDate,'YYYY-MM-DD') }}
        </template>

      </cg-table-column>
      <cg-table-column prop="shiftName" :page="paginationCurrentPage" :label="$t('adDayResult.field.shiftName')" align="left" >
        <template slot-scope="scope">
          {{ scope.row.shiftName }}
        </template>

      </cg-table-column>
      <cg-table-column prop="stateName" :page="paginationCurrentPage" :label="$t('adDayResult.field.stateName')" align="left" >
        <template slot-scope="scope">
          {{ scope.row.stateName }}
        </template>

      </cg-table-column>
      <cg-table-column prop="times" :page="paginationCurrentPage" :label="$t('adDayResult.field.times')" align="right" >
        <template slot-scope="scope">
          {{ scope.row.times }}
        </template>

      </cg-table-column>
      <cg-table-column prop="minutes" :page="paginationCurrentPage" :label="$t('adDayResult.field.minutes')" align="right" >
        <template slot-scope="scope">
          {{ scope.row.minutes }}
        </template>

      </cg-table-column>
      <cg-table-column prop="workMinutes" :page="paginationCurrentPage" :label="$t('adDayResult.field.workMinutes')" align="right" >
        <template slot-scope="scope">
          {{ scope.row.workMinutes }}
        </template>

      </cg-table-column>
      <cg-action v-model="showActionView" mode="2" :url="baseUrl" :actions="list_allActions(myself,'main')" @actionClick="doAction" />
    </el-table>
    <cg-card-list v-else ref="cgList" v-loading="listLoading" :render="rowRender" :data="rows" :mainClass="className" row-key="id" :multiple="false" :height="tableHeight()" 
                  :isLoading="listLoading" :groupBy="rowRenderGroupTitle?rowRenderGroupTitle:'orgName,employeeNo,realName'"
                  :isUnMore="paginationCurrentPage * paginationPageSize >= paginationTotalRecords" :cgList="myself"
                  @doAction="(a,row)=>doAction(a,{row})"
                  @loadMore="list_loadMore(myself)"
                  @refresh="doAction('refresh',{ isPullDownEvent : true})"
                  @row-click="(row, event)=>list_rowClick(myself,{ row, event })" 
                  @row-contextmenu="(row, event)=>list_rowContextmenu(myself,{ row, event })" 
                  @row-dblclick="(row, event)=>list_rowDblclick(myself,{ row, event })" 
                  @selection-change="(selection)=>list_selectionChange(myself, selection)" 
                  @current-change="(selection)=>list_selectionChange(myself, selection)" 
    >
      <template slot="append">
	      <cg-action v-model="showActionView" mode="2" :url="baseUrl" :actions="list_allActions(myself,'main')" @actionClick="doAction" />
      </template>
    </cg-card-list>
    <nut-scroller v-if="mobile && isTableMode()"
                  wrapperElement=".cg-list-addayresult .el-table__body-wrapper"
                  :isLoading="listLoading"
                  :isUnMore="paginationCurrentPage * paginationPageSize >= paginationTotalRecords"
                  type="vertical"
                  :pulldownTxt="$t('system.message.pullDownRefresh')"
                  :loadMoreTxt="$t('system.message.pullUpLoad')"
                  :unloadMoreTxt="$t('system.message.noMoreData')"
                  @loadMore="list_loadMore(myself)"
                  @pulldown="doAction('refresh',{ isPullDownEvent : true})"
    />
    <el-pagination v-if="!mobile" @size-change="doAction('refresh')" @current-change="doAction('refresh')" :page-sizes="[10, 20, 30, 50, 100, 200]" layout="total, sizes, prev, pager, next, jumper"
      :current-page.sync="paginationCurrentPage" :page-size.sync="paginationPageSize" :total="paginationTotalRecords">
    </el-pagination>
    <cg-context-menu :show="contextMenu.visible" :actions="contextMenu.actions"
                     :top="contextMenu.top" :left="contextMenu.left" 
                     @hide="contextMenu.visible = false" @select="(a)=>doAction(a, {row: contextMenu.row, trElement: contextMenu.trElement})"
    />
    <cg-query-condition v-model="showQuery" :modal="!joinMode" :queryRecord="queryRecord"
                        @refresh="doAction('refresh')" @reset="queryRecord=initialQueryRecord()">
      <el-form-item :label="$t('system.action.fuzzyQuery')" prop="search" :size="$store.state.app.size">
        <el-input v-model="queryRecord.search" type="text" name="search" clearable resize autofocus
                  prefix-icon="el-icon-search" :placeholder="$t('system.message.fuzzyQueryTip')" @keyup.enter.native="doAction('refresh')" />
      </el-form-item>
      <el-form-item v-show="queryRecord.search" :label="$t('system.action.field')" prop="searchFields" :size="$store.state.app.size">
        <cg-select v-model="queryRecord.searchFields" dictionary="orgCode|adDayResult.field.orgCode,realName|adDayResult.field.realName,adDate|adDayResult.field.adDate," multiple/>
      </el-form-item>
      <el-divider />
      <div v-show="!queryRecord.search">
        <el-form-item :label="$t('adDayResult.field.orgCode')" prop="orgCode" :size="$store.state.app.size">
          <cg-cascader v-model="queryRecord.orgCode" name="orgCode"  multiple collapse-tags clearable
                       :disabled="fixedQueryRecord.orgCode?true:false" :dictionary="dictionary.dictOrgCode"/>
        </el-form-item>
        <el-form-item :label="$t('adDayResult.field.realName')" prop="realName" :size="$store.state.app.size">
          <el-input v-model="queryRecord.realName" type="text" name="realName"
                    :readonly="fixedQueryRecord.realName?true:false" :label="$t('adDayResult.field.realName')" clearable resize autofocus />
        </el-form-item>
        <el-form-item :label="$t('adDayResult.field.adDate')" prop="adDate" :size="$store.state.app.size">
          <cg-date-picker v-model="queryRecord.adDate" :title="$t('adDayResult.field.adDate')" name="adDate" :align="mobile?'right':'center'" type="daterange" :picker-options="datePickerOptions()"
                          :readonly="fixedQueryRecord.adDate?true:false"  clearable />
        </el-form-item>
      </div>
    </cg-query-condition>
  </div>
</template>

<script>
import cgList from '@/utils/cgList'
import cg from '@/utils/cg'
import {hasAuthority} from '@/utils/cg'
import time from '@/utils/time'
const mixins = []
const mixinContext = require.context('.', false, /CgListAdDayResult-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default {
  name: 'CgListAdDayResult',
  mixins,
  props: {
    joinMode: {
      type: Boolean,
      default: false
    },
    joinMultiple: {
      type: Boolean,
      default: false
    },
    joinShow: {
      type: Boolean,
      default: true
    },
    openID: {
      type: String,
      default: ''
    },
    originSelections: {
      type: String,
      default: ''
    },
    selectionKey: {
      type: String,
      default: 'id'
    },
    height: {
      type: Number,
      default: 0
    },
    fixedQueryRecord: {
      type: Object,
      default: () => { return {} }
    }
  },
  data() {
    return {
      cgList,
      myself: this,
      path: 'list',
      title: this.$t('adDayResult.title.list'),
      showQuery: false,
      showActionView: false,
      defaultOrder: 'org_code,employee_no,ad_date,shift_id,state',
      queryRecord: this.initialQueryRecord(),
      formPath: '/attendance/dayresult/adDayResult/record',
      listLoading: false,
      rows: [],
      selections: this.originSelections,
      localExport: false,
      parentField: null,
      idField: 'id',
			dictionary: {
			  dictOrgCode: []
		  },
      needLoadDictionary: true,
      paginationCurrentPage: 1,
      paginationPageSize: this.$store.state.app.device === 'mobile' ? 10 : 30,
      paginationTotalRecords: 0,
      sortableFields: [],
      sortableFieldsOrder: [],
      editInlineFields: null,
      groupByEntityFields: 'orgName,employeeNo,realName',
      contextMenu: { top: 0, left: 0, visible: false, row: null, actions: [], trElement: null },
      generatorName: 'adDayResult',
      baseUrl: '/attendance/dayresult/adDayResult'
    }
  },
  computed: {
    mobile() {
      return this.$store.state.app.device === 'mobile'
    },
    multiple() {
      return this.joinMode ? this.joinMultiple : false
    },
    className() {
      return this.openID ? 'cg-list-addayresult cg-list-addayresult' + '-'+this.openID : 'cg-list-addayresult'
    },
    additionalActions() {
      if (this.joinMode) return []
      else return [
        {
          action: 'adjust',
          icon: 'fa fa-bolt fa-fw',
          title: 'adDayResult.action.adjust',
          groupid: 10,
          confirm: '',
          rowProperty: 'sr',
          actionProperty: 'aj',
          displayProperties: 'tb',
          appendClass: '',
          needRefresh: true
        },
        {
          action: '_export',
          icon: 'e',
          title: 'adDayResult.action._export',
          groupid: 10,
          confirm: 'adDayResult.action._exportConfirm',
          rowProperty: 'nr',
          actionProperty: 'aj',
          displayProperties: 'tb',
          appendClass: '',
          needRefresh: false
        }
      ]
    },
    allActions() {
      if (this.joinMode) return 'refresh,query'
      else return 'query,,list,export,_export,adjust,'
    }
  },
  watch: {
    fixedQueryRecord: {
      handler(n, o) {
        n && Object.keys(n).length && Object.keys(n).some(k=>{
          if (cg.hasValue(n[k])) return true
        }) && this.doAction('refresh')
      },
      deep: true,
      immediate: true
    },
    joinShow(newValue, oldValue) {
      if (oldValue && !newValue && this.joinMode) {
        this.$emit('closeJoinList', this.multiple ? this.$refs.cgList.store.states.selection : [this.$refs.cgList.store.states.currentRow])
      }
    }
  },
  created() {
    this.doAction('refresh')
  },
  mounted() {
    cgList.list_tableInit(this)
  },
  activated() {
    cgList.list_activedRefresh(this)
  },
  destroyed() {
    cgList.list_destroyScroll(this)
  },
  methods: {
    rowClassName({row, rowIndex}){
      return ''
    },
    isTableMode() {
      return this.joinMode || !this.mobile || this.isLandscape() || typeof this.rowRender !== 'function'
    },
    hasMenu() {
      return this.mobile
    },
    isLandscape() {
      return window.orientation == 90 || window.orientation == -90
    },
    showActionSheet(hidden){
      this.showActionView = !hidden
    },
    tableHeight() {
      if (this.height > 0) return this.height - (this.mobile ? 0 : 40)
      else return (this.containerHeight() - (this.mobile ? 0 : 40))
    },
    initialQueryRecord() {
      return Object.assign({
        orgCode: '',
        realName: '',
        adDate: [time.startOf(time.dateAdd(new Date(),-1,'day'),'day'),time.endOf(time.dateAdd(new Date(),-1,'day'))],
      }, this.fixedQueryRecord)
    },
    groupFields({ row, column, rowIndex, columnIndex }) {
      return cgList.list_groupFields(this, this.groupByEntityFields.split(','), row, column, rowIndex, columnIndex)
    },
    doAction(action, options) {
      this.queryRecord = Object.assign(this.queryRecord, this.fixedQueryRecord)
      cgList.list_doAction(this, action, Object.assign(options ? options : {}, this.additionalActions.find(e => e.action === action)))
    },
    ...cg,
    ...cgList,
    time2String: time.toString
  }
}
</script>
