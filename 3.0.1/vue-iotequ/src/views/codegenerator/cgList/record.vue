<template>
  <div class="cg-form cg-form-cgList">
    <el-card shadow="hover">
      <div slot="header" :class="titleColor">
        <cg-header hoemMenu :icon="icon" :content="content" :title="title"
                   :hasMenu="mobile && !isDetail" rightIcon = 'el-icon-check color-primary'
                   @menuAction="submit" @goBack="goBack"
        />
      </div>
      <CgFormCgList ref="cgForm"
                    :routeParams="routeParams"
                    @openModeChanged="openModeChanged" />
    </el-card>
  </div>
</template>

<script>
import ParentForm from '@/views/common-views/components/record'
import CgFormCgList from './CgFormCgList'
const Comp = {
  name: 'CgListForm',
  components: { CgFormCgList },
  mixins: [ParentForm],
  data() {
    return {
      path: 'record',
      generatorName: 'cgList',
      baseUrl: '/codegenerator/cgList'
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
