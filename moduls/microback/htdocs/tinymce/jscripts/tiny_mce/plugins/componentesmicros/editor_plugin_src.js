/**
 * $Fecha: 30/03/2007 $
 *
 * $Vicente Roca Sevilla $
 */

/* Import plugin specific language pack */
//tinyMCE.importPluginLanguagePack('componentesmicros', 'en,tr,sv,zh_cn,cs,fa,fr_ca,fr,de,pl,pt_br,nl,da,he,nb,hu,ru,ru_KOI8-R,ru_UTF-8,nn,es,cy,is,zh_tw,zh_tw_utf8,sk,ca');
tinyMCE.importPluginLanguagePack('componentesmicros');
// Plucin static class
var TinyMCE_ComponentesmicrosPlugin = {
	getInfo : function() {
		return {
			longname : 'Componentes de Microsite',
			author : 'Vicente Roca Sevilla',
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
			case "componentesmicros":
				return tinyMCE.getButtonHTML(cn, 'lang_componentesmicros_desc', '{$pluginurl}/images/componentes.gif', 'mceComponentemicro');
		}

		return "";
	},

	/**
	 * Executes the mceTipoarchivo command.
	 */
	execCommand : function(editor_id, element, command, user_interface, value) {
		// Handle commands
		switch (command) {
			case "mceComponentemicro":
				var template = new Array();

				template['file'] = '/sacmicroback/listartinycomponentes.do'; // Relative to theme
				template['width'] = 500;
				template['height'] = 300;

				// Language specific width and height addons
				template['width'] += tinyMCE.getLang('lang_componentesmicros_delta_width', 0);
				template['height'] += tinyMCE.getLang('lang_componentesmicros_delta_height', 0);

				tinyMCE.openWindow(template, {editor_id : editor_id, inline : "yes", scrollbars : "yes"});

				return true;
		}

		// Pass to next handler in chain
		return false;
	}
};

// Register plugin
tinyMCE.addPlugin('componentesmicros', TinyMCE_ComponentesmicrosPlugin);
