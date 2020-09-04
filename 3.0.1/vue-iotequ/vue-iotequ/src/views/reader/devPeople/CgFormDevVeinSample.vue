
<template>
  <div :class="mobile || height==0 ? 'cg-form':'cg-form just-v-scroll'" :style="mobile || height==0?'':'height:'+height+'px'">
    <el-form ref="cgForm" v-loading="recordLoading" :model="record" v-cg-form-adjust
                          :class="className" :rules="rules" 
             :label-position="labelPosition" :label-width="labelWidth" :size="$store.state.app.size" 
             hide-required-asterisk >
      <el-row :gutter="mobile?0:20">
        <el-col :span="8">
          <el-form-item class="cg-item-text" :label="$t('devPeople.field.userNo')" prop="userNo" :size="$store.state.app.size" >
            <el-input v-model="record.userNo" name="userNo" 
                      type="text" :maxlength="15" show-word-limit 
                      :label="$t('devPeople.field.userNo')" :placeholder="$t('system.message.needValue')" 
                      readonly resize autofocus validate-event />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item class="cg-item-text" :label="$t('devPeople.field.realName')" prop="realName" :size="$store.state.app.size" >
            <el-input v-model="record.realName" name="realName" 
                      type="text" :maxlength="32" show-word-limit 
                      :label="$t('devPeople.field.realName')" :placeholder="$t('system.message.needValue')" 
                      readonly resize autofocus validate-event />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item class="cg-item-number" :label="$t('devPeople.field.regFingers')" prop="regFingers" :size="$store.state.app.size" >
            <cg-number-input v-model="record.regFingers" name="regFingers" 
                             readonly 
                             :label="$t('devPeople.field.regFingers')" :placeholder="$t('system.message.unknown')" clearable :precision="0" 
                             :min="0" :step="1" :title="$t('system.message.valueRange') + ': 0 - *'" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item :label="$t('devPeople.field.fingerNo1')+(record.warning1?'('+$t('devPeople.field.warning')+')':'')" class="cg-item-select" prop="fingerNo1" :size="$store.state.app.size" >
            <cg-select v-model="record.fingerNo1" name="fingerNo1"
                       v-set-input.input="record.warning1 ? {style: 'append:color:red!important'} : {}"
                       :dictionary="dictionary.dictFingerType" readonly :filterable="false" :allow-create="false" :placeholder="$t('system.message.unknown')" clearable />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('devPeople.field.fingerNo2')+(record.warning2?'('+$t('devPeople.field.warning')+')':'')" class="cg-item-select" prop="fingerNo2" :size="$store.state.app.size" >
            <cg-select v-model="record.fingerNo2" name="fingerNo2"
                       v-set-input.input="record.warning2 ? {style: 'append:color:red!important'} : {}"
                       :dictionary="dictionary.dictFingerType" readonly :filterable="false" :allow-create="false" :placeholder="$t('system.message.unknown')" clearable />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-select cg-auto-focus" :label="$t('devPeople.field.fingerType')" prop="fingerType" :size="$store.state.app.size" >
            <cg-select v-model="record.fingerType" name="fingerType"
                       :dictionary="dictionary.dictFingerType" :readonly="isDetail" :filterable="false" :allow-create="false" :placeholder="$t('system.message.needValue')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-boolean" :label="$t('devPeople.field.warning')" prop="warning" :size="$store.state.app.size" >
            <el-switch v-model="record.warning" name="warning" active-color="red" :active-text="mobile?'':$t('devPeople.field.warning')" inactive-text="" :disabled="isDetail"  />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <div v-if="!isDetail" class="cg-form-buttons">
      <el-divider />
      <el-button-group v-for = "(btn,index) in addtionalActions" :key="btn.action" style="margin-right:5px">
        <el-button v-if="hasAuthorityOf(myself,baseUrl,btn,record)" :class="'cg-button'+''+(btn.appendClass?' '+btn.appendClass:'')"
                   type="default" plain :icon="btn.icon" :disabled="hasOwnProperty('disabledAction') ? disabledAction(btn) : false"
                   @click.native="doAction(btn.action)" >
        {{ $t('devPeople.action.'+btn.action) }}
        </el-button>
      </el-button-group>
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
const mixinContext = require.context('.', false, /CgFormDevVeinSample-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default {
  name: 'CgFormDevVeinSample',
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
      path: 'sample',
      title: this.$t('devPeople.title.'+this.path),
      rules: {},
      idField: 'userNo',
      userNoSaved: this.openParams().record && typeof this.openParams().record === 'object' ? this.openParams().record.userNo : null,
      onChange: typeof this.openParams().onChange === 'function' ? this.openParams().onChange : null,
      recordChanged: false,
      recordLoading: false,
      fixedFields: typeof this.openParams().fixedFields === 'object' ? this.openParams().fixedFields : {},
      openMode: this.openParams().openMode ? this.openParams().openMode : null,
      record: this.openParams().record && typeof this.openParams().record === 'object' ? this.openParams().record : {},
      needDefaultFromServer: false,
      dictionary: {
        dictOrgCode: [],
        dictIdType: [],
        dictUserType: this.getDictionary('1,2','管理员,普通人员'),
        dictRegisterType: [],
        dictFingerType: this.getDictionary('1,2,3,4,5,6,11,12','右食指,右中指,右无名指,左食指,左中指,左无名指,自定义1,自定义2')
      },
      needLoadDictionary: true,
      generatorName: 'devPeople',
      baseUrl: '/reader/devPeople'
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
      return (this.mobile?'cg-form-cell ':'')+'cg-no-border cg-form-devveinsample'
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
          action: 'connectDevice',
          icon: 'fa fa-gg  fa-fw',
          title: 'devPeople.action.connectDevice',
          groupid: 10,
          confirm: '',
          rowProperty: 'sr',
          displayProperties: 'hm,ed',
          actionProperty: 'js',
          appendClass: 'this.connect(0)',
          needRefresh: false
        },
        {
          action: 'registerFinger',
          icon: 'fa fa-hand-pointer-o  fa-fw',
          title: 'devPeople.action.registerFinger',
          groupid: 10,
          confirm: '',
          rowProperty: 'sr',
          displayProperties: 'hm,ed',
          actionProperty: 'pg',
          appendClass: 'this.sample()',
          needRefresh: false
        },
        {
          action: 'verifyFinger',
          icon: 'fa fa-hand-o-left  fa-fw',
          title: 'devPeople.action.verifyFinger',
          groupid: 10,
          confirm: '',
          rowProperty: 'sr',
          displayProperties: 'hm,ed',
          actionProperty: 'pg',
          appendClass: 'this.auth()',
          needRefresh: false
        },
        {
          action: 'matchFinger',
          icon: 'fa fa-address-book-o',
          title: 'devPeople.action.matchFinger',
          groupid: 10,
          confirm: '',
          rowProperty: 'sr',
          displayProperties: 'hm,ed',
          actionProperty: 'pg',
          appendClass: 'this.match()',
          needRefresh: false
        },
        {
          action: 'removeFinger',
          icon: 'fa fa-hand-grab-o  fa-fw',
          title: 'devPeople.action.removeFinger',
          groupid: 10,
          confirm: 'devPeople.action.removeFingerConfirm',
          rowProperty: 'sr',
          displayProperties: 'hm,ed',
          actionProperty: 'pg',
          appendClass: 'this.removeFinger()',
          needRefresh: false
        },
        {
          action: 'resetDevice',
          icon: 'fa fa-flash  fa-fw',
          title: 'devPeople.action.resetDevice',
          groupid: 10,
          confirm: '',
          rowProperty: 'sr',
          displayProperties: 'hm,ed',
          actionProperty: 'js',
          appendClass: 'this.resetU53()',
          needRefresh: false
        }
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
      if (this.record.regFingers === null) this.record.regFingers = undefined
    },
    newRecord: function() {
        return {
            orgCode: 0,
            idType: 1,
            idNumber: '',
            userType: 2,
            registerType: 1,
            realName: '',
            regFingers: 0,
            fingerNo1: '1',
            fingerType: '1',
            warning: false,
        }
    },
    submit: function() {
      if (this.recordChanged) cgForm.form_submit(this, 'save',true)
    },
    doAction(action, options) {
      cgForm.form_doAction(this, action, Object.assign(options ? options : {}, this.addtionalActions.find(e => e.action === action)))
    },
    ...cg
  }
}
</script>
