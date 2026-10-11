import { Constants } from "../../../common/Constants"
import { SettingItem } from "../../../model/SettingItem"
import { PromptActionClass } from "../../../util/dialog/PromptActionClass"
import { ComponentContent } from "@kit.ArkUI"
import { settingDialog } from "../../../util/dialog/SettingDialog"

@Component
export struct SettingCell {
  @Consume('settingPathStack') settingPathStack: NavPathStack
  @Link item: SettingItem
  @Link info: number | string

  build() {
    ListItem() {
      Row() {
        Row({ space: Constants.COMMON_SPACE }) {
          if (this.item.img !== undefined) {
            Image(this.item.img)
              .width($r('app.float.setting_size'))
              .height($r('app.float.setting_size'))
          }
          Text(this.item.title)
            .fontSize($r('app.float.normal_text_size'))
        }

        if (this.item.others === undefined) {
          Row() {
            Column() {
              Text(`${this.item.info}`)
                .fontSize('16fp')
                .fontColor($r('app.color.start_window_background'))
            }
            .width('60vp')
            .height('70%')
            .backgroundColor(Color.Blue)
            .justifyContent(FlexAlign.Center)
            .borderRadius(10)
            .margin({ right: '5vp' })

            Image($r('app.media.right_grey'))
              .width($r('app.float.setting_jump_width'))
              .height($r('app.float.setting_jump_height'))
          }
          .width('30%')
          .height(Constants.PERCENT_100)
          .justifyContent(FlexAlign.End)

        } else {
          Toggle({ type: ToggleType.Switch, isOn: false })
        }
      }
      .justifyContent(FlexAlign.SpaceBetween)
      .width(Constants.PERCENT_100)
      .padding({
        left: $r('app.float.setting_settingCell_left'),
        right: $r('app.float.setting_settingCell_right')
      })
      .onClick(() => {
        if (this.item.title === '购买单词上限'){
          console.info('paypaypay')
          this.settingPathStack.pushPathByName('pay','')
          return
        }
        else if (this.item.title === '现单词上限总量'){
          this.settingPathStack.pushPathByName('VocBoundStat','')
          return
        }
        if (this.item.onClick) {
          let self = this.item
          let node: ComponentContent<SettingItem> =
            new ComponentContent(this.getUIContext(), wrapBuilder(settingDialog), self)
          PromptActionClass.setContentNode(node)
          PromptActionClass.openDialog()
        }
      })
    }
    .height($r('app.float.setting_list_height'))

  }
}
