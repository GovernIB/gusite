/* menu horitzontal */

var imc_finestra,
    imc_menu_navegacio,
    imc_menu_h,
    imc_molla_pa,
    imc_info;


// onReady

$(function(){
    
    imc_finestra = $(window);
    imc_menu_navegacio = $("#imc-menu-navegacio");
    imc_menu_h = $("#imc-menu-h");

    imc_molla_pa = $("#mollaPa");
    imc_info = $("#info");

    imc_menu_h
        .appMenuH();

    var resizeAppMenuH;
    $(window)
        .on('resize', function(e) {
            clearTimeout(resizeAppMenuH);
            resizeAppMenuH = setTimeout(function() {

                imc_menu_h
                    .appMenuH();

            }, 150);
        });

});


// appMenuH

$.fn.appMenuH = function(opcions) {
    var settings = $.extend({
        tipus: false,
        fila: false,
        complet: false
    }, opcions);
    this.each(function(){
        var elm = $(this),
            menu_tipus = settings.tipus,
            menu_fila = settings.fila,
            menu_complet = settings.complet,
            menu_W = false,
            menu_L = false,
            menu_max_posicio = false,
            esMobil = false,
            inicia = function() {

                imc_menu_navegacio
                    .find(".imc-bt-menu-obri, .imc-bt-menu-tanca")
                        .remove()
                        .end()
                    .find(".imc--obert")
                        .removeClass("imc--obert")
                        .end()
                    .removeClass("imc--horitzontal")
                    .removeClass("imc--1-fila");

                imc_menu_h
                    .find("ul:first")
                        .removeAttr("style")
                        .end()
                    .removeClass("imc-amb-botonera");

                imc_molla_pa
                    .removeClass("imc--menu-horizontal");

                imc_info
                    .removeClass("imc--menu-horizontal");

                if (imc_menu_h.css("position") === "fixed") {

                    elm
                        .appMenuMobil();

                    esMobil = true;

                }

                menu_fila = imc_menu_navegacio.attr("data-fila");
                menu_tipus = imc_menu_navegacio.attr("data-tipus");
                menu_complet = imc_menu_navegacio.attr("data-complet");

                if (menu_tipus === "H" && menu_fila === "S" && !esMobil) {

                    imc_menu_navegacio
                        .addClass("imc--1-fila");

                }

                if (menu_tipus === "H" && !esMobil) {

                    elm
                        .appMenuHoritzontal();

                    imc_molla_pa
                        .addClass("imc--menu-horizontal");

                    imc_info
                        .addClass("imc--menu-horizontal");

                }

                menu_W = elm.outerWidth();
                menu_L = elm.offset().left;
                menu_max_posicio = menu_W + menu_L;

                elm
                    .find("ul button")
                        .off(".appMenuHoritzontal")
                        .on("click.appMenuHoritzontal", submenus);

            },
            submenus = function(e) {

                var bt = $(this),
                    opcio = bt.parent();

                if (opcio.hasClass("imc--obert")) {

                    opcio
                        .find(".imc--obert")
                            .removeClass("imc--obert")
                            .end()
                        .find(".imc--esquerre")
                            .removeClass("imc--esquerre");

                    opcio
                        .removeClass("imc--obert");

                } else {

                    opcio
                        .closest("ul")
                            .find("> li.imc--obert")
                                .removeClass("imc--obert")
                                .end()
                            .find("> li.imc--esquerre")
                                .removeClass("imc--esquerre");

                    opcio
                        .addClass("imc--quasi-obert");

                    var estaDins = true;

                    // 1ª opcio supera l'esquerre (nomes horitzontal)

                    if (menu_tipus === "H") {

                        var opcio_llista_1 = elm.find("ul:first"),
                            opcio_marcada_1 = opcio_llista_1.find("> li.imc--quasi-obert");

                        if (opcio_marcada_1.length) {

                            var opcio_marcada_1_W = opcio_marcada_1.outerWidth(),
                                opcio_llista_pos_L = opcio_llista_1.position().left;

                            var opcio_marcada_index = opcio_llista_1.find( "> li" ).index( opcio_marcada_1 ),
                                opcio_marcada_1_L = opcio_marcada_1_W * ( opcio_marcada_index+1 ),
                                opcio_llista_pos_L_max = opcio_marcada_1_L + opcio_llista_pos_L;

                            if (menu_W < opcio_llista_pos_L_max || opcio_llista_pos_L_max < opcio_marcada_1_W) {

                                var op_mou = (opcio_llista_pos_L_max < opcio_marcada_1_W) ? "dreta" : "esquerre";

                                elm
                                    .appMenuHoritzontal({ mou: op_mou });

                                var estaDins = false;

                            }

                        }

                    }

                    // submenu supera l'esquerre (nomes horitzontal)

                    if (menu_tipus === "H") {

                        var superaMenuTemps = (!estaDins) ? 250 : 0,
                            superaMenu = function() {

                                var opcio_llista = opcio.find("ul:first"),
                                    opcio_llista_W = opcio_llista.outerWidth(),
                                    opcio_llista_L = opcio_llista.offset().left,
                                    opcio_llista_max_posicio = opcio_llista_W + opcio_llista_L;

                                if (menu_max_posicio <= opcio_llista_max_posicio) {

                                    opcio
                                        .addClass("imc--esquerre");

                                }

                            };

                        setTimeout(
                            function() {

                                superaMenu();

                            },
                            superaMenuTemps
                        );

                    }

                    // obrim

                    opcio
                        .removeClass("imc--quasi-obert")
                        .addClass("imc--obert");

                    // event widow

                    setTimeout(
                        function() {

                            imc_finestra
                                .off(".appMenuHoritzontal ")
                                .on("click.appMenuHoritzontal", tanca);

                        },100
                    );

                }

            },
            tanca = function(e) {

                var bt_f = $(e.target);

                if (!bt_f.closest(".imc-menu").length) {

                    imc_menu_h
                        .find(".imc--obert")
                            .removeClass("imc--obert");

                }

            };

        // inicia

        inicia();
        
    });
    return this;
}


