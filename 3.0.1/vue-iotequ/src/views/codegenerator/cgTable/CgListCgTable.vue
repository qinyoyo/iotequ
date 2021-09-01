
<template>
  <div :class="joinMode?'cg-join-list':'cg-list'">
    <el-backtop v-if="isTableMode()" ref="cgListBacktop" class="el-backtop" target=".cg-list-cgtable .el-table__body-wrapper" :visibility-height="200">
      <i class="el-icon-caret-top" />
    </el-backtop>
    <el-table ref="cgList" v-if="isTableMode()" v-loading="listLoading" :data="rows" :class="className" row-key="id" :row-class-name="rowClassName" 
              style="width: 100%" :height="tableHeight()" :size="$store.state.app.size" 
              v-set-input:no-tab-index="{tabIndex: -1}" v-table-enter-tab stripe :border="!mobile" highlight-current-row fit :span-method="groupFields" 
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
      <el-table-column v-if="multiple" type="selection" align="center" reserve-selection class-name="drag-filter no-tab-index" width="36" />
      <cg-table-column prop="module" :page="1" :label="$t('cgProject.field.module')" sortable :sort-method="(a,b)=>chineseSort(a.module,b.module)" align="left" >
        <template slot-scope="scope">
          <el-input v-if="scope.row.inlineEditting" v-model="scope.row.module" type="text" />
          <span v-else>{{ scope.row.module }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="code" :page="1" :label="$t('cgTable.field.code')" sortable :sort-method="(a,b)=>chineseSort(a.code,b.code)" align="left" >
        <template slot-scope="scope">
          <el-input v-if="scope.row.inlineEditting" v-model="scope.row.code" type="text" />
          <span v-else>{{ scope.row.code }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="title" :page="1" :label="$t('cgTable.field.title')" align="left" >
        <template slot-scope="scope">
          <cg-input v-if="scope.row.inlineEditting" v-model="scope.row.title" type="text" />
          <span v-else>{{ localeText(scope.row.title) }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="name" :page="1" :label="$t('cgTable.field.name')" sortable :sort-method="(a,b)=>chineseSort(a.name,b.name)" align="left" >
        <template slot-scope="scope">
          <el-input v-if="scope.row.inlineEditting" v-model="scope.row.name" type="text" />
          <span v-else>{{ scope.row.name }}</span>
        </template>

      </cg-table-column>
      <cg-action v-model="showActionView" mode="2" :url="baseUrl" :actions="cgList.list_allActions(myself,'main')" @actionClick="doAction" />
    </el-table>
    <cg-card-list v-else ref="cgList" v-loading="listLoading" :render="rowRender" :data="rows" :mainClass="className" row-key="id" :multiple="false" :height="tableHeight()" 
                  :isLoading="listLoading" :groupBy="rowRenderGroupTitle?rowRenderGroupTitle:'module'"
                  :isUnMore="true" :cgList="myself"
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
        <cg-select v-model="queryRecord.searchFields" dictionary="module|cgProject.field.module,code|cgTable.field.code,name|cgTable.field.name," multiple/>
      </el-form-item>
      <el-divider />
      <div v-show="!queryRecord.search">
        <el-form-item :label="$t('cgProject.field.module')" prop="module" :size="$store.state.app.size">
          <cg-select v-model="queryRecord.module" :dictionary="filterQueryDictionary.module"
                     :disabled="fixedQueryRecord.module?true:false" allow-create multiple clearable />
        </el-form-item>
        <el-form-item :label="$t('cgTable.field.code')" prop="code" :size="$store.state.app.size">
          <el-input v-model="queryRecord.code" type="text" name="code"
                    :readonly="fixedQueryRecord.code?true:false" :label="$t('cgTable.field.code')" clearable resize autofocus/>
        </el-form-item>
        <el-form-item :label="$t('cgTable.field.name')" prop="name" :size="$store.state.app.size">
          <el-input v-model="queryRecord.name" type="text" name="name"
                    :readonly="fixedQueryRecord.name?true:false" :label="$t('cgTable.field.name')" clearable resize autofocus/>
        </el-form-item>
      </div>
    </cg-query-condition>
  </div>
</template>

<script>
import {localeText} from '@/lang'
import {hasAuthority} from '@/utils/cg'
import rulesObject from './rules.js'
import ParentTable from '@/views/common-views/components/table'
const Comp = {
  name: 'CgListCgTable',
  mixins: [ParentTable],
  props: {
    selectionKey: {
      type: String,
      default: 'id'
    }
  },
  data() {
    return {
      rulesObject,
      path: 'list',
      defaultOrder: 'module,code',
      queryRecordFields: ['module','code','name'],
      formPath: '/codegenerator/cgTable/record',
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
      totalEdittingRows: 0,
      editInlineFields: hasAuthority('/codegenerator/cgTable/updateSelective')?['module', 'code', 'title', 'name']:null,
      hasSonTables: true,
      groupByEntityFields: 'module',
      listName: 'cgTable',
      multipleSelection: true,
      generatorName: 'cgTable',
      baseUrl: '/codegenerator/cgTable'
    }
  },
  computed: {
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
  mounted() {
    this.cgList.list_tableInit(this)
  },
  methods: {
    initialQueryRecord() {
      return Object.assign({
        template: null,
        actionList: null,
        flowDynaFieldsOp: null,
        flowDynaFieldsCp: null,
        tableId: null,
        id: null,
        project: null,
        module: null,
        code: null,
        title: null,
        name: null,
      }, this.fixedQueryRecord)
    },
    groupFields({ row, column, rowIndex, columnIndex }) {
      return this.cgList.list_groupFields(this, this.groupByEntityFields.split(','), row, column, rowIndex, columnIndex)
    },
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /CgListCgTable-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
  mixins
}
</script>
