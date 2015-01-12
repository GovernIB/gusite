// Abrir
function abrir(url, name, x, y) {
   nombre = window.open(url, name, 'scrollbars=yes, resizable=yes, width=' + x + ',height=' + y);
   return nombre;
}

// Obrir un pop up per el calendari
function obrirFixa(url) {
   nombre = window.open(url, 'Calendario', 'scrollbars=no, resizable=no, width=235 ,height=175');
   return nombre;
}

function obrirTest(url) {
   nombre = window.open( url, '', 'toolbar, menubar, scrollbars, resizable');
   return nombre;
}

// Auxiliar para formularios
function myIndex(o) {
   var f = o.form;
   
   for (i = 0; i < f.elements.length ; i++) {
      if (f.elements[i] == o) return i;
   }
}

// Confirma una baja mediante un dialogo
function confirmaBaja(texto) {
    var result = confirm(texto)

    if (result == true) {
        return true;
    } else {
        return false;
    }
}

function abrirDoc(id) {
	if (id == null || id =="")
	{
		alert('No es pot previsualitzar el fitxer, primer ha de guardar.');
	}else{
		abrir('archivo.do?id='+id, '', 400, 300);
	}
}

//abrir en una nueva ventana
function abrirWindow(url) {
       nombre = window.open(url, '', '');
   		return nombre;
}