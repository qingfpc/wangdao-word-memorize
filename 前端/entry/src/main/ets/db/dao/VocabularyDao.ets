import { relationalStore, ValuesBucket } from "@kit.ArkData";
import { VocabularyReview } from "../../model/ChapterVocabulary";
import { GlobalContext } from "../../util/GlobalContext";
import { getRdbStore } from "../rdb/RdbStoreConfig";

export class VocabularyDao {
  selectDataByUserIdAndDate(userId: string, date: string):UserVocRevData[] {
    let resultSet = this.store?.querySqlSync('SELECT * FROM user_voc_rev where user_id =? and date(updated_time) =? and is_deleted = 0', [userId, date]);
    let vocRevList: UserVocRevData[] = [];
    if (resultSet) {
      while (resultSet.goToNextRow()) {

        /**
         * export interface UserVocRevData {
         id?: string;
         vocId?: string;
         userId: string;
         factor: number;
         reviewInterval: number;
         nextReviewDate: string;
         usn: number;
         type: number;
         createTime: string;
         updatedTime: string;
         isDeleted: number;
         familiarity?: number;
         consecutiveFailCount?: number;
         totalReview?: number;
         currentCount?: number;
         }
         */
        let vocRevId = resultSet.getString(resultSet.getColumnIndex('id'));
        let vocId = resultSet.getString(resultSet.getColumnIndex('voc_id'));
        let nextReviewDate = resultSet.getString(resultSet.getColumnIndex('next_review_date'));
        let familiarity = resultSet.getLong(resultSet.getColumnIndex('familiarity'));
        let factor = resultSet.getLong(resultSet.getColumnIndex('factor'));
        let reviewInterval = resultSet.getLong(resultSet.getColumnIndex('review_interval'));
        let usn = resultSet.getLong(resultSet.getColumnIndex('usn'));
        let type = resultSet.getLong(resultSet.getColumnIndex('type'));
        let createTime = resultSet.getString(resultSet.getColumnIndex('create_time'));
        let updatedTime = resultSet.getString(resultSet.getColumnIndex('updated_time'));
        let isDeleted = resultSet.getLong(resultSet.getColumnIndex('is_deleted'));
        let consecutiveFailCount = resultSet.getLong(resultSet.getColumnIndex('consecutive_fail_count'));
        let totalReview = resultSet.getLong(resultSet.getColumnIndex('total_review'));
        let currentCount = resultSet.getLong(resultSet.getColumnIndex('current_count'));


        vocRevList.push({
          id: vocRevId,
          userId: userId,
          vocId: vocId,
          nextReviewDate: nextReviewDate,
          familiarity: familiarity,
          factor: factor,
          reviewInterval: reviewInterval,
          usn: usn,
          type: type,
          createTime: createTime,
          updatedTime: updatedTime,
          isDeleted: isDeleted,
          consecutiveFailCount: consecutiveFailCount,
          totalReview: totalReview,
          currentCount: currentCount
        } as UserVocRevData);
      }
    }
    return vocRevList;

  }
  insertBatch(userVocRevData: UserVocRevData[]) {
    try {
      this.store?.batchInsertSync(this.tableName, userVocRevData.map(item => this.dataTransfer(item)))
    } catch (e) {
      console.error(e)
    }
  }
  selectByUserIdAndDate(userId: string, nextReviewDate?: string):VocabularyReview[] {
    let baseSql = 'SELECT DISTINCT u.id vocRevId,v.id,v.content FROM user_voc_rev u left join vocabulary v on u.voc_id = v.id where u.user_id =? and u.is_deleted = 0'
    let param = [userId]
    if (nextReviewDate) {
      baseSql += ' and u.next_review_date <= ?'
      param.push(nextReviewDate)
    }
    console.info(baseSql)
    let resultSet = this.store?.querySqlSync(baseSql, param)
    let vocList: VocabularyReview[] = []
    if (resultSet) {
      while (resultSet.goToNextRow()) {
        let vocRevId = resultSet.getString(resultSet.getColumnIndex('vocRevId'))
        let vocId = resultSet.getString(resultSet.getColumnIndex('id'))
        let content = resultSet.getString(resultSet.getColumnIndex('content'))
        vocList.push({
          vocRevId: vocRevId,
          id: vocId,
          content: content
        } as VocabularyReview)
      }
    }
    return vocList
  }
  queryAllVocabulary(userId: string) {
    let resultSet = this.store?.querySqlSync('SELECT DISTINCT v.content FROM user_voc_rev u left join vocabulary v on u.voc_id = v.id where u.user_id = ? and u.is_deleted = 0', [userId])
    let vocList: string[] = []
    if (resultSet) {
      while (resultSet.goToNextRow()) {
        vocList.push(resultSet.getString(resultSet.getColumnIndex('content')))
      }
    }
    return vocList
  }
  private tableName: string = 'user_voc_rev'
  delete(vocId: string, userId: string): number {
    let predicates = new relationalStore.RdbPredicates(this.tableName)
    predicates.equalTo('voc_id',vocId)
      .and()
      .equalTo('user_id',userId)
    return this.store!.deleteSync(predicates)
  }
  update(userVocRev: UserVocRev) {
    // 更新user_voc_rev
    if (this.store) {
      let valueBucket = this.transfer(userVocRev);

      let predicates = new relationalStore.RdbPredicates('user_voc_rev')
      predicates.equalTo('id',userVocRev.id)
        .and()
        .equalTo('user_id',userVocRev.user_id)
        .and()
        .equalTo('is_deleted',userVocRev.is_deleted)
      try {
        this.store.updateSync(valueBucket, predicates)
      } catch (e) {
        console.info('update err' + e)
      }
    }
    // 更新user_rev_log
    // 更新user_rev_statistics
  }
  fetchRecitingVocabularyIdArray(uid:string):string[] {
    let predicates = new relationalStore.RdbPredicates('user_voc_rev');
    let date = new Date()
    predicates.equalTo('user_id',uid)
      .and()
      .lessThanOrEqualTo('next_review_date',formatDate(date))

    let array:string[] = []
    if (this.store){
      let resultSet = this.store.querySync(predicates,['voc_id'])
      while (resultSet.goToNextRow()){
        let voc_id = resultSet.getString(resultSet.getColumnIndex('voc_id'))
        array.push(voc_id)
      }

    }
    return array;
  }
  // 定义rdbStore，用于数据库存储，类型为RdbStore
  private store?: relationalStore.RdbStore

