
<template>
  <div :class="mobile || height==0 ? 'cg-form':'cg-form just-v-scroll'" :style="mobile || height==0?'':'height:'+height+'px'">
    <el-form ref="cgForm" v-loading="recordLoading" :model="record" v-cg-form-adjust
                          :class="className" :rules="rules" 
             :label-position="labelPosition" :label-width="labelWidth" :size="$store.state.app.size" 
             hide-required-asterisk >
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-date cg-auto-focus" :label="$t('devAuthConfig.field.beginAt')" prop="beginAt" :size="$store.state.app.size" >
            <span slot="label"><cg-icon icon="fa fa-calendar" :size="14" :width="20"/>{{$t('devAuthConfig.field.beginAt')}}</span>
            <cg-date-picker v-model="record.beginAt" name="beginAt" 
                            :align="mobile?'right':'center'" type="date" :title="$t('devAuthConfig.field.beginAt')" :readonly="isDetail" 
                            clearable />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-date" :label="$t('devAuthConfig.field.endAt')" prop="endAt" :size="$store.state.app.size" >
            <span slot="label"><cg-icon icon="fa fa-calendar" :size="14" :width="20"/>{{$t('devAuthConfig.field.endAt')}}</span>
            <cg-date-picker v-model="record.endAt" name="endAt" 
                            :align="mobile?'right':'center'" type="date" :title="$t('devAuthConfig.field.endAt')" :readonly="isDetail" 
                            clearable />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-time" :label="$t('devAuthConfig.field.startTime')" prop="startTime" :size="$store.state.app.size" >
            <span slot="label"><cg-icon icon="fa fa-clock-o" :size="14" :width="20"/>{{$t('devAuthConfig.field.startTime')}}</span>
            <cg-date-picker v-model="record.startTime" name="startTime" 
                            :align="mobile?'right':'center'" type="time" :title="$t('devAuthConfig.field.startTime')" :readonly="isDetail" 
                            format="HH:mm" clearable />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-time" :label="$t('devAuthConfig.field.endTime')" prop="endTime" :size="$store.state.app.size" >
            <span slot="label"><cg-icon icon="fa fa-clock-o" :size="14" :width="20"/>{{$t('devAuthConfig.field.endTime')}}</span>
            <cg-date-picker v-model="record.endTime" name="endTime" 
                            :align="mobile?'right':'center'" type="time" :title="$t('devAuthConfig.field.endTime')" :readonly="isDetail" 
                            format="HH:mm" clearable />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-boolean" :label="$t('devAuthConfig.field.onlyWorkDay')" prop="onlyWorkDay" :size="$store.state.app.size" >
            <el-switch v-model="record.onlyWorkDay" name="onlyWorkDay" :active-text="mobile?'':$t('devAuthConfig.field.onlyWorkDay')" inactive-text="" :disabled="isDetail"  />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-select" :label="$t('devAuthConfig.field.auth')" prop="auth" :size="$store.state.app.size" >
            <cg-select v-model="record.auth" name="auth"
                       :dictionary="dictionary.dictAuth" :readonly="isDetail" :filterable="false" :allow-create="false" numberic :placeholder="$t('system.message.needValue')" />
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
  name: 'CgFormAuthConfig',
  mixins: [ParentForm],
  props: {
  },
  data() {
    return {
      defaultLabelPosition: 'top',
      rulesObject,
      path: 'record',
      idField: 'id',
      idSaved: this.openParams().record && typeof this.openParams().record === 'object' ? this.openParams().record.id : null,
      dictionary: {
        dictAuth: []
      },
      needLoadDictionary: true,
      generatorName: 'devAuthConfig',
      baseUrl: '/reader/devAuthConfig'
    }
  },
  methods: {
    newRecord: function() {
        return {
            roleId: 0,
            onlyWorkDay: false,
            auth: 4,
        }
    },
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /CgFormAuthConfig-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
    mixins
}
</script>
