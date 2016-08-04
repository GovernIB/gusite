// JavaScript Document

function voreImg(obj,img) {
	capa = obj.parentNode.getElementsByTagName('span');
	if(obj.innerHTML == 'Amagar imatge') {
		capa[0].innerHTML = '';
		capa[0].style.height = '5px';
		obj.innerHTML = 'Mostrar imatge';
	} else {
		capa[0].innerHTML = '<img name="imgEnllasada" src="'+ img +'" alrt="" />';
		capa[0].style.height = '100px';
		capa[0].firstChild.style.height = '100px';
		obj.innerHTML = 'Amagar imatge';
	}
}

function borrarImg(obj,img) {
	imatge = obj.parentNode.getElementsByTagName('strong');
	imatgeNom = imatge[0].innerHTML;
	confirmar = confirm('Dessitja esborrar la imatge: '+ imatgeNom);
}

function mostrarForm(obj) {
	lis = obj.parentNode.parentNode.getElementsByTagName('li');
	for(i=0;i<lis.length;i++) lis[i].className = '';
	obj.parentNode.className = 'selec';
	posicion = 0;
	for(i=0;i<lis.length;i++) if(lis[i].className == 'selec') posicion = i;
	divs = document.getElementById('formulario').getElementsByTagName('div');
	divsIdiomas = new Array();
	for(i=0;i<divs.length;i++) {
		if(divs[i].className == 'capaFormIdioma') divsIdiomas.push(divs[i].id);
	}
	for(i=0;i<divsIdiomas.length;i++) {
		if(i != posicion) {
			document.getElementById(divsIdiomas[i]).style.display = 'none';
		} else {
			document.getElementById(divsIdiomas[i]).style.display = 'block';
		}
	}
}

function mostrarForm2(obj) {
	lis2 = obj.parentNode.parentNode.getElementsByTagName('li');
	for(i=0;i<lis2.length;i++) lis2[i].className = '';
	obj.parentNode.className = 'selec2';
	posicion2 = 0;
	for(i=0;i<lis2.length;i++) if(lis2[i].className == 'selec2') posicion2 = i;
	divs2 = document.getElementById('formulario2').getElementsByTagName('div');
	divsIdiomas2 = new Array();
	for(i=0;i<divs2.length;i++) {
		if(divs2[i].className == 'capaFormIdioma2') divsIdiomas2.push(divs2[i].id);
	}
	for(i=0;i<divsIdiomas2.length;i++) {
		if(i != posicion2) {
			document.getElementById(divsIdiomas2[i]).style.display = 'none';
		} else {
			document.getElementById(divsIdiomas2[i]).style.display = 'block';
		}
	}
}

function marcarCheck(obj) {
	obj.className = 'x';
	inputs = document.getElementById('capa_tablaCA').getElementsByTagName('input');
	posicion = 0;
	for(j=0;j<inputs.length;j++) if(inputs[j].className == 'x') posicion = j;
	obj.className = '';
	divs = document.getElementById('formulario').getElementsByTagName('div');
	for(i=1;i<divs.length;i++) {
		if(divs[i].id.indexOf('capa_tabla') != -1) {
			inputs = divs[i].getElementsByTagName('input');
			for(j=0;j<inputs.length;j++) {
				if(j == posicion) {
					if(obj.checked == true) inputs[j].checked = true;
					else inputs[j].checked = false;
				}
			}
		}
	}
}
function ficarCapsal(obj) {
	seleccionat = document.getElementById(obj).options[document.getElementById(obj).selectedIndex].value;
	selTxt = document.getElementById(obj).options[document.getElementById(obj).selectedIndex].text;
	strongs = document.getElementById('formulario').getElementsByTagName('strong');
	for(i=0;i<strongs.length;i++) {
		if(obj.indexOf('capsalSiNo_') != -1) if(strongs[i].id.indexOf('capsalSiNo_') != -1) strongs[i].innerHTML = selTxt;
		if(obj.indexOf('peuSiNo_') != -1) if(strongs[i].id.indexOf('peuSiNo_') != -1) strongs[i].innerHTML = selTxt;
	}
	divs = document.getElementById('formulario').getElementsByTagName('div');
	for(i=0;i<divs.length;i++) {
		if(divs[i].id.indexOf('capa_tabla') != -1) {
			divs2 = divs[i].getElementsByTagName('div');
			for(j=0;j<divs2.length;j++) {
				if(obj.indexOf('capsalSiNo_') != -1 && divs2[j].id.indexOf('capaCapsal') != -1) {
					divs3 = divs2[j].getElementsByTagName('div');
					for(k=0;k<divs3.length;k++) {
						if(k == seleccionat && divs3[k].id.indexOf('siNo_') != -1) divs3[k].style.display = 'block';
						else divs3[k].style.display = 'none';
					}
				} else if(obj.indexOf('peuSiNo_') != -1 && divs2[j].id.indexOf('capaPeu') != -1) {
					divs3 = divs2[j].getElementsByTagName('div');
					for(k=0;k<divs3.length;k++) {
						if(k == seleccionat && divs3[k].id.indexOf('siNo_') != -1) divs3[k].style.display = 'block';
						else divs3[k].style.display = 'none';
					}
				}
			}
		}
	}
}

