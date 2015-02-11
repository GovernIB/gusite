/**
 * Created by tcerda on 23/01/2015.
 */
$(document).ready(function () {

    uri = $("input[name=uri]");
    uri0 = $("#uri0");
    uri1 = $("#uri1");
    uri2 = $("#uri2");
    uri3 = $("#uri3");
    uri4 = $("#uri4");
    type = $("input[name=type]").val();

    uri.blur(function() {
        check(this.value, type, null)
    });
    uri0.blur(function() {
        check(this.value, type, "ca")
    });
    uri1.blur(function() {
        check(this.value, type, "es")
    });
    uri2.blur(function() {
        check(this.value, type, "en")
    });
    uri3.blur(function() {
        check(this.value, type, "de")
    });
    uri4.blur(function() {
        check(this.value, type, "fr")
    });

});

function check(uriCheck, typeCheck, idio) {
    $.get("/sacmicroback/ajaxCheckUri.do", { URI: uriCheck, type: typeCheck, idioma: idio }, preparaRespuesta);
}

function preparaRespuesta(data) {
    var respuesta = $(data).find("dato").text();
    if (respuesta == "existe") {
        alert("Ya existe esta URI!!!")
    } else if (respuesta == "ERROR") {
        alert("ERROR en la consulta!")
    }
}
