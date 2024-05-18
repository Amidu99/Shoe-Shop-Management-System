let SupplierServiceUrl = 'http://localhost:8080/helloshoesbe/api/v1/supplier';

const supplierCodePattern = /^[S]-\d{4}$/;
let supplier_row_index = null;

// toastr error message
function showError(message) {
    toastr.error(message, 'Oops...', {
        "closeButton": true,
        "progressBar": true,
        "positionClass": "toast-top-center",
        "timeOut": "2500"
    });
}

// check availability of the supplierCode
async function isAvailableSupplierCode(supplierCode) {
    const url = new URL(`${SupplierServiceUrl}/get`);
    try {
        const response = await fetch(url, {method: 'GET', headers: {"supplierCode": supplierCode}});
        return response.status !== 204;
    } catch (error) {console.error('Error:', error);}
}

// save supplier
$("#supplier_btns>button[type='button']").eq(0).on("click", async () => {
    let supplierCode = $("#supplier_code").val();
    let supplierName = $("#supplier_name").val();
    let category = $("#supplier_category_select").val();
    let addLine1 = $("#supplier_building_no").val();
    let addLine2 = $("#supplier_lane").val();
    let addLine3 = $("#supplier_city").val();
    let addLine4 = $("#supplier_state").val();
    let addLine5 = $("#supplier_postal_code").val();
    let addLine6 = $("#supplier_origin_country").val();
    let contactNo1 = $("#supplier_contact_no1").val();
    let contactNo2 = $("#supplier_contact_no2").val();
    let email = $("#supplier_email").val();

    if ( supplierName && addLine1 && addLine2 && addLine3 && addLine4 && addLine5 && addLine6 && contactNo1 && contactNo2 && email) {
        if (category!=="0") {
            if (!(await isAvailableSupplierCode(supplierCode))) {
                if (namePattern.test(supplierName)) {
                    if (addressPattern.test(addLine1) && addressPattern.test(addLine2) && addressPattern.test(addLine3) && addressPattern.test(addLine4) && addressPattern.test(addLine6)) {
                        if (postalPattern.test(addLine5)){
                            if (contactPattern.test(contactNo1)) {
                                if (contactPattern.test(contactNo2)) {
                                    if (emailPattern.test(email)) {
                                        let supplierObject = {
                                            supplierCode: supplierCode,
                                            supplierName: supplierName,
                                            category: category,
                                            addLine1: addLine1,
                                            addLine2: addLine2,
                                            addLine3: addLine3,
                                            addLine4: addLine4,
                                            addLine5: addLine5,
                                            addLine6: addLine6,
                                            contactNo1: contactNo1,
                                            contactNo2: contactNo2,
                                            email: email,
                                        };
                                        let supplierJSON = JSON.stringify(supplierObject);
                                        $.ajax({
                                            url: `${SupplierServiceUrl}/save`,
                                            type: "POST",
                                            data: supplierJSON,
                                            headers: {"Content-Type": "application/json"},
                                            success: (res) => {
                                                Swal.fire({width: '225px', position: 'center', icon: 'success', title: 'Saved!', showConfirmButton: false, timer: 2000});
                                                $("#supplier_btns>button[type='button']").eq(3).click();
                                            },
                                            error: (err) => { console.error(err);}
                                        });
                                    } else { showError('Invalid email input!');}
                                } else { showError('Invalid mobile number input!');}
                            } else { showError('Invalid land number input!');}
                        } else { showError('Invalid postal code input!');}
                    } else { showError('Invalid address input!');}
                } else { showError('Invalid name input!');}
            } else { showError('This Supplier Code is already exist!');}
        } else { showError('Fields can not be empty!');}
    } else { showError('Fields can not be empty!');}
});

// update supplier
$("#supplier_btns>button[type='button']").eq(1).on("click", async () => {
    let supplierCode = $("#supplier_code").val();
    let supplierName = $("#supplier_name").val();
    let category = $("#supplier_category_select").val();
    let addLine1 = $("#supplier_building_no").val();
    let addLine2 = $("#supplier_lane").val();
    let addLine3 = $("#supplier_city").val();
    let addLine4 = $("#supplier_state").val();
    let addLine5 = $("#supplier_postal_code").val();
    let addLine6 = $("#supplier_origin_country").val();
    let contactNo1 = $("#supplier_contact_no1").val();
    let contactNo2 = $("#supplier_contact_no2").val();
    let email = $("#supplier_email").val();

    if ( supplierName && addLine1 && addLine2 && addLine3 && addLine4 && addLine5 && addLine6 && contactNo1 && contactNo2 && email) {
        if (category!=="0") {
            if ((await isAvailableSupplierCode(supplierCode))) {
                if (namePattern.test(supplierName)) {
                    if (addressPattern.test(addLine1) && addressPattern.test(addLine2) && addressPattern.test(addLine3) && addressPattern.test(addLine4) && addressPattern.test(addLine6)) {
                        if (postalPattern.test(addLine5)){
                            if (contactPattern.test(contactNo1)) {
                                if (contactPattern.test(contactNo2)) {
                                    if (emailPattern.test(email)) {
                                        let supplierObject = {
                                            supplierCode: supplierCode,
                                            supplierName: supplierName,
                                            category: category,
                                            addLine1: addLine1,
                                            addLine2: addLine2,
                                            addLine3: addLine3,
                                            addLine4: addLine4,
                                            addLine5: addLine5,
                                            addLine6: addLine6,
                                            contactNo1: contactNo1,
                                            contactNo2: contactNo2,
                                            email: email,
                                        };
                                        let supplierJSON = JSON.stringify(supplierObject);
                                        $.ajax({
                                            url: `${SupplierServiceUrl}/update`,
                                            type: "PUT",
                                            data: supplierJSON,
                                            headers: {"Content-Type": "application/json"},
                                            success: (res) => {
                                                Swal.fire({width: '225px', position: 'center', icon: 'success', title: 'Updated!', showConfirmButton: false, timer: 2000});
                                                $("#supplier_btns>button[type='button']").eq(3).click();
                                            },
                                            error: (err) => { console.error(err);}
                                        });
                                    } else { showError('Invalid email input!');}
                                } else { showError('Invalid mobile number input!');}
                            } else { showError('Invalid land number input!');}
                        } else { showError('Invalid postal code input!');}
                    } else { showError('Invalid address input!');}
                } else { showError('Invalid name input!');}
            } else { showError('This Supplier Code is not exist!');}
        } else { showError('Fields can not be empty!');}
    } else { showError('Fields can not be empty!');}
});

