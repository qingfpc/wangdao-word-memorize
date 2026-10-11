import { distributedKVStore, relationalStore } from "@kit.ArkData"
import { GlobalContext } from "../../util/GlobalContext"

const RELATIONAL_STORE_NAME = 'recite_words_rdb.db'
export class DatabaseConfig{

  static readonly  RELATIONAL_STORE_CONFIG :relationalStore.StoreConfig= {
    name: RELATIONAL_STORE_NAME, // 数据库文件名
    securityLevel: relationalStore.SecurityLevel.S1, // 数据库安全级别
  };
  // KV-Store的配置文件提取
  static readonly KV_STORE_OPTIONS: distributedKVStore.Options = {
    createIfMissing: true,
    encrypt: false,
    backup: false,
    autoSync: false,
    // kvStoreType不填时，默认创建多设备协同数据库
    kvStoreType: distributedKVStore.KVStoreType.SINGLE_VERSION,
    // 多设备协同数据库：kvStoreType: distributedKVStore.KVStoreType.DEVICE_COLLABORATION,
    securityLevel: distributedKVStore.SecurityLevel.S1
  };
}
export async function getRdbStore(): Promise<relationalStore.RdbStore>{
  const context = GlobalContext.getContext().getObject('context') as Context
  return await relationalStore.getRdbStore(context,DatabaseConfig.RELATIONAL_STORE_CONFIG)
}
