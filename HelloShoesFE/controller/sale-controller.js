import {OrderDetail} from "../model/OrderDetail.js";
import {Sale} from "../model/Sale.js";
import {
    CustomerServiceUrl,
    InventoryServiceUrl,
    SaleServiceUrl,
    StockServiceUrl,
    UserServiceUrl
} from "../assets/js/urls.js";
import {showError, showSwalError, showSwalWarning} from "../assets/js/notifications.js";
import {namePattern, orderCodePattern} from "../assets/js/regex.js";
import {employeeName} from "./dashboard-controller.js";
import {Stock} from "../model/Stock.js";

let previousPoints = 0;
let order_row_index = null;
let item_row_index = null;
let userCode = null;
let sub_total = 0.00;
var real_temp_cart = [];
var temp_cart = [];

// load all customer codes to the select box
const loadCustomers = () => {
    let title = $('<option>', { text: '-Set Customer-', value: "0" });
    $("#order_customer_select").append(title);
    const getAllCustomerURL = new URL(`${CustomerServiceUrl}/getAll`);
    fetch(getAllCustomerURL, { method: 'GET', headers: { "Authorization": "Bearer " + localStorage.getItem("AuthToken")}})
        .then(response => {
            if (!response.ok) { throw new Error(`HTTP error! Status: ${response.status}`); }
            return response.json();
        })
        .then(data => {
            if (Array.isArray(data)) {
                data.forEach(customer => {
                    let option = $('<option>', { text: customer.customerCode, value: customer.customerName });
                    $("#order_customer_select").append(option);
                });
            } else { console.error('Error: Expected JSON array, but received: ', data); }
        })
        .catch(error => { console.error('Error:', error);});
};

// load all item codes to the select box
const loadItems = () => {
    let title = $('<option>', { text: '-Select Item-', value: "0" });
    $("#order_item_select").append(title);
    const getAllItemURL = new URL(`${InventoryServiceUrl}/getAll`);
    fetch(getAllItemURL, { method: 'GET', headers: { "Authorization": "Bearer " + localStorage.getItem("AuthToken")}})
        .then(response => {
            if (!response.ok) { throw new Error(`HTTP error! Status: ${response.status}`); }
            return response.json();
        })
        .then(data => {
            if (Array.isArray(data)) {
                data.forEach(item => {
                    let option = $('<option>', { text: item.itemCode, value: item.itemDesc+'~'+item.sellPrice });
                    $("#order_item_select").append(option);
                });
            } else { console.error('Error: Expected JSON array, but received: ', data); }
        })
        .catch(error => { console.error('Error:', error);});
};

// load all orders to the table
const loadOrderData = () => {
    $('#order_tbl_body').empty();
    const getAllOrdersURL = new URL(`${SaleServiceUrl}/getAll`);
    fetch(getAllOrdersURL, {method: 'GET', headers: { "Authorization": "Bearer " + localStorage.getItem("AuthToken")}})
        .then(response => {
            if (!response.ok) {throw new Error(`HTTP error! Status: ${response.status}`);}
            return response.json();
        })
        .then(data => {
            if (Array.isArray(data)) {
                $('#order_tbl_body').empty();
                data.forEach(order => {
                    let record = `<tr><td class="orderCode">${order.orderCode}</td>
                                  <td class="customerCode">${order.customerCode ? order.customerCode : 'No Customer Code'}</td>
                                  <td class="customerName">${order.customerName}</td><td class="date">${order.date}</td>
                                  <td class="payMethod">${order.payMethod}</td><td class="addedPoints">${order.addedPoints}</td>
                                  <td class="totalPrice">${order.totalPrice}</td></tr>`;
                    $("#order_tbl_body").append(record);
                });
            } else { console.error('Error: Expected JSON array, but received: ', data);}
        })
    .catch(error => { console.error('Error:', error);});
};

