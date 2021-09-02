const moment = require('moment')
export function timestamp(dt) {
  return Number.parseInt((dt ? moment(dt) : moment()).format('x'))
}
export function partOf(dt,mode) {
  const m = moment(dt)
  switch (mode) {
    case 'year' : return m.year()
    case 'month' : return m.month()
    case 'day' : return m.date()
    case 'hour' : return m.hour()
    case 'minute' : return m.minute()
    case 'second' : return m.second()
    case 'millisecond' : return m.millisecond()
    case 'week' : return m.weekday()
    case 'isoWeek' : return m.isoWeekday()
  }
  return null
}
export function toDate(str, format) {
  if (!str) return new Date()
  else {
    if (typeof str === 'string' && /^[0-9]{1,2}\:[0-9]{1,2}(\:[0-9]{1,2}(\.[0-9]*){0,1}){0,1}$/.test(str)) str = '1970-01-01 '+str 
    if (format) return moment(str, format).toDate()
    else return moment(str).toDate()
  }
}
export function toString(dt, format) {
  if (!dt) return ''
  if (typeof dt === 'string') dt=toDate(dt)
  return (dt ? moment(dt) : moment()).format(format ? format.replace(/y/g, 'Y').replace(/d/g, 'D') : 'YYYY-MM-DD HH:mm:ss')
}
export function fullString(dt) {
  if (!dt) return ''
  return toString(dt,'YYYY-MM-DDTHH:mm:ss.SSSZZ')
}
export function toShortString(dt,showSecond) {
  if (!dt) return ''
  if (typeof dt === 'string') dt=toDate(dt)
  let format = 'YYYY-MM-DD HH:mm'+(showSecond?':ss':'')
  const m = moment(dt), n = moment() 
  if (m.year()==n.year()) {
    format=format.substring(5)
    if (m.month()==n.month()) {
      format=format.substring(3)
      if (m.day()==n.day()) {
        format=format.substring(3)
      }
    }
  } 
  return m.format(format)
}
export function equals(dt0,dt1) {
  if ((dt0 && !dt1) || (!dt0 && dt1)) return false
  else if (!dt0 && !dt1) return true
  else return subtract(dt0,dt1) === 0
}
export function dateAdd(dt, value, mode) {
  return (dt ? moment(dt) : moment()).add(value, mode).toDate()
}
export function subtract(one,two) {
  const m1 = (one ? moment(one) : moment())
  const m2 = (two ? moment(two) : moment())
  return m1.diff(m2, 'seconds')
}
export function startOf(dt, mode) {
  if (mode === 'week') mode = 'isoWeek'
  return (dt ? moment(dt) : moment()).startOf(mode).toDate()
}
export function endOf(dt, mode) {
  if (mode === 'week') mode = 'isoWeek'
  return (dt ? moment(dt) : moment()).endOf(mode).toDate()
}
export function prevWorkDay(dt) {
  const m = (dt ? moment(dt) : moment())
  const week = m.isoWeek()
  if (week === 1) return m.add(-3, 'days').toDate()
  else if (week === 7) return m.add(-2, 'days').toDate()
  else return m.add(-1, 'days').toDate()
}


// mode ：为三个词的字符串，一个空格分隔
// 第一个词为 [this|prev|next] 第二个词为一个数字，表示位移，为1时可以省略
// 第三个词表示单位 [day|week|month|year|hour|minute|second]
// 错误的情况返回空
export function timeRange(mode) {
  let md = mode.toLowerCase().trim().split(' ')
  if (md.length === 1) {
    if (md[0] === 'today') md = ['this', '1', 'day']
    else if (md[0] === 'tomorrow') md = ['next', '1', 'day']
    else if (md[0] === 'yesterday') md = ['prev', '1', 'day']
    else md = ['this', '1', md[0]]
  } else if (md.length === 2) {
    md = [md[0], '1', md[1]]
  }
  if (['this', 'prev', 'next'].indexOf(md[0]) < 0) return null
  if (['day', 'week', 'month', 'year', 'hour', 'minute', 'second', 'millisecond'].indexOf(md[2]) < 0) return null
  if (parseInt(md[1]) === 'NaN') return null
  if (md[0] === 'this') return [moment().startOf(md[2] === 'week' ? 'isoWeek' : md[2]).toDate(), moment().endOf(md[2] === 'week' ? 'isoWeek' : md[2]).toDate()]
  else {
    const v = (md[0] === 'prev' ? -1 : 1) * parseInt(md[1])
    const start = moment().add(v, md[2] + 's').startOf(md[2] === 'week' ? 'isoWeek' : md[2])
    const end = moment().add(v, md[2] + 's').endOf(md[2] === 'week' ? 'isoWeek' : md[2])
    return [start.toDate(), end.toDate()]
  }
}

export function toTime(dt) {
  return toDate('1970-01-01 '+toString(dt,'HH:mm:ss.SSS'))
}
export function ageOf(dt) {
  if (!dt) return null
  return Math.round((new Date().getTime() - new Date(dt).getTime())/365.25/24/3600000)
}
export default {
  timestamp,
  partOf,
  equals,
  dateAdd,
  subtract,
  toString,
  fullString,
  toShortString,
  toDate ,
  toTime,
  startOf,
  endOf,
  prevWorkDay,
  timeRange,
  ageOf
}
