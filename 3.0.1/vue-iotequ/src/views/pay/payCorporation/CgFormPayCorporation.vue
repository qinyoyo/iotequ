
<template>
  <div :class="mobile || height==0 ? 'cg-form':'cg-form just-v-scroll'" :style="mobile || height==0?'':'height:'+height+'px'">
    <el-form ref="cgForm" v-loading="recordLoading" :model="record" v-cg-form-adjust
                          :class="className" :rules="rules" 
             :label-position="labelPosition" :label-width="labelWidth" :size="$store.state.app.size" 
             hide-required-asterisk >
      <el-form-item class="cg-item-select cg-auto-focus" :label="$t('payCorporation.field.parentId')" prop="parentId" :size="$store.state.app.size" >
        <cg-cascader v-model="record.parentId" name="parentId"
                     :readonly="isDetail" :filterable="false" numberic clearable 
                     :dictionary="dictionary.dictParentId" :placeholder="$t('system.message.unknown')" />
      </el-form-item>
      <el-form-item class="cg-item-text" :label="$t('payCorporation.field.name')" prop="name" :size="$store.state.app.size" >
        <el-input v-model="record.name" name="name" 
                  type="text" 
                  :label="$t('payCorporation.field.name')" :placeholder="$t('system.message.needValue')" 
                  resize autofocus validate-event 
                  :readonly="isDetail" :maxlength="45" show-word-limit />
      </el-form-item>
      <el-form-item class="cg-item-text" :label="$t('payCorporation.field.address')" prop="address" :size="$store.state.app.size" >
        <el-input v-model="record.address" name="address" 
                  type="text" 
                  :label="$t('payCorporation.field.address')" :placeholder="$t('system.message.needValue')" 
                  resize autofocus validate-event 
                  :readonly="isDetail" :maxlength="45" show-word-limit />
      </el-form-item>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-text" :label="$t('payCorporation.field.linkman')" prop="linkman" :size="$store.state.app.size" >
            <el-input v-model="record.linkman" name="linkman" 
                      type="text" 
                      :label="$t('payCorporation.field.linkman')" :placeholder="$t('system.message.needValue')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="45" show-word-limit />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-tel" :label="$t('payCorporation.field.linkphone')" prop="linkphone" :size="$store.state.app.size" >
            <el-input v-model="record.linkphone" name="linkphone" 
                      type="tel" 
                      :label="$t('payCorporation.field.linkphone')" :placeholder="$t('system.message.needValue')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="45" show-word-limit />
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
const mixins = [ParentForm]
const mixinContext = require.context('.', false, /CgFormPayCorporation-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default {
  name: 'CgFormPayCorporation',
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
      continueAdd: true,
      path: 'record',
      idField: 'id',
      idSaved: this.openParams().record && typeof this.openParams().record === 'object' ? this.openParams().record.id : null,
      dictionary: {
        dictParentId: []
      },
      needLoadDictionary: true,
      generatorName: 'payCorporation',
      baseUrl: '/pay/payCorporation'
    }
  },
  methods: {
    newRecord: function() {
        return {
            name: '',
            address: '',
            linkman: '',
            linkphone: '',
        }
    },
  }
}
</script>
