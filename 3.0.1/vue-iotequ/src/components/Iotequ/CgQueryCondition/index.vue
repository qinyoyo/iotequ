<template>
    <el-drawer v-set-input:el-drawer__header.span="{tabindex: '-1'}" :title="$t('system.action.queryCondition')" :visible.sync="showQuery" 
               :modal="modal" direction="ltr" append-to-body :size="mobile?(isLandscape()?'50%':'100%'):'500px'" 
               @opened="opened">
      <div class="query-form">
        <el-form ref="cgQuery" :model="queryRecord" :label-position="mobile?'left':'right'" label-width="120px" v-cg-form-adjust :size="$store.state.app.size" :class="(mobile?'cg-form-cell ':'')+'cg-no-border'">
          <slot/>
        </el-form>
        <el-divider />
        <div class='cg-query-buttons'>
          <el-button class="cg-button" type="primary" plain @click.native="doAction('refresh')" icon="el-icon-search">
            {{ $t('system.action.query') }}
          </el-button>
          <el-button class="cg-button" plain icon="el-icon-circle-close" @click.native="doAction('reset')">
            {{ $t('system.action.reset') }}
          </el-button>
          <el-button v-for="(item,index) in addtionalActions" :key="index" class="cg-button" plain :icon="item.icon" @click.native="doAction(item.action)">
            {{ $t(item.title) }}
          </el-button>
        </div>
      </div>
    </el-drawer>
</template>
<script>
import { isVisible } from '@/utils/dom'
export default {
  name: 'CgQueryCondition',
  props: {
    value: {
      type: Boolean,
      default: false
    },
    modal: {
      type: Boolean,
      default: false
    },
    queryRecord: Object,
  },
  data() {
    return {
      showQuery: this.value,
      addtionalActions: []
    }
  },
  watch: {
    value(newValue) {
      this.showQuery = newValue
    },
    showQuery(newValue) {
      this.$emit('input', newValue)
    }
  },
  computed: {
    mobile() {
      return this.$store.state.app.device === 'mobile'
    }
  },
  methods: {
    opened() {
      if (this.$refs.cgQuery && this.$refs.cgQuery.$el) {
        const els = this.$refs.cgQuery.$el.querySelectorAll('input')
        for (let i=0;i<els.length;i++) {
          if (isVisible(els[i])) {
            els[i].focus()
            return 
          }
        }
      }
    },
    doAction(action) {
      if (action!=='reset') this.showQuery = false
      this.$emit(action)
    },
    appendAction(action) {
      this.addtionalActions.push(action)
    },
    isLandscape() {
      return window.orientation == 90 || window.orientation == -90
    }
  }
}
</script>
<style scoped>
.cg-query-buttons {
    text-align: center;
    margin-bottom: 10px;
}
</style>