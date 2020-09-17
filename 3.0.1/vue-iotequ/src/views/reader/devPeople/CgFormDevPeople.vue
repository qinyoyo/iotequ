
<template>
  <div :class="mobile || height==0 ? 'cg-form':'cg-form just-v-scroll'" :style="mobile || height==0?'':'height:'+height+'px'">
    <el-form ref="cgForm" v-loading="recordLoading" :model="record" v-cg-form-adjust
                          :class="className" :rules="rules" 
             :label-position="labelPosition" :label-width="labelWidth" :size="$store.state.app.size" 
             hide-required-asterisk enctype="multipart/form-data" >
      <el-tabs ref="tabs" v-model="tabSelected" @tab-click="autoFocus">
        <el-tab-pane :label="groupPaneTitle('devPeople.title.groupDevPeopleRealName')">
          <el-row :gutter="mobile?0:20">
            <el-col :span="8">
              <el-form-item class="cg-item-text cg-auto-focus" :label="$t('devPeople.field.realName')" prop="realName" :size="$store.state.app.size" >
                <el-input v-model="record.realName" name="realName" 
                          type="text" 
                          :label="$t('devPeople.field.realName')" :placeholder="$t('system.message.needValue')" 
                          resize autofocus validate-event 
                          :readonly="isDetail" :maxlength="32" show-word-limit />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item class="cg-item-radio" :label="$t('devPeople.field.sex')" prop="sex" :size="$store.state.app.size" >
                <cg-radio v-model="record.sex" name="sex" :dictionary="dictionary.dictSex" :readonly="isDetail"  />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item class="cg-item-date" :label="$t('devPeople.field.birthDate')" prop="birthDate" :size="$store.state.app.size" >
                <span slot="label"><cg-icon icon="fa fa-calendar" :size="14" :width="20"/>{{$t('devPeople.field.birthDate')}}</span>
                <cg-date-picker v-model="record.birthDate" name="birthDate" 
                                :align="mobile?'right':'center'" type="date" :title="$t('devPeople.field.birthDate')" :readonly="isDetail" 
                                clearable />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="mobile?0:20">
            <el-col :span="8">
              <el-form-item class="cg-item-select" :label="$t('devPeople.field.idType')" prop="idType" :size="$store.state.app.size" >
                <cg-select v-model="record.idType" name="idType"
                           :dictionary="dictionary.dictIdType" :readonly="!isNew" :filterable="false" :allow-create="false" numberic :placeholder="$t('system.message.needValue')" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item class="cg-item-text" :label="$t('devPeople.field.idNumber')" prop="idNumber" :size="$store.state.app.size" >
                <el-input v-model="record.idNumber" name="idNumber" 
                          type="text" 
                          :label="$t('devPeople.field.idNumber')" :placeholder="$t('system.message.needValue')" 
                          resize autofocus validate-event 
                          :readonly="!isNew" :maxlength="45" show-word-limit />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item class="cg-item-text" :label="$t('devPeople.field.idNation')" prop="idNation" :size="$store.state.app.size" >
                <el-input v-model="record.idNation" name="idNation" 
                          type="text" 
                          :label="$t('devPeople.field.idNation')" :placeholder="$t('system.message.unknown')" 
                          resize autofocus validate-event 
                          :readonly="isDetail" :maxlength="100" show-word-limit clearable />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="mobile?0:20">
            <el-col :span="12">
              <el-form-item class="cg-item-date" :label="$t('devPeople.field.validDate')" prop="validDate" :size="$store.state.app.size" >
                <span slot="label"><cg-icon icon="fa fa-calendar" :size="14" :width="20"/>{{$t('devPeople.field.validDate')}}</span>
                <cg-date-picker v-model="record.validDate" name="validDate" 
                                :align="mobile?'right':'center'" type="date" :title="$t('devPeople.field.validDate')" :readonly="isDetail" 
                                clearable />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item class="cg-item-date" :label="$t('devPeople.field.expiredDate')" prop="expiredDate" :size="$store.state.app.size" >
                <span slot="label"><cg-icon icon="fa fa-calendar" :size="14" :width="20"/>{{$t('devPeople.field.expiredDate')}}</span>
                <cg-date-picker v-model="record.expiredDate" name="expiredDate" 
                                :align="mobile?'right':'center'" type="date" :title="$t('devPeople.field.expiredDate')" :readonly="isDetail" 
                                clearable />
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item class="cg-item-text" :label="$t('devPeople.field.homeAddr')" prop="homeAddr" :size="$store.state.app.size" >
            <el-input v-model="record.homeAddr" name="homeAddr" 
                      type="text" 
                      :label="$t('devPeople.field.homeAddr')" :placeholder="$t('system.message.unknown')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="200" show-word-limit clearable />
          </el-form-item>
          <el-row :gutter="mobile?0:20">
            <el-col :span="8">
              <el-form-item class="cg-item-text" :label="$t('devPeople.field.mobilePhone')" prop="mobilePhone" :size="$store.state.app.size" >
                <el-input v-model="record.mobilePhone" name="mobilePhone" 
                          type="text" 
                          :label="$t('devPeople.field.mobilePhone')" :placeholder="$t('system.message.unknown')" 
                          resize autofocus validate-event 
                          :readonly="isDetail" :maxlength="32" show-word-limit clearable />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item class="cg-item-text" :label="$t('devPeople.field.email')" prop="email" :size="$store.state.app.size" >
                <el-input v-model="record.email" name="email" 
                          type="text" 
                          :label="$t('devPeople.field.email')" :placeholder="$t('system.message.unknown')" 
                          resize autofocus validate-event 
                          :readonly="isDetail" :maxlength="50" show-word-limit clearable />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item class="cg-item-datetime" :label="$t('devPeople.field.regTime')" prop="regTime" :size="$store.state.app.size" >
                <span slot="label"><cg-icon icon="fa fa-calendar-plus-o" :size="14" :width="20"/>{{$t('devPeople.field.regTime')}}</span>
                <cg-date-picker v-model="record.regTime" name="regTime" 
                                :align="mobile?'right':'center'" type="datetime" :title="$t('devPeople.field.regTime')" readonly 
                                clearable />
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item class="cg-item-file" :label="$t('devPeople.field.photo')" prop="photo" :size="$store.state.app.size" >
            <cg-file v-model="blobRecord.photo" name="photo" :id="record.userNo" :baseUrl="baseUrl" field="photo" 
                     accept="image/*" :readonly="isDetail" 
                     @input="recordChanged=true"  />
          </el-form-item>
        </el-tab-pane>
        <el-tab-pane :label="groupPaneTitle('devPeople.title.groupDevPeopleCardNo')">
          <el-row :gutter="mobile?0:20">
            <el-col :span="12">
              <el-form-item class="cg-item-text cg-auto-focus" :label="$t('devPeople.field.cardNo')" prop="cardNo" :size="$store.state.app.size" >
                <el-input v-model="record.cardNo" name="cardNo" 
                          type="text" 
                          :label="$t('devPeople.field.cardNo')" :placeholder="$t('system.message.unknown')" 
                          resize autofocus validate-event 
                          :readonly="isDetail" :maxlength="45" show-word-limit clearable />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item class="cg-item-text" :label="$t('devPeople.field.devPassword')" prop="devPassword" :size="$store.state.app.size" >
                <el-input v-model="record.devPassword" name="devPassword" 
                          type="text" 
                          :label="$t('devPeople.field.devPassword')" :placeholder="$t('system.message.unknown')" 
                          resize autofocus validate-event 
                          :readonly="isDetail" :maxlength="32" show-word-limit clearable />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="mobile?0:20">
            <el-col :span="12">
              <el-form-item class="cg-item-select" :label="$t('devPeople.field.registerType')" prop="registerType" :size="$store.state.app.size" >
                <cg-select v-model="record.registerType" name="registerType"
                           :dictionary="dictionary.dictRegisterType" :readonly="!isNew" :filterable="false" :allow-create="false" numberic :placeholder="$t('system.message.needValue')" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item class="cg-item-radio" :label="$t('devPeople.field.userType')" prop="userType" :size="$store.state.app.size" >
                <cg-radio v-model="record.userType" name="userType" :dictionary="dictionary.dictUserType" :readonly="isDetail" numberic  />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="mobile?0:20">
            <el-col :span="12">
              <el-form-item class="cg-item-select" :label="$t('devPeople.field.orgCode')" prop="orgCode" :size="$store.state.app.size" >
                <cg-cascader v-model="record.orgCode" name="orgCode"
                             :readonly="isDetail" :filterable="false" numberic 
                             :dictionary="dictionary.dictOrgCode" show-all-levels :placeholder="$t('system.message.needValue')" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item class="cg-item-text" :label="$t('devPeople.field.dutyRank')" prop="dutyRank" :size="$store.state.app.size" >
                <el-input v-model="record.dutyRank" name="dutyRank" 
                          type="text" 
                          :label="$t('devPeople.field.dutyRank')" :placeholder="$t('system.message.unknown')" 
                          resize autofocus validate-event 
                          :readonly="isDetail" :maxlength="36" show-word-limit clearable />
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item class="cg-item-text" :label="$t('devPeople.field.note')" prop="note" :size="$store.state.app.size" >
            <el-input v-model="record.note" name="note" 
                      type="text" 
                      :label="$t('devPeople.field.note')" :placeholder="$t('system.message.unknown')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="100" show-word-limit clearable />
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
        {{ $t('devPeople.action.'+btn.action) }}
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
  name: 'CgFormDevPeople',
  mixins: [ParentForm],
  props: {
  },
  data() {
    return {
      defaultLabelPosition: 'top',
      rulesObject,
      continueAdd: true,
      path: 'record',
      idField: 'userNo',
      userNoSaved: this.openParams().record && typeof this.openParams().record === 'object' ? this.openParams().record.userNo : null,
      blobRecord:
      {
        photo: {
          name: this.openParams().record && typeof this.openParams().record === 'object' ? this.openParams().record.photo : null,
          blob: null
        },
      },
      dictionary: {
        dictSex: [],
        dictIdType: [],
        dictRegisterType: [],
        dictUserType: this.getDictionary('1,2','管理员,普通人员'),
        dictOrgCode: []
      },
      needLoadDictionary: true,
      tabSelected: '0',
      generatorName: 'devPeople',
      baseUrl: '/reader/devPeople'
    }
  },
  computed: {
    addtionalActions() {
      return [
        {
          action: 'sample',
          icon: 'fa fa-eyedropper',
          title: 'devPeople.action.sample',
          groupid: 10,
          confirm: '',
          rowProperty: 'sr',
          displayProperties: 'tb,ed,rw',
          actionProperty: 'go',
          appendClass: '{url:"/reader/devPeople/sample",openMode:"edit"}',
          needRefresh: false
        },
      ]
    },
  },
  methods: {
    newRecord: function() {
        return {
            realName: '',
            sex: '1',
            idType: 1,
            idNumber: '',
            regTime: new Date(),
            devPassword: '111111',
            registerType: 1,
            userType: 2,
            orgCode: 0,
        }
    },
    groupPaneTitle: function(defTitle) {
      return this.$t(defTitle)
    },
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /CgFormDevPeople-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
    mixins
}
</script>
