// JavaScript Document

/* CAPA AYUDA */

function ayudaPagina() {
	capaAyuda = document.getElementById('ayudaPagina');
	if(capaAyuda.style.display == 'none') {
		capaAyuda.style.display = 'block';
		nuevoCodigo = '<img src="imgs/iconos/ico_ayuda_cerrar.gif" alt=""> <a href="javascript:void(0); " onclick="ayudaPagina(); ">Cerrar ayuda</a>';
	} else {
		capaAyuda.style.display = 'none';
		nuevoCodigo = '<img src="imgs/iconos/ico_ayuda.gif" alt=""> <a href="javascript:void(0); " onclick="ayudaPagina(); ">Necesito ayuda sobre esta página</a>';
	}
	document.getElementById('ayudaEnlace').innerHTML = nuevoCodigo;
}

/* BÚSQUEDA AVANZADA */

var municipios_1 = new Array(['Jumilla','1'],['Yecla','2']);
var municipios_2 = new Array(['Águilas','3'],['Lorca','4'],['Puerto Lumbreras','5']);
var municipios_3 = new Array(['Aledo','6'],['Alhama de Murcia','7'],['Librilla','8'],['Mazarrón','9'],['Totana','10']);
var municipios_4 = new Array(['Cartagena','11'],['Fuente-Alamo','12'],['La Unión','13']);
var municipios_5 = new Array(['Alcantarilla','14'],['Beniel','15'],['Murcia','16'],['Santomera','17']);
var municipios_6 = new Array(['San Javier','18'],['San Pedro del Pinatar','19'],['Torre Pacheco','20'],['Los Alcázares','21']);
var municipios_7 = new Array(['Bullas','22'],['Calasparras','23'],['Caravaca de la Cruz','24'],['Ceheguin','25'],['Moratalla','26']);
var municipios_8 = new Array(['Abanilla','27'],['Fortuna','28']);
var municipios_9 = new Array(['Albudeite','29'],['Campos del Río','30'],['Mula','31'],['Pliego','32']);
var municipios_10 = new Array(['Archena','33'],['Ricote','34'],['Ulea','35'],['Villanueva del Río Segura','36']);
var municipios_11 = new Array(['Abarán','37'],['Blanca','38'],['Cieza','39']);
var municipios_12 = new Array(['Alguazas','40'],['Ceutí','41'],['Lorqui','42'],['Molina del Segura','43'],['Las Torres de Cotillas','44']);

var numPaso;

