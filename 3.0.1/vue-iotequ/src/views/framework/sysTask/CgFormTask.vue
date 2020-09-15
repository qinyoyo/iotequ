
<template>
  <div :class="mobile || height==0 ? 'cg-form':'cg-form just-v-scroll'" :style="mobile || height==0?'':'height:'+height+'px'">
    <el-form ref="cgForm" v-loading="recordLoading" :model="record" v-cg-form-adjust
                          :class="className" :rules="rules" 
             :label-position="labelPosition" :label-width="labelWidth" :size="$store.state.app.size" 
             hide-required-asterisk >
      <el-row :gutter="mobile?0:20">
        <el-col :span="8">
          <el-form-item class="cg-item-text cg-auto-focus" :label="$t('sysTask.field.name')" prop="name" :size="$store.state.app.size" >
            <el-input v-model="record.name" name="name" 
                      type="text" 
                      :label="$t('sysTask.field.name')" :placeholder="$t('system.message.needValue')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="45" show-word-limit />
          </el-form-item>
        </el-col>
        <el-col :span="16">
          <el-form-item class="cg-item-text" :label="$t('sysTask.field.description')" prop="description" :size="$store.state.app.size" >
            <el-input v-model="record.description" name="description" 
                      type="text" 
                      :label="$t('sysTask.field.description')" :placeholder="$t('system.message.unknown')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="300" show-word-limit clearable />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="8">
          <el-form-item class="cg-item-text" :label="$t('sysTask.field.sceduleYears')" prop="sceduleYears" :size="$store.state.app.size" >
            <el-input v-model="record.sceduleYears" name="sceduleYears" 
                      type="text" 
                      :label="$t('sysTask.field.sceduleYears')" :placeholder="$t('system.message.needValue')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="100" show-word-limit />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item class="cg-item-text" :label="$t('sysTask.field.scheduleMonths')" prop="scheduleMonths" :size="$store.state.app.size" >
            <el-input v-model="record.scheduleMonths" name="scheduleMonths" 
                      type="text" 
                      :label="$t('sysTask.field.scheduleMonths')" :placeholder="$t('system.message.needValue')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="100" show-word-limit />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item class="cg-item-text" :label="$t('sysTask.field.scheduleDays')" prop="scheduleDays" :size="$store.state.app.size" >
            <el-input v-model="record.scheduleDays" name="scheduleDays" 
                      type="text" 
                      :label="$t('sysTask.field.scheduleDays')" :placeholder="$t('system.message.needValue')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="100" show-word-limit />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="8">
          <el-form-item class="cg-item-text" :label="$t('sysTask.field.scheduleWeeks')" prop="scheduleWeeks" :size="$store.state.app.size" >
            <el-input v-model="record.scheduleWeeks" name="scheduleWeeks" 
                      type="text" 
                      :label="$t('sysTask.field.scheduleWeeks')" :placeholder="$t('system.message.needValue')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="100" show-word-limit />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item class="cg-item-text" :label="$t('sysTask.field.scheduleHours')" prop="scheduleHours" :size="$store.state.app.size" >
            <el-input v-model="record.scheduleHours" name="scheduleHours" 
                      type="text" 
                      :label="$t('sysTask.field.scheduleHours')" :placeholder="$t('system.message.needValue')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="100" show-word-limit />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item class="cg-item-text" :label="$t('sysTask.field.scheduleMinutes')" prop="scheduleMinutes" :size="$store.state.app.size" >
            <el-input v-model="record.scheduleMinutes" name="scheduleMinutes" 
                      type="text" 
                      :label="$t('sysTask.field.scheduleMinutes')" :placeholder="$t('system.message.needValue')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="100" show-word-limit />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="8">
          <el-form-item class="cg-item-text" :label="$t('sysTask.field.className')" prop="className" :size="$store.state.app.size" >
            <el-input v-model="record.className" name="className" 
                      type="text" 
                      :label="$t('sysTask.field.className')" :placeholder="$t('system.message.needValue')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="100" show-word-limit />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item class="cg-item-text" :label="$t('sysTask.field.mothodName')" prop="mothodName" :size="$store.state.app.size" >
            <el-input v-model="record.mothodName" name="mothodName" 
                      type="text" 
                      :label="$t('sysTask.field.mothodName')" :placeholder="$t('system.message.needValue')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="500" show-word-limit />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item class="cg-item-boolean" :label="$t('sysTask.field.isStatic')" prop="isStatic" :size="$store.state.app.size" >
            <el-switch v-model="record.isStatic" name="isStatic" :active-text="mobile?'':$t('sysTask.field.isStatic')" inactive-text="" :disabled="isDetail"  />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="16">
          <el-form-item class="cg-item-text" :label="$t('sysTask.field.parames')" prop="parames" :size="$store.state.app.size" >
            <el-input v-model="record.parames" name="parames" 
                      type="text" 
                      :label="$t('sysTask.field.parames')" :placeholder="$t('system.message.unknown')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="100" show-word-limit clearable />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item class="cg-item-boolean" :label="$t('sysTask.field.isRunning')" prop="isRunning" :size="$store.state.app.size" >
            <el-switch v-model="record.isRunning" name="isRunning" :active-text="mobile?'':$t('sysTask.field.isRunning')" inactive-text="" :disabled="isDetail"  />
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
const mixinContext = require.context('.', false, /CgFormTask-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default {
  name: 'CgFormTask',
  mixins,
  props: {
    dialogParams: {
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
      generatorName: 'sysTask',
      baseUrl: '/framework/sysTask'
    }
  },
  methods: {
    newRecord: function() {
        return {
            name: '',
            sceduleYears: '*',
            scheduleMonths: '*',
            scheduleDays: '*',
            scheduleWeeks: '*',
            scheduleHours: '*',
            scheduleMinutes: '*',
            className: '',
            mothodName: '',
            isStatic: true,
            isRunning: true,
        }
    },
  }
}
</script>
