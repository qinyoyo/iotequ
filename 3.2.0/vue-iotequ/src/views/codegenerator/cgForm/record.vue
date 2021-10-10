<template>
  <div class="cg-form cg-form-cgForm">
    <el-card shadow="hover">
      <div slot="header" :class="titleColor">
        <cg-header hoemMenu :icon="icon" :content="content" :title="title"
                   :hasMenu="mobile && !isDetail" rightIcon = 'el-icon-check color-primary'
                   @menuAction="submit" @goBack="goBack"
        />
      </div>
      <CgFormCgForm ref="cgForm"
                    :routeParams="routeParams"
                    @openModeChanged="openModeChanged" />
    </el-card>
  </div>
</template>

<script>
import ParentForm from '@/views/common-views/components/record'
import CgFormCgForm from './CgFormCgForm'
const Comp = {
  name: 'CgFormForm',
  components: { CgFormCgForm },
  mixins: [ParentForm],
  data() {
    return {
      path: 'record',
      generatorName: 'cgForm',
      baseUrl: '/codegenerator/cgForm'
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
