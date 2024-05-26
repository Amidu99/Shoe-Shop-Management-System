export class Stock {
    constructor(stockCode, size, originalQty, availableQty, status, itemCode, supplierCode) {
            this.stockCode = stockCode;
            this.size = size;
            this.originalQty = originalQty;
            this.availableQty = availableQty;
            this.status = status;
            this.itemCode = itemCode;
            this.supplierCode = supplierCode;
    }
}