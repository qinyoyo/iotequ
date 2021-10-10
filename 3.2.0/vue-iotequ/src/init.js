const xhr=new XMLHttpRequest();
xhr.open('GET','/static/settings.json',false)
xhr.send(null)
if(xhr.status===200) {
    window.userSettings = Object.assign({
        title: 'Vue Iotequ Framework',
        footer: '©2020 Qinyoyo　<a href="http://www.beian.miit.gov.cn/" target="_blank" class="text">渝ICP备17011611号-1</a>',
        server: '/api',
        baseUrl: '',
        u53ServerUrl: 'http://localhost:9000',
        showSettings: true,
        disableMLText: false,
        tagsView: true,
        fixedHeader: false,
        insideSpring: true,
        sidebarLogo: true       
    },JSON.parse(xhr.responseText))
    console.log(window.userSettings)
} else alert("打开配置文件/static/settings.json出错")

const initial_context = require.context('@/extend-src', false, /\/initial(\-[a-z0-9]+)*\.js$/)
initial_context.keys().forEach(key=>{
  const obj = initial_context(key).default
  if (obj) {
    Object.keys(obj).forEach(f=>{
      if (typeof obj[f] == 'function') obj[f]()
    })
  }
})
