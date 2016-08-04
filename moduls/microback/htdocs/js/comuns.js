// COMUNS

$(document).ready(function(){
	$("<div>").attr("id","t").appendTo("body");
	// buttons
	$("button").bind("mouseenter",botoOver).bind("mouseleave",botoOut);
	// botonera scrollTop
	botonera_T = $("#botonera").offset().top;
	$(window).scroll(function () {
		window_ST = $(window).scrollTop();
		if(botonera_T < window_ST) {
			$("#botonera").css("margin-top",(window_ST-botonera_T)+"px");
			if ($(document).find("#cercaFORM").size() != 0) {
				bt_T_nou = $("#btCercar").parent().offset().top;
				$("#cercaFORM").css("top",bt_T_nou+"px");
			}
		} else {
			$("#botonera").css("margin-top",0);
			if ($(document).find("#cercaFORM").size() != 0) {
				bt_T_nou = $("#btCercar").parent().offset().top;
				$("#cercaFORM").css("top",bt_T_nou+"px");
			}
		}
	});
});

function botoOver(e) {
	button_T = $(this).offset().top;
	button_L = $(this).offset().left;
	button_H = $(this).outerHeight();
	button_W = $(this).outerWidth();
	tit = $(this).attr("title");
	$(this).attr("title","");
	$("#t").text(tit);
	$("#t").css({display:"block",top:button_T+"px",left:(button_L+button_W)+"px"});
}

function botoOut(e) {
	$(this).attr("title",tit);
	$("#t").css("display","none");
}