function esconderSpans() {
	spans = document.getElementsByTagName('span');
	for(i=0;i<spans.length;i++) if(spans[i].className == 'edicio' || spans[i].className == 'moure') spans[i].style.display = 'none';
}
function menuEditar(obj) {
	//escondemos las capas
	esconderSpans();
	as = document.getElementById('menuArbol').getElementsByTagName('a');
	for(i=0;i<as.length;i++) as[i].style.display = 'inline';
	if(obj.title == 'Editar') {
		obj.style.display = 'none';
		obj.parentNode.style.display = 'none';
		spans = obj.parentNode.parentNode.parentNode.getElementsByTagName('span');
		for(i=0;i<spans.length;i++) {
			if(spans[i].className == 'edicio') {
				spans[i].style.display = 'block';
				if(spans[i].scrollIntoView(false)) spans[i].scrollIntoView(true); // aunque funciona, acaba poniendo el scroll de la p?gina a 0
			}
		}
	} else {
		obj.parentNode.parentNode.style.display = 'none';
		spans = obj.parentNode.parentNode.parentNode.getElementsByTagName('span');
		inputs = obj.parentNode.parentNode.getElementsByTagName('input');
		spans[0].childNodes[0].nodeValue = inputs[0].value;
	}
}

function menuMoure(obj) {
	//escondemos las capas
	esconderSpans();
	as = document.getElementById('menuArbol').getElementsByTagName('a');
	for(i=0;i<as.length;i++) as[i].style.display = 'inline';
	if(obj.title == 'Moure') {
		obj.style.display = 'none';
		obj.parentNode.style.display = 'none';
		spans = obj.parentNode.parentNode.parentNode.getElementsByTagName('span');
		for(i=0;i<spans.length;i++) {
			if(spans[i].className == 'moure') {
				spans[i].style.display = 'block';
				if(spans[i].scrollIntoView(false)) spans[i].scrollIntoView(true); // aunque funciona, acaba poniendo el scroll de la p?gina a 0
			}
		}
	} else {
		inputs = obj.parentNode.parentNode.getElementsByTagName('input');
		for(i=0;i<inputs.length;i++) if(inputs[i].checked == true) menuPadre = i;
		// miramos si el radio ha cambiado
		claseUL = obj.parentNode.parentNode.getElementsByTagName('ul')[0].className;
		if(menuPadre == 'undefined') return false;
		if(claseUL != menuPadre) {
			trs = obj.parentNode.parentNode.parentNode.parentNode.parentNode.getElementsByTagName('tr');
			//cambiamos la clase de UL
			obj.parentNode.parentNode.getElementsByTagName('ul')[0].className = menuPadre;
			nodoClonado = obj.parentNode.parentNode.parentNode.parentNode.cloneNode(true);
			ps = document.getElementById('menuArbol').getElementsByTagName('p');
			if(document.all) claseTabla = ps[menuPadre].nextSibling.className;
			else claseTabla = ps[menuPadre].nextSibling.nextSibling.className;
			if(claseTabla == 'tablaMenu') {
				if(document.all) ps[menuPadre].nextSibling.childNodes[0].appendChild(nodoClonado);
				else ps[menuPadre].nextSibling.nextSibling.appendChild(nodoClonado);
			} else {
				if(document.all) ps[menuPadre].nextSibling.className = 'tablaMenu';
				else ps[menuPadre].nextSibling.nextSibling.className = 'tablaMenu';
				if(document.all) ps[menuPadre].nextSibling.childNodes[0].appendChild(nodoClonado);
				else ps[menuPadre].nextSibling.nextSibling.appendChild(nodoClonado);
			}
			inputs = nodoClonado.getElementsByTagName('input');
			for(i=0;i<inputs.length;i++) if(inputs[i].value == menuPadre && inputs[i].type == 'radio') inputs[i].checked = true; else inputs[i].checked = false;
			// contar els nodes per saber si es l'?ltim
			if(trs.length == 2) {
				if(obj.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.nodeName == 'TABLE') {
					obj.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.className = 'tablaMenuDN';
				} else {
					obj.parentNode.parentNode.parentNode.parentNode.parentNode.className = 'tablaMenuDN';
				}
			}
			// esborrar node original
			nodoABorrarX = obj.parentNode.parentNode.parentNode.parentNode.parentNode;
			nodoABorrar = obj.parentNode.parentNode.parentNode.parentNode;
			nodoABorrarX.removeChild(nodoABorrar);
		}
	}
}

