<template>
  <div class="cg-form" :class="dialogClass">
    <el-dialog ref="dialog" v-el-drag-dialog :visible.sync="showDialog" top="0px" :class="dialogClass" :close-on-click-modal="false"
               :append-to-body="true" :show-close="!mobile || isDetail" :before-close="beforeClose" @closed="close" width="640px">
      <div slot="title" :class="titleColor">
        <cg-header :icon="icon" :content="content" :title="title"
                   :hasMenu="mobile && !isDetail" rightIcon = 'el-icon-check color-primary'
                   @menuAction="submit" @goBack="close"
        />
      </div>
      <img class="img-center" src="/static/img/input.gif" alt="" />
      <CgFormRegister ref="cgForm" class="hide-cg-buttons"
                      :routeParams="routeParams"
                      :showInDialog="showDialog"
                      @closeDialog="handleClose"
                      @openModeChanged="openModeChanged" />
    </el-dialog>
  </div>
</template>

<script>
import ParentForm from '@/views/common-views/components/dialog'
import CgFormRegister from './CgFormRegister'
const Comp = {
  name: 'RegisterForm',
  components: { CgFormRegister },
  mixins: [ParentForm],
  data() {
    return {
      path: 'record',
      generatorName: 'ckRegister',
      baseUrl: '/check-in/ckRegister'
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
