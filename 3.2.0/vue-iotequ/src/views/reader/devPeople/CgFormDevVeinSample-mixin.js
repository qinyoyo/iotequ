import { Message } from 'element-ui'
import cgForm from '@/utils/cgForm'
import clipboard from '@/utils/clipboard'
import u53 from '@/utils/u53'
function showError(data) {
    Message({
      message: data && data.Msg ? data.Msg : 'reader.u53error'.local(),
      code: '',
      type: 'error',
      duration: 3 * 1000
    })
}
export default {
  data() {
    return {
      connected: false,
      hasFingerService: false,
      fingerOriginalType1: null,
      fingerOriginalWarning1: false,
      fingerOriginalType2: null,
      fingerOriginalWarning2: false,
      template: ''
    }
  },
  computed: {
  },
  watch: {
  },
  created () {
    const that = this
    this.record.fingerType = 1
    this.record.warning = false
    this.ignoreRecordChanged = true
    this.connect(1)
    cgForm.form_request({ 
      formObject: that, 
      method: 'get', 
      params: {
        userNo: that.record.userNo
      }, 
      action: 'action/getRegistered',
      silence: true,
      onSuccess: that.getFingerOriginal
    })
  },
  destroyed () {
    if (this.hasFingerService && this.connected) this.disconnect()
  },
  methods: {
    useMixinMethodsFirst() {
      return true
    },  
    getFingerOriginal() {
        this.fingerOriginalType1 = this.record.fingerNo1
        this.fingerOriginalWarning1 = this.record.warning1
        this.fingerOriginalType2 = this.record.fingerNo2
        this.fingerOriginalWarning2 = this.warning2
    },
    extendActionFilter(action) {
      return action!='edit'
    },
    disabledAction(btn) {
      if (btn.action=='removeFinger') return !this.record.regFingers
      else if (btn.action=='verifyFinger') return !this.record.regFingers || !this.hasFingerService || !this.connected
      else if (btn.action=='registerFinger') return !this.record.userNo || !this.hasFingerService || !this.connected || this.record.regFingers>=2 
      else return !this.hasFingerService
    },
    disconnect(onSuccess,onError) {
      u53.u53Cancel();
      u53.u53Disconnect(onSuccess,onError)
    },
    connect(type){
      const that = this
      u53.u53Connect(type,
          (data)=>{
            if (!data.isSucc) {
              that.connected=false;
              that.hasFingerService=false;	
              Message({
                message: 'error.resource_not_found'.local() + ' : '+'reader.checkU53'.local(),
                code: '',
                type: 'error',
                duration: 3 * 1000
              })   
            } else {
              that.connected=true
              that.hasFingerService=true
            }
          },
          (data)=> {
            that.connected=false
            showError(data)
          }
      )
    },
    resetU53(){
      if (!this.hasFingerService) return;
      u53.u53Reset((data) => {
          Message({
            message: 'reader.resetOK'.local(),
            code: '',
            type: 'success',
            duration: 3 * 1000
          })
        }, showError)
    },
    doRegister(fingerId) {
      const that = this
      if (!this.hasFingerService) return
      if (fingerId == 1 || fingerId==2) {     
          u53.u53Register(that.record.userNo,fingerId,
              (data) => {
                if(data.isSucc){
                  that.template = data.Msg
                  cgForm.form_request({ 
                    formObject: that, 
                    method: 'get', 
                    params: {
                      userNo: that.record.userNo,
                      fingerIndex: fingerId,
                      fingerType: fingerId == 1 ? that.record.fingerNo1 : that.record.fingerNo2,
                      template: that.template,
                      update: (fingerId == 1 && that.fingerOriginalType1) || (fingerId==2 && that.fingerOriginalType2),
                      isWarning: that.record.warning1
                    }, 
                    action: 'action/registerFinger',
                    onSuccess: that.getFingerOriginal
                  })
                }
              },showError
          )	
      } else {
        Message({
          message: 'reader.deleteOneFinger'.local(), 
          code: '',
          type: 'warning',
          duration: 3 * 1000
        })
      }	
    },
    auth(index) {
      if (!this.hasFingerService) return
      if ((index==1 && this.fingerOriginalType1) || (index==2 && this.fingerOriginalType2)) {
        cgForm.form_request({ 
          formObject: this, 
          method: 'get', 
          params: {
            userNo: this.record.userNo,
            fingerIndex: index
          }, 
          action: 'action/verifyFinger',
          silence: true,
          onSuccess: (res) => {
            if (res.success) {
              const template = res.template
              u53.u53Auth(this.record.userNo,index,template,null,showError)
            }
          }
        })
      } else {
        Message({
          message: 'reader.noneAuth'.local(), 
          code: '',
          type: 'warning',
          duration: 3 * 1000
        })
      }
    },
    match() {
      const that = this
      if (!this.hasFingerService) return
      const instance=Message({
          showClose: true,
          type: 'success',
          duration: 60000,
          message: 'reader.readFromDevice'.local()
      }) 
      u53.u53Read2(
          (data) => {
            instance.close()
            if(data.isSucc){
              that.template = data.Msg
              that.templateImage = data.Msg1
              cgForm.form_request({ 
                formObject: that, 
                method: 'get', 
                params: {
                  template: that.template,
                  image: that.templateImage,
                  thresh:0
                }, 
                action: 'action/matchFinger',
                silence: true,
                onSuccess: (data)=>{
                  if (data.success && data.matchInfo) {
                    const info = data.matchInfo
                    let msg = '<a id="copytemplate">'+'reader.authInfo.bytes'.local()+':'+that.template.length/2 + '</a><br>'+'reader.authInfo.matches'.local()+':'+info.count+'<br>'
                    if (info.dictSize) msg += ('reader.authInfo.capacity'.local()+':'+that.printf('%,d',info.dictSize)+'<br>')
                    if (info.matchUsed) msg += ('reader.authInfo.authTimes'.local()+':'+that.printf('%,d',info.matchUsed)+'us<br>')
                    if (info.usUsed) msg += ('reader.authInfo.totalTimes'.local()+':'+that.printf('%,d',info.usUsed)+'us<br>')
                    if (info.state) msg += ('reader.authInfo.status'.local()+':'+info.state+'<br>')
                    if (info.count) {
                      msg += ('reader.authInfo.details'.local()+':'+'<br>')
                      for (let i=0;i<info.count;i++) {
                        const f=info.list[i];
                        const fi = f.name + '('+f.idNo+'):'+f.score +'reader.authInfo.score'.local() +' '+that.dictValue(f.fingerType,that.dictionary.dictFingerType,false,true)
                        if (f.warning) msg += '<div style="color:red">'+fi+'</div>'
                        else msg += (fi+'<br>')
                      }
                    }
                    Message({
                      showClose: true,
                      type: 'success',
                      duration: 0,
                      dangerouslyUseHTMLString:true,
                      message: msg
                    }).$el.querySelector('#copytemplate').onclick = function(e) {
                      clipboard(that.template,e)
                    }
                  } else if (data.message)  Message(data.message)
                  else Message('error.network_error'.local())
                }
              })
            }
          },
          (data)=>{
            instance.close()
          },
          {
            isShow: true,
            compress:true
          }
      )		
    },
    doRemove(index) {
      const that = this
      if (!this.hasFingerService) return
      if (index && ( (index==1 && this.fingerOriginalType1) || (index==2 && this.fingerOriginalType2))) {    
        that.$confirm(that.$t('devPeople.action.removeFingerConfirm'), that.$t('system.action.confirm'), {
          confirmButtonText: that.$t('system.action.ok'),
          cancelButtonText: that.$t('system.action.cancel'),
          closeOnClickModal: false,
          type: 'warning'
        }).then(_ => {
          cgForm.form_request({ 
            formObject: that, 
            method: 'get', 
            params: {
              userNo: that.record.userNo,
              fingerIndex: index
            }, 
            action: 'action/removeFinger',
            onSuccess: that.getFingerOriginal
          })
        }).catch(_ => {
        })
      } else {
        Message({
          message: 'reader.noneAuth'.local(),
          code: '',
          type: 'warning',
          duration: 3 * 1000
        })
        return        
      } 
    }
  }
}