  setStore(store: relationalStore.RdbStore) {
    this.store = store
  }
  constructor(store:relationalStore.RdbStore) {
    this.store = store
  }

  createVocabularyReview(idList: string[],userId:string): void {
    let date = new Date()
    //let dateStr = `${date.getFullYear()}-${date.getMonth()}-${date.getDay()}`
    let dateStr = formatDate(date)
    //let dateTimeStr = `${dateStr} ${date.getHours()}:${date.getMinutes()}:${date.getSeconds()}`
    let dateTimeStr = formatDateTime(date)
    if (this.store) {
      let array:Array<UserVocRev> = []
      idList.forEach(id=>{
        let userVocRev: UserVocRev = {
          id: undefined,
          voc_id:id,
          user_id: userId,
          factor: 1.4,
          review_interval: 1,
          next_review_date: dateStr,
          usn: -1,
          type: 0,
          create_time: dateTimeStr,
          updated_time: dateTimeStr,
          is_deleted: 0,
          familiarity: 0,
          consecutive_fail_count: 0,
          total_review: 0,
          current_count: 0
        };
        array.push(userVocRev)
      })
      let buckets = this.batchTransfer(array);
      let innerStore = this.store
      buckets.forEach((bucket)=>{

        try {
          innerStore.insertSync('user_voc_rev', bucket)
          console.info(`valueBucket:${JSON.stringify(bucket)}`)
        } catch (e) {
          console.error(e)
        }
      })

    }
  }

