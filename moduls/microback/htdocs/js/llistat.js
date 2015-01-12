// LLISTAT

$(document).ready(function(){
	// TR
	$("#llistat tbody tr").click(function() {
		if ($(this).attr("class") == "seleccionat") {
			$(this).attr("class","over");
		} else {
			$("#llistat tbody tr").attr("class","");
			$(this).toggleClass("seleccionat");
		}
	}).dblclick(function () {
		TD = $(this).find("td").get(0);
		microID = jQuery.trim($(TD).html());
		document.location = pagSeleccionar + microID;
	}).mouseover(function () {
		if ($(this).attr("class") != "seleccionat") $(this).attr("class","over");
	}).mouseout(function () {
		if ($(this).attr("class") != "seleccionat") $(this).attr("class","");
	});
	// botonera
	$("#btCrear").click(function() {
		// crear
		document.location = pagCrear;
	});
	// BOTONERA
	$("#btMicros, #btSeleccionar, #btExportar, #btEliminar").click(function() {
		microID = 0;
		$("#llistat tbody tr").each(function(i){
			if ($(this).attr("class") == "seleccionat") {
				TD = $(this).find("td").get(0);
				microID = jQuery.trim($(TD).html());
			}
		}); 
		if (microID == 0) {
			alert(txtNoSeleccionat);
			return false;
		} else {
			if ($(this).attr("id") == "btSeleccionar") {
				// seleccionar
				document.location = pagSeleccionar + microID;
			}else if ($(this).attr("id") == "btMicros") {
				// seleccionar
				document.location = pagMicros+ microID;  ;
			} else if ($(this).attr("id") == "btExportar") {
				// exportar
				document.location = pagExportar + microID;
			} else if ($(this).attr("id") == "btEliminar") {
				// eliminar
				confirmacio = confirm(txtAlertEliminar);
				if (confirmacio) {
					//alert("Eliminat!");
					document.location = pagEliminar + microID;
				}
			}
		}
	});
	// CERCADOR
	$("#btCercar").click(function() {
		// posicio
		bt_T = $("#btCercar").parent().offset().top;
		bt_L = $("#btCercar").parent().offset().left;
		bt_W = $("#btCercar").parent().width();
		bt_H = $("#btCercar").parent().height();
		// pintar
		$("body").append('<div id="cercaFORM"></div>');
		codi = '<input id="text" name="text" type="text" />';
		codi += '<button id="btCerca" name="btCerca" type="button">' + txtCerca + '</button>';
		$("#cercaFORM").append(codi);
		$("#cercaFORM").css({top:bt_T+"px",left:bt_L+"px",height:bt_H+"px",paddingLeft:bt_W+"px"});
		$("#cercaFORM").fadeIn("slow");
		$("#btCerca").click(function() {
			if ($("#text").val() == "") {
				alert(txtCercador);
				return false;
			}
			$("#filtro").val($("#text").val());
			$("#formulari").submit();
		});
		// fons
		finestra_W = $(window).width();
		finestra_H = $(window).height();
		body_H = $(document).height();
		if (body_H > finestra_H) finestra_H = body_H;
		// nomes per a IE, amaguem tots els SELECT
		if ($.browser.msie && $.browser.version < 7) {
			$("select").css("display","none");
		}
		// creem fons
		$("body").append('<div id="fons"></div>');
		$("#fons").css({opacity:.3,width:finestra_W+"px",height:finestra_H+"px"});
		$("#fons").click(function() {
			if ($.browser.msie && $.browser.version < 7) {
				$("select").css("display","inline");
			}	
			$("#fons").remove();
			$("#cercaFORM").fadeOut("slow",function () {
				$("#cercaFORM").remove();
			});
		});
	});
});
