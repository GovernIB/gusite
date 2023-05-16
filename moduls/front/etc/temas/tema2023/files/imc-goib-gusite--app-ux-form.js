// Document JavaScript


// on ready

$(function () {


    // preguntes

    imc_body
        .appFormulariValida()
        .appFormulariCampObligatori()
        .appFormulariSelectorMultiple();

});


// formulari - validació JS

$.fn.appFormulariValida = function (options) {

    var settings = $.extend({
        contenidor: false
    }, options);

    this.each(function () {

        var element = $(this)
            , form_els = element.find(".imc--f-el")
            , tipus = false
            , esError = false
            , error_el = false
            , error_etiqueta = false
            , esValid = false;

        var valida = function (e) {

            e.preventDefault();

            // revisa tipus d'elements de formulari

            form_els
                .each(function () {

                    var el = $(this)
                        , esRequerit = el.is("[data-obligatori=si]")
                        , esSelect = el.find("select:first").length
                        , esSelector = el.find(".imc--control-selector:first").length
                        , esTextarea = el.find("textarea:first").length
                        , esRadio = el.find("input[type=radio]:first").length
                        , esCheck = el.find("input[type=checkbox]:first").length
                        ,
                        esText = el.find("input[type=text]:first, input[type=date]:first, input[type=email]:first, input[type=url]:first").length;

                    if (esRequerit && esSelect && el.find("select:first").val() === "") {

                        esError = true;
                        error_el = el;
                        tipus = "select";
                        error_etiqueta = error_el.find("label:first").text();
                        return false;

                    } else if (esRequerit && esSelector && !el.find(".imc--seleccionats:first li").length) {

                        esError = true;
                        error_el = el;
                        tipus = "selector";
                        error_etiqueta = error_el.find(".imc--f-etiqueta:first").text();
                        return false;

                    } else if (esRequerit && esTextarea && el.find("textarea:first").val() === "") {

                        esError = true;
                        error_el = el;
                        tipus = "textarea";
                        error_etiqueta = error_el.find("label:first").text();
                        return false;

                    } else if (esRequerit && esRadio && !el.find("input[type=radio]:checked").length) {

                        esError = true;
                        error_el = el;
                        tipus = "radio";
                        error_etiqueta = error_el.find("legend:first span").text() || error_el.find("imc--f-etiqueta:first > span").text();
                        return false;

                    } else if (esRequerit && esCheck && !el.find("input[type=checkbox]:checked").length) {

                        esError = true;
                        error_el = el;
                        tipus = "check";
                        error_etiqueta = error_el.find("legend:first span").text() || error_el.find("imc--f-etiqueta:first > span").text();
                        return false;

                    } else if (esRequerit && esText && el.find("input:first").val() === "") {

                        esError = true;
                        error_el = el;
                        tipus = "text";
                        error_etiqueta = error_el.find("label:first").text();
                        return false;

                    }

                });


            // marca l'error

            var marcaError = function () {

                error_el
                    .addClass("imc--f-error");

                var element_enfoca = (tipus === "select") ? "select:first" : (tipus === "selector") ? ".imc--control-selector:first" : (tipus === "textarea") ? "textarea:first" : "input:first";

                error_el
                    .find(element_enfoca)
                    .on("keyup.appFormulariValida, mousedown.appFormulariValida", desmarcaError)
                    .focus();

            };

            var desmarcaError = function (e) {

                var el_ = $(this)
                    , el_pare = el_.closest(".imc--f-el");

                el_
                    .off(".appFormulariValida");

                el_pare
                    .removeClass("imc--f-error");

            };


            // si hi ha error

            if (esError) {

                imc_missatge
                    .appMissatge({
                        tipus: "informacio",
                        estat: "error",
                        titol: txtForm_error_JS_titol + " " + error_etiqueta,
                        text: txtForm_error_JS_text,
                        alAcceptar: function () {
                            marcaError();
                        }
                    });

                //return esValid;

            } else {

                esValid = true;

                //return esValid;

            }

        };

        // inicia

        element
            .off(".appFormulariValida")
            .on("click.appFormulariValida", "form button[type=submit]", valida);


    });
    return this;
}


