import { userVocExampleCreate } from "../../../api/privilege"
import { vocDetail } from "../../../api/voc"
import { CommonResult } from "../../../model/CommonResult"
import { UserExample, VocabularyData } from "../../../model/VocabularyData"
import { VocExampleParam } from "../../../model/VocExampleParam"

@Component
export struct VocExamplePage {
  @Consume('recitePathStack') recitePathStack: NavPathStack
  @Consume currentVocabulary: VocabularyData
  private sentence: string = ''
  private meaning: string = ''
  @Consume userExamples: UserExample[]


  /*async updateVocExample() {
    let currentId = this.currentVocabulary.id
    if (currentId) {
      let result = await vocDetail(currentId) as CommonResult
      if (result.code === 200) {
        let data = result.data as VocabularyData
        if (data) {
          this.currentVocabulary.examples = data.examples
        }
      }

    }
  }*/

  build() {
    /*NavDestination(){
      // 输入例句和释义，然后进行提交
      Column(){
        Row(){
          TextInput({
            placeholder: '请输入例句'
          })
            .onChange(value=>{
              this.sentence = value
            })

        }
        Row(){
          TextInput({
            placeholder: '请输入释义'
          })
            .onChange(value => {
              this.meaning = value
            })
        }
        Row(){
          Button('提交')
            .onClick(async ()=>{
              console.info(`例句:${this.sentence},释义:${this.meaning}`)
              if (this.sentence !== '' && this.meaning !== '') {

                let param = {
                  vocId: this.currentVocabulary.id,
                  vocContent: this.currentVocabulary.content,
                  sentence: this.sentence,
                  meaning: this.meaning
                } as VocExampleParam
                let result = await userVocExampleCreate(param)
                if (result.code === 200) {
                  this.sentence = ''
                  this.meaning = ''
                  AlertDialog.show({message: '例句创建成功' })
                  this.recitePathStack.pop()
                }
              }
            })
        }
      }
      .padding(10)
    }*/
    NavDestination() {
      Column() {
        // 例句输入区
        TextInput({ placeholder: '请输入例句 :' })
          .width('100%')
          .height(60)
          .margin({ bottom: 15 })
          .padding(10)
          .fontSize(16)
          .borderRadius(8)
          .borderWidth(1)
          .borderColor('#e0e0e0')
          .onChange(value => {
            this.sentence = value
          })

        // 释义输入区
        TextInput({ placeholder: '请输入释义 :' })
          .width('100%')
          .height(60)
          .margin({ bottom: 25 })
          .padding(10)
          .fontSize(16)
          .borderRadius(8)
          .borderWidth(1)
          .borderColor('#e0e0e0')
          .onChange(value => {
            this.meaning = value
          })

        // 提交按钮
        Button('提 交', { type: ButtonType.Capsule })
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
              // ...原有业务逻辑...
              let param = {
                vocId: this.currentVocabulary.id,
                vocContent: this.currentVocabulary.content,
                sentence: this.sentence,
                meaning: this.meaning
              } as VocExampleParam
              let result = await userVocExampleCreate(param)
              if (result.code === 200) {
                //this.updateVocExample()
                if (this.userExamples){
                  let userExample = {
                    vocContent: this.currentVocabulary.content,
                    sentence: this.sentence,
                    meaning: this.meaning
                  } as UserExample

                  this.userExamples.push(userExample)
                }
                this.sentence = ''
                this.meaning = ''
                AlertDialog.show({ message: '例句创建成功' })
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
        Text('新增例句')
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
}