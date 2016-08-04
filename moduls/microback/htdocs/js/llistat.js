// LLISTAT

$(document).ready(function () {
	// TR
	$("#llistat tbody tr").click(function () {
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

	// CERCADOR AVANÇAR
	$("#btCercarAdvance").click(function () {
		// posicio
		var btCercarAdvance = $("#btCercarAdvance").parent();
		bt_T = btCercarAdvance.offset().top;
		bt_L = btCercarAdvance.offset().left;
		bt_W = btCercarAdvance.width();
		bt_H = btCercarAdvance.offset().height;
		// pintar
		var body = $("body");
		body.append('<div id="cercaFORMADV"></div>');
		codi = '<table>';
		codi += '<tr><td><label for="entitat">Entitat: </label></td>';
		codi += '<td><input id="entitatText" name="entitatText" type="text" /></td></tr>';
		codi += '<tr><td><label for="idEntitat">Id. Entitat: </label></td>';
		codi += '<td><input id="idEntitatText" name="idEntitatText" type="text" /></td></tr>';
		codi += '<tr><td><label for="usuari">Usuari: </label></td>';
		codi += '<td><input id="usuariText" name="usuariText" type="text" /></td></tr>';
		codi += '<tr><td><label for="fecha">Data: </label></td>';
		codi += '<td><input id="fechaText" name="fechaText" type="text" size="10" placeholder="dd/mm/yyyy" /></td></tr>';
		codi += '<tr><td><label for="micro">Microsite: </label></td>';
		codi += '<td><input type="hidden" id="idmicro" name="idmicro" />';
		codi += '<input type="text" id="nombremicro" name="nombremicro" style="width: 550px" /></td></tr>';
		codi += '<tr><td><button id="btCercaAdv" name="btCercaAdv" type="button">' + txtCerca + '</button></td></tr>';
		codi += '</table>';
		var cercaFORMADV = $("#cercaFORMADV");
		cercaFORMADV.append(codi);

		$("#nombremicro").autocomplete({
			source: nombresIdsMicrosites,
			minLength: 1,
			select: function(event, ui) {
				$("#idmicro").val(ui.item.value);
				$("#nombremicro").val(ui.item.label);
				event.preventDefault();
			}
		});
		cercaFORMADV.css({top:bt_T + "px", left:bt_L + "px", height:bt_H + "px", paddingLeft:bt_W + "px"});
		cercaFORMADV.fadeIn("slow");
		$("#btCercaAdv").click(function () {
			var fecha = $("#fechaText").val();
			if (!checkDate(fecha)) {
				return false;
			}
			$("#entity").val($("#entitatText").val());
			$("#idEntity").val($("#idEntitatText").val());
			$("#user").val($("#usuariText").val());
			$("#date").val(fecha);
			$("#micro").val($("#idmicro").val());
			$("#formulari").submit();
		});
		// fons
		finestra_W = $(window).width();
		finestra_H = $(window).height();
		body_H = $(document).height();
		if (body_H > finestra_H) finestra_H = body_H;
		// nomes per a IE, amaguem tots els SELECT
		//if ($.browser.msie && $.browser.version < 7) {
		//	$("select").css("display", "none");
		//}
		// creem fons
		body.append('<div id="fons"></div>');
		var fons = $("#fons");
		fons.css({opacity:.3, width:finestra_W + "px", height:finestra_H + "px"});
		fons.click(function () {
			//if ($.browser.msie && $.browser.version < 7) {
			//	$("select").css("display", "inline");
			//}
			$("#fons").remove();
			$("#cercaFORMADV").fadeOut("slow", function () {
				$("#cercaFORMADV").remove();
			});
		});
	});

});

function checkDate(field) {
	var allowBlank = true;
	var minYear = 1900;
	var maxYear = (new Date()).getFullYear();

	var errorMsg = "";

	// regular expression to match required date format
	re = /^(\d{1,2})\/(\d{1,2})\/(\d{4})$/;

	if (field.valueOf() != '') {
		if (regs = field.valueOf().match(re)) {
			if (regs[1] < 1 || regs[1] > 31) {
				errorMsg = "Invalid value for day: " + regs[1];
			} else if (regs[2] < 1 || regs[2] > 12) {
				errorMsg = "Invalid value for month: " + regs[2];
			} else if (regs[3] < minYear || regs[3] > maxYear) {
				errorMsg = "Invalid value for year: " + regs[3] + " - must be between " + minYear + " and " + maxYear;
			}
		} else {
			errorMsg = txtFecha;
		}
	} else if (!allowBlank) {
		errorMsg = "Empty date not allowed!";
	}

	if (errorMsg != "") {
		alert(errorMsg);
		return false;
	}

	return true;
}
