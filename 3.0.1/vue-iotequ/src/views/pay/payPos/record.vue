<template>
  <div class="cg-form cg-form-payPos">
    <el-dialog ref="dialog" v-el-drag-dialog :visible.sync="showDialog" top="0px" v-if="showDialog || !destroyOnClose"
               :class="'cg-form cg-form-payPos'+(dialogClass?' '+dialogClass:'')" :close-on-click-modal="false"
               :append-to-body="true" :show-close="!mobile || isDetail" :before-close="beforeClose" @closed="close">
      <div slot="title" :class="titleColor">
        <cg-header :icon="icon" :content="content" :title="title"
                   :hasMenu="mobile && !isDetail" rightIcon = 'el-icon-check color-primary'
                   @menuAction="submit" @goBack="close"
        />
      </div>
      <CgFormPayPos ref="cgForm"
                    :routeParams="routeParams"
                    :showInDialog="showDialog"
                    @closeDialog="handleClose"
                    @openModeChanged="openModeChanged" />
    </el-dialog>
  </div>
</template>

<script>
import ParentForm from '@/views/common-views/components/dialog'
import CgFormPayPos from './CgFormPayPos'
const Comp = {
  name: 'PayPosForm',
  components: { CgFormPayPos },
  mixins: [ParentForm],
  data() {
    return {
      destroyOnClose: true,
      path: 'record',
      generatorName: 'payPos',
      baseUrl: '/pay/payPos'
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
