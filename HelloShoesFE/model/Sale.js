export class Sale {
    constructor(orderCode, customerName, totalPrice, date, payMethod, addedPoints, cashierName, userCode, customerCode, saleInventories = []) {
        this.orderCode = orderCode;
        this.customerName = customerName;
        this.totalPrice = totalPrice;
        this.date = date;
        this.payMethod = payMethod;
        this.addedPoints = addedPoints;
        this.cashierName = cashierName;
        this.userCode = userCode;
        this.customerCode = customerCode;
        this.saleInventories = saleInventories;
    }
}