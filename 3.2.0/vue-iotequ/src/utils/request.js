import requestService from './requestService'
import { Message } from 'element-ui'
import { generateTitle } from '@/utils/i18n'
import Vue from 'vue'
import CgProgress from '@/components/Iotequ/CgProgress'
import axios from 'axios'
import { apiUrl } from '@/utils/requestService'
import { addBodyNode } from '@/utils/dom'
export function showMessage( options ) {
  if (options.code) options.message = generateTitle('error.'+options.code) + (options.message ? ':'+options.message : '')
  const instance = Message(options)
  if (options.detail) {
    instance.$el.onclick = function() {
      instance.close()
      Message({
        showClose: true,
        type: 'error',
        duration: 0,
        dangerouslyUseHTMLString:true,
        message: '<div style="height:360px;overflow:auto">'+options.detail.replace(/\n/g,'<br>')+'</div>'
      })
    }
  }
}
const progressService = axios.create({
  baseURL: apiUrl(),
  withCredentials: true,
  crossDomain: true,
  responseType: 'json',
  responseEncoding: 'utf8',
  timeout: 2000
})
let errorTimes = 0
let waitingInstance = null
const startProgress = () => {
  const getProgress = (reset) => {
    if (waitingInstance) {
      progressService.get('/res/progress?reset=' + (reset ? 'true' : 'false')+'&stamp=' + new Date().getTime()).then((d) => {
        if (d && d.data && d.data.hasOwnProperty('progress') && d.data.progress < 100) {
          errorTimes = 0
          waitingInstance.progress = d.data.progress
          setTimeout(_=> getProgress(false) , 200)
        }
        else {
          errorTimes ++
          if (errorTimes>=3) closeProgress()
        }
      }).catch(_=>{
        errorTimes ++
        if (errorTimes>=3) closeProgress()
      })
    } else closeProgress()
  }
  errorTimes = 0
  getProgress(true) 
}

const domId = 'cg_progress_waiting'
export function logout() {
  if (window.$vue.$store.state.user.authentication) {
    requestService({
      url: '/logout',
      method: 'post'
    }).then(_=>{
      window.$vue.$router.push('/login')
    })
  } else {
    window.$vue.$router.push('/')
  }
}
export function showProgress() {
  closeProgress()
  addBodyNode(domId)
  const Constructor = Vue.extend(CgProgress)
  waitingInstance = new Constructor({
    el: '#' + domId
  })
  startProgress()
  return waitingInstance
}
export function closeProgress() {
  if (waitingInstance) {
    waitingInstance.close()
    waitingInstance = null
  }
}

function changeErrorMessage(error,msg) {
  if (error == 'DataIntegrityViolationException') {
    if (msg.indexOf('Cannot delete or update a parent row: a foreign key constraint fails')>=0) return generateTitle('error.db_msg_fk')
  }
  return msg
}
// 后台请求，silence = true静默请求，不显示提示，silence = 'show-error' 不显示成功提示，显示错误提示
export function request(data, silence) {
  const SHOWERROR = 'show-error'
  return new Promise((resolve, reject) => {
    waitingInstance = false
    if (data.hasOwnProperty('timeout') && data.timeout === 0) {
      data.timeout = 0
      showProgress()
    }
    requestService(data).then(res => {
      closeProgress();
      if (Object.prototype.toString.call(res) === '[object Blob]') {
        let reader = new FileReader()
        reader.onload = function () {
          const res = JSON.parse(reader.result)
          if (res && res.hasOwnProperty('success') && !res.success && res.error === 'session_timeout' ) {
            if (!silence || silence==SHOWERROR) showMessageFromServer(res)
            setTimeout(_=>{
              logout()
            }, 100)            
          } else if (!silence || (silence==SHOWERROR && res && res.hasOwnProperty('success') && !res.success)) {
            showMessageFromServer(res)
          }
          resolve(res)
        }
        reader.readAsText(res, 'utf-8')
      }
      else if (res && res.hasOwnProperty('success') && !res.success && res.error === 'session_timeout' ) {
        if (!silence || silence==SHOWERROR) showMessageFromServer(res)
        setTimeout(_=>{
          logout()
        }, 100)
      } else if (!silence || (silence==SHOWERROR && res && res.hasOwnProperty('success') && !res.success)) {
        showMessageFromServer(res)
      }
      resolve(res)
    }).catch(error => {
      closeProgress()
      if (!silence || silence==SHOWERROR) {
        showMessageFromServer(error)
      }
      reject(error)
    })
  })
}
export async function asyncRequest(data) {
  return await requestService(data)
}

export function showMessageFromServer(res) {
  if (!res) {
    showMessage({
      message: generateTitle('error.unknown'),
      code: '',
      type: 'error',
      duration: 3 * 1000
    })
  } else if (typeof res === 'string') {
    showMessage({
      message: res,
      code: '',
      type: 'warn',
      duration: 3 * 1000
    })
  } else if (!res.success) {
    let msg = ''
    if (typeof res.message ==='string' && res.message.indexOf('<html>') >=0 ) {
      showMessage({
        message: changeErrorMessage(res.error, res.message),
        code: res.error,
        type: 'error',
        detail: res.detailMessage,
        dangerouslyUseHTMLString: true,
        duration: 3 * 1000
      })   
      return   
    }
    if (res.error) {
      if (typeof res.error == 'number') {
        msg = generateTitle('error.error_'+res.error)
      }
      else if (typeof res.error === 'string') {
        msg=generateTitle((res.error.indexOf('.')>0 ? '' : 'error.')+res.error)
        if (res.message) msg = msg + '(' + changeErrorMessage(res.error, res.message) + ')'
      } else  {
        msg = JSON.stringify(res)
      }
    } else msg = res.message || generateTitle('error.unknown')
    showMessage({
      message: msg,
      code: res.error,
      type: 'error',
      detail: res.detailMessage,
      duration: 3 * 1000
    })
  } else {
    let message = !res.message || res.message === 'success' ? generateTitle('system.message.success') : generateTitle(res.message)
    if (res.parameter && res.parameter.noChanged) message = generateTitle('system.message.nochange')
    else if (res.parameter && res.parameter.hasOwnProperty('rows')) message = message + '. ' + generateTitle('system.message.successItems') + ' : ' + res.parameter.rows
    showMessage({
      message: message,
      code: res.code,
      type: 'success',
      detail: res.detailMessage,
      duration: 2 * 1000
    })
  }
}
