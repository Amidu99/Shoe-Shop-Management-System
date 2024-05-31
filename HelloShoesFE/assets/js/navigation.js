$("#v-pills-home-tab").on("click", async () => {
    console.log("Home-tab");
});
$("#v-pills-sale-tab").on("click", async () => {
    console.log("Sale-tab");
    if(!$("#order_code").val()){ $("#order_btns>button[type='button']").eq(2).click(); }
});
$("#v-pills-customer-tab").on("click", async () => {
    console.log("Customer-tab");
    if(!$("#customer_code").val()){ $("#customer_btns>button[type='button']").eq(3).click(); }
});
$("#v-pills-items-tab").on("click", async () => {
    console.log("Inventory-tab");
    if(!$("#item_code").val()){ $("#inventory_btns>button[type='button']").eq(3).click(); }
});
$("#v-pills-suppliers-tab").on("click", async () => {
    console.log("Supplier-tab");
    if(!$("#supplier_code").val()){ $("#supplier_btns>button[type='button']").eq(3).click(); }
});
$("#v-pills-stock-tab").on("click", async () => {
    console.log("Stock-tab");
    if(!$("#stock_code").val()){ $("#stock_btns>button[type='button']").eq(3).click(); }
});
$("#v-pills-employee-tab").on("click", async () => {
    console.log("Employee-tab");
    if(!$("#employee_code").val()){ $("#employee_btns>button[type='button']").eq(3).click(); }
});
$("#v-pills-user-tab").on("click", async () => {
    console.log("User-tab");
    $("#user_btns>button[type='button']").eq(2).click();
});