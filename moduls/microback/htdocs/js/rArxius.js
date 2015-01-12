// Document JavaScript

marcado = false;
function iniciarMenu() {
	// funcions per als TR
	trs = document.getElementById('listadoArchivos').getElementsByTagName('tr');
	for(j=0;j<trs.length;j++) {
		cssTR = trs[j].className;
		trs[j].onmouseover = function() {
			if(this.className != 'marcado') this.className = 'over';
		}
		trs[j].onmouseout = function(){
			if(this.className != 'marcado') this.className = '';
		}
		trs[j].onclick = function() {
			if(this.className != 'marcado') {
				this.className = 'marcado';
				this.getElementsByTagName('input')[0].checked = true;
			} else {
				this.className = '';
				this.getElementsByTagName('input')[0].checked = false;
			}
		}
		trs[j].ondblclick = function() {
			this.className = 'marcado';
			this.getElementsByTagName('input')[0].checked = true;
			mostarGuardarArxiu('divGuardarArxiu','modificar');
		}
	}
	
}


function esborrarArxiuTecla(evt) {
	if(evt == null) evt = event;
	var tecla = evt.keyCode || evt.which; 
	if(tecla == 46) esborrarArxiu();
}

function mostarGuardarArxiu(capa,tipo) {
	cCM = document.getElementById(capa);
	cCM_input = cCM.getElementsByTagName('input')[0];
	cCM_h2 = cCM.getElementsByTagName('h2')[0];
	cCM_p = cCM.getElementsByTagName('p')[0];
	if(tipo == 'modificar') {
		comprobarSeleccionado();
		if(inputsSel.length > 1) {
			alert(alert1);
			return false;
		} else if(inputsSel.length == 0) {
			alert(alert2);
			return false;
		}
		inputID = inputsSel[0].id;
		cCM_input.value = document.getElementById(inputID).value; // el ID a modificar
		cCM_h2.innerHTML = mensa1;
		nomArxiu = document.getElementById(inputID).parentNode.parentNode.getElementsByTagName('td')[2].innerHTML;
		cCM_p.innerHTML = mensa2+': <strong>' + nomArxiu + '</strong>.';
	} else {
		cCM_input.value = '0'; // ningun ID a modificar
		cCM_h2.innerHTML = alert3;
		cCM_p.innerHTML = mensa3;
	}
	mostrarFondo();
	fundidoMostrar('fondo');
	formMostrar(capa);
}

function esborrarArxiu() {
	comprobarSeleccionado();

	if(inputsSel.length == 0) {
		alert(alert2);
		return false;
	} else {
		if(inputsSel.length >= 1) 
			if(confirm(alert4)) return true;
			else return false;
	}

}

function verArchivo() {

	comprobarSeleccionado();
	if(inputsSel.length > 1) {
		alert(alert1);
		return false;
	} else if(inputsSel.length == 0) {
		alert(alert2);
		return false;
	}
	abrirDoc(inputsSel[0].value);
}

function comprobarSeleccionado() {
	incorrecte = false;
	inputsSel = new Array();
	inputs = document.getElementById('listadoArchivos').getElementsByTagName('input');
	for(j=0;j<inputs.length;j++) {
		if(inputs[j].checked == true) inputsSel.push(inputs[j]);
	}
	return inputsSel;
}

function formMostrar(capa) {
	cCM = document.getElementById(capa);
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
	return cCM;
}

function formEsconder() {
	cCM.style.display = 'none';
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
	addEvent(fondoDiv,'click',formEsconder,true);
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
