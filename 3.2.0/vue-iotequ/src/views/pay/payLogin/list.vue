<template>
  <div class="cg-list cg-list-payLogin">
    <el-card shadow="hover">
      <div slot="header">
        <cg-header homeMenu icon="el-icon-s-operation" :hasMenu="hasMenu()"
                   :title="title" :content="content" @goBack="goBack" @menuAction="showActionSheet"
        />
      </div>
      <CgListPayLogin ref="cgList" :height="clientHeight" @detail="doShowDetail" :fixedQueryRecord="fixedQueryRecord"/>
    </el-card>
  </div>
</template>

<script>
import CgListPayLogin from './CgListPayLogin.vue'
import ParentList from '@/views/common-views/components/list'
const Comp = {
  name: 'PayLoginList',
  components: { CgListPayLogin },
  mixins: [ParentList],
  data() {
    return {
      path: 'list',
      generatorName: 'payLogin',
      baseUrl: '/pay/payLogin'
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
