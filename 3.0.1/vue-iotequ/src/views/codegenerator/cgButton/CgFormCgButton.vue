
<template>
  <div :class="mobile || height==0 ? 'cg-form':'cg-form just-v-scroll'" :style="mobile || height==0?'':'height:'+height+'px'">
    <el-form ref="cgForm" v-loading="recordLoading" :model="record" v-cg-form-adjust
                          :class="className" :rules="rules" 
             :label-position="labelPosition" :label-width="labelWidth" :size="$store.state.app.size" 
             hide-required-asterisk >
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-number cg-auto-focus" :label="$t('cgButton.field.orderNum')" prop="orderNum" :size="$store.state.app.size" >
            <cg-number-input v-model="record.orderNum" name="orderNum" 
                             :readonly="isDetail" 
                             :label="$t('cgButton.field.orderNum')" :placeholder="$t('system.message.needValue')" :precision="0" 
                             :min="0" :step="1" :title="$t('system.message.valueRange') + ': 0 - *'" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-text" :label="$t('cgButton.field.action')" :title="$t('cgButton.field.actionValid')" prop="action" :size="$store.state.app.size" >
            <el-input v-model="record.action" name="action" 
                      type="text" 
                      :label="$t('cgButton.field.action')" :placeholder="$t('system.message.needValue')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="45" show-word-limit />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-text" :label="$t('cgButton.field.title')" prop="title" :size="$store.state.app.size" >
            <el-input v-model="record.title" name="title" 
                      type="text" 
                      :label="$t('cgButton.field.title')" :placeholder="$t('system.message.needValue')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="45" show-word-limit />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-text" :label="$t('cgButton.field.icon')" prop="icon" :size="$store.state.app.size" >
            <el-input v-model="record.icon" name="icon" 
                      type="text" 
                      :label="$t('cgButton.field.icon')" :placeholder="$t('system.message.needValue')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="45" show-word-limit />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-radio" :label="$t('cgButton.field.actionProperty')" prop="actionProperty" :size="$store.state.app.size" >
            <cg-radio v-model="record.actionProperty" name="actionProperty" :dictionary="dictionary.dictActionProperty" :readonly="isDetail"  />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-radio" :label="$t('cgButton.field.rowProperty')" prop="rowProperty" :size="$store.state.app.size" >
            <cg-radio v-model="record.rowProperty" name="rowProperty" :dictionary="dictionary.dictRowProperty" :readonly="isDetail"  />
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item class="cg-item-text" :label="$t('cgButton.field.appendClass')" :title="$t('cgButton.field.appendClassValid')" prop="appendClass" :size="$store.state.app.size" >
        <el-input v-model="record.appendClass" name="appendClass" 
                  type="text" 
                  :label="$t('cgButton.field.appendClass')" :placeholder="$t('system.message.unknown')" 
                  resize autofocus validate-event 
                  :readonly="isDetail" :maxlength="200" show-word-limit clearable />
      </el-form-item>
      <el-form-item class="cg-item-checkbox" :label="$t('cgButton.field.displayProperties')" prop="displayProperties" :size="$store.state.app.size" >
        <cg-checkbox v-model="record.displayProperties" name="displayProperties" :dictionary="dictionary.dictDisplayProperties" :readonly="isDetail"  />
      </el-form-item>
      <el-row :gutter="mobile?0:20">
        <el-col :span="16">
          <el-form-item class="cg-item-text" :label="$t('cgButton.field.confirmText')" :title="$t('cgButton.field.confirmTextValid')" prop="confirmText" :size="$store.state.app.size" >
            <el-input v-model="record.confirmText" name="confirmText" 
                      type="text" 
                      :label="$t('cgButton.field.confirmText')" :placeholder="$t('system.message.unknown')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="100" show-word-limit clearable />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item class="cg-item-boolean" :label="$t('cgButton.field.isRefreshList')" prop="isRefreshList" :size="$store.state.app.size" >
            <el-switch v-model="record.isRefreshList" name="isRefreshList" :active-text="mobile?'':$t('cgButton.field.isRefreshList')" inactive-text="" :disabled="isDetail"  />
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
const mixinContext = require.context('.', false, /CgFormCgButton-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default {
  name: 'CgFormCgButton',
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
        dictActionProperty: this.getDictionary('js,go,pg,aj','仅前端函数,页面跳转,自定义函数调用后端,仅后端操作'),
        dictRowProperty: this.getDictionary('sr,mr,nr','单行,多行,行无关'),
        dictDisplayProperties: this.getDictionary('hm,hp,tb,rw,ed,gs','手机隐藏,PC隐藏,工具条显示,行显示,编辑显示,分组开始')
      },
      generatorName: 'cgButton',
      baseUrl: '/codegenerator/cgButton'
    }
  },
  methods: {
    just4elInputNumberNullBug: function() {
      if (this.record.orderNum === null) this.record.orderNum = undefined
    },
    newRecord: function() {
        return {
            tableId: '',
            orderNum: 0,
            action: '',
            title: '',
            icon: '',
            actionProperty: '',
            rowProperty: '',
            isRefreshList: false,
        }
    },
  }
}
</script>
