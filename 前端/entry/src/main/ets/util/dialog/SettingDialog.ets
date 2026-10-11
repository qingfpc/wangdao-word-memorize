import { SettingItem } from '../../model/SettingItem'
import { parseDate } from '../dateUtil'

@Builder
export function settingDialog(item: SettingItem) {
  Column() {
    Text(item.title)
      .fontWeight(FontWeight.Bold)
      .margin({ top: 10, bottom: 10 })
    if (item.title == '学习提醒') {
      TimePicker({ selected: parseDate(item.info as string), format: TimePickerFormat.HOUR_MINUTE })
        .onChange((value) => {
          let hour = value.hour
          let minute = value.minute
          let hourStr = hour.toString()
          if (hour < 10) {
            hourStr = '0' + hour
          }
          let minuteStr = minute.toString()
          if (minute < 10) {
            minuteStr = '0' + minute
          }
          item.tmp = hourStr + ':' + minuteStr
          //item.info = value
        })
        .useMilitaryTime(true)
    } else {
      TextInput({ text: `${item.tmp}` })
        .type(InputType.Normal)
        .fontSize('15fp')
        .textAlign(TextAlign.Center)
        .width(100)
        .margin({ bottom: 10 })
        .onChange((value: string | number) => {
          item.tmp = value
          //item.info = value
        })
    }
    Button('保存')
      .onClick(async () => {
        if (item.onClick) {
          let self = item
          item.saved = await item.onClick(self) as boolean
          if (item.saved) {
            if (item.tmp) {
              item.info = item.tmp
              item.saved = false
            }
          }
        }
      })
      .width(100)
      .margin({ bottom: 10 })
  }
  .width(300)
  .backgroundColor(Color.White)
  .borderRadius(10)
}