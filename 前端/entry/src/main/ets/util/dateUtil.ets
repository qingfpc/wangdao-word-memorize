
export function parseDate(dateString: string): Date {
  console.info(dateString)
 // 将 08-35这样包含小时和分钟的字符串转化为Date
  const arr = dateString.split(':').map(item => parseInt(item));
  console.info(JSON.stringify(arr))
  const date = new Date();
  date.setHours(arr[0]);
  date.setMinutes(arr[1]);
  return date;
}