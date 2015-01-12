// Document JavaScript

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
				document.location = uriEdicion + id;
			}
		}
	}
	//
	
	
}

window.onload = iniciarMenu;
