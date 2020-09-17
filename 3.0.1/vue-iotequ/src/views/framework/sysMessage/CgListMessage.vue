
<template>
  <div :class="joinMode?'cg-join-list':'cg-list'">
    <el-backtop v-if="isTableMode()" ref="cgListBacktop" class="el-backtop" target=".cg-list-message .el-table__body-wrapper" :visibility-height="200">
      <i class="el-icon-caret-top" />
    </el-backtop>
    <el-table ref="cgList" v-if="isTableMode()" v-loading="listLoading" :data="rows" :class="className" row-key="id" :row-class-name="rowClassName" 
			  @row-dblclick="(row, event)=>rowDblclick(row, event)" 
              style="width: 100%" :height="tableHeight()" :size="$store.state.app.size" 
              stripe :border="!mobile" highlight-current-row fit 
              @row-click="(row, column, event)=>cgList.list_rowClick(myself,{ row, column, event })" 
              @row-contextmenu="(row, column, event)=>cgList.list_rowContextmenu(myself,{ row, column, event })" 
              @header-click="(column, event)=>cgList.list_headClick(myself,{ column, event })" 
              
              @cell-click="(row, column, cell, event)=>cgList.list_cellClick(myself,{ row, column, cell, event })" 
              @selection-change="(selection)=>cgList.list_selectionChange(myself, selection)" 
              @current-change="(selection)=>cgList.list_selectionChange(myself, selection)" 
    >
      <cg-icon slot="empty" icon="el-icon-minus" color="grey" />
      <el-table-column v-if="!mobile" type="index" width="50" align="center" class-name="drag-filter" label-class-name="pointer-cursor" header-align="center">
        <i slot="header" class="el-icon-menu"/>
      </el-table-column>
      <el-table-column v-if="multiple" type="selection" align="center" reserve-selection class-name="drag-filter" width="36" />
      <cg-table-column prop="readTime" :page="paginationCurrentPage" :label="$t('sysMessage.field.readTime')" sortable align="center" >
        <template slot-scope="scope">
          <cg-icon color="#46a6ff" :icon="scope.row.readTime?'fa fa-check-circle':'fa fa-circle-o'"/>
        </template>

      </cg-table-column>
      <cg-table-column prop="createTime" type="datetime" :page="paginationCurrentPage" :label="$t('sysMessage.field.createTime')" sortable align="center" >
        <template slot-scope="scope">
          {{ time2String(scope.row.createTime,'YYYY-MM-DD HH:mm') }}
        </template>

      </cg-table-column>
      <cg-table-column prop="senderName" :page="paginationCurrentPage" :label="$t('sysMessage.field.senderName')" sortable align="left" >
        <template slot-scope="scope">
          {{ scope.row.senderName }}
        </template>

      </cg-table-column>
      <cg-table-column prop="title" :page="paginationCurrentPage" :label="$t('sysMessage.field.title')" sortable align="left" >
        <template slot-scope="scope">
          {{ scope.row.title }}
        </template>

      </cg-table-column>
      <cg-table-column prop="content" :page="paginationCurrentPage" :label="$t('sysMessage.field.content')" sortable align="left" >
        <template slot-scope="scope">
          {{ scope.row.content }}
        </template>

      </cg-table-column>
      <cg-action v-model="showActionView" mode="2" :url="baseUrl" :actions="cgList.list_allActions(myself,'main')" @actionClick="doAction" />
    </el-table>
    <cg-card-list v-else ref="cgList" v-loading="listLoading" :render="rowRender" :data="rows" :mainClass="className" row-key="id" :multiple="false" :height="tableHeight()" 
                  :isLoading="listLoading" 
                  :isUnMore="paginationCurrentPage * paginationPageSize >= paginationTotalRecords" :cgList="myself"
                  @row-dblclick="(row, event)=>rowDblclick(row, event)" 
                  @doAction="(a,row)=>doAction(a,{row})"
                  @loadMore="cgList.list_loadMore(myself)"
                  @refresh="doAction('refresh',{ isPullDownEvent : true})"
                  @row-click="(row, event)=>cgList.list_rowClick(myself,{ row, event })" 
                  @row-contextmenu="(row, event)=>cgList.list_rowContextmenu(myself,{ row, event })" 
                  
                  @selection-change="(selection)=>cgList.list_selectionChange(myself, selection)" 
                  @current-change="(selection)=>cgList.list_selectionChange(myself, selection)" 
    >
      <template slot="append">
	      <cg-action v-model="showActionView" mode="2" :url="baseUrl" :actions="cgList.list_allActions(myself,'main')" @actionClick="doAction" />
      </template>
    </cg-card-list>
    <nut-scroller v-if="mobile && isTableMode()"
                  wrapperElement=".cg-list-message .el-table__body-wrapper"
                  :isLoading="listLoading"
                  :isUnMore="paginationCurrentPage * paginationPageSize >= paginationTotalRecords"
                  type="vertical"
                  :pulldownTxt="$t('system.message.pullDownRefresh')"
                  :loadMoreTxt="$t('system.message.pullUpLoad')"
                  :unloadMoreTxt="$t('system.message.noMoreData')"
                  @loadMore="cgList.list_loadMore(myself)"
                  @pulldown="doAction('refresh',{ isPullDownEvent : true})"
    />
    <el-pagination v-if="!mobile" @size-change="doAction('refresh')" @current-change="doAction('refresh')" :page-sizes="[10, 20, 30, 50, 100, 200]" layout="total, sizes, prev, pager, next, jumper"
      :current-page.sync="paginationCurrentPage" :page-size.sync="paginationPageSize" :total="paginationTotalRecords">
    </el-pagination>
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
        <cg-select v-model="queryRecord.searchFields" dictionary="readTime|sysMessage.field.readTime,createTime|sysMessage.field.createTime,senderName|sysMessage.field.senderName,title|sysMessage.field.title,content|sysMessage.field.content," multiple/>
      </el-form-item>
      <el-divider />
      <div v-show="!queryRecord.search">
        <el-form-item :label="$t('sysMessage.field.readTime')" prop="readTime" :size="$store.state.app.size">
            <el-checkbox-group v-model="queryRecord.readTime" :max="1">
              <el-checkbox name="readTime" label="is not null" :disabled="fixedQueryRecord.readTime?true:false">{{ $t('system.action.yes') }}</el-checkbox>
              <el-checkbox name="readTime" label="is null" :disabled="fixedQueryRecord.readTime?true:false">{{ $t('system.action.no') }}</el-checkbox>
            </el-checkbox-group>
        </el-form-item>
        <el-form-item :label="$t('sysMessage.field.createTime')" prop="createTime" :size="$store.state.app.size">
          <cg-date-picker v-model="queryRecord.createTime" :title="$t('sysMessage.field.createTime')" name="createTime" :align="mobile?'right':'center'" type="datetimerange" :picker-options="datePickerOptions()"
                          :readonly="fixedQueryRecord.createTime?true:false"  clearable />
        </el-form-item>
        <el-form-item :label="$t('sysMessage.field.senderName')" prop="senderName" :size="$store.state.app.size">
          <el-input v-model="queryRecord.senderName" type="text" name="senderName"
                    :readonly="fixedQueryRecord.senderName?true:false" :label="$t('sysMessage.field.senderName')" clearable resize autofocus/>
        </el-form-item>
        <el-form-item :label="$t('sysMessage.field.title')" prop="title" :size="$store.state.app.size">
          <el-input v-model="queryRecord.title" type="text" name="title"
                    :readonly="fixedQueryRecord.title?true:false" :label="$t('sysMessage.field.title')" clearable resize autofocus/>
        </el-form-item>
        <el-form-item :label="$t('sysMessage.field.content')" prop="content" :size="$store.state.app.size">
          <el-input v-model="queryRecord.content" type="textarea" name="content" :label="$t('sysMessage.field.content')" clearable
                    :readonly="fixedQueryRecord.content?true:false" resize :autosize="{ minRows: 1, maxRows: 10 }" autofocus/>
            
        </el-form-item>
      </div>
    </cg-query-condition>
  </div>
