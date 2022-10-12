import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { PurchaseDto, PurchasesService } from 'build/expense-tracker-frontend-api';
import { PurchasesDataSource } from "../../services/purchases.data-source";
import { MatPaginator } from "@angular/material/paginator";
import { first, map, merge, Observable } from "rxjs";
import { MatSort, SortDirection } from "@angular/material/sort";
import { SelectionModel } from "@angular/cdk/collections";

@Component({
  selector: 'app-purchases-display-table',
  templateUrl: './purchases-display-table.component.html',
  styleUrls: ['./purchases-display-table.component.scss']
})
export class PurchasesDisplayTableComponent implements OnInit, AfterViewInit {

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  dataSource: PurchasesDataSource;
  displayedColumns = ['select', 'index', 'name', 'date', 'products', 'price'];

  initialSortColumn = 'date';
  initialSortDirection: SortDirection = 'desc';

  purchasesLength: Observable<number>;

  selection = new SelectionModel<PurchaseDto>(true, []);
  allSelected = false;

  constructor(private purchasesService: PurchasesService) {
  }

  ngOnInit(): void {
    this.dataSource = new PurchasesDataSource(this.purchasesService)
    this.dataSource.loadPurchases(this.initialSortColumn, this.initialSortDirection)
    this.purchasesLength = this.dataSource.connect().pipe(map(purchases => purchases.length))
  }

  ngAfterViewInit() {
    this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);
    merge(this.sort.sortChange, this.paginator.page).subscribe(() => this.loadPurchasesPage());
  }

  loadPurchasesPage() {
    this.dataSource.loadPurchases(this.sort.active, this.sort.direction, this.paginator.pageIndex, this.paginator.pageSize);
    this.clearSelection();
  }

  selectOrDeselectAll() {
    this.isAllSelected() ? this.clearSelection() : this.dataSource.connect().pipe(first()).subscribe(purchases => this.selection.select(...purchases));
  }

  isAllSelected() {
    const purchasesSelected = this.selection.selected.length;
    const purchasesOnPage = (this.paginator.length - (this.paginator.pageSize * this.paginator.pageIndex)) % (this.paginator.pageSize + 1);
    return purchasesSelected === purchasesOnPage;
  }

  clearSelection() {
    this.selection.clear()
  }
}
