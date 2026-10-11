
/*{
  "vocId": "",
"vocContent": "",
"selected": 0,
"interpretations": [
  {
    "id": "",
  "type": "",
  "content": ""
  }
  ]
}*/
export interface CollectVocabulary{
  vocId:string
  vocContent:string
  selected:number
  interpretations:Interpretation[]

}
export interface Interpretation{
  id:string
  type:string
  content:string
}