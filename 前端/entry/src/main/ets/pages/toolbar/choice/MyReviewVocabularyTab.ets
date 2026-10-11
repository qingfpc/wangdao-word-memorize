import { AuthUser } from "@hw-agconnect/auth"
import { vocDetail } from "../../../api/voc"
import { Constants } from "../../../common/Constants"
import { formatDate } from "../../../db/dao/VocabularyDao"
import {  VocabularyReview } from "../../../model/ChapterVocabulary"
import { Example, Interpretation, UserExample, UserInterpretation, VocabularyData } from "../../../model/VocabularyData"
import { VocabularyReviewService } from "../../../service/VocabularyReviewService"

@Component
export struct MyReviewVocabularyTab {
  @Consume vocabularyArr: VocabularyReview[]
  @State @Watch('syncCurrentVocabulary') expandedId: string = ''
  @Consume vocabulary: VocabularyData

  syncCurrentVocabulary() {
    console.info(' expandedId:' + this.expandedId)
    if (this.expandedId == '') {

      return
    }
    vocDetail(this.expandedId).then(res => {
      if (res.code === 200) {
        this.vocabulary = res.data as VocabularyData
      }
    })
  }
  build() {

    List() {
      ForEach(this.vocabularyArr, (item: VocabularyReview) => {
        ListItemGroup({
          header: this.vocabularyTitle(item)
        }) {
          if (this.expandedId === item.id) {
            ListItem() {
              Column() {
                // 音标部分
                this.buildPhoneticSection(this.vocabulary)

                // 解释区域
                this.buildInterpretations(this.vocabulary)

                // 例句区域
                this.buildExamplesSection(this.vocabulary)
              }
              .padding({ left: 12, right: 12, top: 0, bottom: 12 })
            }
            .backgroundColor($r('app.color.words_base_light'))
            .margin({ top: 4, bottom: 4 })
            .padding(10)
          }
        }
        .divider({
          strokeWidth: 1,
          color: '#eee',
          startMargin: 16,
          endMargin: 16
        })
        .onClick(() => {
          if (this.expandedId === item.id) {
            this.expandedId = ''
          } else {
            this.expandedId = item.id
          }
        })
      })
    }
    .alignListItem(ListItemAlign.Start)
    .divider({
      strokeWidth: 1,
      color: $r('app.color.words_base'),
      startMargin: 16,
      endMargin: 16
    })
    .height('100%')
  }

  @Builder
  vocabularyTitle(item: VocabularyReview) {
    Column() {
      Row() {
        Text(item.content)
          .fontSize(20)
          .fontColor('#1a1a1a')
          .fontWeight(FontWeight.Bold)
      }
      .width('100%')
      .justifyContent(FlexAlign.Start)
    }
    .padding({ left: 16, right: 16, top: 8, bottom: 8 })
  }

  @Builder
  buildPhoneticSection(item: VocabularyData) {
    Column() {
      // 英式发音
      if (item.ukPhonetic) {
        Row() {
          Text(`英 [${item.ukPhonetic}]`)
            .fontSize(14)
            .fontColor('#666')
            .fontStyle(FontStyle.Italic)

          Image($r('app.media.audio'))
            .width(15)
            .height(15)
            .margin({ left: 8, top: 2 })
        }
        .padding({ bottom: 6 })
      }

      // 美式发音
      if (item.usPhonetic) {
        Row() {
          Text(`美 [${item.usPhonetic}]`)
            .fontSize(14)
            .fontColor('#666')
            .fontStyle(FontStyle.Italic)

          Image($r('app.media.audio'))
            .width(15)
            .height(15)
            .margin({ left: 8, top: 2 })
        }
        .padding({ top: 4, bottom: 6 })
      }
    }
    .margin({ bottom: 12 })
  }

  @Builder
  buildInterpretations(item: VocabularyData) {
    Column() {
      // 系统解释
      ForEach(item.interpretations, (interpretation: Interpretation) => {
        this.buildInterpretationItem(interpretation, false)
      })
    }
  }
  @Builder
  buildInterpretationItem(interp: Interpretation, isUser: boolean) {
    Column() {
      Row() {
        Text(interp.type)
          .fontSize(12)
          .fontColor(isUser ? '#fff' : '#1890ff')
          .padding({ left: 6, right: 6, top: 4, bottom: 4 })
          .backgroundColor(isUser ? '#52c41a' : '#e6f7ff')
          .borderRadius(4)
        Text(interp.interContent)
          .fontSize(14)
          .fontColor('#333')
          .lineHeight(20)

      }
      .width('100%')
      .justifyContent(FlexAlign.Start)
    }
    .padding({ left: 12, right: 12, top: 8, bottom: 8 })
    .backgroundColor(Color.White)
    .borderRadius(6)
    .shadow({ radius: 2, color: '#10000000', offsetX: 1, offsetY: 1 })
    .margin({ bottom: 8 })
  }