// Validacions

$.fn.appFormulariValidacions = function (options) {
    var settings = $.extend({
        contenidor: false
    }, options);
    this.each(function () {
        var element = $(this)
            , inputs_required = element.find("form input[required]")
            , inicia = function (e) {

            // llistat radios i checks obligatoris

            element
                .find("fieldset[data-obligatori=si]")
                .each(function () {

                    var fieldset_ = $(this);

                    fieldset_
                        .find("input:first")
                        .prop("required", true);

                });

        }
            , valida = function (e) {

            var input_ = e.target
                , input_type = input_.type;

            if (input_.validity.valueMissing) {
                input_.setCustomValidity(txtForm_campObligatori);
            }

            if (input_.validity.typeMismatch && input_type === "email") {
                input_.setCustomValidity(txtForm_error_campCorreu);
            } else if (input_.validity.typeMismatch && input_type === "url") {
                input_.setCustomValidity(txtForm_error_campPagina);
            }

            $(input_)
                .addClass("imc--f-error");

        }
            , desvalida = function (e) {

            var input_ = e.target;

            input_.setCustomValidity('');

            $(input_)
                .removeClass("imc--f-error");

        };

        // inicia

        inicia();

        // events

        inputs_required
            .off(".appFormulariValidacions")
            .on("invalid.appFormulariValidacions", valida)
            .on("change.appFormulariValidacions", desvalida);

    });
    return this;
}


// selectos múltiple --> llistat de checks

$.fn.appFormulariSelectorMultiple = function (options) {
    var settings = $.extend({
        contenidor: false
    }, options);
    this.each(function () {
        var element = $(this)
            , inicia = function () {

            // revisem selectors

            var selector_multiples_ = element.find("select[multiple]");

            selector_multiples_
                .each(function () {

                    var sel_ = $(this)
                        , sel_contenidor = sel_.closest(".imc--f-el");

                    canvia(sel_);


                });

        }
            , canvia = function (sel_) {

            // canviem si data-tipus = multiple

            var sel_id = sel_.attr("id")
                , sel_name = sel_.attr("name")
                , sel_required = sel_.is(":required")
                , etiqueta_ = sel_.closest(".imc--f-el").find(".imc--f-etiqueta:first")
                , etiqueta_html = $("<span>").text(etiqueta_.text());

            etiqueta_
                .html(etiqueta_html);

            var ul_ = $("<ul>");

            sel_
                .find("option")
                .each(function (i) {

                    var opcio_ = $(this)
                        , opcio_val = opcio_.attr("value")
                        , opcio_txt = opcio_.text();

                    var label_html = "<label class=\"imc--checkbox\"><input type=\"checkbox\" name=\"" + sel_name + "\" id=\"" + (sel_id + "_" + i) + "\" value=\"" + opcio_val + "\"> <span>" + opcio_txt + "</span></label>";

                    $("<li>")
                        .html(label_html)
                        .appendTo(ul_);

                });

            if (sel_required) {

                ul_
                    .find("input[type=checkbox]:first")
                    .prop("required", true);

            }

            sel_
                .replaceWith(ul_);

        };

        // inicia

        inicia();

    });
    return this;
}



//Obligatoriedad de los campos
$.fn.appFormulariCampObligatori = function (options) {
    var settings = $.extend({
        contenidor: false
    }, options);
    this.each(function () {
        var element = $(this)
            , inicia = function () {

            // revisem selectors

            var camps = element.find(".imc--f-control:contains('*')");

            camps
                .each(function () {

                    var camp= $(this).find(":first-child"),
                        req = camp.attr('required');

                    console.log($(this).get(1));
                    if(typeof req !== 'undefined' && req !== false) {
                        camp.closest(".imc--f-el").attr('data-obligatori', 'si');

                    }

                });
            camps.contents()
                .each(function () {
                        if (this.nodeType === Node.TEXT_NODE) this.remove();
                    }
                )

        }


        // inicia

        inicia();

    });
    return this;
}
