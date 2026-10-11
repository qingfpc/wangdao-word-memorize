import { Constants } from "../../../../common/Constants"
import { VocabularyData } from "../../../../model/VocabularyData"

@Component
export struct WordComponent {
  @Consume currentVocabulary: VocabularyData

  play?:(url:string)=>void

  aboutToAppear(): void {
    console.info('WordComponent aboutToAppear')
  }
  aboutToDisappear(): void {
    console.info('WordComponent aboutToDisappear')
  }

  build() {
    Column() {
      // 单词
      Row() {
        Text(`${this.currentVocabulary.content}`)
          .fontSize('40fp')
          .fontWeight(FontWeight.Bold)
          .fontColor($r('app.color.recite_word_font_color'))
      }
      .height(60)
      .width(Constants.PERCENT_100)
      .justifyContent(FlexAlign.Center)

      // 音标
      Row() {
        Text(this.currentVocabulary.usPhonetic ? '美' : '英')
          .fontSize('15fp')
          .padding(3)
          .fontWeight(FontWeight.Medium)
          .margin({ right: 20 })
          .borderRadius(5)
          .fontColor($r('app.color.recite_word_font_color'))
        Text(`[${this.currentVocabulary.usPhonetic ? this.currentVocabulary.usPhonetic :
        this.currentVocabulary.ukPhonetic}]`)
          .fontSize('20fp')
          .fontWeight(FontWeight.Bold)
          .fontColor($r('app.color.recite_word_font_color'))
          .height(Constants.PERCENT_100)
          .margin({ right: 20 })
        Image($r('app.media.audio'))
          .fillColor('#ff1111')
          .fitOriginalSize(false)
          .width(20)
          .aspectRatio(1)
          .onClick(async () => {
            let url = this.currentVocabulary.usPhonetic ? this.currentVocabulary.usPronounciation :
            this.currentVocabulary.ukPronounciation
            //await this.avPlayer.play(url)
            this.play?.(url)
          })
      }
      .height(40)
      .width(Constants.PERCENT_100)
      .justifyContent(FlexAlign.Center)
      .alignItems(VerticalAlign.Center)

    }
    //.height(180)
    .width(Constants.PERCENT_100)
  }
}