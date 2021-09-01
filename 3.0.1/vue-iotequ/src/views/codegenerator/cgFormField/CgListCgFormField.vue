
<template>
  <div :class="joinMode?'cg-join-list':'cg-list'">
    <el-backtop v-if="isTableMode()" ref="cgListBacktop" class="el-backtop" target=".cg-list-cgformfield .el-table__body-wrapper" :visibility-height="200">
      <i class="el-icon-caret-top" />
    </el-backtop>
    <el-table ref="cgList" v-if="isTableMode()" v-loading="listLoading" :data="rows" :class="className" row-key="id" :row-class-name="rowClassName" 
              style="width: 100%" :height="tableHeight()" :size="$store.state.app.size" 
              v-set-input:no-tab-index="{tabIndex: -1}" v-table-enter-tab stripe :border="!mobile" highlight-current-row fit 
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
      <cg-table-column prop="entityField" :page="1" :label="$t('cgFormField.field.entityField')" sortable :sort-method="(a,b)=>chineseSort(a.entityField,b.entityField)" align="left" >
        <template slot-scope="scope">
          <el-input v-if="scope.row.inlineEditting" v-model="scope.row.entityField" type="text" />
          <span v-else>{{ scope.row.entityField }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="width" :page="1" :label="$t('cgFormField.field.width')" align="right" >
        <template slot-scope="scope">
          <el-input v-if="scope.row.inlineEditting" v-model="scope.row.width" type="text" />
          <span v-else>{{ scope.row.width }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="readonly" :page="1" :label="$t('cgFormField.field.readonly')" align="left" >
        <template slot-scope="scope">
          <el-switch v-if="scope.row.inlineEditting" v-model="scope.row.readonly" />
          <cg-icon v-else :color="scope.row.readonly?'#46a6ff':'grey'" :icon="scope.row.readonly?'fa fa-check-circle':'fa fa-circle-o'"/>
        </template>

      </cg-table-column>
      <cg-table-column prop="mustInput" :page="1" :label="$t('cgFormField.field.mustInput')" align="left" >
        <template slot-scope="scope">
          <el-switch v-if="scope.row.inlineEditting" v-model="scope.row.mustInput" />
          <cg-icon v-else :color="scope.row.mustInput?'#46a6ff':'grey'" :icon="scope.row.mustInput?'fa fa-check-circle':'fa fa-circle-o'"/>
        </template>

      </cg-table-column>
      <cg-table-column prop="hidden" :page="1" :label="$t('cgFormField.field.hidden')" align="left" >
        <template slot-scope="scope">
          <el-switch v-if="scope.row.inlineEditting" v-model="scope.row.hidden" />
          <cg-icon v-else :color="scope.row.hidden?'#46a6ff':'grey'" :icon="scope.row.hidden?'fa fa-check-circle':'fa fa-circle-o'"/>
        </template>

      </cg-table-column>
      <cg-table-column prop="validateAsTitle" :page="1" :label="$t('cgFormField.field.validateAsTitle')" align="left" >
        <template slot-scope="scope">
          <el-switch v-if="scope.row.inlineEditting" v-model="scope.row.validateAsTitle" />
          <cg-icon v-else :color="scope.row.validateAsTitle?'#46a6ff':'grey'" :icon="scope.row.validateAsTitle?'fa fa-check-circle':'fa fa-circle-o'"/>
        </template>

      </cg-table-column>
      <cg-table-column prop="groupTitle" :page="1" :label="$t('cgFormField.field.groupTitle')" align="left" >
        <template slot-scope="scope">
          <el-input v-if="scope.row.inlineEditting" v-model="scope.row.groupTitle" type="text" />
          <span v-else>{{ scope.row.groupTitle }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="orderNum" :page="1" :label="$t('cgFormField.field.orderNum')" sortable align="right" >
        <template slot-scope="scope">
          <el-input v-if="scope.row.inlineEditting" v-model="scope.row.orderNum" type="text" />
          <span v-else>{{ scope.row.orderNum }}</span>
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
    <cg-query-condition v-model="showQuery" ref="query" :modal="!joinMode" :queryRecord="queryRecord"
                        @refresh="doAction('refresh')" @reset="queryRecord=initialQueryRecord()">
      <el-form-item :label="$t('system.action.fuzzyQuery')" prop="search" :size="$store.state.app.size">
        <el-input v-model="queryRecord.search" type="text" name="search" clearable resize autofocus
                  prefix-icon="el-icon-search" :placeholder="$t('system.message.fuzzyQueryTip')" @keyup.enter.native="doAction('refresh')" />
      </el-form-item>
      <el-form-item v-show="queryRecord.search" :label="$t('system.action.field')" prop="searchFields" :size="$store.state.app.size">
        <cg-select v-model="queryRecord.searchFields" dictionary="entityField|cgFormField.field.entityField," multiple/>
      </el-form-item>
      <el-divider />
      <div v-show="!queryRecord.search">
        <el-form-item :label="$t('cgFormField.field.entityField')" prop="entityField" :size="$store.state.app.size">
          <el-input v-model="queryRecord.entityField" type="text" name="entityField"
                    :readonly="fixedQueryRecord.entityField?true:false" :label="$t('cgFormField.field.entityField')" clearable resize autofocus/>
        </el-form-item>
      </div>
    </cg-query-condition>
  </div>
</template>

<script>
import {hasAuthority} from '@/utils/cg'
import rulesObject from './rules.js'
import ParentTable from '@/views/common-views/components/table'
const Comp = {
  name: 'CgListCgFormField',
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
      defaultOrder: 'order_num asc',
      queryRecordFields: ['entityField'],
      formPath: '/codegenerator/cgFormField/record',
      idField: 'id',
      dictionary: {
        dictShowType: this.getDictionary('text,textarea,boolean,false_if_null,password,date,time,datetime,number,email,search,file,image,url,tel,color,html,2dcode')
      },
      totalEdittingRows: 0,
      editInlineFields: hasAuthority('/codegenerator/cgFormField/updateSelective')?['entityField', 'width', 'readonly', 'mustInput', 'hidden', 'validateAsTitle', 'groupTitle', 'orderNum']:null,
      listName: 'cgFormField',
      multipleSelection: true,
      generatorName: 'cgFormField',
      baseUrl: '/codegenerator/cgFormField'
    }
  },
  computed: {
    additionalActions() {
      if (this.joinMode) return []
      else return [
        {
          action: 'select',
          icon: 'fa fa-check-square-o',
          title: 'cgFormField.action.select',
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
        readonly: null,
        mustInput: null,
        hidden: null,
        validateAsTitle: null,
        groupTitle: null,
      }, this.fixedQueryRecord)
    },
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /CgListCgFormField-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
  mixins
}
</script>