// when change the order_customer_select
$("#order_customer_select").on("change", function() {
    let customer_name = $(this).val();
    if(customer_name!=="0"){
        $("#order_customer_name").val(customer_name);
    }else{
        $("#order_customer_name").val('');
    }
});

// when change the order_pay_method_select
$("#order_pay_method_select").on("change", function() {
    let selectedPayMethod = $(this).val();
    if(selectedPayMethod==="Cash"){
        $("#card_section").css("display", "none");
        $("#cash_section").css("display", "block");
    }else if(selectedPayMethod==="Card"){
        $("#card_section").css("display", "block");
        $("#cash_section").css("display", "none");
    }else{
        $("#card_section").css("display", "none");
        $("#cash_section").css("display", "none");
    }
});

// when change the order_item_select
$("#order_item_select").on("change", function() {
    let selectedItemValue = $(this).val();
    if(selectedItemValue!=="0"){
        // Split the Value and Destructure into Desc & Price
        const [selectedItemDesc, selectedItemPrice] = selectedItemValue.split('~');
        $("#order_item_description").val(selectedItemDesc);
        $("#order_item_unit_price").val(selectedItemPrice);
    }else{
        $("#order_item_description").val('');
        $("#order_item_unit_price").val('');
    }
});

// when change the order_item_size_select
$("#order_item_size_select").on("change", async function () {
    $("#total_item_qty").val('');
    $("#order_get_item_qty").val('');
    let selectedSize = $(this).val();
    let orderCode = $("#order_code").val();
    let selectedItemCode = $("#order_item_select option:selected").text();
    const urlToGet = new URL(`${StockServiceUrl}/getStockDetail`);
    if (selectedSize!=="0" && selectedItemCode!=='-Select Item-') {
        try {
            const response = await fetch(urlToGet, {method: 'GET', headers: {"itemCode": selectedItemCode, "size": selectedSize, "Authorization": "Bearer " + localStorage.getItem("AuthToken")}});
            if (response.status===200) {
                const data = await response.json();
                const dbAvailableQty = data.availableQty;
                let cart_item_data = temp_cart.find(item => item.orderCode === orderCode && item.itemCode === selectedItemCode && item.size === selectedSize);
                if (cart_item_data) {
                    let in_cart_item_qty = cart_item_data.qty;
                    $("#total_item_qty").val(dbAvailableQty - in_cart_item_qty);
                } else { $("#total_item_qty").val(dbAvailableQty);}
            } else if (response.status===204) { showSwalWarning('Out Of Stock!',`${selectedItemCode} item is not available in size ${selectedSize}.`);
            } else { $("#cart_btns>button[type='reset']").eq(0).click(); console.error(`HTTP error! Status: ${response.status}`); }
        } catch (error) { console.error('Error:', error); }
    }
});

// Get ISO 8601 formatted timestamp
function getCurrentTimestamp() {
    const now = new Date();
    return now.toISOString();
}

// check availability of the code in temp_cart
function isAvailableForUpdate(orderCode) {
    const order_detail = temp_cart.find(order_detail =>
        order_detail.orderCode === orderCode
    );
    return order_detail !== undefined;
}

function isAvailableCode(orderCode, itemCode, size) {
    console.log('temp_cart:', temp_cart);
    const order_detail = temp_cart.find(order_detail =>
        order_detail.orderCode === orderCode &&
        order_detail.itemCode === itemCode &&
        order_detail.size === size
    );
    return order_detail !== undefined;
}

// check availability of the orderCode
async function isAvailableOrderCode(orderCode) {
    const url = new URL(`${SaleServiceUrl}/get`);
    try {
        const response = await fetch(url, {method: 'GET', headers: {"orderCode": orderCode, "Authorization": "Bearer " + localStorage.getItem("AuthToken")}});
        return response.status !== 204;
    } catch (error) {console.error('Error:', error);}
}

