
<template>
  <div :class="mobile || height==0 ? 'cg-form':'cg-form just-v-scroll'" :style="mobile || height==0?'':'height:'+height+'px'">
    <el-form ref="cgForm" v-loading="recordLoading" :model="record" v-cg-form-adjust
                          :class="className" :rules="rules" 
             :label-position="labelPosition" :label-width="labelWidth" :size="$store.state.app.size" 
             hide-required-asterisk >
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-select cg-auto-focus" :label="$t('adShiftTime.field.name')" prop="name" :size="$store.state.app.size" >
            <cg-select v-model="record.name" name="name"
                       :dictionary="dictionary.dictName" :readonly="isDetail" :filterable="false" :allow-create="false" :placeholder="$t('system.message.needValue')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-select" :label="$t('adShiftTime.field.weekDays')" prop="weekDays" :size="$store.state.app.size" >
            <cg-select v-model="record.weekDays" name="weekDays"
                       :dictionary="dictionary.dictWeekDays" :readonly="isDetail" :filterable="false" :allow-create="false" multiple :placeholder="$t('system.message.needValue')" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-time" :label="$t('adShiftTime.field.startWorkTime')" prop="startWorkTime" :size="$store.state.app.size" >
            <span slot="label"><cg-icon icon="fa fa-clock-o" :size="14" :width="20"/>{{$t('adShiftTime.field.startWorkTime')}}</span>
            <cg-date-picker v-model="record.startWorkTime" name="startWorkTime" 
                            :align="mobile?'right':'center'" type="time" :title="$t('adShiftTime.field.startWorkTime')" :readonly="isDetail" 
                            format="HH:mm" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-time" :label="$t('adShiftTime.field.endWorkTime')" prop="endWorkTime" :size="$store.state.app.size" >
            <span slot="label"><cg-icon icon="fa fa-clock-o" :size="14" :width="20"/>{{$t('adShiftTime.field.endWorkTime')}}</span>
            <cg-date-picker v-model="record.endWorkTime" name="endWorkTime" 
                            :align="mobile?'right':'center'" type="time" :title="$t('adShiftTime.field.endWorkTime')" :readonly="isDetail" 
                            format="HH:mm" />
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
const Comp = {
  name: 'CgFormAdShiftTime',
  mixins: [ParentForm],
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
      dictionary: {
        dictName: [],
        dictWeekDays: []
      },
      needLoadDictionary: true,
      generatorName: 'adShiftTime',
      baseUrl: '/attendance/shifttime/adShiftTime'
    }
  },
  methods: {
    newRecord: function() {
        return {
            shiftId: 0,
            name: '',
            weekDays: '1,2,3,4,5',
            startWorkTime: new Date(),
            endWorkTime: new Date(),
        }
    },
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /CgFormAdShiftTime-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
    mixins
}
</script>
