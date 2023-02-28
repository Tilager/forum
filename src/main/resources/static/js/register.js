$("#register-form").submit(function() {
    let username = $("#username");
    let password = $("#password");
    let confirmPassword = $("#confirm_pass");

    $.ajax({
        url: "/api/register",
        method: "post",
        contentType: "application/json",
        data: JSON.stringify({
            "username": username.val(),
            "password": password.val(),
            "confirmPassword": confirmPassword.val()
        }),
        success: function(data) {
            window.location.href = '/login'
        },
        statusCode: {
            400: function (data) {
                let errors = data['responseJSON'];
                errors.forEach(function (item) {
                    if (['passwordNotMatchPattern', 'passwordNotConfirm'].indexOf(item) !== -1) {
                        password.addClass('formsFail');
                    } else {
                        password.removeClass('formsFail');
                    }

                    if (['usernameNotMatchPattern'].indexOf(item) !== -1) {
                        username.addClass('formsFail');
                    } else if (['usernameAlreadyExists'].indexOf(item) !== -1) {
                        username.addClass('formsFail');
                        alert("Такой пользователь уже существует!");
                    } else {
                        username.removeClass('formsFail');
                    }

                    if (['passwordNotConfirm'].indexOf(item) !== -1) {
                        confirmPassword.addClass('formsFail');
                    } else {
                        confirmPassword.removeClass('formsFail');
                    }

                })
            }
        }
    })

    return false;
})