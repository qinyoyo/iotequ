
<template>
  <div :class="mobile || height==0 ? 'cg-form':'cg-form just-v-scroll'" :style="mobile || height==0?'':'height:'+height+'px'">
    <el-form ref="cgForm" v-loading="recordLoading" :model="record" v-cg-form-adjust
                          :class="className" :rules="rules" 
             :label-position="labelPosition" :label-width="labelWidth" :size="$store.state.app.size" 
             hide-required-asterisk enctype="multipart/form-data" >
      <el-tabs ref="tabs" v-model="tabSelected" @tab-click="autoFocus">
        <el-tab-pane :label="groupPaneTitle('sysUser.title.groupUserIcon')">
          <el-form-item class="cg-item-image" :label="$t('sysUser.field.icon')" prop="icon" :size="$store.state.app.size" >
            <cg-image v-model="blobRecord.icon" name="icon" :title="$t('sysUser.field.icon')"  :readonly="isDetail"  :id="record.id" generatorName="sysUser" field="icon" :maxWidth="128" :maxHeight="128" alignCenter 
                      @input="recordChanged=true" />
          </el-form-item>
          <el-row :gutter="mobile?0:20">
            <el-col :span="12">
              <el-form-item class="cg-item-text cg-auto-focus" :label="$t('sysUser.field.name')" :title="$t('sysUser.field.nameValid')" prop="name" :size="$store.state.app.size" >
                <el-input v-model="record.name" name="name" 
                          type="text" 
                          :label="$t('sysUser.field.name')" :placeholder="$t('system.message.needValue')" 
                          resize autofocus validate-event 
                          :readonly="isDetail" :maxlength="32" show-word-limit />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item class="cg-item-text" :label="$t('sysUser.field.realName')" prop="realName" :size="$store.state.app.size" >
                <el-input v-model="record.realName" name="realName" 
                          type="text" 
                          :label="$t('sysUser.field.realName')" :placeholder="$t('system.message.needValue')" 
                          resize autofocus validate-event 
                          :readonly="isDetail" :maxlength="32" show-word-limit />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="mobile?0:20">
            <el-col :span="12">
              <el-form-item class="cg-item-radio" :label="$t('sysUser.field.sex')" prop="sex" :size="$store.state.app.size" >
                <cg-radio v-model="record.sex" name="sex" :dictionary="dictionary.dictSex" :readonly="isDetail"  />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item class="cg-item-date" :label="$t('sysUser.field.birthDate')" prop="birthDate" :size="$store.state.app.size" >
                <span slot="label"><cg-icon icon="fa fa-calendar" :size="14" :width="20"/>{{$t('sysUser.field.birthDate')}}</span>
                <cg-date-picker v-model="record.birthDate" name="birthDate" 
                                :maxValue="new Date()" :minValue="dateAdd(new Date(),-100,'year')"
                                :align="mobile?'right':'center'" type="date" :title="$t('sysUser.field.birthDate')" :readonly="isDetail" 
                                clearable />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="mobile?0:20">
            <el-col :span="12">
              <el-form-item class="cg-item-select" :label="$t('sysUser.field.idType')" prop="idType" :size="$store.state.app.size" >
                <cg-select v-model="record.idType" name="idType"
                           :dictionary="dictionary.dictIdType" :readonly="!isNew" :filterable="false" :allow-create="false" numberic :placeholder="$t('system.message.needValue')" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item class="cg-item-text" :label="$t('sysUser.field.idNumber')" prop="idNumber" :size="$store.state.app.size" >
                <el-input v-model="record.idNumber" name="idNumber" 
                          type="text" 
                          :label="$t('sysUser.field.idNumber')" :placeholder="$t('system.message.needValue')" 
                          resize autofocus validate-event 
                          :readonly="!isNew" :maxlength="45" show-word-limit />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="mobile?0:20">
            <el-col :span="12">
              <el-form-item class="cg-item-select" :label="$t('sysUser.field.orgCode')" prop="orgCode" :size="$store.state.app.size" >
                <cg-cascader v-model="record.orgCode" name="orgCode"
                             :readonly="!admin || isDetail"
                             :filterable="false" numberic required 
                             :dictionary="dictionary.dictOrgCode" show-all-levels :placeholder="$t('system.message.needValue')" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item class="cg-item-datetime" :label="$t('sysUser.field.regTime')" prop="regTime" :size="$store.state.app.size" >
                <span slot="label"><cg-icon icon="fa fa-calendar-plus-o" :size="14" :width="20"/>{{$t('sysUser.field.regTime')}}</span>
                <cg-date-picker v-model="record.regTime" name="regTime" 
                                :align="mobile?'right':'center'" type="datetime" :title="$t('sysUser.field.regTime')" readonly 
                                clearable />
              </el-form-item>
            </el-col>
          </el-row>
        </el-tab-pane>
        <el-tab-pane :label="groupPaneTitle('sysUser.title.groupUserMobilePhone')">
          <el-row :gutter="mobile?0:20">
            <el-col :span="12">
              <el-form-item class="cg-item-tel cg-auto-focus" :label="$t('sysUser.field.mobilePhone')" prop="mobilePhone" :size="$store.state.app.size" >
                <el-input v-model="record.mobilePhone" name="mobilePhone" 
                          type="tel" 
                          :label="$t('sysUser.field.mobilePhone')" :placeholder="$t('system.message.unknown')" 
                          resize autofocus validate-event 
                          :readonly="isDetail" :maxlength="32" show-word-limit clearable />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item class="cg-item-email" :label="$t('sysUser.field.email')" prop="email" :size="$store.state.app.size" >
                <el-input v-model="record.email" name="email" 
                          type="email" 
                          :label="$t('sysUser.field.email')" :placeholder="$t('system.message.unknown')" 
                          resize autofocus validate-event 
                          :readonly="isDetail" :maxlength="50" show-word-limit clearable />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="mobile?0:20">
            <el-col :span="12">
              <el-form-item class="cg-item-false_if_null" :label="$t('sysUser.field.wechatOpenid')" prop="wechatOpenid" :size="$store.state.app.size" >
                <el-switch name="wechatOpenid" :value="record.wechatOpenid?true:false" :active-text="mobile?'':$t('sysUser.field.wechatOpenid')" inactive-text="" disabled />
              </el-form-item>
            </el-col>
          </el-row>
        </el-tab-pane>
        <el-tab-pane :label="groupPaneTitle('sysUser.title.groupUserRoleList')">
          <el-form-item class="cg-item-select cg-auto-focus" :label="$t('sysUser.field.roleList')" prop="roleList" :size="$store.state.app.size" >
            <cg-select v-model="record.roleList" name="roleList"
                       :readonly="!admin || isDetail"
                       :dictionary="dictionary.dictRoleList" :filterable="false" :allow-create="false" multiple :placeholder="$t('system.message.unknown')" clearable />
          </el-form-item>
          <el-form-item class="cg-item-select" :label="$t('sysUser.field.orgPrivilege')" prop="orgPrivilege" :size="$store.state.app.size" >
            <cg-cascader v-model="record.orgPrivilege" name="orgPrivilege"
                         :readonly="!admin || isDetail"
                         :filterable="false" numberic clearable 
                         :dictionary="dictionary.dictOrgPrivilege" :placeholder="$t('system.message.unknown')" />
          </el-form-item>
          <el-row :gutter="mobile?0:20">
            <el-col :span="12">
              <el-form-item class="cg-item-boolean" :label="$t('sysUser.field.locked')" prop="locked" :size="$store.state.app.size" >
                <el-switch v-model="record.locked" name="locked" :disabled="!admin || isDetail" :active-text="mobile?'':$t('sysUser.field.locked')" inactive-text=""  />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item class="cg-item-boolean" :label="$t('sysUser.field.state')" prop="state" :size="$store.state.app.size" >
                <el-switch v-model="record.state" name="state" :disabled="!admin || isDetail" :active-text="mobile?'':$t('sysUser.field.state')" inactive-text=""  />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="mobile?0:20">
            <el-col :span="12">
              <el-form-item class="cg-item-date" :label="$t('sysUser.field.expiredTime')" prop="expiredTime" :size="$store.state.app.size" >
                <span slot="label"><cg-icon icon="fa fa-calendar" :size="14" :width="20"/>{{$t('sysUser.field.expiredTime')}}</span>
                <cg-date-picker v-model="record.expiredTime" name="expiredTime" 
                                :readonly="!admin || isDetail" :minValue="new Date()"
                                :align="mobile?'right':'center'" type="date" :title="$t('sysUser.field.expiredTime')" 
                                clearable />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item class="cg-item-date" :label="$t('sysUser.field.passwordExpiredTime')" prop="passwordExpiredTime" :size="$store.state.app.size" >
                <span slot="label"><cg-icon icon="fa fa-calendar" :size="14" :width="20"/>{{$t('sysUser.field.passwordExpiredTime')}}</span>
                <cg-date-picker v-model="record.passwordExpiredTime" name="passwordExpiredTime" 
                                :readonly="!admin || isDetail" :minValue="new Date()"
                                :align="mobile?'right':'center'" type="date" :title="$t('sysUser.field.passwordExpiredTime')" 
                                clearable />
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item class="cg-item-html" :label="$t('sysUser.field.htmlNote')" prop="htmlNote" :size="$store.state.app.size" >
            <div name="htmlNote">
              <div v-html="$t('sysUser.field.htmlNoteValid')"></div>
              <div v-html="record.htmlNote"></div>
            </div>
          </el-form-item>
        </el-tab-pane>
      </el-tabs>
    </el-form>
    <div v-if="!isDetail" class="cg-form-buttons">
      <el-divider />
      <el-button-group v-for = "(btn,index) in addtionalActions" :key="btn.action" style="margin-right:5px">
        <el-button v-if="hasAuthorityOf(myself,baseUrl,btn,record)" :class="'cg-button'+''+(btn.appendClass?' '+btn.appendClass:'')"
                   type="default" plain :icon="btn.icon" :disabled="hasOwnProperty('disabledAction') ? disabledAction(btn) : false"
                   @click.native="doAction(btn.action)" >
        {{ $t('sysUser.action.'+btn.action) }}
        </el-button>
      </el-button-group>
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
  name: 'CgFormUser',
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
      continueAdd: true,
      path: 'record',
      idField: 'id',
      idSaved: this.openParams().record && typeof this.openParams().record === 'object' ? this.openParams().record.id : null,
      blobRecord:
      {
        icon: {
          name: this.openParams().record && typeof this.openParams().record === 'object' ? this.openParams().record.icon : null,
          blob: null
        },
      },
      needDefaultFromServer: true,
      dictionary: {
        dictSex: [],
        dictIdType: [],
        dictOrgCode: [],
        dictRoleList: [],
        dictOrgPrivilege: []
      },
      needLoadDictionary: true,
      tabSelected: '0',
      generatorName: 'sysUser',
      baseUrl: '/framework/sysUser'
    }
  },
  computed: {
    addtionalActions() {
      return [
        {
          action: 'changePassword',
          icon: 'fa fa-unlock-alt',
          title: 'sysUser.action.changePassword',
          groupid: 10,
          confirm: '',
          rowProperty: 'sr',
          displayProperties: 'ed',
          actionProperty: 'go',
          appendClass: '{url:"/changePassword"}',
          needRefresh: false
        },
      ]
    },
  },
  methods: {
    newRecord: function() {
        return {
            password: '123456',
            passwordErrorTimes: 0,
            name: '',
            realName: '',
            sex: '1',
            idType: 1,
            idNumber: '',
            locked: false,
            state: true,
        }
    },
    groupPaneTitle: function(defTitle) {
      return this.$t(defTitle)
    },
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /CgFormUser-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
    mixins
}
</script>
