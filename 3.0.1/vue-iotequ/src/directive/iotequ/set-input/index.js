/*
binding：一个对象，包含以下属性：
name：指令名，不包括 v- 前缀。
value：指令的绑定值，例如：v-my-directive="1 + 1" 中，绑定值为 2。
oldValue：指令绑定的前一个值，仅在 update 和 componentUpdated 钩子中可用。无论值是否改变都可用。
expression：字符串形式的指令表达式。例如 v-my-directive="1 + 1" 中，表达式为 "1 + 1"。
arg：传给指令的参数，可选。例如 v-my-directive:foo 中，参数为 "foo"。
modifiers：一个包含修饰符的对象。例如：v-my-directive.foo.bar 中，修饰符对象为 { foo: true, bar: true }。
*/

// 用法 v-set-input[:指定一个类名][.标签tag]="设置值的object"
// object 的键位 设置的属性，值为设置的属性值，当值以append:开头时，则属性为添加，如添加一个class
// v-set-input:no-tab-index.input.button.a="{tabIndex: -1}" ,参数(:)必须写在修饰符前面
// 将所有类名为 no-tab-index 的节点下的input,button,a 节点的 tabIndex 设置为 -1
// v-set-input="{type: 'number'}" 移动端可弹出数字键盘
// v-set-input.button="{class: 'append:a-cls'}" 给所有的button添加a-cls类
import { setAttribute } from '@/utils/dom'

export default {
  componentUpdated(el, binding, vnode) {
    if (typeof binding.value !== 'object') return
    const cls = binding.arg ? binding.arg : '*'
    const types = (binding.modifiers ? Object.keys(binding.modifiers) : [])
    const params = Object.keys(binding.value)
    const f=function(e) {
      params.forEach(k=>{
        const append = (typeof binding.value[k] === 'string' && binding.value[k].indexOf('append:')==0)
        const value = append ? binding.value[k].substring(7) : binding.value[k]
        setAttribute(e, k, value, append)
      })       
    }
    if ((cls==='*' || el.classList.contains(cls)) && (types.length==0 || types.indexOf(el.tagName.toLowerCase())>=0)) f(el) 
    if (cls==='*') {
      el.querySelectorAll(types.length==0 ? cls: types.join(',')).forEach(e=>{
        f(e)
      })      
    } else if (types.length==0) {
      el.querySelectorAll('.'+cls+' *').forEach(e=>{
        f(e)
      })
    } else types.forEach(tag=>{
      el.querySelectorAll('.'+cls+' '+tag).forEach(e=>{
        f(e)
      })
    })
  }
}
