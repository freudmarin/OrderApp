import {Category} from "../category/category";

export class Product {
  id: number;
  productCode : String;
  productName : String;
  description : String;
  category : Category;
  unitPrice : number;
  unitInStock : number;
}
