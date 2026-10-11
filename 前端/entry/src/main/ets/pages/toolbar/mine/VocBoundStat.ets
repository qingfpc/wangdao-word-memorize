import { vocBoundDetail, vocBoundStatistics } from "../../../api/voc"
import { PageDataModel } from "../../../model/PageDataModel"
import { VocBoundStatModel } from "../../../model/VocBoundStatModel"
import { VocDetailModel } from "../../../model/VocDetailModel"


@Component
export struct VocBoundStat {
  @Consume('settingPathStack') settingPathStack: NavPathStack
  @State statModel: VocBoundStatModel = {} as VocBoundStatModel
  @State detailList: VocDetailModel[] = []

  build() {
    NavDestination() {
      Column() {
        // 顶部统计卡片
        Row() {
          // 总单词上限卡片
          Column() {
            Text('总单词上限')
              .fontSize(14)
              .fontColor(Color.White)
              .opacity(0.8)
            Text(`${this.statModel.total}`)
              .fontSize(24)
              .fontColor(Color.White)
              .margin({ top: 6 })
          }
          .padding(12)
          .backgroundColor('#5e7ce0')
          .borderRadius(12)
          .layoutWeight(1)
          .height(75)

          // 已占用单词卡片
          Column() {
            Text('已学单词占用')
              .fontSize(14)
              .fontColor(Color.White)
              .opacity(0.8)
            Text(`${this.statModel.occupied}`)
              .fontSize(24)
              .fontColor(Color.White)
              .margin({ top: 6 })
          }
          .padding(12)
          .backgroundColor('#f36f64')
          .borderRadius(12)
          .layoutWeight(1)
          .height(75)
          .margin({ left: 10 })

          // 可用单词卡片
          Column() {
            Text('剩余可用')
              .fontSize(14)
              .fontColor(Color.White)
              .opacity(0.8)
            Text(`${this.statModel.available}`)
              .fontSize(24)
              .fontColor(Color.White)
              .margin({ top: 6 })
          }
          .padding(12)
          .backgroundColor('#50c4a9')
          .borderRadius(12)
          .layoutWeight(1)
          .height(75)
          .margin({ left: 10 })
        }
        .padding({ top: 16, left: 16, right: 16 })

        // 说明区域
        Column() {
          Text('单词上限说明')
            .fontSize(16)
            .fontColor('#333')
            .fontWeight(FontWeight.Bold)
            .margin({ bottom: 8 })
          Text('单词上限用于规划记忆的单词总量，可以用来消耗，比如AI阅读灯功能')
            .fontSize(14)
            .fontColor('#666')
            .lineHeight(20)
        }
        .padding(16)
        .backgroundColor('#f9f9f9')
        .borderRadius(12)
        .margin({ top: 16, left: 16, right: 16 })

        // 来源统计卡片
        Row() {
          // 免费获得卡片
          Column() {
            Text('免费获得')
              .fontSize(14)
              .fontColor('#333')
            Text(`${this.statModel.free}`)
              .fontSize(18)
              .fontColor('#5e7ce0')
              .margin({ top: 4 })
          }
          .padding(12)
          .layoutWeight(1)
          .borderRadius(8)
          .backgroundColor('#f0f3ff')

          // 购买获得卡片
          Column() {
            Text('购买')
              .fontSize(14)
              .fontColor('#333')
            Text(`${this.statModel.pay}`)
              .fontSize(18)
              .fontColor('#f36f64')
              .margin({ top: 4 })
          }
          .padding(12)
          .layoutWeight(1)
          .borderRadius(8)
          .backgroundColor('#f0f3ff')
          .margin({ left: 10 })

          // 兑换消耗卡片
          Column() {
            Text('兑换消耗')
              .fontSize(14)
              .fontColor('#333')
            Text(`${this.statModel.exchange}`)
              .fontSize(18)
              .fontColor('#50c4a9')
              .margin({ top: 4 })
          }
          .padding(12)
          .layoutWeight(1)
          .borderRadius(8)
          .backgroundColor('#f0f3ff')
          .margin({ left: 10 })
        }
        .padding({ top: 16, left: 16, right: 16 })

        // 明细列表标题
        Row() {
          Text('明细记录')
            .fontSize(18)
            .fontColor('#333')
            .fontWeight(FontWeight.Bold)
        }
        .padding({ top: 20, left: 16, right: 16, bottom: 10 })
        .width('100%')

        // 明细列表
        List() {
          ForEach(this.detailList, (item: VocDetailModel) => {
            ListItem() {
              Column() {
                Row() {
                  // 类型标签
                  Text(`${item.type === 1 ? '充值' : item.type === 2 ? '签到' : '消费'}`)
                    .fontSize(14)
                    .padding({ top: 8,bottom:8, left: 4,right:4 })
                    .backgroundColor(item.type === 1 ? '#e6f7ff' : item.type === 2 ? '#f6ffed' : '#fff2e8')
                    .borderColor(item.type === 1 ? '#91d5ff' : item.type === 2 ? '#b7eb8f' : '#ffbb96')
                    .borderRadius(4)
                    .borderWidth(1)
                    .fontColor(item.type === 1 ? '#1890ff' : item.type === 2 ? '#52c41a' : '#fa8c16')

                  // 数量
                  Text(`${item.exchangeCount > 0 ? '+' : ''}${item.exchangeCount}`)
                    .fontSize(16)
                    .fontColor(item.exchangeCount > 0 ? '#52c41a' : '#f5222d')
                    .margin({ left: 12 })

                  // 描述（自动填充剩余空间）
                  Text(`${item.description}`)
                    .fontSize(14)
                    .fontColor('#666')
                    .layoutWeight(1)
                    .margin({ left: 12 })
                    .textOverflow({ overflow: TextOverflow.Ellipsis })
                    .maxLines(1)
                }
                .width('100%')
                .margin({ top: 8 })

                // 时间
                Row() {
                  Text(`${item.logDate}`)
                    .fontSize(12)
                    .fontColor('#999')
                }
                .width('100%')
                .margin({ top: 6, bottom: 8 })
              }
              .padding({ left: 16, right: 16 })
              .backgroundColor(Color.White)
            }
            .borderRadius(8)
            .margin({ top: 6, bottom: 6, left: 16, right: 16 })
            .shadow({ radius: 2, color: '#00000010', offsetX: 0, offsetY: 1 })
          })
        }
        .width('100%')
        .layoutWeight(1)
        .margin({ top: 10 })
      }
      .backgroundColor('#f0f2f5')
      .height('100%')
      .width('100%')
    }
    .title('单词上限')
    .onWillAppear(async () => {
      let result = await vocBoundStatistics()
      this.statModel = result.data as VocBoundStatModel

      let result2 = await vocBoundDetail()
      let pageData = result2.data as PageDataModel<VocDetailModel>
      this.detailList = pageData.pageList

      console.info(JSON.stringify(this.statModel))
      console.info(JSON.stringify(this.detailList))
    })
  }
}