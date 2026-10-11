import { UserCheckin } from "../../model/db/UserCheckin";
import { BaseDao } from "./BaseDao";
import { ValuesBucket } from "@kit.ArkData";

export class CheckinDao extends BaseDao{
  tableName = 'user_checkin'
  insert(item:UserCheckin) {
    let value = this.transferToValueBuckets(item)
    let insert = this.getStore().insertSync(this.tableName,value)
    console.info('inserted:'+insert)
  }
  insertBatch(arr:UserCheckin[]){
    let values = arr.map<ValuesBucket>(item=>this.transferToValueBuckets(item))
    let insert = this.getStore().batchInsert(this.tableName,values)
    console.info('inserted:'+insert)
  }

  listByYearMonth(year:number,month:number,authid:string):UserCheckin[]{
    let monthStr = ''
    if (month < 10) {
      monthStr = `0${month}`
    }else {
      monthStr = `${month}`
    }
    let store = this.getStore()
    let result = store.querySqlSync(`SELECT * FROM ${this.tableName} WHERE sign_date LIKE '${year}-${monthStr}%' AND authid='${authid}'`)
    let checkins:UserCheckin[] = []
    while (result.goToNextRow()) {
      let checkin = {
        id:result.getString(result.getColumnIndex('id')),
        authId:result.getString(result.getColumnIndex('authid')),
        userId:result.getString(result.getColumnIndex('user_id')),
        signDate:result.getString(result.getColumnIndex('sign_date')),
        stickNo:result.getString(result.getColumnIndex('stick_no')),
        createTime:result.getString(result.getColumnIndex('create_time')),
        updatedTime:result.getString(result.getColumnIndex('updated_time')),
        isDeleted:result.getLong(result.getColumnIndex('is_deleted')),
        type:result.getLong(result.getColumnIndex('type'))
      } as UserCheckin
      checkins.push(checkin)
    }
    return checkins
  }

  /**'
   * export interface UserCheckin{
   id:number;
   userId:number;
   signDate:string;
   stickNo:number;
   createTime:string;
   updatedTime:string;
   isDeleted:number;
   type:number;
   }
   */
  transferToValueBuckets(userCheckin:UserCheckin):ValuesBucket{
    let value:ValuesBucket = {
      'user_id':userCheckin.userId,
      'authid':userCheckin.authId,
      'sign_date':userCheckin.signDate,
      'stick_no':userCheckin.stickNo,
      'create_time':userCheckin.createTime,
      'updated_time':userCheckin.updatedTime,
      'is_deleted':userCheckin.isDeleted,
      'type':userCheckin.type
    } as ValuesBucket
		return value;
	}
}