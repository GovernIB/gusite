// Documento JavaScript

var disappeardelay = 10;  // el tiempo de desaparicin del menu onMouseout (en milisegundos)
var enableanchorlink = 0; // activar o desactivar el enlace cuando se clica encima (1=activar, 0=desactivar)
var hidemenu_onclick = 1; // esconder el menu cuando el usuario clica sobre l (1=s, 0=no)

var ie = document.all;
var dom = document.getElementById && !document.all;

var pestanyaSeleccionada = '';

function showhide(obj, e, visible, hidden) {
	if (ie||dom) dropmenuobj.style.left = dropmenuobj.style.top=-500 + 'px';
	//
	if (obj.visibility=="visible") obj.visibility="hidden";
	else obj.visibility="visible";
}

function getposOffset(what, offsettype) {
	var totaloffset=(offsettype=="left") ? what.offsetLeft : what.offsetTop;
	var parentEl=what.offsetParent;
	while (parentEl!=null){
		totaloffset=(offsettype=="left") ? totaloffset+parentEl.offsetLeft : totaloffset+parentEl.offsetTop;
		parentEl=parentEl.offsetParent;
	}
	totaloffset;
	return totaloffset;
}

function dropdownmenu(obj, e, dropmenuID) {
	pestanyaSeleccionada = obj;
	pestanyaSeleccionada.className = 'seleccionado';
	//var xx = ie ? document.all[dropmenuID].style.display = 'block' : document.getElementById(dropmenuID).style.display = 'block';
	var xx = document.getElementById(dropmenuID).style.display = 'block';
	if (window.event) event.cancelBubble=true;
	else if (e.stopPropagation) e.stopPropagation();
	if (typeof dropmenuobj!="undefined") dropmenuobj.style.visibility="hidden"; //hide previous menu
	clearhidemenu();
	// creamos el iframe
	iframeMenuDIV = document.getElementById('iframeMenu');
	if(iframeMenuDIV == null) {
		iF = document.createElement('iframe');
		iF.id = 'iframeMenu';
		iF.src = '';
		iF.frameBorder = 0;
		iF.scrolling = 'no';
		iF.className = 'iframemenu';
		document.body.appendChild(iF);
	}
	// mostramos el menu
	if (ie||dom) {
		obj.onmouseout = delayhidemenu;
		dropmenuobj = document.getElementById(dropmenuID);
		dropmenuiframe = document.getElementById('iframeMenu');
		dropmenuiframe.style.visibility='visible';
		dropmenuobj.onmouseover = clearhidemenu;
		dropmenuobj.onmouseout = ie ? function() { dynamichide(event); } : function(event) { dynamichide(event); }
		showhide(dropmenuobj.style, e, "visible", "hidden");
		dropmenuobj.x = getposOffset(obj, "left");
		dropmenuobj.y = getposOffset(obj, "top");
		dropmenuobjA = dropmenuobj.offsetHeight;
		dropmenuobj.style.left = dropmenuobj.x+2+"px";
		dropmenuobj.style.top = dropmenuobj.y+"px";
		dropmenuiframe.style.left = dropmenuobj.x+1+"px";
		dropmenuiframe.style.top = dropmenuobj.y+"px";
		dropmenuiframe.style.height = dropmenuobjA+1+"px";
	}
}

function dynamichide(e) {
	if (typeof dropmenuobj!="undefined") pestanyaSeleccionada.className = '';
	if (ie&&!dropmenuobj.contains(e.toElement)) delayhidemenu();
	else if (dom&&e.currentTarget!= e.relatedTarget&& !contains_dom(e.currentTarget, e.relatedTarget)) delayhidemenu();

}

function delayhidemenu() {
	delayhide=setTimeout("dropmenuobj.style.visibility='hidden';dropmenuiframe.style.visibility='hidden'",disappeardelay);
}

function clearhidemenu() {
	if (typeof delayhide!="undefined") clearTimeout(delayhide);
}
