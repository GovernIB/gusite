// JS



// onReady

$(function(){


	imc_body
		.appCercador()
		.appCapMenu()
		.appIdioma()
		.appCapTitol();


	//$(".imc--app-logo:first")
	//	.fitText();

});


/* text del títol, ajustar si és massa llarg */

$.fn.appCapTitol = function(options) {

	var settings = $.extend({
			element: ""
		}, options);

	this.each(function(){
		var element = $(this)
			,titol_contenidor = element.find(".imc--app-logo:first")
			,titol_text = titol_contenidor.find("span:first")
			,font_amplaria = false
			,resizeFont = false
			,contador = 0
			,accioAnterior = false
			,revisa = function(accio) {

				if ( parseInt( titol_text.css("font-size"), 10 ) >= 29 ) {
					clearTimeout(resizeFont);
					return;
				}

				if ( contador >= 15 ) {
					clearTimeout(resizeFont);
					return;
				}

				var titol_contenidor_H = parseInt( titol_contenidor.outerHeight(), 10)
					,titol_text_H = parseInt( titol_text.outerHeight(), 10);


				console
					.log(titol_contenidor_H + " - " + titol_text_H + " - " + contador + " - " + accio + " - " + accioAnterior)

				if (titol_contenidor_H < titol_text_H && !contador) {

					ajusta("reduix");

				} else if (titol_contenidor_H > titol_text_H + 5 && !contador) {

					ajusta("augmenta");

				} else if (titol_contenidor_H < titol_text_H && contador && accio === "reduix") {

					ajusta("reduix");

				} else if (titol_contenidor_H > titol_text_H + 5 && contador && accio === "augmenta") {

					ajusta("augmenta");

				} else {

					clearTimeout(resizeFont);

				}

				accioAnterior = accio;

			}
			,ajusta = function(accio) {

				contador++;

				font_amp_ = parseInt( titol_text.css("font-size"), 10 );

				font_amplaria = (accio === "reduix") ? font_amp_ - 1 : font_amp_ + 1;

				titol_text
					.css("font-size", font_amplaria + "px");

				clearTimeout(resizeFont);

				resizeFont = setTimeout(
					function() {

						revisa(accio);

					}
					,50
				);

			};

		// revisa

		revisa();

		// resize
		if(options==undefined ){
			var resizeAppCapTitol;
	
		    imc_finestra
		        .on('resize', function(e) {
		            clearTimeout(resizeAppCapTitol);
		            resizeAppCapTitol = setTimeout(function() {
	
		                imc_body
							.appCapTitol({ desde: "resize" });
	
		            }, 50);
		        });
		}
	});

	return this;
}

/* cercador */

$.fn.appCercador = function(options) {

	var settings = $.extend({
			element: ""
		}, options);

	this.each(function(){
		var element = $(this)
			,form_elm = imc_cap.find(".imc--ce-form:first")
			,activa = function(e) {

				var estaTancat = (form_elm.attr("aria-hidden") === "true") ? true : false
					,estatNou = (estaTancat) ? "false" : "true";

				form_elm
					.attr("aria-hidden", estatNou);

				form_elm
					.find("input:first")
						.focus();

			}
			,tanca = function(e) {

				var obj = $(e.target);

				if (obj.attr("class") !== "imc--ce-form") {
					return;
				}

				element
					.find("button[data-accio=cerca-obri]:first")
						.trigger("click");

			};

		// events
		
		element
			.off(".appCercador")
			.on('click.appCercador', "button[data-accio=cerca-obri]", activa)
			.on('click.appCercador', ".imc--ce-form", tanca);
		
	});

	return this;
}


/* cap -> menu */

