
//Función que controla si hay contenido lateral
$(window).on('load', function() {

    var dataAside = document.getElementById('imc--complementari');
    var contenido = document.getElementById('imc--contenidor');
    console.log("Accedo al load del window");
    console.log(dataAside.innerHTML.trim()=="");

    if(dataAside.innerHTML.trim()=="") {
        contenido.setAttribute("data-aside", "n");
    } else {
        contenido.setAttribute("data-aside", "s")
    }

    var dataCampanyaEnlace = document.getElementById('enllasDestPeu');
    if(dataCampanyaEnlace.innerHTML.trim()=="") {
        dataCampanyaEnlace.className = "invisible";
    }
});

$(function(){


    imc_body
        .appHeader();

});

$.fn.appHeader = function(options) {
    var settings = $.extend({
        contenidor : false
    }, options);
    this.each(function() {
        var element = $(this)
            ,inicia = function(e) {
            var capcalera = element.find("#header"),
                text = capcalera.text().trim();
            console.log(capcalera.text().trim());
            if(text == "") {
                capcalera.remove();
            }
        }
        inicia();
    });

    return this;
}







