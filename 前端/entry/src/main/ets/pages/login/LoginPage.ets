// 导入授权模块、用户类型和验证码操作类型
import auth, { AuthUser, VerifyCodeAction } from "@hw-agconnect/auth";
// 导入业务错误类型
import { BusinessError } from "@kit.BasicServicesKit";
import { authentication, loginComponentManager, LoginWithHuaweiIDButton } from "@kit.AccountKit";
import { hilog } from "@kit.PerformanceAnalysisKit";
import { QuickLoginButtonComponent } from "../../util/login/QuickLoginButtonComponent";
import { util } from "@kit.ArkTS";
import { loginPhone, loginBackServer } from "../../api/login";
import { getPreferences } from "../../db/preferences/PreferencesConfig";
import { HAS_SAVED_CHECKIN_DATA, HAS_SAVED_REVIEW_DATA } from "../../db/init/DatabaseConfig";


// 扩展Row组件，定义一个自定义的布局样式，设置子元素水平排列、宽度100%等
@Extend(Row)
function inputRow(){
  .justifyContent(FlexAlign.SpaceBetween) // 设置子元素水平对齐方式为左右分散
  .width('100%') // 设置宽度为100%
  .height(40) // 设置高度为40vp
  .margin({top: '10vp'}) // 设置上边距为10vp
}

@Component // 组件装饰器
export struct LoginPage {

  // 从父组件接收登录状态和用户信息
  @Consume("isLogin") isLogin: boolean
  @Consume("authUser") authUser: AuthUser

  // 本地状态，用于存储手机号、验证码等信息
  @State hasMobile: boolean = false;
  @State hasVerifyCode: boolean = false;
  @State mobile: string = ''; // 存储手机号
  @State verifyCode: string = ''; // 存储验证码

  @Consume isFirst:boolean
  @Consume backToken:string

  getQuickLoginAnonymousPhone() {
    // 创建授权请求，并设置参数
    const authRequest = new authentication.HuaweiIDProvider().createAuthorizationWithHuaweiIDRequest();
    // 获取匿名手机号需传quickLoginAnonymousPhone这个scope，传参之前需要先申请“华为账号一键登录”权限
    authRequest.scopes = ['quickLoginAnonymousPhone'];
    // 用于防跨站点请求伪造
    authRequest.state = util.generateRandomUUID();
    // 一键登录场景该参数必须设置为false
    authRequest.forceAuthorization = false;
    const controller = new authentication.AuthenticationController();
    try {
      controller.executeRequest(authRequest).then((response: authentication.AuthorizationWithHuaweiIDResponse) => {
        // 获取到UnionID、OpenID、匿名手机号
        const unionID = response.data?.unionID;
        const openID = response.data?.openID;
        const anonymousPhone = response.data?.extraInfo?.quickLoginAnonymousPhone as string;
        if (anonymousPhone) {
          hilog.info(0x0000, 'testTag', 'Succeeded in authentication.');
          const quickLoginAnonymousPhone: string = anonymousPhone;
          console.info(quickLoginAnonymousPhone)
          return;
        }
        hilog.info(0x0000, 'testTag', 'Succeeded in authentication. AnonymousPhone is empty.');
        // 未获取到匿名手机号需要跳转到应用自定义的登录页面
      }).catch((error: BusinessError) => {
        this.dealAllError(error);
      })
    } catch (error) {
      this.dealAllError(error);
    }
  }

  // 错误处理
  dealAllError(error: BusinessError): void {
    hilog.error(0x0000, 'testTag',
      `Failed to get quickLoginAnonymousPhone, errorCode is ${error.code}, errorMessage is ${error.message}`);
  }
  // 构造LoginWithHuaweiIDButton组件的控制器
  controller: loginComponentManager.LoginWithHuaweiIDButtonController =
    new loginComponentManager.LoginWithHuaweiIDButtonController()
      .onClickLoginWithHuaweiIDButton((error: BusinessError, response: loginComponentManager.HuaweiIDCredential) => {
        if (error) {
          hilog.error(0x0000, 'testTag',
            `Failed to onClickLoginWithHuaweiIDButton. Code: ${error.code}, message: ${error.message}`);
          return;
        }

        if (response) {
          hilog.info(0x0000, 'testTag', 'Succeeded in getting response.');
          const authCode = response.authorizationCode;
          const openID = response.openID;
          const unionID = response.unionID;
          const idToken = response.idToken;
          this.isLogin = true
          // 开发者处理authCode、openID、unionID、idToken
          console.info(`authCode:${authCode}`)
          console.info(`openID:${openID}`)
          console.info(`unionID:${unionID}`)
          console.info(`idToken:${idToken}`)
        }
      });

  // 请求验证码的方法
  private fetchVerifyCode: () => void = () => {
    auth.requestVerifyCode({
      action: VerifyCodeAction.REGISTER_LOGIN, // 验证码请求的动作（用于注册或登录）
      lang: 'zh_CN', // 设置语言为中文
      sendInterval: 60, // 设置验证码的发送间隔时间为60秒
      verifyCodeType: {
        phoneNumber: this.mobile, // 使用手机号作为验证
        countryCode: "86", // 国家代码（中国）
        kind: "phone", // 验证码类型为手机验证码
      }
    }).then(verifyCodeResult => {
      // 验证码申请成功
      console.info(`验证码申请成功`)
      AlertDialog.show({ message: "验证码已发送" }) // 弹出提示框告知用户验证码已发送
    }).catch((error: Error) => {
      // 验证码申请失败
      AlertDialog.show({ message: "验证码发送失败" })
      console.info(`验证码申请失败:${JSON.stringify(error)}`) // 打印失败信息
    })

  }

