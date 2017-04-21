/*
 * Permite la incorporación de un mapa y sus marcadores
 * 
 */
var divGusiteMaps = document.getElementById('gusiteMaps');
var iniLat="39.5690036";
var iniLng="2.6436571";
var iniColor = "F00";
var iniColorBorde = "000";
var iniZoom=16;
var pathIconoSVG='M 0,0 C -2,-20 -10,-22 -10,-30 A 10,10 0 1,1 10,-30 C 10,-22 2,-20 0,0 z M -2,-30 a 2,2 0 1,1 4,0 2,2 0 1,1 -4,0';	 
var con=new Array();
var map;

//inicializar el mapa
function initialize() {
	var center = new google.maps.LatLng(iniLat, iniLng);
	var mapOptions = {zoom: iniZoom, center: center};
	map = new google.maps.Map(divGusiteMaps, mapOptions);
	creaMarkers();	
}

//crear los marcadores
function creaMarkers() {	
	var infowindow = new google.maps.InfoWindow();
    var bounds = new google.maps.LatLngBounds();
           
    var elementos = $(".gusiteMapsMarker");    
    elementos.each(function(index){
    	var elem = $(this);
    	var lat = elem.find(".gMMLatitud").first().val();
    	var lng = elem.find(".gMMLongitud").first().val();
    	var col = elem.find(".gMMColor").first().val();
    	var tit = elem.find(".gMMTitulo").first().val();
    	con[index] = elem.find(".gMMContenido").first().html();
    	
    	if(!col){
    		col=iniColor;
    	}
    	
		marker = new google.maps.Marker({
			position: new google.maps.LatLng(lat, lng),
			map: map,
			draggable: false,
			icon : pinSymbol("#"+ col),
			title: tit
		}); 
       	 
		bounds.extend(marker.position);

		google.maps.event.addListener(marker, 'click', (function (marker, i) {
			return function () {
				infowindow.setContent(con[i]);
                infowindow.open(map, marker);
            }
		})(marker, index));    	    
    });

    try {
    	if(elementos.length==1){    	
            map.panTo(marker.position);                        
        	infowindow.setContent(con[0]);
            infowindow.open(map, marker);
    	}else{
    		map.fitBounds(bounds);
    	}
    }catch(err) {}
  
}

// Crear el simbolo(Icono) con el color seleccionado
function pinSymbol(color) {
    return {
        path: pathIconoSVG,
        fillColor: color,
        fillOpacity: 1,
        strokeColor: '#'+ iniColorBorde,
        strokeWeight: 2,
        scale: 1,
   };
}