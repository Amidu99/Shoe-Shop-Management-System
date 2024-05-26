export class Inventory{
    constructor(itemCode, itemDesc, itemPic, category, supplierCode, supplierName, buyPrice, sellPrice, profit, profitMargin) {
        this.itemCode = itemCode;
        this.itemDesc = itemDesc;
        this.itemPic = itemPic;
        this.category = category;
        this.supplierCode = supplierCode;
        this.supplierName = supplierName;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
        this.profit = profit;
        this.profitMargin = profitMargin;
    }
}