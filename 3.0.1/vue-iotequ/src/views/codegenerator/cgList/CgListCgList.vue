
<template>
  <div :class="joinMode?'cg-join-list':'cg-list'">
    <el-backtop v-if="isTableMode()" ref="cgListBacktop" class="el-backtop" target=".cg-list-cglist .el-table__body-wrapper" :visibility-height="200">
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
    >
      <cg-icon slot="empty" icon="el-icon-minus" color="grey" />
      <el-table-column v-if="!mobile" type="index" width="50" align="center" class-name="drag-filter" label-class-name="pointer-cursor" header-align="center">
        <i slot="header" class="el-icon-menu"/>
      </el-table-column>
      <el-table-column v-if="multiple" type="selection" align="center" reserve-selection class-name="drag-filter" width="36" />
      <cg-table-column prop="name" :page="1" :label="$t('cgList.field.name')" sortable align="left" >
        <template slot-scope="scope">
          {{ scope.row.name }}
        </template>

      </cg-table-column>
      <cg-table-column prop="headTitle" :page="1" :label="$t('cgList.field.headTitle')" align="left" >
        <template slot-scope="scope">
          {{ localeText(scope.row.headTitle) }}
        </template>

      </cg-table-column>
      <cg-table-column prop="tagTitle" :page="1" :label="$t('cgList.field.tagTitle')" align="left" >
        <template slot-scope="scope">
          {{ localeText(scope.row.tagTitle) }}
        </template>

      </cg-table-column>
      <cg-table-column prop="pagination" :page="1" :label="$t('cgList.field.pagination')" align="left" >
        <template slot-scope="scope">
          <cg-icon :color="scope.row.pagination?'#46a6ff':'grey'" :icon="scope.row.pagination?'fa fa-check-circle':'fa fa-circle-o'"/>
        </template>

      </cg-table-column>
      <cg-table-column prop="editInline" :page="1" :label="$t('cgList.field.editInline')" align="left" >
        <template slot-scope="scope">
          <cg-icon :color="scope.row.editInline?'#46a6ff':'grey'" :icon="scope.row.editInline?'fa fa-check-circle':'fa fa-circle-o'"/>
        </template>

      </cg-table-column>
      <cg-table-column prop="tableHeight" :page="1" :label="$t('cgList.field.tableHeight')" align="left" >
        <template slot-scope="scope">
          {{ scope.row.tableHeight }}
        </template>

      </cg-table-column>
      <cg-table-column prop="sortField" type="dict" :page="1" :label="$t('cgList.field.sortField')" align="left" >
        <template slot-scope="scope">
          {{ dictValue(scope.row.sortField,dictionary.dictTitleField,false,true) }}
        </template>

      </cg-table-column>
      <cg-table-column prop="multiple" :page="1" :label="$t('cgList.field.multiple')" align="left" >
        <template slot-scope="scope">
          <cg-icon :color="scope.row.multiple?'#46a6ff':'grey'" :icon="scope.row.multiple?'fa fa-check-circle':'fa fa-circle-o'"/>
        </template>

      </cg-table-column>
      <cg-action v-model="showActionView" mode="2" :url="baseUrl" :actions="cgList.list_allActions(myself,'main')" @actionClick="doAction" />
    </el-table>
    <cg-card-list v-else ref="cgList" v-loading="listLoading" :render="rowRender" :data="rows" :mainClass="className" row-key="id" :multiple="false" :height="tableHeight()" 
                  :isLoading="listLoading" :groupBy="rowRenderGroupTitle?rowRenderGroupTitle:'tableId'"
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
        <cg-select v-model="queryRecord.searchFields" dictionary="tableId|cgList.field.tableId,listName|,name|cgList.field.name," multiple/>
      </el-form-item>
      <el-divider />
      <div v-show="!queryRecord.search">
        <el-form-item :label="$t('cgList.field.tableId')" prop="tableId" :size="$store.state.app.size">
          <cg-select v-model="queryRecord.tableId" :dictionary="dictionary.dictTableId"
                     :disabled="fixedQueryRecord.tableId?true:false"  :allow-create="!mobile" multiple clearable />
        </el-form-item>
        <el-form-item :label="$t('')" prop="listName" :size="$store.state.app.size">
          <el-input v-model="queryRecord.listName" type="text" name="listName"
                    :readonly="fixedQueryRecord.listName?true:false" :label="$t('')" clearable resize autofocus/>
        </el-form-item>
        <el-form-item :label="$t('cgList.field.name')" prop="name" :size="$store.state.app.size">
          <el-input v-model="queryRecord.name" type="text" name="name"
                    :readonly="fixedQueryRecord.name?true:false" :label="$t('cgList.field.name')" clearable resize autofocus/>
        </el-form-item>
      </div>
    </cg-query-condition>
  </div>
</template>

<script>
import {localeText} from '@/lang'
import {hasAuthority} from '@/utils/cg'
import ParentTable from '@/views/common-views/components/table'
const Comp = {
  name: 'CgListCgList',
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
      defaultOrder: 'name',
      queryRecordFields: ['tableId','listName','name'],
      formPath: '/codegenerator/cgList/record',
      idField: 'id',
      dictionary: {
        dictSons: [],
        dictSonAlign: this.getDictionary('v,h','cgList.field.sonAlign_0,cgList.field.sonAlign_1'),
        dictTitleField: [],
        dictToolbarMode: this.getDictionary('1,2','cgList.field.toolbarMode_0,cgList.field.toolbarMode_1'),
        dictActionList: [],
        dictTableId: []
      },
      needLoadDictionary: true,
      hasSonTables: true,
      groupByEntityFields: 'tableId',
      listName: 'cgList',
      multipleSelection: true,
      generatorName: 'cgList',
      baseUrl: '/codegenerator/cgList'
    }
  },
  computed: {
    allActions() {
      if (this.joinMode) return 'refresh,query'
      else return 'query,,list,add,view,edit,delete,batdel,import,export,'
    }
  },
  mounted() {
    this.cgList.list_tableInit(this)
  },
  methods: {
    initialQueryRecord() {
      return Object.assign({
        sons: null,
        sonAlign: null,
        titleField: null,
        parentEntity: null,
        treeShowEntity: null,
        toolbarMode: null,
        spanEntities: null,
        actionList: null,
        id: null,
        listId: null,
        tableId: null,
        listName: null,
        name: null,
        headTitle: null,
        tagTitle: null,
        pagination: null,
        editInline: null,
        sortField: null,
        multiple: null,
      }, this.fixedQueryRecord)
    },
    groupFields({ row, column, rowIndex, columnIndex }) {
      return this.cgList.list_groupFields(this, this.groupByEntityFields.split(','), row, column, rowIndex, columnIndex)
    },
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /CgListCgList-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
  mixins
}
</script>
