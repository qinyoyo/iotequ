<template>
  <div class="cg-form">
    <el-form ref="cgForm" :model="record" v-loading="recordLoading" :class="className" label-position="top" label-suffix=":" :size="$store.state.app.size"
             hide-required-asterisk class="cg-no-border">
      <el-tree ref="actionTree" :data="dictionary.actions" :props="defaultProps" accordion node-key="value" show-checkbox 
        @check-change="checkChange" />
    </el-form>
    <div v-if="!isDetail">
      <el-divider></el-divider>
      <el-button class="cg-button" type="primary" plain :disabled="!recordChanged"
               @click.native="submit()" icon="el-icon-check">
               {{ $t('system.action.save') }}
      </el-button>
      <el-button v-if="showInDialog" class="cg-button" plain
                 icon="el-icon-close" @click.native="$emit('closeDialog')">
        {{ $t('system.action.close') }}
      </el-button>
    </div>
  </div>
</template>

<script>
  import cg from '@/utils/cg'
  import cgForm from '@/utils/cgForm'
  import time from '@/utils/time'
  const myContext = require.context('.', false, /CgFormPermissionTree\.js$/)
  const myExtendsUtils = myContext.keys().map(myContext)
  const userExtends = (myExtendsUtils && myExtendsUtils.length > 0 ? myExtendsUtils[0].default : null)
  export default {
    name: 'CgFormPermissionTree',
    props: Object.assign({
      showInDialog: {
        type: Boolean,
        default: false
      },
      openID: {
        type: String,
        default: ''
      },
      queryById: [Number, String]
    }, userExtends ? userExtends.props : null),
    data() {
      return Object.assign({
        rules: Object.assign(userExtends ? userExtends.rules : {}, {
        }),
        defaultProps: {
          label: 'text',
          children: 'nodes'
        },
        idField: 'id',
        recordChanged: false,
        refreshList: this.$route.query.refreshList,
        recordLoading: false ,
        openMode: this.$route.query.openMode ? this.$route.query.openMode : null,
        record: this.$route.query.record ? this.$route.query.record : {} ,
        idSaved: this.$route.query.record ? this.$route.query.record.id : null,
        needDefaultFromServer: false,
        dictionary: this.$route.query.dictionary ? this.$route.query.dictionary : {},
        needLoadDictionary: (this.$route.query.dictionary ? false : true),
        generatorName: 'sysPermissionTree',
        baseUrl: '/framework/sysPermissionTree'
      }, userExtends ? userExtends.data : null)
    },
    computed: Object.assign({
      className() {
        return 'cg-form-permissionform' + (this.openID ? ('-'+this.openID) : '')
      },
      isDetail() {
        return this.openMode == 'detail' || this.openMode == 'view'
      },
      isNew() {
        return false
      },
      isEdit() {
        return this.openMode == 'edit' || this.openMode == 'modify'
      },
    }, userExtends ? userExtends.computed : null),
    watch: Object.assign({
      queryById(n, o) {
        this.refresh()
      }
    }, userExtends ? userExtends.watch : null),
    created() {
      if (this.isNew) cgForm.form_createNewRecord(this)
      else if ((this.isEdit || this.isDetail) && this.$route.query.id && !this.$route.query.record) cgForm.form_getRecordFromServer(this,this.$route.query.id)
      if (userExtends && userExtends.created && typeof userExtends.created === 'function' ) userExtends.created()
    },
    mounted() {
      if (userExtends && userExtends.mounted && typeof userExtends.mounted === 'function' ) userExtends.mounted()
    },
    activated() {
      if (userExtends && userExtends.activated && typeof userExtends.activated === 'function' ) userExtends.activated()
    },
    methods: Object.assign({
      submit: function() {
        cgForm.form_submit(this, 'save')
      },
      refreshTree() {
        if (this.record.actions) this.$refs.actionTree.setCheckedKeys(this.record.actions)
        this.recordChanged = false
      },
      checkChange() {
        this.record.actions = this.$refs.actionTree.getCheckedKeys()
        this.recordChanged = true
      },
      refresh() {
        const formObject = this
        cgForm.form_request({ 
          formObject, method: 'get', 
          params: { needLoadDictionary: formObject.needLoadDictionary, id: formObject.queryById}, action: 'record', silence: true,
          onSuccess: res => {
            if (res && res.hasOwnProperty('success') && res.success) {
              if (res.data) {
                formObject.record = res.data
              } else formObject.record = {}
              formObject.refreshTree()
            }
            formObject.recordChanged = false
          } 
        })
      },
      ...cg
    }, userExtends ? userExtends.methods : null)
  }
</script>
