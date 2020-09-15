
<template>
  <div :class="joinMode?'cg-join-list':'cg-list'">
    <el-backtop v-if="isTableMode()" ref="cgListBacktop" class="el-backtop" target=".cg-list-cgproject .el-table__body-wrapper" :visibility-height="200">
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
      <el-table-column prop="name" width="120" :label="$t('cgProject.field.name')" align="left" >
        <template slot-scope="scope">
          {{ scope.row.name }}
        </template>

      </el-table-column>
      <el-table-column prop="groupId" width="120" :label="$t('cgProject.field.groupId')" align="left" >
        <template slot-scope="scope">
          {{ scope.row.groupId }}
        </template>

      </el-table-column>
      <el-table-column prop="note" width="500" :label="$t('cgProject.field.note')" align="left" >
        <template slot-scope="scope">
          {{ scope.row.note }}
        </template>

      </el-table-column>
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
import ParentTable from '@/views/common-views/components/table'
const mixins = [ParentTable]
const mixinContext = require.context('.', false, /CgListCgProject-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default {
  name: 'CgListCgProject',
  mixins,
  props: {
    selectionKey: {
      type: String,
      default: 'id'
    }
  },
  data() {
    return {
      path: 'list',
      defaultOrder: 'id desc',
      queryRecordFields: [],
      formPath: '/codegenerator/cgProject/record',
      idField: 'id',
      dictionary: {
        dictModules: []
      },
      needLoadDictionary: true,
      hasSonTables: true,
      listName: 'cgProject',
      generatorName: 'cgProject',
      baseUrl: '/codegenerator/cgProject'
    }
  },
  computed: {
    additionalActions() {
      if (this.joinMode) return []
      else return [
        {
          action: 'build',
          icon: 'fa fa-magic fa-fw',
          title: 'cgProject.action.build',
          groupid: 10,
          confirm: '',
          rowProperty: 'mr',
          actionProperty: 'aj',
          displayProperties: 'hm,tb',
          appendClass: '',
          needRefresh: false
        },
        {
          action: 'down',
          icon: 'fa fa-cloud-download fa-fw',
          title: 'cgProject.action.down',
          groupid: 10,
          confirm: '',
          rowProperty: 'nr',
          actionProperty: 'aj',
          displayProperties: 'hm,tb',
          appendClass: '{request: {timeout: 20000,responseType: "blob"}, fileName: "cg.zip"}',
          needRefresh: false
        }
      ]
    },
    allActions() {
      if (this.joinMode) return 'refresh'
      else return ',add,list,view,edit,delete,batdel,import,export,build,down,'
    }
  },
  mounted() {
    this.cgList.list_tableInit(this)
  },
  methods: {
    initialQueryRecord() {
      return Object.assign({
        modules: null,
        id: null,
        code: null,
        name: null,
        groupId: null,
        note: null,
      }, this.fixedQueryRecord)
    },
  }
}
</script>
