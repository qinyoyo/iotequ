
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
                      type="text" 
                      :label="$t('ewUser.field.name')" :placeholder="$t('system.message.needValue')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="32" show-word-limit />
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
                      type="text" 
                      :label="$t('ewUser.field.idNo')" :placeholder="$t('system.message.needValue')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="45" show-word-limit />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="8">
          <el-form-item class="cg-item-text" :label="$t('ewUser.field.mobilePhone')" prop="mobilePhone" :size="$store.state.app.size" >
            <el-input v-model="record.mobilePhone" name="mobilePhone" 
                      type="text" 
                      :label="$t('ewUser.field.mobilePhone')" :placeholder="$t('system.message.unknown')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="45" show-word-limit clearable />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item class="cg-item-text" :label="$t('ewUser.field.email')" prop="email" :size="$store.state.app.size" >
            <el-input v-model="record.email" name="email" 
                      type="text" 
                      :label="$t('ewUser.field.email')" :placeholder="$t('system.message.unknown')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="45" show-word-limit clearable />
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
                      type="text" 
                      :label="$t('ewUser.field.memberGroup')" :placeholder="$t('system.message.unknown')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="45" show-word-limit clearable />
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
import cgForm from '@/utils/cgForm'
import rulesObject from './rules.js'
import ParentForm from '@/views/common-views/components/form'
const Comp = {
  name: 'CgFormEwUser',
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
      path: 'record',
      idField: 'userNo',
      userNoSaved: this.openParams().record && typeof this.openParams().record === 'object' ? this.openParams().record.userNo : null,
      dictionary: {
        dictGender: [],
        dictIdType: []
      },
      needLoadDictionary: true,
      generatorName: 'ewUser',
      baseUrl: '/ewallet/ewUser'
    }
  },
  methods: {
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
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /CgFormEwUser-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
    mixins
}
</script>
