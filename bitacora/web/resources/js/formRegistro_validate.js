$(function () {

    $.validator.addMethod('passwordMatch', function (value, element) {
        // The two password inputs
        var password = $("#password").val();
        var confirmPassword = $("#password2").val();
        // Check for equality with the password inputs
        if (password !== confirmPassword) {
            return false;
        } else {
            return true;
        }
    }, "Your Passwords Must Match");

    $('#formRegistro').validate({
        errorClass: 'error',
        validClass: 'success',
        rules: {
            nickname: {required: true, minlength: 4},
            fullName: {required: true, minlength: 4},
            password: {required: true, minlength: 6},
            password2: {required: true, minlength: 6, passwordMatch: true},
            mail: {
                required: true,
                email: true
                        /*,remote: {
                         url: "compruebaMailRemote",
                         type: "post",
                         data: {mail: function () {
                         return $("#mail").val();
                         }
                         }
                         
                         }*/
            }
        },

        messages: {
            nickname: {required: "El nombre de usuario es obligatorio",
                remote: "Ese nombre de usuario no está disponible",
                minlength: "Indique un usuario de al menos 4 caracteres"},
            fullName: {required: "El campo nombre completo es obligatorio",
                minlength: "Indique un nombre de al menos 4 caracteres"},
            password: {required: "El campo contraseña es obligatorio",
                minlength: "La contraseña debe tener al menos 6 caracteres"},
            password2: {required: "Repita la contraseña",
                minlength: "La contraseña debe tener al menos 6 caracteres",
                passwordMatch: "Las contraseñas deben coincidir"},
            mail: {required: "El campo email es obligatorio",
                email: "El formato de correo introducido no es válido (usuario@dominio.es)",
                remote: "El correo indicado ya está en uso, utilice uno diferente"
            }
        }
    });


});