function u53request(data,action,onSuccess,onError) {
  const url = 'http://localhost:9000/'+action
  const callback = 'CALLBACK' + Math.random().toString().substr(9,18)
  const JSONP = document.createElement('script')
  JSONP.setAttribute('type','text/javascript')
  const headEle = document.getElementsByTagName('head')[0]

  let ret = '';
  if(data) {
    if(typeof data === 'string')
        ret = '&' + data;
    else if(typeof data === 'object') {
        for(let key in data)
            ret += '&' + key + '=' + encodeURIComponent(data[key]);
  }
  ret += '&_time=' + Date.now();
  }
  JSONP.src = `${url}?callback=${callback}${ret}`;
  window[callback] = res => {
    if (res) {
      if (res.isSucc) {
        if (typeof onSuccess === 'function') onSuccess(res)
      } else {
        if (!res.Msg) {
            if (res.status) res.Msg = res.status+ ':U53 error.failure'.local()
            else res.Msg = 'U53 error.failure'.local()
        } 
        if (typeof onError === 'function') onError(res)
      } 
    } else {
        if (typeof onError === 'function') onError({isSucc: false, Msg: 'U53 error.failure'.local()})
    }
    headEle.removeChild(JSONP)
    delete window[callback]
  }
  headEle.appendChild(JSONP)
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

export default {
    u53Connect,
    u53Disconnect,
    u53Reset,
    u53Register,
    u53Auth,
    u53Read
}
