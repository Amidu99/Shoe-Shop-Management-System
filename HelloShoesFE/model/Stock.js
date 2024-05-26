export class Stock {
    constructor(supplierCode, size, originalQty, availableQty, status, itemCode, stockCode) {
            this.stockCode = stockCode;
            this.size = size;
            this.originalQty = originalQty;
            this.availableQty = availableQty;
            this.status = status;
            this.itemCode = itemCode;
            this.supplierCode = supplierCode;
    }
}