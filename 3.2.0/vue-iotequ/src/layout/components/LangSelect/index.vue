<template>
  <el-dropdown trigger="click" class="international" @command="handleSetLanguage">
    <div>
      <cg-icon :icon="titleIcon" />
    </div>
    <el-dropdown-menu slot="dropdown">
      <el-dropdown-item :disabled="language==='zh'" command="zh">
        <cg-icon icon="international-icon svg-zh"/> 中文
      </el-dropdown-item>
      <el-dropdown-item :disabled="language==='en'" command="en">
        <cg-icon icon="international-icon svg-en"/> English
      </el-dropdown-item>
    </el-dropdown-menu>
  </el-dropdown>
</template>

<script>
export default {
  data() {
    return {
      language : this.$i18n.locale,
      titleIcon: 'international-icon svg-'+this.$i18n.locale
    }
  },
  methods: {
    handleSetLanguage(lang) {
      this.language = lang
      this.$i18n.locale = lang
      this.titleIcon = 'international-icon svg-' + lang
      this.$store.dispatch('app/setLanguage', lang)
    }
  }
}
</script>
