<template>
  <el-input v-model="showText" type="text" :clearable="false" readonly :disabled="disabled"
    @click.native.stop="inputClick" >
    <template slot="append">
      <i :class="suffixIcon()" style="padding-left:6px" @click.stop="clearOrDown"/>
    </template>
  </el-input>
</template>
<script>
export default {
  name: 'CgInputForPicker',
  props: {
    showText: [Number,String],
    pickerVisible: { type: Boolean,  default: false },
    clearable: { type: Boolean,  default: false },
    readonly: { type: Boolean,  default: false },
    disabled: { type: Boolean,  default: false }
  },
  data() {
    return {
    }
  },
  methods: {
    suffixIcon() {
      if (this.readonly || this.disabled) return 'el-icon-view'
      else if (this.pickerVisible) return 'el-icon-arrow-up'
      else if (this.clearable && (this.showText || this.showText===0)) return 'el-icon-circle-close'
      else return 'el-icon-arrow-down'
    },
    inputClick() {
      if (!this.readonly && !this.disabled) this.$emit('inputClick')
    },
    clearOrDown() {
      if (this.readonly || this.disabled) return
      if (this.clearable && (this.showText || this.showText===0)) this.$emit('clear')
      else this.$emit('caretClick')
    }
  }
}
</script>
