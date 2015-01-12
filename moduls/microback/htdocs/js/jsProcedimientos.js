// Document JavaScript

/************ METODOS IMPLEMENTADOS EN LA MAQUETA *************/

	marcado = false;
	function iniciarMenu() {
		// buttons edicion, borrar, mover
		tbodys = document.getElementsByTagName('tbody');
		for(i=0;i<tbodys.length;i++) {
			trs = tbodys[i].getElementsByTagName('tr');
			for(j=0;j<trs.length;j++) {
				cssTR = trs[j].className;
				trs[j].onmouseover = function() {
					if(this.className != 'marcado') this.className = 'over';
				}
				trs[j].onmouseout = function(){
					if(this.className != 'marcado') this.className = '';
				}
				trs[j].onclick = function() {
					if(this.className != 'marcado') {
						this.className = 'marcado';
						this.getElementsByTagName('input')[0].checked = true;
					} else {
						this.className = '';
						this.getElementsByTagName('input')[0].checked = false;
					}
				}
			}
		}
		
	}


	function addEvent(obj,tipo,fn,modo) {
		if(obj.addEventListener) {
			obj.addEventListener(tipo,fn,modo);
		} else if(obj.attachEvent) {
			obj.attachEvent("on"+tipo,fn);
		}
	}
	
	addEvent(window,'load',iniciarMenu,true);
	addEvent(window,'load',marcarselecccionados,true);

	/************ METODOS QUE NOOO PERTENECEN A LA MAQUETA *************/


	function marcarselecccionados() {
	
		var accFormLista = document.getElementById('accFormularioLista');
		// recorrer tbody y tr
		tbodys = accFormLista.getElementsByTagName('tbody');
		for(i=0;i<tbodys.length;i++) {
			trs = tbodys[i].getElementsByTagName('tr');
			for(j=0;j<trs.length;j++) {
				inputcheck = trs[j].getElementsByTagName('input');
				if (inputcheck[0].checked) {
					trs[j].className = 'marcado';
				} else {
					trs[j].className = '';
				}
			}
		}
	}

	function selec_deselec() {
	    
	    var accFormLista = document.getElementById('accFormularioLista');
		// recorrer tbody y tr
		tbodys = accFormLista.getElementsByTagName('tbody');
		for(i=0;i<tbodys.length;i++) {
			trs = tbodys[i].getElementsByTagName('tr');
			for(j=0;j<trs.length;j++) {
				inputcheck = trs[j].getElementsByTagName('input');
				if (accFormLista.todonada.checked) {
					trs[j].className = 'marcado';
					inputcheck[0].checked = true;
				} else {
					trs[j].className = '';					
					inputcheck[0].checked = false;
				}
			}
		}	    
	     
	} 	




		