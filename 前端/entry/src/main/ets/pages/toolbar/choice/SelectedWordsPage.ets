import { AuthUser } from "@hw-agconnect/auth"
import { vocDetail } from "../../../api/voc"
import { Constants } from "../../../common/Constants"
import { formatDate } from "../../../db/dao/VocabularyDao"
import { VocabularyReview } from "../../../model/ChapterVocabulary"
import { VocabularyData } from "../../../model/VocabularyData"
import { VocabularyReviewService } from "../../../service/VocabularyReviewService"
import { MyReviewVocabularyTab } from "./MyReviewVocabularyTab"

@Component
export struct SelectedWordsPage {
  @Consume wordsChoicePathStack: NavPathStack
  @State @Watch('loadingVocabulary')selectedWordsCurrentIndex: number = Constants.TODAY_TAB_INDEX


  @Consume authUser:AuthUser
  selectedDate?:string
  private vocabularyService:VocabularyReviewService = new VocabularyReviewService()

  @Provide vocabularyArr:VocabularyReview[] = []
  @Provide vocabulary: VocabularyData = {} as VocabularyData


  aboutToDisappear() {
    this.loadingVocabulary()
  }

  private loadingVocabulary() {
    if (this.selectedWordsCurrentIndex === Constants.TODAY_TAB_INDEX) {
      this.selectedDate = formatDate(new Date())
    } else if (this.selectedWordsCurrentIndex === Constants.TOTAL_TAB_INDEX) {
      this.selectedDate = undefined
    }
    this.syncVocabularyArr()
  }



  syncVocabularyArr() {
    try {
      console.info( `selectedDate:${this.selectedDate}`)
      console.info( `userId:${this.authUser.getUid()}`)
      this.vocabularyArr = this.vocabularyService.queryReviewVocabulary(this.authUser.getUid(), this.selectedDate)
      if (this.vocabularyArr.length !== 0) {
        vocDetail(this.vocabularyArr[0].id).then(res => {
          if (res.code === 200) {
            this.vocabulary = res.data as VocabularyData
          }
        })
      }
    } catch (e) {
      console.error(e)
    }
  }


  build() {
    NavDestination(){
      Tabs() {
        TabContent(){
          MyReviewVocabularyTab()
        }.tabBar('今日')
        TabContent(){
          MyReviewVocabularyTab()
        }.tabBar('全部')
      }
      .barPosition(BarPosition.Start)
      .onChange(index => {
        this.selectedWordsCurrentIndex = index
        console.info(`this.currentIndex:${this.selectedWordsCurrentIndex}`)
      })
    }
    .title('已选单词')
    .hideTitleBar(true)
    .onWillAppear(()=>{
      this.loadingVocabulary()
    })
  }
}