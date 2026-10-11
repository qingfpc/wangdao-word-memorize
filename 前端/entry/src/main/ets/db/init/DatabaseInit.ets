import { initDao2 } from "../dao/DaoInit"
import { getRdbStore } from "../rdb/RdbStoreConfig"
import { UIContext } from "@ohos.arkui.UIContext";
import { DATABASE_RAW_FILE_NAME, PREFERENCE_NAME } from "./DatabaseConfig";
import { preferences } from "@kit.ArkData";
import { BusinessError } from "@kit.BasicServicesKit";
import { copyFile2Sandbox } from "../../util/file";
import { getPreferences } from "../preferences/PreferencesConfig";


// 初始化数据，初始化Database
export async function  initDatabase() {
  // 通过数据库建库语句来初始化
  if(!await checkDatabaseIsReady()){ // 如果还没初始化数据库则先初始化数据库
    let dataIsReady = await initWordsDatabase() // 复制数据库 并且执行数据库
    let dataPreferences = await getPreferences();
    dataPreferences.putSync('dataIsReady',dataIsReady)
    dataPreferences.flushSync()
  }

  let store = await getRdbStore()
  initDao2(store)

}

export async function initWordsDatabase(){
  // 从GlobalContext中获取Context

  let dbName = DATABASE_RAW_FILE_NAME
  await copyFile2Sandbox(dbName) // 将文件复制到沙箱
  try {
    let store = await getRdbStore()
    await store.restore(dbName)
    console.info(`数据库初始化成功`);
    return true
  } catch (e) {
    const err:BusinessError = e
    console.error(`数据库初始化失败. Code:${err.code}, message:${err.message}`);
    return false
  }

}

async function checkDatabaseIsReady() {
  //let dataPreferences = preferences.getPreferencesSync(this.context, {name:Constants.PREFERENCE_NAME});
  let dataPreferences = await getPreferences();
  if (dataPreferences.hasSync('dataIsReady')) {
    let dataIsReady = dataPreferences.getSync('dataIsReady',false) as boolean
    return dataIsReady
  }else {
    return false;
  }
}


