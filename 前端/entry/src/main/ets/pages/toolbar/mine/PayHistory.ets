import { payList } from "../../../api/pay"
import { PageDataModel } from "../../../model/PageDataModel"

interface PayHistory{

  id: number,
  itemName: string,
  itemType: number,
  payPrice: number,
  saleTime: string,
  userId: number

}
//当用户点击购买历史时，可以看到成功购买的商品，特权等列表
@Component
export struct PayHistoryComponent{
  @Consume('settingPathStack') settingPathStack: NavPathStack
  @State payHistoryList:Array<PayHistory> = []


  async aboutToAppear(){
    let result = await payList()
    if (result.code === 200) {

      let data = result.data as PageDataModel<PayHistory>
      if (data) {
        let pageList = data.pageList
        console.info(`pageList:${JSON.stringify(pageList)}`)
        pageList.forEach((item)=>{
          this.payHistoryList.push(item)
        })
      }
    }
  }

  build() {
    NavDestination(){

      Column(){
        List(){
          ForEach(this.payHistoryList,(item:PayHistory)=>{

            ListItem(){
              Row(){
                Text(`${item.itemName}`).width('50%')
                Text(`￥${item.payPrice}`).width('10%')
                Text(`${item.saleTime}`).width('30%')
              }
              .width('100%')
              .justifyContent(FlexAlign.SpaceBetween)
            }
          },(item:PayHistory)=>JSON.stringify(item.id))
        }
        .divider({
          strokeWidth: $r('app.float.setting_list_strokeWidth'),
          color: Color.Grey,
          startMargin: '12vp',
          endMargin: $r('app.float.setting_list_endMargin')
        })
      }
      //.margin({ top: '40vp' }) // 设置上边距
      .width('100%')
      .height('100%')
    }
    .title('历史购买记录')
    .onBackPressed(() => {
      const popDestinationInfo = this.settingPathStack.pop() // 弹出路由栈栈顶元素
      console.info('pop' + '返回值' + JSON.stringify(popDestinationInfo))
      return true
    })

  }
}