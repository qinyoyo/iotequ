<template>
  <div class="cg-list cg-list-adAdjust">
    <el-card shadow="hover">
      <div slot="header">
        <cg-header homeMenu :showMaster="getShowMaster()" icon="fa fa-handshake-o" :hasMenu="hasMenu()"
                   :title="title" :content="content" @goBack="goBack" @menuAction="showActionSheet" @showDetail="showDetail"
        />
      </div>
      <div v-if="mobile">
        <div v-show="showMaster" class="cg-father">
          <CgListAdjust ref="cgList" @detail="doShowDetail" @rowClick="rowClick" @refreshed="refreshed" :fixedQueryRecord="fixedQueryRecord" />
        </div>
        <el-tabs v-show="!showMaster" v-model="selectedSon" class="cg-child">
            <el-tab-pane :label="$t('system.action.flow')" name="cgList_son0" :style="{height: childHeight+'px'}">
              <CgFlow ref="cgList_son0" :url="baseUrl+'/list'" :height="childHeight" :queryById.sync="son0Condition" />
            </el-tab-pane>
        </el-tabs>
      </div>
      <div v-else class="components-container" :style="'height:'+clientHeight+'px'">
        <split-pane :default-percent="70" split="vertical">
          <template slot="paneL">
            <div class="cg-father">
              <CgListAdjust ref="cgList" @rowClick="rowClick" @refreshed="refreshed" :fixedQueryRecord="fixedQueryRecord" :height="fatherHeight" />
            </div>
          </template>
          <template slot="paneR">
            <el-tabs v-model="selectedSon" class="cg-child" type="border-card">
              <el-tab-pane :label="$t('system.action.flow')" name="cgList_son0" :style="{height: childHeight+'px'}">
                <CgFlow ref="cgList_son0" :url="baseUrl+'/list'" :height="childHeight" :queryById.sync="son0Condition" />
              </el-tab-pane>
            </el-tabs>
          </template>
        </split-pane>
      </div>
    </el-card>
  </div>
</template>

<script>
import CgListAdjust from './CgListAdjust.vue'
import ParentList from '@/views/common-views/components/list-sons'
const Comp = {
  name: 'AdjustList',
  components: { CgListAdjust },
  mixins: [ParentList],
  data() {
    return {
      sonCount: 1,
      son0Condition: 'null',
      sonPkFields: ['id'],
      path: 'list',
      generatorName: 'adAdjust',
      baseUrl: '/attendance/adjust/adAdjust'
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
