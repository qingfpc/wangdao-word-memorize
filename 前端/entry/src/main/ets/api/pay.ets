import { AxiosResponse } from "@ohos/axios";
import { CommonResult } from "../model/CommonResult";
import { PaySuccessNotifyCommand } from "../model/PaySuccessNotifyCommand";
import { VocPaySaleCommand } from "../model/VocPaySaleCommand";
import axiosInstance from "./axios";

// 用户用于查看购买历史
export const payList = async ():Promise<CommonResult>=>{
  let resp: AxiosResponse<CommonResult> = await axiosInstance.get('/user/pay/list')

  return resp.data;
}

// /user/pay 用户购买
export const userPay = async (command:VocPaySaleCommand):Promise<CommonResult>=>{
  let resp: AxiosResponse<CommonResult> = await axiosInstance.post('/user/pay',command)
  return resp.data;
}

// /user/pay/notice 用户购买通知
/*export const userPayNotice = async (command:PaySuccessNotifyCommand):Promise<CommonResult>=>{
  let resp: AxiosResponse<CommonResult> = await axiosInstance.post('/user/pay/notice',command)
  return resp.data;
}*/

// /user/pay/{paySaleId} 用户用于获取支付结果
export const getPayResult = async (paySaleId:string):Promise<CommonResult>=>{
  let resp: AxiosResponse<CommonResult> = await axiosInstance.get(`/user/pay/${paySaleId}`)
  return resp.data;
}
