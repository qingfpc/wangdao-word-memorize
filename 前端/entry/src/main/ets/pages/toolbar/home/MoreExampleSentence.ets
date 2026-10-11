import { userVocExampleList } from "../../../api/privilege"
import { CommonResult } from "../../../model/CommonResult"
import { PageDataModel } from "../../../model/PageDataModel"
import { Sentence, WordSentence } from "../../../model/WordSentences"

@Component
export struct MoreExampleSentence {
    @Consume('recitePathStack') recitePathStack: NavPathStack
    private keyword = ''
    @State wordSentenceList: WordSentence[] = []

    async aboutToAppear() {
      await this.searchExampleSentences()

    }

    private async searchExampleSentences() {
      let result: CommonResult = await userVocExampleList(this.keyword)

      let dataModel = result.data as PageDataModel<WordSentence>
      if (dataModel) {
        this.wordSentenceList = dataModel.pageList
      }
    }
  build() {
    NavDestination() {
      Column() {

        this.keywordInput()

        List({ space: 10 }) {
          ForEach(this.wordSentenceList, (item: WordSentence) => {
            ListItemGroup({ header: this.vocabularyTitle(item) }) {
              ForEach(item.sentences, (sentence: Sentence) => {
                ListItem() {
                  Text(sentence.sentence)
                    .fontSize(14)
                    .fontColor('#333')
                    .lineHeight(18)
                    .padding(8)
                    .width('100%')
                    .backgroundColor(Color.White)
                    .borderRadius(4)
                    .shadow({ radius: 4, color: '#10000000', offsetX: 2, offsetY: 2 })
                }
                .margin({ bottom: 8 })
              })
            }
            .divider({ strokeWidth: 1, color: '#eee' })
          })
        }
        .width('100%')
        .height('90%')
        .scrollBar(BarState.Auto)
        .divider({ strokeWidth: 0 })
      }
      .padding({ left: 12, right: 12 })
    }
    .backgroundColor('#fafafa')
    .title('更多例句')
  }

  @Builder
  vocabularyTitle(item: WordSentence) {
    Column() {
      Text(`${item.vocContent}例句`)
        .fontSize(18)
        .fontColor('#666')
        .fontWeight(FontWeight.Medium)
        .margin({ bottom: 6 })
        .width('100%')

      Divider()
        .strokeWidth(1)
        .color('#eee')
    }
    .padding({ top: 8 })
  }

  @Builder
  keywordInput() {
    Row() {
      TextInput({
        placeholder: '请输入关键字',
      })
        .placeholderFont({ size: 14 })
        .fontSize(14)
        .height(40)
        .padding(6)
        .backgroundColor(Color.White)
        .borderRadius(20)
        .margin({ right: 12 })
        .onChange(value => {
          this.keyword = value
        })
        .width('65%')

      Button('搜索')
        .width(80)
        .height(40)
        .backgroundColor('#409EFF')
        .fontColor(Color.White)
        .fontSize(16)
        .fontWeight(FontWeight.Medium)
        .borderRadius(20)
        .stateEffect(true)
        .onClick(async () => {
          await this.searchExampleSentences()
        })
    }
    .justifyContent(FlexAlign.SpaceBetween)
    .padding(12)
    .backgroundColor('#f5f5f5')
    .margin({ bottom: 16 })
    .width('100%')
  }
}
// @Component
// export struct MoreExampleSentence {
//   @Consume('recitePathStack') recitePathStack: NavPathStack
//   private keyword = ''
//   @State wordSentenceList: WordSentence[] = []
//
//   async aboutToAppear() {
//     await this.searchExampleSentences()
//
//   }
//
//   private async searchExampleSentences() {
//     let result: CommonResult = await userVocExampleList(this.keyword)
//
//     let dataModel = result.data as PageDataModel<WordSentence>
//     if (dataModel) {
//       this.wordSentenceList = dataModel.pageList
//     }
//   }
//
//   build() {
//     NavDestination() {
//       Row() {
//         Text('更多例句')
//           .fontSize(30)
//           .fontWeight(FontWeight.Bold)
//       }
//
//       this.keywordInput()
//       List() {
//         ForEach(this.wordSentenceList, (item:WordSentence) => {
//
//           ListItemGroup({ header: this.vocabularyTitle(item) }) {
//             ForEach(item.sentences,(sentence:Sentence)=>{
//               ListItem(){
//                 Row(){
//                   Text(sentence.sentence)
//                    .width('100%')
//                 }
//               }
//             })
//           }
//         })
//       }
//       .divider({ strokeWidth: 1 })
//     }
//   }
//
//   @Builder
//   vocabularyTitle(item:WordSentence) {
//     Row() {
//       Text(`${item.vocContent}例句`)
//         .fontSize(30)
//         .fontWeight(FontWeight.Bold)
//         .flexGrow(1)
//     }
//   }
//
//   @Builder
//   keywordInput() {
//     Row() {
//       TextInput({
//         placeholder: '请输入关键字'
//       })
//         .onChange(value => {
//           this.keyword = value
//         })
//         .width('80%')
//        Button('搜索')
//          .onClick(async () => {
//           await this.searchExampleSentences()
//         })
//     }
//   }
// }