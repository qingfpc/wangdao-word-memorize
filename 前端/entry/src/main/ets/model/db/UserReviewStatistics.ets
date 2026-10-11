/**
 * {
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
 CREATE TABLE `user_rev_statistics` (
 `id` bigint NOT NULL COMMENT 'id',
 `user_id` bigint DEFAULT NULL COMMENT '用户id',
 `voc_new_count` int DEFAULT NULL COMMENT '复习的旧单词数量',
 `voc_review_count` int DEFAULT NULL COMMENT '复习的新单词数量',
 `total_count` int DEFAULT NULL COMMENT '复习的单词总数量',
 `forget_count` int DEFAULT NULL COMMENT '遗忘单词数量',
 `uncertain_count` int DEFAULT NULL COMMENT '模糊单词数量',
 `known_count` int DEFAULT NULL COMMENT '认识单词数量',
 `simple_count` int DEFAULT NULL COMMENT '简单单词数量',
 `review_date` date DEFAULT NULL COMMENT '复习日期',
 `study_time` bigint DEFAULT NULL COMMENT '学习时间ms为单位',
 `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
 `updated_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 `is_deleted` int DEFAULT '0' COMMENT '逻辑删除标志: 0未删除 1已删除',
 PRIMARY KEY (`id`) USING BTREE,
 UNIQUE KEY `idx_user_id_review_date` (`user_id`,`review_date`) USING BTREE,
 KEY `idx_user_id_updated_time` (`user_id`,`updated_time`) USING BTREE
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
 */
export interface UserReviewStatistics{
  id: string; //
  userId: string; // 用户id
  vocNewCount: number; // 复习的旧单词数量
  vocReviewCount: number; // 复习的新单词数量
  totalCount: number; // 复习的单词总数量
  forgetCount: number; // 遗忘单词数量
  uncertainCount: number; // 模糊单词数量
  knowCount: number; // 认识单词数量
  simpleCount: number; // 简单单词数量
  studyTime: string; // 学习时间ms为单位
  reviewDate: string; // 复习日期

}