// reset the invoice form
$("#order_btns>button[type='button']").eq(1).on("click", async () => {
    $("#order_customer_select").empty();
    loadCustomers();
    $("#order_item_select").empty();
    loadItems();
    $("#order_customer_name").val("");
    $("#order_points").val("");
    $("#order_pay_method_select")[0].selectedIndex = 0;
    $("#order_total").val("");
    clear_form2();
    clear_form3();
    temp_cart = [];
    sub_total = 0.00;
    previousPoints = 0;
    $("#order_item_tbl_body").empty();
    loadOrderData();

    const getNextCodeURL = new URL(`${SaleServiceUrl}/getNextCode`);
    try {
        const response = await fetch(getNextCodeURL, {method: 'GET', headers: {"Authorization": "Bearer " + localStorage.getItem("AuthToken")}});
        if (response.ok) {
            const nextOrderCode = await response.text();
            $("#order_code").val(nextOrderCode);
        } else {console.error(`HTTP error! Status: ${response.status}`);}
    } catch (error) {console.error('Error:', error);}
});

// reset the cart form
function clear_form2() {
    $("#cart_btns>button[type='reset']").click();
}

// reset the payment form
function clear_form3() {
    document.getElementById("subTotal").innerHTML = "Sub Total : Rs. 0.00";
    document.getElementById("total").innerHTML = "Total : Rs. 0.00";
    $("#card_section").css("display", "none");
    $("#cash_section").css("display", "none");
    $("#cash").val('');
    $("#balance").val('');
    $("#order_bank_name_select")[0].selectedIndex = 0;
    $("#card_digits").val('');
    $("#payment_date_time").val('');
    $("#cashier_name").val('');
}

// load get items to the cart table
const loadCartItemData = () => {
    $('#order_item_tbl_body').empty();
    temp_cart.forEach((cartItemData, index) => {
        let itemPrice = parseFloat(cartItemData.itemPrice);
        let qty = parseInt(cartItemData.qty);
        let total = itemPrice * qty;
        let record = `<tr>
                        <td class="itemCode">${cartItemData.itemCode}</td>
                        <td class="size">${cartItemData.size}</td>
                        <td class="itemDesc">${cartItemData.itemDesc}</td>
                        <td class="itemPrice">${itemPrice.toFixed(2)}</td>
                        <td class="qty">${qty}</td>
                        <td class="item_total">${total.toFixed(2)}</td>
                      </tr>`;
        $("#order_item_tbl_body").append(record);
    });
};

// calculate the points
function calcPoints(){
    let points = (sub_total/800).toFixed(2);
    $("#order_total").val(sub_total);
    $("#order_points").val(points);
    document.getElementById("subTotal").innerHTML = "Sub Total : Rs. "+sub_total;
}

// add item to the cart
$("#cart_btns>button[type='button']").eq(0).on("click", () => {
    let orderCode = $("#order_code").val();
    let itemCode = $("#order_item_select option:selected").text();
    let itemDesc = $("#order_item_description").val();
    let itemPrice = $("#order_item_unit_price").val();
    let total_item_qty = $("#total_item_qty").val();
    let size = $("#order_item_size_select").val();
    let get_item_qty = $("#order_get_item_qty").val();
    if(orderCode) {
        if (orderCodePattern.test(orderCode)) {
            if (itemCode && get_item_qty) {
                if(parseInt(get_item_qty)<=parseInt(total_item_qty) && parseInt(get_item_qty)>0) {
                    if(isAvailableCode(orderCode, itemCode, size)) {
                        let order_detail = temp_cart.find(order_detail =>
                            order_detail.orderCode === orderCode && order_detail.itemCode === itemCode && order_detail.size === size);
                        let replace_index = temp_cart.findIndex(item =>
                            item.orderCode === orderCode && item.itemCode === itemCode  && item.size === size);
                        let inCart_count = parseInt(order_detail.qty);
                        let new_count = inCart_count + parseInt(get_item_qty);
                        temp_cart[replace_index] = new OrderDetail('', itemCode, itemDesc, itemPrice, orderCode, size, new_count);
                        sub_total -= (itemPrice * inCart_count);
                        sub_total += itemPrice * new_count;
                        calcPoints();
                        $("#cart_btns>button[type='reset']").click();
                        loadCartItemData();
                    } else {
                        let cart_item_obj = new OrderDetail('', itemCode, itemDesc, itemPrice, orderCode, size, get_item_qty);
                        temp_cart.push(cart_item_obj);
                        sub_total += itemPrice * get_item_qty;
                        calcPoints();
                        $("#cart_btns>button[type='reset']").click();
                        loadCartItemData();
                    }
                } else { showError('Invalid Quantity!'); }
            } else { showError('Fields can not be empty!'); }
        } else { showError('Invalid Order Code format!'); }
    } else { showError('Order Code can not be empty!'); }
});

