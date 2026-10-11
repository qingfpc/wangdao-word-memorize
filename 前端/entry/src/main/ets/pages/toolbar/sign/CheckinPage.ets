import { userCheckin, userCheckinCard } from "../../../api/checkin"
import { Constants } from "../../../common/Constants"
import { CommonResult } from "../../../model/CommonResult"
import { SignInfo } from "../../../model/SignInfo"
import { UserCheckinParam } from "../../../model/UserCheckinParam"
import { SignPage } from "./SignPage"
import { CalendarData } from "./CalendarData"
import { CheckinService } from "../../../service/CheckinService"
import { AuthUser } from "@hw-agconnect/auth"
import { CheckinItem } from "../../../api/data/CheckinItem"
import { UserCheckin } from "../../../model/db/UserCheckin"
import axiosInstance from "../../../api/axios"

@Component
export struct CheckinPage {

  @Consume('settingPathStack') settingPathStack: NavPathStack
  @Consume signInfo: SignInfo

  @State canReCheckin:boolean = true
  @Provide @Watch('changeDay') selectedDay: number = 0;

  @State selectedDate:string = ''


  @Consume("authUser") authUser: AuthUser
  @Provide @Watch('changeDayData') currYear: number = new Date().getFullYear();
  @Provide @Watch('changeDayData') currMonth: number = new Date().getMonth() + 1;
  currentMonthCheckinArr:UserCheckin[] = []
  // 签到数据
  @Provide dayData: CalendarData[] = []

  checkinService:CheckinService = new CheckinService();

  // 重新拼接选中的日期
  changeDay(){
    this.selectedDate = `${this.currYear}-${this.currMonth < 10? '0'+this.currMonth:this.currMonth}-${this.selectedDay < 10? '0'+this.selectedDay:this.selectedDay}`
  }

  // 当年份和月份发生变化的时候，重新获取数据
  changeDayData() {
    console.info(`进入签到页面${this.currYear}-${this.currMonth}:${this.authUser.getUid()}`)
    this.currentMonthCheckinArr = this.checkinService.queryCurrentPageData(this.currYear, this.currMonth,this.authUser.getUid())
    console.info('当前用户当前签到月份签到数据：' + JSON.stringify(this.currentMonthCheckinArr))
    this.getDayData(this.currYear, this.currMonth,this.currentMonthCheckinArr);
  }
  getDayData(year: number, month: number,arr:UserCheckin[]) {
    const map = new Map<string, number>();
    for (let i = 0; i < arr.length; i++) {
      const element = arr[i];
      // 只获取日期中的日
      const day = element.signDate.split('-')[2];
      map.set(day, element.type);
    }

    this.dayData = [];
    // 这个月有多少天
    const dayNum = new Date(year, month, 0).getDate();
    let data = []
    // todo: dayData来源于数据库
    for (let i = 1; i <= dayNum; i++) {
      // 判断当前日期是否有签到记录
      let isScoreA = false
      let isScoreB = false
      let dayStr = i < 10 ? `0${i}` : `${i}`
      if (map.has(dayStr)) {
        let type = map.get(dayStr)
        if (type === 1) {
          isScoreA = true
        }else if (type === 2) {
          isScoreB = true
        }
      }
      const obj: CalendarData = {
        isScoreA: isScoreA,
        isScoreB: isScoreB,
        isScoreC: false,
        isScoreD: false,
        isScoreE: false,
        isScoreF: false,
        isScoreG: false,
        date: i
      }

      this.dayData.push(obj);
    }
  }

  aboutToAppear(): void {

    this.changeDayData();
  }

  async checkIn():Promise<CommonResult>{
    let now = new Date()
    let month = (now.getMonth() + 1)/10==1?(now.getMonth() + 1):'0'+(now.getMonth() + 1)

    let date = (now.getDate() )/10>=1?(now.getDate() ):'0'+(now.getDate())
    let param = {
      type: 1,
      signDate: now.getFullYear() + '-' + month + '-' +  date
    } as UserCheckinParam
    let result = await userCheckin(param)
    return result
  }
  /*async reCheckIn(date:string):Promise<CommonResult>{
    let param = {
      type: 2,
      signDate: date
    } as UserCheckinParam
    let result = await userCheckin(param)
    return result
  }*/

  build() {
    NavDestination(){
      SignPage()
      Row(){
        Button('签到')
          .onClick(async  ()=>{
            try {
              let result: CommonResult = await this.checkIn()
              if (result.code === 200) {
                this.signInfo = result.data as SignInfo
                this.checkinService.checkin(this.authUser.getUid(), this.signInfo.userId, 1)
                this.changeDayData()
                AlertDialog.show({ message: result.message })
                // todo: saveCheckin 本地存储签到信息
              } else {
                AlertDialog.show({ message: result.message, title: '错误' })
              }
            } catch (e) {
              console.error(e)
            }
        })
        Button('补签')
          .enabled(this.canReCheckin)
          .backgroundColor(this.canReCheckin?Color.Orange:Color.Grey)
          .onClick(async  ()=>{
            /*if (this.selectedDate == '') {
              AlertDialog.show({message:'请选择补签日期'})
              return
            }*/
            let result:CommonResult = await userCheckinCard()
            if (result.code === 200) {
              //this.signInfo = result.data as SignInfo
              this.changeDayData()
              AlertDialog.show({message:result.message})
              // todo: saveCheckin 本地存储签到信息
            }else {
              AlertDialog.show({message:result.message,title:'错误'})
            }
        })
      }
      .width(Constants.PERCENT_100)
      .justifyContent(FlexAlign.SpaceEvenly)
    }
  }
}