export class OrderDetail {
    constructor(orderDetailCode, itemCode, itemDesc, itemPrice, orderCode, size, qty) {
        this.orderDetailCode = orderDetailCode;
        this.itemCode = itemCode;
        this.itemDesc = itemDesc;
        this.itemPrice = itemPrice;
        this.orderCode = orderCode;
        this.size = size;
        this.qty = qty;
    }
}