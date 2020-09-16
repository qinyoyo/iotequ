
<template>
  <div :class="mobile || height==0 ? 'cg-form':'cg-form just-v-scroll'" :style="mobile || height==0?'':'height:'+height+'px'">
    <el-form ref="cgForm" v-loading="recordLoading" :model="record" v-cg-form-adjust
                          :class="className" :rules="rules" 
             :label-position="labelPosition" :label-width="labelWidth" :size="$store.state.app.size" 
             hide-required-asterisk >
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-text cg-auto-focus" :label="$t('sysOrg.field.name')" prop="name" :size="$store.state.app.size" >
            <el-input v-model="record.name" name="name" 
                      type="text" 
                      :label="$t('sysOrg.field.name')" :placeholder="$t('system.message.needValue')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="32" show-word-limit />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-select" :label="$t('sysOrg.field.parent')" prop="parent" :size="$store.state.app.size" >
            <cg-cascader v-model="record.parent" name="parent"
                         :readonly="isDetail" :filterable="false" numberic clearable 
                         :dictionary="dictionary.dictParent" show-all-levels :placeholder="$t('system.message.unknown')" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-tel" :label="$t('sysOrg.field.phone')" prop="phone" :size="$store.state.app.size" >
            <el-input v-model="record.phone" name="phone" 
                      type="tel" 
                      :label="$t('sysOrg.field.phone')" :placeholder="$t('system.message.unknown')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="32" show-word-limit clearable />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-tel" :label="$t('sysOrg.field.fax')" prop="fax" :size="$store.state.app.size" >
            <el-input v-model="record.fax" name="fax" 
                      type="tel" 
                      :label="$t('sysOrg.field.fax')" :placeholder="$t('system.message.unknown')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="32" show-word-limit clearable />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-select" :label="$t('sysOrg.field.roleList')" prop="roleList" :size="$store.state.app.size" >
            <cg-select v-model="record.roleList" name="roleList"
                       :dictionary="dictionary.dictRoleList" :readonly="isDetail" :filterable="false" :allow-create="false" multiple :placeholder="$t('system.message.unknown')" clearable />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-text" :label="$t('sysOrg.field.address')" prop="address" :size="$store.state.app.size" >
            <el-input v-model="record.address" name="address" 
                      type="text" 
                      :label="$t('sysOrg.field.address')" :placeholder="$t('system.message.unknown')" 
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
const Comp = {
  name: 'CgFormOrg',
  mixins: [ParentForm],
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
      idField: 'orgCode',
      orgCodeSaved: this.openParams().record && typeof this.openParams().record === 'object' ? this.openParams().record.orgCode : null,
      dictionary: {
        dictParent: [],
        dictRoleList: []
      },
      needLoadDictionary: true,
      generatorName: 'sysOrg',
      baseUrl: '/framework/sysOrg'
    }
  },
  methods: {
    newRecord: function() {
        return {
            name: '',
        }
    },
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /CgFormOrg-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
    mixins
}
</script>
