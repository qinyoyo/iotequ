<template>
  <el-input v-model="elValue"
      :size="size"  :resize="resize" :form="form" :disabled="disabled" :readonly="readonly" 
      :type="type=='mltext'?'text':'textarea'" :autosize="autosize" :autocomplete="autocomplete" :validateEvent="validateEvent" 
      :suffixIcon="suffixIcon" :prefixIcon="prefixIcon" :label="label"
      :clearable="clearable" :showPassword="showPassword" :showWordLimit="showWordLimit" :tabindex="tabindex"
  >
  </el-input>
</template>
<script>

import {localeText,setLocaleText} from '@/lang'
export default {
  name: 'CgInput',
  props: {
    value: [String],
    size: String,
    resize: String,
    form: String,
    disabled: Boolean,
    readonly: Boolean,
    type: {
      type: String,
      default: 'text'
    },
    autosize: {
      type: [Boolean, Object],
      default: false
    },
    autocomplete: {
      type: String,
      default: 'off'
    },
    autoComplete: {
      type: String
    },
    validateEvent: {
      type: Boolean,
      default: true
    },
    suffixIcon: String,
    prefixIcon: String,
    label: String,
    clearable: {
      type: Boolean,
      default: false
    },
    showPassword: {
      type: Boolean,
      default: false
    },
    showWordLimit: {
      type: Boolean,
      default: false
    },
    tabindex: String
  },
  data() {
    return {
      elValue: localeText(this.value)
    }
  },
  watch: {
    value: {
      handler(newValue) {
        this.elValue = localeText(this.value)
      },
      immediate:true
    },
    elValue : {
      handler(newValue) {
        newValue = setLocaleText(this.value,newValue)
        if (newValue) this.$emit("input",newValue)
        else this.$emit("input",null)
      },
      immediate:true
    }
  }
}
</script>
