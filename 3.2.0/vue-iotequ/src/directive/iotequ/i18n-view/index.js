/*
设置一个input将内容作为多语言的key显示特定语言
*/
const dataKey = 'inputOriginalValue'
function onfocus() {
  const v0 = this.dataset[dataKey]
  if (v0) this.value=v0
}
function onblur() {
  const v = this.value
  this.dataset[dataKey]=v
  if (v) this.value=v.local()
}
export default {
  bind(el, binding, vnode) {
    el.querySelectorAll(['input']).forEach(e=>{
      e.onfocus=onfocus
      e.onblur=onblur

    })
  },
  componentUpdated(el, binding, vnode) {
    el.querySelectorAll(['input']).forEach(e=>{
      const v = e.value
      e.dataset[dataKey]=v
      if (v) e.value=v.local()
    })
  },
}
