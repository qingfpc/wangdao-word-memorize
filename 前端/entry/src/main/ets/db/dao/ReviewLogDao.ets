import { UserReviewStatistics } from "../../model/db/UserReviewStatistics";
import { BaseDao } from "./BaseDao";
import { ValuesBucket } from "@kit.ArkData";
import { UserReviewLog } from "../../model/db/UserReviewLog";

export class ReviewLogDao extends BaseDao{
  selectTodayFamiliarity(userId: string, dateStr: string): [number, number,number][] {
    let sql: string =
      'SELECT familiarity,count(*),sum(study_time) FROM user_rev_log where user_id = ? and date(create_time) = ? group by familiarity';
    let result = this.getStore().querySqlSync(sql, [userId, dateStr])
    if (!result) {
      return []
    }
    let arr: [number, number,number][] = []
    while (result.goToNextRow()) {
      let familiarity = result.getLong(0)
      let count = result.getLong(1)
      let studyTime = result.getLong(2)
      arr.push([familiarity, count,studyTime])
    }
    return arr
  }
  tableName = 'user_rev_log'


  insertBatch(arr:UserReviewLog[]){
    let values = arr.map<ValuesBucket>(item=>this.transferToValueBuckets(item))
    let insert = this.getStore().batchInsert(this.tableName,values)
    console.info('inserted:'+insert)
  }

  insert(item:UserReviewLog){
    try {
      let value = this.transferToValueBuckets(item)
      let insert = this.getStore().insert(this.tableName, value)
      console.info('inserted:' + insert)
    } catch (e) {
      console.info('insert err:' + JSON.stringify(e))
    }
  }

  /**
   * id: string
   userId: string
   vocId: string
   vocContent: string
   familiarity: number
   createTime: string
   isDeleted: number
   * @param item
   * @returns
   */
  transferToValueBuckets(item: UserReviewLog): ValuesBucket {
    let value = {
      id:item.id,
      user_id: item.userId,
      voc_id: item.vocId,
      voc_content: item.vocContent,
      study_time: item.studyTime,
      familiarity: item.familiarity,
      create_time: item.createTime,
      is_deleted: item.isDeleted
    } as ValuesBucket;
    return value;
  }
}