import cg from '@/utils/cg'
import { request } from '@/utils/request'
import { Message } from 'element-ui'
import Sortable from 'sortablejs'
import time from './time'
import { scrollTop, removeClass,addClass, getParent, offsetToBody, getRect } from '@/utils/dom'
import { generateTitle } from './i18n'
import { record2FormData } from '@/utils/cg'
import { resolvePreset } from '@babel/core'
export default {
  list_tableInit(listObject,sortField) {
    listObject.$refs && listObject.$refs.cgList && listObject.$refs.cgListBacktop 
      && listObject.$refs.cgList.$el && listObject.$refs.cgListBacktop.$el
      && listObject.$refs.cgList.$el.appendChild(listObject.$refs.cgListBacktop.$el)
    if (sortField && !listObject.joinMode && listObject.isTableMode()) this.list_dragRow(listObject, sortField)
    if (listObject.isTableMode()) this.list_dragColumn(listObject)
    listObject.scroller = listObject.$el.querySelector('.cg-mobile-scroller')
  },
  // 初始化列表选择
  list_initialSelections(listObject) {
    if (listObject.selections && listObject.selectionKey) {
      if (listObject.multiple) {
        listObject.selections.split(',').forEach(v => {
          const row = cg.findRecord(v, listObject.rows, listObject.selectionKey, 'children')
          if (row && listObject.$refs.cgList) listObject.$refs.cgList.toggleRowSelection(row, true)
        })
      } else {
        const row = cg.findRecord(listObject.selections, listObject.rows, listObject.selectionKey, 'children')
        if (row && listObject.$refs.cgList) listObject.$refs.cgList.setCurrentRow(row)
      }
    }
  },

  // 列表向后台发起请求 method : 请求方法 silence: 静默方式 params：请求参数 data: post数据
  // action：controller的url方法  onSuccess onError : 成功和错误的回调，一个参数
  list_request({ listObject, method, silence, params, data, action, onSuccess, onError, timeout }) {
    new Promise((resolve, reject) => {
      let req = {
        url: listObject.baseUrl + '/' + action,
        method
      }
      if (params) {
        if (params.request) {
          req = Object.assign(req, params.request)
          delete params.request
        }
        req.params = params
      }
      if (listObject.ignoreOrgFilter) {
        if (req.params) req.params.ORG_FILTER_CONDITION = false
        else req.params = { ORG_FILTER_CONDITION : false }
      }
      if (data) {
        if (action=='save' && !(data instanceof FormData)) req.data = record2FormData(data)
        else req.data = data
      }
      if (timeout) req.timeout = timeout
      if (timeout === 0) req.timeout = 0
      if ((req.timeout === undefined || req.timeout !== 0 ) ) listObject.listLoading = true
      request(req, silence).then(res => {
        if (res && res.hasOwnProperty('success') && res.success) {
          if (listObject.needLoadDictionary && res.dictionary) {
            listObject.dictionary = Object.assign(listObject.dictionary,res.dictionary)
            listObject.needLoadDictionary = false
          }
          if (onSuccess && typeof onSuccess === 'function') onSuccess(res)
        }
        listObject.listLoading = false
        resolve(res)
      }).catch(error => {
        if (onError && typeof onError === 'function') onError(error)
        listObject.listLoading = false
        reject(error)
      })
    })
  },
  list_loadMore(listObject) {
    if (listObject.paginationPageSize && listObject.paginationCurrentPage * listObject.paginationPageSize < listObject.paginationTotalRecords) {
      const that = this
      that.list_getDataFromServer(listObject, null, true)  
    }
  },
  // 获得后台查询参数
  list_getQueryParams(listObject, loadmore) {
    const params = {}
    if (listObject.paginationPageSize) {
      if (loadmore && listObject.rows.length > 0) listObject.paginationCurrentPage ++
      params.pageSize = listObject.paginationPageSize
      params.pageNumber = listObject.paginationCurrentPage
    }
    if (listObject.sortableFields.length > 0) {
      params.sort = listObject.sortableFields.join(',')
      params.order = listObject.sortableFieldsOrder.join(',')
    }
    const fixedQueryRecord = Object.assign({}, listObject.fixedQueryRecord)
    const keys = Object.keys(listObject.queryRecord)
    // 需要查询的字段列表
    let entities = []
    if (listObject.queryRecord.search) {
      if (listObject.queryRecord.searchFields)
      params.search = listObject.queryRecord.search
      if (listObject.queryRecord.searchFields) entities = listObject.queryRecord.searchFields.split(',')
    } else if (listObject.queryRecordFields && listObject.queryRecordFields.length > 0) {
      keys.forEach( key => {
        if (key == 'search' || key == 'searchFields') return
        const realKey = (key.endsWith('_start') ? key.substring(0,key.length - 6) 
          : (key.endsWith('_end') ? key.substring(0,key.length - 4) : key))
        if (listObject.queryRecordFields.indexOf(realKey)<0) return  
        const value = listObject.queryRecord[key]
        const isArray = (Object.prototype.toString.call(value) === '[object Array]')
        if (isArray && value.length > 0 && !fixedQueryRecord[realKey])
        {
          let hasValue = false
          let isDate = false
          let ss =[]
          value.forEach(v=>{
            if (Object.prototype.toString.call(v) === '[object Date]') {
              ss.push(time.toString(v))
              isDate = true
              hasValue = true
            } else if (v || v===0) {
              ss.push(v+'')
              hasValue=true
            } else ss.push('')
          })
          if (hasValue) {
            if (entities.indexOf(realKey) < 0) entities.push(realKey)
            if (isDate) {
              if (ss.length > 0) params[realKey + '_start'] = ss[0]
              if (ss.length > 1) params[realKey + '_end'] = ss[1]
            } else params[key] = ss.join(',')
          }
        }
        else if (!isArray && (value || value ===0) && !fixedQueryRecord[realKey]) {
          if (entities.indexOf(realKey) < 0) entities.push(realKey)
          params[key] = value
        }
      })
    }
    Object.keys(fixedQueryRecord).forEach( key => {
      if (entities.indexOf(key) < 0) entities.push(key)
    })
    params.queryEntities = entities.join(',')
    params.pathNameOfQuery = listObject.path
    return Object.assign(params, fixedQueryRecord) // fixedQueryRecord 优先
  },

  // 清除列表
  list_clearRow(listObject) {
    listObject.rows = []
    listObject.selections =''
    listObject.$emit('selectionChange', '')
    listObject.$emit('refreshed', listObject)
    if (listObject.joinModeRefresh) {
      listObject.$emit('showJoinList')
      delete listObject.joinModeRefresh
    }
    listObject.listLoading = false
  },


  // 刷新后台数据，刷新前已修改号查询参数, loadmore 由 loadmore 传递 === listObject.$refs.infiniteLoading.stateChanger
  list_getDataFromServer(listObject, extParams, loadmore) {
    const params = Object.assign({
      needLoadDictionary: listObject.needLoadDictionary,
      defaultOrder: listObject.defaultOrder,
      ...this.list_getQueryParams(listObject, loadmore)
    }, extParams)
    if (listObject.groupByEntityFields) params.groupByEntityFields = listObject.groupByEntityFields
    if (Object.keys(params).some(key=>params[key] === 'null')) this.list_clearRow(listObject)
    else {
      this.list_request({
        listObject, method: 'get', silence: true,
        params,
        action: 'list',
        onSuccess: res => {
          if (res && res.hasOwnProperty('success') && res.success) {
            if (res.data && res.data.data) {
              if (listObject.paginationPageSize) { // 分页
                listObject.paginationTotalRecords = res.data.total
              } 
              if (loadmore) {
                if (listObject.parentField && listObject.idField) {
                  if (listObject.rows.length > 0) {
                    const rows = cg.treeNode2Rows(listObject.rows)
                    res.data.data.forEach(r=>rows.push(r))
                    listObject.rows = cg.rows2TreeNode(rows, listObject.idField, listObject.parentField)
                  } else listObject.rows = cg.rows2TreeNode(res.data.data, listObject.idField, listObject.parentField)
                }
                else res.data.data.forEach(r=>listObject.rows.push(r))
              } else {
                if (listObject.parentField && listObject.idField) listObject.rows = cg.rows2TreeNode(res.data.data, listObject.idField, listObject.parentField)
                else listObject.rows = res.data.data
              }
              listObject.$refs && listObject.$refs.cgList && listObject.$refs.cgList.clearSelection()
              listObject.$nextTick(() => {
                this.list_initialSelections(listObject)
              })
              //listObject.$refs.cgList.doLayout()
            } else listObject.rows = []
            if (listObject.filterQueryDictionary) {
              const keys = Object.keys(listObject.filterQueryDictionary)
              listObject.rows.forEach(r => {
                keys.forEach( k => {
                  if ((r[k] || r[k] === 0) && listObject.filterQueryDictionary[k].indexOf(r[k]) < 0) 
                    listObject.filterQueryDictionary[k].push(r[k])
                })
              })
            }
            listObject.$emit('selectionChange', '')
            listObject.$emit('refreshed', listObject)
            if (listObject.joinModeRefresh) {
              listObject.$emit('showJoinList')
              delete listObject.joinModeRefresh
            }
            const that = this
            if (listObject.mobile) {
              if (!listObject.widthAdjusted) {
                that.list_adjustWidth(listObject)
                listObject.widthAdjusted = true
              }            
            }
          }
        },
        onError: err => {
        }
      })
    }
  },

  // actived时判断是否刷新数据
  list_activedRefresh(listObject) {
    if (listObject.$route.meta && listObject.$route.meta.refresh) {
      delete listObject.$route.meta.refresh
      this.list_doAction(listObject, 'refresh')
    }
  },

  // 数据分组算法，内部调用
  list_groupFields(listObject, groupFields, row, column, rowIndex, columnIndex) {
    if (listObject.totalEdittingRows) return
    const field = column.property
    const groupIndex = groupFields.indexOf(field)
    if (groupIndex >= 0) {
      let rowspan = 0
      if (rowIndex > 0 && listObject.rows[rowIndex - 1][field] === listObject.rows[rowIndex][field]) {
        return [0, 0]
      }
      for (let i = rowIndex; i < listObject.rows.length - 1; i++) {
        if (listObject.rows[i][field] === listObject.rows[i + 1][field]) rowspan++
        else break
      }
      if (rowspan > 0) {
        return { rowspan: rowspan + 1, colspan: 1 }
      }
    }
  },

  // 排序
  list_fieldSort(listObject, prop, order) {
    if (!order) {
      const pos = listObject.sortableFields.indexOf(prop)
      if (pos >= 0) {
        listObject.sortableFields.splice(pos, 1)
        listObject.sortableFieldsOrder.splice(pos, 1)
      }
    } else {
      const pos = listObject.sortableFields.indexOf(prop)
      if (pos >= 0) {
        listObject.sortableFields.splice(pos, 1)
        listObject.sortableFieldsOrder.splice(pos, 1)
      }
      listObject.sortableFields.unshift(prop)
      listObject.sortableFieldsOrder.unshift(order === 'ascending' ? 'asc' : 'desc')
    }
  },

  // 根据id主键修改一条记录
  list_changeRecord(rows, id, idField, row) {
    const that = this
    if (rows && rows.length > 0) {
      for (let i=0; i<rows.length; i++) {
        if (rows[i][idField] === id) {
          rows.splice(i,1,row)
          return true
        }
        if (rows[i].children && rows[i].children.length > 0 
          && that.list_changeRecord(rows[i].children, id, idField, row)) return true
      }
    }
    return false
  },

  // 删除记录后更新列表而不重新从后台获取数据

  list_removeRecord(listObject,idStringList) {
    if (!listObject || !listObject.idField || !idStringList) return
    let rows = listObject.rows
    if (listObject.parentField && listObject.rows.length) rows = cg.treeNode2Rows(listObject.rows)
    let ids=String(idStringList).split(',')
    let count = rows.length
    for (let i = 0; i < count ; i++) {
      let p = ids.indexOf(String(rows[i][listObject.idField]))
      if (p>=0) {
        listObject.$refs.cgList.toggleRowSelection(rows[i], false)
        rows.splice(i,1)
        ids.splice(p,1)
        count --
        i --
        if (count <=0 || ids.length <= 0) break
      }
    }
    if (count == 0) listObject.rows = []
    else if (listObject.parentField) listObject.rows = cg.rows2TreeNode(rows, listObject.idField, listObject.parentField)
    else listObject.rows = rows
  },

  // 打开表单编辑器 row为 null(新建) id 或记录, path: route , openMode : add/view/edit
  list_openRecordView({listObject, row, path, openMode, flowAction}) {
    const that = this
    const needRefresh = listObject.parentField || getParent(listObject.$el,null,'cg-child')
    let id = row ? (typeof row === 'object' ? row[listObject.idField] : row) : null
    const fixedFields = Object.assign({},listObject.fixedQueryRecord)
    if (!row && listObject.parentField && listObject.idField && !listObject.multiple) { // 只在该条目下新建
      const currentRow = listObject.$refs.cgList.store.states.currentRow
      if (currentRow)  fixedFields[listObject.parentField] = currentRow[listObject.idField]
    }
    const query = {
      openFrom: listObject.$route.path,
      openMode: flowAction ? 'edit' : openMode, 
      flowAction,
      dictionary: listObject.dictionary, 
      fixedFields, 
      onChange: openMode == 'view' ? null : function({ record, isNew, refresh }) {
        if (needRefresh || flowAction || refresh) that.list_doAction(listObject,'refresh') 
        else if (isNew && record) {
          if (listObject.parentField && listObject.idField) {
            if (listObject.rows.length > 0) {
              const rows = cg.treeNode2Rows(listObject.rows)
              rows.push(record)
              listObject.rows = cg.rows2TreeNode(rows, listObject.idField, listObject.parentField)
            } else listObject.rows = [record]
          } else listObject.rows.unshift(record)
          listObject.$emit('refreshed', listObject)
          if (listObject.joinModeRefresh) {
            listObject.$emit('showJoinList')
            delete listObject.joinModeRefresh
          }
        } else if (id) {
          if (that.list_changeRecord(listObject.rows, id, listObject.idField, record)) {
            listObject.$emit('refreshed', listObject)
            if (listObject.joinModeRefresh) {
              listObject.$emit('showJoinList')
              delete listObject.joinModeRefresh
            }
          }
        }
        if (record) id = record[listObject.idField]
      }
    }
    if (listObject.needLoadDictionary) query.needLoadDictionary = true
    if (row && typeof row === 'object') {
      query.record = row
      Object.keys(row).forEach(field => {
        if (row[field] instanceof Date) query.record[field] = time.fullString(row[field])
      })
      query.id = row[listObject.idField]
    }
    else if (row) query.id = row
    cg.jump2Url(path, query, listObject.$router)
  },

  list_getCurrentRow(listObject) {
    if (!listObject) return null
    if (listObject.multiple) {
      if (listObject.$refs && listObject.$refs.cgList && listObject.$refs.cgList.store.states.selection.length == 1) {
        return listObject.$refs.cgList.store.states.selection[0]
      } else return null
    }
    else if (listObject.$refs && listObject.$refs.cgList) return listObject.$refs.cgList.store.states.currentRow  
    else return null
  },
  // 检查选择是否满足要求 multiple : 是否可以多选
  list_checkSelections(listObject, multiple) {
    if ((listObject.multiple && listObject.$refs.cgList.store.states.selection.length === 0) ||
      (!listObject.multiple && !listObject.$refs.cgList.store.states.currentRow)) {
      Message({
        message: listObject.$t('system.message.needItem'),
        type: 'warning',
        duration: 5 * 1000
      })
      return null
    }
    if (listObject.multiple) {
      if (multiple) return listObject.$refs.cgList.store.states.selection
      else if (listObject.$refs.cgList.store.states.selection.length > 1) {
        Message({
          message: listObject.$t('system.message.onlyOne'),
          type: 'warning',
          duration: 5 * 1000
        })
      } else return listObject.$refs.cgList.store.states.selection[0]
    } else if (multiple) return [listObject.$refs.cgList.store.states.currentRow]
    else return listObject.$refs.cgList.store.states.currentRow
  },

  list_adjustWidth(listObject) {
    const $el=listObject.$refs.cgList.$el
    const w = $el.offsetWidth
    const $header = $el.querySelector('.el-table__header-wrapper table')
    const $body = $el.querySelector('.el-table__body-wrapper table')
    const hw =  $header && $header.offsetWidth
    if (w && hw && w>hw) {
      $header.style.width= w+"px"
      $body.style.width= w+"px"
    }
  },
  /* mode : main,context, row 指定某行或选择 */
  list_allActions(listObject,mode,row) {
    let actionList = ""
    let oriAllActions = listObject.allActions
    if (listObject.fixedQueryRecord) {
      const n = listObject.fixedQueryRecord
      Object.keys(n).length && Object.keys(n).some(k=>{
            if (!cg.hasValue(n[k]) || n[k]=='null') { 
              oriAllActions = '' // nulloriAllActions.replace(',editInline_add,',',').replace(',add,',',')  
              return true 
            }
      })
    }
    const allActions = oriAllActions
    let additional =  listObject.additionalActions ? listObject.additionalActions : []
    if (!row) row = (listObject.contextMenu && listObject.contextMenu.visible && listObject.contextMenu.row ? 
      listObject.contextMenu.row : this.list_getCurrentRow(listObject) )
    if (mode=='main') {
      actionList = oriAllActions
      if (row && row.hasOwnProperty('flowAvailableActions'))  {
        actionList = actionList+','+row.flowAvailableActions
      }
      if (!listObject.mobile && listObject.editInlineFields) {
        if (listObject.totalEdittingRows) actionList = actionList+',editInline_save_all,editInline_close_all'
      }
    }
    else {
      if (row && row.hasOwnProperty('flowAvailableActions'))  {
        actionList = row.flowAvailableActions
      }
      else if (row) actionList = allActions
      else actionList = ''
      if (row) {
        additional.forEach(a=>{
          if (a.rowProperty=='sr' && allActions.indexOf(a.action) >=0) actionList = actionList + ',' +a.action
        })
        if (!listObject.mobile && listObject.editInlineFields) {
          if (row.inlineEditting) actionList = actionList+',editInline_save,editInline_close'
          else actionList = actionList+',editInline_open' 
        }
      }
    }   
    if (listObject.flowActionList)  additional=additional.concat(listObject.flowActionList)
    return cg.allActionsOf(listObject, listObject.baseUrl, actionList, additional, mode=='context', row) 
  },

  // 系统action操作  action ：操作名  options : 用户定义按钮参数数据
  list_doAction(listObject, action, options) {
    if (listObject.showQuery) {
      listObject.showQuery = false
      if (action==='refresh' && listObject.joinMode) listObject.joinModeRefresh = true
    }
    listObject.showActionView = false
    if (action=='goHome') listObject.$router.push('/')
    else if (action.indexOf('editInline_')==0)  this.list_editInlineAction(listObject,action,options)
    else if (action === 'refresh') {
      if (listObject.totalEdittingRows) this.list_closeEditInlineAtOnce(listObject)
      if (listObject.scroller) { 
        listObject.paginationCurrentPage = 1
        listObject.paginationTotalRecords = 0
        listObject.rows = [] 
        listObject.scroller.dispatchEvent(new Event('resetScroller'))
      }
      this.list_getDataFromServer(listObject)
    }
    else if (action === 'query') listObject.showQuery = true
    else if (action === 'add') {
      const row = options && options.needRow ? this.list_checkSelections(listObject, false) : null
      this.list_openRecordView({listObject, row, path: listObject.formPath, openMode: 'add'})
    }
    else if (action === 'view' || action === 'edit' || action.indexOf('flow.') == 0) {
      const row = options && options.row ? options.row : this.list_checkSelections(listObject, false)
      if (row) {
        const realAction = (action.indexOf('flow.') == 0 ? action.substring(5) : action)
        if (row.hasOwnProperty('flowAvailableActions') && (!row.flowAvailableActions || (','+row.flowAvailableActions+',').indexOf(','+realAction+',') < 0)) {
          Message({
            message: listObject.$t('error.no_authority'),
            code: '',
            type: 'warn',
            duration: 3 * 1000
          })
          return
        }
        if (action.indexOf('flow.') == 0) this.list_openRecordView({listObject, row, path: listObject.baseUrl+'/'+realAction, openMode:'flow', flowAction: realAction})
        else this.list_openRecordView({listObject, row, path: listObject.formPath, openMode:action})
      }
    } else if (action === 'delete' || action === 'batdel') {
      const row = options && options.row ? options.row : this.list_checkSelections(listObject, action === 'batdel')
      if (row) {
        if (action == 'delete' && row.inlineEditting && !row[listObject.idField]){ // 行内编辑插入行直接删除
          listObject.rows.splice(listObject.rows.findIndex(r => r === row), 1)
          return
        } else if (action == 'batdel') {
          const row1=[]
          row.forEach(r=>{
            if (r.inlineEditting && !r[listObject.idField]) {
              listObject.rows.splice(listObject.rows.findIndex(record => record === r), 1)
              //row.splice(row.findIndex(record => record === r), 1)
            } else row1.push(r)
          })
          if (row1.length==0) return
        }
        const r = (action === 'batdel' ? row[0] : row)
        if (r.hasOwnProperty('flowAvailableActions') && (!r.flowAvailableActions || (','+r.flowAvailableActions+',').indexOf(',delete,') < 0)) {
          Message({
            message: listObject.$t('error.no_authority'),
            code: '',
            type: 'warn',
            duration: 3 * 1000
          })
          return
        }
        if (listObject.parentField && !listObject.removeLeftRecordOnRightJoin) {
          let hasChildren = false
          if (action=='delete' && row.children && row.children.length > 0) hasChildren = true
          else if (action == 'batdel') {
            hasChildren = row.some(r=>r.children && r.children.length > 0)
          }
          if (hasChildren) {
            Message({
              message: listObject.$t('system.message.removeChildrenFirst'),
              code: '',
              type: 'warn',
              duration: 3 * 1000
            })
            return
          }
        }
        const that = this
        const remove = function () {
          const ids = action === 'delete' ? (row[listObject.idField]?row[listObject.idField]:null) 
                : row.reduce((v0, s) => v0 + (s[listObject.idField]?(v0 ? ',' : '') + s[listObject.idField]:''), '')
          if (ids) that.list_request({
            listObject, method: 'delete', silence: false,
            params: action === 'delete' ? { id: ids } : { ids },
            action,
            onSuccess: () => { 
              that.list_removeRecord(listObject, ids) 
              listObject.$emit('refreshed', listObject)
            }
          })
        }
        const confirmText = listObject.removeLeftRecordOnRightJoin ? listObject.$t('system.message.confirmRemoveLeftRecordOnRightJoin') 
           : (listObject.$t(listObject.hasSonTables ? 'system.message.confirmDeleteCascade' : 'system.message.confirmDelete'))
        listObject.$confirm(confirmText, listObject.$t('system.action.confirm'), {
          confirmButtonText: listObject.$t('system.action.ok'),
          cancelButtonText: listObject.$t('system.action.cancel'),
          closeOnClickModal: false,
          type: 'warning'
        }).then(_ => {
          remove()
        }).catch(_ => {
        })
      }
    } else if (action === 'export') {
      if (listObject.localExport) this.list_exportExcel(listObject)
      else 
      {
        let titles = {}
        const shows = []
        if (listObject.rows.length > 0) {
          Object.keys(listObject.rows[0]).forEach(key => {
            titles['title_' + key] = listObject.$t(listObject.generatorName + '.field.' + key)
          })
        }
        listObject.$refs.cgList.store.states.columns.forEach(col => {
          shows.push(col.property)
        })
        generateTitle.dd = ''
        this.list_request({
          listObject, method: 'get', silence: false, 
          params: { 
            request: {
              responseType: 'blob'
            },
            fileName: listObject.generatorName + '.xlsx',
            ...titles, 
            show_entities: shows.join(','), 
            ...this.list_getQueryParams(listObject) 
          }, 
          action: 'export',
          onError: (error) => {
            Message({
              message: error,
              type: 'error',
              duration: 5 * 1000
            })
          }
        })
      }
    } else if (action === 'import') {
      this.list_request({ listObject, method: 'get', params: { ...this.list_getQueryParams(listObject) }, 
        timeout: 0, action: 'import',
        onSuccess: (res) => {
          listObject.doAction('refresh')
        } 
      })
    } else if (options) { // 处理自定义按钮操作
      const that = this
      const func = function(id) {
        let params = { openFrom: listObject.$route.path, id, action }
        if (typeof listObject.getActionParams === 'function') params = Object.assign(params, listObject.getActionParams(action, options, id))
        if ((options.actionProperty === 'aj' || options.actionProperty === 'go') && options.appendClass) {
          try {
            const ap = eval('(' + options.appendClass + ')')
            if (typeof ap === 'object') params = Object.assign(params, ap)
          } catch {}
        }
        if (options.actionProperty === 'aj') { // 后台操作
          that.list_request({ listObject, method: 'get', params, action: 'action/' + action,
            timeout: options.hasOwnProperty('timeout') ? options.timeout : null,
            onSuccess: (res) => { 
              if (options.needRefresh) that.list_getDataFromServer(listObject)
              if (typeof options.onSuccess === 'function')  options.onSuccess(res)
              listObject.$emit(action+'-completed', listObject)
            },
            onError: options.onError,
            silence: options.silence
          })
        } else if (options.actionProperty === 'go') { // 页面跳转          
          if (params.url) {
            const url = params.url
            delete params.url
            if (options.rowProperty == 'sr' || options.rowProperty == 'mr') {
              const row = options.row ? options.row : that.list_checkSelections(listObject, options.rowProperty == 'mr')
              if (!row) return
              params.record=row
            }
            cg.jump2Url(url, params, listObject.$router)
          }
        } else { // 自定义操作函数
          let func = options.appendClass
          if (func) { 
            func = func.trim()
            if (func.indexOf('this.') == 0) func = 'listObject'+func.substring(4)   
            if ((options.rowProperty == 'sr' || options.rowProperty == 'mr') && !options.row) {
              options.row = that.list_checkSelections(listObject, options.rowProperty == 'mr')
              if (!options.row) return              
            } 
            if (/.*\(\s*\)$/.test(func)) func = func.substring(0,func.length-1) + 'options)'
            else func = func.substring(0,func.length-1) + ',options)'
            eval(func)
          }
        }
      }
      let id = null
      if (options.rowProperty === 'sr' || options.rowProperty === 'mr') {
        const row = options && options.row ? options.row : this.list_checkSelections(listObject, options.rowProperty === 'mr')
        if (!row) return
        id = (options.rowProperty === 'sr' ? row[listObject.idField] : row.reduce((v0, s) => v0 + (v0 ? ',' : '') + s[listObject.idField], ''))
      }
      if (options.confirm) {
        listObject.$confirm(listObject.$t(options.confirm), listObject.$t('system.action.confirm'), {
          confirmButtonText: listObject.$t('system.action.ok'),
          cancelButtonText: listObject.$t('system.action.cancel'),
          closeOnClickModal: false,
          type: 'warning'
        }).then(_ => {
          func(id)
        }).catch(_ => {
        })
      } else func(id)
    }
  },

  // 导出显示的字段为excel表单
  list_exportExcel(listObject) {
    // 对象数组转换为json, list为数据， fieldFilter为输出字段的数组
    const formatRows = (list, columns) => {
      if (!list || list.length === 0) return []
      return list.map(v => columns.map(c => {
        if (c.type === 'date') return time.toString(v[c.property],'YYYY-MM-dd')
        else if (c.type === 'datetime') return time.toString(v[c.property],'YYYY-MM-dd HH:mm')
        else if (c.type === 'time') return time.toString(v[c.property],'HH:mm')
        else if (c.type === 'dict') return listObject.dictValue(v[c.property],listObject.dictionary['dict'+c.property.substring(0,1).toUpperCase() + c.property.substring(1)],true,true)
        else if (typeof v[c.property] == 'boolean') return v[c.property] ? 'system.action.yes'.local() : 'system.action.no'.local()
        else return v[c.property]
      }))

    }
    import('@/utils/Export2Excel').then(excel => {
      const columns = listObject.$refs.cgList.store.states.columns.filter((c) => c.type !== 'selection' && c.type !== 'index')
      const tHeader = columns.reduce((v0, c) => {
        v0.push(c.label)
        return v0
      }, [])
      const data = formatRows(listObject.parentField ? cg.treeNode2Rows(listObject.rows) : listObject.rows, columns)
      excel.export_json_to_excel({
        header: tHeader,
        data,
        filename: listObject.generatorName,
        autoWidth: true,
        bookType: 'xlsx'
      })
    })
  },
  // 行拖拽
  list_dragRow(listObject, sortField) {
    if (listObject.scroller) return
    const that = this
    const elrow = listObject.$refs.cgList.$el.querySelectorAll('.el-table__body-wrapper > table > tbody')[0]
    Sortable.create(elrow, {
      ghostClass: 'sortable-ghost', // Class name for the drop placeholder,
      setData: function(dataTransfer) {
        // to avoid Firefox bug
        // Detail see : https://github.com/RubaXa/Sortable/issues/1012
        dataTransfer.setData('Text', '')
      },
      onEnd: evt => {
        if (evt.oldIndex !== evt.newIndex) {
          let newValue
          const rows = listObject.parentField ? cg.treeNode2Rows(listObject.rows) : listObject.rows
          if (evt.oldIndex < evt.newIndex) {
            newValue = (evt.newIndex < rows.length - 1)
              ? Math.round((rows[evt.newIndex][sortField] + rows[evt.newIndex + 1][sortField]) / 2)
              : rows[evt.newIndex][sortField] + 10
          } else {
            newValue = (evt.newIndex > 0)
              ? Math.round((rows[evt.newIndex][sortField] + rows[evt.newIndex - 1][sortField]) / 2)
              : Math.round(rows[evt.newIndex][sortField] / 2)
          }
          rows[evt.oldIndex][sortField] = newValue
          const data = {}
          data[listObject.idField] = rows[evt.oldIndex][listObject.idField]
          data[sortField] = newValue
          that.list_request({ listObject, method: 'post', silence: true, action: 'updateSelective',
            data: [data],
            onSuccess: (res) => {
              that.list_getDataFromServer(listObject, { resortFirstField: sortField })
            }
          })
          const targetRow = rows.splice(evt.oldIndex, 1)[0]
          rows.splice(evt.newIndex, 0, targetRow)
          if (listObject.parentField)  listObject.rows = cg.rows2TreeNode(rows) 
        }
      }
    })
  },
  // 列拖拽，删除
  list_dragColumn(listObject) {
    listObject.$refs.cgList.$on('header-dragend',(newWidth, oldWidth, column, event)=>{  // 边界拖动bug
      if (column && column.id) {
        listObject.$refs.cgList.$el.querySelectorAll('th.'+column.id).forEach($e=>{
          removeClass($e,"sortable-ghost")
        })
      }
    })
    const elcol = listObject.$refs.cgList.$el.querySelectorAll('.el-table__header-wrapper tr')[0]

    Sortable.create(elcol, {
      ghostClass: 'sortable-ghost',
      filter: '.drag-filter',
      setData: function(dataTransfer) {
        dataTransfer.setData('Text', '')
      },
      onEnd: evt => {
        if (evt.oldIndex !== evt.newIndex) {
          const columns = listObject.$refs.cgList.store.states.columns
          const targetCol = columns.splice(evt.oldIndex, 1)[0]
          if (evt.newIndex >= columns.length || (columns[evt.newIndex].type !== 'selection' && columns[evt.newIndex].type !== 'index')) columns.splice(evt.newIndex, 0, targetCol)
          else elcol.removeChild(evt.item)
          setTimeout(_ => {
            elcol.querySelectorAll('th').forEach(dom => {
              removeClass(dom, 'is-hidden')
            })
          }, 200)
        }
      }
    })
  },

  list_editInlineValidate(listObject,record,editInlineFields) {
    if (listObject.rulesObject) {
      let msg=""
      const rules = listObject.rulesObject.getRules(listObject,record)
      editInlineFields.some(f=>{
        const rule=rules[f]
        if (rule) {
          for (let i=0;i<rule.length;i++) {
            if (rule[i].required && !cg.hasValue(record[f])) {
              msg = rule[i].message
            }
            else if (typeof rule[i].validator == 'function') {
              rule[i].validator(rule[i],record[f],(err)=>{
                if (err) {
                  msg = err.message
                }
              })
            }
            if (msg) return true
          }
        }
      })
      if (msg) {
        Message({
          message: msg,
          type: 'error',
          duration: 5 * 1000
        })
        return false
      }
      else return true
    } else return true
  },

  // ****************   Edit Inline  ******************** //
  list_saveRowField(listObject, row, saveAll, $tr) {
    if (!listObject.editInlineFields || listObject.editInlineFields.length === 0) return
    let data = []
    const that = this
    const getRecord = function(record) {
      if (record[listObject.idField]) {
        let r={}
        let modified = false
        if (that.list_editInlineValidate(listObject, record, listObject.editInlineFields)) {
          listObject.editInlineFields.forEach(f => {
            if (record[f]!=null && record[f]!=undefined && String(record[f])!=String(record.inlineEditting[f])) {
              //if (record[f] instanceof Date) r[f] = time.toString(record[f],'YYYY-MM-DD HH:mm:ss.SSS')
              //else 
              r[f] = record[f]
              modified = true
            } else {
              // edit-inline 不允许输入空值,恢复值
            }
          })
        }
        if (modified) {
          r[listObject.idField] = record[listObject.idField]
          data.push(r)
          usedRows.push(record)
        } else {
          that.list_changeRecord(listObject.rows, record[listObject.idField], listObject.idField, record.inlineEditting) // 恢复
          listObject.totalEdittingRows --
          delete record.inlineEditting
        }
      } else {
        record = Object.assign(record, listObject.fixedQueryRecord) // fixedQueryRecord 优先
        if (that.list_editInlineValidate(listObject, record, Object.keys(record))) {
          let r={}
          Object.keys(record).forEach(f => {
            if (record[f]!=null && record[f]!=undefined && String(record[f])!=String(record.inlineEditting[f])) {
              //if (record[f] instanceof Date) r[f] = time.toString(record[f],'YYYY-MM-DD HH:mm:ss.SSS')
              //else 
              r[f] = record[f]
            } 
          })
          data.push(r)
          usedRows.push(record)
        } else {
          // for (let i=0;i<listObject.rows.length;i++) { // 新插入的字段没有树形结构
          //   if (record==listObject.rows[i]) {
          //     listObject.rows.splice(i,1)
          //     break
          //   }
          // }
        }
      }
    }
    const usedRows = []
    if (saveAll) {
      const rowsSeek=function(rows) {
        rows.forEach(r=>{
          if (r.inlineEditting) getRecord(r)
          if (r.children && r.children.length > 0) rowsSeek(r.children)
        })
      }
      rowsSeek(listObject.rows)
    } else {
      getRecord(row)   
    }
    if (data.length>0) this.list_request({ listObject, method: 'POST', silence: false, data, action: 'updateSelective' ,
      onSuccess: (res)=>{ 
        if (saveAll && listObject.parentField) listObject.doAction('refresh') 
        else {
          listObject.$emit('refreshed', listObject)
          if (res.hasOwnProperty('savedRecordIds')) { 
            const ids = res.savedRecordIds
            const currentRow = that.list_getCurrentRow(listObject)
            for (let i=0;i<ids.length;i++) {
              if (ids[i] && i<usedRows.length) { // 保存成功,设置新建记录的主键
                usedRows[i][listObject.idField] = ids[i]
                delete usedRows[i].inlineEditting
                listObject.totalEdittingRows --
                if (usedRows[i]==currentRow) listObject.$emit('rowClick',{row: currentRow})
              }
            }
          }
        }    
        listObject.$refs.cgList.doLayout()
        if (!listObject.totalEdittingRows) {
          this.list_closeEditInlineAtOnce(listObject)
        }
      }
    }) 
  },

  list_closeEditInlineAtOnce(listObject) {
    listObject.$el.removeEventListener('editInline_Add', listObject.editInlineAdd);
    const closeFunc = function(rows) {
      if (rows && rows.length > 0) {
        for (let i=0; i<rows.length; i++) {
          if (listObject.totalEdittingRows <=0 ) return
          if (rows[i].inlineEditting) {
            if (Object.keys(rows[i].inlineEditting).length>0) {
              rows.splice(i,1,rows[i].inlineEditting)
              listObject.totalEdittingRows --
            }
            else { 
              rows.splice(i,1)
              listObject.totalEdittingRows --
              i--
            }
          } else if (rows[i].children && rows[i].children.length > 0) closeFunc(rows[i].children)
        }
      }
    }
    if (listObject.rows.length > 0 && listObject.totalEdittingRows) closeFunc(listObject.rows)
    listObject.totalEdittingRows = 0
    listObject.$refs.cgList.doLayout()
  },

  list_closeEditInline(listObject,row,$tr) {
    const that=this
    if (!row && listObject.totalEdittingRows) {
      listObject.$confirm(listObject.$t('system.message.cancelModified'), listObject.$t('system.action.confirm'), {
        confirmButtonText: listObject.$t('system.action.yes'),
        cancelButtonText: listObject.$t('system.action.cancel'),
        closeOnClickModal: false,
        type: 'warning'
      }).then(_ => {
        that.list_closeEditInlineAtOnce(listObject)
        listObject.$refs.cgList.doLayout()
      }).catch(_ => {
      })
    } else if (row && row.inlineEditting && listObject.totalEdittingRows) {
      if (row[listObject.idField]) {
        this.list_changeRecord(listObject.rows, row[listObject.idField], listObject.idField, row.inlineEditting) // 恢复
        delete row.inlineEditting
      } else { // 新建的删除
        listObject.rows.splice(listObject.rows.findIndex(r=>r==row),1)
      }
      listObject.totalEdittingRows--
      listObject.$refs.cgList.doLayout()
    }
  },
  list_openEditInline(listObject,row, $tr) {
    if (row && !row.inlineEditting) {
      row.inlineEditting = Object.assign({},row)
      listObject.totalEdittingRows++
      listObject.$refs.cgList.doLayout()
      if (listObject.totalEdittingRows == 1 && listObject.allActions.indexOf('editInline_add')>=0) { // 切换为编辑模式
        listObject.$el.addEventListener('editInline_Add', listObject.editInlineAdd);
      }
      $tr && listObject.$nextTick(()=>{
        const el=$tr.querySelector(':enabled:not([tabindex="-1"])')
        el && el.focus()
        el && el.select()
      })
    }
  },

  list_editInlineAdd(listObject) {
    if (listObject.fixedQueryRecord) {
      const n = listObject.fixedQueryRecord
      let nullId = false
      Object.keys(n).length && Object.keys(n).some(k=>{
            if (!cg.hasValue(n[k]) || n[k]=='null') { nullId = true; return true }
      })
      if (nullId) return
    }
    listObject.totalEdittingRows ++
    if (listObject.totalEdittingRows == 1 && listObject.allActions.indexOf('editInline_add')>=0) { // 切换为编辑模式
      listObject.$el.addEventListener('editInline_Add', listObject.editInlineAdd);
    }
    let row = typeof listObject.newRecordForEditInline == 'function' ? listObject.newRecordForEditInline() : {}
    row.inlineEditting = {}
    listObject.rows.push(row)
    listObject.$refs.cgList.doLayout()
    listObject.$nextTick(()=>{
      const $tr=listObject.$el.querySelector('table tbody tr:last-child')
      if ($tr) {
        const el=$tr.querySelector(':enabled:not([tabindex="-1"])')
        el && el.focus()
        el && el.select()
        listObject.$emit('rowClick',{}) // 主从表清除从表
      }
    })
  },

  list_editInlineAction(listObject,action,options) {
    if (action==='editInline_add') this.list_editInlineAdd(listObject)
    else if (action==='editInline_close_all') this.list_closeEditInline(listObject)
    else if (action==='editInline_save_all') this.list_saveRowField(listObject, null, true)
    else {
      const row = options && options.row ? options.row : this.list_checkSelections(listObject, false)
      if (row) {
        const $tr = options ? options.trElement : null
        if (action==='editInline_open') this.list_openEditInline(listObject,row, $tr)
        else if (action==='editInline_close') this.list_closeEditInline(listObject,row,$tr)
        else if (action==='editInline_save') this.list_saveRowField(listObject, row, false, $tr)
      }
    }
  },

  // ****************   list event  ******************** //
  list_rowClick(listObject, { row, column, event }) {
    if (listObject.isTableMode()) {
      if (listObject.multiple) {
        if (event.shiftKey) {
          listObject.$refs.cgList.toggleRowSelection(row, true)
        } else if (event.ctrlKey) {
          listObject.$refs.cgList.toggleRowSelection(row)
        } else if (event.altKey) {
          listObject.$refs.cgList.toggleAllSelection()
        } else {
          listObject.$refs.cgList.clearSelection()
          listObject.$refs.cgList.toggleRowSelection(row, true)
        }
      } 
      else if (listObject.joinMode) listObject.$emit('closeJoinList', [row])
      else if (event.shiftKey) {
        listObject.$refs.cgList.setCurrentRow()
        listObject.$emit('rowClick',{ row : null, column, event })
        return
      }
    }
//    else if (listObject.multiple) listObject.$refs.cgList.toggleRowSelection(row)
    else if (event.shiftKey) {
      listObject.$refs.cgList.setCurrentRow()
      listObject.$emit('rowClick',{ row : null, column, event })
      return
    }
    else listObject.$refs.cgList.setCurrentRow(row)
    listObject.$emit('rowClick',{ row, column, event })
  },

  list_rowContextmenu(listObject, { row, column, event }) {
    if (listObject.isMobile() && listObject.joinMode) listObject.$emit('closeJoinList', listObject.multiple ? listObject.$refs.cgList.store.states.selection : [listObject.$refs.cgList.store.states.currentRow])
    else if (listObject.contextMenu && !listObject.joinMode && row) {
      listObject.contextMenu.row = row
      listObject.contextMenu.actions = this.list_allActions(listObject,'context',row)
      if (listObject.contextMenu.actions.length > 0) {
        listObject.contextMenu.top = event.clientY
        listObject.contextMenu.left = event.clientX
        listObject.contextMenu.visible = true
        if (event && event.target) {
          if (event.target.tagName.toLowerCase() == "tr") listObject.contextMenu.trElement = event.target
          else listObject.contextMenu.trElement = getParent(event.target,'tr')
        } else listObject.contextMenu.trElement = null
      }
    }
    event.preventDefault()
    listObject.$emit('rowContextmenu',{ row, column, event })
  },
  list_headClick(listObject, { column, event }) {
    event.preventDefault()
    if (column.type==='index') listObject.showActionView = true
    else listObject.$emit('headClick',{ column, event })
  },
  list_rowDblclick(listObject, { row, column, event }) {
    if (listObject.totalEdittingRows) return
    if (listObject.joinMode) listObject.$emit('closeJoinList', listObject.multiple ? listObject.$refs.cgList.store.states.selection : [listObject.$refs.cgList.store.states.currentRow])
    else if (typeof listObject.defaultEditMode === 'function') {
      let action = listObject.defaultEditMode(row)
      if(row.hasOwnProperty('flowAvailableActions') && row.flowAvailableActions && row.flowAvailableActions.indexOf(action) < 0)
        action = 'view'
        listObject.doAction(action , { row })
    }
    listObject.$emit('rowDblclick', { row, column, event })
  },
  list_cellClick(listObject, { row, column, cell, event }) {
    listObject.$emit('cellClick', { row, column, cell, event })
  },
  list_selectionChange(listObject, selection) { // 列表选项发生变化，selection为选择 行记录的 数组
    if (!selection) listObject.selections = ''
    else {
      if (selection instanceof Array) {
        if (selection.length === 0) this.selections = ''
        else listObject.selections = selection.reduce((v0, s) => v0 + (v0 ? ',' : '') + s[listObject.selectionKey], '')
      } else listObject.selections = selection[listObject.selectionKey] + ''
    }
    listObject.$emit('selectionChange', selection)
  },
  list_destroyScroll(listObject) {
    if (listObject.bscroller) listObject.bscroller.scroll.destroy()
  }
}
