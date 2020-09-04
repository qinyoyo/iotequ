
<template>
  <div :class="joinMode?'cg-join-list':'cg-list'">
    <el-backtop v-if="isTableMode()" ref="cgListBacktop" class="el-backtop" target=".cg-list-authconfig .el-table__body-wrapper" :visibility-height="200">
      <i class="el-icon-caret-top" />
    </el-backtop>
    <el-table ref="cgList" v-if="isTableMode()" v-loading="listLoading" :data="rows" :class="className" row-key="id" :row-class-name="rowClassName" 
              style="width: 100%" :height="tableHeight()" :size="$store.state.app.size" 
              v-set-input:no-tab-index="{tabIndex: -1}" v-table-enter-tab stripe :border="!mobile" highlight-current-row fit 
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
      <el-table-column v-if="multiple" type="selection" align="center" reserve-selection class-name="drag-filter no-tab-index" width="36" />
      <cg-table-column prop="beginAt" type="date" :page="paginationCurrentPage" :label="$t('devAuthConfig.field.beginAt')" align="left" >
        <template slot-scope="scope">
          <cg-date-picker v-if="scope.row.inlineEditting" v-model="scope.row.beginAt" :align="mobile?'right':'center'" type="date" :title="$t('devAuthConfig.field.beginAt')"  clearable/>
          <span v-else>{{ time2String(scope.row.beginAt,'YYYY-MM-DD') }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="endAt" type="date" :page="paginationCurrentPage" :label="$t('devAuthConfig.field.endAt')" align="left" >
        <template slot-scope="scope">
          <cg-date-picker v-if="scope.row.inlineEditting" v-model="scope.row.endAt" :align="mobile?'right':'center'" type="date" :title="$t('devAuthConfig.field.endAt')"  clearable/>
          <span v-else>{{ time2String(scope.row.endAt,'YYYY-MM-DD') }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="startTime" type="time" :page="paginationCurrentPage" :label="$t('devAuthConfig.field.startTime')" align="left" >
        <template slot-scope="scope">
          <cg-date-picker v-if="scope.row.inlineEditting" v-model="scope.row.startTime" :align="mobile?'right':'center'" type="time" :title="$t('devAuthConfig.field.startTime')" format="HH:mm" clearable/>
          <span v-else>{{ time2String(scope.row.startTime,'HH:mm') }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="endTime" type="time" :page="paginationCurrentPage" :label="$t('devAuthConfig.field.endTime')" align="left" >
        <template slot-scope="scope">
          <cg-date-picker v-if="scope.row.inlineEditting" v-model="scope.row.endTime" :align="mobile?'right':'center'" type="time" :title="$t('devAuthConfig.field.endTime')" format="HH:mm" clearable/>
          <span v-else>{{ time2String(scope.row.endTime,'HH:mm') }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="onlyWorkDay" :page="paginationCurrentPage" :label="$t('devAuthConfig.field.onlyWorkDay')" align="left" >
        <template slot-scope="scope">
          <el-switch v-if="scope.row.inlineEditting" v-model="scope.row.onlyWorkDay" />
          <cg-icon v-else :color="scope.row.onlyWorkDay?'#46a6ff':'grey'" :icon="scope.row.onlyWorkDay?'fa fa-check-circle':'fa fa-circle-o'"/>
        </template>

      </cg-table-column>
      <cg-table-column prop="auth" type="dict" :page="paginationCurrentPage" :label="$t('devAuthConfig.field.auth')" align="right" >
        <template slot-scope="scope">
          <cg-select v-if="scope.row.inlineEditting" v-model="scope.row.auth" automaticDropdown appendToBody :dictionary="dictionary.dictAuth" allow-create numberic />
          <span v-else>{{ dictValue(scope.row.auth,dictionary.dictAuth,false,true) }}</span>
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
                  wrapperElement=".cg-list-authconfig .el-table__body-wrapper"
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
  </div>
</template>

<script>
import cgList from '@/utils/cgList'
import cg from '@/utils/cg'
import {hasAuthority} from '@/utils/cg'
import time from '@/utils/time'
import rulesObject from './rules.js'
const mixins = []
const mixinContext = require.context('.', false, /CgListAuthConfig-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default {
  name: 'CgListAuthConfig',
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
      rulesObject,
      path: 'list',
      title: this.$t('devAuthConfig.title.list'),
      showQuery: false,
      showActionView: false,
      defaultOrder: 'id desc',
      queryRecord: this.initialQueryRecord(),
      formPath: '/reader/devAuthConfig/record',
      listLoading: false,
      rows: [],
      selections: this.originSelections,
      parentField: null,
      idField: 'id',
			dictionary: {
			  dictAuth: []
		  },
      needLoadDictionary: true,
      paginationCurrentPage: 1,
      paginationPageSize: this.$store.state.app.device === 'mobile' ? 10 : 30,
      paginationTotalRecords: 0,
      sortableFields: [],
      sortableFieldsOrder: [],
      totalEdittingRows: 0,
      editInlineFields: hasAuthority('/reader/devAuthConfig/updateSelective')?['beginAt', 'endAt', 'startTime', 'endTime', 'onlyWorkDay', 'auth']:null,
      contextMenu: { top: 0, left: 0, visible: false, row: null, actions: [], trElement: null },
      generatorName: 'devAuthConfig',
      baseUrl: '/reader/devAuthConfig'
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
      return this.openID ? 'cg-list-authconfig cg-list-authconfig' + '-'+this.openID : 'cg-list-authconfig'
    },
    allActions() {
      if (this.joinMode) return 'refresh'
      else return ',list,delete,editInline_add,'
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
      return row && row.inlineEditting ? 'edit-inline' : ''
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
      }, this.fixedQueryRecord)
    },
    editInlineAdd() {
      cgList.list_doAction(this, 'editInline_add')
    },
    newRecordForEditInline() {
      return {
        roleId: '',
        onlyWorkDay: '0',
        auth: '4',
      }
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
