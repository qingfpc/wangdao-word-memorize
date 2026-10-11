
// 支付弹窗组件
@CustomDialog
@Preview
export struct PayDialog{

  controller: CustomDialogController
  title: string = '选择支付方式'
  amount: number = 0
  onWechatPay: () => void = () => {}
  onAlipay: () => void = () => {}



  build() {
    Column() {
      // 标题区域
      Text(this.title)
        .fontSize(18)
        .fontColor('#1a1a1a')
        .fontWeight(FontWeight.Bold)
        .margin({ top: 24, bottom: 16 })

      // 金额显示
      Text(`${this.amount.toFixed(2)}`)
        .fontSize(24)
        .fontColor('#ff5722')
        .fontWeight(FontWeight.Bold)
        //.margin({ bottom: 24 })

      // 支付方式选择
      Column() {
        // 微信支付
        Row() {
          Image($r('app.media.ic_wechat'))
            .width(32)
            .height(32)
            .margin({ right: 16 })

          Column() {
            Text('微信支付')
              .fontSize(16)
              .fontColor('#333')
            Text('推荐微信用户使用')
              .fontSize(12)
              .fontColor('#999')
          }
          .layoutWeight(1)

          Image($r('app.media.radio_selected'))
            .fillColor($r('app.color.words_base'))
            .width(20)
            .height(20)
        }
        .padding(16)
        .backgroundColor('#f5f7fa')
        .borderRadius(12)
        .onClick(() => {
          this.controller.close()
          this.onWechatPay()
        })
        .margin({ bottom: 12 })

        // 支付宝支付
        /*Row() {
          Image($r('app.media.ic_alipay'))
            .width(32)
            .height(32)
            .margin({ right: 16 })

          Column() {
            Text('支付宝支付')
              .fontSize(16)
              .fontColor('#333')
            Text('推荐支付宝用户使用')
              .fontSize(12)
              .fontColor('#999')
          }
          .layoutWeight(1)

          Image($r('app.media.radio_unselect'))
            .width(20)
            .height(20)
        }
        .padding(16)
        .backgroundColor('#f5f7fa')
        .borderRadius(12)
        .onClick(() => {
          this.controller.close()
          this.onAlipay()
        })*/
      }
      .width('100%')
      .padding(24)

      // 取消按钮
      Button('取消支付')
        .fontSize(16)
        .fontColor('#666')
        .backgroundColor(Color.Transparent)
        .margin({ top: 24, bottom: 16 })
        .onClick(() => this.controller.close())
    }
    //.padding(24)
    .backgroundColor('#ffffff')
    /*.borderRadius({
      topLeft:0,
      topRight:0,
      bottomLeft:0,
      bottomRight:0
    })*/
    .width('100%')
    //.shadow({ radius: 24, color: '#40000000', offsetX: 0, offsetY: -8 })
  }
}