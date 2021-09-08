<template>
  <div class="cg-form cg-form-sysUser">
    <el-dialog ref="dialog" v-el-drag-dialog :visible.sync="showDialog" top="0px" :destroy-on-close="destroyOnClose"
               :class="'cg-form cg-form-sysUser'+(dialogClass?' '+dialogClass:'')" :close-on-click-modal="false"
               :append-to-body="true" :show-close="!mobile || isDetail" :before-close="beforeClose" @closed="close">
      <div slot="title" :class="titleColor">
        <cg-header :icon="icon" :content="content" :title="title"
                   :hasMenu="mobile && !isDetail" rightIcon = 'el-icon-check color-primary'
                   @menuAction="submit" @goBack="close"
        />
      </div>
      <CgFormRegister ref="cgForm"
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
      destroyOnClose: true,
      defaultIcon: 'fa fa-user-plus',
      path: 'register',
      generatorName: 'sysUser',
      baseUrl: '/framework/sysUser'
    }
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /register-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
  mixins
}
</script>