$.fn.appCapMenu = function(options) {

	var settings = $.extend({
			element: ""
		}, options);

	this.each(function(){
		var element = $(this)
			,esV = (imc_contenidor.attr("data-menu") === "v") ? true : false
			,esVertical = imc_contenidor.attr("data-menu") === "vertical" ? true : false
			,menu_elm = imc_cap.find(".imc--me-menu:first")
			,esDesplegableVertical = menu_elm.attr("data-desplega") === "vertical" ? true : false
			,esEstatic = menu_elm.attr("data-estatic") === "s" ? true : false
			,inicia = function() {
				var menus = element.find(".imc--me-submenu");
				menus.each(function(i) {
					
					var menu = $(this)
						,menu_fills=menu.children().children().find(".imc--me-submenu")
						,actiu = menu.find(".activo");
						
					//Si hi ha submenús dintre del submenú de nivell 1, revisem que aquests no tinguin actiu cap element
					if(menu_fills.length > 0) {
						menu_fills.each(function(i) {
							var menu = $(this)
								,actiu = menu.find(".activo");
							if(actiu.length === 1) {
								menu.attr("aria-hidden", "false"); 
								return; 
							}
						})
					}
					if(actiu.length === 1) {
						menu.attr("aria-hidden", "false"); 
						return; 
					}

				})
			}
			,altura = function() {

				// revisem altura del llistat

				if (!menu_elm.find(".imc--m-secundari").length) {
					return;
				}

				var m_secundari = menu_elm.find(".imc--m-secundari:first")
					,m_secundari_T = m_secundari.offset().top
					,finestra_H = imc_finestra.height()
					,m_principal_ul = menu_elm.find(".imc--m-principal:first ul")
					,m_principal_ul_T = m_principal_ul.offset().top;

				var altura_ = finestra_H - (finestra_H -m_secundari_T + m_principal_ul_T) - 30;

				m_principal_ul
					.css("height", altura_ + "px");

			}
			,obri = function(e) {

				menu_elm
					.attr("aria-hidden", "false");

				
				menu_elm.find(".imc--me-submenu").attr("aria-hidden", "true");

				menu_elm
					.focus();
				

			}
			,tanca = function() {

				
				menu_elm
					.find(".imc--me-submenu")
						.attr("aria-hidden", "true")
						.end()
					.find("button")
						.removeAttr("data-estat")
						.end()
					.attr("aria-hidden", "true")
					.removeAttr("data-nivell");
				

			}
			,tancaFons = function(e) {

				var obj = $(e.target);

				if (obj.attr("class") !== "imc--me-menu") {
					return;
				}

				element
					.find("button[data-accio=menu-tanca]:first")
						.trigger("click");

			}
			,tancaFinestra = function(e) {


				var obj = $(e.target);

				if (obj.closest(".imc--m-principal").length) {
					return;
				}

				var menu_v_elm = imc_continguts.find(".imc--me-menu:first");

				menu_v_elm
					.find(".imc--me-submenu[aria-hidden=false]")
						.attr("aria-hidden", "true")
						.end()
					.find("button")
						.removeAttr("data-estat")
						.end()
					.removeAttr("data-nivell");

			}
			,submenu = function(e) {

				var bt_ = $(this)
					,bt_nivell = bt_.parents("li").length
					,submenu_el = bt_.parent().find("div:first")
					,submenu_obert = (submenu_el.attr("aria-hidden") === "false") ? true : false;

				

				var esticEnMenuVisible = (bt_.closest(".imc--continguts").length) ? true : false;

				if (esticEnMenuVisible && esEstatic) {
					//return;
				}

				if (submenu_obert) {
					
					enrereMenu(bt_);
					return;

				}

				// marquem el nivell

				bt_
					.closest(".imc--me-menu")
						.attr("data-nivell", bt_nivell);

				// amaguem tots els submenús

				// bt_
				// 	.closest("ul")
				// 		.find(".imc--me-submenu")
				// 			.attr("aria-hidden", "true");

				// mostrem submenú

				bt_
					.parent()
						.find("div:first")
							.attr("aria-hidden", "false");

				// canviem els estats de tots els botons de sumnenú

				// bt_
				// 	.closest("ul")
				// 		.find("button")
				// 			.removeAttr("data-estat");

				// marquem el estat "actiu" del botó polsat

				bt_
					.attr("data-estat", "actiu");

			}
			,enrereMenu = function(bt_) {

				// console.log(bt_);
				// var ultim_ = bt_.closest(".imc--me-menu") .find(".imc--me-submenu[aria-hidden=false]:last");

				var parent = bt_.parent(); 
				var ultim_ = parent.find(".imc--me-submenu[aria-hidden=false]:first")
				

				bt_
					.removeAttr("data-estat");
				
				enrere(ultim_);

			}
			,enrereBt = function(e) {

				var ultim_ = menu_elm.find(".imc--me-submenu[aria-hidden=false]:last");

				enrere(ultim_);

			}
			,enrere = function(ultim_) {
				
				var bt_nivell = ultim_.parents("li").length - 1;

				ultim_
						.attr("aria-hidden", "true");

				if (bt_nivell) {

					menu_elm
						.attr("data-nivell", bt_nivell);

				} else {

					menu_elm
						.removeAttr("data-nivell");

				}

			};

		inicia(); 
		// altura

		altura();

		if(options==undefined ){
			var resizealtura;
	
		    imc_finestra
		        .on('resize.resizealtura', function(e) {
		            clearTimeout(resizealtura);
		            resizealtura = setTimeout(function() {
	
		                altura();
	
		            }, 100);
		        });	
		}
		// events
		
		element
			.off(".appCapMenu")
			.on('click.appCapMenu', "button[data-accio=menu-obri]", obri)
			.on('click.appCapMenu', "button[data-accio=menu-tanca]", tanca)
			.on('click.appCapMenu', "button[data-accio=submenu-obri]", submenu)
			.on('click.appCapMenu', "button[data-accio=menu-enrere]", enrereBt)
			.on('click.appCapMenu', ".imc--me-menu", tancaFons);

		if (esV || esVertical && !esDesplegableVertical) {

			imc_finestra
				.off(".appCapMenu")
				.on('click.appCapMenu', tancaFinestra);

		}
		
	});

	return this;
}