</template>

<script>
import {hasAuthority} from '@/utils/cg'
import ParentTable from '@/views/common-views/components/table'
const Comp = {
  name: 'CgListMessage',
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
      defaultOrder: 'create_time desc',
      queryRecordFields: ['readTime','createTime','senderName','title','content'],
      formPath: '/framework/sysMessage/record',
      idField: 'id',
      paginationCurrentPage: 1,
      paginationPageSize: this.$store.state.app.device === 'mobile' ? 10 : 30,
      paginationTotalRecords: 0,
      listName: 'message',
      multipleSelection: true,
      generatorName: 'sysMessage',
      baseUrl: '/framework/sysMessage'
    }
  },
  computed: {
    additionalActions() {
      if (this.joinMode) return []
      else return [
        {
          action: 'process',
          icon: 'fa fa-hand-o-left',
          title: 'sysMessage.action.process',
          groupid: 10,
          confirm: '',
          rowProperty: 'sr',
          actionProperty: 'js',
          displayProperties: 'tb,rw,ed',
          appendClass: 'this.process()',
          needRefresh: false
        },
        {
          action: 'read',
          icon: 'fa fa-check-square-o',
          title: 'sysMessage.action.read',
          groupid: 10,
          confirm: '',
          rowProperty: 'sr',
          actionProperty: 'pg',
          displayProperties: 'tb,rw',
          appendClass: 'this.read()',
          needRefresh: false
        },
        {
          action: 'readAll',
          icon: 'fa fa-check',
          title: 'sysMessage.action.readAll',
          groupid: 10,
          confirm: 'sysMessage.action.readAllConfirm',
          rowProperty: 'nr',
          actionProperty: 'aj',
          displayProperties: 'tb',
          appendClass: '',
          needRefresh: true
        }
      ]
    },
    allActions() {
      if (this.joinMode) return 'refresh,query'
      else return 'query,,list,delete,batdel,readAll,view,process,read,'
    }
  },
  mounted() {
    this.cgList.list_tableInit(this)
  },
  methods: {
    initialQueryRecord() {
      return Object.assign({
        readTime: [],
        createTime: null,
        senderName: null,
        title: null,
        content: null,
      }, this.fixedQueryRecord)
    },
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /CgListMessage-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
  mixins
}
</script>
