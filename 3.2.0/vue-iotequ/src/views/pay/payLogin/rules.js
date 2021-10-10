
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
         shopId:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('payLogin.field.shopId'), trigger: 'blur' }
         ],
         batchNo:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('payLogin.field.batchNo'), trigger: 'blur' }
         ],
         loginTime:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('payLogin.field.loginTime'), trigger: 'blur' }
         ],
         appVersion:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('payLogin.field.appVersion'), trigger: 'blur' }
         ],
      }, typeof extendRules == 'function' ? extendRules(vueObject, _=>{ return vueObject.record ? vueObject.record : this_.record} ) : extendRules)
   }
}
