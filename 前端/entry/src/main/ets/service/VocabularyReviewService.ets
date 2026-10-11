import { reviewData } from "../api/review";
import { DaoConfig } from "../db/dao/DaoConfig";
import { ReviewStatisticsDao } from "../db/dao/ReviewStatisticsDao";
import { formatDate, UserVocRevData, VocabularyDao } from "../db/dao/VocabularyDao";
import { getRdbStore } from "../db/rdb/RdbStoreConfig";
import { VocabularyReview } from "../model/ChapterVocabulary";
import { ReviewData } from "../model/ReviewData";
import { GlobalContext } from "../util/GlobalContext";

export class VocabularyReviewService {
  vocabularyDao = GlobalContext.getContext().getObject(DaoConfig.VOCABULARY_DAO) as VocabularyDao
  reviewStatisticsDao = GlobalContext.getContext().getObject(DaoConfig.REVIEW_STATISTICS_DAO) as ReviewStatisticsDao

  remove(vocId: string, userId: string): number {
    console.info(`vocId:${vocId} userId:${userId}`)
    return this.vocabularyDao.delete(vocId, userId)
  }

  getAll(userId: string): string[] {
    let vocList = this.vocabularyDao.queryAllVocabulary(userId)
    return vocList
  }

  queryTodayReviewVocabulary(userId: string): UserVocRevData[] {
    let vocList = this.vocabularyDao.selectDataByUserIdAndDate(userId, formatDate(new Date()))
    return vocList
  }

  queryReviewVocabulary(userId: string, nextReviewDate?: string): VocabularyReview[] {
    let vocList = this.vocabularyDao.selectByUserIdAndDate(userId, nextReviewDate)
    return vocList
  }

  async saveAllReviewData(authId: string) {
    if (!this.vocabularyDao) {
      this.vocabularyDao = GlobalContext.getContext().getObject(DaoConfig.VOCABULARY_DAO) as VocabularyDao
    }
    if (!this.reviewStatisticsDao) {
      this.reviewStatisticsDao = GlobalContext.getContext().getObject(DaoConfig.REVIEW_STATISTICS_DAO) as ReviewStatisticsDao
    }
    let result = await reviewData()
    let data = result.data as ReviewData
    console.info('获得复习数据：' + JSON.stringify(data))
    if (data.userReviewStatisticsPage) {

      let userReviewStatisticsData = data.userReviewStatisticsPage.pageList
      userReviewStatisticsData.forEach(item => item.userId = authId)
      this.reviewStatisticsDao.insertBatch(userReviewStatisticsData)
      console.info('user_rev_statistics插入完成')
    }
    if (data.userVocRevPage) {

      let userVocRevData = data.userVocRevPage.pageList
      userVocRevData.forEach(item => {
        item.userId = authId
        item.id = undefined
      })
      this.vocabularyDao.insertBatch(userVocRevData)
      console.info('user_voc_rev插入完成')
    }
  }
}