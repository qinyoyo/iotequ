
<template>
  <div :class="joinMode?'cg-join-list':'cg-list'">
    <el-backtop v-if="isTableMode()" ref="cgListBacktop" class="el-backtop" target=".cg-list-adexception .el-table__body-wrapper" :visibility-height="200">
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
      <cg-table-column prop="startDate" type="date" :page="1" :label="$t('adException.field.startDate')" align="center" >
        <template slot-scope="scope">
          {{ time2String(scope.row.startDate,'YYYY-MM-DD') }}
        </template>

      </cg-table-column>
      <cg-table-column prop="endDate" type="date" :page="1" :label="$t('adException.field.endDate')" align="center" >
        <template slot-scope="scope">
          {{ time2String(scope.row.endDate,'YYYY-MM-DD') }}
        </template>

      </cg-table-column>
      <cg-table-column prop="weekDay" type="dict" :page="1" :label="$t('adException.field.weekDay')" align="left" >
        <template slot-scope="scope">
          {{ dictValue(scope.row.weekDay,dictionary.dictWeekDay,false,true) }}
        </template>

      </cg-table-column>
      <cg-table-column prop="description" :page="1" :label="$t('adException.field.description')" align="left" >
        <template slot-scope="scope">
          {{ scope.row.description }}
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
const Comp = {
  name: 'CgListAdException',
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
      defaultOrder: 'start_date,week_day',
      queryRecordFields: [],
      formPath: '/attendance/exception/adException/record',
      idField: 'id',
      dictionary: {
        dictWeekDay: []
      },
      needLoadDictionary: true,
      listName: 'adException',
      generatorName: 'adException',
      baseUrl: '/attendance/exception/adException'
    }
  },
  computed: {
    allActions() {
      if (this.joinMode) return 'refresh'
      else return ',list,add,view,edit,delete,batchDelete,import,export,'
    }
  },
  mounted() {
    this.cgList.list_tableInit(this)
  },
  methods: {
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /CgListAdException-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
  mixins
}
</script>
