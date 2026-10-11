import { userVocExampleCreate, userVocExampleUpdate } from "../../../api/privilege"
import { vocDetail } from "../../../api/voc"
import { CommonResult } from "../../../model/CommonResult"
import { UserExample, VocabularyData } from "../../../model/VocabularyData"
import { VocExampleParam } from "../../../model/VocExampleParam"

@Component
export struct EditVocExamplePage {
  @Consume('recitePathStack') recitePathStack: NavPathStack
  @Consume currentVocabulary: VocabularyData
  @State sentence: string = ''
  @State meaning: string = ''
  @Consume userExamples: UserExample[]
  private exampleId: string = ''


  build() {
    NavDestination() {
      Column() {
        // 例句输入区
        TextInput({ placeholder: '请输入例句 :' , text: $$this.sentence })
          .width('100%')
          .height(60)
          .margin({ bottom: 15 })
          .padding(10)
          .fontSize(16)
          .borderRadius(8)
          .borderWidth(1)
          .borderColor('#e0e0e0')
          /*.onChange(value => {
            this.sentence = value
          })*/

        // 释义输入区
        TextInput({ placeholder: '请输入释义 :' , text: $$this.meaning })
          .width('100%')
          .height(60)
          .margin({ bottom: 25 })
          .padding(10)
          .fontSize(16)
          .borderRadius(8)
          .borderWidth(1)
          .borderColor('#e0e0e0')
          /*.onChange(value => {
            this.meaning = value
          })*/

        // 提交按钮
        Button('修 改', { type: ButtonType.Capsule })
          .width('60%')
          .height(45)
          .backgroundColor('#409EFF')
          .fontColor(Color.White)
          .fontSize(18)
          .fontWeight(FontWeight.Medium)
          .opacity(this.sentence && this.meaning ? 1 : 0.6)
          .onClick(async () => {
            if (this.sentence && this.meaning) {
              console.info(`例句:${this.sentence},释义:${this.meaning}`)
              console.info(`当前单词:${this.exampleId}`)
              // ...原有业务逻辑...
              let param = {
                id: this.exampleId,
                vocId: this.currentVocabulary.id,
                vocContent: this.currentVocabulary.content,
                sentence: this.sentence,
                meaning: this.meaning
              } as VocExampleParam
              let result = await userVocExampleUpdate(param)
              if (result.code === 200) {
                //this.updateVocExample()
                if (this.userExamples){
                  let userExample = {
                    vocContent: this.currentVocabulary.content,
                    sentence: this.sentence,
                    meaning: this.meaning
                  } as UserExample

                  //this.userExamples.push(userExample)
                  this.userExamples.forEach((item, index) => {
                    if (item.id === this.exampleId) {
                      this.userExamples[index] = userExample
                    }
                  })
                }
                this.sentence = ''
                this.meaning = ''
                AlertDialog.show({ message: '例句更新成功' })
                this.recitePathStack.pop()
              }
            } else {
              AlertDialog.show({
                message: '请填写完整内容',
              })
            }
          })
      }
      .padding(20)
      .width('100%')
      .justifyContent(FlexAlign.Center)
    }
    .menus(this.moreExample())
    .onShown(()=>{
      let list = this.recitePathStack.getParamByName('EditVocExamplePage')as UserExample[]
      console.info('list',JSON.stringify(list))
      if (list){
        let item = list[0]
        this.sentence = item.sentence
        this.meaning = item.meaning
        this.exampleId = item.id
      }
    })

  }

  @Builder
  moreExample() {
    Row() {
      Row()
        .width('20%')
        .onClick(() => {
          this.recitePathStack.pop()
        })
      Row() {
        Text('编辑例句')
          .fontSize('25fp')
          .fontWeight(FontWeight.Bold)
      }
      .width('40%')
      .justifyContent(FlexAlign.Center)
      .alignItems(VerticalAlign.Center)

      Row() {
        Text('更多例句')
          .onClick(() => {
            this.recitePathStack.pushPathByName('moreExample', '')
          })
      }
      .width('30%')
      .justifyContent(FlexAlign.End)
    }
    .width('80%')
    .height('100%')
    .alignItems(VerticalAlign.Center)
    .justifyContent(FlexAlign.SpaceBetween)
  }

  async aboutToAppear() {

  }
}