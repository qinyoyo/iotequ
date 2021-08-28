<template>
  <div class="cg-list cg-list-devReaderGroup">
    <el-card shadow="hover">
      <div slot="header">
        <cg-header homeMenu :showMaster="getShowMaster()" icon="el-icon-s-operation" :hasMenu="hasMenu()"
                   :title="title" :content="content" @goBack="goBack" @menuAction="showActionSheet" @showDetail="showDetail"
        />
      </div>
      <div v-if="mobile">
        <div v-show="showMaster" class="cg-father">
          <CgListDevReaderGroup ref="cgList" @detail="doShowDetail" @rowClick="rowClick" @refreshed="refreshed" :fixedQueryRecord="fixedQueryRecord" />
        </div>
        <el-tabs v-show="!showMaster" v-model="selectedSon" class="cg-child">
            <el-tab-pane :label="$t('devReader.title.list')" name="cgList_son0" :style="{height: childHeight+'px'}">
              <CgListDevReader ref="cgList_son0" :height="childHeight" :fixedQueryRecord.sync="son0Condition" />
            </el-tab-pane>
        </el-tabs>
      </div>
      <div v-else class="components-container" :style="'height:'+clientHeight+'px'">
        <split-pane :default-percent="50" split="vertical">
          <template slot="paneL">
            <div class="cg-father">
              <CgListDevReaderGroup ref="cgList" @rowClick="rowClick" @refreshed="refreshed" :fixedQueryRecord="fixedQueryRecord" :height="fatherHeight" />
            </div>
          </template>
          <template slot="paneR">
            <el-tabs v-model="selectedSon" class="cg-child" type="border-card">
              <el-tab-pane :label="$t('devReader.title.list')" name="cgList_son0" :style="{height: childHeight+'px'}">
                <CgListDevReader ref="cgList_son0" :height="childHeight" :fixedQueryRecord.sync="son0Condition" />
              </el-tab-pane>
            </el-tabs>
          </template>
        </split-pane>
      </div>
    </el-card>
  </div>
</template>

<script>
import CgListDevReaderGroup from './CgListDevReaderGroup.vue'
import ParentList from '@/views/common-views/components/list-sons'
import CgListDevReader from '@/views/reader/devReader/CgListDevReader.vue'
const Comp = {
  name: 'DevReaderGroupList',
  components: { CgListDevReaderGroup, CgListDevReader },
  mixins: [ParentList],
  data() {
    return {
      titleField: 'name',
      sonCount: 1,
      son0Condition: {
        readerGroup: 'null'
      },
      sonPkFields: ['id'],
      path: 'list',
      generatorName: 'devReaderGroup',
      baseUrl: '/reader/devReaderGroup'
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
