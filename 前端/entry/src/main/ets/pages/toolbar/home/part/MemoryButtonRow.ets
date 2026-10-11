import { userVocFavoriteCancel, userVocFavoriteCreate } from "../../../../api/privilege"
import { FavoriteParam } from "../../../../model/FavoriteParam"
import { VocabularyData } from "../../../../model/VocabularyData"
import { MemoryButton } from "./MemoryButton"
import { systemDateTime } from "@kit.BasicServicesKit"

@Component
export struct MemoryButtonRow {

  @Link isShowInfo:boolean
  @Consume currentVocabulary: VocabularyData

  aboutToAppear(): void {
    console.info('MemoryButtonRow aboutToAppear')
  }
  aboutToDisappear(): void {
    console.info(`MemoryButtonRow aboutToDisappear: ${systemDateTime.getTime()}`)
  }

  build() {
    Row() {
      MemoryButton({
        text: '简单',
        color: Color.Green,
        q: 3,
        isShowInfo: this.isShowInfo
      })
        .aspectRatio(1)
      MemoryButton({
        text: '认识',
        color: Color.Pink,
        q: 2,
        isShowInfo: this.isShowInfo
      })
        .aspectRatio(1)
      MemoryButton({
        text: '模糊',
        color: Color.Orange,
        q: 1,
        isShowInfo: this.isShowInfo
      })
        .aspectRatio(1)
      MemoryButton({
        text: '忘记',
        color: Color.Red,
        q: 0,
        isShowInfo: this.isShowInfo
      })
        .aspectRatio(1)
      Image(this.currentVocabulary.favorite == 1 ? $r('app.media.collect_fill') : $r('app.media.collect'))
        .fillColor(this.currentVocabulary.favorite == 1 ? $r('app.color.words_base') : Color.Gray)
        .height(40)
        .aspectRatio(1)
        .margin({ bottom: 20 })
        .onClick(async () => {
          if (this.currentVocabulary.favorite == 1) {
            // 取消收藏
            let vocIds: string[] = []
            vocIds.push(this.currentVocabulary.id)
            let result = await userVocFavoriteCancel(vocIds)
            if (result.code === 200) {
              this.currentVocabulary.favorite = 0
            }
          } else {
            // 收藏
            /*let params:FavoriteParam[] = []
            params.push({
              vocId: this.currentVocabulary.id,
              vocContent: this.currentVocabulary.content,
              selected: 1
            } as FavoriteParam)*/
            let param = {
              vocId: this.currentVocabulary.id,
              vocContent: this.currentVocabulary.content,
              selected: 1
            } as FavoriteParam
            let result = await userVocFavoriteCreate(param)
            if (result.code === 200) {
              this.currentVocabulary.favorite = 1
            }
          }
        })
    }
    .width('100%')
    .justifyContent(FlexAlign.SpaceBetween)
    .padding({
      left: 20,
      right: 20,
      bottom: 5,
      top: 5
    })
    .position({ bottom: 0 })
    .backgroundColor($r('app.color.start_window_background'))
  }
}