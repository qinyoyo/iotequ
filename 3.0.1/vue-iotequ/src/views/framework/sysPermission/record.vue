<template>
  <div class="cg-form">
    <el-card shadow="hover">
      <div slot="header" :class="titleColor">
        <cg-header hoemMenu :icon="icon" :content="content" :title="title"
                   :hasMenu="mobile && !isDetail" rightIcon = 'el-icon-check color-primary'
                   @menuAction="submit" @goBack="goBack"
        />
      </div>
      <CgFormPermission ref="cgForm"
                        @openModeChanged="openModeChanged" />
    </el-card>
  </div>
</template>

<script>
import ParentForm from '@/views/common-views/components/record'
import CgFormPermission from './CgFormPermission'
const Comp = {
  name: 'PermissionForm',
  components: { CgFormPermission },
  mixins: [ParentForm],
  data() {
    return {
      allowAddRecord: false,
      path: 'record',
      generatorName: 'sysPermission',
      baseUrl: '/framework/sysPermission'
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
