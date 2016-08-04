/**
 * $Fecha: 14/12/2006 $
 *
 * $Aitor Tomas Pin $
 */

/* Import plugin specific language pack */
tinyMCE.importPluginLanguagePack('tipoarchivos', 'en,tr,sv,zh_cn,cs,fa,fr_ca,fr,de,pl,pt_br,nl,da,he,nb,hu,ru,ru_KOI8-R,ru_UTF-8,nn,es,cy,is,zh_tw,zh_tw_utf8,sk');

// Plucin static class
var TinyMCE_TipoarchivosPlugin = {
	getInfo : function() {
		return {
			longname : 'Tipos de Archivos',
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
			case "tipoarchivos":
				return tinyMCE.getButtonHTML(cn, 'lang_tipoarchivos_desc', '{$pluginurl}/images/tipoarchivos.gif', 'mceTipoarchivo');
		}

		return "";
	},

	/**
	 * Executes the mceTipoarchivo command.
	 */
	execCommand : function(editor_id, element, command, user_interface, value) {
		// Handle commands
		switch (command) {
			case "mceTipoarchivo":
				var template = new Array();

				template['file'] = '../../plugins/tipoarchivos/tipoarchivos.htm'; // Relative to theme
				template['width'] = 160;
				template['height'] = 160;

				// Language specific width and height addons
				template['width'] += tinyMCE.getLang('lang_tipoarchivos_delta_width', 0);
				template['height'] += tinyMCE.getLang('lang_tipoarchivos_delta_height', 0);

				tinyMCE.openWindow(template, {editor_id : editor_id, inline : "yes"});

				return true;
		}

		// Pass to next handler in chain
		return false;
	}
};

// Register plugin
tinyMCE.addPlugin('tipoarchivos', TinyMCE_TipoarchivosPlugin);
