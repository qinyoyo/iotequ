
<template>
  <div :class="mobile || height==0 ? 'cg-form':'cg-form just-v-scroll'" :style="mobile || height==0?'':'height:'+height+'px'">
    <el-form ref="cgForm" v-loading="recordLoading" :model="record" v-cg-form-adjust
                          :class="className" :rules="rules" 
             :label-position="labelPosition" :label-width="labelWidth" :size="$store.state.app.size" 
             hide-required-asterisk >
      <el-tabs ref="tabs" v-model="tabSelected" @tab-click="autoFocus">
        <el-tab-pane :label="groupPaneTitle('cgForm.title.groupCgFormTableId')">
          <el-row :gutter="mobile?0:20">
            <el-col :span="12">
              <el-form-item class="cg-item-select cg-auto-focus" :label="$t('cgForm.field.tableId')" prop="tableId" :size="$store.state.app.size" >
                <cg-select v-model="record.tableId" name="tableId"
                           :dictionary="dictionary.dictTableId" :readonly="!isNew" :filterable="false" :allow-create="false" :placeholder="$t('system.message.needValue')" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item class="cg-item-text" :label="$t('cgForm.field.name')" prop="name" :size="$store.state.app.size" >
                <el-input v-model="record.name" name="name" 
                          type="text" 
                          :label="$t('cgForm.field.name')" :placeholder="$t('system.message.needValue')" 
                          resize autofocus validate-event 
                          :readonly="isDetail" :maxlength="45" show-word-limit />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="mobile?0:20">
            <el-col :span="8">
              <el-form-item class="cg-item-boolean" :label="$t('cgForm.field.isFlow')" prop="isFlow" :size="$store.state.app.size" >
                <el-switch v-model="record.isFlow" name="isFlow" :active-text="mobile?'':$t('cgForm.field.isFlow')" inactive-text="" :disabled="isDetail"  />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item class="cg-item-boolean" :label="$t('cgForm.field.isDialog')" prop="isDialog" :size="$store.state.app.size" >
                <el-switch v-model="record.isDialog" name="isDialog" :active-text="mobile?'':$t('cgForm.field.isDialog')" inactive-text="" :disabled="isDetail"  />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item class="cg-item-boolean" :label="$t('cgForm.field.continueAdd')" prop="continueAdd" :size="$store.state.app.size" >
                <el-switch v-model="record.continueAdd" name="continueAdd" :active-text="mobile?'':$t('cgForm.field.continueAdd')" inactive-text="" :disabled="isDetail"  />
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item class="cg-item-text" :label="$t('cgForm.field.path')" :title="$t('cgForm.field.pathValid')" prop="path" :size="$store.state.app.size" >
            <el-input v-model="record.path" name="path" 
                      type="text" 
                      :label="$t('cgForm.field.path')" :placeholder="$t('system.message.needValue')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="200" show-word-limit />
          </el-form-item>
          <el-form-item class="cg-item-text" :label="$t('cgForm.field.icon')" :title="$t('cgForm.field.iconValid')" prop="icon" :size="$store.state.app.size" >
            <el-input v-model="record.icon" name="icon" 
                      type="text" 
                      :label="$t('cgForm.field.icon')" :placeholder="$t('system.message.unknown')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="300" show-word-limit clearable />
          </el-form-item>
          <el-form-item class="cg-item-text" :label="$t('cgForm.field.headTitle')" :title="$t('cgForm.field.headTitleValid')" prop="headTitle" :size="$store.state.app.size" >
            <el-input v-model="record.headTitle" name="headTitle" 
                      type="text" 
                      :label="$t('cgForm.field.headTitle')" :placeholder="$t('system.message.needValue')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="400" show-word-limit />
          </el-form-item>
          <el-row :gutter="mobile?0:20">
            <el-col :span="12">
              <el-form-item class="cg-item-text" :label="$t('cgForm.field.tagTitle')" prop="tagTitle" :size="$store.state.app.size" >
                <el-input v-model="record.tagTitle" name="tagTitle" 
                          type="text" 
                          :label="$t('cgForm.field.tagTitle')" :placeholder="$t('system.message.needValue')" 
                          resize autofocus validate-event 
                          :readonly="isDetail" :maxlength="45" show-word-limit />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item class="cg-item-text" :label="$t('cgForm.field.labelPosition')" prop="labelPosition" :size="$store.state.app.size" >
                <el-input v-model="record.labelPosition" name="labelPosition" 
                          type="text" 
                          :label="$t('cgForm.field.labelPosition')" :placeholder="$t('system.message.needValue')" 
                          resize autofocus validate-event 
                          :readonly="isDetail" :maxlength="45" show-word-limit />
              </el-form-item>
            </el-col>
          </el-row>
        </el-tab-pane>
        <el-tab-pane :label="groupPaneTitle('cgForm.title.groupCgFormImages')">
          <el-form-item class="cg-item-text cg-auto-focus" :label="$t('cgForm.field.images')" prop="images" :size="$store.state.app.size" >
            <el-input v-model="record.images" name="images" 
                      type="text" 
                      :label="$t('cgForm.field.images')" :placeholder="$t('system.message.unknown')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="200" show-word-limit clearable />
          </el-form-item>
          <el-form-item class="cg-item-checkbox" :label="$t('cgForm.field.actionList')" prop="actionList" :size="$store.state.app.size" >
            <cg-checkbox v-model="record.actionList" name="actionList" :dictionary="dictionary.dictActionList" :readonly="isDetail"  />
          </el-form-item>
          <el-form-item class="cg-item-textarea" :label="$t('cgForm.field.formProperties')" prop="formProperties" :size="$store.state.app.size" >
            <el-input v-model="record.formProperties" name="formProperties" 
                      type="textarea" 
                      :label="$t('cgForm.field.formProperties')" :placeholder="$t('system.message.unknown')" clearable 
                      :readonly="isDetail" resize :autosize="{ minRows: 1, maxRows: 10 }" autofocus validate-event />
          </el-form-item>
          <el-form-item class="cg-item-textarea" :label="$t('cgForm.field.viewProperties')" prop="viewProperties" :size="$store.state.app.size" >
            <el-input v-model="record.viewProperties" name="viewProperties" 
                      type="textarea" 
                      :label="$t('cgForm.field.viewProperties')" :placeholder="$t('system.message.unknown')" clearable 
                      :readonly="isDetail" resize :autosize="{ minRows: 1, maxRows: 10 }" autofocus validate-event />
          </el-form-item>
          <el-form-item class="cg-item-text" :label="$t('cgForm.field.slotTemplates')" prop="slotTemplates" :size="$store.state.app.size" >
            <el-input v-model="record.slotTemplates" name="slotTemplates" 
                      type="text" 
                      :label="$t('cgForm.field.slotTemplates')" :placeholder="$t('system.message.unknown')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" clearable />
          </el-form-item>
        </el-tab-pane>
      </el-tabs>
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
const mixinContext = require.context('.', false, /CgFormCgForm-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default {
  name: 'CgFormCgForm',
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
      record: Object.assign({
          tableId: null
        }, this.openParams().record && typeof this.openParams().record === 'object' ? this.openParams().record : {}),
      dictionary: {
        dictTableId: [],
        dictActionList: []
      },
      needLoadDictionary: true,
      tabSelected: '0',
      generatorName: 'cgForm',
      baseUrl: '/codegenerator/cgForm'
    }
  },
  watch: {
    'record.tableId': {
      handler(n,o)
      {
        cgForm.form_getDynaDict(this, 'actionList')
      }
    }
  },
  methods: {
	  initDynaDict: function() {
	    cgForm.form_getDynaDict(this, 'actionList')
	  },
    newRecord: function() {
        return {
            tableId: '',
            name: '',
            isFlow: false,
            isDialog: false,
            continueAdd: true,
            path: 'record',
            headTitle: '',
            tagTitle: '',
            labelPosition: 'top',
        }
    },
    groupPaneTitle: function(defTitle) {
      return this.$t(defTitle)
    },
  }
}
</script>
