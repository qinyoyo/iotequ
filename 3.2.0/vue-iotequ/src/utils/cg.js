import { apiUrl } from '@/utils/requestService'
import { addClass, removeClass, getStyle, isVisible } from '@/utils/dom'
import time from '@/utils/time'
import { uppercaseFirst } from '@/filters'
import { findRouteByUrl } from '@/utils/filterRoutes'
import { getQueryObject } from '@/utils'
import {localeText,setLocaleText} from '@/lang'
export const systemActionParams ={
  query: {
    isSystemAction: true,
    icon: 'el-icon-search',
    groupid: 1,
    title: 'system.action.search',
    action: 'query',
    rowProperty: 'nr',
    actionProperty: 'aj',    
    type: 'info'
  },  
  refresh: {
    isSystemAction: true,    
    icon: 'fa fa-refresh fa-fw',
    groupid: 1,
    title: 'system.action.refresh',
    action: 'refresh',
    rowProperty: 'nr',
    actionProperty: 'aj',
    type: 'info'
  },     
  add: {
    isSystemAction: true,
    icon: 'fa fa-plus fa-fw',
    groupid: 2,
    title: 'system.action.add',
    action: 'add',
    rowProperty: 'nr',
    actionProperty: 'aj',
    type: 'success'
  },
  delete: {
    isSystemAction: true,
    icon: 'fa fa-trash-o fa-fw',
    groupid: 2,
    title: 'system.action.delete',
    action: 'delete',
    rowProperty: 'sr',
    actionProperty: 'aj',
    type: 'danger'
  },
  edit: {
    isSystemAction: true,
    icon: 'fa fa-pencil-square-o  fa-fw',
    groupid: 2,
    title: 'system.action.edit',
    action: 'edit',
    rowProperty: 'sr',
    actionProperty: 'aj',
    type: 'success'
  },
  view: {
    isSystemAction: true,
    icon: 'fa fa-ellipsis-h fa-fw',
    groupid: 2,
    title: 'system.action.view',
    action: 'view',
    rowProperty: 'sr',
    actionProperty: 'aj',
    type: 'success'
  },
  batdel: {
    isSystemAction: true,
    icon: 'fa fa-trash fa-fw',
    groupid: 4,
    title: 'system.action.batchDelete',
    action: 'batdel',
    rowProperty: 'mr',
    actionProperty: 'aj',
    displayProperties: 'hm,tb',
    type: 'danger'
  },
  import: {
    isSystemAction: true,
    icon: 'fa fa-cloud-upload fa-fw',
    groupid: 4,
    title: 'system.action.import',
    action: 'import',
    rowProperty: 'nr',
    actionProperty: 'aj',
    displayProperties: 'hm,tb',
    type: 'warning'
  },
  export: {
    isSystemAction: true,
    icon: 'fa fa-cloud-download fa-fw',
    groupid: 4,
    title: 'system.action.export',
    action: 'export',
    rowProperty: 'nr',
    actionProperty: 'aj',
    displayProperties: 'hm,tb',
    type: 'warning'
  },
  localExport: {
    isSystemAction: true,
    icon: 'fa fa-floppy-o fa-fw',
    groupid: 4,
    title: 'system.action.localExport',
    action: 'export',
    rowProperty: 'nr',
    actionProperty: 'js',
    displayProperties: 'hm,tb',
    type: 'warning'
  },
  
  editInline_add: {
    isSystemAction: true,
    icon: 'fa fa-plus-square-o fa-fw',
    groupid: 5,
    title: 'system.action.editInlineAdd',
    action: 'editInline_add',
    rowProperty: 'nr',
    actionProperty: 'js',
    displayProperties: 'hm,tb',
    type: 'warning'
  },
  editInline_open: {
    isSystemAction: true,
    icon: 'fa fa-pencil fa-fw',
    groupid: 5,
    title: 'system.action.editInlineOpen',
    action: 'editInline_open',
    rowProperty: 'sr',
    actionProperty: 'js',
    displayProperties: 'hm,tb',
    type: 'warning'
  },
  editInline_close: {
    isSystemAction: true,
    icon: 'fa fa-times fa-fw',
    groupid: 5,
    title: 'system.action.editInlineClose',
    action: 'editInline_close',
    rowProperty: 'sr',
    actionProperty: 'js',
    displayProperties: 'hm,tb',
    type: 'warning'
  },
  editInline_close_all: {
    isSystemAction: true,
    icon: 'fa fa-times-circle fa-fw',
    groupid: 5,
    title: 'system.action.editInlineCloseAll',
    action: 'editInline_close_all',
    rowProperty: 'nr',
    actionProperty: 'js',
    displayProperties: 'hm,tb',
    type: 'warning'
  },  
  editInline_save: {
    isSystemAction: true,
    icon: 'fa fa-check fa-fw',
    groupid: 5,
    title: 'system.action.save',
    action: 'editInline_save',
    rowProperty: 'sr',
    actionProperty: 'js',
    displayProperties: 'hm,tb',
    type: 'warning'
  },
  editInline_save_all: {
    isSystemAction: true,
    icon: 'fa fa-floppy-o fa-fw',
    groupid: 5,
    title: 'system.action.saveAll',
    action: 'editInline_save_all',
    rowProperty: 'nr',
    actionProperty: 'js',
    displayProperties: 'hm,tb',
    type: 'warning'
  }
}
// 判断是否有权限
export function hasAuthority(auth) {
  const vue = window.$vue
  return (vue.$store.getters.permission_apis.indexOf(auth) >= 0)
}
// 判断是否可以执行某一个action ，action ： 对象
export function hasAuthorityOf(obj,baseUrl,action,record) {
  if (typeof action === 'string') action = systemActionParams[action]
  if (!action) return false
  if (obj && typeof obj.extendActionFilter==='function' && !obj.extendActionFilter(action.action, record))  return false // 调用对象是否定义了 extendActionFilter函数
  const mobile=isMobile()
  if (action.displayProperties && ( (mobile && action.displayProperties.indexOf('hm')>=0) || (!mobile && action.displayProperties.indexOf('hp')>=0)) ) return false 
  if (action.actionProperty!='aj' && action.action != 'editInline_add') return true
  if (!baseUrl) baseUrl = '/'
  else if (baseUrl.substring(baseUrl.length-1,baseUrl.length) !== '/') baseUrl = baseUrl + '/'
  const whiteList = ['/res/','/login/','/common/']
  if (whiteList.some(u=> { if (baseUrl.indexOf(u) ===0) return true })) return true
  const apis = window.$vue.$store.state.user.apis
  const a=action.action
  if (a==='localExport') return true
  else if ((a==='refresh' || a==='query' || a==='list') && apis.indexOf(baseUrl+'list') >= 0) return true
  else if ((a==='add' || a==='editInline_add') && apis.indexOf(baseUrl+'record') >= 0 && apis.indexOf(baseUrl+'default') >= 0 && apis.indexOf(baseUrl+'save') >=0 ) return true
  else if (a==='view' && apis.indexOf(baseUrl+'record') >= 0 ) return true
  else if (a==='edit' && apis.indexOf(baseUrl+'record') >= 0 && apis.indexOf(baseUrl+'save') >=0 ) return true
  else if (action.isSystemAction && apis.indexOf(baseUrl+a) >= 0) return true
  else if (action.prefix === 'flow.' && apis.indexOf(baseUrl+'f_'+a) >= 0) return true
  else if (apis.indexOf(baseUrl+'action/'+a) >= 0) return true
  else return false
}

