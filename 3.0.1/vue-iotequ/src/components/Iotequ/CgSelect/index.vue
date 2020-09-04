<template>
  <el-select ref="elSelect" v-model="valueList" :name="name" :placeholder="placeholder"
             :multiple="multiple" v-set-input="{ readonly : mobile }"
             :clearable="!readonly && clearable"
             :class="readonly && !disabled ? 'readonly-by-disable' : ''"
             :disabled="disabled || readonly"
             :allow-create="allowCreate" :automatic-dropdown="automaticDropdown"
             :filterable="filterable" :popper-append-to-body="appendToBody"
             @change="(v) => { triggerEvent('change',v) }"
             @visible-change="(v) => { triggerEvent('visible-change',v) }"
             @remove-tag="(v) => { triggerEvent('remove-tag',v) }"
             @clear="(v) => { triggerEvent('clear',v) }"
             @blur="(v) => { triggerEvent('blur',v) }"
             @focus="(v) => { triggerEvent('focus',v) }"
  >
    <el-option v-for="(item,index) in dictionaryChanged" :key="index" :label="item.text.local()" :value="item.value" :disabled="item.disabled"/>
  </el-select>
</template>
<script>
import {validDictValue} from '@/utils/cg'
function getValueList(value, multiple, clearable) {
  if (value || value==0) return multiple ? String(value).split(',') : String(value)
  else return (multiple ? [] : null)
}
function getValue(valueList, multiple, clearable) {
  if (multiple) {
    if (valueList && valueList.length > 0) {
      if (valueList instanceof Array)  return valueList.join(',')
      else return valueList+''
    }
    else return null
  } else {
    if (valueList) return valueList
    else return null
  }
}

export default {
  name: 'CgSelect',
  props: {
    value: [String, Number],
    name: String,
    placeholder: String,
    nullText: {
      type: String,
      default: ''
    },
    dictionary: {
      type: [Array,String],
      default: []
    },
    multiple: {
      type: Boolean,
      default: false
    },
    clearable: {
      type: Boolean,
      default: false
    },
    readonly: {
      type: Boolean,
      default: false
    },
    disabled: {
      type: Boolean,
      default: false
    },
    allowCreate: {
      type: Boolean,
      default: false
    },
    filterable: {
      type: Boolean,
      default: false
    },
    numberic: {
      type: Boolean,
      default: false
    },
    appendToBody: {
      type: Boolean,
      default: false
    },
    automaticDropdown: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      valueList: getValueList(this.value, this.multiple, this.clearable)     
    }
  },
  computed: {
    mobile() {
      return this.$store.state.app.device==='mobile'
    },
    dictionaryChanged() {
      const newDict = [] // (this.clearable ? [{ value: null, text: this.nullText }] : [])
      if (this.dictionary)
        if (typeof this.dictionary == 'string') this.dictionary.split(',').forEach(s=>{
          s=s.trim()
          if (s) {
            let ss = s.split('|')
            newDict.push({ text: ss.length>1?ss[1].local() : ss[0].local(), value: ss[0] })
          }
        })
        else this.dictionary.forEach(r=>{
          if (typeof r === 'object') newDict.push({ text: r.text, value: String(r.value)})
          else newDict.push({ text: String(r).local(), value: String(r)})
        })
      return newDict
    }
  },
  watch: {
    value(newValue) {
      this.valueList = getValueList(newValue, this.multiple, this.clearable)
    },
    valueList(newValue) {
      if (!this.allowCreate) newValue = validDictValue(newValue,this.dictionaryChanged,this.multiple)
      this.$emit('input', getValue(newValue, this.multiple, this.clearable))
    }
  },
  methods: {
    triggerEvent(event, options) { 
      this.$emit(event, options) 
    }
  }
}
</script>

<style scoped>
</style>
