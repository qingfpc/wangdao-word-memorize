import { distributedKVStore, relationalStore } from "@kit.ArkData"
import { ArkTSUtils, collections, HashMap } from "@kit.ArkTS";
import { userCheckinStat, userInfo, vocMyPage } from "../../api/user";
import { CommonResult } from "../../model/CommonResult";
import { MineInfo } from "../../model/MineInfo";
import { SignInfo } from "../../model/SignInfo";
import { KVStoreUtil } from "../kvstore/KVStoreUtil";

export interface VocInfo {
  freeVocBound: number;
  payVocBound: number;
  reSignCard: number;
  remindTime: string;
  totalVocBound: number;
  vocCountOfDay: number;
}

export class MineDao {
  static async syncMineInfo(mineInfo: MineInfo): Promise<MineInfo> {
    let result: CommonResult = await userInfo()
    if (result.code === 200) {
      if (result.data) {
        mineInfo = JSON.parse(JSON.stringify(result.data))
      }
    }
    return mineInfo;
  }

  static async syncSignInfo(): Promise<SignInfo> {
    let result: CommonResult = await userCheckinStat()
    let signInfo: SignInfo = new SignInfo()
    if (result.code === 200) {
      if (result.data) {
        signInfo = result.data as SignInfo
      }
    }
    return signInfo;
  }

  // 定义rdbStore，用于数据库存储，类型为RdbStore
  private store: relationalStore.RdbStore | undefined

  setStore(store: relationalStore.RdbStore) {
    this.store = store
  }


  static async syncMineSettingInfo() {
    let result = await vocMyPage();
    let kvStore = await KVStoreUtil.getKVStore();
    console.info(JSON.stringify(result.data))
    if (result.data) {
      let keys = Object.keys(result.data);
      keys.forEach(async (key) => {
        if (result.data) {
          //console.info('value')
          console.info(result.data[key])
          if (result.data[key] == null || result.data[key] == undefined) {
            await kvStore.put(key, 0);
          } else {

            await kvStore.put(key, result.data[key]);
          }
        }
      })
    }
    kvStore.get('vocCountOfDay', async (err, result) => {
      if (err) {
        console.error(`get:Failed to get KVStore: Code:${err.code},message:${err.message}`);
        return;
      }
      console.info('result:' + result)
    })
  }

  static async getSyncMineInfo(): Promise<Map<string, string>> {
    let map: Map<string, string> = await new Map<string, string>()
    let kvStore: distributedKVStore.SingleKVStore = await KVStoreUtil.getKVStore();
    kvStore.get('freeVocBound', async (err, result) => {
      if (err) {
        console.error(`get:Failed to get KVStore: Code:${err.code},message:${err.message}`);
        return;
      }
      console.info('result:' + result)
      map.set('freeVocBound', JSON.stringify(result))
    })
    kvStore.get('payVocBound', (err, result) => {
      if (err) {
        console.error(`get:Failed to get KVStore: Code:${err.code},message:${err.message}`);
        return;
      }
      map.set('payVocBound', JSON.stringify(result))
    })
    kvStore.get('reSignCard', (err, result) => {
      if (err) {
        console.error(`get:Failed to get KVStore: Code:${err.code},message:${err.message}`);
        return;
      }
      console.info('result:' + result)
      if (result == undefined || result == null) {

        map.set('reSignCard', '0')
      } else {

        map.set('reSignCard', JSON.stringify(result))
      }
    })
    kvStore.get('remindTime', (err, result) => {
      if (err) {
        console.error(`get:Failed to get KVStore: Code:${err.code},message:${err.message}`);
        return;
      }
      console.info('result:' + result)
      map.set('remindTime', JSON.stringify(result))
    })
    kvStore.get('totalVocBound', (err, result) => {
      if (err) {
        console.error(`get:Failed to get KVStore: Code:${err.code},message:${err.message}`);
        return;
      }
      console.info('result:' + result)
      map.set('totalVocBound', JSON.stringify(result))
    })
    kvStore.get('vocCountOfDay', async (err, result) => {
      if (err) {
        console.error(`get:Failed to get KVStore: Code:${err.code},message:${err.message}`);
        return;
      }
      console.info('result:' + result)
      map.set('vocCountOfDay', JSON.stringify(result))
    })
    return map
  }
}