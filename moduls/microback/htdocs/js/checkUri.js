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
    idSite = $("input[name=idmicrosite]").val();
    titulo0 = $("#titulo0");
    titulo1 = $("#titulo1");
    titulo2 = $("#titulo2");
    titulo3 = $("#titulo3");
    titulo4 = $("#titulo4");
    tituloTema = $("#tituloTema");
    
    uri.blur(function() {
    	currentUri = uri;
        url = this.value;
        check(this.value, type, idSite, null, id);
    });
    uri0.blur(function() {
    	currentUri = uri0;
        url = this.value;
        check(this.value, type, idSite, "ca", id);
    });
    uri1.blur(function() {
    	currentUri = uri1;
        url = this.value;
        check(this.value, type, idSite, "es", id);
    });
    uri2.blur(function() {
    	currentUri = uri2;
        url = this.value;
        check(this.value, type, idSite, "en", id);
    });
    uri3.blur(function() {
    	currentUri = uri3;
        url = this.value;
        check(this.value, type, idSite, "de", id);
    });
    uri4.blur(function() {
    	currentUri = uri4;
        url = this.value;
        check(this.value, type, idSite, "fr", id);
    });

    titulo0.blur(function() {
        current = uri0;
        uriTit = (current.val() == "") ? this.value : current.val();
        check(uriTit, type, idSite, "ca", id);
    });
    titulo1.blur(function() {
        current = uri1;
        uriTit = (current.val() == "") ? this.value : current.val();
        check(uriTit, type, idSite, "es", id);
    });
    titulo2.blur(function() {
        current = uri2;
        uriTit = (current.val() == "") ? this.value : current.val();
        check(uriTit, type, idSite, "en", id);
    });
    titulo3.blur(function() {
        current = uri3;
        uriTit = (current.val() == "") ? this.value : current.val();
        check(uriTit, type, idSite, "de", id);
    });
    titulo4.blur(function() {
        current = uri4;
        uriTit = (current.val() == "") ? this.value : current.val();
        check(uriTit, type, idSite, "fr", id);
    });

    tituloTema.blur(function() {
        current = uri;
        uriTit = (current.val() == "") ? this.value : current.val();
        check(uriTit, type, idSite, null, id);
    });
    
});

function check(uriCheck, typeCheck, idSite, idio, id) {
    $.get("/sacmicroback/ajaxCheckUri.do", { URI: uriCheck, type: typeCheck, idioma: idio, site: idSite, id: id }, preparaRespuesta);
}

function preparaRespuesta(data) {
    var respuesta = $(data).find("dato").text();
    if (respuesta == "error") {
        alert("ERROR a la consulta!");
    } else if (typeof current !== 'undefined') {
        current.valueOf().val(respuesta);
    } else if (respuesta != url) {
        currentUri.valueOf().val(respuesta);
        alert("S'ha corregit la URI");
    }
    delete current ;
}
