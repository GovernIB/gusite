// Document JavaScript



// on ready

$(function(){


	// preguntes

	imc_body
		.appFaqs();

});



// preguntes freqüents

$.fn.appFaqs = function(options) {
	var settings = $.extend({
		contenidor : false
	}, options);
	this.each(function() {
		var element = $(this)
			,inicia = function(e) {

				var preguntes_ = element.find(".imc--preguntes:first")
					grups_ = preguntes_.find(".imc--grup");

				grups_
					.each(function(i) {

						var grup_ = $(this)
							,converses_ = grup_.find(".imc--conversa");

						converses_
							.each(function(j) {

								var conversa_ = $(this)
									,pregunta_ = conversa_.find("button[data-accio=pregunta-activa]:first")
									,resposta_ = conversa_.find("imc--resposta:first");

								pregunta_
									.attr({ id: "imc--pre-"+i+"-"+j, "aria-controls": "imc--res-"+i+"-"+j });

								resposta_
									.attr({ id: "imc--res-"+i+"-"+j, "aria-labelledby": "imc--pre-"+i+"-"+j });

							})

					});

			}
			,activa = function(e) {

				var titol_el = $(this)
					,conversa_ = titol_el.closest(".imc--conversa")
					,pregunta_ = conversa_.find("button[data-accio=pregunta-activa]:first")
					,resposta_ = conversa_.find(".imc--resposta:first");

				if (resposta_.is(":hidden")) {

					pregunta_
						.attr("aria-expanded", "true");

					resposta_
						.stop()
						.slideDown(200);

				} else {

					pregunta_
						.attr("aria-expanded", "false");

					resposta_
						.stop()
						.slideUp(200);

				}

			};

		// inicia

		inicia();

		// events
		
		element
			.off(".appFaqs")
			.on("click.appFaqs", ".imc--conversa > button[data-accio=pregunta-activa]", activa);

	});
	return this;
}

