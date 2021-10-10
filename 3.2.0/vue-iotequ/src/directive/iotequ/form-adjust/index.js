import { isMobile } from '@/utils/cg'
import { onTransitionEnd, offTransitionEnd,setStyle } from '@/utils/dom'

function doAdjust($el) {
  $el.querySelectorAll(".el-form--label-left.cg-form-cell .el-form-item__label").forEach(e=>{
    $el.querySelectorAll(".el-form--label-left.cg-form-cell .el-form-item__label").forEach(e=>{
      let w = e.offsetWidth
      if (w) {
        let c = e.parentNode.querySelector('.el-form-item__content')
        setStyle(c, 'margin-left', (w + 2)+'px') 
      }
    })
  })
}
function listener() {
  doAdjust(this)
}
export default {
  inserted(el, binding, vnode) {
    if (isMobile()) {
      setTimeout(()=>{
        doAdjust(el)
        //onTransitionEnd(el, listener)
      },500)
    }
  },
  unbind(el) {
    if (isMobile()) offTransitionEnd(el, listener)
  }
}
