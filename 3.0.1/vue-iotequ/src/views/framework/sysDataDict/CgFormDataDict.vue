
<template>
  <div :class="mobile || height==0 ? 'cg-form':'cg-form just-v-scroll'" :style="mobile || height==0?'':'height:'+height+'px'">
    <el-form ref="cgForm" v-loading="recordLoading" :model="record" v-cg-form-adjust
                          :class="className" :rules="rules" 
             :label-position="labelPosition" :label-width="labelWidth" :size="$store.state.app.size" 
             hide-required-asterisk >
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-text cg-auto-focus" :label="$t('sysDataDict.field.dict')" prop="dict" :size="$store.state.app.size" >
            <el-input v-model="record.dict" name="dict" 
                      type="text" 
                      :label="$t('sysDataDict.field.dict')" :placeholder="$t('system.message.needValue')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="45" show-word-limit />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-text" :label="$t('sysDataDict.field.code')" prop="code" :size="$store.state.app.size" >
            <el-input v-model="record.code" name="code" 
                      type="text" 
                      :label="$t('sysDataDict.field.code')" :placeholder="$t('system.message.needValue')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="45" show-word-limit />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-text" :label="$t('sysDataDict.field.text')" prop="text" :size="$store.state.app.size" >
            <el-input v-model="record.text" name="text" 
                      v-i18n-view
                      type="text" 
                      :label="$t('sysDataDict.field.text')" :placeholder="$t('system.message.needValue')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="100" show-word-limit />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-number" :label="$t('sysDataDict.field.orderNum')" prop="orderNum" :size="$store.state.app.size" >
            <cg-number-input v-model="record.orderNum" name="orderNum" 
                             :readonly="isDetail" 
                             :label="$t('sysDataDict.field.orderNum')" :placeholder="$t('system.message.unknown')" clearable :precision="0" 
                             :min="0" :step="1" :title="$t('system.message.valueRange') + ': 0 - *'" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <div v-if="!isDetail" class="cg-form-buttons">
      <el-divider />
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
const mixins = [ParentForm]
const mixinContext = require.context('.', false, /CgFormDataDict-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default {
  name: 'CgFormDataDict',
  mixins,
  props: {
  },
  data() {
    return {
      defaultLabelPosition: 'top',
      rulesObject,
      continueAdd: true,
      path: 'record',
      idField: 'id',
      idSaved: this.openParams().record && typeof this.openParams().record === 'object' ? this.openParams().record.id : null,
      generatorName: 'sysDataDict',
      baseUrl: '/framework/sysDataDict'
    }
  },
  methods: {
    just4elInputNumberNullBug: function() {
      if (this.record.orderNum === null) this.record.orderNum = undefined
    },
    newRecord: function() {
        return {
            dict: this.record.dict,
            code: '',
            text: '',
        }
    },
  }
}
</script>
