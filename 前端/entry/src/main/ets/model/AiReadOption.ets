/**
 * {
 "id": "",
 "name": "",
 "status": 0,
 "description": "",
 "options": [
 {
 "id": "",
 "situationId": "",
 "name": "",
 "description": ""
 }
 ]
 }
 */
export interface AiReadSituation{
  id:string
  name:string
  status:number
  description:string
  options:AiReadOption[]
}

export interface AiReadOption{
  id:string
  situationId:string
  name:string
  description:string
}