function enlacePaso(direccion,id) {
	if(direccion == 'c') {
		num = eval(id.substr(9));
		numPaso = num + 1;
		funcionesPasoC(num);
	} else {
		num = eval(id.substr(11));
		numPaso = num - 1;
		funcionesPasoA(num);
	}
	cPaso = document.getElementById('paso' + numPaso);
	document.getElementById('paso' + num).style.display = 'none';
	cPaso.style.display = 'block';
	if(direccion == 'c') colocarBusqueda(num);
	
}
function funcionesPasoC(valor) {
	if (valor == 1) {
		inputs = document.getElementById('paso' + valor).getElementsByTagName('input');
		for (var i = 0; i < inputs.length; i++) if(inputs[i].checked == true) valorInput = inputs[i].value;
		divs = document.getElementById('paso2').getElementsByTagName('div');
		for (var i = 0; i < divs.length; i++) {
			divs[i].style.display = 'none';
			if(divs[i].id == 'paso2_' + valorInput) divs[i].style.display = 'block';
		}
	} else if (valor == 2) {
		// veamos qué seleccionamos en el paso 1
		inputs = document.getElementById('paso1').getElementsByTagName('input');
		for (var i = 0; i < inputs.length; i++) if(inputs[i].checked == true) valorInputPaso1 = inputs[i].value;
		if(valorInputPaso1 == 2) {
			// cuando hemos elegido empresas saltamos al paso 4
			// veamos qué seleccionamos en el paso 1
			divs = document.getElementById('paso4').getElementsByTagName('div');
			for (var i = 0; i < divs.length; i++) {
				divs[i].style.display = 'none';
				if(divs[i].id == 'paso4_' + valorInputPaso1) divs[i].style.display = 'block';
			}
			return numPaso = 4;
		} else {
			// cuando hemos elegido suelo industrial saltamos al paso 3
			inputs = document.getElementById('paso' + valor + 'Ubicacion').getElementsByTagName('input');
			for (var i = 0; i < inputs.length; i++) if(inputs[i].checked == true) valorInput = inputs[i].value;
			if(valorInput == 1) {
				numPaso = 4;
				// veamos qué seleccionamos en el paso 1
				inputs = document.getElementById('paso1').getElementsByTagName('input');
				for (var i = 0; i < inputs.length; i++) if(inputs[i].checked == true) valorInputPaso1 = inputs[i].value;
				divs = document.getElementById('paso4').getElementsByTagName('div');
				for (var i = 0; i < divs.length; i++) {
					divs[i].style.display = 'none';
					if(divs[i].id == 'paso4_' + valorInputPaso1) divs[i].style.display = 'block';
				}
				return numPaso;
			} else {
				divs = document.getElementById('paso' + eval(valor + 1)).getElementsByTagName('div');
				for (var i = 0; i < divs.length; i++) {
					divs[i].style.display = 'none';
					if(divs[i].id == 'paso3_' + valorInput) divs[i].style.display = 'block';
				}
			}
		}
	} else if (valor == 3) {
		inputs = document.getElementById('paso1').getElementsByTagName('input');
		for (var i = 0; i < inputs.length; i++) if(inputs[i].checked == true) valorInputPaso1 = inputs[i].value;
		divs = document.getElementById('paso4').getElementsByTagName('div');
		for (var i = 0; i < divs.length; i++) {
			divs[i].style.display = 'none';
			if(divs[i].id == 'paso4_' + valorInputPaso1) divs[i].style.display = 'block';
		}
	} else if (valor == 4) {
		// veamos qué seleccionamos en el paso 1
		inputs = document.getElementById('paso1').getElementsByTagName('input');
		for (var i = 0; i < inputs.length; i++) if(inputs[i].checked == true) valorInputPaso1 = inputs[i].value;
		divs = document.getElementById('paso4').getElementsByTagName('div');
		for (var i = 0; i < divs.length; i++) {
			divs[i].style.display = 'none';
			if(divs[i].id == 'paso4_' + valorInputPaso1) divs[i].style.display = 'block';
		}
		return numPaso;
	}
}
function funcionesPasoA(valor) {
	if (valor == 4) {
		// veamos qué seleccionamos en el paso 1
		inputs = document.getElementById('paso1').getElementsByTagName('input');
		for (var i = 0; i < inputs.length; i++) if(inputs[i].checked == true) valorInputPaso1 = inputs[i].value;
		if(valorInputPaso1 == 1) {
			// seleccionamos suelo industrial
			//para saber a qué paso volver
			inputs = document.getElementById('paso2Ubicacion').getElementsByTagName('input');
			for (var i = 0; i < inputs.length; i++) if(inputs[i].checked == true) valorInput = inputs[i].value;
			if(valorInput == 1) numPaso = 2;
			else numPaso = 3;
			return numPaso;
		} else {
			// seleccionamos empresas
			return numPaso = 2;
		}
	}
}
function elegirComarca(obj) {
	// ver si hay algun check marcado
	spanValor = 0;
	var comarca;
	comarca = new Array();
	inputs = obj.parentNode.parentNode.parentNode.getElementsByTagName('input');
	for (i=0; i < inputs.length; i++) {
		if(inputs[i].checked == true) {
			spanValor = 1;
			comarca.push(inputs[i].value);
		}
	}
	if(spanValor == 1) {
		var municipios;
		municipios = new Array();
		for(i=0; i < comarca.length; i++) {
			mMunicipios = eval("municipios_" + comarca[i]);
			municipios = municipios.concat(mMunicipios);
		}
		txtSpan = '';
		for (i=0; i < municipios.length; i++) {
			txtSpan += '<li><label><input name="comarca" type="checkbox" value="' + municipios[i][1] + '" checked class="radio">' + municipios[i][0] + '</label></li>';
		}
		spans = document.getElementById('paso3span').getElementsByTagName('span');
		uls = spans[0].getElementsByTagName('ul');
		uls[0].innerHTML = txtSpan;
		spans[0].style.display = 'block';
	} else {
		spans[0].style.display = 'none';
	}
}
function cambiarInfra(obj) {
	tabla = obj.name + 'Tabla';
	if(obj.checked == true || obj.checked == 'undefined') document.getElementById(tabla).style.display = 'block';
	else document.getElementById(tabla).style.display = 'none';
}
function colocarBusqueda(valor) {
	if(valor == 1) {
		textoBA = '<p>Qué busca.</p><p style="font-weight:bold; ">';
		labels = document.getElementById('paso' + valor).getElementsByTagName('label');
		for (var i = 0; i < labels.length; i++) if(labels[i].childNodes[0].checked == true) textoBA += labels[i].childNodes[1].nodeValue;
		textoBA += '</p>';
		// limpiamos todas las capas
		divs = document.getElementById('seleccionadoCapa').getElementsByTagName('div');
		for (var i = 0; i < divs.length; i++) divs[i].innerHTML = '';
		// rellenamos el primer paso
		document.getElementById('seleccionadoPaso1').innerHTML = textoBA;
	} else if(valor == 2) {
		/* seleccion del paso1 */
		inputs = document.getElementById('paso1').getElementsByTagName('input');
		for (var i = 0; i < inputs.length; i++) if(inputs[i].checked == true) valorInput = inputs[i].value;
		if(valorInput == 2) {  // empresas
			textoBA = '<p>En qué sectores.</p>';
			// agrupamos los labels en un array
			labels = document.getElementById('paso2_2').getElementsByTagName('label');
			var arrayLabels = new Array();
			for (var i = 0; i < labels.length; i++) {
				if(labels[i].childNodes[0].checked == true) {
					arrayLabels.push(labels[i].childNodes[1].nodeValue);
				}
			}
			if(arrayLabels == '') {
				textoBA += '<p style="font-weight:bold; ">Indiferente.</p>';
			} else {
				textoBA += '<p style="font-weight:bold; ">';
				for (var i = 0; i < arrayLabels.length; i++) {
					if(i == arrayLabels.length - 1) textoBA += arrayLabels[i] + '.';
					else textoBA += arrayLabels[i] + ', ';
				}
				textoBA += '</p>';
			}
			// en IE necesita q la capa tenga algo, aunque sea un &nbsp;
			document.getElementById('seleccionadoPaso2').innerHTML = textoBA;
			document.getElementById('seleccionadoPaso4').innerHTML = '<p><strong>Rellene los datos que restan</strong> para completar la búsqueda de empresas y pulse el botón \'BUSCAR\'.</p>';
			document.getElementById('seleccionadoPaso7').innerHTML = '';
		} else {
			textoBA = '<p>Parámetros elegidos.</p>';
			superficieDesde = document.getElementById('BA_superficieDesde').value;
			superficieHasta = document.getElementById('BA_superficieHasta').value;
			precioMaximo = document.getElementById('BA_precioMetroCuadrado').value;
			textoBA += '<p><strong>Superficie</strong>: ';
			if(superficieDesde == '') textoBA += 'indiferente.';
			else textoBA += 'desde '+superficieDesde+' m2, hasta '+superficieHasta+' m2.';
			textoBA += '<br><strong>Precio Máximo</strong>: ';
			if(precioMaximo == '') textoBA += 'indiferente.';
			else textoBA += precioMaximo+' &euro;/m2.';
			textoBA += '</p>';
			labels = document.getElementById('autoviasSiNo').getElementsByTagName('label');
			for (var i = 0; i < labels.length; i++) if(labels[i].childNodes[0].checked == true) valorInputAutovia = labels[i].childNodes[0].value;
			if(valorInputAutovia != 1) {
				textoBA += '<p>Cerca de alguna <strong>Autovía</strong>: NO.</p>';
			} else {
				autoviaDistancia = document.getElementById('BA_autoviaDistancia').value;
				autoviaNombre = document.getElementById('BA_autoviaNombre').options[document.getElementById('BA_autoviaNombre').selectedIndex].childNodes[0].nodeValue;
				textoBA += '<p>Cerca de alguna <strong>Autovía</strong>: SÍ.<br>';
				if(autoviaDistancia == '') textoBA += 'A cualquier distancia de '+autoviaNombre+'.</p>';
				else textoBA += 'A una distancia de '+autoviaDistancia+' km de '+autoviaNombre+'.</p>';
			}
			textoBA += '<p>Dónde busca.</p><p style="font-weight:bold; ">';
			labels = document.getElementById('paso2Ubicacion').getElementsByTagName('label');
			for (var i = 0; i < labels.length; i++) if(labels[i].childNodes[0].checked == true) textoBA += labels[i].childNodes[1].nodeValue;
			/* ubicacion */
			inputs = document.getElementById('paso2').getElementsByTagName('input');
			for (var i = 0; i < inputs.length; i++) if(inputs[i].checked == true) valorInput = inputs[i].value;
			if(valorInput != 1) {
				textoBA += ': <span style="font-style:italic; font-weight:normal; "></span><span style="font-weight:normal; "></span></p>';
			}
			// en IE necesita q la capa tenga algo, aunque sea un &nbsp;
			document.getElementById('seleccionadoPaso2').innerHTML = textoBA;
			document.getElementById('seleccionadoPaso4').innerHTML = '';
			document.getElementById('seleccionadoPaso7').innerHTML = '';
			/* seleccion del paso1 */
			inputs = document.getElementById('paso1').getElementsByTagName('input');
			for (var i = 0; i < inputs.length; i++) if(inputs[i].checked == true) valorInput = inputs[i].value;
			if(valorInput == 2) {  // empresas
				inputs = document.getElementById('paso2').getElementsByTagName('input');
				for (var i = 0; i < inputs.length; i++) if(inputs[i].checked == true) valorInput = inputs[i].value;
				if(valorInput == 1) { // toda la Región
					textoBAfin = '<p><strong>Rellene los datos que restan</strong> para completar la búsqueda de empresas y pulse el botón \'BUSCAR\'.</p>';
				} else {
					textoBAfin = '';
				}
				document.getElementById('seleccionadoPaso4').innerHTML = textoBAfin;
				document.getElementById('seleccionadoPaso7').innerHTML = '';
			}
		}
	} else if(valor == 3) {
		textoBA = document.getElementById('seleccionadoPaso2').innerHTML;
		if(textoBA.indexOf('comarcas') != -1) {
			labels = document.getElementById('paso3_2UL').getElementsByTagName('label');
		} else if(textoBA.indexOf('municipios') != -1) {
			labels = document.getElementById('paso3_3').getElementsByTagName('label');
		} else {
			labels = document.getElementById('paso3_4').getElementsByTagName('label');
		}
		var arrayLabels = new Array();
		for (var i = 0; i < labels.length; i++) {
			if(labels[i].childNodes[0].checked == true) {
				arrayLabels.push(labels[i].childNodes[1].nodeValue);
			}
		}
		textoBAspan = '';
		for (var i = 0; i < arrayLabels.length; i++) {
			if(i == arrayLabels.length - 1) textoBAspan += arrayLabels[i] + '.';
			else textoBAspan += arrayLabels[i] + ', ';
		}
		spans = document.getElementById('seleccionadoPaso2').getElementsByTagName('span');
		if (spans[0] != 'undefined') spans[0].innerHTML = textoBAspan;
		/* si ha eliminado algún municipio */
		if(textoBA.indexOf('comarcas') != -1) {
			labels = document.getElementById('paso3_2UL2').getElementsByTagName('label');
			var arrayLabels = new Array();
			for (var i = 0; i < labels.length; i++) {
				if(labels[i].childNodes[0].checked == false) {
					arrayLabels.push(labels[i].childNodes[1].nodeValue);
				}
			}
			textoMas = '';
			if(arrayLabels != '') {
				textoMas = '<br>Menos en los municipios de <span style="font-style:italic; ">'
				for (var i = 0; i < arrayLabels.length; i++) {
					if(i == arrayLabels.length - 1) textoMas += arrayLabels[i] + '.';
					else textoMas += arrayLabels[i] + ', ';
				}
			}
			textoMas += '</span>'
			spans = document.getElementById('seleccionadoPaso2').getElementsByTagName('span');
			if (spans[1] != 'undefined') spans[1].innerHTML = textoMas;
		}
		/* seleccion del paso1 */
		inputs = document.getElementById('paso1').getElementsByTagName('input');
		for (var i = 0; i < inputs.length; i++) if(inputs[i].checked == true) valorInput = inputs[i].value;
		if(valorInput == 2) {  // empresas
				textoBA = '<p id="BApasoM4"><strong>Rellene los datos que restan</strong> para completar la búsqueda de empresas y pulse el botón \'BUSCAR\'.</p>';
				document.getElementById('seleccionadoPaso4').innerHTML = textoBA;
		}
	} else if(valor == 4) {
		var arraySeleccion = new Array();
		labels = document.getElementById('paso4_1').getElementsByTagName('label');
		for (var i = 0; i < labels.length; i++) if(labels[i].childNodes[0].checked == true) arraySeleccion.push(labels[i].childNodes[0].value);
		if(arraySeleccion != '') {
			labels = document.getElementById('paso4_1').getElementsByTagName('label');
			var arrayLabels = new Array();
			for (var i = 0; i < labels.length; i++) {
				if(labels[i].childNodes[0].checked == true) {
					arrayLabels.push(labels[i].childNodes[1].nodeValue);
				}
			}
			textoBA = '<p><strong>Infraestructuras</strong>: ';
			for (var i = 0; i < arrayLabels.length; i++) {
				if(i == arrayLabels.length - 1) textoBA += arrayLabels[i] + '.';
				else textoBA += arrayLabels[i] + ', ';
			}
			textoBA += '</p>';
			document.getElementById('seleccionadoPaso4').innerHTML = textoBA;
			document.getElementById('seleccionadoPaso7').innerHTML = '<p>Rellene los <strong>datos que restan sobre parcelas</strong> para completar la búsqueda de suelo industrial y pulse el botón \'BUSCAR\'.</p>';
		} else {
			textoBA = '<p><strong>Infraestructuras</strong>: indiferente.';
			document.getElementById('seleccionadoPaso4').innerHTML = textoBA;
			document.getElementById('seleccionadoPaso7').innerHTML = '<p>Rellene los <strong>datos que restan sobre parcelas</strong> para completar la búsqueda de suelo industrial y pulse el botón \'BUSCAR\'.</p>';
		}
	}
}

