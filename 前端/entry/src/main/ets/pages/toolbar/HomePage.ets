import { Constants } from "../../common/Constants"
import { CompletePage } from "./home/CompletePage"
import { RecitePage } from "./home/RecitePage"
import { router } from "@kit.ArkUI"

@Component
export struct HomePage{

  @Consume recitingVocabularyIdArray:string[]



  build() {
    Column(){
      if (this.recitingVocabularyIdArray.length ==0)
      {
        CompletePage({option:this.option})
      }else {
        RecitePage({option:this.option})
      }
    }
    .width('100%')
    .height('100%')
    .expandSafeArea([SafeAreaType.SYSTEM],[SafeAreaEdge.TOP,SafeAreaEdge.BOTTOM])
  }
  @LocalBuilder
  option() {
    // 右上方选项
    Row() {
        Image($r("app.media.option"))
          .fillColor(Color.Gray)
          .height(24)
          .bindMenu(this.optionMenu)
    }
    .height(40)
    .width(Constants.PERCENT_100)
    .justifyContent(FlexAlign.End)
    .alignItems(VerticalAlign.Center)
  }

  @Builder
  optionMenu() {
    Menu(){
      MenuItem({content:'单选题',})
        .onClick(()=>{
        console.info('handle 单选题 select')
          router.pushUrl({url:'pages/router/SingleChoiceTopic'})
       })
      MenuItem({content:'填空题'})
        .onClick(()=>{
        console.info('handle 填空题 select')
          router.pushUrl({url:'pages/router/FillTopic'})
        })
    }
  }


}