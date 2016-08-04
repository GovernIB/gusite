// Document JavaScript

function selecURL(obj) {
	tipus = obj.getElementsByTagName('td')[1].innerHTML;
	titol = obj.getElementsByTagName('td')[0].innerHTML;
	url = obj.getElementsByTagName('td')[2].innerHTML;
	codi = titol + ' (<em>' + tipus + '</em>)';
	span = parent.window.document.getElementById('urlNom');
	parent.window.document.getElementById('enlace1').value = url;
	span.innerHTML = codi;
	parent.amagarURLs();
}
