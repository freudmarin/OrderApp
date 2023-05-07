import {Category} from '../category/category';

export class Product {
  id: number;
  productCode: string;
  productName: string;
  description: string;
  category: Category;
  unitPrice: number;
  unitInStock: number;
  discount: number;
}
