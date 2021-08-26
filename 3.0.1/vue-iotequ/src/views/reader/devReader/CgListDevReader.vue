
<template>
  <div :class="joinMode?'cg-join-list':'cg-list'">
    <el-backtop v-if="isTableMode()" ref="cgListBacktop" class="el-backtop" target=".cg-list-devreader .el-table__body-wrapper" :visibility-height="200">
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
      <cg-table-column prop="name" :page="1" :label="$t('devReader.field.name')" align="left" >
        <template slot-scope="scope">
          {{ localeText(scope.row.name) }}
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
import {localeText} from '@/lang'
import {hasAuthority} from '@/utils/cg'
import ParentTable from '@/views/common-views/components/table'
const Comp = {
  name: 'CgListDevReader',
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
      defaultOrder: 'id desc',
      queryRecordFields: [],
      formPath: '/reader/devReader/record',
      idField: 'id',
      dictionary: {
        dictReaderGroup: [],
        dictAlignMethod: [],
        dictBlacklightTime: this.getDictionary('0,1,2,3,4','devReader.field.blacklightTime_0,devReader.field.blacklightTime_1,devReader.field.blacklightTime_2,devReader.field.blacklightTime_3,devReader.field.blacklightTime_4'),
        dictMenuTime: this.getDictionary('0,1,2,3','devReader.field.menuTime_0,devReader.field.menuTime_1,devReader.field.menuTime_2,devReader.field.menuTime_3'),
        dictWengenform: [],
        dictWengenOutput: [],
        dictWgOrder: this.getDictionary('0,1','devReader.field.wgOrder_0,devReader.field.wgOrder_1'),
        dictAlarmEnable: this.getDictionary('0,1','devReader.field.alarmEnable_0,devReader.field.alarmEnable_1'),
        dictOpenEnable: this.getDictionary('0,1','devReader.field.openEnable_0,devReader.field.openEnable_1'),
        dictDoorState: this.getDictionary('0,1','devReader.field.doorState_0,devReader.field.doorState_1'),
        dictRelayEnable: this.getDictionary('0,1','devReader.field.relayEnable_0,devReader.field.relayEnable_1'),
        dictDoorbellEnable: this.getDictionary('0,1','devReader.field.doorbellEnable_0,devReader.field.doorbellEnable_1'),
        dictType: [],
        dictConnectType: [],
        dictDevMode: []
      },
      needLoadDictionary: true,
      listName: 'devReader',
      generatorName: 'devReader',
      baseUrl: '/reader/devReader'
    }
  },
  computed: {
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
        },
        {
          action: 'reportPeople',
          icon: 'fa fa-user',
          title: 'devReader.action.reportPeople',
          groupid: 10,
          confirm: '',
          rowProperty: 'sr',
          actionProperty: 'pg',
          displayProperties: 'tb',
          appendClass: 'this.reportPeople()',
          needRefresh: false
        },
        {
          action: 'againDownload',
          icon: 'fa fa-arrow-circle-down',
          title: 'devReader.action.againDownload',
          groupid: 10,
          confirm: '',
          rowProperty: 'sr',
          actionProperty: 'pg',
          displayProperties: 'tb',
          appendClass: 'this.againDownload()',
          needRefresh: false
        }
      ]
    },
    allActions() {
      if (this.joinMode) return 'refresh'
      else return ',list,add,view,edit,delete,deleteAllUsers,resetDevice,queryTime,setDeviceTime,data,reportPeople,againDownload,'
    }
  },
  mounted() {
    this.cgList.list_tableInit(this)
  },
  methods: {
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /CgListDevReader-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
  mixins
}
</script>
