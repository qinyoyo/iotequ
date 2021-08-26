
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
         realName:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('devPeople.field.realName'), trigger: 'blur' },
            {
                validator: (rule, value, callback) => {
                    if (!value) callback()
                    else {
                       if (!/^(\S.*?\S|\S)$/.test(value)) callback(new Error(vueObject.$t(vueObject.$te('devPeople.field.realNameValid') ? 'devPeople.field.realNameValid' : 'system.message.errorValue')))
                       else callback()
                    }
                },
                trigger: 'blur'
            }
         ],
         orgCode:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('devPeople.field.orgCode'), trigger: 'blur' }
         ],
         cardNo:[
            {
                validator: (rule, value, callback) => {
                    if (!value) callback()
                    else {
                       if (!/^[0-9A-Fa-f]*$/.test(value)) callback(new Error(vueObject.$t(vueObject.$te('devPeople.field.cardNoValid') ? 'devPeople.field.cardNoValid' : 'system.message.errorValue')))
                       else callback()
                    }
                },
                trigger: 'blur'
            }
         ],
         idType:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('devPeople.field.idType'), trigger: 'blur' }
         ],
         idNumber:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('devPeople.field.idNumber'), trigger: 'blur' }
         ],
         userType:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('devPeople.field.userType'), trigger: 'blur' }
         ],
         registerType:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('devPeople.field.registerType'), trigger: 'blur' }
         ],
         validDate:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('devPeople.field.validDate'), trigger: 'blur' }
         ],
         expiredDate:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('devPeople.field.expiredDate'), trigger: 'blur' }
         ],
      }, typeof extendRules == 'function' ? extendRules(vueObject, _=>{ return vueObject.record ? vueObject.record : this_.record} ) : extendRules)
   }
}
