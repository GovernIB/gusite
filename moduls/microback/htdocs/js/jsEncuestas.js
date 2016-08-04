// Document JavaScript

/************ METODOS IMPLEMENTADOS EN LA MAQUETA *************/

	marcado = false;
	function iniciarMenu() {
		// recorrer tbody y tr
		var accFormLista = document.getElementById('llistatcoses');
		tbodys = accFormLista.getElementsByTagName('tbody');
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
				trs[j].ondblclick = function() {
					id = this.getElementsByTagName('input')[0].value;
					document.location = uriEdicion + id;
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
	
	function esborrarTecla(evt) {
		if(evt == null) evt = event;
		var targ = (evt.target) ? evt.target : evt.srcElement;
		var tecla = evt.keyCode || evt.which; 
		
		if ( (tecla == 46) && 
			 (targ.nodeName!= 'INPUT' ) &&
			 (targ.nodeName!= 'TEXTAREA' ))
			  borravarios();

	}

	addEvent(window,'load',iniciarMenu,true);
	addEvent(document,'keydown',esborrarTecla,true);


	/************ METODOS QUE NOOO PERTENECEN A LA MAQUETA *************/

	
	function crear() {
		var accFormLista = document.getElementById('accFormularioLista');
		
	    accFormLista.accion.value="crear";
	    accFormLista.submit();
	}
	
	function editar() {
		// recorrer tbody y tr
		var accFormLista = document.getElementById('llistatcoses');
				
		tbodys = accFormLista.getElementsByTagName('tbody');
		for(i=0;i<tbodys.length;i++) {
			trs = tbodys[i].getElementsByTagName('tr');
			for(j=0;j<trs.length;j++) {
				inputcheck = trs[j].getElementsByTagName('input');
				if (inputcheck[0].checked) {
					//pillamos el primero que aparezca y au
					id = inputcheck[0].value;
					document.location = uriEdicion + id;
					return;
				}
			}
		}
	    alert (alert1);
	}
	
	
	
		