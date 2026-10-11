import auth, { AuthUser } from "@hw-agconnect/auth"
import { dayVoc, putUserRemind, userUpgradeInfo } from "../../api/user"
import { Constants } from "../../common/Constants"
import { MineDao } from "../../db/dao/MineDao"
import { KVStoreUtil } from "../../db/kvstore/KVStoreUtil"
import { MineInfo } from "../../model/MineInfo"
import { SettingItem } from "../../model/SettingItem"
import { SignInfo } from "../../model/SignInfo"
import { PromptActionClass } from "../../util/dialog/PromptActionClass"
import { AccountInfoView } from "./mine/AccountInfoView"
import { Pay } from "./mine/Pay"
import { PayHistoryComponent } from "./mine/PayHistory"
import { SettingCell } from "./mine/SettingCell"
import { SignInfoView } from "./mine/SignInfoView"
import { distributedKVStore } from "@kit.ArkData"
import { CheckinPage } from "./sign/CheckinPage"
import { CommonResult } from "../../model/CommonResult"
import { UserUpgradeModel } from "../../model/UserUpgradeModel"
import { UserInfoPage } from "./mine/UserInfoPage"
import { VocBoundStat } from "./mine/VocBoundStat"

@Component
export struct MinePage{
  @Provide('settingPathStack') settingPathStack: NavPathStack = new NavPathStack()
  // 个人用户信息
  @Provide mineInfo:MineInfo = new MineInfo()
  // 签到信息
  @Consume signInfo:SignInfo

  // 从父组件接收isLogin状态（是否已登录）
  @Consume("isLogin") isLogin: boolean
  // 从父组件接收当前的用户信息
  @Consume("authUser") authUser: AuthUser|null

  @State vocCntOfDay: number = 0;
  @State freeVocBound: number = 0;
  @State payVocBound: number = 0;
  @State reSignCard: number = 0;
  @State remindTime: string = '';
  @State totalVocBound: number = 0;
  @State levelUpRemainDays:number = 0
  @State dailyLeaningSettingItem: SettingItem =
    {
      title: '每日学习量',
      info: this.vocCntOfDay,
      onClick: this.onUpdateVocOfDay,
      saved: false
    }
  @State totalVocBoundSettingItem: SettingItem =
    {
      title: '现单词上限总量',
      info: this.totalVocBound,
      saved: false
    }
  @State freeVocBoundSettingItem: SettingItem =
    {
      title: '免费获取单词上限',
      info: this.freeVocBound,
      saved: false
    }
  @State payVocBoundSettingItem: SettingItem =
    {
      title: '购买单词上限',
      info: this.payVocBound,
      saved: false
    }
  @State remindTimeSettingItem: SettingItem =
    {
      title: '学习提醒',
      info: this.remindTime,
      onClick:this.onUpdateRemindTime,
      saved: false
    }
  @State reSignCardSettingItem: SettingItem =
    {
      title: '剩余补签卡',
      info: this.reSignCard,
      saved: false
    }

  async onUpdateRemindTime(item: SettingItem): Promise<boolean> {
    let ret = false;
    if (item.userId){
      await putUserRemind({remindTime:item.tmp as string,userId:item.userId}).then(async (result) => {
        if (result.code === 200) {
          // 同步 kvStore
          await (await KVStoreUtil.getKVStore()).put('remindTime', item.info);
          item.info = item.info as string;
          ret = true;
        }
        PromptActionClass.closeDialog();
      });
    }
    return ret;
  }


  async onUpdateVocOfDay(item: SettingItem):Promise<boolean> {
    let ret = false;
    await dayVoc(item.tmp as number).then(async (result) => {
      if (result.code === 200) {
        // 同步kvStore
        await (await KVStoreUtil.getKVStore()).put('vocCountOfDay', item.info)
        item.info = Number.parseInt(item.info as string)
        ret = true;
      }
      PromptActionClass.closeDialog()
    })
    return ret;
  }

  // 定义登出功能，登出后会更新状态并弹出提示
  private logout = () => {
    auth.signOut().then(() => {
      // 清除当前用户信息和登录状态
      this.authUser = null
      this.isLogin = false
      // 弹出登出成功的提示
      AlertDialog.show({ message: "登出" })
    })
  }


