// FORMULARIS

$(document).ready(function(){
	// focus i blur
	$("input, select, textarea").bind("focus", function(e){
		$(this).addClass("on");
		elementActual = $(this).parents("div[class*='element']").get(0);
		$(elementActual).find(".etiqueta").css("font-weight","bold");
	}).bind("blur", function(e){
		$(this).removeClass("on");
		elementActual = $(this).parents("div[class*='element']").get(0);
		$(elementActual).find(".etiqueta").css("font-weight","normal");
	});
});
