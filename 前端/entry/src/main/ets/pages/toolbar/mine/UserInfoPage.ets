import { userPay, getPayResult } from "../../../api/pay"
import { userLevelInfo } from "../../../api/user"
import { MineInfo } from "../../../model/MineInfo"
import { LevelInfo, LevelItem, Privilege } from "../../../model/UserLevelInfoData"
import { UserPayData } from "../../../model/UserPayData"
import { VocPaySaleCommand } from "../../../model/VocPaySaleCommand"
import { LoadingDialog } from "../../dialog/LoadingDialog"
import { PayConfirmDialog } from "../../dialog/PayConfirmDialog"
import { PayDialog } from "../../dialog/PayDialog"

@Component
export struct UserInfoPage {
  @Consume('settingPathStack') settingPathStack: NavPathStack
  @Consume mineInfo: MineInfo
  private selectedLevelItem?: LevelItem
  @State nowLevel: LevelItem[] = []
  @State upperLevel: LevelItem[] = []
  @State @Watch("paySuccessDo") paySuccess: boolean = false
  @State interrupt:boolean = false

  paySuccessDo() {
    if (this.paySuccess) {
      this.loadingDialogController?.close()
      setTimeout(() => {
        this.paySuccess = false;
        AlertDialog.show({ message: '支付成功' })
      },4000)
      this.syncUserLevelInfo()
    }
  }

