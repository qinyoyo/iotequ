
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
                          type="text" :maxlength="30" show-word-limit 
                          :label="$t('devReader.field.name')" :placeholder="$t('system.message.needValue')" 
                          :readonly="isDetail" resize autofocus validate-event />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item class="cg-item-text" :label="$t('devReader.field.readerNo')" prop="readerNo" :size="$store.state.app.size" >
                <el-input v-model="record.readerNo" name="readerNo" 
                          type="text" :maxlength="10" show-word-limit 
                          :label="$t('devReader.field.readerNo')" :placeholder="$t('system.message.needValue')" 
                          :readonly="isDetail" resize autofocus validate-event />
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
            <el-col :span="12">
              <el-form-item class="cg-item-text" :label="$t('devReader.field.ip')" prop="ip" :size="$store.state.app.size" >
                <el-input v-model="record.ip" name="ip" 
                          type="text" :maxlength="20" show-word-limit 
                          :label="$t('devReader.field.ip')" :placeholder="$t('system.message.needValue')" 
                          :readonly="isDetail" resize autofocus validate-event />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item class="cg-item-select" :label="$t('devReader.field.connectType')" prop="connectType" :size="$store.state.app.size" >
                <cg-select v-model="record.connectType" name="connectType"
                           :dictionary="dictionary.dictConnectType" :readonly="isDetail" :filterable="false" :allow-create="false" :placeholder="$t('system.message.needValue')" />
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
                          type="text" :maxlength="100" show-word-limit 
                          :label="$t('devReader.field.address')" :placeholder="$t('system.message.unknown')" clearable 
                          :readonly="isDetail" resize autofocus validate-event />
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
import cg from '@/utils/cg'
import cgForm from '@/utils/cgForm'
import rulesObject from './rules.js'
const mixins = []
const mixinContext = require.context('.', false, /CgFormDevReader-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default {
  name: 'CgFormDevReader',
  mixins,
  props: {
    dialogParams: {
      type: Object,
      default: null
    },
    showInDialog: {
      type: Boolean,
      default: false
    },
    height: {
      type: Number,
      default: 0
    },
    queryById: [Number, String]
  },
  data() {
    return {
      myself: this,
      path: 'record',
      title: this.$t('devReader.title.'+this.path),
      rules: {},
      idField: 'id',
      idSaved: this.openParams().record && typeof this.openParams().record === 'object' ? this.openParams().record.id : null,
      onChange: typeof this.openParams().onChange === 'function' ? this.openParams().onChange : null,
      recordChanged: false,
      recordLoading: false,
      fixedFields: typeof this.openParams().fixedFields === 'object' ? this.openParams().fixedFields : {},
      openMode: this.openParams().openMode ? this.openParams().openMode : null,
      record: this.openParams().record && typeof this.openParams().record === 'object' ? this.openParams().record : {},
      needDefaultFromServer: false,
      dictionary: {
        dictType: [],
        dictConnectType: [],
        dictReaderGroup: [],
        dictDevMode: [],
        dictAlignMethod: [],
        dictBlacklightTime: this.getDictionary('0,1,2,3,4','10s,30s,60s,90s,常亮'),
        dictMenuTime: this.getDictionary('0,1,2,3','10s,30s,60s,90s'),
        dictWengenform: [],
        dictWengenOutput: []
      },
      needLoadDictionary: true,
      tabSelected: '0',
      generatorName: 'devReader',
      baseUrl: '/reader/devReader'
    }
  },
  computed: {
    mobile() {
      return this.$store.state.app.device === 'mobile'
    },
    hasMenu() {
      return false
    },
    className() {
      return (this.mobile?'cg-form-cell ':'')+'cg-no-border cg-form-devreader'
    },
    labelWidth() {
      return this.className.indexOf('cg-form-cell')>=0? '100px' : undefined
    },
    labelPosition() {
      return this.className.indexOf('cg-form-cell')>=0? 'left':'top'
    },
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
    isDetail() {
      return this.openMode === 'view'
    },
    isNew() {
      return !this.openMode || this.openMode === 'add'
    },
    isEdit() {
      return this.openMode === 'edit'
    }
  },
  watch: {
    record: {
      handler() {
        this.recordChanged = true
        this.just4elInputNumberNullBug()
      },
      deep: true
    },
    queryById: {
      handler(n, o) {
        if (n) this.doAction('refresh', {id: n})
      },
      immediate: true
    }
  },
  created() {
    this.rules = rulesObject.getRules(this)
    cgForm.form_getQueryDictionary(this)
    if (this.queryById) {
      cgForm.form_getRecordFromServer(this,this.queryById)
      this.queryRefreshId = this.queryById
    } else if (this.isNew) cgForm.form_createNewRecord(this)
    else if ((this.isEdit || this.isDetail) && this.openParams().id && typeof this.openParams().record !== 'object') {
      cgForm.form_getRecordFromServer(this,this.openParams().id)
      this.queryRefreshId = this.openParams().id
    }
    else if (this.needLoadDictionary) cgForm.form_getDictionary(this)
    this.just4elInputNumberNullBug()
  },
  mounted() {
    cgForm.form_mounted(this)
  },
  methods: {
    openParams: function() {
      return this.dialogParams ? this.dialogParams : this.$route.query
    },
    just4elInputNumberNullBug: function() {
      if (this.record.wengenOutArea === null) this.record.wengenOutArea = undefined
      if (this.record.regfingerOutTime === null) this.record.regfingerOutTime = undefined
      if (this.record.authfingerOutTime === null) this.record.authfingerOutTime = undefined
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
            alignMethod: '4',
            blacklightTime: '50',
            voiceprompt: true,
            menuTime: '50',
            wengenform: '2',
            wengenOutput: '1',
            wengenOutArea: '26',
            regfingerOutTime: '49',
            authfingerOutTime: '49',
        }
    },
    groupPaneTitle: function(defTitle) {
      return this.$t(defTitle)
    },
    submit: function() {
      if (this.recordChanged) cgForm.form_submit(this, 'save')
    },
    doAction(action, options) {
      cgForm.form_doAction(this, action, Object.assign(options ? options : {}, this.addtionalActions.find(e => e.action === action)))
    },
    ...cg
  }
}
</script>
