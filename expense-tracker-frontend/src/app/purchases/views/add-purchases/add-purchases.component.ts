import { Component, Inject, LOCALE_ID, OnInit } from '@angular/core';
import { ProductDto, PurchaseDto, PurchasesService, ShopsService } from 'build/expense-tracker-frontend-api';
import { MatDialog } from "@angular/material/dialog";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { MatSnackBar } from "@angular/material/snack-bar";
import { formatCurrency, formatDate, getCurrencySymbol } from "@angular/common";
import { filter, map, mergeMap } from 'rxjs';
import { ConfirmationDialog } from "@shared/components/confirmation-dialog/confirmation-dialog.component";
import { v4 as randomUUID } from 'uuid';
import { ActivatedRoute } from "@angular/router";
import { isAnyPropertyNonNull } from "@shared/miscellaneous/functions";

const numbro = require('numbro')
const plPL = require('numbro/dist/languages/pl-PL.min')
numbro.registerLanguage(plPL)
numbro.setLanguage('pl-PL')

@Component({
  selector: 'app-add-purchases',
  templateUrl: './add-purchases.component.html',
  styleUrls: ['./add-purchases.component.scss']
})
export class AddPurchasesComponent implements OnInit {

  purchasesForm: FormGroup;

  constructor(
    @Inject(LOCALE_ID) private locale: string,
    private shopsService: ShopsService,
    private purchasesService: PurchasesService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar,
    private fb: FormBuilder,
    private route: ActivatedRoute
  ) {
    this.purchasesForm = fb.group({
      shop: fb.control(null, Validators.required),
      date: fb.control(new Date(), Validators.required),
      products: fb.control(this.initEmptyProductGroups(10), Validators.required)
    })
  }

  ngOnInit() {
    this.route.params.pipe(
      map(({id}) => id),
      filter(id => id != null),
      mergeMap(id => this.purchasesService.getPurchase(id))
    ).subscribe(purchase => this.loadPurchaseData(purchase))
  }

  private loadPurchaseData(purchase: PurchaseDto): void {
    this.purchasesForm.reset({
      shop: purchase.shop,
      date: purchase.date,
      products: purchase.products
    })
  }

  clearForm(): void {
    this.purchasesForm.reset({
      shop: null,
      date: new Date(),
      products: this.initEmptyProductGroups(10)
    })
    this.purchasesForm.markAsPristine();
  }

  onPurchasesSave(): void {
    if (this.purchasesForm.invalid) return;

    const purchase: PurchaseDto = this.mapPurchaseDto();
    this.openConfirmationDialogForSavingPurchase(purchase, () => this.saveNewPurchase(purchase));
  }

  private mapPurchaseDto(): PurchaseDto {
    return {
      id: randomUUID(),
      shop: this.purchasesForm.controls['shop'].value,
      date: this.purchasesForm.controls['date'].value,
      products: this.purchasesForm.controls['products'].value
        .filter(isAnyPropertyNonNull)
        .map((row: any) => ({
          id: randomUUID(),
          category: row.category,
          name: row.name,
          amount: row.amount,
          price: row.price,
          description: row.description
        }))
    };
  }

  private openConfirmationDialogForSavingPurchase(purchase: PurchaseDto, approvedCallback: Function): void {
    this.dialog
      .open(ConfirmationDialog, {
        data: {
          confirmationTitle: "Confirm purchase save",
          confirmationDescription: `
            <p>Purchase shop: ${purchase.shop.name}</p>
            <p>Purchase date: ${formatDate(purchase.date, "dd.MM.yyyy", this.locale)}</p>
            <p>Purchases count: ${purchase.products.length}</p>
            <p>Total price: ${this.calculateAndFormatTotalPrice(purchase.products)}</p>
          `,
          confirmBtnText: "Save",
          rejectBtnText: "Cancel"
        }
      })
      .afterClosed()
      .pipe(filter(approved => approved))
      .subscribe(() => approvedCallback());
  }

  private calculateAndFormatTotalPrice(products: ProductDto[]): string {
    const totalPrice = products
      .map(p => p.amount * p.price)
      .reduce((prevVal, val) => prevVal + val, 0);

    return formatCurrency(totalPrice, this.locale, getCurrencySymbol("PLN", "wide", this.locale), 'PLN', '1.2-2')
  }

  private saveNewPurchase(purchase: PurchaseDto) {
    this.purchasesService.addPurchase(purchase).subscribe(
      () => this.snackBar.open("Purchase saved!", "dismiss", {duration: 3000}),
      () => this.snackBar.open("Failed to save purchase!", "dismiss", {duration: 3000})
    );
  }

  private initEmptyProductGroups(length: number): any[] {
    return Array.from({length}, () => ({
      "category": null,
      "name": null,
      "amount": null,
      "price": null,
      "description": null,
    }));
  }
}

