import { distributedKVStore } from "@kit.ArkData";
import { Context } from "@kit.AbilityKit";
import { GlobalContext } from "../../util/GlobalContext";
import { KV_STORE_NAME } from "../init/DatabaseConfig";
import { DatabaseConfig } from "../rdb/RdbStoreConfig";

export class KVStoreUtil {
  static kvStore: distributedKVStore.SingleKVStore
  static context: Context | undefined = undefined

  static setKVStore(kvStore: distributedKVStore.SingleKVStore) {
    if (KVStoreUtil.kvStore == undefined) {
      KVStoreUtil.kvStore
    }
  }

  static setContext(context: Context) {
    if (!context) {
      const context = GlobalContext.getContext().getObject('context') as Context
      KVStoreUtil.context = context
    } else {
      KVStoreUtil.context = context
    }
  }
  static getContext() {
    if (!KVStoreUtil.context) {
      const context = GlobalContext.getContext().getObject('context') as Context
      KVStoreUtil.context = context
    }
    return KVStoreUtil.context
  }

  static setKVStoreContext() {
    const context = GlobalContext.getContext().getObject('context') as Context
    KVStoreUtil.context = context
  }

  static initKVStore(){
    if (!KVStoreUtil.kvStore) {
      if (!KVStoreUtil.context) {
        KVStoreUtil.setKVStoreContext()
      }
      /*let kvManagerConfig: distributedKVStore.KVManagerConfig = {
        context: KVStoreUtil.context as Context,
        bundleName: 'com.cskaoyan.word.recite'
      };*/
      let kvManagerConfig: distributedKVStore.KVManagerConfig = {
        context: KVStoreUtil.getContext() as Context,
        bundleName: 'com.cskaoyan.word.recite'
      }
      let kvManager = distributedKVStore.createKVManager(kvManagerConfig);
      // -> API: createKVManager(config: KVManagerConfig): KVManager
      // 提供KV-Store数据库选项，用来创建数据库

      // 创建KV-Store数据库，创建完成会执行回调函数
      kvManager.getKVStore(KV_STORE_NAME, DatabaseConfig.KV_STORE_OPTIONS)
        .then((kvStore:distributedKVStore.SingleKVStore)=>{
          KVStoreUtil.kvStore = kvStore
        })
    }
  }

  static async  getKVStore(): Promise<distributedKVStore.SingleKVStore>{
    if (KVStoreUtil.kvStore == undefined) {
      if (!KVStoreUtil.context) {
        KVStoreUtil.setKVStoreContext()
      }
      /*let kvManagerConfig: distributedKVStore.KVManagerConfig = {
        context: KVStoreUtil.context as Context,
        bundleName: 'com.cskaoyan.word.recite'
      };*/
      let kvManagerConfig: distributedKVStore.KVManagerConfig = {
        context: KVStoreUtil.getContext() as Context,
        bundleName: 'com.cskaoyan.word.recite'
      }
      let kvManager = distributedKVStore.createKVManager(kvManagerConfig);
      // -> API: createKVManager(config: KVManagerConfig): KVManager
      // 提供KV-Store数据库选项，用来创建数据库

      // 创建KV-Store数据库，创建完成会执行回调函数
      return await kvManager.getKVStore(KV_STORE_NAME, DatabaseConfig.KV_STORE_OPTIONS);
    }
    return KVStoreUtil.kvStore

  }

  static hasSaveCheckin(){
      
  }
}