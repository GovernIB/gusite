$(document).ready(function () {

    $('#nombremicro').keypress(function (e) {
        if (e.which == 13) {
            if ($('#idmicro').val() !== "") {
                $('#accion').val('nuevomicro');
            } else {
                return false;
            }
        }
    });

});
