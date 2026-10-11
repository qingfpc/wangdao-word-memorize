import { AuthUser } from "@hw-agconnect/auth"
import { userPrivilegeFavoriteBulk, userVocNon, userVocNonCancel } from "../../../api/privilege"
import { reviewVoc } from "../../../api/review"
import { vocBookChapterDisplay, vocBookDisplay, vocDetail } from "../../../api/voc"
import { Constants } from "../../../common/Constants"
import { BookDao } from "../../../db/dao/BookDao"
import { CategoryDao } from "../../../db/dao/CategoryDao"
import { DaoConfig } from "../../../db/dao/DaoConfig"
import { VocabularyDao } from "../../../db/dao/VocabularyDao"
import { Chapter, Vocabulary } from "../../../model/ChapterVocabulary"
import { CommonResult } from "../../../model/CommonResult"
import { FavoriteParam } from "../../../model/FavoriteParam"
import { PageDataModel } from "../../../model/PageDataModel"
import { SelectedBookInfo } from "../../../model/SelectedBookInfo"
import { VocabularyData } from "../../../model/VocabularyData"
import { VocabularyReviewService } from "../../../service/VocabularyReviewService"
import { GlobalContext } from "../../../util/GlobalContext"

@Component
export struct BookVocabulary {
  @State bookChapterVocabularyArray: Array<Chapter> = []
  @State bookVocabularyArray: Vocabulary[] = []
  @Consume selectedBookInfo: SelectedBookInfo
  @State chapterExpandId: string = ''
  @State chapterExpand: boolean = true
  @State addVocabularyIdArray: string[] = []
  @Consume authUser: AuthUser
  @Consume currentVocabulary: VocabularyData
  @Consume selectVocabularyLeadingToAiVocChange: boolean
  @State chooseAll: boolean = true
  @State hasChapter: boolean = true
  vocabularyReviewService: VocabularyReviewService = new VocabularyReviewService()
  private vocabularyDao: VocabularyDao = GlobalContext.getContext().getObject(DaoConfig.VOCABULARY_DAO) as VocabularyDao
  //private categoryDao:CategoryDao= GlobalContext.getContext().getObject(DaoConfig.CATEGORY_DAO) as CategoryDao
  private bookDao: BookDao = GlobalContext.getContext().getObject(DaoConfig.BOOK_DAO) as BookDao
  @Consume wordsChoicePathStack: NavPathStack
  // 正在背诵的数组
  @Consume recitingVocabularyIdArray: string[]
  // 是否显示中文释义
  @Provide isShowChinese: boolean = false

  async aboutToAppear() {

    let result: CommonResult = { code: 0 }

    let chapter = await this.bookDao.findChapter(this.selectedBookInfo.selectedBookId)
    console.info(`chapter:${chapter}`)
    if (chapter > 0) {
      this.hasChapter = true
      // 获取书籍单词列表有章节版
      result = await vocBookChapterDisplay(this.selectedBookInfo.selectedBookId, 0, 0)
    } else {
      this.hasChapter = false
      result = await vocBookDisplay(this.selectedBookInfo.selectedBookId, 0, 0)
    }
    if (result.code === 200 && this.hasChapter) {
      const pageData = result.data as PageDataModel<Chapter>
      this.bookChapterVocabularyArray = pageData.pageList
      this.addVocabularyIdArray = []
      // console.info(JSON.stringify(this.bookChapterVocabularyArray))
      // console.info(JSON.stringify(this.addVocabularyIdArray))
    } else if (result.code === 200) {
      const pageData = result.data as PageDataModel<Vocabulary>
      this.bookVocabularyArray = pageData.pageList
      console.info(JSON.stringify(this.bookVocabularyArray))
      this.addVocabularyIdArray = []
    }
  }

