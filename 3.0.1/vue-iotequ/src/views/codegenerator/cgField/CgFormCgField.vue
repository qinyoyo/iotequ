
<template>
  <div :class="mobile || height==0 ? 'cg-form':'cg-form just-v-scroll'" :style="mobile || height==0?'':'height:'+height+'px'">
    <el-form ref="cgForm" v-loading="recordLoading" :model="record" v-cg-form-adjust
                          :class="className" :rules="rules" 
             :label-position="labelPosition" :label-width="labelWidth" :size="$store.state.app.size" 
             hide-required-asterisk >
      <el-tabs ref="tabs" v-model="tabSelected" @tab-click="autoFocus">
        <el-tab-pane :label="groupPaneTitle('cgField.title.groupCgFieldTitle')">
          <el-row :gutter="mobile?0:20">
            <el-col :span="8">
              <el-form-item class="cg-item-text cg-auto-focus" :label="$t('cgField.field.title')" :title="$t('cgField.field.titleValid')" prop="title" :size="$store.state.app.size" >
                <el-input v-model="record.title" name="title" 
                          type="text" 
                          :label="$t('cgField.field.title')" :placeholder="$t('system.message.needValue')" 
                          resize autofocus validate-event 
                          :readonly="isDetail" :maxlength="32" show-word-limit />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item class="cg-item-text" :label="$t('cgField.field.name')" :title="$t('cgField.field.nameValid')" prop="name" :size="$store.state.app.size" >
                <el-input v-model="record.name" name="name" 
                          type="text" 
                          :label="$t('cgField.field.name')" :placeholder="$t('system.message.unknown')" 
                          resize autofocus validate-event 
                          :readonly="isDetail" :maxlength="32" show-word-limit clearable />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item class="cg-item-text" :label="$t('cgField.field.entityName')" :title="$t('cgField.field.entityNameValid')" prop="entityName" :size="$store.state.app.size" >
                <el-input v-model="record.entityName" name="entityName" 
                          type="text" 
                          :label="$t('cgField.field.entityName')" :placeholder="$t('system.message.needValue')" 
                          resize autofocus validate-event 
                          :readonly="isDetail" :maxlength="45" show-word-limit />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="mobile?0:20">
            <el-col :span="8">
              <el-form-item class="cg-item-select" :label="$t('cgField.field.showType')" :title="$t('cgField.field.showTypeValid')" prop="showType" :size="$store.state.app.size" >
                <cg-select v-model="record.showType" name="showType"
                           :dictionary="dictionary.dictShowType" :readonly="isDetail" :filterable="false" :allow-create="false" :placeholder="$t('system.message.needValue')" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item v-show="record.showType==='select' || record.showType==='file' || record.showType.indexOf('join')>=0" class="cg-item-boolean" :label="$t('cgField.field.dictMultiple')" :title="$t('cgField.field.dictMultipleValid')" prop="dictMultiple" :size="$store.state.app.size" >
                <el-switch v-model="record.dictMultiple" name="dictMultiple" :active-text="mobile?'':$t('cgField.field.dictMultiple')" inactive-text="" :disabled="isDetail"  />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item class="cg-item-number" :label="$t('cgField.field.orderNum')" :title="$t('cgField.field.orderNumValid')" prop="orderNum" :size="$store.state.app.size" >
                <cg-number-input v-model="record.orderNum" name="orderNum" 
                                 :readonly="isDetail" 
                                 :label="$t('cgField.field.orderNum')" :placeholder="$t('system.message.needValue')" 
                                 :min="0" :step="10" :title="$t('system.message.valueRange') + ': 0 - *'" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item v-show="record.showType!='html'" class="cg-item-text" :label="$t('cgField.field.defaultValue')" :title="$t('cgField.field.defaultValueValid')" prop="defaultValue" :size="$store.state.app.size" >
            <el-input v-model="record.defaultValue" name="defaultValue" 
                      type="text" 
                      :label="$t('cgField.field.defaultValue')" :placeholder="$t('system.message.unknown')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="200" show-word-limit clearable >
                  <cg-select v-model="defaultPrepend" slot="prepend" style="width: 100px" :dictionary="dictionaryJsJava" />
            </el-input>
          </el-form-item>
          <el-form-item v-show="record.showType!='html'" :label="getLabel('formatter')" :title="getTitle('formatter')" class="cg-item-text" prop="formatter" :size="$store.state.app.size" >
            <el-input v-model="record.formatter" name="formatter" 
                      type="text" 
                      :label="$t('cgField.field.formatter')" :placeholder="$t('system.message.unknown')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="100" show-word-limit clearable >
                  <cg-select v-model="formatterPrepend" slot="prepend" style="width: 100px"  :dictionary="dictionaryJs" />
            </el-input>
          </el-form-item>
          <el-form-item v-show="record.showType!='html'" :label="getLabel('validExpression')" :title="getTitle('validExpression')" class="cg-item-text" prop="validExpression" :size="$store.state.app.size" >
            <el-input v-model="record.validExpression" name="validExpression" 
                      type="text" 
                      :label="$t('cgField.field.validExpression')" :placeholder="$t('system.message.unknown')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="200" show-word-limit clearable >
              <el-button slot="append" icon="el-icon-circle-check" @click="testValidExpression()" />
            </el-input>
          </el-form-item>
          <el-form-item :label="getLabel('validTitle')" :title="getTitle('validTitle')" class="cg-item-text" prop="validTitle" :size="$store.state.app.size" >
            <el-input v-model="record.validTitle" name="validTitle" 
                      type="text" 
                      :label="$t('cgField.field.validTitle')" :placeholder="$t('system.message.unknown')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="1000" show-word-limit clearable />
          </el-form-item>
        </el-tab-pane>
        <el-tab-pane :label="groupPaneTitle('cgField.title.groupCgFieldDictTable')">
          <el-form-item :label="getLabel('dictTable')" :title="getTitle('dictTable')" class="cg-item-text cg-auto-focus" prop="dictTable" :size="$store.state.app.size" >
            <el-input v-model="record.dictTable" name="dictTable" 
                      :readonly="isDetail || joinMode" :clearable="selectMode" @change="dictTableChange"
                      type="text" 
                      :label="$t('cgField.field.dictTable')" :placeholder="$t('system.message.unknown')" 
                      resize autofocus validate-event 
                      :maxlength="200" show-word-limit >
              <cg-select slot="prepend" v-model="record.dictTable" nullText="输入列表或sql" :clearable="selectMode" style="width:300px" :dictionary="dictDictTable()" :allow-create="selectMode"/>
            </el-input>
          </el-form-item>
          <el-form-item :label="getLabel('dictField')" :title="getTitle('dictField')" class="cg-item-text" prop="dictField" :size="$store.state.app.size" >
            <el-input v-model="record.dictField" name="dictField" 
                      type="text" 
                      :label="$t('cgField.field.dictField')" :placeholder="$t('system.message.unknown')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="1000" show-word-limit clearable />
          </el-form-item>
          <el-form-item :label="getLabel('dictText')" :title="getTitle('dictText')" class="cg-item-text" prop="dictText" :size="$store.state.app.size" >
            <el-input v-model="record.dictText" name="dictText" 
                      type="text" 
                      :label="$t('cgField.field.dictText')" :placeholder="$t('system.message.unknown')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="1000" show-word-limit clearable />
          </el-form-item>
          <el-form-item v-show="joinMode || useSelect" :label="getLabel('dynaCondition')" :title="getTitle('dynaCondition')" class="cg-item-text" prop="dynaCondition" :size="$store.state.app.size" >
            <el-input v-model="record.dynaCondition" name="dynaCondition" 
                      type="text" 
                      :label="$t('cgField.field.dynaCondition')" :placeholder="$t('system.message.unknown')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="200" show-word-limit clearable />
          </el-form-item>
          <el-row :gutter="mobile?0:20">
            <el-col :span="8">
              <el-form-item v-show="useSelect" class="cg-item-boolean" :label="$t('cgField.field.dictFullName')" :title="$t('cgField.field.dictFullNameValid')" prop="dictFullName" :size="$store.state.app.size" >
                <el-switch v-model="record.dictFullName" name="dictFullName" :active-text="mobile?'':$t('cgField.field.dictFullName')" inactive-text="" :disabled="isDetail"  />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item v-show="useSelect" class="cg-item-text" :label="$t('cgField.field.dictParent')" :title="$t('cgField.field.dictParentValid')" prop="dictParent" :size="$store.state.app.size" >
                <el-input v-model="record.dictParent" name="dictParent" 
                          type="text" 
                          :label="$t('cgField.field.dictParent')" :placeholder="$t('system.message.unknown')" 
                          resize autofocus validate-event 
                          :readonly="isDetail" :maxlength="100" show-word-limit clearable />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item v-show="useSelect" class="cg-item-text" :label="$t('cgField.field.dictParentField')" :title="$t('cgField.field.dictParentFieldValid')" prop="dictParentField" :size="$store.state.app.size" >
                <el-input v-model="record.dictParentField" name="dictParentField" 
                          type="text" 
                          :label="$t('cgField.field.dictParentField')" :placeholder="$t('system.message.unknown')" 
                          resize autofocus validate-event 
                          :readonly="isDetail" :maxlength="100" show-word-limit clearable />
              </el-form-item>
            </el-col>
          </el-row>
        </el-tab-pane>
        <el-tab-pane :label="groupPaneTitle('cgField.title.groupCgFieldType')">
          <el-row :gutter="mobile?0:20">
            <el-col :span="8">
              <el-form-item class="cg-item-select cg-auto-focus" :label="$t('cgField.field.type')" :title="$t('cgField.field.typeValid')" prop="type" :size="$store.state.app.size" >
                <cg-select v-model="record.type" name="type"
                           :dictionary="dictionary.dictType" :readonly="isDetail" :filterable="false" :allow-create="false" :placeholder="$t('system.message.needValue')" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item class="cg-item-number" :label="$t('cgField.field.length')" prop="length" :size="$store.state.app.size" >
                <cg-number-input v-model="record.length" name="length" 
                                 :readonly="isDetail" 
                                 :label="$t('cgField.field.length')" :placeholder="$t('system.message.unknown')" clearable 
                                 :min="1" :max="500" :step="1" :title="$t('system.message.valueRange') + ': 1 - 500'" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item class="cg-item-boolean" :label="$t('cgField.field.isNull')" :title="$t('cgField.field.isNullValid')" prop="isNull" :size="$store.state.app.size" >
                <el-switch v-model="record.isNull" name="isNull" :active-text="mobile?'':$t('cgField.field.isNull')" inactive-text="" :disabled="isDetail"  />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="mobile?0:20">
            <el-col :span="8">
              <el-form-item class="cg-item-select" :label="$t('cgField.field.keyType')" :title="$t('cgField.field.keyTypeValid')" prop="keyType" :size="$store.state.app.size" >
                <cg-select v-model="record.keyType" name="keyType"
                           :dictionary="dictionary.dictKeyType" :readonly="isDetail" :filterable="false" :allow-create="false" :placeholder="$t('system.message.needValue')" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item class="cg-item-text" :label="$t('cgField.field.numericPrecision')" prop="numericPrecision" :size="$store.state.app.size" >
                <el-input v-model="record.numericPrecision" name="numericPrecision" 
                          type="text" 
                          :label="$t('cgField.field.numericPrecision')" :placeholder="$t('system.message.unknown')" 
                          resize autofocus validate-event 
                          :readonly="isDetail" clearable />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item class="cg-item-text" :label="$t('cgField.field.numericScale')" prop="numericScale" :size="$store.state.app.size" >
                <el-input v-model="record.numericScale" name="numericScale" 
                          type="text" 
                          :label="$t('cgField.field.numericScale')" :placeholder="$t('system.message.unknown')" 
                          resize autofocus validate-event 
                          :readonly="isDetail" clearable />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="mobile?0:20">
            <el-col :span="6">
              <el-form-item class="cg-item-select" :label="$t('cgField.field.fkTable')" prop="fkTable" :size="$store.state.app.size" >
                <cg-select v-model="record.fkTable" name="fkTable"
                           :dictionary="dictionary.dictFkTable" :readonly="isDetail" :filterable="false" :allow-create="false" :placeholder="$t('system.message.unknown')" clearable />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item class="cg-item-select" :label="$t('cgField.field.fkColumn')" prop="fkColumn" :size="$store.state.app.size" >
                <cg-select v-model="record.fkColumn" name="fkColumn"
                           :dictionary="dictionary.dictFkColumn" :readonly="isDetail" :filterable="false" :allow-create="false" :placeholder="$t('system.message.unknown')" clearable />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item class="cg-item-select" :label="$t('cgField.field.fkOnDelete')" prop="fkOnDelete" :size="$store.state.app.size" >
                <cg-select v-model="record.fkOnDelete" name="fkOnDelete"
                           :dictionary="dictionary.dictFkOnDelete" :readonly="isDetail" :filterable="false" :allow-create="false" :placeholder="$t('system.message.unknown')" clearable />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item class="cg-item-select" :label="$t('cgField.field.fkOnUpdate')" prop="fkOnUpdate" :size="$store.state.app.size" >
                <cg-select v-model="record.fkOnUpdate" name="fkOnUpdate"
                           :dictionary="dictionary.dictFkOnUpdate" :readonly="isDetail" :filterable="false" :allow-create="false" :placeholder="$t('system.message.unknown')" clearable />
              </el-form-item>
            </el-col>
          </el-row>
        </el-tab-pane>
      </el-tabs>
    </el-form>
    <div v-if="!isDetail" class="cg-form-buttons">
      <el-divider />
      <el-button-group v-for = "(btn,index) in addtionalActions" :key="btn.action" style="margin-right:5px">
        <el-button v-if="hasAuthorityOf(myself,baseUrl,btn,record)" :class="'cg-button'+''+(btn.appendClass?' '+btn.appendClass:'')"
                   type="default" plain :icon="btn.icon" :disabled="hasOwnProperty('disabledAction') ? disabledAction(btn) : false"
                   @click.native="doAction(btn.action)" >
        {{ $t('cgField.action.'+btn.action) }}
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
const mixins = [ParentForm]
const mixinContext = require.context('.', false, /CgFormCgField-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default {
  name: 'CgFormCgField',
  mixins,
  props: {
  },
  data() {
    return {
      defaultLabelPosition: 'top',
      rulesObject,
      continueAdd: true,
      path: 'record',
      idField: 'id',
      idSaved: this.openParams().record && typeof this.openParams().record === 'object' ? this.openParams().record.id : null,
      record: Object.assign({
          tableId: null,
          fkTable: null
        }, this.openParams().record && typeof this.openParams().record === 'object' ? this.openParams().record : {}),
      dictionary: {
        dictShowType: this.getDictionary('text,textarea,boolean,false_if_null,checkbox,radio,select,inner_join,left_join,right_join,full_join,dict_list,password,date,time,datetime,number,email,search,file,image,url,tel,color,html,2dcode'),
        dictType: this.getDictionary('int,varchar,boolean,datetime,tinyint,smallint,mediumint,bigint,double,float,decimal,date,time,timestamp,char,tinytext,text,mediumtext,longtext,tinyblob,blob,mediumblob,longblob,bit,binary,varbinary'),
        dictKeyType: this.getDictionary('0,1,2,3,4,5,11,12,13','无,自增长主键,uuid主键,普通主键,唯一索引,非唯一索引,联合唯一索引1,联合唯一索引2,联合唯一索引3'),
        dictFkTable: [],
        dictFkColumn: [],
        dictFkOnDelete: this.getDictionary('CASCADE,SET NULL,NO ACTION,RESTRICT','同步删除子表,设置为空,不允许删除,高权限不允许删除'),
        dictFkOnUpdate: this.getDictionary('CASCADE,SET NULL,NO ACTION,RESTRICT','同步更新子表,设置为空,不允许更新,高权限不允许更新')
      },
      needLoadDictionary: true,
      tabSelected: '0',
      generatorName: 'cgField',
      baseUrl: '/codegenerator/cgField'
    }
  },
  computed: {
    addtionalActions() {
      return [
        {
          action: 'extDict',
          icon: 'w',
          title: 'cgField.action.extDict',
          groupid: 10,
          confirm: '',
          rowProperty: 'nr',
          displayProperties: 'hm,hp,ed',
          actionProperty: 'aj',
          appendClass: '',
          needRefresh: false
        }
      ]
    },
  },
  watch: {
    'record.tableId': {
      handler(n,o)
      {
        cgForm.form_getDynaDict(this, 'fkTable')
      }
    },
    'record.fkTable': {
      handler(n,o)
      {
        cgForm.form_getDynaDict(this, 'fkColumn')
      }
    }
  },
  methods: {
    just4elInputNumberNullBug: function() {
      if (this.record.orderNum === null) this.record.orderNum = undefined
      if (this.record.length === null) this.record.length = undefined
    },
	  initDynaDict: function() {
	    cgForm.form_getDynaDict(this, 'fkTable,fkColumn')
	  },
    newRecord: function() {
        return {
            tableId: '',
            title: '',
            entityName: '',
            showType: 'text',
            dictMultiple: false,
            orderNum: 0,
            dictFullName: false,
            type: 'varchar',
            length: 36,
            isNull: false,
            keyType: '0',
        }
    },
    groupPaneTitle: function(defTitle) {
      return this.$t(defTitle)
    },
  }
}
</script>