// remove item from the cart
$("#cart_btns>button[type='button']").eq(1).on("click", () => {
    let orderCode = $("#order_code").val();
    let itemCode = $("#order_item_select option:selected").text();
    let itemDesc = $("#order_item_description").val();
    let itemPrice = $("#order_item_unit_price").val();
    let size = parseInt($("#order_item_size_select").val());
    let get_item_qty = parseInt($("#order_get_item_qty").val());
    if(orderCode) {
        if (orderCodePattern.test(orderCode)) {
            if (itemCode && get_item_qty) {
                if(isAvailableCode(orderCode, itemCode, size)) {
                    let cart_item_data = temp_cart.find(order_detail =>
                        order_detail.orderCode === orderCode && order_detail.itemCode === itemCode && order_detail.size === size);
                    let remove_index = temp_cart.findIndex(order_detail =>
                        order_detail.orderCode === orderCode && order_detail.itemCode === itemCode && order_detail.size === size);
                    if (cart_item_data) {
                        if (get_item_qty === parseInt(cart_item_data.qty)) {
                            temp_cart.splice(remove_index, 1);
                            sub_total -= itemPrice * get_item_qty;
                            calcPoints();
                            $("#cart_btns>button[type='reset']").click();
                            loadCartItemData();
                        } else if(get_item_qty < cart_item_data.qty && get_item_qty > 0) {
                            let inCart_count = parseInt(cart_item_data.qty);
                            let new_count = inCart_count - get_item_qty;
                            temp_cart[remove_index] = new OrderDetail('', itemCode, itemDesc, itemPrice, orderCode, size, new_count);
                            sub_total -= (itemPrice * inCart_count);
                            sub_total += (itemPrice * new_count);
                            calcPoints();
                            $("#cart_btns>button[type='reset']").click();
                            loadCartItemData();
                        } else { showError('Invalid Quantity!'); }
                    } else { showError('This item is not available to remove!'); }
                } else { showError('This Item is not available in this order!'); }
            } else { showError('Fields can not be empty!'); }
        } else { showError('Invalid Order Code format!'); }
    } else { showError('Order Code can not be empty!'); }
});

// retrieve cart item by table click
$("#order_item_tbl_body").on("click", "tr", function() {
    item_row_index = $(this).index();
    let itemCode = $(this).find(".itemCode").text();
    let size = $(this).find(".size").text();
    let itemDesc = $(this).find(".itemDesc").text();
    let itemPrice = $(this).find(".itemPrice").text();
    let qty = $(this).find(".qty").text();
    selectItemOptionByText(itemCode);
    $("#order_item_size_select").val(size);
    $("#order_item_description").val(itemDesc);
    $("#order_item_unit_price").val(itemPrice);
    $("#order_get_item_qty").val(qty);
});

// select item select vale by text
function selectItemOptionByText(itemCode) {
    const itemSelectBox = document.getElementById('order_item_select');
    for (let i = 0; i < itemSelectBox.options.length; i++) {
        if (itemSelectBox.options[i].text === itemCode) {
            itemSelectBox.selectedIndex = i;
            break;
        }
    }
}

