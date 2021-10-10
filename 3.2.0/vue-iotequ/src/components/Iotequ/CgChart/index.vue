<template>
  <div ref="chart" :class="className()" :style="styleContent()" />
</template>
<script>

const echarts = require('echarts/lib/echarts')
require('echarts/lib/component/tooltip')
require('echarts/lib/component/title')
require('echarts/lib/component/tooltip')
require('echarts/lib/component/legend')
import { isExternal } from '@/utils/validate'
export default {
  name: 'CgChart',
  props: {
    width: [String,Number],
    height:[String,Number],
    extStyle:String,
    extClass:String,
    options: Object
  },
  data() {
    return {   
      echarts,
      instance: null
    }
  },
  watch: {
    options() {
      if (this.instance && this.options) this.instance.setOption(this.options, {
        notMerge: this.options.notMerge,
        lazyUpdate: this.options.lazyUpdate,
        silent: this.options.silent
      })
    }
  },
  beforeMount() {
    window.addEventListener('resize', this.resizeHandler, false)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.resizeHandler, false)
  },
  mounted() {
    this.instance = echarts.init(this.$el)
  },
  methods: {
    className() {
      if (this.extClass) return "cg-chart " + this.extClass
      else return "cg-chart"
    },
    styleContent() {
      let st = this.extStyle ? this.extStyle : ''
      if (this.width) st = st + '; width:' + this.width+(typeof this.width === 'number'?'px':'')
      if (this.height) st = st + '; height:' + this.height+(typeof this.height === 'number'?'px':'')
      return st
    },
    resizeHandler() {
      if (this.instance) this.instance.resize()
    },
  },
  destroyed() {
    if (this.instance) {
      echarts.dispose(this.instance)
      this.instance = null
    }
  }
}
</script>

<style scoped>
.cg-icon {
  vertical-align: middle;
  fill: currentColor;
  overflow: hidden;
}

.external-icon {
  background-color: currentColor;
  mask-size: cover!important;
  display: inline-block;
}
</style>
