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
      <CgFormAdjustApprove ref="cgForm"
                           :dialogParams="dialogParams"
                           :showInDialog="showDialog"
                           @closeDialog="handleClose"
                           @openModeChanged="openModeChanged" />
    </el-dialog>
  </div>
</template>

<script>
import ParentForm from '@/views/common-views/components/dialog'
import CgFormAdjustApprove from './CgFormAdjustApprove'
const mixins = [ParentForm]
const mixinContext = require.context('.', false, /approve-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default {
  name: 'AdjustApproveForm',
  components: { CgFormAdjustApprove },
  mixins,
  data() {
    return {
      isFlowRecord: true,
      defaultIcon: 'fa fa-address-card-o',
      path: this.openParams().flowAction ? this.openParams().flowAction : 'approve',
      generatorName: 'adAdjust',
      baseUrl: '/attendance/adjust/adAdjust'
    }
  }
}
</script>
