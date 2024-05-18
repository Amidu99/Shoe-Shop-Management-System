let InventoryServiceUrl = 'http://localhost:8080/helloshoesbe/api/v1/inventory';

const descPattern = /^[A-Za-z\d\s.,@&*()#!"']{3,}$/;
let base64InventoryPic = null;
let inventory_row_index = null;

// toastr error message
function showError(message) {
    toastr.error(message, 'Oops...', {
        "closeButton": true,
        "progressBar": true,
        "positionClass": "toast-top-center",
        "timeOut": "2500"
    });
}

// check availability of the itemCode
async function isAvailableItemCode(itemCode) {
    const url = new URL(`${InventoryServiceUrl}/get`);
    try {
        const response = await fetch(url, {method: 'GET', headers: {"itemCode": itemCode}});
        return response.status !== 204;
    } catch (error) {console.error('Error:', error);}
}

// load all supplier codes to the select box
const loadSuppliers = () => {
    let title = $('<option>', { text: '-Select Supplier-', value: "0" });
    $("#item_supplier_select").append(title);
    const getAllSupplierURL = new URL(`${SupplierServiceUrl}/getAll`);
    fetch(getAllSupplierURL, { method: 'GET', })
        .then(response => {
            if (!response.ok) { throw new Error(`HTTP error! Status: ${response.status}`); }
            return response.json();
        })
        .then(data => {
            if (Array.isArray(data)) {
                data.forEach(supplier => {
                    let option = $('<option>', { text: supplier.supplierCode, value: supplier.supplierCode });
                    $("#item_supplier_select").append(option);
                });
            } else { console.error('Error: Expected JSON array, but received: ', data); }
        })
    .catch(error => { console.error('Error:', error);});
};

// preview selected image
function previewImage() {
    var file = $('#item_pic').prop('files')[0];
    var reader = new FileReader();

    reader.onload = function() {
        base64InventoryPic = reader.result;
        $('#preview_item_pic').attr('src', base64InventoryPic).css('display', 'block');
    };

    if (file) { reader.readAsDataURL(file);}
    else { $('#preview_item_pic').css('display', 'none'); }
}

$(document).ready(function() {
    $('#item_pic').on('change', previewImage);
});

// calculate profit & profit margin
const item_sellPrice_element = document.getElementById('item_sell_price');
item_sellPrice_element.addEventListener('input', function (event) {
    let sellPrice = parseFloat(event.target.value);
    let buyPrice = parseFloat($("#item_buy_price").val());
    if(sellPrice > buyPrice) {
        let profit = sellPrice - buyPrice;
        let profitMargin = (profit/sellPrice)*100;
        $("#item_profit").val(profit);
        $("#item_profit_margin").val(profitMargin.toFixed(2));
    } else { showError('Invalid Sell Price input!'); }
});

// save inventory
$("#inventory_btns>button[type='button']").eq(0).on("click", async () => {
    let itemNumberCode = $("#item_code").val();
    let itemDesc = $("#item_desc").val();
    let itemPicFile = $('#item_pic').prop('files')[0];
    let itemPic;
    let categoryCode = $("#item_category_select").val();
    let category = $("#item_category_select option:selected").text();
    let genderCode = $("#item_gender_select").val();
    let typeCode = $("#item_type_select").val();
    let supplierCode = $("#item_supplier_select").val();
    let supplierName = $("#item_supplier_name").val();
    let buyPrice = parseFloat($("#item_buy_price").val());
    let sellPrice = parseFloat($("#item_sell_price").val());
    let profit = $("#item_profit").val();
    let profitMargin = $("#item_profit_margin").val();

    if ( itemNumberCode && itemDesc && itemPicFile && supplierName && buyPrice && sellPrice && profit && profitMargin) {
        if (categoryCode!=="0" && genderCode!=="0" && typeCode!=="0" && supplierCode!=="0"){
            let itemCode = categoryCode+genderCode+typeCode+"-"+itemNumberCode;
            itemPic = base64InventoryPic;
            if (!(await isAvailableItemCode(itemCode))) {
                if (descPattern.test(itemDesc)) {
                    if (itemPic) {
                        if (buyPrice > 0 && sellPrice > 0) {
                            if (buyPrice < sellPrice) {
                                let inventoryObject = {
                                    itemCode: itemCode,
                                    itemDesc: itemDesc,
                                    itemPic: itemPic,
                                    category: category,
                                    supplierCode: supplierCode,
                                    supplierName: supplierName,
                                    buyPrice: buyPrice,
                                    sellPrice: sellPrice,
                                    profit: profit,
                                    profitMargin: profitMargin
                                };
                                let inventoryJSON = JSON.stringify(inventoryObject);
                                $.ajax({
                                    url: `${InventoryServiceUrl}/save`,
                                    type: "POST",
                                    data: inventoryJSON,
                                    headers: {"Content-Type": "application/json"},
                                    success: (res) => {
                                        Swal.fire({width: '225px', position: 'center', icon: 'success', title: 'Saved!', showConfirmButton: false, timer: 2000});
                                        $("#inventory_btns>button[type='button']").eq(3).click();
                                    },
                                    error: (err) => { console.error(err);}
                                });
                            } else { showError('Invalid sell price!');}
                        } else { showError('Invalid price input!');}
                    } else { showError('Invalid Item Img!');}
                } else { showError('Invalid desc input!');}
            } else { showError('This Item Code is already exist!');}
        }else{showError('Fields can not be empty!');}
    } else { showError('Fields can not be empty!');}
});

// update inventory
$("#inventory_btns>button[type='button']").eq(1).on("click", async () => {
    let itemCode = $("#item_code").val();
    let itemDesc = $("#item_desc").val();
    let itemPic;
    let categoryCode = itemCode.charAt(0);
    let category;
    let supplierCode = $("#item_supplier_select").val();
    let supplierName = $("#item_supplier_name").val();
    let buyPrice = parseFloat($("#item_buy_price").val());
    let sellPrice = parseFloat($("#item_sell_price").val());
    let profit = $("#item_profit").val();
    let profitMargin = $("#item_profit_margin").val();

    if ( itemCode && itemDesc && supplierName && buyPrice && sellPrice && profit && profitMargin) {
        if (categoryCode!=="0" && supplierCode!=="0") {
            const categoryMap = { 'F': 'Formal', 'C': 'Casual', 'S': 'Sport', 'I': 'Industrial' };
            category = categoryMap[categoryCode];
            itemPic = base64InventoryPic;
            if ((await isAvailableItemCode(itemCode))) {
                if (descPattern.test(itemDesc)) {
                    if (itemPic) {
                        if (buyPrice > 0 && sellPrice > 0) {
                            if (buyPrice < sellPrice) {
                                let inventoryObject = {
                                    itemCode: itemCode,
                                    itemDesc: itemDesc,
                                    itemPic: itemPic,
                                    category: category,
                                    supplierCode: supplierCode,
                                    supplierName: supplierName,
                                    buyPrice: buyPrice,
                                    sellPrice: sellPrice,
                                    profit: profit,
                                    profitMargin: profitMargin
                                };
                                let inventoryJSON = JSON.stringify(inventoryObject);
                                $.ajax({
                                    url: `${InventoryServiceUrl}/update`,
                                    type: "PUT",
                                    data: inventoryJSON,
                                    headers: {"Content-Type": "application/json"},
                                    success: (res) => {
                                        Swal.fire({width: '225px', position: 'center', icon: 'success', title: 'Updated!', showConfirmButton: false, timer: 2000});
                                        $("#inventory_btns>button[type='button']").eq(3).click();
                                    },
                                    error: (err) => { console.error(err);}
                                });
                            } else { showError('Invalid sell price!');}
                        } else { showError('Invalid price input!');}
                    } else { showError('Invalid Item Img!');}
                } else { showError('Invalid desc input!');}
            } else { showError('This Item Code is not exist!');}
        } else {showError('Fields can not be empty!');}
    } else { showError('Fields can not be empty!');}
});

// delete inventory
$("#inventory_btns>button[type='button']").eq(2).on("click", async () => {
    let itemCode = $("#item_code").val();
    if (itemCode) {
        if ((await isAvailableItemCode(itemCode))) {
            Swal.fire({
                width: '300px', title: 'Delete Item',
                text: "Are you sure you want to permanently remove this item?",
                icon: 'warning', showCancelButton: true, confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33', confirmButtonText: 'Yes, delete!'
            }).then((result) => {
                if (result.isConfirmed) {
                    const url = new URL(`${InventoryServiceUrl}/delete`);
                    $.ajax({
                        url: url,
                        type: "DELETE",
                        headers: { "itemCode": itemCode },
                        success: (res) => {
                            Swal.fire({width: '225px', position: 'center', icon: 'success', title: 'Deleted!', showConfirmButton: false, timer: 2000});
                            $("#inventory_btns>button[type='button']").eq(3).click();
                        },
                        error: (err) => { console.error(err)}
                    });
                }
            });
        } else { showError('This Item Code is not exist!');}
    } else { showError('Item Code can not be empty!');}
});

// reset inventories form
$("#inventory_btns>button[type='button']").eq(3).on("click", async () => {
    base64InventoryPic = null;
    $('#preview_item_pic').css('display', 'none');
    loadInventoryData();
    $('#item_supplier_select').empty();
    loadSuppliers();
    $("#item_desc").val("");
    $("#item_pic").val("");
    $("#item_category_select")[0].selectedIndex = 0;
    $("#item_gender_select")[0].selectedIndex = 0;
    $("#item_type_select")[0].selectedIndex = 0;
    $("#item_supplier_select")[0].selectedIndex = 0;
    $("#item_supplier_name").val("");
    $("#item_buy_price").val("");
    $("#item_sell_price").val("");
    $("#item_profit").val("");
    $("#item_profit_margin").val("");

    const getNextCodeURL = new URL(`${InventoryServiceUrl}/getNextCode`);
    try {
        const response = await fetch(getNextCodeURL, { method: 'GET', });
        if (response.ok) {
            const nextItemCode = await response.text();
            $("#item_code").val(nextItemCode);
        } else { console.error(`HTTP error! Status: ${response.status}`);}
    } catch (error) { console.error('Error:', error);}
});

// when change the item_supplier_select
$("#item_supplier_select").on("change", async function () {
    let selectedSupplierCode = $(this).val();
    $("#item_supplier_name").val('');
    const urlToGet = new URL(`${SupplierServiceUrl}/get`);
    if (selectedSupplierCode!=="0") {
        try {
            const response = await fetch(urlToGet, {method: 'GET', headers: {"supplierCode": selectedSupplierCode}});
            if (response.ok) {
                const data = await response.json();
                const supplierName = data.supplierName;
                $("#item_supplier_name").val(supplierName);
            } else { console.error(`HTTP error! Status: ${response.status}`); }
        } catch (error) { console.error('Error:', error); }
    }
});

// load all inventories to the table
const loadInventoryData = () => {
    const getAllURL = new URL(`${InventoryServiceUrl}/getAll`);
    fetch(getAllURL, { method: 'GET', })
        .then(response => {
            if (!response.ok) { throw new Error(`HTTP error! Status: ${response.status}`); }
            return response.json();
        })
        .then(data => {
            if (Array.isArray(data)) {
                $('#inventory_tbl_body').empty();
                data.forEach(inventory => {
                    let record = `<tr><td class="itemCode">${inventory.itemCode}</td><td class="itemDesc">${inventory.itemDesc}</td>
                                  <td><img class="itemPic" src="${inventory.itemPic}" width="100px" height="100px"></td>
                                  <td class="category">${inventory.category}</td><td class="supplierCode">${inventory.supplierCode}</td>
                                  <td class="supplierName">${inventory.supplierName}</td><td class="buyPrice">${inventory.buyPrice}</td>
                                  <td class="sellPrice">${inventory.sellPrice}</td><td class="profit">${inventory.profit}</td>
                                  <td class="profitMargin">${inventory.profitMargin}</td></tr>`;
                    $("#inventory_tbl_body").append(record);
                });
            } else { console.error('Error: Expected JSON array, but received: ', data); }
        })
    .catch(error => { console.error('Error: ', error); });
};

// retrieve item by table click
$("#inventory_tbl_body, #customer_search_tbl_body").on("click", "tr", function() {
    inventory_row_index = $(this).index();
    let itemCode = $(this).find(".itemCode").text();
    let itemDesc = $(this).find(".itemDesc").text();
    base64InventoryPic = $(this).find(".itemPic").attr('src');
    let category = itemCode.charAt(0);
    let gender = itemCode.charAt(1);
    let type = itemCode.slice(2, 4);
    let supplierCode = $(this).find(".supplierCode").text();
    let supplierName = $(this).find(".supplierName").text();
    let buyPrice = $(this).find(".buyPrice").text();
    let sellPrice = $(this).find(".sellPrice").text();
    let profit = $(this).find(".profit").text();
    let profitMargin = $(this).find(".profitMargin").text();

    $("#item_code").val(itemCode);
    $("#item_desc").val(itemDesc);
    $('#preview_item_pic').attr('src', base64InventoryPic).css('display', 'block');
    $("#item_category_select").val(category);
    $("#item_gender_select").val(gender);
    $("#item_type_select").val(type);
    $("#item_supplier_select").val(supplierCode);
    $("#item_supplier_name").val(supplierName);
    $("#item_buy_price").val(buyPrice);
    $("#item_sell_price").val(sellPrice);
    $("#item_profit").val(profit);
    $("#item_profit_margin").val(profitMargin);
});