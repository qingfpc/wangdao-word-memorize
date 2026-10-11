import { UserReviewStatistics } from "../../model/db/UserReviewStatistics";
import { BaseDao } from "./BaseDao";
import { ValuesBucket } from "@kit.ArkData";

export class ReviewStatisticsDao extends BaseDao{
  insert(userReviewStatistics: UserReviewStatistics) {
    let value = this.transferToValueBuckets(userReviewStatistics)
    let insert = this.getStore().insert(this.tableName, value)
    console.info('inserted:' + insert)
  }
  tableName = 'user_rev_statistics'


  insertBatch(arr:UserReviewStatistics[]){
    let values = arr.map<ValuesBucket>(item=>this.transferToValueBuckets(item))
    let insert = this.getStore().batchInsert(this.tableName,values)
    console.info('inserted:'+insert)
  }

  transferToValueBuckets(item: UserReviewStatistics): ValuesBucket {
    let value = {
      id: item.id,
      user_id: item.userId,
      voc_new_count: item.vocNewCount,
      voc_review_count: item.vocReviewCount,
      total_count: item.totalCount,
      forget_count: item.forgetCount,
      uncertain_count: item.uncertainCount,
      know_count: item.knowCount,
      simple_count: item.simpleCount,
      study_time: item.studyTime,
      review_date: item.reviewDate,
    } as ValuesBucket;
    return value;
  }
}