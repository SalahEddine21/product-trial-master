import { Routes } from "@angular/router";
import { HomeComponent } from "./shared/features/home/home.component";
import { LoginComponent } from "./security/login/login.component";
import { authGuard } from "./security/guards/auth.guard";
import { CartComponent } from "./cart/features/cart/cart.component";
import { ContactComponent } from "./contact/features/contact/contact.component";

export const APP_ROUTES: Routes = [
  {
    path: "login",
    component: LoginComponent,
  },
  {
    path: "home",
    component: HomeComponent,
    canActivate: [authGuard]
  },
  { path: 'cart', 
    component: CartComponent,
    canActivate: [authGuard]
  },
  { path: 'contact', 
    component: ContactComponent,
    canActivate: [authGuard]
  },
  {
    path: "products",
    loadChildren: () =>
      import("./products/products.routes").then((m) => m.PRODUCTS_ROUTES)
  },
  { path: "", redirectTo: "map", pathMatch: "full" },
];
