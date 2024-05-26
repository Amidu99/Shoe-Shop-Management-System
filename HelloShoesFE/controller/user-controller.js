import {UserServiceUrl} from "../assets/js/urls.js";
import {passwordPattern} from "../assets/js/regex.js";
import {showError, showSwalError} from "../assets/js/notifications.js";
let user_row_index = null;

// update user password
$("#user_btns>button[type='button']").eq(0).on("click", async () => {
    let employeeCode = $("#user_employee_code").val();
    let email = $("#user_email").val();
    let role = $("#stock_supplier_code").val();
    let oldPassword = $("#user_old_password").val();
    let newPassword = $("#user_new_password").val();

    if ( employeeCode && email && role && oldPassword && newPassword ) {
        if (passwordPattern.test(newPassword)) {
            let userObject = {
                userCode: oldPassword,
                employeeCode: employeeCode,
                email: email,
                role: role,
                password: newPassword
            };
            let userJSON = JSON.stringify(userObject);
            $.ajax({
                url: `${UserServiceUrl}/update`,
                type: "PUT",
                data: userJSON,
                headers: {"Content-Type": "application/json", "Authorization": "Bearer " + localStorage.getItem("AuthToken")},
                success: (res) => {
                    Swal.fire({width: '225px', position: 'center', icon: 'success', title: 'Updated!', showConfirmButton: false, timer: 2000});
                    $("#user_btns>button[type='button']").eq(2).click();
                },
                error: (err) => {
                    if (err.status === 403) { showSwalError('Forbidden','You do not have permission to perform this action!');}
                    else { showSwalError('Error', 'An error occurred while proceeding. Please try again later.');}
                }
            });
        } else {
            Swal.fire({
                width: "325px", title: "Invalid Password pattern!",
                text: "Password must be 8+ characters, with uppercase, lowercase, digit, and special character (e.g., !@#$%^&*).",
                icon: "warning", iconColor: "red"
            });
        }
    } else { showError('Fields can not be empty!');}
});

// delete user
$("#user_btns>button[type='button']").eq(1).on("click", async () => {
    let email = $("#user_email").val();
    if (email) {
        Swal.fire({
            width: '300px', title: 'Delete User', icon: 'question',
            text: "Are you sure you want to permanently remove this user?",  iconColor: '#FF7E00FF',
            showCancelButton: true, confirmButtonText: 'Yes, delete!'
        }).then((result) => {
            if (result.isConfirmed) {
                const url = new URL(`${UserServiceUrl}/delete`);
                $.ajax({
                    url: url,
                    type: "DELETE",
                    headers: { "email": email, "Authorization": "Bearer " + localStorage.getItem("AuthToken")},
                    success: (res) => {
                        Swal.fire({width: '225px', position: 'center', icon: 'success', title: 'Deleted!', showConfirmButton: false, timer: 2000});
                        $("#user_btns>button[type='button']").eq(2).click();
                    },
                    error: (err) => {
                        if (err.status === 403) { showSwalError('Forbidden','You do not have permission to perform this action!');}
                        else { showSwalError('Error', 'An error occurred while proceeding. Please try again later.');}
                    }
                });
            }
        });
    } else { showError('Email can not be empty!');}
});

// reset user form
$("#user_btns>button[type='button']").eq(2).on("click", async () => {
    $("#user_employee_code").val('');
    $("#user_email").val('');
    $("#user_role").val('');
    $("#user_old_password").val('');
    $("#user_new_password").val('');
    loadUserData();
});

// load all user details to the table
const loadUserData = () => {
    const getAllURL = new URL(`${UserServiceUrl}/getAll`);
    fetch(getAllURL, { method: 'GET', headers: {"Authorization": "Bearer " + localStorage.getItem("AuthToken")} })
        .then(response => {
            if (!response.ok) { throw new Error(`HTTP error! Status: ${response.status}`); }
            return response.json();
        })
        .then(data => {
            if (Array.isArray(data)) {
                $('#user_tbl_body').empty();
                data.forEach(user => {
                    let record = `<tr><td class="employeeCode">${user.employee.employeeCode}</td>
                                  <td class="email">${user.email}</td>
                                  <td class="role">${user.role}</td></tr>`;
                    $("#user_tbl_body").append(record);
                });
            } else { console.error('Error: Expected JSON array, but received: ', data); }
        })
        .catch(error => { console.error('Error: ', error); });
};

// retrieve user by table click
$("#user_tbl_body").on("click", "tr", function() {
    user_row_index = $(this).index();
    let employeeCode = $(this).find(".employeeCode").text();
    let email = $(this).find(".email").text();
    let role = $(this).find(".role").text();
    $("#user_employee_code").val(employeeCode);
    $("#user_email").val(email);
    $("#user_role").val(role);
});