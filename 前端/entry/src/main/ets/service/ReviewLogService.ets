import { DaoConfig } from "../db/dao/DaoConfig";
import { ReviewLogDao } from "../db/dao/ReviewLogDao";
import { formatDate } from "../db/dao/VocabularyDao";
import { UserReviewLog } from "../model/db/UserReviewLog";
import { GlobalContext } from "../util/GlobalContext";

export class ReviewLogService {
  getTodayFamiliarity(userId: string): [number, number,number][] {
    if (!this.reviewLogDao) {
      this.reviewLogDao = GlobalContext.getContext().getObject(DaoConfig.REVIEW_LOG_DAO) as ReviewLogDao
    }
    let dateStr = formatDate(new Date())
    let arr:[number,number,number][] = this.reviewLogDao.selectTodayFamiliarity(userId,dateStr)
    return arr
  }
  reviewLogDao = GlobalContext.getContext().getObject(DaoConfig.REVIEW_LOG_DAO) as ReviewLogDao

  createReviewLog(userReviewLog:UserReviewLog){
    if (!this.reviewLogDao) {
      this.reviewLogDao = GlobalContext.getContext().getObject(DaoConfig.REVIEW_LOG_DAO) as ReviewLogDao
    }
    this.reviewLogDao.insert(userReviewLog)
  }
}