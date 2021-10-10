/*
   svg 使用规则 ： icon = 'svg-svgName'
   svgName 以 .svg 结束，直接使用静态文件  /public/static/svg/svgName.  svgName可以包含目录，目录分隔符必须为 / . 如 chart/chart-line.svg
   svgName 不包含 .svg结束, 查找 id (svgName可以包含目录，id不包含目录)，chart/chart-line 和 chart-line的id都为 icon-chart-line，存在id直接引用
   否则通过文件引用。因此设计时使用了目录，然后将文件打包，不需要修改名称
*/
<template>
  <div v-if="isExternal" :title="title" :style="styleExternalIcon" class="external-icon cg-icon" v-on="$listeners" />
  <svg v-else-if="svgName" :class="iconClass" :style="iconStyle" aria-hidden="true" v-on="$listeners">
    <title v-if="title">{{title}}</title>
    <use v-if="hrefExists()" :href="'#'+svgHrefId" />
  </svg>
  <i v-else :title="title" :class="iconClass" :style="iconStyle" aria-hidden="true" v-on="$listeners"></i>
</template>
<script>
/* 同步读取
const getSvgFile = function(url) {
  const xhr=new XMLHttpRequest()
  xhr.open('GET',encodeURI(url),false)
  xhr.send(null)
  if(xhr.status===200) {
      return xhr.responseText
  } else return null
}
*/
const setSvgData = function($el,svgData) {
  let reg = /<svg.*?>(.*)<\/svg>/i
  if (reg.test(svgData)) {
    $el.innerHTML = RegExp.$1
    let width = '', height = '', viewBox = ''
    reg = /<svg.*?width\s*=\s*"(.+?)".*?>/i
    if (reg.test(svgData)) width = RegExp.$1
    reg = /<svg.*?height\s*=\s*"(.+?)".*?>/i
    if (reg.test(svgData)) height = RegExp.$1
    reg = /<svg.*?viewBox\s*=\s*"(.+?)".*?>/i
    if (reg.test(svgData)) viewBox = RegExp.$1
    if (viewBox)  $el.setAttribute("viewBox", viewBox)
    else if (width && height) $el.setAttribute("viewBox", '0 0 '+width+' '+height)
    else if (width) $el.setAttribute("viewBox", '0 0 '+width+' '+width)
    else if (height) $el.setAttribute("viewBox", '0 0 '+height+' '+height)
    // if (width) $el.setAttribute("width", width)
    // if (height) $el.setAttribute("height", height)
    return true
  } return false
}

import axios from 'axios'
import { isExternal } from '@/utils/validate'
export default {
  name: 'CgIcon',
  props: {
    icon: String,  // fa-xxx, el-icon-xxx, svg-xxx
    extClass:String,
    extStyle: String,
    title: String,
    color:String,
    width: Number,
    size: {
      type: Number,
      default: 14
    }
  },
  data() {
    return {
      svgName: this.getSvgName()
    }
  },
  watch: {
    icon() {
      this.svgName = this.getSvgName()
    }
  },
  computed: {
    isExternal() {
      return isExternal(this.icon)
    },
    styleExternalIcon() {
      return {
        mask: `url(${this.extClass}) no-repeat 50% 50%`,
        '-webkit-mask': `url(${this.extClass}) no-repeat 50% 50%`
      }
    },
    svgHrefId() {
      let name = this.svgName
      if (/\.svg$/i.test(name)) return ''
      let p=name.lastIndexOf('/')
      if (p>=0) name=name.substring(p+1)
      if (name) return 'icon-'+name.replace(/\s/g,'-').replace(/[&;,]/g,'')
      else return ''
    },
    iconClass() {
      const name=this.svgName
      let c = (name ? this.icon.replace('svg-'+name,'') : this.icon)
      c = c+' cg-icon'
      return c
    },
    iconStyle() {
      const name=this.svgName  
      let r = this.extStyle ? this.extStyle : ''
      if (this.color) r = (r ? r + ';' : '') + 'color:' + this.color
      if (name) {
        r = (r ? r + ';' : '') + 'width:' + this.size + 'px; height:'+ this.size + 'px'
      }
      else {
        r = (r ? r + ';' : '') + 'font-size:'+ this.size + "px"
      }
      if (this.width > this.size) {
        r = (r ? r + ';' : '') + 'padding-right:' + (this.width - this.size) +'px'
      }
      return r
    }
  },
  // created() {
  //   if (this.svgName && this.svgName.indexOf('.svg')<0) {
  //     setSvgSymbol('/static/svg/'+this.svgName+'.svg',this.svgHrefId)
  //   } 
  // },
  mounted() {
    if (this.svgName && !this.hrefExists()) {
      const $el=this.$el
      /* 同步读取
      const svgData = getSvgFile(this.remoteUrl())
      if (svgData) {
          if (!setSvgData($el,svgData)) console.error('file /static/svg/'+this.svgName+' error')
      } else console.error('get /static/svg/'+svgName+'.svg failed')
      */
     axios.get(this.remoteUrl()).then(res=>{
      const svgData = res.data
      if (svgData) {
          if (!setSvgData($el,svgData)) console.error('file /static/svg/'+this.svgName+' error')
      } else console.error('get /static/svg/'+svgName+'.svg failed')       
     })
    }
  },
  methods: {
    getSvgName() {
      if (this.icon) {
        let p0=this.icon.indexOf('svg-')
        if (p0<0) return ''
        let p1=this.icon.indexOf('.svg')
        if (p1>p0) return this.icon.substring(p0+4,p1+4)
        p1=this.icon.indexOf(' ',p0)
        if (p1>p0) return this.icon.substring(p0+4,p1)
        else return this.icon.substring(p0+4)
      }
      else return ''
    },
    hrefExists() {
      const id=this.svgHrefId
      if (id) {
        const $e = document.querySelector('#'+id)
        if ($e) return true
        else return false
      } else return false
    },
    remoteUrl() {
      return this.svgName ? '/static/svg/'+this.svgName+(this.svgName.indexOf('.svg')>0?'':'.svg') : null
    }
  }
}
</script>

<style scoped>
.cg-icon {
  vertical-align: middle;
  fill: currentColor;
  overflow: hidden;
}

.external-icon {
  background-color: currentColor;
  mask-size: cover!important;
  display: inline-block;
}
</style>
