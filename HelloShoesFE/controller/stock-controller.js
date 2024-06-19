import {Stock} from "../model/Stock.js";
import {InventoryServiceUrl, StockServiceUrl} from "../assets/js/urls.js";
import {showError, showSwalError} from "../assets/js/notifications.js";
import {getLowStockAlerts} from "./dashboard-controller.js";
import {stockCodePattern} from "../assets/js/regex.js";
let stock_row_index = null;

// check availability of the stockCode
async function isAvailableStockCode(stockCode) {
    const url = new URL(`${StockServiceUrl}/get`);
    try {
        const response = await fetch(url, {method: 'GET', headers: {"stockCode": stockCode, "Authorization": "Bearer " + localStorage.getItem("AuthToken")}});
        return response.status !== 204;
    } catch (error) {console.error('Error:', error);}
}

// check availability of the itemCode & size
async function isAvailableSizeStock(itemCode, size) {
    const url = new URL(`${StockServiceUrl}/getStock`);
    try {
        const response = await fetch(url, {method: 'GET', headers: {"itemCode": itemCode, "size": size, "Authorization": "Bearer " + localStorage.getItem("AuthToken")}});
        return response.status !== 204;
    } catch (error) {console.error('Error:', error);}
}

// check availability of the stock
async function isAvailableStock(stockCode, itemCode, size) {
    const url = new URL(`${StockServiceUrl}/checkThisStock`);
    try {
        const response = await fetch(url, {method: 'GET', headers: {"stockCode": stockCode, "itemCode": itemCode, "size": size, "Authorization": "Bearer " + localStorage.getItem("AuthToken")}});
        return response.status !== 204;
    } catch (error) {console.error('Error:', error);}
}

// save stock
$("#stock_btns>button[type='button']").eq(0).on("click", async () => {
    let stockCode = $("#stock_code").val();
    let itemCode = $("#stock_item_code_select").val();
    let supplierCode = $("#stock_supplier_code").val();
    let size = parseInt($("#stock_item_size_select").val());
    let originalQty = parseInt($("#original_qty").val());
    let availableQty = parseInt($("#available_qty").val());
    let status;
    if ( stockCode && itemCode && supplierCode ){
        if (size!==0 && itemCode!=="0"){
            if (!(await isAvailableStockCode(stockCode))){
                if (!(await isAvailableSizeStock(itemCode, size))){
                    if (originalQty > 0){
                        if(availableQty >= 0 && originalQty >= availableQty){
                            if((originalQty/2) < availableQty){status = 'Available';}
                            if((originalQty/2) >= availableQty){status = 'Low';}
                            if(availableQty===0){status = 'Not Available';}
                            let stockObject = new Stock(stockCode, size, originalQty, availableQty, status, itemCode, supplierCode);
                            let stockJSON = JSON.stringify(stockObject);
                            $.ajax({
                                url: `${StockServiceUrl}/save`,
                                type: "POST",
                                data: stockJSON,
                                headers: {"Content-Type": "application/json", "Authorization": "Bearer " + localStorage.getItem("AuthToken")},
                                success: (res) => {
                                    Swal.fire({width: '225px', position: 'center', icon: 'success', title: 'Saved!', showConfirmButton: false, timer: 2000});
                                    $("#stock_btns>button[type='button']").eq(3).click();
                                },
                                error: (err) => {
                                    if (err.status === 403) { showSwalError('Forbidden','You do not have permission to perform this action!');}
                                    else { showSwalError('Error', 'An error occurred while proceeding. Please try again later.');}
                                }
                            });
                        } else { showError('Invalid Available Qty!');}
                    } else { showError('Invalid Original Qty!');}
                } else { showError('This Item Size Stock is already exist!');}
            } else { showError('This Stock Code is already exist!');}
        } else { showError('Fields can not be empty!');}
    } else { showError('Fields can not be empty!');}
});

