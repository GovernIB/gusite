// Document JavaScript

function iniciarMenu() {
	// tr onmouseover/out
	tbodys = document.getElementById('menuArbol').getElementsByTagName('tbody');
	for(i=0;i<tbodys.length;i++) {
		trs = tbodys[i].getElementsByTagName('tr');
		for(j=0;j<trs.length;j++) {
			trs[j].onmouseover = function (){
				spans = this.getElementsByTagName('span');
				if(spans[0] != null) spans[0].className = 'visible';
				this.style.backgroundColor = '#ffc';
			}
			trs[j].onmouseout = function (){
				spans = this.getElementsByTagName('span');
				if(spans[0] != null) spans[0].className = '';
				this.style.backgroundColor = '#fff';
			}
		}
	}
	// button visible
	buttons = document.getElementsByName('visible');
	for(i=0;i<buttons.length;i++) {
		buttons[i].onclick = function() {
			imgBoto = this.getElementsByTagName('img')[0];
			if(imgBoto.src.indexOf('visibleNo.gif') != -1) {
				imgBoto.src = 'imgs/menu/visible.gif';
				previousSibling(this);
				obj_previousSibling.value = 'S';
			} else {
				imgBoto.src = 'imgs/menu/visibleNo.gif';
				previousSibling(this);
				obj_previousSibling.value = 'N';
			}
		}
	}
	// button abrirCerrar padre
	buttons = document.getElementsByName('padre');
	for(i=0;i<buttons.length;i++) {
		buttons[i].onclick = function() {
			capaMarcada = this.parentNode.parentNode.parentNode.parentNode.parentNode.id;
			capaMarcadaClassName =  this.parentNode.parentNode.parentNode.parentNode.parentNode.className;
			amaguem = false;
			imatge = this.getElementsByTagName('img')[0];
			if(imatge.src.indexOf('padreAbierto') != -1) {
				imatge.src = 'imgs/menu/padreCerrado.gif';
				imatge.alt = 'Obrir carpeta';
				imatge.parentNode.title = 'Obrir carpeta';
			} else {
				imatge.src = 'imgs/menu/padreAbierto.gif';
				imatge.alt = 'Tancar carpeta';
				imatge.parentNode.title = 'Tancar carpeta';
			}
			divs = document.getElementById('menuArbol').getElementsByTagName('div');
			for(j=0;j<divs.length;j++) {
				if(amaguem == true) {
					if(divs[j].className != capaMarcadaClassName) {
						if(capaMarcadaClassName == 'nivel2') {
							if(divs[j].getElementsByTagName('img')[2].src.indexOf('nivel2') == -1) {
								if(divs[j].className.indexOf('pagC') != -1) {
									if(divs[j].className.indexOf('noVisible') != -1)	divs[j].className = 'pagC';
									else divs[j].className = 'pagCnoVisibleNivel2';
								}
							} else {
								amaguem = false;
							}
						}
						if(capaMarcadaClassName == 'nivel1') {
							if(divs[j].className.indexOf('nivel2') != -1) {
								if(divs[j].className.indexOf('noVisible') != -1)	divs[j].className = 'nivel2';
								else divs[j].className = 'nivel2noVisible';
							}
							if(divs[j].className.indexOf('pagC') != -1) {
								if(divs[j].className.indexOf('noVisible') != -1)	 {
									if(divs[j].className.indexOf('Nivel2') == -1) divs[j].className = 'pagC';
								} else {
									divs[j].className = 'pagCnoVisibleNivel1';
								}
							}
						}
					} else {
						amaguem = false;
					}
				}
				if(divs[j].id == capaMarcada) amaguem = true;
			}
		}
	}
	// button editar
	buttons = document.getElementsByName('editar');
	for(i=0;i<buttons.length;i++) {
		if(buttons[i].title.indexOf('gina') == -1) {
			buttons[i].onclick = function() {
				tfoot = this.parentNode.parentNode.parentNode.parentNode.parentNode.getElementsByTagName('tfoot')[0];
				trs_0 = tfoot.getElementsByTagName('tr')[0];
				if(document.all) {
					if(tfoot.style.display == 'block') {
						// esconderlos todos
						tfoots = document.getElementById('menuArbol').getElementsByTagName('tfoot');
						for(i=0;i<tfoots.length;i++) {
							tfoots[i].style.display = 'none';
						}
					} else {
						// esconderlos todos
						tfoots = document.getElementById('menuArbol').getElementsByTagName('tfoot');
						for(i=0;i<tfoots.length;i++) {
							tfoots[i].style.display = 'none';
						}
						tfoot.style.display = 'block';
						trs_0.style.display = 'block';
					}
				} else {
					if(tfoot.style.display == 'table-footer-group') {
						// esconderlos todos
						tfoots = document.getElementById('menuArbol').getElementsByTagName('tfoot');
						for(i=0;i<tfoots.length;i++) {
							tfoots[i].style.display = 'none';
						}
					} else {
						// esconderlos todos
						tfoots = document.getElementById('menuArbol').getElementsByTagName('tfoot');
						for(i=0;i<tfoots.length;i++) {
							tfoots[i].style.display = 'none';
						}
						tfoot.style.display = 'table-footer-group';
						trs_0.style.display = 'table-row';
					}
				}
			}
		}
	}
	// button nou menu
	buttons = document.getElementsByName('nouMenu');
	for(i=0;i<buttons.length;i++) {
		buttons[i].onclick = function() {
			idMenuTotal = this.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.id;
			idMenu = idMenuTotal.substr(1);
			txt = document.getElementById(idMenuTotal).getElementsByTagName('td')[3].innerHTML;
			menuTXT = txt.substr(0,txt.indexOf('<'));
			parrafo = document.getElementById('crearMenu_parrafo');
			parrafo.style.display = 'block';
			parrafo.getElementsByTagName('strong')[0].innerHTML = menuTXT;
			document.getElementById('padreCM').value = document.getElementById('padre'+idMenu).value;
			mostrarFondo();
			fundidoMostrar('fondo');
			menuNuevoMostrar();
		}
	}
	// button esborrar
	buttons = document.getElementsByName('esborrar');
	for(i=0;i<buttons.length;i++) {
		buttons[i].onclick = function() {
			idMenuTotal = this.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.id;
			idMenu = idMenuTotal.substr(1);
			idMenuClassName = document.getElementById(idMenuTotal).className;
			idMenuImgNivel = document.getElementById(idMenuTotal).getElementsByTagName('img')[2].name;
			// buscar hijos
			hijosNum = 0;
			empezamos = false;
			divsSON = document.getElementById('menuArbol').getElementsByTagName('div');
			for(n=0;n<divsSON.length;n++) {
				if(divsSON[n].id != 'nuevo') {
					if(empezamos == true) {
						if(idMenuImgNivel == 'nivel2') {
							if(divsSON[n].getElementsByTagName('img')[2].name.indexOf(idMenuImgNivel) != -1 || divsSON[n].className == 'nivel1') {
								empezamos = false;
							}
						} else if(idMenuImgNivel == 'nivel3') {
							if(divsSON[n].getElementsByTagName('img')[2].name.indexOf(idMenuImgNivel) != -1 || divsSON[n].getElementsByTagName('img')[2].name == 'nivel2' || divsSON[n].className == 'nivel1') {
								empezamos = false;
							}
						} else {
							if(divsSON[n].className.indexOf(idMenuClassName) != -1) {
								empezamos = false;
							}
						}
					}
					if(empezamos == true) ++hijosNum;
					if(divsSON[n].id == idMenuTotal) empezamos = true;
				}
			}
			if(hijosNum != 0) {
				alert(alert3);
				return false;
			}

			if (this.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.className!="pagC")
				if(confirm(alert1)) {
				
					document.forms[0].accion.value="borrar"+document.getElementById('padre'+idMenu).value;
					document.forms[0].submit();

				}
				else 				return false;
			else 
				if(confirm(alert2)) document.location.href="contenidosAcc.do?op=borrar&id="+document.getElementById('padre'+idMenu).value;
				else 				return false;
		
		}
	}
	// button moure
	buttons = document.getElementsByName('moure');
	for(i=0;i<buttons.length;i++) {
		buttons[i].className = 'moure';
		buttons[i].onmousedown = function() {
			cambiarPosicion(this);
		}
	}
	// button tancar
	buttons = document.getElementsByName('tancar');
	for(i=0;i<buttons.length;i++) {
		buttons[i].onclick = function() {
			tfoot = this.parentNode.parentNode.parentNode.parentNode;
			tfoot.style.display = 'none';
		}
	}
	//
	
}

