// JS


// constants

var imc_campanya_old;


// onReady

$(function(){

	imc_campanya_old = $("#imc--campanya-old");

	imc_campanya_old
		.appCampanya();

});


// appMissatge

$.fn.appCampanya = function(options) {

	var settings = $.extend({
			element: false
		}, options);

	this.each(function(){

		var element = $(this)
			,destacat_ = element.find("#enllasDest")
			,url_ = element.find("#enllas")
			,titol_ = url_.find(".frase1:first")
			,subtitol_ = url_.find(".frase2:first")
			,inicia = function() {

				var esURL =  element.find("a").length
					,contenidor_html = (esURL) ? "a" : "div";

				var url_href = (esURL) ? `href="${url_href}"` : ""
					,img_fons = destacat_.css("background-image").slice(4, -1).replace(/"/g, "")
					,titol_txt = titol_.text()
					,subtitol_txt = subtitol_.text();

				var campanya = `<${contenidor_html} ${url_href} class="imc--campanya" style="background-image: url(${img_fons})">
									<div>
										<strong>${titol_txt}</strong>
										<span>${subtitol_txt}</span>
									</div>
								</${contenidor_html}>`;

				element
					.replaceWith( campanya );

			};
		
		// inicia

		inicia();
		
	});
	return this;
}

