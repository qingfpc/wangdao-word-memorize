import { userCheckin } from "../../../api/checkin"
import { Constants } from "../../../common/Constants"
import { CommonResult } from "../../../model/CommonResult"
import { SignInfo } from "../../../model/SignInfo"
import { UserCheckinParam } from "../../../model/UserCheckinParam"

// 签到信息
@Component
export struct SignInfoView {
  @Consume('settingPathStack') settingPathStack: NavPathStack
  @Consume signInfo: SignInfo



  @Builder
  divider() {
    Column()
      .height('48vp')
      .width('1vp')
      .backgroundColor($r('app.color.divider'))
  }
  build() {
    Row() {
      // 签到
      Column() {
        Text('去签到')
          .fontSize($r('app.float.setting_level_fontSize'))
          .fontColor($r('app.color.login_more_text_color'))
      }
      .layoutWeight(1)
      .onClick(()=>{
        this.settingPathStack.pushPathByName('checkin','')
      })

      /*.onClick(async  ()=>{
        let result:CommonResult = await this.checkIn(1)
        if (result.code === 200) {
          this.signInfo = result.data as SignInfo
          AlertDialog.show({message:result.message})
          // todo: saveCheckin 本地存储签到信息
        }else {
          AlertDialog.show({message:result.message,title:'错误'})
        }
      })*/
      this.divider()
      // 连续签到
      Column() {
        Text(`${this.signInfo.curContinuousDays}`)
          .fontWeight(FontWeight.Bold)
        Text('连续签到')
          .fontSize($r('app.float.setting_level_fontSize'))
          .fontColor($r('app.color.login_more_text_color'))
      }
      .layoutWeight(1)

      this.divider()
      // 连续签到
      Column() {
        Text(`${this.signInfo.maxContinuousDays}`)
          .fontWeight(FontWeight.Bold)
        Text('最大连续签到')
          .fontSize($r('app.float.setting_level_fontSize'))
          .fontColor($r('app.color.login_more_text_color'))
      }
      .layoutWeight(1)

      this.divider()
      // 累计签到
      Column() {
        Text(`${this.signInfo.totalDays}`)
          .fontWeight(FontWeight.Bold)
        Text('累计签到')
          .fontSize($r('app.float.setting_level_fontSize'))
          .fontColor($r('app.color.login_more_text_color'))
      }
      .layoutWeight(1)

      // 累计学习分钟
      this.divider()
      Column() {
        Text(`${Math.floor(this.signInfo.totalTime)}`)
          .fontWeight(FontWeight.Bold)
        Text('累计学习分钟')
          .fontSize($r('app.float.setting_level_fontSize'))
          .fontColor($r('app.color.login_more_text_color'))
      }
      .layoutWeight(1)
    }
    .width(Constants.PERCENT_100)
    .height('60vp')
    .backgroundColor($r('app.color.setting_button_backgroundColor'))
    .borderRadius(12)
    .justifyContent(FlexAlign.SpaceBetween)

  }
}