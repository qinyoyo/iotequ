<template>
  <div class="icons-container">
    <div>      
      <a href="https://www.iconfont.cn/" class="link-item" target="_blank">Iconfont</a>
    </div>
    <div>
      <a href="http://www.fontawesome.com.cn/faicons/" class="link-item" target="_blank">Font Awesome</a>
    </div>
    <el-tabs>
      <el-tab-pane label="Svg">
        <el-collapse accordion>
          <el-collapse-item title="Internal">
            <div v-for="item of svgIcons" :key="item" @click="handleClipboard(generateIconCode(item),$event)">
              <el-tooltip placement="top">
                <div slot="content">
                  {{ generateIconCode(item) }}
                </div>
                <div class="icon-item">
                  <cg-icon :icon="'svg-'+item+' disabled'" :size="32" />
                  <span>{{ item }}</span>
                </div>
              </el-tooltip>
            </div>
          </el-collapse-item>
          <el-collapse-item v-for="(arrayItem, index) in staticSvgIcons" :key="index" :title="'public/static/svg/'+itemParent(arrayItem[0])">  
            <div v-for="item in arrayItem" :key="item" @click="handleClipboard(generateIconCode(item),$event)">
              <el-tooltip placement="top">
                <div slot="content">
                  {{ generateIconCode(item+'.svg') }}
                </div>
                <div class="icon-item">
                  <cg-icon :icon="'svg-'+item+'.svg disabled'" :size="32" />
                  <span>{{ itemSimpleName(item) }}</span>
                </div>
              </el-tooltip>
            </div>              
          </el-collapse-item>  
        </el-collapse>
      </el-tab-pane>
      <el-tab-pane label="Element-UI">
        <div v-for="item of elementIcons" :key="item" @click="handleClipboard(generateElementIconCode(item),$event)">
          <el-tooltip placement="top">
            <div slot="content">
              {{ generateElementIconCode(item) }}
            </div>
            <div class="icon-item">
              <i :class="'el-icon-' + item" />
              <span>{{ item }}</span>
            </div>
          </el-tooltip>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import clipboard from '@/utils/clipboard'
import svgIcons from './svg-icons'
import staticSvgIcons from './static-svg-icons'
import elementIcons from './element-icons'

export default {
  name: 'Icons',
  data() {
    return {
      svgIcons,
      staticSvgIcons,
      elementIcons
    }
  },
  methods: {
    generateIconCode(symbol) {
      return `svg-${symbol}`
    },
    generateElementIconCode(symbol) {
      return `el-icon-${symbol}`
    },
    handleClipboard(text, event) {
      clipboard(text, event)
    },
    itemParent(item) {
      let p = item.lastIndexOf('/')
      if (p>0) return item.substring(0,p)
      else return ''
    },
    itemSimpleName(item) {
      let p = item.lastIndexOf('/')
      if (p>0) return item.substring(p+1)
      else return item
    }
  }
}
</script>

<style lang="scss" scoped>
.icons-container {
  margin: 10px;
  .icon-item {
    margin: 5px;
    height: 85px;
    text-align: center;
    width: 160px;
    float: left;
    font-size: 30px;
    color: #24292e;
    cursor: pointer;
  }

  span {
    display: block;
    font-size: 16px;
    margin-top: 10px;
  }

  .disabled {
    pointer-events: none;
  }
  .link-item {
    color: -webkit-link;
    cursor: pointer;
    text-decoration: underline;
  }
}
</style>
