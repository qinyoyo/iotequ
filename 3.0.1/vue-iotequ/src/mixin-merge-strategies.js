import Vue from 'vue'

function extend(to, _from) {
  for (const key in _from) {
    to[key] = _from[key]
  }
  return to
}

const optionMergeStrategiesMethods = Vue.config.optionMergeStrategies.methods
export function methodsStrategy(parentVal, childVal, vm, key) {
  if (!parentVal) return childVal
  if (!childVal) return parentVal
  if (typeof parentVal.setChildrenParams === 'function') {
    console.log('setChildrenParams')
  }
  if (typeof parentVal.useMixinMethodsFirst === 'function' && parentVal.useMixinMethodsFirst()) {
    const ret = Object.create(null)
    extend(ret, childVal)
    extend(ret, parentVal)
    Object.keys(childVal).forEach(f => {
      if (parentVal[f]) {
        ret['super_' + f] = childVal[f]
      }
    })
    return ret
  } else if (typeof childVal.useMixinMethodsFirst === 'function' && childVal.useMixinMethodsFirst()) {
    const ret = Object.create(null)
    extend(ret, parentVal)
    extend(ret, childVal)
    Object.keys(parentVal).forEach(f => {
      if (childVal[f]) {
        ret['super_' + f] = parentVal[f]
      }
    })
    return ret
  }  else return optionMergeStrategiesMethods(parentVal, childVal, vm, key)
}

