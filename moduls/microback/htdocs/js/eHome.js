// Document JavaScript

function mostarURLs() {
	mostrarFons();
	if(document.getElementById('capaIframe') == null) {
		// creamos capa
		iframeC = document.createElement('iframe');
		iframeC.setAttribute('id','capaIframe');
		iframeC.frameBorder = 0;
		iframeC.scrolling = 'auto';
		iframeC.src = 'listado_urls.html';
		document.body.appendChild(iframeC);
	}
	// mostramos iframe
	capaIframeDiv = document.getElementById('capaIframe');
	capaIframeDiv.style.display = 'block';
	// capturamos tamanyos
	var ventanaX = document.documentElement.clientWidth;
	var ventanaY = document.documentElement.offsetHeight;
	var ventanaYvisible = document.documentElement.clientHeight;
	capaIframeDiv.w = capaIframeDiv.offsetWidth;
	capaIframeDiv.h = capaIframeDiv.offsetHeight;
	capaIframeDiv.style.left = (ventanaX-capaIframeDiv.w)/2 + 'px';
	capaIframeDiv.style.top = document.documentElement.scrollTop + (ventanaYvisible-capaIframeDiv.h)/2 + 'px';
	//capaIframeDiv.style.position = 'fixed';
}

function amagarURLs() {
	document.getElementById('fons').style.display = 'none';
	document.getElementById('capaIframe').style.display = 'none';
	// solo para IE, mostramos todos los SELECT
	if(document.all) {
		selects = document.getElementsByTagName('select');
		for(i=0;i<selects.length;i++) selects[i].style.display = 'block';
	}
}

function mostrarFons() {
	if(document.getElementById('fons') == null) {
		// creamos capa
		fonsC = document.createElement('div');
		fonsC.setAttribute('id','fons');
		document.body.appendChild(fonsC);
	}
	// capturamos tamanyos
	var ventanaX = document.documentElement.clientWidth;
	var ventanaY = document.documentElement.offsetHeight;
	// solo para IE, escondemos todos los SELECT
	if(document.all) {
		selects = document.getElementsByTagName('select');
		for(i=0;i<selects.length;i++) selects[i].style.display = 'none';
	}
	// mostramos el fons
	fonsDiv = document.getElementById('fons');
	fonsDiv.style.display = 'block';
	if(document.all) fonsDiv.style.filter = "alpha(opacity=30)";
	else fonsDiv.style.opacity = 0.3;
	if(document.all) fonsDiv.style.width = ventanaX - 20 + 'px';
	else fonsDiv.style.width = ventanaX + 'px';
	fonsDiv.style.height = ventanaY + 20 + 'px';
}

function amagarFons() {
	document.getElementById('fons').style.display = 'none';
	// solo para IE, mostramos todos los SELECT
	if(document.all) {
		selects = document.getElementsByTagName('select');
		for(i=0;i<selects.length;i++) selects[i].style.display = 'block';
	}
}


