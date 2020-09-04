
<template>
  <div :class="mobile || height==0 ? 'cg-form':'cg-form just-v-scroll'" :style="mobile || height==0?'':'height:'+height+'px'">
    <el-form ref="cgForm" v-loading="recordLoading" :model="record" v-cg-form-adjust
                          :class="className" :rules="rules" 
             :label-position="labelPosition" :label-width="labelWidth" :size="$store.state.app.size" 
             hide-required-asterisk >
      <el-row :gutter="mobile?0:20">
        <el-col :span="8">
          <el-form-item class="cg-item-boolean cg-auto-focus" :label="$t('ewUser.field.isActive')" prop="isActive" :size="$store.state.app.size" >
            <el-switch v-model="record.isActive" name="isActive" :active-text="mobile?'':$t('ewUser.field.isActive')" inactive-text="" :disabled="isDetail"  />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item class="cg-item-text" :label="$t('ewUser.field.name')" prop="name" :size="$store.state.app.size" >
            <el-input v-model="record.name" name="name" 
                      type="text" :maxlength="32" show-word-limit 
                      :label="$t('ewUser.field.name')" :placeholder="$t('system.message.needValue')" 
                      :readonly="isDetail" resize autofocus validate-event />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item class="cg-item-radio" :label="$t('ewUser.field.gender')" prop="gender" :size="$store.state.app.size" >
            <cg-radio v-model="record.gender" name="gender" :dictionary="dictionary.dictGender" :readonly="isDetail" numberic  />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="8">
          <el-form-item class="cg-item-select" :label="$t('ewUser.field.idType')" prop="idType" :size="$store.state.app.size" >
            <cg-select v-model="record.idType" name="idType"
                       :dictionary="dictionary.dictIdType" :readonly="isDetail" :filterable="false" :allow-create="false" numberic :placeholder="$t('system.message.needValue')" />
          </el-form-item>
        </el-col>
        <el-col :span="16">
          <el-form-item class="cg-item-text" :label="$t('ewUser.field.idNo')" prop="idNo" :size="$store.state.app.size" >
            <el-input v-model="record.idNo" name="idNo" 
                      type="text" :maxlength="45" show-word-limit 
                      :label="$t('ewUser.field.idNo')" :placeholder="$t('system.message.needValue')" 
                      :readonly="isDetail" resize autofocus validate-event />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="8">
          <el-form-item class="cg-item-text" :label="$t('ewUser.field.mobilePhone')" prop="mobilePhone" :size="$store.state.app.size" >
            <el-input v-model="record.mobilePhone" name="mobilePhone" 
                      type="text" :maxlength="45" show-word-limit 
                      :label="$t('ewUser.field.mobilePhone')" :placeholder="$t('system.message.unknown')" clearable 
                      :readonly="isDetail" resize autofocus validate-event />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item class="cg-item-text" :label="$t('ewUser.field.email')" prop="email" :size="$store.state.app.size" >
            <el-input v-model="record.email" name="email" 
                      type="text" :maxlength="45" show-word-limit 
                      :label="$t('ewUser.field.email')" :placeholder="$t('system.message.unknown')" clearable 
                      :readonly="isDetail" resize autofocus validate-event />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item class="cg-item-false_if_null" :label="$t('ewUser.field.wechatOpenid')" prop="wechatOpenid" :size="$store.state.app.size" >
            <el-switch name="wechatOpenid" :value="record.wechatOpenid?true:false" :active-text="mobile?'':$t('ewUser.field.wechatOpenid')" inactive-text="" disabled />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="8">
          <el-form-item class="cg-item-date" :label="$t('ewUser.field.birthDate')" prop="birthDate" :size="$store.state.app.size" >
            <span slot="label"><cg-icon icon="fa fa-calendar" :size="14" :width="20"/>{{$t('ewUser.field.birthDate')}}</span>
            <cg-date-picker v-model="record.birthDate" name="birthDate" 
                            :align="mobile?'right':'center'" type="date" :title="$t('ewUser.field.birthDate')" :readonly="isDetail" 
                            clearable />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item class="cg-item-text" :label="$t('ewUser.field.memberGroup')" prop="memberGroup" :size="$store.state.app.size" >
            <el-input v-model="record.memberGroup" name="memberGroup" 
                      type="text" :maxlength="45" show-word-limit 
                      :label="$t('ewUser.field.memberGroup')" :placeholder="$t('system.message.unknown')" clearable 
                      :readonly="isDetail" resize autofocus validate-event />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item class="cg-item-number" :label="$t('ewUser.field.bonusPoint')" prop="bonusPoint" :size="$store.state.app.size" >
            <cg-number-input v-model="record.bonusPoint" name="bonusPoint" 
                             :readonly="isDetail" 
                             :label="$t('ewUser.field.bonusPoint')" :placeholder="$t('system.message.needValue')" :precision="0" 
                             :min="0" :step="1" :title="$t('system.message.valueRange') + ': 0 - *'" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="8">
          <el-form-item class="cg-item-number" :label="$t('ewUser.field.amountMoney')" prop="amountMoney" :size="$store.state.app.size" >
            <cg-number-input v-model="record.amountMoney" name="amountMoney" 
                             :readonly="isDetail" 
                             :label="$t('ewUser.field.amountMoney')" :placeholder="$t('system.message.needValue')" :precision="0" 
                             :min="0" :step="1" :title="$t('system.message.valueRange') + ': 0 - *'" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item class="cg-item-number" :label="$t('ewUser.field.costLimit')" prop="costLimit" :size="$store.state.app.size" >
            <cg-number-input v-model="record.costLimit" name="costLimit" 
                             :readonly="isDetail" 
                             :label="$t('ewUser.field.costLimit')" :placeholder="$t('system.message.needValue')" :precision="0" 
                             :min="0" :step="1" :title="$t('system.message.valueRange') + ': 0 - *'" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item class="cg-item-number" :label="$t('ewUser.field.dayLimit')" prop="dayLimit" :size="$store.state.app.size" >
            <cg-number-input v-model="record.dayLimit" name="dayLimit" 
                             :readonly="isDetail" 
                             :label="$t('ewUser.field.dayLimit')" :placeholder="$t('system.message.needValue')" :precision="0" 
                             :min="0" :step="1" :title="$t('system.message.valueRange') + ': 0 - *'" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-datetime" :label="$t('ewUser.field.activeSince')" prop="activeSince" :size="$store.state.app.size" >
            <span slot="label"><cg-icon icon="fa fa-calendar-plus-o" :size="14" :width="20"/>{{$t('ewUser.field.activeSince')}}</span>
            <cg-date-picker v-model="record.activeSince" name="activeSince" 
                            :align="mobile?'right':'center'" type="datetime" :title="$t('ewUser.field.activeSince')" :readonly="isDetail" 
                            clearable />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-datetime" :label="$t('ewUser.field.expireAt')" prop="expireAt" :size="$store.state.app.size" >
            <span slot="label"><cg-icon icon="fa fa-calendar-plus-o" :size="14" :width="20"/>{{$t('ewUser.field.expireAt')}}</span>
            <cg-date-picker v-model="record.expireAt" name="expireAt" 
                            :align="mobile?'right':'center'" type="datetime" :title="$t('ewUser.field.expireAt')" :readonly="isDetail" 
                            clearable />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <div v-if="!isDetail" class="cg-form-buttons">
      <el-divider />
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
const mixinContext = require.context('.', false, /CgFormEwUser-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default {
  name: 'CgFormEwUser',
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
      title: this.$t('ewUser.title.'+this.path),
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
        dictGender: [],
        dictIdType: []
      },
      needLoadDictionary: true,
      generatorName: 'ewUser',
      baseUrl: '/ewallet/ewUser'
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
      return (this.mobile?'cg-form-cell ':'')+'cg-no-border cg-form-ewuser'
    },
    labelWidth() {
      return this.className.indexOf('cg-form-cell')>=0? '100px' : undefined
    },
    labelPosition() {
      return this.className.indexOf('cg-form-cell')>=0? 'left':'top'
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
      if (this.record.bonusPoint === null) this.record.bonusPoint = undefined
      if (this.record.amountMoney === null) this.record.amountMoney = undefined
      if (this.record.costLimit === null) this.record.costLimit = undefined
      if (this.record.dayLimit === null) this.record.dayLimit = undefined
    },
    newRecord: function() {
        return {
            checkCode: '',
            isActive: true,
            name: '',
            gender: 0,
            idType: 0,
            idNo: '',
            bonusPoint: 0,
            amountMoney: 0,
            costLimit: 0,
            dayLimit: 0,
        }
    },
    submit: function() {
      if (this.recordChanged) cgForm.form_submit(this, 'save',true)
    },
    doAction(action, options) {
      cgForm.form_doAction(this, action, options)
    },
    ...cg
  }
}
</script>
