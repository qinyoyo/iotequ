<template>
 <div :class="mobile || height==0 ? 'cg-form':'cg-form just-v-scroll'" :style="mobile || height==0?'':'height:'+height+'px'">
    <el-form ref="cgForm" :model="record" v-loading="recordLoading" :class="className" label-position="top" label-suffix=":" :size="$store.state.app.size"
             hide-required-asterisk class="cg-no-border">
      <el-tree ref="actionTree" :data="dictionary.actions" :props="defaultProps" accordion node-key="value" show-checkbox
        @check-change="checkChange" />
    </el-form>
    <div v-if="!isDetail" style="padding-bottom:20px;">
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
  export default {
    name: 'CgFormPermissionTree',
    props: {
      showInDialog: {
        type: Boolean,
        default: false
      },
      openID: {
        type: String,
        default: ''
      },
      height: {
        type: Number,
        default: 0
      },
      queryById: [Number, String]
    },
    data() {
      return {
        rules:{},
        defaultProps: {
          label: (data, node)=>{
            let text = data.text
            if (text) {
              const loc = text.local()
              if (loc!=text) text=loc
              else if (text.indexOf('.')>0) text=''
            }
            return (data.url?data.url:'')+(text?((data.url?' : ':'')+text):'')
          },
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
      }
    },
    computed: {
      mobile() {
        return this.$store.state.app.device === 'mobile'
      },
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
    },
    watch: {
      queryById(n, o) {
        this.refresh()
      }
    }, 
    created() {
      if (this.queryById) this.refresh()
    },
    methods: {
      submit: function() {
        cgForm.form_submit(this, 'update')
      },
      refreshTree() {
        if (this.record.actions) this.$refs.actionTree.setCheckedKeys(this.record.actions)
        else this.$refs.actionTree.setCheckedKeys([])
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
    }
  }
</script>
