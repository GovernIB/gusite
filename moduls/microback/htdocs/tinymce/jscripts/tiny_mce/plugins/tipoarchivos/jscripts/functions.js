function init() {
	tinyMCEPopup.resizeToInnerSize();
}

function insertTipoarchivo(file_name, title) {

	title = tinyMCEPopup.editor.getLang(title);
	URL = 'tinymce/jscripts/' + tinyMCEPopup.editor.baseURI.toRelative('plugins/');

	if (title == null) title = "";

	// XML encode
	/*title = title.replace(/&/g, '&amp;');
	title = title.replace(/\"/g, '&quot;');
	title = title.replace(/</g, '&lt;');
	title = title.replace(/>/g, '&gt;');*/

	var html = '<img src="' + URL  + "/tipoarchivos/images/" + file_name + '" mce_src="' + URL + "/tipoarchivos/images/" + file_name + '" border="0" alt="' + title + '" title="' + title + '" />';

	tinyMCE.execCommand('mceInsertContent', false, html);
	tinyMCEPopup.close();
}

