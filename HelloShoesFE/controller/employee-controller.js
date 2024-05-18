let EmployeeServiceUrl = 'http://localhost:8080/helloshoesbe/api/v1/employee';

const employeeCodePattern = /^[E]-\d{4}$/;
let employee_row_index = null;
let base64EmployeePic = null;

// toastr error message
function showError(message) {
    toastr.error(message, 'Oops...', {
        "closeButton": true,
        "progressBar": true,
        "positionClass": "toast-top-center",
        "timeOut": "2500"
    });
}

// check availability of the employeeCode
async function isAvailableEmployeeCode(employeeCode) {
    const url = new URL(`${EmployeeServiceUrl}/get`);
    try {
        const response = await fetch(url, {method: 'GET', headers: {"employeeCode": employeeCode}});
        return response.status !== 204;
    } catch (error) {console.error('Error:', error);}
}

// preview selected image
function previewEmployeeImage() {
    var file = $('#employee_pic').prop('files')[0];
    var reader = new FileReader();

    reader.onload = function() {
        base64EmployeePic = reader.result;
        $('#preview_employee_pic').attr('src', base64EmployeePic).css('display', 'block');
    };

    if (file) { reader.readAsDataURL(file);}
    else { $('#preview_employee_pic').css('display', 'none'); }
}

$(document).ready(function() {
    $('#employee_pic').on('change', previewEmployeeImage);
});

// change role with designation_select
document.getElementById('designation_select').addEventListener('change', function() {
    let designation = this.value;
    let accessRoleInput = document.getElementById('access_role');
    if (designation === "0") { accessRoleInput.value = ""; }
    else if (designation === "Manager") { accessRoleInput.value = "ADMIN"; }
    else { accessRoleInput.value = "USER"; }
});

// save employee
$("#employee_btns>button[type='button']").eq(0).on("click", async () => {
    let employeeCode = $("#employee_code").val();
    let employeeName = $("#employee_name").val();
    let employeePicFile = $('#employee_pic').prop('files')[0];
    let employeePic;
    let gender = $("#employee_gender_select").val();
    let status = $("#employee_civil_status_select").val();
    let designation = $("#designation_select").val();
    let role = $("#access_role").val();
    let dob = $("#employee_dob").val();
    let joinDate = $("#employee_join_date").val();
    let branch = $("#attached_branch").val();
    let addLine1 = $("#employee_home_no").val();
    let addLine2 = $("#employee_lane").val();
    let addLine3 = $("#employee_city").val();
    let addLine4 = $("#employee_state").val();
    let addLine5 = $("#employee_postal_code").val();
    let contactNo = $("#employee_contact_no").val();
    let email = $("#employee_email").val();
    let guardian = $("#guardian_name").val();
    let guardianContactNo = $("#guardian_contact_no").val();

    if (employeeCode && employeeName && employeePicFile && role && dob && joinDate && branch && addLine1 && addLine2) {
        if (gender!=="0" && status!=="0" && designation!=="0" && addLine3 && addLine4 && addLine5 && contactNo && email && guardian && guardianContactNo){
            employeePic = base64EmployeePic;
            if (!(await isAvailableEmployeeCode(employeeCode))) {
                if (namePattern.test(employeeName) && namePattern.test(guardian)) {
                    if (addressPattern.test(addLine1) && addressPattern.test(addLine2) && addressPattern.test(addLine3) && addressPattern.test(addLine4)) {
                        if (postalPattern.test(addLine5)) {
                            if (contactPattern.test(contactNo) && contactPattern.test(guardianContactNo)) {
                                if (emailPattern.test(email)) {
                                    if (employeePic) {
                                        let employeeObject = {
                                            employeeCode: employeeCode,
                                            employeeName: employeeName,
                                            employeePic: employeePic,
                                            gender: gender,
                                            status: status,
                                            designation: designation,
                                            role: role,
                                            dob: dob,
                                            joinDate: joinDate,
                                            branch: branch,
                                            addLine1: addLine1,
                                            addLine2: addLine2,
                                            addLine3: addLine3,
                                            addLine4: addLine4,
                                            addLine5: addLine5,
                                            contactNo: contactNo,
                                            email: email,
                                            guardian: guardian,
                                            guardianContactNo: guardianContactNo
                                        };
                                        let employeeJSON = JSON.stringify(employeeObject);
                                        $.ajax({
                                            url: `${EmployeeServiceUrl}/save`,
                                            type: "POST",
                                            data: employeeJSON,
                                            headers: {"Content-Type": "application/json"},
                                            success: (res) => {
                                                Swal.fire({width: '225px', position: 'center', icon: 'success', title: 'Saved!', showConfirmButton: false, timer: 2000});
                                                $("#employee_btns>button[type='button']").eq(3).click();
                                            },
                                            error: (err) => { console.error(err);}
                                        });
                                    } else { showError('Invalid employee pic!');}
                                } else { showError('Invalid email input!');}
                            } else { showError('Invalid contact number input!');}
                        } else { showError('Invalid postal code input!');}
                    } else { showError('Invalid address input!');}
                } else { showError('Invalid name input!');}
            } else { showError('This Employee Code is already exist!');}
        } else {showError('Fields can not be empty!');}
    } else { showError('Fields can not be empty!');}
});

