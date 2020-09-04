<template>
  <!--判断scopedSlots.default可知道是否存在子元素-->
  <el-table-column
    v-if="$scopedSlots.default"
    :key="$attrs.label"
    v-bind="$attrs"
    :class-name="className"
    :width="autoWidth"
  >
    <template slot-scope="scope">
      <!--原element组件使用了作用域插槽, 这里仿照-->
      <slot v-bind="scope" />
    </template>
  </el-table-column>
  <!--使用 slot 自定义 header-->
  <el-table-column
    v-else-if="$scopedSlots.header"
    :key="$attrs.label"
    v-bind="$attrs"
    :class-name="className"
    :width="autoWidth"
  >
    <template slot="header" slot-scope="scope">
      <slot name="header" v-bind="scope" />
    </template>
  </el-table-column>
  <el-table-column
    v-else
    :key="$attrs.label"
    v-bind="$attrs"
    :class-name="className"
    :width="autoWidth"
  />
</template>

<script>
import { getTextWidth } from '@/utils/cg'
export default {
  name: 'CgTableColumn',
  props: {
    page: {
      Type: Number,
      default : 1
    }
  },
  computed: {
    labelWidth() {
      if (!this.$attrs.label) return undefined
      return getTextWidth(this.$attrs.label) + (this.$attrs.sortable === undefined ? 36 : 56)
    },
    values() {
      const data = this.$parent.data || []
      return data.map(item => item[this.$attrs.prop])
    },
    className() {
      return this.$attrs.prop + '-cg-table-column'
    }
  },
  watch: {
    values: {
      deep: true,
      handler(val) {
        if (this.page > 1) return // 效率问题，只处理第一页前 10 行
        const that = this
        this.$nextTick(() => {
          if (this.$scopedSlots.default) {
            setTimeout(() => {
              const bodyWrapper = that.$parent.$el
              const nodes = bodyWrapper.querySelectorAll('.' + that.className)
              if (nodes.length) {
                const target = []
                let maxSize = 0
                for (let i=0; i<10 && i<nodes.length; i++) {
                  target.push(nodes[i].innerText)
                }
                that.autoWidth = Math.max(that.autoWidth ? that.autoWidth : that.labelWidth, that.getMaxLength(target) + 36)
              } else this.autoWidth = Math.max(that.autoWidth ? that.autoWidth : that.labelWidth, that.labelWidth)
            }, 0)
          } else {
            that.autoWidth = Math.max(that.autoWidth ? that.autoWidth : that.labelWidth, that.getMaxLength(val) + 36)
          }
        })
      }
    }
  },
  data() {
    return {
      autoWidth: this.labelWidth
    }
  },
  methods: {
    getMaxLength(arr) {
      return arr.reduce((length, item) => {
        if (item) return Math.max(getTextWidth(item), length)
        else return length
      }, 0)
    }
  }
}
</script>
