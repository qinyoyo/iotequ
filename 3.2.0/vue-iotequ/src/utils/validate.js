/**
 * @param {string} path
 * @returns {Boolean}
 */
export function isExternal(path) {
  return /^(https?:|mailto:|tel:)/.test(path)
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function validLowerCase(str) {
  const reg = /^[a-z]+$/
  return reg.test(str)
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function validUpperCase(str) {
  const reg = /^[A-Z]+$/
  return reg.test(str)
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function validAlphabets(str) {
  const reg = /^[A-Za-z]+$/
  return reg.test(str)
}

export function validURL(url) {
  const reg = /^(https?|ftp):\/\/[a-zA-Z0-9\-_]+(\.[a-zA-Z0-9-_]+)*(:\d+)?(\/[a-zA-Z0-9\-_]+)*\/?(\?[a-zA-Z0-9\-_\+&%$#~]+=[a-zA-Z0-9\-_\+&%$#~]*(&[a-zA-Z0-9\-_\+&%$#~]+=[a-zA-Z0-9\-_\+&%$#~]*)*)?$/
  return reg.test(url)
}

export function validEmail(email) {
  const reg = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
  return reg.test(email)
}

export function validFixedPhone(phone) {
  const reg = /^((\+[1-9][0-9]{0,3}-\d{2,4}-)|(\d{2,4}-))?([2-9][0-9]{6,7})(-[0-9]{1,6})?$/
  return reg.test(phone)
}

export function validMobilePhone(phone) {
  const reg = /^((\+86-)?1([358][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}|\+(1|1\d{3}|[2-7,9]\d{0,3}|8[0-5,7-9]\d{0,2})-\d{6,12})$/
  return reg.test(phone)
}

export function validPassport(no) {
  const reg = /(^[EeKkGgDdSsPpHh]\d{8}$)|(^(([Ee][a-fA-F])|([DdSsPp][Ee])|([Kk][Jj])|([Mm][Aa])|(1[45]))\d{7}$)/
  return reg.test(no)
}
export function validIdCard(idNo) {
  const reg = /^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/
  return reg.test(idNo)
}

export function invalidMessage(typ,value) {
  switch (typ) {
    case 'url' : if (validURL(value)) return null; else return ('system.message.invalid_'+typ).local()
    case 'email' : if (validEmail(value)) return null; else return ('system.message.invalid_'+typ).local()
    case 'mobile' : if (validMobilePhone(value)) return null; else return ('system.message.invalid_'+typ).local()
    case 'fixed' : if (validFixedPhone(value)) return null; else return ('system.message.invalid_'+typ).local()
    case 'passport': if (validPassport(value)) return null; else return ('system.message.invalid_'+typ).local()
    case 'idcard': if (validIdCard(value)) return null; else return ('system.message.invalid_'+typ).local()
    case 'phone' : if (validFixedPhone(value) || validMobilePhone(value)) return null; else return ('system.message.invalid_'+typ).local()
  } 
  return null
}
export function validIdNumber(idType,no) {
  const typ=idType+''
  switch (typ) {
    case '1' : return validIdCard(no)
    case '2' : return validFixedPhone(no) || validMobilePhone(no)
    case '3' : return validEmail(no)
    case '5' : return validPassport(no)
  }
  return /^[0-9a-zA-Z]{4,16}$/.test(no)
}

export function validPasswordMessage(pass) {
  if (pass.length < 6 || pass.length > 16) return 'login.passwordLength'.local()
  if (window.userSettings.strongPassword) {
    if (/[A-Z]/.test(pass) === false) return 'login.passwordUpper'.local()
    else if (/[a-z]/.test(pass) === false) return 'login.passwordLower'.local()
    else if (/[0-9]/.test(pass) === false) return 'login.passwordDigit'.local()
    else if (/[\~\`\!\@\#\$\%\^\&\*\(\)\-\_\+\=\{\}\[\]\|\\\:\;\"\'\<\>\?\,\.\/]/.test(pass) === false) return 'login.passwordSpecial'.local()
  }
  return ''
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function isString(str) {
  if (typeof str === 'string' || str instanceof String) {
    return true
  }
  return false
}

/**
 * @param {Array} arg
 * @returns {Boolean}
 */
export function isArray(arg) {
  if (typeof Array.isArray === 'undefined') {
    return Object.prototype.toString.call(arg) === '[object Array]'
  }
  return Array.isArray(arg)
}