addEvent(window,'load',iniciarMenu,true);

function cambiarPosicion(obj) {
	// cerramos todos los tr de input
	tfoots = document.getElementById('menuArbol').getElementsByTagName('tfoot');
	for(i=0;i<tfoots.length;i++) {
		trs = tfoots[i].getElementsByTagName('tr');
		for(j=0;j<trs.length;j++) {
			trs[j].style.display = 'none';
		}
	}
	// borramos el nodo
	if(document.getElementById('nuevo') != null) document.getElementById('menuArbol').removeChild(document.getElementById('nuevo'));
	// calcular posiciones capas
	divs = document.getElementById('menuArbol').getElementsByTagName('div');
	var posicionsStr = '{ "capa": [ ';
	for(i=0;i<divs.length;i++) {
		if(divs[i].id != 'nuevo' && divs[i].className.indexOf('noVisible') == -1) {
			posicionsStr += '{ ';
			// id
			posicionsStr += '"id": "' + divs[i].id +'", ';
			// posicion
			objY = saberPosY(document.getElementById(divs[i].id));
			posicionsStr += '"posicion": "' + objY +'"';
			// cerramos
			if(i == divs.length-1) posicionsStr += ' }';
			else posicionsStr += ' },';
		}
	}
	posicionsStr += ' ] } ';
	posicions = eval('(' + posicionsStr + ')');
	// al tema
	wX = document.documentElement.clientWidth;
	div = obj.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode;
	divID = div.id;
	divClone = div.cloneNode(true);
	divClone.id = 'nuevo';
	divClone.getElementsByTagName('tr')[0].style.backgroundColor = '#fff';
	divClone.style.width = wX - 20 + 'px';
	document.getElementById('menuArbol').appendChild(divClone);
	document.getElementById('nuevo').style.display = 'block';
	if(document.all) document.getElementById('nuevo').style.filter = 'alpha(opacity=50)';
	else document.getElementById('nuevo').style.opacity = 0.5;
	div.style.visibility = 'hidden';
	objY = saberPosY(obj);
	divClone.style.top = objY + 'px';
	// raton inicial
	ratonYinicial = ratonY;
	// altura ventana usuario
	usuariY = document.documentElement.clientHeight;
	// y a?adimos eventos
	addEvent(document,'mousemove',cambiarPosicionGo,true);
	addEvent(document,'mouseup',cambiarPosicionStop,true);
	// y retornamos valores
	return objY, ratonYinicial, posicions, divID, usuariY;
}

