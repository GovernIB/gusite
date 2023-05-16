// Document JavaScript



// on ready

$(function(){


	// seccions del peu en dispositius menuts

	imc_body
		.appPeuSeccions();


	var resizeAppPeuSeccions;

    imc_finestra
        .on('resize', function(e) {
            clearTimeout(resizeAppPeuSeccions);
            resizeAppPeuSeccions = setTimeout(function() {

                imc_body
					.appPeuSeccions({ desde: "resize" });

            }, 150);
        });


});



// seccions del peu

$.fn.appPeuSeccions = function(options) {

	var settings = $.extend({
			contenidor: false
			,desde: false
		}, options);

	this.each(function() {
		var element = $(this)
			,desde = settings.desde
			,seccions_ = imc_peu.find(".imc--seccions:first")
			,llistat_ = seccions_.find("ul:first")
			,llistat_padding_left = false
			,posicio_llimit = 0
			,bt_anterior = seccions_.find("button[data-accio=seccions-anterior]:first")
			,bt_seguent = seccions_.find("button[data-accio=seccions-seguent]:first")
			,inicia = function() {

				// revisem

				if (!seccions_.length) {
					return;
				}

				// continuem, si està seccions

				llistat_padding_left = llistat_.offset().left

				var llistat_li = llistat_.find("li")
					,llistat_li_num = llistat_li.length;

				llistat_
					.find("li")
						.each(function(i) {

							var li_ = $(this)
								,li_L = parseInt( li_.offset().left, 10);

							li_
								.attr("data-offset", li_L);

							if (i === 0) {

								li_
									.addClass("imc--primer")
									.attr("data-posicio", "primer");

							} else if (i === llistat_li_num-1) {

								li_
									.attr("data-posicio", "ultim");

							} else {

								li_
									.removeClass("imc--primer");

							}

						});

				var imc_finestra_W = imc_finestra.width()
					,llistat_W = parseInt( llistat_.outerWidth(), 10);

				posicio_llimit = (llistat_W - imc_finestra_W) * -1;

				if (desde === "resize") {

					aplica( "0" );

				} else {

					llistat_
						.attr("data-llimit", posicio_llimit)
						.attr("data-padding", llistat_padding_left);
				}

				bt_anterior
					.prop("disabled", true);

				bt_seguent
					.prop("disabled", false);

			}
			,mou = function(e) {

				var bt_ = $(this)
					,bt_accio = bt_.attr("data-accio")
					,el_primer = llistat_.find(".imc--primer:first")
					,el_seguent = (bt_accio === "seccions-seguent") ? el_primer.next() : el_primer.prev();

				var el_seguent_L = parseInt( el_seguent.attr("data-offset"), 10) * -1;

				bt_anterior
						.prop("disabled", false);

				bt_seguent
					.prop("disabled", false);

				if (el_seguent_L < posicio_llimit) {

					bt_seguent
						.prop("disabled", true);

				} else if (el_seguent.attr("data-posicio") === "primer" && bt_accio === "seccions-anterior") {

					bt_anterior
						.prop("disabled", true);

				} else if (el_seguent.attr("data-posicio") === "ultim" && bt_accio === "seccions-seguent") {

					bt_seguent
						.prop("disabled", true);

				}

				llistat_
					.find(".imc--primer")
						.removeClass("imc--primer");

				aplica(el_seguent_L)

				el_seguent
					.addClass("imc--primer");

			}
			,aplica = function(el_seguent_L) {

				var ll_seguent_L = (el_seguent_L < posicio_llimit) ? posicio_llimit : el_seguent_L;

				llistat_
                    .css({ transform: "translateX("+ll_seguent_L+"px)" });

			};

		// inicia

		inicia();

		// events
		
		element
			.off(".appPeuSeccions")
			.on("click.appPeuSeccions", "button[data-accio]", mou);

	});
	return this;
}

