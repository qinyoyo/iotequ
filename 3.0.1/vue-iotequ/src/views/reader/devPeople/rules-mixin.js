
import {validIdNumber} from '@/utils/validate'
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
    }
}