// 导入 axios 库，这里使用的是 HarmonyOS 相关的 axios 包
import axios, { AxiosError, AxiosResponse, InternalAxiosRequestConfig } from '@ohos/axios';
import { CommonResult } from '../model/CommonResult';

// 定义基础的 API 地址，用于发送请求时拼接
// const BASE_URL = "http://39.101.189.16:8083/";
// const BASE_URL = 'http://192.168.1.15:8111/';
// const BASE_URL = 'http://192.168.3.45:8090/';
// const BASE_URL = 'http://192.168.31.93:8111/';
// const BASE_URL = 'http://39.101.189.16:8085/';
// const BASE_URL = 'http://192.168.101.62:8111/';
// const BASE_URL = 'http://192.168.1.55:8111/';
// const BASE_URL = 'http://192.168.130.64:8111/';
// const BASE_URL = 'http://192.168.123.236:8111/';
// const BASE_URL = 'http://39.101.189.16:8222/';
const BASE_URL = 'http://113.46.193.206:8111/';
export const TOKEN_HEADER = 'X-Cskaoyan-App-Token';

// 创建 axios 实例，实例化后可以进行更精细的配置
const axiosInstance = axios.create({
  baseURL: BASE_URL,// 设置请求的基础 URL，每次发送请求时，Axios 会自动加上这个基础 URL
  timeout: 10000, // 设置请求的超时时间（单位为毫秒）,这里设置超时时间为 1000ms，即 1 秒,如果请求超时，axios 会抛出错误,
  /*headers: { // 设置请求头，通常用于传递 API 验证令牌或其他认证信息
    'X-Cskaoyan-Token': 'wangdao' // 自定义请求头，传递一个名为 X-Cskaoyan-Token 的 token 参数
  }*/
});

axiosInstance.interceptors.request.use((config:InternalAxiosRequestConfig)=>{
  console.info('请求拦截:' + config.url)
  if (config.url?.endsWith('/ai/read')) {
    config.timeout = 120000
  }else {
    config.timeout = 10000
  }
  return config;
},(err:AxiosError)=>{
  return Promise.reject(err)
})

/*axiosInstance.interceptors.response.use((resp:AxiosResponse)=>{
  console.info(`响应之前：${JSON.stringify(resp.data)}`)
  return resp;
},(err:AxiosError)=>{
  return Promise.reject(err)
})*/
// 配置 Axios 响应拦截器
axiosInstance.interceptors.response.use(
  // 成功响应的处理逻辑
  (resp: AxiosResponse) => {
    // 打印服务器响应体的数据（仅用于调试）
    console.info(`获得服务器的响应体数据：${JSON.stringify(resp.data)}`)

    // 判断响应状态码是否为 200
    if (resp.status === 200) {
      // 将响应体的 data 解析为 CommonResult 类型
      //let result: CommonResult = JSON.parse(JSON.stringify(resp.data))
      let result:CommonResult = resp.data

      // 判断响应体中的 code 是否为 0，表示请求成功
      if (result.code === 200) {
        return resp;  // 请求成功，返回响应
      }
      // 如果 code 是 502，表示登录失败
      else if (result.code == 502) {
        // 弹出提示框，显示登录失败信息
        AlertDialog.show({ message: "登录失败" })
        return Promise.reject()  // 返回拒绝的 Promise，通常用于终止后续操作
      }
      // 如果 code 是 502，表示登录失败
      else {
        // 弹出提示框，显示登录失败信息
        AlertDialog.show({ message: result.message })
        return Promise.reject()  // 返回拒绝的 Promise，通常用于终止后续操作
      }
    }
    // 如果状态码不为 200 或其他逻辑异常，返回拒绝的 Promise
    return Promise.reject()
  },

  // 错误响应的处理逻辑
  (err: AxiosError) => {
    // 打印错误信息，包含请求的 URL 和错误信息
    console.error(`访问 ${err.config?.url}，服务返回状态码 ${err.code}: ${err.message}`)

    // 弹出错误提示框，显示网络错误信息
    AlertDialog.show({ title: '错误', message: '网络错误' })

    // 返回拒绝的 Promise，并传递错误对象
    return Promise.reject(err)
  }
)


// 返回创建的 Axios 实例，后续可以使用该实例来发送请求
export default axiosInstance;