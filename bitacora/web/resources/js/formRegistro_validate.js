$(function () {
    $('#formRegistro').validate({
        rules: {
            nickname: {required: true, minlength: 4},
            nombre: {required: true, minlength: 3},
            password: {required: true, minlength: 9, nifCorrecto: true},
            password2: {number: true, min: 18, max: 99},
            mail: {
                required: true,
                email: true,
                remote: {
                    url: "compruebaEmailRemote",
                    type: "post",
                    data: {Email: function () {
                            return $("#mail").val();
                        }
                    }

                }
                //,regexp: /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/
            }
        },

        messages: {
            nickname: {required: "Nombre Cliente obligatorio"},
            nombre: {number: " edad numerica?", min: "debes ser mayor de edad"},
            mail: {email: "formato de correo no válido ( usuario@dominio.es )",
                remote: "Este correo ya esxixte en el Servidor"}
            //regexp: "Formato de Correo Erroneo..."}
        }
    });
});