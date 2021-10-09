import { jsonp } from '@/utils/jsonp'
function u53request(data,action,onSuccess,onError) {
  const url = window.userSettings.u53ServerUrl+'/'+action
  //console.log(url)
  jsonp(url,data).then((res)=>{
    try {
      if (res) {
        if (res.isSucc) {
          if (typeof onSuccess === 'function') onSuccess(res)
        } else {
          if (!res.Msg) res.Msg = 'reader.u53error'.local()
          if (typeof onError === 'function') onError(res)
        } 
      } else {
          if (typeof onError === 'function') onError({isSucc: false, Msg: 'reader.u53error'.local(), retCode: 1})
      }
    } catch(e){
      console.log(e)
    }
  }).catch((err)=>{
    if (typeof onError === 'function') onError({isSucc: false, Msg: 'reader.checkU53'.local(), retCode: 1001})
  })
}
export function u53Version(onSuccess,onError) {
    return u53request( {}, 'Version',onSuccess,onError)
}
export function u53Connect(type,onSuccess,onError) {
    return u53request( {'isSetComm':type }, 'Connect',onSuccess,onError)
}
export function u53Disconnect(onSuccess,onError) {
    return u53request({},'Disconnect',onSuccess,onError)
}

export function u53Reset(onSuccess,onError) {
    return u53request({},'ClearUsers',onSuccess,onError)
}

export function u53Register(userId,fingerId,onSuccess,onError) {
    return u53request({userId,fingerId,timeOut:60000,isDel:'1'},'RegeistFingers',onSuccess,onError)
}

export function u53Auth(userId,fingerId,templateData,onSuccess,onError) {
    return u53request({userId,fingerId,templateData,timeOut:60000},'AuthFingersOneByOne',onSuccess,onError)
}

export function u53Read2(onSuccess,onError,options) {
    return u53request(options?options:{timeOut:60000},'TemplateAndImage',onSuccess,onError)
}
export function u53Image(onSuccess,onError,options) {
  return u53request(options?options:{timeOut:60000},'Image',onSuccess,onError)
}
export function u53Sample(onSuccess,onError,options) {
  return u53request(options?options:{timeOut:60000},'Template',onSuccess,onError)
}
export function u53DisplayMessage(messageOptions,onSuccess,onError) {
  return u53request(messageOptions,'ShowMessage',onSuccess,onError)
}
export function u53MessageCapacity(onSuccess,onError) {
  return u53request({},'ShowMessage',(d)=>{
    if (d && d.isSucc && d.Msg) {
      if (d.Msg.indexOf('sound')>=0) d.sound=true
      if (d.Msg.indexOf('window')>=0) d.window = true
    }
    if (typeof onSuccess === 'function')  onSuccess(d)
  },onError)
}

export function u53Cancel(onSuccess,onError) {
  return u53request({},'Cancel',onSuccess,onError)
}
export default {
    u53Version,
    u53Connect,
    u53Disconnect,
    u53Reset,
    u53Register,
    u53Auth,
    u53Read2,
    u53Image,
    u53Sample,
    u53MessageCapacity,
    u53DisplayMessage,
    u53Version,
    u53Cancel
}