// update stock
$("#stock_btns>button[type='button']").eq(1).on("click", async () => {
    let stockCode = $("#stock_code").val();
    let itemCode = $("#stock_item_code_select").val();
    let supplierCode = $("#stock_supplier_code").val();
    let size = parseInt($("#stock_item_size_select").val());
    let originalQty = parseInt($("#original_qty").val());
    let availableQty = parseInt($("#available_qty").val());
    let status;
    if ( stockCode && itemCode && supplierCode ){
        if (size!==0 && itemCode!=="0"){
            if ((await isAvailableStockCode(stockCode))){
                if ((await isAvailableStock(stockCode, itemCode, size))){
                    if (originalQty > 0){
                        if(availableQty >= 0 && originalQty >= availableQty){
                            if((originalQty/2) < availableQty){status = 'Available';}
                            if((originalQty/2) >= availableQty){status = 'Low';}
                            if(availableQty===0){status = 'Not Available';}
                            let stockObject = new Stock(stockCode, size, originalQty, availableQty, status, itemCode, supplierCode);
                            let stockJSON = JSON.stringify(stockObject);
                            $.ajax({
                                url: `${StockServiceUrl}/update`,
                                type: "PUT",
                                data: stockJSON,
                                headers: {"Content-Type": "application/json", "Authorization": "Bearer " + localStorage.getItem("AuthToken")},
                                success: (res) => {
                                    Swal.fire({width: '225px', position: 'center', icon: 'success', title: 'Updated!', showConfirmButton: false, timer: 2000});
                                    $("#stock_btns>button[type='button']").eq(3).click();
                                },
                                error: (err) => {
                                    if (err.status === 403) { showSwalError('Forbidden','You do not have permission to perform this action!');}
                                    else { showSwalError('Error', 'An error occurred while proceeding. Please try again later.');}
                                }
                            });
                        } else { showError('Invalid Available Qty!');}
                    } else { showError('Invalid Original Qty!');}
                } else { showError('This Item & Size not match with this Stock Code!');}
            } else { showError('This Stock Code is not exist!');}
        } else { showError('Fields can not be empty!');}
    } else { showError('Fields can not be empty!');}
});

// delete stock
$("#stock_btns>button[type='button']").eq(2).on("click", async () => {
    let stockCode = $("#stock_code").val();
    if (stockCode) {
        if (stockCodePattern.test(stockCode)) {
            if ((await isAvailableStockCode(stockCode))) {
                Swal.fire({
                    width: '300px', title: 'Delete Stock', icon: 'question',
                    text: "Are you sure you want to permanently remove this stock?",  iconColor: '#FF7E00FF',
                    showCancelButton: true, confirmButtonText: 'Yes, delete!'
                }).then((result) => {
                    if (result.isConfirmed) {
                        const url = new URL(`${StockServiceUrl}/delete`);
                        $.ajax({
                            url: url,
                            type: "DELETE",
                            headers: { "stockCode": stockCode, "Authorization": "Bearer " + localStorage.getItem("AuthToken") },
                            success: (res) => {
                                console.log(JSON.stringify(res));
                                Swal.fire({width: '225px', position: 'center', icon: 'success', title: 'Deleted!', showConfirmButton: false, timer: 2000});
                                $("#stock_btns>button[type='button']").eq(3).click();
                            },
                            error: (err) => {
                                if (err.status === 403) { showSwalError('Forbidden','You do not have permission to perform this action!');}
                                else { showSwalError('Error', 'An error occurred while proceeding. Please try again later.');}
                            }
                        });
                    }
                });
            } else { showError('This Stock Code is not exist!');}
        } else { showError('Invalid Stock Code format!');}
    } else { showError('Stock Code can not be empty!');}
});

// reset stock form
$("#stock_btns>button[type='button']").eq(3).on("click", async () => {
    $('#stock_item_code_select').empty();
    loadStockData();
    loadItemCodes();
    $("#stock_item_code_select")[0].selectedIndex = 0;
    $("#stock_supplier_code").val("");
    $("#stock_item_size_select")[0].selectedIndex = 0;
    $("#original_qty").val("");
    $("#available_qty").val("");
    $("#stock_status").val("");
    getLowStockAlerts();
    const getNextCodeURL = new URL(`${StockServiceUrl}/getNextCode`);
    try {
        const response = await fetch(getNextCodeURL, { method: 'GET', headers:{"Authorization": "Bearer " + localStorage.getItem("AuthToken")}});
        if (response.ok) {
            const nextStockCode = await response.text();
            $("#stock_code").val(nextStockCode);
        } else { console.error(`HTTP error! Status: ${response.status}`);}
    } catch (error) { console.error('Error:', error);}
});

