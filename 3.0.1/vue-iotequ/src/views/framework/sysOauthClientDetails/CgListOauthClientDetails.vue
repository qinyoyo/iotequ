
<template>
  <div :class="joinMode?'cg-join-list':'cg-list'">
    <el-backtop v-if="isTableMode()" ref="cgListBacktop" class="el-backtop" target=".cg-list-oauthclientdetails .el-table__body-wrapper" :visibility-height="200">
      <i class="el-icon-caret-top" />
    </el-backtop>
    <el-table ref="cgList" v-if="isTableMode()" v-loading="listLoading" :data="rows" :class="className" row-key="clientId" :row-class-name="rowClassName" 
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
      <cg-table-column prop="authorizedGrantTypes" type="dict" :page="paginationCurrentPage" :label="$t('sysOauthClientDetails.field.authorizedGrantTypes')" align="left" >
        <template slot-scope="scope">
          <cg-select v-if="scope.row.inlineEditting" v-model="scope.row.authorizedGrantTypes" automaticDropdown appendToBody :dictionary="dictionary.dictAuthorizedGrantTypes" allow-create numberic />
          <span v-else>{{ dictValue(scope.row.authorizedGrantTypes,dictionary.dictAuthorizedGrantTypes,false,true) }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="clientId" :page="paginationCurrentPage" :label="$t('sysOauthClientDetails.field.clientId')" align="left" >
        <template slot-scope="scope">
          <el-input v-if="scope.row.inlineEditting" v-model="scope.row.clientId" type="text" />
          <span v-else>{{ scope.row.clientId }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="secret" :page="paginationCurrentPage" :label="$t('sysOauthClientDetails.field.secret')" align="left" >
        <template slot-scope="scope">
          {{ scope.row.secret }}
        </template>

      </cg-table-column>
      <cg-table-column prop="scope" :page="paginationCurrentPage" :label="$t('sysOauthClientDetails.field.scope')" align="left" >
        <template slot-scope="scope">
          <el-input v-if="scope.row.inlineEditting" v-model="scope.row.scope" type="text" />
          <span v-else>{{ scope.row.scope }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="authorities" type="dict" :page="paginationCurrentPage" :label="$t('sysOauthClientDetails.field.authorities')" align="left" >
        <template slot-scope="scope">
          <cg-select v-if="scope.row.inlineEditting" v-model="scope.row.authorities" automaticDropdown appendToBody :dictionary="dictionary.dictAuthorities" allow-create multiple />
          <span v-else>{{ dictValue(scope.row.authorities,dictionary.dictAuthorities,false,true) }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="accessTokenValidity" :page="paginationCurrentPage" :label="$t('sysOauthClientDetails.field.accessTokenValidity')" align="left" >
        <template slot-scope="scope">
          {{ scope.row.accessTokenValidity }}
        </template>

      </cg-table-column>
      <cg-table-column prop="webServerRedirectUri" :page="paginationCurrentPage" :label="$t('sysOauthClientDetails.field.webServerRedirectUri')" align="left" >
        <template slot-scope="scope">
          {{ scope.row.webServerRedirectUri }}
        </template>

      </cg-table-column>
      <cg-table-column prop="decription" :page="paginationCurrentPage" :label="$t('sysOauthClientDetails.field.decription')" align="left" >
        <template slot-scope="scope">
          <el-input v-if="scope.row.inlineEditting" v-model="scope.row.decription" type="text" />
          <span v-else>{{ scope.row.decription }}</span>
        </template>

      </cg-table-column>
      <cg-action v-model="showActionView" mode="2" :url="baseUrl" :actions="list_allActions(myself,'main')" @actionClick="doAction" />
    </el-table>
    <cg-card-list v-else ref="cgList" v-loading="listLoading" :render="rowRender" :data="rows" :mainClass="className" row-key="clientId" :multiple="false" :height="tableHeight()" 
                  :isLoading="listLoading" 
                  :isUnMore="paginationCurrentPage * paginationPageSize >= paginationTotalRecords" :cgList="myself"
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
    <nut-scroller v-if="mobile && isTableMode()"
                  wrapperElement=".cg-list-oauthclientdetails .el-table__body-wrapper"
                  :isLoading="listLoading"
                  :isUnMore="paginationCurrentPage * paginationPageSize >= paginationTotalRecords"
                  type="vertical"
                  :pulldownTxt="$t('system.message.pullDownRefresh')"
                  :loadMoreTxt="$t('system.message.pullUpLoad')"
                  :unloadMoreTxt="$t('system.message.noMoreData')"
                  @loadMore="list_loadMore(myself)"
                  @pulldown="doAction('refresh',{ isPullDownEvent : true})"
    />
    <el-pagination v-if="!mobile" @size-change="doAction('refresh')" @current-change="doAction('refresh')" :page-sizes="[10, 20, 30, 50, 100, 200]" layout="total, sizes, prev, pager, next, jumper"
      :current-page.sync="paginationCurrentPage" :page-size.sync="paginationPageSize" :total="paginationTotalRecords">
    </el-pagination>
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
const mixinContext = require.context('.', false, /CgListOauthClientDetails-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default {
  name: 'CgListOauthClientDetails',
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
      default: 'clientId'
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
      title: this.$t('sysOauthClientDetails.title.list'),
      showQuery: false,
      showActionView: false,
      defaultOrder: 'client_id desc',
      queryRecord: this.initialQueryRecord(),
      formPath: '/framework/sysOauthClientDetails/record',
      listLoading: false,
      rows: [],
      selections: this.originSelections,
      localExport: false,
      parentField: null,
      idField: 'clientId',
			dictionary: {
			  dictAutoapprove: this.getDictionary('true,false'),
			  dictAuthorizedGrantTypes: this.getDictionary('password,authorization_code,implicit,client_credentials','密码模式,授权码模式,简化模式,客户端模式'),
			  dictAuthorities: []
		  },
      needLoadDictionary: true,
      paginationCurrentPage: 1,
      paginationPageSize: this.$store.state.app.device === 'mobile' ? 10 : 30,
      paginationTotalRecords: 0,
      sortableFields: [],
      sortableFieldsOrder: [],
      totalEdittingRows: 0,
      editInlineFields: hasAuthority('/framework/sysOauthClientDetails/updateSelective')?['authorizedGrantTypes', 'clientId', 'scope', 'authorities', 'decription']:null,
      contextMenu: { top: 0, left: 0, visible: false, row: null, actions: [], trElement: null },
      generatorName: 'sysOauthClientDetails',
      baseUrl: '/framework/sysOauthClientDetails'
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
      return this.openID ? 'cg-list-oauthclientdetails cg-list-oauthclientdetails' + '-'+this.openID : 'cg-list-oauthclientdetails'
    },
    allActions() {
      if (this.joinMode) return 'refresh'
      else return ',list,add,view,edit,delete,batdel,import,export,'
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
      if (this.height > 0) return this.height - (this.mobile ? 0 : 40)
      else return (this.containerHeight() - (this.mobile ? 0 : 40))
    },
    initialQueryRecord() {
      return Object.assign({
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
