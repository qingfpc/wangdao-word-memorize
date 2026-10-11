import { relationalStore } from "@kit.ArkData"

export class BaseDao{
  // 定义rdbStore，用于数据库存储，类型为RdbStore
  private store?: relationalStore.RdbStore

  setStore(store: relationalStore.RdbStore) {
    this.store = store
  }
  getStore(): relationalStore.RdbStore  {
    if (!this.store) {
      throw new Error('Store is not initialized.')
    }
    return this.store
  }
  constructor(store:relationalStore.RdbStore) {
    this.store = store
  }
}