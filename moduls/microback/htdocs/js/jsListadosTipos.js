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
				trs[j].ondblclick = function() {
					id = this.getElementsByTagName('input')[0].value;
					clase = this.getElementsByTagName('input')[1].value;
					document.location = uriEdicion + id + uriEdicionClase + clase;
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

	function submitFormBuscar(){
		var accFormSearch = document.getElementById('accFormSearch');
		accFormSearch.submit();
	}

	
	function crear() {
		var accFormLista = document.getElementById('accFormularioLista');
		
	    accFormLista.accion.value="crear";
	    accFormLista.submit();
	}

	function editar() {
		// recorrer tbody y tr
		tbodys = document.getElementsByTagName('tbody');
		for(i=0;i<tbodys.length;i++) {
			alert(tipo);
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
	
	function editarNoticias() {
		// recorrer tbody y tr
		tbodys = document.getElementsByTagName('tbody');
		for(i=0;i<tbodys.length;i++) {
			trs = tbodys[i].getElementsByTagName('tr');
			for(j=0;j<trs.length;j++) {
				inputcheck = trs[j].getElementsByTagName('input');
				if (inputcheck[0].checked) {
					//pillamos el primero que aparezca y au
					id = inputcheck[0].value;
					document.location = uriEdicionNoticias + id;
					return;
				}
			}
		}
	    alert (alert1);
	}
	
	function editarTipo() {
		// recorrer tbody y tr
		tbodys = document.getElementsByTagName('tbody');
		for(i=0;i<tbodys.length;i++) {
			trs = tbodys[i].getElementsByTagName('tr');
			for(j=0;j<trs.length;j++) {
				inputcheck = trs[j].getElementsByTagName('input');
				if (inputcheck[0].checked) {
					//pillamos el primero que aparezca y au
					id = inputcheck[0].value;
					document.location = uriEdicionTipo + id;
					return;
				}
			}
		}
	    alert (alert1);
	}
	
	
	function clonar() {
		
		var accFormLista = document.getElementById('accFormularioLista');
		var nselec=0;
	
	    if (accFormLista.seleccionados.length==undefined) {
	        if (accFormLista.seleccionados.checked) nselec=1;
	    } else {
	        for (var i=0;i<accFormLista.seleccionados.length;i++)
	            if (accFormLista.seleccionados[i].checked) nselec++;
	    }
	    
	    if (nselec==0) {
	            alert (alert1);
	            return;
	    }
	    
		if (nselec>1) {
		    alert (alert3);
		    return;
	    }
		if (nselec==1) {
		    if (!confirm(alert4))
		            return;
		    accFormLista.accion.value='clonar';
		    accFormLista.submit();
	    }	    	    
	}	
	
	function ordenar(campo) {
		var accFormSearch = document.getElementById('accFormSearch');
	    accFormSearch.ordenacion.value=campo;
	    accFormSearch.submit();
	}
	
	function borravarios() {
		var accFormLista = document.getElementById('accFormularioLista');
		var nselec=0;
	
	    if (accFormLista.seleccionados.length==undefined) {
	        if (accFormLista.seleccionados.checked) nselec=1;
	    } else {
	        for (var i=0;i<accFormLista.seleccionados.length;i++)
	            if (accFormLista.seleccionados[i].checked) nselec++;
	    }
	    
	    if (nselec==0) {
	            alert (alert1);
	            return;
	    }
	    
	    if (!confirm(alert2))
	            return;
	
	    accFormLista.accion.value='borrar';
	    accFormLista.submit();
	
	}
	
	function accion (tipo) {
		alert('Hola mundo y su madre');
		var accFormLista = document.getElementById('accFormularioLista');
		var nselec=0;
	
	    if (accFormLista.seleccionados.length==undefined) {
	        if (accFormLista.seleccionados.checked) nselec=1;
	    } else {
	        for (var i=0;i<accFormLista.seleccionados.length;i++)
	            if (accFormLista.seleccionados[i].checked) nselec++;
	    }
	
	    accFormLista.accion.value=tipo;
	    accFormLista.submit();
	
	}
	
		