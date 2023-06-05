// JS


// constants

var imc_missatge;


// onReady

$(function(){

	imc_missatge = $("#imc--missatge");

});


// appMissatge

$.fn.appMissatge = function(options) {
	var settings = $.extend({
			tipus: "accio", // accio (accepta i cancel·la), informacio (d'acord), execucio (quan estem desant o enviant dades)
			estat: "informacio",
			titol: "",
			text: "",
			bt: false,
			araAmaga: false,
			amagaDesdeFons: true,
			txtAccepta: txtAccepta,
			txtCancela: txtCancela,
			txtDacord: txtDacord,
			alMostrar: function() {},
			alAmagar: function() {},
			alAcceptar: function() {},
			alCancelar: function() {},
			alTancar: function() {}
		}, options);
	this.each(function(){
		var element = $(this)
			,tipus = settings.tipus
			,estat = settings.estat
			,titol_txt = settings.titol
			,text_txt = settings.text
			,bt = settings.bt
			,araAmaga = settings.araAmaga
			,amagaDesdeFons = settings.amagaDesdeFons
			,txtAccepta = settings.txtAccepta
			,txtCancela = settings.txtCancela
			,txtDacord = settings.txtDacord
			,alMostrar = settings.alMostrar
			,alAmagar = settings.alAmagar
			,alAcceptar = settings.alAcceptar
			,alCancelar = settings.alCancelar
			,alTancar = settings.alTancar
			,titol_el = element.find("h2:first span")
			,text_el = element.find(".imc--info:first")
			,botonera_el = element.find(".imc--botonera:first")
			,inicia = function() {

				if (araAmaga) {

					amaga();
					return;

				}

				if (tipus === "execucio") {

					amagaDesdeFons = false;
					
				}

				botonera_el
					.find("button[data-accio=accepta]:first span")
						.text( txtAccepta )
						.end()
					.find("button[data-accio=dacord]:first span")
						.text( txtDacord )
						.end()
					.find("button[data-accio=cancela]:first span")
						.text( txtCancela )
						.end()
					.off('.appMissatge')
					.on('click.appMissatge', 'button[data-accio="dacord"]', dacord)
					.on('click.appMissatge', 'button[data-accio="accepta"]', accepta)
					.on('click.appMissatge', 'button[data-accio="cancela"]', cancela);

				if (element.attr("aria-hidden") === "false") {

					espera();
					return;

				}

				pinta();

			},
			espera = function() {

				setTimeout(
					function() {

						pinta();

					}
					,210
				);

			},
			pinta = function() {

				titol_el
					.text( titol_txt );

				text_el
					.text( "" );

				// text HTML

				if (text_txt) {

					text_txt = text_txt.replace("script", "");

					text_el
						.html( text_txt );

				}

				element
					.attr("data-tipus", tipus)
					.attr("data-estat", estat);

				mostra();

			},
			mostra = function() {

				element
					.attr("aria-hidden", "false")
					.appTabulacioNova();

				if (amagaDesdeFons) {

					element
						.on('click.appMissatge', amagaFons);

				}

				setTimeout(
					function() {

						alMostrar();

					}
					,300
				);

			},
			accepta = function(e) {

				alAcceptar();

				amaga();

			},
			cancela = function(e) {

				alCancelar();

				amaga();

			},
			dacord = function(e) {

				alAcceptar();

				amaga();

			},
			amagaFons = function(e) {

				var el_click = $(e.target),
					estaDins = el_click.closest(".imc-mi--con").length;

				if (!estaDins) {
					amaga();
				}

			},
			amaga = function() {

				alTancar();

				element
					.attr("aria-hidden", "true")
					.appTabulacioNova({ accio: "finalitza" });

				// enfoquem al botó llançador

				if (bt) {

					var estaEnPopup = (bt.closest(".imc--popup").length) ? true : false;

					if (estaEnPopup) {

						bt
							.closest(".imc--popup")
								.appTabulacioNova({ enfocaEn: bt });

					} else {

						bt
							.focus();

					}


				}

			};
		
		// inicia
		inicia();
		
	});
	return this;
}