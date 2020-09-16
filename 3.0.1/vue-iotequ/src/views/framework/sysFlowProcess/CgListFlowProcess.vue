
<template>
  <div :class="joinMode?'cg-join-list':'cg-list'">
    <el-backtop v-if="isTableMode()" ref="cgListBacktop" class="el-backtop" target=".cg-list-flowprocess .el-table__body-wrapper" :visibility-height="200">
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
      <cg-table-column prop="operation" :page="1" :label="$t('sysFlowProcess.field.operation')" align="left" >
        <template slot-scope="scope">
          {{ scope.row.operation }}
        </template>

      </cg-table-column>
      <cg-table-column prop="selection" :page="1" :label="$t('sysFlowProcess.field.selection')" align="left" >
        <template slot-scope="scope">
          {{ scope.row.selection }}
        </template>

      </cg-table-column>
      <cg-table-column prop="stateName0" :page="1" :label="$t('sysFlowProcess.field.stateName0')" align="left" >
        <template slot-scope="scope">
          {{ scope.row.stateName0 }}
        </template>

      </cg-table-column>
      <cg-table-column prop="stateName1" :page="1" :label="$t('sysFlowProcess.field.stateName1')" align="left" >
        <template slot-scope="scope">
          {{ scope.row.stateName1 }}
        </template>

      </cg-table-column>
      <cg-table-column prop="operator" :page="1" :label="$t('sysFlowProcess.field.operator')" align="left" >
        <template slot-scope="scope">
          {{ scope.row.operator }}
        </template>

      </cg-table-column>
      <cg-table-column prop="time" type="datetime" :page="1" :label="$t('sysFlowProcess.field.time')" align="center" >
        <template slot-scope="scope">
          {{ time2String(scope.row.time,'yyyy-MM-dd') }}
        </template>

      </cg-table-column>
      <cg-table-column prop="nextOperator" :page="1" :label="$t('sysFlowProcess.field.nextOperator')" align="left" >
        <template slot-scope="scope">
          {{ scope.row.nextOperator }}
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
import ParentTable from '@/views/common-views/components/table'
const mixins = [ParentTable]
const mixinContext = require.context('.', false, /CgListFlowProcess-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default {
  name: 'CgListFlowProcess',
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
      defaultOrder: 'flow_id,time desc',
      queryRecordFields: [],
      formPath: '/framework/sysFlowProcess/record',
      idField: 'id',
      listName: 'flowProcess',
      generatorName: 'sysFlowProcess',
      baseUrl: '/framework/sysFlowProcess'
    }
  },
  computed: {
    allActions() {
      if (this.joinMode) return 'refresh'
      else return ',list,view,'
    }
  },
  mounted() {
    this.cgList.list_tableInit(this)
  },
  methods: {
  }
}
</script>