// update employee
$("#employee_btns>button[type='button']").eq(1).on("click", async () => {
    let employeeCode = $("#employee_code").val();
    let employeeName = $("#employee_name").val();
    let employeePic;
    let gender = $("#employee_gender_select").val();
    let status = $("#employee_civil_status_select").val();
    let designation = $("#designation_select").val();
    let role = $("#access_role").val();
    let dob = $("#employee_dob").val();
    let joinDate = $("#employee_join_date").val();
    let branch = $("#attached_branch").val();
    let addLine1 = $("#employee_home_no").val();
    let addLine2 = $("#employee_lane").val();
    let addLine3 = $("#employee_city").val();
    let addLine4 = $("#employee_state").val();
    let addLine5 = $("#employee_postal_code").val();
    let contactNo = $("#employee_contact_no").val();
    let email = $("#employee_email").val();
    let guardian = $("#guardian_name").val();
    let guardianContactNo = $("#guardian_contact_no").val();

    if (employeeCode && employeeName && role && dob && joinDate && branch && addLine1 && addLine2) {
        if (gender!=="0" && status!=="0" && designation!=="0" && addLine3 && addLine4 && addLine5 && contactNo && email && guardian && guardianContactNo){
            employeePic = base64EmployeePic;
            if ((await isAvailableEmployeeCode(employeeCode))) {
                if (namePattern.test(employeeName) && namePattern.test(guardian)) {
                    if (addressPattern.test(addLine1) && addressPattern.test(addLine2) && addressPattern.test(addLine3) && addressPattern.test(addLine4)) {
                        if (postalPattern.test(addLine5)) {
                            if (contactPattern.test(contactNo) && contactPattern.test(guardianContactNo)) {
                                if (emailPattern.test(email)) {
                                    if (employeePic) {
                                        let employeeObject = {
                                            employeeCode: employeeCode,
                                            employeeName: employeeName,
                                            employeePic: employeePic,
                                            gender: gender,
                                            status: status,
                                            designation: designation,
                                            role: role,
                                            dob: dob,
                                            joinDate: joinDate,
                                            branch: branch,
                                            addLine1: addLine1,
                                            addLine2: addLine2,
                                            addLine3: addLine3,
                                            addLine4: addLine4,
                                            addLine5: addLine5,
                                            contactNo: contactNo,
                                            email: email,
                                            guardian: guardian,
                                            guardianContactNo: guardianContactNo
                                        };
                                        let employeeJSON = JSON.stringify(employeeObject);
                                        $.ajax({
                                            url: `${EmployeeServiceUrl}/update`,
                                            type: "PUT",
                                            data: employeeJSON,
                                            headers: {"Content-Type": "application/json"},
                                            success: (res) => {
                                                Swal.fire({width: '225px', position: 'center', icon: 'success', title: 'Updated!', showConfirmButton: false, timer: 2000});
                                                $("#employee_btns>button[type='button']").eq(3).click();
                                            },
                                            error: (err) => { console.error(err);}
                                        });
                                    } else { showError('Invalid employee pic!');}
                                } else { showError('Invalid email input!');}
                            } else { showError('Invalid contact number input!');}
                        } else { showError('Invalid postal code input!');}
                    } else { showError('Invalid address input!');}
                } else { showError('Invalid name input!');}
            } else { showError('This Employee Code is not exist!');}
        } else {showError('Fields can not be empty!');}
    } else { showError('Fields can not be empty!');}
});

// delete employee
$("#employee_btns>button[type='button']").eq(2).on("click", async () => {
    let employeeCode = $("#employee_code").val();
    if (employeeCode) {
        if ((await isAvailableEmployeeCode(employeeCode))) {
            Swal.fire({
                width: '300px', title: 'Delete Employee',
                text: "Are you sure you want to permanently remove this employee?",
                icon: 'warning', showCancelButton: true, confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33', confirmButtonText: 'Yes, delete!'
            }).then((result) => {
                if (result.isConfirmed) {
                    const url = new URL(`${EmployeeServiceUrl}/delete`);
                    $.ajax({
                        url: url,
                        type: "DELETE",
                        headers: { "employeeCode": employeeCode },
                        success: (res) => {
                            Swal.fire({width: '225px', position: 'center', icon: 'success', title: 'Deleted!', showConfirmButton: false, timer: 2000});
                            $("#employee_btns>button[type='button']").eq(3).click();
                        },
                        error: (err) => { console.error(err)}
                    });
                }
            });
        } else { showError('This Employee Code is not exist!');}
    } else { showError('Employee Code can not be empty!');}
});

