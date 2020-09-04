import axios from 'axios'
import { saveFile } from '@/utils/cg'

axios.defaults.withCredentials = true
axios.defaults.crossDomain = true

const { server, baseUrl } = window.userSettings

// create an axios instance
const service = axios.create({
  baseURL: apiUrl(), // url = base url + request url
  withCredentials: true, // send cookies when cross-domain requests
  crossDomain: true,
  timeout: 15000 // request timeout
})

// request interceptor
service.interceptors.request.use(
  config => {
    // do something before request is sent
    config.url = encodeURI(config.url)
    if (config.data && config.data instanceof FormData) config.headers['Content-Type'] = 'multipart/form-data'
    else config.headers['Content-Type'] = 'application/json; charset=UTF-8'
    return config
  },
  error => {
    // do something with request error
    return Promise.reject({
      message: error.toString(),
      error: 'request_error',
      success: false
    })
  }
)

// response interceptor
service.interceptors.response.use(
  response => {
    const res = response.data
    if (Object.prototype.toString.call(res) === '[object Blob]') {
      if (res.type.toLowerCase()==='application/json') return res
      const fileName =  (response.config && response.config.params && response.config.params.fileName ? response.config.params.fileName : 'iotequ.bin')
      saveFile(res, fileName)
      return {
        message: '',
        success: true
      }
    } return res
  },
  error => {
    if (typeof error === 'string') {
      return {
        error: '500',
        message: error,
        success: false
      }
    }
    var status = error.response && error.response.status ? error.response.status : error.message
    if (error.response && error.response.data) {
      const data = error.response.data
      if (typeof data === 'string') {
        return {
          error: status,
          message: data,
          success: false
        }
      }
      if (!data.message) {
        if (data.error_description) data.message = data.error_description
        else data.message = status || '500'
      }
      if (!data.error) data.error = status
      data.success = false
      return data
    }
    error.success = false
    return error
  }
)

export function apiUrl(url) {
  let api = server + baseUrl // process.env.VUE_APP_BASE_API
  if (api.length > 0 && api.substring(api.length - 1, api.length) === '/') api = api.substring(0, api.length - 1)
  if (!url) return api
  if (url.substring(0, 1) === '/') return api + encodeURI(url)
  else return api + '/' + encodeURI(url)
}

export default service
