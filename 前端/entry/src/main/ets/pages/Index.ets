import auth, { AuthUser } from '@hw-agconnect/auth';
import axios, { InternalAxiosRequestConfig } from '@ohos/axios';
import axiosInstance, { TOKEN_HEADER } from '../api/axios';
import { loginBackServer } from '../api/login';
import { HAS_SAVED_CHECKIN_DATA, HAS_SAVED_REVIEW_DATA } from '../db/init/DatabaseConfig';
import { getPreferences } from '../db/preferences/PreferencesConfig';
import { DatabaseConfig } from '../db/rdb/RdbStoreConfig';
import { SignInfo } from '../model/SignInfo';
import { CheckinService } from '../service/CheckinService';
import { VocabularyReviewService } from '../service/VocabularyReviewService';
import { LoginPage } from './login/LoginPage';
import { MainPage } from './MainPage';
import { BusinessError } from '@kit.BasicServicesKit';

@Entry
@Component
struct Index {
  // 提供者装饰器：标记是否已登录的状态
  @Provide('isLogin') isLogin: boolean = false
  @Provide signInfo: SignInfo = new SignInfo()
  //@Provide backServerUserId:string = ''

  // 提供者装饰器：标记当前用户对象（未登录时为undefined）
  @Provide('authUser') authUser: AuthUser | null = null;
  @Provide @Watch('tokenChange') backToken: string = ''
  @Provide isFirst: boolean = false
  checkinService: CheckinService = new CheckinService();
  vocabularyReviewService: VocabularyReviewService = new VocabularyReviewService()

  tokenChange() {
    console.info('tokenChange')
    axiosInstance.interceptors.request.use((config: InternalAxiosRequestConfig) => {
      // 在发送请求之前做些什么
      console.info('请求拦截:' + config.url)
      config.headers[TOKEN_HEADER] = this.backToken
      //config.headers.concat(`${TOKEN_HEADER}:${this.backToken}`)
      return config;
    })
  }

  async aboutToAppear() {
    await this.loginAndInit()
  }

  loginAndInit = async () => {
    this.authUser = await auth.getCurrentUser()
    let loginData = await loginBackServer()
    console.info(`${JSON.stringify(loginData)}`)
    this.backToken = loginData.token
    this.isFirst = loginData.isFirst
    //await this.checkinService.saveAllCheckinData()
    if (!this.authUser) {
      console.info('未登录')
      return
    }
    let preferences = getPreferences();
    if (this.isFirst || !preferences.hasSync(HAS_SAVED_CHECKIN_DATA)) {
      // 获取全部签到信息并保存
      console.info('正在保存签到数据')
      this.checkinService.saveAllCheckinData(this.authUser?.getUid())
        .then(() => {
          console.info('成功保存签到数据')
          preferences.putSync(HAS_SAVED_CHECKIN_DATA, true)
          preferences.flushSync()
        })
    } else {
      console.info('已经保存过签到数据了')
    }

    if (this.isFirst || !preferences.hasSync(HAS_SAVED_REVIEW_DATA)) {
      //
      console.info('正在保存复习数据')
      this.vocabularyReviewService.saveAllReviewData(this.authUser?.getUid())
        .then(() => {
          console.info('完成保存复习数据')
          preferences.putSync(HAS_SAVED_REVIEW_DATA, true)
          preferences.flushSync()
        }).catch((err: BusinessError) => {
        console.error(err.stack)
      })
    } else {

      console.info('已经保存过复习数据了')
    }
    if (this.authUser) {
      // 如果用户已登录，更新状态
      this.isLogin = true
      // 打印当前用户信息
      console.info(`auth:${JSON.stringify(this.authUser)}`)

      //AlertDialog.show({ message: `登录成功，欢迎${this.authUser.getUid()}` }) // 弹出登录成功提示

    } else {
      // 获取用户信息失败时弹出错误提示
      AlertDialog.show({ message: "获取用户信息失败" })
    }
  }

  build() {
    Column() {
      if (this.authUser) {
        MainPage()
      } else {
        LoginPage({
          loginAndInit: () => {
            this.loginAndInit()
          }
        })
      }

    }
    .height('100%')
    .width('100%')
    .expandSafeArea([SafeAreaType.SYSTEM], [SafeAreaEdge.TOP, SafeAreaEdge.BOTTOM])
  }
}