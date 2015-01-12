/*
 * Version 1.00 (26/04/2011)
 * @requires jQuery v1.2.3 or later
 *
 * Salvador Antich
 * Thanks to Pep LLuis Yuste for some excellent contributions!
 */

	function fillOption(urlRequest, datos, idFiltre, select, selected){			
		$(select).children().remove();
		var param = "";
		
		var option = document.createElement("option");		
        option.value = "";
        option.text = " ";        
        $(select).append(option);
        
        if(idFiltre != null) param = "&id=" + idFiltre;
        
	    $.ajax({ 
		    url: "" + urlRequest,
		    type: "GET",
		    cache: true,
		    data: "accion=" + datos + param,
		    dataType:"json",
		    success: function(j){
		    	try{
			        $(j).each(function (row){		        
				        var option = document.createElement("option");
				        option.value = $(this).attr("codi");
				        option.text = $(this).attr("texte");
				        if (option.value == selected) $(option).attr("selected", "selected");
				        $(select).append(option);
			        })
		    	}catch(err){console.log(err);}
			},
			complete: function() {					
				$(select).change();						
			}			
		});    
	}
	// Opció per IE
	function fillOptionIE(urlRequest, datos, idFiltre, select, selected){
		var htmlSelect = "<option value=''> - </option>";
		$(select).children().remove();		
		var param = "";
        
        if(idFiltre != null) param = "&id=" + idFiltre;
        
	    $.ajax({ 
		    url: "" + urlRequest,
		    type: "GET",
		    cache: true,
		    data: "accion=" + datos + param,
		    dataType:"json",
		    success: function(j){
		    	try{
			        $(j).each(function (row){
			        	htmlSelect = htmlSelect + "<option value='" + $(this).attr("codi") + "'";
				        if ($(this).attr("texte") == selected) {
				        	htmlSelect = htmlSelect + " selected='selected'";
				        }
				        thmlSelect = htmlSelect + ">" + $(this).attr("texte") + "</option>";
			        })
		    	}catch(err){console.log(err);}
			},
			complete: function() {
				$(select).html = htmlSelect;
				$(select).change();
			}			
		});    
	}
	
	function lista(urlRequest, datos, idFiltre, llistat){		
		var param = "";
		//$(lista).children().remove();
		$(llistat).val("");
		if(idFiltre != null) param = "&id=" + idFiltre;
        
	    $.ajax({ 
		    url: "" + urlRequest,
		    type: "GET",
		    cache: false,
		    data: "accion=" + datos + param,
		    dataType:"json",
		    success: function(j){
		    	try{
			        $(j).each(function (row){		        
			        	$(llistat).val($(llistat).val() + "<" + $(this).attr("texte") + "> ");				        
			        })
		    	}catch(err){console.log(err);}
			}
		});	    	    
	}