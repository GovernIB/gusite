/**
 * Created by tcerda on 23/01/2015.
 */
$(document).ready(function () {

    versio = $("select[name=versio]");
    acceso = $("select[name=acceso]");

    versio.change(function() {
        canvi();
    });

    acceso.change(function() {
        canvi()
    });

});

function canvi() {
    var v = versio.val();
    var a = acceso.val();
    if ((v == "IN" && a == "P")
        || (v == "v1" && a == "R")
        || (v == "v4" && a == "R")) {

        alert("No es possible guardar versió " + v + " i accés " + a);
        a = (v == "IN") ? "R" : "P";
        $("select[name=acceso] option").each(function() {
            if ($(this).val() == a) {
                $(this).attr("selected", true);
            } else {
                $(this).removeAttr("selected");
            }
        });
    }
};
