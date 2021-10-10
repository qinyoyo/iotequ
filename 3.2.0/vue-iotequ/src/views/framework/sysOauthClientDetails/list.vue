<template>
  <div class="cg-list cg-list-sysOauthClientDetails">
    <el-card shadow="hover">
      <div slot="header">
        <cg-header homeMenu icon="el-icon-s-operation" :hasMenu="hasMenu()"
                   :title="title" :content="content" @goBack="goBack" @menuAction="showActionSheet"
        />
      </div>
      <CgListOauthClientDetails ref="cgList" :height="clientHeight" @detail="doShowDetail" :fixedQueryRecord="fixedQueryRecord"/>
    </el-card>
  </div>
</template>

<script>
import CgListOauthClientDetails from './CgListOauthClientDetails.vue'
import ParentList from '@/views/common-views/components/list'
const Comp = {
  name: 'OauthClientDetailsList',
  components: { CgListOauthClientDetails },
  mixins: [ParentList],
  data() {
    return {
      path: 'list',
      generatorName: 'sysOauthClientDetails',
      baseUrl: '/framework/sysOauthClientDetails'
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