function mostrarCapa(claseEnlace) {
	capaX = document.getElementById('CAPA'+claseEnlace);
	if(capaX.style.display == 'block') capaX.style.display = 'none';
	else capaX.style.display = 'block';
	if(capaX.id == 'CAPAcrearMenu') document.getElementById('CAPAcrearBloc').style.display = 'none';
	else document.getElementById('CAPAcrearMenu').style.display = 'none';
}

function descartarCapa(obj) {
	obj.parentNode.parentNode.parentNode.style.display = 'none';
}

function mostrarIcones(obj,e) {
	// escondemos si hay un icos anterior
	if (typeof icos != "undefined") {
		icos.style.display = 'none';
	}
	spans = obj.parentNode.getElementsByTagName('span');
	for(i=0;i<spans.length;i++) if(spans[i].className == 'icones') icos = spans[i];
	eliminarMostrarIcones();
	// mostramos el nuevo divAyuda
	var spanX = saberPosX(obj);
	var spanY = saberPosY(obj);
	var spanAltura = obj.offsetHeight;
	with(icos) {
		style.top = spanY+spanAltura+"px";
		style.left = spanX+"px";
		style.display = 'block';
	}
	// capturamos los eventos
	if (window.event) event.cancelBubble = true;
	else if (e.stopPropagation) e.stopPropagation();
	obj.onmouseout = esconderMostrarIcones; // si el rat?n se va fuera del objeto (this)
}
function esconderMostrarIcones() {
	esperaEsconderIcos = setTimeout("icos.style.display='none';",200);
}
function eliminarMostrarIcones() {
	if (typeof esperaEsconderIcos != "undefined") clearTimeout(esperaEsconderIcos);
}
function saberPosX(obj) {
	var curleft = 0;
	if (obj.offsetParent) {
		while (obj.offsetParent) {
			curleft += obj.offsetLeft;
			obj = obj.offsetParent;
		}
	} else if (obj.x) {
		curleft += obj.x;
	}
	return curleft;
}

function saberPosY(obj) {
	var curtop = 0;
	if (obj.offsetParent) {
		while (obj.offsetParent) {
			curtop += obj.offsetTop;
			obj = obj.offsetParent;
		}
	} else if (obj.y) {
		curtop += obj.y;
	}
	return curtop;
}

function menuBorrar(obj) {
	texte = obj.parentNode.parentNode.firstChild.nodeValue;
	confirmar = confirm('Dessitja esborrar el ?tem: '+ texte);
}