/**
 获得可以执行的功能清单
 actionList : 功能字符列表，包括自定义action
 additional ： 自定义功能对象列表
 singleRowMode ： 单行模式
 rowCount: 总行数
 selectionCount： 选择的行数
 */
export function allActionsOf(obj, baseUrl, actionList, additional, singleRowMode, record, rowCount, selectionCount){    
  const vv = []
  if (!actionList) return vv
  const checkAction = function(action,srmode,rows,selctions) {
    if (!action) return false
    if (obj && typeof obj.disabledAction == "function" && obj.disabledAction(action)) return false
    if (srmode) return action.rowProperty.indexOf('sr')>=0
    else if (action.rowProperty.indexOf('sr')>=0) return selctions == 1
    else if (action.rowProperty.indexOf('mr')>=0) return selctions > 0
    else if (action.action=='export' || action.action=='localExport') return rowCount > 0
    else return true
  }
  const ss = actionList.split(',')
  ss.forEach(s => {
    if (s && !vv.some(a=>a.action==s || (s=='list' && a.action=='refresh'))) {
      let action = systemActionParams[s==='list' ? 'refresh' : s]
      if (checkAction(action, singleRowMode, rowCount,selectionCount)) {
        if (hasAuthorityOf(obj,baseUrl,action,record)) vv.push({...action})
      }
    }
  })
  if (additional && additional.length > 0) {
    additional.forEach(action=>{
      if (ss.indexOf(action.action)>=0 && checkAction(action, singleRowMode, rowCount,selectionCount)) {
        if (hasAuthorityOf(obj,baseUrl,action,record)) vv.push({...action})
      }
    })
  }
  if (vv.length > 0) {
    for (let i = 0; i < vv.length - 1; i++) {
      if (vv[i].groupid !== vv[i+1].groupid) vv[i].splited = true
      else vv[i].splited = false
    }
    vv[vv.length-1].splited = false
  }
  return vv
}

