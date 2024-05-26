// toastr error message
export function showError(message) {
    toastr.error(message, 'Oops...', {
        "closeButton": true,
        "progressBar": true,
        "positionClass": "toast-top-center",
        "timeOut": "2500"
    });
}

// SWAL Error message
export function showSwalError(title, text) {
    Swal.fire({
        width: '325px',
        position: 'center',
        icon: 'error',
        iconColor: 'red',
        title: title,
        text: text,
        showConfirmButton: true
    });
}