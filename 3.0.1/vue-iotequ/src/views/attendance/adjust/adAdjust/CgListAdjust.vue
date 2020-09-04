
<template>
  <div :class="joinMode?'cg-join-list':'cg-list'">
    <el-backtop v-if="isTableMode()" ref="cgListBacktop" class="el-backtop" target=".cg-list-adjust .el-table__body-wrapper" :visibility-height="200">
      <i class="el-icon-caret-top" />
    </el-backtop>
    <el-table ref="cgList" v-if="isTableMode()" v-loading="listLoading" :data="rows" :class="className" row-key="id" :row-class-name="rowClassName" 
              style="width: 100%" :height="tableHeight()" :size="$store.state.app.size" 
              stripe highlight-current-row fit 
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
      <cg-table-column prop="adjustType" type="dict" :page="paginationCurrentPage" :label="$t('adAdjust.field.adjustType')" sortable align="left" >
        <template slot-scope="scope">
          {{ dictValue(scope.row.adjustType,dictionary.dictAdjustType,false,true) }}
        </template>

      </cg-table-column>
      <cg-table-column prop="realName" :page="paginationCurrentPage" :label="$t('adAdjust.field.employee')" align="left" >
        <template slot-scope="scope">
          {{ scope.row.realName }}
        </template>

      </cg-table-column>
      <cg-table-column prop="orgCode" type="dict" :page="paginationCurrentPage" :label="$t('adAdjust.field.orgCode')" sortable align="left" >
        <template slot-scope="scope">
          {{ dictValue(scope.row.orgCode,dictionary.dictOrgCode,true,true) }}
        </template>

      </cg-table-column>
      <cg-table-column prop="state" type="dict" :page="paginationCurrentPage" :label="$t('adAdjust.field.state')" sortable align="left" >
        <template slot-scope="scope">
          {{ dictValue(scope.row.state,dictionary.dictState,false,true) }}
        </template>

      </cg-table-column>
      <cg-table-column prop="hrRealName" :page="paginationCurrentPage" :label="$t('adAdjust.field.hr')" align="left" >
        <template slot-scope="scope">
          {{ scope.row.hrRealName }}
        </template>

      </cg-table-column>
      <cg-table-column prop="approverName" :page="paginationCurrentPage" :label="$t('adAdjust.field.approver')" align="left" >
        <template slot-scope="scope">
          {{ scope.row.approverName }}
        </template>

      </cg-table-column>
      <cg-table-column prop="startTime" type="datetime" :page="paginationCurrentPage" :label="$t('adAdjust.field.startTime')" sortable align="left" >
        <template slot-scope="scope">
          {{ time2String(scope.row.startTime,'YYYY-MM-DD HH:mm') }}
        </template>

      </cg-table-column>
      <cg-table-column prop="endTime" type="datetime" :page="paginationCurrentPage" :label="$t('adAdjust.field.endTime')" align="left" >
        <template slot-scope="scope">
          {{ time2String(scope.row.endTime,'YYYY-MM-DD HH:mm') }}
        </template>

      </cg-table-column>
      <cg-action v-model="showActionView" mode="2" :url="baseUrl" :actions="list_allActions(myself,'main')" @actionClick="doAction" />
    </el-table>
    <cg-card-list v-else ref="cgList" v-loading="listLoading" :render="rowRender" :data="rows" :mainClass="className" row-key="id" :multiple="false" :height="tableHeight()" 
                  :isLoading="listLoading" 
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
                  wrapperElement=".cg-list-adjust .el-table__body-wrapper"
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
                     :top="contextMenu.top" :left="contextMenu.left" :flowActions="contextMenu.flowActions"
                     @hide="contextMenu.visible = false" @select="(a)=>doAction(a, {row: contextMenu.row, trElement: contextMenu.trElement})"
    />
    <cg-query-condition v-model="showQuery" :modal="!joinMode" :queryRecord="queryRecord"
                        @refresh="doAction('refresh')" @reset="queryRecord=initialQueryRecord()">
      <el-form-item :label="$t('system.action.fuzzyQuery')" prop="search" :size="$store.state.app.size">
        <el-input v-model="queryRecord.search" type="text" name="search" clearable resize autofocus
                  prefix-icon="el-icon-search" :placeholder="$t('system.message.fuzzyQueryTip')" @keyup.enter.native="doAction('refresh')" />
      </el-form-item>
      <el-form-item v-show="queryRecord.search" :label="$t('system.action.field')" prop="searchFields" :size="$store.state.app.size">
        <cg-select v-model="queryRecord.searchFields" dictionary="adjustType|adAdjust.field.adjustType,orgCode|adAdjust.field.orgCode,state|adAdjust.field.state,startTime|adAdjust.field.startTime," multiple/>
      </el-form-item>
      <el-divider />
      <div v-show="!queryRecord.search">
        <el-form-item :label="$t('adAdjust.field.adjustType')" prop="adjustType" :size="$store.state.app.size">
          <cg-select v-model="queryRecord.adjustType" :dictionary="dictionary.dictAdjustType"
                     :disabled="fixedQueryRecord.adjustType?true:false"  :allow-create="!mobile" multiple clearable />
        </el-form-item>
        <el-form-item :label="$t('adAdjust.field.orgCode')" prop="orgCode" :size="$store.state.app.size">
          <cg-cascader v-model="queryRecord.orgCode" name="orgCode"  multiple collapse-tags clearable
                       :disabled="fixedQueryRecord.orgCode?true:false" :dictionary="dictionary.dictOrgCode" show-all-levels/>
        </el-form-item>
        <el-form-item :label="$t('adAdjust.field.state')" prop="state" :size="$store.state.app.size">
          <cg-select v-model="queryRecord.state" :dictionary="dictionary.dictState"
                     :disabled="fixedQueryRecord.state?true:false"  :allow-create="!mobile" multiple clearable />
        </el-form-item>
        <el-form-item :label="$t('adAdjust.field.startTime')" prop="startTime" :size="$store.state.app.size">
          <cg-date-picker v-model="queryRecord.startTime" :title="$t('adAdjust.field.startTime')" name="startTime" :align="mobile?'right':'center'" type="datetimerange" :picker-options="datePickerOptions()"
                          :readonly="fixedQueryRecord.startTime?true:false"  clearable />
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
import CgListUserJoin from '@/views/framework/sysUser/CgListUserJoin.vue'
const mixins = []
const mixinContext = require.context('.', false, /CgListAdjust-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default {
  name: 'CgListAdjust',
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
  components: { CgListUserJoin },
  data() {
    return {
      cgList,
      myself: this,
      path: 'list',
      title: this.$t('adAdjust.title.list'),
      showQuery: false,
      showActionView: false,
      defaultOrder: 'register_time desc',
      queryRecord: this.initialQueryRecord(),
      formPath: '/attendance/adjust/adAdjust/record',
      listLoading: false,
      rows: [],
      selections: this.originSelections,
      localExport: true,
      parentField: null,
      idField: 'id',
			dictionary: {
			  dictAdjustType: [],
			  dictOrgCode: [],
			  dictState: []
		  },
      needLoadDictionary: true,
      flowActionList: [
        {
          action: 'approve',
          prefix: 'flow.',
          title: this.$t('adAdjust.title.approve'),
          icon: 'fa fa-address-card-o',
          rowProperty: 'sr',
          actionProperty: 'aj',
          groupid: 20
        }
      ],
      employeeJoinVisible: false,
      hrJoinVisible: false,
      approverJoinVisible: false,
      paginationCurrentPage: 1,
      paginationPageSize: this.$store.state.app.device === 'mobile' ? 10 : 30,
      paginationTotalRecords: 0,
      sortableFields: [],
      sortableFieldsOrder: [],
      editInlineFields: null,
      hasSonTables: true,
      contextMenu: { top: 0, left: 0, visible: false, row: null, actions: [], trElement: null },
      generatorName: 'adAdjust',
      baseUrl: '/attendance/adjust/adAdjust'
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
      return this.openID ? 'cg-list-adjust cg-list-adjust' + '-'+this.openID : 'cg-list-adjust'
    },
    employeeDynaCondition() {
      return {
        orgCode: this.queryRecord.orgCode
      }
    },
    allActions() {
      if (this.joinMode) return 'refresh,query'
      else return 'query,,view,flow,list,edit,delete,localExport,'
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
    defaultEditMode(row) {
      if (this.hasAuthorityOf(this,this.baseUrl,'edit',row)) return 'edit'
      else if (this.hasAuthorityOf(this,this.baseUrl,'view',row)) return 'view'
      else return ''
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
        adjustType: '',
        orgCode: '',
        state: '',
        startTime: '',
      }, this.fixedQueryRecord)
    },
    getJoinFields(field,rows) {
      const joinDefine = {
        employee: {
          valueField: 'id',
          fields: 'realName=realName'
        },
        hr: {
          valueField: 'id',
          fields: 'hrRealName=realName'
        },
        approver: {
          valueField: 'id',
          fields: 'approverName=realName'
        },
      }
      this[field+'JoinVisible'] = false
      this.setJoinValues(this.queryRecord, field, joinDefine[field], rows)
    },
    doAction(action, options) {
      this.queryRecord = Object.assign(this.queryRecord, this.fixedQueryRecord)
      cgList.list_doAction(this, action, options)
    },
    ...cg,
    ...cgList,
    time2String: time.toString
  }
}
</script>
