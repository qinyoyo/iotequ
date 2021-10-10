<template>
  <div class="cg-form cg-form-ewUserTime">
    <el-dialog ref="dialog" v-el-drag-dialog :visible.sync="showDialog" top="0px" v-if="showDialog || !destroyOnClose"
               :class="'cg-form cg-form-ewUserTime'+(dialogClass?' '+dialogClass:'')" :close-on-click-modal="false"
               :append-to-body="true" :show-close="!mobile || isDetail" :before-close="beforeClose" @closed="close">
      <div slot="title" :class="titleColor">
        <cg-header :icon="icon" :content="content" :title="title"
                   :hasMenu="mobile && !isDetail" rightIcon = 'el-icon-check color-primary'
                   @menuAction="submit" @goBack="close"
        />
      </div>
      <CgFormEwUserTime ref="cgForm"
                        :routeParams="routeParams"
                        :showInDialog="showDialog"
                        @closeDialog="handleClose"
                        @openModeChanged="openModeChanged" />
    </el-dialog>
  </div>
</template>

<script>
import ParentForm from '@/views/common-views/components/dialog'
import CgFormEwUserTime from './CgFormEwUserTime'
const Comp = {
  name: 'EwUserTimeForm',
  components: { CgFormEwUserTime },
  mixins: [ParentForm],
  data() {
    return {
      destroyOnClose: true,
      allowAddRecord: false,
      path: 'record',
      generatorName: 'ewUserTime',
      baseUrl: '/ewallet/ewUserTime'
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
