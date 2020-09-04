
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
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgListField.field.orderNum'), trigger: 'blur' }
         ],
         entityField:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgListField.field.entityField'), trigger: 'blur' }
         ],
         queryMode:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgListField.field.queryMode'), trigger: 'blur' }
         ],
         fix:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgListField.field.fix'), trigger: 'blur' }
         ],
         expand:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgListField.field.expand'), trigger: 'blur' }
         ],
         overflowTooltip:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgListField.field.overflowTooltip'), trigger: 'blur' }
         ],
         align:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgListField.field.align'), trigger: 'blur' }
         ],
         width:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgListField.field.width'), trigger: 'blur' }
         ],
         hidden:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgListField.field.hidden'), trigger: 'blur' }
         ],
         editInline:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgListField.field.editInline'), trigger: 'blur' }
         ],
      }, typeof extendRules == 'function' ? extendRules(vueObject, _=>{ return vueObject.record ? vueObject.record : this_.record} ) : extendRules)
   }
}
