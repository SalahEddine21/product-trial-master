import { Component, OnInit, inject, signal } from "@angular/core";
import { CartService } from "app/cart/data-access/cart.service";
import { PaginatedData } from "app/models/paginated-data";
import { Product } from "app/products/data-access/product.model";
import { ProductsService } from "app/products/data-access/products.service";
import { ProductFormComponent } from "app/products/ui/product-form/product-form.component";
import { TAB_ROWS_PER_PAGE } from "app/shared/utils/constant";
import { MessageService } from "primeng/api";
import { ButtonModule } from "primeng/button";
import { CardModule } from "primeng/card";
import { DataViewModule } from 'primeng/dataview';
import { DialogModule } from 'primeng/dialog';
import { PaginatorModule } from "primeng/paginator";
import { ToastModule } from 'primeng/toast';

const emptyProduct: Product = {
  id: 0,
  code: "",
  name: "",
  description: "",
  image: "",
  category: "",
  price: 0,
  quantity: 0,
  internalReference: "",
  shellId: 0,
  inventoryStatus: "INSTOCK",
  rating: 0,
  createdAt: 0,
  updatedAt: 0,
};

@Component({
  selector: "app-product-list",
  templateUrl: "./product-list.component.html",
  styleUrls: ["./product-list.component.scss"],
  standalone: true,
  imports: [DataViewModule, CardModule, ButtonModule, DialogModule, ProductFormComponent, ToastModule, PaginatorModule],
})
export class ProductListComponent implements OnInit {
  private readonly productsService = inject(ProductsService);
  private readonly messageService = inject(MessageService);
  private readonly cartService = inject(CartService);

  public readonly products = signal<Product[]>([]);

  public isDialogVisible = false;
  public isCreation = false;
  public readonly editedProduct = signal<Product>(emptyProduct);

  public totalRecords = 0;
  public pageSize = 10;
  public currentPage = 0;
  public rows_per_page = TAB_ROWS_PER_PAGE;

  ngOnInit() {
    this.loadProducts(this.currentPage, this.pageSize);
  }

  public loadProducts(page: number, size: number) {
    this.productsService.get(page, size).subscribe((response: PaginatedData<Product>) => {
      this.products.set(response.content);
      this.totalRecords = response.totalElements;
      this.pageSize = response.size;
      this.currentPage = response.number;
    });
  }

  public onCreate() {
    this.isCreation = true;
    this.isDialogVisible = true;
    this.editedProduct.set(emptyProduct);
  }

  public onUpdate(product: Product) {
    this.isCreation = false;
    this.isDialogVisible = true;
    this.editedProduct.set(product);
  }

  public onDelete(product: Product) {
    this.productsService.delete(product.id).subscribe();
  }

  public onSave(product: Product) {
    if (this.isCreation) {
      this.productsService.create(product).subscribe(() => {
        this.loadProducts(this.currentPage, this.pageSize);
        this.messageService.add({
          severity: 'success',
          summary: 'Product Created',
          detail: `${product.name} created successfully.`,
        });
      });
    } else {
      this.productsService.update(product).subscribe(() => {
        this.loadProducts(this.currentPage, this.pageSize);
        this.messageService.add({
          severity: 'success',
          summary: 'Product Updated',
          detail: `${product.name} updated successfully.`,
        });
      });
    }
    this.closeDialog();
  }

  public onCancel() {
    this.closeDialog();
  }

  private closeDialog() {
    this.isDialogVisible = false;
  }

  public onPageChange(event: any) {
    this.loadProducts(event.page, event.rows);
  }
  
  addToCart(product: Product): void {
    this.cartService.addProduct(product);
    this.messageService.add({
      severity: 'success',
      summary: 'Product Added',
      detail: `${product.name} added to Cart.`,
    });
  }
}
