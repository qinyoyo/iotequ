import { dynaRoutes, constantRoutes } from '@/router'
function hasPermission(whiteList, route, fatherPath) {
  const path = (fatherPath ? fatherPath + '/' : '') + route.path
  if (routeMatchList(path, whiteList)) return true
  else {
    whiteList.some((s) => {
      if (s.indexOf(path) === 0) return true
    })
  }
  return false
}
export function routeMatchList(string, regList) {
  let matched = false
  regList.some(function(s) {
    let regString = ''
    if (s.substring(s.length - 2) === '/*') {
      regString = s.substring(0, s.length - 2).replace(/\*/g, '.*') + '($|/.*$)'
    } else {
      regString = s.replace(/\*/g, '.*') + '/?$'
    }
    const reg = new RegExp(regString, 'i')
    if (string.match(reg)) {
      matched = true
      return true
    }
  })
  return matched
}
export function filterAsyncRoutes(routes, whiteList, fatherPath) {
  const res = []
  routes.forEach(route => {
    const tmp = { ...route }
    if (hasPermission(whiteList, tmp, fatherPath)) {
      if (tmp.children) {
        tmp.children = filterAsyncRoutes(tmp.children, whiteList, (fatherPath ? fatherPath + '/' : '') + tmp.path)
      }
      res.push(tmp)
    }
  })
  return res
}

export function findRouteByUrl(fullPath,$router) {
  if (!fullPath) return null
  if (!$router) $router=window.$vue.$router
  const m = $router.match(fullPath, $router.current) 
  if (m && m.matched && m.matched.length>0) {
    const pos = fullPath.indexOf('?')
    const pathWithoutParams = pos>0 ? fullPath.substring(0,pos) : fullPath
    for (let i=0;i<m.matched.length;i++) {
      if (m.matched[i].path == pathWithoutParams) return m.matched[i]
    }
  }
  return null
}

export function routesWithFullPath(routes, fatherPath) {
  const res = []
  routes.forEach(route => {
    const tmp = {
      name: route.name,
      title: route.meta ? route.meta.title : null,
      path: route.path,
      component: route.component ? (route.component.name === 'Layout' ? 'Layout' : '') : null,
      breadcrumbShow: route.meta ? route.meta.breadcrumb : null,
      needCache: route.meta ? !route.meta.noCache : null,
      tagView: route.meta ? route.meta.tagView : null
    }
    if (fatherPath) tmp.path = fatherPath + '/' + tmp.path
    res.push(tmp)
    if (route.children) {
      const cc = routesWithFullPath(route.children, tmp.path)
      cc.forEach(r => { res.push(r) })
    }
  })
  return res
}
export function filterAsyncRoutesByApis(routes, apis) {
  const res = []
  if (!apis) return res
  if (typeof apis ==='string') apis = apis.split(',')
  if (apis.length === 0) return res
  routes.forEach(route => {
    const tmp = { ...route }
    let permission = true
    if (tmp.meta && tmp.meta.authorities) {
      tmp.meta.authorities.some(r => {
        const url = r + '/'
        let matched = false
        apis.some(api => {
          const a = api + '/'
          if (a.indexOf(url) === 0) {
            matched = true
            return true
          }
        })
        if (!matched) {
          permission = false
          // console.log(tmp)
          return true
        }
      })
    }
    if (permission) {
      if (tmp.children) {
        tmp.children = filterAsyncRoutesByApis(tmp.children, apis)
        if (tmp.children && tmp.children.length > 0) res.push(tmp)
        else if (tmp.component.name !== 'Layout') res.push(tmp)
      } else res.push(tmp)
    }
  })
  return res
}
export function getAllRouteDictionary() {
  const routes = routesWithFullPath(constantRoutes).concat(routesWithFullPath(dynaRoutes))
  routes.sort((a,b)=>a.path > b.path)
  const result=[]
  routes.forEach(r=>result.push({ value: r.path, text: r.path+(r.title?'('+r.title.local()+')':'')}))
  return result
}