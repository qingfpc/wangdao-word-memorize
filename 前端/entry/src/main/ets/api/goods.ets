import { AxiosResponse } from "@ohos/axios";
import { CommonResult } from "../model/CommonResult";
import axiosInstance from "./axios";

export const product = async (types:number[],count:number,keyword:string,pageNumber:number):Promise<CommonResult>=>{
  let resp: AxiosResponse<CommonResult> = await axiosInstance.get('/product?types=1&types=3',{
    params:{
      keyword:keyword,
    }
  })
  return resp.data;
}