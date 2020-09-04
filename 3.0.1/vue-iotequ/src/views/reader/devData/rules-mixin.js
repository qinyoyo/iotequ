
import {validIdNumber, validPasswordMessage} from '@/utils/validate'
import {asyncRequest} from '@/utils/request'

export default function getRules(vueObject, getRecordFunction) {
    return {
        groupSelection:[
            {
                validator: (rule, value, callback) => {
                    if (!value && vueObject.record.deviceSelectionMode=='1') callback(new Error('devData.field.groupSelectionValid'.local())) 
                    else  callback()
                },
                trigger: 'blur'
            }
        ],
        readerName:[
            {
                validator: (rule, value, callback) => {
                    if (!value && vueObject.record.deviceSelectionMode=='2') callback(new Error('devData.field.readerSelectionValid'.local())) 
                    else  callback()
                },
                trigger: 'blur'
            }
        ],        
        orgSelection:[
            {
                validator: (rule, value, callback) => {
                    if (!value && vueObject.record.userSelectionMode=='1') callback(new Error('devData.field.orgSelectionValid'.local())) 
                    else  callback()
                },
                trigger: 'blur'
            }
        ],
        userName:[
            {
                validator: (rule, value, callback) => {
                    if (!value && vueObject.record.userSelectionMode=='2') callback(new Error('devData.field.userSelectionValid'.local())) 
                    else  callback()
                },
                trigger: 'blur'
            }
        ], 
    }
}