const dyncHtml = '<form  id="database_sync_dialog_form">' +
  '<div class="input-group-multiple" style="width:100%">' +
  '  <div class="checkbox">' +
  '    <input type="checkbox" name="options" id="options0" value="0">' +
  '    <label for="options0">自动创建数据库表</label>' +
  '  </div>' +
  '  <div class="checkbox">' +
  '    <input type="checkbox" name="options" id="options1" value="1">' +
  '    <label for="options1">删除表单中数据库不存在的字段</label>' +
  '  </div>' +
  '  <div class="checkbox">' +
  '    <input type="checkbox" name="options" id="options2" value="2">' +
  '   <label for="options2">删除表单未定义的数据库字段</label>' +
  '  </div>' +
  '  <div class="checkbox">' +
  '    <input type="checkbox" name="options" id="options3" value="3">' +
  '    <label for="options3">冲突以表单定义为准</label>' +
  '  </div>' +
  '  <div class="checkbox">' +
  '    <input type="checkbox" name="options" id="options3" value="4">' +
  '    <label for="options3">数据库属性以数据库的定义为准</label>' +
  '  </div>' +
  '</div>' +
  '</form>'
 
const vue = window.$vue
import { request } from '@/utils/request'
import { Message } from 'element-ui'
export default {
  methods: {
    getActionParams(action, options, id) {
      const that=this
      if (action === 'sync') {
        vue.$alert(dyncHtml.replace('TABLE_ID', id), '同步参数', {
          dangerouslyUseHTMLString: true,
          showCancelButton: true
        }).then(_ => {
          const form = document.getElementById('database_sync_dialog_form')
          const opts = []
          form.elements.forEach(e => {
            if (e.type === 'checkbox') {
              if (e.checked) opts.push(e.value)
            }
          })
          const params = { tableIds: id, options: opts.join(',') }
          request({
            url: that.baseUrl+'/action/sync',
            method: 'post',
            params
          })
        })
      } else if (action === 'create') {
        request({
          url: that.baseUrl+'/record',
          method: 'get',
          params : {
            needLoadDictionary: true,
            loadDictionaryOnly: true,
            requestDynaFields: 'notUsedTables'
          }
        }).then((res)=>{
          if (res && res.hasOwnProperty('success') && res.success && res.dictionary && res.dictionary.dictNotUsedTables && res.dictionary.dictNotUsedTables.length>0) {
            let html =   '<form  id="database_auto_create_dialog_form">' +
            '<div class="input-group-multiple" style="width:100%">'
            for (let i=0;i<res.dictionary.dictNotUsedTables.length;i++) {
              const map = res.dictionary.dictNotUsedTables[i]
              html = html + 
                 '  <div class="checkbox">' +
                 '    <input type="checkbox" name="options" id="options'+i+'" value="'+map.value+'">' +
                 '    <label for="options'+i+'">'+map.value+' '+map.text+'</label>' +
                 '  </div>'
            }
            html = html +  '</div>' + '</form>'

            vue.$alert(html, '选择可导入的表', {
              dangerouslyUseHTMLString: true,
              showCancelButton: true
            }).then(_ => {
              const form = document.getElementById('database_auto_create_dialog_form')
              const opts = []
              form.elements.forEach(e => {
                if (e.type === 'checkbox') {
                  if (e.checked) opts.push(e.value)
                }
              })
              request({
                url: that.baseUrl+'/action/create',
                method: 'post',
                params: { tableNames : opts.join(',') }
              }).then((res)=>{
                res && res.success && that.doAction('refresh')
              })
            })
          }
          else Message({
            showClose: true,
            type: 'warn',
            duration: 3000,
            message: "没有可自动导入的数据库表"
          })
        })
      }
      else return {}
    },
    doAddtionalAction: (action, options, params) => {
    }
  }
}
