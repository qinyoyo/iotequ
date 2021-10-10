
export default function getRules(vueObject, getRecordFunction) {
    return {
        dataAction:[
            {
                validator: (rule, value, callback) => {
                    const jsCmd = getRecordFunction().jsCmd
                    if (jsCmd) {
                        if (!value) callback()
                        else try {
                            const ap = eval('(' + value + ')')
                            if (typeof ap === 'object') callback()
                            else callback(new Error('传递参数必须为一个json字符串'))
                          } catch {
                            callback(new Error('传递参数必须为一个json字符串'))
                          }
                    } else  callback()
                },
                trigger: 'blur'
            }
        ],
    }
}