// retrieve order by table click
$("#order_tbl_body").on("click", "tr", async function () {
    order_row_index = $(this).index();
    let orderCode = $(this).find(".orderCode").text();
    let customerCode = $(this).find(".customerCode").text();
    let customerName = $(this).find(".customerName").text();
    let date = $(this).find(".date").text();
    let payMethod = $(this).find(".payMethod").text();
    let addedPoints = $(this).find(".addedPoints").text();
    let totalPrice = $(this).find(".totalPrice").text();

    await getOrderDetailsToTemp(orderCode);
    loadCartItemData();
    $("#order_code").val(orderCode);

    if(customerCode==='No Customer Code'){ $("#order_customer_select")[0].selectedIndex = 0;}
    else{ selectCustomerOptionByText(customerCode);}
    $("#order_customer_name").val(customerName);
    $("#payment_date_time").val(formatISOToLocalDateTime(date));

    if(payMethod==="Cash"){
        $("#order_pay_method_select")[0].selectedIndex = 1;
        $("#card_section").css("display", "none");
    }else{
        // Split the Value and Destructure into payMethod & bank+card4digit
        const [method, bankDetail] = payMethod.split('~');
        const [bank, card] = bankDetail.split('*');
        $("#order_pay_method_select").val(method);
        $("#order_bank_name_select").val(bank);
        $("#card_digits").val(card);
        $("#card_section").css("display", "block");
        $("#cash_section").css("display", "none");
    }
    $("#order_points").val(addedPoints);
    previousPoints = addedPoints;
    sub_total = totalPrice;
    document.getElementById("total").innerHTML = "Total : Rs. " + totalPrice;
    document.getElementById("subTotal").innerHTML = "Sub Total : Rs. " + sub_total;
    $("#order_total").val(totalPrice);
});

// select customer select vale by text
function selectCustomerOptionByText(customerCode) {
    const customerSelectBox = document.getElementById('order_customer_select');
    for (let i = 0; i < customerSelectBox.options.length; i++) {
        if (customerSelectBox.options[i].text === customerCode) {
            customerSelectBox.selectedIndex = i;
            break;
        }
    }
}

function formatISOToLocalDateTime(date) {
    let date1 = new Date(date);
    let year = date1.getFullYear();
    let month = ('0' + (date1.getMonth() + 1)).slice(-2);
    let day = ('0' + date1.getDate()).slice(-2);
    let hours = ('0' + date1.getHours()).slice(-2);
    let minutes = ('0' + date1.getMinutes()).slice(-2);
    let seconds = ('0' + date1.getSeconds()).slice(-2);
    let milliseconds = ('00' + date1.getMilliseconds()).slice(-3);
    return `${year}-${month}-${day}T${hours}:${minutes}:${seconds}.${milliseconds}`;
}

// get order details to temp_cart_db
async function getOrderDetailsToTemp(orderCode) {
    await getOrderDetailsToRealTemp(orderCode);
    temp_cart = [...real_temp_cart];
}

// get order details to real_temp_cart_db
async function getOrderDetailsToRealTemp(orderCode) {
    real_temp_cart = [];
    const url = new URL(`${SaleServiceUrl}/get`);
    try {
        const response = await fetch(url, { method: 'GET', headers: { "orderCode": orderCode, "Authorization": "Bearer " + localStorage.getItem("AuthToken")}});
        if (response.status===204) {
            console.error(`No content order: ${response.status}`);
        }
        const data = await response.json();
        if (data && Array.isArray(data.saleInventories)) {real_temp_cart = data.saleInventories;}
        else { console.error('Error: saleInventories is not an array or is missing from the response.');}
    } catch (error) { console.error('Error:', error);}
}

