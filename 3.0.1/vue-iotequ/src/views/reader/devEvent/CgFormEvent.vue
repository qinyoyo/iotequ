
<template>
  <div :class="mobile || height==0 ? 'cg-form':'cg-form just-v-scroll'" :style="mobile || height==0?'':'height:'+height+'px'">
    <el-form ref="cgForm" v-loading="recordLoading" :model="record" v-cg-form-adjust
                          :class="className" :rules="rules" 
             :label-position="labelPosition" :label-width="labelWidth" :size="$store.state.app.size" 
             hide-required-asterisk >
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-text cg-auto-focus" :label="$t('devEvent.field.devType')" prop="devType" :size="$store.state.app.size" >
            <el-input v-model="record.devType" name="devType" 
                      type="text" 
                      :label="$t('devEvent.field.devType')" :placeholder="$t('system.message.needValue')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="45" show-word-limit />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-text" :label="$t('devEvent.field.devNo')" prop="devNo" :size="$store.state.app.size" >
            <el-input v-model="record.devNo" name="devNo" 
                      type="text" 
                      :label="$t('devEvent.field.devNo')" :placeholder="$t('system.message.needValue')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="45" show-word-limit />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="8">
          <el-form-item class="cg-item-select" :label="$t('devEvent.field.orgCode')" prop="orgCode" :size="$store.state.app.size" >
            <cg-cascader v-model="record.orgCode" name="orgCode"
                         :readonly="isDetail" :filterable="false" numberic clearable 
                         :dictionary="dictionary.dictOrgCode" show-all-levels :placeholder="$t('system.message.unknown')" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <cg-join v-model="userNoJoinVisible" :readonly="isDetail" >
            <CgListDevPeople slot="popover" ref="userNoJoin" openID="userno-join" :height="500" :joinShow="userNoJoinVisible" :joinMultiple="false"
                             :originSelections="String(record.userNo)" selectionKey="userNo" joinMode @closeJoinList="(rows)=>{ getJoinFields('userNo',rows)}"
                             @showJoinList="userNoJoinVisible=true" />
            <el-form-item class="cg-item-mltext" slot="reference" :label="$t('devEvent.field.userNo')" prop="realName" :size="$store.state.app.size" >
              <cg-input v-model="record.realName" name="realName" 
                        type="mltext" 
                        :label="$t('devEvent.field.userNo')" :placeholder="$t('system.message.unknown')" 
                        resize autofocus validate-event 
                        readonly :maxlength="32" show-word-limit clearable />
            </el-form-item>
          </cg-join>
        </el-col>
        <el-col :span="8">
          <el-form-item class="cg-item-select" :label="$t('devEvent.field.auditeeAuthType')" prop="auditeeAuthType" :size="$store.state.app.size" >
            <cg-select v-model="record.auditeeAuthType" name="auditeeAuthType"
                       :dictionary="dictionary.dictAuditeeAuthType" :readonly="isDetail" :filterable="false" :allow-create="false" numberic :placeholder="$t('system.message.unknown')" clearable />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="8">
          <el-form-item class="cg-item-select" :label="$t('devEvent.field.auditorOrg')" prop="auditorOrg" :size="$store.state.app.size" >
            <cg-cascader v-model="record.auditorOrg" name="auditorOrg"
                         :readonly="isDetail" :filterable="false" numberic clearable 
                         :dictionary="dictionary.dictAuditorOrg" show-all-levels :placeholder="$t('system.message.unknown')" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <cg-join v-model="auditorUserNumJoinVisible" :readonly="isDetail" >
            <CgListDevPeople slot="popover" ref="auditorUserNumJoin" openID="auditorusernum-join" :height="500" :joinShow="auditorUserNumJoinVisible" :joinMultiple="false"
                             :originSelections="String(record.auditorUserNum)" selectionKey="userNo" joinMode @closeJoinList="(rows)=>{ getJoinFields('auditorUserNum',rows)}"
                             @showJoinList="auditorUserNumJoinVisible=true" />
            <el-form-item class="cg-item-mltext" slot="reference" :label="$t('devEvent.field.auditorName')" prop="auditorName" :size="$store.state.app.size" >
              <cg-input v-model="record.auditorName" name="auditorName" 
                        type="mltext" 
                        :label="$t('devEvent.field.auditorName')" :placeholder="$t('system.message.unknown')" 
                        resize autofocus validate-event 
                        readonly :maxlength="32" show-word-limit clearable />
            </el-form-item>
          </cg-join>
        </el-col>
        <el-col :span="8">
          <el-form-item class="cg-item-select" :label="$t('devEvent.field.auditorAuthType')" prop="auditorAuthType" :size="$store.state.app.size" >
            <cg-select v-model="record.auditorAuthType" name="auditorAuthType"
                       :dictionary="dictionary.dictAuditorAuthType" :readonly="isDetail" :filterable="false" :allow-create="false" numberic :placeholder="$t('system.message.unknown')" clearable />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="8">
          <el-form-item class="cg-item-select" :label="$t('devEvent.field.authType')" prop="authType" :size="$store.state.app.size" >
            <cg-select v-model="record.authType" name="authType"
                       :dictionary="dictionary.dictAuthType" :readonly="isDetail" :filterable="false" :allow-create="false" numberic :placeholder="$t('system.message.unknown')" clearable />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item class="cg-item-radio" :label="$t('devEvent.field.status')" prop="status" :size="$store.state.app.size" >
            <cg-radio v-model="record.status" name="status" :dictionary="dictionary.dictStatus" :readonly="isDetail" numberic  />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item class="cg-item-datetime" :label="$t('devEvent.field.time')" prop="time" :size="$store.state.app.size" >
            <span slot="label"><cg-icon icon="fa fa-calendar-plus-o" :size="14" :width="20"/>{{$t('devEvent.field.time')}}</span>
            <cg-date-picker v-model="record.time" name="time" 
                            :align="mobile?'right':'center'" type="datetime" :title="$t('devEvent.field.time')" :readonly="isDetail" 
                            />
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
import CgListDevPeople from '@/views/reader/devPeople/CgListDevPeople.vue'
import rulesObject from './rules.js'
import ParentForm from '@/views/common-views/components/form'
const Comp = {
  name: 'CgFormEvent',
  mixins: [ParentForm],
  props: {
  },
  components: { CgListDevPeople },
  data() {
    return {
      allowEditRecord: false,
      allowAddRecord: false,
      defaultLabelPosition: 'top',
      rulesObject,
      continueAdd: true,
      path: 'record',
      idField: 'id',
      idSaved: this.openParams().record && typeof this.openParams().record === 'object' ? this.openParams().record.id : null,
      dictionary: {
        dictOrgCode: [],
        dictAuditeeAuthType: this.getDictionary('0,1,2,3,4,5,6,7','devEvent.field.auditeeAuthType_0,devEvent.field.auditeeAuthType_1,devEvent.field.auditeeAuthType_2,devEvent.field.auditeeAuthType_3,devEvent.field.auditeeAuthType_4,devEvent.field.auditeeAuthType_5,devEvent.field.auditeeAuthType_6,devEvent.field.auditeeAuthType_7'),
        dictAuditorOrg: [],
        dictAuditorAuthType: this.getDictionary('0,1,2,3,4,5,6,7','devEvent.field.auditorAuthType_0,devEvent.field.auditorAuthType_1,devEvent.field.auditorAuthType_2,devEvent.field.auditorAuthType_3,devEvent.field.auditorAuthType_4,devEvent.field.auditorAuthType_5,devEvent.field.auditorAuthType_6,devEvent.field.auditorAuthType_7'),
        dictAuthType: this.getDictionary('0,1','devEvent.field.authType_0,devEvent.field.authType_1'),
        dictStatus: []
      },
      needLoadDictionary: true,
      userNoJoinVisible: false,
      auditorUserNumJoinVisible: false,
      generatorName: 'devEvent',
      baseUrl: '/reader/devEvent'
    }
  },
  computed: {
  },
  methods: {
    getJoinFields(field,rows) {
      const joinDefine = {
        userNo: {
          valueField: 'userNo',
          fields: 'realName=realName'
        },
        auditorUserNum: {
          valueField: 'userNo',
          fields: 'auditorName=realName'
        },
      }
      this[field+'JoinVisible'] = false
      this.setJoinValues(this.record, field, joinDefine[field], rows)
    }
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /CgFormEvent-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
    mixins
}
</script>
