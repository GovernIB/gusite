// Document JavaScript

function idiomaJS() {
	as = document.getElementById('capsalIdioma').getElementsByTagName('a');
	for(i=0;i<as.length;i++) {
		idi = as[i].lang;
		as[i].href = "javascript:idioma('"+idi+"');";
	}
}

// canvis d'idioma
function idioma(codiIdioma) {
	direccio = document.location.toString();
	if(codiIdioma == 'ca' && direccio.indexOf('index_') != -1) {
		document.location = 'index.html';
		return false;
	}
	if(direccio.indexOf('index_') != -1) {
		document.location = 'index_'+codiIdioma+'.html';
		return false;
	}
	idiomas = new Array('ca_','es_','en_','de_');
	for(i=0;i<idiomas.length;i++) {
		if(direccio.indexOf(idiomas[i]) != -1) {
			direccioNova = direccio.replace(idiomas[i], codiIdioma+"_");
			document.location = direccioNova;
			return false;
		}
	}
	document.location = 'index_'+codiIdioma+'.html';
	return false;
}


// Aplicacions departamentals - llistat en forma d'arbre
function obrirArbre(obj){
	if(obj.className == 'pareAD') obj.className = 'pareADon';
	else obj.className = 'pareAD';
	nextList = obj.parentNode.getElementsByTagName('ul')[0];
	if(nextList.style.display == 'none') nextList.style.display = 'block';
	else nextList.style.display = 'none';	
}

// mostrar calendari de la agenda
function mostrarCalendari() {
	num = document.getElementById('agendaSelect').options[document.getElementById('agendaSelect').selectedIndex].value;
	divs = document.getElementById('agendaCalendaris').getElementsByTagName('div');
	for(i=0;i<divs.length;i++) {
		if(i==num) divs[i].style.display = 'block'; else divs[i].style.display = 'none';
	}
}

// men? amb JS
/*
function menuJS(estado) {
	as = document.getElementById('marcLateral').getElementsByTagName('a');
	for(i=0;i<as.length;i++) {
		if(as[i].className == 'pareADon') {
			as[i].href = 'javascript:void(0);';
			as[i].onclick = function() { obrirArbre(this); }; // per IE
			if(estado == 'n') {
				as[i].className = 'pareAD';
				as[i].nextSibling.nextSibling.style.display = 'none';
			} else {
				if(estado != i) {
					as[i].className = 'pareAD';
					as[i].nextSibling.nextSibling.style.display = 'none';
				}
			}
		}
	}
}
*/
/*
function menuJS(nodo) {
	as = document.getElementById('marcLateral').getElementsByTagName('a');
	var nodoX;
	for(i=0;i<as.length;i++) {
		if(as[i].className == 'pareADon') {
			as[i].href = 'javascript:void(0);';
			as[i].onclick = function() { obrirArbre(this); };
			as[i].className = 'pareAD';
			as[i].nextSibling.nextSibling.style.display = 'none';
		}
		if(as[i].parentNode.id != false) nodoX = as[i].parentNode.id;
	}
	if(nodo != 0 && nodo == nodoX) {
		nodePare = document.getElementById(nodo).parentNode;
		nodePare.style.display = 'block';
		nodePareA = nodePare.parentNode.firstChild;
		if(nodePare.parentNode.nodeName != 'DIV') nodePareA.className = 'pareADon';
	}
}
*/
function menuJS(nodo) {
	as = document.getElementById('marcLateral').getElementsByTagName('a');
	var nodoX;
	for(i=0;i<as.length;i++) {		
		if(as[i].className == 'pareADon') {
			as[i].href = 'javascript:void(0);';
			as[i].onclick = function() { obrirArbre(this); };
			as[i].className = 'pareAD';
			obrirArbre(as[i]);
			//if (document.all) as[i].nextSibling.style.display = 'none';
			//else as[i].nextSibling.nextSibling.style.display = 'none';
			
		}
		if(as[i].parentNode.id != false) nodoX = as[i].parentNode.id;
	}
	if(nodo != 0 && nodo == nodoX) {
		//Abre carpeta de segundo nivel
		nodeUL = document.getElementById(nodo).parentNode;
		nodeUL.style.display = 'block';
		nodeA = nodeUL.parentNode.firstChild;
		if(nodeUL.parentNode.nodeName != 'DIV') nodeA.className = 'pareADon';
		//Abre carpeta de primer nivel
		nodeUL = nodeA.parentNode.parentNode;
		nodeUL.style.display = 'block';
		nodeA = nodeUL.parentNode.firstChild;
		if(nodeUL.parentNode.nodeName != 'DIV') nodeA.className = 'pareADon';
		
	}
}
