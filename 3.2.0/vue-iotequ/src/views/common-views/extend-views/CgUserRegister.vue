<template>
  <div class="cg-form">
    <el-dialog v-el-drag-dialog :visible.sync="showDialog" top="0px" :close-on-click-modal="false" destroy-on-close :append-to-body="true" @closed="close">
      <div slot="title" :class="titleColor">
        <cg-header icon="fa fa-user-plus"
                   :title="(isNew?$t('system.action.new'):(isDetail?$t('system.action.view'):$t('system.action.edit')))" :content="$t('sysUser.title.register')" @goBack="close"
        />
      </div>
      <CgFormRegister ref="cgForm"
                      :showInDialog="showDialog"
                      @submitted="registered"
                      @closeDialog="close"
                      @openModeChanged="openModeChanged" />
    </el-dialog>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import CgFormRegister from '@/views/framework/sysUser/CgFormRegister'
const mixins = []
const mixinContext = require.context('.', false, /register-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default {
  name: 'CgUserRegister',
  directives: { elDragDialog },
  components: { CgFormRegister },
  mixins,
  data() {
    return {
      path: '/register',
      openMode: 'new',
      showDialog: true,
      generatorName: 'sysUser',
      baseUrl: '/login/register'
    }
  },
  computed: {
    mobile() {
      return this.$store.state.app.device === 'mobile'
    },
    isDetail() {
      return this.openMode === 'detail' || this.openMode === 'view'
    },
    isNew() {
      return !this.openMode || this.openMode === 'new' || this.openMode === 'add'
    },
    isEdit() {
      return this.openMode === 'edit' || this.openMode === 'modify'
    },
    titleColor() {
      if (this.isNew) return 'color-danger'
      else if (this.isEdit) return 'color-warning'
      else return 'color-info'
    }
  },
  methods: {
    close() {
      this.showDialog = false
      this.$store.dispatch('tagsView/activeLastAfterRemove', this.$route)
    },
    openModeChanged(v) {
      this.openMode = v
    },
    registered(formObject) {
      const _this = this
      _this.loading = true
      const submitData = {
        'remember-me': true,
         locale: _this.$store.getters.language,
         isSmsRandCode: true,
         randCode: formObject.record.randCode,
         userName: formObject.record.mobilePhone
      }
      try {
        _this.$store.dispatch('user/login', submitData).then(() => {
          _this.$store.dispatch('user/getInfo').then(() => {
            _this.$router.push('/')
          }).catch((error) => {})
        }).catch((error) => {})
      } catch (error) {}
    }
  }
}
</script>
