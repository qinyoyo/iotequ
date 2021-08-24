
<template>
  <div :class="joinMode?'cg-join-list':'cg-list'">
    <el-backtop v-if="isTableMode()" ref="cgListBacktop" class="el-backtop" target=".cg-list-cgform .el-table__body-wrapper" :visibility-height="200">
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
      <cg-table-column prop="name" :page="1" :label="$t('cgForm.field.name')" sortable align="left" header-align="left" >
        <template slot-scope="scope">
          {{ scope.row.name }}
        </template>

      </cg-table-column>
      <cg-table-column prop="icon" :page="1" :label="$t('cgForm.field.icon')" align="left" >
        <template slot-scope="scope">
          <el-input v-if="scope.row.inlineEditting" v-model="scope.row.icon" type="text" />
          <span v-else>{{ scope.row.icon }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="headTitle" :page="1" :label="$t('cgForm.field.headTitle')" align="left" >
        <template slot-scope="scope">
          <el-input v-if="scope.row.inlineEditting" v-model="scope.row.headTitle" type="text" />
          <span v-else>{{ scope.row.headTitle }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="tagTitle" :page="1" :label="$t('cgForm.field.tagTitle')" align="left" >
        <template slot-scope="scope">
          <el-input v-if="scope.row.inlineEditting" v-model="scope.row.tagTitle" type="text" />
          <span v-else>{{ scope.row.tagTitle }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="isFlow" :page="1" :label="$t('cgForm.field.isFlow')" align="center" >
        <template slot-scope="scope">
          <el-switch v-if="scope.row.inlineEditting" v-model="scope.row.isFlow" />
          <cg-icon v-else :color="scope.row.isFlow?'#46a6ff':'grey'" :icon="scope.row.isFlow?'fa fa-check-circle':'fa fa-circle-o'"/>
        </template>

      </cg-table-column>
      <cg-table-column prop="isDialog" :page="1" :label="$t('cgForm.field.isDialog')" align="center" >
        <template slot-scope="scope">
          <el-switch v-if="scope.row.inlineEditting" v-model="scope.row.isDialog" />
          <cg-icon v-else :color="scope.row.isDialog?'#46a6ff':'grey'" :icon="scope.row.isDialog?'fa fa-check-circle':'fa fa-circle-o'"/>
        </template>

      </cg-table-column>
      <cg-table-column prop="continueAdd" :page="1" :label="$t('cgForm.field.continueAdd')" align="center" >
        <template slot-scope="scope">
          <el-switch v-if="scope.row.inlineEditting" v-model="scope.row.continueAdd" />
          <cg-icon v-else :color="scope.row.continueAdd?'#46a6ff':'grey'" :icon="scope.row.continueAdd?'fa fa-check-circle':'fa fa-circle-o'"/>
        </template>

      </cg-table-column>
      <cg-table-column prop="labelPosition" :page="1" :label="$t('cgForm.field.labelPosition')" align="left" >
        <template slot-scope="scope">
          <el-input v-if="scope.row.inlineEditting" v-model="scope.row.labelPosition" type="text" />
          <span v-else>{{ scope.row.labelPosition }}</span>
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
        <cg-select v-model="queryRecord.searchFields" dictionary="name|cgForm.field.name,formName|," multiple/>
      </el-form-item>
      <el-divider />
      <div v-show="!queryRecord.search">
        <el-form-item :label="$t('cgForm.field.name')" prop="name" :size="$store.state.app.size">
          <el-input v-model="queryRecord.name" type="text" name="name"
                    :readonly="fixedQueryRecord.name?true:false" :label="$t('cgForm.field.name')" clearable resize autofocus/>
        </el-form-item>
        <el-form-item :label="$t('')" prop="formName" :size="$store.state.app.size">
          <el-input v-model="queryRecord.formName" type="text" name="formName"
                    :readonly="fixedQueryRecord.formName?true:false" :label="$t('')" clearable resize autofocus/>
        </el-form-item>
      </div>
    </cg-query-condition>
  </div>
</template>

<script>
import {hasAuthority} from '@/utils/cg'
import ParentTable from '@/views/common-views/components/table'
const Comp = {
  name: 'CgListCgForm',
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
      queryRecordFields: ['name','formName'],
      formPath: '/codegenerator/cgForm/record',
      idField: 'id',
      dictionary: {
        dictTableId: [],
        dictActionList: []
      },
      needLoadDictionary: true,
      totalEdittingRows: 0,
      editInlineFields: hasAuthority('/codegenerator/cgForm/updateSelective')?['icon', 'headTitle', 'tagTitle', 'isFlow', 'isDialog', 'continueAdd', 'labelPosition']:null,
      hasSonTables: true,
      groupByEntityFields: 'tableId',
      listName: 'cgForm',
      multipleSelection: true,
      generatorName: 'cgForm',
      baseUrl: '/codegenerator/cgForm'
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
        tableId: null,
        actionList: null,
        id: null,
        formId: null,
        name: null,
        formName: null,
        icon: null,
        headTitle: null,
        tagTitle: null,
        isFlow: null,
        isDialog: null,
        continueAdd: null,
        labelPosition: null,
      }, this.fixedQueryRecord)
    },
    groupFields({ row, column, rowIndex, columnIndex }) {
      return this.cgList.list_groupFields(this, this.groupByEntityFields.split(','), row, column, rowIndex, columnIndex)
    },
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /CgListCgForm-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
  mixins
}
</script>