async function getUserCodeByEmail() {
    let email = localStorage.getItem('CurrentUser');
    const url = new URL(`${UserServiceUrl}/get`);
    try {
        const response = await fetch(url, {method: 'GET', headers: {"email": email, "Authorization": "Bearer " + localStorage.getItem("AuthToken")}});
        if (response.status === 200) {
            const data = await response.json();
            userCode = data.userCode;
        } else {console.error('Error:', response.status, response.statusText);}
    } catch (error) {console.error('Error:', error);}
}

async function updatePoints(customerCode, addedPoints, date) {
    const url = new URL(`${CustomerServiceUrl}/updatePoints`);
    try {
        const response = await fetch(url, {
            method: 'PUT',
            headers: {"customerCode": customerCode, "totalPoints": addedPoints, "timestampString": date, "Authorization": "Bearer " + localStorage.getItem("AuthToken")}});
        if (response.status === 200) {
            console.log("Customer updated.");
        } else {console.error('Error:', response.status, response.statusText);}
    } catch (error) {console.error('Error:', error);}
}

//  place order
$("#btn_place_order").on("click", async () => {
    let orderCode = $("#order_code").val();
    let customerName = $("#order_customer_name").val();
    let total = sub_total;
    let date = getCurrentTimestamp();
    let payMethod = $("#order_pay_method_select").val();
    let bank = $("#order_bank_name_select").val();
    let digits = $("#card_digits").val();
    let cash = $("#cash").val();
    let addedPoints = $("#order_points").val();
    let cashierName = employeeName;
    await getUserCodeByEmail();
    let customerCode;
    let customer_select_val = $("#order_customer_select").val();
    if (customer_select_val!=="0"){ customerCode = $("#order_customer_select option:selected").text();}
    else {customerCode = '';}
    let saleInventories = temp_cart;
    if (orderCode) {
        if (orderCodePattern.test(orderCode)) {
            if (!(await isAvailableOrderCode(orderCode))) {
                if (namePattern.test(customerName)) {
                    if (payMethod!=="0") {
                        if(payMethod==="Card"){
                            if(bank!=="0"){
                                if(/^\d{4}$/.test(digits)){
                                    payMethod = (payMethod+'~'+bank+'*'+digits);
                                }else{showError('Enter only the last 4 digits!'); return;}
                            }else{showError('Invalid Bank Name!'); return;}
                        }
                        if(payMethod==="Cash"){ if(cash < total){ showError('Insufficient cash!'); return; }}
                        if (total > 0) {
                            let saleObject = new Sale(orderCode, customerName, total, date, payMethod, addedPoints, cashierName, userCode, customerCode, saleInventories);
                            let saleJSON = JSON.stringify(saleObject);
                            $.ajax({
                                url: `${SaleServiceUrl}/save`,
                                type: "POST",
                                data: saleJSON,
                                headers: {
                                    "Content-Type": "application/json",
                                    "Authorization": "Bearer " + localStorage.getItem("AuthToken")
                                },
                                success: async (res) => {
                                    Swal.fire({
                                        width: '225px', position: 'center', icon: 'success',
                                        title: 'Saved!', showConfirmButton: false, timer: 2000
                                    });
                                    await updateItemQuantities();
                                    displayBill(saleObject.orderCode, getToday(), saleObject.payMethod, saleObject.saleInventories);
                                    if(customerCode!=='') {
                                        await updatePoints(customerCode, addedPoints, date);
                                    }
                                    $("#order_btns>button[type='button']").eq(1).click();
                                },
                                error: (err) => {
                                    if (err.status === 403) { showSwalError('Forbidden','You do not have permission to perform this action!');}
                                    else { showSwalError('Error', 'An error occurred while proceeding. Please try again later.');}
                                }
                            });
                        } else { showError('Invalid total!');}
                    } else { showError('Invalid Pay Method!');}
                } else { showError('Enter valid Customer Name!');}
            } else { showError('This Order Code is already exist!');}
        } else { showError('Invalid Order Code format!');}
    } else { showError('Order Code can not be empty!');}
});

