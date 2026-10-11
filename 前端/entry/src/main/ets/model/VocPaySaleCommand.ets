/**
 * {
 "id": "",
 "userId": "",
 "itemId": "",
 "itemName": "",
 "itemType": 0,
 "payPrice": 0,
 "exchangeVocCount": 0,
 "status": 0,
 "isSignCard": true
 }
 */
export interface VocPaySaleCommand{
  id: string,
  itemId: string,
  payPrice: number,
  exchangeVocCount: number,
  isSignCard: boolean,
  itemName: string,
  itemType: number,
  userId: string,
  status: number
}