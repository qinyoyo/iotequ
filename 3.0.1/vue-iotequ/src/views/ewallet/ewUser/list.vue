<template>
  <div class="cg-list">
    <el-card shadow="hover">
      <div slot="header">
        <cg-header homeMenu :showMaster="getShowMaster()" icon="el-icon-s-operation" :hasMenu="hasMenu()"
                   :title="title" :content="content" @goBack="goBack" @menuAction="showActionSheet" @showDetail="showDetail"
        />
      </div>
      <div v-if="mobile">
        <div v-show="showMaster" class="cg-father">
          <CgListEwUser ref="cgList" @detail="doShowDetail" @rowClick="rowClick" @refreshed="refreshed" :fixedQueryRecord="fixedQueryRecord" />
        </div>
        <el-tabs v-show="!showMaster" v-model="selectedSon" class="cg-child">
            <el-tab-pane :label="$t('ewUserCount.title.list')" name="cgList_son0" :style="{height: childHeight+'px'}">
              <CgListEwUserCount ref="cgList_son0" :height="childHeight" :fixedQueryRecord.sync="son0Condition" />
            </el-tab-pane>
            <el-tab-pane :label="$t('ewUserTime.title.list')" name="cgList_son1" :style="{height: childHeight+'px'}">
              <CgListEwUserTime ref="cgList_son1" :height="childHeight" :fixedQueryRecord.sync="son1Condition" />
            </el-tab-pane>
        </el-tabs>
      </div>
      <div v-else class="components-container" :style="'height:'+clientHeight+'px'">
        <split-pane :default-percent="50" split="horizontal" @resize="resize">
          <template slot="paneL">
            <div class="cg-father">
              <CgListEwUser ref="cgList" @rowClick="rowClick" @refreshed="refreshed" :fixedQueryRecord="fixedQueryRecord" :height="fatherHeight" />
            </div>
          </template>
          <template slot="paneR">
            <el-tabs v-model="selectedSon" class="cg-child" type="border-card">
              <el-tab-pane :label="$t('ewUserCount.title.list')" name="cgList_son0" :style="{height: childHeight+'px'}">
                <CgListEwUserCount ref="cgList_son0" :height="childHeight" :fixedQueryRecord.sync="son0Condition" />
              </el-tab-pane>
              <el-tab-pane :label="$t('ewUserTime.title.list')" name="cgList_son1" :style="{height: childHeight+'px'}">
                <CgListEwUserTime ref="cgList_son1" :height="childHeight" :fixedQueryRecord.sync="son1Condition" />
              </el-tab-pane>
            </el-tabs>
          </template>
        </split-pane>
      </div>
    </el-card>
  </div>
</template>

<script>
import CgListEwUser from './CgListEwUser.vue'
import ParentList from '@/views/common-views/components/list-sons'
import CgListEwUserCount from '@/views/ewallet/ewUserCount/CgListEwUserCount.vue'
import CgListEwUserTime from '@/views/ewallet/ewUserTime/CgListEwUserTime.vue'
const Comp = {
  name: 'EwUserList',
  components: { CgListEwUser, CgListEwUserCount, CgListEwUserTime },
  mixins: [ParentList],
  data() {
    return {
      fatherHeightPercent: 50,
      titleField: 'name',
      sonCount: 2,
      son0Condition: {
        userNo: 'null'
      },
      son1Condition: {
        userNo: 'null'
      },
      sonPkFields: ['userNo', 'userNo'],
      path: 'list',
      generatorName: 'ewUser',
      baseUrl: '/ewallet/ewUser'
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
