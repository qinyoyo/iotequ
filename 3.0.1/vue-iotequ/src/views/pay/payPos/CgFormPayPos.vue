
<template>
  <div :class="mobile || height==0 ? 'cg-form':'cg-form just-v-scroll'" :style="mobile || height==0?'':'height:'+height+'px'">
    <el-form ref="cgForm" v-loading="recordLoading" :model="record" v-cg-form-adjust
                          :class="className" :rules="rules" 
             :label-position="labelPosition" :label-width="labelWidth" :size="$store.state.app.size" 
             hide-required-asterisk >
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-text cg-auto-focus" :label="$t('payPos.field.no')" prop="no" :size="$store.state.app.size" >
            <el-input v-model="record.no" name="no" 
                      type="text" 
                      :label="$t('payPos.field.no')" :placeholder="$t('system.message.needValue')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="45" show-word-limit />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-select" :label="$t('payPos.field.shopId')" prop="shopId" :size="$store.state.app.size" >
            <cg-select v-model="record.shopId" name="shopId"
                       :dictionary="dictionary.dictShopId" :readonly="isDetail" :filterable="false" :allow-create="false" numberic :placeholder="$t('system.message.needValue')" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-number" :label="$t('payPos.field.loginId')" prop="loginId" :size="$store.state.app.size" >
            <cg-number-input v-model="record.loginId" name="loginId" 
                             :readonly="isDetail" 
                             :label="$t('payPos.field.loginId')" :placeholder="$t('system.message.unknown')" clearable :precision="0" 
                             :min="0" :step="1" :title="$t('system.message.valueRange') + ': 0 - *'" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-boolean" :label="$t('payPos.field.ewalletActive')" prop="ewalletActive" :size="$store.state.app.size" >
            <el-switch v-model="record.ewalletActive" name="ewalletActive" :active-text="mobile?'':$t('payPos.field.ewalletActive')" inactive-text="" :disabled="isDetail"  />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-select" :label="$t('payPos.field.countProjectList')" prop="countProjectList" :size="$store.state.app.size" >
            <cg-select v-model="record.countProjectList" name="countProjectList"
                       :dictionary="dictionary.dictCountProjectList" :readonly="isDetail" :filterable="false" :allow-create="false" multiple :placeholder="$t('system.message.unknown')" clearable />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-select" :label="$t('payPos.field.timeProjectList')" prop="timeProjectList" :size="$store.state.app.size" >
            <cg-select v-model="record.timeProjectList" name="timeProjectList"
                       :dictionary="dictionary.dictTimeProjectList" :readonly="isDetail" :filterable="false" :allow-create="false" multiple :placeholder="$t('system.message.unknown')" clearable />
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
  name: 'CgFormPayPos',
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
      idField: 'id',
      idSaved: this.openParams().record && typeof this.openParams().record === 'object' ? this.openParams().record.id : null,
      dictionary: {
        dictShopId: [],
        dictCountProjectList: [],
        dictTimeProjectList: []
      },
      needLoadDictionary: true,
      generatorName: 'payPos',
      baseUrl: '/pay/payPos'
    }
  },
  methods: {
    just4elInputNumberNullBug: function() {
      if (this.record.loginId === null) this.record.loginId = undefined
    },
    newRecord: function() {
        return {
            no: '',
            shopId: 0,
            ewalletActive: true,
        }
    },
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /CgFormPayPos-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
    mixins
}
</script>
