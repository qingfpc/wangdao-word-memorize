import { CommonResult } from "../model/CommonResult";
import axiosInstance from "./axios";
import { AxiosResponse } from "@ohos/axios";

// 查看单词详情
export const vocDetail = async (id:string):Promise<CommonResult> => {
  let resp: AxiosResponse<CommonResult> = await axiosInstance.get(`/user/voc/detail/${id}`)
  return resp.data
}

// 获取当前所选书籍的统计信息
export const vocCurrentStat = async (id:string):Promise<CommonResult> => {
  let resp: AxiosResponse<CommonResult> = await axiosInstance.get(`/user/priviledge/current/${id}/statistics`)
  return resp.data
}

// 获取书籍单词列表有章节版
export const vocBookChapterDisplay = async (bookId:string,count:number,pageNum:number):Promise<CommonResult> => {
  let resp:AxiosResponse<CommonResult> = await axiosInstance.get('/user/voc/book/chapter/display',{
    params:{
      bookId:bookId,
      count:count,
      pageNum:pageNum
    }
  }) as AxiosResponse<CommonResult>
  return resp.data
}

// 获取书籍单词列表(无章节版)
export const vocBookDisplay = async (bookId:string,count:number,pageNum:number):Promise<CommonResult> => {
  let resp:AxiosResponse<CommonResult> = await axiosInstance.get('/user/voc/book/display',{
    params:{
      bookId:bookId,
      count:count,
      pageNum:pageNum
    }
  })
  return resp.data
}

// /user/voc/bound/statistics
export const vocBoundStatistics = async ():Promise<CommonResult> => {
  let resp:AxiosResponse<CommonResult> = await axiosInstance.get('/user/voc/bound/statistics')
  return resp.data
}
// /user/voc/bound/detail
export const vocBoundDetail = async ():Promise<CommonResult> => {
  let resp:AxiosResponse<CommonResult> = await axiosInstance.get('/user/voc/bound/detail',{
    params:{
      count:1000,
      keyword:'',
      pageNumber:1
    }
  })
  return resp.data
}