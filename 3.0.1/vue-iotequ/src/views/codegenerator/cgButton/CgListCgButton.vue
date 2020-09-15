
<template>
  <div :class="joinMode?'cg-join-list':'cg-list'">
    <el-backtop v-if="isTableMode()" ref="cgListBacktop" class="el-backtop" target=".cg-list-cgbutton .el-table__body-wrapper" :visibility-height="200">
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
      <cg-table-column prop="action" :page="1" :label="$t('cgButton.field.action')" align="left" >
        <template slot-scope="scope">
          <el-input v-if="scope.row.inlineEditting" v-model="scope.row.action" type="text" />
          <span v-else>{{ scope.row.action }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="title" :page="1" :label="$t('cgButton.field.title')" align="left" >
        <template slot-scope="scope">
          <el-input v-if="scope.row.inlineEditting" v-model="scope.row.title" type="text" />
          <span v-else>{{ scope.row.title }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="icon" :page="1" :label="$t('cgButton.field.icon')" align="left" >
        <template slot-scope="scope">
          <el-input v-if="scope.row.inlineEditting" v-model="scope.row.icon" type="text" />
          <span v-else>{{ scope.row.icon }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="appendClass" :page="1" :label="$t('cgButton.field.appendClass')" align="left" >
        <template slot-scope="scope">
          <el-input v-if="scope.row.inlineEditting" v-model="scope.row.appendClass" type="text" />
          <span v-else>{{ scope.row.appendClass }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="actionProperty" type="dict" :page="1" :label="$t('cgButton.field.actionProperty')" align="left" >
        <template slot-scope="scope">
          <cg-select v-if="scope.row.inlineEditting" v-model="scope.row.actionProperty" automaticDropdown appendToBody :dictionary="dictionary.dictActionProperty" />
          <span v-else>{{ dictValue(scope.row.actionProperty,dictionary.dictActionProperty,false,true) }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="rowProperty" type="dict" :page="1" :label="$t('cgButton.field.rowProperty')" align="left" >
        <template slot-scope="scope">
          <cg-select v-if="scope.row.inlineEditting" v-model="scope.row.rowProperty" automaticDropdown appendToBody :dictionary="dictionary.dictRowProperty" />
          <span v-else>{{ dictValue(scope.row.rowProperty,dictionary.dictRowProperty,false,true) }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="displayProperties" type="dict" :page="1" :label="$t('cgButton.field.displayProperties')" align="left" >
        <template slot-scope="scope">
          <cg-select v-if="scope.row.inlineEditting" v-model="scope.row.displayProperties" automaticDropdown appendToBody :dictionary="dictionary.dictDisplayProperties" multiple />
          <span v-else>{{ dictValue(scope.row.displayProperties,dictionary.dictDisplayProperties,false,true) }}</span>
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
  </div>
</template>

<script>
import {hasAuthority} from '@/utils/cg'
import rulesObject from './rules.js'
import ParentTable from '@/views/common-views/components/table'
const mixins = [ParentTable]
const mixinContext = require.context('.', false, /CgListCgButton-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default {
  name: 'CgListCgButton',
  mixins,
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
      defaultOrder: 'order_num',
      queryRecordFields: [],
      formPath: '/codegenerator/cgButton/record',
      idField: 'id',
      dictionary: {
        dictActionProperty: this.getDictionary('js,go,pg,aj','仅前端函数,页面跳转,自定义函数调用后端,仅后端操作'),
        dictRowProperty: this.getDictionary('sr,mr,nr','单行,多行,行无关'),
        dictDisplayProperties: this.getDictionary('hm,hp,tb,rw,ed,gs','手机隐藏,PC隐藏,工具条显示,行显示,编辑显示,分组开始')
      },
      totalEdittingRows: 0,
      editInlineFields: hasAuthority('/codegenerator/cgButton/updateSelective')?['action', 'title', 'icon', 'appendClass', 'actionProperty', 'rowProperty', 'displayProperties']:null,
      listName: 'cgButton',
      multipleSelection: true,
      generatorName: 'cgButton',
      baseUrl: '/codegenerator/cgButton'
    }
  },
  computed: {
    allActions() {
      if (this.joinMode) return 'refresh'
      else return ',list,add,view,edit,delete,batdel,import,export,'
    }
  },
  mounted() {
    this.cgList.list_tableInit(this,'orderNum')
  },
  methods: {
    initialQueryRecord() {
      return Object.assign({
        action: null,
        title: null,
        icon: null,
        appendClass: null,
        actionProperty: null,
        rowProperty: null,
        displayProperties: null,
      }, this.fixedQueryRecord)
    },
  }
}
</script>
