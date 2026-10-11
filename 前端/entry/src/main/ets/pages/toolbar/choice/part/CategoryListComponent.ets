import { CategoryDao } from "../../../../db/dao/CategoryDao";
import { DaoConfig } from "../../../../db/dao/DaoConfig";
import { Category, CategoryItem } from "../../../../model/Category"
import { MemoryInfo, SelectedBookInfo } from "../../../../model/SelectedBookInfo";
import { GlobalContext } from "../../../../util/GlobalContext";
import { BusinessError } from "@kit.BasicServicesKit";
import { Constants } from "../../../../common/Constants";
import { vocCurrentStat } from "../../../../api/voc";
import { getPreferences } from "../../../../db/preferences/PreferencesConfig";

@Component
export struct CategoryListComponent{
  @Consume wordsChoicePathStack: NavPathStack


  @State categoryList:Array<Category> = []
  @State categoryExpandId:string = ''

  // 类目是否有更新
  private  categoryUpdated:boolean = false

  private categoryDao:CategoryDao = GlobalContext.getContext().getObject(DaoConfig.CATEGORY_DAO) as CategoryDao


  private  updateCategoryList:()=>void =()=> {


      this.categoryList = this.categoryDao.getCategoryList()
    console.info(`categoryList:${JSON.stringify(this.categoryList)}`)
  }

  aboutToAppear(): void {
    this.updateCategoryList()
  }

  onPageShow(): void {
    if (this.categoryUpdated) {
      this.updateCategoryList()
    }
  }

  build() {
    List(){
      // 云词库 -> 收藏单词列表
      ListItem(){
        Row(){

          Text('收藏')
            .fontSize(20)
          Image($r('app.media.collect_fill'))
            .fillColor($r('app.color.words_base'))
           .width(20)
           .height(20)
           .margin({left:5})
        }
        .backgroundColor($r('app.color.start_window_background'))
        .width('100%')
        .padding(5)

      }
      .onClick(()=>{
        this.wordsChoicePathStack.pushPathByName('CollectListPage','')
      })

      ForEach(this.categoryList,(item:Category)=>{
        BookCategoryGroup({ categoryExpandId:this.categoryExpandId,item:item })
      },(item:Category)=>JSON.stringify(item.id))
    }
    // 粘滞效果
    .sticky(StickyStyle.Header)
    .width('100%')
    .height('100%')
    // 滚动条
    .scrollBar(BarState.Off)
    .divider({
      strokeWidth: 1,
      color: $r('app.color.book_category_list_divider'),
      startMargin: 10,
      endMargin: 10
    })
    .borderRadius({topLeft:15,topRight:15})
  }
}
@Component
struct BookCategoryGroup{
  @Link categoryExpandId:string
  private item:Category = {id:'',name:''}
  @Consume selectedBookInfo:SelectedBookInfo
  @Consume wordsChoicePathStack:NavPathStack

  // 保存选择的图书信息
  async saveSelectedBookId() {
    let id = this.selectedBookInfo.selectedBookId
    let preferences = getPreferences()
    // 保存在Preferences中
    preferences.putSync(Constants.SELECTED_BOOK_ID,id)
    preferences?.flush(this.errCallBack);

  }
  async syncBookReciteInfo() {
    let result = await vocCurrentStat(this.selectedBookInfo.selectedBookId)
    if (result.code === 200 && result.data) {
      let memoryInfo = result.data as MemoryInfo
      this.selectedBookInfo.memoryInfo = memoryInfo
    }
    console.info('同步当前书籍背诵情况2：'+ JSON.stringify(this.selectedBookInfo))
  }


  private errCallBack: (err: BusinessError) => void = (err: BusinessError) => {
    if (err) {
      console.error("Failed to flush. Cause: " + err);
      AlertDialog.show({ message: "数据保存失败，请稍后再试！" });
    } else {
      console.info("Succeeded in flushing.");
    }
  };

  @Builder itemHeader(text:string){
    Text(text)
      .fontSize(20)
      .backgroundColor($r('app.color.start_window_background'))
      .width('100%')
      .padding(5)
  }
  build() {
    ListItemGroup({header:this.itemHeader(this.item.name)}){
      if (this.categoryExpandId == this.item.id){
        ForEach(this.item.items,(item:CategoryItem)=>{
          ListItem(){
            Row(){
              vocCategoryItemRowLeft({ item:item })
                .width('75%')
                .height('100%')
              vocCategoryItemRowRight({ item:item })
                .width('15%')
                .height('100%')
            }
            .height(18)
            .alignItems(VerticalAlign.Center)
            .justifyContent(FlexAlign.SpaceBetween)
            .backgroundColor($r('app.color.voc_book_list'))
            .width('100%')
            .onClick(async ()=>{
              this.selectedBookInfo.selectedBookName = item.name
              this.selectedBookInfo.selectedBookId = item.id
              await this.saveSelectedBookId()
              await this.syncBookReciteInfo()
              this.wordsChoicePathStack.pushPathByName('bookVocabulary','')
              // AlertDialog.show({message:`选择了${item.name}`})
            })
          }
        },(item:CategoryItem)=>JSON.stringify(item.id))
      }
    }.onClick(()=>{
      if (this.categoryExpandId == this.item.id){
        this.categoryExpandId = '0'
      }else {
        this.categoryExpandId = this.item.id
      }
    })
  }
}

@Component
struct vocCategoryItemRowLeft{
  private  item:CategoryItem = {id:'',name:''}

  build() {
    Row(){
      Image($r('app.media.point'))
        .height('100%')
      Text(this.item.name)
        .fontSize('15fp')
    }
  }
}

@Component
struct vocCategoryItemRowRight{
  private item:CategoryItem = {id:'',name:''}
  build(){
    Row(){
      Text('15/100')
      Image($r('app.media.more1'))
        .fillColor($r('app.color.words_base'))
        .height('70%')
        .aspectRatio(1)
    }
    .justifyContent(FlexAlign.End)
    .alignItems(VerticalAlign.Center)
    .width('100%')
    .height('100%')
    .padding(10)
  }
}