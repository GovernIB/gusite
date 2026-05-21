// JS



// onReady

$(function(){


	imc_body
		.appCercador()
		.appCapMenu();


	//$(".imc--app-logo:first")
	//	.fitText();

});


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
			,esVertical = imc_contenidor.attr("data-menu") === "vertical" ? true : false
			,menu_elm = imc_cap.find(".imc--me-menu:first")
			,obri = function(e) {

				menu_elm
					.attr("aria-hidden", "false");

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

				if (submenu_obert) {

					enrere(bt_);
					return;

				}

				// marquem el nivell

				bt_
					.closest(".imc--me-menu")
						.attr("data-nivell", bt_nivell);

				// amaguem tots els submenús

				bt_
					.closest("ul")
						.find(".imc--me-submenu")
							.attr("aria-hidden", "true");

				// mostrem submenú

				bt_
					.parent()
						.find("div:first")
							.attr("aria-hidden", "false");

				// canviem els estats de tots els botons de sumnenú

				bt_
					.closest("ul")
						.find("button")
							.removeAttr("data-estat");

				// marquem el estat "actiu" del botó polsat

				bt_
					.attr("data-estat", "actiu");

			}
			,enrere = function(bt_) {

				var ultim_ = bt_.closest(".imc--me-menu").find(".imc--me-submenu[aria-hidden=false]:last")
					,bt_nivell = ultim_.parents("li").length - 1;

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

		// events
		
		element
			.off(".appCapMenu")
			.on('click.appCapMenu', "button[data-accio=menu-obri]", obri)
			.on('click.appCapMenu', "button[data-accio=menu-tanca]", tanca)
			.on('click.appCapMenu', "button[data-accio=submenu-obri]", submenu)
			.on('click.appCapMenu', "button[data-accio=menu-enrere]", enrere)
			.on('click.appCapMenu', ".imc--me-menu", tancaFons);


		if (imc_contenidor.attr("data-menu") === "v") {

			imc_finestra
				.off(".appCapMenu")
				.on('click.appCapMenu', tancaFinestra);

		}

		if (esVertical) {

			imc_finestra
				.off(".appCapMenu")
				.on('click.appCapMenu', tancaFinestra);

		}
		
	});

	return this;
}

