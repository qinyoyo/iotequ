
<template>
  <div :class="mobile || height==0 ? 'cg-form':'cg-form just-v-scroll'" :style="mobile || height==0?'':'height:'+height+'px'">
    <el-form ref="cgForm" v-loading="recordLoading" :model="record" v-cg-form-adjust
                          :class="className" :rules="rules" 
             :label-position="labelPosition" :label-width="labelWidth" :size="$store.state.app.size" 
             hide-required-asterisk >
      <el-tabs ref="tabs" v-model="tabSelected" @tab-click="autoFocus">
        <el-tab-pane :label="groupPaneTitle('cgListField.title.groupCgListFieldEntityField')">
          <el-row :gutter="mobile?0:20">
            <el-col :span="12">
              <el-form-item class="cg-item-text cg-auto-focus" :label="$t('cgListField.field.entityField')" prop="entityField" :size="$store.state.app.size" >
                <el-input v-model="record.entityField" name="entityField" 
                          type="text" 
                          :label="$t('cgListField.field.entityField')" :placeholder="$t('system.message.needValue')" 
                          resize autofocus validate-event 
                          :readonly="isDetail" :maxlength="45" show-word-limit />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item class="cg-item-select" :label="$t('cgListField.field.queryMode')" prop="queryMode" :size="$store.state.app.size" >
                <cg-select v-model="record.queryMode" name="queryMode"
                           :dictionary="dictionary.dictQueryMode" :readonly="isDetail" :filterable="false" :allow-create="false" numberic :placeholder="$t('system.message.needValue')" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item class="cg-item-text" :label="$t('cgListField.field.defaultQueryValue')" :title="$t('cgListField.field.defaultQueryValueValid')" prop="defaultQueryValue" :size="$store.state.app.size" >
            <el-input v-model="record.defaultQueryValue" name="defaultQueryValue" 
                      type="text" 
                      :label="$t('cgListField.field.defaultQueryValue')" :placeholder="$t('system.message.unknown')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="200" show-word-limit clearable />
          </el-form-item>
          <el-row :gutter="mobile?0:20">
            <el-col :span="12">
              <el-form-item class="cg-item-select" :label="$t('cgListField.field.showType')" prop="showType" :size="$store.state.app.size" >
                <cg-select v-model="record.showType" name="showType"
                           :dictionary="dictionary.dictShowType" :readonly="isDetail" :filterable="false" :allow-create="false" :placeholder="$t('system.message.unknown')" clearable />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item class="cg-item-boolean" :label="$t('cgListField.field.hidden')" prop="hidden" :size="$store.state.app.size" >
                <el-switch v-model="record.hidden" name="hidden" :active-text="mobile?'':$t('cgListField.field.hidden')" inactive-text="" :disabled="isDetail"  />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="mobile?0:20">
            <el-col :span="12">
              <el-form-item class="cg-item-text" :label="$t('cgListField.field.align')" prop="align" :size="$store.state.app.size" >
                <el-input v-model="record.align" name="align" 
                          type="text" 
                          :label="$t('cgListField.field.align')" :placeholder="$t('system.message.needValue')" 
                          resize autofocus validate-event 
                          :readonly="isDetail" :maxlength="45" show-word-limit />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item class="cg-item-boolean" :label="$t('cgListField.field.editInline')" prop="editInline" :size="$store.state.app.size" >
                <el-switch v-model="record.editInline" name="editInline" :active-text="mobile?'':$t('cgListField.field.editInline')" inactive-text="" :disabled="isDetail"  />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="mobile?0:20">
            <el-col :span="12">
              <el-form-item class="cg-item-number" :label="$t('cgListField.field.width')" prop="width" :size="$store.state.app.size" >
                <cg-number-input v-model="record.width" name="width" 
                                 :readonly="isDetail" 
                                 :label="$t('cgListField.field.width')" :placeholder="$t('system.message.needValue')" 
                                 />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item class="cg-item-number" :label="$t('cgListField.field.orderNum')" prop="orderNum" :size="$store.state.app.size" >
                <cg-number-input v-model="record.orderNum" name="orderNum" 
                                 :readonly="isDetail" 
                                 :label="$t('cgListField.field.orderNum')" :placeholder="$t('system.message.needValue')" :precision="0" 
                                 />
              </el-form-item>
            </el-col>
          </el-row>
        </el-tab-pane>
        <el-tab-pane :label="groupPaneTitle('cgListField.title.groupCgListFieldFix')">
          <el-row :gutter="mobile?0:20">
            <el-col :span="12">
              <el-form-item class="cg-item-boolean cg-auto-focus" :label="$t('cgListField.field.fix')" prop="fix" :size="$store.state.app.size" >
                <el-switch v-model="record.fix" name="fix" :active-text="mobile?'':$t('cgListField.field.fix')" inactive-text="" :disabled="isDetail"  />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item class="cg-item-text" :label="$t('cgListField.field.headerAlign')" prop="headerAlign" :size="$store.state.app.size" >
                <el-input v-model="record.headerAlign" name="headerAlign" 
                          type="text" 
                          :label="$t('cgListField.field.headerAlign')" :placeholder="$t('system.message.unknown')" 
                          resize autofocus validate-event 
                          :readonly="isDetail" :maxlength="45" show-word-limit clearable />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="mobile?0:20">
            <el-col :span="12">
              <el-form-item class="cg-item-boolean" :label="$t('cgListField.field.expand')" prop="expand" :size="$store.state.app.size" >
                <el-switch v-model="record.expand" name="expand" :active-text="mobile?'':$t('cgListField.field.expand')" inactive-text="" :disabled="isDetail"  />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item class="cg-item-boolean" :label="$t('cgListField.field.overflowTooltip')" prop="overflowTooltip" :size="$store.state.app.size" >
                <el-switch v-model="record.overflowTooltip" name="overflowTooltip" :active-text="mobile?'':$t('cgListField.field.overflowTooltip')" inactive-text="" :disabled="isDetail"  />
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item class="cg-item-textarea" :label="$t('cgListField.field.columnProperties')" prop="columnProperties" :size="$store.state.app.size" >
            <el-input v-model="record.columnProperties" name="columnProperties" 
                      type="textarea" 
                      :label="$t('cgListField.field.columnProperties')" :placeholder="$t('system.message.unknown')" clearable 
                      :readonly="isDetail" resize :autosize="{ minRows: 1, maxRows: 10 }" autofocus validate-event />
          </el-form-item>
          <el-form-item class="cg-item-text" :label="$t('cgListField.field.cellDisplaySlot')" prop="cellDisplaySlot" :size="$store.state.app.size" >
            <el-input v-model="record.cellDisplaySlot" name="cellDisplaySlot" 
                      type="text" 
                      :label="$t('cgListField.field.cellDisplaySlot')" :placeholder="$t('system.message.unknown')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="500" show-word-limit clearable />
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
const Comp = {
  name: 'CgFormCgListField',
  mixins: [ParentForm],
  props: {
    routeParams: {
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
      dictionary: {
        dictQueryMode: this.getDictionary('0,1,2,3','cgListField.field.queryMode_0,cgListField.field.queryMode_1,cgListField.field.queryMode_2,cgListField.field.queryMode_3'),
        dictShowType: this.getDictionary('text,textarea,boolean,false_if_null,password,date,time,datetime,number,email,search,file,image,url,tel,color,html,2dcode')
      },
      tabSelected: '0',
      generatorName: 'cgListField',
      baseUrl: '/codegenerator/cgListField'
    }
  },
  methods: {
    just4elInputNumberNullBug: function() {
      if (this.record.width === null) this.record.width = undefined
      if (this.record.orderNum === null) this.record.orderNum = undefined
    },
    newRecord: function() {
        return {
            listId: '',
            entityField: '',
            queryMode: 0,
            hidden: false,
            align: 'left',
            editInline: false,
            width: 128,
            orderNum: 0,
            fix: false,
            expand: false,
            overflowTooltip: true,
        }
    },
    groupPaneTitle: function(defTitle) {
      return this.$t(defTitle)
    },
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /CgFormCgListField-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
    mixins
}
</script>
