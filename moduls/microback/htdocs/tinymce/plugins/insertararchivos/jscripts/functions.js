function init() {
	tinyMCEPopup.resizeToInnerSize();
}

function incluirDoc(idsite, id, nom, mime) {
	
	var url = "<img alt=\""+nom+"\" src=\"imgs/archivos_prev/";
		if(mime=="application/msword" || mime=="application/rtf") url+="doc.gif";
		else if(mime=="application/pdf" || mime=="application/x-pdf") url+="pdf.gif";
		else if(mime=="application/vnd.ms-powerpoint" || mime=="application/powerpoint") url+="ppt.gif";
		else if(mime=="application/vnd.ms-excel" || mime=="application/x-msexcel" || mime=="application/ms-excel" || mime=="application/msexcel" || mime=="application/x-excel") url+="xls.gif";
		else if(mime=="application/zip" || mime=="application/x-gtar" || mime=="application/x-tar") url+="zip.gif";
		else if(mime=="text/plain") url+="txt.gif";
		else if(mime=="text/html") url+="htm.gif";
		else if(mime.indexOf("audio")!=-1 || mime.indexOf("video")!=-1) url+="media.gif";
		else if(mime=="application/vnd.sun.xml.writer" || mime=="application/vnd.oasis.opendocument.text" || mime=="application/vnd.stardivision.writer") url+="odt.gif";
		else url+="file.gif";
	url+="\">";

	url+="<a href=\"archivopub.do?ctrl=MCRST"+idsite;
	url+="ZI"+id+"&id="+id+"\">"+nom+"</a>";
	//url+="ZI"+id+"&id="+id+"\">"+nom+"</a>";
		
	tinyMCE.execCommand('mceInsertContent', false, URL);
	tinyMCEPopup.close();
}

function incluirImg(idsite, id, nom) {
	
	var url="";
	
	

		url="<img alt=\""+nom+"\" src=\"archivopub.do?ctrl=MCRST"+idsite;
		url+="ZI"+id+"&id="+id+"\">";

	
	tinyMCE.execCommand('mceInsertContent', false, URL);
	tinyMCEPopup.close();
	
}


function previsualizarDoc(id) {
		abrirDoc(id);
		return;
	}
	
function abrirDoc(id) {
    abrir('archivo.do?id='+id, '', 400, 300);
}

function abrir(url, name, x, y) {
   nombre = window.open(url, name, 'scrollbars=no, resizable=yes, width=' + x + ',height=' + y);
   return nombre;
}