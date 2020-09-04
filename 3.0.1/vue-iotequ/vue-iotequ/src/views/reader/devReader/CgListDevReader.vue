
<template>
  <div :class="joinMode?'cg-join-list':'cg-list'">
    <el-backtop v-if="isTableMode()" ref="cgListBacktop" class="el-backtop" target=".cg-list-devreader .el-table__body-wrapper" :visibility-height="200">
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
      <cg-table-column prop="name" :page="1" :label="$t('devReader.field.name')" align="left" >
        <template slot-scope="scope">
          {{ scope.row.name }}
        </template>

      </cg-table-column>
      <cg-table-column prop="readerNo" :page="1" :label="$t('devReader.field.readerNo')" align="left" >
        <template slot-scope="scope">
          {{ scope.row.readerNo }}
        </template>

      </cg-table-column>
      <cg-table-column prop="ip" :page="1" :label="$t('devReader.field.ip')" align="left" >
        <template slot-scope="scope">
          {{ scope.row.ip }}
        </template>

      </cg-table-column>
      <cg-table-column prop="type" type="dict" :page="1" :label="$t('devReader.field.type')" align="left" >
        <template slot-scope="scope">
          {{ dictValue(scope.row.type,dictionary.dictType,true,true) }}
        </template>

      </cg-table-column>
      <cg-table-column prop="connectType" type="dict" :page="1" :label="$t('devReader.field.connectType')" align="left" >
        <template slot-scope="scope">
          {{ dictValue(scope.row.connectType,dictionary.dictConnectType,true,true) }}
        </template>

      </cg-table-column>
      <cg-table-column prop="devMode" type="dict" :page="1" :label="$t('devReader.field.devMode')" align="left" >
        <template slot-scope="scope">
          {{ dictValue(scope.row.devMode,dictionary.dictDevMode,false,true) }}
        </template>

      </cg-table-column>
      <cg-table-column prop="isOnline" :page="1" :label="$t('devReader.field.isOnline')" align="center" >
        <template slot-scope="scope">
          <cg-icon :color="scope.row.isOnline?'#46a6ff':'grey'" :icon="scope.row.isOnline?'fa fa-check-circle':'fa fa-circle-o'"/>
        </template>

      </cg-table-column>
      <cg-table-column prop="isTimeSync" :page="1" :label="$t('devReader.field.isTimeSync')" align="center" >
        <template slot-scope="scope">
          <cg-icon :color="scope.row.isTimeSync?'#46a6ff':'grey'" :icon="scope.row.isTimeSync?'fa fa-check-circle':'fa fa-circle-o'"/>
        </template>

      </cg-table-column>
      <cg-action v-model="showActionView" mode="2" :url="baseUrl" :actions="list_allActions(myself,'main')" @actionClick="doAction" />
    </el-table>
    <cg-card-list v-else ref="cgList" v-loading="listLoading" :render="rowRender" :data="rows" :mainClass="className" row-key="id" :multiple="false" :height="tableHeight()" 
                  :isLoading="listLoading" 
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
  </div>
</template>

<script>
import cgList from '@/utils/cgList'
import cg from '@/utils/cg'
import {hasAuthority} from '@/utils/cg'
import time from '@/utils/time'
const mixins = []
const mixinContext = require.context('.', false, /CgListDevReader-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default {
  name: 'CgListDevReader',
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
      title: this.$t('devReader.title.list'),
      showQuery: false,
      showActionView: false,
      defaultOrder: 'id desc',
      queryRecord: this.initialQueryRecord(),
      formPath: '/reader/devReader/record',
      listLoading: false,
      rows: [],
      selections: this.originSelections,
      parentField: null,
      idField: 'id',
			dictionary: {
			  dictReaderGroup: [],
			  dictAlignMethod: [],
			  dictBlacklightTime: this.getDictionary('0,1,2,3,4','10s,30s,60s,90s,常亮'),
			  dictMenuTime: this.getDictionary('0,1,2,3','10s,30s,60s,90s'),
			  dictWengenform: [],
			  dictWengenOutput: [],
			  dictType: [],
			  dictConnectType: [],
			  dictDevMode: []
		  },
      needLoadDictionary: true,
      paginationPageSize: 0,
      sortableFields: [],
      sortableFieldsOrder: [],
      editInlineFields: null,
      contextMenu: { top: 0, left: 0, visible: false, row: null, actions: [], trElement: null },
      generatorName: 'devReader',
      baseUrl: '/reader/devReader'
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
      return this.openID ? 'cg-list-devreader cg-list-devreader' + '-'+this.openID : 'cg-list-devreader'
    },
    additionalActions() {
      if (this.joinMode) return []
      else return [
        {
          action: 'queryTime',
          icon: 'fa fa-clock-o  fa-fw',
          title: 'devReader.action.queryTime',
          groupid: 10,
          confirm: '',
          rowProperty: 'sr',
          actionProperty: 'aj',
          displayProperties: 'rw,gs,tb,ed',
          appendClass: '',
          needRefresh: false
        },
        {
          action: 'setDeviceTime',
          icon: 'fa fa-history  fa-fw',
          title: 'devReader.action.setDeviceTime',
          groupid: 10,
          confirm: '',
          rowProperty: 'sr',
          actionProperty: 'aj',
          displayProperties: 'rw,ed,tb',
          appendClass: '',
          needRefresh: false
        },
        {
          action: 'resetDevice',
          icon: 'fa fa-circle-o-notch  fa-fw',
          title: 'devReader.action.resetDevice',
          groupid: 10,
          confirm: 'devReader.action.resetDeviceConfirm',
          rowProperty: 'sr',
          actionProperty: 'aj',
          displayProperties: 'tb,rw,ed,hm',
          appendClass: '',
          needRefresh: false
        },
        {
          action: 'data',
          icon: 'fa fa-arrow-circle-up  fa-fw',
          title: 'devReader.action.data',
          groupid: 10,
          confirm: '',
          rowProperty: 'nr',
          actionProperty: 'go',
          displayProperties: 'tb,hm',
          appendClass: '{url:"/reader/devData/record",openMode:"add"}',
          needRefresh: false
        },
        {
          action: 'deleteAllUsers',
          icon: 'fa fa-bolt  fa-fw',
          title: 'devReader.action.deleteAllUsers',
          groupid: 10,
          confirm: 'devReader.action.deleteAllUsersConfirm',
          rowProperty: 'mr',
          actionProperty: 'aj',
          displayProperties: 'tb,hm',
          appendClass: '',
          needRefresh: false
        }
      ]
    },
    allActions() {
      if (this.joinMode) return 'refresh'
      else return ',list,add,view,edit,delete,deleteAllUsers,resetDevice,queryTime,getData,setDeviceTime,deleteSpecifyUser,download,data,'
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
      if (this.height > 0) return this.height
      else return (this.containerHeight())
    },
    initialQueryRecord() {
      return Object.assign({
      }, this.fixedQueryRecord)
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