  // 构建例句部分
  @Builder
  buildExamplesSection(item: VocabularyData) {
    Column() {
      // 系统例句
      ForEach(item.examples, (example: Example) => {
        this.buildExampleItem(example, false)
      })

      // 用户例句
      ForEach(item.userExamples, (userExample: UserExample) => {
        this.buildUserExampleItem(userExample, true)
      })
    }
    .margin({ top: 12 })
  }
  @Builder
  buildExampleItem(example: Example, isUser: boolean) {
    Column() {
      Column() {


        Text(`"${example.content}"`)
          .fontSize(14)
          .fontColor('#333')
          .lineHeight(20)

        Text(example.meaning)
          .fontSize(12)
          .fontColor('#666')
          .margin({ top: 6 })

        if (isUser) {
          Text('我的例句')
            .fontSize(10)
            .fontColor('#fff')
            .padding({
              left: 6,
              right: 6,
              top: 2,
              bottom: 2
            })
            .backgroundColor('#faad14')
            .borderRadius(2)
        }
      }.width('100%')
    }
    .padding({ left: 12, right: 12, top: 8, bottom: 8 })
    .backgroundColor('#fff')
    .borderRadius(6)
    .shadow({ radius: 2, color: '#10000000', offsetX: 1, offsetY: 1 })
  }
  @Builder
  buildUserExampleItem(example:  UserExample, isUser: boolean) {
    Column() {
      // 用户标记
      if (isUser) {
        Row() {
          Text('我的例句')
            .fontSize(10)
            .fontColor('#ffffff')
            .padding({ left: 6,right:6 })
            .backgroundColor('#faad14')
            .borderRadius(2)
        }
        .width('100%')
      }
      // 例句原文
      Text(`"${example.sentence}"`)
        .fontSize(14)
        .fontColor('#333')
        .lineHeight(20)

      // 例句翻译
      Text(example.meaning)
        .fontSize(12)
        .fontColor('#666')
        .margin({ top: 4 })


    }
    .padding(12)
    .backgroundColor('#fff')
    .borderRadius(6)
    .shadow({ radius: 2, color: '#10000000', offsetX: 1, offsetY: 1 })
    .margin({ bottom: 8,top:8 })
  }
  /*build() {
    List() {
      *//*ForEach(this.vocabularyArr, (item: VocabularyReview) => {
        ListItemGroup({ header: this.vocabularyTitle(item) }) {
          if (this.expandedId === item.id) {
            ForEach(this.vocabulary.interpretations, (interpretation: Interpretation) => {
              ListItem() {
                Text(`${interpretation.type} ${interpretation.interContent}`)
                  .fontSize(14)
                  .fontColor('#333')
                  .lineHeight(18)
                  .padding(8)
                  .width('100%')
                  .backgroundColor(Color.White)
                  .borderRadius(4)
                  .shadow({
                    radius: 4,
                    color: '#10000000',
                    offsetX: 2,
                    offsetY: 2
                  })
              }
              .margin({ bottom: 8 })
            })
          }
        }
        .divider({ strokeWidth: 1, color: '#eee' })
        .onClick(() => {
          if (this.expandedId === item.id) {
            this.expandedId = ''
          } else {
            this.expandedId = item.id
          }
        })
      })*//*
      ForEach(this.vocabularyArr, (item: VocabularyReview) => {
        ListItemGroup({
          header: this.vocabularyTitle(item) // 重构后的头部组件
        }) {
          if (this.expandedId === item.id) {
            ListItem() {
              // 音标和发音按钮
              this.buildPhoneticSection(this.vocabulary)

              // 核心解释区域
              this.buildInterpretations(this.vocabulary)

              // 例句展示
              this.buildExamplesSection(this.vocabulary)

            }
            .padding(12)
            .backgroundColor('#f8f9fa')
            .margin({ top: 8 })
          }
        }
        .divider({
          strokeWidth: 1,
          color: '#eee',
          startMargin: 16,
          endMargin: 16
        })
        .onClick(() => {
          if (this.expandedId === item.id) {
            this.expandedId = ''
          } else {
            this.expandedId = item.id
          }
        })
      })
    }
  }

  @Builder
  vocabularyTitle(item: VocabularyReview) {
    Column() {
      Text(`${item.content}`)
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
  buildVocabularyHeader(item: VocabularyData) {
    Column() {
      Row() {
        // 单词主体
        Text(item.content)
          .fontSize(20)
          .fontWeight(FontWeight.Bold)
          .fontColor('#1a1a1a')
      }
      .justifyContent(FlexAlign.Start)
      .width('100%')
    }
    .padding(16)
    .width('100%')
  }

  // 音标和发音部分
  @Builder
  buildPhoneticSection(item: VocabularyData) {
    Column() {
      // 英式发音
      if (item.ukPhonetic) {
        Row() {
          Text(`英 [${item.ukPhonetic}]`)
            .fontSize(14)
            .fontColor('#666')
            .fontStyle(FontStyle.Italic)

          Image($r('app.media.audio'))
            .width(20)
            .height(20)
            .margin({ left: 8 })
            //.onClick(() => this.playAudio(item.ukPronounciation))
        }
      }

      // 美式发音
      if (item.usPhonetic) {
        Row() {
          Text(`美 [${item.usPhonetic}]`)
            .fontSize(14)
            .fontColor('#666')
            .fontStyle(FontStyle.Italic)
            .margin({ top: 4 })

          Image($r('app.media.audio'))
            .width(20)
            .height(20)
            .margin({ left: 8 })
            //.onClick(() => this.playAudio(item.usPronounciation))
        }
      }
    }
    .margin({ bottom: 12 })
  }

  // 构建解释列表
  @Builder
  buildInterpretations(item: VocabularyData) {
    Column() {
      // 系统解释
      ForEach(item.interpretations, (interpretation: Interpretation) => {
        this.buildInterpretationItem(interpretation, false)
      })


    }
  }

  // 单个解释项
  @Builder
  buildInterpretationItem(interp: Interpretation , isUser: boolean) {
    Column() {
      Row() {
        // 解释类型标签
        Text(interp.type)
          .fontSize(12)
          .fontColor(isUser ? '#ffffff' : '#1890ff')
          .padding({ top: 8,bottom:8, left: 2,right:2 })
          .backgroundColor(isUser ? '#52c41a' : '#e6f7ff')
          .borderRadius(4)

        // 用户标记
        if (isUser) {
          Text('我的笔记')
            .fontSize(10)
            .fontColor('#ffffff')
            .padding({ left: 6,right:6 })
            .backgroundColor('#faad14')
            .borderRadius(2)
            .margin({ left: 8 })
        }
      }

      // 解释内容
      Text(interp.interContent)
        .fontSize(14)
        .fontColor('#333')
        .lineHeight(20)
        .margin({ top: 8 })
        .width('100%')
    }
    .padding(12)
    .backgroundColor(Color.White)
    .borderRadius(6)
    .shadow({ radius: 2, color: '#10000000', offsetX: 1, offsetY: 1 })
    .margin({ bottom: 8 })
  }

  // 构建例句部分
  @Builder
  buildExamplesSection(item: VocabularyData) {
    Column() {
      // 系统例句
      ForEach(item.examples, (example: Example) => {
        this.buildExampleItem(example, false)
      })

      // 用户例句
      ForEach(item.userExamples, (userExample: UserExample) => {
        this.buildUserExampleItem(userExample, true)
      })
    }
    .margin({ top: 12 })
  }

  // 单个例句项
  @Builder
  buildExampleItem(example: Example , isUser: boolean) {
    Column() {
      // 例句原文
      Text(`"${example.content}"`)
        .fontSize(14)
        .fontColor('#333')
        .lineHeight(20)

      // 例句翻译
      Text(example.meaning)
        .fontSize(12)
        .fontColor('#666')
        .margin({ top: 4 })

      // 用户标记
      if (isUser) {
        Row() {
          Text('我的例句')
            .fontSize(10)
            .fontColor('#ffffff')
            .padding({ left: 6,right:6 })
            .backgroundColor('#faad14')
            .borderRadius(2)
        }
        .margin({ top: 8 })
      }
    }
    .padding(12)
    .backgroundColor('#fff')
    .borderRadius(6)
    .shadow({ radius: 2, color: '#10000000', offsetX: 1, offsetY: 1 })
    .margin({ bottom: 8 })
  }
  @Builder
  buildUserExampleItem(example:  UserExample, isUser: boolean) {
    Column() {
      // 例句原文
      Text(`"${example.sentence}"`)
        .fontSize(14)
        .fontColor('#333')
        .lineHeight(20)

      // 例句翻译
      Text(example.meaning)
        .fontSize(12)
        .fontColor('#666')
        .margin({ top: 4 })

      // 用户标记
      if (isUser) {
        Row() {
          Text('我的例句')
            .fontSize(10)
            .fontColor('#ffffff')
            .padding({ left: 6,right:6 })
            .backgroundColor('#faad14')
            .borderRadius(2)
        }
        .margin({ top: 8 })
      }
    }
    .padding(12)
    .backgroundColor('#fff')
    .borderRadius(6)
    .shadow({ radius: 2, color: '#10000000', offsetX: 1, offsetY: 1 })
    .margin({ bottom: 8 })
  }*/
}