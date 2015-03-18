$(document).ready(function () {

    $('#nombremicro').keypress(function (e) {
        if (e.which == 13) {
            $('#accion').val('nuevomicro');
        }
    });

});
