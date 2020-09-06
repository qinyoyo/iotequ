
<template>
  <div :class="joinMode?'cg-join-list':'cg-list'">
    <el-backtop v-if="isTableMode()" ref="cgListBacktop" class="el-backtop" target=".cg-list-cgtable .el-table__body-wrapper" :visibility-height="200">
      <i class="el-icon-caret-top" />
    </el-backtop>
    <el-table ref="cgList" v-if="isTableMode()" v-loading="listLoading" :data="rows" :class="className" row-key="id" :row-class-name="rowClassName" 
              style="width: 100%" :height="tableHeight()" :size="$store.state.app.size" 
              v-set-input:no-tab-index="{tabIndex: -1}" v-table-enter-tab stripe :border="!mobile" highlight-current-row fit :span-method="groupFields" 
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
      <cg-table-column prop="module" :page="1" :label="$t('cgProject.field.name')" sortable align="left" >
        <template slot-scope="scope">
          <el-input v-if="scope.row.inlineEditting" v-model="scope.row.module" type="text" />
          <span v-else>{{ scope.row.module }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="code" :page="1" :label="$t('cgTable.field.code')" sortable align="left" >
        <template slot-scope="scope">
          <el-input v-if="scope.row.inlineEditting" v-model="scope.row.code" type="text" />
          <span v-else>{{ scope.row.code }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="title" :page="1" :label="$t('cgTable.field.title')" align="left" >
        <template slot-scope="scope">
          <el-input v-if="scope.row.inlineEditting" v-model="scope.row.title" type="text" />
          <span v-else>{{ scope.row.title }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="name" :page="1" :label="$t('cgTable.field.name')" sortable align="left" >
        <template slot-scope="scope">
          <el-input v-if="scope.row.inlineEditting" v-model="scope.row.name" type="text" />
          <span v-else>{{ scope.row.name }}</span>
        </template>

      </cg-table-column>
      <cg-action v-model="showActionView" mode="2" :url="baseUrl" :actions="list_allActions(myself,'main')" @actionClick="doAction" />
    </el-table>
    <cg-card-list v-else ref="cgList" v-loading="listLoading" :render="rowRender" :data="rows" :mainClass="className" row-key="id" :multiple="false" :height="tableHeight()" 
                  :isLoading="listLoading" :groupBy="rowRenderGroupTitle?rowRenderGroupTitle:'module'"
                  :isUnMore="true" :cgList="myself"
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
        <cg-select v-model="queryRecord.searchFields" dictionary="projectId|cgProject.field.name,code|cgTable.field.code,name|cgTable.field.name," multiple/>
      </el-form-item>
      <el-divider />
      <div v-show="!queryRecord.search">
        <cg-join v-model="projectIdJoinVisible">
          <CgListCgProject slot="popover" ref="projectIdJoin" openID="projectid-join" :height="joinHeight()" :joinShow="projectIdJoinVisible" joinMultiple
            :originSelections="queryRecord.projectId" selectionKey="id" joinMode @closeJoinList="(rows)=>{ getJoinFields('projectId',rows)}" @showJoinList="projectIdJoinVisible=true"/>
        <el-form-item slot="reference" :label="$t('cgProject.field.name')" prop="module" :size="$store.state.app.size">
          <cg-select v-model="queryRecord.module" :dictionary="filterQueryDictionary.module"
                     :disabled="fixedQueryRecord.module?true:false" allow-create multiple clearable />
        </el-form-item>
        </cg-join>
        <el-form-item :label="$t('cgTable.field.code')" prop="code" :size="$store.state.app.size">
          <el-input v-model="queryRecord.code" type="text" name="code"
                    :readonly="fixedQueryRecord.code?true:false" :label="$t('cgTable.field.code')" clearable resize autofocus />
        </el-form-item>
        <el-form-item :label="$t('cgTable.field.name')" prop="name" :size="$store.state.app.size">
          <el-input v-model="queryRecord.name" type="text" name="name"
                    :readonly="fixedQueryRecord.name?true:false" :label="$t('cgTable.field.name')" clearable resize autofocus />
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
import CgListCgProject from '@/views/codegenerator/cgProject/CgListCgProject.vue'
import rulesObject from './rules.js'
const mixins = []
const mixinContext = require.context('.', false, /CgListCgTable-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default {
  name: 'CgListCgTable',
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
  components: { CgListCgProject },
  data() {
    return {
      cgList,
      myself: this,
      rulesObject,
      path: 'list',
      title: this.$t('cgTable.title.list'),
      showQuery: false,
      showActionView: false,
      defaultOrder: 'module,code',
      queryRecord: this.initialQueryRecord(),
      formPath: '/codegenerator/cgTable/record',
      listLoading: false,
      rows: [],
      selections: this.originSelections,
      localExport: false,
      parentField: null,
      idField: 'id',
			dictionary: {
			  dictTemplate: this.getDictionary('vue-element'),
			  dictActionList: this.getDictionary('list,add,view,edit,delete,batdel,import,export,flow,query,editInline_add'),
			  dictFlowDynaFieldsOp: []
		  },
      needLoadDictionary: true,
      filterQueryDictionary: {
        module: [],
      },
      projectIdJoinVisible: false,
      paginationPageSize: 0,
      sortableFields: [],
      sortableFieldsOrder: [],
      totalEdittingRows: 0,
      editInlineFields: hasAuthority('/codegenerator/cgTable/updateSelective')?['module', 'code', 'title', 'name']:null,
      hasSonTables: true,
      groupByEntityFields: 'module',
      contextMenu: { top: 0, left: 0, visible: false, row: null, actions: [], trElement: null },
      generatorName: 'cgTable',
      baseUrl: '/codegenerator/cgTable'
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
      return this.openID ? 'cg-list-cgtable cg-list-cgtable' + '-'+this.openID : 'cg-list-cgtable'
    },
    additionalActions() {
      if (this.joinMode) return []
      else return [
        {
          action: 'create',
          icon: 'el-icon-receiving',
          title: 'cgTable.action.create',
          groupid: 10,
          confirm: '',
          rowProperty: 'nr',
          actionProperty: 'pg',
          displayProperties: 'tb,hm',
          appendClass: '',
          needRefresh: true
        },
        {
          action: 'sync',
          icon: 'fa fa-retweet fa-fw',
          title: 'cgTable.action.sync',
          groupid: 10,
          confirm: 'cgTable.action.syncConfirm',
          rowProperty: 'mr',
          actionProperty: 'pg',
          displayProperties: 'hm,tb,gs',
          appendClass: '',
          needRefresh: true
        },
        {
          action: 'generator',
          icon: 'fa fa-magic fa-fw',
          title: 'cgTable.action.generator',
          groupid: 10,
          confirm: 'cgTable.action.generatorConfirm',
          rowProperty: 'mr',
          actionProperty: 'aj',
          displayProperties: 'tb,rw',
          appendClass: '{request: {timeout: 0}}',
          needRefresh: false
        },
      ]
    },
    allActions() {
      if (this.joinMode) return 'refresh,query'
      else return 'query,,list,add,view,edit,delete,batdel,import,export,generator,sync,create,'
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
      if (this.height > 0) return this.height
      else return (this.containerHeight())
    },
    initialQueryRecord() {
      return Object.assign({
      }, this.fixedQueryRecord)
    },
    groupFields({ row, column, rowIndex, columnIndex }) {
      return cgList.list_groupFields(this, this.groupByEntityFields.split(','), row, column, rowIndex, columnIndex)
    },
    getJoinFields(field,rows) {
      const joinDefine = {
        projectId: {
          valueField: 'id',
          fields: 'project=code,groupId=groupId,module=name'
        },
      }
      this[field+'JoinVisible'] = false
      this.setJoinValues(this.queryRecord, field, joinDefine[field], rows)
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
