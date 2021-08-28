<template>
  <div class="cg-list cg-list-ewDevice">
    <el-card shadow="hover">
      <div slot="header">
        <cg-header homeMenu icon="el-icon-s-operation" :hasMenu="hasMenu()"
                   :title="title" :content="content" @goBack="goBack" @menuAction="showActionSheet"
        />
      </div>
      <CgListEwDevice ref="cgList" :height="clientHeight" @detail="doShowDetail" :fixedQueryRecord="fixedQueryRecord"/>
    </el-card>
  </div>
</template>

<script>
import CgListEwDevice from './CgListEwDevice.vue'
import ParentList from '@/views/common-views/components/list'
const Comp = {
  name: 'EwDeviceList',
  components: { CgListEwDevice },
  mixins: [ParentList],
  data() {
    return {
      path: 'list',
      generatorName: 'ewDevice',
      baseUrl: '/ewallet/ewDevice'
    }
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /list-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
  mixins
}
</script>
