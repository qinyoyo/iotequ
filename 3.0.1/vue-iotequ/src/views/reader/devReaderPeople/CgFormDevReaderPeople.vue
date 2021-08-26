
<template>
  <div :class="mobile || height==0 ? 'cg-form':'cg-form just-v-scroll'" :style="mobile || height==0?'':'height:'+height+'px'">
    <el-form ref="cgForm" v-loading="recordLoading" :model="record" v-cg-form-adjust
                          :class="className" :rules="rules" 
             :label-position="labelPosition" :label-width="labelWidth" :size="$store.state.app.size" 
             hide-required-asterisk >
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-select" :label="$t('devReaderPeople.field.status')" prop="status" :size="$store.state.app.size" >
            <cg-select v-model="record.status" name="status"
                       :dictionary="dictionary.dictStatus" readonly :filterable="false" :allow-create="false" numberic :placeholder="$t('system.message.unknown')" clearable />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-text" :label="$t('devReaderPeople.field.readerNo')" prop="readerNo" :size="$store.state.app.size" >
            <el-input v-model="record.readerNo" name="readerNo" 
                      type="text" 
                      :label="$t('devReaderPeople.field.readerNo')" :placeholder="$t('system.message.unknown')" 
                      resize autofocus validate-event 
                      readonly :maxlength="20" show-word-limit clearable />
          </el-form-item>
        </el-col>
      </el-row>
      <cg-join v-model="userNoJoinVisible" :readonly="isDetail" >
        <CgListDevPeople slot="popover" ref="userNoJoin" openID="userno-join" :height="500" :joinShow="userNoJoinVisible" :joinMultiple="true"
                         :originSelections="String(record.userNo)" selectionKey="userNo" joinMode @closeJoinList="(rows)=>{ getJoinFields('userNo',rows)}"
                         @showJoinList="userNoJoinVisible=true" />
        <el-form-item class="cg-item-mltext cg-auto-focus" slot="reference" :label="$t('devPeople.field.realName')" prop="realName" :size="$store.state.app.size" >
          <cg-input v-model="record.realName" name="realName" 
                    type="mltext" 
                    :label="$t('devPeople.field.realName')" :placeholder="$t('system.message.unknown')" 
                    resize autofocus validate-event 
                     clearable @clear="clearJoinValues(myself,'userNoJoin')"/>
        </el-form-item>
      </cg-join>
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
  name: 'CgFormDevReaderPeople',
  mixins: [ParentForm],
  props: {
  },
  components: { CgListDevPeople },
  data() {
    return {
      defaultLabelPosition: 'top',
      rulesObject,
      continueAdd: true,
      path: 'record',
      idField: 'id',
      fastMultiJoinField: 'userNo',
      idSaved: this.openParams().record && typeof this.openParams().record === 'object' ? this.openParams().record.id : null,
      dictionary: {
        dictType: this.getDictionary('0,1,2','devReaderPeople.field.type_0,devReaderPeople.field.type_1,devReaderPeople.field.type_2'),
        dictStatus: this.getDictionary('0,1','devReaderPeople.field.status_0,devReaderPeople.field.status_1')
      },
      userNoJoinVisible: false,
      generatorName: 'devReaderPeople',
      baseUrl: '/reader/devReaderPeople'
    }
  },
  computed: {
  },
  methods: {
    newRecord: function() {
        return {
            type: 0,
            status: 1,
            realName: null,
        }
    },
    getJoinFields(field,rows) {
      const joinDefine = {
        userNo: {
          valueField: 'userNo',
          fields: 'birthDate=birthDate,cardNo=cardNo,devPassword=devPassword,dutyRank=dutyRank,email=email,expiredDate=expiredDate,homeAddr=homeAddr,idNation=idNation,idNumber=idNumber,idType=idType,mobilePhone=mobilePhone,note=note,orgCode=orgCode,photo=photo,realName=realName,regFingers=regFingers,registerType=registerType,regTime=regTime,sex=sex,userNoDevPeopleUserNo=userNo,userType=userType,validDate=validDate'
        },
      }
      this[field+'JoinVisible'] = false
      this.setJoinValues(this.record, field, joinDefine[field], rows)
    }
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /CgFormDevReaderPeople-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
    mixins
}
</script>
