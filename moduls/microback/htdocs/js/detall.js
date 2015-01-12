// DETALL

$(document).ready(function(){
	// boto tornar
	$("<li>").attr("class","sep").html('<button id="btTornar" type="button" title="Tornar"><img src="imgs/botons/tornar.gif" alt="" /></button>').appendTo("#botonera");
	$("#btTornar").bind("mouseenter",botoOver).bind("mouseleave",botoOut);
	// botonera
	$("#btGuardar").click(function () {
		// formulari
		$("form").submit();
	});
	$("#btTornar").click(function () {
		// tornar
		document.location = pagTornar;
	});
});
