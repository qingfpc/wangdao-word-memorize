import { AxiosResponse } from "@ohos/axios";
import { CommonResult } from "../model/CommonResult";
import { UserCheckinParam } from "../model/UserCheckinParam";
import axiosInstance from "./axios";


export const userCheckin = async (param:UserCheckinParam):Promise<CommonResult> => {
  let resp:AxiosResponse<CommonResult> = await axiosInstance.post('/user/checkin',param);
  return resp.data;
}
export const userCheckinCard = async ():Promise<CommonResult> => {
  let resp:AxiosResponse<CommonResult> = await axiosInstance.put('/user/checkin/card');
  return resp.data;
}
export const userCheckinList = async ():Promise<CommonResult> => {
  let resp:AxiosResponse<CommonResult> = await axiosInstance.get('/user/checkin/list?pageNum=1&count=1000');
  return resp.data;
}

