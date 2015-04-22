/**
 * Created by tcerda on 23/01/2015.
 */
$(document).ready(function () {

    id = $("input[name=id]").val();
    id = (typeof id === 'undefined') ? 0 : id;
    uri = $("input[name=uri]");
    uri0 = $("#uri0");
    uri1 = $("#uri1");
    uri2 = $("#uri2");
    uri3 = $("#uri3");
    uri4 = $("#uri4");
    type = $("input[name=type]").val();
    titulo0 = $("#titulo0");
    titulo1 = $("#titulo1");
    titulo2 = $("#titulo2");
    titulo3 = $("#titulo3");
    titulo4 = $("#titulo4");
    tituloTema = $("#tituloTema");
    
    uri.blur(function() {
        url = this.value;
        check(this.value, type, null, id)
    });
    uri0.blur(function() {
        url = this.value;
        check(this.value, type, "ca", id)
    });
    uri1.blur(function() {
        url = this.value;
        check(this.value, type, "es", id)
    });
    uri2.blur(function() {
        url = this.value;
        check(this.value, type, "en", id)
    });
    uri3.blur(function() {
        url = this.value;
        check(this.value, type, "de", id)
    });
    uri4.blur(function() {
        url = this.value;
        check(this.value, type, "fr", id)
    });

    titulo0.blur(function() {
        current = uri0;
        uriTit = (current.val() == "") ? this.value : current.val();
        check(uriTit, type, "ca", id);
    });
    titulo1.blur(function() {
        current = uri1;
        uriTit = (current.val() == "") ? this.value : current.val();
        check(uriTit, type, "es", id);
    });
    titulo2.blur(function() {
        current = uri2;
        uriTit = (current.val() == "") ? this.value : current.val();
        check(uriTit, type, "en", id);
    });
    titulo3.blur(function() {
        current = uri3;
        uriTit = (current.val() == "") ? this.value : current.val();
        check(uriTit, type, "de", id);
    });
    titulo4.blur(function() {
        current = uri4;
        uriTit = (current.val() == "") ? this.value : current.val();
        check(uriTit, type, "fr", id);
    });

    tituloTema.blur(function() {
        current = uri;
        uriTit = (current.val() == "") ? this.value : current.val();
        check(uriTit, type, null, id);
    });
    
});

function check(uriCheck, typeCheck, idio, id) {
    $.get("/sacmicroback/ajaxCheckUri.do", { URI: uriCheck, type: typeCheck, idioma: idio, id: id }, preparaRespuesta);
}

function preparaRespuesta(data) {
    var respuesta = $(data).find("dato").text();
    if (respuesta == "error") {
        alert("ERROR en la consulta!");
    } else if (typeof current !== 'undefined') {
        current.valueOf().val(respuesta);
    } else if (respuesta != url) {
        alert("Ya existe esta URI!!!");
    }
    delete current ;
}
