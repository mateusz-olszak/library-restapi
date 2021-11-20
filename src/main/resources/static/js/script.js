$(document).ready(function () {
    $("#logoutLink").on("click", function (e) {
        e.preventDefault();
        document.logoutForm.submit();
    });
});

function checkEmailUnique(form) {
    url = "/readers/register/check_email";
    userEmail = $('#email').val();
    csrfVal = $("input[name='_csrf']").val();
    params = {email: userEmail,_csrf: csrfVal};

    $.post(url,params,function(response) {
        if (response == "OK") {
            form.submit();
        } else if (response == "Duplicated") {
            alert('This email is already taken');
        }
    });
    return false;
}

function clearFilter() {
    window.location.href = "/books";
}

function checkPasswordMatch(confirmPassword) {
    if (confirmPassword.value != $("#password").val()) {
        confirmPassword.setCustomValidity("Passwords do not match");
    } else {
        confirmPassword.setCustomValidity("");
    }
};