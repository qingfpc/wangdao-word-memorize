import { UserVocRev, UserVocRevData } from "../db/dao/VocabularyDao";
import { UserReviewStatistics } from "./db/UserReviewStatistics";
import { PageDataModel } from "./PageDataModel";

/**
 * {
 "userVocRevPage": {
 "pageList": [
 {
 "id": "",
 "vocId": "",
 "userId": "",
 "factor": 0,
 "reviewInterval": 0,
 "nextReviewDate": "",
 "type": 0,
 "familiarity": 0,
 "consecutiveFailCount": 0,
 "totalReview": 0
 }
 ],
 "totalCount": 0
 },
 "userReviewStatisticsPage": {
 "pageList": [
 {
 "id": "",
 "userId": "",
 "vocNewCount": 0,
 "vocReviewCount": 0,
 "totalCount": 0,
 "forgetCount": 0,
 "uncertainCount": 0,
 "knowCount": 0,
 "simpleCount": 0,
 "studyTime": "",
 "reviewDate": ""
 }
 ],
 "totalCount": 0
 }
 }
 */
export interface ReviewData {
  userVocRevPage: PageDataModel<UserVocRevData>;
  userReviewStatisticsPage: PageDataModel<UserReviewStatistics>;
}