  aboutToAppear(): void {
    this.getQuickLoginAnonymousPhone()
  }
  // 登录的方法
  private login = async  () => {
    try {
      let user = await auth.signIn({
        credentialInfo: {
          kind: 'phone', // 登录凭证类型为手机号
          phoneNumber: this.mobile, // 使用手机号进行登录
          countryCode: '86', // 国家代码（中国）
          verifyCode: this.verifyCode // 使用验证码进行登录
        }
      })
      // 登录成功
      this.isLogin = true
      this.authUser = user.getUser() // 获取当前用户信息

      //AlertDialog.show({ message: `登录成功，欢迎${user.getUser().getUid()}` }) // 弹出登录成功提示
    } catch (e) {
      // 登录失败
      AlertDialog.show({ message: `登录失败` }) // 弹出登录失败提示
    }


  }


  // 定义样式函数，设置组件的宽度和高度为100%
  @Styles percent100() {
    .width('100%')
    .height('100%')
  }

  // 构建页面内容
  build() {
    Column() {
      // 页面标题
      Row() {
        Image($r('app.media.app_icon'))
          .width(40)
          .height(40)
          .borderRadius(10)
          .margin({right:'10vp'})
        Text('王道词王') // 显示“登录页面”标题
          .fontSize('25fp') // 设置字体大小
          .textAlign(TextAlign.Center) // 文本居中对齐
          .height('30.00vp')

      }
      .height('90vp') // 设置高度
      .width('100%') // 设置宽度
      .justifyContent(FlexAlign.Center)
      .alignItems(VerticalAlign.Center) // 设置内容水平居中

      // 手机号输入框和验证码输入框
      Column() {
        // 手机号输入框
        Row() {
          TextInput({ placeholder: "请输入手机号" }) // 提示文本为“请输入手机号”
            .width('100%') // 设置宽度为100%
            .textAlign(TextAlign.Start) // 文本左对齐
            .onChange((value) => {
              this.mobile = value // 保存输入的手机号
              this.hasMobile = this.mobile.length == 11 // 判断手机号是否合法
            })
        }
        .inputRow() // 应用自定义样式

        // 验证码输入框和获取验证码按钮
        Row() {
          TextInput({ placeholder: "请输入验证码" }) // 验证码输入框
            .width('65%') // 设置宽度为65%
            .onChange((value) => {
              this.verifyCode = value // 保存输入的验证码
              this.hasVerifyCode = this.verifyCode.length == 6 // 判断验证码是否合法
            })

          Button('获取验证码') // 获取验证码按钮
            .fontSize('15fp') // 设置字体大小
            .backgroundColor(this.hasMobile ? '#ff3590fd' : '#ffe2e2e2') // 设置按钮颜色，只有手机号有效时按钮颜色才为粉色
            .onClick(this.fetchVerifyCode) // 点击按钮时请求验证码
        }.inputRow() // 应用自定义样式

        // 登录按钮
        Row() {
          Button('登录') // 显示登录按钮
            .fontSize('15fp') // 设置字体大小
            .width('70%') // 设置按钮宽度为70%
            .backgroundColor(this.hasMobile && this.hasVerifyCode ? '#fffab626' : '#ffe2e2e2') // 设置按钮背景色，只有手机号和验证码都有效时按钮颜色才为正常颜色
            .onClick(async ()=>{
              await this.login()
              await this.loginAndInit();
            }) // 点击按钮时执行登录操作

        }
        .width('100%') // 设置宽度为100%
        .height(40) // 设置高度为40vp
        .margin({ top: 40 }) // 设置上边距
        .justifyContent(FlexAlign.Center) // 设置内容水平居中
        // 登录按钮
        /*Row(){
          LoginWithHuaweiIDButton({
            params: {
              // LoginWithHuaweiIDButton支持的样式
              style: loginComponentManager.Style.BUTTON_RED,
              // 账号登录按钮在登录过程中展示加载态
              extraStyle: {
                buttonStyle: new loginComponentManager.ButtonStyle().loadingStyle({
                  show: true
                })
              },
              // LoginWithHuaweiIDButton的边框圆角半径
              borderRadius: 24,
              // LoginWithHuaweiIDButton支持的登录类型
              loginType: loginComponentManager.LoginType.ID,
              // LoginWithHuaweiIDButton支持按钮的样式跟随系统深浅色模式切换
              supportDarkMode: true,
              // verifyPhoneNumber：如果华为账号用户在过去90天内未进行短信验证，是否拉起Account Kit提供的短信验证码页面
              verifyPhoneNumber: true
            },
            controller: this.controller
          })
            .height(80)

        }*/
      }
      .width('90%') // 设置宽度为90%
      .height('70%') // 设置高度为70%
    }
    .percent100() // 设置Column组件的宽高为100%
    .justifyContent(FlexAlign.Center) // 内容水平居中
  }

  /*private async loginAndInit() {
    await this.login();
    let loginData = await loginBackServer();
    this.isFirst = loginData.isFirst;

    this.backToken = loginData.token;

  }*/
  loginAndInit:()=> void = async () => {

  }
}