var idCapaOver = '';

function cambiarPosicionGo(event) {
	capaClone = document.getElementById('nuevo');
	capaClone.style.top = objY - (ratonYinicial - ratonY) + 'px';
	for(i=0;i<posicions.capa.length;i++) {
		if(ratonY > posicions.capa[i].posicion) idCapaOver = posicions.capa[i].id;
	}
	if(ratonY < posicions.capa[0].posicion) idCapaOver = '';
	
	usuariActualY = usuariY + document.documentElement.scrollTop;
	
	if(ratonY >= usuariActualY - 40) document.documentElement.scrollTop = document.documentElement.scrollTop + 5;
	if(ratonY >= usuariActualY - 20) document.documentElement.scrollTop = document.documentElement.scrollTop + 10;
	if(ratonY <= document.documentElement.scrollTop + 40) document.documentElement.scrollTop = document.documentElement.scrollTop - 5;
	if(ratonY <= document.documentElement.scrollTop + 20) document.documentElement.scrollTop = document.documentElement.scrollTop - 10;
	
	//document.getElementById('CapaXX').value = ratonY; // solo PRUEBAS!!
	//document.getElementById('ventanaUser').value = usuariActualY; // solo PRUEBAS!!
	
	return idCapaOver;
	if(document.all) {
		window.event.cancelBubble = true;
		window.event.returnValue = false;
  } else event.preventDefault();
}

