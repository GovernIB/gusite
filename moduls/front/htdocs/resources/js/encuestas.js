function comptaChecks(pregunta){ 
	 var num=0; 
	 if(typeof(document.encuesta[pregunta].length)=="undefined" && document.encuesta[pregunta].checked==true){
		 //solo hay una respuesta y está marcada
		 num++;
	 }else{
		 //hay varias respuestas
		for (i=0; i<document.encuesta[pregunta].length; i++){ 
		 	if (document.encuesta[pregunta][i].checked==true) {
		 		num++;
		 	}	 		 
		} 
	 }
	 
	 
	 return num;  
 } 

 function marcaCheck(idResp, idChk){
	 
	 if (document.encuesta["T" + idResp + '_' + idChk].value.length==0) {
	 	document.getElementById(idResp + '_' + idChk).checked = false;
	 }else{
	 	document.getElementById(idResp + '_' + idChk).checked = true;
   }
}

function getChecked(radioObj) {
	 if(!radioObj) {
		 return false;
	 }
	 var radioLength = radioObj.length;
	 if(radioLength == undefined){
   	if(radioObj.checked){
   		return true;
   	}
		else{
			false; 
   	}	
   	}
	for(var i = 0; i < radioLength; i++) {
		if(radioObj[i].checked) return true;
	 } 
	 return false;
} 

function validaencuesta(){
	var txtError = ""; 
	var chk = {}
	for (var i = 0; i < numPreguntas; i++) {
		chk = checksValidar[i];
		if (chk.multiresp == 'S'){	
			//Multiresposta
			if (chk.min && comptaChecks('C' + chk.id) < chk.min) {
				txtError += '\n' + chk.titulo;
			}
			if (chk.max && comptaChecks('C' + chk.id) > chk.max) {
				txtError += '\n' + chk.titulo;
			}		    						    				
		}else if (chk.min==1){
			// Resposta simple obligatoria
			if (!getChecked(document.encuesta['R' + chk.id])) {
				txtError += '\n' + chk.titulo;
			}		
		}
	}
	
	if (txtError == "") { 
		document.encuesta.submit(); 
	} else { 
		alert(txtCondiciones + txtError)
	} 
}
