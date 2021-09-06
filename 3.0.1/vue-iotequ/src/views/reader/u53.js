import { jsonp } from '@/utils/jsonp'
function u53request(data,action,onSuccess,onError) {
  const url = 'http://localhost:9000/'+action
  jsonp(url,data).then((res)=>{
    if (res) {
      if (res.isSucc) {
        if (typeof onSuccess === 'function') onSuccess(res)
      } else {
        if (!res.Msg) {
            if (res.status) res.Msg = res.status+":"+'reader.checkU53'.local()
            else res.Msg = 'reader.u53error'.local()
        } 
        if (typeof onError === 'function') onError(res)
      } 
    } else {
        if (typeof onError === 'function') onError({isSucc: false, Msg: 'reader.u53error'.local()})
    }
  }).catch((err)=>{
    if (typeof onError === 'function') onError({isSucc: false, Msg: '微服务访问失败'})
  })
}
export function u53Connect(type,onSuccess,onError) {
    return u53request( {'isSetComm':type }, 'connectDev',onSuccess,onError)
}
export function u53Disconnect(onSuccess,onError) {
    return u53request({},'existDev',onSuccess,onError)
}
export function u53Check(onSuccess,onError) {
    
}

export function u53Reset(onSuccess,onError) {
    return u53request({},'DynaClearUsers',onSuccess,onError)
}

export function u53Register(userId,fingerId,onSuccess,onError) {
    return u53request({userId,fingerId,timeOut:60000,isDel:'1'},'RegeistFingers',onSuccess,onError)
}

export function u53Auth(userId,fingerId,templateData,onSuccess,onError) {
    return u53request({userId,fingerId,templateData,timeOut:60000},'AuthFingersOneByOne',onSuccess,onError)
}

export function u53Read(onSuccess,onError) {
    return u53request({},'ServerAuth',onSuccess,onError)
}
export function u53DisplayMessage(messageOptions,onSuccess,onError) {
  return u53request(messageOptions,'ServerMessage',onSuccess,onError)
}
export default {
    u53Connect,
    u53Disconnect,
    u53Reset,
    u53Register,
    u53Auth,
    u53Read,
    u53DisplayMessage
}
