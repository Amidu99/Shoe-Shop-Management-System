let CustomerServiceUrl = 'http://localhost:8080/helloshoesbe/api/v1/customer';

const customerCodePattern = /^[C]-\d{4}$/;
const namePattern = /^[A-Za-z\s]{3,}$/;
const addressPattern = /^[a-zA-Z0-9 ',.-]{3,}$/;
const postalPattern = /^\b\d{5}\b$/;
const contactPattern = /^(?:[0-9] ?){6,14}[0-9]$/;
const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
let customer_row_index = null;

// toastr error message
function showError(message) {
    toastr.error(message, 'Oops...', {
        "closeButton": true,
        "progressBar": true,
        "positionClass": "toast-top-center",
        "timeOut": "2500"
    });
}

// check availability of the customerCode
async function isAvailableCustomerCode(customerCode) {
    const url = new URL(`${CustomerServiceUrl}/get`);
    try {
        const response = await fetch(url, {method: 'GET', headers: {"customerCode": customerCode}});
        return response.status !== 204;
    } catch (error) {console.error('Error:', error);}
}

// save customer
$("#customer_btns>button[type='button']").eq(0).on("click", async () => {
    let customerCode = $("#customer_code").val();
    let customerName = $("#customer_name").val();
    let gender = $("#gender_select").val();
    let joinDate = $("#join_date").val();
    let totalPoints = parseInt($("#total_points").val());
    let level;
    let dob = $("#dob").val();
    let addLine1 = $("#building_no").val();
    let addLine2 = $("#lane").val();
    let addLine3 = $("#city").val();
    let addLine4 = $("#state").val();
    let addLine5 = $("#postal_code").val();
    let contactNo = $("#contact_no").val();
    let email = $("#email").val();
    let rpDateTime = $("#rp_date_time").val();
    if ( customerName && joinDate && dob && addLine1 && addLine2 && addLine3 && addLine4 && addLine5 && contactNo && email) {
        if (gender!=="0") {
            if (!(await isAvailableCustomerCode(customerCode))) {
                if (namePattern.test(customerName)) {
                    if (totalPoints >= 0) {
                        if (addressPattern.test(addLine1) && addressPattern.test(addLine2) && addressPattern.test(addLine3) && addressPattern.test(addLine4)) {
                            if (postalPattern.test(addLine5)) {
                                if (contactPattern.test(contactNo)) {
                                    if (emailPattern.test(email)) {
                                        if (totalPoints < 50) { level = 'NEW'; }
                                        if (totalPoints > 49 && totalPoints < 100) { level = 'BRONZE'; }
                                        if (totalPoints > 99 && totalPoints < 200) { level = 'SILVER'; }
                                        if (totalPoints > 200) { level = 'GOLD'; }
                                        $("#customer_level").val(level);
                                        let customerObject = {
                                            customerCode: customerCode,
                                            customerName: customerName,
                                            gender: gender,
                                            joinDate: joinDate,
                                            level: level,
                                            totalPoints: totalPoints,
                                            dob: dob,
                                            addLine1: addLine1,
                                            addLine2: addLine2,
                                            addLine3: addLine3,
                                            addLine4: addLine4,
                                            addLine5: addLine5,
                                            contactNo: contactNo,
                                            email: email,
                                            rpDateTime : rpDateTime
                                        };
                                        let customerJSON = JSON.stringify(customerObject);
                                        $.ajax({
                                            url: `${CustomerServiceUrl}/save`,
                                            type: "POST",
                                            data: customerJSON,
                                            headers: {"Content-Type": "application/json"},
                                            success: (res) => {
                                                Swal.fire({width: '225px', position: 'center', icon: 'success', title: 'Saved!', showConfirmButton: false, timer: 2000});
                                                $("#customer_btns>button[type='button']").eq(3).click();
                                            },
                                            error: (err) => { console.error(err);}
                                        });
                                    } else { showError('Invalid email input!');}
                                } else { showError('Invalid contact number input!');}
                            } else { showError('Invalid postal code input!');}
                        } else { showError('Invalid address input!');}
                    } else { showError('Invalid points input!');}
                } else { showError('Invalid name input!');}
            } else { showError('This Customer Code is already exist!');}
        } else { showError('Select Gender!');}
    } else { showError('Fields can not be empty!');}
});

// update customer
$("#customer_btns>button[type='button']").eq(1).on("click", async () => {
    let customerCode = $("#customer_code").val();
    let customerName = $("#customer_name").val();
    let gender = $("#gender_select").val();
    let joinDate = $("#join_date").val();
    let totalPoints = parseInt($("#total_points").val());
    let level;
    let dob = $("#dob").val();
    let addLine1 = $("#building_no").val();
    let addLine2 = $("#lane").val();
    let addLine3 = $("#city").val();
    let addLine4 = $("#state").val();
    let addLine5 = $("#postal_code").val();
    let contactNo = $("#contact_no").val();
    let email = $("#email").val();
    let rpDateTime = $("#rp_date_time").val();
    if ( customerName && joinDate && dob && addLine1 && addLine2 && addLine3 && addLine4 && addLine5 && contactNo && email) {
        if (gender!=="0") {
            if ((await isAvailableCustomerCode(customerCode))) {
                if (namePattern.test(customerName)) {
                    if (totalPoints >= 0) {
                        if (addressPattern.test(addLine1) && addressPattern.test(addLine2) && addressPattern.test(addLine3) && addressPattern.test(addLine4)) {
                            if (postalPattern.test(addLine5)) {
                                if (contactPattern.test(contactNo)) {
                                    if (emailPattern.test(email)) {
                                        if (totalPoints < 50) { level = 'NEW'; }
                                        if (totalPoints > 49 && totalPoints < 100) { level = 'BRONZE'; }
                                        if (totalPoints > 99 && totalPoints < 200) { level = 'SILVER'; }
                                        if (totalPoints > 200) { level = 'GOLD'; }
                                        $("#customer_level").val(level);
                                        let customerObject = {
                                            customerCode: customerCode,
                                            customerName: customerName,
                                            gender: gender,
                                            joinDate: joinDate,
                                            level: level,
                                            totalPoints: totalPoints,
                                            dob: dob,
                                            addLine1: addLine1,
                                            addLine2: addLine2,
                                            addLine3: addLine3,
                                            addLine4: addLine4,
                                            addLine5: addLine5,
                                            contactNo: contactNo,
                                            email: email,
                                            rpDateTime : rpDateTime
                                        };
                                        let customerJSON = JSON.stringify(customerObject);
                                        $.ajax({
                                            url: `${CustomerServiceUrl}/update`,
                                            type: "PUT",
                                            data: customerJSON,
                                            headers: {"Content-Type": "application/json"},
                                            success: (res) => {
                                                Swal.fire({width: '225px', position: 'center', icon: 'success', title: 'Updated!', showConfirmButton: false, timer: 2000});
                                                $("#customer_btns>button[type='button']").eq(3).click();
                                            },
                                            error: (err) => { console.error(err);}
                                        });
                                    } else { showError('Invalid email input!');}
                                } else { showError('Invalid contact number input!');}
                            } else { showError('Invalid postal code input!');}
                        } else { showError('Invalid address input!');}
                    } else { showError('Invalid points input!');}
                } else { showError('Invalid name input!');}
            } else { showError('This Customer Code is not exist!');}
        } else { showError('Select Gender!');}
    } else { showError('Fields can not be empty!');}
});

// delete customer
$("#customer_btns>button[type='button']").eq(2).on("click", async () => {
    let customerCode = $("#customer_code").val();
    if (customerCode) {
        if (customerCodePattern.test(customerCode)) {
            if ((await isAvailableCustomerCode(customerCode))) {
                Swal.fire({
                    width: '300px', title: 'Delete Customer',
                    text: "Are you sure you want to permanently remove this customer?",
                    icon: 'warning', showCancelButton: true, confirmButtonColor: '#3085d6',
                    cancelButtonColor: '#d33', confirmButtonText: 'Yes, delete!'
                }).then((result) => {
                    if (result.isConfirmed) {
                        const url = new URL(`${CustomerServiceUrl}/delete`);
                        $.ajax({
                            url: url,
                            type: "DELETE",
                            headers: { "customerCode": customerCode },
                            success: (res) => {
                                Swal.fire({width: '225px', position: 'center', icon: 'success', title: 'Deleted!', showConfirmButton: false, timer: 2000});
                                $("#customer_btns>button[type='button']").eq(3).click();
                            },
                            error: (err) => { console.error(err)}
                        });
                    }
                });
            } else { showError('This Customer Code is not exist!');}
        } else { showError('Invalid Customer Code format!');}
    } else { showError('Customer Code can not be empty!');}
});

// reset customer form
$("#customer_btns>button[type='button']").eq(3).on("click", async () => {
    loadCustomerData();
    $("#customer_name").val("");
    $("#gender_select")[0].selectedIndex = 0;
    $("#join_date").val("");
    $("#total_points").val("");
    $("#customer_level").val("");
    $("#dob").val("");
    $("#building_no").val("");
    $("#lane").val("");
    $("#city").val("");
    $("#state").val("");
    $("#postal_code").val("");
    $("#contact_no").val("");
    $("#email").val("");
    $("#rp_date_time").val("");

    const getNextCodeURL = new URL(`${CustomerServiceUrl}/getNextCode`);
    try {
        const response = await fetch(getNextCodeURL, { method: 'GET', });
        if (response.ok) {
            const nextCustomerCode = await response.text();
            $("#customer_code").val(nextCustomerCode);
        } else { console.error(`HTTP error! Status: ${response.status}`);}
    } catch (error) { console.error('Error:', error);}
});

// load all customer details to the table
const loadCustomerData = () => {
    const getAllURL = new URL(`${CustomerServiceUrl}/getAll`);
    fetch(getAllURL, { method: 'GET', })
        .then(response => {
            if (!response.ok) { throw new Error(`HTTP error! Status: ${response.status}`); }
            return response.json();
        })
        .then(data => {
            if (Array.isArray(data)) {
                $('#customer_tbl_body').empty();
                data.forEach(customer => {
                    let record = `<tr><td class="customerCode">${customer.customerCode}</td><td class="customerName">${customer.customerName}</td>
                                  <td class="gender">${customer.gender}</td><td class="joinDate">${customer.joinDate}</td>
                                  <td class="level">${customer.level}</td><td class="totalPoints">${customer.totalPoints}</td><td class="dob">${customer.dob}</td>
                                  <td class="addressData">${customer.addLine1} , ${customer.addLine2} , ${customer.addLine3} , ${customer.addLine4} , ${customer.addLine5}</td>
                                  <td class="contactNo">${customer.contactNo}</td><td class="email">${customer.email}</td><td class="rpDateTime">${customer.rpDateTime}</td></tr>`;
                    $("#customer_tbl_body").append(record);
                });
            } else { console.error('Error: Expected JSON array, but received: ', data); }
        })
    .catch(error => { console.error('Error: ', error); });
};

// retrieve customer by table click
$("#customer_tbl_body").on("click", "tr", function() {
    customer_row_index = $(this).index();
    let customerCode = $(this).find(".customerCode").text();
    let customerName = $(this).find(".customerName").text();
    let gender = $(this).find(".gender").text();
    let toFormatJoinDate = $(this).find(".joinDate").text();
    let dateObject1 = new Date(toFormatJoinDate);
    let joinDate = dateObject1.toISOString().split('T')[0];
    let totalPoints = $(this).find(".totalPoints").text();
    let level = $(this).find(".level").text();
    let toFormatDob = $(this).find(".dob").text();
    let dateObject2 = new Date(toFormatDob);
    let dob = dateObject2.toISOString().split('T')[0];
    let customerAddress = $(this).find(".addressData").text();
    let addLines = customerAddress.split(' , ');
    let addLine1 = addLines[0];
    let addLine2 = addLines[1];
    let addLine3 = addLines[2];
    let addLine4 = addLines[3];
    let addLine5 = addLines[4];
    let contactNo = $(this).find(".contactNo").text();
    let email = $(this).find(".email").text();
    let toFormatRpDateTime = $(this).find(".rpDateTime").text().trim();

    $("#customer_code").val(customerCode);
    $("#customer_name").val(customerName);
    $("#gender_select").val(gender);
    $("#join_date").val(joinDate);
    $("#total_points").val(totalPoints);
    $("#customer_level").val(level);
    $("#dob").val(dob);
    $("#building_no").val(addLine1);
    $("#lane").val(addLine2);
    $("#city").val(addLine3);
    $("#state").val(addLine4);
    $("#postal_code").val(addLine5);
    $("#contact_no").val(contactNo);
    $("#email").val(email);

    if (toFormatRpDateTime !== "") {
        let dateObject = new Date(toFormatRpDateTime);
        if (!isNaN(dateObject.getTime())) { // Check if dateObject is a valid Date object
            let rpDateTime = dateObject.toISOString().slice(0, 16);
            $("#rp_date_time").val(rpDateTime);
        } else { $("#rp_date_time").val("");}
    } else { $("#rp_date_time").val("");}
});