function cambiarPosicionStop(event) {
	document.getElementById('nuevo').style.display = 'none';
	//
	cloneClassName = document.getElementById(divID).className;
	if(cloneClassName != 'nivel1' && idCapaOver == '') {
		document.getElementById(divID).style.visibility = 'visible';
	} else if(cloneClassName == 'nivel1' && idCapaOver == '') {
		capaOverClassName = '';
		capaOverID = '';
	} else {
		capaOverClassName = document.getElementById(idCapaOver).className;
		capaOverID = document.getElementById(idCapaOver).id;
	}
		
	// capas a clonar
	capesPerClonar = new Array();
	clonar = false;
	divsDIV = document.getElementById('menuArbol').getElementsByTagName('div');
	for(k=0;k<divsDIV.length;k++) {
		if(divsDIV[k].id != 'nuevo') {
			if(cloneClassName == 'pagC') clonar = false;
			if(cloneClassName == 'nivel2') {
				if(divsDIV[k].className.indexOf(cloneClassName) != -1 || divsDIV[k].className.indexOf('nivel1') != -1) clonar = false;
				if(document.getElementById(divsDIV[k].id).getElementsByTagName('img')[2].name == 'nivel2') clonar = false;
			}
			if(cloneClassName == 'nivel1') {
				if(divsDIV[k].className.indexOf(cloneClassName) != -1) clonar = false;
			}
			if(divsDIV[k].id == divID) clonar = true;
			if(clonar == true) capesPerClonar.push(divsDIV[k].id);
		}
	}
	
	if(cloneClassName.indexOf('nivel') != -1 && capaOverClassName != '') {
		buscarUltimoHijo(capaOverID,divID);
		capaOverID = nuevoID;
	}
	
	//clonando capas
	for(m=0;m<capesPerClonar.length;m++) {
		divClone = document.getElementById(capesPerClonar[m]).cloneNode(true);
		divClone.id = 'X';
		divClone.style.visibility = 'visible';
		
		//alert(capaOverID + ' - ' + m);
		if(document.getElementById(capaOverID) != null) {
			carpetaModeImg = document.getElementById(capaOverID).getElementsByTagName('img')[1].src;
			carpetaMode = (carpetaModeImg.indexOf('padreAbierto') != -1) ? 'obert' : 'tancat';
			
			//alert(cloneClassName);
			
			if(carpetaMode == 'tancat' && cloneClassName.indexOf('pag') != -1) {
				nuevoID = buscarUltimoHijo(capaOverID,divID);
				//alert(nuevoID);
				capaOverID = nuevoID;
			}
		}
		
		// pegamos capa clonada
		if(m == 0) {
			if(capaOverClassName == '' && cloneClassName == 'nivel1') document.getElementById('menuArbol').insertBefore(divClone,document.getElementById('menuArbol').getElementsByTagName('div')[0]);
			else document.getElementById('menuArbol').insertBefore(divClone, document.getElementById(capaOverID).nextSibling);
		} else {
			document.getElementById('menuArbol').insertBefore(divClone, document.getElementById(capesPerClonar[m-1]).nextSibling);
		}
		
		// borramos antigua
		document.getElementById('menuArbol').removeChild(document.getElementById(capesPerClonar[m]));
		
		if(document.getElementById(capaOverID) != null) {
			imgALT = document.getElementById(capaOverID).getElementsByTagName('img')[1].alt;
			if(imgALT.indexOf('Obrir') != -1) {
				clase = document.getElementById('X').className;
				if(clase == 'nivel2') {
					document.getElementById('X').className = 'nivel2noVisible';
					imatge = document.getElementById('X').getElementsByTagName('img')[1];
					imatge.src = 'imgs/menu/padreCerrado.gif';
					imatge.alt = 'Obrir carpeta';
					imatge.parentNode.title = 'Obrir carpeta';
				} else {
					if(capaOverClassName == 'nivel1') document.getElementById('X').className = 'pagCnoVisibleNivel1';
					else 
					document.getElementById('X').className = (carpetaMode == 'tancat' && cloneClassName.indexOf('pag') != -1) ? 'pagC' : 'pagCnoVisibleNivel2';
				}
			}
		}
		document.getElementById('X').id = capesPerClonar[m];
		
		if(cloneClassName.indexOf('nivel') == -1) {
			if(capaOverClassName == 'nivel1') {
				imgNivel = document.getElementById(capesPerClonar[m]).getElementsByTagName('img')[2];
				imgNivel.src = 'imgs/menu/nivel2.gif';
				imgNivel.name = 'nivel2';
			} else if(capaOverClassName == 'nivel2') {
				imgNivel = document.getElementById(capesPerClonar[m]).getElementsByTagName('img')[2];
				
				if(carpetaMode == 'tancat' && cloneClassName.indexOf('pag') != -1) {
					imgNivel.src = 'imgs/menu/nivel2.gif';
					imgNivel.name = 'nivel2';
				} else {
					imgNivel.src = 'imgs/menu/nivel3.gif';
					imgNivel.name = 'nivel3';
				}
				
			} else if(capaOverClassName.indexOf('pag') != -1) {
				capaOverImgNivel = document.getElementById(capaOverID).getElementsByTagName('img')[2].name;
				if(capaOverImgNivel == 'nivel2') {
					imgNivel = document.getElementById(capesPerClonar[m]).getElementsByTagName('img')[2];
					imgNivel.src = 'imgs/menu/nivel2.gif';
					imgNivel.name = 'nivel2';
				} else {
					imgNivel = document.getElementById(capesPerClonar[m]).getElementsByTagName('img')[2];
					imgNivel.src = 'imgs/menu/nivel3.gif';
					imgNivel.name = 'nivel3';
				}
			}
		}
		
		// cambiamos el idPadre
		if(m == 0) {
			capaActual = document.getElementById(capesPerClonar[m]);
			imgNivel_Name = document.getElementById(capesPerClonar[m]).getElementsByTagName('img')[2].name;
			encontrado = false;
			while(capaActual.previousSibling) {
				if(capaActual.previousSibling.id != null && capaActual.previousSibling.nodeName == 'DIV' && encontrado == false) {
					if(imgNivel_Name == 'nivel2') {
						if(capaActual.previousSibling.getElementsByTagName('img')[2].name == 'nivel1') {
							capaPadre = capaActual.previousSibling.id;
							document.getElementById(capesPerClonar[m]).getElementsByTagName('input')[3].value = document.getElementById(capaPadre).getElementsByTagName("input")[0].value;
							encontrado = true;
						}
					}
					if(imgNivel_Name == 'nivel3') {
						if(capaActual.previousSibling.getElementsByTagName('img')[2].name == 'nivel2') {
							capaPadre = capaActual.previousSibling.id;
							document.getElementById(capesPerClonar[m]).getElementsByTagName('input')[3].value = document.getElementById(capaPadre).getElementsByTagName("input")[0].value;
							encontrado = true;
						}
					}			
				}
				capaActual = capaActual.previousSibling;
			}
		}
	}

	removeEvent(document,'mousemove',cambiarPosicionGo,true);
	removeEvent(document,'mouseup',cambiarPosicionStop,true);
	if(document.all) {
		window.event.cancelBubble = true;
		window.event.returnValue = false;
  } else event.preventDefault();
	// reordenamos
	ordenarNodes();
	// y reiniciamos buttons
	iniciarMenu();
}

