import { AuthUser } from "@hw-agconnect/auth";
import { userVocNon } from "../../../../api/privilege";
import { VocabularyData } from "../../../../model/VocabularyData";
import { VocabularyReviewService } from "../../../../service/VocabularyReviewService";
import { systemDateTime } from "@kit.BasicServicesKit";

@Component
export struct HideDetailComponent {
  @Link isShowInfo: boolean
  @State offsetY: number = 0;
  @Consume currentVocabulary: VocabularyData
  @Consume authUser: AuthUser
  nextVocabulary?: () => void

  private panOption: PanGestureOptions = new PanGestureOptions({ direction: PanDirection.Up | PanDirection.Down });
  vocabularyReviewService?: VocabularyReviewService
  @Consume currentVocabularyReciteStartTime:number

  aboutToAppear(): void {
    console.info(`HideDetailComponent aboutToAppear: ${systemDateTime.getTime()}`)
    this.currentVocabularyReciteStartTime = systemDateTime.getTime()
  }
  aboutToDisappear(): void {
    console.info('HideDetailComponent aboutToDisappear')
  }

  build() {
    Column() {
      Text('请回忆单词发音和释义')
      Text('点击屏幕显示答案')
      Text('上滑标记不想背')
      //    .textAlign(TextAlign.Center)
    }
    .width('100%')
    .height('80%')
    .justifyContent(FlexAlign.Center)
    .onClick(() => {
      this.isShowInfo = true
    })
    .offset({ y: this.offsetY })
    .parallelGesture(
      PanGesture(this.panOption)
        .onActionUpdate((event: GestureEvent) => {
          if (event) {
            this.offsetY = event.offsetY;
          }
        })
        .onActionEnd((event: GestureEvent) => {
          if (event) {
            animateTo({
              curve: Curve.Smooth
            }, async () => {
              // console.info('上下滑动')
              // 左右滑动
              if (Number(event.offsetY) > 80) {
                AlertDialog.show({ message: '下滑没有用' })
              }
              if (event.offsetY < -80) {
                try {
                  let count = this.vocabularyReviewService!.remove(this.currentVocabulary.id,
                    this.authUser.getUid())
                  await userVocNon(this.currentVocabulary.id)
                  console.info(`count:${count}`)
                } catch (e) {
                  console.error(`移除单词失败:${e}`)
                }
                if (this.nextVocabulary) {
                  await this.nextVocabulary()
                }
              }
              this.offsetY = 0;
            })
          }
        })
    )

  }
}