  build() {
    NavDestination() {
      Column() {
        List() {
          if (this.hasChapter) {
            ForEach(this.bookChapterVocabularyArray, (chapter: Chapter) => {
              ChapterGroup({
                chapterExpand: this.chapterExpand,
                item: chapter,
                addVocabularyIdArray: this.addVocabularyIdArray,
                vocabularyReviewService: this.vocabularyReviewService
              })
            }, (chapter: Chapter) => JSON.stringify(chapter.id))
          } else {
            ForEach(this.bookVocabularyArray, (vocabulary: Vocabulary) => {
              VocabularyItem({
                addVocabularyIdArray: this.addVocabularyIdArray,
                vocabulary: vocabulary,
                vocabularyReviewService: this.vocabularyReviewService
              })
            }, (vocabulary: Vocabulary) => JSON.stringify(vocabulary.id))
          }
        }
        // 粘滞效果
        .sticky(StickyStyle.Header)
        .width('100%')
        .height('90%')
        // 滚动条
        .scrollBar(BarState.Off)
        .divider({
          strokeWidth: 1,
          color: $r('app.color.book_category_list_divider'),
          startMargin: 10,
          endMargin: 10
        })
        .borderRadius({ topLeft: 15, topRight: 15 })

        Row({ space: 25 }) {
          Row() {
            Toggle({ type: ToggleType.Switch, isOn: this.chooseAll })
              .onChange(choose => {
                this.chooseAll = !choose
                if (choose) {

                  let newArray: Vocabulary[] = this.bookVocabularyArray.map(item => {
                    item.reviewed = 0
                    return item
                  })
                  this.bookVocabularyArray = []
                  setTimeout(() => {
                    this.bookVocabularyArray = newArray
                    this.addVocabularyIdArray = []
                  }, 1)
                  //this.bookVocabularyArray = newArray

                  //this.bookVocabularyArray = newArray
                } else {

                  let newArray: Vocabulary[] = this.bookVocabularyArray.map(item => {
                    item.reviewed = 1
                    return item
                  })
                  this.bookVocabularyArray = []
                  setTimeout(() => {
                    this.bookVocabularyArray = newArray
                    this.addVocabularyIdArray = this.bookVocabularyArray.map(item => {
                      return item.id
                    })
                  }, 1)
                }
              })
            Text(`${this.chooseAll ? '取消' : '全选'}`)
          }
          .width(100)

          Button(`加入记忆`)
            .onClick(async () => {
              if (this.vocabularyDao) {
                this.vocabularyDao.createVocabularyReview(this.addVocabularyIdArray, this.authUser.getUid())
                reviewVoc(this.addVocabularyIdArray)
              }
              console.info(`reciting:${this.recitingVocabularyIdArray}`)
              this.recitingVocabularyIdArray =
                this.recitingVocabularyIdArray.concat(this.addVocabularyIdArray).filter((value, index, self) => {
                  return self.indexOf(value) === index;
                })
              console.info(`reciting:${this.recitingVocabularyIdArray}`)
              // ai阅读页面要更新单词
              this.selectVocabularyLeadingToAiVocChange = true
              // 更新要背诵的单词
              if (this.recitingVocabularyIdArray.length > 0) {
                const vocId = this.recitingVocabularyIdArray[0]
                //this.currentVocabulary = dao.find(this.authUser.getUid(),vocId)
                let result = await vocDetail(vocId) as CommonResult
                if (result.code === 200) {
                  let data = result.data as VocabularyData
                  if (data) {
                    this.currentVocabulary = data
                  }
                }
              }

              const popDestinationInfo = this.wordsChoicePathStack.pop() // 弹出路由栈栈顶元素
              console.info('pop' + '返回值' + JSON.stringify(popDestinationInfo))
            })
          Button(`加入收藏`)
            .onClick(async () => {
              await this.addToCollection()
            })
        }
        .width('100%')
        .height('10%')
        .justifyContent(FlexAlign.Center)
        .alignItems(VerticalAlign.Center)
      }
    }
    .onBackPressed(() => {
      const popDestinationInfo = this.wordsChoicePathStack.pop() // 弹出路由栈栈顶元素
      console.info('pop' + '返回值' + JSON.stringify(popDestinationInfo))
      return true
    })
    .title(this.selectedBookInfo.selectedBookName)
    .menus([this.chineseMenu])
  }

  async addToCollection() {
    let array = this.addVocabularyIdArray
    let param = array.map<FavoriteParam>(id => {
      return { vocId: id } as FavoriteParam
    })
    let result = await userPrivilegeFavoriteBulk(param)
    if (result.code === 200) {
      console.info(`加入收藏成功:${result}`)

      this.addVocabularyIdArray = []
      AlertDialog.show({
        message: '加入收藏成功',
        alignment: DialogAlignment.Center,
        offset: { dx: 0, dy: 0 },
      })
    }
  }

  private chineseMenu = {
    value: '', icon: $r('app.media.chinese'), action: () => {
      this.switchChinese()
    }
  } as NavigationMenuItem

  //todo: 切换中文
  switchChinese() {
    this.isShowChinese = !this.isShowChinese
  }
}

@Component
export struct ChapterGroup {
  @Link chapterExpand: boolean
  @Link addVocabularyIdArray: string[]
  private item: Chapter = new Chapter()
  vocabularyReviewService?: VocabularyReviewService