// update and refund
$("#order_btns>button[type='button']").eq(0).on("click", async () => {
    let orderCode = $("#order_code").val();
    let customerName = $("#order_customer_name").val();
    let total = sub_total;
    let date = getCurrentTimestamp();
    let payMethod = $("#order_pay_method_select").val();
    let bank = $("#order_bank_name_select").val();
    let digits = $("#card_digits").val();
    let addedPoints = parseFloat($("#order_points").val());
    let cashierName = employeeName;
    await getUserCodeByEmail();
    let customerCode;
    let customer_select_val = $("#order_customer_select").val();
    if (customer_select_val!=="0"){ customerCode = $("#order_customer_select option:selected").text();}
    else {customerCode = '';}
    let saleInventories = temp_cart;
    if (orderCode) {
        if (orderCodePattern.test(orderCode)) {
            if ((await isAvailableOrderCode(orderCode))) {
                if (namePattern.test(customerName)) {
                    if (payMethod!=="0") {
                        if(payMethod==="Card"){
                            if(bank!=="0"){
                                if(/^\d{4}$/.test(digits)){
                                    payMethod = (payMethod+'~'+bank+'*'+digits);
                                }else{showError('Enter only the last 4 digits!'); return;}
                            }else{showError('Invalid Bank Name!'); return;}
                        }
                        if (total >= 0) {
                            if (isAvailableForUpdate(orderCode)) {
                                if (isOrderDateWithinThreeDays()) {
                                    if (parseFloat(previousPoints)!==addedPoints) { addedPoints = (parseFloat(previousPoints) - addedPoints);}
                                    let saleObject = new Sale(orderCode, customerName, total, date, payMethod, addedPoints, cashierName, userCode, customerCode, saleInventories);
                                    let saleJSON = JSON.stringify(saleObject);
                                    $.ajax({
                                        url: `${SaleServiceUrl}/update`,
                                        type: "PUT",
                                        data: saleJSON,
                                        headers: {
                                            "Content-Type": "application/json",
                                            "Authorization": "Bearer " + localStorage.getItem("AuthToken")
                                        },
                                        success: async (res) => {
                                            Swal.fire({
                                                width: '225px', position: 'center', icon: 'success',
                                                title: 'Updated!', showConfirmButton: false, timer: 2000
                                            });
                                            await updateItemQuantities();
                                            if(customerCode!=='' && previousPoints!==addedPoints) {
                                                let updatedPoints = addedPoints - previousPoints;
                                                await updatePoints(customerCode, updatedPoints, date);
                                            }
                                            $("#order_btns>button[type='button']").eq(1).click();
                                        },
                                        error: (err) => {
                                            if (err.status === 403) { showSwalError('Forbidden','You do not have permission to perform this action!');}
                                            else { showSwalError('Error', 'An error occurred while proceeding. Please try again later.');}
                                        }
                                    });
                                } else { showError("Orders over 3 days can't be updated.");}
                            } else { showError('No details to update!');}
                        } else { showError('Invalid total!');}
                    } else { showError('Invalid Pay Method!');}
                } else { showError('Enter valid Customer Name!');}
            } else { showError('This Order Code is not exist for update!');}
        } else { showError('Invalid Order Code format!');}
    } else { showError('Order Code can not be empty!');}
});

// check possibility for refund
function isOrderDateWithinThreeDays() {
    let today = new Date();
    today.setHours(0, 0, 0, 0);
    let orderDate = $("#payment_date_time").val();
    let orderDateTime = new Date(orderDate);
    orderDateTime.setHours(0, 0, 0, 0);
    let timeDifference = today - orderDateTime;
    let dayDifference = timeDifference / (1000 * 60 * 60 * 24);
    return dayDifference <= 3;
}

