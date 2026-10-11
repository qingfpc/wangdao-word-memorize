import { relationalStore } from "@kit.ArkData"
import { Category, CategoryItem } from "../../model/Category";


export class CategoryDao{

  // 定义rdbStore，用于数据库存储，类型为RdbStore
  private store: relationalStore.RdbStore

  constructor(store: relationalStore.RdbStore) {
    this.store = store
  }

  setStore(store:relationalStore.RdbStore){
    this.store = store
  }

  getCategoryList(){
    let categoryList:Array<Category> = []
    let predicates = new relationalStore.RdbPredicates('book_category');
    //predicates.equalTo('status',1)
    predicates.equalTo('is_deleted',0)
    let resultSet = this.store?.querySync(predicates,['id','category_name'])
    while (resultSet?.goToNextRow()){
      let id = resultSet.getString(resultSet.getColumnIndex('id'));
      let name = resultSet.getString(resultSet.getColumnIndex('category_name'))
      let category:Category = {
        id:id,
        name:name
      }
      category.items  = this.getCategoryItems(id)
      //console.info(JSON.stringify(category))
      categoryList.push(category)
    }
    return categoryList;

  }

  getCategoryItems(categoryId:string){
    let predicates = new relationalStore.RdbPredicates('voc_book');
    predicates.equalTo('bc_id',categoryId).orderByAsc('sort_by')
    let resultSet = this.store?.querySync(predicates);
    const items:CategoryItem[] = []
    while (resultSet?.goToNextRow()) {
      let name = resultSet.getString(resultSet.getColumnIndex('name'))
      let id = resultSet.getString(resultSet.getColumnIndex('id'))
      let item:CategoryItem = {
        id:id,
        name:name
      }
      items.push(item)
    }
    return items
  }


}

