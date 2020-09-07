
<template>
  <div :class="joinMode?'cg-join-list':'cg-list'">
    <el-backtop v-if="isTableMode()" ref="cgListBacktop" class="el-backtop" target=".cg-list-adorg .el-table__body-wrapper" :visibility-height="200">
      <i class="el-icon-caret-top" />
    </el-backtop>
    <el-table ref="cgList" v-if="isTableMode()" v-loading="listLoading" :data="rows" :class="className" row-key="orgCode" :row-class-name="rowClassName" 
              style="width: 100%" :height="tableHeight()" :size="$store.state.app.size" 
              v-set-input:no-tab-index="{tabIndex: -1}" v-table-enter-tab stripe :border="!mobile" highlight-current-row fit 
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
      <el-table-column prop="name" width="200" :label="$t('sysOrg.field.name')" align="left" >
        <template slot-scope="scope">
          {{ scope.row.name }}
        </template>

      </el-table-column>
      <cg-table-column prop="shiftId" type="dict" :page="1" :label="$t('adOrg.field.shiftId')" align="left" >
        <template slot-scope="scope">
          <cg-select v-if="scope.row.inlineEditting" v-model="scope.row.shiftId" automaticDropdown appendToBody :dictionary="dictionary.dictShiftId" allow-create numberic />
          <span v-else>{{ dictValue(scope.row.shiftId,dictionary.dictShiftId,false,true) }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="manageLimit" :page="1" :label="$t('adOrg.field.manageLimit')" align="right" >
        <template slot-scope="scope">
          <el-input v-if="scope.row.inlineEditting" v-model="scope.row.manageLimit" type="text" />
          <span v-else>{{ scope.row.manageLimit }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="deviation" :page="1" :label="$t('adOrg.field.deviation')" align="right" >
        <template slot-scope="scope">
          <el-input v-if="scope.row.inlineEditting" v-model="scope.row.deviation" type="text" />
          <span v-else>{{ scope.row.deviation }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="floatLimit" :page="1" :label="$t('adOrg.field.floatLimit')" align="right" >
        <template slot-scope="scope">
          <el-input v-if="scope.row.inlineEditting" v-model="scope.row.floatLimit" type="text" />
          <span v-else>{{ scope.row.floatLimit }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="absentLimit" :page="1" :label="$t('adOrg.field.absentLimit')" align="right" >
        <template slot-scope="scope">
          <el-input v-if="scope.row.inlineEditting" v-model="scope.row.absentLimit" type="text" />
          <span v-else>{{ scope.row.absentLimit }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="freeWorkLimit" :page="1" :label="$t('adOrg.field.freeWorkLimit')" align="right" >
        <template slot-scope="scope">
          <el-input v-if="scope.row.inlineEditting" v-model="scope.row.freeWorkLimit" type="text" />
          <span v-else>{{ scope.row.freeWorkLimit }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="managerName" :page="1" :label="$t('adOrg.field.manager')" align="left" >
        <template slot-scope="scope">
          {{ scope.row.managerName }}
        </template>

      </cg-table-column>
      <cg-table-column prop="hrName" :page="1" :label="$t('adOrg.field.hr')" align="left" >
        <template slot-scope="scope">
          {{ scope.row.hrName }}
        </template>

      </cg-table-column>
      <cg-action v-model="showActionView" mode="2" :url="baseUrl" :actions="list_allActions(myself,'main')" @actionClick="doAction" />
    </el-table>
    <cg-card-list v-else ref="cgList" v-loading="listLoading" :render="rowRender" :data="rows" :mainClass="className" row-key="orgCode" :multiple="false" :height="tableHeight()" 
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
import CgListOrg from '@/views/framework/sysOrg/CgListOrg.vue'
import CgListUserJoin from '@/views/framework/sysUser/CgListUserJoin.vue'
import rulesObject from './rules.js'
const mixins = []
const mixinContext = require.context('.', false, /CgListAdOrg-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default {
  name: 'CgListAdOrg',
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
      default: 'orgCode'
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
  components: { CgListOrg, CgListUserJoin },
  data() {
    return {
      cgList,
      myself: this,
      rulesObject,
      path: 'list',
      title: this.$t('adOrg.title.list'),
      showQuery: false,
      showActionView: false,
      defaultOrder: 'org_code',
      queryRecord: this.initialQueryRecord(),
      queryRecordFields: [],
      formPath: '/attendance/org/adOrg/record',
      listLoading: false,
      rows: [],
      selections: this.originSelections,
      localExport: true,
      removeLeftRecordOnRightJoin: true,
      parentField: 'parent',
      idField: 'orgCode',
			dictionary: {
			  dictParent: [],
			  dictShiftId: [],
			  dictManagerOrgCode: []
		  },
      needLoadDictionary: true,
      orgCodeJoinVisible: false,
      managerJoinVisible: false,
      hrJoinVisible: false,
      paginationPageSize: 0,
      sortableFields: [],
      sortableFieldsOrder: [],
      totalEdittingRows: 0,
      editInlineFields: hasAuthority('/attendance/org/adOrg/updateSelective')?['shiftId', 'manageLimit', 'deviation', 'floatLimit', 'absentLimit', 'freeWorkLimit']:null,
      hasSonTables: true,
      contextMenu: { top: 0, left: 0, visible: false, row: null, actions: [], trElement: null },
      generatorName: 'adOrg',
      baseUrl: '/attendance/org/adOrg'
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
      return this.openID ? 'cg-list-adorg cg-list-adorg' + '-'+this.openID : 'cg-list-adorg'
    },
    allActions() {
      if (this.joinMode) return 'refresh'
      else return ',list,view,edit,import,localExport,delete,'
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
        orgCode: null,
        name: null,
        parent: null,
        shiftId: null,
        managerName: null,
        hrName: null,
        hr: null,
        manager: null,
        managerOrgCode: null,
      }, this.fixedQueryRecord)
    },
    getJoinFields(field,rows) {
      const joinDefine = {
        orgCode: {
          valueField: 'orgCode',
          fields: 'sysOrgCode=orgCode,name=name,parent=parent'
        },
        manager: {
          valueField: 'id',
          fields: 'managerName=realName,managerOrgCode=orgCode'
        },
        hr: {
          valueField: 'id',
          fields: 'hrName=realName'
        },
      }
      this[field+'JoinVisible'] = false
      this.setJoinValues(this.queryRecord, field, joinDefine[field], rows)
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
