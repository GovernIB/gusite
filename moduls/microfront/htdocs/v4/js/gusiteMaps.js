/* Permite mostrar varios mapas simultaneamente.
 * Cada div mapa debe contener una clase gusiteMaps, y, si hay más de un mapa, un id con el código de componente ej id="GM322"
 * los div que contienen los markers de ese mapa además de la clase gusiteMapsMarker deben
 * tener una clase que sea exactamente igual al id del mapa al que pertenencen class="gusiteMapsMarker GM322"
 */

var divsGusiteMaps = jQuery(".gusiteMaps");
var iniLat="39.5690036";
var iniLng="2.6436571";
var iniColor = "F00";
var iniColorBorde = "000";
var iniZoom=16;
var pathIconoSVG='M 0,0 C -2,-20 -10,-22 -10,-30 A 10,10 0 1,1 10,-30 C 10,-22 2,-20 0,0 z M -2,-30 a 2,2 0 1,1 4,0 2,2 0 1,1 -4,0';	 
var con=new Array();
var map=new Array();
var existemarker = new Array();

//inicializar el mapa
function initialize() {
	var center = new google.maps.LatLng(iniLat, iniLng);
	var mapOptions = {zoom: iniZoom, center: center};
	divsGusiteMaps.each(function(index){				
		map[index] = new google.maps.Map(jQuery(this)[0], mapOptions);
		con[index] = new Array();
		var idCompo = jQuery(this).attr('id');		
		existemarker[idCompo]=new Array();
		creaMarkers(index,idCompo);	
	});	
}

//crear los marcadores
function creaMarkers(indice,idPadre) {	
	var infowindow = new google.maps.InfoWindow();
    var bounds = new google.maps.LatLngBounds();
    var clasePadre = (idPadre==="" || typeof idPadre === "undefined")?"":"."+idPadre;
    //solo recuperamos los elementos del padre
    var elementos = jQuery(".gusiteMapsMarker"+clasePadre);    
    elementos.each(function(index){
    	var elem = jQuery(this);
    	var lat = elem.find(".gMMLatitud").first().val();
    	var lng = elem.find(".gMMLongitud").first().val();
    	if(existemarker[idPadre][""+lat+""+lng]){
    		//si ya existe no lo añadimos, continuamos
    		return true;
    	}
    	var col = elem.find(".gMMColor").first().val();
    	var tit = elem.find(".gMMTitulo").first().val();
    	con[indice][index] = elem.find(".gMMContenido").first().html();
    	
    	if(!col){
    		col=iniColor;
    	}
    	
		marker = new google.maps.Marker({
			position: new google.maps.LatLng(lat, lng),
			map: map[indice],
			draggable: false,
			icon : pinSymbol("#"+ col),
			title: tit
		}); 
       	 
		bounds.extend(marker.position);

		google.maps.event.addListener(marker, 'click', (function (marker, i) {
			return function () {
				infowindow.setContent(con[indice][i]);
                infowindow.open(map[indice], marker);
            }
		})(marker, index));   
		
		existemarker[idPadre][""+lat+""+lng]=true;
    });
	
    try {
    	if(elementos.length==1){    	
            map[indice].panTo(marker.position);                        
        	infowindow.setContent(con[indice][0]);
            infowindow.open(map[indice], marker);
    	}else{
    		map[indice].fitBounds(bounds);
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