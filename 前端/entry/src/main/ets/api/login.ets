//登录模块对应功能
import auth from "@hw-agconnect/auth";
import { AxiosResponse } from "@ohos/axios";
import { CommonResult } from "../model/CommonResult";
import { DataCommonResult } from "../model/DataCommonResult";
import { LoginData, LoginParam } from "../model/LoginModel";
import axiosInstance from "./axios";

export const loginPhone = async (param:LoginParam)=>{
  let resp:AxiosResponse<DataCommonResult<LoginData>> = await axiosInstance.post('/user/login/phone',param);
  return resp.data;
}

export const  loginBackServer = async ()=>{
  const authUser = await auth.getCurrentUser()
  if (authUser) {
    let phoneNumber = authUser.getPhone()
    let displayName: string = authUser.getDisplayName()
    let photoUrl: string = authUser.getPhotoUrl()
    let email: string = authUser.getEmail()
    let result= await loginPhone({phoneNumber:phoneNumber,displayName:displayName,photoUrl:photoUrl,email:email})
    return result.data
  }else {
    return new LoginData()
  }
}