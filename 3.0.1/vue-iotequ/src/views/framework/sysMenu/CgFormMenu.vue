
<template>
  <div :class="mobile || height==0 ? 'cg-form':'cg-form just-v-scroll'" :style="mobile || height==0?'':'height:'+height+'px'">
    <el-form ref="cgForm" v-loading="recordLoading" :model="record" v-cg-form-adjust
                          :class="className" :rules="rules" 
             :label-position="labelPosition" :label-width="labelWidth" :size="$store.state.app.size" 
             hide-required-asterisk >
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-number cg-auto-focus" :label="$t('sysMenu.field.sortNum')" prop="sortNum" :size="$store.state.app.size" >
            <cg-number-input v-model="record.sortNum" name="sortNum" 
                             :readonly="isDetail" 
                             :label="$t('sysMenu.field.sortNum')" :placeholder="$t('system.message.needValue')" :precision="0" 
                             :min="0" :step="1" :title="$t('system.message.valueRange') + ': 0 - *'" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-text" :label="$t('sysMenu.field.name')" prop="name" :size="$store.state.app.size" >
            <el-input v-model="record.name" name="name" 
                      v-i18n-view
                      type="text" 
                      :label="$t('sysMenu.field.name')" :placeholder="$t('system.message.needValue')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="45" show-word-limit />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-select" :label="$t('sysMenu.field.parent')" prop="parent" :size="$store.state.app.size" >
            <cg-cascader v-model="record.parent" name="parent"
                         :readonly="isDetail" :filterable="false" numberic clearable 
                         :dictionary="dictionary.dictParent" show-all-levels :placeholder="$t('system.message.unknown')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-boolean" :label="$t('sysMenu.field.isDivider')" prop="isDivider" :size="$store.state.app.size" >
            <el-switch v-model="record.isDivider" name="isDivider" :active-text="mobile?'':$t('sysMenu.field.isDivider')" inactive-text="" :disabled="isDetail"  />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-text" :label="$t('sysMenu.field.icon')" prop="icon" :size="$store.state.app.size" >
            <el-input v-model="record.icon" name="icon" 
                      type="text" 
                      :label="$t('sysMenu.field.icon')" :placeholder="$t('system.message.unknown')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="50" show-word-limit clearable />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-text" :label="$t('sysMenu.field.className')" prop="className" :size="$store.state.app.size" >
            <el-input v-model="record.className" name="className" 
                      type="text" 
                      :label="$t('sysMenu.field.className')" :placeholder="$t('system.message.unknown')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="45" show-word-limit clearable />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-select" :label="$t('sysMenu.field.action')" prop="action" :size="$store.state.app.size" >
            <cg-select v-model="record.action" name="action"
                       :dictionary="dictionary.dictAction" :readonly="isDetail" :filterable="false" :allow-create="false" :placeholder="$t('system.message.unknown')" clearable />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-text" :label="$t('sysMenu.field.jsCmd')" :title="$t('sysMenu.field.jsCmdValid')" prop="jsCmd" :size="$store.state.app.size" >
            <el-input v-model="record.jsCmd" name="jsCmd" 
                      type="text" 
                      :label="$t('sysMenu.field.jsCmd')" :placeholder="$t('system.message.unknown')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="45" show-word-limit clearable />
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item class="cg-item-text" :label="$t('sysMenu.field.dataAction')" :title="$t('sysMenu.field.dataActionValid')" prop="dataAction" :size="$store.state.app.size" >
        <el-input v-model="record.dataAction" name="dataAction" 
                  type="text" 
                  :label="$t('sysMenu.field.dataAction')" :placeholder="$t('system.message.unknown')" 
                  resize autofocus validate-event 
                  :readonly="isDetail" :maxlength="200" show-word-limit clearable />
      </el-form-item>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-text" :label="$t('sysMenu.field.bigicon')" prop="bigicon" :size="$store.state.app.size" >
            <el-input v-model="record.bigicon" name="bigicon" 
                      type="text" 
                      :label="$t('sysMenu.field.bigicon')" :placeholder="$t('system.message.unknown')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="50" show-word-limit clearable />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-boolean" :label="$t('sysMenu.field.mobileHidden')" prop="mobileHidden" :size="$store.state.app.size" >
            <el-switch v-model="record.mobileHidden" name="mobileHidden" :active-text="mobile?'':$t('sysMenu.field.mobileHidden')" inactive-text="" :disabled="isDetail"  />
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
  name: 'CgFormMenu',
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
        dictParent: [],
        dictAction: this.getAllRouteDictionary()
      },
      needLoadDictionary: true,
      generatorName: 'sysMenu',
      baseUrl: '/framework/sysMenu'
    }
  },
  methods: {
    just4elInputNumberNullBug: function() {
      if (this.record.sortNum === null) this.record.sortNum = undefined
    },
    newRecord: function() {
        return {
            sortNum: 10,
            name: '',
            isDivider: false,
            mobileHidden: false,
        }
    },
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /CgFormMenu-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
    mixins
}
</script>
