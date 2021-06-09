<template>
  <div class="home-editor-container">
    <div v-if="authentication"><el-avatar :size="100" :src="avatar+'&tick='+new Date().getTime()"></el-avatar><span class="user-name">{{ name }}</span></div>
    <div class="info-container">
        <span>{{'system.layout.welcome'.local()}}! {{ authentication ? localeText(introduction) : '' }}</span>
    </div>
    <el-collapse accordion>
      <el-collapse-item :title="'system.layout.version'.local()" name="2">
        <div v-for = "item in versions"><span>{{item}}</span></div>
      </el-collapse-item>
      <el-collapse-item :title="'system.layout.role'.local()" name="3">
        <div v-for = "item in roles"><span>{{item}}</span></div>
      </el-collapse-item>  
      <el-collapse-item :title="'system.layout.environment'.local()" name="3">        
        <div v-if="navigator"><span>userAgent：{{navigator.userAgent}}</span></div>
        <div><span>Mode：{{device}}</span></div>
        <div><span>Server：{{server}}</span></div>
        <div><span>Server addr：{{baseUrl}}</span></div>
      </el-collapse-item>            
      <el-collapse-item title="API" name="1">
        <div v-for = "item in permission_apis"><span>{{item}}</span></div>
      </el-collapse-item>
    </el-collapse>    
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import {localeText} from '@/lang'
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
  },
  methods: {
    localeText
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
