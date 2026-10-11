import { AuthUser } from "@hw-agconnect/auth"
import { userVocFavoriteCancel, userVocFavoriteCreate, userVocNon } from "../../../api/privilege"
import { vocDetail } from "../../../api/voc"
import { AVPlayer } from "../../../audio/AVPlayer"
import { Constants } from "../../../common/Constants"
import { DaoConfig } from "../../../db/dao/DaoConfig"
import { VocabularyDao } from "../../../db/dao/VocabularyDao"
import { CommonResult } from "../../../model/CommonResult"
import { FavoriteParam } from "../../../model/FavoriteParam"
import { Example, Interpretation, UserExample, UserInterpretation, VocabularyData } from "../../../model/VocabularyData"
import { VocabularyReviewService } from "../../../service/VocabularyReviewService"
import { GlobalContext } from "../../../util/GlobalContext"
import { EditVocExamplePage } from "./EditVocExample"
import { MoreExampleSentence } from "./MoreExampleSentence"
import { HideDetailComponent } from "./part/HideDetailComponent"
import { MemoryButton } from "./part/MemoryButton"
import { MemoryButtonRow } from "./part/MemoryButtonRow"
import { WordComponent } from "./part/WordComponent"
import { VocExamplePage } from "./VocExample"

@Component
export struct RecitePage {
  @Provide('recitePathStack') recitePathStack: NavPathStack = new NavPathStack()
  // 正在背诵的单词id的数组
  @Consume recitingVocabularyIdArray: string[]
  // 点击屏幕后显示单词的具体信息
  @State isShowInfo: boolean = false
  @Consume currentVocabulary: VocabularyData
  @Consume authUser: AuthUser

  //@State isCollected: boolean = false

  @Provide userExamples: UserExample[] = [];
  private avPlayer: AVPlayer = new AVPlayer()
  vocabularyReviewService: VocabularyReviewService = new VocabularyReviewService()

  @Provide currentVocabularyReciteStartTime: number = 0;
  @BuilderParam option: () => void

  //private dao:VocabularyDao= GlobalContext.getContext().getObject(DaoConfig.VOCABULARY_DAO) as VocabularyDao

  aboutToAppear(): void {
    console.info(`recite aboutToAppear`)
    setTimeout(() => {

      this.userExamples = this.currentVocabulary.userExamples
      console.info(`userExamples:${JSON.stringify(this.userExamples)}`)
    }, 550)
  }

  aboutToDisappear(): void {

    console.info( `recite aboutToDisappear`)

  }

  build() {
    Navigation(this.recitePathStack) {
      Column() {
        //this.option()
        //this.word()
        WordComponent({play:(url)=>{this.avPlayer.play(url)}})
      }
      .backgroundColor($r('app.color.words_base'))
      if (this.isShowInfo) {

        Divider()
          .height(1)
          .color($r('app.color.line_color'))
        // 词义
        Row() {
          List() {
            ForEach(this.currentVocabulary.interpretations, (item: Interpretation) => {
              ListItem() {

                Text(`[${item.type}]${item.interContent}`)
                  .fontSize('20fp')

              }
            })
          }.width('90%')
        }
        .justifyContent(FlexAlign.SpaceBetween)
        .alignItems(VerticalAlign.Top)
      }
      if (this.isShowInfo) {
        List() {
          ListItem() {
            this.sentenceExample()
          }

          ListItem() {
            this.userSentenceExample()
          }

        }

        //this.memoryButton()
        MemoryButtonRow({isShowInfo:this.isShowInfo})

      }
      if (!this.isShowInfo) {
        /*Column() {
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
                      let count = this.vocabularyReviewService.remove(this.currentVocabulary.id,
                        this.authUser.getUid())
                      await userVocNon(this.currentVocabulary.id)
                      console.info(`count:${count}`)
                    } catch (e) {
                      console.error(`移除单词失败:${e}`)
                    }
                    await this.nextVocabulary()
                  }
                  this.offsetY = 0;
                })
              }
            })
        )
*/
        HideDetailComponent({isShowInfo:this.isShowInfo,vocabularyReviewService:this.vocabularyReviewService,nextVocabulary:()=>{this.nextVocabulary()}})
      }
    }
    .width('100%')
    .height('100%')
    .navDestination(this.PagesMap)

  }

