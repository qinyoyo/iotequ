<template>
  <el-cascader v-model="elValue" v-set-input="{ readonly : mobile }" :name="name" :placeholder="placeholder"
    :options="localDictionary(dictionary)" :props="elProps"
    :clearable="!readonly && clearable" 
    :class="readonly && !disabled ? 'readonly-by-disable' : ''"
    :disabled="disabled || readonly"
    :show-all-levels="showAllLevels">
  </el-cascader>
</template>
<script>
import {validDictValue} from '@/utils/cg'
export default {
  name: 'CgCascader',
  props: {
    value: [String, Number],
    name: String,
    placeholder: String,
    dictionary: {
      type: Array,
      required: true
    },
    showAllLevels: {
      type: Boolean,
      default: false
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
    filterable: {
      type: Boolean,
      default: true
    },
    numberic: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      mobile: this.$store.state.app.device==='mobile',
      elProps: {label: 'text', children: 'nodes', expandTrigger: 'hover', multiple: this.multiple, checkStrictly: true, emitPath: false},
      elValue: this.multiple ? (this.value+'' ? (this.value + '').split(',') : []) : this.value+''
    }
  },
  watch: {
    value: {
      handler(newValue) {
        this.elValue = this.multiple ? (newValue+'' ? (newValue + '').split(',') : []) : newValue+''
      },
      immediate:true
    },
    elValue : {
      handler(newValue) {
        newValue = validDictValue(newValue,this.dictionary,this.multiple)
        if (newValue) this.$emit("input",this.multiple ? (newValue instanceof Array ? newValue.join(',') : newValue) : (this.numberic ? parseInt(newValue) : newValue))
        else this.$emit("input",null)
      },
      immediate:true
    }
  },
  methods: {
    localDictionary(dict) {
      const that=this
      dict.forEach(d=>{
        if (d.text) d.text=d.text.local()
        if (d.nodes) that.localDictionary(d.nodes)
      })
      return dict
    }
  }
}
</script>

<style scoped>

</style>
