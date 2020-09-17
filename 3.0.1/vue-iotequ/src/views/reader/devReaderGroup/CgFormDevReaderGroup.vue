
<template>
  <div :class="mobile || height==0 ? 'cg-form':'cg-form just-v-scroll'" :style="mobile || height==0?'':'height:'+height+'px'">
    <el-form ref="cgForm" v-loading="recordLoading" :model="record" v-cg-form-adjust
                          :class="className" :rules="rules" 
             :label-position="labelPosition" :label-width="labelWidth" :size="$store.state.app.size" 
             hide-required-asterisk >
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-text cg-auto-focus" :label="$t('devReaderGroup.field.name')" prop="name" :size="$store.state.app.size" >
            <el-input v-model="record.name" name="name" 
                      type="text" 
                      :label="$t('devReaderGroup.field.name')" :placeholder="$t('system.message.needValue')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="20" show-word-limit />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-select" :label="$t('devReaderGroup.field.parent')" prop="parent" :size="$store.state.app.size" >
            <cg-cascader v-model="record.parent" name="parent"
                         :readonly="isDetail" :filterable="false" numberic clearable 
                         :dictionary="dictionary.dictParent" show-all-levels :placeholder="$t('system.message.unknown')" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-select" :label="$t('devReaderGroup.field.authGroupList')" prop="authGroupList" :size="$store.state.app.size" >
            <cg-select v-model="record.authGroupList" name="authGroupList"
                       :dictionary="dictionary.dictAuthGroupList" :readonly="isDetail" :filterable="false" :allow-create="false" multiple :placeholder="$t('system.message.unknown')" clearable />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-select" :label="$t('devReaderGroup.field.orgCode')" prop="orgCode" :size="$store.state.app.size" >
            <cg-cascader v-model="record.orgCode" name="orgCode"
                         :readonly="isDetail" :filterable="false" numberic 
                         :dictionary="dictionary.dictOrgCode" show-all-levels :placeholder="$t('system.message.needValue')" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-select" :label="$t('devReaderGroup.field.orgAuth')" prop="orgAuth" :size="$store.state.app.size" >
            <cg-select v-model="record.orgAuth" name="orgAuth"
                       :dictionary="dictionary.dictOrgAuth" :readonly="isDetail" :filterable="false" :allow-create="false" multiple :placeholder="$t('system.message.unknown')" clearable />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-select" :label="$t('devReaderGroup.field.subOrgAuth')" prop="subOrgAuth" :size="$store.state.app.size" >
            <cg-select v-model="record.subOrgAuth" name="subOrgAuth"
                       :dictionary="dictionary.dictSubOrgAuth" :readonly="isDetail" :filterable="false" :allow-create="false" multiple :placeholder="$t('system.message.unknown')" clearable />
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
  name: 'CgFormDevReaderGroup',
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
      continueAdd: true,
      path: 'record',
      idField: 'id',
      idSaved: this.openParams().record && typeof this.openParams().record === 'object' ? this.openParams().record.id : null,
      dictionary: {
        dictParent: [],
        dictAuthGroupList: [],
        dictOrgCode: [],
        dictOrgAuth: [],
        dictSubOrgAuth: []
      },
      needLoadDictionary: true,
      generatorName: 'devReaderGroup',
      baseUrl: '/reader/devReaderGroup'
    }
  },
  methods: {
    newRecord: function() {
        return {
            name: '',
            orgCode: 0,
        }
    },
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /CgFormDevReaderGroup-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
    mixins
}
</script>
