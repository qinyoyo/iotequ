
<template>
  <div :class="joinMode?'cg-join-list':'cg-list'">
    <el-backtop v-if="isTableMode()" ref="cgListBacktop" class="el-backtop" target=".cg-list-pmpeople .el-table__body-wrapper" :visibility-height="200">
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
      <cg-table-column prop="realName" :page="1" :label="$t('pmPeople.field.userId')" align="left" >
        <template slot-scope="scope">
          {{ scope.row.realName }}
        </template>

      </cg-table-column>
      <cg-table-column prop="sex" type="dict" :page="1" :label="$t('sysUser.field.sex')" align="left" >
        <template slot-scope="scope">
          {{ dictValue(scope.row.sex,dictionary.dictSex,false,true) }}
        </template>

      </cg-table-column>
      <cg-table-column prop="orgCode" type="dict" :page="1" :label="$t('sysUser.field.orgCode')" align="left" >
        <template slot-scope="scope">
          {{ dictValue(scope.row.orgCode,dictionary.dictOrgCode,true,true) }}
        </template>

      </cg-table-column>
      <cg-table-column prop="mobilePhone" :page="1" :label="$t('sysUser.field.mobilePhone')" align="left" >
        <template slot-scope="scope">
          {{ scope.row.mobilePhone }}
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
const mixinContext = require.context('.', false, /CgListPmPeople-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default {
  name: 'CgListPmPeople',
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
      title: this.$t('pmPeople.title.list'),
      showQuery: false,
      showActionView: false,
      defaultOrder: 'group_id,user_id',
      queryRecord: this.initialQueryRecord(),
      queryRecordFields: [],
      formPath: '/project/people/pmPeople/record',
      listLoading: false,
      rows: [],
      selections: this.originSelections,
      localExport: false,
      parentField: null,
      idField: 'id',
			dictionary: {
			  dictSex: [],
			  dictOrgCode: []
		  },
      needLoadDictionary: true,
      paginationPageSize: 0,
      sortableFields: [],
      sortableFieldsOrder: [],
      editInlineFields: null,
      contextMenu: { top: 0, left: 0, visible: false, row: null, actions: [], trElement: null },
      generatorName: 'pmPeople',
      baseUrl: '/project/people/pmPeople'
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
      return this.openID ? 'cg-list-pmpeople cg-list-pmpeople' + '-'+this.openID : 'cg-list-pmpeople'
    },
    allActions() {
      if (this.joinMode) return 'refresh'
      else return ',list,add,view,delete,batdel,import,export,new,'
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
        groupId: null,
        userId: null,
        realName: null,
        sex: null,
        orgCode: null,
        mobilePhone: null,
      }, this.fixedQueryRecord)
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
