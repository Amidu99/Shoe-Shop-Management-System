import {AuthServiceUrl} from "../assets/js/urls.js";
import {emailPattern, passwordPattern} from "../assets/js/regex.js";
import {showError} from "../assets/js/notifications.js";

$(document).ready(function(){
    $('.signup-slider').slick({
        dots: true,
        arrows: false,
        autoplay: true,
        autoplaySpeed: 2000
    });

    $("img").height($(".main-box").height());
    $(".to-signin").on("click", function () {
        console.log("click in")
        $(this)
            .addClass("top-active-button")
            .siblings()
            .removeClass("top-active-button");
        $(".form-signup").slideUp(500);
        $(".form-signin").slideDown(500);
    });

    $(".to-signup").on("click", function () {
        $(this)
            .addClass("top-active-button")
            .siblings()
            .removeClass("top-active-button");
        $(".form-signin").slideUp(500);
        $(".form-signup").slideDown(500);
    });

    $(".to-signin-link").on("click", function () {
        $(".to-signin")
            .addClass("top-active-button")
            .siblings()
            .removeClass("top-active-button");
        $(".form-signup").slideUp(200);
        $(".form-signin").slideDown(200);
    });

    $(".to-signup-link").on("click", function () {
        $(".to-signup")
            .addClass("top-active-button")
            .siblings()
            .removeClass("top-active-button");
        $(".form-signin").slideUp(200);
        $(".form-signup").slideDown(200);
    });
});

// signIn
$("#signin_btn").on("click", async () => {
    let email = $("#signin_form_email").val();
    let password = $("#signin_form_password").val();
    if ( email && password ) {
        if (emailPattern.test(email)) {
            const signInObject = { email: email, password: password };
            const signInJSON = JSON.stringify(signInObject);
            const signinURL = new URL(`${AuthServiceUrl}/signIn`);

            try {
                const response = await fetch(signinURL, {
                    method: 'POST',
                    body: signInJSON,
                    headers: {
                        "Content-Type": "application/json"
                    }
                });

                if (response.ok) {
                    const token = await response.text();
                    // Parse the JSON string to get the token string
                    const parsedData = JSON.parse(token);
                    const tokenString = parsedData.token;
                    // Split the token string using the '|' delimiter
                    const tokens = tokenString.split('|');
                    // Trim any extra whitespace from the tokens
                    const firstToken = tokens[0].trim();
                    const secondToken = tokens[1].trim();
                    // Save the tokens in local storage
                    localStorage.setItem('AuthToken', firstToken);
                    localStorage.setItem('RefreshToken', secondToken);
                    localStorage.setItem('CurrentUser', email);
                    window.location.href = '../dashboard/dashboard.html';
                } else if (response.status===403) {Swal.fire({width: '215px', icon: "error", title: "Login failed", footer: '<b>Check your Email & Password!</b>', showConfirmButton: false, timer: 3000 });}
            } catch (error) { console.error('Error:', error); }
        } else { showError('Invalid email input!');}
    } else { showError('Fields can not be empty!');}
});

// signUp
$("#signup_btn").on("click", async () => {
    let email = $("#signup_form_email").val();
    let password = $("#signup_form_password").val();
    let re_password = $("#signup_form_re_password").val();
    if ( email && password && re_password ) {
        if (emailPattern.test(email)) {
            if (password===re_password) {
                if (passwordPattern.test(password)) {
                    const signUpObject = { email: email, password: password };
                    const signUpJSON = JSON.stringify(signUpObject);
                    const signupURL = new URL(`${AuthServiceUrl}/signUp`);

                    try {
                        const response = await fetch(signupURL, {
                            method: 'POST',
                            body: signUpJSON,
                            headers: {
                                "Content-Type": "application/json"
                            }
                        });

                        if (response.status === 200) {
                            const token = await response.text();
                            const parsedData = JSON.parse(token);
                            const tokenString = parsedData.token;
                            const tokens = tokenString.split('|');
                            const firstToken = tokens[0].trim();
                            const secondToken = tokens[1].trim();
                            localStorage.setItem('AuthToken', firstToken);
                            localStorage.setItem('RefreshToken', secondToken);
                            localStorage.setItem('CurrentUser', email);
                            window.location.href = '../dashboard/dashboard.html';
                        } else if (response.status===204) {Swal.fire({width: '240px', icon: "error", title: "Signup failed", footer: '<b>This email does not belong to an employee!</b>', showConfirmButton: false, timer: 3000 });
                        } else if (response.status===205) {Swal.fire({width: '240px', icon: "error", title: "Signup failed", footer: '<b>This email is already associated with a user!</b>', showConfirmButton: false, timer: 3000 });}
                    } catch (error) { console.error('Error:', error); }
                } else { Swal.fire({width: "400px", title: "Invalid Password pattern!", text: "Password must be 8+ characters, with uppercase, lowercase, digit, and special character (e.g., !@#$%^&*).", icon: "warning", iconColor: "red"});}
            } else { showError('Passwords did not match!');}
        } else { showError('Invalid email input!');}
    } else { showError('Fields can not be empty!');}
});