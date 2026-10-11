import { AuthUser } from '@hw-agconnect/auth'
import { vocDetail } from '../api/voc'
import { Constants } from '../common/Constants'
import { DaoConfig } from '../db/dao/DaoConfig'
import { VocabularyDao } from '../db/dao/VocabularyDao'
import { CommonResult } from '../model/CommonResult'
import { VocabularyData } from '../model/VocabularyData'
import { GlobalContext } from '../util/GlobalContext'
import { HomePage } from './toolbar/HomePage'
import { MinePage } from './toolbar/MinePage'
import { SelectWordsPage } from './toolbar/SelectWordsPage'
import { AiTab } from './toolbar/AiTab'
import { loginBackServer } from '../api/login'


// 登录成功显示的页面
@Component
export struct MainPage {
  // 从父组件接收isLogin状态（是否已登录）
  @Consume("isLogin") isLogin: boolean
  // 从父组件接收当前的用户信息
  @Consume("authUser") authUser: AuthUser
  // tab菜单索引
  @Provide currentIndex: number = Constants.HOME_TAB_INDEX
  // 正在背诵的单词的id数组初始化
  @Provide recitingVocabularyIdArray: string[] = []

  @Consume backToken:string

  // 当天的背诵任务是否完成
  // 当前正在背诵的单词信息，用来同步背单词页面的数据
  @Provide currentVocabulary: VocabularyData = {
    content: '',
    derivedId: '',
    examples: [],
    id: '',
    interpretations: [],
    notRemember: 0,
    reviewed: 0,
    favorite:0,
    ukPhonetic: '',
    ukPronounciation: '',
    usPhonetic: '',
    usPronounciation: '',
    userExamples: [],
    userInterpretations: []
  }
  @Provide selectVocabularyLeadingToAiVocChange:boolean=false
  // tab控制器
  private tabsController: TabsController = new TabsController();
  private dao:VocabularyDao= GlobalContext.getContext().getObject(DaoConfig.VOCABULARY_DAO) as VocabularyDao


  async aboutToAppear() {
    // 获取当前要背的单词
    await this.fetchTodayRecitingVocabulary()

    console.info(`要背诵的单词id：${JSON.stringify(this.recitingVocabularyIdArray)}`)
    let currentId = this.recitingVocabularyIdArray[this.recitingVocabularyIdArray.length -1]
    if (currentId) {
      console.info(`backToken:${this.backToken}`)
      setTimeout(async ()=>{
        let result = await vocDetail(currentId) as CommonResult
        if (result.code === 200) {
          let data = result.data as VocabularyData
          if (data) {
            this.currentVocabulary = data
          }
        }
      },500)
      // let result = await vocDetail(currentId) as CommonResult
      // if (result.code === 200) {
      //   let data = result.data as VocabularyData
      //   if (data) {
      //     this.currentVocabulary = data
      //   }
      // }
    }
  }
  async fetchTodayRecitingVocabulary() {

    if (this.authUser?.getUid()) {

      let array = this.dao.fetchRecitingVocabularyIdArray(this.authUser?.getUid());
      this.recitingVocabularyIdArray = this.recitingVocabularyIdArray.concat(array).filter((value, index, self) => {
        return self.indexOf(value) === index;})

    }
  }

  build() {
    Tabs({ controller: this.tabsController }) {
      // 首页
      TabContent(){
        HomePage()
      }
      .tabBar(this.tabBuilder('首页',Constants.HOME_TAB_INDEX,$r('app.media.home_selected'),$r('app.media.home_normal')))


      // 选词
      TabContent() {
        //Sign()
        SelectWordsPage()
      }
      .tabBar(this.tabBuilder('选词', Constants.WORDS_CHOICE_TAB_INDEX,
        $r('app.media.choice_selected'), $r('app.media.choice_normal')))

      // 签到
      TabContent() {
        //StatPage()
        AiTab()
      }
      .tabBar(this.tabBuilder('AI阅读', Constants.AI_TAB_INDEX,
        $r('app.media.statistic_selected'), $r('app.media.statistic_normal')))


      // 我的
      TabContent() {
        MinePage()
      }
      .tabBar(this.tabBuilder('我的', Constants.MINE_TAB_INDEX,
        $r('app.media.mine_selected'), $r('app.media.mine_normal')))
    }
    //.barHeight(56)
    .barPosition(BarPosition.End)
    .height('100%')
    .width('100%')
    .onChange(index => {
      console.info(`this.currentIndex:${this.currentIndex}`)
      this.currentIndex = index
    })
    .expandSafeArea([SafeAreaType.SYSTEM],[SafeAreaEdge.BOTTOM])
    //.expandSafeArea([SafeAreaType.SYSTEM],[SafeAreaEdge.TOP,SafeAreaEdge.BOTTOM])

  }

  // 切换按钮
  @Builder tabBuilder(title: string, index: number, selectedImg: Resource, normalImg: Resource) {
    Column() {
      Image(this.currentIndex === index ? selectedImg : normalImg)
        .width(25)
        .aspectRatio(1)
      Text(title)
        .margin({ top: 4 })
        .fontSize('10fp')
        .fontColor(this.currentIndex === index ? $r('app.color.mainPage_selected') : $r('app.color.mainPage_normal'))
    }
    .justifyContent(FlexAlign.Center)
    //.height(56)
    .width(Constants.PERCENT_100)
    .onClick(() => {
      this.currentIndex = index;
      this.tabsController.changeIndex(this.currentIndex);
    })
    .backgroundColor($r('app.color.start_window_background'))
    //.expandSafeArea([SafeAreaType.SYSTEM],[SafeAreaEdge.BOTTOM])

  }
}