
export default function getRules(vueObject, getRecordFunction) {
    return {
        appendClass:[
            {
                validator: (rule, value, callback) => {
                    const ap = getRecordFunction().actionProperty
                    if (ap=='js' || ap=='pg') {
                        if (!value) callback(new Error('前端以及前端+后端定义一个js前端函数调用'))
                        else callback()
                    } else if (ap=='go' || ap=='aj') {
                        if (!value) callback()
                        else {
                            try {
                                const ap = eval('(' + value + ')')
                                if (typeof ap === 'object') callback()
                                else callback(new Error('页面跳转及后端定义操作传递的附加属性，简易json格式，如 { timeout: 0 }定义一个无限等待，将显示进度条 , { url: full_route_path} 定义一个路由切换'))
                              } catch {
                                callback(new Error('页面跳转及后端定义操作传递的附加属性，简易json格式，如 { timeout: 0 }定义一个无限等待，将显示进度条 , { url: full_route_path} 定义一个路由切换'))
                              }
                        }
                    }
                    else callback()
                },
                trigger: 'blur'
            }
         ],
    }
}