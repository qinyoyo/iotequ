
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
         name:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('ewTimeProject.field.name'), trigger: 'blur' }
         ],
         baseValue:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('ewTimeProject.field.baseValue'), trigger: 'blur' }
         ],
         basePrice:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('ewTimeProject.field.basePrice'), trigger: 'blur' }
         ],
         bonusPoint:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('ewTimeProject.field.bonusPoint'), trigger: 'blur' },
            {
                validator: (rule, value, callback) => {
                    if (!value) callback()
                    else {
                       if (!/^(\-|\+)?\d+(\.\d+)?$/.test(value)) callback(new Error(vueObject.$t(vueObject.$te('ewTimeProject.field.bonusPointValid') ? 'ewTimeProject.field.bonusPointValid' : 'system.message.errorValue')))
                       else callback()
                    }
                },
                trigger: 'blur'
            }
         ],
      }, typeof extendRules == 'function' ? extendRules(vueObject, _=>{ return vueObject.record ? vueObject.record : this_.record} ) : extendRules)
   }
}
