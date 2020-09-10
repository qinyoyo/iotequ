<template>
  <div class="cg-list">
    <el-card shadow="hover">
      <div slot="header">
        <cg-header homeMenu icon="el-icon-s-operation" :hasMenu="hasMenu()"
                   :title="title" :content="content" @goBack="goBack" @menuAction="showActionSheet"
        />
      </div>
      <CgListAdEmployee ref="cgList" :height="clientHeight" @detail="doShowDetail" :fixedQueryRecord="fixedQueryRecord" @rowClick="rowClick"/>
    </el-card>
  </div>
</template>

<script>
import cg from '@/utils/cg'
import cgList from '@/utils/cgList'
import CgListAdEmployee from './CgListAdEmployee.vue'
const mixins = []
const mixinContext = require.context('.', false, /list-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default {
  name: 'AdEmployeeList',
  components: { CgListAdEmployee },
  mixins,
  props: {
    fixedQueryRecord: {
      type: Object,
      default: () => { return {} }
    },
  },
  data() {
    return {
      clientHeight: this.height ? this.height : cg.containerHeight(),
      fatherHeight: (this.height ? this.height : cg.containerHeight()),
      childHeight: (this.height?this.height:cg.containerHeight())-70,
      contentSubTitle: '',
      path: 'list',
      generatorName: 'adEmployee',
      baseUrl: '/attendance/employee/adEmployee'
    }
  },
  created() {
    this.$route.query && this.$route.query.fixedQueryRecord && (this.fixedQueryRecord = Object.assign(this.fixedQueryRecord,this.$route.query.fixedQueryRecord))
  },
  computed: {
    mobile() {
      return this.$store.state.app.device === 'mobile'
    },
    title() {
      return this.$t('system.action.list')
    },
    content() {
      return this.$t('adEmployee.title.list') + (!this.mobile && this.contentSubTitle?' - '+this.contentSubTitle:'')
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
      this.contentSubTitle = row && row.realName ? String(row.realName).local() : ''
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
