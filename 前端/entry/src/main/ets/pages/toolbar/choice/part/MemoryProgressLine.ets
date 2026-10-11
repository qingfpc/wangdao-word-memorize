import { vocCurrentStat } from "../../../../api/voc"
import { MemoryInfo, SelectedBookInfo } from "../../../../model/SelectedBookInfo"

export class ProgressLineAttribute{
  progress?:number
  color?:ResourceColor
}

@Component
export struct MemoryProgressLine{
  @Consume selectedBookInfo:SelectedBookInfo
  @Prop  blockColor?:ResourceColor = '#ffaeaeae'
  @Prop  progressHeight?:Length = '8vp'

  async syncBookReciteInfo() {
    let result = await vocCurrentStat(this.selectedBookInfo.selectedBookId)
    if (result.code === 200 && result.data) {
      let memoryInfo = result.data as MemoryInfo
      this.selectedBookInfo.memoryInfo = memoryInfo
    }
    console.info('同步当前书籍背诵情况：'+ JSON.stringify(this.selectedBookInfo))
  }
  async aboutToAppear() {
    if (this.selectedBookInfo.selectedBookId != '' && this.selectedBookInfo.memoryInfo.total ===0) {
      await this.syncBookReciteInfo()
    }
  }

  build() {
    Row(){
      Row(){
      }
      .height(this.progressHeight)
      .backgroundColor($r('app.color.familiar_word_progress'))
      .width(`${Math.floor(this.selectedBookInfo.memoryInfo.familiar/this.selectedBookInfo.memoryInfo.total*100)}%`)
      .borderRadius({ topLeft:20,bottomLeft:20 })
      Row(){
      }
      .height(this.progressHeight)
      .backgroundColor($r('app.color.general_word_progress'))
      .width(`${Math.floor(this.selectedBookInfo.memoryInfo.general/this.selectedBookInfo.memoryInfo.total*100)}%`)
      //.borderRadius(20)
      Row(){
      }
      .height(this.progressHeight)
      .backgroundColor($r('app.color.unfamiliar_word_progress'))
      .width(`${Math.floor(this.selectedBookInfo.memoryInfo.unFamiliar/this.selectedBookInfo.memoryInfo.total*100)}%`)
      .borderRadius({ topRight:20,bottomRight:20 })

    }
    .justifyContent(FlexAlign.Start)
    .backgroundColor(this.blockColor)
    .width('100%')
    .borderRadius(20)
  }
}