// buscar capa padre al clonar
function buscarUltimoHijo(overID, cloneID) {
	overIDClassName = document.getElementById(overID).className;
	cloneIDClassName = document.getElementById(cloneID).className;
	nuevoID = '';
	
	//Caso carpeta principal
	var carpetaPrincipal= false;
	if(document.getElementById(overID).getElementsByTagName('input')[2].value == "m" 
		&& document.getElementById(cloneID).getElementsByTagName('input')[2].value == "m"
	    && overIDClassName == 'nivel1' && cloneIDClassName == 'nivel1'){
		carpetaPrincipal= true;
	}
	if(overIDClassName == 'nivel1' && cloneIDClassName == 'nivel2') {
		nuevoID = overID;
	} else {

		divsSON = document.getElementById('menuArbol').getElementsByTagName('div');
		for(n=0;n<divsSON.length;n++) {
			clonar = false;
			if(divsSON[n].id != 'nuevo') {
				if(cloneIDClassName == 'nivel1') {
					if(divsSON[n].className.indexOf('nivel1') != -1) clonar = false;
				} else {
					if(divsSON[n].className.indexOf('nivel') != -1) clonar = false;
				}
				
				if(carpetaPrincipal){
					//Cuando son dos carpetas principales se clonara la carpeta y sus hijos(los que tienen mismo padre input 3)
					if(divsSON[n].id == overID || 
						document.getElementById(divsSON[n].id).getElementsByTagName('input')[3].value == document.getElementById(overID).getElementsByTagName('input')[0].value){
						clonar = true;		
					}
				}else{
					
					if(document.getElementById(overID).getElementsByTagName('input')[2].value == "m" 
						|| document.getElementById(cloneID).getElementsByTagName('input')[2].value == "m"){
						nuevoID = cloneID;
						break;// acción no permitida
					}
					
					if(divsSON[n].id == overID && 
							(document.getElementById(divsSON[n].id).getElementsByTagName('input')[2].value == "c1")
								|| (document.getElementById(divsSON[n].id).getElementsByTagName('input')[2].value == "c2" && 
										document.getElementById(divsSON[n].id).getElementsByTagName('input')[3].value == document.getElementById(overID).getElementsByTagName('input')[0].value)   ) 
					{
						clonar = true;
					}
					
				}
				
				
				if(clonar == true) nuevoID = divsSON[n].id;
			}
		}
	}
	if(nuevoID == ""){
		nuevoID = cloneID; // se queda como estaba, acción no permitida
	}
	return nuevoID;
}

