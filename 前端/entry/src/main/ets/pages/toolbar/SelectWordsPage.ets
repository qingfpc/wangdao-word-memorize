import { vocCurrentStat } from "../../api/voc"
import { Constants } from "../../common/Constants"
import { BookDao } from "../../db/dao/BookDao"
import { DaoConfig } from "../../db/dao/DaoConfig"
import { getPreferences } from "../../db/preferences/PreferencesConfig"
import { MemoryInfo, SelectedBookInfo } from "../../model/SelectedBookInfo"
import { GlobalContext } from "../../util/GlobalContext"
import { BookVocabulary } from "./choice/BookVocabulary"
import { CollectListPage } from "./choice/CollectListPage"
import { CategoryListComponent } from "./choice/part/CategoryListComponent"
import { CurrentBook } from "./choice/part/CurrentBook"
import { SelectedWords } from "./choice/part/SelectedWords"
import { SelectedWordsPage } from "./choice/SelectedWordsPage"

@Component
export struct SelectWordsPage{

  // 跳往其他页面的栈
  @Provide wordsChoicePathStack: NavPathStack = new NavPathStack()
  @Provide selectedBookInfo:SelectedBookInfo = new SelectedBookInfo()
  // 是否有书籍已经被选中
  @State hasBookSelected:boolean = false

  bookDao = GlobalContext.getContext().getObject(DaoConfig.BOOK_DAO) as BookDao

  async aboutToAppear() {
    //this.changeCurrPattern()
    // 获得当前选中的二级词汇类目
    let preferences = getPreferences()
    if (preferences.hasSync(Constants.SELECTED_BOOK_ID)) {
      this.selectedBookInfo.selectedBookId = preferences.getSync(Constants.SELECTED_BOOK_ID,'') as string
      this.hasBookSelected = true
      this.selectedBookInfo.selectedBookName = this.bookDao.queryBookNameById(this.selectedBookInfo.selectedBookId)
      await this.syncBookReciteInfo()
    }
  }

  // 同步当前书籍背诵信息
  async syncBookReciteInfo() {
    let result = await vocCurrentStat(this.selectedBookInfo.selectedBookId)
    if (result.code === 200 && result.data) {
      let memoryInfo = result.data as MemoryInfo
      this.selectedBookInfo.memoryInfo = memoryInfo
    }
    console.info('同步当前书籍背诵情况：'+ JSON.stringify(this.selectedBookInfo))
  }

  build() {
    Navigation(this.wordsChoicePathStack){
      List(){

        ListItemGroup(){
          ListItem(){
            //当前的书籍
            CurrentBook()
          }
          .width('100%')

          ListItem(){
            // 已经选中的单词
            SelectedWords()
          }
          .width('100%')
          .height('50vp')

        }
        .width('100%')
        .backgroundColor($r('app.color.start_window_background'))
        .borderRadius(10)
        .divider({strokeWidth:1,color:$r('app.color.book_words_divider'),startMargin:10,endMargin:10})
        // 已选单词
        ListItemGroup({header:this.wordLibraryHeader()}){
          CategoryListComponent()
        }
        .backgroundColor($r('app.color.start_window_background'))
        .borderRadius(10)
      }
      .width('100%')
      .alignListItem(ListItemAlign.Center)
      .height('100%')
      .sticky(StickyStyle.Header)
      .scrollBar(BarState.Off)
    }
    .mode(NavigationMode.Stack)
    //.margin({top:40})
    .navDestination(this.PagesMap)
  }
  // Navigation路由
  @Builder PagesMap(name:string){
    if (name === 'bookVocabulary'){
      BookVocabulary()
    }else if(name === 'CollectListPage')
    {
      CollectListPage()
    }else if(name === 'SelectedWordsPage')
    {
      SelectedWordsPage()
    }

  }

  // 单词的选择标头
  @Builder
  wordLibraryHeader(){
    Column(){
      Column(){
        Text('Word Library')
          .fontColor(Color.White)
          .textAlign(TextAlign.Start)
          .fontSize('10vp')
          .width('90%')
          .height('100%')
      }.width('50%')
      .height('20vp')
      .backgroundColor($r('app.color.words_base'))
    }
    .width('100%')
    .height('40vp')
    .alignItems(HorizontalAlign.Start)

  }

}