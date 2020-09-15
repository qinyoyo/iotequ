
<template>
  <div :class="mobile || height==0 ? 'cg-form':'cg-form just-v-scroll'" :style="mobile || height==0?'':'height:'+height+'px'">
    <el-form ref="cgForm" v-loading="recordLoading" :model="record" v-cg-form-adjust
                          :class="className" :rules="rules" 
             :label-position="labelPosition" :label-width="labelWidth" :size="$store.state.app.size" 
             hide-required-asterisk >
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-text cg-auto-focus" :label="$t('sysFlowProcess.field.operation')" prop="operation" :size="$store.state.app.size" >
            <el-input v-model="record.operation" name="operation" 
                      type="text" 
                      :label="$t('sysFlowProcess.field.operation')" :placeholder="$t('system.message.unknown')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="36" show-word-limit clearable />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-text" :label="$t('sysFlowProcess.field.selection')" prop="selection" :size="$store.state.app.size" >
            <el-input v-model="record.selection" name="selection" 
                      type="text" 
                      :label="$t('sysFlowProcess.field.selection')" :placeholder="$t('system.message.unknown')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="36" show-word-limit clearable />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-text" :label="$t('sysFlowProcess.field.stateName0')" prop="stateName0" :size="$store.state.app.size" >
            <el-input v-model="record.stateName0" name="stateName0" 
                      type="text" 
                      :label="$t('sysFlowProcess.field.stateName0')" :placeholder="$t('system.message.unknown')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="45" show-word-limit clearable />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-text" :label="$t('sysFlowProcess.field.stateName1')" prop="stateName1" :size="$store.state.app.size" >
            <el-input v-model="record.stateName1" name="stateName1" 
                      type="text" 
                      :label="$t('sysFlowProcess.field.stateName1')" :placeholder="$t('system.message.unknown')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="45" show-word-limit clearable />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-left_join" :label="$t('sysFlowProcess.field.operator')" prop="operator" :size="$store.state.app.size" >
            <el-input v-model="record.operator" name="operator" 
                      type="left_join" 
                      :label="$t('sysFlowProcess.field.operator')" :placeholder="$t('system.message.needValue')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="36" show-word-limit />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-datetime" :label="$t('sysFlowProcess.field.time')" prop="time" :size="$store.state.app.size" >
            <span slot="label"><cg-icon icon="fa fa-calendar-plus-o" :size="14" :width="20"/>{{$t('sysFlowProcess.field.time')}}</span>
            <cg-date-picker v-model="record.time" name="time" 
                            :align="mobile?'right':'center'" type="datetime" :title="$t('sysFlowProcess.field.time')" :readonly="isDetail" 
                            format="yyyy-MM-dd" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-text" :label="$t('sysFlowProcess.field.note')" prop="note" :size="$store.state.app.size" >
            <el-input v-model="record.note" name="note" 
                      type="text" 
                      :label="$t('sysFlowProcess.field.note')" :placeholder="$t('system.message.unknown')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="200" show-word-limit clearable />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-left_join" :label="$t('sysFlowProcess.field.nextOperator')" prop="nextOperator" :size="$store.state.app.size" >
            <el-input v-model="record.nextOperator" name="nextOperator" 
                      type="left_join" 
                      :label="$t('sysFlowProcess.field.nextOperator')" :placeholder="$t('system.message.unknown')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="45" show-word-limit clearable />
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
const mixins = [ParentForm]
const mixinContext = require.context('.', false, /CgFormFlowProcess-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default {
  name: 'CgFormFlowProcess',
  mixins,
  props: {
    dialogParams: {
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
      generatorName: 'sysFlowProcess',
      baseUrl: '/framework/sysFlowProcess'
    }
  },
  methods: {
  }
}
</script>
