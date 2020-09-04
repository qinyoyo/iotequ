module.exports = {
  presets: [
    ['@vue/app', {
      'useBuiltIns': 'entry', // 兼容 ie
      polyfills: [
        'es6.promise',
        'es6.symbol'
      ]
    }]
  ]
}
