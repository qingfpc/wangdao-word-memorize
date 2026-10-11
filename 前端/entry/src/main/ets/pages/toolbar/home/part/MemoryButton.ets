import { AuthUser } from "@hw-agconnect/auth";
import {memoryAlgorithm} from "../../../../algorithm/MemoryAlgorithm";
import { userCheckin } from "../../../../api/checkin";
import { postUserReviewData } from "../../../../api/review";
import { vocDetail } from "../../../../api/voc";
import { DaoConfig } from "../../../../db/dao/DaoConfig";
import { formatDate, formatDateTime, UserVocRev, VocabularyDao } from "../../../../db/dao/VocabularyDao";
import { CommonResult } from "../../../../model/CommonResult";
import { UserReviewLog } from "../../../../model/db/UserReviewLog";
import { UserReviewStatistics } from "../../../../model/db/UserReviewStatistics";
import { SignInfo } from "../../../../model/SignInfo";
import { UserCheckinParam } from "../../../../model/UserCheckinParam";
import { UserTodayReviewData } from "../../../../model/UserTodayReviewData";
import { UserExample, VocabularyData } from "../../../../model/VocabularyData";
import { CheckinService } from "../../../../service/CheckinService";
import { ReviewLogService } from "../../../../service/ReviewLogService";
import { ReviewStatisticsService } from "../../../../service/ReviewStatisticsService";
import { VocabularyReviewService } from "../../../../service/VocabularyReviewService";
import { GlobalContext } from "../../../../util/GlobalContext";
import { systemDateTime } from "@kit.BasicServicesKit";

@Component
export struct MemoryButton {
  @State text: string = '';
  @State color: ResourceColor = '';
  @State q:number = 0;

  @Link isShowInfo: boolean
  @Consume currentVocabulary: VocabularyData
  @Consume authUser: AuthUser
  @Consume recitingVocabularyIdArray: string[]
  private dao:VocabularyDao= GlobalContext.getContext().getObject(DaoConfig.VOCABULARY_DAO) as VocabularyDao
  private reviewLogService:ReviewLogService = new ReviewLogService()

  checkinService:CheckinService = new CheckinService();
  reviewStatisticsService:ReviewStatisticsService = new ReviewStatisticsService()
  vocabularyReviewService:VocabularyReviewService = new VocabularyReviewService()

  @Consume currentVocabularyReciteStartTime:number
  @Consume userExamples: UserExample[]


  async aboutToAppear() {
    console.info('memoryButton:start')
  }
  async aboutToDisappear() {
    console.info('memoryButton:end')
  }

  build() {

    Button(this.text)
      .backgroundColor(this.color)
      .onClick(async ()=>{
        let userVocRev = this.memory(this.q)
        console.info(`更新单词情况:${JSON.stringify(userVocRev)}`)
        //todo:更新背单词情况
        userVocRev.type = 1
        this.dao.update(userVocRev)

        let reviewLog = this.generateReviewLog()
        console.info(`reviewLog:${JSON.stringify(reviewLog)}`)
        let currentVocabularyReciteEndTime = systemDateTime.getTime()
        let studyTime = currentVocabularyReciteEndTime - this.currentVocabularyReciteStartTime
        reviewLog.studyTime = studyTime
        await this.reviewLogService.createReviewLog(reviewLog)

        let array:string[] = []
        console.info(`去除前:${JSON.stringify(this.recitingVocabularyIdArray)}`)
        this.recitingVocabularyIdArray.forEach(item=>{
          if (item !== this.currentVocabulary.id) {
            array.push(item)
          }
        })
        console.info(`去除id:${this.currentVocabulary.id}`)
        this.recitingVocabularyIdArray = array
        console.info(`去除后:${JSON.stringify(this.recitingVocabularyIdArray)}`)
        if (this.recitingVocabularyIdArray.length === 0) {
          console.info('背诵结束')
          this.completeToday()
        }

        // 更新current
        if (this.recitingVocabularyIdArray.length > 0) {
          const vocId = this.recitingVocabularyIdArray[0]
          //this.currentVocabulary = dao.find(this.authUser.getUid(),vocId)
          let result = await vocDetail(vocId) as CommonResult
          if (result.code === 200) {
            let data = result.data as VocabularyData
            if (data) {
              this.currentVocabulary = data
            }
          }
        }

        this.isShowInfo = false
        //this.userExamples = []
        this.userExamples = this.currentVocabulary.userExamples
      })
  }