// ordenar todos los nodos
function ordenarNodes() {
	divs = document.getElementById('menuArbol').getElementsByTagName('div');
	for(i=0;i<divs.length;i++) {
		if(divs[i].id != 'nuevo') divs[i].getElementsByTagName('input')[1].value = i; // ordenes
	}
}

// saber posicion raton
var ie = document.all ? true : false;
document.onmousemove = posicRatonXY;
var ratonY = 0
function posicRatonXY(e) {
	if(ie) ratonY = event.clientY + document.documentElement.scrollTop;
	else ratonY = e.pageY;
	if(ratonY < 0) ratonY = 0;
	//document.getElementById('Raton').value = ratonY; // solo PRUEBAS!!
	return true;
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

function addEvent(obj,tipo,fn,modo) {
	if(obj.addEventListener) {
		obj.addEventListener(tipo,fn,modo);
	} else if(obj.attachEvent) {
		obj.attachEvent("on"+tipo,fn);
	}
}

function removeEvent(obj,tipo,fn,modo) {
	if(obj.addEventListener) {
		obj.removeEventListener(tipo,fn,modo);
	} else if(obj.attachEvent) {
		obj.detachEvent("on"+tipo,fn);
	}
}

opacidad = 0;
function mostrarObjeto() {
	obj = boto;
	if(opacidad < 100) {
		if(document.all) obj.filters.alpha.opacity = opacidad;
		else obj.style.opacity = opacidad/100;
		opacidad += 10;
		tiempo = setTimeout('mostrarObjeto()', 100);
	} else {
		clearTimeout(tiempo);
		opacidad = 0;
	}
}

/* nuevo nodo */

function menuNuevo() {
	mostrarFondo();
	fundidoMostrar('fondo');
	parrafo = document.getElementById('crearMenu_parrafo');
	parrafo.style.display = 'none';
	document.getElementById('padreCM').value = '0';
	menuNuevoMostrar();
}

function menuNuevoMostrar() {
	cCM = document.getElementById('CAPAcrearMenu');
	cCM.style.display = 'block';
	cCM_w = cCM.offsetWidth;
	cCM_h = cCM.offsetHeight;
	cCM.style.left = (ventanaX - cCM_w)/2 + 'px';
	usuariY = document.documentElement.clientHeight;
	if(window.XMLHttpRequest) {
		cCM.style.position = 'fixed';
		cCM.style.top = (usuariY - cCM_h)/2 + 'px';
	} else {
		ventanaScrollY = document.documentElement.scrollTop;
		cCM.style.top = (usuariY - cCM_h)/2 + ventanaScrollY + 'px';
	}
}

function menuNuevoEsconder() {
	document.getElementById('CAPAcrearMenu').style.display = 'none';
	esconderFondo();
}

//mostrar fons
function mostrarFondo() {
	if(document.getElementById('fondo') == null) {
		// creamos capa
		fondoC = document.createElement('div');
		fondoC.setAttribute('id','fondo');
		document.body.appendChild(fondoC);
	}
	// capturamos tamanyos
	ventanaX = document.documentElement.clientWidth;
	ventanaY = document.body.offsetHeight;
	usuariY = document.documentElement.clientHeight;
	if(usuariY > ventanaY) ventanaY = usuariY;
	// solo para IE, escondemos todos los SELECT
	if(document.all) {
		selects = document.getElementsByTagName('select');
		for(i=0;i<selects.length;i++) selects[i].style.display = 'none';
	}
	// mostramos el fondo
	fondoDiv = document.getElementById('fondo');
	fondoDiv.style.display = 'block';
	if(document.all) fondoDiv.style.filter = "alpha(opacity=0)";
	else fondoDiv.style.opacity = 0;
	fondoDiv.style.width = ventanaX + 'px';
	fondoDiv.style.height = ventanaY + 'px';
	addEvent(fondoDiv,'click',menuNuevoEsconder,true);
	return ventanaX;
}

function esconderFondo() {
	document.getElementById('fondo').style.display = 'none';
	// solo para IE, mostramos todos los SELECT
	if(document.all) {
		selects = document.getElementsByTagName('select');
		for(i=0;i<selects.length;i++) selects[i].style.display = 'inline';
	}
}

// efecte fundit
opacidad = 0;
function fundidoMostrar(id) {
	obj = document.getElementById(id);
	if(opacidad < 40) {
		opacidad += 5;
		if(document.all) obj.filters.alpha.opacity = opacidad;
		else obj.style.opacity = opacidad/100;
		tiempo = setTimeout('fundidoMostrar("'+id+'")', 50);
	} else {
		clearTimeout(tiempo);
		opacidad = 0;
	}
}

// nextSibling && previousSibling
function nextSibling(obj) {
	encontrado = false;
	while(obj.nextSibling) {
		if(obj.nextSibling.nodeType == 1 && encontrado == false) {
			obj_nextSibling = obj.nextSibling;
			encontrado = true;
			return obj_nextSibling;
		}
		obj = obj.nextSibling;
	}
}

function previousSibling(obj) {
	encontrado = false;
	while(obj.previousSibling) {
		if(obj.previousSibling.nodeType == 1 && encontrado == false) {
			obj_previousSibling = obj.previousSibling;
			encontrado = true;
			return obj_previousSibling;
		}
		obj = obj.previousSibling;
	}
}
