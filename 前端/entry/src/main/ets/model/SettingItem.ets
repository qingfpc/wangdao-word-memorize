@Observed
export class SettingItem{
  title:string = ''
  img?:Resource
  info:string|number = 0
  tmp?:string|number = 0
  userId?:string = ''
  saved:boolean = false
  onClick?:((item:SettingItem)=>Promise<boolean>)|(()=>void)
  others?: Resource;
  //node?:ComponentContent<DialogInfo>
}