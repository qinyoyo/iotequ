
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
          <el-form-item class="cg-item-text" :label="$t('sysUser.field.name')" :title="$t('sysUser.field.nameValid')" prop="name" :size="$store.state.app.size" >
            <el-input v-model="record.name" name="name" 
                      type="text" 
                      :label="$t('sysUser.field.name')" :placeholder="$t('system.message.needValue')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="32" show-word-limit />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-mltext" :label="$t('sysUser.field.realName')" prop="realName" :size="$store.state.app.size" >
            <cg-input v-model="record.realName" name="realName" 
                      type="mltext" 
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
import cgForm from '@/utils/cgForm'
import rulesObject from './rules.js'
import ParentForm from '@/views/common-views/components/form'
const Comp = {
  name: 'CgFormRegister',
  mixins: [ParentForm],
  props: {
    routeParams: {
      type: Object,
      default: null
    }
  },
  data() {
    return {
      defaultLabelPosition: 'top',
      rulesObject,
      isDialogForm: true,
      path: 'register',
      idField: 'id',
      idSaved: this.openParams().record && typeof this.openParams().record === 'object' ? this.openParams().record.id : null,
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
  methods: {
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
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /CgFormRegister-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
    mixins
}
</script>
