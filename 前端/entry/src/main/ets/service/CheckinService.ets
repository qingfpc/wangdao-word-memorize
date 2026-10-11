import axiosInstance from "../api/axios";
import { userCheckinList } from "../api/checkin";
import { CheckinItem } from "../api/data/CheckinItem";
import { CheckinDao } from "../db/dao/CheckinDao";
import { DaoConfig } from "../db/dao/DaoConfig";
import { formatDate } from "../db/dao/VocabularyDao";
import { getRdbStore } from "../db/rdb/RdbStoreConfig";
import { UserCheckin } from "../model/db/UserCheckin";
import { PageDataModel } from "../model/PageDataModel";
import { GlobalContext } from "../util/GlobalContext";

export class CheckinService{
  checkinDao = GlobalContext.getContext().getObject(DaoConfig.CHECKIN_DAO) as CheckinDao

  checkin(authid: string, userId: string, type: number) {
    let data = {
      userId: userId,
      authId: authid,
      type: type,
      signDate: formatDate(new Date()),
      createTime: formatDate(new Date()),
      updatedTime: formatDate(new Date()),
      isDeleted: 0,
    } as UserCheckin
   this.checkinDao.insert(data)
  }

  syncCheckinData(){

  }
  async saveAllCheckinData(authId:string) {
    let result = await userCheckinList()
    let data = result.data as PageDataModel<CheckinItem>
    console.info('获得checkin数据：' + JSON.stringify(data))
    let checkinData = data.pageList
    let date = formatDate(new Date())
    let values = checkinData.map<UserCheckin>(item => {
      let userCheckin = {
        id:item.id,
        userId:item.userId,
        authId:authId,
        signDate:item.signDate,
        type:item.type,
        createTime:date,
        updatedTime:date,
      } as UserCheckin
      return userCheckin
    })

   await this.checkinDao.insertBatch(values)
  }
  queryCurrentPageData(year:number,month:number,authid:string){
    return this.checkinDao.listByYearMonth(year,month,authid)
  }
}