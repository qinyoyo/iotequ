<template>
<div>
  <div class="cg-context-menu">
    <el-menu ref="menu" :style="style" v-show="show" :default-active="activeIndex" mode="vertical" @select="handleSelect">
      <el-menu-item v-for="(item,index) in actions" :key="index" :index="(item.prefix?item.prefix:'')+item.action" class="cg-context-menu-item"> 
          <i :class="item.icon+' fa-fw'"></i>
          <span>{{item.title.local()}}</span>
      </el-menu-item>
    </el-menu>
  </div>
</div>
</template>
<script>
import { getTextWidth } from '@/utils/cg'
export default {
  name: 'CgContextMenu',
  props: {
    actions: {
      type: Array,
      default: ()=>[]
    },
    left: {
      type: Number,
      required: true
    },
    top: {
      type: Number,
      required: true
    },
    show: {
      type: Boolean,
      required: false
    }
  },
  data() {
    return {
      activeIndex: '0',
      mobile: this.$store.state.app.device === 'mobile',
      showActionView: this.show
    }
  },
  watch: {
    show(value) {
      if (value) window.addEventListener('click', this.hideContextMenu, false)
      else window.removeEventListener('click', this.hideContextMenu, false)
    },
    showActionView(value) {
      if (!value) this.hideContextMenu()
    }
  },
  computed: {
    style() {
      const height = 40 * this.actions.length
      let width = 32
      this.actions.forEach(a => {
        let w = getTextWidth(a.title.local())
        if (w > width) width = w
      })
      width = (60 + width)
      const top = (this.top + height < document.documentElement.clientHeight ? this.top : this.top - height) + 'px'
      const left =  (this.left + width < document.documentElement.clientWidth ? this.left : document.documentElement.clientWidth - width -10) + 'px'
      return {top, left, width : width + 'px' }
    }
  },
  methods: {
    handleSelect(action) {
      this.$emit('select',action)
    },
    hideContextMenu(evt) {
      this.$emit('hide')
    },
  }
}
</script>
<style scoped>
  i {
    width: 40px!important;
  }
  .cg-context-menu-item {
    height: 40px;
    line-height: 40px;
    padding: 0!important;
  }
  .el-menu {
    border: solid 1px #e6e6e6;
    z-index: 9999;
    width: 200px;
    list-style: none;
    position: fixed;
    margin: 0;
    padding-left: 0;
    background-color: #FFFFFF;
  }
  </style>