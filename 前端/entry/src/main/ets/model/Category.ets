export interface  Category{
  id:string;
  name:string;
  items?:Array<CategoryItem>

  /*constructor(id:number,name:string) {
    this.id = id
    this.name = name
  }*/
}

export interface CategoryItem{
  id:string;
  name:string
}