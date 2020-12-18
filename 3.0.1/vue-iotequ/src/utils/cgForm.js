import cg from '@/utils/cg'
import { request } from '@/utils/request'
import {setStyle, getParent} from '@/utils/dom'

export default {
  form_getQueryDictionary(formObject) {
    if (formObject.hasOwnProperty('dictionary') && formObject.openParams().dictionary && typeof formObject.openParams().dictionary === 'object' ) {
      if (formObject.openParams().hasOwnProperty('needLoadDictionary')) formObject.needLoadDictionary = formObject.openParams().needLoadDictionary
      else {
        const myKeys=Object.keys(formObject.dictionary)
        const queryKeys=Object.keys(formObject.openParams().dictionary)
        formObject.needLoadDictionary = myKeys.some(k=>queryKeys.indexOf(k)<0)
      }
      formObject.dictionary = Object.assign(formObject.dictionary,formObject.openParams().dictionary)
    } else if (formObject.hasOwnProperty('dictionary')) formObject.needLoadDictionary = true
  },
  // 表单后台发起请求，自动处理字典 method : 请求方法 params：请求参数 data: post数据
  // action：controller的url方法  onSuccess onError : 成功和错误的回调，一个参数 silence: 静默方式,默认非静默
  form_request({ formObject, method, params, data, action, onSuccess, onError, silence, timeout }) {
    return new Promise((resolve, reject) => {
      const req = {
        url: formObject.baseUrl + '/' + action,
        method
      }
      if (params) req.params = params
      if (formObject.ignoreOrgFilter) {
        if (req.params) req.params.ORG_FILTER_CONDITION = false
        else req.params = { ORG_FILTER_CONDITION : false }
      }
      if (data) {
        if ((action=='save' || new RegExp(/^f_.+$/, 'gm').test(action)) && !(data instanceof FormData)) req.data = cg.record2FormData(data)
        else req.data = data
      }
      if (timeout) req.timeout = timeout
      if (timeout === 0) req.timeout = 0
      else formObject.recordLoading = true
      request(req, silence).then(res => {
        formObject.recordLoading = false
        if (res && res.hasOwnProperty('success') && res.success) {
          if (formObject.hasOwnProperty('dictionary') && res.dictionary) {
            formObject.dictionary = Object.assign(formObject.dictionary, res.dictionary)
            formObject.needLoadDictionary = false
          }
          if (res.parameter && res.parameter.record) {
            formObject.record = Object.assign(formObject.record,res.parameter.record)
            if (formObject.idField) formObject[formObject.idField + 'Saved'] = formObject[formObject.idField]
            //formObject.recordChanged = false
          }        
          if (onSuccess && typeof onSuccess === 'function') onSuccess(res)
        }
        if (res && res.hasOwnProperty('success') && !res.success) {
          if (onError && typeof onError === 'function') onError(res)
          reject(res)
        }
        else resolve(res)
      }).catch(error => {
        formObject.recordLoading = false
        if (onError && typeof onError === 'function') onError(error)
        reject(error)
      })
    })
  },

  form_flowDictionaryOf(formObject) {
    const dictOp = formObject.dictionary.dictFlowNextOperatorSet
    const dictCp = formObject.dictionary.dictFlowCopyToListSet
    const index = formObject.flowAction ? formObject.record.flowSelection : 'add'
    if (!dictOp) {
      formObject.record.flowNextOperator = null
      formObject.dictionary.dictFlowNextOperator = []
    } else {
      if (formObject.flowNextOperatorSet) {
        if (formObject.flowNextOperatorSet.all) formObject.record.flowNextOperator = formObject.flowNextOperatorSet.all
        else if (index) formObject.record.flowNextOperator = formObject.flowNextOperatorSet[index]
        else formObject.record.flowNextOperator = null
      }
      if (dictOp.all) formObject.dictionary.dictFlowNextOperator = dictOp.all
      else if (index && dictOp[index]) formObject.dictionary.dictFlowNextOperator = dictOp[index]
      else formObject.dictionary.dictFlowNextOperator = []
    }
    if (!dictCp) {
      formObject.record.flowCopyToList = null
      formObject.dictionary.dictFlowCopyToList = []
    } else {
      if (formObject.flowCopyToListSet) {
        if (formObject.flowCopyToListSet.all) formObject.record.flowCopyToList = formObject.flowCopyToListSet.all
        else if (index) formObject.record.flowCopyToList = formObject.flowCopyToListSet[index]
        else formObject.record.flowCopyToList = null
      }
      if (dictCp.all) formObject.dictionary.dictFlowCopyToList = dictCp.all
      else if (index && dictCp[index]) formObject.dictionary.dictFlowCopyToList = dictCp[index]
      else formObject.dictionary.dictFlowCopyToList = []
    }
  },
  form_getDictionary(formObject) {
    let params = { 
      loadDictionaryOnly: true,
      needLoadDictionary: formObject.needLoadDictionary,
      flowAction: formObject.flowAction,
      ...cg.record2Params(formObject.record)
    }
    return this.form_request({ formObject, method: 'get', params, action: 'record', silence: true })
  },
  form_getDynaDict(formObject, fields) {
    this.form_request({ formObject, method: 'get', 
      params: { 
        requestDynaFields: fields,
        loadDictionaryOnly: true,
        flowAction: formObject.flowAction, 
        ...cg.record2Params(formObject.record)
      }, action: formObject.isNew ? 'default' : 'record', silence: true }).then(_=>{}).catch(_=>{})
  },

  // 清除表单数据
  form_clearRecord(formObject) {
    formObject.record = {}
    formObject[formObject.idField + 'Saved'] = null
    formObject.openMode = 'view'
    formObject.$emit('record-change', formObject.record)
    formObject.recordChanged = false
  },

  // 根据 id 从后台检索一条记录
  form_getRecordFromServer(formObject, id) {
    if (formObject.$refs.cgForm) formObject.$refs.cgForm.clearValidate()
    if (id === 'null') { // 清空表
      this.form_clearRecord(formObject)
      if (formObject.needLoadDictionary) {
        const params = { needLoadDictionary: true, loadDictionaryOnly: true }
        return this.form_request({ formObject, method: 'get', params, action: 'record', silence: true})

      } else return new Promise((resolve, reject) =>{
        resolve(null)
      })
    } else {
      const params = { needLoadDictionary: formObject.needLoadDictionary, id }
      if (formObject.flowAction) params.flowAction = formObject.flowAction
      return this.form_request({ formObject, method: 'get', params, action: 'record', silence: true,
        onSuccess: res => {
          if (res && res.hasOwnProperty('success') && res.success) {
            if (res.data) {
              formObject.record = res.data
              formObject[formObject.idField + 'Saved'] = formObject.record[formObject.idField]
              if (formObject.hasAuthorityOf(formObject,formObject.baseUrl,'edit',formObject.record)) formObject.openMode = 'edit'
              else formObject.openMode = 'view'
            } else formObject.record = {}
            this.form_setBlobRecord(formObject)
            formObject.$emit('record-change', formObject.record)
            if (formObject.$refs.cgForm) formObject.$refs.cgForm.clearValidate()
          }
          setTimeout(() => {
            formObject.recordChanged = false
            formObject.$emit('refreshed', formObject)
          }, 200)
        } })
    }
  },

  // 新建一条记录，根据表定义决定是否向后台发请求
  form_createNewRecord(formObject,options) {
    const newRecord = formObject.newRecord()
    const that=this
    let   extendRecord = options && options.record ? options.record : {}
    const func=()=>{
      formObject.record = Object.assign(extendRecord, formObject.fixedFields)
      if (formObject.openMode !== 'add') formObject.$emit('openModeChanged', 'add')
      formObject.openMode = 'add'
      formObject.recordChanged = false
      formObject[formObject.idField + 'Saved'] = null
      that.form_setBlobRecord(formObject)
      formObject.$refs && formObject.$refs.cgForm && formObject.$refs.cgForm.clearValidate()
    }
    if (formObject.needLoadDictionary || formObject.needDefaultFromServer) {
      const params = { needLoadDictionary: formObject.needLoadDictionary, ...cg.record2Params(newRecord) }
      if (formObject.flowAction) params[flowAction] = formObject.flowAction
      return this.form_request({ formObject, method: 'get', params, silence: true, action: 'default',
        onSuccess: res => {
          if (res && res.hasOwnProperty('success') && res.success) {
            if (res.data) {
              formObject.record = Object.assign(extendRecord,newRecord,res.data)
            } else formObject.record = Object.assign(extendRecord,newRecord)
          }
          func()
        },
        onError: () => { 
          formObject.record = Object.assign(extendRecord,newRecord) 
          func()
        }
      })
    } else {
      formObject.record = Object.assign(extendRecord,newRecord)
      func()
      return new Promise((resolve, reject) =>{
        resolve(null)
      })
    }
  },

  form_setBlobRecord(formObject) {
    if (formObject.blobRecord) {
      Object.keys(formObject.blobRecord).forEach(field => {
        if (formObject.blobRecord[field].multiple) {
          const names = formObject.record[field] ? formObject.record[field].split(',') : []
          formObject.blobRecord[field].name = names
          formObject.blobRecord[field].blob = []
          names.forEach(_ => { formObject.blobRecord[field].blob.push(null) })
        } else {
          formObject.blobRecord[field].name = formObject.record[field]
          formObject.blobRecord[field].blob = null
        }
      })
    }
  },
  // 关闭表单
  form_close(formObject) {
    if (formObject.showInDialog) formObject.$emit('closeDialog')
    else {
      formObject.$store.dispatch('tagsView/activeLastAfterRemove', formObject.$route)
    }
  },

  form_submit_batch_join(formObject,field,valueList) {
    const data=[]
    const that=this
    formObject.getJoinFields(field)
    valueList.forEach(v=>{
      let row = Object.assign({ ...cg.record2Params(formObject.record) })
      row[field] = v
      Object.keys(row).forEach(k=>{
        if (row[k] == null || row[k] == undefined) delete row[k]
      })
      data.push(row)
    })
    this.form_request({
      formObject, method: 'post',
      data,
      action: 'updateSelective',
      timeout: 15000 * data.length,
    }).then(_=>{
      if (typeof formObject.onChange === 'function') formObject.onChange({refresh: true})
      formObject.recordChanged = false
      that.form_close(formObject)
    }).catch(_=>{
      formObject.recordChanged = false
    })
  },
  // 表单post提交，参数传保存的主键，用formData方式post提交完整记录以及附加属性
  form_submit(formObject, action, closeAfterSuccess) {
    if (!action) action='save'
    const form = formObject.$refs.cgForm
    const that = this
    form.validate((valid) => {
      if (valid) {
        if (formObject.fastMultiJoinField && formObject.isNew && action=='save') {
          const vv = (formObject.record[formObject.fastMultiJoinField]+'').split(',')
          if (vv.length > 1) {
            this.form_submit_batch_join(formObject,formObject.fastMultiJoinField,vv)
            return
          }
        }
        let params = {}
        if (formObject.idField && !formObject.isNew) {
          params[formObject.idField + 'Saved'] = formObject[formObject.idField + 'Saved']
            ? formObject[formObject.idField + 'Saved'] : formObject.record[formObject.idField]
        }
        else params = null
        let data = action=='save' ? cg.record2FormData(formObject.record,formObject.blobRecord) : formObject.record
        let files = 0
        if (formObject.blobRecord && action=='save') {  // ie 不支持 formData get方法
          const blobRecord = formObject.blobRecord
          Object.keys(blobRecord).forEach(field => {
            if (blobRecord[field] && blobRecord[field].multiple && blobRecord[field].blob) {
                for (let i = 0; i < blobRecord[field].blob.length; i++) {
                  if (blobRecord[field].blob[i] && blobRecord[field].name[i]) {
                    files++
                  }
                }
            } else if (blobRecord[field] && blobRecord[field].blob && blobRecord[field].blob && blobRecord[field].name)  {
                files++
            }
          })        
        }
        this.form_request({
          formObject, method: 'post', params,
          data,
          action: action,
          timeout: 15000 * ((files?files:0) + 1),
          onSuccess: res => {
            if (res && res.success && !(res.parameter && res.parameter.noChanged)) {
              if (formObject.blobRecord &&  files) {
                that.form_setBlobRecord(formObject)
              }
              const pam = { record: formObject.record, isNew: formObject.isNew }
              formObject.$emit('change', pam)
              if (typeof formObject.onChange === 'function') formObject.onChange(pam)
            }
            formObject.openMode = 'edit'
            formObject.$emit('openModeChanged', 'edit')
            formObject.$emit('submitted', formObject)
            if (closeAfterSuccess || formObject.flowAction) that.form_close(formObject)
            else setTimeout(()=>{
              formObject.recordChanged = false
            },200)
          }
        }).then(_=>{}).catch(_=>{})
      } else {
        setTimeout(()=>{
          let $el = formObject.$refs.cgForm.$el.querySelector('.el-tabs__content .el-form-item__error')
          if ($el) {
            $el = getParent($el, null, 'el-tab-pane')
            if ($el) {
              let id = $el.id
              formObject.tabSelected = id.substring(5)
            }
          }
        },200)
      }
    })
  },

  // 初始化数据
  form_initialByQuery(formObject, refresh) {
    const query = formObject.openParams()
    formObject.openMode = query.openMode
    formObject.onChange = typeof query.onChange ==='function' ? query.onChange : null
    formObject.fixedFields = typeof query.fixedFields ==='object' ? query.fixedFields : null
    if (formObject.$refs.cgForm) formObject.$refs.cgForm.clearValidate()
    if (formObject.hasOwnProperty('dictionary')) {
      if (query.hasOwnProperty('dictionary') && typeof query.dictionary ==='object') {
        formObject.dictionary = Object.assign(formObject.dictionary, query.dictionary)
        formObject.needLoadDictionary = false
      } else formObject.needLoadDictionary = true
    }
    if (query.hasOwnProperty('record') && typeof query.record ==='object') {
      if (refresh) this.form_doAction(formObject, 'refresh', { id: query.record[formObject.idField] })
      else {
        formObject.record = query.record
        formObject[formObject.idField + 'Saved'] = query.record[formObject.idField]
        if (!formObject.openMode) formObject.openMode = 'view'
      }
    } else if (query.hasOwnProperty('id') && query.id) {
      this.form_getRecordFromServer(formObject, query.id)
      formObject[formObject.idField + 'Saved'] = query.id
      if (!formObject.openMode) formObject.openMode = 'view'
    } else if (formObject.isNew) {
      this.form_createNewRecord(formObject)
      formObject.openMode = 'add'
    }
    this.form_setBlobRecord(formObject)
    formObject.$emit('initialed', formObject)
  },
  // 激活时检查对象是否更新，决定是否更改record
  form_activedRefresh(formObject) {
    if (formObject.$route.meta && formObject.$route.meta.refresh) {
      delete formObject.$route.meta.refresh
      this.form_initialByQuery(formObject, true)
      formObject.$store.dispatch('tagsView/updateVisitedView', formObject.$route)
    } else if (formObject.openParams() && Object.keys(formObject.openParams()).length > 0) {
      let needChange = false
      const query = formObject.openParams()
      if (formObject.$route.meta && formObject.$route.meta.refreshAtOnce) {
        delete formObject.$route.meta.refreshAtOnce
        needChange = true
      }
      const qom = query.openMode ? query.openMode : 'add'
      const fom = formObject.openMode ? formObject.openMode : 'add'

      if ( (qom !== fom) || ((query.fixedFields || formObject.fixedFields) && !cg.equals(query.fixedFields, formObject.fixedFields)) ) {
        needChange = true
      }
      else if (query.hasOwnProperty('record') && query.record) {
        if (typeof query.record === 'object' && formObject[formObject.idField + 'Saved'] && formObject[formObject.idField + 'Saved'] === query.record[formObject.idField]) return // 避免重复刷新
        needChange = true
      } else if (query.hasOwnProperty('id') && query.id) {
        if (formObject[formObject.idField + 'Saved'] && formObject[formObject.idField + 'Saved'] === query.id) return // 避免重复刷新
        needChange = true
      }
      if (needChange) {
        this.form_initialByQuery(formObject)
        formObject.$store.dispatch('tagsView/updateVisitedView', formObject.$route)
      }
    }
  },

  // 表单action操作
  form_doAction(formObject, action, options) {
    if (action=='goHome') listObject.$router.push('/')
    else if (action === 'add') this.form_createNewRecord(formObject, options)
    else if (action === 'submit') this.form_submit(formObject, 'save')
    else if (action === 'refresh') this.form_getRecordFromServer(formObject, options.id)
    else if (options) { // 处理自定义按钮操作
      const id = formObject.record[formObject.idField]
      let params = { id, action }
      if (typeof formObject.getActionParams === 'function') params = Object.assign(params, formObject.getActionParams(action, options, id))
      if ((options.actionProperty === 'aj' || options.actionProperty === 'go') && options.appendClass) {
        try {
          const ap = eval('(' + options.appendClass + ')')
          if (typeof ap === 'object') params = Object.assign(params, ap)
        } catch {}
      }
      if (options.actionProperty === 'aj') { // 后台操作
        this.form_request({ formObject, method: 'get', params, action: 'action/' + action,
          timeout: params.hasOwnProperty('timeout') ? params.timeout : null,
          onSuccess: ()=>{
            if (typeof options.onSuccess === 'function') options.onSuccess()
            formObject.$emit(action+'-completed', formObject)
          },
          onError: options.onError,
          silence: options.silence
        }).then(_=>{}).catch(_=>{})
      } else if (options.actionProperty === 'go') { // 页面跳转
        if (params.url) {
          const url = params.url
          delete params.url
          if (options.rowProperty == 'sr') {
            params.record=formObject.record
          }
          cg.jump2Url(url, params, formObject.$router)
        }
      } else { // 自定义操作函数
        let func = options.appendClass
        if (func) {
          if (func.indexOf('this.') == 0) func = 'formObject'+func.substring(4)
          eval(func)
        }
      }
    }
  },
  form_disableIme(formObject) {
    setTimeout(()=>{
    formObject.$refs.cgForm.$el.querySelectorAll(".el-form--label-left.cg-form-cell .el-form-item__label").forEach(e=>{
      formObject.$refs.cgForm.$el.querySelectorAll(".el-form--label-left.cg-form-cell .el-form-item__label").forEach(e=>{
        let w = e.offsetWidth
        if (w) {
          let c = e.parentNode.querySelector('.el-form-item__content')
          setStyle(c, 'margin-left', (w + 2)+'px') 
        }
      })
    })
  },500)

    formObject.$refs.cgForm.$el.querySelectorAll(".el-date-editor .el-input__inner").forEach(e=>{
      e.readOnly=true
    })
    formObject.$refs.cgForm.$el.querySelectorAll(".el-select .el-input__inner").forEach(e=>{
      e.readOnly=true
    }) 
  }
}