// when change the stock_item_code_select
$("#stock_item_code_select").on("change", async function () {
    let selectedItemCode = $(this).val();
    $("#stock_supplier_code").val('');
    const url = new URL(`${InventoryServiceUrl}/get`);
    if(selectedItemCode!=="0") {
        try {
            const response = await fetch(url, {method: 'GET', headers: {"itemCode": selectedItemCode, "Authorization": "Bearer " + localStorage.getItem("AuthToken")}});
            if (response.ok) {
                const data = await response.json();
                const supplierCode = data.supplierCode;
                $("#stock_supplier_code").val(supplierCode);
            } else { console.error(`HTTP error! Status: ${response.status}`); }
        } catch (error) { console.error('Error:', error); }
    }
});

// load all item codes to the select box
const loadItemCodes = () => {
    let title = $('<option>', { text: '-Select Item-', value: "0" });
    $("#stock_item_code_select").append(title);
    const getAllItemURL = new URL(`${InventoryServiceUrl}/getAll`);
    fetch(getAllItemURL, { method: 'GET', headers:{"Authorization": "Bearer " + localStorage.getItem("AuthToken")}})
        .then(response => {
            if (!response.ok) { throw new Error(`HTTP error! Status: ${response.status}`); }
            return response.json();
        })
        .then(data => {
            if (Array.isArray(data)) {
                data.forEach(item => {
                    let option = $('<option>', { text: item.itemCode, value: item.itemCode });
                    $("#stock_item_code_select").append(option);
                });
            } else { console.error('Error: Expected JSON array, but received: ', data); }
        })
    .catch(error => { console.error('Error:', error);});
};

// load all stock details to the table
const loadStockData = () => {
    const getAllURL = new URL(`${StockServiceUrl}/getAll`);
    fetch(getAllURL, { method: 'GET', headers:{"Authorization": "Bearer " + localStorage.getItem("AuthToken")}})
        .then(response => {
            if (!response.ok) { throw new Error(`HTTP error! Status: ${response.status}`); }
            return response.json();
        })
        .then(data => {
            if (Array.isArray(data)) {
                $('#stock_tbl_body').empty();
                data.forEach(stock => {
                    let record = `<tr><td class="stockCode">${stock.stockCode}</td><td class="itemCode">${stock.inventory.itemCode}</td>
                                  <td class="supplierCode">${stock.supplier.supplierCode}</td><td class="size">${stock.size}</td>
                                  <td class="originalQty">${stock.originalQty}</td><td class="availableQty">${stock.availableQty}</td>
                                  <td class="status">${stock.status}</td></tr>`;
                    $("#stock_tbl_body").append(record);
                });
            } else { console.error('Error: Expected JSON array, but received: ', data); }
        })
    .catch(error => { console.error('Error: ', error); });
};

// retrieve stock by table click
$("#stock_tbl_body").on("click", "tr", function() {
    stock_row_index = $(this).index();
    let stockCode = $(this).find(".stockCode").text();
    let itemCode = $(this).find(".itemCode").text();
    let supplierCode = $(this).find(".supplierCode").text();
    let size = $(this).find(".size").text();
    let originalQty = $(this).find(".originalQty").text();
    let availableQty = $(this).find(".availableQty").text();
    let status = $(this).find(".status").text();

    $("#stock_code").val(stockCode);
    $("#stock_item_code_select").val(itemCode);
    $("#stock_supplier_code").val(supplierCode);
    $("#stock_item_size_select").val(size);
    $("#original_qty").val(originalQty);
    $("#available_qty").val(availableQty);
    $("#stock_status").val(status);
});