import lottie, { AnimationItem } from "@ohos/lottie"

// 支付弹窗组件
@CustomDialog
@Preview
export struct LoadingDialog{

  controller: CustomDialogController
  message: string = "支付中..."

  build() {
    Column(){
      Text(`${this.message}`)
        .fontSize(20)
        .fontWeight(FontWeight.Bold)
        .textAlign(TextAlign.Center)
        .margin({
          top: 20,
          bottom: 20
        })
      Canvas(this.canvasRenderingContext)
        .width(200)
        .height(200)
        .onReady(() => {
          // 加载动画
          if (this.animateItem != null) {
            // 可在此生命回调周期中加载动画，可以保证动画尺寸正确
            this.animateItem?.resize();
          } else {
            // 抗锯齿的设置
            this.canvasRenderingContext.imageSmoothingEnabled = true;
            this.canvasRenderingContext.imageSmoothingQuality = 'medium'
            this.loadAnimation();
          }
        })
    }
    .backgroundColor('#ffffff')
    .width('100%')
  }
  // 构建上下文
  private renderingSettings: RenderingContextSettings = new RenderingContextSettings(true)
  private canvasRenderingContext: CanvasRenderingContext2D = new CanvasRenderingContext2D(this.renderingSettings)
  private animateItem: AnimationItem | null = null;
  private animateName: string = "loading"; // 动画名称
  loadAnimation() {
    this.animateItem = lottie.loadAnimation({
      container: this.canvasRenderingContext,
      renderer: 'canvas', // canvas 渲染模式
      loop: true,
      autoplay: false,
      name: this.animateName,
      contentMode: 'Contain',
      path: "common/lottie/loading.json",
    })
    // 因为动画是异步加载，所以对animateItem的操作需要放在动画加载完成回调里操作
    this.animateItem.addEventListener('DOMLoaded', (args: Object): void => {
      this.animateItem!.changeColor([51, 26, 254, 1]);
      this.animateItem!.play()
    });
  }

  destroy() {
    this.animateItem!.removeEventListener("DOMLoaded");
    lottie.destroy(this.animateName);
    this.animateItem = null;
  }
}