import { relationalStore } from "@kit.ArkData";

export class BookDao {
  // 定义rdbStore，用于数据库存储，类型为RdbStore
  static readonly TABLE_NAME = 'voc_book'
  private store: relationalStore.RdbStore

  constructor(store: relationalStore.RdbStore ) {
    this.store = store;
  }

  setStore(store: relationalStore.RdbStore) {
    this.store = store
  }

  findChapter(selectedBookId: string) {

    let predicates =  new relationalStore.RdbPredicates(BookDao.TABLE_NAME)
    predicates.equalTo('id',selectedBookId)
    let resultSet = this.store.querySync(predicates,['chapter_count'])
    if (resultSet.goToNextRow()) {
      return resultSet.getLong(resultSet.getColumnIndex('chapter_count'))
    }else {
      return 0
    }
  }

  queryBookNameById(id: string): string {

    let predicates: relationalStore.RdbPredicates = new relationalStore.RdbPredicates('voc_book');
    predicates.equalTo('id', id)
      .and()
      .equalTo('status', 1)
      .and()
      .equalTo('is_deleted', 0)
    let resultSet = this.store?.querySync(predicates)
    if (resultSet?.goToNextRow()) {
      try {
        let name = resultSet.getString(resultSet.getColumnIndex('name'))
        return name
      } catch (e) {
        console.error(e)
      }
    }
    return ''

  }
}