// calculate balance & points
const inputElement = document.getElementById('cash');
inputElement.addEventListener('input', function (event) {
    let cash = event.target.value;
    if(cash>0) {
        let total = sub_total;
        document.getElementById("total").innerHTML = "Total : Rs. "+total;
        let cash = $("#cash").val();
        if (cash>=total) {
            let balance = cash - total;
            let points = (total/800).toFixed(2);
            $("#balance").val(balance);
            $("#order_total").val(total);
            $("#order_points").val(points);
        } else { showSwalWarning('Oops!', 'Insufficient cash for get balance.');}
    } else { showError('Invalid input!'); }
});

// update item quantities in the db
async function updateItemQuantities() {
    for (let i = 0; i < temp_cart.length; i++) {
        let itemCode = temp_cart[i].itemCode;
        let size = temp_cart[i].size;
        let get_qty = temp_cart[i].qty;

        const urlToGet = new URL(`${StockServiceUrl}/getStockDetail`);
        try {
            const response = await fetch(urlToGet, {method: 'GET', headers: {"itemCode": itemCode, "size": size, "Authorization": "Bearer " + localStorage.getItem("AuthToken")}});
            if (response.status===200) {
                const data = await response.json();
                const stockCode = data.stockCode;
                const dbAvailableQty = parseInt(data.availableQty);
                const originalQty = parseInt(data.originalQty);
                const supplierCode = data.supplierCode;
                let status;

                let real_temp_item_data = real_temp_cart.find(real_item => real_item.itemCode === itemCode && real_item.size === size);
                let updated_qty;
                if (real_temp_item_data) {
                    let real_temp_qty = parseInt(real_temp_item_data.qty);
                    updated_qty = (dbAvailableQty + real_temp_qty) - get_qty;
                } else { updated_qty = dbAvailableQty - get_qty; }

                if((originalQty/2) < updated_qty){status = 'Available';}
                if((originalQty/2) >= updated_qty){status = 'Low';}
                if(updated_qty===0){status = 'Not Available';}

                let stockObject = new Stock(stockCode, size, originalQty, updated_qty, status, itemCode, supplierCode);
                let stockJSON = JSON.stringify(stockObject);
                $.ajax({
                    url: `${StockServiceUrl}/update`,
                    type: "PUT",
                    data: stockJSON,
                    headers: {"Content-Type": "application/json", "Authorization": "Bearer " + localStorage.getItem("AuthToken")},
                    success: (res) => { console.log(JSON.stringify(res)); },
                    error: (err) => { console.error(err); }
                });
            } else {console.error(`HTTP error! Status: ${response.status}`);}
        } catch (error) {console.error('Error:', error);}
    }
}

function getToday() {
    const today = new Date();
    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, '0');
    const day = String(today.getDate()).padStart(2, '0');
    return year + "-" + month + "-" + day;
}

function displayBill(orderCode, orderDate, payMethod, orderItems) {
    document.getElementById('billOrderCode').innerText = orderCode;
    document.getElementById('billOrderDate').innerText = orderDate;
    document.getElementById('billPayMethod').innerText = payMethod;

    const tableBody = document.getElementById('billTable').getElementsByTagName('tbody')[0];
    tableBody.innerHTML = '';

    orderItems.forEach(item => {
        const total = item.qty * item.itemPrice;
        const row = document.createElement('tr');
        row.innerHTML = `
                    <td>${item.itemCode}</td>
                    <td>${item.size}</td>
                    <td>${item.qty}</td>
                    <td>${item.itemPrice}</td>
                    <td>${total.toFixed(2)}</td>
                `;
        tableBody.appendChild(row);
    });
    document.getElementById('billTotal').innerText = sub_total.toFixed(2);
    $('#bill_section').css('display', 'block');
}

$(".print-button>button[type='button']").eq(0).on("click", () => {
    const sectionToPrint = document.getElementById("bill");
    printJS({
        printable: sectionToPrint,
        type: 'html',
        ignoreElements: ['print-buttons'],
        targetStyles: ['*']
    });
});

$(".print-button>button[type='button']").eq(1).on("click", () => {
    $('#bill_section').css('display', 'none');
});