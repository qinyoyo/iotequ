
<template>
  <div :class="mobile || height==0 ? 'cg-form':'cg-form just-v-scroll'" :style="mobile || height==0?'':'height:'+height+'px'">
    <el-form ref="cgForm" v-loading="recordLoading" :model="record" v-cg-form-adjust
                          :class="className" :rules="rules" 
             :label-position="labelPosition" :label-width="labelWidth" :size="$store.state.app.size" 
             hide-required-asterisk >
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-select cg-auto-focus" :label="$t('payLogin.field.shopId')" prop="shopId" :size="$store.state.app.size" >
            <cg-select v-model="record.shopId" name="shopId"
                       :dictionary="dictionary.dictShopId" :readonly="isDetail" :filterable="false" :allow-create="false" numberic :placeholder="$t('system.message.needValue')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-left_join" :label="$t('payLogin.field.operatorId')" prop="operatorId" :size="$store.state.app.size" >
            <el-input v-model="record.operatorId" name="operatorId" 
                      type="left_join" 
                      :label="$t('payLogin.field.operatorId')" :placeholder="$t('system.message.needValue')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-text" :label="$t('payLogin.field.batchNo')" prop="batchNo" :size="$store.state.app.size" >
            <el-input v-model="record.batchNo" name="batchNo" 
                      type="text" 
                      :label="$t('payLogin.field.batchNo')" :placeholder="$t('system.message.needValue')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="45" show-word-limit />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-datetime" :label="$t('payLogin.field.loginTime')" prop="loginTime" :size="$store.state.app.size" >
            <span slot="label"><cg-icon icon="fa fa-calendar-plus-o" :size="14" :width="20"/>{{$t('payLogin.field.loginTime')}}</span>
            <cg-date-picker v-model="record.loginTime" name="loginTime" 
                            :align="mobile?'right':'center'" type="datetime" :title="$t('payLogin.field.loginTime')" :readonly="isDetail" 
                            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-datetime" :label="$t('payLogin.field.logoutTime')" prop="logoutTime" :size="$store.state.app.size" >
            <span slot="label"><cg-icon icon="fa fa-calendar-plus-o" :size="14" :width="20"/>{{$t('payLogin.field.logoutTime')}}</span>
            <cg-date-picker v-model="record.logoutTime" name="logoutTime" 
                            :align="mobile?'right':'center'" type="datetime" :title="$t('payLogin.field.logoutTime')" :readonly="isDetail" 
                            clearable />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-text" :label="$t('payLogin.field.deviceStream')" prop="deviceStream" :size="$store.state.app.size" >
            <el-input v-model="record.deviceStream" name="deviceStream" 
                      type="text" 
                      :label="$t('payLogin.field.deviceStream')" :placeholder="$t('system.message.unknown')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="45" show-word-limit clearable />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-text" :label="$t('payLogin.field.appVersion')" prop="appVersion" :size="$store.state.app.size" >
            <el-input v-model="record.appVersion" name="appVersion" 
                      type="text" 
                      :label="$t('payLogin.field.appVersion')" :placeholder="$t('system.message.needValue')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="45" show-word-limit />
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
  name: 'CgFormPayLogin',
  mixins: [ParentForm],
  props: {
    routeParams: {
      type: Object,
      default: null
    }
  },
  data() {
    return {
      allowEditRecord: false,
      allowAddRecord: false,
      defaultLabelPosition: 'top',
      rulesObject,
      isDialogForm: true,
      path: 'record',
      idField: 'id',
      idSaved: this.openParams().record && typeof this.openParams().record === 'object' ? this.openParams().record.id : null,
      dictionary: {
        dictShopId: []
      },
      needLoadDictionary: true,
      generatorName: 'payLogin',
      baseUrl: '/pay/payLogin'
    }
  },
  methods: {
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /CgFormPayLogin-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
    mixins
}
</script>