// reset employee form
$("#employee_btns>button[type='button']").eq(3).on("click", async () => {
    base64EmployeePic = null;
    $('#preview_employee_pic').css('display', 'none');
    loadEmployeeData();
    $("#employee_name").val("");
    $("#employee_pic").val("");
    $("#employee_gender_select")[0].selectedIndex = 0;
    $("#employee_civil_status_select")[0].selectedIndex = 0;
    $("#designation_select")[0].selectedIndex = 0;
    $("#access_role").val("");
    $("#employee_dob").val("");
    $("#employee_join_date").val("");
    $("#attached_branch").val("");
    $("#employee_home_no").val("");
    $("#employee_lane").val("");
    $("#employee_city").val("");
    $("#employee_state").val("");
    $("#employee_postal_code").val("");
    $("#employee_contact_no").val("");
    $("#employee_email").val("");
    $("#guardian_name").val("");
    $("#guardian_contact_no").val("");

    const getNextCodeURL = new URL(`${EmployeeServiceUrl}/getNextCode`);
    try {
        const response = await fetch(getNextCodeURL, { method: 'GET', });
        if (response.ok) {
            const nextEmployeeCode = await response.text();
            $("#employee_code").val(nextEmployeeCode);
        } else { console.error(`HTTP error! Status: ${response.status}`);}
    } catch (error) { console.error('Error:', error);}
});

// load all employees to the table
const loadEmployeeData = () => {
    const getAllURL = new URL(`${EmployeeServiceUrl}/getAll`);
    fetch(getAllURL, { method: 'GET', })
        .then(response => {
            if (!response.ok) { throw new Error(`HTTP error! Status: ${response.status}`); }
            return response.json();
        })
        .then(data => {
            if (Array.isArray(data)) {
                $('#employee_tbl_body').empty();
                data.forEach(employee => {
                    let record = `<tr><td class="employeeCode">${employee.employeeCode}</td><td class="employeeName">${employee.employeeName}</td>
                                  <td><img class="employeePic" src="${employee.employeePic}" width="100px" height="100px"></td>
                                  <td class="gender">${employee.gender}</td><td class="status">${employee.status}</td>
                                  <td class="designation">${employee.designation}</td><td class="role">${employee.role}</td>
                                  <td class="dob">${employee.dob}</td><td class="joinDate">${employee.joinDate}</td><td class="branch">${employee.branch}</td>
                                  <td class="addressData">${employee.addLine1} , ${employee.addLine2} , ${employee.addLine3} , ${employee.addLine4} , ${employee.addLine5}</td>
                                  <td class="contactNo">${employee.contactNo}</td><td class="email">${employee.email}</td>
                                  <td class="guardian">${employee.guardian}</td><td class="guardianContactNo">${employee.guardianContactNo}</td></tr>`;
                    $("#employee_tbl_body").append(record);
                });
            } else { console.error('Error: Expected JSON array, but received: ', data); }
        })
    .catch(error => { console.error('Error: ', error); });
};

// retrieve employee by table click
$("#employee_tbl_body").on("click", "tr", function() {
    employee_row_index = $(this).index();
    let employeeCode = $(this).find(".employeeCode").text();
    let employeeName = $(this).find(".employeeName").text();
    base64EmployeePic = $(this).find(".employeePic").attr('src');
    let gender = $(this).find(".gender").text();
    let status = $(this).find(".status").text();
    let designation =$(this).find(".designation").text();
    let role = $(this).find(".role").text();
    let toFormatDob = $(this).find(".dob").text();
    let dateObject1 = new Date(toFormatDob);
    let dob = dateObject1.toISOString().split('T')[0];
    let toFormatJoinDate = $(this).find(".joinDate").text();
    let dateObject2 = new Date(toFormatJoinDate);
    let joinDate = dateObject2.toISOString().split('T')[0];
    let branch = $(this).find(".branch").text();
    let employeeAddress = $(this).find(".addressData").text();
    let addLines = employeeAddress.split(' , ');
    let addLine1 = addLines[0];
    let addLine2 = addLines[1];
    let addLine3 = addLines[2];
    let addLine4 = addLines[3];
    let addLine5 = addLines[4];
    let contactNo = $(this).find(".contactNo").text();
    let email = $(this).find(".email").text();
    let guardian = $(this).find(".guardian").text();
    let guardianContactNo = $(this).find(".guardianContactNo").text();

    $("#employee_code").val(employeeCode);
    $("#employee_name").val(employeeName);
    $('#preview_employee_pic').attr('src', base64EmployeePic).css('display', 'block');
    $("#employee_gender_select").val(gender);
    $("#employee_civil_status_select").val(status);
    $("#designation_select").val(designation);
    $("#access_role").val(role);
    $("#employee_dob").val(dob);
    $("#employee_join_date").val(joinDate);
    $("#attached_branch").val(branch);
    $("#employee_home_no").val(addLine1);
    $("#employee_lane").val(addLine2);
    $("#employee_city").val(addLine3);
    $("#employee_state").val(addLine4);
    $("#employee_postal_code").val(addLine5);
    $("#employee_contact_no").val(contactNo);
    $("#employee_email").val(email);
    $("#guardian_name").val(guardian);
    $("#guardian_contact_no").val(guardianContactNo);
});