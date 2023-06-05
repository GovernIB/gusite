// JS


// constants

var APP_IDIOMA;

var imc_finestra
	,imc_body
	,imc_contenidor
	,imc_cap
	,imc_peu;


// onReady

$(function(){

	appInicia();

});

function appInicia() {

	APP_IDIOMA = $("html").attr("lang");

	imc_finestra = $(window);
	imc_body = $("body");
	imc_contenidor = $("#imc--contenidor");
	imc_cap = $("#imc--cap");
	imc_peu = $("#imc--peu");
	imc_continguts = $("#imc--continguts");

	imc_body
		.appTitle()
		.appMenuVertical();

}



// appMenuVertical

$.fn.appMenuVertical = function(options) {

	var settings = $.extend({
			element: ""
		}, options);

	this.each(function(){
		var element = $(this)
			,inicia = function() {

				menu();

				var resizeAppMenuVertical;

			    imc_finestra
			    	.off("appMenuVertical")
			        .on('resize.appMenuVertical', function(e) {

			            clearTimeout(resizeAppMenuVertical);

			            resizeAppMenuVertical = setTimeout(function() {

			                menu();

			            }, 50);

			        });

			}
			,menu = function() {

				if (imc_contenidor.attr("data-menu") === "v" && imc_finestra.width() > 1200) {

					imc_contenidor
						.find(".imc--me-menu:first")
							.removeAttr("aria-hidden");

				} else {

					imc_contenidor
						.find(".imc--me-menu:first")
							.attr("aria-hidden", "true");

				}

				if (imc_contenidor.attr("data-menu") === "vertical" && imc_finestra.width() > 1200) {

					if (imc_continguts.find(".imc--me-menu").length) {
						return;
					}

					var me_lateral_ = imc_cap.find(".imc--me-menu:first")
						,me_lateral_html = me_lateral_.html()
						,desplegaVertical = (me_lateral_.attr("data-desplega") === "vertical") ? true : false
						,esEstatic = (me_lateral_.attr("data-estatic") === "s") ? true : false
						,me_lateral_vertical = $("<div>").addClass("imc--me-menu").html( me_lateral_html );

					imc_continguts
						.attr("data-menu", "vertical")
						.prepend( me_lateral_vertical );

					var me_lateral_duplicat_ = imc_continguts.find(".imc--me-menu:first")

					if (desplegaVertical) {

						me_lateral_duplicat_
							.find("ul[style]")
								.removeAttr("style")
								.end()
							.attr("data-desplega", "vertical");

					}

					if (esEstatic) {

						me_lateral_duplicat_
							.find("div[aria-hidden=true]")
								.removeAttr("aria-hidden")
								.end()
							.find("ul[style]")
								.removeAttr("style")
								.end()
							.attr("data-estatic", "s");

						me_lateral_duplicat_
							.find(".imc--me-submenu")
							.attr("data-nivell", "1")
							.end()
							.find(".imc--me-submenu .imc--me-submenu")
							.attr("data-nivell", "2");

					}

				} else {

					imc_continguts
						.find(".imc--me-menu:first")
							.remove()
							.end()
						.removeAttr("data-menu");

				}


			};

			// inicia

			inicia();
		
	});

	return this;
}



// appTitle

