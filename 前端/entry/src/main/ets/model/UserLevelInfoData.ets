
/**
 * {
 "nowLevel": [
 {
 "id": "",
 "levelName": "",
 "levelValue": 0,
 "checkInDays": 0,
 "checkInType": 0,
 "unlockPrice": 0,
 "description": "",
 "privileges": [
 {
 "id": "",
 "privilegeCode": "",
 "privilegeName": "",
 "useType": 0,
 "requireVocBound": 0,
 "freeCount": 0,
 "exchangeType": 0,
 "levelValue": 0,
 "description": ""
 }
 ]
 }
 ],
 "upperLevel": [
 {}
 ]
 }
 */
export interface LevelInfo {
  nowLevel: LevelItem[];
  upperLevel: LevelItem[];
}
export interface LevelItem {
  id: string;
  levelName: string;
  levelValue: number;
  checkInDays: number;
  checkInType: number;
  unlockPrice: number;
  goodsId: string;
  description: string;
  levelPrivileges: Privilege[];
}
export interface Privilege {
  id: string;
  privilegeCode: string;
  privilegeName: string;
  useType: number;
  requireVocBound: number;
  freeCount: number;
  exchangeType: number;
  levelValue: number;
  description: string;
}