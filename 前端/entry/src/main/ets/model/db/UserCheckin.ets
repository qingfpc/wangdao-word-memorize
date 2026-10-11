
//CREATE TABLE `user_checkin` (
//   `id` INTEGER PRIMARY KEY,
//   `user_id` INTEGER,
//   `sign_date` DATE,
//   `stick_no` INTEGER,
//   `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
//   `updated_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
//   `is_deleted` INTEGER DEFAULT 0,
//   `type` INTEGER COMMENT '1. 正常签到 2.补签'
// );

export interface UserCheckin{
  id:string;
  userId:string;
  authId:string
  signDate:string;
  stickNo:string;
  createTime:string;
  updatedTime:string;
  isDeleted:number;
  type:number;
}