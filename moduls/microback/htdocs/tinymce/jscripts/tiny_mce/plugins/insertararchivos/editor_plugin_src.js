/**
 * $Fecha: 14/12/2006 $
 *
 * $Aitor Tomas Pin $
 */

/* Import plugin specific language pack */
tinyMCE.importPluginLanguagePack('insertararchivos', 'en,tr,sv,zh_cn,cs,fa,fr_ca,fr,de,pl,pt_br,nl,da,he,nb,hu,ru,ru_KOI8-R,ru_UTF-8,nn,es,cy,is,zh_tw,zh_tw_utf8,sk');

// Plucin static class
var TinyMCE_InsertaArchivosPlugin = {
	getInfo : function() {
		return {
			longname : 'Inserta Archivos Subidos',
			author : 'Aitor Tomas Pin',
			authorurl : 'http://www.indra.es',
			infourl : '',
			version : tinyMCE.majorVersion + "." + tinyMCE.minorVersion
		};
	},

	/**
	 * Returns the HTML contents of the tipoarchivos control.
	 */
	getControlHTML : function(cn) {
		switch (cn) {
			case "insertararchivos":
				return tinyMCE.getButtonHTML(cn, 'lang_insertararchivos_desc', '{$pluginurl}/images/archivo.gif', 'mceInsertaArchivo');
		}

		return "";
	},

	/**
	 * Executes the mceTipoarchivo command.
	 */
	execCommand : function(editor_id, element, command, user_interface, value) {
		// Handle commands
		switch (command) {
			case "mceInsertaArchivo":
				var template = new Array();

				var idform = tinyMCE.getParam("idform");

				template['file'] = '/sacmicroback/recursos.do?onlydocs=yes&page='+idform; // Relative to theme
				template['width'] = 600;
				template['height'] = 300;

				// Language specific width and height addons
				template['width'] += tinyMCE.getLang('lang_tipoarchivos_delta_width', 0);
				template['height'] += tinyMCE.getLang('lang_tipoarchivos_delta_height', 0);

				tinyMCE.openWindow(template, {editor_id : editor_id, inline : "yes", scrollbars : "yes"});

				return true;
		}

		// Pass to next handler in chain
		return false;
	}
};

// Register plugin
tinyMCE.addPlugin('insertararchivos', TinyMCE_InsertaArchivosPlugin);