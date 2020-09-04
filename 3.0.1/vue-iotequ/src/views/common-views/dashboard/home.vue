<template>
  <div class="home-editor-container">
    <div v-if="authentication"><el-avatar :size="100" :src="avatar+'&tick='+new Date().getTime()"></el-avatar><span class="user-name">{{ name }}</span></div>
    <div class="info-container">
        <span>Welcome! {{ introduction }}</span>
    </div>
    <el-collapse accordion>
      <el-collapse-item title="版本信息" name="2">
        <div v-for = "item in versions"><span>{{item}}</span></div>
      </el-collapse-item>
      <el-collapse-item title="权限集" name="3">
        <div v-for = "item in roles"><span>{{item}}</span></div>
      </el-collapse-item>  
      <el-collapse-item title="环境配置" name="3">        
        <div v-if="navigator"><span>userAgent：{{navigator.userAgent}}</span></div>
        <div><span>运行模式：{{device}}</span></div>
        <div><span>后台服务器：{{server}}</span></div>
        <div><span>后台地址：{{baseUrl}}</span></div>
      </el-collapse-item>            
      <el-collapse-item title="API权限" name="1">
        <div v-for = "item in permission_apis"><span>{{item}}</span></div>
      </el-collapse-item>
    </el-collapse>    
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
export default {
  name: 'Home',
  data() {
    return {
      navigator : navigator
    }
  },
  computed: {
    ...mapGetters([
      'name',
      'avatar',
      'introduction',
      'authentication',
      'roles',
      'permission_routes',
      'permission_apis',
      'versions',
      'footer',
      'device',
      'server',
      'baseUrl'
    ])
  }
}
</script>

<style lang="scss" scoped>
  .emptyGif {
    display: block;
    width: 50%;
    margin: 20px auto;
  }

  .home-editor-container {
    height: 100%;
    padding: 50px 60px 0px;
    .user-name {
      position: relative;
      top: -20px;
      font-size: 48px;
    }
    .info-container {
      position: relative;
      height: 32px;
      font-size: 24px;
    }
  }
</style>
