<template>
<div v-if="usePicker" class="cg-number-input">
  <cg-input-for-picker ref="pickerInput" 
    :pickerVisible="pickerVisible" :showText="number" 
    :clearable="clearable" :readonly="readonly" :disabled="disabled"
    @clear="clear" 
    @inputClick="pickerVisible=!pickerVisible" 
    @caretClick="pickerVisible=!pickerVisible"/>
  <nut-picker :is-visible="pickerVisible" 
    :title="label"
    :list-data="pickData"
    @close="pickerVisible=false"
    @confirm="setChooseValue"
  />
</div>
<el-input-number v-else v-model="number" v-set-input="{ type: 'number' , readonly : readonly }" 
                 :disabled = "disabled || readonly" type="number"
                 resize autofocus validate-event :class="className()"
                 :controls-position="mobile?'':'right'"
                 :label="label" :placeholder="placeholder"
                 :min="min" :max="max" :step="step" :precision="precision" 
                 @focus="focused=true"
                 @blur="focused=false"
/>
</template>
<script>
export default {
  name: 'CgNumberInput',
  props: {
    value: Number,
    label: String,
    placeholder:String,
    min:Number,
    max:Number,
    step: { type: Number,  default: 1 },
    precision: { type: Number,  default: 0 },
    clearable: { type: Boolean, default: false },
    readonly: { type: Boolean, default: false },
    disabled: { type: Boolean, default: false }
  },
  data() {
    return {
      focused: false,
      number: this.value,
      pickerVisible: false
    }
  },
  
  computed: {
    mobile() {
      return this.$store.state.app.device==='mobile'
    },
    usePicker() {
      return (this.mobile && this.step && (this.min || this.min==0) && (this.max || this.max==0) 
        && (this.max-this.min)/this.step <= 200 )
    },
    pickData() {
      const vv=[]
      let v=this.min
      while (v<=this.max) {
        vv.push(v)
        v += this.step
      }
      return [vv]
    },
  },
  watch: {
    value(newValue) {
      this.number = newValue
    },
    number(newValue) {
      this.$emit("input",newValue)
    }
  },
  methods: {
    clear() {
      this.number = null
    },
    className() {
      let nn = ['cg-number-input']
      if (this.readonly && !this.disabled) nn.push('readonly-by-disable')
      if (this.focused) nn.push('focused')
      return nn;
    },
    setChooseValue(v) {
      this.number = Number.parseInt(v[0])
    }
  }
}
</script>
<style scoped>
</style>
