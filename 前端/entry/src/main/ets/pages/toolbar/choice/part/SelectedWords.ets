@Component
export struct SelectedWords{
  @Consume wordsChoicePathStack: NavPathStack

  @Builder leftColumn(){
    Column(){
      Image($r("app.media.collection_fill"))
        .height(20)
        .fillColor($r('app.color.words_base'))
    }.width('8%')
    .alignItems(HorizontalAlign.Center)
    .justifyContent(FlexAlign.Center)
  }
  @Builder centerColumn(){
    Column(){
      Text('已选单词')
        .fontWeight(FontWeight.Medium)
        .fontSize('18vp')
        .width('95%')
        .textAlign(TextAlign.Start)
        .height('20vp')
      Row(){
        //Text(`全部 | 今日 | 顽固 | 熟知 | 未按规划`)
        Text(`全部 | 今日`)
          .fontSize($r('app.float.current_book_status_font_size'))
          .fontColor($r('app.color.current_book_status_font_color'))

      }.width('100%')
      .height(25)

    }.width('84%')
    .onClick(()=>{
      this.wordsChoicePathStack.pushPathByName('SelectedWordsPage','')
    })

  }
  @Builder rightColumn(){
    Column(){

    }.width('8%')
  }

  build() {
    Row(){
      this.leftColumn()
      this.centerColumn()
      this.rightColumn()
    }.justifyContent(FlexAlign.Start)
    .alignItems(VerticalAlign.Top)
    .width('100%')
  }
}