
<template>
  <div :class="joinMode?'cg-join-list':'cg-list'">
    <el-backtop v-if="isTableMode()" ref="cgListBacktop" class="el-backtop" target=".cg-list-menu .el-table__body-wrapper" :visibility-height="200">
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
    >
      <cg-icon slot="empty" icon="el-icon-minus" color="grey" />
      <el-table-column v-if="!mobile" type="index" width="50" align="center" class-name="drag-filter" label-class-name="pointer-cursor" header-align="center">
        <i slot="header" class="el-icon-menu"/>
      </el-table-column>
      <el-table-column v-if="multiple" type="selection" align="center" reserve-selection class-name="drag-filter no-tab-index" width="36" />
      <el-table-column prop="name" width="200" :label="$t('sysMenu.field.name')" sortable align="left" >
        <template slot-scope="scope">
          <el-input v-if="scope.row.inlineEditting" v-model="scope.row.name" type="text" />
          <span v-else>{{ $t(scope.row.name) }}</span>
        </template>

      </el-table-column>
      <cg-table-column prop="sortNum" :page="1" :label="$t('sysMenu.field.sortNum')" sortable align="right" >
        <template slot-scope="scope">
          <el-input v-if="scope.row.inlineEditting" v-model="scope.row.sortNum" type="text" />
          <span v-else>{{ scope.row.sortNum }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="isDivider" :page="1" :label="$t('sysMenu.field.isDivider')" align="center" >
        <template slot-scope="scope">
          <el-switch v-if="scope.row.inlineEditting" v-model="scope.row.isDivider" />
          <cg-icon v-else :color="scope.row.isDivider?'#46a6ff':'grey'" :icon="scope.row.isDivider?'fa fa-check-circle':'fa fa-circle-o'"/>
        </template>

      </cg-table-column>
      <cg-table-column prop="mobileHidden" :page="1" :label="$t('sysMenu.field.mobileHidden')" align="left" >
        <template slot-scope="scope">
          <el-switch v-if="scope.row.inlineEditting" v-model="scope.row.mobileHidden" />
          <cg-icon v-else :color="scope.row.mobileHidden?'#46a6ff':'grey'" :icon="scope.row.mobileHidden?'fa fa-check-circle':'fa fa-circle-o'"/>
        </template>

      </cg-table-column>
      <cg-table-column prop="action" :page="1" :label="$t('sysMenu.field.action')" sortable align="left" >
        <template slot-scope="scope">
          <el-input v-if="scope.row.inlineEditting" v-model="scope.row.action" type="text" />
          <span v-else>{{ scope.row.action }}</span>
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
        <cg-select v-model="queryRecord.searchFields" dictionary="name|sysMenu.field.name,action|sysMenu.field.action," multiple/>
      </el-form-item>
      <el-divider />
      <div v-show="!queryRecord.search">
        <el-form-item :label="$t('sysMenu.field.name')" prop="name" :size="$store.state.app.size">
          <el-input v-model="queryRecord.name" type="text" name="name"
                    :readonly="fixedQueryRecord.name?true:false" :label="$t('sysMenu.field.name')" clearable resize autofocus/>
        </el-form-item>
        <el-form-item :label="$t('sysMenu.field.action')" prop="action" :size="$store.state.app.size">
          <el-input v-model="queryRecord.action" type="text" name="action"
                    :readonly="fixedQueryRecord.action?true:false" :label="$t('sysMenu.field.action')" clearable resize autofocus/>
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
  name: 'CgListMenu',
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
      defaultOrder: 'sort_num',
      queryRecordFields: ['name','action'],
      formPath: '/framework/sysMenu/record',
      parentField: 'parent',
      idField: 'id',
      dictionary: {
        dictParent: []
      },
      needLoadDictionary: true,
      totalEdittingRows: 0,
      editInlineFields: hasAuthority('/framework/sysMenu/updateSelective')?['name', 'sortNum', 'isDivider', 'mobileHidden', 'action']:null,
      listName: 'menu',
      multipleSelection: true,
      generatorName: 'sysMenu',
      baseUrl: '/framework/sysMenu'
    }
  },
  computed: {
    allActions() {
      if (this.joinMode) return 'refresh,query'
      else return 'query,,list,add,view,edit,delete,batdel,import,export,'
    }
  },
  mounted() {
    this.cgList.list_tableInit(this,'sortNum')
  },
  methods: {
    initialQueryRecord() {
      return Object.assign({
        parent: null,
        name: null,
        isDivider: null,
        mobileHidden: null,
        action: null,
      }, this.fixedQueryRecord)
    },
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /CgListMenu-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
  mixins
}
</script>