export function  displayTabpane(tabsRef,index,show) {
  if (!tabsRef) return
  if (tabsRef.$children && tabsRef.$children[0].$refs.tabs && tabsRef.$children[0].$refs.tabs.length > index) {
    tabsRef.$children[0].$refs.tabs[index].style.display = show ? 'inline-block' : 'none'
  }
}
export function printf(format) {
  if (typeof format !== 'string') { return null }
  var r = new RegExp(/%(\+)?([0 \,]|'(.))?(-)?([0-9]+)?(\.([0-9]+))?([%bcdfosxX])/g)
  var parts = []
  var paramIndex = 1
  var part = r.exec(format)
  while (part) {
    if ((paramIndex >= arguments.length) && (part[8] !== '%')) { return null }
    parts[parts.length] = {
      begin: part.index,
      end: part.index + part[0].length,
      sign: (part[1] === '+'),
      negative: (parseFloat(arguments[paramIndex]) < 0),
      padding: (part[2] === undefined) ? (' ') : ((part[2].substring(0, 1) === "'") ? (part[3]) : (part[2])),
      alignLeft: (part[4] === '-'),
      width: (part[5] !== undefined) ? part[5] : false,
      precision: (part[7] !== undefined) ? part[7] : false,
      type: part[8],
      data: (part[8] !== '%') ? String(arguments[paramIndex++]) : false
    }
    part = r.exec(format)
  }
  var newString = ''
  var start = 0
  for (let i = 0; i < parts.length; ++i) {
    newString += format.substring(start, parts[i].begin)
    start = parts[i].end
    var preSubstitution = ''
    switch (parts[i].type) {
      case '%':
        preSubstitution = '%'
        break
      case 'b':
        preSubstitution = Math.abs(parseInt(parts[i].data))
          .toString(2)
        break
      case 'c':
        preSubstitution = String.fromCharCode(Math
          .abs(parseInt(parts[i].data)))
        break
      case 'd':
        preSubstitution = String(Math
          .abs(parseInt(parts[i].data)))
        if (parts[i].padding==',' && preSubstitution.length>3) {
          let sz = preSubstitution.length % 3 == 0 ? 3 : preSubstitution.length % 3
          let pos = 0
          let ns = ''
          while (pos<preSubstitution.length) {
            ns += ((ns?',':'')+preSubstitution.substring(pos,pos+sz))
            pos=pos+sz
            sz = 3
          }
          preSubstitution = ns
        }
        break
      case 'f':
        preSubstitution = (parts[i].precision === false) ? (String((Math
          .abs(parseFloat(parts[i].data))))) : (Math
          .abs(parseFloat(parts[i].data))
          .toFixed(parts[i].precision))
        break
      case 'o':
        preSubstitution = Math.abs(parseInt(parts[i].data))
          .toString(8)
        break
      case 's':
        preSubstitution = parts[i].data
          .substring(0, parts[i].precision ? parts[i].precision : parts[i].data.length)
        break
      case 'x':
        preSubstitution = Math.abs(parseInt(parts[i].data))
          .toString(16).toLowerCase()
        break
      case 'X':
        preSubstitution = Math.abs(parseInt(parts[i].data))
          .toString(16).toUpperCase()
        break
      default:
        return newString
    }
    if (parts[i].type === '%') {
      newString += preSubstitution
      continue
    }
    if (parts[i].width !== false) {
      if (parts[i].width > preSubstitution.length) {
        var origLength = preSubstitution.length
        for (var j = 0; j < parts[i].width - origLength; ++j) {
          preSubstitution = (parts[i].alignLeft === true) ? (preSubstitution + parts[i].padding) : (parts[i].padding + preSubstitution)
        }
      }
    }
    if (parts[i].type === 'b' || parts[i].type === 'd' || parts[i].type === 'o' || parts[i].type === 'f' || parts[i].type === 'x' || parts[i].type === 'X') {
      if (parts[i].negative === true) {
        preSubstitution = '-' + preSubstitution
      } else if (parts[i].sign === true) {
        preSubstitution = '+' + preSubstitution
      }
    }
    newString += preSubstitution
  }
  newString += format.substring(start, format.length)
  return newString
}
// 连接多个字段为一个url地址
export function concatUrl() {
  var numargs = arguments.length
  if (numargs === 0) return null
  var r = ''
  for (var i = 0; i < numargs; i++) {
    var arg = arguments[i]
    if (arg) {
      arg = arg + ''
      if (r.substring(r.length - 1) === '/' && arg.substring(0, 1) === '/') r = r + arg.substring(1)
      else if (r.substring(r.length - 1) !== '/' && arg.substring(0, 1) !== '/') r = r + '/' + arg
      else r = r + arg
    }
  }
  return r
}
export function isWechat() {
  var ua = navigator.userAgent.toLowerCase()
  return ua.indexOf('micromessenger') >= 0
}
export function isMobile() {
  var ua = navigator.userAgent.toLowerCase()
  var agents = ["android", "iphone","symbianos", "phone","mobile"]
  var flag = true;
  if (agents.some(a=>{ if (ua.indexOf(a) >= 0) return true })) return true
  else return false
//    return window.$vue.$store.state.app.device === 'mobile'
}
export function isPhone() {
  return isMobile()
}
export function isLandscape() {
  if (window.$vue.$store.state.app.device === 'mobile' && (window.orientation == 90 || window.orientation == -90))  return true
  else return false
}
export function goBack() {
  const vue=window.$vue
  const cached = vue.$store.state.tagsView.cachedViews
  const visted = vue.$store.state.tagsView.visitedViews
  const view = vue.$route
  const index = cached.indexOf(view.name)
  if (index>=0) {
    const name=cached[index]
    vue.$store.dispatch('tagsView/delCachedView', { name })
    visted.some(v=>{ 
      if (v.name === name) { 
        vue.$store.dispatch('tagsView/delVisitedView', v) 
        return true
      }
    })
  }
  if (cached.length > 0) {
    const name = cached[cached.length -1]
    const found = visted.some(v=>{
      if (v.name === name) {
        vue.$router.push(v)
        return true
      }
    })
    if (!found) vue.$router.push('/')
  } else vue.$router.push('/')
}

export function visible(elm) {
  return elm && (elm.offsetWidth + elm.offsetHeight) > 0
}


// 行数据转换为树形结构，rows记录数组 id字段 parent 字段
export function rows2TreeNode(rows, id, parent) {
  const setChildren = (row, rows, id, parent) => {
    rows.forEach((r) => {
      if (r[parent] === row[id]) {
        if (row.children) row.children.push(r)
        else {
          const children = []
          children.push(r)
          row.children = children
        }
      }
    })
    if (row.children && row.children.length > 0) {
      row.children.forEach((c) => {
        setChildren(c, rows, id, parent)
      })
    }
  }
  if (!rows || rows.length === 0 || !id || !parent) return rows
  const ret = []
  rows.forEach((r) => {
    if (!r[parent]) ret.push(r)
    else {
      let hasParent = false
      rows.some((rr) => {
        if (rr[id] === r[parent]) {
          hasParent = true
          return true
        }
      })
      if (!hasParent) ret.push(r)
    }
  })
  ret.forEach((r) => {
    setChildren(r, rows, id, parent)
  })
  return ret
}
// 树形结构转换为行数据，rows记录数组 id字段 parent 字段
export function treeNode2Rows(nodes) {
  if (!nodes || nodes.length === 0) return []
  const ret = []
  nodes.forEach((node) => {
    let children = null
    if (node.children !== undefined) {
      children = treeNode2Rows(node.children)
      delete node.children
    }
    ret.push(node)
    if (children) children.forEach(r=>ret.push(r))
  })
  return ret
}

export function appendRow(row, rows, id, parent,notAppendToTail) {
  if ((!id || !parent) && !notAppendToTail) {
    rows.push(row)
    return true
  }
  else {
    let appended = false
    rows.som((r) => {
      if (r[id] === row[parent]) {
        if (r.children) r.children.push(row)
        else {
          const children = []
          children.push(row)
          r.children = children
        }
        appended = true
        return true
      } else if (r.children && r.children.length > 0) appended = appendRow(row, r.children,id,parent,false)
      if (appended) return true
    })
    if (!appended && !notAppendToTail) rows.push(row)
    return appended
  }
}
export function hasValue(v) {
  if (v == null || typeof v == 'undefined' || Number.isNaN(v)) return false
  if (typeof v=='number') return true
  else if (v instanceof Array) {
    if (v.length) return true
    else return false
  }
  else if (typeof v=='boolean') return true
  else if (Object.prototype.toString.call(v) === '[object Object]') {
    return Object.keys(v).length && Object.keys(v).some(a=>{
      if (hasValue(v[a])) return true
    })
  }
  else if (v) return true
  else return false
}
function addBlobRecord2FormData(record,formData,blobRecord) {
  if (!blobRecord) return formData
  let files=0
  Object.keys(blobRecord).forEach(field => {
    if (blobRecord[field].multiple) {
      record[field] = blobRecord[field].name ? blobRecord[field].name.join(',') : null
      formData.append(field,record[field])
      if (blobRecord[field].blob) {
        let index = 0
        for (let i = 0; i < blobRecord[field].blob.length; i++) {
          if (blobRecord[field].blob[i] && blobRecord[field].name[i]) {
            formData.append('filepart_' + field + (index !== 0 ? ('_'+index) : ''), blobRecord[field].blob[i], blobRecord[field].name[i])
            files++
            index++
          }
        }
      }
    } else {
      record[field] = blobRecord[field].name
      formData.append(field,record[field])
      if (blobRecord[field].blob && blobRecord[field].name) {
        formData.append('filepart_' + field, blobRecord[field].blob, blobRecord[field].name)
        files++
      }
    }
  })
  formData.append('total_filepart', files)
  return formData
}
export function record2FormData(record,blobRecord) {
    const formData = new FormData()
    if (!record) return addBlobRecord2FormData(record,formData,blobRecord)
    let files = 0
    Object.keys(record).forEach(field => {
      const v = record[field]
      if (!blobRecord || !blobRecord[field]) {
        if (field == 'children' && v instanceof Array) return
        if (hasValue(v)) {
          if (Object.prototype.toString.call(v) === '[object Date]') formData.append(field, time.toString(v))
          else formData.append(field, v)
        }
      }
    })
    return addBlobRecord2FormData(record,formData,blobRecord)
}

export function record2Params(record) {
  const params = {}
  if (!record) return params
  Object.keys(record).forEach(field => {
    const v = record[field]
    if (field == 'children' && v instanceof Array) return
    if (hasValue(v)) {
      if (Object.prototype.toString.call(v) === '[object Date]') params[field]=time.toString(v)
      else if (v instanceof Array){
        const vv = []
        v.forEach(val=>{
          if (hasValue(val)) {
            if (Object.prototype.toString.call(val) === '[object Date]') vv.push(time.toString(val))
            else vv.push(val+'')
          }
        })
        if (vv.length>0) params[field]=vv.join(',')
      }
      else params[field]=v
    }
  })
  return params
}
export function getDictionary(valueString,textString) {
  const newDict = []
  if (typeof valueString == 'string') {
    const vv = valueString.split(',')
    const tt = (typeof textString == 'string' ? textString.split(',') : vv)
    for (let i=0;i<vv.length;i++) {
      newDict.push({ text: i<tt.length ? tt[i].local() : vv[i], value: vv[i] })
    }
  } 
  return newDict
}
export function rows2Dictionary(rows,textField,valueField,idField,parentField,parentText) {
  const dict=[]
  rows.forEach(r=>{
    let d = {
      text: localeText(r[textField]+''),
      value: r[valueField]+''
    }
    if (idField && parentField) {
      d.fullname = (parentText ? parentText + '-' : '') + d.text
      if (r.children && r.children.length > 0) {
        d.nodes=rows2Dictionary(r.children,textField,valueField,idField,parentField,d.text)
      }
    }
    dict.push(d)
  })
  return dict
}
// 设置join字段值，内部调用 field=本表关联字段 join=join的相关参数 rows=join列表选择的记录数组
export function setJoinValues(record, field, join, rows) {
  if (!field || !join) return
  if (rows && rows.length > 0) {
    let vv = []
    rows.forEach(r => { if (r) vv.push(r[join.valueField]) })
    record[field] = vv.join(',')
    join.fields.split(',').forEach(f => {
      const exps = f.split('=')
      vv = []
      rows.forEach(r => { if (r) vv.push(localeText(r[exps[1]])) })
      record[exps[0]] = vv.join(',')
    })
  } else {
    record[field] = null
    join.fields.split(',').forEach(f => {
      const exps = f.split('=')
      record[exps[0]] = null
    })
  }
}

export function clearJoinValues(obj,ref) {
  if (obj && obj.$refs[ref]) {
    obj.$refs[ref].$refs.cgList.clearSelection()
  }
}
// 从字典检索值，支持属性字典(兼容2.2.2，子序列字段名为nodes) value值(单个值或逗号序列) dict字典
// fullname是否显示全名 stringOutput是否已逗号序列方式输出

export function dictValue(value, dict, fullname, stringOutput) {
  const vue = window.$vue
  if ((!value && value!==0)) return stringOutput ? '' : []
  const vv = (typeof value === 'string' ? value.split(',') : [value + ''])
  if (!dict) return stringOutput ? vv.join(',') : vv
  var tt = []
  const fullNameLocale = function(f) {
    let ff = f.split('-')
    for (let i=0;i<ff.length;i++) {
      ff[i] = localeText(ff[i]).local()
    }
    return ff.join('-')
  }
  dict.forEach(d => {
    if (vv.indexOf(d.value + '') >= 0) {
      const txt = fullname && d.fullname ? fullNameLocale(d.fullname) : localeText(d.text).local()
      tt.push(txt?txt:'')
    }
    if (d.nodes && d.nodes.length > 0) {
      const cc = dictValue(value, d.nodes, fullname, false)
      if (cc && cc.length > 0) cc.forEach(c => { tt.push(c) })
    }
  })
  return stringOutput ? tt.join(',') : tt
}
// 一个值在字典里的位置
export function dictIndex(value, dict) {
  if (!value || !dict || dict.length === 0) return -1
  let ret = -dict.length
  let index = 0
  dict.some(d => {
    if (value + '' === d.value + '' || value === d.text || value === d.fullname) {
      ret = index
      return true
    }
    index++
    if (d.nodes && d.nodes.length > 0) {
      const c = dictIndex(value, d.nodes)
      if (c >= 0) {
        ret = index + c
        return true
      } else {
        ret = ret + c
        index = index - c
      }
    }
  })
  return ret
}

export function  validDictValue(value,dict,multiple) {
  if (hasValue(value)) {
    let vv = multiple ? (''+value).split(',') : [value+'']
    for (let i=0;;i++) {
      if (i>=vv.length) break
      if (dictIndex(vv[i],dict) < 0) {
        vv.splice(i)
        i--;
      }
    }
    if (vv.length>0) {
      if (multiple) return vv.join(',')
      else return vv[0]
    } else return null
  } else return null
}
// 随机颜色
export function randomColor(index) {
  const color = ['#d9ecff', '#e1f3d8', '#e9e9eb', '#fdf6ec', '#fde2e2', '#909399']
  if (index < 0) index = 5
  else index = index % 5
  return color[index]
}
// 从记录表里检索一个记录 value 键值  records记录表  idFieldName键名 childrenFieldName子序列字段名
export function findRecord(value, records, idFieldName, childrenFieldName) {
  if (!value || !idFieldName || !records || records.length === 0) return null
  if (!childrenFieldName) childrenFieldName = 'children'
  let ret = null
  records.some(r => {
    if (r[idFieldName] + '' === value + '') ret = r
    else if (r[childrenFieldName] && r[childrenFieldName].length > 0) ret = findRecord(value, r[childrenFieldName], idFieldName, childrenFieldName)
    if (ret) return true
  })
  return ret
}

// 在记录表里替换一个记录 row用来替换的新记录  records记录表  idFieldName键名 childrenFieldName子序列字段名
export function replaceRecord(row, records, idFieldName, childrenFieldName) {
  if (!row || !idFieldName || !records || records.length === 0) return null
  if (!childrenFieldName) childrenFieldName = 'children'
  let found = null
  for (let i = 0; i < records.length; i++)
  {
    if (records[i][idFieldName] === row[idFieldName]) {
      records[i] = row
      found = true
      break
    } else if (records[i][childrenFieldName] && records[i][childrenFieldName].length > 0) found = replaceRecord(row, records[i][childrenFieldName], idFieldName, childrenFieldName)
    if (found) break
  }
  return found
}

// 容器高度
export function containerHeight(noTitle) {
  const mobile = isMobile()
  const ch = document.body.clientHeight
  const nh = mobile?0:50 //!mobile && document.querySelector('.navbar') ? 50 : 0
  const th = mobile?0:34 //!mobile && document.querySelector('.tags-view-container') ? 34 : 0
  const fh = mobile?0:48 //!mobile && document.querySelector('.footer') ? 48 : 0
  const titleH = noTitle ? 0 : ( mobile ? 50 : 60)
  return ch - nh - th - fh - titleH
}

export function joinHeight() {
  const mobile = isMobile()
  const ch = document.body.clientHeight
  if (isLandscape()) return ch - 50
  else if (mobile) return ch/2-50
  else return 500
}
// 将blob保存为文件
export function saveFile(blob, fileName) {
  var type = blob.type
  var force_saveable_type = 'application/octet-stream'
  if (type && type !== force_saveable_type) {
    var slice = blob.slice || blob.webkitSlice || blob.mozSlice
    blob = slice.call(blob, 0, blob.size, force_saveable_type)
  }
  if (window.navigator.msSaveOrOpenBlob) {//IE10+方法
     const result = window.navigator.msSaveOrOpenBlob(blob, fileName)
  } else {
    var url = URL.createObjectURL(blob)
    var save_link = document.createElementNS('http://www.w3.org/1999/xhtml', 'a')
    save_link.href = url
    save_link.download = fileName
    save_link.click()
    URL.revokeObjectURL(url)
  }
}

export function now() {
  return time.toString(new Date())
}

export function focusField(e) {
  if (!e) return
  const el = e.querySelectorAll('.cg-auto-focus a,.cg-auto-focus input,.cg-auto-focus button')  
  if (el) {
    el.forEach(dom=>{
      if (isVisible(dom)) 
         dom.focus()
    })
  }
}

const  autoFocus = (that) => {
  that && that.$el && setTimeout(_=>{
    focusField(that.$el)
  },200)      
}

// 快捷时间选择
export function datePickerOptions() {
  const vue = window.$vue
  return {
    shortcuts: [
      {
        text: vue.$t('system.time.today'),
        range: 'today',
        onClick(picker) {
          picker.$emit('pick', time.timeRange('today'))
        }
      },
      {
        text: vue.$t('system.time.yesterday'),
        range: 'yesterday',
        onClick(picker) {
          picker.$emit('pick', time.timeRange('yesterday'))
        }
      },
      {
        text: vue.$t('system.time.this') + ' ' + vue.$t('system.time.week'),
        range: 'this week',
        onClick(picker) {
          picker.$emit('pick', time.timeRange('this week'))
        }
      },
      {
        text: vue.$t('system.time.this') + ' ' + vue.$t('system.time.month'),
        range: 'this month',
        onClick(picker) {
          picker.$emit('pick', time.timeRange('this month'))
        }
      },
      {
        text: vue.$t('system.time.this') + ' ' + vue.$t('system.time.year'),
        range: 'this year',
        onClick(picker) {
          picker.$emit('pick', time.timeRange('this year'))
        }
      },
      {
        text: vue.$t('system.time.prev') + ' ' + vue.$t('system.time.week'),
        range:'prev week',
        onClick(picker) {
          picker.$emit('pick', time.timeRange('prev week'))
        }
      },
      {
        text: vue.$t('system.time.prev') + ' ' + vue.$t('system.time.month'),
        range: 'prev month',
        onClick(picker) {
          picker.$emit('pick', time.timeRange('prev month'))
        }
      },
      {
        text: vue.$t('system.time.prev') + ' ' + vue.$t('system.time.year'),
        range: 'prev year',
        onClick(picker) {
          picker.$emit('pick', time.timeRange('prev year'))
        }
      }
    ]
  }
}

// 获得驼峰字符串
export function camelString(s) {
  if (!s || !s.trim()) return ''
  s = s.trim()
  const ss = s.toLowerCase().replace(/-\/\\\s\./g,'_').split('_')
  let r = ''
  ss.forEach(str => {
    str = str.trim()
    if (str) {
      if (!r) r = str
      else r = r + str.substring(0,1).toUpperCase() + str.substring(1)
    }
  })
  return r
}
export function equals(a, b, fields) {
  if (typeof a !== typeof b) return false
  else if (a === b) return true
  else if ((!a && b) || (a && !b)) return false
  else if (!a && !b) return true
  if (fields) {
    const ff = typeof fields == 'string' ? fields.split(',') : fields
    if (ff.some(f=>{
      if (!equals(a[f], b[f])) return true
    })) return false
    return true
  }
  else if ((a instanceof Array && !(b instanceof Array)) || (b instanceof Array && !(a instanceof Array))) return false
  else if (a instanceof Array && b instanceof Array) {
    if (a.length !== b.length) return false
    for (let i = 0; i<a.length; i++) {
      if (!equals(a[i], b[i])) return false
    }
    return true
  }
  else if (typeof a === 'object') {
    let equal = true
    if (equals(Object.keys(a),Object.keys(b))) {
      Object.keys(a).some(k=>{
        if (!equals(a[k],b[k])) {
          equal = false
          return true
        }
      })
      return equal
    } else return false
  } else return false
}

export function getTextWidth(htmlText,fontSize) {
  const html = document.createElement('span')
  html.innerHTML = htmlText
  html.className = 't-t-t-auto-width el-table'
  if (fontSize) html.style = 'font-size: '+fontSize + (typeof fontSize === 'number' ? 'px' : '')
  document.querySelector('body').appendChild(html)
  const width = document.querySelector('.t-t-t-auto-width').offsetWidth
  document.querySelector('body').removeChild(html)
  return width
}

export function jump2Url(url, params, $router) {
  if (!$router) $router=window.$vue.$router
  if (isMobile()) {
    $router.push({
      path: url,
      query: params
    })
  } else {
    const route = findRouteByUrl(url,$router)
    const routeParams = Object.assign({},params, getQueryObject(url))
    if (route && route.meta && route.meta.dialog) {
      window.$vue.$dialog(route.components,{routeParams})
    } else $router.push({
      //path: url,
      //query: params
      name: route.name,
      params : /^.*?List$/.test(route.name) ? {
          fixedQueryRecord : routeParams
        } : {
          routeParams
        }
    })
  }
}

export function displayTabPane(tabsObject, paneIndex, show) {
  if (tabsObject && tabsObject.$children && tabsObject.$children.length>0 && tabsObject.$children[0].$refs && 
      tabsObject.$children[0].$refs.tabs && tabsObject.$children[0].$refs.tabs.length > paneIndex) {
    tabsObject.$children[0].$refs.tabs[paneIndex].style.display = (show ? 'inline-block' : 'none')
  }  
}
export function chineseSort(a,b) {
  if (typeof a === 'string') return a.toLocaleLowerCase().localeCompare(b.toLocaleLowerCase(), 'zh-CN')
  else if (a<b) return -1
  else if (a==b) return 0
  else if (a>b) return 1
}
export function toBool(a) {
  return a?true:false
}
export default {
  dateAdd: time.dateAdd,
  timeRange: time.timeRange,
  timeStartOf: time.startOf,
  timeEndOf: time.endOf,
  toDate:time.toDate,
  hasValue,
  addClass,
  removeClass,
  getStyle,
  displayTabpane,
  printf,
  concatUrl,
  apiUrl,
  isWechat,
  isPhone,
  isMobile,
  isLandscape,
  hasAuthority,
  allActionsOf,
  hasAuthorityOf,
  goBack,
  visible,
  record2Params,
  record2FormData,
  getDictionary,
  rows2TreeNode,
  treeNode2Rows,
  appendRow,
  setJoinValues,
  clearJoinValues,
  dictValue,
  dictIndex,
  validDictValue,
  randomColor,
  findRecord,
  replaceRecord,
  containerHeight,
  saveFile,
  now,
  datePickerOptions,
  camelString,
  uppercaseFirst,
  equals,
  getTextWidth,
  joinHeight,
  focusField,
  autoFocus,
  jump2Url,
  displayTabPane,
  localeText,
  setLocaleText,
  chineseSort,
  toBool
}
