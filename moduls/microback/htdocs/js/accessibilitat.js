// Document JavaScript

marcado = false;
function iniciarMenu() {
	// buttons edicion, borrar, mover
	SPANs = document.getElementsByTagName('span');
	for(i=0;i<SPANs.length;i++) {
		if (SPANs[i].className == "text") {
			if (SPANs[i].getElementsByTagName('a')[0] != null) SPANs[i].getElementsByTagName('a')[0].onclick = Detalle;
		}
	}
}

function Detalle(e) {
	if(!e) var e = window.event;
	obj = e.target || e.srcElement;
	LI = obj.parentNode.parentNode.parentNode;
	Ps = LI.getElementsByTagName('p');
	for(i=0;i<Ps.length;i++) {
		if (Ps[i].className.indexOf("detall") != -1) {
			Ps[i].style.display = (Ps[i].style.display != "block") ? "block" : "none";
		}
	}
}

window.onload = iniciarMenu;
