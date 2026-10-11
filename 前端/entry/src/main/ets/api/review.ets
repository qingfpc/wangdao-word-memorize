import { AxiosResponse } from "@ohos/axios";
import { CommonResult } from "../model/CommonResult";
import { UserTodayReviewData } from "../model/UserTodayReviewData";
import axiosInstance from "./axios";

export const reviewVoc = async (param:string[]):Promise<CommonResult> => {
  let resp:AxiosResponse<CommonResult> = await axiosInstance.post('/user/review/voc',param);
  return resp.data;
}

// /user/review/data 下载用户的所有单词复习数据
export const reviewData = async ():Promise<CommonResult> => {
  let resp:AxiosResponse<CommonResult> = await axiosInstance.get('/user/review/data?pageNum=1&count=1000');
  return resp.data;
}

// /user/review/data
export const postUserReviewData = async (param:UserTodayReviewData):Promise<CommonResult> => {
  let resp:AxiosResponse<CommonResult> = await axiosInstance.post('/user/review/data',param);
  return resp.data;
}