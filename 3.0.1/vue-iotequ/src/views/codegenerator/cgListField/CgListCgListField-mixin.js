import cgList from '@/utils/cgList'
import { transfer } from '@/views/common-views/extend-views/CgTransferDialog'
import { request } from '@/utils/request'
export default {
  props: {
    tableId: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      allFieldsInTable: [],
      super: {}
    }
  },
  watch: {
    tableId: {
      handler(n,o) {
        if (n) new Promise((resolve, reject) => {
          const req = {
            url: '/codegenerator/cgTable/action/fields',
            method: 'get',
            params: {
              tableId: n
            }
          }
          request(req, true).then(res => {
            if (res && res.hasOwnProperty('success') && res.success) {
              this.allFieldsInTable = []
              if (res.data) {
                res.data.forEach(r => {
                  const txt = (r.id.indexOf('join:')===0 || r.id.indexOf('list:')===0 ? r.id.substring(0,5) : '')
                    + (this.$te(r.title) ? this.$t(r.title) : r.title)
                  this.allFieldsInTable.push({ key: r.entityName, label: txt })
                })
              }
            }
            resolve(res)
          }).catch(error => {
            reject(error)
          })
        })
      },
      immediate: true
    }
  },
  methods: {
    useMixinMethodsFirst() {
      return true
    },
    newRecord(entityName, orderNum) {
      if (this.fixedQueryRecord.listId) return {
        listId: this.fixedQueryRecord.listId,
        orderNum,
        entityField: entityName,
        queryMode: 0,
        fix: false,
        expand: false,
        overflowTooltip: true,
        align: 'left',
        width: 0,
        hidden: false,
        editInline: false
      } 
      else if (this.fixedQueryRecord.formId) return {
        formId: this.fixedQueryRecord.formId,
        orderNum,
        entityField: entityName,
        width: 12,
        readonly: false,
        mustInput: false,
        validateAsTitle: false,
        hidden: false
      } 
      else return {}
    },
    doAction(action,options) {
      this.showActionView = false
      const that = this
      if (action === 'select') {
        const selected = []
        that.rows.forEach(r => {
          selected.push(r.entityField)
        })
        if (that.allFieldsInTable.length > 0) {
          transfer({
            title: that.title,
            data: that.allFieldsInTable,
            selected,
            onConfirm: function(selected) {
              const fields = Object.assign(selected)
              const fieldsNeedRemove = []
              that.rows.forEach(r => {
                const index = fields.indexOf(r.entityField)
                if (index < 0) fieldsNeedRemove.push(r.id)
                else fields.splice(index, 1)
              })
              if (fieldsNeedRemove.length > 0) {
                cgList.list_request({
                  listObject: that, method: 'delete', silence: false,
                  params: { ids: fieldsNeedRemove.join(',') },
                  action: 'batdel'
                })
              }
              let orderNum = 300
              fields.forEach(f => {
                cgList.list_request({
                  listObject: that, method: 'post', silence: true, action: 'save',
                  data: that.newRecord(f, orderNum)
                })
                orderNum = orderNum + 10
              })
              if (fieldsNeedRemove.length > 0 || fields.length > 0) setTimeout(_ => { cgList.list_getDataFromServer(that) }, 1000)
            }
          })
        }
      } else that.super_doAction(action,options)
    }
  }
}
