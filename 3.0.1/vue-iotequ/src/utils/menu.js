const changeHiddenMenus = function(path,hidden) {
  if (path && window.$sidebar) {
    window.$sidebar.changeHiddenMenus(path,hidden)
  }
}
export function showMenu(path) {
  changeHiddenMenus(path,false)
}

export function hideMenu(path) {
  changeHiddenMenus(path,true)
}
