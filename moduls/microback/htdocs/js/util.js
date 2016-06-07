// Utilidades JS

//Método para detectar si es ie11 o superior
function getInternetExplorerVersion()
{
  var rv = -1;
  var pintaErrorNav= false;
  
  if (navigator.appName == 'Microsoft Internet Explorer')
  {
    var ua = navigator.userAgent;
    var re  = new RegExp("MSIE ([0-9]{1,}[\.0-9]{0,})");
    if (re.exec(ua) != null)
      rv = parseFloat( RegExp.$1 );
  }else if (navigator.appName == 'Netscape') { //Cuando es ie 11 entra por aqui
	    var ua = navigator.userAgent;
	    var re  = new RegExp("Trident/.*rv:([0-9]{1,}[\.0-9]{0,})");
	    if (re.exec(ua) != null)
	      rv = parseFloat( RegExp.$1 );
  }
  
  if (rv >= 11){
	  pintaErrorNav = true;
	  document.getElementById('errorIE11').style.display='block'; // se pone visible el div con el error
  }
  
  
  return pintaErrorNav;
}