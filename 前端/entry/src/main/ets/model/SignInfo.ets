export class SignInfo {
  curContinuousDays: number = 0;
  curContinuousStartDate: string = "";
  id: string = '';
  maxContinuousDays: number = 0;
  maxContinuousStartDate: string = "";
  totalDays: number = 0;
  totalTime: number = 0;
  userId: string = '';
  usn: number = 0;

  constructor(
    curContinuousDays: number = 0,
    curContinuousStartDate: string = "",
    id: string = '',
    maxContinuousDays: number = 0,
    maxContinuousStartDate: string = "",
    totalDays: number = 0,
    totalTime: number = 0,
    userId: string = '',
    usn: number = 0
  ) {
    this.curContinuousDays = curContinuousDays;
    this.curContinuousStartDate = curContinuousStartDate;
    this.id = id;
    this.maxContinuousDays = maxContinuousDays;
    this.maxContinuousStartDate = maxContinuousStartDate;
    this.totalDays = totalDays;
    this.totalTime = totalTime;
    this.userId = userId;
    this.usn = usn;
  }
}