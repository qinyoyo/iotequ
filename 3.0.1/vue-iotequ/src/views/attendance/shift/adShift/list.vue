<template>
  <div class="cg-list cg-list-adShift">
    <el-card shadow="hover">
      <div slot="header">
        <cg-header homeMenu :showMaster="getShowMaster()" icon="el-icon-s-operation" :hasMenu="hasMenu()"
                   :title="title" :content="content" @goBack="goBack" @menuAction="showActionSheet" @showDetail="showDetail"
        />
      </div>
      <div v-if="mobile">
        <div v-show="showMaster" class="cg-father">
          <CgListAdShift ref="cgList" @detail="doShowDetail" @rowClick="rowClick" @refreshed="refreshed" :fixedQueryRecord="fixedQueryRecord" />
        </div>
        <el-tabs v-show="!showMaster" v-model="selectedSon" class="cg-child">
            <el-tab-pane :label="$t('adShiftTime.title.list')" name="cgList_son0" :style="{height: childHeight+'px'}">
              <CgListAdShiftTime ref="cgList_son0" :height="childHeight" :fixedQueryRecord.sync="son0Condition" />
            </el-tab-pane>
            <el-tab-pane :label="$t('adException.title.list')" name="cgList_son1" :style="{height: childHeight+'px'}">
              <CgListAdException ref="cgList_son1" :height="childHeight" :fixedQueryRecord.sync="son1Condition" />
            </el-tab-pane>
        </el-tabs>
      </div>
      <div v-else class="components-container" :style="'height:'+clientHeight+'px'">
        <split-pane :default-percent="50" split="horizontal" @resize="resize">
          <template slot="paneL">
            <div class="cg-father">
              <CgListAdShift ref="cgList" @rowClick="rowClick" @refreshed="refreshed" :fixedQueryRecord="fixedQueryRecord" :height="fatherHeight" />
            </div>
          </template>
          <template slot="paneR">
            <el-tabs v-model="selectedSon" class="cg-child" type="border-card">
              <el-tab-pane :label="$t('adShiftTime.title.list')" name="cgList_son0" :style="{height: childHeight+'px'}">
                <CgListAdShiftTime ref="cgList_son0" :height="childHeight" :fixedQueryRecord.sync="son0Condition" />
              </el-tab-pane>
              <el-tab-pane :label="$t('adException.title.list')" name="cgList_son1" :style="{height: childHeight+'px'}">
                <CgListAdException ref="cgList_son1" :height="childHeight" :fixedQueryRecord.sync="son1Condition" />
              </el-tab-pane>
            </el-tabs>
          </template>
        </split-pane>
      </div>
    </el-card>
  </div>
</template>

<script>
import CgListAdShift from './CgListAdShift.vue'
import ParentList from '@/views/common-views/components/list-sons'
import CgListAdShiftTime from '@/views/attendance/shifttime/adShiftTime/CgListAdShiftTime.vue'
import CgListAdException from '@/views/attendance/exception/adException/CgListAdException.vue'
const Comp = {
  name: 'AdShiftList',
  components: { CgListAdShift, CgListAdShiftTime, CgListAdException },
  mixins: [ParentList],
  data() {
    return {
      fatherHeightPercent: 50,
      titleField: 'name',
      sonCount: 2,
      son0Condition: {
        shiftId: 'null'
      },
      son1Condition: {
        shiftId: 'null'
      },
      sonPkFields: ['id', 'id'],
      path: 'list',
      generatorName: 'adShift',
      baseUrl: '/attendance/shift/adShift'
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