// appMenuHoritzontal

$.fn.appMenuHoritzontal = function(opcions) {
    var settings = $.extend({
        complet: false,
        mou: false
    }, opcions);
    this.each(function(){
        var elm = $(this),
            menu_complet = settings.complet,
            accio_mou = settings.mou,
            llista_principal = false,
            num_items = 0,
            bt_esquerre = false,
            bt_dreta = false,
            menu_H = false,
            llista_principal_H = false,
            moviment_llimit = false,
            inicia = function() {

                menu_complet = imc_menu_navegacio.attr("data-complet");

                imc_menu_navegacio
                    .addClass("imc--horitzontal");

                llista_principal = elm.find("ul:first");
                num_items = llista_principal.find("> li").length;

                var menu_W = elm.outerWidth(),
                    item_W = llista_principal.find("li:first").outerWidth();

                var total_W = num_items*item_W;

                var moltesOpcions = (menu_W < total_W) ? true : false;

                elm
                    .removeClass("imc-amb-botonera");

                if (moltesOpcions) {

                        llista_principal
                            .css("width", total_W+"px");

                        elm
                            .addClass("imc-amb-botonera");

                        llista_principal
                            .attr("data-posicio", "3");

                        creaBotonsLaterals();

                } else {

                    if (menu_complet === "N") {

                        var emplaria_menu = 0;

                        llista_principal
                            .find("> li")
                                .each(function(i) {

                                    var item_el = $(this),
                                        item_el_W = item_el.outerWidth();

                                    emplaria_menu += item_el_W + 1;

                                });

                        elm
                            .css("width", emplaria_menu+"px");

                    } else {

                        var nova_amplaria = menu_W / num_items;

                        elm
                            .find("li")
                                .css("width", nova_amplaria+"px");

                    }

                }

                itemsAmb2Linies();

            },
            creaBotonsLaterals = function() {

                bt_esquerre = $("<button>").attr("type", "button").addClass("imc--bt-menu imc--esquerre").appendTo( elm ).on("mousedown", clicaEsquerre).on("mouseup", para);
                bt_dreta = $("<button>").attr("type", "button").addClass("imc--bt-menu imc--dreta imc--actiu").appendTo( elm ).on("mousedown", clicaDreta).on("mouseup", para);

                menu_H = $( elm.outerWidth() ).toEm();
                llista_principal_H = $( llista_principal.outerWidth() ).toEm();

                moviment_llimit = (parseFloat( $( llista_principal.outerWidth() - elm.outerWidth() ).toEm() ) + 3) * -1;

            },
            moviment = false,
            moviment_posicio = false,
            clicaEsquerre = function(e) {

                if (!moviment && $(this).hasClass("imc--actiu")) {
                    
                    mouDreta();
                    moviment = setInterval(mouDreta, 250);

                } else {

                    para();

                }

            },
            mouDreta = function() {

                moviment_posicio = parseFloat( llista_principal.attr("data-posicio") ) + 15.4;

                if (moviment_posicio >= 3) {
                    moviment_posicio = 3;
                    para();
                }

                mou();

            },
            clicaDreta = function(e) {

                if (!moviment && $(this).hasClass("imc--actiu")) {
                    
                    mouEsquerre();
                    moviment = setInterval(mouEsquerre, 250);

                } else {

                    para();
                    
                }

            },
            mouEsquerre = function() {

                moviment_posicio = parseFloat( llista_principal.attr("data-posicio") ) - 15.4;

                if (moviment_posicio < moviment_llimit) {
                    moviment_posicio = moviment_llimit;
                    para();
                }

                mou();

            },
            mou = function() {

                // amaga submenus

                elm
                    .find(".imc--obert")
                        .removeClass("imc--obert")


                // mou

                llista_principal
                    .css({ transform: "translateX("+moviment_posicio+"em)" });

                llista_principal
                    .attr("data-posicio", moviment_posicio);

                marca();



            }, 
            para = function() {

                if (moviment) {

                    clearInterval(moviment);
                    moviment = false;

                }

            },
            marca = function() {

                if (moviment_posicio < 3) {
                    bt_esquerre
                        .addClass("imc--actiu");
                }

                if (moviment_posicio >= 3) {
                    bt_esquerre
                        .removeClass("imc--actiu");
                }

                if (moviment_posicio === moviment_llimit) {
                    bt_dreta
                        .removeClass("imc--actiu");
                } else {
                    bt_dreta
                        .addClass("imc--actiu");
                }

            },
            itemsAmb2Linies = function() {

                elm
                    .find("span")
                        .each(function(i) {

                            var span_el = $(this),
                                span_el_H = parseFloat( $( span_el.outerHeight() ).toEm() );

                            if (span_el_H > 2.5) {

                                span_el
                                    .addClass("imc--altura");

                            }

                        });

            };

        // accions

        if (accio_mou === "esquerre") {

            elm
                .find(".imc--dreta")
                    .trigger( "mousedown" )
                    .trigger( "mouseup" );

        } else if (accio_mou === "dreta") {

            elm
                .find(".imc--esquerre")
                    .trigger( "mousedown" )
                    .trigger( "mouseup" );

        } else {
        

            inicia();

        }
        
    });
    return this;
}


