
<template>
  <div :class="mobile || height==0 ? 'cg-form':'cg-form just-v-scroll'" :style="mobile || height==0?'':'height:'+height+'px'">
    <el-form ref="cgForm" v-loading="recordLoading" :model="record" v-cg-form-adjust
                          :class="className" :rules="rules" 
             :label-position="labelPosition" :label-width="labelWidth" :size="$store.state.app.size" 
             hide-required-asterisk >
      <el-tabs ref="tabs" v-model="tabSelected" @tab-click="autoFocus">
        <el-tab-pane :label="groupPaneTitle('devReader.title.groupDevReaderName')">
          <el-row :gutter="mobile?0:20">
            <el-col :span="8">
              <el-form-item class="cg-item-text cg-auto-focus" :label="$t('devReader.field.name')" prop="name" :size="$store.state.app.size" >
                <el-input v-model="record.name" name="name" 
                          type="text" 
                          :label="$t('devReader.field.name')" :placeholder="$t('system.message.needValue')" 
                          resize autofocus validate-event 
                          :readonly="isDetail" :maxlength="30" show-word-limit />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item class="cg-item-text" :label="$t('devReader.field.readerNo')" prop="readerNo" :size="$store.state.app.size" >
                <el-input v-model="record.readerNo" name="readerNo" 
                          type="text" 
                          :label="$t('devReader.field.readerNo')" :placeholder="$t('system.message.needValue')" 
                          resize autofocus validate-event 
                          :readonly="isDetail" :maxlength="10" show-word-limit />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item class="cg-item-select" :label="$t('devReader.field.type')" prop="type" :size="$store.state.app.size" >
                <cg-select v-model="record.type" name="type"
                           :dictionary="dictionary.dictType" :readonly="isDetail" :filterable="false" :allow-create="false" :placeholder="$t('system.message.needValue')" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="mobile?0:20">
            <el-col :span="8">
              <el-form-item class="cg-item-text" :label="$t('devReader.field.ip')" prop="ip" :size="$store.state.app.size" >
                <el-input v-model="record.ip" name="ip" 
                          type="text" 
                          :label="$t('devReader.field.ip')" :placeholder="$t('system.message.needValue')" 
                          resize autofocus validate-event 
                          :readonly="isDetail" :maxlength="20" show-word-limit />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item class="cg-item-select" :label="$t('devReader.field.connectType')" prop="connectType" :size="$store.state.app.size" >
                <cg-select v-model="record.connectType" name="connectType"
                           :dictionary="dictionary.dictConnectType" :readonly="isDetail" :filterable="false" :allow-create="false" :placeholder="$t('system.message.needValue')" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item class="cg-item-text" :label="$t('devReader.field.snNo')" prop="snNo" :size="$store.state.app.size" >
                <el-input v-model="record.snNo" name="snNo" 
                          type="text" 
                          :label="$t('devReader.field.snNo')" :placeholder="$t('system.message.unknown')" 
                          resize autofocus validate-event 
                          :readonly="isDetail" :maxlength="36" show-word-limit clearable />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="mobile?0:20">
            <el-col :span="12">
              <el-form-item class="cg-item-select" :label="$t('devReader.field.readerGroup')" prop="readerGroup" :size="$store.state.app.size" >
                <cg-cascader v-model="record.readerGroup" name="readerGroup"
                             :readonly="isDetail" :filterable="false" numberic required 
                             :dictionary="dictionary.dictReaderGroup" :placeholder="$t('system.message.needValue')" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item class="cg-item-text" :label="$t('devReader.field.address')" prop="address" :size="$store.state.app.size" >
                <el-input v-model="record.address" name="address" 
                          type="text" 
                          :label="$t('devReader.field.address')" :placeholder="$t('system.message.unknown')" 
                          resize autofocus validate-event 
                          :readonly="isDetail" :maxlength="100" show-word-limit clearable />
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item class="cg-item-checkbox" :label="$t('devReader.field.devMode')" prop="devMode" :size="$store.state.app.size" >
            <cg-checkbox v-model="record.devMode" name="devMode" :dictionary="dictionary.dictDevMode" :readonly="isDetail"  />
          </el-form-item>
        </el-tab-pane>
        <el-tab-pane :label="groupPaneTitle('devReader.title.groupDevReaderAlignMethod')">
          <el-row :gutter="mobile?0:20">
            <el-col :span="8">
              <el-form-item class="cg-item-select cg-auto-focus" :label="$t('devReader.field.alignMethod')" prop="alignMethod" :size="$store.state.app.size" >
                <cg-select v-model="record.alignMethod" name="alignMethod"
                           :dictionary="dictionary.dictAlignMethod" :readonly="isDetail" :filterable="false" :allow-create="false" numberic :placeholder="$t('system.message.needValue')" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item class="cg-item-select" :label="$t('devReader.field.blacklightTime')" prop="blacklightTime" :size="$store.state.app.size" >
                <cg-select v-model="record.blacklightTime" name="blacklightTime"
                           :dictionary="dictionary.dictBlacklightTime" :readonly="isDetail" :filterable="false" :allow-create="false" numberic :placeholder="$t('system.message.needValue')" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item class="cg-item-boolean" :label="$t('devReader.field.voiceprompt')" prop="voiceprompt" :size="$store.state.app.size" >
                <el-switch v-model="record.voiceprompt" name="voiceprompt" :active-text="mobile?'':$t('devReader.field.voiceprompt')" inactive-text="" :disabled="isDetail"  />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="mobile?0:20">
            <el-col :span="8">
              <el-form-item class="cg-item-select" :label="$t('devReader.field.menuTime')" prop="menuTime" :size="$store.state.app.size" >
                <cg-select v-model="record.menuTime" name="menuTime"
                           :dictionary="dictionary.dictMenuTime" :readonly="isDetail" :filterable="false" :allow-create="false" numberic :placeholder="$t('system.message.needValue')" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item class="cg-item-select" :label="$t('devReader.field.wengenform')" prop="wengenform" :size="$store.state.app.size" >
                <cg-select v-model="record.wengenform" name="wengenform"
                           :dictionary="dictionary.dictWengenform" :readonly="isDetail" :filterable="false" :allow-create="false" numberic :placeholder="$t('system.message.needValue')" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item class="cg-item-select" :label="$t('devReader.field.wengenOutput')" prop="wengenOutput" :size="$store.state.app.size" >
                <cg-select v-model="record.wengenOutput" name="wengenOutput"
                           :dictionary="dictionary.dictWengenOutput" :readonly="isDetail" :filterable="false" :allow-create="false" numberic :placeholder="$t('system.message.needValue')" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="mobile?0:20">
            <el-col :span="8">
              <el-form-item class="cg-item-number" :label="$t('devReader.field.wengenOutArea')" prop="wengenOutArea" :size="$store.state.app.size" >
                <cg-number-input v-model="record.wengenOutArea" name="wengenOutArea" 
                                 :readonly="isDetail" 
                                 :label="$t('devReader.field.wengenOutArea')" :placeholder="$t('system.message.needValue')" :precision="0" 
                                 :min="0" :step="1" :title="$t('system.message.valueRange') + ': 0 - *'" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item class="cg-item-number" :label="$t('devReader.field.regfingerOutTime')" prop="regfingerOutTime" :size="$store.state.app.size" >
                <cg-number-input v-model="record.regfingerOutTime" name="regfingerOutTime" 
                                 :readonly="isDetail" 
                                 :label="$t('devReader.field.regfingerOutTime')" :placeholder="$t('system.message.needValue')" :precision="0" 
                                 :min="0" :step="1" :title="$t('system.message.valueRange') + ': 0 - *'" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item class="cg-item-number" :label="$t('devReader.field.authfingerOutTime')" prop="authfingerOutTime" :size="$store.state.app.size" >
                <cg-number-input v-model="record.authfingerOutTime" name="authfingerOutTime" 
                                 :readonly="isDetail" 
                                 :label="$t('devReader.field.authfingerOutTime')" :placeholder="$t('system.message.needValue')" :precision="0" 
                                 :min="0" :step="1" :title="$t('system.message.valueRange') + ': 0 - *'" />
              </el-form-item>
            </el-col>
          </el-row>
        </el-tab-pane>
        <el-tab-pane :label="groupPaneTitle('devReader.title.groupDevReaderWgOrder')">
          <el-row :gutter="mobile?0:20">
            <el-col :span="12">
              <el-form-item class="cg-item-select cg-auto-focus" :label="$t('devReader.field.wgOrder')" prop="wgOrder" :size="$store.state.app.size" >
                <cg-select v-model="record.wgOrder" name="wgOrder"
                           :dictionary="dictionary.dictWgOrder" :readonly="isDetail" :filterable="false" :allow-create="false" numberic :placeholder="$t('system.message.unknown')" clearable />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item class="cg-item-number" :label="$t('devReader.field.relayTime')" prop="relayTime" :size="$store.state.app.size" >
                <cg-number-input v-model="record.relayTime" name="relayTime" 
                                 :readonly="isDetail" 
                                 :label="$t('devReader.field.relayTime')" :placeholder="$t('system.message.unknown')" clearable 
                                 :min="1" :max="1000" :step="5" :title="$t('system.message.valueRange') + ': 1 - 1000'" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="mobile?0:20">
            <el-col :span="12">
              <el-form-item class="cg-item-text" :label="$t('devReader.field.fixedValue')" prop="fixedValue" :size="$store.state.app.size" >
                <el-input v-model="record.fixedValue" name="fixedValue" 
                          type="text" 
                          :label="$t('devReader.field.fixedValue')" :placeholder="$t('system.message.unknown')" 
                          resize autofocus validate-event 
                          :readonly="isDetail" :maxlength="36" show-word-limit clearable />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item class="cg-item-select" :label="$t('devReader.field.alarmEnable')" prop="alarmEnable" :size="$store.state.app.size" >
                <cg-select v-model="record.alarmEnable" name="alarmEnable"
                           :dictionary="dictionary.dictAlarmEnable" :readonly="isDetail" :filterable="false" :allow-create="false" numberic :placeholder="$t('system.message.unknown')" clearable />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="mobile?0:20">
            <el-col :span="12">
              <el-form-item class="cg-item-select" :label="$t('devReader.field.openEnable')" prop="openEnable" :size="$store.state.app.size" >
                <cg-select v-model="record.openEnable" name="openEnable"
                           :dictionary="dictionary.dictOpenEnable" :readonly="isDetail" :filterable="false" :allow-create="false" numberic :placeholder="$t('system.message.unknown')" clearable />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item class="cg-item-select" :label="$t('devReader.field.doorState')" prop="doorState" :size="$store.state.app.size" >
                <cg-select v-model="record.doorState" name="doorState"
                           :dictionary="dictionary.dictDoorState" :readonly="isDetail" :filterable="false" :allow-create="false" numberic :placeholder="$t('system.message.unknown')" clearable />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="mobile?0:20">
            <el-col :span="12">
              <el-form-item class="cg-item-select" :label="$t('devReader.field.relayEnable')" prop="relayEnable" :size="$store.state.app.size" >
                <cg-select v-model="record.relayEnable" name="relayEnable"
                           :dictionary="dictionary.dictRelayEnable" :readonly="isDetail" :filterable="false" :allow-create="false" numberic :placeholder="$t('system.message.unknown')" clearable />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item class="cg-item-select" :label="$t('devReader.field.doorbellEnable')" prop="doorbellEnable" :size="$store.state.app.size" >
                <cg-select v-model="record.doorbellEnable" name="doorbellEnable"
                           :dictionary="dictionary.dictDoorbellEnable" :readonly="isDetail" :filterable="false" :allow-create="false" numberic :placeholder="$t('system.message.unknown')" clearable />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="mobile?0:20">
            <el-col :span="12">
              <el-form-item class="cg-item-text" :label="$t('devReader.field.wifiSsid')" prop="wifiSsid" :size="$store.state.app.size" >
                <el-input v-model="record.wifiSsid" name="wifiSsid" 
                          type="text" 
                          :label="$t('devReader.field.wifiSsid')" :placeholder="$t('system.message.unknown')" 
                          resize autofocus validate-event 
                          :readonly="isDetail" :maxlength="36" show-word-limit clearable />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item class="cg-item-text" :label="$t('devReader.field.wifiPsw')" prop="wifiPsw" :size="$store.state.app.size" >
                <el-input v-model="record.wifiPsw" name="wifiPsw" 
                          type="text" 
                          :label="$t('devReader.field.wifiPsw')" :placeholder="$t('system.message.unknown')" 
                          resize autofocus validate-event 
                          :readonly="isDetail" :maxlength="36" show-word-limit clearable />
              </el-form-item>
            </el-col>
          </el-row>
        </el-tab-pane>
      </el-tabs>
    </el-form>
    <div v-if="!isDetail" class="cg-form-buttons">
      <el-divider />
      <el-button-group v-for = "(btn,index) in addtionalActions" :key="btn.action" style="margin-right:5px">
        <el-button v-if="hasAuthorityOf(myself,baseUrl,btn,record)" :class="'cg-button'+''+(btn.appendClass?' '+btn.appendClass:'')"
                   type="default" plain :icon="btn.icon" :disabled="hasOwnProperty('disabledAction') ? disabledAction(btn) : false"
                   @click.native="doAction(btn.action)" >
        {{ $t('devReader.action.'+btn.action) }}
        </el-button>
      </el-button-group>
      <el-button v-if="!mobile && hasAuthorityOf(myself,baseUrl,'add',record)" class="cg-button" type="default" plain icon="fa fa-plus"
                 @click.native="doAction('add')">
        {{ $t('system.action.new') }}
      </el-button>
      <el-button v-if="hasAuthorityOf(myself,baseUrl,'edit',record)" class="cg-button" type="primary" plain :disabled="!recordChanged"
                 icon="el-icon-check" @click.native="submit()">
        {{ $t('system.action.save') }}
      </el-button>
      <el-button v-if="!mobile && showInDialog" class="cg-button" plain icon="el-icon-close"
                 @click.native="$emit('closeDialog')">
        {{ $t('system.action.close') }}
      </el-button>
    </div>
  </div>
