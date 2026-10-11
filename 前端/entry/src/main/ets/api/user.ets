import { AxiosResponse } from "@ohos/axios";
import { CommonResult } from "../model/CommonResult";
import axiosInstance from "./axios";

export interface UserRemindParam {
  id?: number;
  messageStatus?: number;
  remindTime: string;
  sysStatus?: number;
  userId: string;
}
// 修改每天复习提醒时间
export const putUserRemind = async (param: UserRemindParam): Promise<CommonResult> => {
  let resp: AxiosResponse<CommonResult> = await axiosInstance.put('/user/remind', param)

  return resp.data;
}

// 修改每天复习单词数量
export const dayVoc = async (count: number | string): Promise<CommonResult> => {
  let resp: AxiosResponse<CommonResult> = await axiosInstance.put('/user/day/voc', null, {
    params: {
      count: count
    }
  });
  return resp.data;
}

// 获取用户信息
export const userInfo = async (): Promise<CommonResult> => {
  let resp: AxiosResponse<CommonResult> = await axiosInstance.get('/user/info')

  return resp.data
}
export const userCheckinStat = async (): Promise<CommonResult> => {
  let result: AxiosResponse<CommonResult> = await axiosInstance.get('/user/checkin/statistics')
  return result.data;
}

// 返回用户页面展示条目的信息
export const vocMyPage = async (): Promise<CommonResult> => {
  let result: AxiosResponse<CommonResult> = await axiosInstance.get('/user/voc/my/page')
  return result.data;
}

// /user/upgrade/info 获取用户升级信息
export const userUpgradeInfo = async (): Promise<CommonResult> => {
  let result: AxiosResponse<CommonResult> = await axiosInstance.get('/user/upgrade/info')
  return result.data;
}

// /user/level/info 用户等级信息
export const userLevelInfo = async (): Promise<CommonResult> => {
  let result: AxiosResponse<CommonResult> = await axiosInstance.get('/user/level/info')
  return result.data;
}