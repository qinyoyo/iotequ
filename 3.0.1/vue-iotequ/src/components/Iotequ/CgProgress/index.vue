<template>
  <transition name="el-loading-fade">
    <div v-show="visible"
         :id="id"
         ref="loading"
         :style="{ backgroundColor: background }"
         :class="[customClass, 'el-loading-mask', 'is-fullscreen']"
    >
      <div class="el-loading-spinner">
        <svg class="circular" viewBox="25 25 50 50">
          <circle class="path" cx="50" cy="50" r="20" fill="none" />
        </svg>
        <el-progress :class="isPortrait()?'h-center-mobile':'h-center'" :text-inside="true" :stroke-width="24" :percentage="progress" status="success" />
      </div>
    </div>
  </transition>
</template>

<script>
import { addClass, removeClass, getStyle } from 'element-ui/src/utils/dom'
import { removeBodyNode } from '@/utils/dom'
export default {
  name: 'CgProgress',
  props: {
    background: {
      type: String,
      default: 'rgba(0, 0, 0, 0.6)'
    },
    id: {
      type: String,
      default: 'cg_progress_waiting'
    },
    customClass: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      progress: 0,
      visible: false
    }
  },
  mounted() {
    const target = document.body
    const instance = this.$refs.loading
    instance.originalPosition = getStyle(document.body, 'position')
    instance.originalOverflow = getStyle(document.body, 'overflow')
    addClass(target, 'el-loading-parent--relative')
    addClass(target, 'el-loading-parent--hidden')
    target.appendChild(instance)
    this.visible = true
  },
  methods: {
    close() {
      const target = document.body
      removeClass(target, 'el-loading-parent--relative')
      removeClass(target, 'el-loading-parent--hidden')
      removeBodyNode(this.id)
    },
    isPortrait() {
      return window.$vue.$store.state.app.device === 'mobile' && (window.orientation == 0 || window.orientation == 180)
    }
  }
}
</script>
<style scoped>
.h-center {
  left: 50%;
  margin-left: -200px;
  width: 400px;
}
.h-center-mobile {
  left: 0px;
  margin-left: 0px;
  width: 100%;
  padding: 0 50px;
}
</style>
