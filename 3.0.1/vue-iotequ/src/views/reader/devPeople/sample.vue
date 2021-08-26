<template>
  <div class="cg-form" :class="dialogClass">
    <el-dialog ref="dialog" v-el-drag-dialog :visible.sync="showDialog" top="0px" :class="dialogClass" :close-on-click-modal="false"
               :append-to-body="true" :show-close="!mobile || isDetail" :before-close="beforeClose" @closed="close">
      <div slot="title" :class="titleColor">
        <cg-header :icon="icon" :content="content" :title="title"
                   :hasMenu="mobile && !isDetail" rightIcon = 'el-icon-check color-primary'
                   @menuAction="submit" @goBack="close"
        />
      </div>
      <CgFormDevVeinSample ref="cgForm"
                           :routeParams="routeParams"
                           :showInDialog="showDialog"
                           @closeDialog="handleClose"
                           @openModeChanged="openModeChanged" />
    </el-dialog>
  </div>
</template>

<script>
import ParentForm from '@/views/common-views/components/dialog'
import CgFormDevVeinSample from './CgFormDevVeinSample'
const Comp = {
  name: 'DevVeinSampleForm',
  components: { CgFormDevVeinSample },
  mixins: [ParentForm],
  data() {
    return {
      defaultIcon: 'fa fa-eyedropper',
      path: 'sample',
      generatorName: 'devPeople',
      baseUrl: '/reader/devPeople'
    }
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /sample-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
  mixins
}
</script>
