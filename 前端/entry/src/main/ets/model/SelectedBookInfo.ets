@Observed
export class SelectedBookInfo{
  selectedBookName:string = ''
  selectedBookId:string = ''
  memoryInfo:MemoryInfo = {
    familiar: 0,
    familiarPercent: 0,
    general: 0,
    generalPercent: 0,
    notStarted: 0,
    unSelected: 0,
    unFamiliar: 0,
    unFamiliarPercent: 0,
    total: 0,
  }
}
@Observed
export class MemoryInfo{
  familiar: number = 0
  familiarPercent: number = 0
  general: number = 0
  generalPercent: number = 0
  notStarted: number = 0
  unSelected: number = 0
  unFamiliar: number = 0
  unFamiliarPercent: number = 0
  total: number = 0
}