  checkIn = async ()=>{
    let now = new Date()
    let month = (now.getMonth() + 1)/10==1?(now.getMonth() + 1):'0'+(now.getMonth() + 1)
    let param = {
      type: 1,
      signDate: now.getFullYear() + '-' + month + '-' + now.getDate()
    } as UserCheckinParam
    let result = await userCheckin(param)

    if (result.code === 200) {
      let signInfo = result.data as SignInfo
      this.checkinService.checkin(this.authUser.getUid(), signInfo.userId, 1)
      // todo: saveCheckin 本地存储签到信息
    } else {
      AlertDialog.show({ message: result.message, title: '错误' })
    }
  }

  completeToday() {
    // 签到
    this.checkIn()
      .then(()=>{
        console.info(`签到成功`)
        AlertDialog.show({ message: `自动签到成功` })

      })

    // 生成统计情况
    let reviewStatistics = this.generateReviewStatistics(this.authUser.getUid())

    // 保存统计情况
    this.reviewStatisticsService.createReviewStatistics(reviewStatistics)

    // 同步到后端
    let todayReviewData:UserTodayReviewData = this.generateTodayReviewData(reviewStatistics)
    postUserReviewData(todayReviewData)
      .then((result)=>{
        console.info('同步成功')
        //todo: 更新单词状态
      })

  }

  generateTodayReviewData(reviewStatistics: UserReviewStatistics): UserTodayReviewData {
    let arr = this.vocabularyReviewService.queryTodayReviewVocabulary(this.authUser.getUid())

    let data = {
      revStatisticsCommand:reviewStatistics,
      vocRevCommands:arr
    } as UserTodayReviewData
    return data;
  }

  /**
   * id: string; //
   userId: string; // 用户id
   vocNewCount: number; // 复习的旧单词数量
   vocReviewCount: number; // 复习的新单词数量
   totalCount: number; // 复习的单词总数量
   forgetCount: number; // 遗忘单词数量
   uncertainCount: number; // 模糊单词数量
   knowCount: number; // 认识单词数量
   simpleCount: number; // 简单单词数量
   studyTime: string; // 学习时间ms为单位
   reviewDate: string; // 复习日期
   * @param userId
   * @returns
   */
  generateReviewStatistics(userId: string) {

    // todo: 暂时生成数据
    let familiarityMap:[number,number,number][] = this.reviewLogService.getTodayFamiliarity(userId)

    let forgetCount = 0
    let uncertainCount = 0
    let knowCount = 0
    let simpleCount = 0
    let totalStudyTime = 0
    for (let index = 0; index < familiarityMap.length; index++) {
      const element = familiarityMap[index];
      totalStudyTime += element[2]
      if (element[0] === 0) {
        forgetCount = element[1]
      } else if (element[0] === 1) {
        uncertainCount = element[1]
      } else if (element[0] === 2) {
        knowCount = element[1]
      } else if (element[0] === 3) {
        simpleCount = element[1]
      }

    }
    let studyTime = totalStudyTime.toString()
    console.info(`studyTime:${studyTime}`)
    let totalCount = forgetCount + uncertainCount + knowCount + simpleCount
    let reviewStatistics = {
      userId:userId,
      vocNewCount:0,
      vocReviewCount:0,
      totalCount:totalCount,
      forgetCount:forgetCount,
      uncertainCount:uncertainCount,
      knowCount:knowCount,
      simpleCount:simpleCount,
      studyTime: totalStudyTime.toString(),
      reviewDate:formatDate(new Date())
    } as UserReviewStatistics
    return reviewStatistics;
  }

  generateReviewLog():UserReviewLog {
    let dateStr = formatDateTime(new Date())
    let reviewLog:UserReviewLog = {
      userId:this.authUser.getUid(),
      vocId:this.currentVocabulary.id,
      vocContent:this.currentVocabulary.content,
      familiarity:this.q,
      createTime:dateStr,
      isDeleted:0

    } as UserReviewLog
		return reviewLog;
	}

  // 点击不同的按钮，更新q和newInterval、newFactor
  memory(q:number): UserVocRev{
    // 提供参数voc_id,user_id
    //console.info(uid)

    let vocRev = this.dao.find(this.authUser.getUid(),this.currentVocabulary.id)

    vocRev = memoryAlgorithm.newInterval(vocRev,q)
    // 设置最近一次背诵的熟悉度
    vocRev.familiarity = q
    return vocRev;
  }
}