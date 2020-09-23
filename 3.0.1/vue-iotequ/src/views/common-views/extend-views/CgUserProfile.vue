<template>
  <div class="cg-form">
    <el-dialog v-el-drag-dialog :visible.sync="showDialog" top="0px" :close-on-click-modal="false" 
               :class="dialogClass" :append-to-body="true" @closed="close">
      <div slot="title" class="color-info">
        <cg-header icon="fa fa-user-plus"
                    :content="$t('system.layout.profile')" @goBack="close"
        />
      </div>
      <CgFormUser ref="cgForm" :queryById="userId" 
                      :showInDialog="showDialog"
                      @created="created"
                      @submitted="submitted"
                      @closeDialog="close"/>
    </el-dialog>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import CgFormUser from '@/views/framework/sysUser/CgFormUser'
export default {
  name: 'CgUserProfile',
  directives: { elDragDialog },
  components: { CgFormUser },
  props: {
    routeParams: {
      type: Object,
      default: null
    },
    dialogClass: {
      type: String,
      default: null
    }
  },  
  data() {
    return {
      path: '/profile',
      openMode: 'edit',
      showDialog: true,
      mobile: this.$store.state.app.device === 'mobile',
      baseUrl: '/login/profile'
    }
  },
  computed: {
    userId() {
      return this.$store.state.user.id
    }
  },
  methods: {
    created(formObject) {
      formObject.baseUrl = '/login/profile'
    },
    submitted() {
      this.$store.dispatch('user/getInfo',this.$refs.cgForm.record)
    },
    close() {
      this.showDialog = false
      this.$emit('close')
      if (!this.routeParams) this.$store.dispatch('tagsView/activeLastAfterRemove', this.$route)
    }
  }
}
</script>
