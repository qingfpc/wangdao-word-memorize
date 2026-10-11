import { PayDialog } from "./PayDialog"

// 支付弹窗组件
@CustomDialog
@Preview
export struct PayConfirmDialog {
  // 控制器
  controller?: CustomDialogController
  // 可配置参数
  title: string = '提示'
  content: string = ''
  privilegeStr:string = ''
  confirmText: string = '确定'
  cancelText: string = '取消'
  onConfirm: () => void = () => {}
  onCancel: () => void = () => {}

  // 构造参数接口
  // constructor(title?: string,
  //   content?: string,
  //   confirmText?: string,
  //   cancelText?: string,
  //   onConfirm?: () => void,
  //   onCancel?: () => void
  // ) {
  //   super()
  //   this.controller = new CustomDialogController({
  //     builder: this,
  //     cancel: onCancel,
  //     alignment: DialogAlignment.Center
  //   })
  //   if (title) this.title = title
  //   if (content) this.content = content
  //   if (confirmText) this.confirmText = confirmText
  //   if (cancelText) this.cancelText = cancelText
  //   if (onConfirm) this.onConfirm = onConfirm
  //   if (onCancel) this.onCancel = onCancel
  // }

  build() {
    Column() {
      // 标题区域
      Text(this.title)
        .fontSize(18)
        .fontColor('#1a1a1a')
        .fontWeight(FontWeight.Bold)
        .margin({ top: 24, bottom: 16 })

      // 内容区域
      Scroll() {
        Column(){
          Text(this.content)
          Text(this.privilegeStr)
            .fontSize(16)
            .fontColor('#ffaa00')
            .fontWeight(FontWeight.Bold)
        }
        .padding({ left: 24,right:24 })
      }
      .height(120)
      .width('100%')

      // 操作按钮区域
      Row() {
        Button(this.cancelText)
          .fontSize(14)
          .fontColor('#666')
          .backgroundColor('#f5f5f5')
          .padding(12)
          .borderRadius(8)
          .onClick(() => {
            this.controller!.close()
            this.onCancel()
          })
          .margin({ right: 16 })

        Button(this.confirmText)
          .fontSize(14)
          .fontColor('#ffffff')
          .backgroundColor($r('app.color.words_base'))
          .padding(12)
          .borderRadius(8)
          .onClick(() => {
            this.controller!.close()
            this.onConfirm()

          })
      }
      .justifyContent(FlexAlign.Center)
      .margin({ top: 24, bottom: 24 })
    }
    .backgroundColor('#ffffff')
    .borderRadius(20)
    .width('100%')
    .shadow({ radius: 24, color: '#40000000', offsetX: 0, offsetY: 8 })
  }



}