  async nextVocabulary() {
    let array: string[] = []
    console.info(`去除前:${JSON.stringify(this.recitingVocabularyIdArray)}`)
    this.recitingVocabularyIdArray.forEach(item => {
      if (item !== this.currentVocabulary.id) {
        array.push(item)
      }
    })
    console.info(`去除id:${this.currentVocabulary.id}`)
    this.recitingVocabularyIdArray = array
    console.info(`去除后:${JSON.stringify(this.recitingVocabularyIdArray)}`)
    // AlertDialog.show({message:'下滑'})
    // 更新current
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
  }

  @Builder
  PagesMap(name: string) {
    // if (name === 'PageOne'){
    //   PageOne()
    // }
    if (name === 'vocExample') {
      VocExamplePage()
    } else if (name === 'moreExample') {
      MoreExampleSentence()
    } else if (name === 'EditVocExamplePage') {
      // 签到界面
      EditVocExamplePage()
    }
  }

  // 单词
  /*@Builder
  word() {
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
          .margin({right:20})
        Image($r('app.media.audio'))
          .fillColor('#ff1111')
          .fitOriginalSize(false)
          .width(20)
          .aspectRatio(1)
          .onClick(async () => {
            let url = this.currentVocabulary.usPhonetic ? this.currentVocabulary.usPronounciation :
            this.currentVocabulary.ukPronounciation
            await this.avPlayer.play(url)
          })
      }
      .height(40)
      .width(Constants.PERCENT_100)
      .justifyContent(FlexAlign.Center)
      .alignItems(VerticalAlign.Center)


    }
    //.height(180)
    .width(Constants.PERCENT_100)

  }*/

  // 选项
  /*@Builder
  option() {
    // 右上方选项
    Row() {
      Image($r("app.media.option"))
        .height(24)
        .fillColor(Color.Gray)
    }
    .height(40)
    .width(Constants.PERCENT_100)
    .justifyContent(FlexAlign.End)
    .alignItems(VerticalAlign.Center)

    //.margin({ top: 40 })
  }*/

  // 例句部分
  @Builder
  sentenceExample() {
    Column() {
      // 例句第一行
      Row() {
        Row() {
          Text('例句')
            .fontSize('15fp')
            .fontColor(Color.Gray)
            .margin({ left: '20px' })
          Text('A中')
            .fontSize('15fp')
            .fontColor(Color.Gray)
        }
        .width('100%')
        .justifyContent(FlexAlign.SpaceBetween)

      }
      .height(30)
      .width('100%')
      .justifyContent(FlexAlign.SpaceBetween)
      .alignItems(VerticalAlign.Center)

      Divider()
        .width('100%')
        .height(1)//.color($r('app.color.line_color'))
        .color(Color.Gray)
      ForEach(this.currentVocabulary.examples, (item: Example) => {
        Row() {
          Column() {
            Text(item.content)
              .fontSize('18fp')
              .width('100%')
              .fontWeight(FontWeight.Medium)
              .margin({ left: '20px' })
            Text(item.meaning)
              .fontSize('18fp')
              .width('100%')
              .fontWeight(FontWeight.Medium)
              .margin({ left: '20px' })
          }
          .width('90%')
          .alignItems(HorizontalAlign.Start)
          .justifyContent(FlexAlign.Start)

          Column() {
            /*Image($r('app.media.option'))
              .fillColor(Color.Gray)
              .height(20)
              .aspectRatio(1)*/
          }
          .width('10%')
        }
        .width('100%')
        .height(60)
        .justifyContent(FlexAlign.SpaceBetween)
      })
    }
    //.height(40)
    .width('100%')
  }

