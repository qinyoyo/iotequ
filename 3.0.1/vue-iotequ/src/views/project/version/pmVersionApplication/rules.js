
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
         flowState:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('pmVersionApplication.field.flowState'), trigger: 'blur' }
         ],
         flowRegisterTime:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('pmVersionApplication.field.flowRegisterTime'), trigger: 'blur' }
         ],
         project:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('pmVersionApplication.field.project'), trigger: 'blur' }
         ],
         applicationType:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('pmVersionApplication.field.applicationType'), trigger: 'blur' }
         ],
         customer:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('pmVersionApplication.field.customer'), trigger: 'blur' }
         ],
         licence:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('pmVersionApplication.field.licence'), trigger: 'blur' }
         ],
         description:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('pmVersionApplication.field.description'), trigger: 'blur' }
         ],
      }, typeof extendRules == 'function' ? extendRules(vueObject, _=>{ return vueObject.record ? vueObject.record : this_.record} ) : extendRules)
   }
}