// capa con iframe para gestionar datos dentro de formularios sin tener que ir a otra p?gina
function editarItem(tipo) {
	var capaI = document.getElementById('capaIframe');
	var capaIF = document.getElementById('capaIframeFondo');
	// tama?os de la ventana y la p?gina
	var ventanaX = document.documentElement.clientWidth;
	var ventanaY = document.documentElement.clientHeight;
	var capaY = document.getElementById('continguts').offsetHeight;
	// la capa de fondo ocupa toda la p?gina
	with (capaIF) {
		if(ventanaY > capaY) style.height = ventanaY + 20 +  'px';
		else style.height = capaY + 20 + 'px';
		if(document.all) style.filter = "alpha(opacity=30)";
		else style.MozOpacity = 0.3;
		if(document.all) style.width = ventanaX + 'px';
		style.display = 'block';
	}
	capaIF.onclick = cerrarInfo;
	// colocamos el contenido adecuado
	contenido = '<p class="tancar"><a href="#" class="tancar" onclick="cerrarInfo()">Tancar finestra</a></p>';
	if(tipo == 'tema') {
		if(document.all) contenido += '<iframe src="08faqs_editar_tema01.html" frameborder="0" style="width:60em; height:28em;"></iframe>';
		else contenido += '<iframe src="08faqs_editar_tema01.html" frameborder="0" style="width:60em; height:27em;"></iframe>';
	} else if(tipo == 'activitat') {
		if(document.all) contenido += '<iframe src="06agenda_editar_act01.html" frameborder="0" style="width:60em; height:28em;"></iframe>';
		else contenido += '<iframe src="06agenda_editar_act01.html" frameborder="0" style="width:60em; height:27em;"></iframe>';
	}
	capaI.innerHTML = contenido;
	// mostramos, miramos su tama?o y centramos la capaInfo con respecto a la ventana
	capaI.style.display = 'block';
	capaInfoX = capaI.offsetWidth;
	capaInfoY = capaI.offsetHeight;
	with (capaI) {
		style.left = (ventanaX-capaInfoX)/2 + 'px';
		style.top = (ventanaY-capaInfoY)/2 + 'px';
	}
}
// esconder las capas info
function cerrarInfo() {
	if(document.getElementById('capaIframe')) document.getElementById('capaIframe').style.display = 'none';
	else document.getElementById('capaCapaSup').style.display = 'none';
	document.getElementById('capaIframeFondo').style.display = 'none';
}

// para crear nuevos ?tems de los forms de contacto
function crearCampForm(){
	var capaI = document.getElementById('capaIframe');
	var capaIF = document.getElementById('capaIframeFondo');
	// tama?os de la ventana y la p?gina
	var ventanaX = document.documentElement.clientWidth;
	var ventanaY = document.documentElement.clientHeight;
	var capaY = document.getElementById('continguts').offsetHeight;
	// la capa de fondo ocupa toda la p?gina
	with (capaIF) {
		if(ventanaY > capaY) style.height = ventanaY + 20 +  'px';
		else style.height = capaY + 20 + 'px';
		if(document.all) style.filter = "alpha(opacity=30)";
		else style.MozOpacity = 0.3;
		if(document.all) style.width = ventanaX + 'px';
		style.display = 'block';
	}
	capaIF.onclick = cerrarInfo;
	// mostramos, miramos su tama?o y centramos la capaInfo con respecto a la ventana
	capaI.style.display = 'block';
	capaInfoX = capaI.offsetWidth;
	capaInfoY = capaI.offsetHeight;
	with (capaI) {
		style.left = (ventanaX-capaInfoX)/2 + 'px';
		style.top = (ventanaY-capaInfoY)/2 + 'px';
	}
}
function pintarCampForm() {
	//capturamos valores
	document.getElementById('formCCvisible1').checked ? visibleV = 'S?' : visibleV = 'No';
	ordreV = document.getElementById('formCCordre').value;
	if(ordreV == '') {
		alert('Ha de ficar un ordre');
		return false;
	}
	document.getElementById('formCCobligatori1').checked ? obligatoriV = 'S?' : obligatoriV = 'No';
	etiquetaV = document.getElementById('formCCetiquetaCA').value;
	if(etiquetaV == '') {
		alert('Ha de ficar un text per al camp');
		return false;
	}
	if(document.getElementById('tipo').options[document.getElementById('tipo').selectedIndex].value == 0) {
		alert('Ha de triar un tipus');
		return false;
	}
	tipusV = document.getElementById('tipo').options[document.getElementById('tipo').selectedIndex].text;
	//montamos el XHTML
	tabla = document.getElementById('tablaFormContacte');
	tablaCode = tabla.innerHTML;
	tablaCode += '				<tr>';
	tablaCode += '					<td><input name="" type="checkbox" value="" /></td>';
	tablaCode += '					<td>'+visibleV+'</td>';
	tablaCode += '					<td>'+ordreV+'</td>';
	tablaCode += '					<td>'+obligatoriV+'</td>';
	tablaCode += '					<td>'+etiquetaV+'</td>';
	tablaCode += '					<td>'+tipusV+'</td>';
	tablaCode += '					<td><a href="" class="editar">Editar</a></td>';
	tablaCode += '				</tr>';
	//alert(tablaCode);
	tabla.innerHTML = tablaCode;
	cerrarInfo();
}
function triarTipusFormContacte() {
	selectorNum = document.getElementById('tipo').options[document.getElementById('tipo').selectedIndex].value;
	spansX = document.getElementById('tipusCampSpan').getElementsByTagName('span');
	for(i=0;i<spansX.length;i++) {
		if(selectorNum == (i+1)) spansX[i].style.display = 'inline';
		else spansX[i].style.display = 'none';
	}
	if(selectorNum == 2) document.getElementById('lineas').value = 0;
	if(selectorNum == 3) document.getElementById('tamano').value = 0;
	if(selectorNum != spansX.length) {
		spansXX = document.getElementById('llistatNumSelectors').getElementsByTagName('span');
		for(i=0;i<spansXX.length;i++) spansXX[i].style.display = 'none';
		//document.getElementById('numSelectors').value = 0;
	}
}
function triaNumSelectors() {
	spans = document.getElementById('llistatNumSelectors').getElementsByTagName('span');
	selectorNum = document.getElementById('numSelectors').options[document.getElementById('numSelectors').selectedIndex].value;
	for(i=0;i<spans.length;i++) if(i < selectorNum) spans[i].style.display = 'block'; else spans[i].style.display = 'none';
}