</template>
<script>
import cgForm from '@/utils/cgForm'
import rulesObject from './rules.js'
import ParentForm from '@/views/common-views/components/form'
const Comp = {
  name: 'CgFormDevReader',
  mixins: [ParentForm],
  props: {
    dialogParams: {
      type: Object,
      default: null
    }
  },
  data() {
    return {
      defaultLabelPosition: 'top',
      rulesObject,
      isDialogForm: true,
      continueAdd: true,
      path: 'record',
      idField: 'id',
      idSaved: this.openParams().record && typeof this.openParams().record === 'object' ? this.openParams().record.id : null,
      dictionary: {
        dictType: [],
        dictConnectType: [],
        dictReaderGroup: [],
        dictDevMode: [],
        dictAlignMethod: [],
        dictBlacklightTime: this.getDictionary('0,1,2,3,4','10s,30s,60s,90s,常亮'),
        dictMenuTime: this.getDictionary('0,1,2,3','10s,30s,60s,90s'),
        dictWengenform: [],
        dictWengenOutput: [],
        dictWgOrder: this.getDictionary('0,1','低位先发,高位先发'),
        dictAlarmEnable: this.getDictionary('0,1','关闭,开启'),
        dictOpenEnable: this.getDictionary('0,1','关闭,开启'),
        dictDoorState: this.getDictionary('0,1','关闭,开启'),
        dictRelayEnable: this.getDictionary('0,1','关闭,开启'),
        dictDoorbellEnable: this.getDictionary('0,1','关闭,开启')
      },
      needLoadDictionary: true,
      tabSelected: '0',
      generatorName: 'devReader',
      baseUrl: '/reader/devReader'
    }
  },
  computed: {
    addtionalActions() {
      return [
        {
          action: 'queryTime',
          icon: 'fa fa-clock-o  fa-fw',
          title: 'devReader.action.queryTime',
          groupid: 10,
          confirm: '',
          rowProperty: 'sr',
          displayProperties: 'rw,gs,tb,ed',
          actionProperty: 'aj',
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
          displayProperties: 'rw,ed,tb',
          actionProperty: 'aj',
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
          displayProperties: 'tb,rw,ed,hm',
          actionProperty: 'aj',
          appendClass: '',
          needRefresh: false
        },
      ]
    },
  },
  methods: {
    just4elInputNumberNullBug: function() {
      if (this.record.wengenOutArea === null) this.record.wengenOutArea = undefined
      if (this.record.regfingerOutTime === null) this.record.regfingerOutTime = undefined
      if (this.record.authfingerOutTime === null) this.record.authfingerOutTime = undefined
      if (this.record.relayTime === null) this.record.relayTime = undefined
    },
    newRecord: function() {
        return {
            isOnline: false,
            isTimeSync: false,
            name: '',
            readerNo: '',
            type: 'D10',
            ip: '',
            connectType: '',
            readerGroup: 0,
            devMode: '',
            alignMethod: 4,
            blacklightTime: 0,
            voiceprompt: true,
            menuTime: 0,
            wengenform: 2,
            wengenOutput: 1,
            wengenOutArea: 26,
            regfingerOutTime: 49,
            authfingerOutTime: 49,
            wgOrder: 0,
            relayTime: 5,
            alarmEnable: 1,
            openEnable: 0,
            doorState: 1,
            relayEnable: 1,
            doorbellEnable: 1,
        }
    },
    groupPaneTitle: function(defTitle) {
      return this.$t(defTitle)
    },
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /CgFormDevReader-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
    mixins
}
</script>
