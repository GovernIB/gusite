// Document JavaScript

function ajustarEscritori() {
	dEines = document.getElementById('eines');
	dContinguts = document.getElementById('continguts');
	wY = document.documentElement.clientHeight;
	wX = document.documentElement.clientWidth;
	dEinesX = dEines.offsetWidth;
	// ferramentes
	dEines.style.height = wY + 'px';
	// continguts
	dContinguts.style.height = wY + 'px';
	dContinguts.style.left = dEinesX + 'px';
	dContinguts.style.width = wX - dEinesX + 'px';
}

window.onload = ajustarEscritori;
window.onresize = ajustarEscritori;





