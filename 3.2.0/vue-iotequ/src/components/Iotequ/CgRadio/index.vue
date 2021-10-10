<template>
  <el-radio-group v-model="valueString">
    <el-radio v-for="(item,index) in dictionaryChanged" :key="index" v-set-input="{ readonly : readonly }"
              name="name"
              :label="item.value" 
              :class="readonly && !disabled ? 'readonly-by-disable' : ''"
              :disabled="disabled || readonly"
              @change="change"
    >
      {{ item.text.local() }}
    </el-radio>
  </el-radio-group>
</template>
<script>
export default {
  name: 'CgRadio',
  props: {
    value: [String, Number],
    name: String,
    dictionary: {
      type: Array,
      required: true
    },
    disabled: {
      type: Boolean,
      default: false
    },
    readonly: {
      type: Boolean,
      default: false
    },
    numberic: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      valueString: this.getValueString()
    }
  },
  computed: {
    dictionaryChanged() {
      const newDict = Object.assign(this.dictionary ? this.dictionary : [])
      newDict.forEach(e => e.value = String(e.value))
      return newDict
    }
  },
  watch: {
    value(newValue) {
      this.valueString = this.getValueString()
    },
    valueString(newValue, oldValue) {
      if (newValue) this.$emit("input",this.numberic ? parseInt(newValue) : newValue)
      else this.$emit("input",null)
    }
  },
  methods: {
    getValueString() {
      return this.numberic ? (this.value || this.value==0 ? this.value+'' : '') : this.value
    },
    change(v) {
      this.$emit("change",v)
    }
  }
}
</script>