function triaNumSelectors2() {
	spans = document.getElementById('llistatNumSelectors').getElementsByTagName('span');
	selectorNum = document.getElementById('numSelectors2').options[document.getElementById('numSelectors2').selectedIndex].value;
	for(i=0;i<spans.length;i++) if(i < selectorNum) spans[i].style.display = 'block'; else spans[i].style.display = 'none';
}

// para anyadir documentos a las paginas de contenidos
function insertarDoc(obj){
	enllasPosY = saberPosY(obj);
	enllasPosX = saberPosX(obj);
	var capaI = document.getElementById('capaCapaSup');
	var capaIF = document.getElementById('capaIframeFondo');
	// tama?os de la ventana y la p?gina
	var ventanaX = document.documentElement.clientWidth;
	var ventanaY = document.documentElement.clientHeight;
	var capaY = document.getElementById('continguts').offsetHeight;
	// la capa de fondo ocupa toda la p?gina
	with (capaIF) {
		if(ventanaY > capaY) style.height = ventanaY + 20 +  'px';
		else style.height = capaY + 20 + 'px';
		if(document.all) style.filter = "alpha(opacity=30)";
		else style.MozOpacity = 0.3;
		if(document.all) style.width = ventanaX + 'px';
		style.display = 'block';
	}
	capaIF.onclick = cerrarInfo;
	// mostramos, miramos su tamanyo y centramos la capaInfo con respecto a la ventana
	capaI.style.display = 'block';
	capaInfoX = capaI.offsetWidth;
	capaInfoY = capaI.offsetHeight;
	with (capaI) {
		style.left = enllasPosX + 'px';
		style.top = enllasPosY + 30 + 'px';
	}
}


function editClasificacion_display(idx) {
	objedit = document.getElementById("editClasificacion_" + idx);
	objlistado = document.getElementById("listadoClasificacion_" + idx);
	if (objedit.style.display == "none") {
		objedit.style.display = "block";				        								
		objlistado.style.display = "none";									
	} else {
		objedit.style.display = "none";		
		objlistado.style.display = "block";											        								
	}
}

// Metodo para borrar ficheros en los diferentes mantenimientos. Borra visualmente.
function borraFile(este, namedimg, namedimgid, namedimgbor, namedimgnom)	{
	
	
	var inputs = este.parentNode.getElementsByTagName('input');
	for(i=0;i<inputs.length;i++) {
		var objinput = inputs[i];
		if (objinput.name==namedimgid) objinput.value="";
		if (objinput.name==namedimgbor) objinput.checked=true;
		if (objinput.name==namedimgnom) objinput.value="";
	}
	
	
	var respuestaHTML;
	var divspadre = este.parentNode.parentNode.getElementsByTagName('div');
	for(i=0;i<divspadre.length;i++) {
		var objdiv=divspadre[i];
		if (objdiv.id.indexOf('microManagedFile') != -1) {
			respuestaHTML="<input type='hidden' name='" + namedimgid + "' value=''>";
			respuestaHTML="<input type='hidden' name='" + namedimgbor + "' value='true'>";
			respuestaHTML+="<input type='file' name='" + namedimg + "' size='30' value=''>";
			objdiv.innerHTML=respuestaHTML;
		}
		
	}
	
}



