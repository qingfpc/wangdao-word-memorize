import { DaoConfig } from "../db/dao/DaoConfig";
import { ReviewLogDao } from "../db/dao/ReviewLogDao";
import { ReviewStatisticsDao } from "../db/dao/ReviewStatisticsDao";
import { UserReviewLog } from "../model/db/UserReviewLog";
import { UserReviewStatistics } from "../model/db/UserReviewStatistics";
import { GlobalContext } from "../util/GlobalContext";

export class ReviewStatisticsService {
  reviewStatisticsDao = GlobalContext.getContext().getObject(DaoConfig.REVIEW_STATISTICS_DAO) as ReviewStatisticsDao

  createReviewStatistics(userReviewStatistics:UserReviewStatistics){
    if (!this.reviewStatisticsDao) {
      this.reviewStatisticsDao = GlobalContext.getContext().getObject(DaoConfig.REVIEW_STATISTICS_DAO) as ReviewStatisticsDao
    }
    this.reviewStatisticsDao.insert(userReviewStatistics)
  }
}