$.fn.appTitle = function(options) {

	var settings = $.extend({
			element: ""
		}, options);

	this.each(function(){
		var element = $(this)
			,id_el = false
			,onAplica = "a.imc--img, button, input[aria-label], .imc--calendari td a.imc--bt"
			,pinta = function(e) {

				// pinta

				var elm_t = $(this)
					,elm_aria_label = elm_t.attr("aria-label") || false;

				if (elm_t.find("> span").css("position") !== "absolute" && !elm_aria_label) {
					return;
				}

				var elm_t_text = (elm_aria_label) ? elm_aria_label : elm_t.find("span").text();

				imc_body
					.find("div[role=tooltip]")
						.remove();

				var title_el = $("<div>")
									.attr({ "role": "tooltip", "aria-hidden": "true" })
									.text( elm_t_text )
									.insertBefore( imc_contenidor );

				// posiciona

				var finestra_W = imc_finestra.width();

				var bt_T = elm_t.offset().top
					,bt_L = elm_t.offset().left
					,bt_H = elm_t.outerHeight()
					,bt_W = elm_t.outerWidth(true)
					,bt_marge_L = parseInt(elm_t.css("marginLeft"), 10) || 0;

				var id_el_R = title_el.outerWidth(true) + bt_L;

				if (id_el_R+6 >= finestra_W) {

					var pos_R = finestra_W - (bt_L + bt_W - bt_marge_L) - 10;
					
					title_el
						.removeAttr("style")
						.css({ top: (bt_T + bt_H) + "px", right: (pos_R) + "px" })
						.addClass("imc--dreta");

				} else {

					title_el
						.removeAttr("style")
						.css({ top: (bt_T + bt_H) + "px", left: (bt_L+bt_marge_L-10) + "px" });

				}

				// mostra

				title_el
					.attr("aria-hidden", "false");

			}
			,amaga = function(e) {

				// pinta

				var elm_t = $(this);

				imc_body
					.find("div[role=tooltip]:first")
						.attr("aria-hidden", "true");

			};
		
		// events
		
		element
			.off(".appTitle")
			.on('mouseenter.appTitle, focus.appTitle', onAplica, pinta)
			.on('mouseleave.appTitle, blur.appTitle', onAplica, amaga);
		
	});

	return this;
}



// appTabulacioNova

$.fn.appTabulacioNova = function(options) {

	var settings = $.extend({
			element: ""
			,accio: false
			,enfocaEn: false
		}, options);

	this.each(function(){
		var element = $(this)
			,accio = settings.accio
			,enfocaEn = settings.enfocaEn
			,el_num = 0
			,elems_tab = []
			,elems_tab_size = 0
			,inicia = function() {

				if (accio === "finalitza") {

					element
						.off(".appTabulacioNova");

					return;

				}

				el_num = 0;
				elems_tab = [];
				elems_tab_size = 0;

				setTimeout(
					function() {

						activa();

					},100
				);

			},
			activa = function() {

				elems_tab = element.find("*[data-tabula=si]:visible:not(:disabled)");
				elems_tab_size = elems_tab.length;

				if (elems_tab_size) {

					elems_tab
						.each(function(i) {

							var el = $(this);

							el
								.attr("data-tabpos", i+1);
							
						});

					elems_tab
						.splice(0, 0, element);

					element
						.off(".appTabulacioNova")
						.on("focus.appTabulacioNova", "*[data-tabula]", reposiciona)
						.on("focus.appTabulacioNova", reposiciona)
						.on("keydown.appTabulacioNova", tabula)
						.attr("data-tabpos", 0);

				}

				// enfoquem en algun element?

				if (enfocaEn) {

					enfocaEn
						.focus();

				} else {

					element
						.focus();

				}

			},
			reposiciona = function(e) {

				var inp_el = $(this)
					,in_tabpos = parseInt( inp_el.attr("data-tabpos"), 10);

				el_num = in_tabpos;

			},
			tabula = function(e) {

				var tecla = e.keyCode
					,esShift = !!e.shiftKey;

				// tabula

				if ( esShift && tecla === 9) {

					e.preventDefault();

					el_num--;

					if (el_num < 0) {
						el_num = elems_tab_size;
					}

					elems_tab[el_num]
						.focus();

				} else if ( !esShift && tecla === 9){
				
					e.preventDefault();

					el_num++;

					if (el_num > elems_tab_size) {
						el_num = 0;
					}

					elems_tab[el_num]
						.focus();

				}

				// escapa i amaga

				if (tecla === 27) {

					element
						.find("button[data-accio=galeria-tanca]:first")
							.trigger("click");

				}

			};
		
		// inicia
		inicia();
		
	});

	return this;
}