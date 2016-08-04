function init() {
	tinyMCEPopup.resizeToInnerSize();
}

function insertComponente(type_componente, id_componente, title) {

	//var html = '<div property="MCRST_DUMMY_' + type_componente + '" propertyID="' + id_componente + '" style="background: #fff; width: 120px; height: 120px; border: #494949 5px solid" contenteditable="false" class="mceNonEditable"><b>' + title + '</b><br />Componente de microsite (no tocar) </div>';
	var html = '<div versionMCRST="1.4" property="MCRST_DUMMY_' + type_componente + '" componenteID="' + id_componente + '" style="background: #fff; width: 120px; height: 120px; border: #494949 5px solid" contenteditable="false" class="mceNonEditable"><b>' + title + '</b><br />Componente de microsite (no tocar) </div>';


	tinyMCE.execCommand('mceInsertContent', false, html);
	tinyMCEPopup.close();
}
