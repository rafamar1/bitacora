$(function () {
    var button = document.querySelector('.btn');
    var form = document.querySelector('.form');

    button.addEventListener('click', function () {
        form.classList.add('form-noCompleto');
    });
});
