
export default function getRules(vueObject, getRecordFunction) {
    return {
        entityName: [{
            validator: (rule, value, callback) => {
                const record = getRecordFunction()
                if (record.name) {
                    const name = vueObject.camelString(record.name.split(':')[0])
                    if (value !== name) {
                    record.entityName = name
                    callback(new Error('请使用数据库名的驼峰表达式:' + name))
                    } else callback()
                } else callback()
            },
            trigger: 'blur'
        }],
        dictTable : [{
            validator: (rule, value, callback) => {
            if (vueObject.joinMode && !value) callback(new Error('必须设置join的列表视图'))
            else if (vueObject.useOtherDict && !value.split(':')[1].trim()) callback(new Error('必须设置引用的其他字典名'))
            else callback()
            },
            trigger: 'blur'
        }],
        dictField : [{
            validator: (rule, value, callback) => {
            if (vueObject.joinMode && !value) callback(new Error('必须设置join的列表视图关联字段'))
            else if (vueObject.useConstValueText && !value) callback(new Error('必须设置取值序列'))
            else if (vueObject.useSelect && !value) callback(new Error('必须设置数据库表值字段'))
            else callback()
            },
            trigger: 'blur'
        }],
        dictText : [{
            validator: (rule, value, callback) => {
            if (vueObject.joinMode && !value) callback(new Error('必须设置join的列表视图引入字段列表'))
            else if (vueObject.useSelect && !value) callback(new Error('必须设置数据库表显示字段'))
            else callback()
            },
            trigger: 'blur'
        }]
    }
}