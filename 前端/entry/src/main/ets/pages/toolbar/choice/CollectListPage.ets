import { userPrivilegeFavoriteList } from "../../../api/privilege"
import { CollectVocabulary, Interpretation } from "../../../model/CollectVocabulary"
import { CommonResult } from "../../../model/CommonResult"
import { PageDataModel } from "../../../model/PageDataModel"

@Component
export struct CollectListPage {
  @Consume wordsChoicePathStack: NavPathStack
  private keyword = ''
  @State collectVocabularyList: CollectVocabulary[] = []

  @State expandedId:string = ''

  build() {
    NavDestination(){
      Column() {

        this.keywordInput()

        List({ space: 10 }) {
          ForEach(this.collectVocabularyList, (item: CollectVocabulary) => {
            ListItemGroup({ header: this.vocabularyTitle(item) }) {
              if (this.expandedId === item.vocId){
                ForEach(item.interpretations, (interpretation: Interpretation) => {
                  ListItem() {
                    Text(`${interpretation.type} ${interpretation.content}`)
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
            }
            .divider({ strokeWidth: 1, color: '#eee' })
            .onClick(() => {
              if (this.expandedId === item.vocId) {
                this.expandedId = ''
              } else {
                this.expandedId = item.vocId
              }
            })
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
    .title('我的收藏')
    .onWillAppear(async ()=>{
      await this.searchCollectList()

    })

  }
  @Builder
  vocabularyTitle(item: CollectVocabulary) {
    Column() {
      Text(`${item.vocContent}`)
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
          await this.searchCollectList()
        })
    }
    .justifyContent(FlexAlign.SpaceBetween)
    .padding(12)
    .backgroundColor('#f5f5f5')
    .margin({ bottom: 16 })
    .width('100%')
  }

  async aboutToAppear() {
    await this.searchCollectList()
  }

  async searchCollectList() {
    let result: CommonResult = await userPrivilegeFavoriteList(this.keyword)

    let dataModel = result.data as PageDataModel<CollectVocabulary>
    if (dataModel) {
      this.collectVocabularyList = dataModel.pageList
    }
  }
}