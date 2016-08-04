// JavaScript Document


    var arespuesta = new Array();
	    
	function fhijo(valor,texto,idpadre){
		this.valor = valor;
		this.texto = texto;
		this.idpadre = idpadre;
	}

	function fpadre(valor,texto){
		this.texto = texto;
		this.valor = valor;
	}
	
	function addarespuesta(valor,texto,idpadre){
		var hijo = new fhijo(valor,texto,idpadre);
	    arespuesta.push(hijo);
	}
	
    function slctdependDefault(padre,hijo,slctdefault){
		    
	    var index = 1;
	    var arraylength = hijo.length;

	    for(m=arraylength;m>1;m--){
		    hijo.remove(m-1);
	    }

       if(padre.selectedIndex != 0){
       		for(m=0;m<arespuesta.length;m++){
       			if(padre.options[padre.selectedIndex].value == arespuesta[m].idpadre){      		  		
       				if(arespuesta[m].valor == slctdefault){
       					hijo.options[index] = new Option(arespuesta[m].texto,arespuesta[m].valor,true,true);
       		  		}else{
       		  			hijo.options[index] = new Option(arespuesta[m].texto);
       		  			hijo.options[index].value = arespuesta[m].valor;
       		  		}
       		  		index = index + 1;
       		  
       		 	}
       		}
       }   
    } 
    
        function alertControl(){
		    var indicepregunta = document.cerca.slctpregunta.selectedIndex;
		    var indicerespuesta = document.cerca.slctresposta.selectedIndex;
		    var valorpregunta = document.cerca.slctpregunta.value;
		    
		    if (indicepregunta == 0){
			      alert("Ha de seleccionar una q"+'\u00FC'+"esti"+'\u00f3'+".");
		    }else{
		    	if (indicerespuesta == 0 && valorpregunta != "-1"){
			      alert("Ha de seleccionar una resposta.");

			    }else{ 
			      document.cerca.submit();

			    }
		    }
		    
		} 