/* idioma menú desplegable */

$.fn.appIdioma = function(options) {

	var settings = $.extend({
			element: ""
			,desde: false
		}, options);

	this.each(function(){
		var element = $(this)
			,desde = settings.desde
			,idioma_elm = imc_cap.find(".imc--idioma:first")
			,idioma_llista = idioma_elm.find(".imc--llista:first")
			,imc_finestra_W = false
			,revisa = function() {

				imc_finestra_W = imc_finestra.width();

				idioma_elm
					.find("button[data-accio=idioma-activa]")
						.remove();

				if (imc_finestra_W < 1000) {

					inicia();

				} else {
					
					idioma_llista
						.removeAttr("aria-hidden");

				}

			}
			,inicia = function(e) {

				var idioma_val = $("html").attr("lang")
					,bt_selec = idioma_elm.find("button[data-idioma="+idioma_val+"]:first")
					,bt_clonat = bt_selec.clone();

				bt_clonat
					.attr({ "data-accio": "idioma-activa" })

				idioma_elm
					.prepend(bt_clonat);

				idioma_llista
					.attr("aria-hidden", "true");

			}
			,activa = function(e) {

				var esVisible = (idioma_llista.attr("aria-hidden") === "true") ? false : true;

				if (!esVisible) {

					idioma_llista
						.attr("aria-hidden", "false");

					setTimeout(
						function() {

							imc_finestra
								.off(".appIdioma")
								.on("click.appIdioma", tanca);

						}
						,50
					);

				} else {

					tanquem();

				}

			}
			,tanca = function(e) {

				var obj = $(e.target)
					,esIdioma = obj.closest(".imc--idioma").length;

				if (esIdioma) {
					return;
				}

				tanquem();

			}
			,tanquem = function(e) {

				idioma_llista
					.attr("aria-hidden", "true");

				imc_finestra
					.off(".appIdioma");

			};

		// si no és visible, no actuem

		if (!idioma_elm.is(":visible")) {
			return;
		}

		// revisa

		revisa();

		// resize
		if(options==undefined ){
			var resizeAppIdioma;
		    imc_finestra
		        .on('resize', function(e) {
		            clearTimeout(resizeAppIdioma);
		            resizeAppIdioma = setTimeout(function() {
	
		                imc_body
							.appIdioma({ desde: "resize" });
	
		            }, 50);
		        });
		}
		// events
		
		element
			.off(".appIdioma")
			.on('click.appIdioma', "button[data-accio=idioma-activa]", activa);
		
	});

	return this;
}