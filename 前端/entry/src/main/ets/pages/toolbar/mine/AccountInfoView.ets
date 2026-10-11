import { Constants } from "../../../common/Constants"
import { MineInfo } from "../../../model/MineInfo"

@Component
export struct AccountInfoView {
  @Prop mineInfo:MineInfo
  @Prop levelUpRemainDays:number = 0
  routeUserInfo?:()=>void

  build(){

    Row() {
      Column() {
        //Image($r('app.media.account'))
        Image(this.mineInfo.headImg)
          .width($r('app.float.setting_account_size'))
          .height($r('app.float.setting_account_size'))
          .borderRadius(8)
      }
      .width('20%')


      Column() {
        // 名字
        Text(`${this.mineInfo.nickName}`)
          .fontSize($r('app.float.setting_account_fontSize'))
          .width(Constants.PERCENT_100)
          .textAlign(TextAlign.Start)
          .fontWeight(FontWeight.Bold)
          .letterSpacing(1)
        // 级别
        Row() {
          Text(`${this.mineInfo.levelName}`)
            .backgroundColor(Color.Blue)
            .fontColor($r('app.color.start_window_background'))
            .fontSize($r('app.float.setting_level_fontSize'))
            .padding(5)
            .borderRadius(5)
          Text(`再签到 ${this.levelUpRemainDays} 天升级 >`)
            .fontSize($r('app.float.setting_level_fontSize'))
            .padding('5vp')
            .fontColor($r('app.color.login_more_text_color'))
            .width('50%')
        }
        .width(Constants.PERCENT_100)
        //.width('250vp')
        .margin({ top: '5vp' })
        .justifyContent(FlexAlign.SpaceBetween)

        // 升级时间
        Row() {
          Slider({
            value: this.mineInfo.levelValue,
            min: 0,
            max: 100,
            style: SliderStyle.NONE,
          })
            .height('12vp')
            .onChange((value, model) => {
              this.mineInfo.levelValue = value
            })
            .sliderInteractionMode(SliderInteraction.SLIDE_ONLY)

        }
        .width(Constants.PERCENT_100)
        .justifyContent(FlexAlign.Center)

      }
      .height($r('app.float.setting_account_size'))
      .width('80%')
      .onClick(()=>{
        if(this.routeUserInfo){
          this.routeUserInfo()
        }
     })


    }
    .width(Constants.PERCENT_100)
    .height($r('app.float.setting_account_size'))
  }
}
