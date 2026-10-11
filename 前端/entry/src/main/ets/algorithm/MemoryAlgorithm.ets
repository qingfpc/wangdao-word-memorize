import { formatDateTime,formatDate, UserVocRev } from "../db/dao/VocabularyDao";

export class MemoryAlgorithm{
  private initFactor:number = 1.4;

  constructor() {

  }

  newInterval(userVocRev: UserVocRev, q: number): UserVocRev {
    let newFactor: number;
    let newInterval: number = 1;
    userVocRev.current_count = userVocRev.current_count?userVocRev.current_count + 1:1
    // 当 q 为 0,且失败3次 时，重置相关参数
    if (q === 0 && userVocRev.consecutive_fail_count ===2) {
      userVocRev.factor = this.initFactor;
      userVocRev.review_interval = 1;
      userVocRev.current_count = 0
      userVocRev.total_review = userVocRev.total_review?userVocRev.total_review + 1:1
      // todo:失败次数更新么
      userVocRev.consecutive_fail_count = userVocRev.consecutive_fail_count?userVocRev.consecutive_fail_count + 1:1
      return userVocRev
    }
    // 计算新的复习因子 newFactor
    if (userVocRev.consecutive_fail_count === 3) {
      newFactor = this.initFactor;
    } else {
      //newFactor = userVocRev.factor + (0.1 - (3 - q) * (0.08 + (3 - q) * 0.02));
      // 修改间隔计算方式
      newFactor = userVocRev.factor + (0.1 - (5 - q) * 0.05);
    }
    if (!userVocRev.current_count)
      userVocRev.current_count = 0
    // 计算新的复习间隔 newInterval
    if (userVocRev.current_count === 0) {
      newInterval = 1;
      userVocRev.consecutive_fail_count = userVocRev.consecutive_fail_count?userVocRev.consecutive_fail_count + 1:1
    } else if (userVocRev.current_count === 1) {
      newInterval = 1;
    } else if (userVocRev.current_count > 1) {
      newInterval = userVocRev.review_interval * newFactor;
    } else if (userVocRev.consecutive_fail_count === 3) {
      newInterval = 1;
    }

    userVocRev.total_review = userVocRev.total_review?userVocRev.total_review + 1:1
    userVocRev.factor = newFactor
    userVocRev.review_interval = newInterval

    userVocRev.updated_time = formatDateTime(new Date())
    let date = new Date()
    console.info(`date:${date.getDate()}`)
    date.setDate(date.getDate()+1)
    console.info(`date:${date.getDate()}`)
    userVocRev.next_review_date = formatDate(date)

    return userVocRev
  }
}

export const memoryAlgorithm:MemoryAlgorithm = new MemoryAlgorithm();