// delete supplier
$("#supplier_btns>button[type='button']").eq(2).on("click", async () => {
    let supplierCode = $("#supplier_code").val();
    if (supplierCode) {
        if (supplierCodePattern.test(supplierCode)) {
            if ((await isAvailableSupplierCode(supplierCode))) {
                Swal.fire({
                    width: '300px', title: 'Delete Supplier',
                    text: "Are you sure you want to permanently remove this supplier?",
                    icon: 'warning', showCancelButton: true, confirmButtonColor: '#3085d6',
                    cancelButtonColor: '#d33', confirmButtonText: 'Yes, delete!'
                }).then((result) => {
                    if (result.isConfirmed) {
                        const url = new URL(`${SupplierServiceUrl}/delete`);
                        $.ajax({
                            url: url,
                            type: "DELETE",
                            headers: { "supplierCode": supplierCode },
                            success: (res) => {
                                Swal.fire({width: '225px', position: 'center', icon: 'success', title: 'Deleted!', showConfirmButton: false, timer: 2000});
                                $("#supplier_btns>button[type='button']").eq(3).click();
                            },
                            error: (err) => { console.error(err)}
                        });
                    }
                });
            } else { showError('This Supplier Code is not exist!');}
        } else { showError('Invalid Supplier Code format!');}
    } else { showError('Supplier Code can not be empty!');}
});

// reset supplier form
$("#supplier_btns>button[type='button']").eq(3).on("click", async () => {
    loadSupplierData();
    $("#supplier_name").val("");
    $("#supplier_category_select")[0].selectedIndex = 0;
    $("#supplier_building_no").val("");
    $("#supplier_lane").val("");
    $("#supplier_city").val("");
    $("#supplier_state").val("");
    $("#supplier_postal_code").val("");
    $("#supplier_origin_country").val("");
    $("#supplier_contact_no1").val("");
    $("#supplier_contact_no2").val("");
    $("#supplier_email").val("");

    const getNextCodeURL = new URL(`${SupplierServiceUrl}/getNextCode`);
    try {
        const response = await fetch(getNextCodeURL, { method: 'GET', });
        if (response.ok) {
            const nextSupplierCode = await response.text();
            $("#supplier_code").val(nextSupplierCode);
        } else { console.error(`HTTP error! Status: ${response.status}`);}
    } catch (error) { console.error('Error:', error);}
});

// load all supplier details to the table
const loadSupplierData = () => {
    const getAllURL = new URL(`${SupplierServiceUrl}/getAll`);
    fetch(getAllURL, { method: 'GET', })
        .then(response => {
            if (!response.ok) { throw new Error(`HTTP error! Status: ${response.status}`); }
            return response.json();
        })
        .then(data => {
            if (Array.isArray(data)) {
                $('#supplier_tbl_body').empty();
                data.forEach(supplier => {
                    let record = `<tr><td class="supplierCode">${supplier.supplierCode}</td><td class="supplierName">${supplier.supplierName}</td>
                                  <td class="category">${supplier.category}</td>
                                  <td class="addressData">${supplier.addLine1} , ${supplier.addLine2} , ${supplier.addLine3} 
                                  , ${supplier.addLine4} , ${supplier.addLine5} , ${supplier.addLine6}</td>
                                  <td class="contactNo1">${supplier.contactNo1}</td><td class="contactNo2">${supplier.contactNo2}</td>
                                  <td class="email">${supplier.email}</td></tr>`;
                    $("#supplier_tbl_body").append(record);
                });
            } else { console.error('Error: Expected JSON array, but received: ', data); }
        })
    .catch(error => { console.error('Error: ', error); });
};

// retrieve supplier by table click
$("#supplier_tbl_body").on("click", "tr", function() {
    supplier_row_index = $(this).index();
    let supplierCode = $(this).find(".supplierCode").text();
    let supplierName = $(this).find(".supplierName").text();
    let category = $(this).find(".category").text();
    let supplierAddress = $(this).find(".addressData").text();
    let addLines = supplierAddress.split(' , ');
    let addLine1 = addLines[0];
    let addLine2 = addLines[1];
    let addLine3 = addLines[2];
    let addLine4 = addLines[3];
    let addLine5 = addLines[4];
    let addLine6 = addLines[5];
    let contactNo1 = $(this).find(".contactNo1").text();
    let contactNo2 = $(this).find(".contactNo2").text();
    let email = $(this).find(".email").text();

    $("#supplier_code").val(supplierCode);
    $("#supplier_name").val(supplierName);
    $("#supplier_category_select").val(category);
    $("#supplier_building_no").val(addLine1);
    $("#supplier_lane").val(addLine2);
    $("#supplier_city").val(addLine3);
    $("#supplier_state").val(addLine4);
    $("#supplier_postal_code").val(addLine5);
    $("#supplier_origin_country").val(addLine6);
    $("#supplier_contact_no1").val(contactNo1);
    $("#supplier_contact_no2").val(contactNo2);
    $("#supplier_email").val(email);
});