
import {validIdNumber, validPasswordMessage} from '@/utils/validate'
import {asyncRequest} from '@/utils/request'

export default function getRules(vueObject, getRecordFunction) {
    return {
        idNumber:[
            { required: true, message: vueObject.$t('system.message.needValue'), trigger: 'blur' },
            {
                validator: (rule, value, callback) => {
                    if (!value) callback()
                    else if (validIdNumber(getRecordFunction().idType,value)) callback() 
                    else  callback(new Error(vueObject.$t('system.message.errorFormat')))
                },
                trigger: 'blur'
            }
        ],
        password:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('sysUser.field.password'), trigger: 'blur' },
            {
                validator: (rule, value, callback) => {
                    if (!value) callback()
                    else {
                        const msg = validPasswordMessage(value)
                        if (msg) callback(new Error(msg))
                        else callback()
                    }   
                  },
                trigger: 'blur'                
            }
        ],
        passwordConfirm: [
            {
                validator: (rule, value, callback) => {
                    if (value==vueObject.record.password) callback()
                    else callback(new Error('system.message.passwordConfirmValid'.local()))
                  },
                trigger: 'blur'                
            }
        ],
        name:[           
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('sysUser.field.name'), trigger: 'blur' },
            {
                validator: (rule, value, callback) => {
                  if (!value) callback()
                  else {
                    if (!/^(\S.*?\S|\S)$/.test(value)) callback(new Error(vueObject.$t(vueObject.$te('sysUser.field.nameValid') ? 'sysUser.field.nameValid' : 'system.message.errorValue')))
                    else if (vueObject.isNew) { // && vueObject.path==='register') {
                        asyncRequest({
                            url: '/login/register/checkUserName',
                            method: 'get',
                            timeout: 2000,
                            params: {
                                name: value
                            }
                        }).then(res=>{
                            if (res && res.hasOwnProperty('success') && res.success) callback()
                            else callback(new Error('login.nameUsed'.local()))
                        }).catch(e=>{
                            callback(new Error('login.nameUsed'.local()))
                        })
                    }
                    else callback()
                  }
                },
                trigger: 'blur'
            }
        ]
    }
}