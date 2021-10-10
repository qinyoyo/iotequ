
<template>
  <div :class="mobile || height==0 ? 'cg-form':'cg-form just-v-scroll'" :style="mobile || height==0?'':'height:'+height+'px'">
    <el-form ref="cgForm" v-loading="recordLoading" :model="record" v-cg-form-adjust
                          :class="className" :rules="rules" 
             :label-position="labelPosition" :label-width="labelWidth" :size="$store.state.app.size" 
             hide-required-asterisk >
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-radio cg-auto-focus" :label="$t('adSpecialShift.field.shiftMode')" prop="shiftMode" :size="$store.state.app.size" >
            <cg-radio v-model="record.shiftMode" name="shiftMode" :dictionary="dictionary.dictShiftMode" :readonly="isDetail" numberic  />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-text" :label="$t('adSpecialShift.field.name')" prop="name" :size="$store.state.app.size" >
            <el-input v-model="record.name" name="name" 
                      type="text" 
                      :label="$t('adSpecialShift.field.name')" :placeholder="$t('system.message.needValue')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="45" show-word-limit />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-radio" :label="$t('adSpecialShift.field.modeProperty')" prop="modeProperty" :size="$store.state.app.size" >
            <cg-radio v-model="record.modeProperty" name="modeProperty" :dictionary="dictionary.dictModeProperty" :readonly="isDetail" numberic  />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-radio" :label="$t('adSpecialShift.field.sexProperty')" :title="$t('adSpecialShift.field.sexPropertyValid')" prop="sexProperty" :size="$store.state.app.size" >
            <cg-radio v-model="record.sexProperty" name="sexProperty" :dictionary="dictionary.dictSexProperty" :readonly="isDetail" numberic  />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-date" :label="$t('adSpecialShift.field.startDate')" prop="startDate" :size="$store.state.app.size" >
            <span slot="label"><cg-icon icon="fa fa-calendar" :size="14" :width="20"/>{{$t('adSpecialShift.field.startDate')}}</span>
            <cg-date-picker v-model="record.startDate" name="startDate" 
                            :align="mobile?'right':'center'" type="date" :title="$t('adSpecialShift.field.startDate')" :readonly="isDetail" 
                            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-date" :label="$t('adSpecialShift.field.endDate')" prop="endDate" :size="$store.state.app.size" >
            <span slot="label"><cg-icon icon="fa fa-calendar" :size="14" :width="20"/>{{$t('adSpecialShift.field.endDate')}}</span>
            <cg-date-picker v-model="record.endDate" name="endDate" 
                            :align="mobile?'right':'center'" type="date" :title="$t('adSpecialShift.field.endDate')" :readonly="isDetail" 
                            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item class="cg-item-select" :label="$t('adSpecialShift.field.orgCodes')" :title="$t('adSpecialShift.field.orgCodesValid')" prop="orgCodes" :size="$store.state.app.size" >
        <cg-cascader v-model="record.orgCodes" name="orgCodes"
                     :readonly="isDetail" :filterable="false" multiple collapse-tags 
                     :dictionary="dictionary.dictOrgCodes" :placeholder="$t('system.message.needValue')" />
      </el-form-item>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-number" :label="$t('adSpecialShift.field.ageProperty0')" :title="$t('adSpecialShift.field.ageProperty0Valid')" prop="ageProperty0" :size="$store.state.app.size" >
            <cg-number-input v-model="record.ageProperty0" name="ageProperty0" 
                             :readonly="isDetail" 
                             :label="$t('adSpecialShift.field.ageProperty0')" :placeholder="$t('system.message.unknown')" clearable :precision="0" 
                             :min="0" :step="1" :title="$t('system.message.valueRange') + ': 0 - *'" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-number" :label="$t('adSpecialShift.field.ageProperty1')" :title="$t('adSpecialShift.field.ageProperty1Valid')" prop="ageProperty1" :size="$store.state.app.size" >
            <cg-number-input v-model="record.ageProperty1" name="ageProperty1" 
                             :readonly="isDetail" 
                             :label="$t('adSpecialShift.field.ageProperty1')" :placeholder="$t('system.message.unknown')" clearable :precision="0" 
                             :min="0" :step="1" :title="$t('system.message.valueRange') + ': 0 - *'" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-number" :label="$t('adSpecialShift.field.levelProperty0')" :title="$t('adSpecialShift.field.levelProperty0Valid')" prop="levelProperty0" :size="$store.state.app.size" >
            <cg-number-input v-model="record.levelProperty0" name="levelProperty0" 
                             :readonly="isDetail" 
                             :label="$t('adSpecialShift.field.levelProperty0')" :placeholder="$t('system.message.unknown')" clearable :precision="0" 
                             :min="0" :step="1" :title="$t('system.message.valueRange') + ': 0 - *'" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-number" :label="$t('adSpecialShift.field.levelProperty1')" :title="$t('adSpecialShift.field.levelProperty1Valid')" prop="levelProperty1" :size="$store.state.app.size" >
            <cg-number-input v-model="record.levelProperty1" name="levelProperty1" 
                             :readonly="isDetail" 
                             :label="$t('adSpecialShift.field.levelProperty1')" :placeholder="$t('system.message.unknown')" clearable :precision="0" 
                             :min="0" :step="1" :title="$t('system.message.valueRange') + ': 0 - *'" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item class="cg-item-text" :label="$t('adSpecialShift.field.description')" prop="description" :size="$store.state.app.size" >
        <el-input v-model="record.description" name="description" 
                  type="text" 
                  :label="$t('adSpecialShift.field.description')" :placeholder="$t('system.message.unknown')" 
                  resize autofocus validate-event 
                  :readonly="isDetail" :maxlength="45" show-word-limit clearable />
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
  name: 'CgFormAdSpecialShift',
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
        dictShiftMode: [],
        dictModeProperty: [],
        dictSexProperty: [],
        dictOrgCodes: []
      },
      needLoadDictionary: true,
      generatorName: 'adSpecialShift',
      baseUrl: '/attendance/specialshift/adSpecialShift'
    }
  },
  methods: {
    just4elInputNumberNullBug: function() {
      if (this.record.ageProperty0 === null) this.record.ageProperty0 = undefined
      if (this.record.ageProperty1 === null) this.record.ageProperty1 = undefined
      if (this.record.levelProperty0 === null) this.record.levelProperty0 = undefined
      if (this.record.levelProperty1 === null) this.record.levelProperty1 = undefined
    },
    newRecord: function() {
        return {
            shiftMode: 0,
            name: '',
            startDate: new Date(),
            endDate: new Date(),
            orgCodes: '',
        }
    },
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /CgFormAdSpecialShift-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
    mixins
}
</script>
