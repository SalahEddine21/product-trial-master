import {
  Component,
  inject,
} from "@angular/core";
import { Router, RouterModule } from "@angular/router";
import { SplitterModule } from 'primeng/splitter';
import { ToolbarModule } from 'primeng/toolbar';
import { PanelMenuComponent } from "./shared/ui/panel-menu/panel-menu.component";
import { NgIf } from "@angular/common";
import { CartService } from "./cart/data-access/cart.service";

@Component({
  selector: "app-root",
  templateUrl: "./app.component.html",
  styleUrls: ["./app.component.scss"],
  standalone: true,
  imports: [RouterModule, SplitterModule, ToolbarModule, PanelMenuComponent, NgIf],
})
export class AppComponent {
  title = "ALTEN SHOP";
  private router = inject(Router);
  public readonly cartService = inject(CartService);

  isLoginRoute(): boolean {
    return this.router.url === '/login';
  }
}
