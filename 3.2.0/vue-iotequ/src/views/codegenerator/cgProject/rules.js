
import { invalidMessage } from '@/utils/validate'
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
         code:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgProject.field.code'), trigger: 'blur' },
            {
                validator: (rule, value, callback) => {
                    if (!/^[a-z]{2,3}$/.test(value)) callback(new Error(vueObject.$t(vueObject.$te('cgProject.field.codeValid') ? 'cgProject.field.codeValid' : 'system.message.errorValue')))
                    else callback()
                },
                trigger: 'blur'
            }
         ],
         groupId:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgProject.field.groupId'), trigger: 'blur' },
            {
                validator: (rule, value, callback) => {
                    if (!/^[a-z]+(\.[a-z]+)*$/.test(value)) callback(new Error(vueObject.$t(vueObject.$te('cgProject.field.groupIdValid') ? 'cgProject.field.groupIdValid' : 'system.message.errorValue')))
                    else callback()
                },
                trigger: 'blur'
            }
         ],
         name:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgProject.field.name'), trigger: 'blur' },
            {
                validator: (rule, value, callback) => {
                    if (!/^[a-z][\-,a-z,0-9]*$/.test(value)) callback(new Error(vueObject.$t(vueObject.$te('cgProject.field.nameValid') ? 'cgProject.field.nameValid' : 'system.message.errorValue')))
                    else callback()
                },
                trigger: 'blur'
            }
         ],
         version:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgProject.field.version'), trigger: 'blur' }
         ],
         springModule:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgProject.field.springModule'), trigger: 'blur' }
         ],
         mavenModule:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgProject.field.mavenModule'), trigger: 'blur' }
         ],
         mavenServer:[
            {
                validator: (rule, value, callback) => {
                    if (!value) callback()
                    else {
                       const checked = invalidMessage('url',value)
                       if (typeof checked === 'boolean') {
                           if (checked) callback()
                           else callback(new Error(vueObject.$t(vueObject.$te('cgProject.field.mavenServerValid') ? 'cgProject.field.mavenServerValid' : 'system.message.errorValue')))
                       } else if (typeof checked == 'string' && checked) callback(new Error(checked))
                       else callback()
                    }
                },
                trigger: 'blur'
            }
         ],
         test:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgProject.field.test'), trigger: 'blur' }
         ],
      }, typeof extendRules == 'function' ? extendRules(vueObject, _=>{ return vueObject.record ? vueObject.record : this_.record} ) : extendRules)
   }
}
