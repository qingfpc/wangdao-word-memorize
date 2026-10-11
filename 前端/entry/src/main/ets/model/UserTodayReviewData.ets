import { UserVocRev, UserVocRevData } from "../db/dao/VocabularyDao";
import { UserReviewStatistics } from "./db/UserReviewStatistics";

export interface UserTodayReviewData {
  revStatisticsCommand:UserReviewStatistics
  vocRevCommands:UserVocRevData[]
}