  // 用户自建例句部分
  @Builder
  userSentenceExample() {
    Column() {
      // 例句第一行
      Row() {
        Row() {
          Text('自建例句')
            .fontSize('15fp')
            .fontColor(Color.Gray)
            .margin({ left: '20px' })
          Text('A中')
            .fontSize('15fp')
            .fontColor(Color.Gray)
        }
        .width('90%')
        .justifyContent(FlexAlign.SpaceBetween)

        Column() {
          Image($r('app.media.add'))// .fillColor(Color.Gray)
            .fillColor($r('app.color.words_base'))
            .height(20)
            .aspectRatio(1)
            .onClick(() => {
              this.recitePathStack.pushPathByName('vocExample', '');
            })
          //.width('100%')
        }
        .width('10%')
      }
      .height(30)
      .width('100%')
      .justifyContent(FlexAlign.SpaceBetween)
      .alignItems(VerticalAlign.Center)

      Divider()
        .width('100%')
        .height(1)//.color($r('app.color.line_color'))
        .color(Color.Gray)
      //ForEach(this.currentVocabulary.userExamples, (item: UserExample) => {
      ForEach(this.userExamples, (item: UserExample) => {
        Row() {
          Column() {
            Text(item.sentence)
              .fontSize('18fp')
              .height('50%')
              .width('100%')
              .fontWeight(FontWeight.Medium)
              .margin({ left: '20px' })
            Text(item.meaning)
              .fontSize('18fp')
              .height('50%')
              .width('100%')
              .fontWeight(FontWeight.Medium)
              .margin({ left: '20px' })
            Divider()
          }
          .width('90%')
          .alignItems(HorizontalAlign.Start)
          .justifyContent(FlexAlign.Start)

          Column() {
            Image($r('app.media.option'))
              .fillColor(Color.Gray)
              .height(20)
              .aspectRatio(1)
              .onClick(() => {
                console.info(`item:${JSON.stringify(item)}`)
                this.recitePathStack.pushPathByName('EditVocExamplePage', item)
              })
          }
          .width('10%')
        }
        .width('100%')
        .height(60)
        .justifyContent(FlexAlign.SpaceBetween)
      })
    }
    //.height(40)
    .width('100%')
  }

  // 用户自建释义部分
  @Builder
  userInterpretations() {
    Column() {
      // 例句第一行
      Row() {
        Row() {
          Text('自建释义')
            .fontSize('15fp')
            .fontColor(Color.Gray)
            .margin({ left: '20px' })
          Text('')
            .fontSize('15fp')
            .fontColor(Color.Gray)
        }
        .width('90%')
        .justifyContent(FlexAlign.SpaceBetween)

        Column() {
          Image($r('app.media.option'))
            .fillColor(Color.Gray)
            .height(20)
            .aspectRatio(1)
          //.width('100%')
        }
        .width('10%')
      }
      .height(30)
      .width('100%')
      .justifyContent(FlexAlign.SpaceBetween)
      .alignItems(VerticalAlign.Center)

      Divider()
        .width('100%')
        .height(1)//.color($r('app.color.line_color'))
        .color(Color.Gray)
      ForEach(this.currentVocabulary.userInterpretations, (item: UserInterpretation) => {
        Row() {
          Column() {
            Text(`${item.type} : ${item.meaning}`)
              .fontSize('18fp')
              .height('100%')
              .width('100%')
              .fontWeight(FontWeight.Medium)
              .margin({ left: '20px' })

          }
          .width('90%')
          .alignItems(HorizontalAlign.Start)
          .justifyContent(FlexAlign.Start)

          Column() {
            Image($r('app.media.option'))
              .fillColor(Color.Gray)
              .height(20)
              .aspectRatio(1)
          }
          .width('10%')
        }
        .width('100%')
        .height(60)
        .justifyContent(FlexAlign.SpaceBetween)
      })
    }
    //.height(40)
    .width('100%')
  }

  // 词典
  @Builder
  dictionary() {
    Column() {
      this.title('词典', $r('app.media.option'))
    }.width('100%')

  }

  @Builder
  memoryHistory() {
    Column() {
      this.title('记忆历史', $r('app.media.option'))
    }.width('100%')

  }

  // 标题
  @Builder
  title(title: string, img: Resource) {
    Row() {
      Row() {
        Text(title)
          .fontSize('15fp')
          .fontColor(Color.Gray)
          .margin({ left: 20 })
      }
      .width('90%')
      .justifyContent(FlexAlign.SpaceBetween)

      Column() {
        Image(img)
          .fillColor(Color.Gray)
          .height(20)
          .aspectRatio(1)
      }
      .width('10%')
    }
    .height(40)
    .width('100%')
    .justifyContent(FlexAlign.SpaceBetween)
    .alignItems(VerticalAlign.Center)

    Divider()
      .width('100%')
      .height(1)
      .color($r('app.color.line_color'))
  }

  /*@Builder
  memoryButton() {
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
            *//*let params:FavoriteParam[] = []
            params.push({
              vocId: this.currentVocabulary.id,
              vocContent: this.currentVocabulary.content,
              selected: 1
            } as FavoriteParam)*//*
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
  }*/
}