  transfer(userVocRev:UserVocRev):ValuesBucket{
    let result = {
      "id": userVocRev.id,
      "voc_id": userVocRev.voc_id?? null,
      "user_id": userVocRev.user_id,
      "factor": userVocRev.factor,
      "review_interval": userVocRev.review_interval,
      "next_review_date": userVocRev.next_review_date,
      "usn": userVocRev.usn,
      "type": userVocRev.type,
      "create_time": userVocRev.create_time,
      "updated_time": userVocRev.updated_time,
      "is_deleted": userVocRev.is_deleted,
      "familiarity": userVocRev.familiarity?? null,
      "consecutive_fail_count": userVocRev.consecutive_fail_count?? null,
      "total_review": userVocRev.total_review?? null,
      "current_count": userVocRev.current_count?? null
    } as ValuesBucket
    return result
  }
  find(userId:string,vocId:string):UserVocRev{
    const record:UserVocRev = {
      user_id: userId,
      voc_id:vocId,
      factor: 0,
      review_interval: 0,
      next_review_date: '',
      usn: 0,
      type: 0,
      create_time: '',
      updated_time: '',
      is_deleted: 0
    }
    if (this.store) {
      let predicates = new relationalStore.RdbPredicates('user_voc_rev');
      predicates.equalTo('voc_id',vocId)
        .and()
        .equalTo('user_id',userId)
        .and()
        .equalTo('is_deleted',0)
      let resultSet = this.store.querySync(predicates,['id','factor','review_interval','consecutive_fail_count','create_time','total_review','current_count'])
      while (resultSet.goToNextRow()){
        // 假设 record 是你的对象，resultSet 是 ResultSet 类型的对象
        record.id = resultSet.getString(resultSet.getColumnIndex("id"));
        record.factor = resultSet.getLong(resultSet.getColumnIndex("factor"));
        record.review_interval = resultSet.getLong(resultSet.getColumnIndex("review_interval"));
        record.create_time = resultSet.getString(resultSet.getColumnIndex('create_time'))
        record.consecutive_fail_count = resultSet.getLong(resultSet.getColumnIndex("consecutive_fail_count"));
        record.total_review = resultSet.getLong(resultSet.getColumnIndex("total_review"));
        record.current_count = resultSet.getLong(resultSet.getColumnIndex("current_count"));
      }
    }
    return record;
  }

  dataTransfer(userVocRev:UserVocRevData):ValuesBucket{
    let value = {
      "id": userVocRev.id,
      "voc_id": userVocRev.vocId,
      "user_id": userVocRev.userId,
      "factor": userVocRev.factor,
      "review_interval": userVocRev.reviewInterval,
      "next_review_date": userVocRev.nextReviewDate,
      "usn": userVocRev.usn,
      "type": userVocRev.type,
      "create_time": userVocRev.createTime,
      "updated_time": userVocRev.updatedTime,
      "is_deleted": userVocRev.isDeleted,
      "familiarity": userVocRev.familiarity,
      "consecutive_fail_count": userVocRev.consecutiveFailCount,
      "total_review": userVocRev.totalReview,
      "current_count": userVocRev.currentCount    } as ValuesBucket
      return value;

  }


  batchTransfer(userVocRevArray:Array<UserVocRev>):ValuesBucket[]{
    let result = userVocRevArray.map((userVocRev)=>{
      return {
        "id": userVocRev.id,
        "voc_id": userVocRev.voc_id?? null,
        "user_id": userVocRev.user_id,
        "factor": userVocRev.factor,
        "review_interval": userVocRev.review_interval,
        "next_review_date": userVocRev.next_review_date,
        "usn": userVocRev.usn,
        "type": userVocRev.type,
        "create_time": userVocRev.create_time,
        "updated_time": userVocRev.updated_time,
        "is_deleted": userVocRev.is_deleted,
        "familiarity": userVocRev.familiarity?? null,
        "consecutive_fail_count": userVocRev.consecutive_fail_count?? null,
        "total_review": userVocRev.total_review?? null,
        "current_count": userVocRev.current_count?? null
      } as ValuesBucket

    })
    return result
  }
}

// 定义user_voc_rev表对应的接口
export interface UserVocRev {
  id?: string;
  voc_id?: string;
  user_id: string;
  factor: number;
  review_interval: number;
  next_review_date: string;
  usn: number;
  type: number;
  create_time: string;
  updated_time: string;
  is_deleted: number;
  familiarity?: number;
  consecutive_fail_count?: number;
  total_review?: number;
  current_count?: number;
}
// 定义user_voc_rev表对应的接口
export interface UserVocRevData {
  id?: string;
  vocId?: string;
  userId: string;
  factor: number;
  reviewInterval: number;
  nextReviewDate: string;
  usn: number;
  type: number;
  createTime: string;
  updatedTime: string;
  isDeleted: number;
  familiarity?: number;
  consecutiveFailCount?: number;
  totalReview?: number;
  currentCount?: number;
}
export function formatDate(date: Date): string {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
}
export function formatDateTime(date: Date): string {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');
  const seconds = String(date.getSeconds()).padStart(2, '0');
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}




