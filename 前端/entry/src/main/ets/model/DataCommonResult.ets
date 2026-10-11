export interface DataCommonResult<T>{
  data: T;
  message?: string;
  code: number;
}