
<template>
  <div :class="mobile || height==0 ? 'cg-form':'cg-form just-v-scroll'" :style="mobile || height==0?'':'height:'+height+'px'">
    <el-form ref="cgForm" v-loading="recordLoading" :model="record" v-cg-form-adjust
                          :class="className" :rules="rules" 
             :label-position="labelPosition" :label-width="labelWidth" :size="$store.state.app.size" 
             hide-required-asterisk >
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-text cg-auto-focus" :label="$t('adShift.field.name')" prop="name" :size="$store.state.app.size" >
            <el-input v-model="record.name" name="name" 
                      type="text" 
                      :label="$t('adShift.field.name')" :placeholder="$t('system.message.needValue')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="45" show-word-limit />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-radio" :label="$t('adShift.field.adMode')" prop="adMode" :size="$store.state.app.size" >
            <cg-radio v-model="record.adMode" name="adMode" :dictionary="dictionary.dictAdMode" :readonly="isDetail" numberic  />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="8">
          <el-form-item class="cg-item-date" :label="$t('adShift.field.startDate')" prop="startDate" :size="$store.state.app.size" >
            <span slot="label"><cg-icon icon="fa fa-calendar" :size="14" :width="20"/>{{$t('adShift.field.startDate')}}</span>
            <cg-date-picker v-model="record.startDate" name="startDate" 
                            :align="mobile?'right':'center'" type="date" :title="$t('adShift.field.startDate')" :readonly="isDetail" 
                            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item class="cg-item-date" :label="$t('adShift.field.endDate')" prop="endDate" :size="$store.state.app.size" >
            <span slot="label"><cg-icon icon="fa fa-calendar" :size="14" :width="20"/>{{$t('adShift.field.endDate')}}</span>
            <cg-date-picker v-model="record.endDate" name="endDate" 
                            :align="mobile?'right':'center'" type="date" :title="$t('adShift.field.endDate')" :readonly="isDetail" 
                            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item class="cg-item-number" :label="$t('adShift.field.minutesPerDay')" prop="minutesPerDay" :size="$store.state.app.size" >
            <cg-number-input v-model="record.minutesPerDay" name="minutesPerDay" 
                             :readonly="isDetail" 
                             :label="$t('adShift.field.minutesPerDay')" :placeholder="$t('system.message.needValue')" :precision="0" 
                             :min="0" :step="1" :title="$t('system.message.valueRange') + ': 0 - *'" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item class="cg-item-text" :label="$t('adShift.field.description')" prop="description" :size="$store.state.app.size" >
        <el-input v-model="record.description" name="description" 
                  type="text" 
                  :label="$t('adShift.field.description')" :placeholder="$t('system.message.unknown')" 
                  resize autofocus validate-event 
                  :readonly="isDetail" :maxlength="200" show-word-limit clearable />
      </el-form-item>
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
  name: 'CgFormAdShift',
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
        dictAdMode: []
      },
      needLoadDictionary: true,
      generatorName: 'adShift',
      baseUrl: '/attendance/shift/adShift'
    }
  },
  methods: {
    just4elInputNumberNullBug: function() {
      if (this.record.minutesPerDay === null) this.record.minutesPerDay = undefined
    },
    newRecord: function() {
        return {
            name: '',
            adMode: 1,
            startDate: new Date(),
            endDate: new Date(),
            minutesPerDay: 480,
        }
    },
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /CgFormAdShift-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
    mixins
}
</script>
