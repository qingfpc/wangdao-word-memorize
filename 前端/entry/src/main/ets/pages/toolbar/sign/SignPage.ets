import { CalendarComponent } from "./CalendarComponent";
import { descriptions, SignResultDescription } from "./SignResultDescription";


/*@Component
export struct SignPage{
  build() {
    Text('todo')
  }
}*/
@Component
export struct SignPage{
  // 当前年份
  @Consume currYear: number
  // 当前月份
  @Consume currMonth: number


  // 成绩的描述信息
  @State descriptions: SignResultDescription[] = descriptions;
  async aboutToAppear() {
  }

  build() {
    Column(){
      CalendarComponent()
      Divider().color($r('app.color.words_base'))
      Row({ space: 12 }) {
        ForEach(this.descriptions, (item: SignResultDescription) => {
          Row({ space: 6 }) {
            Column().width(10).height(10)
              .backgroundColor(item.color)
              .borderRadius(3)
            Text(item.title)
              .fontSize(12)
              .fontColor($r('app.color.words_base_light'))
          }
        })

      }
      .backgroundColor($r('app.color.start_window_background'))
      .padding(12)
      .width('100%')
    }.width('100%')
  }
}

