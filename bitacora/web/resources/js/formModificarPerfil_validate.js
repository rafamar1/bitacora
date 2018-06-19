$(function () {

    $.validator.addMethod('passwordMatch', function (value, element) {
        
        var password = $("#password").val();
        var confirmPassword = $("#password2").val();
       
        if (password !== confirmPassword) {
            return false;
        } else {
            return true;
        }
    }, "Your Passwords Must Match");

    $('#form-modif-usuario').validate({
        errorClass: 'error',
        validClass: 'success',
        rules: {
            fullName: {minlength: 4},
            password: {minlength: 6},
            password2: {minlength: 6, passwordMatch: true},
            mail: {email: true }
        },

        messages: {
            fullName: {minlength: "Indique un nombre de al menos 4 caracteres"},
            password: {minlength: "La contraseña debe tener al menos 6 caracteres"},
            password2: {minlength: "La contraseña debe tener al menos 6 caracteres",
                passwordMatch: "Las contraseñas deben coincidir"},
            mail: {email: "Introduzca un formato email(usuario@dominio.es)"}
        }
    });


});