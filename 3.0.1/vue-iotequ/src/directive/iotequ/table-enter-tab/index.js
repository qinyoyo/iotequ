import { on,off,getParent } from "@/utils/dom";
function focusNext(el){
  const td = getParent(el,'td')
  if (!td) return
  let next = td.nextElementSibling
  while (next && next.tagName.toLowerCase()==='td') {
    const l = next.querySelector(':enabled:not([tabindex="-1"])')
    if (l) {
      l.focus()
      l.select()
      return
    }
    next = next.nextElementSibling
  }
  let tr=getParent(td,'tr')
  let lastTr=tr
  if (tr) tr=tr.nextElementSibling
  while (tr && tr.tagName.toLowerCase()==='tr') {
    next = tr.querySelector('td')
    while (next && next.tagName.toLowerCase()==='td') {
      const l = next.querySelector(':enabled:not([tabindex="-1"])')
      if (l) {
        l.focus()
        l.select()
        return
      }
      next = next.nextElementSibling
    }
    lastTr=tr
    tr=tr.nextElementSibling
  }
  getParent(el,'div','cg-list').dispatchEvent(new Event('editInline_Add'))
  setTimeout(_=>{
    if (lastTr) tr=lastTr.nextElementSibling
    if (tr) {
      next = tr.querySelector('td')
      while (next && next.tagName.toLowerCase()==='td') {
        const l = next.querySelector(':enabled:not([tabindex="-1"])')
        if (l) {
          l.focus()
          l.select()
          return
        }
        next = next.nextElementSibling
      }
    }
  },200)
}

function enterCallback(e) {
  if (e.keyCode === 13) {
    
    if (e.target.className.indexOf("el-switch__input") >=0) e.target.click() // switch 回车不修改
    let activeEl = document.activeElement
    activeEl && focusNext(activeEl)
    return false
  }
 }
export default {
  bind(el, binding, vnode) {
    on(el,'keydown', enterCallback)
  },
  unbind(el) {
    off(el,'keydown', enterCallback)
  }
}
