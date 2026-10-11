import { CalendarData } from "./CalendarData";

@Component
export struct CalendarComponent {
  // 用来显示丁目的标题
  weeksList: string[] = ['日', '一', '二', '三', '四', '五', '六'];

  // 父组件传过来的当前年和月份
  //@Link @Watch('changeDayData') currYear: number;
  //@Link @Watch('changeDayData') currMonth: number;

  @Consume currYear: number;
  @Consume currMonth: number;

  // 当前的年份和月份和日期
  @State today: Date = new Date(); // 当天
  nowYear: number = this.today.getFullYear();
  nowMonth: number = this.today.getMonth() + 1;
  nowDate: number = this.today.getDate();

  // 选中的日期格子是红色边框
  @Consume selectedDay: number

  @State offsetX: number = 0;

  @State currDate: number = new Date().getDate(); // 当前日期 用来标记今天
  @State startDay: Date = new Date(this.nowYear, this.today.getMonth(), 1) // 当前显示的月份的第一天

  // 日期上的数据，用来呈现格子的效果
  @Consume dayData: CalendarData[]

  private panOption: PanGestureOptions = new PanGestureOptions({ direction: PanDirection.Left | PanDirection.Right });

  aboutToAppear(): void {
    console.info('显示页面')
    this.selectedDay = this.nowDate;

    // 返回当天
    this.startDay = new Date(this.nowYear, this.today.getMonth(), 1)
    this.currYear = this.today.getFullYear();
    this.currMonth = this.today.getMonth() + 1;
  }




  getOffsetColumn(date: Date): number {
    // 正好对应前面需要空的列数
    return date.getDay();
  }

  build() {
    Column() {
      Text(`${this.currYear}年${this.currMonth}月`)
        .fontColor($r('app.color.words_base'))
        .fontSize(14)
        .fontWeight(FontWeight.Bold)
      Flex({
        justifyContent: FlexAlign.SpaceAround
      }) {
        ForEach(this.weeksList, (item: string) => {
          Text(item)
            .fontSize(12)
            .fontColor($r('app.color.words_base'))
        })
      }

      Divider().color($r('app.color.words_base'))
      Row() {
        GridRow({
          columns: this.weeksList.length,
          gutter: 6
        }) {
          ForEach(this.dayData, (item: CalendarData, index: number) => {
            GridCol({
              offset: index === 0 ? this.getOffsetColumn(new Date(`${this.currYear}-${this.currMonth}-01`)) : 0
            }) {
              Column() {
                Text(`${item.date}`)
                  .fontColor(Color.White)
                  .textAlign(TextAlign.Center)
                  .fontWeight(FontWeight.Medium)
                //Image($r('app.media.water_drop'))
                if (item.date === this.currDate && this.nowYear === this.currYear && this.nowMonth === this.currMonth &&
                  this.nowDate === this.currDate) {
                  Text('今天')
                    .fontSize(12)
                    .fontColor((item.isScoreA || item.isScoreB) ? Color.White :
                    $r('app.color.words_base'))
                    .width('100%')
                    .fontWeight(FontWeight.Bold)
                    .textAlign(TextAlign.Center)
                    .position({ y: 28 })
                }
              }
              .borderRadius(5)
              .width($r('app.float.calendar_size'))
              .height($r('app.float.calendar_size'))
              .justifyContent(FlexAlign.Center)
              .backgroundColor(item.isScoreA ? $r('app.color.color_score_a') :
                (item.isScoreB ? $r('app.color.color_score_b') : item.isScoreC?$r('app.color.color_score_c'):$r('app.color.color_score_d')))
              .border({
                width: 1,
                color: this.selectedDay === item.date ? $r('app.color.calendar_num_select_color') :
                Color.White
              })
              .onClick(() => {
                this.selectedDay = item.date;
              })
            }
          })
        }
        .offset({ x: this.offsetX })
        .parallelGesture(
          PanGesture(this.panOption)
            .onActionUpdate((event: GestureEvent) => {
              if (event) {
                this.offsetX = event.offsetX;
              }
            })
            .onActionEnd((event: GestureEvent) => {
              if (event) {
                animateTo({
                  curve: Curve.Smooth
                }, () => {
                  // 左右滑动
                  if (Number(event.offsetX) > 80) { // 上个月
                    this.startDay.setMonth(this.startDay.getMonth() - 1)
                  }
                  if (event.offsetX < - 80) { // 下个月
                    this.startDay.setMonth(this.startDay.getMonth() + 1)
                  }
                  const startDay = this.startDay;
                  this.currYear = startDay.getFullYear();
                  this.currMonth = startDay.getMonth() + 1;
                  this.offsetX = 0;
                })
              }
            })
        )
      }
      .backgroundColor(Color.White)
    }
  }
}