  @Builder
  itemHeader(text: string) {
    Text(text)
      .fontSize(20)
      .backgroundColor($r('app.color.start_window_background'))
      .width('100%')
      .padding(5)
  }

  build() {
    ListItemGroup({ header: this.itemHeader(this.item.chapterName) }) {
      if (this.chapterExpand) {
        ForEach(this.item.displayVocs, (vocabulary: Vocabulary) => {
          VocabularyItem({
            addVocabularyIdArray: this.addVocabularyIdArray,
            vocabulary: vocabulary,
            vocabularyReviewService: this.vocabularyReviewService
          })
        }, (vocabulary: Vocabulary) => JSON.stringify(vocabulary.id))
      }
    }
    .onClick(() => {
      this.chapterExpand = !this.chapterExpand
    })
    .divider({
      strokeWidth: 1,
      color: $r('app.color.chapter_vocabulary_divider'),
      startMargin: 10,
      endMargin: 10
    })
  }
}

@Component
struct VocabularyItem {
  @Link addVocabularyIdArray: string[]
  @State isShowNotWantLearn: boolean = false
  //@State canChoose:boolean = true
  @State vocabulary: Vocabulary = new Vocabulary()
  vocabularyReviewService?: VocabularyReviewService
  @Consume authUser: AuthUser
  @Consume isShowChinese: boolean

  aboutToAppear(): void {
    this.isShowNotWantLearn = this.vocabulary.notRemember === 1
  }

  build() {
    ListItem() {
      Row() {
        Toggle({ type: ToggleType.Checkbox, isOn: this.vocabulary.reviewed === 1 })//.switchPointColor(Color.Red)
          .width(24)
          .height(24)
          .margin({ right: 12 })
          .enabled(!this.isShowNotWantLearn)
          .onChange((isOn) => {
            if (isOn) {
              this.addVocabularyIdArray.push(this.vocabulary.id)
            } else {
              this.addVocabularyIdArray.slice(this.addVocabularyIdArray.indexOf(this.vocabulary.id), 1)
            }
            console.info(JSON.stringify(this.addVocabularyIdArray))
          })

        // 内容区域
        Row() {
          Row() {
            Text(this.vocabulary.content)
              .fontSize($r('app.float.text_regular'))
              .fontColor($r('app.color.text_primary'))
              .textOverflow({ overflow: TextOverflow.Ellipsis })
              .maxLines(1)
              .lineHeight(14)
              .width('50%')
            if (this.isShowChinese) {
              Text(this.vocabulary.interpretations[0].content)
                .fontSize($r('app.float.text_regular'))
                .fontColor($r('app.color.text_primary'))
                .textOverflow({ overflow: TextOverflow.Ellipsis })
                .maxLines(1)
                .lineHeight(14)
                .padding({right:20})
                .width('50%')
            }
          }
          .justifyContent(FlexAlign.SpaceBetween)
          .alignItems(VerticalAlign.Center)
          .layoutWeight(1)
          // 操作按钮区域
          Row() {
            if (this.isShowNotWantLearn) {
              Image($r('app.media.notwantlearn'))
                .objectFit(ImageFit.Contain)
                .width(16)
                .height(16)
                .margin({ right: 8 })
                .onClick(async () => {
                  await userVocNonCancel(this.vocabulary.id)
                  this.isShowNotWantLearn = !this.isShowNotWantLearn
                })
            } else {
              Text('不想背')
                .fontSize($r('app.float.text_small'))
                .fontColor($r('app.color.text_secondary'))
                .lineHeight(14)
                .margin({ right: 8 })
                .onClick(async () => {
                  await userVocNon(this.vocabulary.id)
                  this.vocabularyReviewService!.remove(this.vocabulary.id,
                    this.authUser.getUid())
                  this.vocabulary.reviewed === 0
                  this.isShowNotWantLearn = !this.isShowNotWantLearn
                })
            }

            Image($r('app.media.right_grey'))
              .objectFit(ImageFit.Contain)
              .width(12)
              .height(12)
          }
          .height('100%')
        }
        .height('100%')
        .width('92%')
      }
      .padding({
        top: 6,
        bottom: 6,
        left: 16,
        right: 8
      })
      .height(40)
      .backgroundColor($r('app.color.list_item_bg'))
      .borderRadius(8)
      .width(Constants.PERCENT_100)
      .stateStyles({
        pressed: {
          .backgroundColor($r('app.color.list_item_pressed'))
          .opacity(0.9)
        }
      })
    }
  }
}
