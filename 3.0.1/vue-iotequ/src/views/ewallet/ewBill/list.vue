<template>
  <div class="cg-list">
    <el-card shadow="hover">
      <div slot="header">
        <cg-header homeMenu icon="el-icon-s-operation" :hasMenu="hasMenu()"
                   :title="title" :content="content" @goBack="goBack" @menuAction="showActionSheet"
        />
      </div>
      <CgListEwBill ref="cgList" :height="clientHeight" @detail="doShowDetail" @rowClick="rowClick"/>
    </el-card>
  </div>
</template>

<script>
import cg from '@/utils/cg'
import cgList from '@/utils/cgList'
import CgListEwBill from './CgListEwBill.vue'
const mixins = []
const mixinContext = require.context('.', false, /list-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default {
  name: 'EwBillList',
  components: { CgListEwBill },
  mixins,
  data() {
    return {
      clientHeight: this.height ? this.height : cg.containerHeight(),
      fatherHeight: (this.height ? this.height : cg.containerHeight()),
      childHeight: (this.height?this.height:cg.containerHeight())-70,
      contentSubTitle: '',
      path: 'list',
      generatorName: 'ewBill',
      baseUrl: '/ewallet/ewBill'
    }
  },
  watch: {
    fixedQueryRecord: {
      handler(n, o) {
        if (n && Object.keys(n).length > 0 && this.$refs.cgList && typeof this.$refs.cgList.doAction === 'function') this.$refs.cgList.doAction('refresh')
      },
      deep: true,
      immediate: true
    }
  },
  computed: {
    mobile() {
      return this.$store.state.app.device === 'mobile'
    },
    title() {
      return this.$t('system.action.list')
    },
    content() {
      return this.$t('ewBill.title.list') + (!this.mobile && this.contentSubTitle?' - '+this.contentSubTitle:'')
    }
  },
  methods: {
    goBack() {
      if (this.mobile) cg.goBack()
    },
    hasMenu() {
      return this.mobile
    },
    showActionSheet() {
      this.$refs.cgList.showActionSheet()
    },
    rowClick({ row, column, event }) {
      this.contentSubTitle = row && row.userNo ? String(row.userNo).local() : ''
    },
    refreshed(listObject) {
      const row = cgList.list_getCurrentRow(listObject)
      this.setChildrenParams(row)
    },
    doShowDetail(row) {
      if (!row) row = cgList.list_getCurrentRow(this.$refs.cgList)
      cgList.list_rowDblclick(this.$refs.cgList, { row })
    },
  }
}
</script>