  async aboutToAppear() {
    this.syncUserUpgradeInfo()
    this.remindTimeSettingItem.userId = this.authUser?.getUid()
    PromptActionClass.setContext(this.getUIContext())
    // freeVocBound
    // 同步个人信息
    await MineDao.syncMineSettingInfo()
    this.mineInfo = await MineDao.syncMineInfo(this.mineInfo)
    this.signInfo = await MineDao.syncSignInfo()
    let kvStore: distributedKVStore.SingleKVStore = await KVStoreUtil.getKVStore();
    let self1 = this.dailyLeaningSettingItem
    await kvStore.get('vocCountOfDay', async (err, result) => {
      if (err) {
        console.error(err.message)
      }
      console.info(`vocCountOfDay:${result}`)
      self1.info = result as number
      self1.tmp = result as number
    })
    // 对于 totalVocBound 和 totalVocBoundSettingItem
    let self2 = this.totalVocBoundSettingItem;
    await kvStore.get('totalVocBound', async (err, result) => {
      if (err) {
        console.error(err.message);
      }
      console.info(`totalVocBound:${result}`);
      self2.info = result as number;
      self2.tmp = result as number;
    });

    // 对于 freeVocBound 和 freeVocBoundSettingItem
    let self3 = this.freeVocBoundSettingItem;
    await kvStore.get('freeVocBound', async (err, result) => {
      if (err) {
        console.error(err.message);
      }
      console.info(`freeVocBound:${result}`);
      self3.info = result as number;
      self3.tmp = result as number;
    });

    // 对于 payVocBound 和 payVocBoundSettingItem
    let self4 = this.payVocBoundSettingItem;
    await kvStore.get('payVocBound', async (err, result) => {
      if (err) {
        console.error(err.message);
      }
      console.info(`payVocBound:${result}`);
      self4.info = result as number;
      self4.tmp = result as number;
    });

    // 对于 remindTime 和 remindTimeSettingItem
    let self5 = this.remindTimeSettingItem;
    await kvStore.get('remindTime', async (err, result) => {
      if (err) {
        console.error(err.message);
      }
      console.info(`remindTime:${result}`);
      self5.info = result as string;
      self5.tmp = result as string;
    });

    // 对于 reSignCard 和 reSignCardSettingItem
    let self6 = this.reSignCardSettingItem;
    await kvStore.get('reSignCard', async (err, result) => {
      if (err) {
        console.error(err.message);
      }
      console.info(`reSignCard:${result}`);
      self6.info = result as number;
      self6.tmp = result as number;
    });
  }

  syncUserUpgradeInfo() {
    userUpgradeInfo()
      .then((result:CommonResult)=>{
        if (result.code === 200) {
          let data = result.data as UserUpgradeModel
          this.levelUpRemainDays = data.remainDays
        }
      })
  }

  build() {
    Navigation(this.settingPathStack){
      // 卷轴
      Scroll() {
        Column({ space: Constants.COMMON_SPACE }) {
          // 班级和消息 → 顶部 两侧
          Column() {
            Row() {
              Column() {
                Image($r('app.media.class'))
                  .height('25vp')
              }
              .width(Constants.PERCENT_50)
              .alignItems(HorizontalAlign.Start)

              Row() {
                /*Image($r('app.media.message_wd'))
                  .height('25vp')*/
                Image($r('app.media.message_wd'))
                  .height('25vp')
              }.width(Constants.PERCENT_50)
              .justifyContent(FlexAlign.End)
            }
            .width(Constants.PERCENT_100)
          }
          .width(Constants.PERCENT_100)
          .alignItems(HorizontalAlign.Start)

          // 用户信息
          AccountInfoView({mineInfo:this.mineInfo,levelUpRemainDays:this.levelUpRemainDays,routeUserInfo:()=>{
            this.settingPathStack.pushPathByName('UserInfoPage','')
          }})

          // 签到信息
          SignInfoView()


          // 菜单选项
          List() {
            SettingCell({
              item: this.dailyLeaningSettingItem,
              info: this.vocCntOfDay
            })
            SettingCell({
              item: this.totalVocBoundSettingItem,
              info: this.totalVocBound
            })

            SettingCell({
              item: this.freeVocBoundSettingItem,
              info: this.freeVocBound
            })

            SettingCell({
              item: this.payVocBoundSettingItem,
              info: this.payVocBound
            })

            SettingCell({
              item: this.remindTimeSettingItem,
              info: this.remindTime
            })

            SettingCell({
              item: this.reSignCardSettingItem,
              info: this.reSignCard
            })
          }
          .scrollBar(BarState.Off)
          //.backgroundColor(Color.White)
          .backgroundColor($r('app.color.start_window_background'))
          .width(Constants.PERCENT_100)
          //.height(Constants.SET_LIST_WIDTH)
          .height(6 * 48.25 + 8)
          .divider({
            strokeWidth: $r('app.float.setting_list_strokeWidth'),
            color: Color.Grey,
            //startMargin: $r('app.float.setting_list_startMargin'),
            startMargin: '12vp',
            endMargin: $r('app.float.setting_list_endMargin')
          })
          .borderRadius($r('app.float.setting_list_borderRadius'))
          .padding({
            top: $r('app.float.setting_list_padding'),
            bottom: $r('app.float.setting_list_padding')
          })

          // 退出登录
          Blank()

          Button($r('app.string.setting_button'), { type: ButtonType.Capsule })
            .width(Constants.BUTTON_WIDTH)
            .height($r('app.float.login_button_height'))
            .fontSize($r('app.float.normal_text_size'))
            .fontColor($r('app.color.setting_button_fontColor'))
            .fontWeight(FontWeight.Medium)
            .backgroundColor($r('app.color.setting_button_backgroundColor'))
            .margin({ bottom: $r('app.float.setting_button_bottom') })
            .onClick(this.logout)
        }
        .height(Constants.PERCENT_100)
        .backgroundColor($r('app.color.start_window_background'))
      }
    }
    //.margin({ top: $r('app.float.status_height') })
    .mode(NavigationMode.Stack)
    .navDestination(this.PagesMap)
  }

  @Builder PagesMap(name:string){
    // if (name === 'PageOne'){
    //   PageOne()
    // }
    if (name === 'pay'){
      Pay()
    }
    else if (name === 'payHistory'){
      PayHistoryComponent()
    }else if (name === 'checkin'){
      // 签到界面
      CheckinPage()
    }else if(name === 'UserInfoPage'){
      UserInfoPage()
    }else if(name === 'VocBoundStat'){
      VocBoundStat()
    }
  }
}


