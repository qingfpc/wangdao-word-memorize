import { GlobalContext } from "../../util/GlobalContext"
import { getRdbStore } from "../rdb/RdbStoreConfig"
import { VocabularyDao } from "./VocabularyDao"
import { relationalStore } from "@kit.ArkData"
import { DaoConfig } from "./DaoConfig"
import { CategoryDao } from "./CategoryDao"
import { BookDao } from "./BookDao"
import { CheckinDao } from "./CheckinDao"
import { ReviewStatisticsDao } from "./ReviewStatisticsDao"
import { ReviewLogDao } from "./ReviewLogDao"

export async  function initDao(){
  let rdbstore = await getRdbStore()
  let vocabularyDao:VocabularyDao = new VocabularyDao(rdbstore)
  GlobalContext.getContext().setObject(DaoConfig.VOCABULARY_DAO,vocabularyDao)
}
export function initDao2(rdbstore:relationalStore.RdbStore){
  let vocabularyDao:VocabularyDao = new VocabularyDao(rdbstore)
  let categoryDao:CategoryDao = new CategoryDao(rdbstore)
  let bookDao = new BookDao(rdbstore)
  let checkinDao = new CheckinDao(rdbstore)
  let reviewStatisticsDao = new ReviewStatisticsDao(rdbstore)
  let reviewLogDao = new ReviewLogDao(rdbstore)

  GlobalContext.getContext().setObject(DaoConfig.VOCABULARY_DAO,vocabularyDao)
  GlobalContext.getContext().setObject(DaoConfig.CATEGORY_DAO,categoryDao)
  GlobalContext.getContext().setObject(DaoConfig.BOOK_DAO,bookDao)
  GlobalContext.getContext().setObject(DaoConfig.CHECKIN_DAO,checkinDao)
  GlobalContext.getContext().setObject(DaoConfig.REVIEW_STATISTICS_DAO,reviewStatisticsDao)
  GlobalContext.getContext().setObject(DaoConfig.REVIEW_LOG_DAO,reviewLogDao)
}