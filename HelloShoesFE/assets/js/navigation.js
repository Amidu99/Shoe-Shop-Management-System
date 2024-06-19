$("#v-pills-home-tab").on("click", async () => {
});

$("#v-pills-sale-tab").on("click", async () => {
    if(!$("#order_code").val()){ $("#order_btns>button[type='button']").eq(1).click(); }
});

$("#v-pills-customer-tab").on("click", async () => {
    if(!$("#customer_code").val()){ $("#customer_btns>button[type='button']").eq(3).click(); }
});

$("#v-pills-items-tab").on("click", async () => {
    if(!$("#item_code").val()){ $("#inventory_btns>button[type='button']").eq(3).click(); }
});

$("#v-pills-suppliers-tab").on("click", async () => {
    if(!$("#supplier_code").val()){ $("#supplier_btns>button[type='button']").eq(3).click(); }
});

$("#v-pills-stock-tab").on("click", async () => {
    if(!$("#stock_code").val()){ $("#stock_btns>button[type='button']").eq(3).click(); }
});

$("#v-pills-employee-tab").on("click", async () => {
    if(!$("#employee_code").val()){ $("#employee_btns>button[type='button']").eq(3).click(); }
});

$("#v-pills-user-tab").on("click", async () => {
    $("#user_btns>button[type='button']").eq(2).click();
});