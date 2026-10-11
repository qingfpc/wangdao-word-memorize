
/**
 * CREATE TABLE "user_rev_log" (
 "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
 "user_id" INTEGER,
 "voc_id" INTEGER,
 "voc_content" TEXT,
 "familiarity" integer,
 "create_time" TEXT DEFAULT (datetime('now', 'localtime')),
 "is_deleted" INTEGER DEFAULT 0
 );
 */
export interface UserReviewLog {

  id: string
  userId: string
  vocId: string
  vocContent: string
  familiarity: number
  studyTime: number
  createTime: string
  isDeleted: number
}