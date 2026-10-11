import { Constants } from "../../common/Constants"

@Component
export struct AiReadVocabularyListComponent {

  // 选择的词语
  @Link selectedVocs: string[]
  @Link revVocs: string[]

  build() {
      List({space:12}) {
        ForEach(this.revVocs, (voc: string) => {
          ListItem() {
            Row() {
              Toggle({ type:ToggleType.Checkbox, isOn:this.selectedVocs.includes(voc)  })
                .width(24)
                .height(24)
                .margin({ right: 12 })
                .onChange((value) => {
                  if(value) {
                    this.selectedVocs.push(voc)
                  }else {
                    let index = this.selectedVocs.indexOf(voc)
                    if(index != -1) {
                      this.selectedVocs.splice(index,1)
                    }
                  }
               })
              Text(voc)
                .fontSize(14)
            }
          }
        })
      }
  }
}