  build() {
    NavDestination() {
      List() {
        /*ListItem(){
          Text('解锁')
            .onClick(()=>{
              this.dialogController.open()
            })
        }*/
        // 当前等级卡片组
        ForEach(this.nowLevel, (item: LevelItem, index) => {
          ListItem() {
            Column() {
              // 等级信息头部
              Row() {
                Column() {
                  Text(item.levelName)
                    .fontSize(20)
                    .fontColor('#1a1a1a')
                    .fontWeight(FontWeight.Bold)

                  Row() {
                    Text(`已签到${item.checkInDays}天`)
                      .fontSize(12)
                      .fontColor('#666')
                    if (item.unlockPrice) {
                      Text(`¥${item.unlockPrice}`)
                        .fontSize(14)
                        .fontColor($r('app.color.words_base'))
                        .margin({ left: 8 })
                    }
                  }
                  .margin({ top: 6 })
                }
                .layoutWeight(1)

                // 等级状态标识
                Column() {
                  Text('当前等级')
                    .fontSize(12)
                    .fontColor($r('app.color.words_base'))
                    .padding(4)
                    .backgroundColor('#ffd3e0ff')
                    .borderRadius(4)
                  Text(`${item.levelName}`)
                    .fontSize(24)
                    .fontColor($r('app.color.words_base'))
                    .margin({ top: 8 })
                }
                .alignItems(HorizontalAlign.End)
              }
              .padding(16)

              // 特权列表
              Column() {
                ForEach(item.levelPrivileges, (privilege: Privilege, index) => {
                  if (privilege.privilegeName && privilege.description) {
                    Row() {
                      Column() {
                        Text(privilege.privilegeName)
                          .fontSize(16)
                          .fontColor('#333')
                        Text(privilege.description)
                          .fontSize(12)
                          .fontColor('#666')
                          .margin({ top: 4 })
                      }
                      .layoutWeight(1)

                      Text(`免费×${privilege.freeCount}`)
                        .fontSize(16)
                        .fontColor($r('app.color.words_base'))
                        .padding(8)
                    }
                    .padding({ top: 12, bottom: 12 })
                    .width('100%')
                    .border({ width: index !== item.levelPrivileges.length - 1 ? 1 : 0, color: '#eee' })
                  }
                })
              }
              .padding({ left: 16, right: 16 })
              .margin({ top: 8 })
            }
            .backgroundColor('#ffffff')
            .borderRadius(16)
            .shadow({
              radius: 8,
              color: '#4070080f',
              offsetX: 2,
              offsetY: 4
            })
            .margin({
              top: 12,
              bottom: 12,
              left: 16,
              right: 16
            })
          }
          .border({ width: 0 })
        })

        // 未解锁等级卡片组
        ForEach(this.upperLevel, (item: LevelItem, index) => {
          ListItem() {
            Column() {
              // 等级信息头部
              Row() {
                Column() {
                  Text(item.levelName)
                    .fontSize(20)
                    .fontColor('#999')
                    .fontWeight(FontWeight.Medium)

                  Row() {
                    Text(`累计签到需${item.checkInDays}天`)
                      .fontSize(12)
                      .fontColor('#ccc')
                    if (item.unlockPrice) {
                      Text(`¥${item.unlockPrice}`)
                        .fontSize(14)
                        .fontColor('#ff5722')
                        .margin({ left: 8 })
                    }
                  }
                  .margin({ top: 6 })
                }
                .layoutWeight(1)

                // 解锁标识
                Column() {
                  Image($r('app.media.ic_lock'))
                    .fillColor($r('app.color.words_base'))
                    .width(20)
                    .height(20)
                    .margin({ bottom: 4 })
                  /*Text(`Lv.${index + 1}`)
                    .fontSize(24)
                    .fontColor('#ccc')*/
                }
                .alignItems(HorizontalAlign.End)
              }
              .padding(16)
              .opacity(0.9)

              // 特权列表
              Column() {
                ForEach(item.levelPrivileges, (privilege: Privilege, index) => {
                  Row() {
                    Column() {
                      Row() {
                        Text(privilege.privilegeName)
                          .fontSize(16)
                          .fontColor('#999')
                        Image($r('app.media.ic_lock'))
                          .fillColor($r('app.color.words_base'))
                          .width(14)
                          .height(14)
                          .margin({ left: 8 })
                      }

                      Text(privilege.description)
                        .fontSize(12)
                        .fontColor('#ccc')
                        .margin({ top: 4 })
                    }
                    .layoutWeight(1)

                    Text(`免费×${privilege.freeCount}`)
                      .fontSize(16)
                      .fontColor('#999')
                  }
                  .padding({ top: 12, bottom: 12 })
                  .width('100%')
                  .border({ width: index !== item.levelPrivileges.length - 1 ? 1 : 0, color: '#eee' })
                })
              }
              .padding({ left: 16, right: 16 })
              .margin({ top: 8 })
              .opacity(0.8)
            }
            .backgroundColor('#fafafa')
            .border({ width: 1, color: '#eee' })
            .borderRadius(16)
            .margin({
              top: 12,
              bottom: 12,
              left: 16,
              right: 16
            })
            // 点击打开弹窗提示
            .onClick(() => {
              let privilegeArr = item.levelPrivileges
              let privilegeStrArr = privilegeArr.map<string>(privilege => privilege.privilegeName)
              let privilegeStr = privilegeStrArr.join('、')

              this.selectedLevelItem = item

              this.dialogController = new CustomDialogController({
                builder: PayConfirmDialog({
                  title: `解锁${item.levelName}特权`,
                  content: `确认支付¥${item.unlockPrice}解锁${item.levelName}等级？解锁后可享受`,
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
          .border({ width: 0 })
        })
      }
      .backgroundColor('#f5f7fa')
      .width('100%')
      .height('100%')
    }
    .title('等级特权')
    .onWillAppear(async () => {
      this.interrupt = true
      console.info('interrupt: ' + this.interrupt)
      await this.syncUserLevelInfo()
    })
  }

  confirm() {
    this.payDialogController = new CustomDialogController({
      builder: PayDialog({
        amount: this.selectedLevelItem?.unlockPrice,
        onWechatPay: async (): Promise<void> => this.handleWechatPay(),
        onAlipay: async (): Promise<void> => this.handleAlipay()
      }),
      isModal: false,
      cancel: () => console.log('关闭支付弹窗'),
      alignment: DialogAlignment.Bottom
    })
    this.payDialogController.open()
  }

  @Builder
  levelHeader(item: LevelItem): CustomBuilder {
    Row() {
      Column() {
        Row() {
          Text(item.levelName)
            .fontSize(18)
            .fontColor(item.unlockPrice ? '#666' : $r('app.color.words_base'))
            .fontWeight(FontWeight.Bold)

          if (item.unlockPrice) {
            Text(`¥${item.unlockPrice}`)
              .fontSize(14)
              .fontColor('#fff')
              .padding({
                left: 8,
                right: 8,
                top: 2,
                bottom: 2
              })
              .backgroundColor('#ff5722')
              .borderRadius(12)
              .margin({ left: 8 })
          }
        }
        .alignItems(VerticalAlign.Center)

        Text(`${item.description}`)
          .fontSize(12)
          .fontColor(item.unlockPrice ? '#999' : '#666')
          .margin({ top: 4 })
      }
    }
    .padding({
      top: 16,
      bottom: 16,
      left: 24,
      right: 24
    })
    .backgroundColor(item.unlockPrice ? '#f5f5f5' : '#ffb7e7fc')
    .width('100%')
  }

  async syncUserLevelInfo() {
    let result = await userLevelInfo()
    if (result.code === 200) {
      let levelInfo = result.data as LevelInfo
      this.nowLevel = levelInfo.nowLevel
      this.upperLevel = levelInfo.upperLevel
    }
  }

  dialogController?: CustomDialogController /* = new CustomDialogController({
    builder: PayConfirmDialog({
      title: '解锁VIP特权',
      content: '确认支付¥98解锁VIP等级？解锁后可享受专属特权服务...',
      confirmText: '立即解锁',
      onConfirm: () => {
        console.log('执行解锁操作')
        // 这里添加业务逻辑
        AlertDialog.show({message:'解锁中'})
      }
    }),
    cancel: () => console.log('取消操作'),
    alignment: DialogAlignment.Center
  })*/
  /*dialogController: CustomDialogController = new CustomDialogController({
    builder: PayConfirmDialog({
      title: '解锁VIP特权',
      content: '确认支付¥98解锁VIP等级？解锁后可享受专属特权服务...',
      confirmText: '立即解锁',
      onConfirm: () => {
        console.log('执行解锁操作')
        // 这里添加业务逻辑
        AlertDialog.show({message:'解锁中'})
      }
    }),
    cancel: () => console.log('取消操作'),
    alignment: DialogAlignment.Center
  })*/

  payDialogController?: CustomDialogController
  /*= new CustomDialogController({
  builder: PayDialog({
    amount: 98.00,
    onWechatPay: ():void => this.handleWechatPay(),
    onAlipay: ():void => this.handleAlipay()
  }),
  cancel: () => console.log('关闭支付弹窗'),
  alignment: DialogAlignment.Bottom
})*/

  loadingDialogController?: CustomDialogController

  async handleWechatPay() {
    if (this.payDialogController) {
      this.payDialogController.close()
    }
    if (this.dialogController) {
      this.dialogController.close()
    }
    // let tmpId = '1862844551188381697'
    let command = {
      itemId: this.selectedLevelItem?.goodsId,
      // itemId:tmpId,
      itemName: this.selectedLevelItem?.levelName,
      itemType: 2,
      payPrice: this.selectedLevelItem?.unlockPrice,
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
      this.checkPayNotation(interval, id); // 递归调用
    }, interval);
  }

  handleAlipay() {
    this.loadingDialogController = new CustomDialogController({
      builder: LoadingDialog({
        message: '支付中'
      }),
      alignment: DialogAlignment.Center
    })
    this.loadingDialogController.open()
    console.log('调用支付宝支付接口')
    // 实际支付逻辑实现
  }
}