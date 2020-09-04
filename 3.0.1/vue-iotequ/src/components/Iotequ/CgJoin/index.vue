<template>
  <div class="cg-join">
    <div v-if="mobile">
      <el-drawer v-if="mobile" :direction="isLandscape()?'ltr':'btt'" :title="$t('system.action.pleaseSelect')" append-to-body 
                 :visible.sync="showJoinList" size="50%">
        <slot name="popover"/>
      </el-drawer>
      <div @click = "click">
      <slot name="reference" />
      </div>
    </div>
    <div v-else>
      <el-popover v-model="showJoinList" placement="bottom" class="cg-join-list" :disabled="disabled || readonly" trigger="click">
        <slot name="popover" />
        <div slot="reference">
            <slot name="reference" />
        </div>
    </el-popover>
    </div>
  </div>
</template>
<script>

export default {
  name: 'CgJoin',
  props: {
    value: {
      type: Boolean,
      default: false
    },
    readonly: {
      type: Boolean,
      default: false
    },
    disabled: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      mobile: this.$store.state.app.device === 'mobile',
      showJoinList: this.value
    }
  },
  watch: {
    value(newValue) {
      this.showJoinList = newValue
    },
    showJoinList(newValue) {
      this.$emit('input', newValue)
    }
  },
  computed: {
  },
  methods: {
    click() {
      if (!this.readonly && !this.disabled) this.showJoinList=!this.showJoinList
    },
    isLandscape() {
      return window.orientation == 90 || window.orientation == -90
    }
  }
}
</script>
<style scoped>
</style>