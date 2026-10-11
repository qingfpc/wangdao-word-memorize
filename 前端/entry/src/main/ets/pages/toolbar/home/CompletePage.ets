
@Component
export struct CompletePage{

  @BuilderParam option: () => void

  build() {
    Column(){
      this.option()
      Text('您已完成全部单词背诵')
        .height(50)
        .margin({top:300})
      Text('点击上方选项可以答题')
        .height(50)
    }
    .width('100%')
    .height('100%')
    .justifyContent(FlexAlign.Start)
  }


}