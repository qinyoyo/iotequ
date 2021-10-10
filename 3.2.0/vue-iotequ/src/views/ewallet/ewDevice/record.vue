<template>
  <div class="cg-form cg-form-ewDevice">
    <el-dialog ref="dialog" v-el-drag-dialog :visible.sync="showDialog" top="0px" v-if="showDialog || !destroyOnClose"
               :class="'cg-form cg-form-ewDevice'+(dialogClass?' '+dialogClass:'')" :close-on-click-modal="false"
               :append-to-body="true" :show-close="!mobile || isDetail" :before-close="beforeClose" @closed="close">
      <div slot="title" :class="titleColor">
        <cg-header :icon="icon" :content="content" :title="title"
                   :hasMenu="mobile && !isDetail" rightIcon = 'el-icon-check color-primary'
                   @menuAction="submit" @goBack="close"
        />
      </div>
      <CgFormEwDevice ref="cgForm"
                      :routeParams="routeParams"
                      :showInDialog="showDialog"
                      @closeDialog="handleClose"
                      @openModeChanged="openModeChanged" />
    </el-dialog>
  </div>
</template>

<script>
import ParentForm from '@/views/common-views/components/dialog'
import CgFormEwDevice from './CgFormEwDevice'
const Comp = {
  name: 'EwDeviceForm',
  components: { CgFormEwDevice },
  mixins: [ParentForm],
  data() {
    return {
      destroyOnClose: true,
      path: 'record',
      generatorName: 'ewDevice',
      baseUrl: '/ewallet/ewDevice'
    }
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /record-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
  mixins
}
</script>
