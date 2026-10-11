import { SelectedBookInfo } from "../../../../model/SelectedBookInfo"
import { MemoryProgressLine } from "./MemoryProgressLine"

@Component
export struct CurrentBook{
  @Consume selectedBookInfo:SelectedBookInfo
  @Builder leftColumn(){
    Column(){
      Image($r('app.media.note'))
        .height('20vp')
        .margin({left:'8vp',top:'8vp'})
    }
    .width('8%')
  }
  @Builder centerColumn(){
    Column(){
      // 词汇名称
      Text(this.selectedBookInfo.selectedBookName)
        .fontWeight(FontWeight.Medium)
        .fontSize('18vp')
        .width('95%')
        .textAlign(TextAlign.Start)
        .onClick(()=>{
          // 点击选词
          AlertDialog.show({message:'点击选词'})
        })
        .height('20vp')
      Row(){
        MemoryProgressLine()
      }
      .width('95%')
      .justifyContent(FlexAlign.Center)
      .margin({top:'5vp'})
      // 学习程度
      Column(){
        Row(){
          Text(`熟悉${this.selectedBookInfo.memoryInfo.familiar}`)
            .fontColor($r('app.color.familiar_word_progress'))
            .fontSize($r('app.float.current_book_status_font_size'))
          Text(' | ')
          Text(`一般${this.selectedBookInfo.memoryInfo.general}`)
            .fontColor($r('app.color.general_word_progress'))
            .fontSize($r('app.float.current_book_status_font_size'))
          Text(' | ')
            .fontColor($r('app.color.current_book_status_font_color'))
          Text(`不熟${this.selectedBookInfo.memoryInfo.unFamiliar}`)
            .fontSize($r('app.float.current_book_status_font_size'))
            .fontColor($r('app.color.unfamiliar_word_progress'))
          Text(' | ')
            .fontColor($r('app.color.current_book_status_font_color'))
          Text(`未开始${this.selectedBookInfo.memoryInfo.notStarted}`)
            .fontColor($r('app.color.current_book_status_font_color'))
            .fontSize($r('app.float.current_book_status_font_size'))
          Text(' | ')
            .fontColor($r('app.color.current_book_status_font_color'))
          Text(`未选择${this.selectedBookInfo.memoryInfo.unSelected}`)
            .fontSize($r('app.float.current_book_status_font_size'))
            .fontColor($r('app.color.current_book_status_font_color'))

        }
        .width('100%')
        .height('25vp')
        Row(){
          Text(`不熟${this.selectedBookInfo.memoryInfo.unFamiliar}`)
            .fontSize($r('app.float.current_book_status_font_size'))
            .fontColor($r('app.color.current_book_status_font_color'))
          Text(' | 完成情况 ')
            .fontColor($r('app.color.current_book_status_font_color'))
          Text(`${this.selectedBookInfo.memoryInfo.familiar+this.selectedBookInfo.memoryInfo.unFamiliar+this.selectedBookInfo.memoryInfo.general}/${this.selectedBookInfo.memoryInfo.total}`)
            .fontSize($r('app.float.current_book_status_font_size'))
              //.fontColor($r('app.color.current_book_status_font_color'))
            .fontColor($r('app.color.words_base'))
        }
        .width('100%')
        .height('25vp')
      }
      .width('95%')
    }
    .width('84%')
    .alignItems(HorizontalAlign.Center)
  }
  @Builder rightColumn(){
    Column(){
      Image($r('app.media.more1'))
        .width('50%')
        .margin({top:'8vp'})
    }.width('8%')
  }
  build() {

    // 预测图片
    Row(){
      this.leftColumn()
      this.centerColumn()
      this.rightColumn()
    }
    .justifyContent(FlexAlign.Start)
    .alignItems(VerticalAlign.Top)
    .width('100%')

  }
}
