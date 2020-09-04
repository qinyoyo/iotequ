
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
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('sysTask.field.name'), trigger: 'blur' }
         ],
         sceduleYears:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('sysTask.field.sceduleYears'), trigger: 'blur' }
         ],
         scheduleMonths:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('sysTask.field.scheduleMonths'), trigger: 'blur' }
         ],
         scheduleDays:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('sysTask.field.scheduleDays'), trigger: 'blur' }
         ],
         scheduleWeeks:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('sysTask.field.scheduleWeeks'), trigger: 'blur' }
         ],
         scheduleHours:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('sysTask.field.scheduleHours'), trigger: 'blur' }
         ],
         scheduleMinutes:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('sysTask.field.scheduleMinutes'), trigger: 'blur' }
         ],
         className:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('sysTask.field.className'), trigger: 'blur' }
         ],
         mothodName:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('sysTask.field.mothodName'), trigger: 'blur' }
         ],
         isStatic:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('sysTask.field.isStatic'), trigger: 'blur' }
         ],
         isRunning:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('sysTask.field.isRunning'), trigger: 'blur' }
         ],
      }, typeof extendRules == 'function' ? extendRules(vueObject, _=>{ return vueObject.record ? vueObject.record : this_.record} ) : extendRules)
   }
}