// appMenuMobil

$.fn.appMenuMobil = function(opcions) {
    var settings = $.extend({
        tipus: false
    }, opcions);
    this.each(function(){
        var elm = $(this),
            inicia = function() {

                imc_menu_navegacio
                    .find(".imc-bt-menu-obri, .imc-bt-menu-tanca")
                        .remove();

                // botó menú obri

                var bt_menu_obri_txt = $("<span>").text( txtMenu ),
                    bt_menu_obri = $("<button>").attr({ id: "imc-bt-menu-obri", type: "button"}).addClass("imc-bt-menu-obri").html( bt_menu_obri_txt ).on("click", obri).appendTo( imc_menu_navegacio );

                // botó menú tanca

                var bt_menu_tanca_txt = $("<span>").text( txtTanca ),
                    bt_menu_tanca = $("<button>").attr({ id: "imc-bt-menu-tanca", type: "button"}).addClass("imc-bt-menu-tanca").html( bt_menu_tanca_txt ).on("click", tanca).appendTo( imc_menu_h );
                
                // tanca fons

                imc_menu_h
                    .off(".appMenuMobil")
                    .on("click.appMenuMobil", tancaFons);

            },
            obri = function() {

                imc_menu_h
                    .addClass("imc-menu-h--obert");

            },
            tanca = function() {

                imc_menu_h
                    .removeClass("imc-menu-h--obert");

            },
            tancaFons = function(e) {

                var bt_f = $(e.target);

                console.log(bt_f.attr("css"));

                if (!bt_f.closest("ul").length) {

                    tanca();

                }



            };

        // inicia

        inicia();
        
    });
    return this;
}


// toEm

$.fn.toEm = function(settings) {
    settings = jQuery.extend({
        contenidor : 'body'
    }, settings);
    var valor = parseInt(this[0], 10),
        obj = jQuery('<div style="display:none; font-size:1em; margin:0; padding:0; height:auto; line-height:1; border:0;"> </div>').appendTo( settings.contenidor ),
        obj_H = obj.height();
    obj.remove();
    var valor_em = (valor) ? (valor / obj_H).toFixed(2) : "0";
    return valor_em;
}
