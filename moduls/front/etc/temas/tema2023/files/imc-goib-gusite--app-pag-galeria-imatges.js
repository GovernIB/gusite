// JS



// onReady

$(function(){

	imc_body
		.appGaleriaImatges();

});



// appGaleriaImatges

$.fn.appGaleriaImatges = function(options) {

	var settings = $.extend({
			element: ""
		}, options);

	this.each(function(){
		var element = $(this)
			,llistat_ = element.find(".imc--galeria-imatges:first")
			,detall_ = element.find(".imc--galeria-detall:first")
			,img_ = false
			,img_text = false
			,figure_ = llistat_.find("ul figure")
			,figure_num = figure_.length
			,index_ = false
			,activa = function(e) {

				var figure_ = $(this);

				pinta(figure_);

			}
			,pinta = function(figure_) {


				var figure_img = figure_.find("img:first")
					,figure_src = figure_img.attr("src")
					,figure_text = figure_.find("figcaption:first").text()
					,figure_li = figure_.closest("li");

				index_ = llistat_.find("li").index( figure_li );

				var figure_img_W = figure_img.outerWidth()
					,figure_img_H = figure_img.outerHeight()
					,finestra_W = imc_finestra.width()
					,finestra_H = imc_finestra.height();

				var img_amplaria = finestra_W - 60+"px"
					,img_altura = "auto";

				var img_ratio = figure_img_W / figure_img_H
					,finestra_ratio = finestra_W / finestra_H;

				if (img_ratio < finestra_ratio) {

					img_amplaria = "auto";
					img_altura = finestra_H - 60+"px";

				}

				img_ = detall_.find("img:first")
				img_text = detall_.find("figcaption:first");

				img_
					.attr({ "src": figure_src, "alt": "" })
					.css({ width: img_amplaria, height: img_altura });

				img_text
					.text( figure_text );

				figure_
					.addClass("imc--seleccionada");

				detall_
					.find("p:first")
						.text( (index_+1) + "/" + figure_num + ' ' + txtImatge );

				detall_
					.attr("aria-hidden", "false")
					.appTabulacioNova();

			}
			,tanca = function(e) {

				img_
					.attr({ "src": "", "alt": "" });
				
				llistat_
					.find(".imc--seleccionada")
						.removeClass("imc--seleccionada")
						.focus();

				detall_
					.attr("aria-hidden", "true");

			}
			,seguent = function(e) {

				var index_nou = (index_ === figure_num-1) ? 0 : index_ + 1;

				navega(index_nou);


			}
			,anterior = function(e) {

				var index_nou = (index_ === 0) ? figure_num-1 : index_ -1;

				navega(index_nou);

			}
			,navega = function(index_nou) {

				llistat_
					.find(".imc--seleccionada")
						.removeClass("imc--seleccionada");

				var figure_ = llistat_.find("li:eq("+index_nou+") figure:first");

				pinta(figure_);

			}
			,presiona = function(e) {

				var tecla = e.keyCode
					,esShift = !!e.shiftKey;

				var obj = $(e.target);

				if (tecla === 13 && obj.is("figure")) {

					pinta(obj);

				}

			};
		
		// events
		
		element
			.off(".appGaleriaImatges")
			.on("keydown.appGaleriaImatges", presiona)
			.on('click.appGaleriaImatges', ".imc--galeria-imatges li figure", activa)
			.on('click.appGaleriaImatges', "button[data-accio=galeria-tanca]", tanca)
			.on('click.appGaleriaImatges', "button[data-accio=galeria-anterior]", anterior)
			.on('click.appGaleriaImatges', "button[data-accio=galeria-seguent]", seguent);
		
	});

	return this;
}

