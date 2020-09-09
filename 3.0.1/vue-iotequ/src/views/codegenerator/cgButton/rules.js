
let extendRules = null
const mixinContext = require.context('.', false, /rules-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { extendRules = mixinContext(key).default })
export default  {
   object: null,
   record: null,
   init: function(obj, rec) {
      this.object=obj
      if (rec) this.record=rec
      else if (this.object) this.record = this.object.record
   },
   getRules: function(obj, rec) {
      if (obj) this.init(obj,rec)
      else if (rec) this.record=rec
      const vueObject=this.object
      const this_ = this
      const getRecord = function() {
         if (vueObject.record) return vueObject.record
         else if (this_.record) return this_.record
         else return {}
      }
      return Object.assign({
         orderNum:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgButton.field.orderNum'), trigger: 'blur' }
         ],
         action:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgButton.field.action'), trigger: 'blur' },
            {
                validator: (rule, value, callback) => {
                    if (!value) callback()
                    else {
                       if (!/^[a-z][a-z0-9]+([A-Z][a-z0-9]+)*$/.test(value)) callback(new Error(vueObject.$t(vueObject.$te('cgButton.field.actionValid') ? 'cgButton.field.actionValid' : 'system.message.errorValue')))
                       else callback()
                    }
                },
                trigger: 'blur'
            }
         ],
         title:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgButton.field.title'), trigger: 'blur' }
         ],
         icon:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgButton.field.icon'), trigger: 'blur' }
         ],
         appendClass:[
            {
                validator: (rule, value, callback) => {
                    if (!value) callback()
                    else {
                       if (!/^[a-zA-Z][\.a-zA-Z0-9_]*\(.*\)|{\s*\w+\s*:\s*('.*'|\[.*\]|[^\,\'\[\]]+)\s*(,\s*\w+\s*:\s*('.*'|\[.*\]|[^\,\'\[\]]+)\s*)*}$/.test(value)) callback(new Error(vueObject.$t(vueObject.$te('cgButton.field.appendClassValid') ? 'cgButton.field.appendClassValid' : 'system.message.errorValue')))
                       else callback()
                    }
                },
                trigger: 'blur'
            }
         ],
         actionProperty:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgButton.field.actionProperty'), trigger: 'blur' }
         ],
         rowProperty:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgButton.field.rowProperty'), trigger: 'blur' }
         ],
         isRefreshList:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgButton.field.isRefreshList'), trigger: 'blur' }
         ],
      }, typeof extendRules == 'function' ? extendRules(vueObject, _=>{ return vueObject.record ? vueObject.record : this_.record} ) : extendRules)
   }
}
