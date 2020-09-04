
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
         orgCode:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('adAdjust.field.orgCode'), trigger: 'blur' }
         ],
         state:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('adAdjust.field.state'), trigger: 'blur' }
         ],
         registerTime:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('adAdjust.field.registerTime'), trigger: 'blur' }
         ],
         startTime:[
            {
                validator: (rule, value, callback) => {
                  if (!value) callback()
                  else {
                    const checked = (vueObject.validCheckDate(value))
                    if (typeof checked === 'boolean') {
                        if (checked) callback()
                        else callback(new Error(vueObject.$t(vueObject.$te('adAdjust.field.startTimeValid') ? 'adAdjust.field.startTimeValid' : 'system.message.errorValue')))
                    } else if (typeof checked == 'string' && checked) callback(new Error(checked))
                    else callback()
                  }
                },
                trigger: 'blur'
            }
         ],
         endTime:[
            {
                validator: (rule, value, callback) => {
                  if (!value) callback()
                  else {
                    const checked = (vueObject.validCheckDate(value))
                    if (typeof checked === 'boolean') {
                        if (checked) callback()
                        else callback(new Error(vueObject.$t(vueObject.$te('adAdjust.field.endTimeValid') ? 'adAdjust.field.endTimeValid' : 'system.message.errorValue')))
                    } else if (typeof checked == 'string' && checked) callback(new Error(checked))
                    else callback()
                  }
                },
                trigger: 'blur'
            }
         ],
         description:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('adAdjust.field.description'), trigger: 'blur' }
         ],
      }, typeof extendRules == 'function' ? extendRules(vueObject, _=>{ return vueObject.record ? vueObject.record : this_.record} ) : extendRules)
   }
}
