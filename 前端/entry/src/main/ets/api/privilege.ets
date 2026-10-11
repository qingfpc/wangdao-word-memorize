import { AxiosResponse } from "@ohos/axios";
import { AiReadParam } from "../model/AiReadParam";
import { CommonResult } from "../model/CommonResult";
import { FavoriteParam } from "../model/FavoriteParam";
import { VocExampleParam } from "../model/VocExampleParam";
import axiosInstance from "./axios";

// /user/priviledge/voc/example 创建自建例句
export const userVocExampleCreate = async (param:VocExampleParam):Promise<CommonResult> => {
  let resp:AxiosResponse<CommonResult> = await axiosInstance.post('/user/priviledge/voc/example',param);
  return resp.data;
}

// /user/priviledge/favorite/cancel 取消收藏
export const userVocFavoriteCancel = async (vocIds:string[]):Promise<CommonResult> => {
  let resp:AxiosResponse<CommonResult> = await axiosInstance.post(`/user/priviledge/favorite/cancel`,vocIds);
  return resp.data;
}

// /user/priviledge/favorite 收藏
export const userVocFavoriteCreate = async (param:FavoriteParam):Promise<CommonResult> => {
  let resp:AxiosResponse<CommonResult> = await axiosInstance.post(`/user/priviledge/favorite`,param);
  return resp.data;
}

// /user/priviledge/voc/example/list 自建例句列表
export const userVocExampleList = async (keyword:string):Promise<CommonResult> => {
  let resp:AxiosResponse<CommonResult> = await axiosInstance.get(`/user/priviledge/voc/example/list?pageNum=1&count=100&keyword=${keyword}`);
  return resp.data;
}

// /user/priviledge/favorite/list
export const userPrivilegeFavoriteList = async (keyword:string):Promise<CommonResult> => {
  let resp:AxiosResponse<CommonResult> = await axiosInstance.get(`/user/priviledge/favorite/list?pageNum=1&count=100&keyword=${keyword}`);
  return resp.data;
}

// /user/priviledge/voc/example put方法修改例句
export const userVocExampleUpdate = async (param:VocExampleParam):Promise<CommonResult> => {
  let resp:AxiosResponse<CommonResult> = await axiosInstance.put(`/user/priviledge/voc/example`,param);
  return resp.data;
}

// /user/priviledge/voc/non 标识用户不想背
export const userVocNon = async (vocId:string):Promise<CommonResult> => {
  let resp:AxiosResponse<CommonResult> = await axiosInstance.post(`/user/priviledge/voc/non`,`vocId=${vocId}`);
  return resp.data;
}

// /user/priviledge/voc/non/cancel 取消标识用户不想背
export const userVocNonCancel = async (vocId:string):Promise<CommonResult> => {
  let resp:AxiosResponse<CommonResult> = await axiosInstance.put(`/user/priviledge/voc/non/cancel`,`vocId=${vocId}`);
  return resp.data;
}

// /user/priviledge/review/ai/read/options 智能阅读选项
export const userPrivilegeReviewAiReadOptions = async ():Promise<CommonResult> => {
  let resp:AxiosResponse<CommonResult> = await axiosInstance.post(`/user/priviledge/review/ai/read/options`);
  return resp.data;
}

// /user/priviledge/review/ai/read/free 智能阅读免费次数
export const userPrivilegeReviewAiReadFree = async ():Promise<CommonResult> => {
  let resp:AxiosResponse<CommonResult> = await axiosInstance.post(`/user/priviledge/review/ai/read/free`);
  return resp.data;
}
///user/priviledge/review/ai/read 智能阅读
export const userPrivilegeReviewAiRead = async (param:AiReadParam):Promise<CommonResult> => {
  let resp:AxiosResponse<CommonResult> = await axiosInstance.post(`/user/priviledge/review/ai/read`,param);
  return resp.data;
}

// /user/priviledge/favorite/bulk 批量添加用户收藏单词
export const userPrivilegeFavoriteBulk = async (param:FavoriteParam[]):Promise<CommonResult> => {
  let resp:AxiosResponse<CommonResult> = await axiosInstance.post(`/user/priviledge/favorite/bulk`,param);
  return resp.data;
}