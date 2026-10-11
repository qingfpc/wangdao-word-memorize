export interface SignResultDescription {
  color: ResourceStr
  title: string
}

// 成绩标识列表 ScoreDayData
export const descriptions: SignResultDescription[] = [
  { color: $r('app.color.color_score_a'), title: '已签到' },
  { color: $r('app.color.color_score_b'), title: '已补签' },
  { color: $r('app.color.color_score_c'), title: '未签到' }
];