
<template>
  <div :class="joinMode?'cg-join-list':'cg-list'">
    <el-backtop v-if="isTableMode()" ref="cgListBacktop" class="el-backtop" target=".cg-list-pmversionapplication .el-table__body-wrapper" :visibility-height="200">
      <i class="el-icon-caret-top" />
    </el-backtop>
    <el-table ref="cgList" v-if="isTableMode()" v-loading="listLoading" :data="rows" :class="className" row-key="id" :row-class-name="rowClassName" 
              style="width: 100%" :height="tableHeight()" :size="$store.state.app.size" 
              stripe :border="!mobile" highlight-current-row fit 
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
      <cg-table-column prop="flowState" type="dict" :page="paginationCurrentPage" :label="$t('pmVersionApplication.field.flowState')" sortable align="left" >
        <template slot-scope="scope">
          {{ dictValue(scope.row.flowState,dictionary.dictFlowState,false,true) }}
        </template>

      </cg-table-column>
      <cg-table-column prop="flowRegisterTime" type="datetime" :page="paginationCurrentPage" :label="$t('pmVersionApplication.field.flowRegisterTime')" align="center" >
        <template slot-scope="scope">
          {{ time2String(scope.row.flowRegisterTime,'YYYY-MM-DD HH:mm') }}
        </template>

      </cg-table-column>
      <cg-table-column prop="registerByName" :page="paginationCurrentPage" :label="$t('pmVersionApplication.field.flowRegisterBy')" align="left" >
        <template slot-scope="scope">
          {{ scope.row.registerByName }}
        </template>

      </cg-table-column>
      <cg-table-column prop="applicationType" type="dict" :page="paginationCurrentPage" :label="$t('pmVersionApplication.field.applicationType')" sortable align="left" >
        <template slot-scope="scope">
          {{ dictValue(scope.row.applicationType,dictionary.dictApplicationType,false,true) }}
        </template>

      </cg-table-column>
      <cg-table-column prop="project" type="dict" :page="paginationCurrentPage" :label="$t('pmVersionApplication.field.project')" sortable align="left" >
        <template slot-scope="scope">
          {{ dictValue(scope.row.project,dictionary.dictProject,false,true) }}
        </template>

      </cg-table-column>
      <cg-table-column prop="customer" :page="paginationCurrentPage" :label="$t('pmVersionApplication.field.customer')" sortable align="left" >
        <template slot-scope="scope">
          {{ scope.row.customer }}
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
                  wrapperElement=".cg-list-pmversionapplication .el-table__body-wrapper"
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
        <cg-select v-model="queryRecord.searchFields" dictionary="flowState|pmVersionApplication.field.flowState,applicationType|pmVersionApplication.field.applicationType,project|pmVersionApplication.field.project,customer|pmVersionApplication.field.customer," multiple/>
      </el-form-item>
      <el-divider />
      <div v-show="!queryRecord.search">
        <el-form-item :label="$t('pmVersionApplication.field.flowState')" prop="flowState" :size="$store.state.app.size">
          <cg-select v-model="queryRecord.flowState" :dictionary="dictionary.dictFlowState"
                     :disabled="fixedQueryRecord.flowState?true:false"  :allow-create="!mobile" multiple clearable />
        </el-form-item>
        <el-form-item :label="$t('pmVersionApplication.field.applicationType')" prop="applicationType" :size="$store.state.app.size">
          <cg-select v-model="queryRecord.applicationType" :dictionary="dictionary.dictApplicationType"
                     :disabled="fixedQueryRecord.applicationType?true:false"  :allow-create="!mobile" multiple clearable />
        </el-form-item>
        <el-form-item :label="$t('pmVersionApplication.field.project')" prop="project" :size="$store.state.app.size">
          <cg-select v-model="queryRecord.project" :dictionary="dictionary.dictProject"
                     :disabled="fixedQueryRecord.project?true:false"  :allow-create="!mobile" multiple clearable />
        </el-form-item>
        <el-form-item :label="$t('pmVersionApplication.field.customer')" prop="customer" :size="$store.state.app.size">
          <el-input v-model="queryRecord.customer" type="text" name="customer"
                    :readonly="fixedQueryRecord.customer?true:false" :label="$t('pmVersionApplication.field.customer')" clearable resize autofocus/>
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
const mixinContext = require.context('.', false, /CgListPmVersionApplication-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default {
  name: 'CgListPmVersionApplication',
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
      title: this.$t('pmVersionApplication.title.list'),
      showQuery: false,
      showActionView: false,
      defaultOrder: 'flow_register_time desc',
      queryRecord: this.initialQueryRecord(),
      queryRecordFields: ['flowState','applicationType','project','customer'],
      formPath: '/project/version/pmVersionApplication/record',
      listLoading: false,
      rows: [],
      selections: this.originSelections,
      localExport: false,
      parentField: null,
      idField: 'id',
			dictionary: {
			  dictFlowState: [],
			  dictApplicationType: [],
			  dictProject: []
		  },
      needLoadDictionary: true,
      flowActionList: [
        {
          action: 'assess',
          prefix: 'flow.',
          title: this.$t('pmVersionApplication.title.assess'),
          icon: 'fa fa-binoculars',
          rowProperty: 'sr',
          actionProperty: 'aj',
          groupid: 20
        },
        {
          action: 'decision',
          prefix: 'flow.',
          title: this.$t('pmVersionApplication.title.decision'),
          icon: 'fa fa-gavel',
          rowProperty: 'sr',
          actionProperty: 'aj',
          groupid: 20
        },
        {
          action: 'publish',
          prefix: 'flow.',
          title: this.$t('pmVersionApplication.title.publish'),
          icon: 'fa fa-check',
          rowProperty: 'sr',
          actionProperty: 'aj',
          groupid: 20
        },
        {
          action: 'review',
          prefix: 'flow.',
          title: this.$t('pmVersionApplication.title.review'),
          icon: 'fa fa-search-plus',
          rowProperty: 'sr',
          actionProperty: 'aj',
          groupid: 20
        }
      ],
      paginationCurrentPage: 1,
      paginationPageSize: this.$store.state.app.device === 'mobile' ? 10 : 30,
      paginationTotalRecords: 0,
      sortableFields: [],
      sortableFieldsOrder: [],
      editInlineFields: null,
      hasSonTables: true,
      contextMenu: { top: 0, left: 0, visible: false, row: null, actions: [], trElement: null },
      generatorName: 'pmVersionApplication',
      baseUrl: '/project/version/pmVersionApplication'
    }
  },
  computed: {
    mobile() {
      return this.$store.state.app.device === 'mobile'
    },
    multiple() {
      return this.joinMode ? this.joinMultiple : this.isTableMode()
    },
    className() {
      return this.openID ? 'cg-list-pmversionapplication cg-list-pmversionapplication' + '-'+this.openID : 'cg-list-pmversionapplication'
    },
    allActions() {
      if (this.joinMode) return 'refresh,query'
      else return 'query,,list,add,view,edit,delete,batdel,import,export,flow,'
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
        flowState: null,
        flowRegisterTime: null,
        registerByName: null,
        applicationType: null,
        project: null,
        customer: null,
      }, this.fixedQueryRecord)
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
