<template>
  <el-checkbox-group v-model="valueArray">
    <el-checkbox v-for="(item,index) in dictionaryChanged" v-set-input="{ readonly : readonly }" :key="index" name="name" :label="String(item.value)" 
                 :class="readonly && !disabled ? 'readonly-by-disable' : ''"
                 :disabled="disabled || readonly"
    >
      {{ item.text.local() }}
    </el-checkbox>
  </el-checkbox-group>
</template>
<script>
export default {
  name: 'CgCheckbox',
  props: {
    value: String,
    name: String,
    dictionary: {
      type: Array,
      required: true
    },
    readonly: {
      type: Boolean,
      default: false
    },
    disabled: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      valueArray: this.value ? this.value.split(',') : []
    }
  },
  computed: {
    dictionaryChanged() {
      const newDict = Object.assign(this.dictionary?this.dictionary:[])
      newDict.forEach(e => e.value = String(e.value))
      return newDict
    }
  },
  watch: {
    dictionaryChanged(n) {
      this.setValueArray()
    },
    value(newValue) {
      this.setValueArray()
    },
    valueArray(newValue) {
      if (newValue && newValue.length > 0) this.$emit("input",newValue.join(','))
      else this.$emit("input",null)
    }
  },
  methods: {
    setValueArray() {
      if (this.dictionaryChanged && this.dictionaryChanged.length) {
        const va = this.value ? this.value.split(',') : []
        let nva = []
        va.forEach(v=>{
          this.dictionaryChanged.some(d=>{
            if (d.value == v) {
              nva.push(v)
              return true
            }
          })
        })
        this.valueArray = nva
      }
    }
  }
}
</script>
