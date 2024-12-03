import { Product } from "app/products/data-access/product.model";

export interface Cart {
    id : string;
    product: Product;
}