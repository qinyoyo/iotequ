
<template>
  <div :class="mobile || height==0 ? 'cg-form':'cg-form just-v-scroll'" :style="mobile || height==0?'':'height:'+height+'px'">
    <el-form ref="cgForm" v-loading="recordLoading" :model="record" v-cg-form-adjust
                          :class="className" :rules="rules" 
             :label-position="labelPosition" :label-width="labelWidth" :size="$store.state.app.size" 
             hide-required-asterisk >
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-date cg-auto-focus" :label="$t('adException.field.startDate')" prop="startDate" :size="$store.state.app.size" >
            <span slot="label"><cg-icon icon="fa fa-calendar" :size="14" :width="20"/>{{$t('adException.field.startDate')}}</span>
            <cg-date-picker v-model="record.startDate" name="startDate" 
                            :align="mobile?'right':'center'" type="date" :title="$t('adException.field.startDate')" :readonly="isDetail" 
                            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-date" :label="$t('adException.field.endDate')" prop="endDate" :size="$store.state.app.size" >
            <span slot="label"><cg-icon icon="fa fa-calendar" :size="14" :width="20"/>{{$t('adException.field.endDate')}}</span>
            <cg-date-picker v-model="record.endDate" name="endDate" 
                            :align="mobile?'right':'center'" type="date" :title="$t('adException.field.endDate')" :readonly="isDetail" 
                            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-select" :label="$t('adException.field.weekDay')" prop="weekDay" :size="$store.state.app.size" >
            <cg-select v-model="record.weekDay" name="weekDay"
                       :dictionary="dictionary.dictWeekDay" :readonly="isDetail" :filterable="false" :allow-create="false" numberic :placeholder="$t('system.message.needValue')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-text" :label="$t('adException.field.description')" prop="description" :size="$store.state.app.size" >
            <el-input v-model="record.description" name="description" 
                      type="text" 
                      :label="$t('adException.field.description')" :placeholder="$t('system.message.unknown')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="100" show-word-limit clearable />
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
const mixinContext = require.context('.', false, /CgFormAdException-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default {
  name: 'CgFormAdException',
  mixins,
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
        dictWeekDay: []
      },
      needLoadDictionary: true,
      generatorName: 'adException',
      baseUrl: '/attendance/exception/adException'
    }
  },
  methods: {
    newRecord: function() {
        return {
            shiftId: 0,
            startDate: new Date(),
            endDate: new Date(),
            weekDay: 0,
        }
    },
  }
}
</script>
