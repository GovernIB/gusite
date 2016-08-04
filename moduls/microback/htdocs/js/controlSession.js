function addEventControlSession(obj,tipo,fn,modo) {
	if(obj.addEventListener) {
		obj.addEventListener(tipo,fn,modo);
	} else if(obj.attachEvent) {
		obj.attachEvent("on"+tipo,fn);
	}
}

function controlSession(){
	if (mensajeControlSession == false){
		var cab_idMicro = parent.document.getElementById('idMicro_cab');
		var det_idMicro = idMicro_det;	
		if(cab_idMicro!=null & det_idMicro!=null) {	
			if(cab_idMicro.value!=det_idMicro) {
				mensajeControlSession = true;			
				alert("El microsite actiu per a aquesta sessi"+ '\u00f3' + " no coincideix amb aquest que est"+ '\u00e0' + " visualitzant. Treballi "+ '\u00fa' +"nicament amb un microsite alhora. Es va a recarregar el microsite actual en aquesta p"+ '\u00e0' +"gina");
				parent.document.location.href="home.do?idsite="+det_idMicro;
			}
		}
	}
}

addEventControlSession(window,'load',controlSession,true);
var mensajeControlSession = false;