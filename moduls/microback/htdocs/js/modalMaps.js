//valores por defecto
var iniLat="39.5690036";
var iniLng="2.6436571";
var iniZoom=16;
var iniColor = "F00";
var iniColorBorde = "000";
var colorVacio = "FFF";	
var pathIconoSVG='M 0,0 C -2,-20 -10,-22 -10,-30 A 10,10 0 1,1 10,-30 C 10,-22 2,-20 0,0 z M -2,-30 a 2,2 0 1,1 4,0 2,2 0 1,1 -4,0';	 
	
// labels por defecto	 
var lb_coordenadasInvalidas = "Coordenadas Invalidas";
var lb_colorInvalido = "Color Invalido";
var lb_completeDireccion = "Debe rellenar la dirección";
var lb_DireccionNoEncontrada = "Error localizando la dirección";	 	 
 
//////////////////////////

 /*
  * deben existir los siguientes elementos:
  * - input name=latitud
  * - input name=longitud
  * - input name=colorIcono
  * - div id=modalMaps // div modal global
  * - id=close_modal // elemento que cerrara el modal
  * - div id=modal_map //donde se pinta el mapa, dentro del modal
  * 
  * */
 
 //inputs página principal
var inputLatitud=$('#latitud');
var inputLongitud=$('#longitud');
var inputColorIcono=$('#colorIcono');
var inputDireccion =$('#modal_direccion');


// modal
var modal = document.getElementById('modalMaps');
var divModalMapa = document.getElementById('modal_map');
var span = document.getElementsByClassName("close_modal")[0];//boton cierra el modal

//variables de mapa
var map;
var marker;

//abrir modal y ubicar
function ubicarEnMapa(){
    modal.style.display = "block";
	resizeMap();
	
	// recuperamos los valores de los inputs
	var lat = inputLatitud.val();
	var lng = inputLongitud.val();
	var color = getColor(false);//recupera el color, si no es valido usa el de por defecto
	
	//coordenadas invalidas, se usan las de por defecto
	if(!validaCoordenadas(lat,lng)){
		//alert(lb_coordenadasInvalidas);
		lat = iniLat;
		lng = iniLng;
	}
	
	//actualizamos el marker
	var center = new google.maps.LatLng(lat,lng);
	if (marker == null) {                
		creaMarker(center,color);                
	} else {
		marker.setPosition(center);
		marker.setIcon(pinSymbol("#"+color));
		centrarMapa();
	}
}

//botón cerrar modal
span.onclick = function() {
    cerrarModal();
}
//si clicamos fuera del modal lo cerramos
window.onclick = function(event) {
    if (event.target == modal) {
        cerrarModal();
    }
}

//cerrar el modal
function cerrarModal(){
	 modal.style.display = "none";
}



 //inicializar el mapa
function initialize() {
	var center = new google.maps.LatLng(iniLat, iniLng);
	var mapOptions = {zoom: iniZoom, center: center};
	map = new google.maps.Map(divModalMapa, mapOptions);
	creaMarker(center,getColor(false));
}

// crear el marcador y centra el mapa en el
function creaMarker(point,color) {
	marker = new google.maps.Marker({
		position: new google.maps.LatLng(point.lat(), point.lng()),
		map: map,
		draggable: true,
		icon : pinSymbol("#"+color)
	});
	centrarMapa();
}

// crear el simbolo(Icono) con el color seleccionado
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

//recupera el color del imput, si no es valido retorna valor por defecto
function getColor(alertar){
var color = inputColorIcono.val();
	if (!isHexaColor(color)){
		if(alertar){alert(lb_colorInvalido);}
		color=iniColor;
	}
	return color;
}

//redimensiona el mapa, necesario si se redimensiona la pantalla
function resizeMap() {
   if(typeof map =="undefined") return;
   setTimeout( function(){resizingMap();} , 400);
}

function resizingMap() {
   if(typeof map =="undefined") return;
   var center = map.getCenter();
   google.maps.event.trigger(map, "resize");
   map.setCenter(center); 
}


//valida un rango
function inRange(min,number,max){
    if ( number !="" && !isNaN(number) && (number >= min) && (number <= max) ){
        return true;
    } else {
        return false;
    }
}

//valida las coordenadas introducidas
function validaCoordenadas(number_lat,number_lng) {
    if (inRange(-90,number_lat,90) && inRange(-180,number_lng,180)) {
        return true;
    }else {
        return false;
    }
}

//valida Color hexadecimal (sin #)
function isHexaColor(sNum){
  return (typeof sNum === "string") 
		&& (sNum.length === 6 || sNum.length === 3)
        && !isNaN( parseInt(sNum, 16) );
}


////////* Botones modal*/////////
//busca la dirección en el mapa
function buscarDireccion(){
	address = inputDireccion.val();
	if(address==""){
		alert(lb_completeDireccion);
		return;
	}
	var color = inputColorIcono.val();
	geocoder = new google.maps.Geocoder();
	geocoder.geocode({'address': address}, function(results, status) {
		if (status != google.maps.GeocoderStatus.OK) {
    		alert(lb_DireccionNoEncontrada);
		} else {
			var point = results[0].geometry.location;    		
            if (marker == null) { 				
                creaMarker(point,getColor(false));                
            } else {                         
                map.setCenter(point);
                marker.setPosition(point);                
            }			
	    }		
    });
}

//guarda la posición del marker
function guardarPosicion(){
	var point = marker.getPosition();
	inputLatitud.val(point.lat());
	inputLongitud.val(point.lng());
}


function borrarPosicion(){	
	inputLatitud.val("");
	inputLongitud.val("");
	inputColorIcono.val("");
	inputColorIcono.css("background-color", "#"+ colorVacio);
}
//centra el mapa en el marker
function centrarMapa(){
	map.setCenter(marker.getPosition());
} 

//valida todos los campos en función de si son
//obligatorios u optativos
function validaCampos(obligatorio){
	var lt = inputLatitud.val();
	var lg = inputLongitud.val();
	var cl = inputColorIcono.val();
	
	if(obligatorio || lt!="" || lg!=""){
		//obligatorio
		if(!validaCoordenadas(lt,lg)){
			alert(lb_coordenadasInvalidas);
			return false;
		}						
	}
	
	if(obligatorio || cl!="" ){
		if(!isHexaColor(cl)){
			alert(lb_colorInvalido);
			return false;
		}
	}

	return true;
}



