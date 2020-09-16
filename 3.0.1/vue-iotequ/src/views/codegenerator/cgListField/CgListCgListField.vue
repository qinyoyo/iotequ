
<template>
  <div :class="joinMode?'cg-join-list':'cg-list'">
    <el-backtop v-if="isTableMode()" ref="cgListBacktop" class="el-backtop" target=".cg-list-cglistfield .el-table__body-wrapper" :visibility-height="200">
      <i class="el-icon-caret-top" />
    </el-backtop>
    <el-table ref="cgList" v-if="isTableMode()" v-loading="listLoading" :data="rows" :class="className" row-key="id" :row-class-name="rowClassName" 
              style="width: 100%" :height="tableHeight()" :size="$store.state.app.size" 
              stripe :border="!mobile" highlight-current-row fit 
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
      <cg-table-column prop="entityField" :page="1" :label="$t('cgListField.field.entityField')" sortable align="left" >
        <template slot-scope="scope">
          <el-input v-if="scope.row.inlineEditting" v-model="scope.row.entityField" type="text" />
          <span v-else>{{ scope.row.entityField }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="orderNum" :page="1" :label="$t('cgListField.field.orderNum')" sortable align="right" >
        <template slot-scope="scope">
          <el-input v-if="scope.row.inlineEditting" v-model="scope.row.orderNum" type="text" />
          <span v-else>{{ scope.row.orderNum }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="width" :page="1" :label="$t('cgListField.field.width')" align="left" >
        <template slot-scope="scope">
          <el-input v-if="scope.row.inlineEditting" v-model="scope.row.width" type="text" />
          <span v-else>{{ scope.row.width }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="queryMode" type="dict" :page="1" :label="$t('cgListField.field.queryMode')" align="right" >
        <template slot-scope="scope">
          <cg-select v-if="scope.row.inlineEditting" v-model="scope.row.queryMode" automaticDropdown appendToBody :dictionary="dictionary.dictQueryMode" allow-create numberic />
          <span v-else>{{ dictValue(scope.row.queryMode,dictionary.dictQueryMode,false,true) }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="align" :page="1" :label="$t('cgListField.field.align')" align="left" >
        <template slot-scope="scope">
          <el-input v-if="scope.row.inlineEditting" v-model="scope.row.align" type="text" />
          <span v-else>{{ scope.row.align }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="hidden" :page="1" :label="$t('cgListField.field.hidden')" align="left" >
        <template slot-scope="scope">
          <el-switch v-if="scope.row.inlineEditting" v-model="scope.row.hidden" />
          <cg-icon v-else :color="scope.row.hidden?'#46a6ff':'grey'" :icon="scope.row.hidden?'fa fa-check-circle':'fa fa-circle-o'"/>
        </template>

      </cg-table-column>
      <cg-table-column prop="editInline" :page="1" :label="$t('cgListField.field.editInline')" align="left" >
        <template slot-scope="scope">
          <el-switch v-if="scope.row.inlineEditting" v-model="scope.row.editInline" />
          <cg-icon v-else :color="scope.row.editInline?'#46a6ff':'grey'" :icon="scope.row.editInline?'fa fa-check-circle':'fa fa-circle-o'"/>
        </template>

      </cg-table-column>
      <cg-action v-model="showActionView" mode="2" :url="baseUrl" :actions="cgList.list_allActions(myself,'main')" @actionClick="doAction" />
    </el-table>
    <cg-card-list v-else ref="cgList" v-loading="listLoading" :render="rowRender" :data="rows" :mainClass="className" row-key="id" :multiple="false" :height="tableHeight()" 
                  :isLoading="listLoading" 
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
    <cg-query-condition v-model="showQuery" :modal="!joinMode" :queryRecord="queryRecord"
                        @refresh="doAction('refresh')" @reset="queryRecord=initialQueryRecord()">
      <el-form-item :label="$t('system.action.fuzzyQuery')" prop="search" :size="$store.state.app.size">
        <el-input v-model="queryRecord.search" type="text" name="search" clearable resize autofocus
                  prefix-icon="el-icon-search" :placeholder="$t('system.message.fuzzyQueryTip')" @keyup.enter.native="doAction('refresh')" />
      </el-form-item>
      <el-form-item v-show="queryRecord.search" :label="$t('system.action.field')" prop="searchFields" :size="$store.state.app.size">
        <cg-select v-model="queryRecord.searchFields" dictionary="entityField|cgListField.field.entityField," multiple/>
      </el-form-item>
      <el-divider />
      <div v-show="!queryRecord.search">
        <el-form-item :label="$t('cgListField.field.entityField')" prop="entityField" :size="$store.state.app.size">
          <el-input v-model="queryRecord.entityField" type="text" name="entityField"
                    :readonly="fixedQueryRecord.entityField?true:false" :label="$t('cgListField.field.entityField')" clearable resize autofocus/>
        </el-form-item>
      </div>
    </cg-query-condition>
  </div>
</template>

<script>
import {hasAuthority} from '@/utils/cg'
import ParentTable from '@/views/common-views/components/table'
const Comp = {
  name: 'CgListCgListField',
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
      defaultOrder: 'order_num asc',
      queryRecordFields: ['entityField'],
      formPath: '/codegenerator/cgListField/record',
      idField: 'id',
      dictionary: {
        dictShowType: this.getDictionary('text,textarea,boolean,false_if_null,password,date,time,datetime,number,email,search,file,image,url,tel,color,html,2dcode'),
        dictQueryMode: this.getDictionary('0,1,2,3','无,自动,筛选,范围')
      },
      totalEdittingRows: 0,
      editInlineFields: hasAuthority('/codegenerator/cgListField/updateSelective')?['entityField', 'orderNum', 'width', 'queryMode', 'align', 'hidden', 'editInline']:null,
      listName: 'cgListField',
      multipleSelection: true,
      generatorName: 'cgListField',
      baseUrl: '/codegenerator/cgListField'
    }
  },
  computed: {
    additionalActions() {
      if (this.joinMode) return []
      else return [
        {
          action: 'select',
          icon: 'fa fa-check-square-o',
          title: 'cgListField.action.select',
          groupid: 10,
          confirm: '',
          rowProperty: 'nr',
          actionProperty: 'js',
          displayProperties: 'tb',
          appendClass: '',
          needRefresh: true
        }
      ]
    },
    allActions() {
      if (this.joinMode) return 'refresh,query'
      else return 'query,,list,add,view,edit,delete,batdel,import,export,select,'
    }
  },
  mounted() {
    this.cgList.list_tableInit(this,'orderNum')
  },
  methods: {
    initialQueryRecord() {
      return Object.assign({
        showType: null,
        entityField: null,
        queryMode: null,
        align: null,
        hidden: null,
        editInline: null,
      }, this.fixedQueryRecord)
    },
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /CgListCgListField-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
  mixins
}
</script>
