import { inject } from "@angular/core";
import { ActivatedRouteSnapshot, Routes } from "@angular/router";
import { ProductListComponent } from "./features/product-list/product-list.component";
import { authGuard } from "app/security/guards/auth.guard";

export const PRODUCTS_ROUTES: Routes = [
	{
		path: "list",
		component: ProductListComponent,
		canActivate: [authGuard]
	},
	{ path: "**", redirectTo: "list" },
];
