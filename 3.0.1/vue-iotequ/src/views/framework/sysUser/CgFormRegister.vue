
<template>
  <div :class="mobile || height==0 ? 'cg-form':'cg-form just-v-scroll'" :style="mobile || height==0?'':'height:'+height+'px'">
    <el-form ref="cgForm" v-loading="recordLoading" :model="record" v-cg-form-adjust
                          :class="className" :rules="rules" 
             :label-position="labelPosition" :label-width="labelWidth" :size="$store.state.app.size" 
             hide-required-asterisk enctype="multipart/form-data" >
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-tel" :label="$t('sysUser.field.mobilePhone')" prop="mobilePhone" :size="$store.state.app.size" >
            <el-input v-model="record.mobilePhone" name="mobilePhone" 
                      type="tel" 
                      :label="$t('sysUser.field.mobilePhone')" :placeholder="$t('system.message.unknown')" 
                      resize autofocus validate-event 
                      readonly :maxlength="32" show-word-limit clearable />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item v-if="record.hasOwnProperty('randCode')" :label="$t('login.smsRandCode')" class="cg-item-text cg-auto-focus" prop="randCode" :size="$store.state.app.size" >
            <el-input v-model="record.randCode" name="randCode" 
                      :label="$t('login.smsRandCode')" :placeholder="$t('login.smsRandCode')" type="number"
                      
                      
                      resize autofocus validate-event 
                      :readonly="isDetail" clearable />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-text" :label="$t('sysUser.field.name')" prop="name" :size="$store.state.app.size" >
            <el-input v-model="record.name" name="name" 
                      type="text" 
                      :label="$t('sysUser.field.name')" :placeholder="$t('system.message.needValue')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="32" show-word-limit />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-text" :label="$t('sysUser.field.realName')" prop="realName" :size="$store.state.app.size" >
            <el-input v-model="record.realName" name="realName" 
                      type="text" 
                      :label="$t('sysUser.field.realName')" :placeholder="$t('system.message.needValue')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="32" show-word-limit />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-radio" :label="$t('sysUser.field.sex')" prop="sex" :size="$store.state.app.size" >
            <cg-radio v-model="record.sex" name="sex" :dictionary="dictionary.dictSex" :readonly="isDetail"  />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-date" :label="$t('sysUser.field.birthDate')" prop="birthDate" :size="$store.state.app.size" >
            <span slot="label"><cg-icon icon="fa fa-calendar" :size="14" :width="20"/>{{$t('sysUser.field.birthDate')}}</span>
            <cg-date-picker v-model="record.birthDate" name="birthDate" 
                            :maxValue="new Date()" :minValue="dateAdd(new Date(),-100,'year')"
                            :align="mobile?'right':'center'" type="date" :title="$t('sysUser.field.birthDate')" :readonly="isDetail" 
                            clearable />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-select" :label="$t('sysUser.field.idType')" prop="idType" :size="$store.state.app.size" >
            <cg-select v-model="record.idType" name="idType"
                       :dictionary="dictionary.dictIdType" :readonly="!isNew" :filterable="false" :allow-create="false" numberic :placeholder="$t('system.message.needValue')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-text" :label="$t('sysUser.field.idNumber')" prop="idNumber" :size="$store.state.app.size" >
            <el-input v-model="record.idNumber" name="idNumber" 
                      type="text" 
                      :label="$t('sysUser.field.idNumber')" :placeholder="$t('system.message.needValue')" 
                      resize autofocus validate-event 
                      :readonly="!isNew" :maxlength="45" show-word-limit />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item v-if="isNew" class="cg-item-password" :label="$t('sysUser.field.password')" prop="password" :size="$store.state.app.size" >
            <el-input v-model="record.password" name="password" 
                      type="password" 
                      :label="$t('sysUser.field.password')" :placeholder="$t('system.message.needValue')" 
                      show-password resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="32" show-word-limit />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item v-if="isNew" class="cg-item-password" :label="$t('system.message.passwordConfirm')" prop="passwordConfirm" :size="$store.state.app.size" >
            <el-input v-model="record.passwordConfirm" name="passwordConfirm" 
                      type="password" 
                      :label="$t('system.message.passwordConfirm')" :placeholder="$t('system.message.needValue')" 
                      show-password resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="32" show-word-limit />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-email" :label="$t('sysUser.field.email')" prop="email" :size="$store.state.app.size" >
            <el-input v-model="record.email" name="email" 
                      type="email" 
                      :label="$t('sysUser.field.email')" :placeholder="$t('system.message.unknown')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="50" show-word-limit clearable />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-image" :label="$t('sysUser.field.icon')" prop="icon" :size="$store.state.app.size" >
            <cg-image v-model="blobRecord.icon" name="icon" :title="$t('sysUser.field.icon')"  :readonly="isDetail"  :id="record.id" generatorName="sysUser" field="icon" :maxWidth="128" :maxHeight="128" alignCenter 
                      @input="recordChanged=true" />
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
const mixinContext = require.context('.', false, /CgFormRegister-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default {
  name: 'CgFormRegister',
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
      path: 'register',
      title: this.$t('sysUser.title.'+this.path),
      rules: {},
      idField: 'id',
      idSaved: this.openParams().record && typeof this.openParams().record === 'object' ? this.openParams().record.id : null,
      onChange: typeof this.openParams().onChange === 'function' ? this.openParams().onChange : null,
      recordChanged: false,
      recordLoading: false,
      fixedFields: typeof this.openParams().fixedFields === 'object' ? this.openParams().fixedFields : {},
      openMode: this.openParams().openMode ? this.openParams().openMode : null,
      record: this.openParams().record && typeof this.openParams().record === 'object' ? this.openParams().record : {},
      blobRecord:
      {
        icon: {
          name: this.openParams().record && typeof this.openParams().record === 'object' ? this.openParams().record.icon : null,
          blob: null
        },
      },
      needDefaultFromServer: true,
      dictionary: {
        dictOrgCode: [],
        dictSex: [],
        dictIdType: []
      },
      needLoadDictionary: true,
      generatorName: 'sysUser',
      baseUrl: '/framework/sysUser'
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
      return (this.mobile?'cg-form-cell ':'')+'cg-no-border cg-form-register'
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
  },
  mounted() {
    cgForm.form_mounted(this)
  },
  methods: {
    openParams: function() {
      return this.dialogParams ? this.dialogParams : this.$route.query
    },
    newRecord: function() {
        return {
            locked: false,
            state: true,
            passwordErrorTimes: 0,
            name: '',
            realName: '',
            sex: '1',
            idType: 1,
            idNumber: '',
            password: '123456',
            passwordConfirm: '123456',
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
