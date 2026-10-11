import { uiObserver } from "@kit.ArkUI"
import { product } from "../../../api/goods"
import { getPayResult, userPay } from "../../../api/pay"
import { PageDataModel } from "../../../model/PageDataModel"
import { LevelItem } from "../../../model/UserLevelInfoData"
import { UserPayData } from "../../../model/UserPayData"
import { VocPaySaleCommand } from "../../../model/VocPaySaleCommand"
import { LoadingDialog } from "../../dialog/LoadingDialog"
import { PayConfirmDialog } from "../../dialog/PayConfirmDialog"
import { PayDialog } from "../../dialog/PayDialog"

interface Product{
  desc: string,
  id: string,
  name: string,
  price: number,
  value: number
  type: number
}

// 主要用于当用户点击购买单词上限时，展示商品列表
@Component
@Preview
export struct Pay{

  @Consume('settingPathStack') settingPathStack: NavPathStack
  @State productList:Array<Product> = []
  private unlockPrice:number = 0
  @State @Watch("paySuccessDo") paySuccess: boolean = false
  @State interrupt:boolean = false
  private selectedProduct?: Product


  paySuccessDo() {
    if (this.paySuccess) {
      this.loadingDialogController?.close()
      setTimeout(() => {
        this.paySuccess = false;
        AlertDialog.show({ message: '支付成功' })
      },4000)
      this.syncProductList()
    }
  }

  dialogController?: CustomDialogController
  loadingDialogController?: CustomDialogController
  payDialogController?: CustomDialogController

  aboutToAppear(){

    console.info('PayComponent onAppear')
    this.syncProductList()
  }

  private syncProductList() {
    product([1, 3], 0, '', 0).then((result) => {
      if (result.code === 200) {

        let data = result.data as PageDataModel<Product>
        if (data) {
          let pageList = data.pageList
          console.info(`pageList:${JSON.stringify(pageList)}`)
          pageList.forEach((item) => {
            this.productList.push(item)
          })
        }
      }
    })
  }

  confirm() {
    this.payDialogController = new CustomDialogController({
      builder: PayDialog({
        amount: this.unlockPrice,
        onWechatPay: async (): Promise<void> => this.handleWechatPay(),
        onAlipay: async (): Promise<void> => {}
      }),
      isModal: false,
      cancel: () => console.log('关闭支付弹窗'),
      alignment: DialogAlignment.Bottom
    })
    this.payDialogController.open()
  }

  async handleWechatPay() {
    if (this.payDialogController) {
      this.payDialogController.close()
    }
    if (this.dialogController) {
      this.dialogController.close()
    }
    // let tmpId = '1862844551188381697'
    let command = {
      itemId: this.selectedProduct?.id,
      // itemId:tmpId,
      itemName: this.selectedProduct?.name,
      itemType: this.selectedProduct?.type,
      payPrice: this.selectedProduct?.price,
      isSignCard: false,
    } as VocPaySaleCommand
    let result = await userPay(command)
    if (result.code === 200) {
      console.log('支付发起中...', result.data)
      this.loadingDialogController = new CustomDialogController({
        builder: LoadingDialog({
          message: '支付中',
        }),
        cancel:()=> this.interrupt = true,
        alignment: DialogAlignment.Center
      })
      this.loadingDialogController.open()
      console.log('调用微信支付接口')

      let data = result.data as UserPayData
      this.interrupt = false
      this.checkPayNotation(3000, data.id)

      //await this.syncUserLevelInfo()
    }

    // 实际支付逻辑实现
  }

  // 递归执行方法
  private checkPayNotation(interval: number, id: string) {
    if (this.paySuccess || this.interrupt) {
      return;
    }

    setTimeout(() => {
      console.log(`检查支付状态...${interval}ms后重试`);
      getPayResult(id)
        .then((result) => {
          let data = JSON.parse(JSON.stringify(result.data)) as number
          console.info(`pay stauts:${data}`)
          if (result.code === 200 && data == 2) {
            this.paySuccess = true;
          }
        })
      if (this.paySuccess || this.interrupt) {
        return;
      }
      this.checkPayNotation(interval, id); // 递归调用
    }, interval);
  }


  build() {
    NavDestination() {
      Column() {
        List() {
          ForEach(this.productList, (item: Product) => {
            ListItem() {
              Row() {
                Text(`${item.desc}`)
                  .width('80%')
                  .fontSize($r('app.float.text_regular'))  // 使用字体资源
                  .fontColor(Color.Black)

                Text(`￥${item.price}`)
                  .fontSize($r('app.float.text_emphasis'))  // 强调文本
                  .fontColor('#FF3A3A')                     // 使用品牌红色
                  .fontWeight(FontWeight.Medium)
              }
              .width('100%')
              .padding({ left: 12, right: 12 })             // 增加横向内边距
              .justifyContent(FlexAlign.SpaceBetween)
              .height('56vp')                              // 明确列表项高度
              .onClick(()=>{
                let privilegeStr = item.desc
                this.unlockPrice = item.price
                this.selectedProduct = item
                this.dialogController = new CustomDialogController({
                  builder: PayConfirmDialog({
                    title: `解锁${item.desc}特权`,
                    content: `确认支付¥${item.price}解锁${item.desc}等级？解锁后可享受`,
                    privilegeStr: privilegeStr,
                    confirmText: '立即解锁',
                    onConfirm: () => {
                      console.log('执行解锁操作')
                      // 这里添加业务逻辑
                      this.confirm()
                    }
                  }),
                })
                this.dialogController.open()
              })
            }
            .backgroundColor(Color.White)                   // 明确背景色
          }, (item: Product) => item.id)
        }
        .divider({
          strokeWidth: $r('app.float.setting_list_strokeWidth'),
          color: $r('app.color.divider_grey'),             // 使用资源颜色
          startMargin: '12vp',
          endMargin: $r('app.float.setting_list_endMargin')
        })
        .width('100%')
        .margin({ top: '8vp' })                             // 优化列表间距
      }
      .width('100%')
      .height('100%')
      .backgroundColor('#F5F5F5')                           // 添加背景色
    }
    .onBackPressed(() => {
      const popDestinationInfo = this.settingPathStack.pop()
      console.info('pop' + '返回值' + JSON.stringify(popDestinationInfo))
      return true
    })
    .menus(this.payHistoryMenu())
    .onWillAppear(()=>{
      this.interrupt = true
      console.info('interrupt: ' + this.interrupt)
      this.syncProductList()
    })
  }

  @Builder payHistoryMenu(){
    Row(){
      Row()
        .width('20%')
        .onClick(()=>{
          this.settingPathStack.pop()
        })
      Row(){
        Text('购买')
          .fontSize('25fp')
          .fontWeight(FontWeight.Bold)
      }
        .width('20%')
      .justifyContent(FlexAlign.Center)
      .alignItems(VerticalAlign.Center)
      Row(){
        Text('历史记录')
          .onClick(()=>{
            this.settingPathStack.pushPathByName('payHistory','')
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