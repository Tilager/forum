$("#profile-form").submit(function () {
    let about = $("#about");
    let password = $("#password");
    let login = $("#login");
    let confirmPassword = $("#confirm-password");
    let logoInput = $("#logo-upload")

    if (confirmPassword.val() === "") {
        confirmPassword.addClass("formsFail");
    } else {
        confirmPassword.removeClass("formsFail");

        let authUUID = '';
        $.ajax({
            type: "get",
            url: "api/getAuthUserUUID",
            async: false,
            success: function(data) {
                authUUID = data;
            }
        });


        const formData = new FormData();
        formData.append("username", login.val());
        formData.append("password", password.val());
        formData.append("confirmPassword", confirmPassword.val());
        formData.append("about", about.val());
        formData.append("uuid", authUUID);
        if (logoInput.prop("files").length > 0) {
            formData.append("logo", logoInput.prop("files")[0]);
        }

        $.ajax({
            url: "/api/changeProfile",
            method: "post",
            cache: false,
            processData: false,
            contentType: false,
            data: formData,
            success: function () {
                location.reload();
            },
            statusCode: {
                400: function (data) {
                    let errors = data['responseJSON'];
                    console.log(errors);
                    errors.forEach(error => {
                        if (["confirmPasswordNotMatches"].indexOf(error) !== -1) {
                            confirmPassword.addClass("formsFail");
                        } else {
                            confirmPassword.removeClass("formsFail");
                        }

                        if (["passwordNotMatchPattern"].indexOf(error) !== -1) {
                            password.addClass("formsFail");
                        } else {
                            password.removeClass("formsFail");
                        }

                        if (["usernameNotMatchPattern", "usernameAlreadyExists"].indexOf(error) !== -1) {
                            login.addClass("formsFail");
                        } else {
                            login.removeClass("formsFail");
                        }
                    })
                },
                500: function (data) { console.log(data); }
            }
        })
    }
    return false;
})
